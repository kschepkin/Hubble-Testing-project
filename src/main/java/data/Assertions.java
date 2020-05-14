package data;

public final class Assertions {

    //Текст ошибок
    public static final String EMAIL_ERROR_TEXT = "Ошибка";

    //Ассерты
    public static final String ASSERT_LOG_ERROR = "Текст элемента: [%s] не содержит в себе [%s]";
    public static final String ASSERT_LOG_SUCCESS = "Текст элемента: [%s] соответствует ожидаемому: [%s]";

    private Assertions() {
        throw new IllegalStateException("Utility class");
    }
}