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
package org.geotools.mbtiles;

import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.SQLException;
import org.apache.commons.io.IOUtils;
import org.geotools.data.DataStore;
import org.geotools.data.DataUtilities;
import org.geotools.data.Query;
import org.geotools.data.store.ContentFeatureCollection;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.util.URLs;
import org.geotools.util.factory.Hints;
import org.junit.After;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.spatial.BBOX;

public class MBTilesFeatureSourceTest {
    static final FilterFactory2 FF = CommonFactoryFinder.getFilterFactory2();

    DataStore store;

    @After
    public void disposeStore() {
        if (store != null) {
            store.dispose();
        }
    }

    @Test
    public void readSinglePointDataTypes() throws IOException, ParseException {
        File file =
                URLs.urlToFile(MBTilesFileVectorTileTest.class.getResource("datatypes.mbtiles"));
        this.store = new MBTilesDataStore(new MBTilesFile(file));
        SimpleFeature feature =
                DataUtilities.first(store.getFeatureSource("datatypes").getFeatures(Query.ALL));
        assertThat(feature.getAttribute("bool_false"), equalTo(false));
        assertThat(feature.getAttribute("bool_true"), equalTo(true));
        assertThat((Double) feature.getAttribute("float_value"), closeTo(1.25, 0.01));
        assertThat((Double) feature.getAttribute("int64_value"), closeTo(123456789012345d, 1));
        assertThat((Double) feature.getAttribute("neg_int_value"), closeTo(-1, 0));
        assertThat((Double) feature.getAttribute("pos_int_value"), closeTo(1, 1.23456789));
        assertThat(feature.getAttribute("string_value"), equalTo("str"));
        Point expected = (Point) new WKTReader().read("POINT (215246.671651058 6281289.23636264)");
        Point actual = (Point) feature.getDefaultGeometry();
        assertTrue(actual.equalsExact(expected, 0.01));
    }

    @Test
    public void readSinglePolygon() throws IOException, ParseException {
        MBTilesFeatureSource fs = getMadagascarSource("water");
        BBOX bbox =
                FF.bbox(
                        FF.property(""),
                        new ReferencedEnvelope(
                                5635550,
                                5948635,
                                -1565430,
                                -1252345,
                                MBTilesFile.SPHERICAL_MERCATOR));
        ContentFeatureCollection fc = fs.getFeatures(new Query("water", bbox));
        assertEquals(1, fc.size());
        SimpleFeature feature = DataUtilities.first(fc);
        assertThat(feature.getAttribute("class"), equalTo("ocean"));
        String wkt =
                "POLYGON ((5953527.258247068 -1570322.3088720688, 5630657.250815428 -1570322.3088720688, 5630657.250815428 -1247452.301440428, 5953527.258247068 -1247452.301440428, 5953527.258247068 -1570322.3088720688))";
        Polygon expected = (Polygon) new WKTReader().read(wkt);
        Polygon actual = (Polygon) feature.getDefaultGeometry();
        assertTrue(
                "Expected:\n" + expected + "\nBut got:\n" + actual,
                actual.equalsExact(expected, 0.1));

        // check the clip mask
        Geometry clip = (Geometry) feature.getUserData().get(Hints.GEOMETRY_CLIP);
        String clipWkt =
                "POLYGON ((5635549.220624998 -1565430.3390625007, 5948635.288437498 -1565430.3390625007, 5948635.288437498 -1252344.2712500007, 5635549.220624998 -1252344.2712500007, 5635549.220624998 -1565430.3390625007))";
        Polygon expectedClip = (Polygon) new WKTReader().read(clipWkt);
        assertTrue(
                "Expected:\n" + expectedClip + "\nBut got:\n" + clip,
                clip.equalsExact(expectedClip, 0.1));
    }

    @Test
    public void queryAllBounds() throws IOException, SQLException {
        MBTilesFeatureSource fs = getMadagascarSource("water");
        RectangleLong bounds = fs.getTileBoundsFor(new Query("water"), 7);
        assertEquals(new RectangleLong(79, 82, 54, 59), bounds);
    }

    @Test
    public void queryWorldBounds() throws IOException, SQLException {
        MBTilesFeatureSource fs = getMadagascarSource("water");
        BBOX bbox = FF.bbox(FF.property(""), MBTilesFile.WORLD_ENVELOPE);
        RectangleLong bounds = fs.getTileBoundsFor(new Query("water", bbox), 7);
        assertEquals(new RectangleLong(0, 128, 0, 128), bounds);
    }

