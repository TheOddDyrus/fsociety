package com.thomax.agent.premain;

import com.thomax.agent.transformer.TestClassTransformer;

import java.lang.instrument.Instrumentation;

public class PremainAgent {

    //JVM 首先尝试在代理类上调用以下方法
    public static void premain(String agentArgs, Instrumentation inst) {
        inst.addTransformer(new TestClassTransformer());
    }

    //如果代理类没有实现上面的方法，那么 JVM 将尝试调用该方法
    public static void premain(String agentArgs) {

    }

}
