/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2026, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.mbstyle.transform;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;
import javax.measure.Unit;
import javax.measure.quantity.Length;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.style.LineSymbolizer;
import org.geotools.api.style.Style;
import org.geotools.api.style.StyledLayerDescriptor;
import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.filter.function.EnvFunction;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.FeatureLayer;
import org.geotools.map.MapContent;
import org.geotools.mbstyle.MBStyle;
import org.geotools.mbstyle.MapboxTestUtils;
import org.geotools.mbstyle.function.ZoomLevelFunction;
import org.geotools.measure.Units;
import org.geotools.referencing.CRS;
import org.geotools.renderer.RenderListener;
import org.geotools.renderer.lite.StreamingRenderer;
import org.geotools.util.logging.Logging;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

/**
 * Reproduce the following error:
 *
 * <pre>
 * <code>
 * java.lang.ArrayIndexOutOfBoundsException: array exceeds maximum capacity !
 * at java.desktop/sun.java2d.marlin.ArrayCacheConst.getNewLargeSize(ArrayCacheConst.java:145)
 * at java.desktop/sun.java2d.marlin.Renderer.addLine(Renderer.java:400)
 * at java.desktop/sun.java2d.marlin.Renderer.closePath(Renderer.java:740)
 * at java.desktop/sun.java2d.marlin.Stroker.emitClose(Stroker.java:815)
 * at java.desktop/sun.java2d.marlin.Stroker.finish(Stroker.java:751)
 * at java.desktop/sun.java2d.marlin.Stroker._moveTo(Stroker.java:550)
 * at java.desktop/sun.java2d.marlin.Stroker.moveTo(Stroker.java:526)
 * at java.desktop/sun.java2d.marlin.Dasher.goTo(Dasher.java:320)
 * at java.desktop/sun.java2d.marlin.Dasher._lineTo(Dasher.java:480)
 * at java.desktop/sun.java2d.marlin.Dasher.lineTo(Dasher.java:427)
 * at java.desktop/sun.java2d.marlin.DMarlinRenderingEngine.pathToLoop(DMarlinRenderingEngine.java:777)
 * at java.desktop/sun.java2d.marlin.DMarlinRenderingEngine.pathTo(DMarlinRenderingEngine.java:737)
 * at java.desktop/sun.java2d.marlin.DMarlinRenderingEngine.strokeTo(DMarlinRenderingEngine.java:549)
 * at java.desktop/sun.java2d.marlin.DMarlinRenderingEngine.strokeTo(DMarlinRenderingEngine.java:309)
 * at java.desktop/sun.java2d.marlin.DMarlinRenderingEngine.getAATileGenerator(DMarlinRenderingEngine.java:982)
 * at java.desktop/sun.java2d.pipe.AAShapePipe.renderPath(AAShapePipe.java:147)
 * at java.desktop/sun.java2d.pipe.AAShapePipe.draw(AAShapePipe.java:78)
 * at java.desktop/sun.java2d.pipe.PixelToParallelogramConverter.draw(PixelToParallelogramConverter.java:148)
 * at java.desktop/sun.java2d.pipe.ValidatePipe.draw(ValidatePipe.java:154)
 * at java.desktop/sun.java2d.SunGraphics2D.draw(SunGraphics2D.java:2505)
 * at org.geotools.renderer.lite.StyledShapePainter.paintLineStyle(StyledShapePainter.java:347)
 * at org.geotools.renderer.lite.StyledShapePainter.paint(StyledShapePainter.java:263)
 * at org.geotools.renderer.lite.StreamingRenderer$PaintShapeRequest.execute(StreamingRenderer.java:3508)
 * at org.geotools.renderer.lite.StreamingRenderer$PainterThread.run(StreamingRenderer.java:3867)
 * at org.geoserver.wms.ThreadLocalTransferExecutor.lambda$0(ThreadLocalTransferExecutor.java:31)
 * at java.base/java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:545)
 * at java.base/java.util.concurrent.FutureTask.run(FutureTask.java:328)
 * at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1090)
 * at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:614)
 * at java.base/java.lang.Thread.run(Thread.java:1474)
 * 19 Jan 13:33:20 ERROR  [geoserver.ows] -
 * org.geoserver.platform.ServiceException: Rendering process failed. Layers: versatiles_file:streets
 * at org.geoserver.wms.map.RenderedImageMapOutputFormat.produceMap(RenderedImageMapOutputFormat.java:547)
 * at org.geoserver.wms.map.RenderedImageMapOutputFormat.produceMap(RenderedImageMapOutputFormat.java:197)
 * at org.geoserver.wms.map.RenderedImageMapOutputFormat.produceMap(RenderedImageMapOutputFormat.java:1)
 * at org.geoserver.wms.GetMap.executeInternal(GetMap.java:327)
 * at org.geoserver.wms.GetMap.run(GetMap.java:199)
 * at org.geoserver.wms.GetMap.run(GetMap.java:113)
 * </code>
 * </pre>
 */
