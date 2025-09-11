package com.gridnine.testing.filter;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.gridnine.testing.base.Flight;
import com.gridnine.testing.base.Segment;

public class FilterArrivalBeforeDeparture implements FlightFilter {

    private static final Logger logger =
            LoggerFactory.getLogger(FilterArrivalBeforeDeparture.class);

    public boolean process(final Flight flight, final Object... args) {

        if (flight == null) {
            throw new IllegalArgumentException("Flight cannot be null");
        }

        List<Segment> segments = flight.getSegments();
        if (segments == null || segments.isEmpty()) {
            return false;
        }

        for (final Segment segment : flight.getSegments()) {
            if (segment.getArrivalDate().isBefore(segment.getDepartureDate())) {
                logger.info("Arrival befor departure: {}", flight);
                return true;
            }
        }

        return false;
    }
}
