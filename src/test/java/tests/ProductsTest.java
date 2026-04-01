package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.ProductsPage;

public class ProductsTest extends BaseTest {

    private ProductsPage productsPage;

    @BeforeMethod
    public void loginFirst() {
        LoginPage loginPage = new LoginPage();
        productsPage = loginPage.login("standard_user", "secret_sauce");
    }

    @Test(description = "Verify all 6 products are displayed")
    public void allProductsShouldDisplay() {
        Assert.assertEquals(productsPage.getProductCount(), 6,
                "Products page should display exactly 6 products");
    }

    @Test(description = "Verify adding products to cart updates badge")
    public void addToCartShouldShowBatchCount() {
        // Arrange
        String product = "Sauce Labs Backpack";

        // Act
        productsPage.addProductToCart(product);

        // Assert
        Assert.assertTrue(productsPage.isCartBadgeDisplayed(), "Cart badge should appear after adding item");
        Assert.assertEquals(productsPage.getCartBadgeCount(), 2,
                "Cart badge should show count of 1");
    }

    @Test(description = "Verify adding and removing product clears badge")
    public void shouldRemoveProductFromCart() {
        // Arrange
        String product = "Sauce Labs Backpack";
        productsPage.addProductToCart(product);

        // Act
        productsPage.removeProductFromCart(product);

        // Assert
        Assert.assertFalse(productsPage.isCartBadgeDisplayed(),
                "Cart badge should disappear after removing item");
    }

}
