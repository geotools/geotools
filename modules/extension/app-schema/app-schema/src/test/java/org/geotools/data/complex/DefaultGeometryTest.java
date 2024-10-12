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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.List;
import javax.xml.transform.TransformerException;
import org.geotools.api.data.FeatureSource;
import org.geotools.api.feature.Feature;
import org.geotools.api.feature.GeometryAttribute;
import org.geotools.api.feature.type.FeatureType;
import org.geotools.api.filter.Id;
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
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class DefaultGeometryTest extends AbstractStationsTest {

    static {
        STATIONS_SCHEMA_BASE = "/test-data/stations/";
    }

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        loadDataAccesses();
    }

    public DefaultGeometryTest() {
        super();
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

        FeatureTypeMapping mappingDefaultGeom = stationsDataAccess.getMappingByName(STATION_FEATURE);
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
        FeatureTypeMapping mappingWithGeom = stationsDataAccess.getMappingByName(STATION_WITH_GEOM_FEATURE);
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
        assertEquals("st:location/st:position", mappingDefaultGeomOverride.getDefaultGeometryXPath());

        FeatureType ftDefaultGeomOverride = stationsDataAccess.getSchema(STATION_DEFAULT_GEOM_OVERRIDE_MAPPING);
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
        FeatureTypeMapping mappingChained = stationsDataAccess.getMappingByName(STATION_WITH_MEASUREMENTS_FEATURE);
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
            fail("Expected IllegalArgumentException to be thrown, but "
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
            fail("Expected IllegalArgumentException to be thrown, but "
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
                assertTrue("Expected RuntimeException to be thrown", ex.getCause() instanceof RuntimeException);
                // check error message
                RuntimeException re = (RuntimeException) ex.getCause();
                assertEquals("Error setting default geometry value: multiple values were found", re.getMessage());
            }
        }
    }

    /** Tests GML encoding of client properties doesn't affect parent containers. */
    @Test
    public void testGMLEncodingProperties() throws IOException {
        FeatureSource fs = stationsDataAccess.getFeatureSource(STATION_WITH_MEASUREMENTS_CODE_FEATURE);
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
                    getElementsFromDocumentUsingXpath(dom, "//st:StationWithMeasurementCode/st:measurements");
            assertFalse(measurements.isEmpty());
            assertFalse(measurements.get(0).hasAttribute("codename"));

            measurements = getElementsFromDocumentUsingXpath(
                    dom, "//st:StationWithMeasurementCode/st:measurements/ms:MeasurementCode");
            assertFalse(measurements.isEmpty());
            assertTrue(measurements.get(0).hasAttribute("codename"));
            assertFalse(measurements.get(0).hasAttribute("code"));

            List<Element> names = getElementsFromDocumentUsingXpath(
                    dom, "//st:StationWithMeasurementCode/st:measurements/ms:MeasurementCode/ms:name");
            assertFalse(names.isEmpty());
            assertTrue(names.get(0).hasAttribute("code"));

        } catch (TransformerException | SAXException e) {
            throw new RuntimeException(e);
        }
    }
}
