/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2005, David Zwiers
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
package org.geotools.data.wfs.v1_1_0;

import static org.geotools.data.wfs.v1_1_0.DataTestSupport.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.geotools.data.FeatureReader;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.wfs.WFSDataStore;
import org.geotools.data.wfs.WFSDataStoreFactory;
import org.geotools.data.wfs.protocol.http.HTTPResponse;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.Hints;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;

/**
 * 
 *
 * @source $URL$
 */
public class GeoServerOnlineTest extends AbstractWfsDataStoreOnlineTest {

    public static final String SERVER_URL = "http://localhost:8080/geoserver/wfs?service=WFS&request=GetCapabilities&version=1.1.0"; //$NON-NLS-1$

    public GeoServerOnlineTest() {
        super(SERVER_URL, GEOS_STATES, "the_geom", MultiPolygon.class, 49, ff.id(Collections
                .singleton(ff.featureId("states.1"))));
    }

    @Test
    public void testFeatureSourceGetFeaturesFilter() throws IOException {
        if (Boolean.FALSE.equals(serviceAvailable)) {
            return;
        }

        SimpleFeatureSource featureSource;
        featureSource = wfs.getFeatureSource(testType.FEATURETYPENAME);
        assertNotNull(featureSource);

        Query query = new Query(testType.FEATURETYPENAME);

        GeometryFactory gf = new GeometryFactory();
        //GEOT-2283: use lat/lon coordinate order, this is a wfs 1.1 instance
        Coordinate[] coordinates = { new Coordinate(39, -107), new Coordinate(38, -107),
                new Coordinate(38, -104), new Coordinate(39, -104), new Coordinate(39, -107) };
        LinearRing shell = gf.createLinearRing(coordinates);
        Polygon polygon = gf.createPolygon(shell, null);
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
        Filter filter = ff.intersects(ff.property(defaultGeometryName), ff.literal(polygon));
        // System.out.println(filter);
        query.setFilter(filter);

        SimpleFeatureCollection features;
        features = featureSource.getFeatures(query);
        assertNotNull(features);

        SimpleFeatureType schema = features.getSchema();
        assertNotNull(schema);

        SimpleFeatureIterator iterator = features.features();
        assertNotNull(iterator);
        try {
            assertTrue(iterator.hasNext());
            SimpleFeature next = iterator.next();
            assertNotNull(next);
            assertNotNull(next.getDefaultGeometry());
            assertFalse(iterator.hasNext());
        } finally {
            iterator.close();
        }
    }
    
    @Test
    public void testVendorParametersGet() throws Exception {
        testVendorParameters(Boolean.FALSE);
    }
    
    @Test
    public void testVendorParametersPost() throws Exception {
        testVendorParameters(Boolean.TRUE);
    }

    private void testVendorParameters(Boolean usePost) throws IOException {
        if (Boolean.FALSE.equals(serviceAvailable)) {
            return;
        }
        
        // we alter the data store, thus we cannot use the static "wfs" field, we need to create a new one
        Map<String, Serializable> params = new HashMap<String, Serializable>();
        params.put(WFSDataStoreFactory.URL.key, SERVER_URL);
        params.put(WFSDataStoreFactory.PROTOCOL.key, usePost);
        params.put("USE_PULL_PARSER", Boolean.TRUE);
        WFSDataStoreFactory dataStoreFactory = new WFSDataStoreFactory();
        WFSDataStore wfs = dataStoreFactory.createDataStore(params);
        
        final WFS_1_1_0_Protocol originalHandler = (WFS_1_1_0_Protocol) ((WFS_1_1_0_DataStore) wfs).wfs;
        originalHandler.http = new HttpProtocolWrapper(originalHandler.http) {
            
            @Override
            public HTTPResponse issueGet(URL baseUrl, Map<String, String> kvp) throws IOException {
                // check the vendor params actually made it into the url (at this stage they are not url encoded)
                assertEquals("true", kvp.get("strict"));
                assertEquals("mysecret", kvp.get("authkey"));
                assertEquals("low:2000000;high:5000000", kvp.get("viewparams"));
                
                return super.issueGet(baseUrl, kvp);
            }
            
            @Override
            public HTTPResponse issuePost(URL targetUrl, POSTCallBack callback) throws IOException {
                String[] keyValueArray = targetUrl.getQuery().split("&");
                Map<String, String> kvp = new HashMap<String, String>();
                for (String keyValue : keyValueArray) {
                    String[] skv = keyValue.split("=");
                    kvp.put(skv[0], skv[1]);
                }
                
                // check the vendor params actually made it into the url
                assertEquals("true", kvp.get("strict"));
                assertEquals("mysecret", kvp.get("authkey"));
                assertEquals("low%3A2000000%3Bhigh%3A5000000", kvp.get("viewparams"));
                
                return super.issuePost(targetUrl, callback);
            }

        };    
        

        Map<String, String> vparams = new HashMap<String, String>();
        vparams.put("authkey", "mysecret");
        vparams.put("viewparams", "low:2000000;high:5000000");
        vparams.put("strict", "true");
        Hints hints = new Hints(WFSDataStore.WFS_VENDOR_PARAMETERS, vparams);
        Query q = new Query("topp:states");
        q.setHints(hints);
        
        // read some features, check
        FeatureReader fr = wfs.getFeatureReader(q, Transaction.AUTO_COMMIT);
        assertTrue(fr.hasNext());
        fr.close();
    }
   

}
