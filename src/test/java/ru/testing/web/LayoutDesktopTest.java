package ru.testing.web;

import controllers.BaseDesktopTest;
import controllers.GalenController;
import io.qameta.allure.Link;
import io.qameta.allure.Owner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;
import static data.Constants.ERROR404_URL;

@DisplayName("Проверка спецификаций для страниц сайта")
public class LayoutDesktopTest extends BaseDesktopTest {
    GalenController galenController = new GalenController();
    static final String DESKTOP = "desktop";

    @BeforeEach
    public void getBase() throws InterruptedException {
        setup();
        open(BASE_URL);
        addCookies();
    }

    @Owner(value = "Автор теста")
    @DisplayName("Ошибка 404")
    @Link(value = "номер кейса", url = "https://ссылка")
    @Test
    public void error404Test() {
        open(BASE_URL + ERROR404_URL);
        galenController.compareCurrentPageWithSpec("404.spec", DESKTOP);
    }
}
