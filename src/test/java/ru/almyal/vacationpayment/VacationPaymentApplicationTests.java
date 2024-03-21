package ru.almyal.vacationpayment;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.almyal.vacationpayment.services.CalculatorService;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class VacationPaymentApplicationTests {

    @Autowired
	private CalculatorService calculatorService;

    private static final LocalDate NOW = LocalDate.now();

    @Test
    void contextLoads() {
    }

    @Test
    void findPaymentByAverageSalaryAndNumberOfDays() {
        double averageSalaryFirst = 258900;
        int vacationDaysFirst = 30;
        String result1 = "Введите допустимое число дней";

        double averageSalarySecond = 127897.86;
        int vacationDaysSecond = 20;
        String result2 = String.format("%.2f", averageSalarySecond / 29.3 * vacationDaysSecond);

        assertEquals(result1,
                calculatorService.getPayByAverageSalaryAndVacationDays(averageSalaryFirst, vacationDaysFirst));
        assertEquals(result2,
                calculatorService.getPayByAverageSalaryAndVacationDays(averageSalarySecond, vacationDaysSecond));
    }

    @Test
    void findPaymentByAverageSalaryNumberOfDaysAndStartDate() {
        double averageSalaryFirst = 258900;
        int vacationDaysFirst = 30;
        LocalDate startDateFirst = LocalDate.of(LocalDate.now().getYear(), 10, 10);
        String result1 = "Введите допустимое число дней";

        double averageSalarySecond = 127897.86;
        int vacationDaysSecond = 14;
        LocalDate startDateSecond = LocalDate.of(NOW.getYear(), 12, 30)
                .isAfter(NOW) ? LocalDate.of(NOW.getYear(), 12, 30) :
                LocalDate.of(NOW.getYear() + 1, 12, 30);
        String result2 = String.format("%.2f", averageSalarySecond / 29.3 * (vacationDaysSecond - 8));

        double averageSalaryThird = 90780;
        int vacationDaysThird = 5;
        LocalDate startDateThird = LocalDate.of(NOW.getYear(), 6, 10)
                .isAfter(NOW) ? LocalDate.of(NOW.getYear(), 6, 10) :
                LocalDate.of(NOW.getYear() + 1, 6, 10);
        String result3 = String.format("%.2f", averageSalaryThird / 29.3 * (vacationDaysThird - 1));

        assertEquals(result1,
                calculatorService.getPayByAverageSalaryVacationDaysAndStartDate(averageSalaryFirst,
                        vacationDaysFirst, startDateFirst));
        assertEquals(result2,
                calculatorService.getPayByAverageSalaryVacationDaysAndStartDate(averageSalarySecond,
                        vacationDaysSecond, startDateSecond));
        assertEquals(result3,
                calculatorService.getPayByAverageSalaryVacationDaysAndStartDate(averageSalaryThird,
                        vacationDaysThird, startDateThird));
    }
}
