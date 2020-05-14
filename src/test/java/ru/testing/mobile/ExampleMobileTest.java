package ru.testing.mobile;

import controllers.BaseMobileTest;
import io.qameta.allure.Owner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;

@DisplayName("Шаблон для мобильной версии")
public class ExampleMobileTest extends BaseMobileTest {

    @BeforeEach
    public void getBase() {
        open(BASE_URL);
        addCookies();
    }
    @Owner(value = "name")
    @DisplayName("title")
    @Test
    public void exampleTest() {

    }
}