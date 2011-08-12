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

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JPanel;

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.MapContent;
import org.geotools.swing.event.MapMouseListener;
import org.geotools.swing.tool.MapToolManager;

/**
 * Mock map pane class for tests that require a graphics environment.
 * Allows access to the MapToolManager field.
 * 
 * @author Michael Bedward
 * @since 8.0
 * @source $URL$
 * @version $Id$
 */
public class MockGraphicsMapPane extends JPanel {

    private MockMapPane innerPane;

    public MockGraphicsMapPane() {
        innerPane = new MockMapPane();
        addMouseListener(new DelegatingMouseListener(innerPane.getMapToolManager()));
        addMouseMotionListener(new DelegatingMouseListener(innerPane.getMapToolManager()));
    }

    public void setMapContent(MapContent content) {
        innerPane.setMapContent(content);
    }

    public ReferencedEnvelope getDisplayArea() {
        return innerPane.getDisplayArea();
    }

    public void addMouseListener(MapMouseListener listener) {
        innerPane.addMouseListener(listener);
    }
    

    class DelegatingMouseListener implements MouseListener, MouseMotionListener {

        private final MapToolManager delegate;

        DelegatingMouseListener(MapToolManager delegate) {
            this.delegate = delegate;
        }

        @Override
        public void mouseClicked(MouseEvent me) {
            delegate.mouseClicked(me);
        }

        @Override
        public void mousePressed(MouseEvent me) {
            delegate.mousePressed(me);
        }

        @Override
        public void mouseReleased(MouseEvent me) {
            delegate.mouseReleased(me);
        }

        @Override
        public void mouseEntered(MouseEvent me) {
            delegate.mouseEntered(me);
        }

        @Override
        public void mouseExited(MouseEvent me) {
            delegate.mouseExited(me);
        }

        @Override
        public void mouseDragged(MouseEvent me) {
            delegate.mouseDragged(me);
        }

        @Override
        public void mouseMoved(MouseEvent me) {
            delegate.mouseMoved(me);
        }
    }
}
