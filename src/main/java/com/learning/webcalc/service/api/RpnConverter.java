package com.learning.webcalc.service.api;

import java.util.List;

public interface RpnConverter
{
    List<Object> convert(List<Object> tokens);
}
