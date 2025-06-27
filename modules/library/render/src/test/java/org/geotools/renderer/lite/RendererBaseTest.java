/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontFormatException;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.HeadlessException;
import java.awt.Panel;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.io.IOException;
import org.geotools.api.style.NamedLayer;
import org.geotools.api.style.NamedStyle;
import org.geotools.api.style.ResourceLocator;
import org.geotools.api.style.Style;
import org.geotools.api.style.StyleFactory;
import org.geotools.api.style.StyledLayerDescriptor;
import org.geotools.api.style.UserLayer;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.MapContent;
import org.geotools.renderer.GTRenderer;
import org.geotools.renderer.RenderListener;
import org.geotools.renderer.style.FontCache;
import org.geotools.sld.v1_1.SLDConfiguration;
import org.geotools.styling.DefaultResourceLocator;
import org.geotools.test.TestData;
import org.geotools.xml.styling.SLDParser;
import org.geotools.xsd.Parser;
import org.junit.Assert;

/**
 * Used to test a renderer implementation.
 *
 * <p>Mostly tests that the renderer produces an image at all.
 *
 * @author Simone Giannecchini
 */
public abstract class RendererBaseTest {

    protected RendererBaseTest() {}

    /**
     * bounds may be null
     *
     * @param testName Name reported in event of failure
     * @param renderer Renderer being tested
     * @param timeOut Maximum time allowed for test
     * @param bounds area to draw
     * @throws Exception In the event of failure
     */
    protected static BufferedImage showRender(
            String testName, GTRenderer renderer, long timeOut, ReferencedEnvelope... bounds) throws Exception {
        return showRender(testName, renderer, timeOut, bounds, null);
    }

