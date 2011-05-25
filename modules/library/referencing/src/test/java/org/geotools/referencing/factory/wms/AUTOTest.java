/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.factory.wms;

import static org.junit.Assert.*;

import java.util.Collection;

import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.geotools.referencing.operation.projection.EquatorialOrthographic;
import org.geotools.referencing.operation.projection.EquidistantCylindrical;
import org.geotools.referencing.operation.projection.ObliqueOrthographic;
import org.geotools.referencing.operation.projection.PolarOrthographic;
import org.junit.Before;
import org.junit.Test;
import org.opengis.metadata.citation.Citation;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CRSAuthorityFactory;
import org.opengis.referencing.crs.ProjectedCRS;


/**
 * Tests {@link AutoCRSFactory}.
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Jody Garnett
 * @author Martin Desruisseaux
 */
public final class AUTOTest {
    /**
     * The factory to test.
     */
    private CRSAuthorityFactory factory;

    /**
     * Initializes the factory to test.
     */
    @Before
    public void setUp() {
        factory = new AutoCRSFactory();
    }

    /**
     * Tests the registration in {@link ReferencingFactoryFinder}.
     */
    @Test
    public void testFactoryFinder() {
        final Collection<String> authorities = ReferencingFactoryFinder.getAuthorityNames();
        assertTrue(authorities.contains("AUTO"));
        assertTrue(authorities.contains("AUTO2"));
        factory = ReferencingFactoryFinder.getCRSAuthorityFactory("AUTO", null);
        assertTrue(factory instanceof AutoCRSFactory);
        assertSame(factory, ReferencingFactoryFinder.getCRSAuthorityFactory("AUTO2", null));
    }

    /**
     * Checks the authority names.
     */
    @Test
    public void testAuthority() {
        final Citation authority = factory.getAuthority();
        assertTrue (Citations.identifierMatches(authority, "AUTO"));
        assertTrue (Citations.identifierMatches(authority, "AUTO2"));
        assertFalse(Citations.identifierMatches(authority, "EPSG"));
        assertFalse(Citations.identifierMatches(authority, "CRS"));
    }

    /**
     * UDIG requires this to work.
     */
    @Test
    public void test42001() throws FactoryException {
        final ProjectedCRS utm = factory.createProjectedCRS("AUTO:42001,0.0,0.0");
        assertNotNull("auto-utm", utm);
        assertSame   (utm, factory.createObject("AUTO :42001 ,0,0"));
        assertSame   (utm, factory.createObject("AUTO2:42001 ,0,0"));
        assertSame   (utm, factory.createObject(      "42001 ,0,0"));
        assertNotSame(utm, factory.createObject("AUTO :42001 ,30,0"));
        assertEquals ("Transverse_Mercator", utm.getConversionFromBase().getMethod().getName().getCode());
    }
    
    /**
     * Check we can parse also the unit
     */
    @Test
    public void test42001Units() throws FactoryException {
        final ProjectedCRS utm = factory.createProjectedCRS("AUTO:42001,9001,0.0,0.0");
        assertNotNull("auto-utm", utm);
        assertSame   (utm, factory.createObject("AUTO :42001, 9001,0,0"));
        assertSame   (utm, factory.createObject("AUTO2:42001, 9001,0,0"));
        assertSame   (utm, factory.createObject(      "42001, 9001,0,0"));
        assertNotSame(utm, factory.createObject("AUTO :42001, 9001,30,0"));
        assertEquals ("Transverse_Mercator", utm.getConversionFromBase().getMethod().getName().getCode());
    }
    
    @Test
    public void test42003() throws FactoryException {
        ProjectedCRS eqc = factory.createProjectedCRS("AUTO:42003,9001,0.0,0");
        assertEquals ("Orthographic", eqc.getConversionFromBase().getMethod().getName().getCode());
        assertTrue(eqc.getConversionFromBase().getMathTransform() instanceof EquatorialOrthographic);
        
        eqc = factory.createProjectedCRS("AUTO:42003,9001,0.0,90");
        assertEquals ("Orthographic", eqc.getConversionFromBase().getMethod().getName().getCode());
        assertTrue(eqc.getConversionFromBase().getMathTransform() instanceof PolarOrthographic);
        
        eqc = factory.createProjectedCRS("AUTO:42003,9001,0.0,45");
        assertEquals ("Orthographic", eqc.getConversionFromBase().getMethod().getName().getCode());
        assertTrue(eqc.getConversionFromBase().getMathTransform() instanceof ObliqueOrthographic);
    }
    
    @Test
    public void test42004() throws FactoryException {
        final ProjectedCRS eqc = factory.createProjectedCRS("AUTO:42004,9001,0.0,35");
        assertEquals ("Equidistant_Cylindrical", eqc.getConversionFromBase().getMethod().getName().getCode());
        String stdParallel1Code = EquidistantCylindrical.Provider.STANDARD_PARALLEL_1.getName().getCode();
        double stdParallel1 = eqc.getConversionFromBase().getParameterValues().parameter(stdParallel1Code).doubleValue();
        assertEquals(35.0, stdParallel1, 1e-9);
    }
}
