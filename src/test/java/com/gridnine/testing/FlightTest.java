package com.gridnine.testing;

import org.junit.jupiter.api.Test;
import com.gridnine.testing.base.Flight;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class FlightTest {

    @Test
    void process_WhenSegmentsIsNull_ShouldThrowException() {
        assertThatNullPointerException()
                .isThrownBy(() -> new Flight(null))
                .withMessage("Segments cannot be null");
    }
}
