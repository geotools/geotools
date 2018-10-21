/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.lite.gridcoverage2d;

import static org.junit.Assert.fail;

import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import org.geotools.coverage.CoverageFactoryFinder;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.function.EnvFunction;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.image.test.ImageAssert;
import org.geotools.map.GridCoverageLayer;
import org.geotools.map.MapContent;
import org.geotools.referencing.crs.DefaultEngineeringCRS;
import org.geotools.renderer.RenderListener;
import org.geotools.renderer.lite.RendererBaseTest;
import org.geotools.renderer.lite.StreamingRenderer;
import org.geotools.styling.ChannelSelection;
import org.geotools.styling.ContrastEnhancement;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.SLD;
import org.geotools.styling.SelectedChannelType;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.FilterFactory2;
import org.opengis.style.ContrastMethod;

/**
 * Tests rendering a GridCoverage2D object directly (ie. not via a coverage reader).
 *
 * @author mbedward
 * @since 2.7
 * @version $Id$
 */
public class DisplayCoverageTest {

    private static final int WIDTH = 10;
    private static final String LOGGER_NAME = "org.geotools.rendering";

    private final boolean headless;
    private final Rectangle bounds;
    private final ReferencedEnvelope env;

    public DisplayCoverageTest() {
        headless = GraphicsEnvironment.isHeadless();
        bounds = new Rectangle(0, 0, WIDTH, WIDTH);
        env = new ReferencedEnvelope(bounds, DefaultEngineeringCRS.GENERIC_2D);
    }

    /**
     * This test was added after changes to the render module broke direct rendering of
     * GridCoverage2D objects in GridCoverageLayers.
     *
     * <p>It does not compare the rendering to the coverage data. Rather it just checks that
     * rendering is done without error.
     *
     * <p>The test is skipped in a headless build.
     */
    @Test
    public void renderCoverage() throws IOException {
        Style style = createCoverageStyle("1");
        String styleName = "contrastStretchSimple";
        testCoverageStyle(style, styleName);
    }

    @Test
    public void renderCoverageWithEnvContrastLow() throws IOException {
        Style style = createEnvCoverageStyle("1");
        EnvFunction.setLocalValue("gamma", "0.5");
        try {
            String styleName = "contrastStretchEnvLow";
            testCoverageStyle(style, styleName);
        } finally {
            EnvFunction.clearLocalValues();
        }
    }

    @Test
    public void renderCoverageWithEnvContrastHigh() throws IOException {
        Style style = createEnvCoverageStyle("1");
        EnvFunction.setLocalValue("gamma", "2");
        try {
            String styleName = "contrastStretchEnvHigh";
            testCoverageStyle(style, styleName);
        } finally {
            EnvFunction.clearLocalValues();
        }
    }

    @Test
    public void renderCoverageWithEnvMinMax() throws IOException {
        Style style = createEnvMinMaxCoverageStyle("1");
        EnvFunction.setLocalValue("range_min", "4");
        EnvFunction.setLocalValue("range_max", "16");
        try {
            String styleName = "contrastStretchEnvMinMax";
            testCoverageStyle(style, styleName);
        } finally {
            EnvFunction.clearLocalValues();
        }
    }

    private void testCoverageStyle(Style style, String styleName) throws IOException {
        GridCoverage2D coverage = createCoverage();
        StreamingRenderer renderer = new StreamingRenderer();

        MapContent context = new MapContent();
        context.addLayer(new GridCoverageLayer(coverage, style));
        renderer.setMapContent(context);

        RenderListener listener =
                new RenderListener() {
                    public void featureRenderer(SimpleFeature feature) {}

                    public void errorOccurred(Exception e) {
                        java.util.logging.Logger.getGlobal()
                                .log(java.util.logging.Level.INFO, "", e);
                        fail("Failed to render coverage");
                    }
                };
        BufferedImage image = RendererBaseTest.renderImage(renderer, env, listener);

        File reference =
                new File(
                        "./src/test/resources/org/geotools/renderer/lite/gridcoverage2d/"
                                + styleName
                                + ".png");

        ImageAssert.assertEquals(reference, image, 0);
    }

    private GridCoverage2D createCoverage() {
        GridCoverageFactory gcf = CoverageFactoryFinder.getGridCoverageFactory(null);
        float[][] matrix = new float[WIDTH][WIDTH];
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < WIDTH; j++) {
                matrix[i][j] = i + j;
            }
        }

        return gcf.create("coverage", matrix, env);
    }

    private Style createCoverageStyle(String bandName) {
        StyleFactory sf = CommonFactoryFinder.getStyleFactory(null);
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);

        ContrastEnhancement ce = sf.contrastEnhancement(ff.literal(1.0), ContrastMethod.NORMALIZE);
        SelectedChannelType sct = sf.createSelectedChannelType(bandName, ce);

        RasterSymbolizer sym = sf.getDefaultRasterSymbolizer();
        ChannelSelection sel = sf.channelSelection(sct);
        sym.setChannelSelection(sel);

        return SLD.wrapSymbolizers(sym);
    }

    private Style createEnvCoverageStyle(String bandName) {
        StyleFactory sf = CommonFactoryFinder.getStyleFactory(null);
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);

        ContrastEnhancement ce =
                sf.contrastEnhancement(
                        ff.function("env", ff.literal("gamma"), ff.literal(1)),
                        ContrastMethod.NORMALIZE);
        SelectedChannelType sct = sf.createSelectedChannelType(bandName, ce);

        RasterSymbolizer sym = sf.getDefaultRasterSymbolizer();
        ChannelSelection sel = sf.channelSelection(sct);
        sym.setChannelSelection(sel);

        return SLD.wrapSymbolizers(sym);
    }

    private Style createEnvMinMaxCoverageStyle(String bandName) {
        StyleFactory sf = CommonFactoryFinder.getStyleFactory(null);
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);

        ContrastEnhancement ce = sf.contrastEnhancement(ff.literal(1.0), ContrastMethod.NORMALIZE);
        ce.addOption("algorithm", ff.literal("StretchToMinimumMaximum"));
        ce.addOption("minValue", ff.function("env", ff.literal("range_min"), ff.literal(0)));
        ce.addOption("maxValue", ff.function("env", ff.literal("range_max"), ff.literal(220)));
        SelectedChannelType sct = sf.createSelectedChannelType(bandName, ce);

        RasterSymbolizer sym = sf.getDefaultRasterSymbolizer();
        ChannelSelection sel = sf.channelSelection(sct);
        sym.setChannelSelection(sel);

        return SLD.wrapSymbolizers(sym);
    }
}
