/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2023, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.complex;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.geotools.api.feature.type.FeatureType;
import org.geotools.api.feature.type.Name;
import org.geotools.api.filter.FilterFactory;
import org.geotools.appschema.filter.FilterFactoryImplNamespaceAware;
import org.geotools.api.data.DataAccess;
import org.geotools.api.data.DataAccessFinder;
import org.geotools.api.data.FeatureSource;
import org.geotools.data.complex.feature.type.Types;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.locationtech.jts.io.WKTWriter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.helpers.NamespaceSupport;

public abstract class AbstractStationsTest {

    protected static String STATIONS_SCHEMA_BASE;

    static final String STATIONS_NS = "http://www.stations.org/1.0";

    static final Name STATION_FEATURE_TYPE = Types.typeName(STATIONS_NS, "StationType");

    static final Name STATION_FEATURE = Types.typeName(STATIONS_NS, "Station");
    static final Name STATION_WITH_MEASUREMENTS_CODE_FEATURE =
            Types.typeName(STATIONS_NS, "StationWithMeasurementCode");

    static final Name STATION_NO_DEFAULT_GEOM_MAPPING = Types.typeName("stationsNoDefaultGeometry");

    static final Name STATION_MULTIPLE_GEOM_MAPPING = Types.typeName("stationsMultipleGeometries");

    static final Name STATION_WITH_MEASUREMENTS_FEATURE_TYPE =
            Types.typeName(STATIONS_NS, "StationWithMeasurementsType");

    static final Name STATION_WITH_MEASUREMENTS_FEATURE =
            Types.typeName(STATIONS_NS, "StationWithMeasurements");

    static final Name STATION_WITH_GEOM_FEATURE_TYPE =
            Types.typeName(STATIONS_NS, "StationWithGeometryPropertyType");

    static final Name STATION_WITH_GEOM_FEATURE =
            Types.typeName(STATIONS_NS, "StationWithGeometryProperty");

    static final Name STATION_DEFAULT_GEOM_OVERRIDE_MAPPING =
            Types.typeName("stationsDefaultGeometryOverride");

    static final String MEASUREMENTS_NS = "http://www.measurements.org/1.0";

    static final Name MEASUREMENT_FEATURE_TYPE = Types.typeName(MEASUREMENTS_NS, "MeasurementType");

    static final Name MEASUREMENT_FEATURE = Types.typeName(MEASUREMENTS_NS, "Measurement");
    static final Name MEASUREMENT_CODE_FEATURE = Types.typeName(MEASUREMENTS_NS, "MeasurementCode");

    static final Name MEASUREMENT_MANY_TO_ONE_MAPPING = Types.typeName("measurementsManyToOne");

    static FilterFactory ff;

    WKTWriter writer = new WKTWriter();

    NamespaceSupport namespaces = new NamespaceSupport();

    static AppSchemaDataAccess stationsDataAccess;

    static AppSchemaDataAccess measurementsDataAccess;

    protected static final XPath XPATH = buildXPath();

    public AbstractStationsTest() {
        namespaces.declarePrefix("st", STATIONS_NS);
        namespaces.declarePrefix("ms", MEASUREMENTS_NS);
        ff = new FilterFactoryImplNamespaceAware(namespaces);
    }

    protected static AppSchemaDataAccess loadDataAccess(String mappingFile) throws IOException {
        Map<String, Serializable> dsParams = new HashMap<>();
        URL url = DefaultGeometryTest.class.getResource(STATIONS_SCHEMA_BASE + mappingFile);
        assertNotNull(url);

        dsParams.put("dbtype", "app-schema");
        dsParams.put("url", url.toExternalForm());
        DataAccess dataAccess = DataAccessFinder.getDataStore(dsParams);
        assertNotNull(dataAccess);
        assertTrue(dataAccess instanceof AppSchemaDataAccess);
        return (AppSchemaDataAccess) dataAccess;
    }

