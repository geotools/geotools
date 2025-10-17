/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.solr;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.commons.io.FileUtils;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.geotools.api.data.DataAccess;
import org.geotools.api.data.DataAccessFinder;
import org.geotools.api.data.FeatureSource;
import org.geotools.api.feature.Feature;
import org.geotools.api.feature.type.FeatureType;
import org.geotools.api.feature.type.Name;
import org.geotools.data.complex.feature.type.Types;
import org.geotools.data.solr.SolrTypeData.SolrTypes;
import org.geotools.data.solr.StationData.Stations;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.NameImpl;
import org.geotools.test.OnlineTestCase;
import org.geotools.util.URLs;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * This class contains the integration tests (online tests) for the integration between App-Schema and Apache Solr
 * Create appschema.properties file in {{user-dir}}/.geotools folder Set solr_url property URL config example:
 * solr_url=http://localhost:8983/solr, and create "stations" core in Solr
 */
public final class AppSchemaIntegrationTest extends OnlineTestCase {

    private static final String SOLR_URL_KEY = "solr_url";
    private static final String CORE_NAME = "stations";
    private static final String testData = "/test-data/appschema/";
    private static final String xmlFileName = "mappings_solr.xml";
    private static final String ST_NAMESPACE = "http://www.stations.org/1.0";
    private static final Name mappedTypeName = Types.typeName("multi_stations_solr");
    private static final String testDirStr = "target/test/" + AppSchemaIntegrationTest.class.getSimpleName();
    private static final File testDir = new File(testDirStr);

    private static final File appSchemaCacheDir =
            new File("target/test/" + AppSchemaIntegrationTest.class.getSimpleName() + "/app-schema-cache");

    private HttpSolrClient client;
    private static DataAccess<FeatureType, Feature> mappingDataStore;

    @Test
    public void testFeaturesData() throws Exception {
        FeatureSource<FeatureType, Feature> fSource = mappingDataStore.getFeatureSource(mappedTypeName);
        List<Feature> features = toFeaturesList(fSource.getFeatures());
        // check features count
        assertEquals(2, features.size());
        // check features data
        Name stationName = new NameImpl(ST_NAMESPACE, "stationName");
        for (Feature afeature : features) {
            String id = afeature.getIdentifier().getID();
            assertTrue(id.equals("13") || id.equals("7"));
            // check geom type
            assertTrue(afeature.getDefaultGeometryProperty().getValue() instanceof Point);
            Point theGeom = (Point) afeature.getDefaultGeometryProperty().getValue();
            GeometryFactory gf = new GeometryFactory();
            Point point1;
            switch (id) {
                case "7":
                    assertEquals("Bologna", (String)
                            afeature.getProperty(stationName).getValue());
                    // check geom
                    point1 = gf.createPoint(new Coordinate(11.34, 44.5));
                    assertEquals(point1, theGeom);
                    break;
                case "13":
                    assertEquals("Alessandria", (String)
                            afeature.getProperty(stationName).getValue());
                    // check geom
                    point1 = gf.createPoint(new Coordinate(8.63, 44.92));
                    assertEquals(point1, theGeom);
                    break;
            }
        }
    }

    private List<Feature> toFeaturesList(FeatureCollection<FeatureType, Feature> features) {
        List<Feature> result = new ArrayList<>();
        try (FeatureIterator<Feature> i = features.features()) {
            while (i.hasNext()) {
                result.add(i.next());
            }
        }
        return result;
    }

    @Override
    protected void setUpInternal() throws Exception {
        client = new HttpSolrClient.Builder(getSolrCoreURL()).build();
        solrDataSetup();
        prepareFiles();
        setupDataStore();
    }

    protected void setupDataStore() throws Exception {
        final Map<String, Object> dsParams = new HashMap<>();
        dsParams.put("dbtype", "app-schema");
        final URL url = new URL(new URL("file:"), "./" + testDirStr + "/" + xmlFileName);
        dsParams.put("url", url.toExternalForm());
        mappingDataStore = DataAccessFinder.getDataStore(dsParams);
    }

    protected String getSolrCoreURL() {
        return fixture.getProperty(SOLR_URL_KEY) + "/" + CORE_NAME;
    }

    protected void solrDataSetup() throws Exception {
        typeSetup();
        fieldsSetup();
        indexSetup();
    }

    protected void typeSetup() {
        // create geometry type
        TestsSolrUtils.createGeometryFieldType(client);
    }

    protected void fieldsSetup() throws Exception {
        File inFile = new File(AppSchemaIntegrationTest.class
                .getResource(testData + "solr_types.xml")
                .toURI());
        JAXBContext jcontext = JAXBContext.newInstance(SolrTypes.class);
        Unmarshaller um = jcontext.createUnmarshaller();
        SolrTypes types = (SolrTypes) um.unmarshal(inFile);
        for (SolrTypeData adata : types.getTypes()) {
            createField(adata.getName(), adata.getType(), adata.getMulti());
        }
    }

    protected void indexSetup() throws Exception {
        File inFile = new File(AppSchemaIntegrationTest.class
                .getResource(testData + "stationsData.xml")
                .toURI());
        JAXBContext jcontext = JAXBContext.newInstance(Stations.class);
        Unmarshaller um = jcontext.createUnmarshaller();
        Stations stations = (Stations) um.unmarshal(inFile);
        for (StationData adata : stations.getStations()) {
            client.add(adata.toSolrDoc());
        }
        client.commit();
    }

    protected void createField(String name, String type, boolean multiValued) {
        TestsSolrUtils.createField(client, name, type, multiValued);
    }

    protected void prepareFiles() throws Exception {
        // copy meteo.xsd
        copyTestData("meteo.xsd", testDir);
        // stationsData.xml
        copyTestData("stationsData.xml", testDir);

        // Modify datasource and copy xml
        File xmlFile = URLs.urlToFile(AppSchemaIntegrationTest.class.getResource(testData + xmlFileName));
        Document doc = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder()
                .parse(new InputSource(new FileInputStream(xmlFile)));
        Node solrDs = doc.getElementsByTagName("SolrDataStore").item(0);
        NodeList dsChilds = solrDs.getChildNodes();
        for (int i = 0; i < dsChilds.getLength(); i++) {
            Node achild = dsChilds.item(i);
            if (achild.getNodeName().equals("url")) {
                achild.setTextContent(getSolrCoreURL());
            }
        }
        // write new xml file:
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(testDir.getPath() + "/" + xmlFileName));
        transformer.transform(source, result);

        // create app-schema-cache folder
        appSchemaCacheDir.mkdir();
    }

    private void copyTestData(String baseFileName, File destDir) throws IOException {
        destDir.mkdirs();
        FileUtils.copyFileToDirectory(
                URLs.urlToFile(AppSchemaIntegrationTest.class.getResource(testData + baseFileName)), destDir);
    }

    /** appschema.properties file required */
    @Override
    protected String getFixtureId() {
        return "appschema";
    }
}
