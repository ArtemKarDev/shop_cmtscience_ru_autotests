package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import pages.components.ProductCard;

import java.util.List;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static org.openqa.selenium.By.xpath;

public class CatalogPage {

    private final ElementsCollection productCards = $$(".ml-buy-item");
    private final SelenideElement

    filterCheckBoxProtein = $(xpath("//div[@class='mr-filters']//span[text()='Протеин и батончики']")),
    filterCheckBoxOmega = $(xpath("//div[@class='mr-filters']//span[text()='Омега-3']"));

    @Step("Открыть страницу Магазин")
    public CatalogPage openCatalogPage() {
        open("/catalog");
        $(".page-title ").shouldHave(text("Магазин"));
        return this;
    }

    @Step("Поиск товара по названию")
    public ProductCard findProductCardByName(String productName) {
        return productCards.stream()
                .map(ProductCard::new)
                .filter(card -> productName.equals(card.getProductName()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Товар '" + productName + "' не найден"));
    }

    @Step("Поиск товара по частичному названию")
    public ProductCard findProductCardByPartialName(String name) {
        return productCards.stream()
                .map(ProductCard::new)
                .filter(card -> card.getProductName().contains(name))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Товар '" + name + "' не найден"));
    }

    @Step("Поиск всех товаров по частичному названию")
    public List<ProductCard> findAllProductByPartialName(String name) {
        return productCards
                .asFixedIterable()
                .stream()
                .map(ProductCard::new)
                .filter(productCard -> productCard.getProductName().toLowerCase()
                        .contains(name.toLowerCase()))
                .toList();
    }

    public boolean isProductCardPresent(String productName) {
        if (productCards.stream()
                .map(ProductCard::new)
                .anyMatch(card -> productName.equals(card.getProductName()))) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isProductCardNotPresent(String productName) {
        if (productCards.stream()
                .map(ProductCard::new)
                .anyMatch(card -> productName.equals(card.getProductName()))) {
            return false;
        } else {
            return true;
        }
    }

    public int getProductCardCount() {
        return productCards.size();
    }

    @Step("Применить фильтр Протеин")
    public void applyFilterProtein() {
        filterCheckBoxProtein.click();
    }

    @Step("Применить фильтр Омега")
    public void applyFilterOmega() {
        filterCheckBoxOmega.click();
    }


}
