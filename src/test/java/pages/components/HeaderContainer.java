package pages.components;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import pages.CartPage;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.webdriver;
import static org.assertj.core.error.ShouldBeVisible.shouldBeVisible;

public class HeaderContainer {
    private final SelenideElement headerContainer = $(".m-header__container");
    private final SelenideElement mainLogo = $(".main-logo a");
    private final SelenideElement hamburgerMenu = $("#my-icon");
    private final SelenideElement searchIcon = $("#m-search");
    private final SelenideElement searchInput = $("#m-search-inp");
    private final SelenideElement searchForm = $(".search-form");
    private final SelenideElement notificationsIcon = $(".notifications-icon");
    private final SelenideElement notificationsCount = $(".notifications-count");
    private final SelenideElement basketIcon = $(".hr-basket .bask-icon");
    private final SelenideElement basketLink = $(".hr-basket .bask-right");
    private final SelenideElement basketItemCount = $(".baks-item-count");
    private final SelenideElement basketTotalPrice = $(".full-price");
    private final SelenideElement loginButton = $("#main-login-button");
    private final SelenideElement userName = $(".hr-name");
    private final SelenideElement articlesMenu = $("a[href='/articles']");
    private final SelenideElement videosMenu = $("a[href='/videos']");
    private final SelenideElement coursesMenu = $("a[href='/courses']");
    private final SelenideElement catalogMenu = $("a[href='/catalog']");
    private final SelenideElement aboutMenu = $("a[href='/about']");

    public HeaderContainer shouldBeVisible() {
        headerContainer.shouldBe(Condition.visible);
        return this;
    }

    public boolean isHeaderDisplayed() {
        return headerContainer.isDisplayed();
    }


    public void clickLogo() {
        mainLogo.click();
    }
    public boolean isLogoDisplayed() {
        return mainLogo.isDisplayed();
    }

    public void openArticles() {
        articlesMenu.click();
    }

    public void openVideos() {
        videosMenu.click();
    }

    public void openCourses() {
        coursesMenu.click();
    }

    public void openCatalog() {
        catalogMenu.click();
    }

    public void openAbout() {
        aboutMenu.click();
    }

    public void openLogin() {
        loginButton.click();
    }

    public String getUserName() {
        return userName.getText();
    }

    public void verifyAllHeaderElements() {
        shouldBeVisible();
        assert isLogoDisplayed() : "Логотип не отображается";
        assert articlesMenu.isDisplayed() : "Меню 'Статьи' не отображается";
        assert videosMenu.isDisplayed() : "Меню 'Видео' не отображается";
        assert catalogMenu.isDisplayed() : "Меню 'Магазин' не отображается";
        assert basketIcon.isDisplayed() : "Иконка корзины не отображается";
        assert loginButton.isDisplayed() : "Кнопка входа не отображается";
    }

    public String getCurrentUrl() {
        return webdriver().object().getCurrentUrl();
    }


    public CartPage openCart() {
        basketLink.click();
        return new CartPage();
    }

    public int getCartItemsCountFromHeader() {
        return Integer.parseInt(basketItemCount.getText().trim());
    }

    public void waitForCartUpdate(int expectedCount) {
        basketItemCount.shouldHave(Condition.exactText(String.valueOf(expectedCount)));
    }

    public boolean isCartEmpty() {
        return basketItemCount.getText().trim().equals("0");
    }

    @Step("Проверка, что в корзине {count} товаров")
    public boolean checkCountToCart(int count) {
            return getCartItemsCountFromHeader() == count;

    }
}
