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

package org.geotools.swing.action;

import java.awt.event.ActionEvent;
import org.geotools.swing.MapPane;
import org.geotools.swing.locale.LocaleUtils;

/**
 * An action to de-select any active map cursor tool.
 *
 * @author Michael Bedward
 * @since 8.0
 * @version $Id$
 */
public class NoToolAction extends MapAction {
    /** Name for this tool */
    public static final String TOOL_NAME = LocaleUtils.getValue("CursorTool", "None");

    /** Tool tip text */
    public static final String TOOL_TIP = LocaleUtils.getValue("CursorTool", "NoneTooltip");

    /** Icon for the control */
    public static final String ICON_IMAGE = "/org/geotools/swing/icons/pointer.png";

    /**
     * Constructor. The associated control will be labelled with an icon.
     *
     * @param mapPane the map pane being serviced by this action
     */
    public NoToolAction(MapPane mapPane) {
        this(mapPane, false);
    }

    /**
     * Constructor. The associated control will be labelled with an icon and, optionally, the tool
     * name.
     *
     * @param mapPane the map pane being serviced by this action
     * @param showToolName set to true for the control to display the tool name
     */
    public NoToolAction(MapPane mapPane, boolean showToolName) {
        String toolName = showToolName ? TOOL_NAME : null;

        super.init(mapPane, toolName, TOOL_TIP, ICON_IMAGE);
    }

    /**
     * Called when the control is activated. Calls the map pane to reset the display
     *
     * @param ev the event (not used)
     */
    @Override
    public void actionPerformed(ActionEvent ev) {
        getMapPane().setCursorTool(null);
    }
}
