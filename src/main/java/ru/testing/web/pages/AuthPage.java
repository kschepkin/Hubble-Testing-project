package ru.testing.web.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import controllers.AllureLogger;
import helpers.HacHelper;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static controllers.BaseDesktopTest.assertContainsText;
import static controllers.BaseDesktopTest.waitElementsUntil;
import static controllers.PropertyLoader.*;
import static controllers.TestData.getUserMailPlusRandom;
import static data.Assertions.*;
import static helpers.TestDataHelper.getRandomEnString;

public class AuthPage extends BasePage {

    public AuthPage() {
        super();
    }

    SocialNetworksPages socialNetworksPages = new SocialNetworksPages();
    //    Форма регистрации
    public final SelenideElement firstNameField = $(By.xpath(".//*[@data-qa='regFirstName']"));
    public final SelenideElement registrationEmailField = $(By.xpath(".//*[@data-qa='regEmail']"));
    public final SelenideElement registrationPasswordField = $(By.xpath(".//*[@data-qa='regPassword']"));
    public final SelenideElement registrationSubmitButton = $(By.xpath(".//*[@data-qa='regSubmitBtn']"));
    public final SelenideElement registrationGetPhoneFormButton = $(By.xpath(".//*[@data-qa='regGetPhoneForm']"));
    public final SelenideElement registrationGetEmailAuthFormButton = $(By.xpath(".//div[contains(@class, 'modal-pop-up')][@data-block-name='ingress']"));
    public final SelenideElement registrationShowPasswordEye = $(By.xpath(".//*[@data-qa='regShowPassword']"));
    public final SelenideElement registrationFieldError = $(By.xpath(".//*[@class='text-error js-text-error active']"));
    public final SelenideElement registrationFieldHeader = $(By.xpath(".//*[text() = 'Регистрация']"));
    public final SelenideElement regByEmailRadioButton = $(By.xpath(".//div[contains(@class, 'radio-button')][@data-block-name='registrationEmail']"));
    public final SelenideElement regByPhoneRadioButton = $(By.xpath(".//div[contains(@class, 'radio-button')][@data-block-name='registrationPhone']"));
    public final SelenideElement phoneRegistrationField = $(By.xpath(".//div[@data-block-name=\'registration\']//input[@name=\'cellphone\']"));
    public final SelenideElement phoneRegistrationError = $(By.xpath(".//div[@data-block-name='registration']//p[@data-qa='phoneAuthWrongError']"));
    public final SelenideElement phoneRegistrationSendCode = $(By.xpath(".//div[@data-block-name='registration']//button[@data-qa='phoneSendCode']"));
    public final SelenideElement phoneRegistrationCodeField = $(By.xpath(".//div[@data-block-name='registration']//input[@data-qa='phoneSmsCode']"));
    public final SelenideElement phoneRegistrationCodeError = $(By.xpath(".//form[@data-form-name='phoneFormRegistration']//div[@class='text-input js-code-input']//p[contains(@class,'text-error')]"));
    public final SelenideElement phoneRegistrationSubmitButton = $(By.xpath(".//div[@data-block-name='registration']//button[@data-qa='phoneSubmit']"));
    public final SelenideElement phoneRegistrationEmailField = $(By.xpath(".//*[@data-qa='regByPhoneEmail']"));
    public final SelenideElement phoneRegistrationFinishButton = $(By.xpath(".//*[text() = 'завершить регистрацию']"));
    public final SelenideElement phoneRegistrationEmailError = $(By.xpath(".//form[contains(@class,'js-enter-email-after-phone-registration-form')]//p[contains(@class,'text-error')]"));

