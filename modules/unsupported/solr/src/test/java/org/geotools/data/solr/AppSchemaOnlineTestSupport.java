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
import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.commons.io.FileUtils;
import org.apache.solr.client.solrj.impl.HttpJdkSolrClient;
import org.geotools.api.data.DataAccess;
import org.geotools.api.data.DataAccessFinder;
import org.geotools.api.feature.Feature;
import org.geotools.api.feature.type.FeatureType;
import org.geotools.api.feature.type.Name;
import org.geotools.data.complex.feature.type.Types;
import org.geotools.data.solr.SolrTypeData.SolrTypes;
import org.geotools.data.solr.StationData.Stations;
import org.geotools.test.OnlineTestCase;
import org.geotools.util.URLs;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public abstract class AppSchemaOnlineTestSupport extends OnlineTestCase {
    // Postgres configs:
    public static final String PG_HOST_KEY = "pg_host";
    public static final String PG_PORT_KEY = "pg_port";
    public static final String PG_DATABASE_KEY = "pg_db";
    public static final String PG_USER_KEY = "pg_user";
    public static final String PG_PASS_KEY = "pg_pass";
    // Solr configs:
    public static final String SOLR_URL_KEY = "solr_url";
    protected String CORE_NAME = "stations";
    protected String typesFileName = "solr_types.xml";
    protected String solrDataFilename = "stationsData.xml";
    // appschema configs:
    protected String testData = "/test-data/appschema/";
    protected String xsdFileName = "meteo.xsd";
    protected String xmlFileName = "mappings_solr.xml";
    protected String ST_NAMESPACE = "http://www.stations.org/1.0";
    protected Name mappedTypeName = Types.typeName("StationType-f46d72da-5591-4873-b210-5ed30a6ffb0d");
    //
    @Rule
    public TemporaryFolder tempfolder = new TemporaryFolder();

    protected File tempDir;
    protected File appSchemaCacheDir;

    protected HttpJdkSolrClient client;
    protected DataAccess<FeatureType, Feature> mappingDataStore;

    protected void copyTestData(String baseFileName, File destDir) throws IOException {
        destDir.mkdirs();
        FileUtils.copyFileToDirectory(URLs.urlToFile(this.getClass().getResource(testData + baseFileName)), destDir);
    }

    /** Clone entire folder to temp for possible modifications */
    private void createTestFolder() throws IOException {
        tempfolder.create();
        tempDir = tempfolder.newFolder(this.getClass().getSimpleName());
        // create app-schema-cache folder
        appSchemaCacheDir = new File(tempDir.getPath() + "/app-schema-cache");
        appSchemaCacheDir.mkdir();
    }

    @Override
    protected void setUpInternal() throws Exception {
        configFieldsSetup();
        TestContainersSupport.solrCoreUrl(CORE_NAME);
        fixture.setProperty(SOLR_URL_KEY, TestContainersSupport.solrServerUrl());
        fixture.setProperty(PG_HOST_KEY, TestContainersSupport.postgresHost());
        fixture.setProperty(PG_PORT_KEY, TestContainersSupport.postgresPort());
        fixture.setProperty(PG_DATABASE_KEY, TestContainersSupport.postgresDatabase());
        fixture.setProperty(PG_USER_KEY, TestContainersSupport.postgresUser());
        fixture.setProperty(PG_PASS_KEY, TestContainersSupport.postgresPassword());
        createTestFolder();
        client = new HttpJdkSolrClient.Builder(getSolrCoreURL()).build();
        solrDataSetup();
        prepareFiles();
        setupDataStore();
    }

    protected void configFieldsSetup() {}

    private void setupDataStore() throws Exception {
        final Map<String, Serializable> dsParams = new HashMap<>();
        dsParams.put("dbtype", "app-schema");
        final URL url = new URL(tempDir.toURI().toURL().toExternalForm() + File.separator + xmlFileName);
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

    private void typeSetup() {
        // create geometry type
        TestsSolrUtils.createGeometryFieldType(client);
    }

    protected void fieldsSetup() throws Exception {
        File inFile =
                new File(this.getClass().getResource(testData + typesFileName).toURI());
        JAXBContext jcontext = JAXBContext.newInstance(SolrTypes.class);
        Unmarshaller um = jcontext.createUnmarshaller();
        SolrTypes types = (SolrTypes) um.unmarshal(inFile);
        for (SolrTypeData adata : types.getTypes()) {
            createField(adata.getName(), adata.getType(), adata.getMulti());
        }
    }

    protected void indexSetup() throws Exception {
        File inFile = new File(
                this.getClass().getResource(testData + solrDataFilename).toURI());
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
        // copy *.xsd
        copyTestData(this.xsdFileName, tempDir);
        copyAndPatchTestData(this.xmlFileName);
    }

    protected void copyAndPatchTestData(String baseFileName) throws Exception {
        File file = URLs.urlToFile(this.getClass().getResource(testData + baseFileName));
        Document doc = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder()
                .parse(new InputSource(new FileInputStream(file)));
        NodeList solrStores = doc.getElementsByTagName("SolrDataStore");
        for (int i = 0; i < solrStores.getLength(); i++) {
            NodeList children = solrStores.item(i).getChildNodes();
            for (int j = 0; j < children.getLength(); j++) {
                Node child = children.item(j);
                if ("url".equals(child.getNodeName())) {
                    child.setTextContent(getSolrCoreURL());
                }
            }
        }
        NodeList parameters = doc.getElementsByTagName("Parameter");
        for (int i = 0; i < parameters.getLength(); i++) {
            NodeList children = parameters.item(i).getChildNodes();
            String name = null;
            Node value = null;
            for (int j = 0; j < children.getLength(); j++) {
                Node child = children.item(j);
                if ("name".equals(child.getNodeName())) {
                    name = child.getTextContent();
                } else if ("value".equals(child.getNodeName())) {
                    value = child;
                }
            }
            if (value == null || name == null) {
                continue;
            }
            switch (name) {
                case "host":
                    value.setTextContent(fixture.getProperty(PG_HOST_KEY));
                    break;
                case "port":
                    value.setTextContent(fixture.getProperty(PG_PORT_KEY));
                    break;
                case "database":
                    value.setTextContent(fixture.getProperty(PG_DATABASE_KEY));
                    break;
                case "user":
                    value.setTextContent(fixture.getProperty(PG_USER_KEY));
                    break;
                case "passwd":
                    value.setTextContent(fixture.getProperty(PG_PASS_KEY));
                    break;
                default:
                    break;
            }
        }
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.transform(new DOMSource(doc), new StreamResult(new File(tempDir, baseFileName)));
    }

    @Override
    protected String getFixtureId() {
        return null;
    }

    @Override
    protected Properties createOfflineFixture() {
        return TestContainersSupport.offlineFixture();
    }
}
