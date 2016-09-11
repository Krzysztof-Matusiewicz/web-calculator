package com.learning.webcalc.service;

import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

public class RpnConverterTest
{

    private RpnConverter objectUnderTest;

    public RpnConverterTest()
    {
        this.objectUnderTest = new RpnConverter();
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
        assertThat(result).containsOnlyElementsOf(rpnTokens);
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
        assertThat(result).containsOnlyElementsOf(rpnTokens);
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
        assertThat(result).containsOnlyElementsOf(rpnTokens);
    }

}