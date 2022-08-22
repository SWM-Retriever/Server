package org.retriever.server.dailypet.global.utils;

import java.time.LocalDate;
import java.time.Period;

public class LocalDateTimeUtils {

    public static int calculateDaysFromNow(LocalDate date) {
        LocalDate now = LocalDate.now();

        Period period = Period.between(date, now);

        return Math.abs(period.getDays());
    }
}
