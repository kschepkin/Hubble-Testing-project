package ru.testing.mobile.web.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import controllers.AllureLogger;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import java.util.Random;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static controllers.BaseDesktopTest.assertContainsText;
import static controllers.BaseDesktopTest.waitElementsUntil;
import static controllers.BaseMobileTest.clickUntilResult;
import static controllers.BaseTest.*;

public class BaseMobilePage {
    AuthMobilePage authMobilePage = new AuthMobilePage();
    ListingMobilePage listingMobilePage = new ListingMobilePage();
    Random random = new Random();

    public final SelenideElement gjLogo = $(By.xpath(".//a[contains(@class,'new-logo__icon')]"));
    public final SelenideElement burgerMenuBtn
            = $(By.xpath(".//div[contains(@class,'burger__icon')]"));

    // Burger menu items
    public final SelenideElement closeBurger =
            $(By.xpath(".//div[@class='modal-top__wrapper-close']//div[@class = 'modal-close-icon js-close-button']"));
    public final SelenideElement burgerCurrentRegion =
            $(By.xpath(".//p[contains(@class,'region-name')]"));
    public final SelenideElement burgerChangeRegionBtn =
            $(By.xpath(".//p[contains(@class,'link js-location')]"));
    public final SelenideElement burgerGetAuthForm =
            $(By.xpath(".//div[contains(@class,'burger-menu-modal')]//div[contains(@data-pop-up-name,'authorization-registration')]"));
    public final SelenideElement burgerFaq =
            $(By.xpath(".//div[contains(@class,'burger')]//a[contains(@href,'faq')]"));
    public final SelenideElement burgerStoreLocator =
            $(By.xpath(".//div[contains(@class,'burger')]//a[contains(@href,'store-locator')]"));
    public final SelenideElement burgerUserName =
            $(By.xpath(".//*[contains(@class, 'burger-account__name')]"));
    public final SelenideElement goOutFromProfileButton = $(By.xpath(".//*[contains(text(),'Выход')]"));

    //Header buttons
    public final SelenideElement callButton =
            $(By.xpath(".//div[@class = 'header-functions__mobile']//*[contains(@class, 'icon--tel icon icon--desktop-size-30 icon-item mobile--only')]"));
    public final SelenideElement goToSearchButton =
            $(By.xpath(".//div[@class = 'header-functions__mobile']//*[@data-pop-up-name = 'search-block']"));
    public final SelenideElement goToFavoritesButton =
            $(By.xpath(".//div[@class = 'header-functions__mobile']//*[contains(@class, 'icon icon--heart')]"));
    public final SelenideElement goToCartButton =
            $(By.xpath(".//div[@class = 'header-functions__mobile']//*[@data-qa = 'icon-cart']"));
    public final SelenideElement oldGoToCartButton =
            $(By.xpath(".//div[@class='wrapper-mobile-header mobile--only']//a[@class = 'basket js-header-minicart']"));


    //Search popout
    public final SelenideElement closeSearchPageButton =
            $(By.xpath(".//div[contains(@class,'search-field--modal')]//div[@class = 'modal-close-icon js-close-button']"));

    //Меню категорий
    public final ElementsCollection categoryMenu =
            $$(By.xpath(".//div[@class='navigation-list mobile-show']/span"));
    public final ElementsCollection categoryLinks =
            $$(By.xpath(".//a[@class='header-dropdown-content__link']"));

    @Step("Открытие бокового меню")
    public void burgerMenuOpen() {
        waitForLoadPage();
        waitElementsUntil(visible, burgerMenuBtn);
        jsClick(burgerMenuBtn);
    }

    @Step("Переход на страницу авторизации")
    public void getAuthPage() {
        burgerMenuOpen();
        burgerGetAuthForm.click();
        waitElementsUntil(visible, authMobilePage.loginEmailField);
        AllureLogger.info("Выполнен переход на страницу авторизации");
    }

    @Step("Проверка авторизации пользователя")
    public void checkAuthorization(String name) {
        burgerMenuOpen();
        assertContainsText(burgerUserName, name);
        AllureLogger.info("Пользователь авторизован");
        closeBurger.click();
    }

    @Step("Переход на страницу поиска")
    public void goToSearchPage() {
        waitElementsUntil(visible, goToSearchButton);
        clickUntilResult(goToSearchButton, closeSearchPageButton);
    }



    @Step("Выход из профиля")
    public void logout() {
        burgerMenuOpen();
        goOutFromProfileButton.click();
    }
}
