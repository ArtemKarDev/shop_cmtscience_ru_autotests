package pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.page;

public class LoginModalForm {
    private final SelenideElement
            modalForm = $(".container--modal"),
            modalTittle = $(".title-tiny"),
            emailField = $(".email"),
            passwordField = $(".password"),
            submitButton = $(".btn__def"),
            closeButton = $(".mfp-close"),
            helpBlock = $(".help-block"),
            buttonResetPassword = $("a[href='https://cmtscience.ru/password/reset']"),
            buttonGoToRegister = $("a[href='https://cmtscience.ru/register']");

    @Step("Проверить видимость модального окна логина")
    public LoginModalForm shouldBeVisible() {
        modalForm.shouldBe(visible);
        emailField.shouldBe(visible);
        passwordField.shouldBe(visible);
        submitButton.shouldBe(visible);
        return this;
    }

    @Step("Проверить, что модальное окно закрыто")
    public LoginModalForm shouldBeHidden() {
        modalForm.shouldNotBe(visible);
        return this;
    }

    @Step("Заполнить поле email: {email}")
    public LoginModalForm setEmail(String email) {
        emailField.shouldBe(visible, enabled)
                .setValue(email);
        return this;
    }

    @Step("Заполнить поле пароль")
    public LoginModalForm setPassword(String password) {
        passwordField.shouldBe(visible, enabled)
                .setValue(password);
        return this;
    }

    @Step("Нажать кнопку 'Войти'")
    public void clickLoginButton() {
        submitButton.shouldBe(enabled).click();
    }

    @Step("Закрыть модальное окно")
    public void closeModal() {
        closeButton.shouldBe(visible).click();
        modalForm.shouldNotBe(visible);
    }

    @Step("Проверить сообщение об ошибке")
    public LoginModalForm verifyErrorMessage(String expectedError) {
        helpBlock.shouldBe(visible)
                .shouldHave(text(expectedError));
        return this;
    }
}