    /**
     * bounds may be null
     *
     * @param testName Name reported in event of failure
     * @param renderer Renderer being tested
     * @param timeOut Maximum time allowed for test
     * @param bounds area to draw
     * @param listener Optional listener
     * @throws Exception In the event of failure
     */
    protected static BufferedImage showRender(
            String testName, GTRenderer renderer, long timeOut, ReferencedEnvelope[] bounds, RenderListener listener)
            throws Exception {
        BufferedImage[] images = new BufferedImage[bounds.length];
        for (int i = 0; i < images.length; i++) {
            images[i] = renderImage(renderer, bounds[i], listener);
        }
        if (renderer.getMapContent() != null) {
            renderer.getMapContent().dispose();
        }
        final BufferedImage image = mergeImages(images);

        showImage(testName, timeOut, image);
        boolean hasData = false; // All I can seem to check reliably.

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                if (image.getRGB(x, y) != 0) {
                    hasData = true;
                }
            }
        }

        assert hasData;
        return image;
    }

    public static void showImage(String testName, long timeOut, final BufferedImage image) throws InterruptedException {
        final String headless = System.getProperty("java.awt.headless", "false");
        if (!headless.equalsIgnoreCase("true") && TestData.isInteractiveTest()) {
            try {
                Frame frame = new Frame(testName);
                frame.addWindowListener(new WindowAdapter() {

                    @Override
                    public void windowClosing(WindowEvent e) {
                        e.getWindow().dispose();
                    }
                });

                Panel p = new Panel() {

                    /** <code>serialVersionUID</code> field */
                    private static final long serialVersionUID = 1L;

                    {
                        setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
                    }

                    @Override
                    public void paint(Graphics g) {
                        g.drawImage(image, 0, 0, this);
                    }
                };

                frame.add(p);
                frame.pack();
                frame.setVisible(true);

                Thread.sleep(timeOut);
                frame.dispose();
            } catch (HeadlessException exception) {
                // The test is running on a machine without X11 display. Ignore.
            }
        }
    }

    public static BufferedImage renderImage(GTRenderer renderer, ReferencedEnvelope bounds, RenderListener listener) {
        int w = 300;
        int h = 300;
        return renderImage(renderer, bounds, listener, w, h);
    }

    public static BufferedImage renderImage(
            GTRenderer renderer, ReferencedEnvelope bounds, RenderListener listener, int w, int h) {
        final BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics g = image.getGraphics();
        g.setColor(Color.white);
        g.fillRect(0, 0, w, h);
        render(renderer, g, new Rectangle(w, h), bounds, listener);
        return image;
    }

    public static BufferedImage mergeImages(BufferedImage[] images) {
        // merge horizontally
        int totalWidth = 0;
        int height = 0;
        for (BufferedImage bufferedImage : images) {
            totalWidth += bufferedImage.getWidth();
            height = Math.max(height, bufferedImage.getHeight());
        }
        BufferedImage joinedImage = new BufferedImage(totalWidth, height, BufferedImage.TYPE_INT_ARGB);
        Graphics g = joinedImage.getGraphics();
        int x = 0;
        for (BufferedImage bufferedImage : images) {
            g.drawImage(bufferedImage, x, 0, null);
            x += bufferedImage.getWidth();
        }
        return joinedImage;
    }

    /**
     * responsible for actually rendering.
     *
     * @param obj Rendering implementation being tested, usually GTRenderer
     * @param g graphics used
     * @param rect image area
     * @param bounds world area
     * @param rendererListener optional rendererListener, may be null
     */
    private static void render(
            GTRenderer renderer,
            Graphics g,
            Rectangle rect,
            ReferencedEnvelope bounds,
            RenderListener rendererListener) {
        try {
            if (rendererListener != null) {
                renderer.addRenderListener(rendererListener);
            }

            if (bounds == null) {
                renderer.paint((Graphics2D) g, rect, new AffineTransform());
            } else {
                renderer.paint((Graphics2D) g, rect, bounds);
            }
        } finally {
            if (rendererListener != null) {
                renderer.removeRenderListener(rendererListener);
            }
        }
    }

    /** Utility to quickly render a set of vector data on top of a buffered image */
    public static BufferedImage render(MapContent map) throws Exception {
        StreamingRenderer r = new StreamingRenderer();
        r.setMapContent(map);

        return RendererBaseTest.showRender("testPointLabeling", r, 5000, map.getMaxBounds());
    }

    /** Load a style from the test-data directory associated with the object. */
    public static Style loadStyle(Object loader, String sldFilename) throws IOException {
        StyleFactory factory = CommonFactoryFinder.getStyleFactory(null);

        java.net.URL surl = TestData.getResource(loader, sldFilename);
        SLDParser stylereader = new SLDParser(factory, surl);

        Style style = stylereader.readXML()[0];
        return style;
    }

    /** Load a Symbology Encoding style from the test-data directory associated with the object. */
    protected static Style loadSEStyle(Object loader, String sldFilename) throws IOException {
        try {
            final java.net.URL surl = TestData.getResource(loader, sldFilename);
            SLDConfiguration configuration = new SLDConfiguration() {
                @Override
                protected void configureContext(org.picocontainer.MutablePicoContainer container) {
                    DefaultResourceLocator locator = new DefaultResourceLocator();
                    locator.setSourceUrl(surl);
                    container.registerComponentInstance(ResourceLocator.class, locator);
                }
            };
            Parser parser = new Parser(configuration);

            StyledLayerDescriptor sld = (StyledLayerDescriptor) parser.parse(surl.openStream());

            for (int i = 0; i < sld.getStyledLayers().length; i++) {
                Style[] styles = null;

                if (sld.getStyledLayers()[i] instanceof NamedLayer) {
                    NamedLayer layer = (NamedLayer) sld.getStyledLayers()[i];
                    styles = layer.getStyles();
                } else if (sld.getStyledLayers()[i] instanceof UserLayer) {
                    UserLayer layer = (UserLayer) sld.getStyledLayers()[i];
                    styles = layer.getUserStyles();
                }

                if (styles != null) {
                    for (Style s : styles) {
                        if (!(s instanceof NamedStyle)) {
                            return s;
                        }
                    }
                }
            }

            return null;

        } catch (Exception e) {
            if (e instanceof IOException) throw (IOException) e;
            throw (IOException) new IOException().initCause(e);
        }
    }

    /** Checks the pixel i/j has the specified color */
    public static void assertPixel(BufferedImage image, int i, int j, Color color) {
        Color actual = getPixelColor(image, i, j);

        Assert.assertEquals(color, actual);
    }

    /** Checks the pixel i/j has the specified color */
    public static void assertPixel(BufferedImage image, int i, int j, Color color, int tolerance) {
        Color actual = getPixelColor(image, i, j);

        Assert.assertTrue(Math.abs(color.getRed() - actual.getRed()) < tolerance);
        Assert.assertTrue(Math.abs(color.getGreen() - actual.getGreen()) < tolerance);
        Assert.assertTrue(Math.abs(color.getBlue() - actual.getBlue()) < tolerance);
    }

    /** Gets a specific pixel color from the specified buffered image */
    private static Color getPixelColor(BufferedImage image, int i, int j) {
        ColorModel cm = image.getColorModel();
        Raster raster = image.getRaster();
        Object pixel = raster.getDataElements(i, j, null);

        Color actual;
        if (cm.hasAlpha()) {
            actual = new Color(cm.getRed(pixel), cm.getGreen(pixel), cm.getBlue(pixel), cm.getAlpha(pixel));
        } else {
            actual = new Color(cm.getRed(pixel), cm.getGreen(pixel), cm.getBlue(pixel), 255);
        }
        return actual;
    }

    /** Configure the Bistream Vera Sans font so that it's available to the JVM */
    public static void setupVeraFonts() throws IOException, FontFormatException {
        FontCache.getDefaultInstance()
                .registerFont(java.awt.Font.createFont(
                        java.awt.Font.TRUETYPE_FONT,
                        TestData.getResource(RendererBaseTest.class, "Vera.ttf").openStream()));
    }
}
