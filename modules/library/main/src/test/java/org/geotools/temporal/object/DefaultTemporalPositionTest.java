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

import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.NamedIdentifier;
import org.geotools.temporal.reference.DefaultTemporalReferenceSystem;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.geotools.api.temporal.IndeterminateValue;
import org.geotools.api.temporal.TemporalPosition;
import org.geotools.api.temporal.TemporalReferenceSystem;

/** @author Mehdi Sidhoum (Geomatys) */
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

    /** Test of getIndeterminatePosition method, of class DefaultTemporalPosition. */
    @Test
    public void testGetIndeterminatePosition() {
        IndeterminateValue result = temporalPosition1.getIndeterminatePosition();
        assertNotEquals(temporalPosition2.getIndeterminatePosition(), result);
    }

    /** Test of getFrame method, of class DefaultTemporalPosition. */
    @Test
    public void testGetFrame() {
        TemporalReferenceSystem result = ((DefaultTemporalPosition) temporalPosition1).getFrame();
        assertNotEquals(((DefaultTemporalPosition) temporalPosition2).getFrame(), result);
    }

    /** Test of setFrame method, of class DefaultTemporalPosition. */
    @Test
    public void testSetFrame() {
        TemporalReferenceSystem result = ((DefaultTemporalPosition) temporalPosition1).getFrame();
        NamedIdentifier name = new NamedIdentifier(Citations.CRS, "Babylonian calendar");
        ((DefaultTemporalPosition) temporalPosition1)
                .setFrame(new DefaultTemporalReferenceSystem(name, null));
        assertNotEquals(((DefaultTemporalPosition) temporalPosition1).getFrame(), result);
    }

    /** Test of setIndeterminatePosition method, of class DefaultTemporalPosition. */
    @Test
    public void testSetIndeterminatePosition() {
        IndeterminateValue result = temporalPosition1.getIndeterminatePosition();
        ((DefaultTemporalPosition) temporalPosition1)
                .setIndeterminatePosition(IndeterminateValue.BEFORE);
        assertNotEquals(temporalPosition1.getIndeterminatePosition(), result);
    }

    /** Test of equals method, of class DefaultTemporalPosition. */
    @Test
    public void testEquals() {
        assertNotEquals(null, temporalPosition1);
        assertEquals(temporalPosition1, temporalPosition1);
        assertNotEquals(temporalPosition1, temporalPosition2);
    }

    /** Test of hashCode method, of class DefaultTemporalPosition. */
    @Test
    public void testHashCode() {
        int result = temporalPosition1.hashCode();
        assertNotEquals(temporalPosition2.hashCode(), result);
    }

    /** Test of toString method, of class DefaultTemporalPosition. */
    @Test
    public void testToString() {
        String result = temporalPosition1.toString();
        assertNotEquals(temporalPosition2.toString(), result);
    }
}