public class DashArrayIntegrationTest {

    private static final Logger LOGGER = Logging.getLogger(DashArrayIntegrationTest.class);

    private static final String MBSTYLE =
            """
            {
              "version": 8,
              "name": "versatiles-colorful",
              "layers": [
                {
                  "source": "versatiles-shortbread",
                  "id": "tunnel-transport-rail",
                  "type": "line",
                  "source-layer": "streets",
                  "paint": {
                    "line-color": "rgb(197,204,211)",
                    "line-width": {
                      "stops": [[14,0],[15,1],[20,10]]
                    },
                    "line-dasharray": [
                      2,
                      2
                    ],
                    "line-opacity": {
                      "stops": [
                        [14,0],
                        [15,0.3
                        ]
                      ]
                    }
                  },
                  "minzoom": 14
                }
              ]
            }
            """;

    private static final String[] GEOMETRIES = {
        "LINESTRING (3067086.5189067367 7149409.050333023, 3066968.877542892 7149447.268847166, 3066804.657364935 7149498.027811261)",
        "LINESTRING (3066956.9342572223 7149440.102875764, 3066896.0235003075 7149458.614968551)",
        "LINESTRING (3066888.2603646223 7149434.728397212, 3066949.171121537 7149414.424811574)"
    };

    private static final long DISPLAY_TIME = 1000;

    private static final double DASH_TOLERANCE = 1e-9;

    private Style style;

    @Before
    public void setUp() throws org.json.simple.parser.ParseException {
        final double scaleDenominator = 17061.836668394135; // approximately zoom level 14
        EnvFunction.setGlobalValue("wms_scale_denominator", scaleDenominator);
        style = createStyle();
    }

    @After
    public void tearDown() {
        EnvFunction.removeGlobalValue("wms_scale_denominator");
    }

    /**
     * Tests that MBStyle line-dasharray expressions evaluate correctly at zoom levels where line-width interpolates to
     * near-zero values. At zoom ~14, line-width is ~0 (per the stops [[14,0],[15,1],[20,10]]), so dash values become
     * astronomically small. This verifies the expressions are constructed correctly - the actual rendering protection
     * against near-zero values is tested in SLDStyleFactoryTest.testDashArrayNearZero.
     */
    @Test
    public void testDashArrayExpressionValues() throws Exception {
        org.geotools.api.style.LineSymbolizer ls = (org.geotools.api.style.LineSymbolizer)
                style.featureTypeStyles().get(0).rules().get(0).symbolizers().get(0);

        // Verify UOM is set to pixel
        Unit<Length> unitOfMeasure = ls.getUnitOfMeasure();
        assertSame(Units.PIXEL, unitOfMeasure);

        // Verify dash array has 2 expressions (from line-dasharray: [2, 2])
        assertEquals(2, ls.getStroke().dashArray().size());

        // At zoom ~14, line-width interpolates to near-zero, so dash values should be near-zero
        // (dash = 2 * lineWidth, where lineWidth ~ 0 at z14)
        double strokeWidth = ls.getStroke().getWidth().evaluate(null, Double.class);
        assertTrue("Stroke width should be near zero at zoom 14", strokeWidth < 1e-6);

        double dash0 = ls.getStroke().dashArray().get(0).evaluate(null, Double.class);
        double dash1 = ls.getStroke().dashArray().get(1).evaluate(null, Double.class);
        assertTrue("Dash values should be near zero at zoom 14", dash0 < 1e-6);
        assertTrue("Dash values should be near zero at zoom 14", dash1 < 1e-6);

        // Verify the ratio is preserved (dash = 2 * lineWidth)
        assertEquals(2.0, dash0 / strokeWidth, 0.001);
        assertEquals(2.0, dash1 / strokeWidth, 0.001);
    }

