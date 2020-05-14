//package controllers;
//
//import com.codeborne.selenide.WebDriverProvider;
//import io.github.bonigarcia.wdm.DriverManagerType;
//import io.github.bonigarcia.wdm.WebDriverManager;
//import lombok.extern.slf4j.Slf4j;
//import net.lightbody.bmp.BrowserMobProxy;
//import net.lightbody.bmp.BrowserMobProxyServer;
//import net.lightbody.bmp.proxy.BlacklistEntry;
//import org.openqa.selenium.Dimension;
//import org.openqa.selenium.MutableCapabilities;
//import org.openqa.selenium.Point;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.chrome.ChromeDriver;
//import org.openqa.selenium.chrome.ChromeOptions;
//import org.openqa.selenium.firefox.FirefoxDriver;
//import org.openqa.selenium.firefox.FirefoxOptions;
//import org.openqa.selenium.ie.InternetExplorerDriver;
//import org.openqa.selenium.ie.InternetExplorerOptions;
//import org.openqa.selenium.opera.OperaDriver;
//import org.openqa.selenium.opera.OperaOptions;
//import org.openqa.selenium.remote.CapabilityType;
//import org.openqa.selenium.remote.DesiredCapabilities;
//import org.openqa.selenium.remote.RemoteWebDriver;
//import org.openqa.selenium.safari.SafariDriver;
//import org.openqa.selenium.safari.SafariOptions;
//
//import java.net.MalformedURLException;
//import java.net.URI;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import static com.codeborne.selenide.WebDriverRunner.*;
//import static controllers.PropertyLoader.loadProperty;
//import static controllers.PropertyLoader.loadPropertyOrDefault;
//
///**
// * Провайдер драйверов, который позволяет запускать тесты локально или удаленно, используя Selenoid
// * Параметры запуска можно задавать, как системные переменные.
// * <p>
// * Например, можно указать браузер, версию браузера, remote Url(где будут запущены тесты), ширину и высоту окна браузера:
// * -Dbrowser=chrome -DbrowserVersion=63.0 -DremoteUrl=http://some/url -Dwidth=1000 -Dheight=500
// * Если параметр remoteUrl не указан - тесты будут запущены локально в заданном браузере последней версии.
// * Все необходимые опции можно прописывать в переменную options, разделяя их пробелом.
// * Если указан параметр remoteUrl и browser, но версия браузера не указана,
// * по умолчанию будет установлена версия latest
// * Если браузер не указан - по умолчанию будет запущен chrome
// * По умолчанию размер окна браузера при remote запуске равен 1920x1080
// * Предусмотрена возможность запуска в режиме мобильного браузера (-Dbrowser=mobile)
// * С указанием устройства, на котором будем эмулироваться запуск мобильного chrome браузера (-Ddevice=iPhone 6)
// */
//@Slf4j
//public class CustomDriverProvider implements WebDriverProvider {
//    public static final String MOBILE_DRIVER = "mobile";
//    public static final String BROWSER = "browser";
//    public static final String REMOTE_URL = "remoteUrl";
//    public static final String WINDOW_WIDTH = "width";
//    public static final String WINDOW_HEIGHT = "height";
//    public static final String VERSION_LATEST = "latest";
//    public static final String LOCAL = "local";
//    public static final int DEFAULT_WIDTH = 1920;
//    public static final int DEFAULT_HEIGHT = 1080;
//
//    private BrowserMobProxy proxy = new BrowserMobProxyServer();
//    private String[] options;
//
//    @Override
//    public WebDriver createDriver(DesiredCapabilities capabilities) {
//        String expectedBrowser = System.getProperty("settings.browser");
//        String remoteUrl = loadPropertyOrDefault("REMOTE_URL", LOCAL);
//        System.out.println("load driver success");
//        if (FIREFOX.equalsIgnoreCase(expectedBrowser)) {
//            return LOCAL.equalsIgnoreCase(remoteUrl) ? createFirefoxDriver() : getRemoteDriver(getFirefoxDriverOptions(), remoteUrl);
//        }
//
//        if (MOBILE_DRIVER.equalsIgnoreCase(expectedBrowser)) {
//            return LOCAL.equalsIgnoreCase(remoteUrl) ? createChromeDriver(getMobileChromeOptions()) : getRemoteDriver(getMobileChromeOptions(), remoteUrl);
//        }
//
//        if (OPERA.equalsIgnoreCase(expectedBrowser)) {
//            return LOCAL.equalsIgnoreCase(remoteUrl) ? createOperaDriver() : getRemoteDriver(getOperaDriverOptions(), remoteUrl);
//        }
//
//        if (SAFARI.equalsIgnoreCase(expectedBrowser)) {
//            return LOCAL.equalsIgnoreCase(remoteUrl) ? createSafariDriver() : getRemoteDriver(getSafariDriverOptions(), remoteUrl);
//        }
//
//        if (INTERNET_EXPLORER.equalsIgnoreCase(expectedBrowser)) {
//            return createIEDriver();
//        }
//
//        log.info("remoteUrl=" + remoteUrl + " expectedBrowser= " + expectedBrowser + " BROWSER_VERSION=" + System.getProperty(CapabilityType.BROWSER_VERSION));
//        return LOCAL.equalsIgnoreCase(remoteUrl) ? createChromeDriver(getChromeDriverOptions()) : getRemoteDriver(getChromeDriverOptions(), remoteUrl);
//    }
//
//    /**
//     * Задает capabilities для запуска Remote драйвера для Selenoid
//     *
//     * @param capabilities - capabilities для установленного браузера
//     * @param remoteUrl    - url для запуска тестов, например http://remoteIP:4444/wd/hub
//     * @return
//     */
//    private WebDriver getRemoteDriver(MutableCapabilities capabilities, String remoteUrl) {
//        log.info("---------------run Selenoid Remote Driver---------------------");
//        capabilities.setCapability("enableVNC", true);
//
//        try {
//            return new RemoteWebDriver(
//                    URI.create(remoteUrl).toURL(),
//                    capabilities
//            );
//        } catch (MalformedURLException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    /**
//     * Задает capabilities для запуска Remote драйвера для Selenoid
//     * со списком регулярных выражений соответствующих URL, которые добавляются в Blacklist
//     *
//     * @param capabilities
//     * @param remoteUrl
//     * @param blacklistEntries - список url для добавления в Blacklist
//     * @return
//     */
//    private WebDriver getRemoteDriver(MutableCapabilities capabilities, String remoteUrl, List<BlacklistEntry> blacklistEntries) {
//        proxy.setBlacklist(blacklistEntries);
//        return getRemoteDriver(capabilities, remoteUrl);
//    }
//
//    /**
//     * Устанавливает ChromeOptions для запуска google chrome эмулирующего работу мобильного устройства (по умолчанию nexus 5)
//     * Название мобильного устройства (device) может быть задано через системные переменные
//     *
//     * @return ChromeOptions
//     */
//    private ChromeOptions getMobileChromeOptions() {
//        log.info("---------------run CustomMobileDriver---------------------");
//        String mobileDeviceName = loadPropertyOrDefault("device", "iPhone 6");
//        ChromeOptions chromeOptions = new ChromeOptions().addArguments("disable-extensions",
//                "test-type", "no-default-browser-check", "ignore-certificate-errors");
//        Map<String, String> mobileEmulation = new HashMap<>();
//
//        mobileEmulation.put("deviceName", mobileDeviceName);
//        chromeOptions.setExperimentalOption("mobileEmulation", mobileEmulation);
//        return chromeOptions;
//    }
//
//    /**
//     * Задает options для запуска Chrome драйвера
//     *
//     * @return
//     */
//    private ChromeOptions getChromeDriverOptions() {
//        log.info("---------------Chrome Driver---------------------");
//        ChromeOptions chromeOptions = !options[0].equals("") ? new ChromeOptions().addArguments(options) : new ChromeOptions();
//        Map<String, Object> prefs = new HashMap<>();
//        prefs.put("intl.accept_languages", "ru-RU,ru");
//        chromeOptions.setExperimentalOption("prefs", prefs);
//        return chromeOptions;
//    }
//
//    /**
//     * Задает options для запуска Firefox драйвера
//     *
//     * @return
//     */
//    private FirefoxOptions getFirefoxDriverOptions() {
//        log.info("---------------Firefox Driver---------------------");
//        return !options[0].equals("") ? new FirefoxOptions().addArguments(options) : new FirefoxOptions();
//    }
//
//    /**
//     * Задает options для запуска Opera драйвера
//     *
//     * @return
//     */
//    private OperaOptions getOperaDriverOptions() {
//        log.info("---------------Opera Driver---------------------");
//        return !options[0].equals("") ? new OperaOptions().addArguments(options) : new OperaOptions();
//    }
//
//    /**
//     * Задает options для запуска IE драйвера
//     *
//     * @return
//     */
//    private InternetExplorerOptions getIEDriverOptions() {
//        log.info("---------------IE Driver---------------------");
//        return !options[0].equals("") ? new InternetExplorerOptions().addCommandSwitches(options) : new InternetExplorerOptions();
//    }
//
//    /**
//     * Задает options для запуска Safari драйвера
//     *
//     * @return
//     */
//    private SafariOptions getSafariDriverOptions() {
//        log.info("---------------Safari Driver---------------------");
//        return new SafariOptions();
//    }
//
//    /**
//     * Создает WebDriver
//     *
//     * @return
//     */
//    private WebDriver createChromeDriver(ChromeOptions chromeOptions) {
//        WebDriverManager.getInstance(DriverManagerType.CHROME).setup();
//        WebDriver chromeDriver = new ChromeDriver(chromeOptions);
//        chromeDriver.manage().window().setPosition(getPoint(10, 10));
////        chromeDriver.manage().window().maximize();
//        return chromeDriver;
//    }
//
//    private WebDriver createFirefoxDriver() {
//        WebDriverManager.getInstance(DriverManagerType.FIREFOX).setup();
//        WebDriver firefoxDriver = new FirefoxDriver(getFirefoxDriverOptions());
//        firefoxDriver.manage().window().setSize(setDimension());
//        return firefoxDriver;
//    }
//
//    private WebDriver createOperaDriver() {
//        WebDriverManager.getInstance(DriverManagerType.OPERA).setup();
//        WebDriver operaDriver = new OperaDriver(getOperaDriverOptions());
//        operaDriver.manage().window().setSize(setDimension());
//        return operaDriver;
//    }
//
//    private WebDriver createIEDriver() {
//        WebDriverManager.getInstance(DriverManagerType.IEXPLORER).setup();
//        WebDriver internetExplorerDriver = new InternetExplorerDriver(getIEDriverOptions());
//        internetExplorerDriver.manage().window().setSize(setDimension());
//        return internetExplorerDriver;
//    }
//
//    private WebDriver createSafariDriver() {
//        SafariDriver safariDriver = new SafariDriver(getSafariDriverOptions());
//        safariDriver.manage().window().setSize(setDimension());
//        return safariDriver;
//    }
//
//    /**
//     * Задает настройки разрешения
//     */
//    private Dimension setDimension() {
//        try {
//            return new Dimension(Integer.parseInt(loadProperty("WINDOW_WIDTH")),
//                    Integer.parseInt(loadProperty("WINDOW_HEIGHT")));
//        } catch (Exception E) {
//            return new Dimension(1920, 1080);
//        }
//    }
//
//    protected Point getPoint(int x, int y) {
//        return new Point(x, y);
//    }
//}