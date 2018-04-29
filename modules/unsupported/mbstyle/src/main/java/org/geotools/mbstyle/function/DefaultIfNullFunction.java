/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.mbstyle.function;

import java.util.List;
import java.util.logging.Logger;
import org.geotools.data.Parameter;
import org.geotools.filter.FunctionImpl;
import org.geotools.filter.capability.FunctionNameImpl;
import org.geotools.text.Text;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.expression.Expression;

/**
 * Function that takes an input value, and a default value. If the input value is null (or
 * evaluating it raises an exception), returns the default value; otherwise, returns the input
 * value.
 */
public class DefaultIfNullFunction extends FunctionImpl {

    public static final FunctionName NAME;

    private static final Logger LOGGER = Logger.getLogger(DefaultIfNullFunction.class.getName());

    static {
        Parameter<Object> result = new Parameter<Object>("result", Object.class, 1, 1);
        Parameter<Object> input = new Parameter<Object>("input", Object.class, 1, 1);
        Parameter<Object> fallback =
                new Parameter<Object>(
                        "DefaultIfNull",
                        Object.class,
                        Text.text("DefaultIfNull"),
                        Text.text("The value to return if the input is null"),
                        true,
                        0,
                        1,
                        1.0,
                        null);
        NAME = new FunctionNameImpl("DefaultIfNull", result, input, fallback);
    }

    public DefaultIfNullFunction() {
        this.functionName = NAME;
    }

    @Override
    public <T> T evaluate(Object object, Class<T> context) {
        List<Expression> parameters = getParameters();

        Expression input = parameters.get(0);
        Expression fallback = parameters.get(1);

        T fallbackEvaluated = fallback.evaluate(object, context);
        T inputEvaluated;
        try {
            inputEvaluated = input.evaluate(object, context);
        } catch (Exception e) {
            inputEvaluated = null;
            LOGGER.warning(
                    "Exception evaluating expression, falling back to default value. Exception was: "
                            + e.getClass().getSimpleName()
                            + " (message: "
                            + e.getMessage()
                            + ")");
        }

        if (inputEvaluated != null) {
            return inputEvaluated;
        } else {
            return fallbackEvaluated;
        }
    }
}
