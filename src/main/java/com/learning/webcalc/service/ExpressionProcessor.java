package com.learning.webcalc.service;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Double.valueOf;
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;
import static org.apache.commons.lang3.StringUtils.isNumeric;

@Component
public class ExpressionProcessor
{

    private static final String[] TOKENS = new String[] { "+", "-", "*", "/", "(", ")", "[", "]", "{", "}" };

    private static final String TOKENS_PATTERN = stream(TOKENS).map(Pattern::quote).collect(joining("|"));

    private static final List<String> OPERATORS = asList("+", "-", "*", "/");

    public String clean(String expression)
    {
        return expression
                .replaceAll("\\s", "")
                .replaceAll("\\{", "(")
                .replaceAll("\\}", ")")
                .replaceAll("\\[", "(")
                .replaceAll("\\]", ")");
    }

    public List<String> tokenize(String expression)
    {
        List<String> tokens = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\d+|" + TOKENS_PATTERN);
        Matcher matcher = pattern.matcher(expression);
        while (matcher.find())
        {
            tokens.add(matcher.group());
        }
        return tokens;
    }

    public List<String> toReversePolishNotation(List<String> tokens)
    {
        List<String> output = new ArrayList<>();
        Stack<String> stack = new Stack<>();

        for (String token : tokens)
        {
            if (isNumeric(token))
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

    private void emptyStackUntilOpenParenthesis(Stack<String> stack, List<String> output)
    {
        while (true)
        {
            String tokenFromStack = stack.pop();
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

    private void emptyStackFromRemainingOperators(Stack<String> stack, List<String> output)
    {
        while (!stack.isEmpty())
        {
            String token = stack.pop();
            if (isOperator(token))
            {
                output.add(token);
                continue;
            }
            throw new IllegalStateException();
        }
    }

    private boolean shouldBeTaken(String operatorOnStack, String operatorFromTokens) // TODO: rename
    {
        return isOperator(operatorOnStack) && getImportance(operatorFromTokens) <= getImportance(operatorOnStack);
    }

    private boolean isOperator(String token)
    {
        return OPERATORS.contains(token);
    }

    private int getImportance(String operatorFromTokens)
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

    public double calculateReversePolishNotation(List<String> tokens)
    {
        if (tokens.isEmpty())
        {
            return 0;
        }
        Stack<String> stack = new Stack<>();
        for (String token : tokens)
        {
            if (isNumeric(token))
            {
                stack.push(token);
                continue;
            }
            if (isOperator(token))
            {
                double result = calculate(token, valueOf(stack.pop()), valueOf(stack.pop()));
                stack.push(Double.toString(result));
            }
        }
        double result = valueOf(stack.pop());
        if (!stack.isEmpty())
        {
            throw new IllegalStateException();
        }
        return result;
    }

    private double calculate(String operator, double value2, double value1)
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
