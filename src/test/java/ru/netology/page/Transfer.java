package ru.netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper;

import java.time.Duration;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class Transfer {
    private final SelenideElement amountField = $("[data-test-id='amount'] input");
    private final SelenideElement fromField = $("[data-test-id='from'] input");
    private final SelenideElement transferButton = $("[data-test-id='action-transfer']");
    private final SelenideElement transferHeading = $(byText("Пополнение карты"));
    private final SelenideElement errorMessage = $("[data-test-id='error-notification']");

    public Transfer() {
        transferHeading.shouldBe(Condition.visible);
    }

    public void makeTransfer(String amountToTransfer, DataHelper.CardInfo cardInfo) {
        amountField.setValue(amountToTransfer);
        fromField.setValue(cardInfo.getCardNumber());
        transferButton.click();
    }

    public Dashboard makeValidTransfer(String amountToTransfer, DataHelper.CardInfo cardInfo) {
        makeTransfer(amountToTransfer, cardInfo);
        return new Dashboard();
    }

    public void findErrorMessage(String expectedText) {
        errorMessage.shouldHave(Condition.text(expectedText), Duration.ofSeconds(15)).shouldBe(Condition.visible);
    }
}
