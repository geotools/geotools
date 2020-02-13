/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.mbstyle.sprite;

/**
 * Thrown by {@link SpriteGraphicFactory} in case of an exception while retrieving or parsing either
 * a spritesheet or a sprite index file.
 */
public class MBSpriteException extends RuntimeException {

    /** serialVersionUID */
    private static final long serialVersionUID = 1371164543614500159L;

    public MBSpriteException(String msg) {
        super(msg);
    }

    public MBSpriteException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
