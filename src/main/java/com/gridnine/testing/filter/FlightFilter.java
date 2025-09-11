package com.gridnine.testing.filter;

import com.gridnine.testing.base.Flight;

public interface FlightFilter {

    Flight filter(final Flight flight, final FlightCallback callback);
}
