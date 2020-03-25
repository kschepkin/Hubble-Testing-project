package controllers;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.logevents.SelenideLogger;
import com.google.common.collect.ImmutableMap;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.PerformsTouchActions;
import io.appium.java_client.TouchAction;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import io.qameta.allure.Step;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.testing.mobile.web.pages.*;
import ru.testing.web.pages.YandexPaymentPages;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.sleep;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static controllers.PropertyLoader.loadProperty;
import static controllers.PropertyLoader.loadPropertyOrDefault;
import static data.Constants.DEFAULT_TIMEOUT;
import static data.Constants.TITTLE_HUBBLE;
import static helpers.HacHelper.setExpertSenderStatus;
import static helpers.ListHelper.clickRandomListElementByMaxCondition;
import static helpers.ListHelper.getRandomElementFromList;
import static io.appium.java_client.remote.MobileCapabilityType.*;
import static org.openqa.selenium.remote.CapabilityType.BROWSER_NAME;
import static org.openqa.selenium.remote.CapabilityType.PLATFORM_NAME;

public class BaseMobileTest extends BaseTest {
    private static AppiumDriver driver;
    private TouchAction _touchAction;
    protected AuthMobilePage authMobilePage = new AuthMobilePage();
    protected BaseMobilePage baseMobilePage = new BaseMobilePage();
    protected CartMobilePage cartMobilePage = new CartMobilePage();
    protected SocialNetworksMobilePage socialNetworksMobilePage = new SocialNetworksMobilePage();
    protected ListingMobilePage listingMobilePage = new ListingMobilePage();
    protected ProductMobilePage productMobilePage = new ProductMobilePage();
    protected OrderHistoryMobilePage orderHistoryMobilePage = new OrderHistoryMobilePage();
    protected ThankYouMobilePage thankYouMobilePage = new ThankYouMobilePage();
    protected CheckoutMobilePage checkoutMobilePage = new CheckoutMobilePage();
    protected YandexPaymentPages yandexPaymentPage = new YandexPaymentPages();
    protected ProfileMobilePage profileMobilePage = new ProfileMobilePage();
    protected OrderMobilePage orderMobilePage = new OrderMobilePage();
    protected ResetPasswordMobilePage resetPasswordMobilePage = new ResetPasswordMobilePage();
    protected SearchMobilePage searchMobilePage = new SearchMobilePage();
    protected SearchResultMobilePage searchResultMobilePage = new SearchResultMobilePage();
    protected WishlistMobilePage wishlistMobilePage = new WishlistMobilePage();

