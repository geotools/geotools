/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.postgis.filter;

import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.capability.FunctionNameImpl;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.expression.VolatileFunction;
import org.opengis.geometry.Geometry;

/**
 * NEAREST function implementation for Postgis <br>
 * Function name: pgNearest <br>
 * example:
 *
 * <pre>     pgNearest(POINT(16.36 48.205),30)=true </pre>
 *
 * @author Fernando Mino, Geosolutions
 */
public class FilterFunction_pgNearest extends FunctionExpressionImpl implements VolatileFunction {

    public static FunctionName NAME =
            new FunctionNameImpl(
                    "pgNearest",
                    Boolean.class,
                    // required parameters:
                    FunctionNameImpl.parameter("geometry", Geometry.class),
                    FunctionNameImpl.parameter("num_features", Integer.class));

    public FilterFunction_pgNearest() {
        super(NAME);
    }

    @Override
    public Object evaluate(Object feature) {
        throw new UnsupportedOperationException("Unsupported usage of Nearest operator");
    }
}
