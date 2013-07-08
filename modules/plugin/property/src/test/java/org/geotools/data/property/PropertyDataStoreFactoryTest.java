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
package org.geotools.data.property;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;

import junit.framework.TestCase;

import org.geotools.data.DataStore;
import org.geotools.data.simple.SimpleFeatureSource;


/**
 * 
 *
 * @source $URL$
 */
public class PropertyDataStoreFactoryTest extends TestCase {
    PropertyDataStoreFactory factory;
    HashMap params;
    
    protected void setUp() throws Exception {
        factory = new PropertyDataStoreFactory();
        factory.setBaseDirectory( new File("src/main/resources").getAbsoluteFile());
        params = new HashMap();
        params.put(PropertyDataStoreFactory.NAMESPACE.key, "http://www.geotools.org/test");
        params.put(PropertyDataStoreFactory.DIRECTORY.key, "./data");
    }

    public void testCanProcess() throws Exception {
        assertFalse(factory.canProcess(Collections.EMPTY_MAP));
        assertTrue(factory.canProcess(params));
    }
    
    public void testCreateDataStore() throws Exception {
        DataStore ds = factory.createDataStore( params );
        assertNotNull( ds );
        SimpleFeatureSource src = ds.getFeatureSource("feature");
        assertTrue(src.getFeatures().size()==4);
    }
    
}
