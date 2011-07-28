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

import javax.swing.JLabel;

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.swing.MapPane;
import org.geotools.swing.event.MapPaneAdapter;
import org.geotools.swing.event.MapPaneEvent;

/**
 * A status bar item that displays the map pane's world bounds.
 *
 * @see JMapStatusBar
 *
 * @author Michael Bedward
 * @since 8.0
 * @source $URL$
 * @version $Id$
 */
public class ExtentStatusBarItem extends StatusBarItem {
    private static final ReferencedEnvelope EMPTY_ENV = new ReferencedEnvelope();

    private final JLabel label;

    private int decLen;
    private String numFormat;
    private ReferencedEnvelope lastExtent;

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

        lastExtent = EMPTY_ENV;
        decLen = JMapStatusBar.DEFAULT_NUM_DECIMAL_DIGITS;
        setLabelFormat();
        
        label = new JLabel();
        label.setFont(JMapStatusBar.DEFAULT_FONT);
        add(label);

        mapPane.addMapPaneListener(new MapPaneAdapter() {
            @Override
            public void onDisplayAreaChanged(MapPaneEvent ev) {
                displayExtent(ev.getSource().getDisplayArea(), true);
            }
        });
    }

    /**
     * Displays extent in world coordinates.
     * 
     * @param extent the map pane extent
     * @param cache whether to cache a copy of {@code extent}
     */
    private void displayExtent(ReferencedEnvelope extent, boolean cache) {
        if (extent == null || extent.isEmpty()) {
            label.setText("Undefined extent");
            lastExtent = EMPTY_ENV;

        } else {
            label.setText(String.format(numFormat,
                    extent.getMinX(), extent.getMaxX(),
                    extent.getMinY(), extent.getMaxY()));

            if (cache) {
                lastExtent = new ReferencedEnvelope(extent);
            }
        }
    }

    /**
     * Sets te number of digits to display after the decimal place.
     *
     * @param numDecimals number of digits after decimal place
     */
    @Override
    public void setNumDecimals(int numDecimals) {
        this.decLen = numDecimals;
        setLabelFormat();
        displayExtent(lastExtent, false);
    }

    /**
     * Private helper for {@linkplain #setPrecision(int)} that can be called
     * safely from the constructor.
     */
    private void setLabelFormat() {
        StringBuilder sb = new StringBuilder();
        sb.append("x=[%.").append(decLen).append("f, ");
        sb.append("%.").append(decLen).append("f] ");
        sb.append("y=[%.").append(decLen).append("f, ");
        sb.append("%.").append(decLen).append("f]");
        
        numFormat = sb.toString();
    }

}
