package com.learning.webcalc.service;

import com.learning.webcalc.service.api.CalculationException;
import com.learning.webcalc.service.api.RpnCalculator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(MockitoJUnitRunner.class)
public class DefaultRpnCalculatorTest
{

    @Mock
    private DefaultIntegralCalculator defaultIntegralCalculatorMock;

    private RpnCalculator objectUnderTest;

    @Before
    public void setUp()
    {
        initMocks(this);
        this.objectUnderTest = new DefaultRpnCalculator(defaultIntegralCalculatorMock);
    }

    @Test
    public void shouldCalculateSimpleExpression() throws Exception
    {
        // given
        final List<Object> rpnTokens = asList(3d, 4d, 2d, "*", "+");

        // when
        double result = objectUnderTest.calculate(rpnTokens);

        // then
        assertThat(result).isEqualTo(11);
    }

    @Test
    public void shouldCalculateComplexExpression() throws Exception
    {
        // given
        final List<Object> rpnTokens = asList(2d, 7d, "+", 3d, "/", 3d, 14d, "-", 4d, "*", "+", 2d, "/");

        // when
        double result = objectUnderTest.calculate(rpnTokens);

        // then
        assertThat(result).isEqualTo(-20.5);
    }

    @Test
    public void shouldCalculateExpressionWithExponentiation() throws Exception
    {
        // given
        final List<Object> rpnTokens = asList(5d, 4d, 1d, "-", 2d, "^", "^");

        // when
        double result = objectUnderTest.calculate(rpnTokens);

        // then
        assertThat(result).isEqualTo(1953125);
    }

    @Test
    public void shouldCalculateExpressionWithSqrt() throws Exception
    {
        // given
        final List<Object> rpnTokens = asList(1d, 8d, 10d, "*", 1d, "+", "s", 3d, "/", "+");

        // when
        double result = objectUnderTest.calculate(rpnTokens);

        // then
        assertThat(result).isEqualTo(4);
    }

    @Test
    public void shouldCalculateExpressionWithIntegral() throws Exception
    {
        // given
        final List<Object> rpnTokens = asList(1d, 8d, 10d, "*", 4d, 5d, "-", 9d, 2d, "i", 3d, "/", "+");
        Mockito.stub(defaultIntegralCalculatorMock.calculate(80d, -1d, 9, 2)).toReturn(999d);

        // when
        double result = objectUnderTest.calculate(rpnTokens);

        // then
        assertThat(result).isEqualTo(334);
    }

    @Test(expected = CalculationException.class)
    public void shouldCalculateExplodeForDivisionByZero() throws Exception
    {
        // given
        final List<Object> rpnTokens = asList(8d, 0d, "/");

        // when
        objectUnderTest.calculate(rpnTokens);
    }

    @Test(expected = CalculationException.class)
    public void shouldCalculateExplodeForNegativeSqrt() throws Exception
    {
        // given
        final List<Object> rpnTokens = asList(-10d, "s");

        // when
        objectUnderTest.calculate(rpnTokens);
    }

    @Test
    public void shouldCalculateEmptyExpression() throws Exception
    {
        // given
        final List<Object> rpnTokens = emptyList();

        // when
        double result = objectUnderTest.calculate(rpnTokens);

        // then
        assertThat(result).isEqualTo(0);
    }

}