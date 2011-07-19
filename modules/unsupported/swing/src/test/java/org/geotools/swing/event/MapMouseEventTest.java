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
package org.geotools.swing.event;

import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.swing.testutils.MockMapPane;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for MapMouseEvent.
 * 
 * @author Michael Bedward
 * @since 8.0
 * @source $URL$
 * @version $Id$
 */
public class MapMouseEventTest {
    
    private static final double TOL = 1.0e-6;
    
    // screen area with aspect 1:1
    private static final Rectangle SCREEN = new Rectangle(100, 100);
    
    // world area with aspect 2:1
    private static final ReferencedEnvelope WORLD = new ReferencedEnvelope(
            149.0, 153.0, -33, -35, DefaultGeographicCRS.WGS84);
    
    private static final int x = SCREEN.width / 2;
    private static final int y = SCREEN.height / 2;

    private static final MockMapPane pane = new MockMapPane();
    
    @Before
    public void setup() {
        pane.setScreenArea(SCREEN);
        pane.setDisplayArea(WORLD);
    }

    @Test
    public void getSource() {
        MapMouseEvent ev = createEvent(0, 0);
        assertEquals(pane, ev.getSource());
    }
    
    @Test
    public void getWorldPos() {
        AffineTransform tr = pane.getMapContent().getViewport().getScreenToWorld();
        Point2D p = new Point2D.Double(x, y);
        tr.transform(p, p);
        
        MapMouseEvent ev = createEvent(x, y);
        DirectPosition2D pos = ev.getWorldPos();
        
        assertEquals(p.getX(), pos.x, TOL);
        assertEquals(p.getY(), pos.y, TOL);
    }
    
    @Test
    public void getEnvelopeByPixels() {
        AffineTransform tr = pane.getMapContent().getViewport().getScreenToWorld();
        Rectangle2D screenRect = new Rectangle2D.Double(x - 0.5, y - 0.5, 1, 1);
        Rectangle2D expected = tr.createTransformedShape(screenRect).getBounds2D();
        
        MapMouseEvent ev = createEvent(x, y);
        ReferencedEnvelope actual = ev.getEnvelopeByPixels(1);
        
        assertRect(expected, actual);
    }
    
    @Test
    public void getEnvelopeByWorld() {
        final double w = 0.1;
        AffineTransform tr = pane.getMapContent().getViewport().getScreenToWorld();
        Point2D p = new Point2D.Double(x, y);
        tr.transform(p, p);
        Rectangle2D expected = new Rectangle2D.Double(p.getX() - w/2, p.getY() - w/2, w, w);
        
        MapMouseEvent ev = createEvent(x, y);
        ReferencedEnvelope actual = ev.getEnvelopeByWorld(0.1);
        
        assertRect(expected, actual);
    }

    private MapMouseEvent createEvent(int x, int y) {
        MouseEvent ev = new MouseEvent(pane, MouseEvent.MOUSE_PRESSED, 0L, 0, x, y, 1, false);
        MapMouseEvent mapEv = new MapMouseEvent(pane, ev);
        return mapEv;
    }

    private void assertRect(Rectangle2D expected, ReferencedEnvelope actual) {
        assertEquals(expected.getMinX(), actual.getMinX(), TOL);
        assertEquals(expected.getMaxX(), actual.getMaxX(), TOL);
        assertEquals(expected.getMinY(), actual.getMinY(), TOL);
        assertEquals(expected.getMaxY(), actual.getMaxY(), TOL);
    }
}
