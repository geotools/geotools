/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2013, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.lite;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.net.URL;
import javax.imageio.ImageIO;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.api.style.ExternalGraphic;
import org.geotools.api.style.GraphicLegend;
import org.geotools.api.style.Rule;
import org.geotools.api.style.Style;
import org.geotools.api.style.StyleFactory;
import org.geotools.data.ows.MockURLChecker;
import org.geotools.data.ows.URLCheckers;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.geometry.jts.Decimator;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.geometry.jts.LiteShape2;
import org.geotools.referencing.operation.transform.ProjectiveTransform;
import org.geotools.styling.GraphicImpl;
import org.geotools.test.TestData;
import org.geotools.util.factory.GeoTools;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

public class StyledShapePainterTest {

    @After
    public void cleanupCheckers() {
        URLCheckers.reset();
    }

    @Test
    public void testGraphicLegendNullLegend() throws Exception {
        StyledShapePainter painter = new StyledShapePainter();
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = (Graphics2D) image.getGraphics();
        MathTransform transform = ProjectiveTransform.create(new AffineTransform());
        Decimator decimator = new Decimator(transform, new Rectangle());
        Point point = new GeometryFactory().createPoint(new Coordinate(10, 10));
        LiteShape2 pointShape = new LiteShape2(point, transform, decimator, false);
        try {
            painter.paint(g2, pointShape, (GraphicLegend) null, 1, false);
            Assert.fail();
        } catch (NullPointerException e) {
            Assert.assertEquals("ShapePainter has been asked to paint a null legend!!", e.getMessage());
        }
    }

    @Test
    public void testGraphicLegend() throws Exception {

        // Load image directly from file, for comparison with painter output
        final URL imageURL = TestData.getResource(this, "icon64.png");
        final BufferedImage testImage = ImageIO.read(imageURL);
        final int width = testImage.getWidth();
        final int height = testImage.getHeight();

        // Get graphic legend from style
        final Style style = RendererBaseTest.loadStyle(this, "testGraphicLegend.sld");
        final Rule rule = style.featureTypeStyles().get(0).rules().get(0);
        final GraphicLegend legend = rule.getLegend();

        // Paint legend using StyledShapePainter
        final Point point = new GeometryFactory().createPoint(new Coordinate(width / 2, height / 2));
        final LiteShape2 shape = new LiteShape2(point, null, null, false);

        int imageType = testImage.getType();
        if (imageType == BufferedImage.TYPE_CUSTOM) {
            imageType = BufferedImage.TYPE_INT_RGB;
        }
        final BufferedImage paintedImage = new BufferedImage(width, height, imageType);
        final Graphics2D graphics = paintedImage.createGraphics();
        final StyledShapePainter painter = new StyledShapePainter();
        painter.paint(graphics, shape, legend, 1, false);
        graphics.dispose();

        // Ensure painted legend matches image from file
        Assert.assertTrue(imagesIdentical(paintedImage, testImage));
    }

    @Test
    public void testGraphicLegendURLCheckerAllowed() throws Exception {
        URLCheckers.register(new MockURLChecker(u -> u.contains("icon64.png")));
        testGraphicLegend();
    }

    @Test
    public void testGraphicLegendURLCheckerDisallowed() throws Exception {
        URLCheckers.register(new MockURLChecker(u -> false));

        // Get graphic legend from style
        Style style = RendererBaseTest.loadStyle(this, "testGraphicLegend.sld");
        Rule rule = style.featureTypeStyles().get(0).rules().get(0);
        GraphicLegend legend = rule.getLegend();

        // The painter has nothing to paint if the URL check fails so passing in a null graphics and
        // shape won't throw an exception here
        new StyledShapePainter().paint(null, null, legend, 1, false);
    }

    @Test
    public void testGraphicLegendNegativeScale() throws Exception {

        // Load image directly from file, for comparison with painter output
        final URL imageURL = TestData.getResource(this, "icon64.png");
        final BufferedImage testImage = ImageIO.read(imageURL);
        final int width = testImage.getWidth();
        final int height = testImage.getHeight();

        // Get graphic legend from style
        final Style style = RendererBaseTest.loadStyle(this, "testGraphicLegend.sld");
        final Rule rule = style.featureTypeStyles().get(0).rules().get(0);
        final GraphicLegend legend = rule.getLegend();

        // Paint legend using StyledShapePainter
        final Point point = new GeometryFactory().createPoint(new Coordinate(width / 2, height / 2));
        final LiteShape2 shape = new LiteShape2(point, null, null, false);

        int imageType = testImage.getType();
        if (imageType == BufferedImage.TYPE_CUSTOM) {
            imageType = BufferedImage.TYPE_INT_RGB;
        }
        final BufferedImage paintedImage = new BufferedImage(width, height, imageType);
        final Graphics2D graphics = paintedImage.createGraphics();
        final StyledShapePainter painter = new StyledShapePainter();
        painter.paint(graphics, shape, legend, -1, false);
        graphics.dispose();

        // Ensure painted legend matches image from file
        Assert.assertTrue(imagesIdentical(paintedImage, testImage));
    }

