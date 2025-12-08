package tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pages.CatalogPage;
import pages.LoginModalForm;
import pages.components.HeaderContainer;

public class LoginTests {

    private CatalogPage catalogPage;
    private LoginModalForm loginModalForm;
    private HeaderContainer headerContainer;

    @Test
    @Tag("smoke")
    @DisplayName("Успешный логин через модальное окно")
    void successfulLoginTest() {
        // 1. Открываем главную страницу
        catalogPage = catalogPage.openCatalogPage();
        loginModalForm.shouldBeVisible().closeModal();
        headerContainer.verifyUserNotLoggedIn();
        headerContainer.clickLogo();
        loginModalForm.shouldBeVisible()
                .setEmail("ivanIvanov@mail.ru")
                .setPassword("ivanIvanov")
                .clickLoginButton();
        headerContainer.verifyUserLoggedIn();

    }

}
