/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2021, Open Source Geospatial Foundation (OSGeo)
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

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import javax.xml.namespace.QName;
import org.geotools.data.FeatureSource;
import org.geotools.data.wfs.WFSDataStoreFactory;
import org.geotools.data.wfs.WFSTestData;
import org.geotools.data.wfs.WFSTestData.TestDataType;
import org.geotools.data.wfs.impl.WFSContentDataAccess;
import org.geotools.data.wfs.impl.WFSDataAccessFactory;
import org.geotools.feature.FeatureIterator;
import org.geotools.test.TestData;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.locationtech.jts.geom.Polygon;
import org.opengis.feature.Feature;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.feature.type.Name;
import org.opengis.feature.type.PropertyDescriptor;
import org.opengis.filter.Filter;
import org.opengis.filter.Id;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/** @author Roar Brænden */
public class KartverketStedsnavnDataStoreOnlineTest extends AbstractWfsDataStoreOnlineTest {

    static final String SERVER_URL =
            "https://wfs.geonorge.no/skwms1/wfs.stedsnavn?request=GetCapabilities&service=WFS";

    static final String NAME = "Sted";

    static final TestDataType KARTVERKET_STEDSNAVN =
            new TestDataType(
                    "KartverketNo",
                    new QName(
                            "http://skjema.geonorge.no/SOSI/produktspesifikasjon/StedsnavnForVanligBruk/20181115",
                            "sted"),
                    "app_" + NAME,
                    "urn:ogc:def:crs:EPSG::4258");

    static final String defaultGeometryName = "område";

    static final Class<?> geometryType = Polygon.class;

    static final Id fidFilter = ff.id(Collections.singleton(ff.featureId("404676")));

    static final Filter spatialFilter =
            ff.bbox(defaultGeometryName, 68.0, 17.0, 69.0, 18.0, "urn:ogc:def:crs:EPSG::4258");

    public KartverketStedsnavnDataStoreOnlineTest() {
        super(
                SERVER_URL,
                KARTVERKET_STEDSNAVN,
                defaultGeometryName,
                geometryType,
                -1,
                fidFilter,
                spatialFilter,
                WFSDataStoreFactory.AXIS_ORDER_COMPLIANT);
    }

    @Override
    protected void setUpParameters(final Map<String, Serializable> params) {
        super.setUpParameters(params);
        params.put(WFSDataStoreFactory.USE_HTTP_CONNECTION_POOLING.key, "False");
    }

    @Override
    @Test
    public void testTypes() throws IOException, NoSuchElementException {
        if (Boolean.FALSE.equals(serviceAvailable)) {
            return;
        }

        String[] types = wfs.getTypeNames();
        List<String> typeNames = Arrays.asList(types);
        Assert.assertTrue(typeNames.contains(testType.FEATURETYPENAME));

        SimpleFeatureType schema = wfs.getSchema(testType.FEATURETYPENAME);
        Assert.assertNotNull(schema);
        GeometryDescriptor geometryDescriptor = schema.getGeometryDescriptor();
        Assert.assertNotNull(geometryDescriptor);
        Assert.assertEquals(defaultGeometryName, geometryDescriptor.getLocalName());
        Assert.assertEquals(geometryType, geometryDescriptor.getType().getBinding());
        CoordinateReferenceSystem crs = geometryDescriptor.getCoordinateReferenceSystem();
        Assert.assertNotNull(crs);
    }

    /**
     * Check that the returned feature doesn't have other property names than those specified when
     * calling getSchema()
     */
    @Test
    public void testComplexSchemaMatches() throws Exception {
        if (Boolean.FALSE.equals(serviceAvailable)) {
            return;
        }

        Map<String, Serializable> params = new HashMap<>();
        params.put(WFSDataStoreFactory.URL.key, new URL(SERVER_URL));
        params.put(WFSDataStoreFactory.PROTOCOL.key, Boolean.FALSE); // Prefer GET to support Schema
        params.put(
                "WFSDataStoreFactory:SCHEMA_CACHE_LOCATION",
                TestData.file(WFSTestData.class, "KartverketNo")
                        .getAbsolutePath()); // Must be specified when Schema is http

        WFSDataAccessFactory dataStoreFactory = new WFSDataAccessFactory();
        WFSContentDataAccess dataAccess =
                (WFSContentDataAccess) dataStoreFactory.createDataStore(params);
        Name featureName = null;
        for (Name nextName : dataAccess.getNames()) {
            if (NAME.equals(nextName.getLocalPart())) {
                featureName = nextName;
                break;
            }
        }
        Assert.assertNotNull(String.format("We should find %s here.", NAME), featureName);

        FeatureSource<FeatureType, Feature> source = dataAccess.getFeatureSource(featureName);
        HashSet<String> properties = new HashSet<>();
        for (PropertyDescriptor desc : source.getSchema().getDescriptors()) {
            properties.add(desc.getName().getLocalPart());
        }

        try (FeatureIterator<Feature> iterator = source.getFeatures().features()) {
            Feature feature = iterator.next();
            for (Property prop : feature.getProperties()) {
                final String name = prop.getName().getLocalPart();
                Assert.assertTrue(
                        "Schema doesn't contain property: " + name, properties.contains(name));
            }
        }
    }

    @Override
    @Test
    @Ignore
    public void testDataStoreHandlesAxisFlipping() {
        // disabled, not implemented for 2.0.0
    }
}
