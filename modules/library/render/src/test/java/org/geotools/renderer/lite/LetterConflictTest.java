/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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

import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.HeadlessException;
import java.awt.Panel;
import java.awt.RenderingHints;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.style.Style;
import org.geotools.data.property.PropertyDataStore;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.FeatureLayer;
import org.geotools.map.MapContent;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.renderer.label.LabelCacheImpl;
import org.geotools.test.TestData;
import org.geotools.util.logging.Logging;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/** Confirm functionality of letter level conflict detection. */
public class LetterConflictTest {
    static final Logger LOGGER = Logging.getLogger(LetterConflictTest.class);
    /**
     * Makes the test interactive, showing a Swing dialog with image.
     *
     * <p>Build with mvn -P image.interactive
     */
    static final boolean IMAGE_INTERACTIVE = Boolean.getBoolean("org.geotools.image.test.interactive");

    /**
     * Forces the image comparison / output tests to be skipped.
     *
     * <p>Build with org.geotools.image.test.skip=true
     */
    private static boolean IMAGE_SKIP = Boolean.getBoolean("org.geotools.image.test.skip");

    /**
     * Write images to temp directory for debugging.
     *
     * <p>Test with -D org.geotools.image.test.output=true
     */
    private static boolean OUTPUT_IMAGE = Boolean.getBoolean("org.geotools.image.test.output");

    private static final long TIME = 5000;
    SimpleFeatureSource fs_line1;
    SimpleFeatureSource fs_line2;
    SimpleFeatureSource fs_line3;
    SimpleFeatureSource fs_line4;
    ReferencedEnvelope bounds1;
    ReferencedEnvelope bounds2;

    @Before
    public void setUp() throws Exception {

        File property_line = new File(
                TestData.getResource(this, "letterConflict1.properties").toURI());
        PropertyDataStore ds_line = new PropertyDataStore(property_line.getParentFile());

        fs_line1 = ds_line.getFeatureSource("letterConflict1");
        fs_line2 = ds_line.getFeatureSource("letterConflict2");
        fs_line3 = ds_line.getFeatureSource("letterConflict3");
        bounds1 = new ReferencedEnvelope(-10, 10, -10, 10, DefaultGeographicCRS.WGS84);

        fs_line4 = ds_line.getFeatureSource("letterConflict4");
        bounds2 = new ReferencedEnvelope(20, 80, 23, 86, DefaultGeographicCRS.WGS84);

        RendererBaseTest.setupVeraFonts();
    }

