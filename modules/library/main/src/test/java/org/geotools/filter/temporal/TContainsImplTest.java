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

import org.opengis.temporal.Instant;
import org.opengis.temporal.Period;
import org.opengis.temporal.TemporalPrimitive;

/**
 * 
 *
 * @source $URL$
 */
public class TContainsImplTest extends TemporalFilterTestSupport {

    public void test() throws Exception {
        Instant i1 = instant("2001-07-04T12:08:56.235-0700");
        Instant i2 = instant("2001-07-05T12:08:56.235-0700");
        doAssert(i1, i2, false);
        doAssert(i2, i1, false);
        doAssert(i1, i1, false);
        
        Period p1 = period("2001-07-04T12:08:56.235-0700", "2001-07-05T12:08:56.235-0700");
        Instant i3 = instant("2001-07-05T12:00:00.235-0700");
        doAssert(p1, i1, false);
        doAssert(p1, i2, false);
        doAssert(p1, i3, true);
        doAssert(i3, p1, false);
        
        Period p2 = period("2001-07-04T12:08:56.235-0700", "2001-07-05T12:00:56.235-0700");
        doAssert(p1, p2, false);
        
        Period p3 = period("2001-07-04T13:08:56.235-0700", "2001-07-05T12:08:56.235-0700");
        doAssert(p1, p3, false);
        
        Period p4 = period("2001-07-04T13:08:56.235-0700", "2001-07-05T12:00:56.235-0700");
        doAssert(p1, p4, true);
    }

    void doAssert(TemporalPrimitive tp1, TemporalPrimitive tp2, boolean b) {
        TContainsImpl a = new TContainsImpl(ff.literal(tp1), ff.literal(tp2));
        assertEquals(b, a.evaluate(null));
    }
}
