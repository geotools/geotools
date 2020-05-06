/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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

import static org.geotools.filter.capability.FunctionNameImpl.parameter;

import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.capability.FunctionNameImpl;
import org.opengis.filter.capability.FunctionName;

/**
 * Array creator function. Evaluates all the arguments in order and creates an array from them.
 *
 * @author mauro.bartolomeoli at geo-solutions.it
 */
public class ArrayFunction extends FunctionExpressionImpl {

    public static FunctionName NAME =
            new FunctionNameImpl("array", parameter("values", Object.class, 1, -1));

    public ArrayFunction() {
        super(NAME);
    }

    public Object evaluate(Object obj) {
        Object[] array = new Object[getParameters().size()];
        for (int count = 0; count < getParameters().size(); count++) {
            array[count] = getExpression(count).evaluate(obj);
        }
        return array;
    }
}
