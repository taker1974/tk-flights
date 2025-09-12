package com.gridnine.testing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import com.gridnine.testing.base.Flight;
import com.gridnine.testing.base.Segment;
import com.gridnine.testing.filter.FilterFlewOutEarlier;
import java.time.LocalDateTime;
import java.util.List;
import static org.assertj.core.api.Assertions.*;

class FilterFlewOutEarlierTest {
    private FilterFlewOutEarlier filter;
    private LocalDateTime fixedDateTime;

    @BeforeEach
    void setUp() {
        fixedDateTime = LocalDateTime.of(2025, 1, 1, 12, 0);
        filter = new FilterFlewOutEarlier(() -> fixedDateTime);
    }

    @Test
    void process_WhenFlightIsNull_ShouldThrowException() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> filter.process(null))
                .withMessage("Flight cannot be null");
    }

    @ParameterizedTest
    @EmptySource
    void process_WhenNoSegments_ShouldReturnFalse(List<Segment> segments) {
        Flight flight = new Flight(segments);
        assertThat(filter.process(flight)).isFalse();
    }

    @Test
    void process_WhenAllSegmentsInFuture_ShouldReturnFalse() {
        Segment futureSegment1 =
                new Segment(fixedDateTime.plusHours(1), fixedDateTime.plusHours(2));
        Segment futureSegment2 =
                new Segment(fixedDateTime.plusHours(3), fixedDateTime.plusHours(4));
        Flight flight = new Flight(List.of(futureSegment1, futureSegment2));

        assertThat(filter.process(flight)).isFalse();
    }

    @Test
    void process_WhenOneSegmentInPast_ShouldReturnTrue() {
        Segment pastSegment = new Segment(fixedDateTime.minusHours(1), fixedDateTime.plusHours(1));
        Segment futureSegment = new Segment(fixedDateTime.plusHours(2), fixedDateTime.plusHours(3));
        Flight flight = new Flight(List.of(pastSegment, futureSegment));

        assertThat(filter.process(flight)).isTrue();
    }

    @Test
    void process_WhenSegmentDepartureEqualsNow_ShouldReturnFalse() {
        Segment boundarySegment = new Segment(fixedDateTime, fixedDateTime.plusHours(1));
        Flight flight = new Flight(List.of(boundarySegment));

        assertThat(filter.process(flight)).isFalse();
    }

    @Test
    void process_WhenUsingCustomTimeViaArgs_ShouldOverrideSupplier() {
        Segment segment = new Segment(fixedDateTime.minusHours(1), fixedDateTime.plusHours(1));
        Flight flight = new Flight(List.of(segment));
        LocalDateTime customTime = fixedDateTime.minusHours(2);

        assertThat(filter.process(flight, customTime)).isFalse();
    }

    @Test
    void process_WhenArgsContainNonDateTime_ShouldUseSupplier() {
        Segment segment = new Segment(fixedDateTime.minusHours(1), fixedDateTime.plusHours(1));
        Flight flight = new Flight(List.of(segment));

        assertThat(filter.process(flight, "invalid")).isTrue();
    }

    @Test
    void process_WithMultipleArgs_ShouldUseFirstDateTime() {
        Segment segment = new Segment(fixedDateTime.minusHours(1), fixedDateTime.plusHours(1));
        Flight flight = new Flight(List.of(segment));
        LocalDateTime customTime = fixedDateTime.minusHours(2);

        assertThat(filter.process(flight, customTime, "extra")).isFalse();
    }
}
