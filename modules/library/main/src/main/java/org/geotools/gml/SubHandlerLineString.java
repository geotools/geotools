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

import java.util.ArrayList;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;


/**
 * Creates a simple OGC LineString element.
 *
 * @author Ian Turton, CCG
 * @author Rob Hranac, Vision for New York
 *
 * @source $URL$
 * @version $Id$
 */
public class SubHandlerLineString extends SubHandler {
    /** List of coordinates for LineString. */
    private ArrayList coordinateList = new ArrayList();

    /**
     * Empty constructor.
     */
    public SubHandlerLineString() {
    }

    /**
     * Adds a coordinate to the LineString.
     *
     * @param coordinate Coordinate to add to LineString.
     */
    public void addCoordinate(Coordinate coordinate) {
        coordinateList.add(coordinate);
    }

    /**
     * Determine whether or not this LineString is ready to be created.
     *
     * @param message The geometry type.
     *
     * @return Ready for creation flag.
     */
    public boolean isComplete(String message) {
        if (coordinateList.size() > 1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Create the LineString.
     *
     * @param geometryFactory The geometry factory needed to do the build.
     *
     * @return JTS LineString geometry.
     */
    public Geometry create(GeometryFactory geometryFactory) {
        Coordinate[] coords = (Coordinate[]) coordinateList.toArray(new Coordinate[ coordinateList.size()]);
        LineString lineString = geometryFactory.createLineString(coords);
        lineString.setUserData( getSRS() );
        lineString.setSRID( getSRID() );
        return lineString;
    }
}
