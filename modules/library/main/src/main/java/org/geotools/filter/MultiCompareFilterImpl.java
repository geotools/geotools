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
package org.geotools.filter;

import java.util.Collection;
import java.util.Collections;

import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;

/**
 * Support for Multi-valued properties when comparing
 * 
 * @author Niels Charlier, Curtin University of Technology
 * 
 */
public abstract class MultiCompareFilterImpl extends CompareFilterImpl {

    protected MultiCompareFilterImpl(FilterFactory factory, Expression e1, Expression e2) {
        super(factory, e1, e2);
    }

    protected MultiCompareFilterImpl(FilterFactory factory, Expression e1, Expression e2,
            boolean matchCase) {
        super(factory, e1, e2, matchCase);
    }

    public final boolean evaluate(Object feature) {
        final Object object1 = eval(expression1, feature);
        final Object object2 = eval(expression2, feature);
        
        if (!(object1 instanceof Collection) && !(object2 instanceof Collection)) {
            return evaluateInternal(object1, object2);
        }

        Collection<Object> leftValues = object1 instanceof Collection ? (Collection<Object>) object1
                : Collections.<Object>singletonList(object1);
        Collection<Object> rightValues = object2 instanceof Collection ? (Collection<Object>) object2
                : Collections.<Object>singletonList(object2);

        for (Object value1 : leftValues) {
            for (Object value2 : rightValues) {
                if (evaluateInternal(value1, value2)) {
                    return true;
                }
            }
        }

        return false;
    }

    public abstract boolean evaluateInternal(Object value1, Object value2);

}
