package com.gridnine.testing.filter;

import com.gridnine.testing.base.Flight;

@FunctionalInterface
public interface FlightFilter {

    boolean process(final Flight flight);
}
