package pages.components;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class ProductCard {

    private final SelenideElement
        productName = $(".product__desc-wrap a"),
        basketButton = $(".to-basket-desktop"),
        productPrice = $(".product__buy-wrap div span"),
        productLink = $(".product__link");

    public ProductCard(SelenideElement cardElement) {}


    public String getProductName() {
        return productName
                .shouldBe(Condition.visible)
                .text();
    }

    public String getProductPrice() {
        return productPrice
                .shouldBe(Condition.visible)
                .text()
                .replaceAll("\\s+", "")
                .trim();
    }

    public void goToProductPage() {
        productLink.click();
    }

    @Step("Добавление карточки товара в корзину")
    public ProductCard addToCart() {
        this.basketButton.click();
        return this;
    }
    @Step("Проверка цены товара")
    public ProductCard checkProductPrice(String price) {
        productPrice.shouldHave(text(price));
        return this;
    }


}
