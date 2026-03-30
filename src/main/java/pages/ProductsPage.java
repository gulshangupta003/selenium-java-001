package pages;

import org.openqa.selenium.By;

public class ProductsPage extends BasePage {

    private final By pageTitle = By.className("title");

    public boolean isPageLoaded() {
        return isDisplayed(pageTitle);
    }

    public String getPageTitleText() {
        return getText(pageTitle);
    }

}
