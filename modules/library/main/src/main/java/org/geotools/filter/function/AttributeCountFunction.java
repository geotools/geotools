/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.function;

import java.util.Collection;
import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.capability.FunctionNameImpl;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.expression.Expression;

/**
 * Takes an AttributeExpression, and computes the node count for the attribute.
 *
 * @author Rini Angreani (CSIRO Mineral Resources)
 */
public class AttributeCountFunction extends FunctionExpressionImpl {

    public static FunctionName NAME = new FunctionNameImpl("attributeCount", "count");

    public AttributeCountFunction() {
        super(NAME);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.filter.Expression#getValue(org.geotools.feature.Feature)
     */
    public Object evaluate(Object feature) {
        Expression ae = (Expression) getParameters().get(0);
        Object value = ae.evaluate(feature);
        if (value == null) {
            return 0;
        } else if (value instanceof Collection) {
            return ((Collection<?>) value).size();
        } else {
            return 1;
        }
    }
}
