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

import static org.hamcrest.Matchers.closeTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CalculatorControllerIntegralTest
{

    private static final double TEST_PRECISION = 0.00000000001;

    @Autowired
    private MockMvc mvc;
    
    @Test
    public void getCalculateIntegralAsPartOfExpression() throws Exception
    {
        performTestFor("5+integral(1;(6-4);(1.5*2);sqrt(16))^2", 26.8161322856996);
    }

    private void performTestFor(String inputExpression, double expectedResult) throws Exception
    {
        mvc.perform(MockMvcRequestBuilders.get("/calculate")
                .param("exp", inputExpression)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result", closeTo(expectedResult, TEST_PRECISION)));
    }

}