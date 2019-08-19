/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.mbstyle.source;

import static org.junit.Assert.*;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;
import org.geotools.mbstyle.MBStyle;
import org.geotools.mbstyle.MapboxTestUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;

public class StyleSourcesTest {

    MBStyle testStyle;

    @Before
    public void setUp() throws IOException, ParseException {
        JSONObject json = MapboxTestUtils.parseTestStyle("sourcesTest.json");
        testStyle = MBStyle.create(json);
    }

    @Test
    public void testIt() {
        Map<String, MBSource> sourceMap = testStyle.getSources();

        for (Entry<String, MBSource> e : sourceMap.entrySet()) {
            // System.out.println(e.getKey() + "(" + e.getValue().getClass().getSimpleName() + ")");
        }

        assertEquals(10, sourceMap.size());
    }

    @Test
    public void testGeoJsonDefaults() {
        Map<String, MBSource> sourceMap = testStyle.getSources();
        assertTrue(sourceMap.get("geojson-defaults") instanceof GeoJsonMBSource);
        GeoJsonMBSource geo = (GeoJsonMBSource) sourceMap.get("geojson-defaults");
        assertEquals("./lines.geojson", geo.getData());
        assertEquals("geojson", geo.getType());
        assertEquals(18, geo.getMaxZoom());
        assertEquals(128, geo.getBuffer());
        assertEquals(0.375, geo.getTolerance());
        assertEquals(false, geo.getCluster());
        assertEquals(50, geo.getClusterRadius());
        assertEquals(17, geo.getClusterMaxZoom());
    }

    @Test
    public void testImageMBSource() {
        Map<String, MBSource> sourceMap = testStyle.getSources();
        assertTrue(sourceMap.get("image") instanceof ImageMBSource);
        ImageMBSource image = (ImageMBSource) sourceMap.get("image");
        assertEquals("image", image.getType());
        assertEquals("/mapbox-gl-js/assets/radar.gif", image.getUrl());
        assertEquals(
                Arrays.asList(
                        new Point2D.Double(-80.425, 46.437),
                        new Point2D.Double(-71.516, 46.437),
                        new Point2D.Double(-71.516, 37.936),
                        new Point2D.Double(-80.425, 37.936)),
                image.getCoordinates());
    }

    @Test
    public void testCanvasMBSource() {
        Map<String, MBSource> sourceMap = testStyle.getSources();
        assertTrue(sourceMap.get("canvas") instanceof CanvasMBSource);
        CanvasMBSource canvas = (CanvasMBSource) sourceMap.get("canvas");
        assertEquals("canvas", canvas.getType());
        assertEquals(true, canvas.getAnimate());
        assertEquals(
                Arrays.asList(
                        new Point2D.Double(-122.51596391201019, 37.56238816766053),
                        new Point2D.Double(-122.51467645168304, 37.56410183312965),
                        new Point2D.Double(-122.51309394836426, 37.563391708549425),
                        new Point2D.Double(-122.51423120498657, 37.56161849366671)),
                canvas.getCoordinates());
        assertEquals("mycanvas", canvas.getCanvas());
    }

    @Test
    public void testGeoJsonInlineMBSource() {
        Map<String, MBSource> sourceMap = testStyle.getSources();
        assertTrue(sourceMap.get("geojson-inline") instanceof GeoJsonMBSource);
        GeoJsonMBSource geo = (GeoJsonMBSource) sourceMap.get("geojson-inline");
        assertTrue(geo.getData() instanceof JSONObject);
        assertEquals("geojson", geo.getType());
        assertEquals(17, geo.getMaxZoom().intValue());
        assertEquals(0, geo.getBuffer().intValue());
        assertEquals(0.5, geo.getTolerance());
        assertEquals(true, geo.getCluster());
        assertEquals(512, geo.getClusterRadius().intValue());
        assertEquals(10, geo.getClusterMaxZoom().intValue());
    }

    @Test
    public void testVectorUrlMBSource() {
        Map<String, MBSource> sourceMap = testStyle.getSources();
        assertTrue(sourceMap.get("vector-url") instanceof VectorMBSource);
        VectorMBSource vector = (VectorMBSource) sourceMap.get("vector-url");
        assertEquals("vector", vector.getType());
        assertEquals("mapbox://mapbox.mapbox-streets-v6", vector.getUrl());
        assertEquals(0, vector.getMinZoom().intValue()); // Default
        assertEquals(22, vector.getMaxZoom().intValue()); // Default
    }

    @Test
    public void testVectorMBSource() {
        Map<String, MBSource> sourceMap = testStyle.getSources();
        assertTrue(sourceMap.get("vector-tiles") instanceof VectorMBSource);
        VectorMBSource vector = (VectorMBSource) sourceMap.get("vector-tiles");
        assertEquals("vector", vector.getType());
        assertEquals(null, vector.getUrl());
        assertEquals(10, vector.getMinZoom().intValue());
        assertEquals(14, vector.getMaxZoom().intValue());
        assertEquals(
                Arrays.asList(
                        "http://a.example.com/tiles/{z}/{x}/{y}.pbf",
                        "http://b.example.com/tiles/{z}/{x}/{y}.pbf"),
                vector.getTiles());
    }

    @Test
    public void testVideoMBSource() {
        Map<String, MBSource> sourceMap = testStyle.getSources();
        assertTrue(sourceMap.get("video") instanceof VideoMBSource);
        VideoMBSource video = (VideoMBSource) sourceMap.get("video");
        assertEquals("video", video.getType());
        assertEquals(
                Arrays.asList(
                        "https://www.mapbox.com/drone/video/drone.mp4",
                        "https://www.mapbox.com/drone/video/drone.webm"),
                video.getUrls());
        assertEquals(
                Arrays.asList(
                        new Point2D.Double(-122.51596391201019, 37.56238816766053),
                        new Point2D.Double(-122.51467645168304, 37.56410183312965),
                        new Point2D.Double(-122.51309394836426, 37.563391708549425),
                        new Point2D.Double(-122.51423120498657, 37.56161849366671)),
                video.getCoordinates());
    }

    @Test
    public void testRasterDefaultsMBSource() {
        Map<String, MBSource> sourceMap = testStyle.getSources();
        assertTrue(sourceMap.get("raster-defaults") instanceof RasterMBSource);
        RasterMBSource raster = (RasterMBSource) sourceMap.get("raster-defaults");
        assertEquals("raster", raster.getType());
        assertEquals("mapbox://mapbox.satellite", raster.getUrl());
        assertEquals(0, raster.getMinZoom().intValue());
        assertEquals(22, raster.getMaxZoom().intValue());
        assertEquals(512, raster.getTileSize().intValue());
    }

    @Test
    public void testRasterMBSource() {
        Map<String, MBSource> sourceMap = testStyle.getSources();
        assertTrue(sourceMap.get("raster") instanceof RasterMBSource);
        RasterMBSource raster = (RasterMBSource) sourceMap.get("raster");
        assertEquals("raster", raster.getType());
        assertNull(raster.getUrl());
        assertEquals(1, raster.getMinZoom().intValue());
        assertEquals(19, raster.getMaxZoom().intValue());
        assertEquals(256, raster.getTileSize().intValue());
        assertEquals(
                Arrays.asList(
                        "http://a.example.com/wms?bbox={bbox-epsg-3857}&format=image/png&service=WMS&version=1.1.1&request=GetMap&srs=EPSG:3857&width=256&height=256&layers=example"),
                raster.getTiles());
    }
}
