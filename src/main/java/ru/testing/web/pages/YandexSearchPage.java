package ru.testing.web.pages;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;
import static controllers.BaseTest.waitChangeElementProperty;

public class YandexSearchPage extends BasePage {

    public YandexSearchPage() {
        super();
    }

    public final SelenideElement yandexSearchField = $(By.id("text"));
    public final SelenideElement yandexSearchButton= $(By.cssSelector(".button_theme_websearch"));
    public final SelenideElement yandexSearchRequest = $(By.xpath(".//*[@aria-label='Запрос']"));

    public void searchByTextInYandexPage(String requestText) {
        yandexSearchField.click();
        yandexSearchField.setValue(requestText);
        yandexSearchButton.click();
        waitChangeElementProperty(yandexSearchRequest, "value", requestText);
    }
}
