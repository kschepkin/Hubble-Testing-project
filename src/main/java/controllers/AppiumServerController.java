package controllers;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;

import static io.appium.java_client.service.local.AppiumDriverLocalService.buildService;

public final class AppiumServerController {
    private static final AppiumDriverLocalService service;

    private AppiumServerController() {
        throw new IllegalStateException("Utility class");
    }

    static {
        service = buildService(new AppiumServiceBuilder().
                withIPAddress("127.0.0.1").
                usingPort(Integer.parseInt("4723"))
                .withAppiumJS(new File("/usr/local/bin/appium"))
                .withArgument(GeneralServerFlag.LOG_LEVEL, "info")
                .withArgument(GeneralServerFlag.ALLOW_INSECURE, "chromedriver_autodownload"));
    }

    public static void startAppiumServer() {
        try {
            service.start();
        } catch (Exception e) {
            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));
            AllureLogger.error(errors.toString());
        }
    }

    public static void stopAppiumServer() {
        try {
            if (service.isRunning()) {
                service.stop();
            }
        } catch (Exception e) {
            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));
            AllureLogger.error(errors.toString());
        }
    }
}
