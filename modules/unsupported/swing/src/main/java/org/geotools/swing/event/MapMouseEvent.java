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
package org.geotools.swing.event;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;

import org.geotools.geometry.DirectPosition2D;
import org.geotools.swing.JMapPane;

/**
 * A MouseEvent which contains methods to obtain coordinates in real world CoordinateSystem as well
 * as Screen Coordinates.
 * 
 * @author Michael Bedward (adapted from code by Cameron Shorter)
 * @since 2.6
 *
 * @source $URL$
 *         http://svn.osgeo.org/geotools/trunk/modules/unsupported/swing/src/main/java/org/geotools
 *         /swing/event/MapMouseEvent.java $
 * @version $Id$
 */
public final class MapMouseEvent extends MouseEvent {
    private static final long serialVersionUID = 3894658044662688321L;

    private DirectPosition2D geoCoords;

    private boolean isWheelEvent;

    private int wheelAmount;

    /**
     * Constructor. Calculates the map position of the mouse event.
     * 
     * @param pane
     *            the map pane sending this event
     * @param event
     *            the mouse event
     */
    public MapMouseEvent(JMapPane pane, MouseEvent event) {
        super(pane, event.getID(), event.getWhen(), event.getModifiers(), event
                .getX(), event.getY(), event.getClickCount(), event.isPopupTrigger(), event
                .getButton());

        isWheelEvent = false;
        wheelAmount = 0;

        AffineTransform tr = pane.getScreenToWorldTransform();
        geoCoords = new DirectPosition2D(event.getX(), event.getY());
        tr.transform(geoCoords, geoCoords);
        geoCoords.setCoordinateReferenceSystem(pane.getMapContext().getCoordinateReferenceSystem());
    }

    /**
     * Constructor for mouse wheel events.
     * <p>
     * @todo do we need to calculate map position for a mouse wheel event ?
     * 
     * @param pane the map pane
     * @param event the mouse wheel event
     */
    public MapMouseEvent(JMapPane pane, MouseWheelEvent event) {        
        super(pane, event.getID(), event.getWhen(), event.getModifiers(), event
                .getX(), event.getY(), event.getClickCount(), event.isPopupTrigger());

        isWheelEvent = true;
        wheelAmount = event.getWheelRotation();
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
     * Get the position, in map (world) coordinates of this mouse event
     * 
     * @return a new DirectPosition2D object for the world coordinates
     */
    public DirectPosition2D getMapPosition() {
        return new DirectPosition2D(
                geoCoords.getCoordinateReferenceSystem(),
                geoCoords.x, geoCoords.y);
    }
}
