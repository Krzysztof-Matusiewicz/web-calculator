package com.learning.webcalc.service;

import com.learning.webcalc.service.api.CalculationException;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static com.learning.webcalc.service.util.ExpressionUtil.isOperator;
import static java.lang.Character.isDigit;
import static java.lang.Character.isLetter;

@Component
public class ExpressionProcessor
{

    private final NumberFormat numberFormat;

    private final char decimalSeparator;

    public ExpressionProcessor()
    {
        numberFormat = NumberFormat.getInstance();
        DecimalFormatSymbols symbols = ((DecimalFormat)numberFormat).getDecimalFormatSymbols();
        decimalSeparator = symbols.getDecimalSeparator();
    }

    public String validateBrackets(String expression)
    {
        Stack<Character> brackets = new Stack<>();
        for (char c : expression.toCharArray())
        {
            if (c == '(' || c == '[' || c == '{')
            {
                brackets.push(c);
            }
            if (c == ')' || c == ']' || c == '}')
            {
                if (brackets.isEmpty() || !bracketsMatch(brackets.pop(), c))
                {
                    throw CalculationException.forInvalidBrackets();
                }
            }
        }
        if (!brackets.isEmpty())
        {
            throw CalculationException.forInvalidBrackets();
        }
        return expression;
    }

    private boolean bracketsMatch(char openingBracket, char closingBracket)
    {
        if (openingBracket == '(' && closingBracket == ')')
        {
            return true;
        }
        if (openingBracket == '[' && closingBracket == ']')
        {
            return true;
        }
        if (openingBracket == '{' && closingBracket == '}')
        {
            return true;
        }
        return false;
    }

    public String clean(String expression)
    {
        return expression
                .replaceAll("\\s", "")
                .replaceAll("\\{", "(")
                .replaceAll("\\}", ")")
                .replaceAll("\\[", "(")
                .replaceAll("\\]", ")")
                .replaceAll("sqrt", "s");
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
            else if (isOperator(c) || isParenthesis(c) || isFunction(c))
            {
                if (number.length() > 0)
                {
                    tokens.add(toDouble(number.toString()));
                    number = new StringBuilder();
                }
                tokens.add(Character.toString(c));
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
        return tokens.isEmpty() || tokens.get(tokens.size()-1).equals("(");
    }

    private boolean isParenthesis(char character)
    {
        return character == '(' || character == ')';
    }

    private boolean isFunction(char character)
    {
        return isLetter(character);
    }

    private double toDouble(String value) throws ParseException
    {
        return numberFormat.parse(value).doubleValue();
    }

}
