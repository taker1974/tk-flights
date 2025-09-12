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
        // 3 часа на земле
        Segment segment1 = new Segment(baseDateTime, baseDateTime.plusHours(1));
        Segment segment2 = new Segment(baseDateTime.plusHours(4), baseDateTime.plusHours(5));
        Flight flight = new Flight(List.of(segment1, segment2));

        assertThat(filter.process(flight)).isTrue();
    }

    @Test
    void process_WhenMultipleSegmentsWithExceedingGroundTime_ShouldReturnTrue() {
        // 1 час на земле
        Segment segment1 = new Segment(baseDateTime, baseDateTime.plusHours(1));
        Segment segment2 = new Segment(baseDateTime.plusHours(2), baseDateTime.plusHours(3));

        // 2 часа на земле
        Segment segment3 = new Segment(baseDateTime.plusHours(5), baseDateTime.plusHours(6));
        Flight flight = new Flight(List.of(segment1, segment2, segment3));

        assertThat(filter.process(flight)).isTrue(); // Общее время на земле - 3 часа
    }

    @Test
    void process_WhenUsingCustomLimitViaArgs_ShouldOverrideDefault() {
        // 1 час на земле
        Segment segment1 = new Segment(baseDateTime, baseDateTime.plusHours(1));
        Segment segment2 = new Segment(baseDateTime.plusHours(2), baseDateTime.plusHours(3));
        Flight flight = new Flight(List.of(segment1, segment2));

        // Устанавливаем лимит в 30 минут (меньше 1 часа)
        assertThat(filter.process(flight, 30L)).isTrue();

        // Устанавливаем лимит в 90 минут (больше 1 часа)
        assertThat(filter.process(flight, 90L)).isFalse();
    }

    @Test
    void process_WhenArgsContainNonLong_ShouldUseDefaultLimit() {
        // 3 часа на земле
        Segment segment1 = new Segment(baseDateTime, baseDateTime.plusHours(1));
        Segment segment2 = new Segment(baseDateTime.plusHours(4), baseDateTime.plusHours(5));
        Flight flight = new Flight(List.of(segment1, segment2));

        // Передаем не-Long аргумент: должен использоваться лимит по умолчанию (2 часа)
        assertThat(filter.process(flight, "invalid")).isTrue();
    }

    @Test
    void process_WithMultipleArgs_ShouldUseFirstLong() {
        // 1 час на земле
        Segment segment1 = new Segment(baseDateTime, baseDateTime.plusHours(1));
        Segment segment2 = new Segment(baseDateTime.plusHours(2), baseDateTime.plusHours(3));
        Flight flight = new Flight(List.of(segment1, segment2));

        // Используем первый аргумент (Long), игнорируем остальные
        assertThat(filter.process(flight, 30L, "extra", 123)).isTrue();
    }

    @Test
    void process_WhenNegativeGroundTime_ShouldHandleCorrectly() {
        // Создаем сегменты с "обратным" временем (не должно происходить в реальности)
        Segment segment1 = new Segment(baseDateTime, baseDateTime.plusHours(1));
        Segment segment2 = new Segment(baseDateTime.minusHours(1), baseDateTime.plusHours(2));
        Flight flight = new Flight(List.of(segment1, segment2));

        // Duration.between обработает отрицательный интервал корректно
        assertThat(filter.process(flight)).isFalse();
    }

    @ParameterizedTest
    @ValueSource(longs = {0, -1, -100})
    void process_WithNonPositiveCustomLimit_ShouldHandleCorrectly(long limit) {
        // 1 час на земле
        Segment segment1 = new Segment(baseDateTime, baseDateTime.plusHours(1));
        Segment segment2 = new Segment(baseDateTime.plusHours(2), baseDateTime.plusHours(3));
        Flight flight = new Flight(List.of(segment1, segment2));

        // Любое положительное время на земле превысит неположительный лимит
        assertThat(filter.process(flight, limit)).isTrue();
    }
}
