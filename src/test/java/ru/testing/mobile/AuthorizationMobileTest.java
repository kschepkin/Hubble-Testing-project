package ru.testing.mobile;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import controllers.AllureLogger;
import controllers.BaseMobileTest;
import io.qameta.allure.Link;
import io.qameta.allure.Owner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;
import static controllers.PropertyLoader.*;
import static controllers.TestData.getUserMailPlusRandom;

@DisplayName("Авторизация в мобильной версии")
public class AuthorizationMobileTest extends BaseMobileTest {

    @BeforeEach
    public void getBase() {
        open(BASE_URL);
        addCookies();
    }

    @DisplayName("Авторизация по номеру телефона")
    @Link(value = "ЮС CA18 Авторизация через номер телефона и код", url = "https://---")
    @Test
    public void phoneAuthTest() {
        String name = loadProperty(CURRENT_USER);
        String phone = loadProperty(name + PHONE);

        baseMobilePage.getAuthPage();
        authMobilePage.getPhoneAuthForm();
        authMobilePage.authByPhone(phone);
        baseMobilePage.checkAuthorization(name);
    }

    @DisplayName("Некорректная авторизация с главной страницы")
    @Test
    public void mainPageWrongAuthorizationTest() {
        baseMobilePage.getAuthPage();
        authMobilePage.checkIncorrectAuth();
    }

    @DisplayName("Авторизация с чекаута")
    @Test
    public void checkoutAuthorizationTest() {
        String name = loadProperty(CURRENT_USER);
        String mail = getUserMailPlusRandom(loadProperty(name + EMAIL));
        String password = loadProperty(name + PASSWORD);

        baseMobilePage.getAuthPage();
        authMobilePage.getRegForm();
        authMobilePage.submitRegForm(name, mail, password);
        clearCookiesAndRefresh();

        goToCartWithSeveralProducts(2);
        waitElementsUntil(Condition.enabled, cartMobilePage.checkoutButton);
        cartMobilePage.goToCheckoutRegistrationPage();
        authMobilePage.getEmailAuthForm();
        authMobilePage.tryAuthorizeUserByMail(mail, password, true);
        baseMobilePage.checkAuthorization(name);
    }

    @DisplayName("Некорректная авторизация с чекаута")
    @Test
    public void checkoutWrongAuthorizationTest() {
        goToCartWithSeveralProducts(2);
        waitElementsUntil(Condition.enabled, cartMobilePage.checkoutButton);
        cartMobilePage.goToCheckoutRegistrationPage();
        authMobilePage.getEmailAuthForm();
        authMobilePage.checkIncorrectAuth();
    }


}