    //    Форма авторизации по почте
    public final SelenideElement loginEmailField = $(By.xpath(".//*[@data-qa='loginEmail']"));
    public final SelenideElement loginPasswordField = $(By.xpath(".//*[@data-qa='loginPassword']"));
    public final SelenideElement loginButton = $(By.xpath(".//*[@data-qa='loginSubmit']"));
    public final SelenideElement loginForgotPasswordButton = $(By.xpath(".//div[@data-block-name=\"ingressEmail\"]//*[@data-block-name='forgot-password']"));
    public final SelenideElement loginShowPasswordEye = $(By.xpath(".//*[@data-qa='loginShowPassword']"));
    public final SelenideElement loginGetAuthorizationEmailFormRadioButton = $(By.xpath(".//div[@data-block-name=\"ingressEmail\"]//div[contains(@class,'radio-button')]"));
    public final SelenideElement loginGetAuthorizationPhoneFormRadioButton = $(By.xpath(".//div[@data-block-name=\"ingressPhone\"]//div[contains(@class,'radio-button')]"));
    public final SelenideElement loginGetRegistrationFormButton = $(By.xpath(".//div[contains(@class, 'modal-pop-up')][@data-block-name='registration']"));
    public final SelenideElement loginEmailFieldError = $(By.xpath(".//*[@data-qa='loginEmailError']"));
    public final SelenideElement loginPasswordFieldError = $(By.xpath(".//*[@data-qa='loginPasswordError']"));
    public final SelenideElement loginFormError = $(By.xpath(".//div[@data-block-name='ingress']//p[contains(@class,'js-error-block-form')]"));

    public final SelenideElement mobileLoginFormError = $(By.xpath(".//p[@class='js-error-in-button error-block mobile-error-block']"));


    //    Форма авторизации по телефону
    public final SelenideElement phoneNumberAuthField = $(By.xpath(".//*[@data-qa='phoneAuthField']"));
    public final SelenideElement phoneAuthSendCodeButton = $(By.xpath(".//*[@data-qa='phoneSendCode']"));
    public final SelenideElement phoneGetEmailAuthorizationFormButton = $(By.xpath(".//*[@data-qa='phoneGetEmailAuthForm']"));
    public final SelenideElement phoneGetRegistrationFormButton = $(By.xpath(".//*[@data-qa='phoneGetRegForm']"));
    public final SelenideElement smsCodeField = $(By.xpath(".//*[@data-qa='phoneSmsCode']"));
    public final SelenideElement resendSmsCodeButton = $(By.xpath(".//*[@data-qa='phoneResendSmsCode']"));
    public final SelenideElement phoneAuthSubmitButton = $(By.xpath(".//*[@data-qa='phoneSubmit']"));
    public final SelenideElement phoneAuthError = $(By.xpath(".//*[@data-qa='phoneAuthWrongError']"));
    public final ElementsCollection testCollection = $$(By.xpath(".//a[@data-dropdown='js-header-dropdown']"));

    //    Всплывающее меню пользователя
    public final SelenideElement modalUserMenu = $(By.xpath(".//*[@data-qa='account-link']"));
    public final SelenideElement logoutBtn = $(By.xpath(".//*[contains(@class,'modal-account')]/a[@href='/logout']"));


    //    Методы регистрации

    /**
     * Переходит к регистрации нажатием на кнопку войти и регистрирует нового пользователя
     *
     * @param name     Имя пользователя
     * @param mail     почта (не должна быть зарегистрирована на сайте)
     * @param password пароль (должен соотсветствовать проверки сложности)
     */
    @Step("Регистрация пользователя")
    public void registerNewUser(String name, String mail, String password) {
        AllureLogger.info("Регистрация пользователя: " + name + " - " + mail);
        BasePage basePage = new BasePage();
        basePage.goToAuthPage();
        getRegForm();
        submitRegForm(name, mail, password);
        basePage.goToAuthPageBtn.shouldHave(text(name));
        AllureLogger.info("Пользователь " + mail + " зарегистрирован");
    }

    /**
     * Заполняет попап регистрации на чекауте и нажимает кнопку подтвердить
     */
    @Step("Регистрация пользователя на чекауте")
    public void regUserFromCheckout(String mail) {
        registrationEmailField.setValue(mail);
        registrationSubmitButton.click();
        try {
            waitElementsUntil(not(visible), registrationSubmitButton);
        } catch (Throwable condition) {
            registrationSubmitButton.click();
        }
    }

