package com.thomax.letsgo.advanced.thread;

import java.util.concurrent.Executor;

public class ExecutorHandling {
    public Executor getExecutor1(Runnable command1) {
        return (Runnable command) -> {
            new Thread(command1).start(); //由CPU调度
        };
    }
    public Executor getExecutor2(Runnable command2) {
        return (Runnable command) -> {
            command2.run(); //当做普通对象直接执行方法直接运行方法
        };
    }
}


