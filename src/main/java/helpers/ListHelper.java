package helpers;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import controllers.AllureLogger;
import controllers.BaseTest;
import io.qameta.allure.Step;

import java.util.List;

import static com.codeborne.selenide.Selenide.*;
import static helpers.TestDataHelper.getRandomInt;


public class ListHelper extends BaseTest {

    /**
     * Метод получения рандомного элемента из списка
     *
     * @return случайный селенид элемент
     */
    @Step("Выбор случайного элемента из списка")
    public static SelenideElement getRandomElementFromList(ElementsCollection elements) {
        try {
            waitElementsUntil(Condition.visible, elements.first());
            SelenideElement selectedElement = elements.get(getRandomInt(elements.size()));
            AllureLogger.info("Выбран элемент из списка: " + selectedElement.getText());
            return selectedElement;
        } catch (IllegalArgumentException | IndexOutOfBoundsException e) {
            AllureLogger.error("В списке не найдено ни одного элемента.");

            return null;
        }
    }

    /**
     * Метод получения элемента из списка по его тексту
     *
     * @return селенид элемент
     */
    @Step("Выбор элемента из списка по его тексту")
    public static SelenideElement getElementFromListByText(ElementsCollection elements, String text) {
        try {
            waitElementsUntil(Condition.visible, elements.first());
            for (int i = 0; i < elements.size(); i++) {
                SelenideElement element = elements.get(i);
                if (element.text().toLowerCase().contains(text.toLowerCase())) {
                    AllureLogger.info("Выбран элемент из списка: " + element.getText());
                    return element;
                }
            }
            AllureLogger.error("В списке не найдено элемента с указанным текстом: " + text);
            return null;
        } catch (IllegalArgumentException | IndexOutOfBoundsException e) {
            AllureLogger.error("В списке не найдено ни одного элемента.");
            return null;
        }
    }

    @Step("Проверка на наличие элемента в списке по тексту")
    public static boolean listContainsElementByText(ElementsCollection elements, String text) {
        if (getElementFromListByText(elements, text) != null) {
            AllureLogger.debug("Элемент с текстом " + text + " найден в списке");
            return true;
        } else {
            AllureLogger.debug("Элемент с текстом " + text + " не найден в списке");
            return false;
        }
    }

    /**
     * Кликает по любому элементу из спика до тех пор, пока значение другого элемента не станет = value
     *
     * @param listName    список из которого выбирается рандомный элемент для клика
     * @param elementName элемент у которого расчитывается кол-во
     * @param value       значение до кторого должен измениться элемент elementName
     */
    @Step("Нажимаем на элемент до тех пор, пока значение другого не достигнет нужного значения")
    public static void clickRandomListElementByMaxCondition(ElementsCollection listName, SelenideElement elementName, String value) {
        float summ = Float.parseFloat(value);

        for (float cartsumm = Float.parseFloat($(elementName).getText().replaceAll("\\D+", ""));
             cartsumm < summ;
             cartsumm = Float.parseFloat($(elementName).getText().replaceAll("\\D+", ""))) {
            List<SelenideElement> listOfElementsFromPage = $$(listName);
            listOfElementsFromPage.get(getRandomInt(listOfElementsFromPage.size()))
                    .shouldBe(Condition.visible).click();
            sleep(300);
        }
    }

    @Step("Проверка элемента на соответствие тексту")
    public static boolean checkElementByText(String element, String text) {
        if (element.equals(text)) {
            AllureLogger.debug("Элемент " + element + " соответствует " + text);
            return true;
        } else {
            AllureLogger.debug("Элемент " + element + " НЕ соответствует " + text);
            return false;
        }
    }
}