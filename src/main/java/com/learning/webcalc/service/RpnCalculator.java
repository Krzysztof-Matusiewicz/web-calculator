package com.learning.webcalc.service;

import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.List;
import java.util.Stack;

import static com.learning.webcalc.service.util.ExpressionUtil.isOperator;

@Component
public class RpnCalculator
{

    public double calculate(List<Object> tokens) throws ParseException
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
        if (operator.equals("^"))
        {
            return Math.pow(value1, value2);
        }
        throw new UnsupportedOperationException();
    }

}
