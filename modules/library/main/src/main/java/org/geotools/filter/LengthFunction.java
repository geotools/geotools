/*
 *    GeoTools - The Open Source Java GIS Toolkit
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
package org.geotools.filter;

import static org.geotools.filter.capability.FunctionNameImpl.parameter;

import org.geotools.filter.capability.FunctionNameImpl;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.expression.Expression;

/**
 * Takes an AttributeExpression, and computes the length of the data for the attribute.
 *
 * @author dzwiers
 */
public class LengthFunction extends FunctionExpressionImpl {
    // public static FunctionName NAME = new FunctionNameImpl("length","string");
    public static FunctionName NAME =
            new FunctionNameImpl(
                    "length",
                    parameter("length", Integer.class),
                    parameter("string", String.class));

    public LengthFunction() {
        super(NAME);
    }

    /* (non-Javadoc)
     * @see org.geotools.filter.Expression#getValue(org.geotools.feature.Feature)
     */
    public Object evaluate(Object feature) {
        Expression ae = (Expression) getParameters().get(0);
        String value = (String) ae.evaluate(feature, String.class);
        return Integer.valueOf(value == null ? 0 : value.length());
    }
}
