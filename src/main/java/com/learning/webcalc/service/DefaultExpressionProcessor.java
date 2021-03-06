package com.learning.webcalc.service;

import com.learning.webcalc.service.api.CalculationException;
import com.learning.webcalc.service.api.ExpressionProcessor;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static com.learning.webcalc.service.Operator.EXPONENTIATION;
import static com.learning.webcalc.service.util.Constants.*;
import static com.learning.webcalc.service.util.ExpressionUtil.isArgumentSeparator;
import static java.lang.Character.isDigit;
import static java.lang.Character.isLetter;
import static java.util.Arrays.asList;

@Component
public class DefaultExpressionProcessor implements ExpressionProcessor
{

    private static final List<Object> TOKENS_PRECEDING_NEGATIVE_VALUE = asList(Bracket.OPENING, EXPONENTIATION, ARGUMENT_SEPARATOR);

    private static final String EMPTY_BRACKETS = String.valueOf(Bracket.OPENING.getSymbol()) + String.valueOf(Bracket.CLOSING.getSymbol());

    private final NumberFormat numberFormat;

    private final char decimalSeparator;

    public DefaultExpressionProcessor()
    {
        numberFormat = NumberFormat.getInstance();
        DecimalFormatSymbols symbols = ((DecimalFormat)numberFormat).getDecimalFormatSymbols();
        decimalSeparator = symbols.getDecimalSeparator();
    }

    public String validateBracketsParity(String expression)
    {
        int openedBrackets = 0;
        for (char c : expression.toCharArray())
        {
            if (c == Bracket.OPENING.getSymbol())
            {
                openedBrackets++;
            }
            if (c == Bracket.CLOSING.getSymbol())
            {
                if (--openedBrackets < 0)
                {
                    throw CalculationException.forIncorrectBrackets();
                }
            }
        }
        if (openedBrackets != 0)
        {
            throw CalculationException.forIncorrectBrackets();
        }
        return expression;
    }

    public String clean(String expression)
    {
        return expression
                .replaceAll("\\s", "")
                .replaceAll("\\.|,", Character.toString(decimalSeparator))
                .replaceAll(SQRT_NAME, SQRT_SYMBOL)
                .replaceAll(INTEGRAL_NAME, INTEGRAL_SYMBOL);
    }

    public String validateBracketsContent(String expression)
    {
        if (expression.contains(EMPTY_BRACKETS))
        {
            throw CalculationException.forIncorrectBrackets();
        }
        return expression;
    }

    public List<Object> tokenize(String expression)
    {
        try
        {
            return tryTokenize(expression);
        }
        catch (ParseException e)
        {
            throw CalculationException.forProcessingError(e);
        }
    }

    private List<Object> tryTokenize(String expression) throws ParseException
    {
        StringBuilder number = new StringBuilder();
        List<Object> tokens = new ArrayList<>();
        for (char c : expression.toCharArray())
        {
            if (isDigit(c) || isDecimalSeparator(c) || isMinusSignAsPartOfNegativeValue(c, number, tokens))
            {
                number.append(c);
            }
            else if (Operator.existsFor(c) || Bracket.existsFor(c) || isFunction(c) || isArgumentSeparator(c))
            {
                if (number.length() > 0)
                {
                    tokens.add(toDouble(number.toString()));
                    number = new StringBuilder();
                }
                tokens.add(createToken(c));
            }
            else
            {
                throw CalculationException.forUnexpectedToken(c);
            }
        }
        if (number.length() > 0)
        {
            tokens.add(toDouble(number.toString()));
        }
        return tokens;
    }

    private boolean isDecimalSeparator(char character)
    {
        return character == decimalSeparator;
    }

    private boolean isMinusSignAsPartOfNegativeValue(char character, StringBuilder number, List<Object> tokens)
    {
        return character == '-' && number.length() == 0 && beginningOfValue(tokens);
    }

    private boolean beginningOfValue(List<Object> tokens)
    {
        if (tokens.isEmpty())
        {
            return true;
        }
        Object lastToken = tokens.get(tokens.size()-1);
        return TOKENS_PRECEDING_NEGATIVE_VALUE.contains(lastToken);
    }

    private boolean isFunction(char character)
    {
        return isLetter(character);
    }

    private double toDouble(String value) throws ParseException
    {
        return numberFormat.parse(value).doubleValue();
    }

    private Object createToken(char c)
    {
        if (Operator.existsFor(c))
        {
            return Operator.valueOf(c);
        }
        if (Bracket.existsFor(c))
        {
            return Bracket.valueOf(c);
        }
        return Character.toString(c);
    }

}
