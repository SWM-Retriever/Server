package org.retriever.server.dailypet.global.utils;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class LocalDateTimeUtilsTest {

    @Test
    void calculateDaysFromNow() {
        LocalDate startDate = LocalDate.of(2020, 9, 2);

        long diff = LocalDateTimeUtils.calculateDaysFromNow(startDate);

        assertThat(diff).isGreaterThan(365 * 2L);
    }
}
