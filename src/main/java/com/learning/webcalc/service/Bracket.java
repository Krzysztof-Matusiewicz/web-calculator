package com.learning.webcalc.service;

import java.util.stream.Stream;

public enum Bracket implements MathBracket
{

    OPENING {
        @Override
        public char getSymbol()
        {
            return '(';
        }
    },

    CLOSING {
        @Override
        public char getSymbol()
        {
            return ')';
        }
    };

    static Bracket valueOf(Character c)
    {
        return Stream.of(values()).filter(o -> o.getSymbol() == c).findFirst().get();
    }

    public static boolean existsFor(char c)
    {
        return Stream.of(values()).filter(o -> o.getSymbol() == c).findAny().isPresent();
    }

}
