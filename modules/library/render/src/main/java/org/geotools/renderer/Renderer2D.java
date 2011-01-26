/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer;

// J2SE dependencies
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;


/**
 * Renderer draws a map on behalf on <code>MapPane</code>.  It determines what features to draw,
 * bounding box, size, and style from the {@linkplain org.geotools.map.Context context}.
 *
 * @author Cameron Shorter
 * @source $URL$
 * @version $Id$
 *
 * @task REVISIT Renderer2D should extend Renderer once Renderer has been
 *       cleaned up.
 */
public interface Renderer2D {
    /**
     * Render features based on the {@link org.geotools.map.LayerList},
     * bounding box and {@link org.geotools.styling.Style} specified in 
     * the {@linkplain org.geotools.map.Context context}.
     *
     * @param graphics The graphics handler to draw to.
     * @param paintArea The bounds of the output area in output units (usually pixels).
     *        The upper left corner is (0,0) in most cases. However, a different value
     *        is allowed if some widget area must be preserved, for example a margin on
     *        the left and top size for painting a graduation.
     * @param transform A transform which converts "World coordinates" to output
     *        coordinates.
     *        This transform will be concatenated to the <code>graphics</code> transform (as of
     *     <code>graphics.{@link Graphics2D#transform(AffineTransform) transform}(transform)</code>)
     *        before the rendering take place.
     */
    public void paint(Graphics2D graphics, Rectangle paintArea, AffineTransform transform);
}
