package pages.components;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class ProductCard {

    private final SelenideElement cardElement;

    // Конструктор теперь только сохраняет элемент
    public ProductCard(SelenideElement cardElement) {
        this.cardElement = cardElement;
    }

    // Методы для получения элементов ОТНОСИТЕЛЬНО cardElement
    private SelenideElement getProductNameElement() {
        return cardElement.$(".product__desc-wrap a");
    }

    private SelenideElement getAddToCartElement() {
        return cardElement.$(".to-basket-desktop");
    }

    private SelenideElement getPriceElement() {
        return cardElement.$(".product__buy-wrap div span");
    }

    private SelenideElement getLinkElement() {
        return cardElement.$(".product__link");
    }

//    private final SelenideElement
//            productName,
//            addToCartButton,
//            productPrice,
//            productLink;
//
//    public ProductCard(SelenideElement cardElement) {
//        this.productName = $(".product__desc-wrap a");
//        this.addToCartButton = $(".to-basket-desktop");
//        this.productPrice = $(".product__buy-wrap div span");
//        this.productLink = $(".product__link");
//    }


    public String getProductName() {
        try {
            return getProductNameElement()
                    .shouldBe(visible, Duration.ofSeconds(3))
                    .getText()
                    .trim();
        } catch (Exception e) {
            // Если не видно, пробуем получить без ожидания
            return getProductNameElement().getText().trim();
        }

    }

    @Step("Получить цену товара")
    public String getProductPrice() {
        return getPriceElement()
                //.shouldBe(Condition.visible)
                .getText()
                .replaceAll("\\s+", "")
                .trim();
    }

    @Step("Перейти на страницу товара")
    public void goToProductPage() {
        getLinkElement().click();
    }

    @Step("Проверка кнопки Корзина")
    public boolean isAddToCartEnabled() {
        return getAddToCartElement().isEnabled();
    }

    @Step("Добавить товар в корзину")
    public ProductModal clickAddToCart() {
        this.getAddToCartElement().click();
        return new ProductModal();
    }
    @Step("Проверка имени товара")
    public boolean checkProductName(String name) {
        return getProductNameElement().shouldHave(text(name)).getText().contains(name);
    }
    @Step("Проверка цены товара")
    public boolean checkProductPrice(String price) {
        return getPriceElement().shouldHave(text(price)).getText().contains(price);
    }


    public SelenideElement getCardElement() {
        return getProductNameElement();
    }
}
