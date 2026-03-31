package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.ProductsPage;

public class LoginTest extends BaseTest {

    private static final String VALID_USER = "standard_user";
    private static final String VALID_PASSWORD = "secret_sauce";
    private static final String INVALID_PASSWORD = "wrong_password";
    private static final String LOCKED_USER = "locked_out_user";

    @Test(description = "Verify successful login with valid credentials")
    public void loginWithValidCredentials() {
        // Arrange
        LoginPage loginPage = new LoginPage();

        // Act
        ProductsPage productsPage = loginPage.login(VALID_USER, VALID_PASSWORD);

        // Assert
        Assert.assertTrue(productsPage.isPageLoaded(),
                "Product page should be displayed after login");
        Assert.assertEquals(productsPage.getPageTitleText(), "Products",
                "Page title should be Products");
    }

    @Test(description = "Verify error message with invalid credentials (password)")
    public void loginWithInvalidCredentials() {
        // Arrange
        LoginPage loginPage = new LoginPage();

        // Act
        loginPage.login(VALID_USER, INVALID_PASSWORD);

        // Assert
        Assert.assertTrue(loginPage.isErrorDisplayed(), "Error message should be displayed");
        Assert.assertTrue(loginPage.getErrorMessageText().contains("Username and password do not match"),
                "Error should mention credential mismatch");
    }

    @Test(description = "Verify locked out user cannot login")
    public void loginWithLockedUser() {
        // Arrange
        LoginPage loginPage = new LoginPage();

        // Act
        loginPage.loginExpectingError(LOCKED_USER, VALID_USER);

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
