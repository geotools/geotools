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

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
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
    protected static BufferedImage showRender(String testName, Object renderer, long timeOut,
            ReferencedEnvelope bounds) throws Exception {
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
    protected static BufferedImage showRender(String testName, Object renderer, long timeOut,
            ReferencedEnvelope bounds, RenderListener listener) throws Exception {
        final int w = 300;
        final int h = 300;
        final BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics g = image.getGraphics();
        g.setColor(Color.white);
        g.fillRect(0, 0, w, h);
        render(renderer, g, new Rectangle(w, h), bounds, listener);

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
                        setPreferredSize(new Dimension(w, h));
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

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                if (image.getRGB(x, y) != 0) {
                    hasData = true;
                }
            }
        }

        assert (hasData);
        return image;
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
    private static void render(Object renderingImplementation, Graphics g, Rectangle rect,
            ReferencedEnvelope bounds, RenderListener rendererListener) {
        if (renderingImplementation instanceof GTRenderer) {
            GTRenderer renderer = (GTRenderer) renderingImplementation;
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
