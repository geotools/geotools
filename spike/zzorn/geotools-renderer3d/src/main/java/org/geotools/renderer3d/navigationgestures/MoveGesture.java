/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer3d.navigationgestures;

import com.jme.renderer.Camera;

import java.awt.event.MouseWheelEvent;

/**
 * A gesture for moving forward or back.
 * <p/>
 * Move camera with W,S,A,D or arrow keys or scroll wheel (forward and back and strafes to the sides,
 * acceleration is enabled (and a bit of inertia too for effect)).
 * With scroll wheel, a few scrolls gives a small thrust, many scrolls leaves the thrust on.
 * Scroll back to turn the thrust off again (and to thrust backwards).
 * <p/>
 * Also scale move amount by altitude.
 *
 * @author Hans Häggström
 */
public final class MoveGesture
        extends AbstractNavigationGesture
{

    //======================================================================
    // Private Constants

    private static final float DEFAULT_MOVE_SENSITIVITY = 10.0f;

    //======================================================================
    // Public Methods

    //----------------------------------------------------------------------
    // Constructors

    public MoveGesture()
    {
        super( DEFAULT_MOVE_SENSITIVITY );
    }

    //----------------------------------------------------------------------
    // MouseWheelListener Implementation

    public void mouseWheelMoved( final MouseWheelEvent e )
    {
        final Camera camera = getCamera();

        final float movementAmount = -e.getWheelRotation() * getSensitivity() * getAltitudeFactor();

        // TODO: Implement inertia, acceleration, locked movement, and keyboard input

        camera.getLocation().scaleAdd( movementAmount, camera.getDirection(), camera.getLocation() );
        camera.onFrameChange();
    }

}
