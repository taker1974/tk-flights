package com.gridnine.testing;

import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.gridnine.testing.base.Flight;
import com.gridnine.testing.base.FlightBuilder;
import com.gridnine.testing.filter.FilterFlewOutEarlier;
import com.gridnine.testing.filter.FlightFilter;

public class Main {

    private static final Logger logger =
            LoggerFactory.getLogger(Main.class);

    private static void verboseFlights(final String title, final List<Flight> flights) {

        if (flights == null) {
            return;
        }

        logger.info("{}:", title);
        flights.stream().forEach(f -> logger.info(f.toString()));
        logger.info("");
    }

    public static void main(String[] args) {

        List<Flight> flights = FlightBuilder.createFlights();

        verboseFlights("Original flights", flights);

        var withoutFlewOut = filterFlights(flights, new FilterFlewOutEarlier());
        verboseFlights("Without flew out", withoutFlewOut);
    }

    private static List<Flight> filterFlights(
            final List<Flight> flights, final FlightFilter filter) {

        if (flights == null) {
            return Collections.emptyList();
        }

        return flights.stream()
                .filter(f -> !filter.process(f))
                .toList();
    }
}
