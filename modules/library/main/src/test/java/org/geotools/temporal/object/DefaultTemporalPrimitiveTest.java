/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.temporal.object;

import static org.junit.Assert.assertNotEquals;

import java.util.Calendar;
import java.util.Date;
import org.geotools.api.temporal.Instant;
import org.geotools.api.temporal.Position;
import org.geotools.api.temporal.RelativePosition;
import org.geotools.api.temporal.TemporalPrimitive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/** @author Mehdi Sidhoum (Geomatys) */
public class DefaultTemporalPrimitiveTest {

    private TemporalPrimitive temporalPrimitive1;
    private TemporalPrimitive temporalPrimitive2;
    private Position position1;
    private Position position2;
    private Calendar cal = Calendar.getInstance();

    @Before
    public void setUp() {

        cal.set(1981, 6, 25);
        Date date = cal.getTime();

        position1 = new DefaultPosition(date);
        position2 = new DefaultPosition(new Date());
        temporalPrimitive1 = new DefaultInstant(position1);
        temporalPrimitive2 = new DefaultInstant(position2);
    }

    @After
    public void tearDown() {
        position1 = null;
        position2 = null;
        temporalPrimitive1 = null;
        temporalPrimitive2 = null;
    }

    /** Test of relativePosition method, of class DefaultTemporalPrimitive. */
    @Test
    public void testRelativePosition() {

        // relative position between Instant objects
        cal.set(2000, 0, 1);
        Position position = new DefaultPosition(cal.getTime());
        TemporalPrimitive other = new DefaultInstant(position);
        RelativePosition result = temporalPrimitive1.relativePosition(other);
        assertNotEquals(temporalPrimitive2.relativePosition(other), result);

        // relative position between Instant and Period
        Instant instant1 = new DefaultInstant(new DefaultPosition(cal.getTime()));
        Instant instant2 = (DefaultInstant) temporalPrimitive2;

        other = new DefaultPeriod(instant1, instant2);
        result = temporalPrimitive1.relativePosition(other);
        assertNotEquals(temporalPrimitive2.relativePosition(other), result);

        // relative position between Period onbjects
        temporalPrimitive1 = new DefaultPeriod(new DefaultInstant(position1), instant1);
        cal.set(2200, 9, 1);
        temporalPrimitive2 = new DefaultPeriod(instant2, new DefaultInstant(new DefaultPosition(cal.getTime())));
        result = temporalPrimitive1.relativePosition(other);
        assertNotEquals(temporalPrimitive2.relativePosition(other), result);
    }
}
