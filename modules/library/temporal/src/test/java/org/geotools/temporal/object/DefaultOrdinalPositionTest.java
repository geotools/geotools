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

import java.util.Calendar;
import java.util.Date;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.NamedIdentifier;
import org.geotools.temporal.reference.DefaultOrdinalEra;
import org.geotools.temporal.reference.DefaultTemporalReferenceSystem;
import org.geotools.util.SimpleInternationalString;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.opengis.temporal.IndeterminateValue;
import static org.junit.Assert.*;
import org.opengis.temporal.OrdinalEra;
import org.opengis.temporal.OrdinalPosition;
import org.opengis.temporal.TemporalReferenceSystem;

/**
 *
 * @author Mehdi Sidhoum (Geomatys)
 *
 * @source $URL$
 */
public class DefaultOrdinalPositionTest {

    private OrdinalPosition ordinalPosition1;
    private OrdinalPosition ordinalPosition2;
    private Calendar cal = Calendar.getInstance();

    @Before
    public void setUp() {
        NamedIdentifier name = new NamedIdentifier(Citations.CRS, "Gregorian calendar");
        TemporalReferenceSystem frame = new DefaultTemporalReferenceSystem(name, null);
        
        cal.set(500, 0, 1);
        Date beginning1 = cal.getTime();
        cal.set(1000, 0, 1);
        Date end1 = cal.getTime();
        OrdinalEra ordinalEra1 = new DefaultOrdinalEra(new SimpleInternationalString("Mesozoic"), beginning1, end1);
        cal.set(1000, 1, 1);
        Date beginning2 = cal.getTime();
        cal.set(2000, 0, 1);
        Date end2 = cal.getTime();
        OrdinalEra ordinalEra2 = new DefaultOrdinalEra(new SimpleInternationalString("Cenozoic"), beginning2, end2);
        ordinalPosition1 = new DefaultOrdinalPosition(frame, IndeterminateValue.UNKNOWN, ordinalEra1);
        ordinalPosition2 = new DefaultOrdinalPosition(frame, IndeterminateValue.AFTER, ordinalEra2);
    }

    @After
    public void tearDown() {
        ordinalPosition1 = null;
        ordinalPosition2 = null;
    }

    /**
     * Test of getOrdinalPosition method, of class DefaultOrdinalPosition.
     */
    @Test
    public void testGetOrdinalPosition() {
        OrdinalEra result = ordinalPosition1.getOrdinalPosition();
        assertFalse(ordinalPosition2.getOrdinalPosition().equals(result));
    }

    /**
     * Test of setOrdinalPosition method, of class DefaultOrdinalPosition.
     */
    @Test
    public void testSetOrdinalPosition() {
        OrdinalEra result = ordinalPosition1.getOrdinalPosition();
        cal.set(10, 0, 0);
        Date beginning = cal.getTime();
        cal.set(2012, 12, 23);
        Date end = cal.getTime();
        OrdinalEra ordinalEra = new DefaultOrdinalEra(new SimpleInternationalString("Era"), beginning, end);
        ((DefaultOrdinalPosition) ordinalPosition1).setOrdinalPosition(ordinalEra);
        assertFalse(ordinalPosition1.getOrdinalPosition().equals(result));
    }

    /**
     * Test of equals method, of class DefaultOrdinalPosition.
     */
    @Test
    public void testEquals() {
        assertFalse(ordinalPosition1.equals(null));
        assertEquals(ordinalPosition1, ordinalPosition1);
        assertFalse(ordinalPosition1.equals(ordinalPosition2));
    }

    /**
     * Test of hashCode method, of class DefaultOrdinalPosition.
     */
    @Test
    public void testHashCode() {
        int result = ordinalPosition1.hashCode();
        assertFalse(ordinalPosition2.hashCode() == result);
    }

    /**
     * Test of toString method, of class DefaultOrdinalPosition.
     */
    @Test
    public void testToString() {
        String result = ordinalPosition1.toString();
        assertFalse(ordinalPosition2.toString().equals(result));
    }
}
