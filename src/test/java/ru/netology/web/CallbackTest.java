package ru.netology.web;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CallbackTest {
    private WebDriver driver;

    @BeforeAll
    static void setUpAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();

        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        options.addArguments("--disable-gpu");

        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999");
    }

    @AfterEach
    void tearDown() {
        driver.quit();
        driver = null;
    }

    @Test
    void sendingACallbackFormUsingTheFormElement() {
        WebElement form = driver.findElement(By.tagName("form"));
        form.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Василий");
        form.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79270000000");
        form.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        form.findElement(By.cssSelector("button.button")).click();
        String text = driver.findElement(
                By.cssSelector("[data-test-id=order-success]")
        ).getText();
        assertEquals(
                "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.",
                text.trim()
        );
    }

    @Test
    void shouldShowErrorIfAllFieldsAreEmpty() {
        driver.findElement(By.xpath("//button[contains(., 'Продолжить')]")).click();

        WebElement error = driver.findElement(
                By.cssSelector("[data-test-id='name'].input_invalid"));
        assertTrue(error.isDisplayed());
        assertTrue(error.getText().contains("Поле обязательно для заполнения"));
    }

    @Test
    void shouldShowErrorIfPhoneIsEmpty() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Иванов Иван");
        driver.findElement(By.xpath("//button[contains(., 'Продолжить')]")).click();

        WebElement error = driver.findElement(
                By.cssSelector("[data-test-id='phone'].input_invalid"));
        assertTrue(error.getText().contains("Поле обязательно для заполнения"));
    }

    @Test
    void shouldShowErrorIfNameContainsDigits() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Ив123");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79991234567");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.xpath("//button[contains(., 'Продолжить')]")).click();

        WebElement error = driver.findElement(
                By.cssSelector("[data-test-id='name'].input_invalid"));
        assertTrue(error.isDisplayed());
        assertTrue(error.getText().contains("Имя и Фамилия указаные неверно"));
    }

    @Test
    void shouldShowErrorIfNameContainsLatinLetters() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Ivan");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79991234567");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.xpath("//button[contains(., 'Продолжить')]")).click();

        WebElement error = driver.findElement(
                By.cssSelector("[data-test-id='name'].input_invalid"));
        assertTrue(error.isDisplayed());
        assertTrue(error.getText().contains("Имя и Фамилия указаные неверно"));
    }

    // Проверка имени со специальными символами
    @Test
    void shouldShowErrorIfNameContainsSpecialCharacters() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Иван@Иванов");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79991234567");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.xpath("//button[contains(., 'Продолжить')]")).click();

        WebElement error = driver.findElement(
                By.cssSelector("[data-test-id='name'].input_invalid"));
        assertTrue(error.isDisplayed());
        assertTrue(error.getText().contains("Имя и Фамилия указаные неверно"));
    }

    @Test
    void shouldShowErrorIfPhoneDoesNotStartWithPlus() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Иван Иванов");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("79991234567");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.xpath("//button[contains(., 'Продолжить')]")).click();

        WebElement error = driver.findElement(
                By.cssSelector("[data-test-id='phone'].input_invalid"));
        assertTrue(error.isDisplayed());
        assertTrue(error.getText().contains("Телефон указан неверно"));
    }

    @Test
    void shouldShowErrorIfPhoneContainsLatinLetters() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Иван Иванов");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("abcdefghijk");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.xpath("//button[contains(., 'Продолжить')]")).click();

        WebElement error = driver.findElement(
                By.cssSelector("[data-test-id='phone'].input_invalid"));
        assertTrue(error.isDisplayed());
        assertTrue(error.getText().contains("Телефон указан неверно"));
    }

    @Test
    void shouldShowErrorIfPhoneContainsCyrillicLetters() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Иван Иванов");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("абвгдежзийк");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.xpath("//button[contains(., 'Продолжить')]")).click();

        WebElement error = driver.findElement(
                By.cssSelector("[data-test-id='phone'].input_invalid"));
        assertTrue(error.isDisplayed());
        assertTrue(error.getText().contains("Телефон указан неверно"));
    }

    @Test
    void shouldShowErrorIfPhoneHasMoreThanElevenDigits() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Иван Иванов");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+799912345678");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.xpath("//button[contains(., 'Продолжить')]")).click();

        WebElement error = driver.findElement(
                By.cssSelector("[data-test-id='phone'].input_invalid"));
        assertTrue(error.isDisplayed());
        assertTrue(error.getText().contains("Телефон указан неверно"));
    }

    @Test
    void shouldShowErrorIfPhoneHasLessThanElevenDigits() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Иван Иванов");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+7999123456");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.xpath("//button[contains(., 'Продолжить')]")).click();

        WebElement error = driver.findElement(
                By.cssSelector("[data-test-id='phone'].input_invalid"));
        assertTrue(error.isDisplayed());
        assertTrue(error.getText().contains("Телефон указан неверно"));
    }

    @Test
    void shouldShowErrorIfPlusIsNotAtTheBeginningOfPhone() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Иван Иванов");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+7$991234567");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.xpath("//button[contains(., 'Продолжить')]")).click();

        WebElement error = driver.findElement(
                By.cssSelector("[data-test-id='phone'].input_invalid"));
        assertTrue(error.isDisplayed());
        assertTrue(error.getText().contains("Телефон указан неверно"));
    }

    @Test
    void shouldShowErrorIfAgreementIsNotChecked() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Иван Иванов");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79991234567");
        driver.findElement(By.xpath("//button[contains(., 'Продолжить')]")).click();
        ;

        assertTrue(driver.findElement(
                By.cssSelector("[data-test-id='agreement'].input_invalid")).isDisplayed());
    }
}
