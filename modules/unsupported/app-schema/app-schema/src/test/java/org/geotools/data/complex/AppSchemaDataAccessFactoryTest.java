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

package org.geotools.data.complex;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.geotools.data.DataAccess;
import org.geotools.data.DataAccessFinder;
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.data.FeatureSource;
import org.geotools.feature.Types;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.Name;

/**
 * 
 * @author Gabriel Roldan, Axios Engineering
 * @version $Id$
 * @source $URL$
 * @since 2.4
 */
public class AppSchemaDataAccessFactoryTest extends TestCase {

    AppSchemaDataAccessFactory factory;

    Map params;

    private static final String NSURI = "http://online.socialchange.net.au";

    static final Name mappedTypeName = Types.typeName(NSURI, "RoadSegment");

    protected void setUp() throws Exception {
        super.setUp();
        factory = new AppSchemaDataAccessFactory();
        params = new HashMap();
        params.put("dbtype", "app-schema");
        URL resource = getClass().getResource("/test-data/roadsegments.xml");
        if (resource == null) {
            fail("Can't find resouce /test-data/roadsegments.xml");
        }
        params.put("url", resource);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        factory = null;
        params = null;
    }

    /**
     * Test method for 'org.geotools.data.complex.AppSchemaDataAccessFactory.createDataStore(Map)'
     */
    public void testCreateDataStorePreconditions() {
        Map badParams = new HashMap();
        try {
            factory.createDataStore(badParams);
            fail("allowed bad params");
        } catch (IOException e) {
            // OK
        }
        badParams.put("dbtype", "app-schema");
        try {
            factory.createDataStore(badParams);
            fail("allowed bad params");
        } catch (IOException e) {
            // OK
        }
        badParams.put("url", "file://_inexistentConfigFile123456.xml");
        try {
            factory.createDataStore(badParams);
            fail("allowed bad params");
        } catch (IOException e) {
            // OK
        }
    }

    /**
     * 
     * @throws IOException
     */
    public void testCreateDataStore() throws IOException {
        DataAccess<FeatureType, Feature> ds = factory.createDataStore(params);
        assertNotNull(ds);
        FeatureSource<FeatureType, Feature> mappedSource = ds.getFeatureSource(mappedTypeName);
        assertNotNull(mappedSource);
        assertSame(ds, mappedSource.getDataStore());
        ds.dispose();
    }

    /**
     * 
     * @throws IOException
     */
    public void testFactoryLookup() throws IOException {
        DataAccess<FeatureType, Feature> ds = DataAccessFinder.getDataStore(params);
        assertNotNull(ds);
        assertTrue(ds instanceof AppSchemaDataAccess);

        FeatureSource<FeatureType, Feature> mappedSource = ds.getFeatureSource(mappedTypeName);
        assertNotNull(mappedSource);
        
        ds.dispose();
    }

    /**
     * Test method for
     * 'org.geotools.data.complex.AppSchemaDataAccessFactory.createNewDataStore(Map)'
     */
    public void testCreateNewDataStore() throws IOException {
        try {
            factory.createNewDataStore(Collections.EMPTY_MAP);
            fail("unsupported?");
        } catch (UnsupportedOperationException e) {
            // OK
        }
    }

    /**
     * Test method for 'org.geotools.data.complex.AppSchemaDataAccessFactory.getParametersInfo()'
     */
    public void testGetParametersInfo() {
        DataStoreFactorySpi.Param[] params = factory.getParametersInfo();
        assertNotNull(params);
        assertEquals(2, params.length);
        assertEquals(String.class, params[0].type);
        assertEquals(URL.class, params[1].type);
    }

    /**
     * 
     * Test method for 'org.geotools.data.complex.AppSchemaDataAccessFactory.canProcess(Map)'
     */
    public void testCanProcess() {
        Map params = new HashMap();
        assertFalse(factory.canProcess(params));
        params.put("dbtype", "arcsde");
        params.put("url", "http://somesite.net/config.xml");
        assertFalse(factory.canProcess(params));
        params.remove("url");
        params.put("dbtype", "app-schema");
        assertFalse(factory.canProcess(params));

        params.put("url", "http://somesite.net/config.xml");
        assertTrue(factory.canProcess(params));
    }

    /**
     * 
     * Test method for 'org.geotools.data.complex.AppSchemaDataAccessFactory.isAvailable()'
     */
    public void testIsAvailable() {
        assertTrue(factory.isAvailable());
    }

    /**
     * 
     * Test method for
     * 'org.geotools.data.complex.AppSchemaDataAccessFactory.getImplementationHints()'
     */
    public void testGetImplementationHints() {
        assertNotNull(factory.getImplementationHints());
        assertEquals(0, factory.getImplementationHints().size());
    }

}
