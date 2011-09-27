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

import static org.geotools.filter.capability.FunctionNameImpl.parameter;

import java.awt.Color;

import org.geotools.filter.capability.FunctionNameImpl;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.expression.Expression;


/**
 * A function that returns the minimum of two arguments.
 *
 * @author James
 *
 *
 * @source $URL$
 * @version $Id$
 * @deprecated - use org.geotools.filter.function.math.MinFunction instead
 */
public class MinFunction extends FunctionExpressionImpl{
    
   //public static FunctionName NAME = new FunctionNameImpl("Min","number","number");
    public static FunctionName NAME = new FunctionNameImpl("Min",
            parameter("min", Double.class),
            parameter("number", Number.class),
            parameter("number", Number.class));
    /**
     * Creates a new instance of MinFunction
     */
    public MinFunction() {
        super(NAME);
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

        return new Double(Math.min(first, second));
    }

}
