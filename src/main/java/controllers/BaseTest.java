package controllers;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Arrays;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static controllers.PropertyLoader.loadProperty;
import static controllers.PropertyLoader.loadPropertyOrDefault;
import static data.Assertions.ASSERT_LOG_ERROR;
import static data.Assertions.ASSERT_LOG_SUCCESS;
import static data.Constants.DEFAULT_TIMEOUT;
import static java.lang.String.format;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsStringIgnoringCase;

public class BaseTest {
    private static WebDriver driver;
    public static final String BASE_URL = loadPropertyOrDefault("baseUrl", "https://ya.ru");

    /**
     * Создаем куку
     */
    public void replaceCookie(String cookieName, String cookieValue) {
        getWebDriver().manage().addCookie(new Cookie(cookieName, cookieValue));
    }

    /**
     * Clear browser cookies.
     * <p>
     * It can be useful e.g. if you are trying to avoid restarting browser between tests
     */
    public void clearBrowserCookies() {
        Selenide.clearBrowserCookies();
    }

    /**
     * Выполняется запуск js-скрипта с указанием в js.executeScript его логики
     * Скрипт можно передать как аргумент метода или значение из application.properties
     */
    public void executeJsScript(String scriptName) {
        try {
            String content = loadProperty(scriptName);

            Selenide.executeJavaScript(content);
            AllureLogger.info("Выполнен js скрипт " + content);
        } catch (NullPointerException e) {
            Selenide.executeJavaScript(scriptName);
            AllureLogger.info("Выполнен js скрипт " + scriptName);
        }
    }

    /**
     * Проверяем вызывался ли степ из тестов для мобилки
     */
    public static boolean isMobile() {
        boolean isMobile = false;
        Throwable thr = new Throwable();
        StackTraceElement[] ste = thr.getStackTrace();
        for (int i = 0; i < ste.length; i++)
            if (ste[i].getClassName().contains("ru.testing.mobile.")) {
                isMobile = true;
                break;
            }
        return isMobile;
    }

    /**
     * Обертка над Selenide waitUntil для произвольного числа элементов
     *
     * @param selenideCondition Selenide.Condition
     * @param selenideElements  произвольное количество selenide-элементов
     * @see SelenideElement#waitUntil(Condition, long)
     */
    public static void waitElementsUntil(Condition selenideCondition, SelenideElement... selenideElements) {
        try {
            int timeout = DEFAULT_TIMEOUT;
            Arrays.stream(selenideElements).forEach(e -> e.waitUntil(selenideCondition, timeout));
        } catch (Exception condition) {
            AllureLogger.error(String.format("Элемент не отображается на странице: [%s]", (Object) selenideElements));
        }
    }

    /**
     * Ждем элемент на странице
     *
     * @param element - ожидаемый элемент
     */
    public void waitForElement(WebElement element) {
        new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOf(element));
    }

    /**
     * Ждем что элемент станет кликабельный
     *
     * @param element
     */
    @Step("Проверка, что элемент кликабелен")
    public void waitForEnabledElement(WebElement element) {
        new WebDriverWait(driver, 10).until(ExpectedConditions.elementToBeClickable(element));
    }

    /**
     * Ждем пока на элементе не появится текст
     *
     * @param element - ожидаемый элемент
     * @param text    - ожидаем текст на элементу
     */
    @Step("Проверка отображения элемента на странице")
    public void waitForElementText(WebElement element, String text) {
        new WebDriverWait(driver, 10).until(ExpectedConditions.textToBePresentInElement(element, text));
    }

    /**
     * Скроллим к элементу
     *
     * @param element
     */
    public void scrollToElement(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    /**
     * Проверить отображение элемента
     *
     * @param element
     * @return
     */
    public boolean isElementDisplayed(WebElement element) {
        try {
            element.isDisplayed();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Переключиться на вкладку
     *
     * @param numberWindow номер вкладки в браузере
     */
    public void switchToWindow(int numberWindow) {
        String handle = driver.getWindowHandles().toArray()[numberWindow].toString();
        driver.switchTo().window(handle);
    }

    /**
     * Метод для перемещения ползунка слайдера
     *
     * @param x             множитель перемещения по координатам сладйера
     * @param sliderElement элемент слайдера
     */
    @Step("Перемещение ползунка слайдера")
    public void slider(int x, WebElement sliderElement) {
        int width = sliderElement.getSize().getWidth();
        AllureLogger.info(String.valueOf(width));
        Actions move = new Actions(driver);
        move.moveToElement(sliderElement, ((width * x) / 100), 0).click();
        move.build().perform();
    }

    /**
     * буль, который показывает присутствует ли элемент на странице
     *
     * @param element
     * @return
     */
    public boolean isElementPresent(WebElement element) {
        try {
            element.isDisplayed();
            return true;
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return false;
        }
    }

    @Step("Проверка текста в элементе")
    public static void assertContainsText(SelenideElement element, String equalsText) {
        waitElementsUntil(visible, element);
        assertThat(format(ASSERT_LOG_ERROR, element.text(), equalsText), element.text(),
                containsStringIgnoringCase(equalsText));
        AllureLogger.info(format(ASSERT_LOG_SUCCESS, element.text(), equalsText));
    }

    /**
     * Проверка, что свойство элемента изменилось на нужное нам
     *
     * @param element   элемент, у которого проверяем свойство
     * @param attribute название аттрибута
     * @param value     значение аттрибута
     */
    @Step("Проверка, что свойство элемента имеет нужное значение")
    public static void waitChangeElementProperty(WebElement element, String attribute, String value) {
        new WebDriverWait(getWebDriver(), 5).until(ExpectedConditions.attributeToBe(element, attribute, value));
    }

    public static void waitForLoadPage() {
        new WebDriverWait(getWebDriver(), 3000).until(
                webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
    }

    public static String formatPhone(String phone) {
        return "+7 " + phone.substring(0, 3) + "-" + phone.substring(3, 6) + "-" + phone.substring(6, 8) + "-" + phone.substring(8, 10);
    }

    @Step("Добавляем нужные куки")
    public void addCookies() {
    }

    @Step("Чистим куки, перезагружаем страницу и добавляем нужные куки")
    public void clearCookiesAndRefresh() {
        clearBrowserCookies();
        AllureLogger.info("Куки очищены");
        Selenide.refresh();
        waitForLoadPage();
        addCookies();
    }

    public static void jsClick(SelenideElement element) {
        ((JavascriptExecutor) getWebDriver()).executeScript("arguments[0].click()", element);
    }
}