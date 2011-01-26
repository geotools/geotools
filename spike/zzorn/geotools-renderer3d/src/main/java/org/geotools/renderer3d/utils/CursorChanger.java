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
package org.geotools.renderer3d.utils;

import java.awt.Cursor;

/**
 * Allows setting the mouse cursor for some visual component from several different sources.
 * <p/>
 * Keeps track of who set the cursor to what shape, and resets to previous states when someone doesn't need to show
 * their state anymore.
 *
 * @author Hans Häggström
 */
public interface CursorChanger
{
    /**
     * Sets the current cursor state.   Overrides earlier states.
     *
     * @param cursorSetter some object that represents the instance that is setting the cursor to an image.
     *                     Used to allow resetting the change.  Should not be null.
     * @param cursor       the cursor to use, or null to use an empty cursor.
     */
    void setCursor( Object cursorSetter, Cursor cursor );

    /**
     * Removes the cursor icon set by the specified cursorSetter, and restores the cursor to the icon used before.
     *
     * @param cursorSetter the instance that set the cursor to some state.  Should not be null.
     */
    void resetCursor( Object cursorSetter );

}
