package controllers;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.open;
import static controllers.BaseTest.waitForLoadPage;
import static controllers.PropertyLoader.loadPropertyOrDefault;

public class UrlController {
    public static final String BASE_URL = loadPropertyOrDefault("baseUrl", "https://ya.ru");

    private UrlController() {
        throw new IllegalStateException("Utility class");
    }

    @Step("Переходим по ссылке и после загрузки страницы меняем URL на текущий стенд")
    public static void urlReplacer(String url) {
        open(url);
        open(WebDriverRunner.url().replace("https://www.gloria-jeans.ru/", BASE_URL));
    }


}
