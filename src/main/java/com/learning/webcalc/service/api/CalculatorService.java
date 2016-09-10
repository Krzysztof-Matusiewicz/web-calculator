package com.learning.webcalc.service.api;

public interface CalculatorService
{

    double calculate(String expression);

    History getHistory();

}
