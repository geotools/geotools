/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2025, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.dggs.h3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assume.assumeFalse;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.geotools.api.data.DataStore;
import org.geotools.api.data.Query;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.dggs.datastore.DGGSDataStore;
import org.geotools.dggs.datastore.DGGSStoreFactory;
import org.geotools.dggs.gstore.DGGSFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.junit.Before;
import org.junit.Test;

public class H3DGGSDelegateDatastoreTest extends H3DGGSInstanceTest {

    /** Flag to detect Windows OS */
    private static final boolean IS_WINDOWS =
            System.getProperty("os.name").toLowerCase().contains("windows");

    private static final FilterFactory FF = CommonFactoryFinder.getFilterFactory();

    @Before
    public void setUp() {
        // Skip test on Windows due to path handling differences
        assumeFalse("Test disabled on Windows platform", IS_WINDOWS);
    }

    @Test
    public void testDelegateDatastore() throws IOException, URISyntaxException {
        DGGSStoreFactory factory = new DGGSStoreFactory();
        URL resource = getClass().getResource("/org/geotools/dggs/h3/localsample.parquet");
        File file = new File(resource.toURI());
        String dataUri = file.toURI().toASCIIString();

        Map<String, Object> params = new HashMap<>();
        // DGGSDatastore params
        params.put(DGGSStoreFactory.ZONE_ID_COLUMN_NAME.key, "h3indexstr");
        params.put(DGGSStoreFactory.RESOLUTION.key, "10");
        params.put(DGGSStoreFactory.DGGS_FACTORY_ID.key, "H3");

        // Delegate DataStore params
        params.put(DGGSStoreFactory.DELEGATE_PREFIX + "dbtype", "geoparquet");
        params.put(DGGSStoreFactory.DELEGATE_PREFIX + "uri", dataUri);
        DGGSDataStore<?> datastore = null;
        try {
            datastore = (DGGSDataStore<?>) factory.createDataStore(params);
            DataStore delegateDatastore = datastore.getDelegate();
            assertEquals(
                    "class org.geotools.data.geoparquet.GeoparquetDataStore",
                    delegateDatastore.getClass().toString());
            String[] typeNames = datastore.getTypeNames();
            assertEquals(1, typeNames.length);
            String typeName = typeNames[0];
            assertEquals("localsample", typeName);
            DGGSFeatureSource<?> source = datastore.getFeatureSource(typeName);
            int count = source.getFeatures().size();
            assertEquals(548, count);
            Filter filter = FF.greater(FF.property("landcover"), FF.literal("20"));
            Query landcoverQuery = new Query(typeName, filter);
            SimpleFeatureCollection above20 = source.getFeatures(landcoverQuery);
            assertEquals(25, above20.size());

        } finally {
            if (datastore != null) {
                datastore.dispose();
            }
        }
    }

    @Test
    public void testDelegateDatastorePrimaryKeyLongIndex() throws IOException, URISyntaxException {
        DGGSStoreFactory factory = new DGGSStoreFactory();
        URL resource = getClass().getResource("/org/geotools/dggs/h3/local_no_id.parquet");
        File file = new File(resource.toURI());
        String dataUri = file.toURI().toASCIIString();

        Map<String, Object> params = new HashMap<>();
        // DGGSDatastore params
        params.put(DGGSStoreFactory.ZONE_ID_COLUMN_NAME.key, "cell_h3index");
        params.put(DGGSStoreFactory.RESOLUTION.key, "10");
        params.put(DGGSStoreFactory.DGGS_FACTORY_ID.key, "H3");

        // Delegate DataStore params
        params.put(DGGSStoreFactory.DELEGATE_PREFIX + "dbtype", "geoparquet");
        params.put(DGGSStoreFactory.DELEGATE_PREFIX + "primary_key_id", "fire_id");
        params.put(DGGSStoreFactory.DELEGATE_PREFIX + "uri", dataUri);
        DGGSDataStore<?> datastore = null;
        try {
            datastore = (DGGSDataStore<?>) factory.createDataStore(params);
            DataStore delegateDatastore = datastore.getDelegate();
            assertEquals(
                    "class org.geotools.data.geoparquet.GeoparquetDataStore",
                    delegateDatastore.getClass().toString());
            String[] typeNames = datastore.getTypeNames();
            assertEquals(1, typeNames.length);
            String typeName = typeNames[0];
            assertEquals("local_no_id", typeName);
            DGGSFeatureSource<?> source = datastore.getFeatureSource(typeName);
            int count = source.getFeatures().size();
            assertEquals(42, count);
            Filter filter = FF.greater(FF.property("fire_area_ha"), FF.literal("20.5"));
            Query fireAreaQuery = new Query(typeName, filter);
            SimpleFeatureCollection collection = source.getFeatures(fireAreaQuery);
            assertEquals(23, collection.size());

        } finally {
            if (datastore != null) {
                datastore.dispose();
            }
        }
    }
}
