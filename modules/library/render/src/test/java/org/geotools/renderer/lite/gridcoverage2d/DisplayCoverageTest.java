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

import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.coverage.CoverageFactoryFinder;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.MapContext;
import org.geotools.referencing.crs.DefaultEngineeringCRS;
import org.geotools.renderer.RenderListener;
import org.geotools.renderer.lite.StreamingRenderer;
import org.geotools.styling.ChannelSelection;
import org.geotools.styling.ContrastEnhancement;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.SLD;
import org.geotools.styling.SelectedChannelType;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.style.ContrastMethod;
import org.opengis.filter.FilterFactory2;

import static org.junit.Assert.fail;
import org.junit.Test;

/**
 * Tests rendering a GridCoverage2D object directly (ie. not via a coverage reader).
 * 
 * @author mbedward
 *
 * @source $URL$
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
     * This test was added after changes to the render module broke direct
     * rendering of GridCoverage2D objects in GridCoverageLayers. 
     * 
     * It does not compare the rendering to the coverage data. Rather it just
     * checks that rendering is done without error.
     * 
     * The test is skipped in a headless build.
     */
    @Test
    public void renderCoverage() {
        System.out.println("   render grid coverage");
        if (headless) {
            System.out.println("      Skipping test in headless build");
            return;
        }

        GridCoverage2D coverage = createCoverage();
        Style style = createCoverageStyle("1");

        MapContext context = new MapContext();
        context.addLayer(coverage, style);
        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setContext(context);

        RenderListener listener = new RenderListener() {
            public void featureRenderer(SimpleFeature feature) {}

            public void errorOccurred(Exception e) {
                fail("Failed to render coverage");
            }
        };

        renderer.addRenderListener(listener);

        BufferedImage image = new BufferedImage(WIDTH, WIDTH, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2D = image.createGraphics();

        /*
         * Turn off logging and try to render the coverage
         */
        Logger logger = Logger.getLogger(LOGGER_NAME);
        final Level savedLevel = logger.getLevel();
        logger.setLevel(Level.OFF);

        try {
            renderer.paint(g2D, bounds, env);
        } finally {
            logger.setLevel(savedLevel);
        }
    }

    private GridCoverage2D createCoverage() {
        GridCoverageFactory gcf = CoverageFactoryFinder.getGridCoverageFactory(null);
        float[][] matrix = new float[WIDTH][WIDTH];
        Random rand = new Random();
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < WIDTH; j++) {
                matrix[i][j] = rand.nextFloat() * 255;
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
}
