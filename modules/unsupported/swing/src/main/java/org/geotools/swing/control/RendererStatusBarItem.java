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
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import org.geotools.swing.MapPane;
import org.geotools.swing.event.MapPaneAdapter;
import org.geotools.swing.event.MapPaneEvent;

/**
 * A status bar item that displays an animated icon to indicate renderer activity.
 *
 * @author Michael Bedward
 * @since 8.0
 * @source $URL$
 * @version $Id$
 */
public class RendererStatusBarItem extends StatusBarItem {
    private static final String TOOL_TIP = "Renderer activity";
    private static final String BUSY_IMAGE = "icons/busy.gif";
    private static final String IDLE_IMAGE = "icons/idle.gif";

    private final ImageIcon busyIcon;
    private final ImageIcon idleIcon;

    /*
     * Creates a new item associated with teh given map.
     */
    public RendererStatusBarItem(MapPane mapPane) {
        super("Busy", false);

        busyIcon = new ImageIcon(RendererStatusBarItem.class.getResource(BUSY_IMAGE));
        idleIcon = new ImageIcon(RendererStatusBarItem.class.getResource(IDLE_IMAGE));

        final JLabel renderLabel = new JLabel();
        renderLabel.setIcon(idleIcon);
        renderLabel.setToolTipText(TOOL_TIP);

        Insets insets = getInsets();
        renderLabel.setMinimumSize(new Dimension(
                busyIcon.getIconWidth() + insets.left + insets.right,
                busyIcon.getIconHeight() + insets.top + insets.bottom));

        add(renderLabel);

        mapPane.addMapPaneListener(new MapPaneAdapter() {
            @Override
            public void onRenderingStarted(MapPaneEvent ev) {
                renderLabel.setIcon(busyIcon);
            }

            @Override
            public void onRenderingStopped(MapPaneEvent ev) {
                renderLabel.setIcon(idleIcon);
            }
        });
    }

}
