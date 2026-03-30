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

}
