/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.mysql;

import java.util.Map;
import java.util.Properties;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.geotools.data.DataSourceException;
import org.geotools.data.DataStore;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * Test Params used by PostgisDataStoreFactory.
 *
 * @author jgarnett, Refractions Research, Inc.
 * @author $Author: aaime $ (last modification)
 * @source $URL$
 * @version $Id$
 */
public class MySQLDataStoreFactoryTest extends TestCase {
    static MySQLDataStoreFactory factory = new MySQLDataStoreFactory();
    Map local;

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(MySQLDataStoreFactoryTest.class);

        return suite;
    }

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        Properties resource = new Properties();
        resource.load(this.getClass().getResourceAsStream("fixture.properties"));
        this.local = resource;
    }

    public void testLocal() throws Exception {
        assertTrue("canProcess", factory.canProcess(local));

        try {
            DataStore temp = factory.createDataStore(local);
            assertNotNull("created", temp);
        } catch (DataSourceException expected) {
            expected.printStackTrace();
            assertEquals("Could not get connection", expected.getMessage());
        }
    }
    
    public void testNamespace() throws Exception {
        DataStore ds = factory.createDataStore(local);
        SimpleFeatureType ft = ds.getSchema("road");
        assertEquals(local.get("namespace"), ft.getName().getNamespaceURI());
    }
}
