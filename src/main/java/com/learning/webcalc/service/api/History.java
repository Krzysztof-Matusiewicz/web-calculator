package com.learning.webcalc.service.api;

import java.util.ArrayList;
import java.util.List;

public class History
{

    private final List<HistoryItem> items = new ArrayList<>();

    public List<HistoryItem> getItems()
    {
        return items;
    }

    public void addItem(HistoryItem historyItem)
    {
        items.add(historyItem);
    }

}
