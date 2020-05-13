/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.shapefile;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureSource;
import org.geotools.data.Transaction;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.test.TestData;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.GeometryAttribute;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.filter.Filter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ShapeFileDbaseHeaderCharsetTest extends TestCaseSupport {

    private File testFile;

    @Before
    public void setUp() throws Exception {
        testFile = getTempFile();
    }

    @Test
    public void testCreateShapefile_en() throws Exception {
        Fields fields;
        try (InputStream inputStream = TestData.url(this, "dbase-cs/data_en.xml").openStream()) {
            fields = Fields.buildFromXml(inputStream);
        }
        createShapefile(testFile, null, fields);
        checkShapefile(testFile, null, fields, false);
        checkShapefile(testFile, null, fields, true);
    }

    @Test
    public void testCreateShapefile_ru() throws Exception {
        Fields fields;
        try (InputStream inputStream = TestData.url(this, "dbase-cs/data_ru.xml").openStream()) {
            fields = Fields.buildFromXml(inputStream);
        }
        createShapefile(testFile, "CP1251", fields);
        checkShapefile(testFile, "CP1251", fields, false);
        checkShapefile(testFile, "CP1251", fields, true);
    }

    @Test
    public void testCreateShapefile_cn() throws Exception {
        Fields fields;
        try (InputStream inputStream = TestData.url(this, "dbase-cs/data_cn.xml").openStream()) {
            fields = Fields.buildFromXml(inputStream);
        }
        createShapefile(testFile, "GB2312", fields);
        checkShapefile(testFile, "GB2312", fields, false);
        checkShapefile(testFile, "GB2312", fields, true);
    }

    private void checkShapefile(
            File shpfile, String charsetName, Fields fields, boolean memoryMapped)
            throws IOException {
        ShapefileDataStore dataStore = openShapefileDataStore(shpfile, charsetName, memoryMapped);
        try {
            String typeName = dataStore.getTypeNames()[0];
            FeatureSource<SimpleFeatureType, SimpleFeature> source =
                    dataStore.getFeatureSource(typeName);
            checkFieldNames(fields, source);
            checkFieldValues(fields, source);
        } finally {
            dataStore.dispose();
        }
    }

    private void checkFieldValues(
            Fields fields, FeatureSource<SimpleFeatureType, SimpleFeature> source)
            throws IOException {
        Filter filter = Filter.INCLUDE;
        FeatureCollection<SimpleFeatureType, SimpleFeature> collection = source.getFeatures(filter);

        int j = 0;
        try (FeatureIterator<SimpleFeature> features = collection.features()) {
            while (features.hasNext()) {
                SimpleFeature feature = features.next();
                Fields.Row row = fields.rows.get(j);
                int k = 0;
                for (Property attribute : feature.getProperties()) {
                    // a geometry attribute isn't the purpose of this test case
                    if (attribute instanceof GeometryAttribute) continue;
                    String value = (String) attribute.getValue();
                    assertThat(value, is(equalTo(row.get(k))));
                    k++;
                }
                j++;
            }
        }
    }

    private void checkFieldNames(
            Fields fields, FeatureSource<SimpleFeatureType, SimpleFeature> source) {
        int i = 0;
        SimpleFeatureType schema = source.getSchema();
        for (AttributeDescriptor descriptor : schema.getAttributeDescriptors()) {
            String name = descriptor.getLocalName();
            if (name.equals("the_geom")) continue;
            assertThat(name, is(equalTo(fields.names.get(i))));
            i++;
        }
    }

    private ShapefileDataStore openShapefileDataStore(
            File shpfile, String charsetName, boolean memoryMapped) throws IOException {
        ShapefileDataStoreFactory dataStoreFactory = new ShapefileDataStoreFactory();
        Map<String, Serializable> params = new HashMap<>();
        params.put("url", shpfile.toURI().toURL());
        if (charsetName != null && !charsetName.isEmpty()) {
            params.put("charset", charsetName);
        }
        if (memoryMapped) {
            params.put("memory mapped buffer", true);
        }
        return (ShapefileDataStore) dataStoreFactory.createDataStore(params);
    }

    private void createShapefile(File shpfile, String charsetName, Fields fields) throws Exception {
        String attributeDefinitionString = buildAttributeDefinitionString(fields);
        SimpleFeatureType featureType =
                DataUtilities.createType("Location", attributeDefinitionString);

        List<SimpleFeature> features = buildFeatures(fields, featureType);

        ShapefileDataStore newDataStore = createShapefileDataStore(shpfile, charsetName);
        try {
            writeFeatures(newDataStore, features, featureType);
        } finally {
            newDataStore.dispose();
        }
    }

    private List<SimpleFeature> buildFeatures(Fields fields, SimpleFeatureType featureType) {
        List<SimpleFeature> features = new ArrayList<>();
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(featureType);
        GeometryFactory geometryFactory = new GeometryFactory();

        double longitude;
        double latitude;

        int rowCount = 0;
        for (Fields.Row row : fields.rows) {
            rowCount++;
            longitude = Double.parseDouble(row.getLongitude());
            latitude = Double.parseDouble(row.getLatitude());

            Point point = geometryFactory.createPoint(new Coordinate(longitude, latitude));
            featureBuilder.add(point);
            for (int k = 0; k < row.size(); k++) {
                String fieldName = fields.names.get(k);
                String fieldValue = row.get(k);
                featureBuilder.set(fieldName, fieldValue);
            }
            SimpleFeature feature = featureBuilder.buildFeature("" + rowCount);
            features.add(feature);
        }

        return features;
    }

    private void writeFeatures(
            ShapefileDataStore newDataStore,
            List<SimpleFeature> features,
            SimpleFeatureType featureType)
            throws IOException {

        SimpleFeatureCollection collection = new ListFeatureCollection(featureType, features);

        newDataStore.createSchema(featureType);
        newDataStore.forceSchemaCRS(DefaultGeographicCRS.WGS84);
        String typeName = newDataStore.getTypeNames()[0];
        SimpleFeatureSource featureSource = newDataStore.getFeatureSource(typeName);

        SimpleFeatureStore featureStore = (SimpleFeatureStore) featureSource;
        Transaction transaction = new DefaultTransaction("create");
        featureStore.setTransaction(transaction);
        try {
            featureStore.addFeatures(collection);
            transaction.commit();
        } finally {
            transaction.close();
        }
    }

    private ShapefileDataStore createShapefileDataStore(File shpfile, String charsetName)
            throws IOException {
        ShapefileDataStoreFactory dataStoreFactory = new ShapefileDataStoreFactory();
        Map<String, Serializable> params = new HashMap<>();
        params.put("url", shpfile.toURI().toURL());
        if (charsetName != null && !charsetName.isEmpty()) {
            params.put("charset", charsetName);
        }
        return (ShapefileDataStore) dataStoreFactory.createNewDataStore(params);
    }

    private String buildAttributeDefinitionString(Fields fields) {
        StringBuilder builder = new StringBuilder("the_geom:Point:srid=4326,");
        for (int i = 0; i < fields.names.size(); i++) {
            String attributeName = fields.names.get(i);
            builder.append(attributeName).append(":String");
            if (i < fields.names.size() - 1) {
                builder.append(",");
            }
        }
        return builder.toString();
    }

    private static class Fields {
        List<String> names = new ArrayList<>();
        List<Row> rows = new ArrayList<>();
        private String lon, lat;
        private int lonIndex, latIndex;

        static Fields buildFromXml(InputStream inputStream) throws Exception {
            Fields fields = new Fields();

            // Document
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(inputStream);
            Element root = document.getDocumentElement();

            // Coordinate's fields names
            Element coordNode = (Element) root.getElementsByTagName("Coordinate").item(0);
            fields.lon = coordNode.getElementsByTagName("longitude").item(0).getTextContent();
            fields.lat = coordNode.getElementsByTagName("latitude").item(0).getTextContent();

            // Attributes
            Element attributesNode = (Element) root.getElementsByTagName("Attributes").item(0);
            NodeList attrNodes = attributesNode.getElementsByTagName("Attribute");
            for (int i = 0; i < attrNodes.getLength(); i++) {
                Row row = fields.new Row();

                NodeList fieldNodes = attrNodes.item(i).getChildNodes();
                int cnt = 0;
                for (int j = 0; j < fieldNodes.getLength(); j++) {
                    if (fieldNodes.item(j).getNodeType() == Node.ELEMENT_NODE) {
                        Element fieldNode = (Element) fieldNodes.item(j);
                        if (i == 0) {
                            String fieldName = fieldNode.getNodeName();
                            fields.names.add(fieldName);
                            if (fieldName.equals(fields.lon)) fields.lonIndex = cnt;
                            if (fieldName.equals(fields.lat)) fields.latIndex = cnt;
                        }
                        row.add(fieldNode.getTextContent());
                        cnt++;
                    }
                }

                fields.rows.add(row);
            }

            return fields;
        }

        private class Row {
            private List<String> fieldValues = new ArrayList<>();

            boolean add(String value) {
                return fieldValues.add(value);
            }

            String get(int i) {
                return fieldValues.get(i);
            }

            int size() {
                return fieldValues.size();
            }

            String getLongitude() {
                return fieldValues.get(lonIndex);
            }

            String getLatitude() {
                return fieldValues.get(latIndex);
            }

            @Override
            public String toString() {
                return fieldValues.toString();
            }
        }
    }
}
