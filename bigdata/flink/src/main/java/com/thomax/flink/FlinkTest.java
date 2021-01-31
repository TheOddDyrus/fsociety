package com.thomax.flink;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;

public class FlinkTest {

    public static void main(String[] args) {
        wordCount();
    }

    /**
     * flink单词个数统计
     */
    private static void wordCount() {
        //获取执行环节
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        //通过socket输入数据
        String hostname = "";
        int port = 0;
        DataStream<String> text = env.socketTextStream(hostname, port);

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
            env.execute("Socket Window WordCount");
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
