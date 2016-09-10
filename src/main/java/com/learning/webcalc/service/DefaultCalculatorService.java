package com.learning.webcalc.service;

import com.learning.webcalc.service.api.CalculatorService;
import com.learning.webcalc.service.api.History;
import com.learning.webcalc.service.api.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class DefaultCalculatorService implements CalculatorService
{

    private HistoryService historyService;

    @Autowired
    public DefaultCalculatorService(HistoryService historyService)
    {
        this.historyService = historyService;
    }

    @Override
    public double calculate(String expression)
    {
        double result = (new Random()).nextInt(1000);
        historyService.store(expression, result);
        return result;
    }

    public History getHistory()
    {
        return historyService.getHistory();
    }

}
