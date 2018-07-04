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
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.net.URL;
import javax.imageio.ImageIO;
import junit.framework.TestCase;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.GeoTools;
import org.geotools.geometry.jts.Decimator;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.geometry.jts.LiteShape2;
import org.geotools.referencing.operation.transform.ProjectiveTransform;
import org.geotools.styling.ExternalGraphic;
import org.geotools.styling.GraphicImpl;
import org.geotools.styling.GraphicLegend;
import org.geotools.styling.Rule;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.test.TestData;
import org.junit.Assert;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.opengis.filter.FilterFactory2;
import org.opengis.referencing.operation.MathTransform;

public class StyledShapePainterTest extends TestCase {

    public void testGraphicLegendNullLegend() throws Exception {
        StyledShapePainter painter = new StyledShapePainter();
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = (Graphics2D) image.getGraphics();
        MathTransform transform = ProjectiveTransform.create(new AffineTransform());
        Decimator decimator = new Decimator(transform);
        Point point = new GeometryFactory().createPoint(new Coordinate(10, 10));
        LiteShape2 pointShape = new LiteShape2(point, transform, decimator, false);
        try {
            painter.paint(g2, pointShape, (GraphicLegend) null, 1, false);
            fail();
        } catch (NullPointerException e) {
            assertEquals("ShapePainter has been asked to paint a null legend!!", e.getMessage());
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
        final GraphicLegend legend = (GraphicLegend) rule.getLegend();

        // Paint legend using StyledShapePainter
        final Point point =
                new GeometryFactory().createPoint(new Coordinate(width / 2, height / 2));
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
    public void testGraphicLegendNegativeScale() throws Exception {

        // Load image directly from file, for comparison with painter output
        final URL imageURL = TestData.getResource(this, "icon64.png");
        final BufferedImage testImage = ImageIO.read(imageURL);
        final int width = testImage.getWidth();
        final int height = testImage.getHeight();

        // Get graphic legend from style
        final Style style = RendererBaseTest.loadStyle(this, "testGraphicLegend.sld");
        final Rule rule = style.featureTypeStyles().get(0).rules().get(0);
        final GraphicLegend legend = (GraphicLegend) rule.getLegend();

        // Paint legend using StyledShapePainter
        final Point point =
                new GeometryFactory().createPoint(new Coordinate(width / 2, height / 2));
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

    public void testGraphicLegendRotation() throws Exception {
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
        GeometryFactory gf = JTSFactoryFinder.getGeometryFactory();

        // Load image directly from file, for comparison with painter output
        final URL imageURL = TestData.getResource(this, "icon64.png");
        final BufferedImage testImage = ImageIO.read(imageURL);
        final int width = testImage.getWidth();
        final int height = testImage.getHeight();

        // Get graphic legend from style
        final Style style = RendererBaseTest.loadStyle(this, "testGraphicLegend.sld");
        final Rule rule = style.featureTypeStyles().get(0).rules().get(0);
        final GraphicLegend legend = (GraphicLegend) rule.getLegend();

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

    public void testGraphicLegend2() throws Exception {

        StyledShapePainter painter = new StyledShapePainter();
        GraphicImpl legend =
                new GraphicImpl(CommonFactoryFinder.getFilterFactory(GeoTools.getDefaultHints()));
        legend.setRotation(0);
        legend.setOpacity(1);
        Style pStyle = RendererBaseTest.loadStyle(this, "externalGraphic.sld");
        URL url = StreamingRenderer.class.getResource("test-data/");
        StyleFactory sf = CommonFactoryFinder.getStyleFactory(null);
        ExternalGraphic eg = sf.createExternalGraphic(url + "icon64.png", "image/png");
        legend.addExternalGraphic(eg);
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = (Graphics2D) image.getGraphics();
        MathTransform transform = ProjectiveTransform.create(new AffineTransform());
        Decimator decimator = new Decimator(transform);
        Point point = new GeometryFactory().createPoint(new Coordinate(10, 10));
        LiteShape2 pointShape = new LiteShape2(point, transform, decimator, false);
        painter.paint(g2, pointShape, legend, 1, false);
        // check it is correctly painted
        assertEquals(0, image.getRaster().getSample(0, 0, 0));
        assertEquals(64, image.getRaster().getSample(0, 0, 1));
        assertEquals(128, image.getRaster().getSample(0, 0, 2));

        assertEquals(255, image.getRaster().getSample(30, 30, 0));
        assertEquals(255, image.getRaster().getSample(30, 30, 1));
        assertEquals(255, image.getRaster().getSample(30, 30, 2));

        assertEquals(0, image.getRaster().getSample(90, 30, 0));
        assertEquals(0, image.getRaster().getSample(90, 30, 1));
        assertEquals(0, image.getRaster().getSample(90, 30, 2));

        // check with a scaling factor
        image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        g2 = (Graphics2D) image.getGraphics();
        painter.paint(g2, pointShape, legend, 2, false);
        assertEquals(255, image.getRaster().getSample(0, 0, 0));
        assertEquals(255, image.getRaster().getSample(0, 0, 1));
        assertEquals(192, image.getRaster().getSample(0, 0, 2));

        assertEquals(0, image.getRaster().getSample(30, 30, 0));
        assertEquals(0, image.getRaster().getSample(30, 30, 1));
        assertEquals(0, image.getRaster().getSample(30, 30, 2));

        assertEquals(0, image.getRaster().getSample(90, 30, 0));
        assertEquals(0, image.getRaster().getSample(90, 30, 1));
        assertEquals(0, image.getRaster().getSample(90, 30, 2));
    }
}
