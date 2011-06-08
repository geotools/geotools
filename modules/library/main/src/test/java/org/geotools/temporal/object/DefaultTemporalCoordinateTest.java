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

import java.util.GregorianCalendar;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.NamedIdentifier;
import org.geotools.temporal.reference.DefaultTemporalCoordinateSystem;
import org.geotools.util.SimpleInternationalString;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.opengis.temporal.IndeterminateValue;
import org.opengis.temporal.TemporalCoordinate;
import org.opengis.temporal.TemporalCoordinateSystem;
import static org.junit.Assert.*;


/**
 *
 * @author Mehdi Sidhoum (Geomatys)
 *
 *
 * @source $URL$
 */
public class DefaultTemporalCoordinateTest {

    private TemporalCoordinate temporalCoordinate1;
    private TemporalCoordinate temporalCoordinate2;

    @Before
    public void setUp() {
        NamedIdentifier name = new NamedIdentifier(Citations.CRS, "Gregorian calendar");
        GregorianCalendar gc = new GregorianCalendar(-4713, 1, 1);
        Number coordinateValue = 100;
        TemporalCoordinateSystem frame1 = new DefaultTemporalCoordinateSystem(new NamedIdentifier(Citations.CRS, new SimpleInternationalString("Julian calendar")),
                null, gc.getTime(), new SimpleInternationalString("day"));
        TemporalCoordinateSystem frame2 = new DefaultTemporalCoordinateSystem(new NamedIdentifier(Citations.CRS, new SimpleInternationalString("Julian calendar")),
                null, gc.getTime(), new SimpleInternationalString("hour"));
        temporalCoordinate1 = new DefaultTemporalCoordinate(frame1, IndeterminateValue.NOW, coordinateValue);
        temporalCoordinate2 = new DefaultTemporalCoordinate(frame2, IndeterminateValue.AFTER, coordinateValue);
    }

    @After
    public void tearDown() {
        temporalCoordinate1 = null;
        temporalCoordinate2 = null;
    }

    /**
     * Test of getCoordinateValue method, of class DefaultTemporalCoordinate.
     */
    @Test
    public void testGetCoordinateValue() {
        Number result = temporalCoordinate1.getCoordinateValue();
        assertTrue(temporalCoordinate2.getCoordinateValue() == result);
    }

    /**
     * Test of setCoordinateValue method, of class DefaultTemporalCoordinate.
     */
    @Test
    public void testSetCoordinateValue() {
        Number result = temporalCoordinate1.getCoordinateValue();
        ((DefaultTemporalCoordinate) temporalCoordinate1).setCoordinateValue(250);
        assertFalse(temporalCoordinate1.getCoordinateValue() == result);
    }

    /**
     * Test of equals method, of class DefaultTemporalCoordinate.
     */
    @Test
    public void testEquals() {
        assertFalse(temporalCoordinate1.equals(null));
        assertEquals(temporalCoordinate1, temporalCoordinate1);
        assertFalse(temporalCoordinate1.equals(temporalCoordinate2));
    }

    /**
     * Test of hashCode method, of class DefaultTemporalCoordinate.
     */
    @Test
    public void testHashCode() {
        int result = temporalCoordinate1.hashCode();
        assertFalse(temporalCoordinate2.hashCode() == result);
    }

    /**
     * Test of toString method, of class DefaultTemporalCoordinate.
     */
    @Test
    public void testToString() {
        String result = temporalCoordinate1.toString();
        assertFalse(temporalCoordinate2.toString().equals(result));
    }
}
