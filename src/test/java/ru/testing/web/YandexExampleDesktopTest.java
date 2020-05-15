package ru.testing.web;

import controllers.AllureLogger;
import controllers.BaseDesktopTest;
import io.qameta.allure.Link;
import io.qameta.allure.Owner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.sleep;

@DisplayName("Тест поиска")
public class YandexExampleDesktopTest extends BaseDesktopTest {

    @BeforeEach
    public void getBase() {
        setup();
        open(BASE_URL);
        addCookies();
    }

    @Owner(value = "Автор теста")
    @DisplayName("Пример теста яндекса")
    @Link(value = "Кейс", url = "https://")
    @Test
    public void yandexSearchTest() {
        String searchQuery = "котики";

        yandexSearchPage.yandexSearchField.click();
        yandexSearchPage.yandexSearchField.setValue(searchQuery);
        yandexSearchPage.yandexSearchButton.click();
        waitChangeElementProperty(yandexSearchPage.yandexSearchRequest, "value", searchQuery);
        AllureLogger.info("Инфо лог");
        sleep(1000);
    }

    @Owner(value = "Автор теста")
    @DisplayName("Пример теста яндекса")
    @Link(value = "Кейс", url = "https://")
    @Test
    public void yandexSearch2variantTest() {
        String searchQuery = "котики";

        yandexSearchPage.searchByTextInYandexPage(searchQuery);
        AllureLogger.debug("дебаг лог");
        sleep(1000);
    }
}