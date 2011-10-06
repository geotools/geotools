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
package org.geotools.xml;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.geotools.factory.Hints;
import org.geotools.util.CommonsConverterFactory;
import org.geotools.util.Converter;
import org.geotools.util.ConverterFactory;
import org.geotools.xml.impl.DatatypeConverterImpl;


/**
 * A ConverterFactory which can convert strings using {@link javax.xml.datatype.DatatypeFactory}.
 * <p>
 * Supported converstions:
 * <ul>
 *         <li>String to {@link java.util.Date}
 *         <li>String to {@link java.util.Calendar}
 * </ul>
 * </p>
 *
 * @author Justin Deoliveira, The Open Planning Project
 *
 *
 *
 *
 * @source $URL$
 */
public class XmlConverterFactory implements ConverterFactory {
    
    public Converter createConverter(Class source, Class target, Hints hints) {
        boolean canHandleTarget = String.class.equals(target)
                || java.util.Date.class.isAssignableFrom(target) || Calendar.class.equals(target);
        boolean canHandleSource = String.class.equals(source)
                || java.util.Date.class.isAssignableFrom(source) || Calendar.class.equals(source);

        if (canHandleSource && canHandleTarget) {
            return new XmlConverter();
        }

        return null;
    }

    static class XmlConverter implements Converter {
        public Object convert(Object source, Class target)
            throws Exception {
            if (String.class.equals(source)) {
                return convertFromString((String) source, target);
            }
            return convertToString(source);
        }
        
        private Object convertFromString(final String source, final Class<?> target) {

            // don't bother performing conversions if the target types are not dates/times
            if(!Calendar.class.equals(target) && !Date.class.isAssignableFrom(target))
                return null;

            //JD: this is a bit of a hack but delegate to the 
            // commons converter in case we are executing first.
            try {
                Converter converter = new CommonsConverterFactory().createConverter(String.class,
                        target, null);
    
                if (converter != null) {
                    Object converted = null;
    
                    try {
                        converted = converter.convert(source, target);
                    } catch (Exception e) {
                        //ignore
                    }
    
                    if (converted != null) {
                        return converted;
                    }
                }
            }
            catch(Exception e) {
                //fall through to jaxb parsing
            }
            
            Calendar date;

            //try parsing as dateTime
            try {
                try {
                    date = DatatypeConverterImpl.getInstance().parseDateTime(source);
                }
                catch(Exception e) {
                    //try as just date
                    date = DatatypeConverterImpl.getInstance().parseDate(source);    
                }
                
            } catch (Exception e) {
                //try as just time
                date = DatatypeConverterImpl.getInstance().parseTime(source);
            }

            if (Calendar.class.equals(target)) {
                return date;
            }

            if (Date.class.isAssignableFrom(target)) {
                Date time = date.getTime();

                //check for subclasses
                if (java.sql.Date.class.equals(target)) {
                    return new java.sql.Date(time.getTime());
                }

                if (Time.class.equals(target)) {
                    return new Time(time.getTime());
                }

                if (Timestamp.class.equals(target)) {
                    return new Timestamp(time.getTime());
                }

                return time;
            }

            return null;
        }

        private String convertToString(Object unconvertedValue) {
            String textValue = null;

            if (unconvertedValue instanceof Calendar) {

                Calendar cal = (Calendar) unconvertedValue;
                textValue = DatatypeConverterImpl.getInstance().printDateTime(cal);

            } else if (unconvertedValue instanceof java.util.Date) {

                Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
                cal.setTimeInMillis(((java.util.Date) unconvertedValue).getTime());
                DatatypeConverterImpl converter = DatatypeConverterImpl.getInstance();

                if (unconvertedValue instanceof java.sql.Date) {
                    textValue = converter.printDate(cal);
                } else if (unconvertedValue instanceof java.sql.Time) {
                    textValue = converter.printTime(cal);
                } else {
                    // java.util.Date and java.sql.TimeStamp
                    textValue = converter.printDateTime(cal);
                }
            }

            return textValue;
        }
    }

}
