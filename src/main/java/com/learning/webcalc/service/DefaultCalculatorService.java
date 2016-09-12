package com.learning.webcalc.service;import com.learning.webcalc.service.api.*;import org.springframework.beans.factory.annotation.Autowired;import org.springframework.stereotype.Service;import java.util.Optional;@Servicepublic class DefaultCalculatorService implements CalculatorService{    private final HistoryService historyService;    private final ExpressionProcessor expressionProcessor;    private final RpnConverter rpnConverter;    private final RpnCalculator rpnCalculator;    @Autowired    public DefaultCalculatorService(HistoryService historyService, ExpressionProcessor expressionProcessor, RpnConverter rpnConverter, RpnCalculator rpnCalculator)    {        this.historyService = historyService;        this.expressionProcessor = expressionProcessor;        this.rpnConverter = rpnConverter;        this.rpnCalculator = rpnCalculator;    }    @Override    public double calculate(String expression)    {        double result = calculateResult(expression);        historyService.store(expression, result);        return result;    }    public History getHistory()    {        return historyService.getHistory();    }    private double calculateResult(String expression)    {        return Optional.of(expression)                .map(expressionProcessor::validateBrackets)                .map(expressionProcessor::clean)                .map(expressionProcessor::tokenize)                .map(rpnConverter::convert)                .map(rpnCalculator::calculate).get();    }}