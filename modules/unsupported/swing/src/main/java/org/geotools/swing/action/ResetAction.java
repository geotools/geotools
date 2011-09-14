/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008-2011, Open Source Geospatial Foundation (OSGeo)
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

import org.geotools.swing.locale.LocaleUtils;
import org.geotools.swing.MapPane;


/**
 * An action to reset a map pane display to its full extent.
 * 
 * @author Michael Bedward
 * @since 2.6
 * @source $URL$
 * @version $Id$
 */
public class ResetAction extends MapAction {
    /** Name for this tool */
    public static final String TOOL_NAME = LocaleUtils.getValue("CursorTool", "Reset");
    
    /** Tool tip text */
    public static final String TOOL_TIP = LocaleUtils.getValue("CursorTool", "ResetTooltip");
    
    /** Icon for the control */
    public static final String ICON_IMAGE = "/org/geotools/swing/icons/mActionZoomFullExtent.png";
    
    /**
     * Constructor. The associated control will be labelled with an icon.
     * 
     * @param mapPane the map pane being serviced by this action
     */
    public ResetAction(MapPane mapPane) {
        this(mapPane, false);
    }

    /**
     * Constructor. The associated control will be labelled with an icon and,
     * optionally, the tool name.
     * 
     * @param mapPane the map pane being serviced by this action
     * @param showToolName set to true for the control to display the tool name
     */
    public ResetAction(MapPane mapPane, boolean showToolName) {
        String toolName = showToolName ? TOOL_NAME : null;
        
        String iconImagePath = null;
        super.init(mapPane, toolName, TOOL_TIP, ICON_IMAGE);
    }
    
    /**
     * Called when the control is activated. Calls the map pane to reset the 
     * display 
     *
     * @param ev the event (not used)
     */
    @Override
    public void actionPerformed(ActionEvent ev) {
        getMapPane().reset();
    }

}
