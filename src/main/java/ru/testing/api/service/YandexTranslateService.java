package ru.testing.api.service;

import controllers.AllureLogger;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import ru.testing.api.entities.YandexTranslateResponse;

import static controllers.PropertyLoader.loadPropertyOrDefault;
import static data.Constants.TRANSLATE_URL;
import static io.restassured.RestAssured.*;

public class YandexTranslateService {
    private static final String BASE_PATH = loadPropertyOrDefault("backUrl", "https://translate.yandex.net");

    @Step("Получение перевода")
    public static YandexTranslateResponse yandexTranslate(String text, String lang) {
        AllureLogger.info("Получение перевода");
        RestAssured.basePath = BASE_PATH;
        Response response = given()
                .param("key", "trnsl.1.1.20190719T142310Z.7e07444177f2a1f1.4c77c7cedd23634966b3bc7a8aa113ceab856777")
                .param("text", text)
                .param("lang", lang)
                .log()
                .all()
                .get(BASE_PATH + TRANSLATE_URL)
                .then()
                .log()
                .all()
                .extract().response();

        Allure.addAttachment("Response", response.asString());
        return response.body().as(YandexTranslateResponse.class);
    }


}