package pages.components;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import java.util.List;
import java.util.stream.Collectors;
import static com.codeborne.selenide.Selenide.*;

public class ProductModal {


    // Основные элементы модального окна
    private final SelenideElement modalContainer = $(".container--modal");
    private final SelenideElement modalTitle = $("#mfp-product-title");

    // Элементы десктопной версии таблицы
    private final SelenideElement desktopTable = $("#to-basket-popup-table");
    private final ElementsCollection desktopTableRows = $$("#to-basket-popup-table .tr-bot-items");

    // Элементы мобильной версии таблицы
    private final SelenideElement mobileTable = $("#to-basket-popup-table-mobile");
    private final ElementsCollection mobileTableRows = $$("#to-basket-popup-table-mobile .custom-row");

    // Методы проверки загрузки модального окна
    public ProductModal shouldBeLoaded() {
        modalContainer.shouldBe(Condition.visible);
        modalTitle.shouldBe(Condition.visible);
        return this;
    }

    public boolean isModalDisplayed() {
        return modalContainer.isDisplayed() && modalTitle.isDisplayed();
    }

    public String getModalTitle() {
        return modalTitle.getText();
    }

    // Методы работы с вариантами товара
    public List<ProductVariant> getAllVariants() {
        // Используем десктопную версию по умолчанию
        return desktopTableRows.stream()
                .map(ProductVariant::new)
                .collect(Collectors.toList());
    }

    public List<ProductVariant> getAvailableVariants() {
        return getAllVariants().stream()
                .filter(ProductVariant::isAvailable)
                .collect(Collectors.toList());
    }

    public List<ProductVariant> getUnavailableVariants() {
        return getAllVariants().stream()
                .filter(variant -> !variant.isAvailable())
                .collect(Collectors.toList());
    }

    public ProductVariant findVariantByTaste(String tasteName) {
        return getAllVariants().stream()
                .filter(variant -> tasteName.equals(variant.getTasteName()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Вариант с вкусом '" + tasteName + "' не найден"));
    }

    public ProductVariant findVariantByAlias(String alias) {
        return getAllVariants().stream()
                .filter(variant -> alias.equals(variant.getAlias()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Вариант с alias '" + alias + "' не найден"));
    }

    // Методы добавления в корзину
    public void addToCartByTaste(String tasteName) {
        ProductVariant variant = findVariantByTaste(tasteName);
        variant.addToCart();
    }

    public void addToCartByAlias(String alias) {
        ProductVariant variant = findVariantByAlias(alias);
        variant.addToCart();
    }

    public void addFirstAvailableToCart() {
        ProductVariant availableVariant = getAvailableVariants().stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Нет доступных вариантов для добавления в корзину"));
        availableVariant.addToCart();
    }

    // Методы проверки состояний
    public boolean hasAvailableVariants() {
        return !getAvailableVariants().isEmpty();
    }

    public boolean hasUnavailableVariants() {
        return !getUnavailableVariants().isEmpty();
    }

    public int getTotalVariantsCount() {
        return getAllVariants().size();
    }

    public int getAvailableVariantsCount() {
        return getAvailableVariants().size();
    }

    // Методы работы с ценами
    public List<Double> getAllPrices() {
        return getAllVariants().stream()
                .map(ProductVariant::getCurrentPrice)
                .collect(Collectors.toList());
    }

    public double getMinPrice() {
        return getAllPrices().stream()
                .min(Double::compare)
                .orElse(0.0);
    }

    public double getMaxPrice() {
        return getAllPrices().stream()
                .max(Double::compare)
                .orElse(0.0);
    }

    // Вспомогательные методы
    public void waitForModalToClose() {
        modalContainer.should(Condition.disappear);
    }

    public boolean isModalClosed() {
        return !modalContainer.isDisplayed();
    }
}

// Вложенный класс для работы с отдельным вариантом товара
public class ProductVariant {
    private final SelenideElement variantElement;

    // Элементы варианта товара (десктопная версия)
    private final SelenideElement tasteNameElement = $(".tr_text_block");
    private final SelenideElement priceElement = $("td[data-td='Цена']");
    private final SelenideElement originalPriceElement = $("s");
    private final SelenideElement currentPriceElement = $("td[data-td='Цена'] b");
    private final SelenideElement addToCartButton = $(".tr__add_row .btn--colored");
    private final SelenideElement outOfStockButton = $(".tr__out-of-stock-btn");
    private final SelenideElement quantityInput = $(".ml-count");
    private final SelenideElement decreaseButton = $(".mlb-min");
    private final SelenideElement increaseButton = $(".mlb-plus");
    private final SelenideElement addRow = $(".tr__add_row");
    private final SelenideElement numRow = $(".tr__num_row");

    public ProductVariant(SelenideElement variantElement) {
        this.variantElement = variantElement;
    }

    public String getTasteName() {
        return variantElement.$(".tr_text_block").getText().trim();
    }

    public String getAlias() {
        return variantElement.getAttribute("data-alias");
    }

    public double getOriginalPrice() {
        if (originalPriceElement.exists()) {
            return parsePrice(originalPriceElement.getText());
        }
        return getCurrentPrice();
    }

    public double getCurrentPrice() {
        return parsePrice(currentPriceElement.getText());
    }

    public boolean hasDiscount() {
        return originalPriceElement.exists() && getOriginalPrice() > getCurrentPrice();
    }

    public double getDiscountAmount() {
        return getOriginalPrice() - getCurrentPrice();
    }

    public double getDiscountPercentage() {
        if (!hasDiscount()) return 0;
        return ((getOriginalPrice() - getCurrentPrice()) / getOriginalPrice()) * 100;
    }

    public boolean isAvailable() {
        return addToCartButton.exists() && addToCartButton.isEnabled();
    }

    public boolean isOutOfStock() {
        return outOfStockButton.exists();
    }

    // Методы работы с количеством и добавлением в корзину
    public void addToCart() {
        if (!isAvailable()) {
            throw new RuntimeException("Вариант '" + getTasteName() + "' недоступен для добавления в корзину");
        }
        addToCartButton.click();
    }

    public void setQuantity(int quantity) {
        if (!isAvailable()) {
            throw new RuntimeException("Нельзя изменить количество для недоступного варианта");
        }

        // Если кнопка "В корзину" видна, кликаем на нее чтобы показать счетчик
        if (addRow.isDisplayed()) {
            addToCartButton.click();
        }

        quantityInput.setValue(String.valueOf(quantity));
    }

    public int getQuantity() {
        if (numRow.isDisplayed() && quantityInput.exists()) {
            String quantityText = quantityInput.getValue();
            return Integer.parseInt(quantityText);
        }
        return 0;
    }

    public void increaseQuantity() {
        if (numRow.isDisplayed()) {
            increaseButton.click();
        }
    }

    public void decreaseQuantity() {
        if (numRow.isDisplayed()) {
            decreaseButton.click();
        }
    }

    public boolean isQuantityControlsVisible() {
        return numRow.isDisplayed();
    }

    public boolean isAddButtonVisible() {
        return addRow.isDisplayed();
    }

    // Вспомогательные методы
    private double parsePrice(String priceText) {
        String cleanedPrice = priceText.replaceAll("[^\\d,.]", "").replace(",", ".");
        return Double.parseDouble(cleanedPrice);
    }

    public SelenideElement getElement() {
        return variantElement;
    }

    @Override
    public String toString() {
        return String.format("ProductVariant{ taste='%s', price=%.2f, available=%s }",
                getTasteName(), getCurrentPrice(), isAvailable());
    }



}
