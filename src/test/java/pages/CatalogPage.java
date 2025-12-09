package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import pages.components.ProductCard;

import java.util.List;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static org.openqa.selenium.By.xpath;

public class CatalogPage {

    private ElementsCollection getProductCardElements() {
        return $$(".ml-buy-item").filter(visible);
    }

    private List<ProductCard> getProductCards() {
        return getProductCardElements()
                .asFixedIterable()
                .stream()
                .map(ProductCard::new)
                .collect(Collectors.toList());
    }
    private final SelenideElement
    filterCheckBoxProtein = $(xpath("//div[@class='mr-filters']//span[text()='Протеин и батончики']")),
    filterCheckBoxOmega = $(xpath("//div[@class='mr-filters']//span[text()='Омега-3']"));


    @Step("Открыть страницу Магазин")
    public CatalogPage openCatalogPage() {
        open("/catalog");
        $(".page-title ").shouldHave(text("Магазин"));
        return this;
    }


    @Step("Поиск товара по частичному названию")
    public ProductCard findProductCardByPartialName(String partialName) {

        System.out.println("Ищем товар содержащий: '" + partialName + "'");
        System.out.println("Всего товаров на странице: " + getProductCards().size());

        for (ProductCard card : getProductCards()) {
            String productName = card.getProductName();
            System.out.println("Проверяем: '" + productName + "' содержит '" + partialName + "'?");

        if (productName.contains(partialName)) {
            System.out.println("НАЙДЕН: " + productName);
            scrollToProductCard(card);
            return card;
        }
    }

        throw new RuntimeException("Товар содержащий '" + partialName + "' не найден. " +
                "Всего товаров на странице: " + getProductCardElements().size());
    }
    @Step("Скроллить до элемента")
    public CatalogPage scrollToProductCard(ProductCard card) {
        executeJavaScript(
                "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});",
                card.getCardElement()
        );
        sleep(300); // Небольшая пауза для стабилизации
        return this;
    }

    @Step("Скроллить вниз")
    private void scrollDown() {
        executeJavaScript("window.scrollBy(0, 800);");
    }

    @Step("Поиск всех товаров по частичному названию")
    public List<ProductCard> findAllProductsByPartialName(String partialName) {

        return getProductCards()
                .stream()
                .filter(card -> card.getProductName().contains(partialName))
                .collect(Collectors.toList());
    }

    @Step("Проверить, что товар с названием '{productName}' отображается")
    public CatalogPage verifyProductIsVisible(String productName) {
        boolean found = getProductCards().stream()
                .anyMatch(card -> {
                    String cardName = card.getProductName();
                    if (cardName.contains(productName)) {
                        scrollToProductCard(card);
                        card.getCardElement().shouldBe(visible.because("Ожидалось, что товар "+productName+" будет виден"));
                        return true;
                    }
                    return false;
                });

        if (!found) {
            throw new AssertionError("Товар с названием '" + productName + "' не найден среди видимых элементов.");
        }
        return this;
    }



    @Step("Применить фильтр Протеин")
    public CatalogPage applyFilterProtein() {
        filterCheckBoxProtein.click();
        return this;
    }

    @Step("Применить фильтр Омега")
    public CatalogPage applyFilterOmega() {
        filterCheckBoxOmega.click();
        return this;
    }


}
