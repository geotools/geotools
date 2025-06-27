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

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.util.Arrays;
import javax.swing.JLabel;
import org.geotools.api.geometry.Bounds;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.geometry.Position2D;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.swing.MapPane;
import org.geotools.swing.event.MapMouseAdapter;
import org.geotools.swing.event.MapMouseEvent;
import org.geotools.swing.event.MapPaneAdapter;
import org.geotools.swing.event.MapPaneEvent;
import org.geotools.swing.locale.LocaleUtils;

/**
 * A status bar item that displays the world coordinates of the mouse cursor position.
 *
 * @see JMapStatusBar
 * @author Michael Bedward
 * @since 8.0
 * @version $Id$
 */
public class JCoordsStatusBarItem extends StatusBarItem {
    private static final String COMPONENT_NAME = LocaleUtils.getValue("StatusBar", "CoordsItemName");

    private static final String TOOL_TIP = LocaleUtils.getValue("StatusBar", "CoordsTooltip");
    private static final int DEFAULT_NUM_INTEGER_DIGITS = 3;

    private static final String NO_COORDS = LocaleUtils.getValue("StatusBar", "CoordsNone");

    private final JLabel label;

    private int intLen;
    private int decLen;
    private String numFormat;

    /**
     * Creates a new item to display cursor position for the given map pane.
     *
     * @param mapPane the map pane
     */
    public JCoordsStatusBarItem(MapPane mapPane) {
        super(COMPONENT_NAME);

        if (mapPane == null) {
            throw new IllegalArgumentException("mapPane must not be null");
        }

        label = new JLabel();
        label.setFont(JMapStatusBar.DEFAULT_FONT);
        add(label);

        setToolTipText(TOOL_TIP);

        decLen = JMapStatusBar.DEFAULT_NUM_DECIMAL_DIGITS;

        setFormat(mapPane.getDisplayArea());

        mapPane.addMouseListener(new MapMouseAdapter() {
            @Override
            public void onMouseEntered(MapMouseEvent ev) {
                displayCoords(ev.getWorldPos());
            }

            @Override
            public void onMouseExited(MapMouseEvent ev) {
                displayNoCursor();
            }

            @Override
            public void onMouseMoved(MapMouseEvent ev) {
                displayCoords(ev.getWorldPos());
            }
        });

        mapPane.addMapPaneListener(new MapPaneAdapter() {
            @Override
            public void onDisplayAreaChanged(MapPaneEvent ev) {
                setFormat((ReferencedEnvelope) ev.getData());
            }
        });

        displayNoCursor();
    }

    /**
     * Sets te number of digits to display after the decimal place.
     *
     * @param numDecimals number of digits after decimal place
     */
    @Override
    public void setNumDecimals(int numDecimals) {
        decLen = numDecimals;
        setLabelSizeAndFormat();
    }

    /**
     * Displays coordinates of the given position.
     *
     * @param p world position
     */
    private void displayCoords(Position2D p) {
        label.setText(String.format(numFormat, p.getX(), p.getY()));
        ensureMinLabelWidth();
    }

    /** Sets a message to indicate that the cursor is out of the map pane. */
    private void displayNoCursor() {
        label.setText(NO_COORDS);
    }

    /**
     * Sets the minimum width of the coordinate display label and the format string used to print values. The map extent
     * is used to estimate the number of digits required.
     *
     * @param env map extent
     */
    private void setFormat(ReferencedEnvelope env) {
        if (env == null || env.isEmpty()) {
            intLen = DEFAULT_NUM_INTEGER_DIGITS;
        } else {
            setIntegerLen(env);
        }
        setLabelSizeAndFormat();
    }

    /** Sets the minimum width of the coordinate display label and the format string used to print values. */
    private void setLabelSizeAndFormat() {
        int minLabelWidth = getStringWidth();
        Dimension labelSize = label.getSize();
        if (labelSize.width < minLabelWidth) {
            label.setMinimumSize(new Dimension(minLabelWidth, labelSize.height));
            revalidate();
        }

        StringBuilder sb = new StringBuilder();
        sb.append("%").append(intLen).append(".").append(decLen).append("f, ");
        sb.append("%").append(intLen).append(".").append(decLen).append("f");
        numFormat = sb.toString();
    }

    /**
     * Estimates the maximum display width of the coordinate string based on current integer and fractional part
     * lengths.
     *
     * @return maximum display width
     */
    private int getStringWidth() {
        FontMetrics fm = label.getFontMetrics(label.getFont());
        char[] c = new char[intLen + decLen + 1];
        Arrays.fill(c, '0');
        String s = String.valueOf(c);
        s = s + ", " + s;
        return fm.stringWidth(s);
    }

    /**
     * Examines the map extent and tries to determine the number of digits that will be needed in the display. If a
     * coordinate reference system with valid extent defined, it is used to determine coordinate limits; otherwise the
     * extent of the envelope is used directly. If all else fails, a default number of digits is set.
     *
     * @param env the map extent (may be {@code null})
     */
    private void setIntegerLen(Bounds env) {
        int len = -1;
        if (env != null) {
            // Try to get a valid extent for the CRS and use this to
            // determine num coordinate digits
            CoordinateReferenceSystem crs = env.getCoordinateReferenceSystem();
            if (crs != null) {
                Bounds validExtent = CRS.getEnvelope(crs);
                if (validExtent != null) {
                    len = getMaxIntegerLen(validExtent);
                }
            }

            if (len < 0) {
                // Use map extent directly
                len = getMaxIntegerLen(env);
            }

        } else {
            // Nothing to go on: use an arbitrary length
            len = DEFAULT_NUM_INTEGER_DIGITS;
        }

        intLen = len;
    }

    /**
     * Gets the maximum number of digits in the integer part of envelope corner coordinates.
     *
     * @param env the envelope
     * @return maximum number of digits
     */
    private int getMaxIntegerLen(Bounds env) {
        int len = integerPartLen(env.getMinimum(0));
        len = Math.max(len, integerPartLen(env.getMinimum(1)));
        len = Math.max(len, integerPartLen(env.getMaximum(0)));
        len = Math.max(len, integerPartLen(env.getMaximum(1)));

        // Add 1 to allow for negative sign
        return len + 1;
    }

    /**
     * Gets the length of the integer part of a double value.
     *
     * @param x the value
     * @return number of digits in the integer part
     */
    private int integerPartLen(double x) {
        return 1 + (int) Math.log10(Math.abs(x));
    }

    /**
     * Checks the current label width against its minimum width and, if the current width is larger, adjusts the minimum
     * to prevent the label growing and shrinking as the cursor is moved.
     */
    private void ensureMinLabelWidth() {
        Dimension minDim = label.getMinimumSize();
        Dimension curDim = label.getSize();

        if (curDim.width > minDim.width) {
            label.setMinimumSize(new Dimension(curDim.width, minDim.height));
        }
    }
}
