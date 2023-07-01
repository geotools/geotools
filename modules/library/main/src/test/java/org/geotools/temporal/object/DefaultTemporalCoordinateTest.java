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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import java.util.GregorianCalendar;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.NamedIdentifier;
import org.geotools.temporal.reference.DefaultTemporalCoordinateSystem;
import org.geotools.util.SimpleInternationalString;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.geotools.api.temporal.IndeterminateValue;
import org.geotools.api.temporal.TemporalCoordinate;
import org.geotools.api.temporal.TemporalCoordinateSystem;

/** @author Mehdi Sidhoum (Geomatys) */
public class DefaultTemporalCoordinateTest {

    private TemporalCoordinate temporalCoordinate1;
    private TemporalCoordinate temporalCoordinate2;

    @Before
    public void setUp() {
        GregorianCalendar gc = new GregorianCalendar(-4713, 1, 1);
        Number coordinateValue = 100;
        TemporalCoordinateSystem frame1 =
                new DefaultTemporalCoordinateSystem(
                        new NamedIdentifier(
                                Citations.CRS, new SimpleInternationalString("Julian calendar")),
                        null,
                        gc.getTime(),
                        new SimpleInternationalString("day"));
        TemporalCoordinateSystem frame2 =
                new DefaultTemporalCoordinateSystem(
                        new NamedIdentifier(
                                Citations.CRS, new SimpleInternationalString("Julian calendar")),
                        null,
                        gc.getTime(),
                        new SimpleInternationalString("hour"));
        temporalCoordinate1 =
                new DefaultTemporalCoordinate(frame1, IndeterminateValue.NOW, coordinateValue);
        temporalCoordinate2 =
                new DefaultTemporalCoordinate(frame2, IndeterminateValue.AFTER, coordinateValue);
    }

    @After
    public void tearDown() {
        temporalCoordinate1 = null;
        temporalCoordinate2 = null;
    }

    /** Test of getCoordinateValue method, of class DefaultTemporalCoordinate. */
    @Test
    public void testGetCoordinateValue() {
        Number result = temporalCoordinate1.getCoordinateValue();
        assertSame(temporalCoordinate2.getCoordinateValue(), result);
    }

    /** Test of setCoordinateValue method, of class DefaultTemporalCoordinate. */
    @Test
    public void testSetCoordinateValue() {
        Number result = temporalCoordinate1.getCoordinateValue();
        ((DefaultTemporalCoordinate) temporalCoordinate1).setCoordinateValue(250);
        assertNotSame(temporalCoordinate1.getCoordinateValue(), result);
    }

    /** Test of equals method, of class DefaultTemporalCoordinate. */
    @Test
    public void testEquals() {
        assertNotEquals(null, temporalCoordinate1);
        assertEquals(temporalCoordinate1, temporalCoordinate1);
        assertNotEquals(temporalCoordinate1, temporalCoordinate2);
    }

    /** Test of hashCode method, of class DefaultTemporalCoordinate. */
    @Test
    public void testHashCode() {
        int result = temporalCoordinate1.hashCode();
        assertNotEquals(temporalCoordinate2.hashCode(), result);
    }

    /** Test of toString method, of class DefaultTemporalCoordinate. */
    @Test
    public void testToString() {
        String result = temporalCoordinate1.toString();
        assertNotEquals(temporalCoordinate2.toString(), result);
    }
}
