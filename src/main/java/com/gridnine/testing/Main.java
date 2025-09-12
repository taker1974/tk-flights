package com.gridnine.testing;

import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.gridnine.testing.base.Flight;
import com.gridnine.testing.base.FlightBuilder;
import com.gridnine.testing.filter.FilterArrivalBeforeDeparture;
import com.gridnine.testing.filter.FilterFlewOutEarlier;
import com.gridnine.testing.filter.FilterGroundTimeExceed;
import com.gridnine.testing.filter.FlightFilter;

/**
 * Main class.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
public class Main {

    private static final Logger logger =
            LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        List<Flight> flights = FlightBuilder.createFlights();

        verboseFlights("Original flights", flights);

        var withoutFlewOut = filterFlights(flights, new FilterFlewOutEarlier());
        verboseFlights("Without flew out", withoutFlewOut);

        var withoutWrongDates = filterFlights(withoutFlewOut, new FilterArrivalBeforeDeparture());
        verboseFlights("Without wrong dates", withoutWrongDates);

        var withoutGroundTimeExceeded =
                filterFlights(withoutWrongDates, new FilterGroundTimeExceed());
        verboseFlights("Without ground time exceeded", withoutGroundTimeExceeded);
    }

    private static List<Flight> filterFlights(
            final List<Flight> flights, final FlightFilter filter, final Object... args) {

        if (flights == null) {
            return Collections.emptyList();
        }

        return flights.stream()
                .filter(f -> !filter.process(f, args))
                .toList();
    }

    private static void verboseFlights(final String title, final List<Flight> flights) {

        if (flights == null) {
            return;
        }

        logger.info("{}:", title);
        flights.stream().forEach(f -> logger.info(f.toString()));
        logger.info("");
    }
}
