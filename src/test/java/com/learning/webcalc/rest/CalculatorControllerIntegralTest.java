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
public class CalculatorControllerIntegralTest
{

    @Autowired
    private MockMvc mvc;
    
    @Test
    public void getCalculateIntegralAsPartOfExpression() throws Exception
    {
        performTestFor("5+integral(1;(6-4);(1.5*2);sqrt(16))^2", 105); // TODO: use real values
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