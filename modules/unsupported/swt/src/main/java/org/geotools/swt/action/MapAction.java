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

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.geotools.swt.SwtMapPane;

/**
 * Base class for map pane actions.
 * 
 * @author Andrea Antonello (www.hydrologis.com)
 * @since 2.7
 *
 *
 *
 * @source $URL$
 */
public abstract class MapAction extends Action {

    protected SwtMapPane mapPane;

    public MapAction( String toolName, String toolTip, Image image ) {
        if (toolName != null) {
            setText(toolName);
        }
        if (toolTip != null) {
            setToolTipText(toolTip);
        }
        if (image != null) {
            setImageDescriptor(ImageDescriptor.createFromImage(image));
        }
    }

    public abstract void run();

    /**
     * Set the right {@link SwtMapPane map pane} to the action.
     * 
     * @param mapPane the map pane to use.
     */
    public void setMapPane( SwtMapPane mapPane ) {
        this.mapPane = mapPane;
    }

    /**
     * Getter for the current {@link SwtMapPane map pane}.
     * 
     * @return the current map pane.
     */
    public SwtMapPane getMapPane() {
        return mapPane;
    }
}
