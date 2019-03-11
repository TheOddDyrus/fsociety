package com.thomax.example.test;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Throwables;
import com.thomax.example.model.MsgContent;
import com.thomax.example.model.Result;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PerformanceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(PerformanceTest.class);

    private static RestTemplate restTemplate;

    static {
        int maxTotalConnect = 100; //连接池的最大连接数默认为0
        int maxConnectPerRoute = 100; //单个主机的最大连接数
        int connectTimeout = 5; //连接超时默认2s
        int readTimeout = 30; //读取超时默认30s

        HttpClient httpClient = HttpClientBuilder.create()
                                                .setMaxConnTotal(maxTotalConnect)
                                                .setMaxConnPerRoute(maxConnectPerRoute).build();
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
        factory.setConnectTimeout(connectTimeout);
        factory.setReadTimeout(readTimeout);
        restTemplate = new RestTemplate(factory);
    }

    @Test
    public void testCreateMessage() {
        ExecutorService executorService = Executors.newFixedThreadPool(500); //线程池容量
        List<Future<Result>> futures = new ArrayList<>();
        int nTasks = 100000; //压测总量

        final CountDownLatch startGate = new CountDownLatch(1);
        final CountDownLatch endGate = new CountDownLatch(nTasks);

        for (int i = 0; i < nTasks; i++) {
            final int finalI = i;
            Callable callable = new Callable() {
                @Override
                public Object call() {
                    Result result = new Result();
                    result.setThreadId(Thread.currentThread().getId());
                    result.setThreadName(Thread.currentThread().getName());
                    try {
                        Object obj = new Object();
                        MsgContent msgContent = new MsgContent();
                        List<Object> list = new ArrayList<>();
                        list.add("参数1");
                        list.add("参数2");
                        msgContent.setContent(list);
                        msgContent.setType(1);

                        String json = JSON.toJSONString(msgContent);
                        startGate.await(); //闭锁1阻塞
                        HttpHeaders headers = new HttpHeaders();
                        headers.setContentType(MediaType.valueOf("application/json;UTF-8"));
                        HttpEntity<String> httpEntity = new HttpEntity<>(json, headers);
                        ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://localhost:8700/test", httpEntity, String.class);
                        LOGGER.info("线程：{}，返回状态：{}，状态代码:{}，返回值：{},id:{},返回内容:{}", Thread.currentThread().getName(),
                                                                                                    responseEntity.getStatusCode(),
                                                                                                    responseEntity.getStatusCodeValue(),
                                                                                                    responseEntity.getBody(),
                                                                                                    finalI);
                        result.setResultCode(responseEntity.getStatusCodeValue());
                        result.setResultMsg(responseEntity.getBody());
                    } catch (Exception e) {
                        result.setResultMsg(Throwables.getStackTraceAsString(e));
                        result.setResultCode(-1);
                    } finally {
                        endGate.countDown(); //闭锁2计数减1
                    }
                    return result;
                }
            };
            futures.add(executorService.submit(callable));
        }

        List<Result> results = new ArrayList<>();
        long startTime = 0;
        long endTime = 0;
        try {
            startTime = System.nanoTime();
            startGate.countDown(); //闭锁1释放
            endGate.await(); //闭锁等待计数清0
            endTime = System.nanoTime();
            for (Future future : futures) {
                Result result = (Result) future.get();
                if (result.getResultCode() == -1) {
                    results.add(result);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            throw launderThrowable(e);
        }

        LOGGER.info("发送：{}", futures.size());
        LOGGER.info("失败：{}", results.size());
        LOGGER.info("耗时：{} 毫秒", (endTime - startTime) / 1000000);
        executorService.shutdown();
    }

    private RuntimeException launderThrowable(Throwable t) {
        if (t instanceof RuntimeException) {
            return (RuntimeException) t;
        } else if (t instanceof Error) {
            throw (Error) t;
        } else {
            throw new IllegalStateException("not unchecked", t);
        }
    }

}
