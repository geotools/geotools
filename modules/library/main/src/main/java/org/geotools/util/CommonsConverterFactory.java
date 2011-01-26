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
package org.geotools.util;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.geotools.factory.Hints;

/**
 * Convert String to common scalar values.
 * <p>
 * Formally this class made use of the apache commons {@link org.apache.commons.beanutils.Converter}
 * interface.
 * 
 * @author Justin Deoliveira, The Open Planning Project
 * @since 2.4
 * @version 2.7
 * @source $URL$
 */
public class CommonsConverterFactory implements ConverterFactory {
	
    // some additional converters
    /**
     * converts a string to a uri.
     */
    static class URIConverter implements Converter {
        public <T> T convert(Object source, Class<T> target) throws Exception {
            if( source == null ) return null;
            String string = (String) source;
            try {
                    URI uri = new URI( string );
                    return target.cast( uri );
            } 
            catch (URISyntaxException e) { } 
            return null;
        }
    }
    static class NumberConverter implements Converter {
        public <T> T convert(Object source, Class<T> target) throws Exception {
            if( source == null ) return null;
            String string = (String) source;
            Number parsed = null;
            try { //first try integer
                parsed = (Number) new IntegerConverter().convert(string,Integer.class);
            }
            catch(Exception e) {}            
            if ( parsed == null ) { //try double
                parsed = (Number) new DoubleConverter().convert(string,Double.class);
            }            
            return target.cast(parsed);
        }
    }
    static class ByteConverter implements Converter {
        public <T> T convert(Object source, Class<T> target) throws Exception {
            if( source == null ) return null;
            String string = (String) source;
            try {
                Byte parsed = new Byte(string);
                return target.cast(parsed);
            } catch (Exception e) {
                return null;
            }
        }
    }
    static class ShortConverter implements Converter {
        public <T> T convert(Object source, Class<T> target) throws Exception {
            if( source == null ) return null;
            String string = (String) source;
            try {
                Short parsed = new Short(string);
                return target.cast(parsed);
            } catch (Exception e) {
                return null;
            }
        }
    }
    static class IntegerConverter implements Converter {
        public <T> T convert(Object source, Class<T> target) throws Exception {
            if( source == null ) return null;
            String string = (String) source;
            try {
                Integer parsed = new Integer(string);
                return target.cast(parsed);
            } catch (Exception e) {
                return null;
            }
        }
    }
    static class LongConverter implements Converter {
        public <T> T convert(Object source, Class<T> target) throws Exception {
            if( source == null ) return null;
            String string = (String) source;
            try {
                Long parsed = new Long(string);
                return target.cast(parsed);
            } catch (Exception e) {
                return null;
            }
        }
    }
    static class BigIntegerConverter implements Converter {
        public <T> T convert(Object source, Class<T> target) throws Exception {
            if( source == null ) return null;
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
        public <T> T convert(Object source, Class<T> target) throws Exception {
            if( source == null ) return null;
            String string = (String) source;
            try {
                Float parsed = new Float(string);
                return target.cast(parsed);
            } catch (Exception e) {
                return null;
            }
        }
    }
    static class DoubleConverter implements Converter {
        public <T> T convert(Object source, Class<T> target) throws Exception {
            if( source == null ) return null;
            String string = (String) source;
            try {
                Double parsed = new Double(string);
                return target.cast(parsed);
            } catch (Exception e) {
                return null;
            }
        }
    }
    static class BigDecimalConverter implements Converter {
        public <T> T convert(Object source, Class<T> target) throws Exception {
            if( source == null ) return null;
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
        
        public <T> T convert(Object source, Class<T> target) throws Exception {
            if( source == null ) return null;
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
            if (string.equalsIgnoreCase("yes") ||
                string.equalsIgnoreCase("y") ||
                string.equalsIgnoreCase("true") ||
                string.equalsIgnoreCase("on") ||
                string.equalsIgnoreCase("1")) {
                return target.cast(Boolean.TRUE);
            } else if (string.equalsIgnoreCase("no") ||
                       string.equalsIgnoreCase("n") ||
                       string.equalsIgnoreCase("false") ||
                       string.equalsIgnoreCase("off") ||
                       string.equalsIgnoreCase("0")) {
                return target.cast(Boolean.FALSE);
            } else {
                return null;
            }
        }
    }
    static class CharacterConverter implements Converter {
        public <T> T convert(Object source, Class<T> target) throws Exception {
            if( source == null ) return null;
            String string = (String) source;
            if( string.length() > 0 ){
                return target.cast( string.charAt(0) );
            }
            return null;
        }
    }
    
    static class DateConverter implements Converter {
        private static SimpleDateFormat format1 = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss.S a" );
        private static SimpleDateFormat format2 = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ssa" );
        
        public <T> T convert(Object source, Class<T> target) throws Exception {
            if( source == null ) return null;
            String string = (String) source;
            
            try {
                Date parsed = format1.parse(string);
                return target.cast( parsed );
            }
            catch( Exception ignore){
            }
            try {
                Date parsed = format2.parse(string);
                return target.cast( parsed );
            }
            catch( Exception ignore){
            }
            return null;
        }
    }
    
    static class URLConverter implements Converter {
        public <T> T convert(Object source, Class<T> target) throws Exception {
            if( source == null ) return null;
            String string = (String) source;
            try {
                URL parsed = new URL( string );
                return target.cast(parsed);
            } catch (Exception e) {
                return null;
            }
        }
    }
    
    static class SQLDateConverter implements Converter {
        public <T> T convert(Object source, Class<T> target) throws Exception {
            if( source == null ) return null;
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
        public <T> T convert(Object source, Class<T> target) throws Exception {
            if( source == null ) return null;
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
        public <T> T convert(Object source, Class<T> target) throws Exception {
            if( source == null ) return null;
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
        public <T> T convert(Object source, Class<T> target) throws Exception {
            if( source == null ) return null;
            String string = (String) source;
            try {
                File parsed = new File(string);
                return target.cast(parsed);
            } catch (Exception e) {
                return null;
            }
        }
    }
    /**
     * No need for FastHashMap - we are only registering during construction
     */
    private static HashMap<Class<?>, Converter> register = new HashMap<Class<?>, Converter>();
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
        register.put(BigDecimalConverter.class, new BigDecimalConverter());
        register.put(Boolean.class, new BooleanConverter());
        register.put(Boolean.TYPE, new BooleanConverter());
        register.put(Character.class, new CharacterConverter());
        register.put(Character.TYPE, new CharacterConverter());
        
        // the follow was required to pass tests
        // (they were not registered explicitly)
        // java.lang.Class</li>
        register.put( File.class, new FileConverter() );
        register.put( URL.class, new URLConverter() );
        register.put(java.sql.Date.class, new SQLDateConverter() );
        register.put(java.sql.Time.class, new SQLTimeConverter() );
        register.put(java.sql.Timestamp.class, new SQLTimestampConverter() );
        
        register.put(Date.class, new DateConverter() );

    }
	
	
	/**
	 * Delegates to {@link ConvertUtils#lookup(java.lang.Class)} to create a 
	 * converter instance.
	 * 
	 * @see ConverterFactory#createConverter(Class, Class, Hints). 
	 */
	public Converter createConverter(Class<?> source, Class<?> target, Hints hints) {
		if ( source == null || !source.equals( String.class ) ) {
		    return null; // only do strings
		}
		Converter converter = register.get(target);
		if( converter != null ){
		    return converter;
		}
		return null;
	}
}
