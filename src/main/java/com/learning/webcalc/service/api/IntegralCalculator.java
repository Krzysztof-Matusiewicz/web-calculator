package com.learning.webcalc.service.api;

public interface IntegralCalculator
{

    Double calculate(double lowerBound, double upperBound, int intervalCount, int threadCount) throws InterruptedException;

}
