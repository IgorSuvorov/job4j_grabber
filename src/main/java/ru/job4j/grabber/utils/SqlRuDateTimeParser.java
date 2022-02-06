package ru.job4j.grabber.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static java.util.Map.entry;

public class SqlRuDateTimeParser implements DateTimeParser {
    private static final Map<String, String> YESTERDAY = Map.of("сегодня", LocalDateTime.now()
    .format(DateTimeFormatter.ofPattern("d-MM-yy")));

    private static final Map<String, String> TODAY = Map.of("вчера", LocalDateTime.now()
    .minusDays(1).format(DateTimeFormatter.ofPattern("d-MM-yy")));

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-MM-yy HH:mm");

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
        LocalDateTime rsl = null;
        String month = "сегодня";
        String[] dateTime  = parse.trim().split(", ");

        if (TODAY.equals(dateTime[0])) {
            return LocalDateTime.parse(TODAY.get("сегодня") + " " + dateTime[1], formatter);
        } else if (YESTERDAY.equals(dateTime[0])) {
            return LocalDateTime.parse(YESTERDAY.get("вчера") + " " + dateTime[1], formatter);
        }

        String[] dayAndYear = dateTime[0].split(" " + month + " ");
        rsl = LocalDateTime.parse(dayAndYear[0] + "-"
                        + MONTHS.get(month) + "-"
                        + dayAndYear[1] + " "
                        + dateTime[1],
                formatter);
        return rsl;
    }
}