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

import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;

import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;

/**
 * Turn camera using right mouse button drag (change yaw and pitch, pitch locked to avoid rolling to back,
 * there could be a maximum angle to avoid getting lost looking at the sky) (cursor disappears while mouse pressed,
 * relative movement is measured. Similar to first person view games). (A right mouse button click without drag will
 * typically show a context menu for the feature under the mouse).
 *
 * @author Hans Häggström
 */
public final class RotateGesture
        extends AbstractDragGesture
{

    //======================================================================
    // Private Fields

    private Quaternion myRotation = new Quaternion( 0, 0, 0, 1 );
    private Quaternion myDirection = new Quaternion( 0, 0, 0, 1 );

    //======================================================================
    // Private Constants

    private static final Vector3f Z_AXIS = new Vector3f( 0, 0, 1 );
    private static final float DEFAULT_ROTATION_SENSITIVITY = 0.01f;

    //======================================================================
    // Public Methods

    //----------------------------------------------------------------------
    // Constructors

    public RotateGesture()
    {
        super( DEFAULT_ROTATION_SENSITIVITY, MouseEvent.BUTTON1, InputEvent.BUTTON1_DOWN_MASK );
    }

    //======================================================================
    // Protected Methods

    protected void applyDragGesture( final Camera camera, final float deltaX, final float deltaY )
    {

        // Get quaternion from camera
        final Vector3f left = camera.getLeft();
        final Vector3f up = camera.getUp();
        final Vector3f forward = camera.getDirection();
        myDirection.fromAxes( left, up, forward );

        // TODO: Block rotation so that it is not possible to turn upside down

        // Apply rotation to around current position
        myRotation.fromAngleNormalAxis( deltaY, left );
        myRotation.mult( myDirection, myDirection );
        myRotation.fromAngleNormalAxis( -deltaX, Z_AXIS );
        myRotation.mult( myDirection, myDirection );

        // Apply new direction to camera
        camera.setAxes( myDirection );

        // TODO: Stabilize the left, up, and forward vectors so that they stay orthogonal despite rotation rounding errors.
    }

}
