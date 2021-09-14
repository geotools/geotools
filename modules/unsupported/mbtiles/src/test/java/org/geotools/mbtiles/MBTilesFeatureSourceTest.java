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

import static org.geotools.mbtiles.MBTilesFile.SPHERICAL_MERCATOR;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.commons.io.IOUtils;
import org.geotools.data.DataStore;
import org.geotools.data.DataUtilities;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.store.ContentFeatureCollection;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.util.URLs;
import org.geotools.util.factory.Hints;
import org.hamcrest.Matchers;
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
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.spatial.BBOX;

public class MBTilesFeatureSourceTest {
    static final FilterFactory2 FF = CommonFactoryFinder.getFilterFactory2();
    private static final PropertyName DEFAULT_GEOM = FF.property("");

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
        assertThat(
                ((Float) feature.getAttribute("float_value")).doubleValue(), closeTo(1.25, 0.01));
        assertThat(feature.getAttribute("int64_value"), equalTo(123456789012345L));
        assertThat(feature.getAttribute("neg_int_value"), equalTo(-1L));
        assertThat(feature.getAttribute("pos_int_value"), equalTo(1L));
        assertThat(feature.getAttribute("string_value"), equalTo("str"));
        Point expected = (Point) new WKTReader().read("POINT (215246.671651058 6281289.23636264)");
        Point actual = (Point) feature.getDefaultGeometry();
        assertTrue(actual.equalsExact(expected, 0.01));
    }

    @Test
    public void readSinglePolygon() throws IOException, ParseException {
        MBTilesFeatureSource fs = getMadagascarSource("water");
        BBOX bbox = getMercatorBoxFilter(5635550, 5948635, -1565430, -1252345);
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
        List<RectangleLong> bounds = fs.getTileBoundsFor(new Query("water"), 7);
        assertThat(bounds, contains(new RectangleLong(0, 127, 0, 127)));
    }

    @Test
    public void queryWorldBounds() throws IOException, SQLException {
        MBTilesFeatureSource fs = getMadagascarSource("water");
        BBOX bbox = FF.bbox(DEFAULT_GEOM, MBTilesFile.WORLD_ENVELOPE);
        List<RectangleLong> bounds = fs.getTileBoundsFor(new Query("water", bbox), 7);
        assertThat(bounds, contains(new RectangleLong(0, 127, 0, 127)));
    }

    @Test
    public void querySingleTile() throws IOException, SQLException {
        MBTilesFeatureSource fs = getMadagascarSource("water");
        BBOX bbox = getMercatorBoxFilter(5635550, 5948635, -1565430, -1252345);
        List<RectangleLong> bounds = fs.getTileBoundsFor(new Query("water", bbox), 7);
        assertThat(bounds, contains(new RectangleLong(82, 82, 59, 59)));
    }

    @Test
    public void queryTwoTiles() throws IOException, SQLException {
        MBTilesFeatureSource fs = getMadagascarSource("water");
        BBOX bbox = getMercatorBoxFilter(5635550, 5948637, -1565430, -1252344);
        List<RectangleLong> bounds = fs.getTileBoundsFor(new Query("water", bbox), 7);
        assertThat(bounds, contains(new RectangleLong(82, 83, 59, 60)));
    }

    private MBTilesFeatureSource getMadagascarSource(String typeName) throws IOException {
        if (this.store == null) {
            File file =
                    URLs.urlToFile(
                            MBTilesFileVectorTileTest.class.getResource("madagascar.mbtiles"));
            this.store = new MBTilesDataStore(new MBTilesFile(file));
        }
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
                                        StandardCharsets.UTF_8));
        Geometry actual = (Geometry) feature.getDefaultGeometry();
        // there is some difference in size, but nothing major (200k square meters are a square
        // with a side of less than 500 meters, against a tile that covers 1/4 of the planet
        assertEquals(0, expected.difference(actual).getArea(), 200000);
        assertEquals(0, actual.difference(expected).getArea(), 200000);
    }

    // make it work with clients passing the distance hint instead of the simplification one
    @Test
    public void testGetLowerResolutionDistance() throws IOException, ParseException {
        MBTilesFeatureSource fs = getMadagascarSource("water");
        Query query = new Query("water", FF.equal(FF.property("class"), FF.literal("ocean"), true));
        query.setHints(new Hints(Hints.GEOMETRY_DISTANCE, 78271d));
        ContentFeatureCollection fc = fs.getFeatures(query);
        assertEquals(1, fc.size());
        SimpleFeature feature = DataUtilities.first(fc);
        // this one got from ogrinfo on the tile
        Geometry expected =
                new WKTReader()
                        .read(
                                IOUtils.toString(
                                        getClass().getResourceAsStream("ocean_1_0_1.wkt"),
                                        StandardCharsets.UTF_8));
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
        checkFirstFeature("boundary", "admin_level", 2L);
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

    @Test
    public void testReadCache() throws Exception {
        Set<MBTilesRange> rangesRead = new HashSet<>();
        this.store = getMadagascarRangeReadRecorder(rangesRead);
        SimpleFeatureSource water = store.getFeatureSource("water");

        // read from a single tile, should kick a disk read
        BBOX bbox = getMercatorBoxFilter(5700000, 5900000, -1500000, -1300000);
        SimpleFeatureCollection fc = water.getFeatures(new Query("water", bbox));
        assertEquals(1, countByVisit(fc));
        assertThat(rangesRead, Matchers.contains(new MBTilesRange(7, 82, 82, 59, 59)));

        // read it again, this time no disk reads should be performed, it's all in the tile cache
        rangesRead.clear();
        assertEquals(1, countByVisit(fc));
        assertThat(rangesRead, Matchers.empty());

        // perform new read, making area larger towards east, should read one more tile, but not the
        // previous one
        rangesRead.clear();
        bbox = getMercatorBoxFilter(5500000, 5900000, -1500000, -1300000);
        fc = water.getFeatures(new Query("water", bbox));
        assertEquals(2, countByVisit(fc));
        assertThat(rangesRead, Matchers.contains(new MBTilesRange(7, 81, 81, 59, 59)));

        // same as above but different layer, again the cache should kick in
        rangesRead.clear();
        fc = store.getFeatureSource("landcover").getFeatures(new Query("landcover", bbox));
        assertEquals(2, countByVisit(fc));
        assertThat(rangesRead, Matchers.empty());
    }

    @Test
    public void testReadSeparateBounds() throws Exception {
        Set<MBTilesRange> rangesRead = new HashSet<>();
        this.store = getMadagascarRangeReadRecorder(rangesRead);
        SimpleFeatureSource water = store.getFeatureSource("water");

        // read from two well separate bounding boxes
        BBOX bbox1 = getMercatorBoxFilter(5700000, 5900000, -1500000, -1300000);
        BBOX bbox2 = getMercatorBoxFilter(5700000, 5900000, -2500000, -2100000);
        SimpleFeatureCollection fc = water.getFeatures(new Query("water", FF.or(bbox1, bbox2)));
        assertEquals(3, countByVisit(fc));
        assertThat(
                rangesRead,
                Matchers.contains(
                        new MBTilesRange(7, 82, 82, 59, 59), new MBTilesRange(7, 82, 82, 56, 57)));
    }

    @Test
    public void testReadSeparateBoundsSmallGap() throws Exception {
        Set<MBTilesRange> rangesRead = new HashSet<>();
        this.store = getMadagascarRangeReadRecorder(rangesRead);
        SimpleFeatureSource water = store.getFeatureSource("water");

        // read from two bounding boxes with a small gap, resulting in a single tile range to read
        BBOX bbox1 = getMercatorBoxFilter(5700000, 5900000, -1500000, -1300000);
        BBOX bbox2 = getMercatorBoxFilter(5700000, 5900000, -1700000, -1550000);
        SimpleFeatureCollection fc = water.getFeatures(new Query("water", FF.or(bbox1, bbox2)));
        assertEquals(2, countByVisit(fc));
        assertThat(rangesRead, Matchers.contains(new MBTilesRange(7, 82, 82, 58, 59)));
    }

    @Test
    public void testReadOutsideWorld() throws Exception {
        Set<MBTilesRange> rangesRead = new HashSet<>();
        this.store = getMadagascarRangeReadRecorder(rangesRead);
        SimpleFeatureSource water = store.getFeatureSource("water");

        // try to read from an invalid coordinate set (past dateline, no APH here)
        BBOX bbox = getMercatorBoxFilter(25000000, 26000000, -1500000, -1300000);
        SimpleFeatureCollection fc = water.getFeatures(new Query("water", bbox));
        assertEquals(0, countByVisit(fc));
        // should not have read anything, outside valid tile range
        assertThat(rangesRead, Matchers.empty());
    }

    @Test
    public void testGetBBOXQueryALL() throws Exception {
        File file =
                URLs.urlToFile(MBTilesFileVectorTileTest.class.getResource("madagascar.mbtiles"));
        this.store = new MBTilesDataStore(new MBTilesFile(file));
        ReferencedEnvelope envelope = store.getFeatureSource("water").getBounds();
        assertNotNull(envelope);
        assertEquals(
                new ReferencedEnvelope(
                        -2.0037508342789244E7,
                        2.0037508342789244E7,
                        -2.0037471205137067E7,
                        2.003747120513706E7,
                        CRS.decode("EPSG:3857", true)),
                envelope);
    }

    @Test
    public void testGetBBoxWithFilter() throws IOException {
        MBTilesFeatureSource fs = getMadagascarSource("water");
        assertEquals("water", fs.getSchema().getTypeName());
        Filter filter = FF.equal(FF.property("class"), FF.literal("ocean"), true);
        Query query = new Query("water", filter);
        ReferencedEnvelope envelope = fs.getBounds(query);
        // expecting null if filter is provided since bbox won't be
        // get by mbtiles file metadata
        assertNull(envelope);
    }

    private BBOX getMercatorBoxFilter(double minX, double maxX, double minY, double maxY) {
        return FF.bbox(
                DEFAULT_GEOM, new ReferencedEnvelope(minX, maxX, minY, maxY, SPHERICAL_MERCATOR));
    }

    private MBTilesDataStore getMadagascarRangeReadRecorder(Set<MBTilesRange> rangesRead)
            throws IOException {
        File file =
                URLs.urlToFile(MBTilesFileVectorTileTest.class.getResource("madagascar.mbtiles"));
        MBTilesFile mbtiles =
                new MBTilesFile(file) {
                    @Override
                    public TileIterator tiles(
                            long zoomLevel,
                            long leftTile,
                            long bottomTile,
                            long rightTile,
                            long topTile)
                            throws SQLException {
                        rangesRead.add(
                                new MBTilesRange(
                                        zoomLevel, leftTile, rightTile, bottomTile, topTile));
                        return super.tiles(zoomLevel, leftTile, bottomTile, rightTile, topTile);
                    }
                };
        return new MBTilesDataStore(mbtiles);
    }

    private int countByVisit(SimpleFeatureCollection fc) throws IOException {
        AtomicInteger count = new AtomicInteger();
        fc.accepts(f -> count.incrementAndGet(), null);
        return count.get();
    }
}
