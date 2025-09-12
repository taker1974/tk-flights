package com.gridnine.testing.base;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Class that represents a flight.
 */
public class Flight {
    private final List<Segment> segments;

    public Flight(final List<Segment> segs) {
        segments = Objects.requireNonNull(segs, "Segments cannot be null");
    }

    public List<Segment> getSegments() {
        return segments;
    }

    @Override
    public String toString() {
        return segments.stream().map(Object::toString)
                .collect(Collectors.joining(" -> "));
    }
}
