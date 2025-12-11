package tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pages.RegisterPage;

public class RegisterTests extends BaseTest {

    private final RegisterPage registerPage = new RegisterPage();

    @Test
    @Tag("smoke")
    @DisplayName("Попытка регистрации — появление ошибки капчи (антибот)")
    void registrationShouldShowCaptcha() {
        registerPage.openRegisterPage()
                .setFirstName("Иван")
                .setLastName("Иванов")
                .setPatronymic("Иванович")
                .setEmail("ivan.ivanov1@example.com")
                .setPassword("Password123")
                .setPasswordConfirm("Password123")
                .submitClick();

        registerPage.checkCaptchaError();
    }



}
