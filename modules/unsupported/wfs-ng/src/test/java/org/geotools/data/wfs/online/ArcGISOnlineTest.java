/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2016, Open Source Geospatial Foundation (OSGeo)
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

import java.io.*;
import java.net.URL;
import java.util.*;
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
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/** @source $URL$ */
public class ArcGISOnlineTest {

    public static final String SERVER_URL =
            "https://services.slip.wa.gov.au/public/services/SLIP_Public_Services/Farming_WFS/MapServer/WFSServer";

    public static final String SERVER_URL_110 =
            String.format("%s?VERSION=1.1.0&SERVICE=WFS&REQUEST=GetCapabilities", SERVER_URL);

    public static final String WFS_ARCGIS_STRATEGY = "arcgis";

    private URL url_110;

    private WFSDataStore wfs110;

    private WFSDataStore wfs110_with_get;

    @Before
    public void setUp() throws Exception {
        url_110 = new URL(SERVER_URL_110);

        try {
            Map<String, Serializable> params;
            params = new HashMap<String, Serializable>();
            params.put(WFSDataStoreFactory.URL.key, url_110);
            params.put(WFSDataStoreFactory.WFS_STRATEGY.key, WFS_ARCGIS_STRATEGY);
            params.put(WFSDataStoreFactory.GML_COMPATIBLE_TYPENAMES.key, Boolean.TRUE);
            params.put(WFSDataStoreFactory.PROTOCOL.key, Boolean.TRUE);
            wfs110 = new WFSDataStoreFactory().createDataStore(params);
            wfs110_with_get = new WFSDataStoreFactory().createDataStore(params);
        } catch (Exception e) {
            System.err.println("Server is not available. test disabled ");
            url_110 = null;
        }
    }

    @After
    public void tearDown() throws Exception {
        if (url_110 != null) {
            wfs110.dispose();
        }
    }

    @Test
    public void testSingleType_WFS_1_1() throws Exception {
        testSingleType(wfs110);
    }

    public void testSingleType(DataStore wfs) throws Exception {

        String typeName =
                "SLIP_Public_Services_Farming_WFS_Land_Capability_-_Vineyards__DPIRD-034_";

        //
        // "SLIP_Public_Services_Farming_WFS:Land_Capability_-_Vineyards__DPIRD-034_";
        WFSOnlineTestSupport.doFeatureType(wfs, typeName);

        SimpleFeatureSource source = wfs.getFeatureSource(typeName);
        GeometryDescriptor geometryDesc = wfs.getSchema(typeName).getGeometryDescriptor();
        CoordinateReferenceSystem crs = geometryDesc.getCoordinateReferenceSystem();
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(GeoTools.getDefaultHints());
        Filter filter =
                ff.bbox(
                        ff.property(geometryDesc.getLocalName()),
                        new ReferencedEnvelope(116.7085, 117.1355, -33.6462, -33.371, crs));
        Query query = new Query(typeName);
        query.setFilter(filter);
        query.setPropertyNames(
                new String[] {source.getSchema().getType(0).getName().getLocalPart()});
        SimpleFeatureCollection features = source.getFeatures(query);
        /*
                   FeatureReader<SimpleFeatureType, SimpleFeature> featureReader =
                           wfs.getFeatureReader(query, Transaction.AUTO_COMMIT);
                   assertNotNull(featureReader);
        */
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
