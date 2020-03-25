package ru.testing.web.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.ElementsContainer;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static controllers.BaseTest.waitElementsUntil;

public class BasePage extends ElementsContainer {

    //  элементы шапки сайта
    public final SelenideElement goToAuthPageBtn = $(By.xpath(".//span[@data-qa='account-link']"));
    public final SelenideElement authPageFromProfileBtn = $(By.xpath(".//*[contains(@class,'personal-block')]//a"));
    public final SelenideElement saveAutodetectRegionButton = $(By.xpath(".//*[@data-qa='saveAutodetectRegion']"));
    public final SelenideElement selectAnotherRegionButton = $(By.xpath(".//*[@data-qa='selectAnotherRegion']"));
    public final SelenideElement goToProfilePageButton = $(By.xpath(".//*[@data-qa='modal-profile']"));
    public final SelenideElement goToOrderHistoryPageButton = $(By.xpath(".//*[contains(@href,'orders')]"));
    public final SelenideElement goToAddressBookPageButton = $(By.xpath(".//a[@href='/my-account/address-book']"));
    public final ElementsCollection categoryMenu = $$(By.xpath(".//div[@class='navigation-list desktop-show']/a"));
    public final SelenideElement iconHeart = $(By.xpath(".//span[contains(@class,'heart icon--desktop')]"));
    public final SelenideElement goOutFromProfileButton = $(By.xpath(".//*[contains(text(),'Выйти')]"));
    public final SelenideElement oldCartButton = $(By.xpath(".//div[contains(@class, 'js-login-block')]//a[@href='/cart']"));
    public final SelenideElement cartButton = $(By.xpath(".//div[@class = 'header-functions']//*[@data-qa = 'icon-cart']"));
    public final SelenideElement searchInput = $(By.id("js-site-search-input"));


    @Step("Выход из пользователя")
    public void outFromProfile() {
        goToAuthPageBtn.hover();
        goOutFromProfileButton.click();
    }

}
