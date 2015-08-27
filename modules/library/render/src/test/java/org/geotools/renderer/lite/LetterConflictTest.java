/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011 - 2015, Open Source Geospatial Foundation (OSGeo)
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

import junit.framework.TestCase;
import org.geotools.data.property.PropertyDataStore;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.DefaultMapContext;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.renderer.label.LabelCacheImpl;
import org.geotools.styling.Style;
import org.geotools.test.TestData;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;

/**
 * Created by mmichaud on 19/08/2015.
 */
public class LetterConflictTest extends TestCase {

    private boolean SHOW_RESULT = Boolean.getBoolean("org.geotools.labelcache.showLetterConflictTestResult");
    private boolean OUTPUT_IMAGE = Boolean.getBoolean("org.geotools.labelcache.outputLetterConflictTestResult");

    private static final long TIME = 5000;
    SimpleFeatureSource fs_line1;
    SimpleFeatureSource fs_line2;
    SimpleFeatureSource fs_line3;
    SimpleFeatureSource fs_line4;
    ReferencedEnvelope bounds1;
    ReferencedEnvelope bounds2;

    @Override
    protected void setUp() throws Exception {

        File property_line = new File(TestData.getResource(this, "letterConflict1.properties").toURI());
        PropertyDataStore ds_line = new PropertyDataStore(property_line.getParentFile());

        fs_line1 = ds_line.getFeatureSource("letterConflict1");
        fs_line2 = ds_line.getFeatureSource("letterConflict2");
        fs_line3 = ds_line.getFeatureSource("letterConflict3");
        bounds1 = new ReferencedEnvelope(-10, 10, -10, 10, DefaultGeographicCRS.WGS84);

        fs_line4 = ds_line.getFeatureSource("letterConflict4");
        bounds2 = new ReferencedEnvelope(20, 80, 23, 86, DefaultGeographicCRS.WGS84);

    }

