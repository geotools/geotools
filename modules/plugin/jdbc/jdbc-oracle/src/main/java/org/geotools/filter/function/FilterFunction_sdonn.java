/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2012, Open Source Geospatial Foundation (OSGeo)
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

import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.capability.FunctionNameImpl;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.expression.VolatileFunction;
import org.opengis.geometry.Geometry;

/**
 * Oracle function SDO_NN to identify the nearest neighbors for a geometry
 * 
 * @author Davide Savazzi - GeoSolutions
 */
public class FilterFunction_sdonn extends FunctionExpressionImpl implements VolatileFunction {

    public static FunctionName NAME = new FunctionNameImpl("sdo_nn", Boolean.class,
            // required parameters:
            FunctionNameImpl.parameter("geometry", Geometry.class), 
            FunctionNameImpl.parameter("sdo_num_res", Integer.class), 
            // optional parameters:
            FunctionNameImpl.parameter("cql_filter", String.class, 0, 1), 
            FunctionNameImpl.parameter("sdo_batch_size", Integer.class, 0, 1));

    public FilterFunction_sdonn() {
        super(NAME);
    }

    @Override
    public int getArgCount() {
        return 0;
    }

    @Override
    public Object evaluate(Object feature) {
        throw new UnsupportedOperationException("Unsupported usage of SDO_NN Oracle function");
    }
}