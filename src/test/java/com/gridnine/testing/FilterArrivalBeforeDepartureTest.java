package com.gridnine.testing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import com.gridnine.testing.base.Flight;
import com.gridnine.testing.base.Segment;
import com.gridnine.testing.filter.FilterArrivalBeforeDeparture;
import java.time.LocalDateTime;
import java.util.List;
import static org.assertj.core.api.Assertions.*;

class FilterArrivalBeforeDepartureTest {
    private FilterArrivalBeforeDeparture filter;
    private LocalDateTime baseDateTime;

    @BeforeEach
    void setUp() {
        filter = new FilterArrivalBeforeDeparture();
        baseDateTime = LocalDateTime.of(2025, 1, 1, 12, 0);
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
    void process_WhenAllSegmentsValid_ShouldReturnFalse() {
        Segment validSegment1 = new Segment(baseDateTime, baseDateTime.plusHours(2));
        Segment validSegment2 = new Segment(baseDateTime.plusHours(3), baseDateTime.plusHours(5));
        Flight flight = new Flight(List.of(validSegment1, validSegment2));

        assertThat(filter.process(flight)).isFalse();
    }

    @Test
    void process_WhenOneSegmentHasArrivalBeforeDeparture_ShouldReturnTrue() {
        Segment invalidSegment = new Segment(baseDateTime, baseDateTime.minusHours(1));
        Segment validSegment = new Segment(baseDateTime.plusHours(2), baseDateTime.plusHours(4));
        Flight flight = new Flight(List.of(invalidSegment, validSegment));

        assertThat(filter.process(flight)).isTrue();
    }

    @Test
    void process_WhenArrivalEqualsDeparture_ShouldReturnFalse() {
        Segment boundarySegment = new Segment(baseDateTime, baseDateTime);
        Flight flight = new Flight(List.of(boundarySegment));

        assertThat(filter.process(flight)).isFalse();
    }

    @Test
    void process_WhenMultipleInvalidSegments_ShouldReturnTrue() {
        Segment invalidSegment1 = new Segment(baseDateTime, baseDateTime.minusHours(1));
        Segment invalidSegment2 = new Segment(baseDateTime.plusHours(2), baseDateTime.plusHours(1));
        Flight flight = new Flight(List.of(invalidSegment1, invalidSegment2));

        assertThat(filter.process(flight)).isTrue();
    }

    @Test
    void process_WhenFirstSegmentInvalid_ShouldReturnTrue() {
        Segment invalidSegment = new Segment(baseDateTime, baseDateTime.minusHours(1));
        Segment validSegment = new Segment(baseDateTime.plusHours(1), baseDateTime.plusHours(3));
        Flight flight = new Flight(List.of(invalidSegment, validSegment));

        assertThat(filter.process(flight)).isTrue();
    }

    @Test
    void process_WhenLastSegmentInvalid_ShouldReturnTrue() {
        Segment validSegment = new Segment(baseDateTime, baseDateTime.plusHours(2));
        Segment invalidSegment = new Segment(baseDateTime.plusHours(3), baseDateTime.plusHours(2));
        Flight flight = new Flight(List.of(validSegment, invalidSegment));

        assertThat(filter.process(flight)).isTrue();
    }

    @Test
    void process_WithSingleInvalidSegment_ShouldReturnTrue() {
        Segment invalidSegment = new Segment(baseDateTime, baseDateTime.minusHours(1));
        Flight flight = new Flight(List.of(invalidSegment));

        assertThat(filter.process(flight)).isTrue();
    }

    @Test
    void process_WithSingleValidSegment_ShouldReturnFalse() {
        Segment validSegment = new Segment(baseDateTime, baseDateTime.plusHours(2));
        Flight flight = new Flight(List.of(validSegment));

        assertThat(filter.process(flight)).isFalse();
    }

    @Test
    void process_WithArgs_ShouldNotAffectBehavior() {
        Segment invalidSegment = new Segment(baseDateTime, baseDateTime.minusHours(1));
        Flight flight = new Flight(List.of(invalidSegment));

        assertThat(filter.process(flight, "someArg", 123)).isTrue();
    }
}
