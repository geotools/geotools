/*
 * GeoTools - The Open Source Java GIS Toolkit
 * http://geotools.org
 *
 * (C) 2017, Open Source Geospatial Foundation (OSGeo)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation;
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 */
package org.geotools.renderer.lite;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import org.geotools.TestData;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.data.FeatureSource;
import org.geotools.data.Query;
import org.geotools.data.property.PropertyDataStore;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.gce.geotiff.GeoTiffReader;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.image.ImageWorker;
import org.geotools.map.*;
import org.geotools.referencing.CRS;
import org.geotools.renderer.RenderListener;
import org.geotools.styling.Style;
import org.geotools.util.factory.GeoTools;
import org.geotools.util.factory.Hints;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opengis.coverage.grid.Format;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CRSAuthorityFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public class RenderingTransformationTest {

    private static final long TIME = 4000;

    @BeforeClass
    public static void setup() {
        // System.setProperty("org.geotools.test.interactive", "true");
        System.setProperty(GeoTools.FORCE_LONGITUDE_FIRST_AXIS_ORDER, "false");
        CRS.reset("all");
    }

    @AfterClass
    public static void tearDown() {
        Hints.removeSystemDefault(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER);
    }

    @Test
    public void testTransformReprojectedGridReader() throws Exception {
        Style style = RendererBaseTest.loadStyle(this, "coverageCenter.sld");

        GeoTiffReader reader = new GeoTiffReader(TestData.copy(this, "geotiff/world.tiff"));

        MapContent mc = new MapContent();
        mc.addLayer(new GridReaderLayer(reader, style));

        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(mc);

        ReferencedEnvelope reWgs84 =
                new ReferencedEnvelope(-70, 70, -160, 160, CRS.decode("EPSG:4326"));
        ReferencedEnvelope re = reWgs84.transform(CRS.decode("EPSG:3857"), true);

        BufferedImage image =
                RendererBaseTest.showRender("Lines with circle stroke", renderer, TIME, re);
        // if everything worked we are going to have a red dot in the middle of the map
        assertEquals(Color.RED, getPixelColor(image, image.getWidth() / 2, image.getHeight() / 2));
        assertEquals(
                Color.WHITE, getPixelColor(image, image.getWidth() / 4, image.getHeight() / 2));
        assertEquals(
                Color.WHITE, getPixelColor(image, image.getWidth() / 2, image.getHeight() / 4));
        assertEquals(
                Color.WHITE, getPixelColor(image, image.getWidth() / 4, image.getHeight() / 4));
    }

    @Test
    public void testTransformBBOXIsCorrect() throws Exception {
        Style style = RendererBaseTest.loadStyle(this, "noop_colormap.sld");
        GeoTiffReader reader = new GeoTiffReader(TestData.file(this, "watertemp.tiff"));
        MapContent mc = new MapContent();
        mc.addLayer(new GridReaderLayer(reader, style));

        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(mc);

        CRSAuthorityFactory factory = CRS.getAuthorityFactory(true);
        CoordinateReferenceSystem crs = factory.createCoordinateReferenceSystem("EPSG:4326");

        ReferencedEnvelope reWgs84 =
                new ReferencedEnvelope(
                        9.848993475036622,
                        11.958867853088378,
                        40.74254816253662,
                        41.64941961090088,
                        crs);

        BufferedImage image =
                RendererBaseTest.showRender("Transform BBOX", renderer, 4000, reWgs84);
        // last pixel is white when doing a transformation and the rendering transform BBOX
        // transforms are
        // incorrect, it shouldn't be
        assertNotEquals(Color.WHITE, getPixelColor(image, 299, 0));
        assertEquals(new Color(133, 130, 188), getPixelColor(image, 299, 0));
    }

    @Test
    public void testRasterToVectorTransformAcrossDateline() throws Exception {
        Style style = RendererBaseTest.loadStyle(this, "coverageCenter.sld");

        GeoTiffReader reader = new GeoTiffReader(TestData.copy(this, "geotiff/world.tiff"));

        MapContent mc = new MapContent();
        mc.addLayer(new GridCoverageLayer(reader.read(null), style));

        StreamingRenderer renderer = new StreamingRenderer();
        Map<Object, Object> rendererParams = new HashMap<>();
        rendererParams.put(StreamingRenderer.ADVANCED_PROJECTION_HANDLING_KEY, true);
        rendererParams.put(StreamingRenderer.CONTINUOUS_MAP_WRAPPING, true);
        renderer.setRendererHints(rendererParams);
        renderer.setMapContent(mc);

        ReferencedEnvelope reWgs84 =
                new ReferencedEnvelope(-90, 90, 0, 360, CRS.decode("EPSG:4326"));

        BufferedImage image =
                RendererBaseTest.showRender("Lines with circle stroke", renderer, TIME, reWgs84);
        // we are straddling the dateline, so the center of the coverage should be
        // a red dot vertically centered at at the edges of the image
        assertEquals(Color.RED, getPixelColor(image, 0, image.getHeight() / 2));
        assertEquals(Color.RED, getPixelColor(image, image.getWidth() - 1, image.getHeight() / 2));
        // there should NOT be a red dot at the center
        assertEquals(
                Color.WHITE, getPixelColor(image, image.getWidth() / 2, image.getHeight() / 2));
        assertEquals(
                Color.WHITE, getPixelColor(image, image.getWidth() - 1, image.getHeight() / 4));
        assertEquals(Color.WHITE, getPixelColor(image, 0, image.getHeight() / 4));
        assertEquals(
                Color.WHITE, getPixelColor(image, image.getWidth() / 4, image.getHeight() / 4));
    }

    @Test
    public void testRasterToTransformVectorPastDateline() throws Exception {
        Style style = RendererBaseTest.loadStyle(this, "coverageCenter.sld");

        GeoTiffReader reader = new GeoTiffReader(TestData.copy(this, "geotiff/world.tiff"));

        MapContent mc = new MapContent();
        mc.addLayer(new GridCoverageLayer(reader.read(null), style));

        StreamingRenderer renderer = new StreamingRenderer();
        Map<Object, Object> rendererParams = new HashMap<>();
        rendererParams.put(StreamingRenderer.ADVANCED_PROJECTION_HANDLING_KEY, true);
        rendererParams.put(StreamingRenderer.CONTINUOUS_MAP_WRAPPING, true);
        renderer.setRendererHints(rendererParams);
        renderer.setMapContent(mc);

        ReferencedEnvelope reWgs84 =
                new ReferencedEnvelope(-70, 70, 200, 520, CRS.decode("EPSG:4326"));
        ReferencedEnvelope re = reWgs84.transform(CRS.decode("EPSG:3857"), true);

        BufferedImage image =
                RendererBaseTest.showRender("Lines with circle stroke", renderer, TIME, re);
        // if everything worked we are going to have a red dot in the middle of the map
        assertEquals(Color.RED, getPixelColor(image, image.getWidth() / 2, image.getHeight() / 2));
        assertEquals(
                Color.WHITE, getPixelColor(image, image.getWidth() / 4, image.getHeight() / 2));
        assertEquals(
                Color.WHITE, getPixelColor(image, image.getWidth() / 2, image.getHeight() / 4));
        assertEquals(
                Color.WHITE, getPixelColor(image, image.getWidth() / 4, image.getHeight() / 4));
    }

    @Test
    public void testTransformReprojectedGridCoverage() throws Exception {
        Style style = RendererBaseTest.loadStyle(this, "coverageCenter.sld");

        GeoTiffReader reader = new GeoTiffReader(TestData.copy(this, "geotiff/world.tiff"));

        MapContent mc = new MapContent();
        mc.addLayer(new GridCoverageLayer(reader.read(null), style));

        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(mc);

        ReferencedEnvelope reWgs84 =
                new ReferencedEnvelope(-70, 70, -160, 160, CRS.decode("EPSG:4326"));
        ReferencedEnvelope re = reWgs84.transform(CRS.decode("EPSG:3857"), true);

        BufferedImage image =
                RendererBaseTest.showRender("Lines with circle stroke", renderer, TIME, re);
        // if everything worked we are going to have a red dot in the middle of the map
        assertEquals(Color.RED, getPixelColor(image, image.getWidth() / 2, image.getHeight() / 2));
        assertEquals(
                Color.WHITE, getPixelColor(image, image.getWidth() / 4, image.getHeight() / 2));
        assertEquals(
                Color.WHITE, getPixelColor(image, image.getWidth() / 2, image.getHeight() / 4));
        assertEquals(
                Color.WHITE, getPixelColor(image, image.getWidth() / 4, image.getHeight() / 4));
    }

    @Test
    public void testTransformNullCoverage() throws Exception {
        Style style = RendererBaseTest.loadStyle(this, "coverageCenter.sld");

        GridCoverage2DReader reader =
                new AbstractGridCoverage2DReader() {

                    @Override
                    public Format getFormat() {
                        return null;
                    }

                    @Override
                    public GridCoverage2D read(GeneralParameterValue[] parameters)
                            throws IllegalArgumentException, IOException {
                        // we return null on purpose, simulating a reader queried outside of its
                        // area, or
                        // on a dimension value it does not have
                        return null;
                    }
                };

        MapContent mc = new MapContent();
        mc.addLayer(new GridReaderLayer(reader, style));

        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(mc);

        ReferencedEnvelope re = new ReferencedEnvelope(-70, 70, -160, 160, CRS.decode("EPSG:4326"));

        BufferedImage image =
                RendererBaseTest.showRender("Transformation with null input", renderer, TIME, re);
        // full white, no NPE
        double[] minimums = new ImageWorker(image).getMinimums();
        assertEquals(255, minimums[0], 0d);
        assertEquals(255, minimums[1], 0d);
        assertEquals(255, minimums[2], 0d);
        assertEquals(255, minimums[3], 0d);
    }

    @Test
    public void testTransformWithQueryNoInvert() throws Exception {
        testTransformWithQuery(false);
    }

    @Test
    public void testTransformWithQueryInvert() throws Exception {
        testTransformWithQuery(true);
    }

    private void testTransformWithQuery(boolean invert)
            throws IOException, URISyntaxException, CQLException, NoSuchAuthorityCodeException,
                    FactoryException, Exception {
        // grab the style
        Style style =
                RendererBaseTest.loadStyle(
                        this, invert ? "attributeRename.sld" : "attributeRenameNoInvert.sld");
        // grab the data
        File property = new File(TestData.getResource(this, "point.properties").toURI());
        PropertyDataStore ds = new PropertyDataStore(property.getParentFile());
        FeatureSource fs = ds.getFeatureSource("point");

        // prepare a feature layer with a query and the rendering tx
        FeatureLayer layer = new FeatureLayer(fs, style);
        layer.setQuery(new Query(null, CQL.toFilter("id > 5")));

        // render it
        MapContent mc = new MapContent();
        mc.addLayer(layer);
        StreamingRenderer renderer = new StreamingRenderer();
        final AtomicInteger counter = new AtomicInteger();
        renderer.addRenderListener(
                new RenderListener() {

                    @Override
                    public void featureRenderer(SimpleFeature feature) {
                        counter.incrementAndGet();
                    }

                    @Override
                    public void errorOccurred(Exception e) {}
                });
        renderer.setMapContent(mc);
        ReferencedEnvelope re = new ReferencedEnvelope(0, 12, 0, 12, CRS.decode("EPSG:4326"));
        BufferedImage image =
                RendererBaseTest.showRender("Lines with circle stroke", renderer, TIME, re);

        // if everything went fine we'll have a single red dot in the middle, and we rendered
        // just one feature
        assertEquals(1, counter.get());
        assertEquals(Color.RED, getPixelColor(image, image.getWidth() / 2, image.getHeight() / 2));
    }

    @Test
    public void testTransformReproject() throws Exception {
        // grab the style
        Style style = RendererBaseTest.loadStyle(this, "reproject-rt.sld");
        // grab the data
        File property = new File(TestData.getResource(this, "point.properties").toURI());
        PropertyDataStore ds = new PropertyDataStore(property.getParentFile());
        FeatureSource fs = ds.getFeatureSource("point");

        // prepare a feature layer with a query and the rendering tx
        FeatureLayer layer = new FeatureLayer(fs, style);

        // prepare a bbox in UTM-32N
        ReferencedEnvelope reWgs84 = new ReferencedEnvelope(0, 12, 0, 12, CRS.decode("EPSG:4326"));
        ReferencedEnvelope reUTM32N = reWgs84.transform(CRS.decode("EPSG:3857"), true);

        // render it
        MapContent mc = new MapContent();
        mc.addLayer(layer);
        StreamingRenderer renderer = new StreamingRenderer();
        final AtomicInteger counter = new AtomicInteger();
        renderer.addRenderListener(
                new RenderListener() {

                    @Override
                    public void featureRenderer(SimpleFeature feature) {
                        counter.incrementAndGet();
                    }

                    @Override
                    public void errorOccurred(Exception e) {}
                });
        renderer.setMapContent(mc);
        BufferedImage image =
                RendererBaseTest.showRender("Lines with circle stroke", renderer, TIME, reUTM32N);

        // if everything went fine we rendered all the features
        assertEquals(10, counter.get());
        assertEquals(Color.RED, getPixelColor(image, image.getWidth() / 2, image.getHeight() / 2));
    }

    /** Gets a specific pixel color from the specified buffered image */
    protected Color getPixelColor(BufferedImage image, int i, int j) {
        ColorModel cm = image.getColorModel();
        Raster raster = image.getRaster();
        Object pixel = raster.getDataElements(i, j, null);

        Color actual;
        if (cm.hasAlpha()) {
            actual =
                    new Color(
                            cm.getRed(pixel),
                            cm.getGreen(pixel),
                            cm.getBlue(pixel),
                            cm.getAlpha(pixel));
        } else {
            actual = new Color(cm.getRed(pixel), cm.getGreen(pixel), cm.getBlue(pixel), 255);
        }
        return actual;
    }
}
