package ru.testing.api;

import controllers.BaseApiTest;
import io.qameta.allure.Link;
import io.qameta.allure.Owner;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.testing.api.entities.authorization.AuthorizationResponse;
import ru.testing.api.service.AuthorizationService;

@DisplayName("API: Авторизация/получение токена")
public class AuthorizationTest extends BaseApiTest {
    public AuthorizationService authService = new AuthorizationService();

    @BeforeEach
    public void getBase() {
        setup();
    }

    @Test
    @Link(value = " получение/обновление токена", url = "https://--")
    @DisplayName("Проверка получения токена")
    public void authorization() {
        AuthorizationResponse authorizationResponse = authService.getNewToken();
        Assertions.assertNotNull(authorizationResponse.getAccess_token());
    }
}