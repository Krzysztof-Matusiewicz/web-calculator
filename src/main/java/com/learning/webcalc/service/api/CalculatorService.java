package com.learning.webcalc.service.api;

import java.text.ParseException;

public interface CalculatorService
{

    double calculate(String expression) throws ParseException;

    History getHistory();

}
