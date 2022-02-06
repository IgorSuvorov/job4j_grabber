package ru.job4j.grabber.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static java.util.Map.entry;

public class SqlRuDateTimeParser implements DateTimeParser {

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
            entry("дек", "12"),
            entry("сегодня", LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("d-MM-yy"))),
            entry("вчера", LocalDateTime.now()
                    .minusDays(1).format(DateTimeFormatter.ofPattern("d-MM-yy"))));

    @Override
    public LocalDateTime parse(String parse) {
        return null;
    }
}