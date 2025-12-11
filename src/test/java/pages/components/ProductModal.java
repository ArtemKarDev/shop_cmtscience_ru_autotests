package pages.components;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import java.util.List;
import java.util.stream.Collectors;
import static com.codeborne.selenide.Selenide.*;

public class ProductModal {

    private final SelenideElement
            modalContainer = $(".container--modal"),
            modalTitle = $("#mfp-product-title"),
            modalCloseButton = $(".mfp-close");

    private final ElementsCollection desktopTableRows = $$("#to-basket-popup-table .tr-bot-items");


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

    public List<ProductVariant> getAllVariants() {
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

    public ProductVariant addFirstAvailableToCart() {
        ProductVariant availableVariant = getAvailableVariants().stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Нет доступных вариантов для добавления в корзину"));
        availableVariant.addToCart();
        return availableVariant;
    }

    public boolean hasAvailableVariants() {
        return !getAvailableVariants().isEmpty();
    }

    public int getTotalVariantsCount() {
        return getAllVariants().size();
    }

    public void waitForModalToClose() {
        modalContainer.should(Condition.disappear);
    }

    public boolean isModalClosed() {
        return !modalContainer.isDisplayed();
    }
}

