package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import java.util.List;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;

public class CartPage {

    private final SelenideElement basketBlock = $("#basket-block");
    private final SelenideElement pageTitle = $(".page-title h1");
    private final SelenideElement basketItemsCount = $(".baks-item-count");
    private final SelenideElement emptyBasketBlock = $("#empty-basket-block");

    // Элементы товаров в корзине
    private final ElementsCollection basketItems = $$(".ml-bask-item");

    // Элементы боковой панели (итоги, промокоды и т.д.)
    private final SelenideElement totalPriceWithoutDiscount = $(".price-without-action");
    private final SelenideElement totalPriceWithDiscount = $(".full-price-discount");
    private final SelenideElement checkoutButton = $("#apply-order");
    private final SelenideElement freeShippingInfo = $(".ml-bot-bar span");

    // Элементы промокода
    private final SelenideElement promoToggle = $(".js-promo-clicker");
    private final SelenideElement promoInput = $("#promo_code");
    private final SelenideElement applyPromoButton = $("#get-promocode");
    private final SelenideElement promoError = $("#promocode-error");
    private final SelenideElement promoSuccess = $("#promocode-success");
    private final SelenideElement currentPromoCode = $("#promocode-name");

    // Элементы бонусных баллов
    private final SelenideElement bonusToggle = $(".js-bonus-clicker");
    private final SelenideElement bonusInput = $("#bonus_points");
    private final SelenideElement applyBonusButton = $("#get-bonus_points");
    private final SelenideElement bonusError = $("#bonus_points-error");
    private final SelenideElement bonusSuccess = $("#bonus_points-success");
    private final SelenideElement availableBonusPoints = $("#bonusPointsAvailable");


    @Step("Открыть страницу Корзина")
    public CartPage openCatalogPage() {
        open("/catalog");
        $(".page-title ").shouldHave(text("Магазин"));
        return this;
    }
    // Методы проверки загрузки страницы
    public CartPage shouldBeLoaded() {
        basketBlock.shouldBe(Condition.visible);
        pageTitle.shouldBe(Condition.visible);
        return this;
    }

    public boolean isPageLoaded() {
        return basketBlock.isDisplayed() && pageTitle.isDisplayed();
    }

    // Методы проверки состояния корзины
    public boolean isEmpty() {
        return emptyBasketBlock.isDisplayed();
    }

    public boolean hasItems() {
        return !isEmpty() && basketItems.size() > 0;
    }

    public int getItemsCount() {
        if (isEmpty()) {
            return 0;
        }
        String countText = basketItemsCount.getText().trim();
        return Integer.parseInt(countText);
    }

    // Методы работы с товарами в корзине
    public List<CartItem> getAllItems() {
        return basketItems.stream()
                .map(CartItem::new)
                .collect(Collectors.toList());
    }

