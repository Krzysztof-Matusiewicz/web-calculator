package com.learning.webcalc.service.api;

import java.util.List;

public interface RpnCalculator
{

    Double calculate(List<Object> tokens);

}
