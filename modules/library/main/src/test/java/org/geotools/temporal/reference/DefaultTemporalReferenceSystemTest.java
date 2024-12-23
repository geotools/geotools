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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Set;
import org.geotools.api.metadata.extent.Extent;
import org.geotools.api.metadata.extent.TemporalExtent;
import org.geotools.api.referencing.ReferenceIdentifier;
import org.geotools.api.temporal.TemporalReferenceSystem;
import org.geotools.api.util.GenericName;
import org.geotools.api.util.InternationalString;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.metadata.iso.extent.ExtentImpl;
import org.geotools.metadata.iso.extent.TemporalExtentImpl;
import org.geotools.referencing.NamedIdentifier;
import org.geotools.util.SimpleInternationalString;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/** @author Mehdi Sidhoum (Geomatys) */
public class DefaultTemporalReferenceSystemTest {

    private TemporalReferenceSystem temporalReferenceSystem1;
    private TemporalReferenceSystem temporalReferenceSystem2;

    @Before
    public void setUp() {
        NamedIdentifier name1 = new NamedIdentifier(Citations.CRS, "ref system1");
        NamedIdentifier name2 = new NamedIdentifier(Citations.CRS, "ref system2");
        temporalReferenceSystem1 = new DefaultTemporalReferenceSystem(name1, null);
        temporalReferenceSystem2 = new DefaultTemporalReferenceSystem(name2, null);
    }

    @After
    public void tearDown() {
        temporalReferenceSystem1 = null;
        temporalReferenceSystem2 = null;
    }

    /** Test of getName method, of class DefaultTemporalReferenceSystem. */
    @Test
    public void testGetName() {
        ReferenceIdentifier result = temporalReferenceSystem1.getName();
        assertNotEquals(temporalReferenceSystem2.getName(), result);
    }

    /** Test of getDomainOfValidity method, of class DefaultTemporalReferenceSystem. */
    @Test
    public void testGetDomainOfValidity() {
        Extent result = temporalReferenceSystem1.getDomainOfValidity();
        assertEquals(temporalReferenceSystem2.getDomainOfValidity(), result);
    }

    /** Test of getScope method, of class DefaultTemporalReferenceSystem. */
    @Test
    public void testGetScope() {
        InternationalString result = temporalReferenceSystem1.getScope();
        assertEquals(temporalReferenceSystem2.getScope(), result);
    }

    /** Test of getAlias method, of class DefaultTemporalReferenceSystem. */
    @Test
    public void testGetAlias() {
        Collection<GenericName> result = temporalReferenceSystem1.getAlias();
        assertEquals(temporalReferenceSystem2.getAlias(), result);
    }

    /** Test of getIdentifiers method, of class DefaultTemporalReferenceSystem. */
    @Test
    public void testGetIdentifiers() {
        Set<ReferenceIdentifier> result = temporalReferenceSystem1.getIdentifiers();
        assertEquals(temporalReferenceSystem2.getIdentifiers(), result);
    }

    /** Test of getRemarks method, of class DefaultTemporalReferenceSystem. */
    @Test
    public void testGetRemarks() {
        InternationalString result = temporalReferenceSystem1.getRemarks();
        assertEquals(temporalReferenceSystem2.getRemarks(), result);
    }

    /** Test of toWKT method, of class DefaultTemporalReferenceSystem. */
    @Test
    public void testToWKT() {
        // @TODO this method is not implemented yet!
    }

    /** Test of setName method, of class DefaultTemporalReferenceSystem. */
    @Test
    public void testSetName() {
        ReferenceIdentifier result = temporalReferenceSystem1.getName();
        ((DefaultTemporalReferenceSystem) temporalReferenceSystem1)
                .setName(new NamedIdentifier(Citations.CRS, "new name"));
        assertNotEquals(temporalReferenceSystem1.getName(), result);
    }

    /** Test of setDomainOfValidity method, of class DefaultTemporalReferenceSystem. */
    @Test
    public void testSetDomainOfValidity() {
        Extent result = temporalReferenceSystem1.getDomainOfValidity();
        ExtentImpl domainOfValidity = new ExtentImpl();
        domainOfValidity.setDescription(new SimpleInternationalString("Western Europe"));
        Calendar cal = Calendar.getInstance();
        cal.set(0, 0, 0);
        TemporalExtentImpl temporalExt = new TemporalExtentImpl(cal.getTime(), new Date());
        Collection<TemporalExtent> collection = new ArrayList<>();
        collection.add(temporalExt);
        domainOfValidity.setTemporalElements(collection);
        ((DefaultTemporalReferenceSystem) temporalReferenceSystem1).setDomainOfValidity(domainOfValidity);
        assertNotEquals(temporalReferenceSystem1.getDomainOfValidity(), result);
    }

    /** Test of setValidArea method, of class DefaultTemporalReferenceSystem. */
    @Test
    public void testSetValidArea() {
        // This method is deprecated
    }

    /** Test of setScope method, of class DefaultTemporalReferenceSystem. */
    @Test
    public void testSetScope() {
        InternationalString result = temporalReferenceSystem1.getScope();
        assertEquals(temporalReferenceSystem1.getScope(), result);
    }

    /** Test of equals method, of class DefaultTemporalReferenceSystem. */
    @Test
    public void testEquals() {
        assertNotEquals(null, temporalReferenceSystem1);
        assertEquals(temporalReferenceSystem1, temporalReferenceSystem1);
        assertNotEquals(temporalReferenceSystem1, temporalReferenceSystem2);
    }

    /** Test of hashCode method, of class DefaultTemporalReferenceSystem. */
    @Test
    public void testHashCode() {
        int result = temporalReferenceSystem1.hashCode();
        assertNotEquals(temporalReferenceSystem2.hashCode(), result);
    }

    /** Test of toString method, of class DefaultTemporalReferenceSystem. */
    @Test
    public void testToString() {
        String result = temporalReferenceSystem1.toString();
        assertNotEquals(temporalReferenceSystem2.toString(), result);
    }
}
