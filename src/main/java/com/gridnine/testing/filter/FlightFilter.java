package com.gridnine.testing.filter;

import com.gridnine.testing.base.Flight;

/**
 * Filter interface.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@FunctionalInterface
public interface FlightFilter {

    /**
     * Method for finding an element.
     * 
     * @param flight Flight object.
     * @param args Arguments.
     * @return true if an element found, false otherwise. Also return false if segments is null or
     *         empty.
     * @throws IllegalArgumentException if flight is null.
     */
    boolean process(final Flight flight, final Object... args);
}
