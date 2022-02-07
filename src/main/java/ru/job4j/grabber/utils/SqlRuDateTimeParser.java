package ru.job4j.grabber.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static java.util.Map.entry;

public class SqlRuDateTimeParser implements DateTimeParser {
    private static final String YESTERDAY = "вчера";

    private static final String TODAY = "сегодня";

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("d-MM-yy HH:mm");

    private static final Map<String, String> MONTHS = Map.ofEntries(
            entry("янв", "01"),
            entry("фев", "02"),
            entry("мар", "03"),
            entry("апр", "04"),
            entry("май", "05"),
            entry("июн", "06"),
            entry("июл", "07"),
            entry("авг", "08"),
            entry("сен", "09"),
            entry("окт", "10"),
            entry("ноя", "11"),
            entry("дек", "12"));

    @Override
    public LocalDateTime parse(String parse) {
        String[] dateTime  = parse.split(", ");

        if (TODAY.equals(dateTime[0])) {
            return LocalDateTime.parse(LocalDate.now() + " " + dateTime[1], FORMATTER);
        } else if (YESTERDAY.equals(dateTime[0])) {
            return LocalDateTime.parse(LocalDateTime.now()
                    .minusDays(1) + " " + dateTime[1], FORMATTER);
        }

        String[] dayMonthYear = dateTime[0].split(" ");
        return LocalDateTime.parse(dayMonthYear[0] + "-"
                        + MONTHS.get(dayMonthYear[1]) + "-"
                        + dayMonthYear[2] + " "
                        + dateTime[1],
                FORMATTER);
    }
}