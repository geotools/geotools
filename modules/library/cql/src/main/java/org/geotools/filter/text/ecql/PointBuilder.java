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

import org.geotools.filter.text.commons.BuildResultStack;
import org.geotools.filter.text.commons.Result;
import org.geotools.filter.text.cql2.CQLException;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;



/**
 * Builds a point using the coordinates of stack that maintain the coordinates made
 * in the parsing process.
 *
 * @author Mauricio Pazos (Axios Engineering)
 * @since 2.6
 */
final class PointBuilder extends GeometryBuilder {

    
    public PointBuilder( String stmt, BuildResultStack resultStack) {
        super (stmt, resultStack);
    }

    /**
     * Builds a Point geometry
     */
    public Geometry build() throws CQLException {
        Result result = getResultStack().popResult();
        org.geotools.filter.text.commons.IToken token = result.getToken();
        try {
            Coordinate coordinate = (Coordinate) result.getBuilt();

            Point point = getGeometryFactory().createPoint(coordinate);

            return point;
            
        } catch (ClassCastException e) {
            throw new CQLException(e.getMessage(), token, getStatemet());
        }
    }

}
