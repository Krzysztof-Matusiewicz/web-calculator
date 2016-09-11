package com.learning.webcalc.rest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CalculatorControllerTest
{

    @Autowired
    private MockMvc mvc;
    
    @Test
    public void getCalculateSimpleExpression() throws Exception
    {
        performTestFor("3+4*2", 11);
    }

    @Test
    public void getCalculateExpressionWithWhitespaces() throws Exception
    {
        performTestFor("  3+ 4 *\t2 ", 11);
    }

    @Test
    public void getCalculateExpressionWithNonIntegersWithPeriodSeparator() throws Exception
    {
        String expression = String.format("(2.45+567.789)*0.4-0.99");
        performTestFor(expression, 227.1056);
    }

    @Test
    public void getCalculateExpressionWithNonIntegersWithCommaSeparator() throws Exception
    {
        String expression = String.format("(2,45+567,789)*0,4-0,99");
        performTestFor(expression, 227.1056);
    }

    @Test
    public void getCalculateEmptyExpression() throws Exception
    {
        performTestFor("", 0);
    }

    @Test
    public void getCalculateEmptyExpressionWithWhitespaces() throws Exception
    {
        performTestFor(" \t ", 0);
    }

    @Test
    public void getCalculateComplexExpressionWithNegativeValues() throws Exception
    {
        performTestFor("-5*(-18+(-3))", 105);
    }

    @Test
    public void getCalculateComplexExpression() throws Exception
    {
        performTestFor("((2+7)/3+(3-14)*4)/2", -20.5);
    }

    @Test
    public void getCalculateComplexExpressionWithCutResult() throws Exception
    {
        performTestFor("1+(8*10+(98/3*(20)-8))", 726.3333333333333d);
    }

    @Test
    public void getCalculateExpressionWithExponentiation() throws Exception
    {
        performTestFor("4^3^2", 262144);
    }

    @Test
    public void getCalculateExpressionWithExponentiationAndBrackets() throws Exception
    {
        performTestFor("5^(4-1)^2", 1953125);
    }

    @Test
    public void getCalculateComplexExpressionWithExponentiation() throws Exception
    {
        performTestFor("3+4*2/(1-5)^2", 3.5);
    }

    @Test
    public void getCalculateComplexExpressionWithSqrt() throws Exception
    {
        performTestFor("1+sqrt(8*10+1)/3", 4);
    }

    @Test
    public void getCalculateReturnErrorForDivisionByZero() throws Exception
    {
        mvc.perform(MockMvcRequestBuilders.get("/calculate")
                .param("exp", "5+6/0-7")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", equalTo("Cannot divide by zero")));
    }

    @Test
    public void getCalculateReturnErrorForMissingBracket() throws Exception
    {
        mvc.perform(MockMvcRequestBuilders.get("/calculate")
                .param("exp", "(5+6)/2)-7")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", equalTo("At least one bracket is missing")));
    }

    @Test
    public void getCalculateReturnErrorForUnexpectedToken() throws Exception
    {
        mvc.perform(MockMvcRequestBuilders.get("/calculate")
                .param("exp", "5+6>2-7")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", equalTo("Unexpected token: '>'")));
    }

    @Test
    public void getCalculateReturnErrorForOtherExpressionError() throws Exception
    {
        mvc.perform(MockMvcRequestBuilders.get("/calculate")
                .param("exp", "2-+7")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message", equalTo("Cannot execute expression. Check the expression value and try again.")));
    }

    private void performTestFor(String inputExpression, double expectedResult) throws Exception
    {
        mvc.perform(MockMvcRequestBuilders.get("/calculate")
                .param("exp", inputExpression)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result", equalTo(expectedResult)));
    }

}