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
    private static final String PATH_TO_PROPERTIES_D1 = "src/test/resources/d1/Application.properties";
    private static final String PATH_TO_PROPERTIES_S1 = "src/test/resources/s1/Application.properties";
    private static String path = null;
    public static final String NAME = "name";
    public static final String EMAIL = ".email";
    public static final String PASSWORD = ".password";
    public static final String PHONE = ".phone";
    public static final String FIRST_NAME = ".firstName";
    public static final String SECOND_NAME = ".secondName";
    public static final String MIDDLE_NAME = ".middleName";
    public static final String FACEBOOK_MAIL = ".facebookmail";
    public static final String FACEBOOK_PASSWORD = ".facebookpassword";
    public static final String YANDEX_MAIL = ".yandexmail";
    public static final String YANDEX_PASSWORD = ".yandexpassword";
    public static final String MAILRU_MAIL = ".mailrumail";
    public static final String MAILRU_PASSWORD = ".mailrupassword";
    public static final String MAIL_LOGIN = ".maillogin";
    public static final String MAIL_PASSWORD = ".mailpassword";
    public static final String CURRENT_USER = "currentUser";
    public static final String CORRECT_CARD_NUMBER = "correctCardNumber";
    public static final String INCORRECT_CARD_NUMBER = "incorrectCardNumber";
    public static final String EXPIRE_MONTH = "expireMonth";
    public static final String EXPIRE_YEAR = "expireYear";
    public static final String ADDRESS = ".address";
    public static final String APARTMENT = ".apartment";
    public static final String ADDRESS2 = ".address2";
    public static final String APARTMENT2 = ".apartment2";
    public static final String CHECK_EMAIL = "checkEmail";

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
                    path = PATH_TO_PROPERTIES_UAT;
            }
        } catch (NullPointerException e) {
            path = PATH_TO_PROPERTIES_S1;
            AllureLogger.info("ENVIRONMENT выбран по умолчанию (uat)");
        }
    }
}
