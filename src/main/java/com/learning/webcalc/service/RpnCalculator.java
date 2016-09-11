package com.learning.webcalc.service;

import com.learning.webcalc.service.api.CalculationException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Stack;

import static com.learning.webcalc.service.util.ExpressionUtil.isFunction;
import static com.learning.webcalc.service.util.ExpressionUtil.isOperator;

@Component
public class RpnCalculator
{

    public Double calculate(List<Object> tokens)
    {
        if (tokens.isEmpty())
        {
            return 0d;
        }
        Stack<Object> stack = new Stack<>();
        for (Object token : tokens)
        {
            if (token instanceof Number)
            {
                stack.push(token);
                continue;
            }
            else if (isOperator(token))
            {
                double result = calculate(token, (Double)stack.pop(), (Double)stack.pop());
                stack.push(result);
            }
            else if (isFunction(token))
            {
                stack.push(executeFunction(token, stack));
            }
            else
            {
                throw CalculationException.forUnexpectedToken(token);
            }
        }
        double result = (Double)stack.pop();
        if (!stack.isEmpty())
        {
            throw new IllegalStateException("Stack should be empty after processing whole expression");
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
            if (value2 == 0)
            {
                throw CalculationException.forDivisionByZero();
            }
            return value1 / value2;
        }
        if (operator.equals("^"))
        {
            return Math.pow(value1, value2);
        }
        throw CalculationException.forUnexpectedToken(operator);
    }

    private double executeFunction(Object token, Stack<Object> stack)
    {
        if (token.equals("s"))
        {
            return Math.sqrt((Double)stack.pop());
        }
        throw CalculationException.forUnexpectedToken(token);
    }

}
