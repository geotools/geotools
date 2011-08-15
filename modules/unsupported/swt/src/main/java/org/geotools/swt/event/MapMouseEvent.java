/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.swt.event;

import java.awt.geom.AffineTransform;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.swt.SwtMapPane;

/**
 * A MouseEvent which contains methods to obtain coordinates in real world CoordinateSystem as well
 * as Screen Coordinates.
 * 
 * @author Michael Bedward (adapted from code by Cameron Shorter)
 * @author Andrea Antonello (www.hydrologis.com)
 * @since 2.6
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/trunk/modules/unsupported/swt/src/main/java/org/geotools/swt/event/MapMouseEvent.java $
 */
public final class MapMouseEvent {
    private static final long serialVersionUID = 3894658044662688321L;

    private DirectPosition2D geoCoords;

    private boolean isWheelEvent;

    private int wheelAmount;

    private final MouseEvent event;

    /**
     * Constructor. Calculates the map position of the mouse event.
     * 
     * @param pane the map pane sending this event.
     * @param event the mouse event.
     * @param isWheel if <code>true</code> then the event comes from the mouse wheel.
     */
    public MapMouseEvent( SwtMapPane pane, MouseEvent event, boolean isWheel ) {
        this.event = event;

        isWheelEvent = isWheel;
        if (!isWheel) {
            wheelAmount = 0;

            AffineTransform tr = pane.getScreenToWorldTransform();
            geoCoords = new DirectPosition2D(event.x, event.y);
            tr.transform(geoCoords, geoCoords);
            geoCoords.setCoordinateReferenceSystem(pane.getMapContent().getCoordinateReferenceSystem());
        } else {
            wheelAmount = 3;// event.getWheelRotation();
        }
    }

    /**
     * Indicates a mouse wheel event.
     * 
     * @return <code>true</code> if this a mouse wheel event
     */
    public boolean isWheelEvent() {
        return isWheelEvent;
    }

    /**
     * When isWheelEvent is true this indicate the amount of travel.
     * 
     * @return Amount of travel when isWheelEvent is true
     */
    public int getWheelAmount() {
        return wheelAmount;
    }

    /**
     * Getter for the mouse location?
     * 
     * @return the mouse location.
     */
    public Point getPoint() {
        return new Point(event.x, event.y);
    }

    /**
     * Get the position, in map (world) coordinates of this mouse event
     * 
     * @return a new DirectPosition2D object for the world coordinates
     */
    public DirectPosition2D getMapPosition() {
        return new DirectPosition2D(geoCoords.getCoordinateReferenceSystem(), geoCoords.x, geoCoords.y);
    }
}
