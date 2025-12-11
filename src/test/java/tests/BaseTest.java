package tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import helpers.Attach;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.Map;

import static com.codeborne.selenide.Selenide.*;

public class BaseTest {

    @BeforeAll
    static void setUpConfig() {

        Configuration.browser = System.getProperty("browser", "chrome");
        Configuration.browserVersion = System.getProperty("version", "127.0");
        Configuration.browserSize = System.getProperty("windowSize", "1920x1080");
        Configuration.baseUrl = "https://cmtscience.ru";
        Configuration.pageLoadStrategy = "eager";
        Configuration.remote = System.getProperty("remote");
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("selenoid:options", Map.of(
                    "enableVNC", true,
                    "enableVideo", true
            ));
        Configuration.browserCapabilities = capabilities;

//        boolean isJenkins = System.getenv("JENKINS_HOME") != null;
//
//        if (isJenkins) {
//            String wdHost = System.getProperty("wdHost", "selenoid.autotests.cloud");
//            Configuration.remote = System.getProperty("remote","https://user1:1234@"+wdHost+"/wd/hub");
//
//            DesiredCapabilities capabilities = new DesiredCapabilities();
//            capabilities.setCapability("selenoid:options", Map.of(
//                    "enableVNC", true,
//                    "enableVideo", true
//            ));
//            Configuration.browserCapabilities = capabilities;
//
//            System.out.println("=== Running on Jenkins with Selenoid ===");
//        } else {
//            DesiredCapabilities capabilities = new DesiredCapabilities();
//            capabilities.setCapability("goog:chromeOptions", Map.of("args", new ArrayList<>()));
//            Configuration.browserCapabilities = capabilities;
//            Configuration.remote = null;
//            Configuration.headless = false;
//            System.out.println("=== Running locally (headless Chrome) ===");
//        }

    }

    @BeforeEach
    void beforeEach(){
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide());
    }

    @AfterEach
    void closeUp() {
        closeWebDriver();
    }

    @AfterEach
    void tearDown() {
        // Очистка cookies после каждого теста
        clearBrowserCookies();
        clearBrowserLocalStorage();
    }

    @AfterEach
    void addAttachments(){
        Attach.screenshotAs("Last screenshot");
        Attach.pageSource();
        Attach.browserConsoleLogs();
        Attach.addVideo();
    }


}
