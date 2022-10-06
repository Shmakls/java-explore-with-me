package ru.andrianov.emw.business.helper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateTimeStringConverter {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static String toFormattedString(LocalDateTime localDateTime) {

        return localDateTime.format(FORMATTER);

    }

    public static LocalDateTime fromFormattedString(String localDateTime) {

        return LocalDateTime.parse(localDateTime, FORMATTER);

    }

}
