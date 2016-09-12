package com.learning.webcalc.service.api;

import java.util.List;

public interface ExpressionProcessor
{

    String validateBrackets(String expression);

    String clean(String expression);

    List<Object> tokenize(String expression);

}
