/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.util;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;
import org.geotools.util.Converter;
import org.geotools.util.ConverterFactory;
import org.geotools.util.factory.Hints;

/**
 * Convert String to common scalar values.
 *
 * <p>Formerly this class made use of the apache commons {@link org.apache.commons.beanutils.Converter} interface.
 *
 * @author Justin Deoliveira, The Open Planning Project
 * @since 2.4
 * @version 2.7
 */
public class CommonsConverterFactory implements ConverterFactory {

    // some additional converters
    /** converts a string to a uri. */
    static class URIConverter implements Converter {
        @Override
        public <T> T convert(Object source, Class<T> target) throws Exception {
            if (source == null) return null;
            String string = (String) source;
            try {
                URI uri = new URI(string);
                return target.cast(uri);
            } catch (URISyntaxException e) {
                // ignore
            }
            return null;
        }
    }

    static class NumberConverter implements Converter {
        // target.cast won't work for both the object wrapper and the primitive class
        @Override
        @SuppressWarnings("unchecked")
        public <T> T convert(Object source, Class<T> target) throws Exception {
            if (source == null) return null;
            String string = (String) source;
            Number parsed = null;
            try { // first try integer
                parsed = new IntegerConverter().convert(string, Integer.class);
            } catch (Exception e) {
                // ignore
            }
            if (parsed == null) { // try double
                parsed = new DoubleConverter().convert(string, Double.class);
            }
            return (T) parsed;
        }
    }

    static class ByteConverter implements Converter {
        // target.cast won't work for both the object wrapper and the primitive class
        @Override
        @SuppressWarnings("unchecked")
        public <T> T convert(Object source, Class<T> target) throws Exception {
            if (source == null) return null;
            String string = (String) source;
            try {
                Byte parsed = Byte.valueOf(string);
                return (T) parsed;
            } catch (Exception e) {
                return null;
            }
        }
    }

    static class ShortConverter implements Converter {
        // target.cast won't work for both the object wrapper and the primitive class
        @Override
        @SuppressWarnings("unchecked")
        public <T> T convert(Object source, Class<T> target) throws Exception {
            if (source == null) return null;
            String string = (String) source;
            try {
                Short parsed = Short.valueOf(string);
                return (T) parsed;
            } catch (Exception e) {
                return null;
            }
        }
    }

    static class IntegerConverter implements Converter {
        // target.cast won't work for both the object wrapper and the primitive class
        @Override
        @SuppressWarnings("unchecked")
        public <T> T convert(Object source, Class<T> target) throws Exception {
            if (source == null) return null;
            String string = (String) source;
            try {
                Integer parsed = Integer.valueOf(string);
                return (T) parsed;
            } catch (Exception e) {
                return null;
            }
        }
    }

    static class LongConverter implements Converter {
        // target.cast won't work for both the object wrapper and the primitive class
        @Override
        @SuppressWarnings("unchecked")
        public <T> T convert(Object source, Class<T> target) throws Exception {
            if (source == null) return null;
            String string = (String) source;
            try {
                Long parsed = Long.valueOf(string);
                return (T) parsed;
            } catch (Exception e) {
                return null;
            }
        }
    }

    static class BigIntegerConverter implements Converter {
        @Override
        public <T> T convert(Object source, Class<T> target) throws Exception {
            if (source == null) return null;
            String string = (String) source;
            try {
                BigInteger parsed = new BigInteger(string);
                return target.cast(parsed);
            } catch (Exception e) {
                return null;
            }
        }
    }

    static class FloatConverter implements Converter {
        // target.cast won't work for both the object wrapper and the primitive class
        @Override
        @SuppressWarnings("unchecked")
        public <T> T convert(Object source, Class<T> target) throws Exception {
            if (source == null) return null;
            String string = (String) source;
            try {
                Float parsed = Float.valueOf(string);
                return (T) parsed;
            } catch (Exception e) {
                return null;
            }
        }
    }

