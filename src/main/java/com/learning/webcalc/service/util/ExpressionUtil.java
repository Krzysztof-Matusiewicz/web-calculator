package com.learning.webcalc.service.util;

import java.util.List;

import static com.learning.webcalc.service.util.Constants.FUNCTION_ARGUMENT;
import static java.lang.Character.isLetter;
import static java.util.Arrays.asList;

public class ExpressionUtil
{

    private static final List<String> LEFT_ASSOCIATIVE_OPERATORS = asList("+", "-", "*", "/");

    private static final List<String> RIGHT_ASSOCIATIVE_OPERATORS = asList("^");

    public static boolean isOperator(char character)
    {
        return isOperator(Character.toString(character));
    }

    public static boolean isOperator(Object operator)
    {
        return isLeftAssociativeOperator(operator) || isRightAssociativeOperator(operator);
    }

    public static boolean isLeftAssociativeOperator(Object token)
    {
        return LEFT_ASSOCIATIVE_OPERATORS.contains(token);
    }

    public static boolean isRightAssociativeOperator(Object token)
    {
        return RIGHT_ASSOCIATIVE_OPERATORS.contains(token);
    }

    public static int getImportance(Object operator)
    {
        if (operator.equals("+") || operator.equals("-"))
        {
            return 1;
        }
        if (operator.equals("*") || operator.equals("/"))
        {
            return 2;
        }
        if (operator.equals("^"))
        {
            return 3;
        }
        throw new UnsupportedOperationException();
    }

    public static boolean isFunction(Object token)
    {
        String tokenAsString = token.toString();
        return tokenAsString.length() == 1 && isLetter(tokenAsString.charAt(0));
    }

    public static boolean isArgumentSeparator(char character)
    {
        return Character.toString(character).equals(FUNCTION_ARGUMENT);
    }

    public static boolean isArgumentSeparator(Object token)
    {
        return token.equals(FUNCTION_ARGUMENT);
    }

}
