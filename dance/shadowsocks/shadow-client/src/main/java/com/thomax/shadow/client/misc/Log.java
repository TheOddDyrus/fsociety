package com.thomax.shadow.client.misc;

import java.util.Locale;
import java.util.Properties;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Initialized level of root logger
 */
public class Log {
    private static boolean handlerInit = false;

    public static void init() {
        init(Level.INFO);
    }

    public static void init(Level level) {
        Logger rootLogger = getRootLogger();
        if (handlerInit) {
            rootLogger.setLevel(level);
            for(Handler handler : rootLogger.getHandlers()) {
                handler.setLevel(level);
            }
            return;
        }

        // disable message localization
        Locale.setDefault(Locale.ENGLISH);
        // config log output format
        Properties props = System.getProperties();
        props.setProperty("java.util.logging.SimpleFormatter.format", "%1$tY-%1$tb-%1$td %1$tT [%4$s] %5$s%n");
        // setup root logger
        //Logger rootLogger = getRootLogger();
        rootLogger.setUseParentHandlers(false);
        for(Handler handler : rootLogger.getHandlers()) {
            rootLogger.removeHandler(handler);
        }
        // set log level and format
        rootLogger.setLevel(level);
        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(level);
        rootLogger.addHandler(handler);
        handlerInit = true;
    }

    public static void init(String level) {
        Level l = Level.parse(level);
        init(l);
    }

    public static void addHandler(Handler handler) {
        Logger rootLogger = getRootLogger();
        Level logLevel = Level.INFO;
        for (Handler h : rootLogger.getHandlers()) {
            logLevel = h.getLevel();
        }

        handler.setLevel(logLevel);
        rootLogger.addHandler(handler);
    }

    private static Logger getRootLogger() {
        return Logger.getLogger("com.thomax.shadow.client");
    }
}
