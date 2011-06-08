/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.geotools.filter.temporal;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.temporal.object.DefaultInstant;
import org.geotools.temporal.object.DefaultPeriod;
import org.geotools.temporal.object.DefaultPosition;
import org.opengis.filter.FilterFactory;
import org.opengis.temporal.Instant;
import org.opengis.temporal.Period;

import junit.framework.TestCase;

public class TemporalFilterTestSupport extends TestCase {

    protected static FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
    
    static DateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    
    protected Date date(String date) throws ParseException {
        return FORMAT.parse(date);
    }
    
    protected Instant instant(String d) throws ParseException {
        return new DefaultInstant(new DefaultPosition(date(d)));
    }
    protected Period period(String d1, String d2) throws ParseException {
        return new DefaultPeriod(instant(d1), instant(d2));
    }
}
