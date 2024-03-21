package ru.almyal.vacationpayment.services;

import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class CalculatorServiceImpl implements CalculatorService {
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##");

    /**
     * Список праздников инициализируется при загрузке класса, исходя из текущей даты.
     * В список попадают 1-8 января, 23 февраля, 8 марта, 1 и 9 мая, 12 июня, 4 ноября на год вперед от начала текущей даты.
     * Например, при текущей дате 2024-06-10 в формате yyyy-mm-dd 12 июня, 4 ноября будут относиться к 2024 году,
     * оставшиеся дни перейдут на 2025 год.
     */
    private static final List<LocalDate> OFFICIAL_HOLIDAYS = new ArrayList<>(14);
    public static final LocalDate NOW = LocalDate.now();
    public static final String INCORRECT_DAYS_NUMBER_MESSAGE = "Введите допустимое число дней";

    static {
        int currentYear = NOW.getYear();
        int nextYear = currentYear + 1;

        IntStream.range(1, 9)
                .mapToObj(i -> LocalDate.of(currentYear, 1, i).isAfter(NOW) ?
                        LocalDate.of(currentYear, 1, i) :
                        LocalDate.of(nextYear, 1, i))
                .forEach(OFFICIAL_HOLIDAYS::add);

        OFFICIAL_HOLIDAYS.add(LocalDate.of(currentYear, 2, 23).isAfter(NOW) ?
                LocalDate.of(currentYear, 2, 23) :
                LocalDate.of(nextYear, 2, 23));

        OFFICIAL_HOLIDAYS.add(LocalDate.of(currentYear, 3, 8).isAfter(NOW) ?
                LocalDate.of(currentYear, 3, 8) :
                LocalDate.of(nextYear, 3, 8));

        OFFICIAL_HOLIDAYS.add(LocalDate.of(currentYear, 5, 1).isAfter(NOW) ?
                LocalDate.of(currentYear, 5, 1) :
                LocalDate.of(nextYear, 5, 1));

        OFFICIAL_HOLIDAYS.add(LocalDate.of(currentYear, 5, 9).isAfter(NOW) ?
                LocalDate.of(currentYear, 5, 9) :
                LocalDate.of(nextYear, 5, 9));

        OFFICIAL_HOLIDAYS.add(LocalDate.of(currentYear, 6, 12).isAfter(NOW) ?
                LocalDate.of(currentYear, 6, 12) :
                LocalDate.of(nextYear, 6, 12));

        OFFICIAL_HOLIDAYS.add(LocalDate.of(currentYear, 11, 4).isAfter(NOW) ?
                LocalDate.of(currentYear, 11, 4) :
                LocalDate.of(nextYear, 11, 4));
    }

    /**
     * Расчет производится при положительном числе дней отпуска равным не более 28.
     * Расчет отпускных по формуле: среднегодовая ЗП / 29.3 * число дней отпуска
     *
     * @param averageSalary
     *        Средняя ЗП с дробной частью в 2 знака
     * @param vacationDays
     *        Число дней отпуска
     * @return
     *        Сумма отпускных в формате String
     */

    @Override
    public String getPayByAverageSalaryAndVacationDays(Double averageSalary, Integer vacationDays) {
        if (0 > vacationDays || vacationDays > 28) return INCORRECT_DAYS_NUMBER_MESSAGE;
        return DECIMAL_FORMAT.format(averageSalary / 29.3 * vacationDays);
    }

    /**
     * Расчет производится при положительном числе дней отпуска равным не более 28.
     * При совпадении отпускных дней с праздничными, количество отпускных дней уменьшается на количество праздничных.
     * Расчет отпускных по формуле: среднегодовая ЗП / 29.3 * (число дней отпуска - число праздничных дней)
     *
     * @param averageSalary
     *        Средняя ЗП с дробной частью в 2 знака
     * @param vacationDays
     *        Число дней отпуска
     * @param startVacationDate
     *        Дата начала отпуска
     * @return Сумма отпускных в формате String
     */
    @Override
    public String getPayByAverageSalaryVacationDaysAndStartDate(Double averageSalary,
                                                                Integer vacationDays,
                                                                LocalDate startVacationDate) {
        if (0 > vacationDays || vacationDays > 28) return INCORRECT_DAYS_NUMBER_MESSAGE;
        LocalDate lastVacationDate = startVacationDate.plusDays(vacationDays - 1);
        List<LocalDate> vacationDates = startVacationDate
                .datesUntil(lastVacationDate.plusDays(1))
                .collect(Collectors.toList());
        long unpaidDays = vacationDates.stream()
                .filter(OFFICIAL_HOLIDAYS::contains)
                .count();
        return getPayByAverageSalaryAndVacationDays(averageSalary,
                vacationDays - Long.valueOf(unpaidDays).intValue());
    }
}
