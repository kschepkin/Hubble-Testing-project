package controllers;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class PropertyLoader {

    private PropertyLoader() {
    }

    private static final String PATH_TO_PROPERTIES_DEV = "src/test/resources/dev/Application.properties";
    private static final String PATH_TO_PROPERTIES_UAT = "src/test/resources/uat/Application.properties";
    private static final String PATH_TO_PROPERTIES_STG = "src/test/resources/stg/Application.properties";
    private static String path = null;
    public static final String NAME = "name";
    public static final String EMAIL = ".email";
    public static final String PASSWORD = ".password";
    public static final String PHONE = ".phone";
    public static final String CURRENT_USER = "currentUser";

    /**
     * Метод получения строки с тестовыми данными из environment.properties
     *
     * @return Строка с тестовымиданными
     */
    public static String loadProperty(String name) {
        //инициализируем специальный объект Properties
        //типа Hashtable для удобной работы с данными
        if (path == null) {
            setPropertyPath();
        }
        Properties prop = new Properties();
        if (System.getProperty("settings." + name) != null) return (System.getProperty("settings." + name));
        if (tryGetProp(prop, path)) return prop.getProperty(name);
        return null;
    }

    /**
     * @param prop
     * @param path
     * @return
     */
    public static boolean tryGetProp(Properties prop, String path) {
        FileInputStream fileInputStream = null;
        InputStreamReader reader = null;
        try {
            //обращаемся к файлу и получаем данные
            fileInputStream = new FileInputStream(path);
            reader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
            prop.load(reader);

            return true;
        } catch (IOException e) {
            AllureLogger.error("Файл " + path + " не обнаружен");
            AllureLogger.getStackTrace(e);
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                if (reader != null) {
                    reader.close();
                }
            } catch (NullPointerException | IOException e) {
                AllureLogger.getStackTrace(e);
            }
        }
        return false;
    }

    /**
     * @param name
     * @param def
     * @return
     */
    public static String loadPropertyOrDefault(String name, String def) {
        String variable = loadProperty(name);
        if (variable != null) {
            return variable;
        } else {
            return def;
        }
    }

    /**
     *
     */
    public static void setPropertyPath() {
        try {
            String environment = System.getProperty("settings.env");
            AllureLogger.info("ENVIRONMENT: " + environment);
            switch (environment) {
                case ("dev"):
                    path = PATH_TO_PROPERTIES_DEV;
                    break;
                case ("stg"):
                    path = PATH_TO_PROPERTIES_STG;
                    break;
                case ("uat"):
                    path = PATH_TO_PROPERTIES_UAT;
                    break;
                default:
                    path = PATH_TO_PROPERTIES_DEV;
            }
        } catch (NullPointerException e) {
            path = PATH_TO_PROPERTIES_DEV;
            AllureLogger.info("ENVIRONMENT выбран по умолчанию (dev)");
        }
    }
}
