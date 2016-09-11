package com.learning.webcalc.service;

import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static java.lang.Character.isDigit;
import static java.util.Arrays.asList;

@Component
public class ExpressionProcessor
{

    private static final List<String> OPERATORS = asList("+", "-", "*", "/");

    private final NumberFormat numberFormat;

    private final char decimalSeparator;

    public ExpressionProcessor()
    {
        numberFormat = NumberFormat.getInstance();
        DecimalFormatSymbols symbols = ((DecimalFormat)numberFormat).getDecimalFormatSymbols();
        decimalSeparator = symbols.getDecimalSeparator();
    }

    public String clean(String expression)
    {
        return expression
                .replaceAll("\\s", "")
                .replaceAll("\\{", "(")
                .replaceAll("\\}", ")")
                .replaceAll("\\[", "(")
                .replaceAll("\\]", ")");
    }

    public List<Object> tokenize(String expression) throws ParseException
    {
        StringBuilder number = new StringBuilder();
        List<Object> tokens = new ArrayList<>();

        for (char c : expression.toCharArray())
        {
            if (isDigit(c) || isDecimalSeparator(c) || isMinusSignAsPartOfNegativeValue(c, number, tokens))
            {
                number.append(c);
            }
            else if (isOperator(c) || isParenthesis(c))
            {
                if (number.length() > 0)
                {
                    tokens.add(toDouble(number.toString()));
                    number = new StringBuilder();
                }
                tokens.add(Character.toString(c)); // TODO: keep character
            }
            else
            {
                throw new UnsupportedOperationException();
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

    public List<Object> toReversePolishNotation(List<Object> tokens)
    {
        List<Object> output = new ArrayList<>();
        Stack<Object> stack = new Stack<>();

        for (Object token : tokens)
        {
            if (token instanceof Number)
            {
                output.add(token);
                continue;
            }
            if (isOperator(token))
            {
                while (!stack.isEmpty() && shouldBeTaken(stack.peek(), token))
                {
                    output.add(stack.pop());
                }
                stack.push(token);
                continue;
            }
            if (token.equals("("))
            {
                stack.push(token);
                continue;
            }
            if (token.equals(")"))
            {
                emptyStackUntilOpenParenthesis(stack, output);
                continue;
            }
            throw new IllegalStateException();
        }
        emptyStackFromRemainingOperators(stack, output);
        return output;
    }

    private void emptyStackUntilOpenParenthesis(Stack<Object> stack, List<Object> output)
    {
        while (true)
        {
            Object tokenFromStack = stack.pop();
            if (isOperator(tokenFromStack))
            {
                output.add(tokenFromStack);
                continue;
            }
            if (tokenFromStack.equals("("))
            {
                return;
            }
            throw new IllegalStateException();
        }
    }

    private void emptyStackFromRemainingOperators(Stack<Object> stack, List<Object> output)
    {
        while (!stack.isEmpty())
        {
            Object token = stack.pop();
            if (isOperator(token))
            {
                output.add(token);
                continue;
            }
            throw new IllegalStateException();
        }
    }

    private boolean shouldBeTaken(Object operatorOnStack, Object operatorFromTokens) // TODO: rename
    {
        return isOperator(operatorOnStack) && getImportance(operatorFromTokens) <= getImportance(operatorOnStack);
    }

    private boolean isParenthesis(char character)
    {
        return character == '(' || character == ')';
    }

    public boolean isNumber(String value)
    {
        try
        {
            numberFormat.parse(value);
            return true;
        }
        catch (ParseException e)
        {
            return false;
        }
    }

    private boolean isOperator(char character)
    {
        return isOperator(Character.toString(character));
    }

    private boolean isOperator(Object token)
    {
        return OPERATORS.contains(token);
    }

    private int getImportance(Object operatorFromTokens)
    {
        if (operatorFromTokens.equals("+") || operatorFromTokens.equals("-")) // TODO: use enums for operators?
        {
            return 1;
        }
        if (operatorFromTokens.equals("*") || operatorFromTokens.equals("/"))
        {
            return 2;
        }
        throw new UnsupportedOperationException();
    }

    public double calculateReversePolishNotation(List<Object> tokens) throws ParseException
    {
        if (tokens.isEmpty())
        {
            return 0;
        }
        Stack<Object> stack = new Stack<>();
        for (Object token : tokens)
        {
            if (token instanceof Number)
            {
                stack.push(token);
                continue;
            }
            if (isOperator(token))
            {
                double result = calculate(token, (Double)stack.pop(), (Double)stack.pop());
                stack.push(result);
            }
        }
        double result = (Double)stack.pop();
        if (!stack.isEmpty())
        {
            throw new IllegalStateException();
        }
        return result;
    }

    private double toDouble(String value) throws ParseException
    {
        return numberFormat.parse(value).doubleValue();
    }

    private double calculate(Object operator, double value2, double value1)
    {
        if (operator.equals("+"))
        {
            return value1 + value2;
        }
        if (operator.equals("-"))
        {
            return value1 - value2;
        }
        if (operator.equals("*"))
        {
            return value1 * value2;
        }
        if (operator.equals("/"))
        {
            return value1 / value2;
        }
        throw new UnsupportedOperationException();
    }

}
