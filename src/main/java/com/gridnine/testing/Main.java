package com.gridnine.testing;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.gridnine.testing.base.Flight;
import com.gridnine.testing.base.FlightBuilder;

public class Main {

    private static final Logger logger =
            LoggerFactory.getLogger(com.gridnine.testing.Main.class);

    public static void main(String[] args) {

        List<Flight> flights = FlightBuilder.createFlights();
        flights.stream().forEach(f -> logger.trace(f.toString()));
    }

}
