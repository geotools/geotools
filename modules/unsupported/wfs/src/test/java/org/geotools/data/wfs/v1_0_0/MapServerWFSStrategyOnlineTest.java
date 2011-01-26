/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.wfs.v1_0_0;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.geotools.data.DefaultQuery;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.wfs.WFSDataStoreFactory;
import org.geotools.filter.Filter;
import org.geotools.filter.FilterFactory;
import org.geotools.filter.FilterFactoryFinder;
import org.geotools.filter.FilterType;
import org.geotools.filter.GeometryFilter;

public class MapServerWFSStrategyOnlineTest extends TestCase {
    private static final String TYPE_NAME = "hospitals"; //$NON-NLS-1$

    private WFS_1_0_0_DataStore ds;

    private int totalFeatures;

    protected void setUp() throws Exception {
        URL host=new URL("http://mapserver.refractions.net/cgi-bin/mapserv48?map=/home/www/mapserv/maps/victoria-wms.map&SERVICE=WFS&VERSION=1.0.0&REQUEST=GetCapabilities");
        WFSDataStoreFactory dsfac = new WFSDataStoreFactory();
        Map params = new HashMap();
        params.put(WFSDataStoreFactory.URL.key, host);
        params.put(WFSDataStoreFactory.TIMEOUT.key, 10000);
        params.put(WFSDataStoreFactory.BUFFER_SIZE.key, 100);
        params.put(WFSDataStoreFactory.TRY_GZIP.key, Boolean.TRUE);
        params.put(WFSDataStoreFactory.LENIENT.key, Boolean.TRUE);

        //ds=new WFSDataStore(host, null, null, null, 10000, 100, true, true);
        ds = (WFS_1_0_0_DataStore) dsfac.createDataStore(params);
        assertTrue( ds.strategy instanceof MapServerWFSStrategy );
        FilterFactory fac=FilterFactoryFinder.createFilterFactory();
        
        GeometryFilter filter = fac.createGeometryFilter(FilterType.GEOMETRY_BBOX);
        
        String attName = ds.getSchema(TYPE_NAME).getGeometryDescriptor().getLocalName();
        filter.addLeftGeometry(fac.createAttributeExpression(attName));
        
        filter.addRightGeometry(fac.createBBoxExpression(ds.getBounds(new DefaultQuery(TYPE_NAME))));
        
        Query query=new DefaultQuery(TYPE_NAME, filter);
        
        SimpleFeatureCollection features = ds.getFeatureSource(TYPE_NAME).getFeatures(query);
        
        SimpleFeatureIterator iter = features.features();
        try{
            int count=0;
            while(iter.hasNext()){
                iter.next();
                count++;
            }
            this.totalFeatures=count;
        }finally{
            iter.close();
        }
    }

    public void testFilterNONE() throws Exception {
        SimpleFeatureSource source=ds.getFeatureSource(TYPE_NAME);

        SimpleFeatureCollection reader = source.getFeatures(Query.ALL);
        assertCorrectSize(reader);

        reader = source.getFeatures(Filter.NONE);
        assertCorrectSize(reader);

        reader = source.getFeatures(new DefaultQuery(TYPE_NAME, Filter.NONE));
        assertCorrectSize(reader);
}

    private void assertCorrectSize( SimpleFeatureCollection collection ) throws Exception{
        SimpleFeatureIterator iter = collection.features();
        
        try{
            int count=0;
            while( iter.hasNext() ){
                count++;
                iter.next();
            }
            assertEquals( this.totalFeatures, count );
            
        }finally{
            iter.close();
        }
    }

}
