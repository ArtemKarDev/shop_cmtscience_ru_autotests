package tests;

import io.qameta.allure.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import pages.CartPage;
import pages.CatalogPage;
import pages.ProductPage;
import pages.components.*;

import java.util.function.Consumer;
import java.util.stream.Stream;

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

    record ProductFilterTestParams(String productName, Consumer<CatalogPage> filterAction) {}
    static Stream<Arguments> provideProductsAndFilters() {
        return Stream.of(
                Arguments.of(new ProductFilterTestParams("Протеин", CatalogPage::applyFilterProtein)),
                Arguments.of(new ProductFilterTestParams("Омега", CatalogPage::applyFilterOmega))
        );
    }

    @ParameterizedTest
    @CsvSource({
            "Протеин сывороточный 80% белка (60 порций), 1799",
            "Биотин витамин B7 (5000 мкг), 367",
    })
    @Feature("Карточка товара в магазине")
    @Story("Выбор продукта")
    @Owner("KarlashovArtem")
    @Severity(SeverityLevel.BLOCKER)
    @Tag("smoke")
    @DisplayName("Выбор товара по наименованию и проверка данных")
    public void testFindProductByNameAndVerifyData(String productName, String price){
        catalogPage.openCatalogPage()
                .findProductCardByPartialName(productName)
                .checkProductPrice(price);
    }


    @ParameterizedTest(name = "Товар с названием: {0}")
    @ValueSource(strings = {"Биотин", "Креатин", "Протеин"})
    @Tag("smoke")
    @DisplayName("Добавление {} в корзину через модальное окно выбора вкуса")
    public void testAddMultipleProductsToBasket(String partialProductName) {
        catalogPage = catalogPage.openCatalogPage();

        ProductCard card = catalogPage
                .findProductCardByPartialName(partialProductName);

        ProductModal modal = card
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

    @ParameterizedTest(name = "Товар с названием: {0}")
    @MethodSource("provideProductsAndFilters")
    @Tag("smoke")
    @DisplayName("Проверка наличия товаров в каталоге с фильром")
    public void testFindProductWithFilter(ProductFilterTestParams params) {
        catalogPage = catalogPage
                .openCatalogPage();
        params.filterAction.accept(catalogPage);

        ProductCard card = catalogPage
                .findProductCardByPartialName(params.productName());

        catalogPage.verifyProductIsVisible(card.getProductName());

    }

    @ParameterizedTest
    @Tag("smoke")
    @ValueSource(strings = {"Рыбный жир Омега-3 900 мг"})
    @DisplayName("Переход на страницу товара из каталога")
    public void testNavigateToProductPageFromCatalog(String productName) {
        catalogPage.openCatalogPage()
                .findProductCardByPartialName(productName)
                .goToProductPage()
                .checkProductTitle(productName);

    }



}
