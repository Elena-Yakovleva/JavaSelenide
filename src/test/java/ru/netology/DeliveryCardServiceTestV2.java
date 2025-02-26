package ru.netology;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Objects;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static org.openqa.selenium.Keys.BACK_SPACE;
import static org.openqa.selenium.Keys.ENTER;

public class DeliveryCardServiceTestV2 {

    public String generateDate(int days, String pattern) {
        return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern(pattern));
    }


    @Test
    void shouldSelectCityFromList() {

        String planningDate = generateDate(4, "dd.MM.yyyy");

        Selenide.open("http://localhost:9999");// открыть страницу
        $("[data-test-id='city'] input").sendKeys("Пс");
        $$(".menu-item__control").find(Condition.text("Псков")).shouldHave(Condition.visible).click(); // ввести город (один из административных центров субъектов РФ.)
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.chord(BACK_SPACE), planningDate); // стереть дату по умолчанию (не ранее трёх дней с текущей даты)

        $("[data-test-id='name'] input").sendKeys("Сидоров Иван"); // ввести фамилию и имя (в поле фамилии и имени разрешены только русские буквы, дефисы и пробелы)
        $("[data-test-id='phone'] input").sendKeys("+79992221133"); // ввести номер телефона (только 11 цифр, символ + на первом месте)
        $("[data-test-id='agreement']").click(); // подтвердить чек-бокс
        $("button.button").click();
        $("[data-test-id='notification']").shouldBe(Condition.visible, Duration.ofSeconds(15));// подтвердить оформление
        $("[data-test-id='notification']").shouldBe(Condition.text("Встреча успешно забронирована на " + planningDate));

    }

    @Test
    void shouldSelectDataFromCalendar() {

        int daysToMeeting = 16; // через сколько дней планируется встреча
        int minDaysToMeeting = 3; // минимальная дата, на которую возможна встреча

        String planningDate = generateDate(daysToMeeting, "dd.MM.yyyy");
        String planningDayNumber = generateDate(daysToMeeting, "d");
        int planningMonth = Integer.parseInt(generateDate(daysToMeeting, "MM"));
        int planningYear = Integer.parseInt(generateDate(daysToMeeting, "yyyy"));
        int minMonth = Integer.parseInt(generateDate(minDaysToMeeting, "MM"));
        int minYear = Integer.parseInt(generateDate(minDaysToMeeting, "yyyy"));


        Selenide.open("http://localhost:9999"); // открыть страницу
        $("[data-test-id='city'] input").sendKeys("Иваново"); // ввести город (один из административных центров субъектов РФ.)
        $(".icon-button").shouldHave(Condition.visible).click();
        // сравнение ожидаемого и текущего года
        if (minYear < planningYear) {
            $("[data-step='12']").click();
        }
        // сравнение ожидаемого и текущего месяца
        if (minMonth < planningMonth) {
            $("[data-step='1']").click();
            $$(".calendar__day").find(Condition.text(planningDayNumber)).click(); // ввод ожидаемой даты
        } else {
            $$(".calendar__day").find(Condition.text(planningDayNumber)).click(); // ввод ожидаемой даты
        }

        $("[data-test-id='name'] input").sendKeys("Сидоров Иван"); // ввести фамилию и имя (в поле фамилии и имени разрешены только русские буквы, дефисы и пробелы)
        $("[data-test-id='phone'] input").sendKeys("+79992221133"); // ввести номер телефона (только 11 цифр, символ + на первом месте)
        $("[data-test-id='agreement']").click(); // подтвердить чек-бокс
        $("button.button").click();
        $("[data-test-id='notification']").shouldBe(Condition.visible, Duration.ofSeconds(15));// подтвердить оформление
        $("[data-test-id='notification']").shouldBe(Condition.text("Встреча успешно забронирована на " + planningDate));

    }

}
