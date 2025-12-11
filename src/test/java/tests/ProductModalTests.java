package tests;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import pages.CatalogPage;
import pages.components.HeaderContainer;
import pages.components.ProductCard;
import pages.components.ProductModal;
import pages.components.ProductVariant;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ProductModalTests extends BaseTest {

    private ProductModal productModal;
    private CatalogPage catalogPage;
    private HeaderContainer header;

    @BeforeEach
    public void setUp() {
        catalogPage = new CatalogPage();
        productModal = new ProductModal();
        header = new HeaderContainer();
    }


    @Tag("smoke")
    @ParameterizedTest(name = "Товар с названием: {0}")
    @ValueSource(strings = {"Биотин"})
    @DisplayName("Проверка загрузки модального окна выбора вкуса")
    public void testModalLoad(String partialProductName) {
        catalogPage = catalogPage.openCatalogPage();

        catalogPage
                .findProductCardByPartialName(partialProductName)
                .clickAddToCart()
                .shouldBeLoadedProductModal();

        assertTrue(productModal.isModalDisplayed());
        assertEquals("Выберите количество", productModal.getModalTitle());
        assertTrue(productModal.getTotalVariantsCount() > 0);
    }

    @ParameterizedTest(name = "Товар с названием: {0}")
    @Tag("smoke")
    @ValueSource(strings = {"Биотин"})
    @DisplayName("Проверка доступных вариантов вкусов")
    public void testAvailableVariants(String partialProductName) {

        catalogPage = catalogPage.openCatalogPage();

        catalogPage
                .findProductCardByPartialName(partialProductName)
                .clickAddToCart()
                .shouldBeLoadedProductModal();

        assertTrue(productModal.hasAvailableVariants(), "Должен быть хотя бы один доступный вариант");

        List<ProductVariant> availableVariants = productModal.getAvailableVariants();
        for (ProductVariant variant : availableVariants) {
            assertTrue(variant.isAvailable(), "Вариант должен быть доступен: " + variant.getTasteName());
            assertFalse(variant.isOutOfStock(), "Вариант не должен быть out of stock: " + variant.getTasteName());
            assertTrue(variant.getCurrentPrice() > 0, "Цена должна быть положительной");
        }
    }

    @Tag("smoke")
    @ParameterizedTest(name = "Товар с названием: {0}")
    @ValueSource(strings = {"Протеин"})
    @DisplayName("Изменение количества товара c 1 на 0 - появляется кнопка Корзина")
    public void testChangeQuantityBeforeAdding(String partialProductName) {

        catalogPage = catalogPage.openCatalogPage();

        ProductCard card = catalogPage
                .findProductCardByPartialName(partialProductName);

        ProductModal modal = card
                .clickAddToCart()
                .shouldBeLoadedProductModal();

        ProductVariant variant = modal.addFirstAvailableToCart();
        variant.decreaseQuantity();

        modal.closeModal();
        modal.waitForModalToClose();
        modal.isModalClosed();
        header.checkCountToCart(0);
    }

}