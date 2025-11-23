package tests;

import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import pages.CartPage;
import pages.CatalogPage;
import pages.ProductPage;
import pages.components.ProductCard;
import pages.components.ProductModal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CatalogPageTests extends TestBase{

    private CatalogPage catalogPage;
    private ProductPage productPage;
    private ProductModal productModal;


    @Test// @ParameterizedTest(value = "")
    @Feature("Картоска товара с магазине")
    @Story("Выбор продукта")
    @Owner("KarlashovArtem")
    @Severity(SeverityLevel.BLOCKER)
    @Tag("smoke")
    @DisplayName("Выбор товара по наименованию и проверка данных")
    public void testFindProductByNameAndVerifyData(String productName, String price){
        catalogPage.openCatalogPage()
                .findProductCardByName(productName)
                .checkProductPrice(price);
    }


    @Test
    @DisplayName("Добавление нескольких товаров в корзину")
    public void testAddMultipleProductsToBasket(String productName) {
        catalogPage.openCatalogPage()
                .findProductCardByName(productName)
                .addToCart();
        productModal.addFirstAvailableToCart();


    }

    @Test
    @Tag("smoke")
    @DisplayName("Переход на страницу товара из каталога")
    public void testNavigateToProductPageFromCatalog(String productName) {
        catalogPage.openCatalogPage()
                .findProductCardByName(productName)
                .goToProductPage();

        productPage.checkProductTitle(productName);

    }

    @Test
    @DisplayName("Добавление товара в корзину из каталога")
    public void testAddProductToBasketFromCatalog() {

        String productName = "Протеин";
        CartPage cartPage = new CartPage(); // Предполагаем существование класса корзины

        ProductCard productCard = catalogPage.findProductCardByName(productName);
        String productPrice = productCard.getProductPrice();
        productCard.addToCart();

        assertTrue(cartPage.isProductInBasket(productName),
                "Товар должен быть добавлен в корзину");
        assertEquals(productPrice, basketPage.getProductPriceInBasket(productName),
                "Цена товара в корзине должна совпадать с ценой в каталоге");
    }

    @Test
    @DisplayName("Проверка количества товаров в корзине")
    public void testBasketItemsCount() {
        int itemsCount = header.getBasketItemsCount();
        assertTrue(itemsCount >= 0, "Количество товаров в корзине не может быть отрицательным");

        if (itemsCount > 0) {
            assertFalse(header.isBasketEmpty(), "Корзина не должна быть пустой");
        }
    }


    @Test
    @DisplayName("Проверка наличия товаров в каталоге с фильром")
    public void testCatalogNotEmptyAndBasicStructure() {}

}
