/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008-2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.wfs.integration.v1_1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import org.geotools.data.DataStore;
import org.geotools.data.DataUtilities;
import org.geotools.data.ResourceInfo;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.data.wfs.WFSDataStore;
import org.geotools.data.wfs.WFSTestData;
import org.geotools.data.wfs.integration.AbstractIntegrationTest;
import org.geotools.data.wfs.integration.IntegrationTestWFSClient;
import org.geotools.data.wfs.internal.WFSClient;
import org.geotools.data.wfs.internal.WFSConfig;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.referencing.CRS;
import org.junit.Ignore;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;

public class GeoServerIntegrationTest extends AbstractIntegrationTest {

    protected WFSClient wfs;

    @Override
    public DataStore createDataStore() throws Exception {
        wfs = mockUpWfsClient();

        WFSDataStore wfsds = new WFSDataStore(wfs);
        return wfsds;
    }

    private WFSClient mockUpWfsClient() throws Exception {
        WFSConfig config = WFSTestData.getGmlCompatibleConfig();
        String baseDirectory = "GeoServer_2.0/1.1.0/";

        return new IntegrationTestWFSClient(baseDirectory, config);
    }

    @Override
    public DataStore tearDownDataStore(DataStore data) throws Exception {
        data.dispose();
        return data;
    }

    @Override
    protected TestDataType createFirstType() throws Exception {
        TestDataType testDataType = new TestDataType();

        testDataType.featureType =
                DataUtilities.createType(
                        "roadsType", "the_geom:MultiLineString,cat:java.lang.Long,label:String");
        testDataType.featureType =
                DataUtilities.createSubType(
                        testDataType.featureType,
                        null,
                        CRS.decode("urn:x-ogc:def:crs:EPSG:6.11.2:26713"));

        testDataType.stringAttribute = "label";
        testDataType.numberOfFeatures = 3;
        testDataType.typeName = "sf_roads";
        testDataType.newFeature =
                SimpleFeatureBuilder.build(
                        testDataType.featureType,
                        new Object[] {
                            new GeometryFactory()
                                    .createLineString(
                                            new Coordinate[] {
                                                new Coordinate(1, 2), new Coordinate(2, 3)
                                            }),
                            new Integer(4),
                            "somekindofroad"
                        },
                        "roads.4");

        return testDataType;
    }

    @Override
    protected TestDataType createSecondType() throws Exception {
        TestDataType testDataType = new TestDataType();

        testDataType.featureType =
                DataUtilities.createType(
                        "archsitesType", "the_geom:Point,cat:java.lang.Long,str1:String");
        testDataType.featureType =
                DataUtilities.createSubType(
                        testDataType.featureType, null, CRS.decode("EPSG:26713", true));

        testDataType.stringAttribute = "str1";
        testDataType.numberOfFeatures = 3;
        testDataType.typeName = "sf_archsites";
        return testDataType;
    }

    @Test
    public void testInfo() throws IOException {
        SimpleFeatureStore store1 = (SimpleFeatureStore) data.getFeatureSource(first.typeName);

        ResourceInfo info = store1.getInfo();

        assertEquals("sf_roads", info.getName());
        assertEquals("Generated from sfRoads", info.getDescription());
        assertTrue(info.getKeywords().contains("sfRoads roads"));
        assertEquals("roads_Type", info.getTitle());
        assertEquals("http://www.openplans.org/spearfish", info.getSchema().toString());
        assertEquals(589275.24, info.getBounds().getMinX(), 0.01);
        assertEquals("EPSG:NAD27 / UTM zone 13N", info.getCRS().getName().toString());
    }

    @Override
    @Ignore
    @Test
    public void testFeatureEvents() throws Exception {
        // temporarily disabled until events issue solved
    }
}
