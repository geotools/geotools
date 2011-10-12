/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.v2_0.bindings;

import javax.xml.namespace.QName;

import org.geotools.filter.v2_0.FES;
import org.geotools.xml.Node;
import org.opengis.filter.BinaryComparisonOperator;
import org.opengis.filter.BinaryLogicOperator;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.spatial.BinarySpatialOperator;
import org.opengis.filter.temporal.BinaryTemporalOperator;

/**
 * Utility class for FES bindings.
 * 
 * @author Justin Deoliveira, OpenGeo
 *
 */
public class FESParseEncodeUtil {

    /**
     * Parses the two operands for a binary temporal filter.
     */
    static Expression[] temporal(Node node, FilterFactory factory) {
        PropertyName name = (PropertyName) node.getChildValue(PropertyName.class);
        Object other = null;
        for (Object o : node.getChildValues(Object.class)) {
            if (o == name) {
                continue;
            }
            
            other = o;
            break;
        }
        
        if (other == null) {
           throw new IllegalArgumentException("Temporal filter did not specify two operands");
        }
        
        Expression expr = null;
        if (other instanceof Expression) {
            expr = (Expression) other;
        }
        else {
            expr = factory.literal(other);
        }
        
        return new Expression[]{name, expr};
    }
    
    static Expression getProperty(BinaryTemporalOperator op, QName name) {
        return getProperty(op.getExpression1(), op.getExpression2(), name);
    }
    static Expression getProperty(BinarySpatialOperator op, QName name) {
        return getProperty(op.getExpression1(), op.getExpression2(), name);
    }
    
    static Expression getProperty(Expression e1, Expression e2, QName name) {
        if (FES.ValueReference.equals(name)) {
            if (e1 instanceof PropertyName) {
                return e1;
            }
            else if (e2 instanceof PropertyName) {
                return e2;
            }
        }
        if (FES.expression.equals(name)) {
            if (e1 instanceof PropertyName) {
                return e2;
            }
            else if (e2 instanceof PropertyName) {
                return e1;
            }
        }
        
        return null;
    }
}