    private StreamingRenderer getNewRenderer(DefaultMapContext context) {
        StreamingRenderer renderer = new StreamingRenderer();
        Map rendererParams = new HashMap();
        LabelCacheImpl labelCache = new LabelCacheImpl();
        rendererParams.put(StreamingRenderer.LABEL_CACHE_KEY, labelCache);
        renderer.setRendererHints(rendererParams);
        renderer.setJava2DHints(new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON));
        renderer.setContext(context);
        return renderer;
    }

    public void testLetterConflictEnabled() throws Exception {

        LabelCacheImpl.DISABLE_LETTER_LEVEL_CONFLICT = true;
        Style style = RendererBaseTest.loadStyle(this, "letterConflict20.sld");
        DefaultMapContext mc = new DefaultMapContext(DefaultGeographicCRS.WGS84);
        mc.addLayer(fs_line1, style);
        StreamingRenderer renderer = getNewRenderer(mc);
        final BufferedImage image1 = RendererBaseTest.renderImage(renderer, bounds1, null);

        LabelCacheImpl.DISABLE_LETTER_LEVEL_CONFLICT = false;
        style = RendererBaseTest.loadStyle(this, "letterConflict20.sld");
        mc = new DefaultMapContext(DefaultGeographicCRS.WGS84);
        mc.addLayer(fs_line1, style);
        renderer = getNewRenderer(mc);
        final BufferedImage image2 = RendererBaseTest.renderImage(renderer, bounds1, null);

        assertTrue("More labels in image2 than image1",
                countPixels(image2, Color.BLACK) > countPixels(image1, Color.BLACK));

        if (OUTPUT_IMAGE) {
            // Write to file
            File tmpFile = File.createTempFile("geotools", ".png");
            ImageIO.write(image1, "png", tmpFile);
            tmpFile = File.createTempFile("geotools", ".png");
            ImageIO.write(image2, "png", tmpFile);
        }
        if (SHOW_RESULT) {
            // Interactive visualization
            showImage("letterConflictEnabled false", TIME, image1);
            showImage("letterConflictEnabled true", TIME, image2);
        }
    }

    public void testLetterConflictEnabled2Lines() throws Exception {

        LabelCacheImpl.DISABLE_LETTER_LEVEL_CONFLICT = true;
        Style style = RendererBaseTest.loadStyle(this, "letterConflict20.sld");
        DefaultMapContext mc = new DefaultMapContext(DefaultGeographicCRS.WGS84);
        mc.addLayer(fs_line2, style);
        StreamingRenderer renderer = getNewRenderer(mc);
        final BufferedImage image1 = RendererBaseTest.renderImage(renderer, bounds1, null);

        LabelCacheImpl.DISABLE_LETTER_LEVEL_CONFLICT = false;
        style = RendererBaseTest.loadStyle(this, "letterConflict20.sld");
        mc = new DefaultMapContext(DefaultGeographicCRS.WGS84);
        mc.addLayer(fs_line2, style);
        renderer = getNewRenderer(mc);
        final BufferedImage image2 = RendererBaseTest.renderImage(renderer, bounds1, null);

        assertTrue("More labels in image2 than image1",
                countPixels(image2, Color.BLACK) > countPixels(image1, Color.BLACK));

        if (OUTPUT_IMAGE) {
            // Write to file
            File tmpFile = File.createTempFile("geotools",".png");
            ImageIO.write(image1, "png", tmpFile);
            tmpFile = File.createTempFile("geotools",".png");
            ImageIO.write(image2, "png", tmpFile);
        }
        if (SHOW_RESULT) {
            // Interactive visualization
            showImage("letterConflictEnabled false", TIME, image1);
            showImage("letterConflictEnabled true", TIME, image2);
        }
    }

    public void testLetterConflictEnabledCurvedLine() throws Exception {

        LabelCacheImpl.DISABLE_LETTER_LEVEL_CONFLICT = true;
        Style style = RendererBaseTest.loadStyle(this, "letterConflict20.sld");
        DefaultMapContext mc = new DefaultMapContext(DefaultGeographicCRS.WGS84);
        mc.addLayer(fs_line3, style);
        StreamingRenderer renderer = getNewRenderer(mc);
        final BufferedImage image1 = RendererBaseTest.renderImage(renderer, bounds1, null);

        LabelCacheImpl.DISABLE_LETTER_LEVEL_CONFLICT = false;
        style = RendererBaseTest.loadStyle(this, "letterConflict20.sld");
        mc = new DefaultMapContext(DefaultGeographicCRS.WGS84);
        mc.addLayer(fs_line3, style);
        renderer = getNewRenderer(mc);
        final BufferedImage image2 = RendererBaseTest.renderImage(renderer, bounds1, null);

        assertTrue("More labels in image2 than image1",
                countPixels(image2, Color.BLACK) > countPixels(image1, Color.BLACK));

        if (OUTPUT_IMAGE) {
            // Write to file
            File tmpFile = File.createTempFile("geotools", ".png");
            ImageIO.write(image1, "png", tmpFile);
            tmpFile = File.createTempFile("geotools", ".png");
            ImageIO.write(image2, "png", tmpFile);
        }
        if (SHOW_RESULT) {
            // Interactive visualization
            showImage("letterConflictEnabled false", TIME, image1);
            showImage("letterConflictEnabled true", TIME, image2);
        }
    }

    public void testLetterConflictEnabledPerf() throws Exception {
        synchronized (LabelCacheImpl.class) {
            LabelCacheImpl.DISABLE_LETTER_LEVEL_CONFLICT = true;
            Style style = RendererBaseTest.loadStyle(this, "letterConflict6.sld");
            DefaultMapContext mc = new DefaultMapContext(DefaultGeographicCRS.WGS84);
            mc.addLayer(fs_line4, style);
            StreamingRenderer renderer = getNewRenderer(mc);
            BufferedImage image1 = RendererBaseTest.renderImage(renderer, bounds2, null);

            long t0, t1, t2, t3;
            long ta = 0;
            for (int i = 0; i < 10; i++) {
                renderer = getNewRenderer(mc);
                t0 = System.nanoTime();
                image1 = RendererBaseTest.renderImage(renderer, bounds2, null);
                t1 = System.nanoTime();
                ta += (t1 - t0);
            }
            System.out.println("time false " + ta / 10000000);

            LabelCacheImpl.DISABLE_LETTER_LEVEL_CONFLICT = false;
            mc = new DefaultMapContext(DefaultGeographicCRS.WGS84);
            mc.addLayer(fs_line4, style);
            renderer = getNewRenderer(mc);
            BufferedImage image2 = RendererBaseTest.renderImage(renderer, bounds2, null);

            ta = 0;
            for (int i = 0; i < 10; i++) {
                renderer = getNewRenderer(mc);
                t2 = System.nanoTime();
                image2 = RendererBaseTest.renderImage(renderer, bounds2, null);
                t3 = System.nanoTime();
                //System.out.println("time true " + (t3 - t2) + " ms");
                ta += (t3 - t2);
            }
            System.out.println("time true " + ta / 10000000);

            assertTrue("More labels in image2 than image1",
                    countDarkPixels(image2) > countDarkPixels(image1));

            if (OUTPUT_IMAGE) {
                // Write to file
                File tmpFile = File.createTempFile("geotools", ".png");
                ImageIO.write(image1, "png", tmpFile);
                tmpFile = File.createTempFile("geotools", ".png");
                ImageIO.write(image2, "png", tmpFile);
            }
            if (SHOW_RESULT) {
                // Interactive visualization
                showImage("letterConflictEnabled false", TIME, image1);
                showImage("letterConflictEnabled true", TIME, image2);
            }
        }
    }

    public int countPixels(BufferedImage bi, Color color) {
        int count = 0;
        for (int i = 0 ; i < bi.getWidth() ; i++) {
            for (int j = 0 ; j < bi.getHeight() ; j++) {
                if (bi.getRGB(i,j) == color.getRGB()) count++;
            }
        }
        return count;
    }


    public int countDarkPixels(BufferedImage bi) {
        int count = 0;
        for (int i = 0 ; i < bi.getWidth() ; i++) {
            for (int j = 0 ; j < bi.getHeight() ; j++) {
                Color col = new Color(bi.getRGB(i,j));
                if (col.getBlue() < 127 && col.getGreen() < 127 &&  col.getRed() < 127) count++;
            }
        }
        return count;
    }


    public static void showImage(String testName, long timeOut, final BufferedImage image)
            throws InterruptedException {
        //final String headless = System.getProperty("java.awt.headless", "false");
        //if (!headless.equalsIgnoreCase("true") && TestData.isInteractiveTest()) {
        try {
            Frame frame = new Frame(testName);
            frame.addWindowListener(new WindowAdapter() {

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
        //}
    }
}