    @Test
    public void testDashArrayWithStrokeWitdthCollapsedToNearZeroRendersOk() throws Exception {
        // bounds matching WMTS tile TileMatrix=EPSG:900913:15&TileCol=18891&TileRow=TileRow=10538
        final ReferencedEnvelope tileBounds = new ReferencedEnvelope(
                3066042.0781481937, 3067265.070600584, 7148390.884234618, 7149613.876687013, CRS.decode("EPSG:3857"));

        SimpleFeatureCollection collection = createStreetsCollection();

        MapContent mc = new MapContent();
        mc.addLayer(new FeatureLayer(collection, style));

        final List<SimpleFeature> rendered = new ArrayList<>();
        final List<Exception> errors = new ArrayList<>();

        RenderListener renderListener = new RenderListener() {
            public @Override void featureRenderer(SimpleFeature feature) {
                rendered.add(feature);
            }

            public @Override void errorOccurred(Exception e) {
                LOGGER.log(Level.SEVERE, "Unexpected rendering error", e);
                errors.add(e);
            }
        };

        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(mc);
        ReferencedEnvelope[] envelopes = {tileBounds};
        BufferedImage image;
        try {
            image = MapboxTestUtils.showRender("DashArray Test", renderer, DISPLAY_TIME, envelopes, renderListener);
        } finally {
            mc.dispose();
        }
        assertNotNull(image);

        assertEquals(GEOMETRIES.length, rendered.size());
        // Before the fix to SLDStyleFactory.allZeroes(float[] dashes) this fails with
        // java.lang.ArrayIndexOutOfBoundsException: array exceeds maximum capacity !
        // at java.desktop/sun.java2d.marlin.ArrayCacheConst.getNewLargeSize(ArrayCacheConst.java:145)
        assertEquals(0, errors.size());
    }

    private Style createStyle() throws org.json.simple.parser.ParseException {
        return createStyle(MBSTYLE);
    }

    private Style createStyle(String mbstyle) throws org.json.simple.parser.ParseException {
        JSONObject jsonObject = (JSONObject) new JSONParser().parse(mbstyle);
        StyledLayerDescriptor sld = new MBStyle(jsonObject).transform();
        return MapboxTestUtils.getStyle(sld, 0);
    }

    private static double scaleForZoom(int zoom) {
        return ZoomLevelFunction.EPSG_3857_O_SCALE / Math.pow(2, zoom);
    }

    private List<Expression> dashArrayOf(String layer) throws Exception {
        Style s = createStyle(MapboxTestUtils.style(layer));
        LineSymbolizer ls = (LineSymbolizer)
                s.featureTypeStyles().get(0).rules().get(0).symbolizers().get(0);
        return ls.getStroke().dashArray();
    }

    /** A zoom "step" between dash arrays becomes one dash expression per position, scaled by the line width. */
    @Test
    public void testZoomStepDashArray() throws Exception {
        String layer =
                """
                {
                  "id": "boundaries",
                  "type": "line",
                  "source": "src",
                  "source-layer": "x",
                  "paint": {
                    "line-color": "#000",
                    "line-width": 0.5,
                    "line-dasharray": [
                      "step",
                      ["zoom"],
                      ["literal", [2, 0]],
                      4,
                      ["literal", [2, 1]]
                    ]
                  }
                }""";
        List<Expression> dash = dashArrayOf(layer);
        assertEquals(2, dash.size());

        EnvFunction.setGlobalValue("wms_scale_denominator", scaleForZoom(2));
        assertEquals(1.0, dash.get(0).evaluate(null, Double.class), DASH_TOLERANCE);
        assertEquals(0.0, dash.get(1).evaluate(null, Double.class), DASH_TOLERANCE);

        EnvFunction.setGlobalValue("wms_scale_denominator", scaleForZoom(10));
        assertEquals(1.0, dash.get(0).evaluate(null, Double.class), DASH_TOLERANCE);
        assertEquals(0.5, dash.get(1).evaluate(null, Double.class), DASH_TOLERANCE);
    }