    static class DoubleConverter implements Converter {
        // target.cast won't work for both the object wrapper and the primitive class
        @Override
        @SuppressWarnings("unchecked")
        public <T> T convert(Object source, Class<T> target) throws Exception {
            if (source == null) return null;
            String string = (String) source;
            try {
                Double parsed = Double.valueOf(string);
                return (T) parsed;
            } catch (Exception e) {
                return null;
            }
        }
    }

    static class BigDecimalConverter implements Converter {
        @Override
        public <T> T convert(Object source, Class<T> target) throws Exception {
            if (source == null) return null;
            String string = (String) source;
            try {
                BigDecimal parsed = new BigDecimal(string);
                return target.cast(parsed);
            } catch (Exception e) {
                return null;
            }
        }
    }

    static class BooleanConverter implements Converter {
        //        static final Set<String> YES = new HashSet<String>(
        //                Arrays.asList(new String[]{"YES","Y","TRUE","ON","1"}) );
        //        static final Set<String> NO = new HashSet<String>(
        //                Arrays.asList(new String[]{"NO","N","FALSE","OFF","0"}) );

        // target.cast won't work for both the object wrapper and the primitive class
        @Override
        @SuppressWarnings("unchecked")
        public <T> T convert(Object source, Class<T> target) throws Exception {
            if (source == null) return null;
            String string = (String) source;
            //            string = string.toUpperCase();
            //            if( YES.contains(string) ){
            //                return target.cast( Boolean.TRUE );
            //            }
            //            else if( NO.contains(string)){
            //                return target.cast( Boolean.FALSE );
            //            }
            //            else {
            //                return null;
            //            }
            if (string.equalsIgnoreCase("yes")
                    || string.equalsIgnoreCase("y")
                    || string.equalsIgnoreCase("true")
                    || string.equalsIgnoreCase("on")
                    || string.equalsIgnoreCase("1")) {
                return (T) Boolean.TRUE;
            } else if (string.equalsIgnoreCase("no")
                    || string.equalsIgnoreCase("n")
                    || string.equalsIgnoreCase("false")
                    || string.equalsIgnoreCase("off")
                    || string.equalsIgnoreCase("0")) {
                return (T) Boolean.FALSE;
            } else {
                return null;
            }
        }
    }

    static class CharacterConverter implements Converter {
        @Override
        public <T> T convert(Object source, Class<T> target) throws Exception {
            if (source == null) return null;
            String string = (String) source;
            if (string.length() > 0) {
                return target.cast(string.charAt(0));
            }
            return null;
        }
    }

    static class DateConverter implements Converter {

        public static List<String> formats =
                Arrays.asList("yyyy-MM-dd HH:mm:ss.S a", "yyyy-MM-dd HH:mm:ssa", "yyyy-MM-dd'T'HH:mm:ss.SSSX");

        @Override
        public <T> T convert(Object source, Class<T> target) throws Exception {

            if (source == null) {
                return null;
            }

            String string = (String) source;

            for (String format : formats) {
                try {
                    return target.cast(new SimpleDateFormat(format).parse(string));
                } catch (Exception ignore) {
                    // ignore
                }
            }
            return null;
        }
    }

    static class URLConverter implements Converter {
        @Override
        public <T> T convert(Object source, Class<T> target) throws Exception {
            if (source == null) return null;
            String string = (String) source;
            try {
                URL parsed = new URL(string);
                return target.cast(parsed);
            } catch (Exception e) {
                // try to fetch a data protocol URL, or return null
                return tryGetDataURL(string, target);
            }
        }

        private <T> T tryGetDataURL(String string, Class<T> target) {
            // check if it's a data protocol URL
            if (string.startsWith(DataUrlConnection.DATA_PREFIX)) {
                try {
                    URL parsed = new URL(null, string, new DataUrlHandler());
                    return target.cast(parsed);
                } catch (Exception e2) {
                    return null;
                }
            } else {
                return null;
            }
        }
    }

