package com.thomax.agent.premain;

import com.thomax.agent.transformer.TestClassTransformer;

import java.lang.instrument.Instrumentation;

public class AgentMain {

    public static void agentmain(String agentArgs, Instrumentation instrumentation) {
        instrumentation.addTransformer(new TestClassTransformer(), true);
    }

}
