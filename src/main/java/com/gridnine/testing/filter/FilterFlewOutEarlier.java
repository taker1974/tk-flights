package com.gridnine.testing.filter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.gridnine.testing.base.Flight;
import com.gridnine.testing.base.Segment;

public class FilterFlewOutEarlier implements FlightFilter {

    private static final Logger logger =
            LoggerFactory.getLogger(FilterFlewOutEarlier.class);

    private final Supplier<LocalDateTime> currentDateTimeSupplier;

    public FilterFlewOutEarlier() {
        this(LocalDateTime::now);
    }

    public FilterFlewOutEarlier(Supplier<LocalDateTime> currentDateTimeSupplier) {
        this.currentDateTimeSupplier = currentDateTimeSupplier;
    }

    public boolean process(final Flight flight, final Object... args) {

        LocalDateTime now = currentDateTimeSupplier.get();
        if (args != null && args.length > 0 && args[0] instanceof LocalDateTime dt) {
            now = dt;
        }

        if (flight == null) {
            throw new IllegalArgumentException("Flight cannot be null");
        }

        List<Segment> segments = flight.getSegments();
        if (segments == null || segments.isEmpty()) {
            return false;
        }

        for (final Segment segment : segments) {
            if (segment.getDepartureDate().isBefore(now)) {
                logger.info("Flew out: {}", flight);
                return true;
            }
        }

        return false;
    }
}
