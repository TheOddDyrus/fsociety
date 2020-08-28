package com.thomax.shadow.client.network.proxy;

import java.util.List;

public interface IProxy {
    enum TYPE {SOCKS5, HTTP, AUTO}

    boolean isReady();
    TYPE getType();
    byte[] getResponse(byte[] data);
    List<byte[]> getRemoteResponse(byte[] data);
    boolean isMine(byte[] data);
}
