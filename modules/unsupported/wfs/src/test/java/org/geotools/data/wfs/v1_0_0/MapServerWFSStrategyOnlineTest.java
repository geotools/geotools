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

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.wfs.WFSDataStoreFactory;
import org.geotools.factory.CommonFactoryFinder;
import org.opengis.filter.Filter;
import org.geotools.filter.FilterType;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.xml.XMLHandlerHints;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.spatial.BBOX;

/**
 * 
 *
 * @source $URL$
 */
public class MapServerWFSStrategyOnlineTest extends TestCase {

    protected void setUp() throws Exception {
    }

    public void testWfsStrategyOverride() throws Exception {
        String getCapabilities = "http://212.0.113.22:8080/cgi-bin/mywfs?WIDTH=512&SERVICE=WFS&VERSION=1.0.0&REQUEST=GetCapabilities";
        
        Map connectionParameters = new HashMap();
        connectionParameters.put("WFSDataStoreFactory:GET_CAPABILITIES_URL", getCapabilities );
        connectionParameters.put("WFSDataStoreFactory:WFS_STRATEGY", "mapserver" );
        connectionParameters.put("WFSDataStoreFactory:FILTER_COMPLIANCE", XMLHandlerHints.VALUE_FILTER_COMPLIANCE_HIGH );

        // Step 2 - connection
        DataStore data = DataStoreFinder.getDataStore( connectionParameters );
        WFS_1_0_0_DataStore wfs = (WFS_1_0_0_DataStore) data;
        System.out.println(wfs.strategy.getClass());
        assertTrue(wfs.strategy instanceof MapServerWFSStrategy);
    }
    
    public void XtestFilterNONE() throws Exception {
        final String TYPE_NAME = "hospitals"; //$NON-NLS-1$

        WFS_1_0_0_DataStore ds;

        int totalFeatures;

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
        
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
        
        String attName = ds.getSchema(TYPE_NAME).getGeometryDescriptor().getLocalName();
        ReferencedEnvelope bounds = ds.getBounds(new Query(TYPE_NAME));
        
        BBOX filter = ff.bbox(ff.property(attName),bounds);
        
        
        
        Query query=new Query(TYPE_NAME, filter);
        
        SimpleFeatureCollection features = ds.getFeatureSource(TYPE_NAME).getFeatures(query);
        
        SimpleFeatureIterator iter = features.features();
        try{
            int count=0;
            while(iter.hasNext()){
                iter.next();
                count++;
            }
            totalFeatures=count;
        }finally{
            iter.close();
        }

        SimpleFeatureSource source=ds.getFeatureSource(TYPE_NAME);

        SimpleFeatureCollection reader = source.getFeatures(Query.ALL);
        assertCorrectSize(reader, totalFeatures);

        reader = source.getFeatures(Filter.INCLUDE);
        assertCorrectSize(reader, totalFeatures);

        reader = source.getFeatures(new Query(TYPE_NAME, Filter.INCLUDE));
        assertCorrectSize(reader, totalFeatures);
}

    private void assertCorrectSize( SimpleFeatureCollection collection, Object totalFeatures ) throws Exception{
        SimpleFeatureIterator iter = collection.features();
        
        try{
            int count=0;
            while( iter.hasNext() ){
                count++;
                iter.next();
            }
            assertEquals( totalFeatures, count );
            
        }finally{
            iter.close();
        }
    }

}
