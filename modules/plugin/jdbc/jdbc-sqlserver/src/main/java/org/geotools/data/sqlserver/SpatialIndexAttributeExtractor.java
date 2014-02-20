/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.sqlserver;

import java.util.HashMap;
import java.util.Map;

import org.geotools.filter.visitor.DefaultFilterVisitor;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.spatial.BBOX;
import org.opengis.filter.spatial.Beyond;
import org.opengis.filter.spatial.BinarySpatialOperator;
import org.opengis.filter.spatial.Contains;
import org.opengis.filter.spatial.Crosses;
import org.opengis.filter.spatial.DWithin;
import org.opengis.filter.spatial.Disjoint;
import org.opengis.filter.spatial.Equals;
import org.opengis.filter.spatial.Intersects;
import org.opengis.filter.spatial.Overlaps;
import org.opengis.filter.spatial.Touches;
import org.opengis.filter.spatial.Within;

/**
 * Extracts property names involved in spatial filters
 * 
 * @author Andrea Aime - GeoSolutions
 * 
 */
class SpatialIndexAttributeExtractor extends DefaultFilterVisitor {

    Map<String, Integer> spatialProperties = new HashMap<String, Integer>();

    Map<String, Integer> getSpatialProperties() {
        return spatialProperties;
    }

    public Object visit(BBOX filter, Object extraData) {
        return visitBinarySpatialOperator((BinarySpatialOperator) filter, extraData);
    }

    public Object visit(Beyond filter, Object extraData) {
        return visitBinarySpatialOperator((BinarySpatialOperator) filter, extraData);
    }

    public Object visit(Contains filter, Object extraData) {
        return visitBinarySpatialOperator((BinarySpatialOperator) filter, extraData);
    }

    public Object visit(Crosses filter, Object extraData) {
        return visitBinarySpatialOperator((BinarySpatialOperator) filter, extraData);
    }

    public Object visit(Disjoint filter, Object extraData) {
        // nope, this one cannot be used with spatial indexes
        // return visitBinarySpatialOperator((BinarySpatialOperator) filter, extraData);
        return null;
    }

    public Object visit(DWithin filter, Object extraData) {
        return visitBinarySpatialOperator((BinarySpatialOperator) filter, extraData);
    }

    public Object visit(Equals filter, Object extraData) {
        return visitBinarySpatialOperator((BinarySpatialOperator) filter, extraData);
    }

    public Object visit(Intersects filter, Object extraData) {
        return visitBinarySpatialOperator((BinarySpatialOperator) filter, extraData);
    }

    public Object visit(Overlaps filter, Object extraData) {
        return visitBinarySpatialOperator((BinarySpatialOperator) filter, extraData);
    }

    public Object visit(Touches filter, Object extraData) {
        return visitBinarySpatialOperator((BinarySpatialOperator) filter, extraData);
    }

    public Object visit(Within filter, Object extraData) {
        return visitBinarySpatialOperator((BinarySpatialOperator) filter, extraData);
    }

    protected Object visitBinarySpatialOperator(BinarySpatialOperator filter, Object extraData) {
        Expression ex1 = filter.getExpression1();
        Expression ex2 = filter.getExpression2();
        PropertyName pn = null;
        if (ex1 instanceof PropertyName && ex2 instanceof Literal) {
            pn = (PropertyName) ex1;
        } else if (ex1 instanceof Literal && ex2 instanceof PropertyName) {
            pn = (PropertyName) ex2;
        }
        if (pn != null) {
            String name = pn.getPropertyName();
            if(spatialProperties.containsKey(name)) {
                Integer count = spatialProperties.get(name);
                spatialProperties.put(name, count + 1);
            } else {
                spatialProperties.put(name, 1);
            }
        }

        return null;
    }
}
