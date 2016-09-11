package com.learning.webcalc.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.text.ParseException;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(MockitoJUnitRunner.class)
public class RpnCalculatorTest
{

    @Mock
    private IntegralCalculator integralCalculatorMock;

    private RpnCalculator objectUnderTest;

    @Before
    public void setUp()
    {
        initMocks(this);
        this.objectUnderTest = new RpnCalculator(integralCalculatorMock);
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
    public void shouldCalculateExpressionWithSqrt() throws ParseException
    {
        // given
        final List<Object> rpnTokens = asList(1d, 8d, 10d, "*", 1d, "+", "s", 3d, "/", "+");

        // when
        double result = objectUnderTest.calculate(rpnTokens);

        // then
        assertThat(result).isEqualTo(4);
    }

    @Test
    public void shouldCalculateExpressionWithIntegral() throws ParseException
    {
        // given
        final List<Object> rpnTokens = asList(1d, 8d, 10d, "*", 4d, 5d, "-", 9d, 2d, "i", 3d, "/", "+");
        Mockito.stub(integralCalculatorMock.calc(80d, -1d, 2, 9)).toReturn(999d);

        // when
        double result = objectUnderTest.calculate(rpnTokens);

        // then
        assertThat(result).isEqualTo(334);
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