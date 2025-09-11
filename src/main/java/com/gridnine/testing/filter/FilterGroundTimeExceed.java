package com.gridnine.testing.filter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.gridnine.testing.base.Flight;
import com.gridnine.testing.base.Segment;

public class FilterGroundTimeExceed implements FlightFilter {

    private static final Logger logger =
            LoggerFactory.getLogger(FilterGroundTimeExceed.class);

    public boolean process(final Flight flight, final Object... args) {

        long groundLimit = 120; // 2 hours by default
        if (args != null && args.length > 0 && args[0] instanceof Long longValue) {
            groundLimit = longValue;
        }
        long groundTime = 0;

        List<Segment> segments = flight.getSegments();
        int upperIndex = segments.size() - 1;
        for (int i = 0; i < upperIndex; i++) {
            LocalDateTime arrival = segments.get(i).getArrivalDate();
            LocalDateTime nextDeparture = segments.get(i + 1).getDepartureDate();
            groundTime += Duration.between(arrival, nextDeparture).toMinutes();

            if (groundTime > groundLimit) {
                logger.info("Ground time exceeded: {}", flight);
                return true;
            }
        }

        return false;
    }

}
