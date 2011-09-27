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
package org.geotools.swt.action;

import org.geotools.swt.SwtMapPane;
import org.geotools.swt.tool.InfoTool;
import org.geotools.swt.utils.ImageCache;

/**
 * Action that activates the Info tool for the current {@link SwtMapPane map pane}.
 * 
 * @author Andrea Antonello (www.hydrologis.com)
 *
 *
 *
 * @source $URL$
 */
public class InfoAction extends MapAction {

    public InfoAction() {
        super(InfoTool.TOOL_NAME + "@I", InfoTool.TOOL_TIP, ImageCache.getInstance().getImage(ImageCache.IMAGE_INFO));
    }

    /**
     * Called when the associated control is activated. Leads to the
     * map pane's cursor tool being set to a PanTool object
     * 
     * @param ev the event (not used)
     */
    public void run() {
        getMapPane().setCursorTool(new InfoTool());
    }

}
