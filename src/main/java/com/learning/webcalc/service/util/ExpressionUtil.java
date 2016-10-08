package com.learning.webcalc.service.util;

import com.learning.webcalc.service.Operator;
import java.util.List;

import static com.learning.webcalc.service.util.Constants.ARGUMENT_SEPARATOR;
import static java.lang.Character.isLetter;
import static java.util.Arrays.asList;

public class ExpressionUtil
{

    public static boolean isFunction(Object token)
    {
        String tokenAsString = token.toString();
        return tokenAsString.length() == 1 && isLetter(tokenAsString.charAt(0));
    }

    public static boolean isArgumentSeparator(char character)
    {
        return Character.toString(character).equals(ARGUMENT_SEPARATOR);
    }

    public static boolean isArgumentSeparator(Object token)
    {
        return token.equals(ARGUMENT_SEPARATOR);
    }

}
