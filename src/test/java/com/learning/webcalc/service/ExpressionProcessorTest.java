package com.learning.webcalc.service;

import org.junit.Test;

import java.text.ParseException;
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
    public void shouldTokenize() throws ParseException
    {
        // given
        final String testExpression = "1+(8*10+(98/3*(20)-8))"; // TODO: sqrt &  root
        final List<Object> tokens = asList(1d, "+", "(", 8d, "*", 10d, "+", "(", 98d, "/", 3d, "*", "(", 20d, ")", "-", 8d, ")", ")" );

        // when
        List<Object> result = objectUnderTest.tokenize(testExpression);

        // then
        assertThat(result).containsOnlyElementsOf(tokens);
    }

    @Test
    public void shouldTokenizeExpressionWithNegativeValues() throws ParseException
    {
        // given
        final String testExpression = "-5*(-18+(-3))";
        final List<Object> tokens = asList(-5d, "*", "(", -18d, "+", "(", -3d, ")", ")" );

        // when
        List<Object> result = objectUnderTest.tokenize(testExpression);

        // then
        assertThat(result).containsOnlyElementsOf(tokens);
    }

    @Test
    public void shouldTokenizeExpressionWithNonIntegerValues() throws ParseException
    {
        // given
        final String testExpression = String.format("(%.2f+%.3f)*%.1f-%.2f", 2.45, 567.789, 0.4, .99);
        final List<Object> tokens = asList("(", 2.45d, "+", 567.789, ")", "*", 0.4, "-", 0.99 );

        // when
        List<Object> result = objectUnderTest.tokenize(testExpression);

        // then
        assertThat(result).containsOnlyElementsOf(tokens);
    }

    @Test
    public void shouldTokenizeEmptyExpression() throws ParseException
    {
        // given

        // when
        List<Object> result = objectUnderTest.tokenize(EMPTY_EXPRESSION);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    public void shouldToReversePolishNotationSimpleExpression()
    {
        // given
        final List<Object> testTokens = asList(3, "+", 4, "*", 2);
        final List<Object> rpnTokens = asList(3, 4, 2, "*", "+");

        // when
        List<Object> result = objectUnderTest.toReversePolishNotation(testTokens);

        // then
        assertThat(result).containsOnlyElementsOf(rpnTokens);
    }

    @Test
    public void shouldToReversePolishNotationComplexExpression()
    {
        // given
        final List<Object> testTokens = asList("(", "(", 2, "+", 7, ")", "/", 3, "+", "(", 3, "-", 14, ")", "*", 4, ")", "/", 2);
        final List<Object> rpnTokens = asList(2, 7, "+", 3,  "/", 3, 14, "-", 4, "*", "+", 2, "/");

        // when
        List<Object> result = objectUnderTest.toReversePolishNotation(testTokens);

        // then
        assertThat(result).containsOnlyElementsOf(rpnTokens);
    }

    @Test
    public void shouldToReversePolishNotationEmptyExpression()
    {
        // given
        final List<Object> testTokens = emptyList();
        final List<Object> rpnTokens = emptyList();

        // when
        List<Object> result = objectUnderTest.toReversePolishNotation(testTokens);

        // then
        assertThat(result).containsOnlyElementsOf(rpnTokens);
    }

    @Test
    public void shouldCalculateReversePolishNotationSimpleExpression() throws ParseException
    {
        // given
        final List<Object> rpnTokens = asList(3d, 4d, 2d, "*", "+");

        // when
        double result = objectUnderTest.calculateReversePolishNotation(rpnTokens);

        // then
        assertThat(result).isEqualTo(11);
    }

    @Test
    public void shouldCalculateReversePolishNotationComplexExpression() throws ParseException
    {
        // given
        final List<Object> rpnTokens = asList(2d, 7d, "+", 3d,  "/", 3d,  14d, "-", 4d, "*", "+", 2d, "/");

        // when
        double result = objectUnderTest.calculateReversePolishNotation(rpnTokens);

        // then
        assertThat(result).isEqualTo(-20.5);
    }

    @Test
    public void shouldCalculateReversePolishNotationEmptyExpression() throws ParseException
    {
        // given
        final List<Object> rpnTokens = emptyList();

        // when
        double result = objectUnderTest.calculateReversePolishNotation(rpnTokens);

        // then
        assertThat(result).isEqualTo(0);
    }

}