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

import java.io.File;
import java.io.IOException;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.map.FeatureLayer;
import org.geotools.map.MapContent;
import org.geotools.styling.Style;
import org.geotools.swt.control.JFileDataStoreChooser;
import org.geotools.swt.utils.ImageCache;
import org.geotools.swt.utils.Utils;

/**
 * Action to open shapefile.
 * 
 * @author Andrea Antonello (www.hydrologis.com)
 *
 *
 *
 * @source $URL$
 */
public class OpenShapefileAction extends MapAction implements ISelectionChangedListener {

    public OpenShapefileAction() {
        super("Open Shapefile", "Load a shapefile into the viewer.", ImageCache.getInstance().getImage(ImageCache.OPEN));
    }

    public void run() {
        Display display = Display.getCurrent();
        Shell shell = new Shell(display);
        File openFile = JFileDataStoreChooser.showOpenFile(new String[]{"*.shp"}, shell); //$NON-NLS-1$

        try {
            if (openFile != null && openFile.exists()) {
                MapContent mapContent = mapPane.getMapContent();
                FileDataStore store = FileDataStoreFinder.getDataStore(openFile);
                SimpleFeatureSource featureSource = store.getFeatureSource();
                Style style = Utils.createStyle(openFile, featureSource);
                FeatureLayer featureLayer = new FeatureLayer(featureSource, style);
                mapContent.addLayer(featureLayer);
                mapPane.redraw();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void selectionChanged( SelectionChangedEvent arg0 ) {

    }

}
