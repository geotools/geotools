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
 */
package org.geotools.filter.function.math;

import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.capability.FunctionNameImpl;
import org.opengis.filter.capability.FunctionName;

/**
 * Allow access to the value of Math.PI as an expression
 *
 * @author Jody Garnett
 * @since 2.2, 2.5
 */
public class PiFunction extends FunctionExpressionImpl {
    public static FunctionName NAME = new FunctionNameImpl("pi", Double.class);

    public PiFunction() {
        super(NAME);
    }

    public String toString() {
        return "PI()";
    }

    @Override
    public Object evaluate(Object object) {
        return new Double(Math.PI);
    }
}
