package com.learning.webcalc.service;

import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

public class ExpressionProcessorTest
{

    private static final String EMPTY_EXPRESSION = "";

    private ExpressionProcessor objectUnderTest;

    public ExpressionProcessorTest()
    {
        this.objectUnderTest = new ExpressionProcessor();
    }

    @Test
    public void shouldClean()
    {
        // given
        final String testExpression = " 5 + [(7-{ 43 *3})/2 ] ";
        final String cleanExpression = "5+((7-(43*3))/2)";

        // when
        String result = objectUnderTest.clean(testExpression);

        // then
        assertThat(result).isEqualTo(cleanExpression);
    }

    @Test
    public void shouldCleanEmptyExpression()
    {
        // given

        // when
        String result = objectUnderTest.clean(EMPTY_EXPRESSION);

        // then
        assertThat(result).isEqualTo(EMPTY_EXPRESSION);
    }

    @Test
    public void shouldTokenize()
    {
        // given
        final String testExpression = "1+(8*10+(98/3*(20)-8))"; // TODO: sqrt &  root
        final List<String> tokens = asList("1", "+", "(", "8", "*", "10", "+", "(", "98", "/", "3", "*", "(", "20", ")", "-", "8", ")", ")" );

        // when
        List<String> result = objectUnderTest.tokenize(testExpression);

        // then
        assertThat(result).containsOnlyElementsOf(tokens);
    }

    @Ignore
    @Test
    public void shouldTokenizeExpressionWithNegativeValues() // TODO: support for floats
    {
        // given
        final String testExpression = "-5*(-18+(-3))";
        final List<String> tokens = asList("-5", "*", "(", "-18", "+", "(", "-3", ")", ")" );

        // when
        List<String> result = objectUnderTest.tokenize(testExpression);

        // then
        assertThat(result).containsOnlyElementsOf(tokens);
    }

    @Test
    public void shouldTokenizeEmptyExpression()
    {
        // given

        // when
        List<String> result = objectUnderTest.tokenize(EMPTY_EXPRESSION);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    public void shouldToReversePolishNotationSimpleExpression()
    {
        // given
        final List<String> testTokens = asList("3", "+", "4", "*", "2");
        final List<String> rpnTokens = asList("3", "4", "2", "*", "+");

        // when
        List<String> result = objectUnderTest.toReversePolishNotation(testTokens);

        // then
        assertThat(result).containsOnlyElementsOf(rpnTokens);
    }

    @Test
    public void shouldToReversePolishNotationComplexExpression()
    {
        // given
        final List<String> testTokens = asList("(", "(", "2", "+", "7", ")", "/", "3", "+", "(", "14", "-", "3", ")", "*", "4", ")", "/", "2");
        final List<String> rpnTokens = asList("2", "7", "+", "3",  "/", "14",  "3", "-", "4", "*", "+", "2", "/");

        // when
        List<String> result = objectUnderTest.toReversePolishNotation(testTokens);

        // then
        assertThat(result).containsOnlyElementsOf(rpnTokens);
    }

    @Test
    public void shouldToReversePolishNotationEmptyExpression()
    {
        // given
        final List<String> testTokens = emptyList();
        final List<String> rpnTokens = emptyList();

        // when
        List<String> result = objectUnderTest.toReversePolishNotation(testTokens);

        // then
        assertThat(result).containsOnlyElementsOf(rpnTokens);
    }

    @Test
    public void shouldToReversePolishNotationExpressionWithNegativeValues()
    {
        // given
        final List<String> testTokens = asList("-", "3", "+", "(", "-", "5", ")");
        final List<String> rpnTokens = asList("-", "3", "-", "5", "+");

        // when
        List<String> result = objectUnderTest.toReversePolishNotation(testTokens);

        // then
        assertThat(result).containsOnlyElementsOf(rpnTokens);
    }

    @Test
    public void shouldCalculateReversePolishNotationSimpleExpression()
    {
        // given
        final List<String> rpnTokens = asList("3", "4", "2", "*", "+");

        // when
        double result = objectUnderTest.calculateReversePolishNotation(rpnTokens);

        // then
        assertThat(result).isEqualTo(11);
    }

    @Test
    public void shouldCalculateReversePolishNotationComplexExpression()
    {
        // given
        final List<String> rpnTokens = asList("2", "7", "+", "3",  "/", "14",  "3", "-", "4", "*", "+", "2", "/");

        // when
        double result = objectUnderTest.calculateReversePolishNotation(rpnTokens);

        // then
        assertThat(result).isEqualTo(23.5);
    }

    @Test
    public void shouldCalculateReversePolishNotationEmptyExpression()
    {
        // given
        final List<String> rpnTokens = emptyList();

        // when
        double result = objectUnderTest.calculateReversePolishNotation(rpnTokens);

        // then
        assertThat(result).isEqualTo(0);
    }

    @Ignore // TODO: fix
    @Test
    public void shouldCalculateReversePolishNotationExpressionWithNegativeValues()
    {
        // given
        final List<String> rpnTokens = asList("-", "3", "-", "5", "+");

        // when
        double result = objectUnderTest.calculateReversePolishNotation(rpnTokens);

        // then
        assertThat(result).isEqualTo(-8);
    }

}