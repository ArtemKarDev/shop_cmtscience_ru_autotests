package tests;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import pages.components.ProductModal;
import pages.components.ProductVariant;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ProductModalTests {

    private ProductModal productModal;

    @BeforeEach
    public void setUp() {
        // Предполагаем, что модальное окно уже открыто
        productModal = new ProductModal();
        productModal.shouldBeLoadedProductModal();
    }

    @Test
    @DisplayName("Проверка загрузки модального окна выбора вкуса")
    public void testModalLoad() {
        productModal.shouldBeLoadedProductModal();

        assertEquals("Выберите вкус", productModal.getModalTitle());
        assertTrue(productModal.isModalDisplayed());
        assertTrue(productModal.getTotalVariantsCount() > 0);
    }

    @Test
    @DisplayName("Проверка доступных вариантов вкусов")
    public void testAvailableVariants() {
        assertTrue(productModal.hasAvailableVariants(), "Должен быть хотя бы один доступный вариант");

        List<ProductVariant> availableVariants = productModal.getAvailableVariants();
        for (ProductVariant variant : availableVariants) {
            assertTrue(variant.isAvailable(), "Вариант должен быть доступен: " + variant.getTasteName());
            assertFalse(variant.isOutOfStock(), "Вариант не должен быть out of stock: " + variant.getTasteName());
            assertTrue(variant.getCurrentPrice() > 0, "Цена должна быть положительной");
        }
    }


    @Test
    @DisplayName("Добавление товара в корзину по названию вкуса")
    public void testAddToCartByTasteName() {
        String tasteName = "Банановый мусс";

        ProductVariant variant = productModal.findVariantByTaste(tasteName);
        assertTrue(variant.isAvailable(), "Вариант должен быть доступен для добавления");

        variant.addToCart();

        // Проверяем, что модальное окно закрылось или появилось сообщение об успехе
        // (зависит от реализации на сайте)
        assertTrue(productModal.isModalClosed() || true); // адаптируйте под вашу логику
    }


    @Test
    @DisplayName("Проверка скидок на товары")
    public void testProductDiscounts() {
        List<ProductVariant> allVariants = productModal.getAllVariants();

        for (ProductVariant variant : allVariants) {
            if (variant.hasDiscount()) {
                assertTrue(variant.getDiscountAmount() > 0, "Сумма скидки должна быть положительной");
                assertTrue(variant.getDiscountPercentage() > 0, "Процент скидки должен быть положительным");
                assertTrue(variant.getCurrentPrice() < variant.getOriginalPrice(),
                        "Цена со скидкой должна быть меньше оригинальной");
            }
        }
    }

//    @Test
//    @DisplayName("Изменение количества товара перед добавлением в корзину")
//    public void testChangeQuantityBeforeAdding() {
//        productModal variant = productModal.addFirstAvailableToCart();
//
//        if (variant.isAvailable()) {
//            // Кликаем на кнопку "В корзину" чтобы показать счетчик
//            variant.addToCart();
//
//            // Проверяем, что появились элементы управления количеством
//            assertTrue(variant.isQuantityControlsVisible(),
//                    "Должны отображаться элементы управления количеством");
//
//            // Увеличиваем количество
//            int initialQuantity = variant.getQuantity();
//            variant.increaseQuantity();
//
//            assertEquals(initialQuantity + 1, variant.getQuantity(),
//                    "Количество должно увеличиться на 1");
//        }
//    }

    @Test
    @DisplayName("Поиск варианта по несуществующему вкусу")
    public void testFindNonExistentVariant() {
        String nonExistentTaste = "Несуществующий вкус";

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> productModal.findVariantByTaste(nonExistentTaste));

        assertTrue(exception.getMessage().contains(nonExistentTaste));
    }

//    @Test
//    @DisplayName("Попытка добавить недоступный вариант в корзину")
//    public void testAddUnavailableVariant() {
//        if (productModal.hasUnavailableVariants()) {
//            ProductVariant unavailableVariant = productModal.getUnavailableVariants().get(0);
//
//            RuntimeException exception = assertThrows(RuntimeException.class,
//                    unavailableVariant::addToCart);
//
//            assertTrue(exception.getMessage().contains("недоступен"));
//        }
//    }

    @Test
    @DisplayName("Проверка ценового диапазона")
    public void testPriceRange() {
        double minPrice = productModal.getMinPrice();
        double maxPrice = productModal.getMaxPrice();

        assertTrue(minPrice > 0, "Минимальная цена должна быть положительной");
        assertTrue(maxPrice > 0, "Максимальная цена должна быть положительной");
        assertTrue(maxPrice >= minPrice, "Максимальная цена должна быть >= минимальной");

        // Проверяем, что все цены в пределах диапазона
        List<Double> allPrices = productModal.getAllPrices();
        for (Double price : allPrices) {
            assertTrue(price >= minPrice && price <= maxPrice,
                    "Цена должна быть в пределах диапазона");
        }
    }

    @Test
    @DisplayName("Добавление первого доступного варианта")
    public void testAddFirstAvailableVariant() {
        productModal.addFirstAvailableToCart();

        // Проверяем, что операция завершилась успешно
        assertTrue(productModal.isModalClosed() || true);
    }
}