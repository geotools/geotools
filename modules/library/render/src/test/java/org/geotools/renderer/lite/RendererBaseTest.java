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
import java.io.IOException;

import org.geotools.data.FeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.DefaultMapContext;
import org.geotools.map.MapContent;
import org.geotools.map.MapContext;
import org.geotools.renderer.GTRenderer;
import org.geotools.renderer.RenderListener;
import org.geotools.styling.SLDParser;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.test.TestData;

/**
 * Used to test a renderer implementation.
 * <p>
 * Mostly tests that the renderer produces an image at all.
 * 
 * @author Simone Giannecchini
 * 
 *
 * @source $URL$
 *         http://svn.osgeo.org/geotools/trunk/modules/library/render/src/test/java/org/geotools
 *         /renderer/lite/RendererBaseTest.java $
 */
public abstract class RendererBaseTest {

    protected RendererBaseTest() {

    }

    /**
     * bounds may be null
     * 
     * @param testName
     *            Name reported in event of failure
     * @param renderer
     *            Renderer being tested
     * @param timeOut
     *            Maximum time allowed for test
     * @param bounds
     *            area to draw
     * @throws Exception
     *             In the event of failure
     */
    protected static BufferedImage showRender(String testName, GTRenderer renderer, long timeOut,
            ReferencedEnvelope... bounds) throws Exception {
        return showRender(testName, renderer, timeOut, bounds, null);
    }

    /**
     * bounds may be null
     * 
     * @param testName
     *            Name reported in event of failure
     * @param renderer
     *            Renderer being tested
     * @param timeOut
     *            Maximum time allowed for test
     * @param bounds
     *            area to draw
     * @param listener
     *            Optional listener
     * @throws Exception
     *             In the event of failure
     */
    protected static BufferedImage showRender(String testName, GTRenderer renderer, long timeOut,
            ReferencedEnvelope[] bounds, RenderListener listener) throws Exception {
        BufferedImage[] images = new BufferedImage[bounds.length];
        for (int i = 0; i < images.length; i++) {
            images[i] = renderImage(renderer, bounds[i], listener);
        }
        final BufferedImage image = mergeImages(images);

        final String headless = System.getProperty("java.awt.headless", "false");
        if (!headless.equalsIgnoreCase("true") && TestData.isInteractiveTest()) {
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
                return image;
            }
        }
        boolean hasData = false; // All I can seem to check reliably.

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                if (image.getRGB(x, y) != 0) {
                    hasData = true;
                }
            }
        }

        assert (hasData);

        return image;
    }

    private static BufferedImage renderImage(GTRenderer renderer, ReferencedEnvelope bounds,
            RenderListener listener) {
        int w = 300;
        int h = 300;
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
        BufferedImage joinedImage = new BufferedImage(totalWidth, height,
                BufferedImage.TYPE_INT_ARGB);
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
     * @param obj
     *            Rendering implementation being tested, usually GTRenderer
     * @param g
     *            graphics used
     * @param rect
     *            image area
     * @param bounds
     *            world area
     * @param rendererListener
     *            optional rendererListener, may be null
     */
    private static void render(GTRenderer renderer, Graphics g, Rectangle rect,
            ReferencedEnvelope bounds, RenderListener rendererListener) {
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

    /**
     * Utility to quickly render a set of vector data on top of a buffered image
     * 
     * @param sources
     * @param styles
     * @return
     * @throws Exception
     */
    public static BufferedImage render(MapContext map) throws Exception {
        StreamingRenderer r = new StreamingRenderer();
        r.setContext(map);

        return RendererBaseTest.showRender("testPointLabeling", r, 5000, map.getLayerBounds());
    }

    /**
     * Load a style from the test-data directory associated with the object.
     * 
     * @param loader
     * @param sldFilename
     * @return
     * @throws IOException
     */
    protected static Style loadStyle(Object loader, String sldFilename) throws IOException {
        StyleFactory factory = CommonFactoryFinder.getStyleFactory(null);

        java.net.URL surl = TestData.getResource(loader, sldFilename);
        SLDParser stylereader = new SLDParser(factory, surl);

        Style style = stylereader.readXML()[0];
        return style;
    }
}
