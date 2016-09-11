package com.learning.webcalc.service;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static com.learning.webcalc.service.util.ExpressionUtil.isOperator;

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

}
