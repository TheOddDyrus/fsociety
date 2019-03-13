package com.thomax.letsgo.advanced.thread;

import java.util.concurrent.Executor;

public class ExecutorHandling { }

class ThreadPerTaskExecutor implements Executor {
    @Override
    public void execute(Runnable command) {
        new Thread(command).start(); //由CPU调度
    }
}
class WithinThreadExecutor implements Executor {
    @Override
    public void execute(Runnable command) {
        command.run(); //直接运行方法
    }
}
