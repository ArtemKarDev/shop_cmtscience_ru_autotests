package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

public class ProductPage {

    private final SelenideElement
    productTitle = $(".page-title");

    @Step("Проверка заголовка страницы продукта")
    public ProductPage checkProductTitle(String productName){
        productTitle.shouldHave(Condition.text(productName));
        return this;
    }

}
