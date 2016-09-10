package com.learning.webcalc.rest;

import com.learning.webcalc.service.api.History;
import com.learning.webcalc.service.api.HistoryService;

public class HistoryResponse
{

    private History history;

    public History getHistory()
    {
        return history;
    }

    public void setHistory(History history)
    {
        this.history = history;
    }

}
