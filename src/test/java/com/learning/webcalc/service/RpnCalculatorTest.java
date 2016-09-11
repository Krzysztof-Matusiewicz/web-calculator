package com.learning.webcalc.service;

import org.junit.Test;

import java.text.ParseException;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

public class RpnCalculatorTest
{

    private RpnCalculator objectUnderTest;

    public RpnCalculatorTest()
    {
        this.objectUnderTest = new RpnCalculator();
    }

    @Test
    public void shouldCalculateSimpleExpression() throws ParseException
    {
        // given
        final List<Object> rpnTokens = asList(3d, 4d, 2d, "*", "+");

        // when
        double result = objectUnderTest.calculate(rpnTokens);

        // then
        assertThat(result).isEqualTo(11);
    }

    @Test
    public void shouldCalculateComplexExpression() throws ParseException
    {
        // given
        final List<Object> rpnTokens = asList(2d, 7d, "+", 3d, "/", 3d, 14d, "-", 4d, "*", "+", 2d, "/");

        // when
        double result = objectUnderTest.calculate(rpnTokens);

        // then
        assertThat(result).isEqualTo(-20.5);
    }

    @Test
    public void shouldCalculateExpressionWithExponentiation() throws ParseException
    {
        // given
        final List<Object> rpnTokens = asList(5d, 4d, 1d, "-", 2d, "^", "^");

        // when
        double result = objectUnderTest.calculate(rpnTokens);

        // then
        assertThat(result).isEqualTo(1953125);
    }

    @Test
    public void shouldCalculateEmptyExpression() throws ParseException
    {
        // given
        final List<Object> rpnTokens = emptyList();

        // when
        double result = objectUnderTest.calculate(rpnTokens);

        // then
        assertThat(result).isEqualTo(0);
    }

}