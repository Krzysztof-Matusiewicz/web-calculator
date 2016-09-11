package com.learning.webcalc.rest;

import com.learning.webcalc.service.api.CalculationException;
import com.learning.webcalc.service.api.CalculatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

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

    @RequestMapping(path = "/calculate", method = RequestMethod.GET)
    public CalculateResponse calculate(@RequestParam("exp") String expression) throws ParseException
    {
        return new CalculateResponse(calculatorService.calculate(expression));
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CalculationException.class)
    public ErrorResponse handleCalculationException(Exception exc)
    {
        return new ErrorResponse(exc.getMessage());
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponse handleException(Exception exc)
    {
        return new ErrorResponse("Cannot execute expression. Check the expression value and try again.");
    }

}
