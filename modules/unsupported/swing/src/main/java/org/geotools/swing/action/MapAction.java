/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.swing.action;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;

import org.geotools.swing.JMapPane;


/**
 * Base class for map pane actions; just provides a common initializing method and
 * a reference to the map pane being serviced.
 * 
 * @author Michael Bedward
 * @since 2.6
 * @source $URL$
 * @version $Id$
 */
public abstract class MapAction extends AbstractAction {
    private static final long serialVersionUID = 2400755645451641127L;

    private JMapPane mapPane;

    /**
     * Called by sub-classes to set the map pane field and initialize
     * the control action properties
     *
     * @param mapPane the map pane that this action is working with
     * @param toolName short (single word) tool name; may be {@code null}
     * @param toolTip brief tool description for GUI tool tip
     * @param iconImage path to the icon to display on the control
     */
    protected void init(JMapPane mapPane, String toolName, String toolTip, String iconImage) {
        this.mapPane = mapPane;

        if (toolName != null) {
            this.putValue(Action.NAME, toolName);
        }

        this.putValue(Action.SHORT_DESCRIPTION, toolTip);

        if (iconImage != null) {
            this.putValue(Action.SMALL_ICON, new ImageIcon(MapAction.class.getResource(iconImage)));
        }
    }

    /**
     * Get the map pane that this Action is working with
     *
     * @return the map pane
     */
    public JMapPane getMapPane() {
        return mapPane;
    }
}
