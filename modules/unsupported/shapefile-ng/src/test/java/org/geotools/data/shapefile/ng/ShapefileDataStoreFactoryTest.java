/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2012, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.shapefile.ng;

import java.io.IOException;
import java.net.URL;

import org.geotools.TestData;
import org.geotools.util.KVP;
import org.junit.Test;

import static org.geotools.data.shapefile.ng.ShapefileDataStoreFactory.*;

/**
 * Test the functionality of ShapefileDataStoreFactory; specifically the handling of 
 * connection parameters.
 *
 * @author Jody Garnett
 */
public class ShapefileDataStoreFactoryTest extends TestCaseSupport {

    private ShapefileDataStore store = null;
    private ShapefileDataStoreFactory factory = new ShapefileDataStoreFactory();
    
    public ShapefileDataStoreFactoryTest(String testName) throws IOException {
        super(testName);
    }
    @Override
    protected void tearDown() throws Exception {
        if(store != null) {
                store.dispose();
        }
        super.tearDown();
    }
    
    @Test
    public void testFSTypeParameter() throws Exception {
        URL url = TestData.url(STATE_POP);
        
        KVP params = new KVP( URLP.key,url );
        
        assertTrue( "Sorting is optional", factory.canProcess(params) );
        
        params.put( FSTYPE.key, "shape-ng" );
        assertTrue( "Shape NG supported", factory.canProcess(params) );
        
        params.put(FSTYPE.key, "shape" );
        assertFalse( "Plain shape not supported", factory.canProcess(params) );
        
        params.put(FSTYPE.key, "index" );
        assertFalse( "Plain index not supported", factory.canProcess(params) );
    }

}
