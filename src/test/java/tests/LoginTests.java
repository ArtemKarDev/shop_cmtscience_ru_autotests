package tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pages.CatalogPage;
import pages.LoginModalForm;
import pages.components.HeaderContainer;



public class LoginTests extends BaseTest {

    private CatalogPage catalogPage;
    private LoginModalForm loginModalForm;
    private HeaderContainer headerContainer;
    @BeforeEach
    void setUp() {
        catalogPage = new CatalogPage();
        catalogPage.openCatalogPage();
        headerContainer = new HeaderContainer();
        loginModalForm = new LoginModalForm();

    }
    @Test
    @Tag("smoke")
    @DisplayName("Успешный логин через модальное окно")
    void successfulLoginTest() {
        catalogPage.openCatalogPage();
        if (loginModalForm.shouldBeHidden()){} else {loginModalForm.closeModal();}

        headerContainer.verifyUserNotLoggedIn();
        headerContainer.clickLoginButton();
        loginModalForm.shouldBeVisible()
                .setEmail("ivanIvanov@mail.ru")
                .setPassword("ivanIvanov")
                .clickLoginButton();
        headerContainer.verifyUserLoggedIn();
    }

    @Test
    @DisplayName("Логин с неверными данными")
    void loginWithInvalidCredentials() {
        catalogPage.openCatalogPage();
        if (loginModalForm.shouldBeHidden()){} else {loginModalForm.closeModal();}
        headerContainer.clickLoginButton();
        loginModalForm.shouldBeVisible()
                .shouldBeVisible()
                .setEmail("peterIvanov@mail.ru")
                .setPassword("ivanIvanov")
                .clickLoginButton();
        loginModalForm.verifyErrorMessage("Неправильный email или пароль.");
    }

}
