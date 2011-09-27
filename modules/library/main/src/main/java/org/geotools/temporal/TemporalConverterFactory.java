/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.geotools.temporal;

import java.util.Date;

import org.geotools.factory.Hints;
import org.geotools.temporal.object.DefaultInstant;
import org.geotools.temporal.object.DefaultPosition;
import org.geotools.util.Converter;
import org.geotools.util.ConverterFactory;
import org.geotools.util.Converters;
import org.opengis.temporal.Instant;
import org.opengis.temporal.TemporalObject;

/**
 * Factory that converts String and {@link java.util.Date} objects to instances of 
 * {@link TemporalObject}.
 * 
 * @author Justin Deoliveira, OpenGeo
 *
 *
 * @source $URL$
 */
public class TemporalConverterFactory implements ConverterFactory {

    static Converter dateToInstant = new Converter() {
        public <T> T convert(Object source, Class<T> target) throws Exception {
            return (T) new DefaultInstant(new DefaultPosition((Date)source));
        }
    };
    
    static Converter stringToInstant = new Converter() {

        public <T> T convert(Object source, Class<T> target) throws Exception {
            //first go to java.util.Date
            Date d = Converters.convert(source, Date.class);
            
            //then go from date to instant
            return d != null ? dateToInstant.convert(d, target) : null;
        }
        
    };
    
    public Converter createConverter(Class<?> source, Class<?> target, Hints hints) {
        if (Instant.class.isAssignableFrom(target)) {
            if (Date.class.isAssignableFrom(source)) {
                return dateToInstant;
            }
            
            if (String.class.equals(source)) {
                return stringToInstant;
            }
        }
        
        return null;
    }

    
    
}
