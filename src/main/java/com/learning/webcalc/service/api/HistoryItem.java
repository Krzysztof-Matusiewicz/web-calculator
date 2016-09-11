package com.learning.webcalc.service.api;

public class HistoryItem
{

    private final String expression;

    private final double result;

    public HistoryItem(String expression, double result)
    {
        this.expression = expression;
        this.result = result;
    }

    public String getExpression()
    {
        return expression;
    }

    public double getResult()
    {
        return result;
    }

}
