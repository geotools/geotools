/*
 * JGrass - Free Open Source Java GIS http://www.jgrass.org 
 * (C) HydroloGIS - www.hydrologis.com 
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
 * @source $URL: http://svn.osgeo.org/geotools/trunk/modules/unsupported/swt/src/main/java/org/geotools/swt/action/OpenShapefileAction.java $
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
