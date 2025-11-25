package pages.components;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

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