    private StreamingRenderer getNewRenderer(MapContent context) {
        StreamingRenderer renderer = new StreamingRenderer();
        Map<Object, Object> rendererParams = new HashMap<>();
        LabelCacheImpl labelCache = new LabelCacheImpl();
        rendererParams.put(StreamingRenderer.LABEL_CACHE_KEY, labelCache);
        renderer.setRendererHints(rendererParams);
        renderer.setJava2DHints(new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON));
        renderer.setMapContent(context);
        return renderer;
    }

    @Test
    public void testLetterConflictEnabled() throws Exception {

        LabelCacheImpl.DISABLE_LETTER_LEVEL_CONFLICT = true;
        Style style = RendererBaseTest.loadStyle(this, "letterConflict20.sld");

        MapContent mc = new MapContent();
        mc.getViewport().setCoordinateReferenceSystem(DefaultGeographicCRS.WGS84);
        mc.addLayer(new FeatureLayer(fs_line1, style));
        StreamingRenderer renderer = getNewRenderer(mc);
        final BufferedImage image1 = RendererBaseTest.renderImage(renderer, bounds1, null);
        mc.dispose();

        LabelCacheImpl.DISABLE_LETTER_LEVEL_CONFLICT = false;
        style = RendererBaseTest.loadStyle(this, "letterConflict20.sld");

        mc = new MapContent();
        mc.getViewport().setCoordinateReferenceSystem(DefaultGeographicCRS.WGS84);
        mc.addLayer(new FeatureLayer(fs_line1, style));
        renderer = getNewRenderer(mc);
        final BufferedImage image2 = RendererBaseTest.renderImage(renderer, bounds1, null);
        mc.dispose();

        Assert.assertTrue(
                "More labels in image2 than image1",
                countPixels(image2, Color.BLACK) >= countPixels(image1, Color.BLACK));

        writeImage("letterConflictEnabledFalse", image1);
        writeImage("letterConflictEnabledTrue", image2);
        showImage("letterConflictEnabled false", TIME, image1);
        showImage("letterConflictEnabled true", TIME, image2);
    }

    @Test
    public void testLetterConflictEnabled2Lines() throws Exception {

        LabelCacheImpl.DISABLE_LETTER_LEVEL_CONFLICT = true;
        Style style = RendererBaseTest.loadStyle(this, "letterConflict20.sld");
        MapContent mc = new MapContent();
        mc.getViewport().setCoordinateReferenceSystem(DefaultGeographicCRS.WGS84);
        mc.addLayer(new FeatureLayer(fs_line2, style));
        StreamingRenderer renderer = getNewRenderer(mc);
        final BufferedImage image1 = RendererBaseTest.renderImage(renderer, bounds1, null);
        mc.dispose();

        LabelCacheImpl.DISABLE_LETTER_LEVEL_CONFLICT = false;
        style = RendererBaseTest.loadStyle(this, "letterConflict20.sld");
        mc = new MapContent();
        mc.getViewport().setCoordinateReferenceSystem(DefaultGeographicCRS.WGS84);

        mc.addLayer(new FeatureLayer(fs_line2, style));
        renderer = getNewRenderer(mc);
        final BufferedImage image2 = RendererBaseTest.renderImage(renderer, bounds1, null);

        Assert.assertTrue(
                "More labels in image2 than image1",
                countPixels(image2, Color.BLACK) > countPixels(image1, Color.BLACK));

        writeImage("letterConflictEnabled2False", image1);
        writeImage("letterConflictEnabled2True", image2);
        showImage("letterConflictEnabled2 false", TIME, image1);
        showImage("letterConflictEnabled2 true", TIME, image2);
        mc.dispose();
    }

    @Test
    public void testLetterConflictEnabledCurvedLine() throws Exception {

        LabelCacheImpl.DISABLE_LETTER_LEVEL_CONFLICT = true;
        Style style = RendererBaseTest.loadStyle(this, "letterConflict20.sld");
        MapContent mc = new MapContent();
        mc.getViewport().setCoordinateReferenceSystem(DefaultGeographicCRS.WGS84);
        mc.addLayer(new FeatureLayer(fs_line3, style));

        StreamingRenderer renderer = getNewRenderer(mc);
        final BufferedImage image1 = RendererBaseTest.renderImage(renderer, bounds1, null);
        mc.dispose();

        LabelCacheImpl.DISABLE_LETTER_LEVEL_CONFLICT = false;
        style = RendererBaseTest.loadStyle(this, "letterConflict20.sld");
        mc = new MapContent();
        mc.getViewport().setCoordinateReferenceSystem(DefaultGeographicCRS.WGS84);
        mc.addLayer(new FeatureLayer(fs_line3, style));
        renderer = getNewRenderer(mc);
        final BufferedImage image2 = RendererBaseTest.renderImage(renderer, bounds1, null);
        mc.dispose();

        Assert.assertTrue(
                "More labels in image2 than image1",
                countPixels(image2, Color.BLACK) > countPixels(image1, Color.BLACK));

        writeImage("letterConflictEnabledCurvedLineFalse", image1);
        writeImage("letterConflictEnabledCurvedLineTrue", image2);

        showImage("letterConflictEnabledCurvedLine false", TIME, image1);
        showImage("letterConflictEnabledCurvedLine true", TIME, image2);
    }

    @Test
    public void testLetterConflictEnabledPerf() throws Exception {
        synchronized (LabelCacheImpl.class) {
            LabelCacheImpl.DISABLE_LETTER_LEVEL_CONFLICT = true;
            Style style = RendererBaseTest.loadStyle(this, "letterConflict6.sld");
            MapContent mc = new MapContent();
            mc.getViewport().setCoordinateReferenceSystem(DefaultGeographicCRS.WGS84);
            mc.addLayer(new FeatureLayer(fs_line4, style));

            StreamingRenderer renderer = getNewRenderer(mc);
            BufferedImage image1 = RendererBaseTest.renderImage(renderer, bounds2, null);

            long t0, t1, t2, t3;
            long ta = 0;
            for (int i = 0; i < 10; i++) {
                renderer = getNewRenderer(mc);
                t0 = System.nanoTime();
                image1 = RendererBaseTest.renderImage(renderer, bounds2, null);
                t1 = System.nanoTime();
                ta += t1 - t0;
            }
            LOGGER.fine("time false " + ta / 10000000);
            mc.dispose();

            LabelCacheImpl.DISABLE_LETTER_LEVEL_CONFLICT = false;
            mc = new MapContent();
            mc.getViewport().setCoordinateReferenceSystem(DefaultGeographicCRS.WGS84);
            mc.addLayer(new FeatureLayer(fs_line4, style));
            renderer = getNewRenderer(mc);
            BufferedImage image2 = RendererBaseTest.renderImage(renderer, bounds2, null);

            ta = 0;
            for (int i = 0; i < 10; i++) {
                renderer = getNewRenderer(mc);
                t2 = System.nanoTime();
                image2 = RendererBaseTest.renderImage(renderer, bounds2, null);
                t3 = System.nanoTime();
                // System.out.println("time true " + (t3 - t2) + " ms");
                ta += t3 - t2;
            }
            LOGGER.fine("time true " + ta / 10000000);
            mc.dispose();

            Assert.assertTrue("More labels in image2 than image1", countDarkPixels(image2) >= countDarkPixels(image1));

            writeImage("letterConflictEnabledPerfFalse", image1);
            writeImage("letterConflictEnabledPerfTrue", image2);
            showImage("letterConflictEnabledPref false", TIME, image1);
            showImage("letterConflictEnabledPerf true", TIME, image2);
        }
    }

    public int countPixels(BufferedImage bi, Color color) {
        int count = 0;
        for (int i = 0; i < bi.getWidth(); i++) {
            for (int j = 0; j < bi.getHeight(); j++) {
                if (bi.getRGB(i, j) == color.getRGB()) count++;
            }
        }
        return count;
    }

    public int countDarkPixels(BufferedImage bi) {
        int count = 0;
        for (int i = 0; i < bi.getWidth(); i++) {
            for (int j = 0; j < bi.getHeight(); j++) {
                Color col = new Color(bi.getRGB(i, j));
                if (col.getBlue() < 127 && col.getGreen() < 127 && col.getRed() < 127) count++;
            }
        }
        return count;
    }

    /** Internal utility method used to write out image for debugging purposes. */
    static void writeImage(String testName, BufferedImage image) throws IOException {
        if (IMAGE_SKIP) return;

        if (OUTPUT_IMAGE) {
            File tmpFile = File.createTempFile("geotools-" + testName, ".png");
            ImageIO.write(image, "png", tmpFile);
        }
    }

    /**
     * Internal utility method used to display an image for interactive tests.
     *
     * @param testName test name used as window name
     */
    static void showImage(String testName, long timeOut, final BufferedImage image) throws InterruptedException {
        boolean HEADLESS = Boolean.getBoolean("java.awt.headless");

        if (HEADLESS || IMAGE_SKIP) {
            return; // obvious reasons to skip showing the image
        }

        if (IMAGE_INTERACTIVE && TestData.isInteractiveTest()) {
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
}
