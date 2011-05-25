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

import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.NamedIdentifier;
import org.geotools.temporal.reference.DefaultTemporalReferenceSystem;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.opengis.temporal.IndeterminateValue;
import org.opengis.temporal.TemporalPosition;
import org.opengis.temporal.TemporalReferenceSystem;


/**
 *
 * @author Mehdi Sidhoum (Geomatys)
 *
 *
 * @source $URL$
 */
public class DefaultTemporalPositionTest {

    private TemporalPosition temporalPosition1;
    private TemporalPosition temporalPosition2;

    @Before
    public void setUp() {
        NamedIdentifier name1 = new NamedIdentifier(Citations.CRS, "Gregorian calendar");
        NamedIdentifier name2 = new NamedIdentifier(Citations.CRS, "Julian calendar");
        TemporalReferenceSystem frame1 = new DefaultTemporalReferenceSystem(name1, null);
        TemporalReferenceSystem frame2 = new DefaultTemporalReferenceSystem(name2, null);
        temporalPosition1 = new DefaultTemporalPosition(frame1, IndeterminateValue.UNKNOWN);
        temporalPosition2 = new DefaultTemporalPosition(frame2, IndeterminateValue.NOW);
    }

    @After
    public void tearDown() {
        temporalPosition1 = null;
        temporalPosition2 = null;
    }

    /**
     * Test of getIndeterminatePosition method, of class DefaultTemporalPosition.
     */
    @Test
    public void testGetIndeterminatePosition() {
        IndeterminateValue result = temporalPosition1.getIndeterminatePosition();
        assertFalse(temporalPosition2.getIndeterminatePosition().equals(result));
    }

    /**
     * Test of getFrame method, of class DefaultTemporalPosition.
     */
    @Test
    public void testGetFrame() {
        TemporalReferenceSystem result = ((DefaultTemporalPosition) temporalPosition1).getFrame();
        assertFalse(((DefaultTemporalPosition) temporalPosition2).getFrame().equals(result));
    }

    /**
     * Test of setFrame method, of class DefaultTemporalPosition.
     */
    @Test
    public void testSetFrame() {
        TemporalReferenceSystem result = ((DefaultTemporalPosition) temporalPosition1).getFrame();
        NamedIdentifier name = new NamedIdentifier(Citations.CRS, "Babylonian calendar");
        ((DefaultTemporalPosition) temporalPosition1).setFrame(new DefaultTemporalReferenceSystem(name, null));
        assertFalse(((DefaultTemporalPosition) temporalPosition1).getFrame().equals(result));
    }

    /**
     * Test of setIndeterminatePosition method, of class DefaultTemporalPosition.
     */
    @Test
    public void testSetIndeterminatePosition() {
        IndeterminateValue result = temporalPosition1.getIndeterminatePosition();
        ((DefaultTemporalPosition) temporalPosition1).setIndeterminatePosition(IndeterminateValue.BEFORE);
        assertFalse(temporalPosition1.getIndeterminatePosition().equals(result));
    }

    /**
     * Test of equals method, of class DefaultTemporalPosition.
     */
    @Test
    public void testEquals() {
        assertFalse(temporalPosition1.equals(null));
        assertEquals(temporalPosition1, temporalPosition1);
        assertFalse(temporalPosition1.equals(temporalPosition2));
    }

    /**
     * Test of hashCode method, of class DefaultTemporalPosition.
     */
    @Test
    public void testHashCode() {
        int result = temporalPosition1.hashCode();
        assertFalse(temporalPosition2.hashCode() == result);
    }

    /**
     * Test of toString method, of class DefaultTemporalPosition.
     */
    @Test
    public void testToString() {
        String result = temporalPosition1.toString();
        assertFalse(temporalPosition2.toString().equals(result));
    }
}
