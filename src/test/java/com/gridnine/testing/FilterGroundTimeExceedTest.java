package com.gridnine.testing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import com.gridnine.testing.base.Flight;
import com.gridnine.testing.base.Segment;
import com.gridnine.testing.filter.FilterGroundTimeExceed;
import java.time.LocalDateTime;
import java.util.List;
import static org.assertj.core.api.Assertions.*;

class FilterGroundTimeExceedTest {
    private FilterGroundTimeExceed filter;
    private LocalDateTime baseDateTime;

    @BeforeEach
    void setUp() {
        filter = new FilterGroundTimeExceed();
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
    void process_WhenSingleSegment_ShouldReturnFalse() {
        Segment segment = new Segment(baseDateTime, baseDateTime.plusHours(2));
        Flight flight = new Flight(List.of(segment));

        assertThat(filter.process(flight)).isFalse();
    }

    @Test
    void process_WhenGroundTimeWithinLimit_ShouldReturnFalse() {
        Segment segment1 = new Segment(baseDateTime, baseDateTime.plusHours(1));
        Segment segment2 =
                new Segment(baseDateTime.plusHours(1).plusMinutes(30), baseDateTime.plusHours(3));
        Flight flight = new Flight(List.of(segment1, segment2));

        assertThat(filter.process(flight)).isFalse();
    }

    @Test
    void process_WhenGroundTimeExactlyAtLimit_ShouldReturnFalse() {
        Segment segment1 = new Segment(baseDateTime, baseDateTime.plusHours(1));
        Segment segment2 = new Segment(baseDateTime.plusHours(3), baseDateTime.plusHours(4));
        Flight flight = new Flight(List.of(segment1, segment2));

        assertThat(filter.process(flight)).isFalse();
    }

    @Test
    void process_WhenGroundTimeExceedsLimit_ShouldReturnTrue() {
        // 3 hours of ground time
        Segment segment1 = new Segment(baseDateTime, baseDateTime.plusHours(1));
        Segment segment2 = new Segment(baseDateTime.plusHours(4), baseDateTime.plusHours(5));
        Flight flight = new Flight(List.of(segment1, segment2));

        assertThat(filter.process(flight)).isTrue();
    }

    @Test
    void process_WhenMultipleSegmentsWithExceedingGroundTime_ShouldReturnTrue() {
        // 1 hour of ground time
        Segment segment1 = new Segment(baseDateTime, baseDateTime.plusHours(1));
        Segment segment2 = new Segment(baseDateTime.plusHours(2), baseDateTime.plusHours(3));

        // 2 hours of ground time
        Segment segment3 = new Segment(baseDateTime.plusHours(5), baseDateTime.plusHours(6));
        Flight flight = new Flight(List.of(segment1, segment2, segment3));

        assertThat(filter.process(flight)).isTrue(); // 3 hours of ground time
    }

    @Test
    void process_WhenUsingCustomLimitViaArgs_ShouldOverrideDefault() {
        // 1 hour of ground time
        Segment segment1 = new Segment(baseDateTime, baseDateTime.plusHours(1));
        Segment segment2 = new Segment(baseDateTime.plusHours(2), baseDateTime.plusHours(3));
        Flight flight = new Flight(List.of(segment1, segment2));

        // Set limit of 30 minutes (less than 1 hour)
        assertThat(filter.process(flight, 30L)).isTrue();

        // Set limit of 90 minutes (greater than 1 hour)
        assertThat(filter.process(flight, 90L)).isFalse();
    }

    @Test
    void process_WhenArgsContainNonLong_ShouldUseDefaultLimit() {
        // 3 hours of ground time
        Segment segment1 = new Segment(baseDateTime, baseDateTime.plusHours(1));
        Segment segment2 = new Segment(baseDateTime.plusHours(4), baseDateTime.plusHours(5));
        Flight flight = new Flight(List.of(segment1, segment2));

        // Pass not-Long argument: should use default limit of 120 minutes
        assertThat(filter.process(flight, "invalid")).isTrue();
    }

    @Test
    void process_WithMultipleArgs_ShouldUseFirstLong() {
        // 1 hour of ground time
        Segment segment1 = new Segment(baseDateTime, baseDateTime.plusHours(1));
        Segment segment2 = new Segment(baseDateTime.plusHours(2), baseDateTime.plusHours(3));
        Flight flight = new Flight(List.of(segment1, segment2));

        // Use first argument (Long), ignore others
        assertThat(filter.process(flight, 30L, "extra", 123)).isTrue();
    }

    @Test
    void process_WhenNegativeGroundTime_ShouldHandleCorrectly() {
        // Create a segment with negative ground time
        Segment segment1 = new Segment(baseDateTime, baseDateTime.plusHours(1));
        Segment segment2 = new Segment(baseDateTime.minusHours(1), baseDateTime.plusHours(2));
        Flight flight = new Flight(List.of(segment1, segment2));

        // Duration.between will handle negative intervals correctly
        assertThat(filter.process(flight)).isFalse();
    }

    @ParameterizedTest
    @ValueSource(longs = {0, -1, -100})
    void process_WithNonPositiveCustomLimit_ShouldHandleCorrectly(long limit) {
        // 1 hour of ground time
        Segment segment1 = new Segment(baseDateTime, baseDateTime.plusHours(1));
        Segment segment2 = new Segment(baseDateTime.plusHours(2), baseDateTime.plusHours(3));
        Flight flight = new Flight(List.of(segment1, segment2));

        // Any positive limit will exceed non-positive ground time
        assertThat(filter.process(flight, limit)).isTrue();
    }
}
