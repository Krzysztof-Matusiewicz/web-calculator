package com.learning.webcalc.service;

import static java.util.Arrays.asList;

import com.learning.webcalc.service.api.CalculationException;
import java.util.List;
import java.util.function.*;
import java.util.stream.Stream;

public enum Operator implements MathOperator
{

    ADDITION {
                @Override
                public char getSymbol()
                {
                    return '+';
                }

                @Override
                public Double apply(Double value1, Double value2)
                {
                    return value1 + value2;
                }

                @Override
                public Associativity getAssociativity()
                {
                    return Associativity.LEFT;
                }

                @Override
                public int getImportance()
                {
                    return 1;
                }
    },

    SUBTRACTION {
                @Override
                public char getSymbol()
                {
                    return '-';
                }

                @Override
                public Double apply(Double value1, Double value2)
                {
                    return value1 - value2;
                }

                @Override
                public Associativity getAssociativity()
                {
                    return Associativity.LEFT;
                }

                @Override
                public int getImportance()
                {
                    return 1;
                }
            },

    MULTIPLICATION {
                @Override
                public char getSymbol()
                {
                    return '*';
                }

                @Override
                public Double apply(Double value1, Double value2)
                {
                    return value1 * value2;
                }

                @Override
                public Associativity getAssociativity()
                {
                    return Associativity.LEFT;
                }

                @Override
                public int getImportance()
                {
                    return 2;
                }
            },

    DIVISION
            {
                @Override
                public char getSymbol()
                {
                    return '/';
                }

                @Override
                public Double apply(Double value1, Double value2)
                {
                    if (value2 == 0)
                    {
                        throw CalculationException.forDivisionByZero();
                    }
                    return value1 / value2;
                }

                @Override
                public Associativity getAssociativity()
                {
                    return Associativity.LEFT;
                }

                @Override
                public int getImportance()
                {
                    return 2;
                }
            },

    EXPONENTIATION
            {
                @Override
                public char getSymbol()
                {
                    return '^';
                }

                @Override
                public Double apply(Double value1, Double value2)
                {
                    return Math.pow(value1, value2);
                }

                @Override
                public Associativity getAssociativity()
                {
                    return Associativity.RIGHT;
                }

                @Override
                public int getImportance()
                {
                    return 3;
                }
            };

    static Operator valueOf(Character c)
    {
        return Stream.of(values()).filter(o -> o.getSymbol() == c).findFirst().get();
    }

    public static boolean existsFor(char c)
    {
        return Stream.of(values()).filter(o -> o.getSymbol() == c).findAny().isPresent();
    }

}
