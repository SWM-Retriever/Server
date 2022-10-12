package org.retriever.server.dailypet.global.utils;

import java.time.Duration;
import java.time.LocalDate;

public class LocalDateTimeUtils {

    public static long calculateDaysFromNow(LocalDate birthDate) {
        LocalDate now = LocalDate.now();

        return Duration.between(birthDate.atStartOfDay(), now.atStartOfDay()).toDays();
    }
}
