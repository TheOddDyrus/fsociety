package com.thomax.shadow.client.network;

import com.thomax.shadow.client.config.Constant;
import com.thomax.shadow.client.misc.Config;
import com.thomax.shadow.client.misc.Util;
import com.thomax.shadow.client.network.io.PipeSocket;
import com.thomax.shadow.client.ss.CryptFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.InvalidAlgorithmParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

/**
 * Blocking local server for shadowsocks
 */
public class LocalServer implements IServer {
    private Logger logger = Logger.getLogger(LocalServer.class.getName());
    private Config _config;
    private ServerSocket _serverSocket;
    private Executor _executor;
    private List<PipeSocket> _pipes;

    public LocalServer(Config config) throws IOException, InvalidAlgorithmParameterException {
        if (!CryptFactory.isCipherExisted(config.getMethod())) {
            throw new InvalidAlgorithmParameterException(config.getMethod());
        }
        _config = config;
        _serverSocket = new ServerSocket(config.getLocalPort(), 128);
        _executor = Executors.newCachedThreadPool();
        _pipes = new ArrayList<>();

        // print server info
        logger.info("shadow-client v" + Constant.VERSION);
        logger.info(config.getProxyType() + " Proxy Server starts at port: " + config.getLocalPort());
    }

    @Override
    public void run() {
        while (true) {
            try {
                Socket localSocket = _serverSocket.accept();
                PipeSocket pipe = new PipeSocket(_executor, localSocket, _config);
                _pipes.add(pipe);
                _executor.execute(pipe);
            } catch (IOException e) {
                logger.warning(Util.getErrorMessage(e));
            }
        }
    }

    public void close() {
        try {
            for (PipeSocket p : _pipes) {
                p.close();
            }
            _pipes.clear();
            _serverSocket.close();
        } catch (IOException e) {
            logger.warning(Util.getErrorMessage(e));
        }
    }

}
