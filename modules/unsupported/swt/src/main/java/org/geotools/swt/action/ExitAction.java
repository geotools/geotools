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
package org.geotools.swt.action;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.window.ApplicationWindow;

/**
 * Action to exit the viewer.
 *
 * @author Andrea Antonello (www.hydrologis.com)
 */
public class ExitAction extends MapAction implements ISelectionChangedListener {

    private ApplicationWindow window;

    public ExitAction(ApplicationWindow window) {
        super("Exit", "Close the viewer.", null);
        this.window = window;
    }

    public void run() {
        window.close();
    }

    public void selectionChanged(SelectionChangedEvent arg0) {}
}
