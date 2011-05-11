/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.grid;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import org.geotools.geometry.jts.ReferencedEnvelope;

/**
 * The base interface for vector grid elements.
 * 
 * @author mbedward
 * @since 8.0
 * @source $URL: http://svn.osgeo.org/geotools/trunk/modules/unsupported/grid/src/main/java/org/geotools/grid/Grids.java $
 * @version $Id: Grids.java 37149 2011-05-10 11:47:02Z mbedward $
 */
public interface GridElement {

    /**
     * Gets the bounds of this grid element.
     *
     * @return the bounding rectangle
     */
    ReferencedEnvelope getBounds();

    /**
     * Gets the vertices of this grid element.
     *
     * @return the vertices
     */
    Coordinate[] getVertices();

    /**
     * Creates a new {@code Geometry} from this grid element.
     * 
     * @return a new {@code Geometry}
     */
    Geometry toGeometry();

    /**
     * Creates a new, densified {@code Geometry} from this grid element.
     *
     * @param maxSpacing the maximum distance between adjacent vertices
     *
     * @return a new {@code Geometry}
     *
     * @throws IllegalArgumentException if maxSpacing is {@code <=} 0
     */
    Geometry toDenseGeometry(double maxSpacing);
}
