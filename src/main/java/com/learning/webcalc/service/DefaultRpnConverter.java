package com.learning.webcalc.service;

import com.learning.webcalc.service.api.CalculationException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static com.learning.webcalc.service.util.ExpressionUtil.*;

@Component
public class DefaultRpnConverter implements com.learning.webcalc.service.api.RpnConverter
{

    @Override
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
                while (stack.peek() != Bracket.OPENING)
                {
                    output.add(stack.pop());
                }
            }
            else if (token instanceof Operator)
            {
                final Operator operator = (Operator)token;
                while (!stack.isEmpty() && stack.peek() instanceof Operator && (
                        leftAssociativeWithLessOrEqualPriority(operator, (Operator)stack.peek()) ||
                        rightAssociativeWithLessPriority(operator, (Operator)stack.peek())
                    ))
                {
                    output.add(stack.pop());
                }
                stack.push(operator);
            }
            else if (token == Bracket.OPENING)
            {
                stack.push(token);
            }
            else if (token == Bracket.CLOSING)
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
        while (!stack.isEmpty())
        {
            Object token = stack.pop();
            if (token instanceof Operator)
            {
                output.add(token);
                continue;
            }
            if (token == Bracket.OPENING)
            {
                return;
            }
            throw CalculationException.forUnexpectedToken(token);
        }
        throw CalculationException.forProcessingError();
    }

    private void emptyStackFromRemainingOperators(Stack<Object> stack, List<Object> output)
    {
        while (!stack.isEmpty())
        {
            Object token = stack.pop();
            if (token instanceof Operator)
            {
                output.add(token);
                continue;
            }
            throw CalculationException.forUnexpectedToken(token);
        }
    }

    private boolean leftAssociativeWithLessOrEqualPriority(Operator operatorFromTokens, Operator operatorOnStack)
    {
        return operatorOnStack.getAssociativity() == MathOperator.Associativity.LEFT && operatorFromTokens.getImportance() <= operatorOnStack.getImportance();
    }

    private boolean rightAssociativeWithLessPriority(Operator operatorFromTokens, Operator operatorOnStack)
    {
        return operatorOnStack.getAssociativity() == MathOperator.Associativity.RIGHT && operatorFromTokens.getImportance() < operatorOnStack.getImportance();
    }

}
