package com.thomax.bootspring.test;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Throwables;
import com.thomax.bootspring.model.MsgContent;
import com.thomax.bootspring.model.Result;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StressTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(StressTest.class);

    @Autowired
    private RestTemplate restTemplate;

    private long startTime;
    private long endTime;

    @Test
    public void testCreateMessage() {
        ExecutorService executorService = Executors.newFixedThreadPool(500);
        final CountDownLatch cdOrder = new CountDownLatch(1);//指挥官的命令，设置为1，指挥官一下达命令，则cutDown,变为0，战士们执行任务
        final CountDownLatch cdAnswer = new CountDownLatch(100000);//因为有三个战士，所以初始值为3，每一个战士执行任务完毕则cutDown一次，当三个都执行完毕，变为0，则指挥官停止等待。
        List<Future<Result>> futures = new ArrayList<>();
        final Random random = new Random();
        startTime = System.currentTimeMillis();
        for (int i = 0; i < 1; i++) { //100000
            final int finalI = i;
            Callable callable = new Callable() {

                @Override
                public Object call() {

                    Result result = new Result();
                    result.setThreadId(Thread.currentThread().getId());
                    result.setThreadName(Thread.currentThread().getName());
                    try {
                        //Thread.sleep(100);
                        Object obj = new Object(); //换成请求对象
                        MsgContent msgContent = new MsgContent();
                        msgContent.setContent(Lists.newArrayList(obj));
                        msgContent.setType(1);

                        String json = JSON.toJSONString(msgContent);
                        //cdOrder.await(); //战士们都处于等待命令状态
                        HttpHeaders headers = new HttpHeaders();
                        headers.setContentType(MediaType.valueOf("application/json;UTF-8"));
                        HttpEntity<String> strEntity = new HttpEntity<String>(json, headers);
                        ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://localhost:8700/api/MsgProcess.do", json, String.class);  //http://10.0.108.101:8072/api/MsgProcess.do
                        //ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://localhost:8700/api/MsgProcess.do" ,null, String.class,data);
                        LOGGER.info("线程：{}，返回状态：{}，状态代码:{}，返回值：{},id:{},返回内容:{}", Thread.currentThread().getName(), responseEntity.getStatusCode(), responseEntity.getStatusCodeValue(), responseEntity.getBody(), finalI);


                        result.setResultCode(responseEntity.getStatusCodeValue());
                        result.setResultMsg(responseEntity.getBody());


                    } catch (Exception e) {
                        //e.printStackTrace();
                        result.setResultMsg(Throwables.getStackTraceAsString(e));
                        result.setResultCode(-1);
                    } finally {
                        //cdAnswer.countDown();
                    }
                    return result;
                }
            };

            futures.add(executorService.submit(callable));

        }
        List<Result> results = new ArrayList<>();
        try {
            Thread.sleep(20000);
            //cdOrder.countDown();
            //cdAnswer.await();
            for (Future future : futures) {
                Result result = (Result) future.get();
                LOGGER.info("result:{}", result);
                if (result.getResultCode() == -1) {
                    results.add(result);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        endTime = System.currentTimeMillis();
        LOGGER.info("发送：{}", futures.size());
        LOGGER.info("失败：{}", results.size());
        LOGGER.info("耗时：{}", (endTime - startTime) / 1000);
        executorService.shutdown();

       /* try {
            for (int i = 0; i < 10000; i++) {
                TlxAppTradeOrderInfo tlxAppTradeOrderInfo = new TlxAppTradeOrderInfo();
                tlxAppTradeOrderInfo.setAmount(BigDecimal.valueOf(i));
                tlxAppTradeOrderInfo.setCredate(new Date());
                tlxAppTradeOrderInfo.setDtamount(BigDecimal.valueOf(0));
                tlxAppTradeOrderInfo.setId((long) i);
                tlxAppTradeOrderInfo.setOrgname("测试平台");
                tlxAppTradeOrderInfo.setOrgno(1000);
                tlxAppTradeOrderInfo.setPayname(i % 2 == 0 ? "微信" : "支付宝");
                tlxAppTradeOrderInfo.setTradecurrency("CNY");
                tlxAppTradeOrderInfo.setTradetype(1);
                tlxAppTradeOrderInfo.setShopno(20160000);
                tlxAppTradeOrderInfo.setTradecurrencysign("¥");
                MsgContent msgContent = new MsgContent();
                msgContent.setContent(Lists.newArrayList(tlxAppTradeOrderInfo));
                msgContent.setType(1);

                String json = JSON.toJSONString(msgContent);
                String key = TLinxUtil.AESEncrypt(json, "bfc49882b70ae48f16e33ffd45454151");
                LOGGER.info("加密数据：{},{}", json, key);
                //cdOrder.await(); //战士们都处于等待命令状态

                ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://localhost:8088/api/MsgProcess.do?data=" + key, null, String.class);
                LOGGER.info("线程：{}，返回状态：{}，状态代码:{}，返回值：{},id:{}", Thread.currentThread().getName(), responseEntity.getStatusCode(), responseEntity.getStatusCodeValue(), responseEntity.getBody(), i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

}
