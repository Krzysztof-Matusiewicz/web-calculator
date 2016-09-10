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

    private CalculatorService calculatorService;

    @Autowired
    public CalculatorController(CalculatorService calculatorService)
    {
        this.calculatorService = calculatorService;
    }

    @RequestMapping(path = "/history", method = RequestMethod.GET)
    public HistoryResponse history()
    {
        HistoryResponse response = new HistoryResponse();
        response.setHistory(calculatorService.getHistory());
        return response;
    }

    @RequestMapping(path = "/calculate/{expression}", method = RequestMethod.GET)
    public CalculateResponse calculate(@PathVariable String expression)
    {
        CalculateResponse response = new CalculateResponse();
        double result = calculatorService.calculate(expression);
        response.setResult(result);
        return response;
    }

}
