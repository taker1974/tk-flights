package com.gridnine.testing.filter;

import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.gridnine.testing.base.Flight;
import com.gridnine.testing.base.Segment;

public class FilterFlewOutEarlier implements FlightFilter {

    private static final Logger logger =
            LoggerFactory.getLogger(FilterFlewOutEarlier.class);

    public boolean process(final Flight flight) {
        final LocalDateTime now = LocalDateTime.now();

        for (final Segment segment : flight.getSegments()) {
            if (segment.getDepartureDate().isBefore(now)) {
                logger.info("Flew out: {}", flight);
                return true;
            }
        }

        return false;
    }

}
