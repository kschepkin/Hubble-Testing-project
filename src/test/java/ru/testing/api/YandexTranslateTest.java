package ru.testing.api;

import controllers.BaseApiTest;
import io.qameta.allure.Link;
import io.qameta.allure.Owner;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.testing.api.entities.YandexTranslateResponse;
import ru.testing.api.service.YandexTranslateService;

@DisplayName("API: Яндекс Перевод")
public class YandexTranslateTest extends BaseApiTest {
    public YandexTranslateService yandexTranslateService = new YandexTranslateService();

    @BeforeEach
    public void getBase() {
        setup();
    }

    @Test
    @Owner("Автор")
    @Link(value = "ЮС перевод текста", url = "https://ссылка")
    @DisplayName("Проверка получения перевода")
    public void yandexTranslate() {
        String text = "Hello world";
        String lang = "en-ru";
        String expectResult = "Привет мир";

        YandexTranslateResponse yandexTranslateResponse = yandexTranslateService.yandexTranslate(text, lang);
        Assertions.assertNotNull(yandexTranslateResponse.getCode());
        Assertions.assertEquals(200, yandexTranslateResponse.getCode());
        Assertions.assertEquals(expectResult, yandexTranslateResponse.getText().get(0));
        System.out.println("Перевод: "  + yandexTranslateResponse.getText().get(0));
    }
}