package tests;

import base.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.ProductsPage;

@Epic("saucedemo platform")
@Feature("Products module")
public class ProductsTest extends BaseTest {

    private ProductsPage productsPage;

    @BeforeMethod
    public void loginFirst() {
        LoginPage loginPage = new LoginPage();
        productsPage = loginPage.login("standard_user", "secret_sauce");
    }

    @Test(description = "Verify all 6 products are displayed")
    @Story("Display products")
    @Description("All 6 products should be displayed on Products page")
    public void allProductsShouldDisplay() {
        Assert.assertEquals(productsPage.getProductCount(), 6,
                "Products page should display exactly 6 products");
    }

    @Test(description = "Verify adding products to cart updates badge")
    @Story("Cart count update")
    @Description("After adding item to cart, cart icon should be updated with number of item added in cart")
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
    @Story("Remove cart badge")
    @Description("If there cart will be empty then cart badge should not be displayed on cart icon")
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
