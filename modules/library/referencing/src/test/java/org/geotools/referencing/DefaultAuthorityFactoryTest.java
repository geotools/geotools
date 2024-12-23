/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2022, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing;

import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.referencing.factory.MockCRSAuthorityFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Testing the creation of CRS when more than one factory have the same authority responding on the same code. Checking
 * how to handle version vs priority.
 *
 * @author Roar Brænden
 */
public class DefaultAuthorityFactoryTest {

    @Before
    public void setupFactories() {
        ReferencingFactoryFinder.reset();
        System.setProperty(MockCRSAuthorityFactory.USE_MOCK_CRS_FACTORY, Boolean.toString(true));
    }

    @Test
    public void testCRSStraight() throws Exception {
        DefaultAuthorityFactory factory = new DefaultAuthorityFactory(false);
        CoordinateReferenceSystem crs = factory.createCoordinateReferenceSystem("EPSG:99999");
        Assert.assertEquals("First", crs.getName().getCode());
    }

    /** Notice that SecondEPSGFactory has a hint VERSION with blank string */
    @Test
    public void testMissingVersion() throws Exception {
        DefaultAuthorityFactory factory = new DefaultAuthorityFactory(false);
        CoordinateReferenceSystem crs = factory.createCoordinateReferenceSystem("urn:ogc:def:crs:EPSG:2.0:99999");
        Assert.assertEquals("First", crs.getName().getCode());
    }

    @Test
    public void testVersionBeforePriority() throws Exception {
        DefaultAuthorityFactory factory = new DefaultAuthorityFactory(false);
        CoordinateReferenceSystem crs = factory.createCoordinateReferenceSystem("urn:ogc:def:crs:EPSG:3.0:99999");
        Assert.assertEquals("Third", crs.getName().getCode());
    }

    @After
    public void cleanUp() {
        System.clearProperty(MockCRSAuthorityFactory.USE_MOCK_CRS_FACTORY);
        ReferencingFactoryFinder.reset();
    }
}
