package com.gridnine.testing.filter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.gridnine.testing.base.Flight;
import com.gridnine.testing.base.Segment;

/**
 * Filter by "flew out".
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
public class FilterFlewOutEarlier implements FlightFilter {

    private static final Logger logger =
            LoggerFactory.getLogger(FilterFlewOutEarlier.class);

    private final Supplier<LocalDateTime> currentDateTimeSupplier;

    /** Default constructor. */
    public FilterFlewOutEarlier() {
        this(LocalDateTime::now);
    }

    /** Constructor with custom current date time supplier. */
    public FilterFlewOutEarlier(Supplier<LocalDateTime> currentDateTimeSupplier) {
        this.currentDateTimeSupplier = Objects.requireNonNull(currentDateTimeSupplier);
    }

    /**
     * Method for finding an element with departure time in past. Uses streams. Can be parallelized.
     * 
     * @param flight Flight object.
     * @param args Arguments.
     * @return true if there is an element with departure time is in past, false otherwise. Also
     *         return false if segments is null or empty.
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

        final LocalDateTime now =
                (args != null && args.length > 0 && args[0] instanceof LocalDateTime dt) ? dt
                        : currentDateTimeSupplier.get();

        boolean hasInvalidSegment = segments.stream()
                .anyMatch(segment -> segment.getDepartureDate().isBefore(now));

        if (hasInvalidSegment) {
            logger.info("Flew out: {}", flight);
        }

        return hasInvalidSegment;
    }
}
