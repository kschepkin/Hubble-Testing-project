package ru.testing.web;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import controllers.AllureLogger;
import controllers.BaseDesktopTest;
import io.qameta.allure.Link;
import io.qameta.allure.Owner;
import io.qameta.allure.Step;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.testing.web.pages.CartPage;

import static com.codeborne.selenide.Selenide.clearBrowserLocalStorage;
import static com.codeborne.selenide.Selenide.open;
import static controllers.PropertyLoader.*;
import static controllers.TestData.getUserMailPlusRandom;

@DisplayName("Авторизация")
public class AuthDesktopTest extends BaseDesktopTest {

    @BeforeEach
    public void getBase() {
        setup();
        open(BASE_URL);
        addCookies();
    }

    @Link(value = "Регистрация/авторизация в всплывающем окне", url = "https://--")
    @Test
    @DisplayName("Авторизация с главной страницы")
    @Step("Авторизация пользователя с главной страницы")
    public void mainPageAuthorizationTest() {
        String name = loadProperty(CURRENT_USER);
        String mail = getUserMailPlusRandom(loadProperty(name + EMAIL));
        String password = loadProperty(name + PASSWORD);

        closeAutodetectedRegion();
        authPage.registerNewUser(name, mail, password);
        clearCookiesAndRefresh();
        closeAutodetectedRegion();
        basePage.goToAuthPage();
        authPage.tryAuthorizeUserByMail(mail, password, true);
        checkUserAuthorization(name, "");
    }


    @DisplayName("Авторизация по номеру телефона")
    @Link(value = "Авторизация через номер телефона и код", url = "https://--")
    @Test
    public void mainPagePhoneAuthorizationTest() {
        String name = loadProperty(CURRENT_USER);
        String secondName = loadProperty(name + SECOND_NAME);
        String phone = loadProperty(name + PHONE);

        closeAutodetectedRegion();
        basePage.goToAuthPage();
        authPage.getPhoneAuthForm();
        authPage.authByPhone(phone);
        checkUserAuthorization(name, secondName);
    }

    @DisplayName("Авторизация с чекаута")
    @Test
    public void checkoutAuthorizationTest() {
        CartPage cartPage = new CartPage();
        String name = loadProperty(CURRENT_USER);
        String mail = getUserMailPlusRandom(loadProperty(name + EMAIL));
        String password = loadProperty(name + PASSWORD);

        closeAutodetectedRegion();
        authPage.registerNewUser(name, mail, password);
        clearCookiesAndRefresh();
        closeAutodetectedRegion();
        goToCartWithSeveralProducts(2);
        waitElementsUntil(Condition.enabled, cartPage.checkoutButton);
        cartPage.goToCheckoutRegistrationPage();
        authPage.getEmailAuthForm();
        authPage.tryAuthorizeUserByMail(mail, password, true);
        checkUserAuthorization(name, "");
    }

    @DisplayName("Некорректная авторизация с главной страницы")
    @Test
    public void mainPageWrongAuthorizationTest() {
        closeAutodetectedRegion();
        basePage.goToAuthPage();
        authPage.checkIncorrectAuth();
    }

    @DisplayName("Некорректная авторизация на чекауте")
    @Test
    public void checkoutWrongAuthorizationTest() {
        CartPage cartPage = new CartPage();
        closeAutodetectedRegion();
        goToCartWithSeveralProducts(2);
        cartPage.goToCheckoutRegistrationPage();
        authPage.getEmailAuthForm();
        authPage.checkIncorrectAuth();
    }

    @AfterEach
    public void testStop() {
        clearBrowserCookies();
        clearBrowserLocalStorage();
    }
}
