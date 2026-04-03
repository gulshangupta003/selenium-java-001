package tests;

import base.BaseTest;
import io.qameta.allure.Allure;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.ProductsPage;
import utils.DataProviderHelper;

import java.util.Map;

public class LoginDataDrivenTest extends BaseTest {

    @DataProvider(name = "loginDataFromExcel")
    public Object[][] loginDataFromExcel() {
        return DataProviderHelper.fromExcelAsMap("login_data.xlsx", "Sheet1");
    }

    @DataProvider(name = "loginDataFromJson")
    public Object[][] loginDataFromJson() {
        return DataProviderHelper.fromJson("login_data.json");
    }

    @Test(dataProvider = "loginDataFromExcel", description = "Data driven login test from Excel file")
    @Story("Data Driven Login")
    public void testLoginFromExcel(Map<String, String> data) {
        String username = data.get("username");
        String password = data.get("password");
        String expectedResult = data.get("expectedResult");
        String errorMessage = data.get("errorMessage");

        // Dynamic Allure step description based on data
        Allure.step("Test data — User: " + username + " | Expected: " + expectedResult);

        LoginPage loginPage = new LoginPage();

        if ("success".equalsIgnoreCase(expectedResult)) {
            ProductsPage productsPage = loginPage.login(username, password);

            Assert.assertTrue(productsPage.isPageLoaded(),
                    "Products page should load for user: " + username);
        } else {
            loginPage.loginExpectingError(username, password);

            Assert.assertTrue(loginPage.isErrorDisplayed(),
                    "Error should be displayed for user: " + username);
            Assert.assertTrue(loginPage.getErrorMessageText().contains(errorMessage),
                    "Error message should contain: " + errorMessage);
        }
    }

    @Test(dataProvider = "loginDataFromJson", description = "Data-driven login test from JSON")
    @Story("Data Driven Login")
    public void testLoginFromJson(Map<String, String> data) {
        String username = data.get("username");
        String password = data.get("password");
        String expectedResult = data.get("expectedResult");
        String errorMessage = data.get("errorMessage");

        Allure.step("Test data — User: " + username + " | Expected: " + expectedResult);

        LoginPage loginPage = new LoginPage();

        if ("success".equalsIgnoreCase(expectedResult)) {
            ProductsPage productsPage = loginPage.login(username, password);

            Assert.assertTrue(productsPage.isPageLoaded(),
                    "Products page should load for user: " + username);
        } else {
            loginPage.loginExpectingError(username, password);

            Assert.assertTrue(loginPage.isErrorDisplayed(),
                    "Error should be displayed for user: " + username);
            Assert.assertTrue(loginPage.getErrorMessageText().contains(errorMessage),
                    "Error message should contain: " + errorMessage);
        }
    }

}
