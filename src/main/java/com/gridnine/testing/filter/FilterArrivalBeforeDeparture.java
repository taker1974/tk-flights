package com.gridnine.testing.filter;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.gridnine.testing.base.Flight;
import com.gridnine.testing.base.Segment;

/**
 * Filter by "arrival before departure".
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
public class FilterArrivalBeforeDeparture implements FlightFilter {

    private static final Logger logger =
            LoggerFactory.getLogger(FilterArrivalBeforeDeparture.class);

    /**
     * Method for finding an element with arrival time before departure time. Uses streams. Can be
     * parallelized.
     * 
     * @param flight Flight object.
     * @param args Arguments.
     * @return true if there is an element with arrival time before departure time, false otherwise.
     *         Also return false if segments is null or empty.
     * @throws IllegalArgumentException if flight is null.
     */
    @Override
    public boolean process(final Flight flight, final Object... args) {

        if (flight == null) {
            throw new IllegalArgumentException("Flight cannot be null");
        }

        List<Segment> segments = flight.getSegments();
        if (segments == null || segments.isEmpty()) {
            return false;
        }

        boolean hasInvalidSegment = segments.stream()
                .anyMatch(segment -> segment.getArrivalDate().isBefore(segment.getDepartureDate()));

        if (hasInvalidSegment) {
            logger.info("Arrival before departure: {}", flight);
        }

        return hasInvalidSegment;
    }
}
