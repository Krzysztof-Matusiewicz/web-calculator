package com.learning.webcalc.service;

import org.springframework.stereotype.Component;

@Component
public class IntegralCalculator
{

    public Double calc(double val1, double val2, int val3, int val4)
    {
        return val1 + val2 + val3 + val4;
    }

}
