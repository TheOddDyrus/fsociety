package com.thomax.shadow.client.network.proxy;

import com.thomax.shadow.client.misc.Reflection;
import com.thomax.shadow.client.misc.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Proxy factory
 */
public class ProxyFactory {
    public static final Map<IProxy.TYPE, String> proxies = new HashMap<IProxy.TYPE, String>() {{
        put(IProxy.TYPE.HTTP, HttpProxy.class.getName());
        put(IProxy.TYPE.SOCKS5, Socks5Proxy.class.getName());
        put(IProxy.TYPE.AUTO, AutoProxy.class.getName());
    }};
    private static Logger logger = Logger.getLogger(ProxyFactory.class.getName());

    public static boolean isProxyTypeExisted(String name) {
        IProxy.TYPE type = IProxy.TYPE.valueOf(name);
        return (proxies.get(type) != null);
    }

    public static IProxy get(IProxy.TYPE type) {
        try {
            Object obj = Reflection.get(proxies.get(type));
            return (IProxy)obj;

        } catch (Exception e) {
            logger.info(Util.getErrorMessage(e));
        }

        return null;
    }

    public static List<IProxy.TYPE> getSupportedProxyTypes() {
        List sortedKeys = new ArrayList<>(proxies.keySet());
        Collections.sort(sortedKeys);
        return sortedKeys;
    }
}
