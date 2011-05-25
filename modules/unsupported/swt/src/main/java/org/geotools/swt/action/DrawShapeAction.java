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

import java.awt.geom.AffineTransform;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.event.MapBoundsEvent;
import org.geotools.map.event.MapBoundsListener;
import org.geotools.swt.SwtMapPane;
import org.geotools.swt.tool.InfoTool;
import org.geotools.swt.utils.ImageCache;

/**
 * Action that activates the Info tool for the current {@link SwtMapPane map pane}.
 * 
 * @author Andrea Antonello (www.hydrologis.com)
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/trunk/modules/unsupported/swt/src/main/java/org/geotools/swt/action/DrawShapeAction.java $
 */
public class DrawShapeAction extends MapAction implements MapBoundsListener {

    public DrawShapeAction() {
        super("Drawshape@D", InfoTool.TOOL_TIP, ImageCache.getInstance().getImage(ImageCache.IMAGE_INFO));

    }

    private static boolean odd = true;
    private static boolean first = true;
    /**
     * Called when the associated control is activated. Leads to the
     * map pane's cursor tool being set to a PanTool object
     * 
     * @param ev the event (not used)
     */
    public void run() {
        if (first) {
            getMapPane().getMapContext().addMapBoundsListener(this);
            first = false;
        }

        Rectangle visibleRect = getMapPane().getVisibleRect();
        ReferencedEnvelope displayArea = getMapPane().getDisplayArea();
        drawShapes(visibleRect, displayArea, false);

        /*
         * to switch of drawing, simply do
         */
        // getMapPane().setOverlay(null, null, false);
        // getMapPane().redraw();
    }

    /**
     * Draws shapes on the map.
     * 
     * @param visibleRect the rectangle in teh screen space.
     * @param areaOfInterest the area of interest in world coordinates.
     */
    private void drawShapes( Rectangle visibleRect, ReferencedEnvelope areaOfInterest, boolean boundsChanged ) {
        Display display = Display.getDefault();

        /*
         * create an image with transparent color (this can be done better?)
         */
        Color white = display.getSystemColor(SWT.COLOR_WHITE);
        PaletteData palette = new PaletteData(new RGB[]{white.getRGB()});
        final ImageData sourceData = new ImageData(visibleRect.width, visibleRect.height, 1, palette);
        sourceData.transparentPixel = 0;

        // create the image to draw on
        Image img = new Image(display, sourceData);
        GC gc = new GC(img);
        gc.setAntialias(SWT.ON);

        // example lat/long coordinates to draw
        double[] worldCoords;
        if (odd) {
            worldCoords = new double[]{10.0, 40.0, 11.2, 43.3, 11.3, 45.2, 11.4, 46.5};
        } else {
            worldCoords = new double[]{11.0, 41.0, 12.2, 44.3, 11.3, 45.2, 11.4, 46.5};
        }
        odd = !odd;

        // get the world to screen transform
        double[] screenCoords = new double[8];
        AffineTransform worldToScreenTransform = getMapPane().getWorldToScreenTransform();
        // do the transform
        worldToScreenTransform.transform(worldCoords, 0, screenCoords, 0, worldCoords.length / 2);

        // draw lines
        gc.setForeground(display.getSystemColor(SWT.COLOR_MAGENTA));
        gc.setLineWidth(2);
        gc.drawLine((int) screenCoords[0], (int) screenCoords[1], (int) screenCoords[2], (int) screenCoords[3]);
        gc.drawLine((int) screenCoords[2], (int) screenCoords[3], (int) screenCoords[4], (int) screenCoords[5]);

        // draw dots
        int size = 10;
        gc.setBackground(display.getSystemColor(SWT.COLOR_RED));
        gc.fillOval((int) screenCoords[0] - size / 2, (int) screenCoords[1] - size / 2, size, size);
        gc.fillOval((int) screenCoords[2] - size / 2, (int) screenCoords[3] - size / 2, size, size);
        gc.fillOval((int) screenCoords[4] - size / 2, (int) screenCoords[5] - size / 2, size, size);
        gc.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
        gc.drawOval((int) screenCoords[0] - size / 2, (int) screenCoords[1] - size / 2, size, size);
        gc.drawOval((int) screenCoords[2] - size / 2, (int) screenCoords[3] - size / 2, size, size);
        gc.drawOval((int) screenCoords[4] - size / 2, (int) screenCoords[5] - size / 2, size, size);

        gc.setAntialias(SWT.OFF);
        gc.dispose();

        // overlay the image
        getMapPane().setOverlay(img, areaOfInterest, false, boundsChanged);
    }

    public void selectionChanged( IAction action, ISelection selection ) {
    }

    public void mapBoundsChanged( MapBoundsEvent event ) {
        /*
         * every time the bounds change (zoom, etc...), the drawing 
         * has to occurr again on the new bounds
         */
        ReferencedEnvelope newAreaOfInterest = event.getNewAreaOfInterest();
        Rectangle visibleRect = getMapPane().getVisibleRect();
        drawShapes(visibleRect, newAreaOfInterest, true);
    }
}