    /** Dash arrays of different lengths cannot be split by position, so the last one is used at every zoom. */
    @Test
    public void testUnequalLengthDashArray() throws Exception {
        String layer =
                """
                {
                  "id": "boundaries",
                  "type": "line",
                  "source": "src",
                  "source-layer": "x",
                  "paint": {
                    "line-color": "#000",
                    "line-width": 1,
                    "line-dasharray": [
                      "step",
                      ["zoom"],
                      ["literal", [2]],
                      4,
                      ["literal", [3, 1]]
                    ]
                  }
                }""";
        List<Expression> dash = dashArrayOf(layer);
        assertEquals(2, dash.size());
        EnvFunction.setGlobalValue("wms_scale_denominator", scaleForZoom(2));
        assertEquals(3.0, dash.get(0).evaluate(null, Double.class), DASH_TOLERANCE);
        assertEquals(1.0, dash.get(1).evaluate(null, Double.class), DASH_TOLERANCE);
        EnvFunction.setGlobalValue("wms_scale_denominator", scaleForZoom(10));
        assertEquals(3.0, dash.get(0).evaluate(null, Double.class), DASH_TOLERANCE);
        assertEquals(1.0, dash.get(1).evaluate(null, Double.class), DASH_TOLERANCE);
    }

    /** A "match" label that lists several values selects the same dash array for each of them. */
    @Test
    public void testMatchArrayLabelDashArray() throws Exception {
        String layer =
                """
                {
                  "id": "boundaries",
                  "type": "line",
                  "source": "src",
                  "source-layer": "x",
                  "paint": {
                    "line-color": "#000",
                    "line-width": 1,
                    "line-dasharray": [
                      "match",
                      ["get", "admin_level"],
                      [2, 4],
                      ["literal", [2, 1]],
                      ["literal", [1, 1]]
                    ]
                  }
                }""";
        List<Expression> dash = dashArrayOf(layer);
        assertEquals(2, dash.size());
        SimpleFeatureType type = DataUtilities.createType("boundaries", "admin_level:Long");
        SimpleFeature level4 = SimpleFeatureBuilder.build(type, new Object[] {4L}, "b.4");
        SimpleFeature level6 = SimpleFeatureBuilder.build(type, new Object[] {6L}, "b.6");
        assertEquals(2.0, dash.get(0).evaluate(level4, Double.class), DASH_TOLERANCE);
        assertEquals(1.0, dash.get(0).evaluate(level6, Double.class), DASH_TOLERANCE);
    }

    private SimpleFeatureCollection createStreetsCollection() throws SchemaException {
        SimpleFeatureType schema = DataUtilities.createType("streets", "geom:Geometry:srid=3857");
        List<SimpleFeature> features = IntStream.range(0, GEOMETRIES.length)
                .mapToObj(i -> feature(schema, i, GEOMETRIES[i]))
                .toList();
        return DataUtilities.collection(features);
    }

    private SimpleFeature feature(SimpleFeatureType schema, int id, String wkt) {
        SimpleFeatureBuilder builder = new SimpleFeatureBuilder(schema);
        Geometry geometry;
        try {
            geometry = geom(wkt);
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
        builder.set(schema.getGeometryDescriptor().getLocalName(), geometry);
        return builder.buildFeature(String.valueOf(id));
    }

    private Geometry geom(String wkt) throws ParseException {
        return new WKTReader().read(wkt);
    }
}
