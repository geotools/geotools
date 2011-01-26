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
package org.geotools.data.dir;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

public class DirectoryDataStoreFactoryTest extends TestCase {
    
    public void testStringListParam() throws IOException {
        DirectoryDataStoreFactory factory = new DirectoryDataStoreFactory();
        Map params = new HashMap();
        params.put(DirectoryDataStoreFactory.DIRECTORY.key, getClass().getResource("test-data/test1"));
        params.put(DirectoryDataStoreFactory.CREATE_SUFFIX_ORDER.key, "shp mif");
        factory.createDataStore(params);
    }
} 
