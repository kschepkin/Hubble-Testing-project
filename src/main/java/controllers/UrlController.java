package controllers;

import com.codeborne.selenide.WebDriverRunner;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.open;
import static controllers.PropertyLoader.loadPropertyOrDefault;

public class UrlController {
    public static final String BASE_URL = loadPropertyOrDefault("baseUrl", "https://yandex.ru");

    private UrlController() {
        throw new IllegalStateException("Utility class");
    }

    @Step("Переходим по ссылке и после загрузки страницы меняем URL на текущий стенд")
    public static void urlReplacer(String url) {
        open(url);
        open(WebDriverRunner.url().replace("https://www.yandex.ru/", BASE_URL));
    }
}
