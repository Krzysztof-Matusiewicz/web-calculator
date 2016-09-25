package com.learning.webcalc.service.api;

import java.util.List;

public interface ExpressionProcessor
{

    String validateBracketsParity(String expression);

    String clean(String expression);

    String validateBracketsContent(String expression);

    List<Object> tokenize(String expression);

}
