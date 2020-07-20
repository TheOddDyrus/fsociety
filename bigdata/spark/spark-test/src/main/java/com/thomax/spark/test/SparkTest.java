package com.thomax.spark.test;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.Arrays;

import static com.google.common.base.Preconditions.checkArgument;

public class SparkTest {

    public static void main(String[] args) {
        checkArgument(args.length > 0, "Please provide the path of input file as first parameter.");
        new SparkTest().run(args[0]);
    }

    public void run(String inputFilePath) {

        /*
          This is the address of the Spark cluster. We will call the task from WordCountTest and we
          use a local standalone cluster. [*] means use all the cores available.
          See http://spark.apache.org/docs/latest/submitting-applications.html#master-urls
         */
        String master = "local[*]";

        /*
         * Initialises a Spark context.
         */
        SparkConf conf = new SparkConf()
                .setAppName(SparkTest.class.getName())
                .setMaster(master);
        JavaSparkContext context = new JavaSparkContext(conf);

        /*
         * Performs a work count sequence of tasks and prints the output with a logger.
         */
        context.textFile(inputFilePath)
                .flatMap(text -> Arrays.asList(text.split(" ")).iterator())
                .mapToPair(word -> new Tuple2<>(word, 1))
                .reduceByKey(Integer::sum)
                .foreach(result -> System.out.println(String.format("Word [%s] count [%d].", result._1(), result._2)));
    }
}
