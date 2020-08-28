package com.thomax.shadow.client.network.nio;

/**
 * Interface of socket handler
 */
public interface ISocketHandler {
    void send(ChangeRequest request, byte[] data);
    void send(ChangeRequest request);
}