    public CartItem findItemByName(String productName) {
        return getAllItems().stream()
                .filter(item -> productName.equals(item.getProductName()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Товар '" + productName + "' не найден в корзине"));
    }

    public boolean isItemInCart(String productName) {
        return getAllItems().stream()
                .anyMatch(item -> productName.equals(item.getProductName()));
    }

    public void removeItem(String productName) {
        CartItem item = findItemByName(productName);
        item.removeFromCart();
    }

    public void removeAllItems() {
        getAllItems().forEach(CartItem::removeFromCart);
    }

    // Методы работы с ценами и итогами
    public double getTotalPriceWithoutDiscount() {
        return parsePrice(totalPriceWithoutDiscount.getText());
    }

    public double getTotalPriceWithDiscount() {
        return parsePrice(totalPriceWithDiscount.getText());
    }

    public double getDiscountAmount() {
        return getTotalPriceWithoutDiscount() - getTotalPriceWithDiscount();
    }

    public String getFreeShippingThreshold() {
        return freeShippingInfo.getText();
    }

    public boolean isFreeShippingAvailable() {
        return getTotalPriceWithDiscount() >= 2500; // Бесплатная доставка от 2500 руб
    }

    // Методы работы с промокодами
    public void togglePromoSection() {
        promoToggle.click();
    }

    public void applyPromoCode(String promoCode) {
        togglePromoSection();
        promoInput.setValue(promoCode);
        applyPromoButton.click();
    }

    public boolean isPromoApplied() {
        return promoSuccess.isDisplayed();
    }

    public boolean hasPromoError() {
        return promoError.isDisplayed();
    }

    public String getCurrentPromoCode() {
        return currentPromoCode.getText();
    }

    public void removePromoCode() {
        if (!getCurrentPromoCode().isEmpty()) {
            applyPromoCode(""); // Очищаем промокод
        }
    }

    // Методы работы с бонусными баллами
    public void toggleBonusSection() {
        bonusToggle.click();
    }

    public void applyBonusPoints(int points) {
        toggleBonusSection();
        bonusInput.setValue(String.valueOf(points));
        applyBonusButton.click();
    }

    public int getAvailableBonusPoints() {
        String text = availableBonusPoints.getText();
        String points = text.replaceAll("\\D", "");
        return points.isEmpty() ? 0 : Integer.parseInt(points);
    }

    public boolean isBonusApplied() {
        return bonusSuccess.isDisplayed();
    }

    public boolean hasBonusError() {
        return bonusError.isDisplayed();
    }

    // Методы оформления заказа
    public void proceedToCheckout() {
        checkoutButton.click();
    }

    public boolean isCheckoutAvailable() {
        return checkoutButton.isDisplayed() && hasItems();
    }

    // Вспомогательные методы
    private double parsePrice(String priceText) {
        String cleanedPrice = priceText.replaceAll("[^\\d,.]", "").replace(",", ".");
        return Double.parseDouble(cleanedPrice);
    }

    // Метод для ожидания обновления корзины
    public CartPage waitForCartUpdate() {
        sleep(1000); // Краткая пауза для обновления AJAX
        return this;
    }

    // Метод проверки всех основных элементов
    public void verifyCartPageElements() {
        shouldBeLoaded();
        assert basketBlock.isDisplayed() : "Блок корзины не отображается";
        assert pageTitle.isDisplayed() : "Заголовок страницы не отображается";

        if (hasItems()) {
            assert totalPriceWithDiscount.isDisplayed() : "Итоговая цена не отображается";
            assert checkoutButton.isDisplayed() : "Кнопка оформления заказа не отображается";
        }
    }
}

// Вложенный класс для работы с отдельным товаром в корзине
class CartItem {
    private final SelenideElement itemElement;

    // Элементы товара
    private final SelenideElement productImage = $(".basket-img img");
    private final SelenideElement productName = $(".taste");
    private final SelenideElement quantityInput = $(".ml-count");
    private final SelenideElement decreaseButton = $(".mlb-min");
    private final SelenideElement increaseButton = $(".mlb-plus");
    private final SelenideElement itemTotalPrice = $(".position-full-price .value");
    private final SelenideElement itemUnitPrice = $(".position-once-price");
    private final SelenideElement itemOriginalPrice = $(".position-original-price");
    private final SelenideElement removeButton = $(".remove-basket-item");
    private final SelenideElement addToWishlistButton = $(".add-to-boormark");

    public CartItem(SelenideElement itemElement) {
        this.itemElement = itemElement;
    }

    public String getProductName() {
        return productName.getText().trim();
    }

    public int getQuantity() {
        String quantityText = quantityInput.getValue();
        return Integer.parseInt(quantityText);
    }

    public void setQuantity(int quantity) {
        quantityInput.setValue(String.valueOf(quantity));
    }

    public void increaseQuantity() {
        increaseButton.click();
    }

    public void decreaseQuantity() {
        decreaseButton.click();
    }

    public double getUnitPrice() {
        return parsePrice(itemUnitPrice.getText());
    }

    public double getTotalPrice() {
        return parsePrice(itemTotalPrice.getText());
    }

    public double getOriginalPrice() {
        return parsePrice(itemOriginalPrice.getText());
    }

    public boolean hasDiscount() {
        return getUnitPrice() < getOriginalPrice();
    }

    public double getDiscountAmount() {
        return getOriginalPrice() - getUnitPrice();
    }

    public void removeFromCart() {
        removeButton.click();
    }

    public void addToWishlist() {
        addToWishlistButton.click();
    }

    public boolean isInWishlist() {
        // Проверяем, активна ли кнопка добавления в избранное
        return addToWishlistButton.has(Condition.cssClass("active"));
    }

    private double parsePrice(String priceText) {
        String cleanedPrice = priceText.replaceAll("[^\\d,.]", "").replace(",", ".");
        return Double.parseDouble(cleanedPrice);
    }

    public SelenideElement getElement() {
        return itemElement;
    }

}
