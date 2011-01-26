/*
 *    GeoTools - The Open Source Java GIS Tookit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.filter.text.ecql;

import java.util.Stack;

import org.geotools.filter.text.commons.BuildResultStack;
import org.geotools.filter.text.cql2.CQLException;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;

/**
 * Builds a LineString
 *
 * @author Mauricio Pazos (Axios Engineering)
 * @since 2.6
 */
final class LineStringBuilder extends GeometryBuilder {

    /**
     * @param statement
     * @param resultStack
     */
    public LineStringBuilder(String statement, BuildResultStack resultStack) {
        super(statement, resultStack);

    }

    /* (non-Javadoc)
     * @see org.geotools.filter.text.txt.GeometryBuilder#build()
     */
    @Override
    public Geometry build(final int pointNode) throws CQLException {
        // Retrieve the linestirng points
        Stack<Coordinate> pointStack = popCoordinatesOf(pointNode);
        // now pointStack has the coordinate in the correct order
        // the next code creates the coordinate array used to create
        // the lineString
        Coordinate[] coordinates = asCoordinate(pointStack);
        LineString line= getGeometryFactory().createLineString(coordinates);

        return line;
    }

}
