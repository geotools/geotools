package org.geotools.data.complex;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.NamespaceContext;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.geotools.appschema.filter.FilterFactoryImplNamespaceAware;
import org.geotools.data.DataAccess;
import org.geotools.data.DataAccessFinder;
import org.geotools.data.FeatureSource;
import org.geotools.data.complex.feature.type.Types;
import org.geotools.data.complex.util.ComplexFeatureConstants;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.gml3.GML;
import org.geotools.gml3.GMLConfiguration;
import org.geotools.xsd.Encoder;
import org.junit.BeforeClass;
import org.junit.Test;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.WKTWriter;
import org.opengis.feature.Feature;
import org.opengis.feature.GeometryAttribute;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.Id;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.NamespaceSupport;

public class DefaultGeometryTest {

    static final String STATIONS_SCHEMA_BASE = "/test-data/stations/";

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

    private static FilterFactory2 ff;

    private WKTWriter writer = new WKTWriter();

    private NamespaceSupport namespaces = new NamespaceSupport();

    private static AppSchemaDataAccess stationsDataAccess;

    private static AppSchemaDataAccess measurementsDataAccess;
    private static final XPath XPATH = buildXPath();

    public DefaultGeometryTest() {
        namespaces.declarePrefix("st", STATIONS_NS);
        namespaces.declarePrefix("ms", MEASUREMENTS_NS);
        ff = new FilterFactoryImplNamespaceAware(namespaces);
    }

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        loadDataAccesses();
    }

    /** Load all the data accesses. */
    private static void loadDataAccesses() throws Exception {
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

    private static AppSchemaDataAccess loadDataAccess(String mappingFile) throws IOException {
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

    private static int size(FeatureCollection features) {
        int size = 0;
        try (FeatureIterator iterator = features.features()) {
            while (iterator.hasNext()) {
                iterator.next();
                size++;
            }
            return size;
        }
    }

    /**
     * Tests that the default geometry configuration in the mapping file is correctly picked up and
     * properly applied.
     */
    @Test
    public void testDefaultGeometryMappingConfiguration() throws IOException {
        FeatureType ftWithDefaultGeom = stationsDataAccess.getSchema(STATION_FEATURE);
        assertNotNull(ftWithDefaultGeom.getGeometryDescriptor());
        assertEquals(
                ComplexFeatureConstants.DEFAULT_GEOMETRY_LOCAL_NAME,
                ftWithDefaultGeom.getGeometryDescriptor().getLocalName());

        FeatureTypeMapping mappingDefaultGeom =
                stationsDataAccess.getMappingByName(STATION_FEATURE);
        assertNotNull(mappingDefaultGeom);
        assertNotNull(mappingDefaultGeom.getDefaultGeometryXPath());
        assertEquals("st:location/st:position", mappingDefaultGeom.getDefaultGeometryXPath());

        FeatureSource fs = stationsDataAccess.getFeatureSource(STATION_FEATURE);
        Id filter = ff.id(ff.featureId("st.1"));
        FeatureCollection fc = fs.getFeatures(filter);
        assertEquals(1, size(fc));
        try (FeatureIterator it = fc.features()) {
            Feature station1 = it.next();
            assertEquals("st.1", station1.getIdentifier().toString());

            GeometryAttribute defaultGeom = station1.getDefaultGeometryProperty();
            assertNotNull(defaultGeom);
            assertNotNull(defaultGeom.getValue());
            assertTrue(defaultGeom.getValue() instanceof Point);

            // check geometry value
            Point point = (Point) defaultGeom.getValue();
            assertEquals("POINT (-1 1)", writer.write(point));
        }
    }

    /**
     * Tests that no default geometry is available for a feature type with no direct child geometry
     * property.
     */
    @Test
    public void testDefaultGeometryNone() throws IOException {
        FeatureTypeMapping mappingNoDefaultGeom =
                stationsDataAccess.getMappingByNameOrElement(STATION_NO_DEFAULT_GEOM_MAPPING);
        assertNotNull(mappingNoDefaultGeom);
        // no default geometry configured
        assertNull(mappingNoDefaultGeom.getDefaultGeometryXPath());

        FeatureType ftNoDefaultGeom = stationsDataAccess.getSchema(STATION_NO_DEFAULT_GEOM_MAPPING);
        // therefore, no default geometry descriptor available
        assertNull(ftNoDefaultGeom.getGeometryDescriptor());
    }

    /**
     * Tests that the default geometry configuration overrides the default geometry that would be
     * automatically picked up by the feature type building machinery.
     */
    @Test
    public void testDefaultGeometryOverride() throws IOException {
        FeatureTypeMapping mappingWithGeom =
                stationsDataAccess.getMappingByName(STATION_WITH_GEOM_FEATURE);
        assertNotNull(mappingWithGeom);
        // no default geometry configured
        assertNull(mappingWithGeom.getDefaultGeometryXPath());

        FeatureType ftWithGeom = stationsDataAccess.getSchema(STATION_WITH_GEOM_FEATURE);
        assertNotNull(ftWithGeom.getGeometryDescriptor());
        // the direct child geometry property is automatically picked as default geometry
        assertEquals(
                Types.typeName(STATIONS_NS, "geometry"),
                ftWithGeom.getGeometryDescriptor().getName());

        // same feature type, this time default geometry is configured
        FeatureTypeMapping mappingDefaultGeomOverride =
                stationsDataAccess.getMappingByName(STATION_DEFAULT_GEOM_OVERRIDE_MAPPING);
        assertEquals(
                mappingWithGeom.getTargetFeature().getName(),
                mappingDefaultGeomOverride.getTargetFeature().getName());
        assertNotNull(mappingDefaultGeomOverride);
        assertEquals(
                "st:location/st:position", mappingDefaultGeomOverride.getDefaultGeometryXPath());

        FeatureType ftDefaultGeomOverride =
                stationsDataAccess.getSchema(STATION_DEFAULT_GEOM_OVERRIDE_MAPPING);
        assertEquals(ftWithGeom.getName(), ftDefaultGeomOverride.getName());
        assertNotNull(ftDefaultGeomOverride.getGeometryDescriptor());
        // the direct child geometry property is automatically picked as default geometry
        assertEquals(
                ComplexFeatureConstants.DEFAULT_GEOMETRY_LOCAL_NAME,
                ftDefaultGeomOverride.getGeometryDescriptor().getLocalName());
    }

    /**
     * Tests that properties nested inside chained feature types may be used as default geometry.
     */
    @Test
    public void testDefaultGeometryInsideChainedFeatureType() throws IOException {
        FeatureTypeMapping mappingChained =
                stationsDataAccess.getMappingByName(STATION_WITH_MEASUREMENTS_FEATURE);
        assertNotNull(mappingChained);
        assertEquals(
                "st:measurements/ms:Measurement/ms:sampledArea/ms:SampledArea/ms:geometry",
                mappingChained.getDefaultGeometryXPath());

        FeatureType ftChained = stationsDataAccess.getSchema(STATION_WITH_MEASUREMENTS_FEATURE);
        assertNotNull(ftChained.getGeometryDescriptor());
        assertEquals(
                ComplexFeatureConstants.DEFAULT_GEOMETRY_LOCAL_NAME,
                ftChained.getGeometryDescriptor().getLocalName());

        FeatureSource fs = stationsDataAccess.getFeatureSource(STATION_WITH_MEASUREMENTS_FEATURE);
        Id filter = ff.id(ff.featureId("st.1"));
        FeatureCollection fc = fs.getFeatures(filter);
        assertEquals(1, size(fc));
        try (FeatureIterator it = fc.features()) {
            Feature station1 = it.next();
            assertEquals("st.1", station1.getIdentifier().toString());

            GeometryAttribute defaultGeom = station1.getDefaultGeometryProperty();
            assertNotNull(defaultGeom);
            assertNotNull(defaultGeom.getValue());
            // the geometry property of the chained feature type has been picked
            // --> must be a Polygon
            assertTrue(defaultGeom.getValue() instanceof Polygon);

            // check geometry value
            Polygon poly = (Polygon) defaultGeom.getValue();
            assertEquals("POLYGON ((-2 2, 0 2, 0 -2, -2 -2, -2 2))", writer.write(poly));
        }
    }

    /**
     * Tests that an exception is thrown at runtime if the evaluation of the default geometry
     * expression against a target feature type does not yield a {@link GeometryDescriptor}
     * instance.
     */
    @Test
    public void testDefaultGeometryWrongType() {
        try {
            // try to load data access with faulty configuration --> exception should be thrown
            loadDataAccess("stationsDefaultGeometryWrongType.xml");
            fail("Expected exception to be thrown");
        } catch (IOException ex) {
            assertTrue(ex.getCause() instanceof IllegalArgumentException);
            IllegalArgumentException iae = (IllegalArgumentException) ex.getCause();
            // check error message
            assertEquals(
                    "Default geometry descriptor could not be found for type "
                            + "\"http://www.stations.org/1.0:Station\" at x-path \"st:location/st:name\"",
                    iae.getMessage());
        } catch (Exception e) {
            fail(
                    "Expected IllegalArgumentException to be thrown, but "
                            + e.getClass().getName()
                            + " was thrown instead");
        }
    }

    /**
     * Tests that an exception is thrown at runtime if the default geometry expression addresses a
     * non-existent property.
     */
    @Test
    public void testDefaultGeometryNonExistentProperty() {
        try {
            // try to load data access with faulty configuration --> exception should be thrown
            loadDataAccess("stationsDefaultGeometryNonExistentProperty.xml");
            fail("Expected exception to be thrown");
        } catch (IOException ex) {
            assertTrue(ex.getCause() instanceof IllegalArgumentException);
            IllegalArgumentException iae = (IllegalArgumentException) ex.getCause();
            // check error message
            assertEquals(
                    "Default geometry descriptor could not be found for type "
                            + "\"http://www.stations.org/1.0:Station\" at x-path \"st:location/st:notThere\"",
                    iae.getMessage());
        } catch (Exception e) {
            fail(
                    "Expected IllegalArgumentException to be thrown, but "
                            + e.getClass().getName()
                            + " was thrown instead");
        }
    }

    /**
     * Tests that no default geometry value is set if evaluating the default geometry expression
     * against a particular feature yields multiple values.
     */
    @Test
    public void testDefaultGeometryMultipleValues() throws IOException {
        // try to iterate over feature collection containing a Station feature with multiple
        // default geometry values --> default geometry will be set to null
        FeatureSource fs = stationsDataAccess.getFeatureSource(STATION_MULTIPLE_GEOM_MAPPING);
        FeatureCollection fc = fs.getFeatures();
        try (FeatureIterator it = fc.features()) {
            try {
                it.next();
            } catch (Exception ex) {
                assertNotNull(ex.getCause());
                assertTrue(
                        "Expected RuntimeException to be thrown",
                        ex.getCause() instanceof RuntimeException);
                // check error message
                RuntimeException re = (RuntimeException) ex.getCause();
                assertEquals(
                        "Error setting default geometry value: multiple values were found",
                        re.getMessage());
            }
        }
    }

    /** Tests GML encoding of client properties doesn't affect parent containers. */
    @Test
    public void testGMLEncodingProperties() throws IOException {
        FeatureSource fs =
                stationsDataAccess.getFeatureSource(STATION_WITH_MEASUREMENTS_CODE_FEATURE);
        GMLConfiguration gml31Config = new GMLConfiguration();
        Encoder encoder = new Encoder(gml31Config);
        // filter for station with id "st.1"
        Id filter = ff.id(ff.featureId("st.1"));
        FeatureCollection fc = fs.getFeatures(filter);
        assertEquals(1, size(fc));
        try (FeatureIterator it = fc.features()) {
            Feature station1 = it.next();
            assertEquals("st.1", station1.getIdentifier().toString());
            Document dom = encoder.encodeAsDOM(station1, GML.featureMember);

            List<Element> measurements =
                    getElementsFromDocumentUsingXpath(
                            dom, "//st:StationWithMeasurementCode/st:measurements");
            assertFalse(measurements.isEmpty());
            assertFalse(measurements.get(0).hasAttribute("codename"));

            measurements =
                    getElementsFromDocumentUsingXpath(
                            dom,
                            "//st:StationWithMeasurementCode/st:measurements/ms:MeasurementCode");
            assertFalse(measurements.isEmpty());
            assertTrue(measurements.get(0).hasAttribute("codename"));
            assertFalse(measurements.get(0).hasAttribute("code"));

            List<Element> names =
                    getElementsFromDocumentUsingXpath(
                            dom,
                            "//st:StationWithMeasurementCode/st:measurements/ms:MeasurementCode/ms:name");
            assertFalse(names.isEmpty());
            assertTrue(names.get(0).hasAttribute("code"));

        } catch (TransformerException | SAXException e) {
            throw new RuntimeException(e);
        }
    }

    protected static XPath buildXPath() {
        XPath xpath = XPathFactory.newInstance().newXPath();
        xpath.setNamespaceContext(new SimpleNamespaceContext());
        return xpath;
    }

    private List<Element> getElementsFromDocumentUsingXpath(
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
