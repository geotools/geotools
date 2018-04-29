/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.swing.tool;

import java.awt.Cursor;
import java.awt.Rectangle;
import javax.swing.JComponent;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.Envelope2D;
import org.geotools.swing.JMapPane;
import org.geotools.swing.event.MapMouseEvent;

/**
 * Allow scrolling with the mouse wheel.
 *
 * @author Ian Turton
 * @since 2.15
 */
public class ScrollWheelTool extends AbstractZoomTool {

    public ScrollWheelTool(JMapPane mapPane) {
        setMapPane(mapPane);
    }

    @Override
    public Cursor getCursor() {
        return null;
    }

    @Override
    public void onMouseWheelMoved(MapMouseEvent ev) {

        Rectangle paneArea = ((JComponent) getMapPane()).getVisibleRect();

        DirectPosition2D mapPos = ev.getWorldPos();

        double scale = getMapPane().getWorldToScreenTransform().getScaleX();
        int clicks = ev.getWheelAmount();

        double actualZoom = 1;
        // positive clicks are down - zoom out

        if (clicks > 0) {
            actualZoom = -1.0 / (clicks * getZoom());
        } else {
            actualZoom = clicks * getZoom();
        }
        double newScale = scale * actualZoom;

        DirectPosition2D corner =
                new DirectPosition2D(
                        mapPos.getX() - 0.5d * paneArea.getWidth() / newScale,
                        mapPos.getY() + 0.5d * paneArea.getHeight() / newScale);

        // I would prefer to offset the new map based on the cursor but this matches
        // the current zoom in/out tools.

        Envelope2D newMapArea = new Envelope2D();
        newMapArea.setFrameFromCenter(mapPos, corner);
        getMapPane().setDisplayArea(newMapArea);
    }
}
