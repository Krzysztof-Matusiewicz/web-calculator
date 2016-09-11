package com.learning.webcalc.service.util;

import java.util.List;

import static java.util.Arrays.asList;

public class ExpressionUtil
{

    private static final List<String> OPERATORS = asList("+", "-", "*", "/");

    public static boolean isOperator(char character)
    {
        return isOperator(Character.toString(character));
    }

    public static boolean isOperator(Object token)
    {
        return OPERATORS.contains(token);
    }

}
