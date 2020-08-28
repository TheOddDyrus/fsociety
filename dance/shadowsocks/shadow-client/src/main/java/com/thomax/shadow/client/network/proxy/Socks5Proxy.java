package com.thomax.shadow.client.network.proxy;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Provide local socks5 statue and required response
 */
public class Socks5Proxy implements IProxy {
    public final static int ATYP_IP_V4 = 0x1;
    public final static int ATYP_DOMAIN_NAME = 0x3;
    public final static int ATYP_IP_V6 = 0x4;

    private Logger logger = Logger.getLogger(Socks5Proxy.class.getName());
    private enum STAGE {SOCK5_HELLO, SOCKS_ACK, SOCKS_READY}
    private STAGE _stage;

    public Socks5Proxy() {
        _stage = STAGE.SOCK5_HELLO;
    }

    public TYPE getType() {
        return TYPE.SOCKS5;
    }

    public boolean isReady() {
        return (_stage == STAGE.SOCKS_READY);
    }

    public byte[] getResponse(byte[] data) {
        byte[] respData = null;

        switch (_stage) {
            case SOCK5_HELLO:
                if (isMine(data)) {
                    respData = new byte[] {5, 0};
                }
                else {
                    respData = new byte[] {0, 91};
                }
                _stage = STAGE.SOCKS_ACK;
                break;
            case SOCKS_ACK:
                respData = new byte[] {5, 0, 0, 1, 0, 0, 0, 0, 0, 0};
                _stage = STAGE.SOCKS_READY;
                break;
            default:
                // TODO: exception
                break;

        }

        return respData;
    }

    public List<byte[]> getRemoteResponse(byte[] data) {
        List<byte[]> respData = null;
        int dataLength = data.length;

        /*
        There are two stage of establish Sock5:
            1. HELLO (3 bytes)
            2. ACK (3 bytes + dst info)
        as Client sending ACK, it might contain dst info.
        In this case, server needs to send back ACK response to client and start the remote socket right away,
        otherwise, client will wait until timeout.
         */
        if (_stage == STAGE.SOCKS_READY) {
            respData = new ArrayList<>(1);
            // remove socks5 header (partial)
            if (dataLength > 3) {
                dataLength -= 3;
                byte[] temp = new byte[dataLength];
                System.arraycopy(data, 3, temp, 0, dataLength);
                respData.add(temp);
            }
        }

        return respData;
    }

    @Override
    public boolean isMine(byte[] data) {
        if (data[0] == 0x5) {
            return true;
        }

        return false;
    }
}
