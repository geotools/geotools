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
package org.geotools.temporal.reference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.Calendar;
import java.util.Date;
import org.geotools.api.temporal.TemporalCoordinate;
import org.geotools.api.temporal.TemporalCoordinateSystem;
import org.geotools.api.util.InternationalString;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.NamedIdentifier;
import org.geotools.temporal.object.DefaultTemporalCoordinate;
import org.geotools.util.SimpleInternationalString;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/** @author Mehdi Sidhoum (Geomatys) */
public class DefaultTemporalCoordinateSystemTest {

    private TemporalCoordinateSystem temporalCoordinateSystem1;
    private TemporalCoordinateSystem temporalCoordinateSystem2;
    private DefaultTemporalCoordinate temporalCoordinate1;
    private DefaultTemporalCoordinate temporalCoordinate2;

    @Before
    public void setUp() {
        NamedIdentifier name1 = new NamedIdentifier(Citations.CRS, "Coordinate1");
        NamedIdentifier name2 = new NamedIdentifier(Citations.CRS, "Coordinate2");
        Calendar cal = Calendar.getInstance();
        cal.set(2000, 1, 1);
        temporalCoordinateSystem1 =
                new DefaultTemporalCoordinateSystem(
                        name1, null, cal.getTime(), new SimpleInternationalString("day"));
        temporalCoordinate1 =
                new DefaultTemporalCoordinate(temporalCoordinateSystem1, null, 50785.48);
        cal.set(1981, 6, 25);
        temporalCoordinateSystem2 =
                new DefaultTemporalCoordinateSystem(
                        name2, null, cal.getTime(), new SimpleInternationalString("month"));
        temporalCoordinate2 = new DefaultTemporalCoordinate(temporalCoordinateSystem2, null, 285);
    }

    @After
    public void tearDown() {
        temporalCoordinateSystem1 = null;
        temporalCoordinateSystem2 = null;
        temporalCoordinate1 = null;
        temporalCoordinate2 = null;
    }

    /** Test of setOrigin method, of class DefaultTemporalCoordinateSystem. */
    @Test
    public void testSetOrigin() {
        Date result = temporalCoordinateSystem1.getOrigin();
        ((DefaultTemporalCoordinateSystem) temporalCoordinateSystem1).setOrigin(new Date());
        assertNotEquals(temporalCoordinateSystem1.getOrigin(), result);
    }

    /** Test of setInterval method, of class DefaultTemporalCoordinateSystem. */
    @Test
    public void testSetInterval() {
        InternationalString result = temporalCoordinateSystem1.getInterval();
        ((DefaultTemporalCoordinateSystem) temporalCoordinateSystem1)
                .setInterval(new SimpleInternationalString("hour"));
        assertNotEquals(temporalCoordinateSystem1.getInterval(), result);
    }

    /** Test of getOrigin method, of class DefaultTemporalCoordinateSystem. */
    @Test
    public void testGetOrigin() {
        Date result = temporalCoordinateSystem1.getOrigin();
        assertNotEquals(temporalCoordinateSystem2.getOrigin(), result);
    }

    /** Test of getInterval method, of class DefaultTemporalCoordinateSystem. */
    @Test
    public void testGetInterval() {
        InternationalString result = temporalCoordinateSystem1.getInterval();
        assertNotEquals(temporalCoordinateSystem2.getInterval(), result);
    }

    /** Test of transformCoord method, of class DefaultTemporalCoordinateSystem. */
    @Test
    public void testTransformCoord() {

        Date result = temporalCoordinateSystem1.transformCoord(temporalCoordinate1);
        assertNotEquals(temporalCoordinateSystem2.transformCoord(temporalCoordinate2), result);
    }

    /** Test of transformDateTime method, of class DefaultTemporalCoordinateSystem. */
    @Test
    public void testTransformDateTime() {
        TemporalCoordinate result = temporalCoordinateSystem1.transformDateTime(new Date());
        assertNotEquals(temporalCoordinateSystem2.transformDateTime(new Date()), result);
    }

    /** Test of equals method, of class DefaultTemporalCoordinateSystem. */
    @Test
    public void testEquals() {
        assertNotEquals(null, temporalCoordinateSystem1);
        assertEquals(temporalCoordinateSystem1, temporalCoordinateSystem1);
        assertNotEquals(temporalCoordinateSystem1, temporalCoordinateSystem2);
    }

    /** Test of hashCode method, of class DefaultTemporalCoordinateSystem. */
    @Test
    public void testHashCode() {
        int result = temporalCoordinateSystem1.hashCode();
        assertNotEquals(temporalCoordinateSystem2.hashCode(), result);
    }

    /** Test of toString method, of class DefaultTemporalCoordinateSystem. */
    @Test
    public void testToString() {
        String result = temporalCoordinateSystem1.toString();
        assertNotEquals(temporalCoordinateSystem2.toString(), result);
    }
}
