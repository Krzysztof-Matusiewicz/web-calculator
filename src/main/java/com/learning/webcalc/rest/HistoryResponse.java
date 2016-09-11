package com.learning.webcalc.rest;

import com.learning.webcalc.service.api.History;

public class HistoryResponse
{

    private final History history;

    public History getHistory()
    {
        return history;
    }

    public HistoryResponse(History history)
    {
        this.history = history;
    }

}
