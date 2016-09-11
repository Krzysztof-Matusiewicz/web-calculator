package com.learning.webcalc.service;

import com.learning.webcalc.service.api.CalculationException;
import org.junit.Test;

import java.text.ParseException;
import java.util.List;

import static java.util.Arrays.asList;
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
        final String testExpression = " 5 + [(7-sqrt{ 43 *3})/2 ] ";
        final String cleanExpression = "5+((7-s(43*3))/2)";

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
    public void shouldValidateBrackets()
    {
        // given
        String expression = "1+(8*10+[(98/3)^{2}-8])";

        // when
        String result = objectUnderTest.validateBrackets(expression);

        // then
        assertThat(result).isEqualTo(expression);
    }

    @Test(expected = CalculationException.class)
    public void shouldValidateBracketsExplodeForMissingOpeningBracket()
    {
        // given
        String expression = "1+8*10/3)";

        // when
        objectUnderTest.validateBrackets(expression);
    }

    @Test(expected = CalculationException.class)
    public void shouldValidateBracketsExplodeForMissingClosingBracket()
    {
        // given
        String expression = "1+(8*10/3";

        // when
        objectUnderTest.validateBrackets(expression);
    }

    @Test(expected = CalculationException.class)
    public void shouldValidateBracketsExplodeForClosingBracketNotMatchingOpening()
    {
        // given
        String expression = "1+(8*[10)/3]";

        // when
        objectUnderTest.validateBrackets(expression);
    }

    @Test
    public void shouldTokenize() throws ParseException
    {
        // given
        final String testExpression = "1+(8*10+(98/3^(2)-8))";
        final List<Object> tokens = asList(1d, "+", "(", 8d, "*", 10d, "+", "(", 98d, "/", 3d, "^", "(", 2d, ")", "-", 8d, ")", ")");

        // when
        List<Object> result = objectUnderTest.tokenize(testExpression);

        // then
        assertThat(result).containsExactlyElementsOf(tokens);
    }

    @Test
    public void shouldTokenizeWithSqrt() throws ParseException
    {
        // given
        final String testExpression = "1+s(8*10+1)/3";
        final List<Object> tokens = asList(1d, "+", "s", "(", 8d, "*", 10d, "+", 1d, ")", "/", 3d);

        // when
        List<Object> result = objectUnderTest.tokenize(testExpression);

        // then
        assertThat(result).containsExactlyElementsOf(tokens);
    }

    @Test
    public void shouldTokenizeExpressionWithNegativeValues() throws ParseException
    {
        // given
        final String testExpression = "-5*(-18+(-3))";
        final List<Object> tokens = asList(-5d, "*", "(", -18d, "+", "(", -3d, ")", ")");

        // when
        List<Object> result = objectUnderTest.tokenize(testExpression);

        // then
        assertThat(result).containsExactlyElementsOf(tokens);
    }

    @Test
    public void shouldTokenizeExpressionWithNonIntegerValues() throws ParseException
    {
        // given
        final String testExpression = String.format("(%.2f+%.3f)*%.1f-%.2f", 2.45, 567.789, 0.4, .99);
        final List<Object> tokens = asList("(", 2.45d, "+", 567.789, ")", "*", 0.4, "-", 0.99);

        // when
        List<Object> result = objectUnderTest.tokenize(testExpression);

        // then
        assertThat(result).containsExactlyElementsOf(tokens);
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

}