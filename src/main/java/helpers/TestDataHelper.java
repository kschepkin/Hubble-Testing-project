package helpers;

import controllers.AllureLogger;
import io.qameta.allure.Step;

import java.util.Random;

public class TestDataHelper {

    private static Random random = new Random();

    /**
     * Получить рандомную строку
     *
     * @param stringLength длина рандомной строки
     * @return
     */
    @Step("Получение строки случайных символов")
    public static String getRandomEnString(int stringLength) {
        StringBuilder builder = new StringBuilder();
        String randomString;

        for (int i = 0; i < stringLength; i++) {
            char symbol = getRandomEnChar();
            builder.append(symbol);
        }
        randomString = builder.toString();
        AllureLogger.info("Случайная строка: " + randomString);
        return randomString;
    }

    /**
     * @return
     */
    private static char getRandomEnChar() {
        char randomChar;

        randomChar = (char) (97 + getRandomInt(26));
        return randomChar;
    }

    /**
     * @return
     */
    public static int getRandomInt(int max) {
        return (TestDataHelper.random.nextInt(max));
    }

    /**
     * @return
     */
    @Step("Получение последовательности случайных чисел")
    public static String getRandomNumString(int qty) {
        StringBuilder bld = new StringBuilder();

        for (int i = 0; i < qty; i++) {
            bld.append(getRandomInt(9));
        }
        AllureLogger.debug("Получена последовательность чисел: " + bld.toString());
        return bld.toString();
    }
}
