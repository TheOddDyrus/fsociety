package com.thomax.shadow.client.misc;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.thomax.shadow.client.network.proxy.IProxy.TYPE;
import com.thomax.shadow.client.ss.AesCrypt;

/**
 * Data class for configuration to bring up server
 */
public class Config {
    private String _ipAddr;
    private int _port;
    private String _localIpAddr;
    private int _localPort;
    private String _method;
    private String _password;
    private String _logLevel;
    private TYPE _proxyType;

    public Config() {
        loadFromJson("");
    }

    public Config(String ipAddr, int port, String localIpAddr, int localPort, String method, String password) {
        this();
        _ipAddr = ipAddr;
        _port = port;
        _localIpAddr = localIpAddr;
        _localPort = localPort;
        _method = method;
        _password = password;
        _proxyType = TYPE.AUTO;
    }

    public Config(String ipAddr, int port, String localIpAddr, int localPort, String method, String password, TYPE type) {
        this(ipAddr, port, localIpAddr, localPort, method, password);
        _proxyType = type;
    }

    public void setRemoteIpAddress(String value) {
        _ipAddr = value;
    }

    public String getRemoteIpAddress() {
        return _ipAddr;
    }

    public void setLocalIpAddress(String value) {
        _localIpAddr = value;
    }

    public String getLocalIpAddress() {
        return _localIpAddr;
    }

    public void setRemotePort(int value) {
        _port = value;
    }

    public int getRemotePort() {
        return _port;
    }

    public void setLocalPort(int value) {
        _localPort = value;
    }

    public int getLocalPort() {
        return _localPort;
    }

    public void setProxyType(String value) {
        _proxyType = TYPE.AUTO;
        if (value.toLowerCase().equals(TYPE.HTTP.toString().toLowerCase())) {
            _proxyType = TYPE.HTTP;
        }
        else if (value.toLowerCase().equals(TYPE.SOCKS5.toString().toLowerCase())) {
            _proxyType = TYPE.SOCKS5;
        }
    }

    public void setProxyType(TYPE value) {
        _proxyType = value;
    }
    public TYPE getProxyType() {
        return _proxyType;
    }

    public void setMethod(String value) {
        _method = value;
    }

    public String getMethod() {
        return _method;
    }

    public void setPassword(String value) {
        _password = value;
    }

    public String getPassword() {
        return _password;
    }

    public void setLogLevel(String value) {
        _logLevel = value;
        Log.init(getLogLevel());
    }

    public String getLogLevel() {
        return _logLevel;
    }

    public void loadFromJson(String jsonStr) {
        if (jsonStr.length() == 0) {
            jsonStr = "{}";
        }

        JSONObject jObj = JSON.parseObject(jsonStr);
        _ipAddr = (String)jObj.getOrDefault("remoteIpAddress", "");
        _port = ((Number)jObj.getOrDefault("remotePort", 1080)).intValue();
        _localIpAddr = (String)jObj.getOrDefault("localIpAddress", "127.0.0.1");
        _localPort = ((Number)jObj.getOrDefault("localPort", 1080)).intValue();
        _method = (String)jObj.getOrDefault("method", AesCrypt.CIPHER_AES_256_CFB);
        _password = (String)jObj.getOrDefault("password", "");
        _logLevel = (String)jObj.getOrDefault("logLevel", "INFO");
        setProxyType((String) jObj.getOrDefault("proxyType", TYPE.SOCKS5.toString().toLowerCase()));
        setLogLevel(_logLevel);
    }

    public String saveToJson() {
        JSONObject jObj = new JSONObject();
        jObj.put("remoteIpAddress", _ipAddr);
        jObj.put("remotePort", _port);
        jObj.put("localIpAddress", _localIpAddr);
        jObj.put("localPort", _localPort);
        jObj.put("method", _method);
        jObj.put("password", _password);
        jObj.put("proxyType", _proxyType.toString().toLowerCase());
        jObj.put("logLevel", _logLevel);

        return jObj.toJSONString();
    }
}
