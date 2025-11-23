package tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.CartPage;
import pages.CatalogPage;
import pages.components.ProductCard;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CatalogPageTests {

    private CatalogPage catalogPage;


    @Test
    @DisplayName("Поиск товара по наименованию и проверка данных")
    public void testFindProductByNameAndVerifyData(){}

    @Test
    @DisplayName("Поиск товара по наименованию")
    public void testFindProductByPartialName() {}

    @Test
    @DisplayName("Добавление нескольких товаров в корзину")
    public void testAddMultipleProductsToBasket() {}

    @Test
    @DisplayName("Переход на страницу товара из каталога")
    public void testNavigateToProductPageFromCatalog() {}

    @Test
    @DisplayName("Добавление товара в корзину из каталога")
    public void testAddProductToBasketFromCatalog() {

        String productName = "Samsung Galaxy S21";
        CartPage basketPage = new CartPage(); // Предполагаем существование класса корзины

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
