package com.gridnine.testing.filter;

import com.gridnine.testing.base.Flight;

@FunctionalInterface
public interface FlightCallback {

    void call(final String title, final Flight flight);
}
