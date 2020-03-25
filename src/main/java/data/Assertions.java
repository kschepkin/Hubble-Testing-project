package data;

public final class Assertions {



    //    Текст ошибок
    public static final String EMAIL_ERROR_TEXT = "Пожалуйста, введите корректный адрес электронной почты.";
    public static final String EMAIL_ERROR_BIG_TEXT = "Слишком длинное значение. Допустимая длина не больше 120 знаков";
    public static final String PASSWORD_ERROR_BIG_TEXT = "Слишком длинное значение. Допустимая длина не больше 120 знаков";
    public static final String NAME_ERROR_BIG_TEXT = "Слишком длинное значение. Допустимая длина не больше 32 знаков.";
    public static final String FIELD_WRONG_SYMBOLS = "Поле содержит недопустимые символы.";
    public static final String EMPTY_FIELD_ERROR_TEXT = "Поле обязательно для заполнения.";
    public static final String WRONG_SIMBOLS_ERROR_TEXT = "Поле содержит недопустимые символы.";
    public static final String WRONG_PHONE_ERROR_TEXT = "Пользователь с таким номером телефона не зарегистрирован на сайте";
    public static final String LOGIN_FORM_ERROR_TEXT = "Неверное имя пользователя или пароль.";
    public static final String PASSWORD_ERROR_TEXT = "Пароль должен быть не менее 6 символов и содержать латинские буквы.";
    public static final String IN_CASH_RADIOBTN_TEXT = "При получении, наличными или картой";
    public static final String BY_CARD_RADIOBTN_TEXT = "Онлайн, картой";
    public static final String NOT_EQUAL_PASSWORDS = "Пароли не совпадают.";
    public static final String INCORRECT_PHONE_FORMAT = "Формат телефона некорректен.";
    public static final String USER_WITH_PHONE_ALREADY_EXIST = "Пользователь с таким номером уже зарегистрирован\n" +
            "Если это вы, то войдите в Личный кабинет. Если это не вы, то воспользуйтесь другим способом регистрации";
    public static final String WRONG_PHONE_CODE = "Неверный код. Введите код еще раз или запросите новый";
    public static final String USER_WITH_EMAIL_ALREADY_EXIST = "Пользователь с таким email уже зарегистрирован\n" +
            "Если это вы, то войдите в Личный кабинет. Если это не вы, то воспользуйтесь другим способом регистрации";



    private Assertions() {
        throw new IllegalStateException("Utility class");
    }
}