    /** Load all the data accesses. */
    protected static void loadDataAccesses() throws Exception {
        /** Load measurements data access */
        measurementsDataAccess = loadDataAccess("measurementsDefaultGeometry.xml");

        /** Load stations data access */
        stationsDataAccess = loadDataAccess("stationsDefaultGeometry.xml");

        FeatureType ft = stationsDataAccess.getSchema(STATION_FEATURE);
        assertNotNull(ft);
        assertEquals(STATION_FEATURE_TYPE, ft.getName());
        assertNotNull(stationsDataAccess.getSchema(STATION_NO_DEFAULT_GEOM_MAPPING));
        assertNotNull(stationsDataAccess.getSchema(STATION_MULTIPLE_GEOM_MAPPING));

        ft = stationsDataAccess.getSchema(STATION_WITH_MEASUREMENTS_FEATURE);
        assertNotNull(ft);
        assertEquals(STATION_WITH_MEASUREMENTS_FEATURE_TYPE, ft.getName());

        ft = stationsDataAccess.getSchema(STATION_WITH_GEOM_FEATURE);
        assertNotNull(ft);
        assertEquals(STATION_WITH_GEOM_FEATURE_TYPE, ft.getName());
        assertNotNull(stationsDataAccess.getSchema(STATION_DEFAULT_GEOM_OVERRIDE_MAPPING));

        FeatureSource fs = stationsDataAccess.getFeatureSource(STATION_FEATURE);
        FeatureCollection stationFeatures = fs.getFeatures();
        assertEquals(3, size(stationFeatures));

        ft = measurementsDataAccess.getSchema(MEASUREMENT_FEATURE);
        assertNotNull(ft);
        assertEquals(MEASUREMENT_FEATURE_TYPE, ft.getName());
        assertNotNull(measurementsDataAccess.getSchema(MEASUREMENT_MANY_TO_ONE_MAPPING));

        ft = measurementsDataAccess.getSchema(MEASUREMENT_CODE_FEATURE);
        assertNotNull(ft);
    }

    protected static int size(FeatureCollection features) {
        int size = 0;
        try (FeatureIterator iterator = features.features()) {
            while (iterator.hasNext()) {
                iterator.next();
                size++;
            }
            return size;
        }
    }

    protected List<Element> getElementsFromDocumentUsingXpath(
            Document document, String xpathExpression) {
        try {
            NodeList nodes =
                    (NodeList) XPATH.evaluate(xpathExpression, document, XPathConstants.NODESET);
            // filter to have only Elements
            List<Element> elements = new ArrayList<>();
            for (int i = 0; i < nodes.getLength(); i++) {
                Node node = nodes.item(i);
                if (node instanceof Element) {
                    elements.add((Element) node);
                }
            }
            return elements;
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }
    }

    protected static XPath buildXPath() {
        XPath xpath = XPathFactory.newInstance().newXPath();
        xpath.setNamespaceContext(new DefaultGeometryTest.SimpleNamespaceContext());
        return xpath;
    }

    /** NamespaceContext implementation with basic namespaces */
    static class SimpleNamespaceContext implements NamespaceContext {
        private Map<String, String> namespaces = new HashMap<>();

        public SimpleNamespaceContext() {
            namespaces.put("gml", "http://www.opengis.net/gml");
            namespaces.put("xlink", "http://www.w3.org/1999/xlink");
            namespaces.put("xsi", "http://www.w3.org/2001/XMLSchema-instance");
            namespaces.put("st", "http://www.stations.org/1.0");
            namespaces.put("ms", "http://www.measurements.org/1.0");
        }

        @Override
        public String getNamespaceURI(String prefix) {
            return namespaces.get(prefix);
        }

        @Override
        public String getPrefix(String namespaceURI) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Iterator<String> getPrefixes(String namespaceURI) {
            throw new UnsupportedOperationException();
        }
    }
}
