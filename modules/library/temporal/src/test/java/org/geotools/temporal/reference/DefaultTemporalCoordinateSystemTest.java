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

import java.util.Calendar;
import java.util.Date;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.NamedIdentifier;
import org.geotools.temporal.object.DefaultTemporalCoordinate;
import org.geotools.util.SimpleInternationalString;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.opengis.temporal.TemporalCoordinate;
import org.opengis.temporal.TemporalCoordinateSystem;
import org.opengis.util.InternationalString;


/**
 *
 * @author Mehdi Sidhoum (Geomatys)
 *
 *
 * @source $URL$
 */
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
        temporalCoordinateSystem1 = new DefaultTemporalCoordinateSystem(name1, null, cal.getTime(), new SimpleInternationalString("day"));
        temporalCoordinate1 = new DefaultTemporalCoordinate(temporalCoordinateSystem1, null, 50785.48);
        cal.set(1981, 6, 25);
        temporalCoordinateSystem2 = new DefaultTemporalCoordinateSystem(name2, null, cal.getTime(), new SimpleInternationalString("month"));
        temporalCoordinate2 = new DefaultTemporalCoordinate(temporalCoordinateSystem2, null, 285);
    }

    @After
    public void tearDown() {
        temporalCoordinateSystem1 = null;
        temporalCoordinateSystem2 = null;
        temporalCoordinate1 = null;
        temporalCoordinate2 = null;
    }

    /**
     * Test of setOrigin method, of class DefaultTemporalCoordinateSystem.
     */
    @Test
    public void testSetOrigin() {
        Date result = temporalCoordinateSystem1.getOrigin();
        ((DefaultTemporalCoordinateSystem) temporalCoordinateSystem1).setOrigin(new Date());
        assertFalse(temporalCoordinateSystem1.getOrigin().equals(result));
    }

    /**
     * Test of setInterval method, of class DefaultTemporalCoordinateSystem.
     */
    @Test
    public void testSetInterval() {
        InternationalString result = temporalCoordinateSystem1.getInterval();
        ((DefaultTemporalCoordinateSystem) temporalCoordinateSystem1).setInterval(new SimpleInternationalString("hour"));
        assertFalse(temporalCoordinateSystem1.getInterval().equals(result));
    }

    /**
     * Test of getOrigin method, of class DefaultTemporalCoordinateSystem.
     */
    @Test
    public void testGetOrigin() {
        Date result = temporalCoordinateSystem1.getOrigin();
        assertFalse(temporalCoordinateSystem2.getOrigin().equals(result));
    }

    /**
     * Test of getInterval method, of class DefaultTemporalCoordinateSystem.
     */
    @Test
    public void testGetInterval() {
        InternationalString result = temporalCoordinateSystem1.getInterval();
        assertFalse(temporalCoordinateSystem2.getInterval().equals(result));
    }

    /**
     * Test of transformCoord method, of class DefaultTemporalCoordinateSystem.
     */
    @Test
    public void testTransformCoord() {

        Date result = temporalCoordinateSystem1.transformCoord(temporalCoordinate1);
        assertFalse(temporalCoordinateSystem2.transformCoord(temporalCoordinate2).equals(result));
    }

    /**
     * Test of transformDateTime method, of class DefaultTemporalCoordinateSystem.
     */
    @Test
    public void testTransformDateTime() {
        TemporalCoordinate result = temporalCoordinateSystem1.transformDateTime(new Date());
        assertFalse(temporalCoordinateSystem2.transformDateTime(new Date()).equals(result));
    }

    /**
     * Test of equals method, of class DefaultTemporalCoordinateSystem.
     */
    @Test
    public void testEquals() {
        assertFalse(temporalCoordinateSystem1.equals(null));
        assertEquals(temporalCoordinateSystem1, temporalCoordinateSystem1);
        assertFalse(temporalCoordinateSystem1.equals(temporalCoordinateSystem2));
    }

    /**
     * Test of hashCode method, of class DefaultTemporalCoordinateSystem.
     */
    @Test
    public void testHashCode() {
        int result = temporalCoordinateSystem1.hashCode();
        assertFalse(temporalCoordinateSystem2.hashCode() == result);
    }

    /**
     * Test of toString method, of class DefaultTemporalCoordinateSystem.
     */
    @Test
    public void testToString() {
        String result = temporalCoordinateSystem1.toString();
        assertFalse(temporalCoordinateSystem2.toString().equals(result));
    }
}
