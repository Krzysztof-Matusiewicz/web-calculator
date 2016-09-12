package com.learning.webcalc.service;

import com.learning.webcalc.service.api.CalculationException;
import org.assertj.core.data.Percentage;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.Percentage.withPercentage;

public class DefaultIntegralCalculatorTest
{

    private static final Percentage TEST_PRECISION = withPercentage(0.0000000001);

    private DefaultIntegralCalculator objectUnderTest;

    public DefaultIntegralCalculatorTest()
    {
        this.objectUnderTest = new DefaultIntegralCalculator();
    }

    @Test
    public void shouldCalculate() throws Exception
    {
        // given
        double lowerBound = 2;
        double upperBound = 7;
        int intervalCount = 1;
        int threadCount = 1;

        // when
        Double result = objectUnderTest.calculate(lowerBound, upperBound, intervalCount, threadCount);

        // then
        assertThat(result).isCloseTo(1089.2441023295300, TEST_PRECISION);
    }

    @Test
    public void shouldCalculateWithReversedBound() throws Exception
    {
        // given
        double lowerBound = 7;
        double upperBound = 2;
        int intervalCount = 1;
        int threadCount = 1;

        // when
        Double result = objectUnderTest.calculate(lowerBound, upperBound, intervalCount, threadCount);

        // then
        assertThat(result).isCloseTo(-1089.2441023295300, TEST_PRECISION);
    }

    @Test
    public void shouldCalculateWithNegativeBound() throws Exception
    {
        // given
        double lowerBound = -7;
        double upperBound = -2;
        int intervalCount = 1;
        int threadCount = 1;

        // when
        Double result = objectUnderTest.calculate(lowerBound, upperBound, intervalCount, threadCount);

        // then
        assertThat(result).isCloseTo(0.1344234012711, TEST_PRECISION);
    }

    @Test
    public void shouldCalculateWithNonIntegerBounds() throws Exception
    {
        // given
        double lowerBound = 1.25;
        double upperBound = 4.66;
        int intervalCount = 1;
        int threadCount = 1;

        // when
        Double result = objectUnderTest.calculate(lowerBound, upperBound, intervalCount, threadCount);

        // then
        assertThat(result).isCloseTo(102.1457391991980, TEST_PRECISION);
    }

    @Test
    public void shouldCalculateForManyIntervals() throws Exception
    {
        // given
        double lowerBound = 2;
        double upperBound = 7;
        int intervalCount = 111;
        int threadCount = 1;

        // when
        Double result = objectUnderTest.calculate(lowerBound, upperBound, intervalCount, threadCount);

        // then
        assertThat(result).isCloseTo(1089.2441023295300, TEST_PRECISION);
    }

    @Test
    public void shouldCalculateForManyThreads() throws Exception
    {
        // given
        double lowerBound = 2;
        double upperBound = 7;
        int intervalCount = 111;
        int threadCount = 25;

        // when
        Double result = objectUnderTest.calculate(lowerBound, upperBound, intervalCount, threadCount);

        // then
        assertThat(result).isCloseTo(1089.2441023295300, TEST_PRECISION);
    }

    @Test
    public void shouldCalculateForBigValues() throws Exception
    {
        // given
        double lowerBound = 66;
        double upperBound = 71;
        int intervalCount = 1000000;
        int threadCount = 34;

        // when
        Double result = objectUnderTest.calculate(lowerBound, upperBound, intervalCount, threadCount);

        // then
        assertThat(result).isCloseTo(6791599363419430000000000000000d, withPercentage(0.0000001));
    }

    @Test(expected = CalculationException.class)
    public void shouldCalculateExplodeOnZeroIntervalCount() throws Exception
    {
        // given
        double lowerBound = 2;
        double upperBound = 7;
        int intervalCount = 0;
        int threadCount = 1;

        // when
        objectUnderTest.calculate(lowerBound, upperBound, intervalCount, threadCount);
    }

    @Test(expected = CalculationException.class)
    public void shouldCalculateExplodeOnNegativeIntervalCount() throws Exception
    {
        // given
        double lowerBound = 2;
        double upperBound = 7;
        int intervalCount = -5;
        int threadCount = 1;

        // when
        objectUnderTest.calculate(lowerBound, upperBound, intervalCount, threadCount);
    }

    @Test(expected = CalculationException.class)
    public void shouldCalculateExplodeOnZeroThreadCount() throws Exception
    {
        // given
        double lowerBound = 2;
        double upperBound = 7;
        int intervalCount = 1;
        int threadCount = 0;

        // when
        objectUnderTest.calculate(lowerBound, upperBound, intervalCount, threadCount);
    }

    @Test(expected = CalculationException.class)
    public void shouldCalculateExplodeOnNegativeThreadCount() throws Exception
    {
        // given
        double lowerBound = 2;
        double upperBound = 7;
        int intervalCount = 1;
        int threadCount = -5;

        // when
        objectUnderTest.calculate(lowerBound, upperBound, intervalCount, threadCount);
    }

}