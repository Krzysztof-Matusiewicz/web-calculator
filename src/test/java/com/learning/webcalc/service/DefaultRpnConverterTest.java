package com.learning.webcalc.service;

import org.junit.Test;

import java.util.List;

import static com.learning.webcalc.service.Operator.ADDITION;
import static com.learning.webcalc.service.Operator.DIVISION;
import static com.learning.webcalc.service.Operator.EXPONENTIATION;
import static com.learning.webcalc.service.Operator.MULTIPLICATION;
import static com.learning.webcalc.service.Operator.SUBTRACTION;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

public class DefaultRpnConverterTest
{

    private DefaultRpnConverter objectUnderTest;

    public DefaultRpnConverterTest()
    {
        this.objectUnderTest = new DefaultRpnConverter();
    }

    @Test
    public void shouldConvertSimpleExpression()
    {
        // given
        final List<Object> testTokens = asList(3, ADDITION, 4, MULTIPLICATION, 2);
        final List<Object> rpnTokens = asList(3, 4, 2, MULTIPLICATION, ADDITION);

        // when
        List<Object> result = objectUnderTest.convert(testTokens);

        // then
        assertThat(result).containsExactlyElementsOf(rpnTokens);
    }

    @Test
    public void shouldConvertComplexExpression()
    {
        // given
        final List<Object> testTokens = asList(Bracket.OPENING, Bracket.OPENING, 2, ADDITION, 7, Bracket.CLOSING, DIVISION, 3, ADDITION, Bracket.OPENING, 3, SUBTRACTION, 14, Bracket.CLOSING, MULTIPLICATION, 4, Bracket.CLOSING, DIVISION, 2);
        final List<Object> rpnTokens = asList(2, 7, ADDITION, 3,  DIVISION, 3, 14, SUBTRACTION, 4, MULTIPLICATION, ADDITION, 2, DIVISION);

        // when
        List<Object> result = objectUnderTest.convert(testTokens);

        // then
        assertThat(result).containsExactlyElementsOf(rpnTokens);
    }

    @Test
    public void shouldConvertExpressionWithExponentiation()
    {
        // given
        final List<Object> testTokens = asList(4, EXPONENTIATION, 3, EXPONENTIATION, 2);
        final List<Object> rpnTokens = asList(4, 3, 2, EXPONENTIATION, EXPONENTIATION);

        // when
        List<Object> result = objectUnderTest.convert(testTokens);

        // then
        assertThat(result).containsExactlyElementsOf(rpnTokens);
    }

    @Test
    public void shouldConvertExpressionWithExponentiationAndBrackets()
    {
        // given
        final List<Object> testTokens = asList(5, EXPONENTIATION, Bracket.OPENING, 4, SUBTRACTION, 1, Bracket.CLOSING, EXPONENTIATION, 2);
        final List<Object> rpnTokens = asList(5, 4, 1, SUBTRACTION, 2, EXPONENTIATION, EXPONENTIATION);

        // when
        List<Object> result = objectUnderTest.convert(testTokens);

        // then
        assertThat(result).containsExactlyElementsOf(rpnTokens);
    }

    @Test
    public void shouldConvertComplexExpressionWithExponentiation()
    {
        // given
        final List<Object> testTokens = asList(3, ADDITION, 4, MULTIPLICATION, 2, DIVISION, Bracket.OPENING, 1, SUBTRACTION, 5, Bracket.CLOSING, EXPONENTIATION, 2);
        final List<Object> rpnTokens = asList(3, 4, 2, MULTIPLICATION, 1, 5, SUBTRACTION, 2, EXPONENTIATION, DIVISION, ADDITION);

        // when
        List<Object> result = objectUnderTest.convert(testTokens);

        // then
        assertThat(result).containsExactlyElementsOf(rpnTokens);
    }

    @Test
    public void shouldConvertExpressionWithSqrt()
    {
        // given
        final List<Object> testTokens = asList(1, ADDITION, "s", Bracket.OPENING, 8, MULTIPLICATION, 10, ADDITION, 1, Bracket.CLOSING, DIVISION, 3);
        final List<Object> rpnTokens = asList(1, 8, 10, MULTIPLICATION, 1, ADDITION, "s", 3, DIVISION, ADDITION);

        // when
        List<Object> result = objectUnderTest.convert(testTokens);

        // then
        assertThat(result).containsExactlyElementsOf(rpnTokens);
    }

    @Test
    public void shouldConvertExpressionWithIntegral()
    {
        // given
        final List<Object> testTokens = asList(1, ADDITION, "i", Bracket.OPENING, 8, MULTIPLICATION, 10, ";", 4, SUBTRACTION, 5, ";", 13, Bracket.CLOSING, DIVISION, 3);
        final List<Object> rpnTokens = asList(1, 8, 10, MULTIPLICATION, 4, 5, SUBTRACTION, 13, "i", 3, DIVISION, ADDITION);

        // when
        List<Object> result = objectUnderTest.convert(testTokens);

        // then
        assertThat(result).containsExactlyElementsOf(rpnTokens);
    }

    @Test
    public void shouldConvertEmptyExpression()
    {
        // given
        final List<Object> testTokens = emptyList();
        final List<Object> rpnTokens = emptyList();

        // when
        List<Object> result = objectUnderTest.convert(testTokens);

        // then
        assertThat(result).containsExactlyElementsOf(rpnTokens);
    }

}