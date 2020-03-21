/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.geojson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import org.geotools.data.DataUtilities;
import org.geotools.feature.FeatureCollection;
import org.geotools.test.TestData;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.io.ParseException;
import org.opengis.feature.Feature;
import org.opengis.feature.Property;

/** @author ian */
public class GeoJSONReaderTest {

    private final GeometryFactory gf = new GeometryFactory();

    /** Test method for {@link org.geotools.data.geojson.GeoJSONReader#getFeatures()}. */
    @Test
    public void testGetFeatures() throws IOException, ParseException {
        URL url = TestData.url(GeoJSONDataStore.class, "locations.json");

        try (GeoJSONReader reader = new GeoJSONReader(url)) {
            FeatureCollection features = reader.getFeatures();
            assertNotNull(features);
            assertEquals("wrong number of features read", 9, features.size());
        }
    }

    @Test
    public void testGetChangingSchema() throws IOException, ParseException {
        URL url = TestData.url(GeoJSONDataStore.class, "locations-changable.json");

        try (GeoJSONReader reader = new GeoJSONReader(url)) {
            FeatureCollection features = reader.getFeatures();
            assertNotNull(features);
            assertEquals("wrong number of features read", 9, features.size());

            HashMap<String, Object> expected = new HashMap<>();

            expected.put("LAT", 46.066667);
            expected.put("LON", 11.116667);
            expected.put("CITY", "Trento");
            expected.put("NUMBER", null);
            expected.put("YEAR", null);
            expected.put("the_geom", gf.createPoint(new Coordinate(11.117, 46.067)));
            Feature first = DataUtilities.first(features);
            for (Property prop : first.getProperties()) {
                assertEquals(expected.get(prop.getName().getLocalPart()), prop.getValue());
            }
        }
    }
}
