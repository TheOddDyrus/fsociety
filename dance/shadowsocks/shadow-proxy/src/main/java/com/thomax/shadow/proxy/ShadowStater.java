package com.thomax.shadow.proxy;

import com.thomax.shadow.proxy.socks.SocksClient;

public class ShadowStater {

    public static void main(String[] args) {
        try {
            SocksClient.run();
        } catch (InterruptedException e) {
            System.out.println("发生异常:" + e.getMessage());
        }
    }

}
