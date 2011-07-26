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

package org.geotools.swing.control;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.swing.JLabel;

import org.geotools.geometry.DirectPosition2D;
import org.geotools.swing.MapPane;
import org.geotools.swing.event.MapMouseAdapter;
import org.geotools.swing.event.MapMouseEvent;

/**
 * A status bar item that displays the world coordinates of the mouse cursor
 * position.
 *
 * @author Michael Bedward
 * @since 8.0
 * @source $URL$
 * @version $Id$
 */
public class CoordsStatusBarItem extends StatusBarItem {
    private static final int DEFAULT_NUM_DECIMALS = 2;
    private static final String NO_COORDS = "No cursor";

    private final JLabel label;

    private int numDecimals;
    private String numFormat;

    private final Lock lock = new ReentrantLock();

    /**
     * Creates a new item to display cursor position for the given map pane.
     *
     * @param mapPane the map pane
     */
    CoordsStatusBarItem(MapPane mapPane) {
        super("Coordinates");

        if (mapPane == null) {
            throw new IllegalArgumentException("mapPane must not be null");
        }

        doSetPrecision(DEFAULT_NUM_DECIMALS);
        
        label = new JLabel();
        label.setFont(JMapStatusBar.getDefaultFont());
        add(label);
        noCursor();

        mapPane.addMouseListener(new MapMouseAdapter() {
            @Override
            public void onMouseEntered(MapMouseEvent ev) {
                displayCoords(ev.getWorldPos());
            }

            @Override
            public void onMouseExited(MapMouseEvent ev) {
                noCursor();
            }

            @Override
            public void onMouseMoved(MapMouseEvent ev) {
                displayCoords(ev.getWorldPos());
            }
        });
    }

    /**
     * Gets the current number of decimal places displayed for
     * coordinates.
     *
     * @return number of decimal places
     */
    public int getPrecision() {
        return numDecimals;
    }

    /**
     * Sets the number of decimal places to display for coordinates.
     *
     * @param numDecimals number of decimal places
     */
    public void setPrecision(int numDecimals) {
        doSetPrecision(numDecimals);
    }

    /**
     * Private helper for {@linkplain #setPrecision(int)} that can be called
     * safely from the constructor.
     *
     * @param numDecimals number of decimal places
     */
    private void doSetPrecision(int numDecimals) {
        lock.lock();
        try {
            numDecimals = DEFAULT_NUM_DECIMALS;

            StringBuilder sb = new StringBuilder();
            sb.append("%.").append(numDecimals).append("f, ");
            sb.append("%.").append(numDecimals).append("f");
            numFormat = sb.toString();
        } finally {
            lock.unlock();
        }
    }

    /**
     * Displays coordinates of the given position.
     *
     * @param p world position
     */
    private void displayCoords(DirectPosition2D p) {
        lock.lock();
        try {
            label.setText(String.format(numFormat, p.getX(), p.getY()));
        } finally {
            lock.unlock();
        }
    }

    /**
     * Sets a message to indicate that the cursor is out of the map pane.
     */
    private void noCursor() {
        label.setText(NO_COORDS);
    }

}
