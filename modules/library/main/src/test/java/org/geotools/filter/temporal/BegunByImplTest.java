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

public class BegunByImplTest extends TemporalFilterTestSupport {

    public void test() throws Exception {
        Instant i1 = instant("2001-07-05T12:08:56.235-0700");
        Instant i2 = instant("2001-07-04T12:08:56.235-0700");
        doAssert(i1, i2, false);
        doAssert(i2, i1, false);
        doAssert(i1, i1, false);
        
        Period p1 = period("2001-07-05T12:08:56.235-0700", "2001-07-06T12:08:56.235-0700");
        doAssert(i1, p1, false);
        doAssert(p1, i1, true);
        
        Period p2 = period("2001-07-05T12:08:56.235-0700", "2001-07-06T12:08:57.235-0700");
        Period p3 = period("2001-07-05T12:08:56.235-0700", "2001-07-06T12:08:55.235-0700");
        doAssert(p1, p1, false);
        doAssert(p1, p2, false);
        doAssert(p1, p3, true);
        doAssert(p2, p1, true);
        doAssert(p3, p1, false);
        
    }

    void doAssert(TemporalPrimitive tp1, TemporalPrimitive tp2, boolean b) {
        BegunByImpl a = new BegunByImpl(ff.literal(tp1), ff.literal(tp2));
        assertEquals(b, a.evaluate(null));
    }
}
