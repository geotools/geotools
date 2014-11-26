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
package org.geotools.data.wfs.online;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.geotools.data.DataStore;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.wfs.WFSDataStore;
import org.geotools.data.wfs.WFSDataStoreFactory;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.GeoTools;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * 
 * 
 * @source $URL$
 */
public class MapServerOnlineTest {

    public static final String SERVER_URL_100 = "http://demo.mapserver.org/cgi-bin/wfs?SERVICE=WFS&VERSION=1.0.0&REQUEST=GetCapabilities";

    public static final String SERVER_URL_110 = "http://demo.mapserver.org/cgi-bin/wfs?SERVICE=WFS&VERSION=1.1.0&REQUEST=GetCapabilities";
   
    private URL url_100;

    private URL url_110;

    private WFSDataStore wfs100;

    private WFSDataStore wfs110;

    @Before
    public void setUp() throws Exception {
        url_100 = new URL(SERVER_URL_100);
        url_110 = new URL(SERVER_URL_110);
        if (url_100 != null) {
            try {
                Map<String, Serializable> params;
                params = new HashMap<String, Serializable>();
                params.put(WFSDataStoreFactory.URL.key, url_100);
                params.put(WFSDataStoreFactory.WFS_STRATEGY.key, "mapserver");
                wfs100 = new WFSDataStoreFactory().createDataStore(params);

                params = new HashMap<String, Serializable>();
                params.put(WFSDataStoreFactory.URL.key, url_110);
                params.put(WFSDataStoreFactory.WFS_STRATEGY.key, "mapserver");
                wfs110 = new WFSDataStoreFactory().createDataStore(params);

                assertEquals("1.0.0", wfs100.getInfo().getVersion());
                assertEquals("1.1.0", wfs110.getInfo().getVersion());
            }
            catch(Exception e) {
                System.err.println("Server is not available. test disabled ");
                url_100 = null;
            }
        }
    }

    @After
    public void tearDown() throws Exception {
        if (url_100 != null) {
            wfs100.dispose();
            wfs110.dispose();
        }
    }

    @Test
    public void testSingleType_WFS_1_0() throws IOException, NoSuchElementException {
        testSingleType(wfs100);
    }

    @Test
    public void testSingleType_WFS_1_1() throws IOException, NoSuchElementException {
        testSingleType(wfs110);
    }

    private void testSingleType(DataStore wfs) throws IOException, NoSuchElementException {
        if (url_100 == null)
            return;

        String typeName = "ms_cities";
        SimpleFeatureType type = wfs.getSchema(typeName);
        type.getTypeName();
        type.getName().getNamespaceURI();
        assertEquals(typeName, type.getName().getLocalPart());

        SimpleFeatureSource source = wfs.getFeatureSource(typeName);
        source.getBounds();

        SimpleFeatureCollection features = source.getFeatures();
        features.getBounds();
        features.getSchema();
        
        GeometryDescriptor geometryDesc = wfs.getSchema(typeName).getGeometryDescriptor();
        CoordinateReferenceSystem crs = geometryDesc.getCoordinateReferenceSystem();

        ReferencedEnvelope env = new ReferencedEnvelope(-59, -58, -35, -34, crs);
          
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(GeoTools.getDefaultHints());        
        Filter filter = ff.bbox(ff.property("msGeometry"), env);
        Query query = new Query();
        query.setTypeName(typeName);
        query.setFilter(filter);
        
        features = source.getFeatures(query);
        
        int size = features.size();
        assertEquals(2, size);

        SimpleFeatureIterator iterator = features.features();
        try {
            while (iterator.hasNext()) {
                SimpleFeature feature = iterator.next();
                System.out.println(feature.getID());
            }
        } finally {
            iterator.close();
        }
    }
}
