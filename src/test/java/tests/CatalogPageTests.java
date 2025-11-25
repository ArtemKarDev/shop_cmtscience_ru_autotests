package tests;

import com.codeborne.selenide.Condition;
import io.qameta.allure.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import pages.CartPage;
import pages.CatalogPage;
import pages.ProductPage;
import pages.components.*;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CatalogPageTests extends TestBase{

    private CatalogPage catalogPage;
    private ProductPage productPage;
    private ProductModal productModal;
    private ProductVariant productVariant;
    private HeaderContainer header;
    private FooterContainer footer;
    private CartPage cartPage;
    @BeforeEach
    void setUp() {
        catalogPage = new CatalogPage();
        catalogPage.openCatalogPage();
        header = new HeaderContainer();
        footer = new FooterContainer();
    }



    @ParameterizedTest
    @CsvSource({
            "Протеин, 1500",
            "Креатин, 800",
            "BCAA, 1200"
    })
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



    @ParameterizedTest(name = "Товар с названием: {0}")
    @ValueSource(strings = {"Протеин", "Креатин"})
    @DisplayName("Добавление {} в корзину через модальное окно выбора вкуса")
    public void testAddMultipleProductsToBasket(String partialProductName) {

        ProductModal modal = catalogPage
                .findProductCardByPartialName(partialProductName)
                .clickAddToCart()
                .shouldBeLoadedProductModal();

        ProductVariant variant = modal.addFirstAvailableToCart();
        variant.increaseQuantity();
        variant.increaseQuantity();

        modal.closeModal();
        modal.waitForModalToClose();
        modal.isModalClosed();
        header.checkCountToCart(3);

    }

//    @Test
//    @Tag("smoke")
//    @DisplayName("Переход на страницу товара из каталога")
//    public void testNavigateToProductPageFromCatalog(String productName) {
//        catalogPage.openCatalogPage()
//                .findProductCardByName(productName)
//                .goToProductPage();
//
//        productPage.checkProductTitle(productName);
//
//    }
//
//    @Test
//    @DisplayName("Добавление товара в корзину из каталога")
//    public void testAddProductToBasketFromCatalog() {
//
//        String productName = "Протеин";
//        CartPage cartPage = new CartPage(); // Предполагаем существование класса корзины
//
//        ProductCard productCard = catalogPage.findProductCardByName(productName);
//        String productPrice = productCard.getProductPrice();
//        productCard.clickAddToCart();
//
//        assertTrue(cartPage.isProductInBasket(productName),
//                "Товар должен быть добавлен в корзину");
//        assertEquals(productPrice, basketPage.getProductPriceInBasket(productName),
//                "Цена товара в корзине должна совпадать с ценой в каталоге");
//    }
//
//    @Test
//    @DisplayName("Проверка количества товаров в корзине")
//    public void testBasketItemsCount() {
//        int itemsCount = header.getBasketItemsCount();
//        assertTrue(itemsCount >= 0, "Количество товаров в корзине не может быть отрицательным");
//
//        if (itemsCount > 0) {
//            assertFalse(header.isBasketEmpty(), "Корзина не должна быть пустой");
//        }
//    }
//
//
//    @Test
//    @DisplayName("Проверка наличия товаров в каталоге с фильром")
//    public void testCatalogNotEmptyAndBasicStructure() {}

}
