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

import java.util.List;
import java.util.Map;
import org.geotools.api.coverage.grid.GridCoverageReader;
import org.geotools.api.coverage.grid.GridGeometry;
import org.geotools.api.data.Parameter;
import org.geotools.api.data.Query;
import org.geotools.api.feature.type.Name;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.Literal;
import org.geotools.api.parameter.GeneralParameterValue;
import org.geotools.filter.function.RenderingTransformation;
import org.geotools.process.Process;
import org.geotools.process.ProcessException;
import org.geotools.process.RenderingProcess;

/**
 * A function wrapping a {@link Process} with a single output. All inputs to the function are supposed to evaluate to
 * Map<String, Object> where the key is the name of an argument and the value is the argument value
 *
 * @author Andrea Aime - GeoSolutions
 * @author Daniele Romagnoli - GeoSolutions
 */
class RenderingProcessFunction extends ProcessFunction implements RenderingTransformation {

    public RenderingProcessFunction(
            Name processName,
            List<Expression> inputExpressions,
            Map<String, Parameter<?>> parameters,
            RenderingProcess process,
            Literal fallbackValue) {
        super(processName, inputExpressions, parameters, process, fallbackValue);
    }

    @Override
    public Query invertQuery(Query targetQuery, GridGeometry gridGeometry) {
        RenderingProcess process = (RenderingProcess) this.process;
        // evaluate input expressions
        // at this point do not have an object to evaluate them against
        Map<String, Object> inputs = evaluateInputs(null);
        try {
            return process.invertQuery(inputs, targetQuery, gridGeometry);
        } catch (ProcessException e) {
            throw new RuntimeException("Failed to invert the query, error is: " + e.getMessage(), e);
        }
    }

    @Override
    public GridGeometry invertGridGeometry(Query targetQuery, GridGeometry targetGridGeometry) {
        RenderingProcess process = (RenderingProcess) this.process;
        // evaluate input expressions
        // at this point do not have an object to evaluate them against
        Map<String, Object> inputs = evaluateInputs(null);
        try {
            return process.invertGridGeometry(inputs, targetQuery, targetGridGeometry);
        } catch (ProcessException e) {
            throw new RuntimeException("Failed to invert the grid geometry, error is: " + e.getMessage(), e);
        }
    }

    @Override
    public GeneralParameterValue[] customizeReadParams(GridCoverageReader reader, GeneralParameterValue... params) {
        RenderingProcess process = (RenderingProcess) this.process;
        // evaluate input expressions
        // at this point do not have an object to evaluate them against
        Map<String, Object> inputs = evaluateInputs(null);
        try {
            return process.customizeReadParams(inputs, reader, params);
        } catch (ProcessException e) {
            throw new RuntimeException("Failed to customize the reader parameters, error is: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean clipOnRenderingArea() {
        RenderingProcess process = (RenderingProcess) this.process;
        // evaluate input expressions
        // at this point do not have an object to evaluate them against
        Map<String, Object> inputs = evaluateInputs(null);
        try {
            return process.clipOnRenderingArea(inputs);
        } catch (ProcessException e) {
            throw new RuntimeException("Failed to customize the reader parameters, error is: " + e.getMessage(), e);
        }
    }
}
