/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2021, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
/*
 * Copyright 2019-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.geotools.data.elasticsearch.date;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalQuery;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.util.Assert;

/**
 * Provides Converter instances to convert to and from Dates in the different date and time formats
 * that elasticsearch understands.
 *
 * @author Peter-Josef Meisch
 * @since 4.0
 */
public final class ElasticsearchDateConverter {

    private static final ConcurrentHashMap<String, ElasticsearchDateConverter> patternConverters =
            new ConcurrentHashMap<>();

    private static final ConcurrentHashMap<DateFormat, ElasticsearchDateConverter>
            dateFormatConverters = new ConcurrentHashMap<>();

    private final DateFormatter dateFormatter;

    public static ElasticsearchDateConverter forFormat(String format) {
        try {
            // try with a well-known format
            DateFormat dateFormat = DateFormat.valueOf(format);
            return of(dateFormat);
        } catch (IllegalArgumentException e) {
            // assume it's a pattern
            return of(format);
        }
    }

    /**
     * Creates an ElasticsearchDateConverter for the given {@link DateFormat}.
     *
     * @param dateFormat must not be @{literal null}
     * @return converter
     */
    public static ElasticsearchDateConverter of(DateFormat dateFormat) {

        Assert.notNull(dateFormat, "dateFormat must not be null");

        return dateFormatConverters.computeIfAbsent(
                dateFormat, d -> new ElasticsearchDateConverter(forDateFormat(d)));
    }

    /**
     * Creates an ElasticsearchDateConverter for the given pattern.
     *
     * @param pattern must not be {@literal null}
     * @return converter
     */
    public static ElasticsearchDateConverter of(String pattern) {
        Assert.notNull(pattern, "pattern must not be null");
        Assert.hasText(pattern, "pattern must not be empty");

        String[] subPatterns = pattern.split("\\|\\|");

        return patternConverters.computeIfAbsent(
                subPatterns[0].trim(), p -> new ElasticsearchDateConverter(forPattern(p)));
    }

    private ElasticsearchDateConverter(DateFormatter dateFormatter) {
        this.dateFormatter = dateFormatter;
    }

    /**
     * Formats the given {@link TemporalAccessor} into a String.
     *
     * @param accessor must not be {@literal null}
     * @return the formatted object
     */
    public String format(TemporalAccessor accessor) {

        Assert.notNull("accessor", "accessor must not be null");

        if (accessor instanceof Instant) {
            Instant instant = (Instant) accessor;
            ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(instant, ZoneId.of("UTC"));
            return dateFormatter.format(zonedDateTime);
        }

        return dateFormatter.format(accessor);
    }

    /**
     * Formats the given {@link TemporalAccessor} int a String
     *
     * @param date must not be {@literal null}
     * @return the formatted object
     */
    public String format(Date date) {

        Assert.notNull(date, "accessor must not be null");

        return dateFormatter.format(Instant.ofEpochMilli(date.getTime()));
    }

    /**
     * Parses a String into a TemporalAccessor.
     *
     * @param input the String to parse, must not be {@literal null}.
     * @param type the class to return
     * @param <T> the class of type
     * @return the new created object
     */
    public <T extends TemporalAccessor> T parse(String input, Class<T> type) {
        return dateFormatter.parse(input, type);
    }

    /**
     * Parses a String into a Date.
     *
     * @param input the String to parse, must not be {@literal null}.
     * @return the new created object
     */
    public Date parse(String input) {
        return new Date(dateFormatter.parse(input, Instant.class).toEpochMilli());
    }

