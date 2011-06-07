/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.process.function;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geotools.data.Parameter;
import org.geotools.data.Query;
import org.geotools.filter.function.RenderingTransformation;
import org.geotools.process.Process;
import org.geotools.process.ProcessException;
import org.geotools.process.RenderingProcess;
import org.opengis.coverage.grid.GridGeometry;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Literal;

/**
 * A function wrapping a {@link Process} with a single output. All inputs to the function are
 * supposed to evaluate to Map<String, Object> where the key is the name of an argument and the
 * value is the argument value
 * 
 * @author Andrea Aime - GeoSolutions
 * @author Daniele Romagnoli - GeoSolutions
 */
class RenderingProcessFunction extends ProcessFunction implements RenderingTransformation {

    public RenderingProcessFunction(String name, List<Expression> inputExpressions,
            Map<String, Parameter<?>> parameters, RenderingProcess process, Literal fallbackValue) {
        super(name, inputExpressions, parameters, process, fallbackValue);
    }

    public Query invertQuery(Query targetQuery, GridGeometry gridGeometry) {
        RenderingProcess process = (RenderingProcess) this.process;
        Map<String, Object> params = new HashMap<String,Object>();
        params.putAll(parameters);
        try {
            return process.invertQuery(params, targetQuery, gridGeometry);
        } catch (ProcessException e) {
            throw new RuntimeException("Failed to invert the query, error is: "
                    + e.getMessage(), e);
        }
    }

    public GridGeometry invertGridGeometry(Query targetQuery, GridGeometry targetGridGeometry) {
        RenderingProcess process = (RenderingProcess) this.process;
        Map<String, Object> params = new HashMap<String,Object>();
        params.putAll(parameters);
        try {
            return process.invertGridGeometry(params, targetQuery, targetGridGeometry);
        } catch (ProcessException e) {
            throw new RuntimeException("Failed to invert the grid geometry, error is: "
                    + e.getMessage(), e);
        }
    }

}
