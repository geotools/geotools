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
package org.geotools.filter.function.color;

import static org.geotools.filter.capability.FunctionNameImpl.parameter;

import java.awt.Color;
import java.util.Iterator;
import java.util.List;
import org.geotools.filter.FunctionImpl;
import org.geotools.filter.capability.FunctionNameImpl;
import org.opengis.filter.capability.FunctionName;

/**
 * Base for lesscss.org HSL color manipulation functions
 *
 * @author Andrea Aime - GeoSolutions
 */
public abstract class AbstractHSLFunction extends FunctionImpl {

    enum Method {
        absolute,
        relative
    };

    public static FunctionName NAME =
            new FunctionNameImpl(
                    "abstractHSL",
                    parameter("result", Color.class),
                    parameter("color", Color.class),
                    parameter("amount", Float.class),
                    parameter("method", Method.class, 0, 1));

    public AbstractHSLFunction(String name) {
        this.functionName = new FunctionNameImpl(name, NAME.getReturn(), NAME.getArguments());
    }

    @Override
    public Object evaluate(Object object) {
        Color source = (Color) getParameterValue(object, 0);
        float amount = (Float) getParameterValue(object, 1);
        Method method = (Method) getParameterValue(object, 2, Method.absolute);

        HSLColor hsl = new HSLColor(source);
        if (method == Method.absolute) {
            adjustAbsolute(amount, hsl);
        } else {
            adjstRelative(amount, hsl);
        }

        return hsl.toRGB();
    }

    protected abstract void adjstRelative(float amount, HSLColor hsl);

    protected abstract void adjustAbsolute(float amount, HSLColor hsl);

    /**
     * Creates a String representation of this Function with the function name and the arguments.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getName());
        sb.append("(");
        List<org.opengis.filter.expression.Expression> params = getParameters();
        if (params != null) {
            org.opengis.filter.expression.Expression exp;
            for (Iterator<org.opengis.filter.expression.Expression> it = params.iterator();
                    it.hasNext(); ) {
                exp = it.next();
                sb.append("[");
                sb.append(exp);
                sb.append("]");
                if (it.hasNext()) {
                    sb.append(", ");
                }
            }
        }
        sb.append(")");
        return sb.toString();
    }
}
