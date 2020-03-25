package controllers;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.github.bonigarcia.wdm.DriverManagerType;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.Step;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import ru.testing.web.pages.*;

import java.util.Random;

import static com.codeborne.selenide.Condition.not;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.clearBrowserLocalStorage;
import static controllers.UrlController.addInStockFilter;
import static data.Constants.TITTLE_HUBBLE;
import static helpers.HacHelper.setExpertSenderStatus;
import static helpers.ListHelper.clickRandomListElementByMaxCondition;
import static helpers.ListHelper.getRandomElementFromList;

public class BaseDesktopTest extends BaseTest {
    protected BasePage basePage = new BasePage();
    protected AuthPage authPage = new AuthPage();
    protected CartPage cartPage = new CartPage();
    protected SocialNetworksPages socialNetworksPages = new SocialNetworksPages();

    protected Random random = new Random();

    @BeforeAll
    public static void setup() {
        Configuration.holdBrowserOpen = false;
        Configuration.pollingInterval = 50;
        Configuration.timeout = 15000;
        Configuration.versatileSetValue = true;
        Configuration.driverManagerEnabled = true;
        WebDriverManager.getInstance(DriverManagerType.CHROME).setup();
        Configuration.startMaximized = true;
        System.out.println(TITTLE_HUBBLE);
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide().screenshots(true).savePageSource(false));
        setExpertSenderStatus(true);
    }

    @Step("Закрыть окно автоопределения региона")
    public void closeAutodetectedRegion() {
        waitElementsUntil(Condition.appear, basePage.saveAutodetectRegionButton);
        if (basePage.saveAutodetectRegionButton.isDisplayed()) {
            basePage.saveAutodetectRegionButton.click();
        }
        waitElementsUntil(not(Condition.appear), basePage.saveAutodetectRegionButton);
    }

    @Step("Проверка авторизации пользователя")
    public void checkUserAuthorization(String name, String secondName) {
        AuthPage authPage = new AuthPage();
        basePage.goToAuthPageBtn.shouldHave(text(name));
        authPage.modalUserMenu.hover();
        authPage.logoutBtn.shouldHave(text("Выйти"));
        AllureLogger.info("Авторизация выполнена успешно");
    }

    @AfterAll
    static void tearDown() {
        Selenide.close();
        setExpertSenderStatus(false);
    }


    @AfterEach
    public void clean() {
        clearBrowserCookies();
        clearBrowserLocalStorage();
    }

}
