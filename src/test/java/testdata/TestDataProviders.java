package testdata;

import org.testng.annotations.DataProvider;
import utils.DataProviderHelper;

/**
 * Centralized DataProvider class.
 * Any test class can reference these providers using:
 *
 * @Test(dataProvider = "name", dataProviderClass = TestDataProviders.class)
 * <p>
 * Keeps data logic out of test classes entirely.
 */
public class TestDataProviders {

    @DataProvider(name = "validLoginData", parallel = true)
    public static Object[][] validLoginData() {
        return DataProviderHelper.fromExcelFiltered("login_data.xlsx", "Sheet1",
                "expectedResult", "success");
    }

    @DataProvider(name = "invalidLoginData", parallel = true)
    public static Object[][] invalidLoginData() {
        return DataProviderHelper.fromExcelFiltered("login_data.xlsx", "Sheet1",
                "expectedResult", "error");
    }

    @DataProvider(name = "allLoginData", parallel = true)
    public static Object[][] allLoginData() {
        return DataProviderHelper.fromExcelAsMap("login_data.xlsx", "Sheet1");
    }

    @DataProvider(name = "cartProducts", parallel = true)
    public static Object[][] cartProducts() {
        return DataProviderHelper.fromJson("cart_data.json");
    }

}
