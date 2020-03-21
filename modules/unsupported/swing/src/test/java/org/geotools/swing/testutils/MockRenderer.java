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

package org.geotools.swing.testutils;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.MapContent;
import org.geotools.renderer.GTRenderer;
import org.geotools.renderer.RenderListener;
import org.geotools.util.factory.Hints;
import org.locationtech.jts.geom.Envelope;

/**
 * A simple mock GTRenderer.
 *
 * @author Michael Bedward
 * @since 8.0
 * @version $Id$
 */
public class MockRenderer implements GTRenderer {
    protected List<RenderListener> listeners = new ArrayList<RenderListener>();

    private MapContent mapContent;
    private long paintTime;
    private boolean verbose;

    private CountDownLatch paintLatch = new CountDownLatch(0);
    private Lock lock = new ReentrantLock();

    public MockRenderer() {
        this(null);
    }

    public MockRenderer(MapContent mapContent) {
        this.mapContent = mapContent;
        this.paintTime = 0;
    }

    /**
     * Sets the time that the mock renderer will pretend to paint.
     *
     * @param millis time in milliseconds
     */
    public void setPaintTime(long millis) {
        paintTime = millis < 0 ? 0 : millis;
    }

    public void setVerbose(boolean b) {
        verbose = b;
    }

    @Override
    public void stopRendering() {
        lock.lock();
        try {
            paintLatch.countDown();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void addRenderListener(RenderListener listener) {
        this.listeners.add(listener);
    }

    @Override
    public void removeRenderListener(RenderListener listener) {
        this.listeners.remove(listener);
    }

    @Override
    public void setJava2DHints(RenderingHints hints) {}

    @Override
    public RenderingHints getJava2DHints() {
        return new Hints();
    }

    @Override
    public void setRendererHints(Map<Object, Object> hints) {}

    @Override
    public Map<Object, Object> getRendererHints() {
        return Collections.emptyMap();
    }

    @Override
    public void setMapContent(MapContent mapContent) {
        this.mapContent = mapContent;
    }

    @Override
    public MapContent getMapContent() {
        return mapContent;
    }

    @Override
    public void paint(Graphics2D graphics, Rectangle paintArea, AffineTransform worldToScreen) {
        pretendToPaint();
    }

    @Override
    public void paint(Graphics2D graphics, Rectangle paintArea, Envelope mapArea) {
        pretendToPaint();
    }

    @Override
    public void paint(Graphics2D graphics, Rectangle paintArea, ReferencedEnvelope mapArea) {
        pretendToPaint();
    }

    @Override
    public void paint(
            Graphics2D graphics,
            Rectangle paintArea,
            Envelope mapArea,
            AffineTransform worldToScreen) {
        pretendToPaint();
    }

    @Override
    public void paint(
            Graphics2D graphics,
            Rectangle paintArea,
            ReferencedEnvelope mapArea,
            AffineTransform worldToScreen) {
        pretendToPaint();
    }

    protected void pretendToPaint() {
        lock.lock();
        try {
            if (verbose) {
                // System.out.println("mock paint started");
                System.out.flush();
            }

            if (paintTime > 0) {
                paintLatch = new CountDownLatch(1);
            }
        } finally {
            lock.unlock();
        }

        boolean wasCancelled = false;
        try {
            wasCancelled = paintLatch.await(paintTime, TimeUnit.MILLISECONDS);
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }

        if (verbose) {
            if (wasCancelled) {
                // System.out.println("mock paint cancelled");
            } else {
                // System.out.println("mock paint finished");
            }
            System.out.flush();
        }
    }
}
