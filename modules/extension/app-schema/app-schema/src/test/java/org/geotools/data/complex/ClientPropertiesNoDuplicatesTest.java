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
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;
import javax.xml.transform.TransformerException;
import org.geotools.api.data.FeatureSource;
import org.geotools.api.feature.Feature;
import org.geotools.api.filter.Id;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.gml3.GML;
import org.geotools.gml3.GMLConfiguration;
import org.geotools.xsd.Encoder;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class ClientPropertiesNoDuplicatesTest extends AbstractStationsTest {

    static {
        STATIONS_SCHEMA_BASE = "/test-data/stations-nestednogml/";
    }

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        loadDataAccesses();
    }

    protected static void loadDataAccesses() throws Exception {
        /** Load measurements data access */
        measurementsDataAccess = loadDataAccess("measurementsDefaultGeometry.xml");

        /** Load stations data access */
        stationsDataAccess = loadDataAccess("stationsDefaultGeometry.xml");
    }

    public ClientPropertiesNoDuplicatesTest() {
        super();
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
