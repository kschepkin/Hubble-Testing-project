package controllers;

import static helpers.TestDataHelper.getRandomEnString;

public class TestData {

    /**
     * генерация случайного адрес электронной почты пользователя от заданной почты
     *
     * @param mail почта к которой добавляются символы после +
     * @return
     */
    public static String getUserMailPlusRandom(String mail) {
        String[] retval = mail.split("@", 2);

        return retval[0] + "+" + getRandomEnString(5) + "@" + retval[1];
    }

    /**
     * генерация случайного адреса электронной почты
     *
     * @return
     */
    public String getRandomEmail() {
        String charSeq = getRandomEnString(5);
        String charSeq2 = getRandomEnString(5);

        return charSeq + "@" + charSeq2 + ".autotested.nn";
    }
}
