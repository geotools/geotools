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
package org.geotools.data.shapefile;

import java.io.IOException;
import java.net.URL;

import org.geotools.TestData;
import org.geotools.data.DataStore;
import org.geotools.data.QueryCapabilities;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.util.KVP;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.sort.SortBy;
import org.opengis.filter.sort.SortOrder;

import static org.geotools.data.shapefile.ShapefileDataStoreFactory.*;

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
        
        assertTrue( "FST is optional", factory.canProcess(params) );
        
        params.put(FSTYPE.key, "shape" );
        assertTrue( "Plain shape implementation provided", factory.canProcess(params) );
        
        params.put(FSTYPE.key, "index" );
        assertTrue( "Index shape implementation provided", factory.canProcess(params) );
        
        params.put( FSTYPE.key, "shape-ng" );
        assertFalse( "Shape NG sorting not supported", factory.canProcess(params) );
        
        params.put( FSTYPE.key, "smurf" );
        assertFalse( "Feeling blue; don't try a smruf", factory.canProcess(params) );
    }

    @Test
    public void testQueryCapabilities() throws Exception {
        URL url = TestData.url(STATE_POP);
        
        KVP params = new KVP( URLP.key,url );
        DataStore dataStore = factory.createDataStore( params );
        Name typeName = dataStore.getNames().get(0);
        SimpleFeatureSource featureSource = dataStore.getFeatureSource( typeName);
        
        QueryCapabilities caps = featureSource.getQueryCapabilities();
        
        SortBy[] sortBy = new SortBy[]{SortBy.NATURAL_ORDER,};
        assertTrue( "Natural", caps.supportsSorting( sortBy ));
        
        SimpleFeatureType schema = featureSource.getSchema();
        String attr = schema.getDescriptor(1).getLocalName();
        
        sortBy[0] = ff.sort( attr, SortOrder.ASCENDING );
        assertFalse( "Cannot sort "+attr, caps.supportsSorting( sortBy ));
        
        sortBy[0] = ff.sort( "the_geom", SortOrder.ASCENDING );
        assertFalse( "Cannot sort the_geom", caps.supportsSorting( sortBy ));
    }
}
