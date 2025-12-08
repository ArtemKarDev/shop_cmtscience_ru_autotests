package pages.components;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class ProductCard {

    private final SelenideElement rootElement,
            productName,
            addToCartButton,
            productPrice,
            productLink;

    public ProductCard(SelenideElement cardElement) {
        this.rootElement = cardElement;
        this.productName = rootElement.$(".product__desc-wrap a");
        this.addToCartButton = rootElement.$(".to-basket-desktop");
        this.productPrice = rootElement.$(".product__buy-wrap div span");
        this.productLink = rootElement.$(".product__link");
    }


    public String getProductName() {
        return rootElement
                .shouldBe(Condition.visible)
                .text();
    }

    @Step("Получить цену товара")
    public String getProductPrice() {
        return productPrice
                .shouldBe(Condition.visible)
                .text()
                .replaceAll("\\s+", "")
                .trim();
    }

    @Step("Перейти на страницу товара")
    public void goToProductPage() {
        productLink.click();
    }

    @Step("Проверка кнопки Корзина")
    public boolean isAddToCartEnabled() {
        return addToCartButton.isEnabled();
    }

    @Step("Добавить товар в корзину")
    public ProductModal clickAddToCart() {
        this.addToCartButton.click();
        return new ProductModal();
    }
    @Step("Проверка цены товара")
    public ProductCard checkProductPrice(String price) {
        productPrice.shouldHave(text(price));
        return this;
    }


}
