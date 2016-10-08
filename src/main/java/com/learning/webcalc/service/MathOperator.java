package com.learning.webcalc.service;

import java.util.function.BinaryOperator;

public interface MathOperator extends BinaryOperator<Double>
{

    Associativity getAssociativity();

    int getImportance();

    char getSymbol();

    public enum Associativity
    {

        LEFT,

        RIGHT

    }

}
