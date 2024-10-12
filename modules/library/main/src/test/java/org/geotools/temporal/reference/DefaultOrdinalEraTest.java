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
import java.util.Collection;
import java.util.Date;
import org.geotools.api.temporal.OrdinalEra;
import org.geotools.api.util.InternationalString;
import org.geotools.util.SimpleInternationalString;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/** @author Mehdi Sidhoum (Geomatys) */
public class DefaultOrdinalEraTest {

    private OrdinalEra ordinalEra1;
    private OrdinalEra ordinalEra2;
    private Calendar cal = Calendar.getInstance();

    @Before
    public void setUp() {

        cal.set(1900, 1, 1);
        Date beginning1 = cal.getTime();
        cal.set(2000, 1, 1);
        Date end1 = cal.getTime();
        cal.set(2000, 1, 1);
        Date beginning2 = cal.getTime();
        cal.set(2012, 1, 1);
        Date end2 = cal.getTime();
        ordinalEra1 = new DefaultOrdinalEra(new SimpleInternationalString("old Era"), beginning1, end1);
        ordinalEra2 = new DefaultOrdinalEra(new SimpleInternationalString("new Era"), beginning2, end2);
    }

    @After
    public void tearDown() {
        ordinalEra1 = null;
        ordinalEra2 = null;
    }

    /** Test of getName method, of class DefaultOrdinalEra. */
    @Test
    public void testGetName() {
        InternationalString result = ordinalEra1.getName();
        assertNotEquals(ordinalEra2.getName(), result);
    }

    /** Test of getBeginning method, of class DefaultOrdinalEra. */
    @Test
    public void testGetBeginning() {
        Date result = ordinalEra1.getBeginning();
        assertNotEquals(ordinalEra2.getBeginning(), result);
    }

    /** Test of getEnd method, of class DefaultOrdinalEra. */
    @Test
    public void testGetEnd() {
        Date result = ordinalEra1.getEnd();
        assertNotEquals(ordinalEra2.getEnd(), result);
    }

    /** Test of getComposition method, of class DefaultOrdinalEra. */
    @Test
    public void testGetComposition() {
        Collection<OrdinalEra> result = ordinalEra1.getComposition();
        assertEquals(ordinalEra2.getComposition(), result);
    }

    /** Test of setName method, of class DefaultOrdinalEra. */
    @Test
    public void testSetName() {
        InternationalString result = ordinalEra1.getName();
        ((DefaultOrdinalEra) ordinalEra1).setName(new SimpleInternationalString(""));
        assertNotEquals(ordinalEra1.getName(), result);
    }

    /** Test of setBeginning method, of class DefaultOrdinalEra. */
    @Test
    public void testSetBeginning() {
        Date result = ordinalEra1.getBeginning();
        ((DefaultOrdinalEra) ordinalEra1).setBeginning(new Date());
        assertNotEquals(ordinalEra1.getBeginning(), result);
    }

    /** Test of setEnd method, of class DefaultOrdinalEra. */
    @Test
    public void testSetEnd() {
        Date result = ordinalEra1.getEnd();
        ((DefaultOrdinalEra) ordinalEra1).setEnd(new Date());
        assertNotEquals(ordinalEra1.getEnd(), result);
    }

    /** Test of getGroup method, of class DefaultOrdinalEra. */
    @Test
    public void testGetGroup() {
        DefaultOrdinalEra result = ((DefaultOrdinalEra) ordinalEra1).getGroup();
        assertEquals(((DefaultOrdinalEra) ordinalEra2).getGroup(), result);
    }

    /** Test of setGroup method, of class DefaultOrdinalEra. */
    @Test
    public void testSetGroup() {
        DefaultOrdinalEra result = ((DefaultOrdinalEra) ordinalEra1).getGroup();
        cal.set(1900, 0, 0);
        ((DefaultOrdinalEra) ordinalEra1)
                .setGroup(new DefaultOrdinalEra(new SimpleInternationalString(""), cal.getTime(), new Date()));
        assertNotEquals(((DefaultOrdinalEra) ordinalEra1).getGroup(), result);
    }

    /** Test of equals method, of class DefaultOrdinalEra. */
    @Test
    public void testEquals() {
        assertNotEquals(null, ordinalEra1);
        assertEquals(ordinalEra1, ordinalEra1);
        assertNotEquals(ordinalEra1, ordinalEra2);
    }

    /** Test of hashCode method, of class DefaultOrdinalEra. */
    @Test
    public void testHashCode() {
        int result = ordinalEra1.hashCode();
        assertNotEquals(ordinalEra2.hashCode(), result);
    }

    /** Test of toString method, of class DefaultOrdinalEra. */
    @Test
    public void testToString() {
        String result = ordinalEra1.toString();
        assertNotEquals(ordinalEra2.toString(), result);
    }
}
