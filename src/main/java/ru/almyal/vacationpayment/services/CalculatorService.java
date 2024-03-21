package ru.almyal.vacationpayment.services;

import java.time.LocalDate;

public interface CalculatorService {
    String getPayByAverageSalaryAndVacationDays(Double averageSalary, Integer vacationDays);

    String getPayByAverageSalaryVacationDaysAndStartDate(Double averageSalary,
                                                         Integer vacationDays,
                                                         LocalDate startVacationDate);
}
