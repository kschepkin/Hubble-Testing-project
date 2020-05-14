package controllers;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.github.bonigarcia.wdm.DriverManagerType;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import ru.testing.web.pages.BasePage;
import ru.testing.web.pages.YandexSearchPage;

import java.util.Random;

import static com.codeborne.selenide.Selenide.clearBrowserLocalStorage;
import static data.Constants.TITTLE_HUBBLE;

public class BaseDesktopTest extends BaseTest {
    protected BasePage basePage = new BasePage();
    protected YandexSearchPage yandexSearchPage = new YandexSearchPage();
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
    }

    @AfterAll
    static void tearDown() {
        Selenide.closeWindow();
    }

    @AfterEach
    public void clean() {
        clearBrowserCookies();
        clearBrowserLocalStorage();
    }
}