    @Test
    public void querySingleTile() throws IOException, SQLException {
        MBTilesFeatureSource fs = getMadagascarSource("water");
        BBOX bbox =
                FF.bbox(
                        FF.property(""),
                        new ReferencedEnvelope(
                                5635550,
                                5948635,
                                -1565430,
                                -1252345,
                                MBTilesFile.SPHERICAL_MERCATOR));
        RectangleLong bounds = fs.getTileBoundsFor(new Query("water", bbox), 7);
        assertEquals(new RectangleLong(82, 82, 59, 59), bounds);
    }

    @Test
    public void queryTwoTiles() throws IOException, SQLException {
        MBTilesFeatureSource fs = getMadagascarSource("water");
        BBOX bbox =
                FF.bbox(
                        FF.property(""),
                        new ReferencedEnvelope(
                                5635550,
                                5948637,
                                -1565430,
                                -1252344,
                                MBTilesFile.SPHERICAL_MERCATOR));
        RectangleLong bounds = fs.getTileBoundsFor(new Query("water", bbox), 7);
        assertEquals(new RectangleLong(82, 83, 59, 60), bounds);
    }

    private MBTilesFeatureSource getMadagascarSource(String typeName) throws IOException {
        File file =
                URLs.urlToFile(MBTilesFileVectorTileTest.class.getResource("madagascar.mbtiles"));
        this.store = new MBTilesDataStore(new MBTilesFile(file));
        return (MBTilesFeatureSource) store.getFeatureSource(typeName);
    }

    @Test
    public void testGetLowerResolution() throws IOException, ParseException {
        MBTilesFeatureSource fs = getMadagascarSource("water");
        Query query = new Query("water", FF.equal(FF.property("class"), FF.literal("ocean"), true));
        query.setHints(new Hints(Hints.GEOMETRY_SIMPLIFICATION, 78271d));
        ContentFeatureCollection fc = fs.getFeatures(query);
        assertEquals(1, fc.size());
        SimpleFeature feature = DataUtilities.first(fc);
        // this one got from ogrinfo on the tile
        Geometry expected =
                new WKTReader()
                        .read(
                                IOUtils.toString(
                                        getClass().getResourceAsStream("ocean_1_0_1.wkt"),
                                        Charset.forName("UTF8")));
        Geometry actual = (Geometry) feature.getDefaultGeometry();
        // there is some difference in size, but nothing major (200k square meters are a square
        // with a side of less than 500 meters, against a tile that covers 1/4 of the planet
        assertEquals(0, expected.difference(actual).getArea(), 200000);
        assertEquals(0, actual.difference(expected).getArea(), 200000);
    }

    @Test
    public void testReadMultipleLayers() throws IOException, ParseException {
        // a bug was causing multiple layers to return the same schema, checking that along with a
        // few extra basic consistency properties
        checkFirstFeature("water", "class", "ocean");
        checkFirstFeature("water", "class", "lake");
        checkFirstFeature("landcover", "class", "ice");
        checkFirstFeature("boundary", "admin_level", 2d);
        checkFirstFeature("place", "name", "Madagascar");
    }

    public void checkFirstFeature(String layerName, String propertyName, Object propertyValue)
            throws IOException {
        MBTilesFeatureSource fs = getMadagascarSource(layerName);
        assertEquals(layerName, fs.getSchema().getTypeName());
        Filter filter = FF.equal(FF.property(propertyName), FF.literal(propertyValue), true);
        Query query = new Query(layerName, filter);
        query.setHints(new Hints(Hints.GEOMETRY_SIMPLIFICATION, 78271d));
        ContentFeatureCollection fc = fs.getFeatures(query);
        SimpleFeature feature = DataUtilities.first(fc);
        assertNotNull("Could not find a feature with filter " + filter, feature);
        // check schema consistency
        assertEquals(feature.getFeatureType(), fc.getSchema());
        assertEquals(propertyValue, feature.getAttribute(propertyName));
    }

    @Test
    public void testReadFeaturesNotFound() throws IOException {
        // tests scanning over tiles and never finding a feature
        String layerName = "aerodrome_label";
        MBTilesFeatureSource fs = getMadagascarSource(layerName);
        ContentFeatureCollection fc = fs.getFeatures(new Query(layerName));
        assertNull(DataUtilities.first(fc));
    }
}
