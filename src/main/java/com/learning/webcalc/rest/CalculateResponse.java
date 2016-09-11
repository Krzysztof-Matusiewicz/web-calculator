package com.learning.webcalc.rest;

public class CalculateResponse
{

    private final double result;

    public double getResult()
    {
        return result;
    }

    public CalculateResponse(double result)
    {
        this.result = result;
    }

}
