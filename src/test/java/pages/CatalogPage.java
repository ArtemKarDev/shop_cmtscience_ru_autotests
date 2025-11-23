package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Tag;
import pages.components.ProductCard;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static org.openqa.selenium.By.xpath;

public class CatalogPage {

    private final ElementsCollection productCards = $$(".product-card");
    private final SelenideElement

    filterCheckBoxProtein = $(xpath("//div[@class='mr-filters']//span[text()='Протеин и батончики']")),
    filterCheckBoxOmega = $(xpath("//div[@class='mr-filters']//span[text()='Омега-3']"));

    @Step("Открыть страницу Магазин")
    public CatalogPage openCatalogPage() {
        open("/catalog");
        $(".page-title ").shouldHave(text("Магазин"));
        return this;
    }

    public int countProductCarts() {
        return productCards.size();
    }

    public ProductCard findProductCardByName(String productName) {
        return productCards.stream()
                .map(ProductCard::new)
                .filter(card -> productName.equals(card.getProductName()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Товар '" + productName + "' не найден"));
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
