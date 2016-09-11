package com.learning.webcalc.service;

import com.learning.webcalc.service.api.CalculationException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static com.learning.webcalc.service.util.ExpressionUtil.*;

@Component
public class RpnConverter
{

    public List<Object> convert(List<Object> tokens)
    {
        List<Object> output = new ArrayList<>();
        Stack<Object> stack = new Stack<>();

        for (Object token : tokens)
        {
            if (token instanceof Number)
            {
                output.add(token);
            }
            else if (isFunction(token))
            {
                stack.push(token);
            }
            else if (isArgumentSeparator(token))
            {
                while (!stack.peek().equals("("))
                {
                    output.add(stack.pop());
                }
            }
            else if (isOperator(token))
            {
                while (!stack.isEmpty() && (
                        leftAssociativeWithLessOrEqualPriority(token, stack.peek()) ||
                        rightAssociativeWithLessPriority(token, stack.peek())
                    ))
                {
                    output.add(stack.pop());
                }
                stack.push(token);
            }
            else if (token.equals("("))
            {
                stack.push(token);
            }
            else if (token.equals(")"))
            {
                emptyStackUntilOpenParenthesis(stack, output);
                if (!stack.isEmpty() && isFunction(stack.peek()))
                {
                    output.add(stack.pop());
                }
            }
            else
            {
                throw CalculationException.forUnexpectedToken(token);
            }
        }
        emptyStackFromRemainingOperators(stack, output);
        return output;
    }

    private void emptyStackUntilOpenParenthesis(Stack<Object> stack, List<Object> output)
    {
        while (true)
        {
            Object token = stack.pop();
            if (isOperator(token))
            {
                output.add(token);
                continue;
            }
            if (token.equals("("))
            {
                return;
            }
            throw CalculationException.forUnexpectedToken(token);
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
            throw CalculationException.forUnexpectedToken(token);
        }
    }

    private boolean leftAssociativeWithLessOrEqualPriority(Object operatorFromTokens, Object operatorOnStack)
    {
        return isLeftAssociativeOperator(operatorOnStack) && getImportance(operatorFromTokens) <= getImportance(operatorOnStack);
    }

    private boolean rightAssociativeWithLessPriority(Object operatorFromTokens, Object operatorOnStack)
    {
        return isRightAssociativeOperator(operatorOnStack) && getImportance(operatorFromTokens) < getImportance(operatorOnStack);
    }

}
