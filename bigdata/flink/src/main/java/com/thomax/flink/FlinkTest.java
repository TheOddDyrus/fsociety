package com.thomax.flink;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Flink是基于事件驱动的，它是一个面向流的处理框架，Flink基于每个事件一行一行地流式处理，是真正的流式计算.
 * 另外他也可以基于流来模拟批进行计算实现批处理，所以他在技术上具有更好的扩展性，
 * 未来可能会成为一个统一的大数据处理引擎
 */
public class FlinkTest {

    public static void main(String[] args) {
        wordCount();
    }

    /**
     * flink单词个数统计
     */
    private static void wordCount() {
        int port = 8888;

        //自定义服务流
        new Thread(() -> {
            try {
                System.out.println("开始启动服务器....");
                ServerSocket ss = new ServerSocket(port);
                Socket s = ss.accept();
                int i = 0;
                while (true) {
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
                    if (i++ % 2 == 0) {
                        bw.write("a b" + "\n");
                    } else {
                        bw.write("a" + "\n");
                    }

                    bw.flush();
                    Thread.sleep(2000);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        //获取执行环节
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //通过socket输入数据
        DataStream<String> text = env.socketTextStream("127.0.0.1", port);
        //解析数据、对数据进行分组、窗口函数和统计个数
        DataStream<WordAndCount> windowCounts = text.flatMap(new FlatMapFunction<String, WordAndCount>() {
            private static final long serialVersionUID = 1L;

            public void flatMap(String value, Collector<WordAndCount> out) throws Exception {
                for (String word : value.split(" ")) {
                    out.collect(new WordAndCount(word, 1));
                }
            }
        }).keyBy(new KeySelector<WordAndCount, Object>() {
            @Override
            public Object getKey(WordAndCount wordAndCount) throws Exception {
                return wordAndCount.word;
            }
        }).timeWindow(Time.seconds(5), Time.seconds(1))
                .reduce(new ReduceFunction<WordAndCount>() {
                    public WordAndCount reduce(WordAndCount value1, WordAndCount value2) throws Exception {
                        return new WordAndCount(value1.word, value1.count + value2.count);
                    }
                });
        windowCounts.print().setParallelism(1);
        try {
            env.execute("Socket Window WordAndCount");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class WordAndCount {
        public String word;
        public long count;

        public WordAndCount() { }

        public WordAndCount(String word, long count) {
            this.word = word;
            this.count = count;
        }

        @Override
        public String toString() {
            return word + " : " + count;
        }
    }

}
