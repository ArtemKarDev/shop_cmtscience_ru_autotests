package pages.components;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import java.util.List;
import java.util.stream.Collectors;
import static com.codeborne.selenide.Selenide.*;

public class ProductModal {


    // Основные элементы модального окна
    private final SelenideElement
            modalContainer = $(".container--modal"),
            modalTitle = $("#mfp-product-title"),
            modalCloseButton = $(".mfp-close");


    private final ElementsCollection desktopTableRows = $$("#to-basket-popup-table .tr-bot-items");



    // Методы проверки загрузки модального окна
    public ProductModal shouldBeLoadedProductModal() {
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

    public ProductVariant findVariantByTaste(String tasteName) {
        return getAllVariants().stream()
                .filter(variant -> tasteName.equals(variant.getTasteName()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Вариант с вкусом '" + tasteName + "' не найден"));
    }

    public void closeModal() {
        modalCloseButton.click();
    }

    // Методы добавления в корзину
    public void addToCartByTaste(String tasteName) {
        ProductVariant variant = findVariantByTaste(tasteName);
        variant.addToCart();
    }

    public ProductVariant addFirstAvailableToCart() {
        ProductVariant availableVariant = getAvailableVariants().stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Нет доступных вариантов для добавления в корзину"));
        availableVariant.addToCart();
        return availableVariant;
    }

    // Методы проверки состояний
    public boolean hasAvailableVariants() {
        return !getAvailableVariants().isEmpty();
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

