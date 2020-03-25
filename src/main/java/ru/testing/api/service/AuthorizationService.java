package ru.testing.api.service;

import controllers.AllureLogger;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import ru.testing.api.entities.authorization.AuthorizationResponse;

import static controllers.PropertyLoader.loadProperty;
import static controllers.PropertyLoader.loadPropertyOrDefault;
import static data.Constants.OAUTH_URL;
import static io.restassured.RestAssured.given;

public class AuthorizationService {
    private static String token;
    private static final String BASE_PATH = loadPropertyOrDefault("backUrl", "https://ya.ru");

    @Step("Получение токена авторизации")
    public static AuthorizationResponse getNewToken() {
        AllureLogger.info("Получение токена авторизации");
        RestAssured.basePath = BASE_PATH;
        Response response = given()
                // приватно
                .post(BASE_PATH + OAUTH_URL)
                .then()
                .extract().response();

        AuthorizationResponse parsedResponse = response.body().as(AuthorizationResponse.class);
        token = parsedResponse.getAccess_token();
        AllureLogger.info("Получен токен: " + token);
        Allure.addAttachment("Response", response.body().asString());
        return parsedResponse;
    }

    public static String getToken() {
        //приватно
    }
}