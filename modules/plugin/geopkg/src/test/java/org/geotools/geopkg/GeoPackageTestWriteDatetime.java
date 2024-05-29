/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2024, Open Source Geospatial Foundation (OSGeo)
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
 *
 *    @author Stijn Goedertier
 */
package org.geotools.geopkg;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.Instant;
import org.apache.commons.io.FileUtils;
import org.geotools.api.data.SimpleFeatureReader;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.util.factory.Hints;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;

public class GeoPackageTestWriteDatetime {

    GeoPackage geopkg;

    @BeforeClass
    public static void setUpOnce() {
        Hints.putSystemDefault(Hints.COMPARISON_TOLERANCE, 1e-9);
    }

    @Before
    public void setUp() throws Exception {
        geopkg = new GeoPackage(File.createTempFile("geopkg", "db", new File("target")));
        geopkg.init();
    }

    @After
    public void tearDown() throws Exception {
        geopkg.close();

        // for debugging, copy the current geopackage file to a well known file
        File f = new File("target", "geopkg.gpkg");
        if (f.exists()) {
            f.delete();
        }

        FileUtils.copyFile(geopkg.getFile(), f);
    }

    private SimpleFeatureType createFeatureTypeWithAttribute(
            String featureName, String attributeName, Class<?> attributeClazz) {
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName(featureName);
        builder.setCRS(DefaultGeographicCRS.WGS84);
        builder.add("the_geom", LineString.class);
        builder.add(attributeName, attributeClazz);
        return builder.buildFeatureType();
    }

    private Geometry createGeometry() {
        return new GeometryFactory()
                .createLineString(
                        new Coordinate[] {
                            new Coordinate(0.1, 0.1), new Coordinate(0.2, 0.2),
                        });
    }

    private SimpleFeature createSimpleFeatureWithValue(
            SimpleFeatureBuilder featureBuilder, Object value) {
        featureBuilder.add(createGeometry());
        featureBuilder.add(value);
        return featureBuilder.buildFeature(null);
    }

    @Test
    public void testDatetime() throws Exception {
        String featureName = "my_feature";
        String attributeName = "my_datetime_attribute";

        // create explicitly GMT timestamp
        Instant instant = Instant.parse("2024-02-27T08:13:00.0Z");
        Timestamp timestamp = Timestamp.from(instant);

        SimpleFeatureType featureType =
                createFeatureTypeWithAttribute(featureName, attributeName, Timestamp.class);
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(featureType);
        SimpleFeature simpleFeature = createSimpleFeatureWithValue(featureBuilder, timestamp);
        SimpleFeatureCollection collection = DataUtilities.collection(simpleFeature);
        FeatureEntry entry = new FeatureEntry();
        geopkg.add(entry, collection);

        FeatureEntry readFeature = geopkg.features().get(0);
        try (SimpleFeatureReader reader = geopkg.reader(readFeature, null, null)) {
            Object attribute = reader.next().getAttribute(attributeName);

            assertTrue(attribute instanceof Timestamp);
            Timestamp readValue = (Timestamp) attribute;
            assertIsDatetimeColumn(featureName, attributeName);
            assertEquals(timestamp, readValue);
        }
    }

    // timestamp is null
    @Test
    public void testDatetimeNull() throws Exception {
        String featureName = "my_feature";
        String attributeName = "my_datetime_attribute";

        Timestamp timestamp = null;

        SimpleFeatureType featureType =
                createFeatureTypeWithAttribute(featureName, attributeName, Timestamp.class);
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(featureType);
        SimpleFeature simpleFeature = createSimpleFeatureWithValue(featureBuilder, timestamp);
        SimpleFeatureCollection collection = DataUtilities.collection(simpleFeature);
        FeatureEntry entry = new FeatureEntry();
        geopkg.add(entry, collection);

        FeatureEntry readFeature = geopkg.features().get(0);
        try (SimpleFeatureReader reader = geopkg.reader(readFeature, null, null)) {
            Object attribute = reader.next().getAttribute(attributeName);

            assertNull(attribute);
        }
    }

    void assertIsDatetimeColumn(String table, String column) throws Exception {
        String query =
                "SELECT type AS data_type" + " FROM pragma_table_info(?)" + " WHERE name = ?";
        try (Connection cx = geopkg.getDataSource().getConnection();
                PreparedStatement ps = cx.prepareStatement(query)) {
            ps.setString(1, table);
            ps.setString(2, column);
            try (ResultSet rs = ps.executeQuery(); ) {
                assertEquals("DATETIME", rs.getString(1));
            }
        }
    }
}
