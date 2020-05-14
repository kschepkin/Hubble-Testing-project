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
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.testing.mobile.web.pages.BaseMobilePage;
import ru.testing.mobile.web.pages.YandexSearchMobilePage;

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
import static data.Constants.DEFAULT_TIMEOUT;
import static data.Constants.TITTLE_HUBBLE;
import static io.appium.java_client.remote.AndroidMobileCapabilityType.AVD_ARGS;
import static io.appium.java_client.remote.AndroidMobileCapabilityType.CHROME_OPTIONS;
import static io.appium.java_client.remote.IOSMobileCapabilityType.XCODE_ORG_ID;
import static io.appium.java_client.remote.IOSMobileCapabilityType.XCODE_SIGNING_ID;
import static io.appium.java_client.remote.MobileCapabilityType.*;
import static org.openqa.selenium.remote.CapabilityType.BROWSER_NAME;
import static org.openqa.selenium.remote.CapabilityType.PLATFORM_NAME;

public class BaseMobileTest extends BaseTest {
    private static AppiumDriver driver;
    static DesiredCapabilities capabilities = new DesiredCapabilities();
    private TouchAction touchAction;
    protected YandexSearchMobilePage yandexSearchMobilePage = new YandexSearchMobilePage();
    protected BaseMobilePage baseMobilePage = new BaseMobilePage();

    @BeforeAll
    public static void setUp() throws MalformedURLException {
        AppiumServerController.startAppiumServer();

        // Set capabilities for devices
        switch (Objects.requireNonNull(loadProperty("devicePlatform"))) {
            case "Android":
                setAndroidBaseCapabilities("9.0", "Pixel");
                capabilities.setCapability(AVD_ARGS, "-writable-system");
                break;
            case "iphone7":
                setIOSBaseCapabilities("13.3", "iPhone (Gloria)", "set_UID_here");
                break;
            default:
                setIOSBaseCapabilities("13.3", "iPhone 8", "");
                break;
        }

        //setup the web driver and launch the webview app.
        URL url = new URL("http://127.0.0.1:4723/wd/hub");
        driver = new AppiumDriver(url, capabilities);
        WebDriverRunner.setWebDriver(driver);
        driver.manage().timeouts().implicitlyWait(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS);
        System.out.println(TITTLE_HUBBLE);
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide().screenshots(true).savePageSource(false));
        if (!isIOS()) closeSelectSearchEngineChromePopup();
    }

    public static void setAndroidBaseCapabilities(String platformVersion, String deviceName) {
        capabilities.setCapability(PLATFORM_NAME, "Android");
        capabilities.setCapability(BROWSER_NAME, "Chrome");
        capabilities.setCapability(AUTOMATION_NAME, "Appium");
        capabilities.setCapability(CHROME_OPTIONS, ImmutableMap.of("w3c", false));
        capabilities.setCapability(NEW_COMMAND_TIMEOUT, 120);
        capabilities.setCapability(PLATFORM_VERSION, platformVersion);
        capabilities.setCapability(DEVICE_NAME, deviceName);
    }

    public static void setIOSBaseCapabilities(String platformVersion, String deviceName, String udid) {
        capabilities.setCapability(PLATFORM_NAME, "ios");
        capabilities.setCapability(PLATFORM_VERSION, platformVersion);
        capabilities.setCapability(BROWSER_NAME, "Safari");
        capabilities.setCapability(DEVICE_NAME, deviceName);
        capabilities.setCapability(AUTOMATION_NAME, "XCUITest");
        capabilities.setCapability(UDID, udid);
        capabilities.setCapability(XCODE_ORG_ID, "");
        capabilities.setCapability(NEW_COMMAND_TIMEOUT, 120);
        capabilities.setCapability(XCODE_SIGNING_ID, "iPhone Developer");
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
        Selenide.closeWindow();
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
        if (touchAction == null) {
            touchAction = new TouchAction((PerformsTouchActions) getWebDriver());
        }
        return touchAction;
    }

    protected void swipeElementWithDirection(MobileElement element, Direction direction) {
        double endXPercen = 0.9;
        int startX;
        int startY;
        int endX;
        int endY;
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

}
