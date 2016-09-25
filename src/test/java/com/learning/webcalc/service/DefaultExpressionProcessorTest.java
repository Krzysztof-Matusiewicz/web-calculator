package com.learning.webcalc.service;

import com.learning.webcalc.service.api.CalculationException;
import org.junit.Test;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class DefaultExpressionProcessorTest
{

    private static final String EMPTY_EXPRESSION = "";

    private final char decimalSeparator;

    private DefaultExpressionProcessor objectUnderTest;

    public DefaultExpressionProcessorTest()
    {
        this.objectUnderTest = new DefaultExpressionProcessor();
        DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getInstance();
        DecimalFormatSymbols symbols = decimalFormat.getDecimalFormatSymbols();
        decimalSeparator = symbols.getDecimalSeparator();
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
    public void shouldCleanReplaceCommaSeparatorsToDefault()
    {
        // given
        final String testExpression = "5,5+4,65*(0,123-1)";
        final String cleanExpression = testExpression.replace(',', decimalSeparator);

        // when
        String result = objectUnderTest.clean(testExpression);

        // then
        assertThat(result).isEqualTo(cleanExpression);
    }

    @Test
    public void shouldCleanTranslateSqrt()
    {
        // given
        final String testExpression = "sqrt(3+8)-(sqrt(-4))^sqrt(0)";
        final String cleanExpression = "s(3+8)-(s(-4))^s(0)";

        // when
        String result = objectUnderTest.clean(testExpression);

        // then
        assertThat(result).isEqualTo(cleanExpression);
    }

    @Test
    public void shouldCleanTranslateIntegral()
    {
        // given
        final String testExpression = "integral(3;4;5;6)-(integral(-4))^integral(0)";
        final String cleanExpression = "i(3;4;5;6)-(i(-4))^i(0)";

        // when
        String result = objectUnderTest.clean(testExpression);

        // then
        assertThat(result).isEqualTo(cleanExpression);
    }

    @Test
    public void shouldCleanReplacePeriodSeparatorsToDefault()
    {
        // given
        final String testExpression = "5.5+4.65*(0.123-1)";
        final String cleanExpression = testExpression.replace('.', decimalSeparator);

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
    public void shouldTokenizeWithIntegral() throws ParseException
    {
        // given
        final String testExpression = "1+i(8*10;4-5;13)/3";
        final List<Object> tokens = asList(1d, "+", "i", "(", 8d, "*", 10d, ";", 4d, "-", 5d, ";", 13d, ")", "/", 3d);

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
    public void shouldTokenizeExpressionWithNegativeValuesInsideFunction() throws ParseException
    {
        // given
        final String testExpression = "i(-4;-5;-6)";
        final List<Object> tokens = asList("i", "(", -4d, ";", -5d, ";", -6d, ")");

        // when
        List<Object> result = objectUnderTest.tokenize(testExpression);

        // then
        assertThat(result).containsExactlyElementsOf(tokens);
    }

    @Test
    public void shouldTokenizeExpressionWithNegativePowerExponent() throws ParseException
    {
        // given
        final String testExpression = "8^-2";
        final List<Object> tokens = asList(8d, "^", -2d);

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