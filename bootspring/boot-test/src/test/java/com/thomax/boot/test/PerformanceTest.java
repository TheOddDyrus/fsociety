package com.thomax.boot.test;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 性能测试
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class PerformanceTest {

    @Test
    public void testCreateMessage() {
        int nTasks = 100000; //压测总量

        List<Future<Result>> resultList = new ArrayList<>(nTasks);
        ExecutorService executorService = Executors.newFixedThreadPool(500);

        final CountDownLatch startGate = new CountDownLatch(1); //开始开关
        final CountDownLatch endGate = new CountDownLatch(nTasks); //结束开关

        for (int i = 0; i < nTasks; i++) {
            final int finalI = i;
            Callable<Result> callable = () -> {
                Result result = new Result();
                try {
                    startGate.await();

                    Map<String, Object> params = new HashMap<>();
                    params.put("param1", 123);
                    String resultStr = HttpUtil.post("http://localhost:8700/test", params, 30 * 1000);
                    log.info("times:{}, thread:{}, result:{}", finalI, Thread.currentThread().getName(), resultStr);

                    JSONObject resultJson = JSON.parseObject(resultStr);
                    result.setResultCode(resultJson.getInteger("code"));
                    result.setResultMsg(resultJson.getString("msg"));
                } catch (Exception e) {
                    log.error("times:{}, thread:{}", finalI, Thread.currentThread().getName());
                    result.setResultCode(-1);
                    result.setResultMsg(e.getMessage());
                } finally {
                    endGate.countDown();
                }
                return result;
            };
            resultList.add(executorService.submit(callable));
        }

        List<Result> failedList = new ArrayList<>();
        long startTime = System.currentTimeMillis(); //开始时间
        startGate.countDown();
        try {
            endGate.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis(); //结束时间

        while (resultList.size() != 0) {
            Iterator<Future<Result>> iterator = resultList.iterator();
            while (iterator.hasNext()) {
                Future<Result> future = iterator.next();
                if (future.isDone()) {
                    Result result = null;
                    try {
                        result = future.get();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (result == null) {
                        continue;
                    }

                    if (!Objects.equals(result.getResultCode(), 200)) {
                        failedList.add(result);
                    }
                    iterator.remove();
                }
            }
        }

        log.info("================ 压测结束 ===================");
        log.info("发送：{}", resultList.size());
        log.info("失败：{}", failedList.size());
        log.info("耗时：{} 秒", (endTime - startTime) / 1000);
        executorService.shutdown();
    }

    @Data
    private static class Result {

        private Integer resultCode;

        private String resultMsg;

    }

}
