package com.thomax.shadow.client.network.nio;

import com.thomax.shadow.client.config.Constant;
import com.thomax.shadow.client.misc.Config;
import com.thomax.shadow.client.misc.Util;
import com.thomax.shadow.client.network.IServer;
import com.thomax.shadow.client.ss.CryptFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedSelectorException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.security.InvalidAlgorithmParameterException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Logger;

/**
 * Base class of socket handler for processing all IO event for sockets
 */
public abstract class SocketHandlerBase implements IServer, ISocketHandler {
    private Logger logger = Logger.getLogger(SocketHandlerBase.class.getName());
    protected Selector _selector;
    protected Config _config;
    protected final List _pendingRequest = new LinkedList();
    protected final ConcurrentHashMap _pendingData = new ConcurrentHashMap();
    protected ConcurrentMap<SocketChannel, PipeWorker> _pipes = new ConcurrentHashMap<>();
    protected ByteBuffer _readBuffer = ByteBuffer.allocate(Constant.BUFFER_SIZE);

    protected abstract Selector initSelector() throws IOException;
    protected abstract boolean processPendingRequest(ChangeRequest request);
    protected abstract void processSelect(SelectionKey key);


    public SocketHandlerBase(Config config) throws IOException, InvalidAlgorithmParameterException {
        if (!CryptFactory.isCipherExisted(config.getMethod())) {
            throw new InvalidAlgorithmParameterException(config.getMethod());
        }
        _config = config;
        _selector = initSelector();
    }

    @Override
    public void run() {
        while (true) {
            try {
                synchronized (_pendingRequest) {
                    Iterator changes = _pendingRequest.iterator();
                    while (changes.hasNext()) {
                        ChangeRequest change = (ChangeRequest) changes.next();
                        if (!processPendingRequest(change))
                            break;
                        changes.remove();
                    }
                }

                // wait events from selected channels
                _selector.select();

                Iterator selectedKeys = _selector.selectedKeys().iterator();
                while (selectedKeys.hasNext()) {
                    SelectionKey key = (SelectionKey) selectedKeys.next();
                    selectedKeys.remove();

                    if (!key.isValid()) {
                        continue;
                    }

                    processSelect(key);
                }
            }
            catch (ClosedSelectorException e) {
                break;
            }
            catch (Exception e) {
                logger.warning(Util.getErrorMessage(e));
            }
        }
        logger.fine(this.getClass().getName() + " Closed.");
    }

    protected void createWriteBuffer(SocketChannel socketChannel) {
        List queue = new ArrayList();
        Object put;
        put = _pendingData.putIfAbsent(socketChannel, queue);
        if (put != null) {
            logger.severe("Dup write buffer creation: " + socketChannel);
        }
    }

    protected void cleanUp(SocketChannel socketChannel) {
        try {
            socketChannel.close();
        } catch (IOException e) {
            logger.info(Util.getErrorMessage(e));
        }
        SelectionKey key = socketChannel.keyFor(_selector);
        if (key != null) {
            key.cancel();
        }

        if (_pendingData.containsKey(socketChannel)) {
            _pendingData.remove(socketChannel);
        }
    }

    @Override
    public void send(ChangeRequest request, byte[] data) {
        switch (request.type) {
            case ChangeRequest.CHANGE_SOCKET_OP:
                List queue = (List) _pendingData.get(request.socket);
                if (queue != null) {
                    synchronized (queue) {
                        // in general case, the write queue is always existed, unless, the socket has been shutdown
                        queue.add(ByteBuffer.wrap(data));
                    }
                }
                else {
                    logger.warning(Util.getErrorMessage(new Throwable("Socket is closed! dropping this request")));
                }
                break;
        }

        synchronized (_pendingRequest) {
            _pendingRequest.add(request);
        }

        _selector.wakeup();
    }

    @Override
    public void send(ChangeRequest request) {
        send(request, null);
    }

    public void close() {
        for (PipeWorker p : _pipes.values()) {
            p.forceClose();
        }
        _pipes.clear();
        try {
            _selector.close();
        } catch (IOException e) {
            logger.warning(Util.getErrorMessage(e));
        }
    }
}
