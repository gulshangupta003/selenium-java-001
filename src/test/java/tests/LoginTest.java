package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.ProductsPage;

public class LoginTest extends BaseTest {

    @Test(description = "Verify successful login with valid credentials")
    public void loginWithValidCredentials() {
        // Arrange
        String username = "standard_user";
        String password = "secret_sauce";
        LoginPage loginPage = new LoginPage();

        // Act
        ProductsPage productsPage = loginPage.login(username, password);

        // Assert
        Assert.assertTrue(productsPage.isPageLoaded(),
                "Product page should be displayed after login");
        Assert.assertEquals(productsPage.getPageTitleText(), "Products",
                "Page title should be Products");
    }

    @Test(description = "Verify error message with invalid credentials (password)")
    public void loginWithInvalidCredentials() {
        // Arrange
        String username = "standard_user";
        String password = "wrong_password";
        LoginPage loginPage = new LoginPage();

        // Act
        loginPage.login(username, password);

        // Assert
        Assert.assertTrue(loginPage.isErrorDisplayed(), "Error message should be displayed");
        Assert.assertTrue(loginPage.getErrorMessageText().contains("Username and password do not match"),
                "Error should mention credential mismatch");
    }

    @Test(description = "Verify locked out user cannot login")
    public void loginWithLockedUser() {
        // Arrange
        String username = "locked_out_user";
        String password = "secret_sauce";
        LoginPage loginPage = new LoginPage();

        // Act
        loginPage.loginExpectingError(username, password);

        // Assert
        Assert.assertTrue(loginPage.isErrorDisplayed(), "Error message should be displayed");
        Assert.assertTrue(loginPage.getErrorMessageText().contains("locked out"),
                "Error should mention user is locked");
    }

    @Test(description = "Verify login page displayed on launch")
    public void loginPageLoad() {
        // Arrange
        LoginPage loginPage = new LoginPage();

        // Act

        // Assert
        Assert.assertTrue(loginPage.isLoginPageDisplayed(),
                "Login page should be displayed on application launch");
    }

}