    /**
     * Выполняет переход на форму регистрации с формы авторизации
     */
    @Step("Переход на форму регистрации")
    public void getRegForm() {
        loginGetRegistrationFormButton.click();
        regByEmailRadioButton.click();
        waitElementsUntil(visible, firstNameField);
        waitElementsUntil(visible, registrationEmailField);
    }

    /**
     * Заполняет поля, необходимые для регистрации пользователя и нажимает кнопку "зарегистрироваться"
     *
     * @param name     Имя пользователя
     * @param mail     почта (не должна быть зарегистрирована на сайте)
     * @param password пароль (должен соотсветствовать проверки сложности)
     */
    @Step("Заполнение полей регистрации пользователя")
    public void submitRegForm(String name, String mail, String password) {
        firstNameField.setValue(name);
        registrationEmailField.setValue(mail);
        registrationPasswordField.setValue(password);
        registrationSubmitButton.click();
        if (registrationEmailField.isDisplayed()) {
            registrationSubmitButton.click();
        }
        waitElementsUntil(not(visible), registrationSubmitButton);
        welcomePopupClose.click();
    }

    /**
     * Заполняет полей авторизации и нажатие кнопки подтвердить
     *
     * @param mail     Имя пользователя
     * @param password пароль
     */
    @Step("Авторизация при помощи адреса эл почты: {0}")
    public void tryAuthorizeUserByMail(String mail, String password, boolean assumeSuccess) {
        loginEmailField.setValue(mail);
        loginPasswordField.setValue(password);
        loginEmailField.click();
        waitElementsUntil(enabled, loginButton);
        sleep(1000);
        loginButton.click();
        if (assumeSuccess) waitElementsUntil(not(visible), loginGetRegistrationFormButton);
    }

    @Step("Переход на форму авторизации по номеру телефона")
    public void getPhoneAuthForm() {
        loginGetAuthorizationPhoneFormRadioButton.click();
        waitElementsUntil((appear), phoneNumberAuthField);
    }

    @Step("Переход на форму авторизации по email")
    public void getEmailAuthForm() {
        registrationGetEmailAuthFormButton.click();
        loginGetAuthorizationEmailFormRadioButton.click();
        waitElementsUntil(visible, loginEmailField);
    }

    @Step("Заполнение номера для авторизации по номеру телефона")
    public void sendSmsCodeToAuthByPhone(String phone) {
        phoneNumberAuthField.isDisplayed();
        phoneNumberAuthField.setValue(phone);
        sleep(1500);
        phoneAuthSendCodeButton.click();
    }

    @Step("Авторизация по номеру телефона")
    public void authByPhone(String phone) {
        sendSmsCodeToAuthByPhone(phone);
        waitElementsUntil((appear), smsCodeField);
        smsCodeField.setValue(HacHelper.getLastSmsCodeByPhone(phone));
        phoneAuthSubmitButton.click();
        AllureLogger.info("Пользователь авторизовался по номеру телефона: " + phone);
        waitElementsUntil(not(visible), smsCodeField);
    }

    /**
     * Нажимает кнопку "Забыли пароль"
     */
    @Step("Переход на страницу восстановления пароля")
    public void getPasswordRestoreForm() {
        loginForgotPasswordButton.click();
        waitElementsUntil(visible, forgotPassEmailField);
    }

    @Step("Проверка валидации при регистрации по телефону")
    public void phoneRegistration(String email, String phone) {
        getRegForm();
        regByPhoneRadioButton.click();
        phoneRegistrationField.setValue(phone);
        phoneRegistrationSendCode.click();
        phoneRegistrationCodeField.setValue(HacHelper.getLastSmsCodeByPhone(phone));
        phoneRegistrationSubmitButton.click();
        phoneRegistrationEmailField.setValue(email);
        phoneRegistrationFinishButton.click();
        waitElementsUntil(not(visible), phoneRegistrationFinishButton);
        welcomePopupClose.click();
    }
}