    @Test
    public void testGraphicLegendRotation() throws Exception {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory();
        GeometryFactory gf = JTSFactoryFinder.getGeometryFactory();

        // Load image directly from file, for comparison with painter output
        final URL imageURL = TestData.getResource(this, "icon64.png");
        final BufferedImage testImage = ImageIO.read(imageURL);
        final int width = testImage.getWidth();
        final int height = testImage.getHeight();

        // Get graphic legend from style
        final Style style = RendererBaseTest.loadStyle(this, "testGraphicLegend.sld");
        final Rule rule = style.featureTypeStyles().get(0).rules().get(0);
        final GraphicLegend legend = rule.getLegend();

        // Set rotation to 45 degrees
        legend.setRotation(ff.literal(45.0));

        // Paint legend using StyledShapePainter
        final Point point = gf.createPoint(new Coordinate(width / 2, height / 2));
        final LiteShape2 shape = new LiteShape2(point, null, null, false);

        int imageType = testImage.getType();
        if (imageType == BufferedImage.TYPE_CUSTOM) {
            imageType = BufferedImage.TYPE_INT_RGB;
        }
        final BufferedImage paintedImage = new BufferedImage(width, height, imageType);
        final Graphics2D graphics = paintedImage.createGraphics();
        final StyledShapePainter painter = new StyledShapePainter();
        painter.paint(graphics, shape, legend, 1, false);
        graphics.dispose();

        // Ensure painted legend does not match image from file
        Assert.assertFalse(imagesIdentical(paintedImage, testImage));
    }

    /** Determines whether two buffered images are identical. */
    private static boolean imagesIdentical(BufferedImage image1, BufferedImage image2) {
        final WritableRaster raster1 = image1.getRaster();
        final WritableRaster raster2 = image2.getRaster();
        final int numBands = raster1.getNumBands();
        if (raster2.getNumBands() != numBands) {
            return false;
        }
        final int width = raster1.getWidth();
        if (raster2.getWidth() != width) {
            return false;
        }
        final int height = raster1.getHeight();
        if (raster2.getHeight() != height) {
            return false;
        }
        final int[] pixel1 = new int[numBands];
        final int[] pixel2 = new int[numBands];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                raster1.getPixel(x, y, pixel1);
                raster2.getPixel(x, y, pixel2);
                for (int i = 0; i < numBands; i++) {
                    if (pixel1[i] != pixel2[i]) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Test
    public void testGraphicLegend2() throws Exception {

        StyledShapePainter painter = new StyledShapePainter();
        GraphicImpl legend = new GraphicImpl(CommonFactoryFinder.getFilterFactory(GeoTools.getDefaultHints()));
        FilterFactory ff = CommonFactoryFinder.getFilterFactory();
        legend.setRotation(ff.literal(0));
        legend.setOpacity(ff.literal(1));
        URL url = StreamingRenderer.class.getResource("test-data/");
        StyleFactory sf = CommonFactoryFinder.getStyleFactory(null);
        ExternalGraphic eg = sf.createExternalGraphic(url + "icon64.png", "image/png");
        legend.graphicalSymbols().add(eg);
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = (Graphics2D) image.getGraphics();
        MathTransform transform = ProjectiveTransform.create(new AffineTransform());
        Decimator decimator = new Decimator(transform, new Rectangle());
        Point point = new GeometryFactory().createPoint(new Coordinate(10, 10));
        LiteShape2 pointShape = new LiteShape2(point, transform, decimator, false);
        painter.paint(g2, pointShape, legend, 1, false);
        // check it is correctly painted
        Assert.assertEquals(0, image.getRaster().getSample(0, 0, 0));
        Assert.assertEquals(64, image.getRaster().getSample(0, 0, 1));
        Assert.assertEquals(128, image.getRaster().getSample(0, 0, 2));

        Assert.assertEquals(255, image.getRaster().getSample(30, 30, 0));
        Assert.assertEquals(255, image.getRaster().getSample(30, 30, 1));
        Assert.assertEquals(255, image.getRaster().getSample(30, 30, 2));

        Assert.assertEquals(0, image.getRaster().getSample(90, 30, 0));
        Assert.assertEquals(0, image.getRaster().getSample(90, 30, 1));
        Assert.assertEquals(0, image.getRaster().getSample(90, 30, 2));

        // check with a scaling factor
        image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        g2 = (Graphics2D) image.getGraphics();
        painter.paint(g2, pointShape, legend, 2, false);
        Assert.assertEquals(255, image.getRaster().getSample(0, 0, 0));
        Assert.assertEquals(255, image.getRaster().getSample(0, 0, 1));
        Assert.assertEquals(192, image.getRaster().getSample(0, 0, 2));

        Assert.assertEquals(0, image.getRaster().getSample(30, 30, 0));
        Assert.assertEquals(0, image.getRaster().getSample(30, 30, 1));
        Assert.assertEquals(0, image.getRaster().getSample(30, 30, 2));

        Assert.assertEquals(0, image.getRaster().getSample(90, 30, 0));
        Assert.assertEquals(0, image.getRaster().getSample(90, 30, 1));
        Assert.assertEquals(0, image.getRaster().getSample(90, 30, 2));
    }
}
