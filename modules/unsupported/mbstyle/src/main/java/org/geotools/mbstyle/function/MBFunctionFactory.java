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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.geotools.feature.NameImpl;
import org.geotools.filter.FunctionFactory;
import org.opengis.feature.type.Name;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;

/**
 * Custom functions to support the use of MBStyle, {@link ZoomLevelFunction},
 * {@link ExponentialFunction}.
 */
public class MBFunctionFactory implements FunctionFactory {

    @Override
    public List<FunctionName> getFunctionNames() {
        List<FunctionName> functionList = new ArrayList<>();
        functionList.add(ZoomLevelFunction.NAME);
        functionList.add(ExponentialFunction.NAME);
        functionList.add(CSSFunction.NAME);
        functionList.add(DefaultIfNullFunction.NAME);
        functionList.add(ToRgb.NAME);
        return Collections.unmodifiableList(functionList);
    }

    @Override
    public Function function(String name, List<Expression> args, Literal fallback) {
        return function(new NameImpl(name), args, fallback);
    }

    @Override
    public Function function(Name name, List<Expression> args, Literal fallback) {
        if (ZoomLevelFunction.NAME.getFunctionName().equals(name)) {
            ZoomLevelFunction f = new ZoomLevelFunction();
            f.setParameters(args);
            f.setFallbackValue(fallback);
            return f;
        }
        if (ExponentialFunction.NAME.getFunctionName().equals(name)) {
            ExponentialFunction f = new ExponentialFunction();
            f.setParameters(args);
            f.setFallbackValue(fallback);

            return f;
        }
        if (CSSFunction.NAME.getFunctionName().equals(name)) {
            CSSFunction f = new CSSFunction();
            f.setParameters(args);
            f.setFallbackValue(fallback);

            return f;
        }
        if (DefaultIfNullFunction.NAME.getFunctionName().equals(name)) {
            DefaultIfNullFunction f = new DefaultIfNullFunction();
            f.setParameters(args);
            f.setFallbackValue(fallback);
            return f;
        }
        if (StringTransformFunction.NAME.getFunctionName().equals(name)) {
            StringTransformFunction f = new StringTransformFunction();
            f.setParameters(args);
            f.setFallbackValue(fallback);
            return f;
        }
        if (ToRgb.NAME.getFunctionName().equals(name)){
            ToRgb f = new ToRgb();
            f.setParameters(args);
            f.setFallbackValue(fallback);
            return f;
        }
        return null;
    }
}
