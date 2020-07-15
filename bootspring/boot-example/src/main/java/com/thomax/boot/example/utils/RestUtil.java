package com.thomax.boot.example.utils;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Spring的restTemplate工具类
 */
public class RestUtil {

    private static RestTemplate restTemplate = new RestTemplate();

    public static <T> T get(String url, Class<T> returnClazz, Map<String, Object> parameters) {
        if (parameters == null) {
            return restTemplate.getForObject(url, returnClazz);
        }
        return restTemplate.getForObject(url, returnClazz, parameters);
    }

    public static <T> T post(String url, Class<T> returnClazz, Map<String, Object> inputHeader, Map<String, Object> inputParameter, String jsonBody) {
        //构建Header
        HttpHeaders httpHeaders = new HttpHeaders();
        if (inputHeader != null) {
            Set<String> keys = inputHeader.keySet();
            for (String key : keys) {
                httpHeaders.add(key, inputHeader.get(key).toString());
            }
        }
        //设置请求的类型及编码
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        httpHeaders.setContentType(type);
        httpHeaders.add("Accept", "application/json");
        List<MediaType> acceptableMediaTypes = new ArrayList<>();
        acceptableMediaTypes.add(MediaType.ALL);
        httpHeaders.setAccept(acceptableMediaTypes);

        HttpEntity<String> formEntity = new HttpEntity<>(jsonBody, httpHeaders);
        if (inputParameter == null) {
            return restTemplate.postForObject(url, formEntity, returnClazz);
        }
        return restTemplate.postForObject(url, formEntity, returnClazz, inputParameter);
    }

    public static void main(String[] args) {
        String s = RestUtil.get("https://www.baidu.com", String.class, null);
        System.out.println(s);
    }

}
