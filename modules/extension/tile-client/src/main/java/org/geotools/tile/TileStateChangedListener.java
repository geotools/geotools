/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015-2017, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2004-2010, Refractions Research Inc.
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
package org.geotools.tile;

/**
 * A listener for tile state change events.
 *
 * <p>See net.refractions.uidg.project.render.Tile. Taken from <a href=
 * "https://github.com/moovida/uDig/tree/master/plugins/net.refractions.udig.project/src/net/refractions/udig/project/render"
 * >uDig</a>.
 *
 * @author Emily Gouge (Refractions Research)
 * @since 12.0
 */
public interface TileStateChangedListener {
    /** Called when the screen state of a tile has changed. */
    void screenStateChanged(Tile tile);

    /** Called when the render state of a tile has changed. */
    void renderStateChanged(Tile tile);

    /** Called when the context state of a tile has changed. */
    void contextStateChanged(Tile tile);

    /** Called when the validation state of a tile has changed. */
    void validationStateChanged(Tile tile);
}
