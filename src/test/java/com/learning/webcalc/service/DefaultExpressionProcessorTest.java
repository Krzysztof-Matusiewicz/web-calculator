package com.learning.webcalc.service;

import com.learning.webcalc.service.api.CalculationException;
import org.junit.Test;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;

import static com.learning.webcalc.service.Operator.ADDITION;
import static com.learning.webcalc.service.Operator.DIVISION;
import static com.learning.webcalc.service.Operator.EXPONENTIATION;
import static com.learning.webcalc.service.Operator.MULTIPLICATION;
import static com.learning.webcalc.service.Operator.SUBTRACTION;
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
        final String testExpression = " 5 + ((7-( 43 *3))/2 ) ";
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
    public void shouldValidateBracketsParity()
    {
        // given
        String expression = "1+(8*10+((98/3)^(2)-8))";

        // when
        String result = objectUnderTest.validateBracketsParity(expression);

        // then
        assertThat(result).isEqualTo(expression);
    }

    @Test(expected = CalculationException.class)
    public void shouldValidateBracketsParityExplodeForMissingOpeningBracket()
    {
        // given
        String expression = "1+8*10/3)";

        // when
        objectUnderTest.validateBracketsParity(expression);
    }

    @Test(expected = CalculationException.class)
    public void shouldValidateBracketsParityExplodeForMissingClosingBracket()
    {
        // given
        String expression = "1+(8*10/3";

        // when
        objectUnderTest.validateBracketsParity(expression);
    }

    @Test(expected = CalculationException.class)
    public void shouldValidateBracketsContentExplodeForEmptyBrackets()
    {
        // given
        String expression = "1+()/3";

        // when
        objectUnderTest.validateBracketsContent(expression);
    }

    @Test(expected = CalculationException.class)
    public void shouldValidateBracketsContentExplodeForClosingBracketBeforeOpening()
    {
        // given
        String expression = "1+)8*10(/3";

        // when
        objectUnderTest.validateBracketsParity(expression);
    }

    @Test
    public void shouldTokenize() throws ParseException
    {
        // given
        final String testExpression = "1+(8*10+(98/3^(2)-8))";
        final List<Object> tokens = asList(1d, ADDITION, Bracket.OPENING, 8d, MULTIPLICATION, 10d, ADDITION, Bracket.OPENING, 98d, DIVISION, 3d, EXPONENTIATION, Bracket.OPENING, 2d, Bracket.CLOSING, SUBTRACTION, 8d, Bracket.CLOSING, Bracket.CLOSING);

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
        final List<Object> tokens = asList(1d, ADDITION, "s", Bracket.OPENING, 8d, MULTIPLICATION, 10d, ADDITION, 1d, Bracket.CLOSING, DIVISION, 3d);

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
        final List<Object> tokens = asList(1d, ADDITION, "i", Bracket.OPENING, 8d, MULTIPLICATION, 10d, ";", 4d, SUBTRACTION, 5d, ";", 13d, Bracket.CLOSING, DIVISION, 3d);

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
        final List<Object> tokens = asList(-5d, MULTIPLICATION, Bracket.OPENING, -18d, ADDITION, Bracket.OPENING, -3d, Bracket.CLOSING, Bracket.CLOSING);

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
        final List<Object> tokens = asList("i", Bracket.OPENING, -4d, ";", -5d, ";", -6d, Bracket.CLOSING);

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
        final List<Object> tokens = asList(8d, EXPONENTIATION, -2d);

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
        final List<Object> tokens = asList(Bracket.OPENING, 2.45d, ADDITION , 567.789, Bracket.CLOSING, MULTIPLICATION, 0.4, SUBTRACTION, 0.99);

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