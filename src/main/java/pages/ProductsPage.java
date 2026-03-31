package pages;

import org.openqa.selenium.By;

public class ProductsPage extends BasePage {

    private final By pageTitle = By.className("title");
    private final By productNames = By.className("inventory_item_name");
    private final By cartBadge = By.className("shopping_cart_badge");
    private final By cartIcon = By.className("shopping_cart_link");
    private final By hamburgerMenu = By.id("react-burger-menu-btn");
    private final By logoutLink = By.id("logout_sidebar_link");

    public boolean isPageLoaded() {
        return isDisplayed(pageTitle);
    }

    public String getPageTitleText() {
        return getText(pageTitle);
    }

    public int getProductCount() {
        return getElementCount(productNames);
    }

    public void addProductToCart(String productName) {
        String buttonId = "add-to-cart-" + productName.toLowerCase().replace(" ", "-");
        click(By.id(buttonId));
    }

    public void removeProductFromCart(String productName) {
        String buttonId = "remove-" + productName.toLowerCase().replace(" ", "-");
        click(By.id(buttonId));
    }

    public int getCartBadgeCount() {
        String text = getText(cartBadge);

        return Integer.parseInt(text);
    }

    public boolean isCartBadgeDisplayed() {
        return isDisplayed(cartBadge);
    }

    public void clickCart() {
        click(cartIcon);
    }



    public LoginPage logout() {
        click(hamburgerMenu);
        click(logoutLink);
        return new LoginPage();
    }

}
