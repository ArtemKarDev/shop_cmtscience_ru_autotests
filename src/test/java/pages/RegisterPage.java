package pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class RegisterPage {

    private final SelenideElement
            firstNameInput = $("#firstName"),
            lastNameInput = $("#lastName"),
            emailInput = $("#email"),
            passwordInput = $("#password"),
            passwordConfirmInput = $("#password-confirm"),
            submitButton = $("#submit");


    @Step("Открыть страницу регистрационной формы")
    public RegisterPage openRegisterPage() {
        open("/register");
        //executeJavaScript("$('#fixban').remove()");
        //executeJavaScript("$('footer').remove()");
        $(".common-form").shouldHave(text("Регистрация"));
        return this;
    }

    @Step("Вввести имя пользователя {value}.")
    public RegisterPage setFirstName(String value) {
        firstNameInput.sendKeys(value);
        return this;
    }

    @Step("Вввести фамилию пользователя {value}.")
    public RegisterPage setLastName(String value) {
        lastNameInput.sendKeys(value);
        return this;
    }

    @Step("Вввести email {value}.")
    public RegisterPage setEmail(String value) {
        emailInput.sendKeys(value);
        return this;
    }

    @Step("Вввести Password {value}.")
    public RegisterPage setPassword(String value) {
        passwordInput.sendKeys(value);
        return this;
    }
    @Step("Вввести подтверждение Password {value}.")
    public RegisterPage setPasswordConfirm(String value) {
        passwordConfirmInput.sendKeys(value);
        return this;
    }

    @Step("Нажать на кнопку Submit.")
    public RegisterPage submitClick() {
        submitButton.click();
        return this;
    }

//    @Step("Проверить, что введенные данные {value} отображаются в странице кабинета {key}.")
//    public RegisterPage checkResult(String key, String value) {
//        modalFormComponent.checkModalForm(key, value);
//
//        return this;
//



}