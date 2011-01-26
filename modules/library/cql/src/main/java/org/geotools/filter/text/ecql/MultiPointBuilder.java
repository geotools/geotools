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

import java.util.List;

import org.geotools.filter.text.commons.BuildResultStack;
import org.geotools.filter.text.cql2.CQLException;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.Point;

/**
 * Builds a {@link MultiPoint} using the points made in previous steps of parsing processS
 *
 * @author Mauricio Pazos (Axios Engineering)
 * @since 2.6
 */
final class MultiPointBuilder extends GeometryBuilder {

    /**
     * @param statement
     * @param resultStack
     */
    public MultiPointBuilder(String statement, BuildResultStack resultStack) {
        super(statement, resultStack);

    }

    /**
     * Builds a {@link MultiPoint} using the point nodes presents in the stack of result
     */
    @Override
    public Geometry build(int pointNode) throws CQLException {

        List<Geometry> pointList = popGeometry(pointNode);
        
        int pointListSize = pointList.size();
        Point[] arrayOfPoint = pointList.toArray(new Point[pointListSize]) ;
        
        MultiPoint multiPoint= getGeometryFactory().createMultiPoint(arrayOfPoint);

        return multiPoint;
        
    }
    
    

}
