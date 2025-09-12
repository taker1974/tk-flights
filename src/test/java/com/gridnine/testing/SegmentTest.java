package com.gridnine.testing;

import org.junit.jupiter.api.Test;
import com.gridnine.testing.base.Segment;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import java.time.LocalDateTime;

class SegmentTest {

    @Test
    void process_WhenDatesIsNull_ShouldThrowException() {

        assertThatNullPointerException()
                .isThrownBy(() -> new Segment(null, LocalDateTime.now()))
                .withMessage("Departure date cannot be null");

        assertThatNullPointerException()
                .isThrownBy(() -> new Segment(LocalDateTime.now(), null))
                .withMessage("Arrival date cannot be null");
    }
}