    @BeforeAll
    public static void setUp() throws MalformedURLException {
        AppiumServerController.startAppiumServer();

        // Установка capabilities для устройств. Оставил часть для примера
        DesiredCapabilities capabilities = new DesiredCapabilities();
        switch (Objects.requireNonNull(loadProperty("devicePlatform"))) {
            case "Android":
                capabilities.setCapability(PLATFORM_NAME, "Android");
                capabilities.setCapability(PLATFORM_VERSION, "9.0");
                capabilities.setCapability(DEVICE_NAME, "Pixel");
                capabilities.setCapability("avd", "Pixel");
                capabilities.setCapability(BROWSER_NAME, "Chrome");
                capabilities.setCapability(AUTOMATION_NAME, "Appium");
                capabilities.setCapability("avdArgs", "-writable-system");
                capabilities.setCapability("appium:chromeOptions", ImmutableMap.of("w3c", false));
                capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 120);
                break;
            case "iphone7":
                capabilities.setCapability("platformName", "ios");
                capabilities.setCapability("platformVersion", "13.3");
                capabilities.setCapability("browserName", "Safari");
                capabilities.setCapability("deviceName", "iPhone (Gloria)");
                capabilities.setCapability("automationName", "XCUITest");
                capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 120);
                capabilities.setCapability("xcodeSigningId", "iPhone Developer");
                break;
            default:
                capabilities.setCapability("platformName", "ios");
                capabilities.setCapability("platformVersion", "13.3");
                capabilities.setCapability("browserName", "Safari");
                capabilities.setCapability("deviceName", "iPhone 8");
                capabilities.setCapability("automationName", "XCUITest");
                capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 120);
                break;
        }

        //setup the web driver and launch the webview app.
        URL url = new URL("http://127.0.0.1:4723/wd/hub");
        driver = new AppiumDriver(url, capabilities);
        WebDriverRunner.setWebDriver(driver);
        driver.manage().timeouts().implicitlyWait(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS);
        System.out.println(TITTLE_HUBBLE);
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide().screenshots(true).savePageSource(false));
        setExpertSenderStatus(true);
        if (!isIOS()) closeSelectSearchEngineChromePopup();
    }

    public static void clickUntilResult(SelenideElement clickableElement, SelenideElement expectedVisibleElement) {
        clickableElement.shouldBe(visible);
        clickableElement.click();
        sleep(200);
        try {
            expectedVisibleElement.toWebElement().isDisplayed();
        } catch (AssertionError | Exception e) {
            clickableElement.click();
        }
        expectedVisibleElement.shouldBe(visible);
    }

    @AfterAll
    public static void tearDown() {
        AppiumServerController.stopAppiumServer();
        Selenide.close();
        setExpertSenderStatus(false);
        driver.quit();
    }


    @AfterEach
    public void clearCookie() {
        sleep(2000);
        clearBrowserCookies();
    }

    protected static boolean isIOS() {
        return driver.getCapabilities().getPlatform().name().equals("MAC");
    }

    private static void closeSelectSearchEngineChromePopup() {
        driver.context("NATIVE_APP");
        $(By.xpath(".//android.widget.RadioButton[1]")).click();
        $(By.id("button_primary")).click();
        driver.context(("CHROMIUM"));
    }



    protected boolean checkIfPopupWindowClosed() {
        try {
            return (new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
                @Override
                public Boolean apply(WebDriver d) {
                    return d.getWindowHandles().size() < 2;
                }
            });
        } catch (TimeoutException e) {
            return false;
        }
    }

    private TouchAction touchAction() {
        if (_touchAction == null) {
            _touchAction = new TouchAction((PerformsTouchActions) getWebDriver());
        }
        return _touchAction;
    }

    protected void swipeElementWithDirection(MobileElement element, Direction direction) {

        double endXPercen = 0.9;
        int startX, startY, endX, endY;

        int screenWidth = driver.manage().window().getSize().width;
        int screenHeight = driver.manage().window().getSize().height;
        int cssWidth = Integer.parseInt(((JavascriptExecutor) getWebDriver()).executeScript("return screen.availWidth").toString());
        int cssHeight = Integer.parseInt(((JavascriptExecutor) getWebDriver()).executeScript("return screen.availHeight").toString());
        double xRatio = (double) screenWidth / (double) cssWidth;
        double yRatio = (double) screenHeight / (double) cssHeight;

        switch (direction) {
            case TOP:
                startX = element.getCenter().getX();
                startY = element.getSize().getHeight();

                endX = startX;
                endY = cssHeight - (int) (cssHeight * endXPercen);
                break;
            case RIGHT:
                startX = cssWidth - (int) (cssWidth * endXPercen);
                startY = element.getCenter().getY();

                endX = (int) (cssWidth * endXPercen);
                endY = startY;
                break;
            case LEFT:
                startX = (int) (cssWidth * endXPercen);
                startY = element.getCenter().getY();

                endX = cssWidth - (int) (cssWidth * endXPercen);
                endY = startY;
                break;

            default:
                startX = element.getCenter().getX();
                startY = element.getLocation().getY();

                endX = startX;
                endY = (int) (cssHeight * endXPercen);
                break;
        }
        Point startPoint = new Point((int) (startX * xRatio), (int) (startY * yRatio));
        PointOption startPointOption = new PointOption().withCoordinates(startPoint);

        Point endPoint = new Point((int) (endX * xRatio), (int) (endY * yRatio));
        PointOption endPointOption = new PointOption().withCoordinates(endPoint);

        WaitOptions waitOptions = new WaitOptions().withDuration(Duration.ofMillis(50));

        touchAction().press(startPointOption).waitAction(waitOptions).moveTo(endPointOption).waitAction().release().perform();
    }

    public enum Direction {
        TOP, RIGHT, BOTTOM, LEFT
    }



    private void scrollDown() {

        //The viewing size of the device
        Dimension size = driver.manage().window().getSize();

        //x position set to mid-screen horizontally
        int width = size.width / 2;

        //Starting y location set to 80% of the height (near bottom)
        int startPoint = (int) (size.getHeight() * 0.75);

        //Ending y location set to 20% of the height (near top)
        int endPoint = (int) (size.getHeight() * 0.25);

        new TouchAction(driver).press(PointOption.point(width, startPoint)).waitAction(WaitOptions.waitOptions(Duration.ofMillis(1000))).moveTo(PointOption.point(width, endPoint)).release().perform();

    }

    public void clickWithScroll(SelenideElement element) {
        for (int i = 0; i < 5; i++) {
            try {
                element.click();
                break;
            } catch (Exception e) {
                scrollDown();
            }
        }
    }
}