    private static DateFormatter forDateFormat(DateFormat format) {
        if (DateFormat.epoch_millis.equals(format)) {
            return new EpochMillisDateFormatter();
        }

        if (DateFormat.epoch_second.equals(format)) {
            return new EpochSecondDateFormatter();
        }

        if (DateFormat.date_optional_time.equals(format)) {
            DateTimeFormatter formatter =
                    new DateTimeFormatterBuilder()
                            .appendPattern("uuuu-MM-dd'T'HH:mm:ss")
                            .optionalStart()
                            .appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, true)
                            .optionalEnd()
                            .appendPattern("[X]")
                            .toFormatter();
            DateTimeFormatter fullParser =
                    new DateTimeFormatterBuilder()
                            .appendPattern("uuuu-MM-dd'T'HH:mm:ss")
                            .optionalStart()
                            .appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, true)
                            .optionalEnd()
                            .appendPattern("[VV][X]")
                            .toFormatter();
            DateTimeFormatter simplifiedParser =
                    DateTimeFormatter.ofPattern("[uuuu-M[-d['T'H[:m[:s]]]]]");
            DateTimeFormatter parser =
                    new DateTimeFormatterBuilder()
                            .appendOptional(fullParser)
                            .appendOptional(simplifiedParser)
                            .toFormatter();
            return new PatternDateFormatter(formatter, parser);
        }

        String parsePattern = format.getParsePattern();
        String formatPattern = format.getFormatPattern();
        DateTimeFormatter parser = DateTimeFormatter.ofPattern(parsePattern);
        if (formatPattern.equals(parsePattern)) return new PatternDateFormatter(parser, parser);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatPattern);
        return new PatternDateFormatter(formatter, parser);
    }

    /**
     * Creates a {@link DateFormatter} for a given pattern. The pattern can be the name of a {@link
     * DateFormat} enum value or a literal pattern.
     *
     * @param pattern the pattern to use
     * @return DateFormatter
     */
    private static DateFormatter forPattern(String pattern) {

        try {
            DateFormat df = DateFormat.valueOf(pattern);
            return forDateFormat(df);
        } catch (IllegalArgumentException e) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
            return new PatternDateFormatter(dateTimeFormatter, dateTimeFormatter);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T extends TemporalAccessor> TemporalQuery<T> getTemporalQuery(Class<T> type) {
        return temporal -> {
            // no reflection for java.time classes (GraalVM native)
            if (type == java.time.chrono.HijrahDate.class) {
                return (T) java.time.chrono.HijrahDate.from(temporal);
            }
            if (type == java.time.chrono.JapaneseDate.class) {
                return (T) java.time.chrono.JapaneseDate.from(temporal);
            }
            if (type == java.time.ZonedDateTime.class) {
                return (T) java.time.ZonedDateTime.from(temporal);
            }
            if (type == java.time.LocalDateTime.class) {
                return (T) java.time.LocalDateTime.from(temporal);
            }
            if (type == java.time.chrono.ThaiBuddhistDate.class) {
                return (T) java.time.chrono.ThaiBuddhistDate.from(temporal);
            }
            if (type == java.time.LocalTime.class) {
                return (T) java.time.LocalTime.from(temporal);
            }
            if (type == java.time.ZoneOffset.class) {
                return (T) java.time.ZoneOffset.from(temporal);
            }
            if (type == java.time.OffsetTime.class) {
                return (T) java.time.OffsetTime.from(temporal);
            }
            if (type == java.time.chrono.ChronoLocalDate.class) {
                return (T) java.time.chrono.ChronoLocalDate.from(temporal);
            }
            if (type == java.time.Month.class) {
                return (T) java.time.Month.from(temporal);
            }
            if (type == java.time.chrono.ChronoLocalDateTime.class) {
                return (T) java.time.chrono.ChronoLocalDateTime.from(temporal);
            }
            if (type == java.time.MonthDay.class) {
                return (T) java.time.MonthDay.from(temporal);
            }
            if (type == java.time.Instant.class) {
                return (T) java.time.Instant.from(temporal);
            }
            if (type == java.time.OffsetDateTime.class) {
                return (T) java.time.OffsetDateTime.from(temporal);
            }
            if (type == java.time.chrono.ChronoZonedDateTime.class) {
                return (T) java.time.chrono.ChronoZonedDateTime.from(temporal);
            }
            if (type == java.time.chrono.MinguoDate.class) {
                return (T) java.time.chrono.MinguoDate.from(temporal);
            }
            if (type == java.time.Year.class) {
                return (T) java.time.Year.from(temporal);
            }
            if (type == java.time.DayOfWeek.class) {
                return (T) java.time.DayOfWeek.from(temporal);
            }
            if (type == java.time.LocalDate.class) {
                return (T) java.time.LocalDate.from(temporal);
            }
            if (type == java.time.YearMonth.class) {
                return (T) java.time.YearMonth.from(temporal);
            }

            // for implementations not covered until here use reflection to check for the existence
            // of a static
            // from(TemporalAccessor) method
            try {
                Method method = type.getMethod("from", TemporalAccessor.class);
                Object o = method.invoke(null, temporal);
                return type.cast(o);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(
                        "no 'from' factory method found in class " + type.getName());
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException("could not create object of class " + type.getName(), e);
            }
        };
    }
    // endregion

    /** a DateFormatter to convert epoch milliseconds */
    static class EpochMillisDateFormatter implements DateFormatter {

        @Override
        public String format(TemporalAccessor accessor) {

            Assert.notNull(accessor, "accessor must not be null");

            return Long.toString(Instant.from(accessor).toEpochMilli());
        }

        @Override
        public <T extends TemporalAccessor> T parse(String input, Class<T> type) {

            Assert.notNull(input, "input must not be null");
            Assert.notNull(type, "type must not be null");

            Instant instant = Instant.ofEpochMilli(Long.parseLong(input));
            TemporalQuery<T> query = getTemporalQuery(type);
            return query.queryFrom(instant);
        }
    }

    /**
     * a DateFormatter to convert epoch seconds. Elasticsearch's formatter uses double values, so do
     * we
     */
    static class EpochSecondDateFormatter implements DateFormatter {

        @Override
        public String format(TemporalAccessor accessor) {

            Assert.notNull(accessor, "accessor must not be null");

            long epochMilli = Instant.from(accessor).toEpochMilli();
            long fraction = epochMilli % 1_000;
            if (fraction == 0) {
                return Long.toString(epochMilli / 1_000);
            } else {
                Double d = ((double) epochMilli) / 1_000;
                return String.format(Locale.ROOT, "%.03f", d);
            }
        }

        @Override
        public <T extends TemporalAccessor> T parse(String input, Class<T> type) {

            Assert.notNull(input, "input must not be null");
            Assert.notNull(type, "type must not be null");

            Double epochMilli = Double.parseDouble(input) * 1_000;
            Instant instant = Instant.ofEpochMilli(epochMilli.longValue());
            TemporalQuery<T> query = getTemporalQuery(type);
            return query.queryFrom(instant);
        }
    }

    static class PatternDateFormatter implements DateFormatter {

        private final DateTimeFormatter formatter;
        private final DateTimeFormatter parser;

        PatternDateFormatter(DateTimeFormatter formatter, DateTimeFormatter parser) {

            this.formatter = formatter;
            this.parser = parser;
        }

        @Override
        public String format(TemporalAccessor accessor) {

            Assert.notNull(accessor, "accessor must not be null");

            try {
                return formatter.format(accessor);
            } catch (Exception e) {
                if (accessor instanceof Instant) {
                    // as alternatives try to format a ZonedDateTime or LocalDateTime
                    return formatter.format(
                            ZonedDateTime.ofInstant((Instant) accessor, ZoneId.of("UTC")));
                } else {
                    throw e;
                }
            }
        }

        @Override
        @SuppressWarnings("unchecked")
        public <T extends TemporalAccessor> T parse(String input, Class<T> type) {

            Assert.notNull(input, "input must not be null");
            Assert.notNull(type, "type must not be null");

            try {
                return parser.parse(input, getTemporalQuery(type));
            } catch (Exception exception) {
                // if it's a generic instant, try to figure out what was the target
                // based on the components that got parsed
                if (type.equals(Instant.class)) {
                    TemporalAccessor accessor = parser.parse(input);
                    if (accessor.isSupported(ChronoField.OFFSET_SECONDS)
                            && accessor.isSupported(ChronoField.YEAR)
                            && accessor.isSupported(ChronoField.MONTH_OF_YEAR)) {
                        ZonedDateTime zonedDateTime =
                                accessor.query(getTemporalQuery(ZonedDateTime.class));
                        return (T) zonedDateTime.toInstant();
                    } else if (accessor.isSupported(ChronoField.YEAR)
                            && accessor.isSupported(ChronoField.MONTH_OF_YEAR)) {
                        if (accessor.isSupported(ChronoField.HOUR_OF_DAY)) {
                            LocalDateTime localDateTime =
                                    accessor.query(getTemporalQuery(LocalDateTime.class));
                            return (T) localDateTime.toInstant(ZoneOffset.UTC);
                        } else {
                            YearMonth ym = accessor.query(getTemporalQuery(YearMonth.class));
                            return (T) ym.atDay(1).atStartOfDay().toInstant(ZoneOffset.UTC);
                        }
                    } else if (accessor.isSupported(ChronoField.YEAR)) {
                        Year y = accessor.query(getTemporalQuery(Year.class));
                        return (T) y.atDay(1).atStartOfDay().toInstant(ZoneOffset.UTC);
                    } else if (accessor.isSupported(ChronoField.HOUR_OF_DAY)) {
                        LocalTime lt = accessor.query(getTemporalQuery(LocalTime.class));
                        return (T) lt.atDate(LocalDate.ofEpochDay(0)).toInstant(ZoneOffset.UTC);
                    }
                }
                throw exception;
            }
        }
    }
}
