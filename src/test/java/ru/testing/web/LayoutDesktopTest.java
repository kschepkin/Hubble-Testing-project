package ru.testing.web;

import controllers.BaseDesktopTest;
import controllers.GalenController;
import io.qameta.allure.Link;
import io.qameta.allure.Owner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.testing.web.pages.OrderHistoryPage;

import static com.codeborne.selenide.Selenide.open;
import static controllers.PropertyLoader.*;

@DisplayName("Проверка спецификаций для страниц сайта")
public class LayoutDesktopTest extends BaseDesktopTest {
    GalenController galenController = new GalenController();

    @BeforeEach
    public void getBase() throws InterruptedException {
        setup();
        open(BASE_URL);
        addCookies();
    }

    @DisplayName("Ошибка 404")
    @Link(value = " Личный кабинет ", url = "https://--")
    @Test
    public void error404Test() {
        open(BASE_URL + "/123");
        GalenController galenController = new GalenController();
        galenController.compareCurrentPageWithSpec("404.spec", "desktop");
    }

    @DisplayName("Проверка хедера на соответствие спецификации")
    @Link(value = "Личный кабинет ", url = "https://--")
    @Test
    public void headerTest() {
        closeAutodetectedRegion();
        basePage.categoryMenu.get(0).hover();
        galenController.compareCurrentPageWithSpec("header.spec", "desktop");
    }



}
