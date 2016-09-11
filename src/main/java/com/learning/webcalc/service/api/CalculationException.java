package com.learning.webcalc.service.api;

public class CalculationException extends RuntimeException
{

    public CalculationException(String message)
    {
        super(message);
    }

    public CalculationException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public static CalculationException forDivisionByZero()
    {
        return new CalculationException("Cannot divide by zero");
    }

    public static CalculationException forProcessingError(Exception cause)
    {
        return new CalculationException("Invalid expression", cause);
    }

    public static CalculationException forUnexpectedToken(Object token)
    {
        return new CalculationException(String.format("Unexpected token: '%s'", token));
    }

    public static CalculationException forInvalidBrackets()
    {
        return new CalculationException("At least one bracket is missing");
    }

}
