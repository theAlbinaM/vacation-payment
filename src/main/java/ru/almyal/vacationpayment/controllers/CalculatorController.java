package ru.almyal.vacationpayment.controllers;

import org.springframework.web.bind.annotation.*;
import ru.almyal.vacationpayment.services.CalculatorService;

import java.time.LocalDate;

@RestController
@RequestMapping("/calculate")
public class CalculatorController {

    CalculatorService calculatorService;

    public CalculatorController(CalculatorService calculatorService) {
        this.calculatorService = calculatorService;
    }

    @GetMapping("/{averageSalary}/{vacationDays}")
    public String calculateWithSalaryDaysParams(@PathVariable("averageSalary") Double averageSalary,
                                         @PathVariable("vacationDays") Integer vacationDays) {
        return calculatorService
                .getPayByAverageSalaryAndVacationDays(averageSalary, vacationDays);
    }

    @GetMapping("/{averageSalary}/{vacationDays}/{startVacationDate}")
    public String calculateWithSalaryDaysDateParams(@PathVariable("averageSalary") Double averageSalary,
                                           @PathVariable("vacationDays") Integer vacationDays,
                                           @PathVariable("startVacationDate") LocalDate startVacationDate) {
        return calculatorService
                .getPayByAverageSalaryVacationDaysAndStartDate(averageSalary,
                        vacationDays, startVacationDate);
    }
}
