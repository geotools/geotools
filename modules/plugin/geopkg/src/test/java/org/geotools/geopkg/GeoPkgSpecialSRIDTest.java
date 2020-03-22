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
package org.geotools.geopkg;

import static org.geotools.referencing.crs.DefaultEngineeringCRS.GENERIC_2D;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.Collections;
import java.util.Map;
import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.jdbc.JDBCDataStore;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

public class GeoPkgSpecialSRIDTest {

    private static final File GENERIC_CRS_GPKG = new File("target/generic_crs.gpkg");

    @Before
    public void cleanup() {
        GENERIC_CRS_GPKG.delete();
    }

    @Test
    public void testReadUnknownGeographic() throws Exception {
        File lakes0 = new File("./src/test/resources/org/geotools/geopkg/lakes_srs_0.gpkg");
        Map<String, File> params = Collections.singletonMap("database", lakes0);
        JDBCDataStore store = new GeoPkgDataStoreFactory().createDataStore(params);
        try {
            // check the schema
            SimpleFeatureType lakes = store.getSchema("lakes_null");
            assertEquals(GENERIC_2D, lakes.getCoordinateReferenceSystem());

            // check the features
            SimpleFeature first =
                    DataUtilities.first(store.getFeatureSource("lakes_null").getFeatures());
            assertEquals(GENERIC_2D, ((Geometry) first.getDefaultGeometry()).getUserData());
        } finally {
            if (store != null) store.dispose();
        }
    }

    @Test
    public void testWriteGenericCRS() throws Exception {
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.add("id", Integer.class);
        tb.add("geom", LineString.class, GENERIC_2D);
        tb.add("name", String.class);
        tb.setName("road");
        SimpleFeatureType roadType = tb.buildFeatureType();
        GeometryFactory gf = new GeometryFactory();
        SimpleFeature[] roadFeatures = new SimpleFeature[3];
        roadFeatures[0] =
                SimpleFeatureBuilder.build(
                        roadType,
                        new Object[] {
                            Integer.valueOf(1), line(gf, new int[] {1, 1, 2, 2, 4, 2, 5, 1}), "r1",
                        },
                        "road.rd1");

        Map<String, File> params = Collections.singletonMap("database", GENERIC_CRS_GPKG);
        JDBCDataStore store = new GeoPkgDataStoreFactory().createDataStore(params);
        try {
            store.createSchema(roadType);
            SimpleFeatureStore road = (SimpleFeatureStore) store.getFeatureSource("road");
            road.addFeatures(DataUtilities.collection(roadFeatures));

            // check -1
            assertEquals(GENERIC_2D, store.getSchema("road").getCoordinateReferenceSystem());
        } finally {
            if (store != null) store.dispose();
        }
    }

    /**
     * Creates a line from the specified (<var>x</var>,<var>y</var>) coordinates. The coordinates
     * are stored in a flat array.
     */
    public LineString line(GeometryFactory gf, int[] xy) {
        Coordinate[] coords = new Coordinate[xy.length / 2];

        for (int i = 0; i < xy.length; i += 2) {
            coords[i / 2] = new Coordinate(xy[i], xy[i + 1]);
        }

        return gf.createLineString(coords);
    }
}
