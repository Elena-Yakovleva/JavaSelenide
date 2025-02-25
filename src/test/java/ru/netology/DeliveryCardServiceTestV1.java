package ru.netology;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Selenide.$;

import static org.openqa.selenium.Keys.*;

public class DeliveryCardServiceTestV1 {

    public String generateDate(int days, String pattern) {
       return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern(pattern));
    }

    @Test
    void shouldDeliveryCardServiceTest() {

        String planningDate = generateDate(4, "dd.MM.yyyy");

        Selenide.open("http://localhost:9999"); // открыть страницу
        $("[data-test-id='city'] input").sendKeys("Иваново"); // ввести город (один из административных центров субъектов РФ.)
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.chord(BACK_SPACE), planningDate); // стереть дату по умолчанию (не ранее трёх дней с текущей даты)
        $("[data-test-id='name'] input").sendKeys("Сидоров Иван"); // ввести фамилию и имя (в поле фамилии и имени разрешены только русские буквы, дефисы и пробелы)
        $("[data-test-id='phone'] input").sendKeys("+79992221133"); // ввести номер телефона (только 11 цифр, символ + на первом месте)
        $("[data-test-id='agreement']").click(); // подтвердить чек-бокс
        $("button.button").click();
        $("[data-test-id='notification']").shouldBe(Condition.visible, Duration.ofSeconds(15));// подтвердить оформление
        $("[data-test-id='notification']").shouldBe(Condition.text("Встреча успешно забронирована на " + planningDate));


    }

}
