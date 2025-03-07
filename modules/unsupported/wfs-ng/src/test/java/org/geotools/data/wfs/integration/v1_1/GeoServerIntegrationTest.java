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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import net.opengis.wfs.IdentifierGenerationOptionType;
import org.geotools.api.data.DataStore;
import org.geotools.api.data.ResourceInfo;
import org.geotools.api.data.SimpleFeatureStore;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.Name;
import org.geotools.data.DataUtilities;
import org.geotools.data.wfs.WFSDataStore;
import org.geotools.data.wfs.WFSTestData;
import org.geotools.data.wfs.integration.AbstractIntegrationTest;
import org.geotools.data.wfs.integration.IntegrationTestWFSClient;
import org.geotools.data.wfs.internal.TransactionRequest;
import org.geotools.data.wfs.internal.WFSClient;
import org.geotools.data.wfs.internal.WFSConfig;
import org.geotools.feature.NameImpl;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureImpl;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.filter.identity.FeatureIdImpl;
import org.geotools.referencing.CRS;
import org.geotools.util.factory.Hints;
import org.junit.Ignore;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class GeoServerIntegrationTest extends AbstractIntegrationTest {
    private static final QName TYPE1 = new QName("http://www.census.gov", "poi", "tiger");
    private static final Name featureName =
            new NameImpl(TYPE1.getNamespaceURI(), TYPE1.getPrefix() + "_" + TYPE1.getLocalPart());

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
                DataUtilities.createType("roadsType", "the_geom:MultiLineString,cat:java.lang.Long,label:String");
        testDataType.featureType = DataUtilities.createSubType(
                testDataType.featureType, null, CRS.decode("urn:x-ogc:def:crs:EPSG:6.11.2:26713"));

        testDataType.stringAttribute = "label";
        testDataType.numberOfFeatures = 3;
        testDataType.typeName = "sf_roads";
        testDataType.newFeature = SimpleFeatureBuilder.build(
                testDataType.featureType,
                new Object[] {
                    new GeometryFactory()
                            .createLineString(new Coordinate[] {new Coordinate(1, 2), new Coordinate(2, 3)}),
                    Integer.valueOf(4),
                    "somekindofroad"
                },
                "roads.4");

        return testDataType;
    }

    @Override
    protected TestDataType createSecondType() throws Exception {
        TestDataType testDataType = new TestDataType();

        testDataType.featureType =
                DataUtilities.createType("archsitesType", "the_geom:Point,cat:java.lang.Long,str1:String");
        testDataType.featureType =
                DataUtilities.createSubType(testDataType.featureType, null, CRS.decode("EPSG:26713", true));

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
        assertEquals(589276.63, info.getBounds().getMinX(), 0.01);
        assertEquals("EPSG:NAD27 / UTM zone 13N", info.getCRS().getName().toString());
    }

    @Override
    @Ignore
    @Test
    public void testFeatureEvents() throws Exception {
        // temporarily disabled until events issue solved
    }

    @Test
    public void testTransactionInsertWithIdgenUseExisting()
            throws SAXException, IOException, ParserConfigurationException {
        WFSDataStore dataStore = new WFSDataStore(
                new IntegrationTestWFSClient("GeoServer_2.0/1.1.0/", WFSTestData.getGmlCompatibleConfig()));

        GeometryFactory geomfac = new GeometryFactory(new PrecisionModel(10));

        Coordinate insideCoord = new Coordinate(5.2, 7.5);
        Point myPoint = geomfac.createPoint(insideCoord);

        SimpleFeatureType sft = dataStore.getSchema(featureName.getLocalPart());

        SimpleFeatureTypeBuilder typeBuilder = new SimpleFeatureTypeBuilder();
        typeBuilder.setCRS(sft.getCoordinateReferenceSystem());
        typeBuilder.add("field", Geometry.class);
        typeBuilder.setName(TYPE1.getLocalPart());
        typeBuilder.setNamespaceURI(sft.getName().getNamespaceURI());

        SimpleFeature feat = new SimpleFeatureImpl(
                Arrays.asList(new Object[] {myPoint}),
                typeBuilder.buildFeatureType(),
                new FeatureIdImpl(UUID.randomUUID().toString()));

        feat.getUserData().put(Hints.USE_PROVIDED_FID, true);

        TransactionRequest transactionRequest = dataStore.getWfsClient().createTransaction();
        TransactionRequest.Insert insert = transactionRequest.createInsert(dataStore.getRemoteTypeName(sft.getName()));
        insert.add(feat);
        transactionRequest.add(insert);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            transactionRequest.performPostOutput(out);

            try (ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray())) {

                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                factory.setNamespaceAware(true);

                Document document = factory.newDocumentBuilder().parse(in);

                Element root = document.getDocumentElement();
                NodeList insertNodes = root.getElementsByTagNameNS("http://www.opengis.net/wfs", "Insert");

                assertTrue(insertNodes.getLength() > 0);

                Element insertElement = (Element) insertNodes.item(0);

                NamedNodeMap attributes = insertElement.getAttributes();
                Node idgenAttribute = attributes.getNamedItemNS(null, "idgen");

                assertNotNull(idgenAttribute);

                assertEquals(
                        IdentifierGenerationOptionType.USE_EXISTING_LITERAL.getName(), idgenAttribute.getNodeValue());
            }
        }
    }
}
