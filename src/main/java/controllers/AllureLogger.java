package controllers;

import io.qameta.allure.Step;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.PrintWriter;
import java.io.StringWriter;

public class AllureLogger {

    private static final Logger logger = LogManager.getLogger(AllureLogger.class);
    private static final String BRACKETS_STRING = "{} {}";

    private AllureLogger() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * @param thr
     * @return caller class name with brackets []
     */
    private static String getCaller(Throwable thr) {
        StackTraceElement[] ste = thr.getStackTrace();
        String callerClassName = null;

        int i = 1;
        while (i < ste.length && ste[i].getMethodName().startsWith("access$")) {
            ++i;
        }
        if (i < ste.length) {
            callerClassName = ste[i].getClassName().replace("ru.testing.", "");
        }
        return String.format("[%s]", callerClassName);
    }

    @Step("{0}")
    public static void info(String message) {
        Throwable thr = new Throwable();
        logger.log(Level.INFO, BRACKETS_STRING, getCaller(thr), message);
    }

    @Step("{0}")
    public static void debug(String message) {
        Throwable thr = new Throwable();
        logger.log(Level.DEBUG, BRACKETS_STRING, getCaller(thr), message);
    }

    @Step("{0}")
    public static void error(String message) {
        Throwable thr = new Throwable();
        logger.log(Level.ERROR, BRACKETS_STRING, getCaller(thr), message);
        thr.addSuppressed(new Exception());
    }

    /**
     * @param e
     */
    @Step("Stacktrace")
    public static void getStackTrace(Exception e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        logger.log(Level.ERROR, sw.toString());
    }
}