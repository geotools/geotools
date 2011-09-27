/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.swt.utils;

import org.eclipse.jface.window.ApplicationWindow;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.swt.SwtMapPane;
import org.geotools.swt.event.MapMouseAdapter;
import org.geotools.swt.event.MapMouseEvent;
import org.geotools.swt.event.MapPaneAdapter;
import org.geotools.swt.event.MapPaneEvent;

/**
 * The notifier for the statusbar. 
 * 
 * @author Andrea Antonello (www.hydrologis.com)
 *
 *
 *
 * @source $URL$
 */
public class StatusBarNotifier {
    private final ApplicationWindow applicationWindow;
    private MapMouseAdapter mouseListener;
    private MapPaneAdapter mapPaneListener;

    public StatusBarNotifier( ApplicationWindow applicationWindow, SwtMapPane mapPane ) {
        this.applicationWindow = applicationWindow;

        createListeners();

        mapPane.addMouseListener(mouseListener);
        mapPane.addMapPaneListener(mapPaneListener);
    }

    /**
     * Initialize the mouse and map bounds listeners
     */
    private void createListeners() {
        mouseListener = new MapMouseAdapter(){

            @Override
            public void onMouseMoved( MapMouseEvent ev ) {
                displayCoords(ev.getMapPosition());
            }

            @Override
            public void onMouseExited( MapMouseEvent ev ) {
                clearCoords();
            }
        };

        mapPaneListener = new MapPaneAdapter(){

            @Override
            public void onDisplayAreaChanged( MapPaneEvent ev ) {
            }

            @Override
            public void onResized( MapPaneEvent ev ) {
            }

            @Override
            public void onRenderingStarted( MapPaneEvent ev ) {
                applicationWindow.setStatus("rendering...");
            }

            @Override
            public void onRenderingStopped( MapPaneEvent ev ) {
                applicationWindow.setStatus("");
            }

            @Override
            public void onRenderingProgress( MapPaneEvent ev ) {
            }

        };
    }

    /**
     * Format and display the coordinates of the given position
     *
     * @param mapPos mouse cursor position (world coords)
     */
    public void displayCoords( DirectPosition2D mapPos ) {
        if (mapPos != null) {
            applicationWindow.setStatus(String.format("  %.2f %.2f", mapPos.x, mapPos.y));
        }
    }

    public void clearCoords() {
        applicationWindow.setStatus("");
    }

}
