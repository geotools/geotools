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

public class TEqualsImplTest extends TemporalFilterTestSupport {

    public void test() throws Exception {
        Instant i1 = instant("2001-07-04T12:08:00.000-0700");
        Instant i2 = instant("2001-07-05T12:08:00.000-0700");
        Instant i3 = instant("2001-07-05T12:08:00.000-0700");
        doAssert(i1, i2, false);
        doAssert(i2, i3, true);
        doAssert(i3, i2, true);

        Period p1 = period("2001-07-04T12:07:00.000-0700", "2001-07-05T12:08:00.000-0700");
        doAssert(p1, i1, false);
        doAssert(i1, p1, false);
        doAssert(p1, i2, false);
        doAssert(i2, p1, false);

        Period p2 = period("2001-07-04T12:06:59.000-0700", "2001-07-05T12:08:00.000-0700");
        Period p3 = period("2001-07-04T12:06:59.000-0700", "2001-07-05T12:08:00.000-0700");
        doAssert(p1, p2, false);
        doAssert(p2, p3, true);
        doAssert(p3, p2, true);
    }

    void doAssert(TemporalPrimitive tp1, TemporalPrimitive tp2, boolean b) {
        TEqualsImpl a = new TEqualsImpl(ff.literal(tp1), ff.literal(tp2));
        assertEquals(b, a.evaluate(null));
        assertFalse(a.toString().contains(TEqualsImpl.class.getName()));
        assertTrue(a.toString().contains(TEqualsImpl.NAME));
    }
}
