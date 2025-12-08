package pages.components;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class ProductCard {

    private final SelenideElement
            productName,
            addToCartButton,
            productPrice,
            productLink;

    public ProductCard(SelenideElement cardElement) {
        this.productName = $(".product__desc-wrap a");
        this.addToCartButton = $(".to-basket-desktop");
        this.productPrice = $(".product__buy-wrap div span");
        this.productLink = $(".product__link");
    }


    public String getProductName() {
        return productName
                .shouldBe(Condition.visible)
                .getText();
    }

    @Step("Получить цену товара")
    public String getProductPrice() {
        return productPrice
                .shouldBe(Condition.visible)
                .getText()
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
    @Step("Проверка имени товара")
    public boolean checkProductName(String name) {
        return productName.shouldHave(text(name)).getText().contains(name);
    }
    @Step("Проверка цены товара")
    public boolean checkProductPrice(String price) {
        return productPrice.shouldHave(text(price)).getText().contains(price);
    }


}
