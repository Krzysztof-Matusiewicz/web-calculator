package com.learning.webcalc.service;

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
            if (token instanceof Number) // TODO: else if
            {
                output.add(token);
                continue;
            }
            if (isFunction(token))
            {
                stack.push(token);
                continue;
            }
            if (isOperator(token))
            {
                while (!stack.isEmpty() && (
                        leftAssociativeWithLessOrEqualPriority(token, stack.peek()) ||
                        rightAssociativeWithLessPriority(token, stack.peek())
                    ))
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
                if (!stack.isEmpty() && isFunction(stack.peek()))
                {
                    output.add(stack.pop());
                }
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

    private boolean leftAssociativeWithLessOrEqualPriority(Object operatorFromTokens, Object operatorOnStack) // TODO: rename
    {
        return isLeftAssociativeOperator(operatorOnStack) && getImportance(operatorFromTokens) <= getImportance(operatorOnStack);
    }

    private boolean rightAssociativeWithLessPriority(Object operatorFromTokens, Object operatorOnStack)
    {
        return isRightAssociativeOperator(operatorOnStack) && getImportance(operatorFromTokens) < getImportance(operatorOnStack);
    }



}
