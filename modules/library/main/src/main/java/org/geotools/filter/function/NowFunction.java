/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2021, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.function;

import java.util.Date;
import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.capability.FunctionNameImpl;
import org.opengis.filter.capability.FunctionName;

/** Return the current time (milliseconds) as a Date */
public class NowFunction extends FunctionExpressionImpl {
    public static FunctionName NAME = new FunctionNameImpl("now", Date.class);

    public NowFunction() {
        super(NAME);
    }

    @Override
    public String toString() {
        return "NOW()";
    }

    @Override
    public Object evaluate(Object object) {
        return new Date();
    }
}
