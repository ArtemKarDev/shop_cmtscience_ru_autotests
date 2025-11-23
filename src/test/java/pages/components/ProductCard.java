package pages.components;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

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

    public void addToCart() {
        this.basketButton.click();
    }


}
