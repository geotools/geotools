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
