/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.geotools.grid;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import org.geotools.geometry.jts.ReferencedEnvelope;

/**
 *
 * @author michael
 */
public interface Element {

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
