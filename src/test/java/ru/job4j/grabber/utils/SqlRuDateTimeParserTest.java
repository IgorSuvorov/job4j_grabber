package ru.job4j.grabber.utils;

import org.junit.*;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import ru.job4j.grabber.utils.SqlRuDateTimeParser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SqlRuDateTimeParserTest {
    @Test
    public void parseFullDate() {
        SqlRuDateTimeParser srdtp = new SqlRuDateTimeParser();
        String testDate =  "2 дек 19, 22:29";
        assertThat(srdtp.parse(testDate).toString(), is("2019-12-02T22:29"));
    }

//    @Test
//    public void parseToday() {
//        SqlRuDateTimeParser srdtp = new SqlRuDateTimeParser();
//        String testDate =  "сегодня, 02:30";
//        assertThat(srdtp.parse(testDate).toString(), is("2022-02-07T02:30"));
//    }
}
