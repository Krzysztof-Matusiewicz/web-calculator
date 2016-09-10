package com.learning.webcalc;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController
{

    @RequestMapping(path = "/history", method = RequestMethod.GET)
    public HistoryResponse history()
    {
        HistoryResponse response = new HistoryResponse();
        response.setValue("The history list");
        return response;
    }

    @RequestMapping(path = "/calculate/{query}", method = RequestMethod.GET)
    public CalculateResponse calculate(@PathVariable String query)
    {
        CalculateResponse response = new CalculateResponse();
        response.setValue("Result for query: " + query);
        return response;
    }

}
