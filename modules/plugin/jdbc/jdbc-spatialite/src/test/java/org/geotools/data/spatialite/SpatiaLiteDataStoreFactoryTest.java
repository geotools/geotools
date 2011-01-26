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
package org.geotools.data.spatialite;

import java.util.Collections;
import java.util.HashMap;

import junit.framework.TestCase;

import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCDataStoreFactory;

public class SpatiaLiteDataStoreFactoryTest extends TestCase {

    SpatiaLiteDataStoreFactory factory;
    HashMap params;
    
    protected void setUp() throws Exception {
        factory = new SpatiaLiteDataStoreFactory();
        params = new HashMap();
        params.put(JDBCDataStoreFactory.NAMESPACE.key, "http://www.geotools.org/test");
        params.put(JDBCDataStoreFactory.DATABASE.key, "geotools");
        params.put(JDBCDataStoreFactory.DBTYPE.key, "spatialite");
    }

    public void testCanProcess() throws Exception {
        assertFalse(factory.canProcess(Collections.EMPTY_MAP));
        assertTrue(factory.canProcess(params));
    }
    
    public void testCreateDataStore() throws Exception {
        JDBCDataStore ds = factory.createDataStore( params );
        assertNotNull( ds );
    }
}
