package com.learning.webcalc.service.api;

public interface HistoryService
{

    History getHistory();

    void store(String expression, double result);

}
