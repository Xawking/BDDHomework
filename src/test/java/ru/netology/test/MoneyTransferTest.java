package ru.netology.test;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.chrome.ChromeOptions;
import ru.netology.data.DataHelper;
import ru.netology.page.Dashboard;
import ru.netology.page.Login;


import java.util.HashMap;
import java.util.Map;

import static com.codeborne.selenide.Selenide.open;
import static ru.netology.data.DataHelper.*;

public class MoneyTransferTest {
    Dashboard dashboard;
    DataHelper.CardInfo firstCardInfo;
    DataHelper.CardInfo secondCardInfo;
    int firstCardBalance;
    int secondCardBalance;

    @BeforeEach
    void setup(){
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        Map<String, Object> prefs = new HashMap<String, Object>();
        prefs.put("credentials_enable_service", false);
        prefs.put("password_manager_enabled", false);
        options.setExperimentalOption("prefs", prefs);
        Configuration.browserCapabilities = options;
        var login = open("http://localhost:9999/", Login.class);
        var authInfo = getAuthInfo();
        var verification = login.validLogin(authInfo);
        var verificationCode = getVerificationCode();
        dashboard = verification.validVerify(verificationCode);
        firstCardInfo = getFirstCardInfo();
        secondCardInfo = getSecondCardInfo();
        firstCardBalance = dashboard.getCardBalance(0);
        secondCardBalance = dashboard.getCardBalance(1);

    }
    @Test
    void firstToSecondTransferValid(){
        var amount = generateValidAmount(firstCardBalance);
        var expectedFirstCardBalance = firstCardBalance - amount;
        var expectedSecondCardBalance = secondCardBalance + amount;
        var transfer = dashboard.selectCardToTransfer(secondCardInfo);
        dashboard = transfer.makeValidTransfer(String.valueOf(amount),firstCardInfo);
        var actualFirstCardBalance = dashboard.getCardBalance(0);
        var actualSecondCardBalance = dashboard.getCardBalance(1);
        Assertions.assertEquals(expectedFirstCardBalance, actualFirstCardBalance);
        Assertions.assertEquals(expectedSecondCardBalance, actualSecondCardBalance);
    }
    @Test
    void secondToFirstTransferValid(){
        var amount = generateValidAmount(secondCardBalance);
        var expectedFirstCardBalance = firstCardBalance + amount;
        var expectedSecondCardBalance = secondCardBalance - amount;
        var transfer = dashboard.selectCardToTransfer(firstCardInfo);
        dashboard = transfer.makeValidTransfer(String.valueOf(amount),secondCardInfo);
        var actualFirstCardBalance = dashboard.getCardBalance(0);
        var actualSecondCardBalance = dashboard.getCardBalance(1);
        Assertions.assertEquals(expectedFirstCardBalance, actualFirstCardBalance);
        Assertions.assertEquals(expectedSecondCardBalance, actualSecondCardBalance);
    }
    @Test
    void firstToSecondTransferInvalid(){
        var amount = generateInvalidAmount(firstCardBalance);
        var transfer = dashboard.selectCardToTransfer(secondCardInfo);
        transfer.makeTransfer(String.valueOf(amount),firstCardInfo);
        transfer.findErrorMessage("Ошибка");

    }
    @Test
    void secondToFirstTransferInvalid(){
        var amount = generateInvalidAmount(secondCardBalance);
        var transfer = dashboard.selectCardToTransfer(firstCardInfo);
        transfer.makeTransfer(String.valueOf(amount),secondCardInfo);
        transfer.findErrorMessage("Ошибка");

    }
}
