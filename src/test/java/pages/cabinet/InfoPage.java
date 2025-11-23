package pages.cabinet;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class InfoPage {

    private final SelenideElement
            firstNameInput = $("input[name=\"firstname\"]"),
            lastNameInput = $("input[name=\"lastname\"]"),
            patronymicNameInput = $("input[name=\"patronymic\"]"),
            phoneInput = $("input[name=\"phone\"]"),
            emailInput = $("input[name=\"email\"]"),
            saveButton = $("#user-save");


}
