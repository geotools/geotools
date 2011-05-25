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
 *
 *    Created on 28 July 2002, 16:03
 */
package org.geotools.filter;

import org.opengis.filter.expression.Expression;


/**
 * A function that returns the maximum of two arguments.
 *
 * @author James
 *
 * @source $URL$
 * @version $Id$
 * @deprecated - use org.geotools.filter.function.math.MinFunction instead
 */
public class MaxFunction extends FunctionExpressionImpl{
    /**
     * Creates a new instance of MinFunction
     */
    public MaxFunction() {
        super("Max");
    }

    /**
     * Returns a value for this expression.
     *
     * @param feature Specified feature to use when returning value.
     *
     * @return Value of the feature object.
     */
    public Object evaluate(Object feature) {
        org.opengis.filter.expression.Expression expA = (Expression) getParameters().get(0);
        org.opengis.filter.expression.Expression expB = (Expression) getParameters().get(1);
        
        double first = ((Number) expA.evaluate(feature)).doubleValue();
        double second = ((Number) expB.evaluate(feature)).doubleValue();

        return new Double(Math.max(first, second));
    }

    /**
     * Gets the number of arguments that are set.
     *
     * @return the number of args.
     */
    public int getArgCount() {
        return 2;
    }

}