    static class SQLDateConverter implements Converter {
        @Override
        public <T> T convert(Object source, Class<T> target) throws Exception {
            if (source == null) return null;
            String string = (String) source;
            try {
                java.sql.Date parsed = java.sql.Date.valueOf(string);
                return target.cast(parsed);
            } catch (Exception e) {
                return null;
            }
        }
    }

    static class SQLTimeConverter implements Converter {
        @Override
        public <T> T convert(Object source, Class<T> target) throws Exception {
            if (source == null) return null;
            String string = (String) source;
            try {
                java.sql.Time parsed = java.sql.Time.valueOf(string);
                return target.cast(parsed);
            } catch (Exception e) {
                return null;
            }
        }
    }

    static class SQLTimestampConverter implements Converter {
        @Override
        public <T> T convert(Object source, Class<T> target) throws Exception {
            if (source == null) return null;
            String string = (String) source;
            try {
                java.sql.Timestamp parsed = java.sql.Timestamp.valueOf(string);
                return target.cast(parsed);
            } catch (Exception e) {
                return null;
            }
        }
    }

    static class FileConverter implements Converter {
        @Override
        public <T> T convert(Object source, Class<T> target) throws Exception {
            if (source == null) return null;
            String string = (String) source;
            try {
                File parsed = new File(string);
                return target.cast(parsed);
            } catch (Exception e) {
                return null;
            }
        }
    }

    static class TimeZoneConverter implements Converter {
        @Override
        public <T> T convert(Object source, Class<T> target) throws Exception {
            if (source == null) return null;
            String string = (String) source;
            TimeZone timezone = TimeZone.getTimeZone(string);
            if (!string.equals(timezone.getID())) {
                // timezone will return UTC if nothing matches, so technically,
                // this is not a valid parse
                return null;
            }
            return target.cast(timezone);
        }
    }
    /** No need for FastHashMap - we are only registering during construction */
    private static HashMap<Class<?>, Converter> register = new HashMap<>();

    static {
        register.put(URI.class, new URIConverter());
        register.put(Number.class, new NumberConverter());

        // make sure numeric converters do not use default value
        register.put(Byte.class, new ByteConverter());
        register.put(Byte.TYPE, new ByteConverter());
        register.put(Short.class, new ShortConverter());
        register.put(Short.TYPE, new ShortConverter());
        register.put(Integer.class, new IntegerConverter());
        register.put(Integer.TYPE, new IntegerConverter());
        register.put(Long.class, new LongConverter());
        register.put(Long.TYPE, new LongConverter());
        register.put(BigInteger.class, new BigIntegerConverter());
        register.put(Float.class, new FloatConverter());
        register.put(Float.TYPE, new FloatConverter());
        register.put(Double.class, new DoubleConverter());
        register.put(Double.TYPE, new DoubleConverter());
        register.put(BigDecimal.class, new BigDecimalConverter());
        register.put(Boolean.class, new BooleanConverter());
        register.put(Boolean.TYPE, new BooleanConverter());
        register.put(Character.class, new CharacterConverter());
        register.put(Character.TYPE, new CharacterConverter());

        // the follow was required to pass tests
        // (they were not registered explicitly)
        // java.lang.Class</li>
        register.put(File.class, new FileConverter());
        register.put(URL.class, new URLConverter());
        register.put(java.sql.Date.class, new SQLDateConverter());
        register.put(java.sql.Time.class, new SQLTimeConverter());
        register.put(java.sql.Timestamp.class, new SQLTimestampConverter());
        register.put(TimeZone.class, new TimeZoneConverter());

        register.put(Date.class, new DateConverter());
    }

    /**
     * Delegates to {@link ConvertUtils#lookup(java.lang.Class)} to create a converter instance.
     *
     * @see ConverterFactory#createConverter(Class, Class, Hints).
     */
    @Override
    public Converter createConverter(Class<?> source, Class<?> target, Hints hints) {

        if (source == null || !source.equals(String.class)) {
            return null; // only do strings
        }
        Converter converter = register.get(target);
        if (converter != null) {
            return converter;
        }
        return null;
    }
}
