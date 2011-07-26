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

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.swing.MapPane;
import org.geotools.swing.event.MapPaneAdapter;
import org.geotools.swing.event.MapPaneEvent;

/**
 * A status bar item that displays the map pane's world bounds.
 *
 * @author Michael Bedward
 * @since 8.0
 * @source $URL$
 * @version $Id$
 */
public class ExtentStatusBarItem extends StatusBarItem {
    private final JLabel label;

    private static final int DEFAULT_NUM_DECIMALS = 2;
    private int numDecimals;
    private String numFormat;

    private final Lock lock = new ReentrantLock();

    /**
     * Creates a new item to display the extent of the associated
     * map pane.
     *
     * @param mapPane the map pane
     * @throws IllegalArgumentException if {@code mapPane} is {@code null}
     */
    public ExtentStatusBarItem(MapPane mapPane) {
        super("Extent");

        if (mapPane == null) {
            throw new IllegalArgumentException("mapPane must not be null");
        }

        doSetPrecision(DEFAULT_NUM_DECIMALS);
        label = new JLabel();
        label.setFont(JMapStatusBar.getDefaultFont());
        add(label);

        mapPane.addMapPaneListener(new MapPaneAdapter() {
            @Override
            public void onDisplayAreaChanged(MapPaneEvent ev) {
                displayExtent(ev.getSource().getDisplayArea());
            }
        });
    }

    /**
     * Displays extent in world coordinates.
     * 
     * @param extent the map pane extent
     */
    private void displayExtent(ReferencedEnvelope extent) {
        if (extent == null || extent.isEmpty()) {
            label.setText("Undefined extent");
        } else {
            label.setText(String.format(numFormat,
                    extent.getMinX(), extent.getMaxX(),
                    extent.getMinY(), extent.getMaxY()));
        }
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
            this.numDecimals = numDecimals;

            StringBuilder sb = new StringBuilder();
            sb.append("x=[%.").append(numDecimals).append("f, ");
            sb.append("%.").append(numDecimals).append("f] ");
            sb.append("y=[%.").append(numDecimals).append("f, ");
            sb.append("%.").append(numDecimals).append("f]");
            numFormat = sb.toString();
        } finally {
            lock.unlock();
        }
    }

}
