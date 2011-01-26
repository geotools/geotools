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

import org.apache.commons.beanutils.converters.SqlDateConverter;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
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
 * @source $URL$
 */
public class XmlConverterFactory implements ConverterFactory {
    
    public Converter createConverter(Class source, Class target, Hints hints) {
        if (String.class.equals(source)) {
            return new XmlConverter();
        }

        return null;
    }

    static class XmlConverter implements Converter {
        public Object convert(Object source, Class target)
            throws Exception {
            String value = (String) source;

            // don't bother performing conversions if the target types are not dates/times
            if(!Calendar.class.equals(target) && !Date.class.isAssignableFrom(target))
                return null;

            //JD: this is a bit of a hack but delegate to the 
            // commons converter in case we are executing first.
            try {
                Converter converter = new CommonsConverterFactory().createConverter(value.getClass(),
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
                    date = DatatypeConverterImpl.getInstance().parseDateTime(value);
                }
                catch(Exception e) {
                    //try as just date
                    date = DatatypeConverterImpl.getInstance().parseDate(value);    
                }
                
            } catch (Exception e) {
                //try as just time
                date = DatatypeConverterImpl.getInstance().parseTime(value);
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
    }
}
