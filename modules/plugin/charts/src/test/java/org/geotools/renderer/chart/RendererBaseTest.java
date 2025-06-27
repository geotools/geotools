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
package org.geotools.renderer.chart;

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
import org.geotools.api.style.Style;
import org.geotools.api.style.StyleFactory;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.renderer.GTRenderer;
import org.geotools.test.TestData;
import org.geotools.xml.styling.SLDParser;

/** @author Simone Giannecchini */
public abstract class RendererBaseTest {

    public RendererBaseTest() {}

    /** bounds may be null */
    protected static void showRender(
            String testName, Object renderer, long timeOut, ReferencedEnvelope bounds, final int w, final int h)
            throws Exception {
        final BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics g = image.getGraphics();
        g.setColor(Color.white);
        g.fillRect(0, 0, w, h);
        render(renderer, g, new Rectangle(w, h), bounds);

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
                        setPreferredSize(new Dimension(w, h));
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
                return;
            }
        }
        boolean hasData = false; // All I can seem to check reliably.

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                if (image.getRGB(x, y) != -1) {
                    hasData = true;
                }
            }
        }

        assert hasData;
    }

    /** responsible for actually rendering. */
    private static void render(Object obj, Graphics g, Rectangle rect, ReferencedEnvelope bounds) {
        if (obj instanceof GTRenderer) {
            GTRenderer renderer = (GTRenderer) obj;

            if (bounds == null) {
                renderer.paint((Graphics2D) g, rect, new AffineTransform());
            } else {
                renderer.paint((Graphics2D) g, rect, bounds);
            }
        }
    }

    static Style loadStyle(Object loader, String sldFilename) throws IOException {
        StyleFactory factory = CommonFactoryFinder.getStyleFactory(null);

        java.net.URL surl = TestData.getResource(loader, sldFilename);
        SLDParser stylereader = new SLDParser(factory, surl);

        Style style = stylereader.readXML()[0];
        return style;
    }
}
