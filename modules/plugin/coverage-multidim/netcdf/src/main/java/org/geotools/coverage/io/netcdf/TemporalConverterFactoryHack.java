/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2014, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.io.netcdf;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import javax.xml.datatype.XMLGregorianCalendar;
import org.geotools.util.Converter;
import org.geotools.util.ConverterFactory;
import org.geotools.util.factory.Hints;

/**
 * Converter factory which created converting between temporal types and {@link String}
 *
 * <p>Supported save conversions:
 *
 * <ul>
 *   <li>{@link java.util.Date} to {@link String}
 *   <li>{@link java.sql.Time} to {@link to {@link String}}
 *   <li>{@link java.util.Date} to {@link to {@link String}}
 *   <li>{@link java.util.Calendar} to {@link to {@link String}}
 *   <li>{@link XMLGregorianCalendar} to {@link to {@link String}}
 * </ul>
 *
 * <p>The hint {@link ConverterFactory#SAFE_CONVERSION} is used to control which conversions will be
 * applied.
 *
 * @author Simone Giannecchini, GeoSolutions
 * @since 9.0
 */
class TemporalConverterFactoryHack implements ConverterFactory {

    /**
     * Not thread safe, maybe switch to DateTimeFormatter, but also check
     * https://martin-grigorov.medium.com/compare-performance-of-javas-simpledateformat-against-datetimeformatter-31be58cadf1d
     */
    private static SimpleDateFormat getDateFormat() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("UTC")); // we DO work only with UTC times
        return df;
    }

    private static final Converter DATE_STRING =
            new Converter() {
                @Override
                @SuppressWarnings("unchecked")
                public <T> T convert(Object source, Class<T> target) throws Exception {
                    if (source instanceof Date) {
                        return (T) getDateFormat().format((Date) source);
                    }
                    return null;
                }
            };

    private static final Converter CALENDAR_STRING =
            new Converter() {
                @Override
                @SuppressWarnings("unchecked")
                public <T> T convert(Object source, Class<T> target) throws Exception {
                    if (source instanceof Calendar) {
                        return (T) getDateFormat().format(((Calendar) source).getTime());
                    }
                    return null;
                }
            };

    private static final Converter XML_CALENDAR_STRING =
            new Converter() {
                @Override
                @SuppressWarnings("unchecked")
                public <T> T convert(Object source, Class<T> target) throws Exception {
                    if (source instanceof XMLGregorianCalendar) {
                        GregorianCalendar date =
                                ((XMLGregorianCalendar) source)
                                        .toGregorianCalendar(
                                                TimeZone.getTimeZone("GMT"),
                                                Locale.getDefault(),
                                                null);
                        return (T) getDateFormat().format(date.getTime());
                    }
                    return null;
                }
            };

    @Override
    public Converter createConverter(Class source, Class target, Hints hints) {

        if (Date.class.isAssignableFrom(source) && String.class.equals(target)) return DATE_STRING;

        if (Calendar.class.isAssignableFrom(source) && String.class.equals(target))
            return CALENDAR_STRING;

        if (XMLGregorianCalendar.class.isAssignableFrom(source) && String.class.equals(target))
            return XML_CALENDAR_STRING;

        return null;
    }
}
