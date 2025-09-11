package com.gridnine.testing.filter;

import java.time.LocalDateTime;
import com.gridnine.testing.base.Flight;

public class FilterFlewOutEarlier implements FlightFilter {

    @Override
    public Flight filter(final Flight flight, final FlightCallback callback) {
        final LocalDateTime now = LocalDateTime.now();
        // ...
        return flight;
    }

}
