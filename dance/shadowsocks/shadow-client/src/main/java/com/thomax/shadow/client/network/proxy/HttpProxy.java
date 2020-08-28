package com.thomax.shadow.client.network.proxy;

import com.thomax.shadow.client.config.Constant;
import com.thomax.shadow.client.misc.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Provide local HTTP proxy statue and required response
 */
public class HttpProxy implements IProxy {
    private static final String[] HTTP_METHODS =
            new String[] {"OPTIONS", "GET", "HEAD", "POST", "PUT", "DELETE", "TRACE", "CONNECT"};

    private Logger logger = Logger.getLogger(HttpProxy.class.getName());
    private boolean _isReady;
    private boolean _isHttpConnect;
    private Map<String, String> methodCache;

    public HttpProxy() {
        _isReady = false;
        _isHttpConnect = false;
    }

    public TYPE getType() {
        return TYPE.HTTP;
    }

    public boolean isReady() {
        return _isReady;
    }

    public byte[] getResponse(byte[] data) {
        if (methodCache == null) {
            methodCache = getHttpMethod(data);
        }
        setHttpMethod(methodCache);

        if (_isHttpConnect)
            return String.format("HTTP/1.0 200\r\nProxy-agent: %s/%s\r\n\r\n",
                    Constant.PROG_NAME, Constant.VERSION).getBytes();

        return null;
    }

    public List<byte[]> getRemoteResponse(byte[] data) {
        List<byte[]> respData = new ArrayList<>(2);
        String host;
        int port = 80; // HTTP port
        if (methodCache == null) {
            methodCache = getHttpMethod(data);
        }
        String[] hostInfo = methodCache.get("host").split(":");

        // get hostname and port
        host = hostInfo[0];
        if (hostInfo.length > 1) {
            port = Integer.parseInt(hostInfo[1]);
        }

        byte[] ssHeader = Util.composeSSHeader(host, port);
        respData.add(ssHeader);
        if (!_isHttpConnect) {
            byte[] httpHeader = reconstructHttpHeader(methodCache, data);
            respData.add(httpHeader);
        }

        _isReady = true;
        return respData;
    }

    @Override
    public boolean isMine(byte[] data) {
        if (methodCache == null) {
            methodCache = getHttpMethod(data);
        }
        String method = methodCache.get("method");

        if (method != null) {
            for (String s : HTTP_METHODS) {
                if (s.equals(method)) {
                    return true;
                }
            }
        }

        return false;
    }

    private Map<String, String> getHttpMethod(byte[] data) {
        String httpRequest = Util.bytesToString(data, 0, data.length);
        String[] httpHeaders = httpRequest.split("\\r?\\n");
        boolean isHostFound = true;
        //Pattern pattern = Pattern.compile("^([a-zA-Z]*) [hHtTpP]{0,4}[:\\/]{0,3}(\\S[^/ ]*)");
        Pattern pattern = Pattern.compile("^([a-zA-Z]*) [htps]{0,4}[:/]{0,3}(\\S[^/]*)(\\S*) (\\S*)");
        Map<String, String> header = new HashMap<>();
        if (httpHeaders.length > 0) {
            logger.fine("HTTP Header: " + httpHeaders[0]);
            Matcher matcher = pattern.matcher(httpHeaders[0]);
            if (matcher.find()) {
                header.put("method", matcher.group(1));
                if (matcher.group(2).startsWith("/")) {
                    header.put("url", "/");
                    isHostFound = false;
                }
                else {
                    header.put("host", matcher.group(2));
                    header.put("url", matcher.group(3));
                }
                header.put("version", matcher.group(4));
            }
        }

        if (!isHostFound) {
            for (String line : httpHeaders) {
                if (line.toLowerCase().contains("host")) {
                    String info = line.split(":")[1].trim();
                    header.put("host", info);
                    break;
                }
            }
        }
        return header;
    }

    private byte[] reconstructHttpHeader(Map<String, String> method, byte[] data) {
        String httpRequest = Util.bytesToString(data, 0, data.length);
        String[] httpHeaders = httpRequest.split("\\r?\\n");
        StringBuilder sb = new StringBuilder();
        boolean isFirstLine = true;

        //logger.info("original HttpHeader:" + httpRequest);
        for (String line : httpHeaders) {
            if (isFirstLine && _isHttpConnect) {
                sb.append(method.get("method"));
                sb.append(" ");
                sb.append(method.get("host"));
                sb.append(" ");
                sb.append(method.get("version"));
                sb.append("\r\n");
                sb.append("User-Agent: test/0.1\r\n");
                break;
            }
            else if (isFirstLine) {
                sb.append(method.get("method"));
                sb.append(" ");
                sb.append(method.get("url"));
                sb.append(" ");
                sb.append(method.get("version"));
                isFirstLine = false;
            }
            else if (line.toLowerCase().contains("cache-control")) {
                sb.append("Pragma: no-cache\r\n");
                sb.append("Cache-Control: no-cache");
            }
            else if (line.toLowerCase().contains("proxy-connection")) {
                //Proxy-Connection
                String[] fields = line.split(":");
                sb.append("Connection: ");
                sb.append(fields[1].trim());
            }
            else if (line.toLowerCase().contains("if-none-match")) {
                continue;
            }
            else if (line.toLowerCase().contains("if-modified-since")) {
                continue;
            }
            else {
                sb.append(line);
            }
            sb.append("\r\n");
        }

        sb.append("\r\n");
        //logger.info("reconstructHttpHeader:" + sb.toString());
        return sb.toString().getBytes();
    }

    private void setHttpMethod(Map<String, String> header) {
        String method = header.get("method");

        if (method != null) {
            if (method.toUpperCase().equals("CONNECT")) {
                _isHttpConnect = true;
            }
            else {
                _isHttpConnect = false;
            }
        }
    }

}
