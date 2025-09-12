package com.gridnine.testing.filter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.gridnine.testing.base.Flight;
import com.gridnine.testing.base.Segment;

/**
 * Filter by "sum of ground time".
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
public class FilterGroundTimeExceed implements FlightFilter {

    /** Default grounf time limit, minutes. */
    public static final long DEFAULT_GROUND_LIMIT = 120;

    private static final Logger logger =
            LoggerFactory.getLogger(FilterGroundTimeExceed.class);

    /**
     * Method of summing time spent on the ground. Can use streams. Can be parallelized.
     * 
     * @param flight Flight object.
     * @param args Arguments. arg[0] can be a long value with limit for ground time, minutes.
     * @return true if there is an element with arrival time before departure time, false otherwise.
     *         Also return false if segments is null or empty.
     * @throws IllegalArgumentException if flight is null.
     */
    public boolean process(final Flight flight, final Object... args) {

        if (flight == null) {
            throw new IllegalArgumentException("Flight cannot be null");
        }

        List<Segment> segments = flight.getSegments();
        if (segments == null || segments.isEmpty()) {
            return false;
        }

        final long groundLimit =
                (args != null && args.length > 0 && args[0] instanceof Long longValue) ? longValue
                        : DEFAULT_GROUND_LIMIT;

        long groundTime = 0;

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

        /**
         * Can use streams, can be parallelized:
         * 
         * AtomicLong totalTime = new AtomicLong(0); boolean exceeded = IntStream.range(0,
         * segments.size() - 1) .mapToObj(i -> Duration.between( segments.get(i).getArrivalDate(),
         * segments.get(i + 1).getDepartureDate()).toMinutes()) .takeWhile(time -> { long newTotal =
         * totalTime.addAndGet(time); return newTotal <= groundLimit; }) .count() < segments.size()
         * - 1;
         * 
         * if (exceeded) { logger.info("Ground time exceeded: {}", flight); } return exceeded;
         */

        return false;
    }
}
