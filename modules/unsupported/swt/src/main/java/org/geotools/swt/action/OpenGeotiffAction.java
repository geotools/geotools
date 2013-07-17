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

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.map.GridReaderLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.SLD;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactoryImpl;
import org.geotools.swt.control.JFileImageChooser;
import org.geotools.swt.utils.ImageCache;

/**
 * Action to open geotiff files.
 * 
 * @author Andrea Antonello (www.hydrologis.com)
 *
 *
 *
 * @source $URL$
 */
public class OpenGeotiffAction extends MapAction implements ISelectionChangedListener {

    public OpenGeotiffAction() {
        super("Open Image", "Load an image file into the viewer.", ImageCache.getInstance().getImage(ImageCache.OPEN));
    }

    public void run() {
        Display display = Display.getCurrent();
        Shell shell = new Shell(display);
        File openFile = JFileImageChooser.showOpenFile(shell);

        if (openFile != null && openFile.exists()) {
            AbstractGridFormat format = GridFormatFinder.findFormat(openFile);
            AbstractGridCoverage2DReader tiffReader = format.getReader(openFile);
            StyleFactoryImpl sf = new StyleFactoryImpl();
            RasterSymbolizer symbolizer = sf.getDefaultRasterSymbolizer();
            Style defaultStyle = SLD.wrapSymbolizers(symbolizer);

            MapContent mapContent = mapPane.getMapContent();
            Layer layer = new GridReaderLayer(tiffReader, defaultStyle);
            layer.setTitle(openFile.getName());
            mapContent.addLayer(layer);
            mapPane.redraw();
        }
    }

    public void selectionChanged( SelectionChangedEvent arg0 ) {

    }

}
