package tests;

import base.BaseTest;
import io.qameta.allure.Allure;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.ProductsPage;
import utils.DataProviderHelper;

import java.util.Map;

public class CartDataDrivenTest extends BaseTest {

    private ProductsPage productsPage;

    @BeforeMethod
    public void login() {
        LoginPage loginPage = new LoginPage();
        productsPage = loginPage.login("standard_user", "secret_sauce");
    }

    @DataProvider(name = "cartData", parallel = true)
    public Object[][] cartData() {
        return DataProviderHelper.fromJson("cart_data.json");
    }

    @Test(dataProvider = "cartData", description = "Verify add to cart for each product")
    @Story("Add to Cart")
    public void testAddProductToCart(Map<String, String> data) {
        String productName = data.get("productName");

        Allure.step("Adding product: " + productName);

        productsPage.addProductToCart(productName);

        Assert.assertTrue(productsPage.isCartBadgeDisplayed(),
                "Cart badge should appear after adding: " + productName);
        Assert.assertEquals(productsPage.getCartBadgeCount(), 1,
                "Cart should have 1 item after adding: " + productName);
    }

    @Test(dataProvider = "cartData", description = "Verify add and remove from cart for each product")
    @Story("Remove from Cart")
    public void testAddAndRemoveProduct(Map<String, String> data) {
        String productName = data.get("productName");

        Allure.step("Adding product: " + productName);
        productsPage.addProductToCart(productName);

        Allure.step("Remove Product: " + productName);
        productsPage.removeProductFromCart(productName);

        Assert.assertFalse(productsPage.isCartBadgeDisplayed(),
                "Cart badge should disappear after removing: " + productName);
    }

}
