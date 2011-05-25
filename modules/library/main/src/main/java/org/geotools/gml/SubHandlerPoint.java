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
package org.geotools.gml;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;


/**
 * Creates an OGC simple point.
 *
 * @author Ian Turton, CCG
 * @author Rob Hranac, Vision for New York
 *
 * @source $URL$
 * @version $Id$
 */
public class SubHandlerPoint extends SubHandler {
    /** The coordinate of the point. */
    Coordinate coordinate = null;

    /**
     * Creates a new instance of GMLPointHandler.
     */
    public SubHandlerPoint() {
    }

    /**
     * Sets the coordinate for the point.
     *
     * @param coordinate Coordinate.
     */
    public void addCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    /**
     * Determines whether or not this Point is ready to be created.
     *
     * @param message GML element that prompted this query.
     *
     * @return Ready for creation flag.
     */
    public boolean isComplete(String message) {
        if (this.coordinate != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Generates the point.
     *
     * @param geometryFactory Geometry factory to be used to create the point.
     *
     * @return Created Point.
     */
    public Geometry create(GeometryFactory geometryFactory) {
        Point point = geometryFactory.createPoint(coordinate);
        point.setUserData( getSRS() );
        point.setSRID( getSRID() );
        return point;
    }
}
