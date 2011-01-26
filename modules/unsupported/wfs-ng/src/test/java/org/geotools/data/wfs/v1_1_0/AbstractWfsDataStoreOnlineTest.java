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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.data.DefaultQuery;
import org.geotools.data.Query;
import org.geotools.data.ResourceInfo;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.wfs.WFSDataStore;
import org.geotools.data.wfs.WFSDataStoreFactory;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.util.logging.Logging;
import org.junit.Before;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.Id;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public abstract class AbstractWfsDataStoreOnlineTest  {

    private static final Logger LOGGER = Logging.getLogger("org.geotools.wfs.v_1_1_0.data.test");

    protected static final FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);

    private final String SERVER_URL;

    protected static Boolean serviceAvailable = null;

    /**
     * The DataStore under test, static so we create it only once
     */
    protected static WFSDataStore wfs = null;

    protected final DataTestSupport.TestDataType testType;

    protected final String defaultGeometryName;

    private final int featureCount;

    /**
     * The filter used for the count test, may be null if
     * {@link #testFeatureSourceGetCountFilter()} is not expected to be run
     */
    private final Id fidFilter;

    private final Class geometryType;

    public AbstractWfsDataStoreOnlineTest(String serverURL, DataTestSupport.TestDataType testType,
            String defaultGeometryName, Class geometryType, int featureCount, Id fidFilter) {
        this.SERVER_URL = serverURL;
        this.testType = testType;
        this.defaultGeometryName = defaultGeometryName;
        this.featureCount = featureCount;
        this.fidFilter = fidFilter;
        this.geometryType = geometryType;
    }

    @Before
    public void setUp() throws IOException {
        if (serviceAvailable == null) {
            // check for service availability only once
            URL url = new URL(SERVER_URL);
            serviceAvailable = Boolean.FALSE;
            InputStream stream = null;
            try {
                stream = url.openStream();
                serviceAvailable = Boolean.TRUE;
            } catch (Throwable t) {
                LOGGER.log(Level.WARNING, "The test server is not available: " + SERVER_URL + ". "
                        + getClass().getSimpleName() + " test disabled ");
                url = null;
                return;
            } finally {
                if (stream != null)
                    try {
                        stream.close();
                    } catch (IOException e) {
                        // whatever
                    }
            }
        }

        if (wfs == null && serviceAvailable.booleanValue()) {
            LOGGER.info("Creating test datastore for " + SERVER_URL);

            Map<String, Serializable> params = new HashMap<String, Serializable>();
            params.put(WFSDataStoreFactory.URL.key, SERVER_URL);
            params.put("USE_PULL_PARSER", Boolean.TRUE);
            WFSDataStoreFactory dataStoreFactory = new WFSDataStoreFactory();
            wfs = dataStoreFactory.createDataStore(params);
            LOGGER.fine("WFS datastore created");
        }
    }

    @Test
    public void testTypes() throws IOException, NoSuchElementException {
        if (Boolean.FALSE.equals(serviceAvailable)) {
            return;
        }

        assertTrue(wfs instanceof WFS_1_1_0_DataStore);

        String types[] = wfs.getTypeNames();
        List<String> typeNames = Arrays.asList(types);
        assertTrue(typeNames.contains(testType.FEATURETYPENAME));

        SimpleFeatureType schema = wfs.getSchema(testType.FEATURETYPENAME);
        assertNotNull(schema);
        GeometryDescriptor geometryDescriptor = schema.getGeometryDescriptor();
        assertNotNull(geometryDescriptor);
        assertEquals(defaultGeometryName, geometryDescriptor.getLocalName());
        assertEquals(geometryType, geometryDescriptor.getType().getBinding());
        CoordinateReferenceSystem crs = geometryDescriptor.getCoordinateReferenceSystem();
        assertNotNull(crs);
    }

    @Test
    public void testFeatureSourceInfo() throws IOException, NoSuchAuthorityCodeException,
            FactoryException {
        if (Boolean.FALSE.equals(serviceAvailable)) {
            return;
        }

        SimpleFeatureSource featureSource;
        featureSource = wfs.getFeatureSource(testType.FEATURETYPENAME);

        assertNotNull(featureSource);

        ResourceInfo info = featureSource.getInfo();
        assertNotNull(info.getCRS());

        ReferencedEnvelope bounds = info.getBounds();
        assertSame(info.getCRS(), bounds.getCoordinateReferenceSystem());

        assertEquals(testType.FEATURETYPENAME, info.getName());
    }

    @Test
    public void testFeatureSourceGetBounds() throws IOException, NoSuchAuthorityCodeException,
            FactoryException {
        if (Boolean.FALSE.equals(serviceAvailable)) {
            return;
        }

        SimpleFeatureSource featureSource;
        featureSource = wfs.getFeatureSource(testType.FEATURETYPENAME);
        assertNotNull(featureSource);

        ReferencedEnvelope infoBounds = featureSource.getInfo().getBounds();
        ReferencedEnvelope bounds = featureSource.getBounds();

        assertEquals(infoBounds, bounds);

        DefaultQuery query = new DefaultQuery(featureSource.getInfo().getName());
        CoordinateReferenceSystem queryCrs = CRS.decode("EPSG:23030");
        query.setCoordinateSystem(queryCrs);

        bounds = featureSource.getBounds(query);
        assertNotNull(bounds);
        assertSame("the bounds were not reprojected", queryCrs, bounds
                .getCoordinateReferenceSystem());

        final String geometryName = featureSource.getSchema().getGeometryDescriptor()
                .getLocalName();
        query.setFilter(ff.bbox(geometryName, -180, -90, 180, 90, "EPSG:4326"));

        bounds = featureSource.getBounds(query);
        assertNull(bounds); // too expensive to calculate
    }

    @Test
    public void testFeatureSourceGetCountAll() throws IOException {
        if (Boolean.FALSE.equals(serviceAvailable)) {
            return;
        }

        SimpleFeatureSource featureSource;
        featureSource = wfs.getFeatureSource(testType.FEATURETYPENAME);
        assertNotNull(featureSource);

        assertEquals(featureCount, featureSource.getCount(Query.ALL));
    }

    /**
     * Performs a FeatureSource.getCount(Query) with the constructor provided
     * fid filter if the filter is not null.
     * 
     * @throws IOException
     */
    @Test
    public void testFeatureSourceGetCountFilter() throws IOException {
        if (Boolean.FALSE.equals(serviceAvailable)) {
            return;
        }
        if (fidFilter == null) {
            LOGGER.info("Ignoring testFeatureSourceGetCountFilter "
                    + "since the subclass didn't provide a fid filter");
            return;
        }

        SimpleFeatureSource featureSource;
        featureSource = wfs.getFeatureSource(testType.FEATURETYPENAME);
        assertNotNull(featureSource);

        DefaultQuery query = new DefaultQuery(featureSource.getInfo().getName());
        query.setFilter(fidFilter);

        assertEquals(1, featureSource.getCount(query));
    }

    @Test
    public void testFeatureSourceGetFeatures() throws IOException {
        if (Boolean.FALSE.equals(serviceAvailable)) {
            return;
        }
        if (fidFilter == null) {
            LOGGER.info("Ignoring testFeatureSourceGetCountFilter "
                    + "since the subclass didn't provide a fid filter");
            return;
        }

        SimpleFeatureSource featureSource;
        featureSource = wfs.getFeatureSource(testType.FEATURETYPENAME);
        assertNotNull(featureSource);

        SimpleFeatureCollection features;
        features = featureSource.getFeatures();
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
        } finally {
            iterator.close();
        }
    }
}
