package com.learning.webcalc.service;

import org.junit.Test;

import java.util.List;

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
        final List<Object> testTokens = asList(3, "+", 4, "*", 2);
        final List<Object> rpnTokens = asList(3, 4, 2, "*", "+");

        // when
        List<Object> result = objectUnderTest.convert(testTokens);

        // then
        assertThat(result).containsExactlyElementsOf(rpnTokens);
    }

    @Test
    public void shouldConvertComplexExpression()
    {
        // given
        final List<Object> testTokens = asList("(", "(", 2, "+", 7, ")", "/", 3, "+", "(", 3, "-", 14, ")", "*", 4, ")", "/", 2);
        final List<Object> rpnTokens = asList(2, 7, "+", 3,  "/", 3, 14, "-", 4, "*", "+", 2, "/");

        // when
        List<Object> result = objectUnderTest.convert(testTokens);

        // then
        assertThat(result).containsExactlyElementsOf(rpnTokens);
    }

    @Test
    public void shouldConvertExpressionWithExponentiation()
    {
        // given
        final List<Object> testTokens = asList(4, "^", 3, "^", 2);
        final List<Object> rpnTokens = asList(4, 3, 2, "^", "^");

        // when
        List<Object> result = objectUnderTest.convert(testTokens);

        // then
        assertThat(result).containsExactlyElementsOf(rpnTokens);
    }

    @Test
    public void shouldConvertExpressionWithExponentiationAndBrackets()
    {
        // given
        final List<Object> testTokens = asList(5, "^", "(", 4, "-", 1, ")", "^", 2);
        final List<Object> rpnTokens = asList(5, 4, 1, "-", 2, "^", "^");

        // when
        List<Object> result = objectUnderTest.convert(testTokens);

        // then
        assertThat(result).containsExactlyElementsOf(rpnTokens);
    }

    @Test
    public void shouldConvertComplexExpressionWithExponentiation()
    {
        // given
        final List<Object> testTokens = asList(3, "+", 4, "*", 2, "/", "(", 1, "-", 5, ")", "^", 2);
        final List<Object> rpnTokens = asList(3, 4, 2, "*", 1, 5, "-", 2, "^", "/", "+");

        // when
        List<Object> result = objectUnderTest.convert(testTokens);

        // then
        assertThat(result).containsExactlyElementsOf(rpnTokens);
    }

    @Test
    public void shouldConvertExpressionWithSqrt()
    {
        // given
        final List<Object> testTokens = asList(1, "+", "s", "(", 8, "*", 10, "+", 1, ")", "/", 3);
        final List<Object> rpnTokens = asList(1, 8, 10, "*", 1, "+", "s", 3, "/", "+");

        // when
        List<Object> result = objectUnderTest.convert(testTokens);

        // then
        assertThat(result).containsExactlyElementsOf(rpnTokens);
    }

    @Test
    public void shouldConvertExpressionWithIntegral()
    {
        // given
        final List<Object> testTokens = asList(1, "+", "i", "(", 8, "*", 10, ";", 4, "-", 5, ";", 13, ")", "/", 3);
        final List<Object> rpnTokens = asList(1, 8, 10, "*", 4, 5, "-", 13, "i", 3, "/", "+");

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