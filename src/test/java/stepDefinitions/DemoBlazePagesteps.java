package stepDefinitions;

import io.cucumber.java.Scenario;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.Assert;
import pages.CartPage;
import pages.CategoriesPage;
import pages.HomePage;
import pages.ProductPage;
import pages.SignInSignUpPage;
import utils.EnvironmentVariables;

public class DemoBlazePagesteps {
    HomePage homePage;
    SignInSignUpPage signInSignUpPage;
    CartPage cartPage;
    CategoriesPage categoriesPage;
    ProductPage productPage;

    Scenario scenario = TestBase.scenario;

    @Given("^URL is loaded in to browser$")
    public void url_is_loaded_in_to_browser() {
        homePage = new HomePage(TestBase.driver);
        Assert.assertTrue("Home page is not loaded", homePage.isCategoriesVisible());
    }

    @When("^Customer registers himself$")
    public void author_logins_with_a_valid_credentials() {
        scenario.log("Customer Credentials: " + EnvironmentVariables.WEB_USERNAME + " | " + EnvironmentVariables.WEB_PASSWORD);
        signInSignUpPage = homePage.navigateToSignUpPage();
        homePage = signInSignUpPage.doSignUp(EnvironmentVariables.WEB_USERNAME, EnvironmentVariables.WEB_PASSWORD);
        Assert.assertTrue("Home page is not visible", homePage.isCategoriesVisible());
    }

    @And("^Customer logins to online store$")
    public void customer_logins_to_online_store() {
        signInSignUpPage = homePage.navigateToLoginPage();
        homePage = signInSignUpPage.doLogin(EnvironmentVariables.WEB_USERNAME, EnvironmentVariables.WEB_PASSWORD);
        Assert.assertTrue("Logout link is not visible on home page", homePage.isLogoutVisible());
    }

    @Then("^Customer navigates to (.*)")
    public void customer_navigates_to(String page) {
        if (page.equalsIgnoreCase("HOME")) {
            homePage = homePage.navigateToHomePage();
            Assert.assertTrue("Home page is not visible", homePage.isCategoriesVisible());
        } else if (page.equalsIgnoreCase("CART")) {
            cartPage = homePage.navigateToCartPage();
            Assert.assertTrue("Cart page is not visible", cartPage.isPlaceOrderVisible());
        } else {
            categoriesPage = homePage.navigateToCategories(page);
            Assert.assertTrue("Product cards are not visible", categoriesPage.isProductCardVisible());
        }
    }

    @Then("^Customer adds to cart$")
    public void customer_adds_to_cart() {
        boolean isAdded = productPage.clickAddToCart();
        Assert.assertTrue("Product is not added to cart", isAdded);
    }

    @Then("^Customer select product (.*)$")
    public void selects_product(String productName) {
        productPage = categoriesPage.selectProduct(productName);
        Assert.assertTrue("Product not found", productPage != null);

        String actualProductName = productPage.getProductName();
        Assert.assertEquals("Incorrect product name on Product page", productName, actualProductName);
    }

    @Then("^Customer removes product (.*) from cart$")
    public void customer_removes_product_from_cart(String productName) {
        cartPage = cartPage.deleteProduct(productName);
        Assert.assertTrue("Unable to delete product: " + productName, cartPage != null);
    }

    @Then("^Customer place order and fill form details$")
    public void customer_place_order_and_fill_form_details() {
        boolean isSuccess = cartPage.placeOrder(org.apache.commons.lang.RandomStringUtils.randomAlphabetic(5), RandomStringUtils.randomAlphabetic(5),
                RandomStringUtils.randomAlphabetic(5), RandomStringUtils.randomNumeric(16),
                RandomStringUtils.randomNumeric(2), RandomStringUtils.randomNumeric(4));
        Assert.assertTrue("Unable to Place order", isSuccess);
        scenario.log("Order Id: " + cartPage.getOrderNumber());
        scenario.log("Order Amount: " + cartPage.getOrderTotal());
    }

    @Then("^Customer Verify the amount on order confirmation$")
    public void verify_the_amount_on_order_confirmation() {
        String cartTotal = cartPage.getCartTotal();
        String orderTotal = cartPage.getOrderTotal();
        scenario.log("Cart Total: " + cartTotal);
        scenario.log("Order Total: " + orderTotal);
        Assert.assertEquals("Cart total and Order total doesn't match", cartTotal + " USD", orderTotal);
    }

    @Then("^Customer clicks on OK button$")
    public void clicks_on_ok_button() {
        cartPage.clickOk();
    }
}
