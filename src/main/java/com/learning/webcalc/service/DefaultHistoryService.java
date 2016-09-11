package com.learning.webcalc.service;

import com.learning.webcalc.service.api.History;
import com.learning.webcalc.service.api.HistoryItem;
import com.learning.webcalc.service.api.HistoryService;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(value="session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class DefaultHistoryService implements HistoryService
{

    private final History history = new History();

    @Override
    public History getHistory()
    {
        return history;
    }

    @Override
    public void store(String expression, double result)
    {
        history.addItem(new HistoryItem(expression, result));
    }
}
