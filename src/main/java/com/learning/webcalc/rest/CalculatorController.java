package com.learning.webcalc.rest;

import com.learning.webcalc.service.api.CalculatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CalculatorController
{

    private final CalculatorService calculatorService;

    @Autowired
    public CalculatorController(CalculatorService calculatorService)
    {
        this.calculatorService = calculatorService;
    }

    @RequestMapping(path = "/history", method = RequestMethod.GET)
    public HistoryResponse history()
    {
        return new HistoryResponse(calculatorService.getHistory());
    }

    @RequestMapping(path = "/calculate/{expression}", method = RequestMethod.GET)
    public CalculateResponse calculate(@PathVariable String expression)
    {
        return new CalculateResponse(calculatorService.calculate(expression));
    }

}
