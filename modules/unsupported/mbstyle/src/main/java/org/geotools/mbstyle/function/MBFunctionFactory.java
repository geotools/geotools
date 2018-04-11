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
        functionList.add(MBFunction_equalTo.NAME);
        functionList.add(MBFunction_notEqualTo.NAME);
        functionList.add(MBFunction_all.NAME);
        functionList.add(MBFunction_any.NAME);
        functionList.add(MBFunction_case.NAME);
        functionList.add(MBFunction_coalesce.NAME);
        functionList.add(MBFunction_match.NAME);
        functionList.add(ListSizeFunction.NAME);
        functionList.add(MBFunction_length.NAME);
        functionList.add(MBFunction_at.NAME);
        functionList.add(MBFunction_get.NAME);
        functionList.add(MBFunction_has.NAME);
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
        if (MBFunction_equalTo.NAME.getFunctionName().equals(name)) {
            MBFunction_equalTo f = new MBFunction_equalTo();
            f.setParameters(args);
            f.setFallbackValue(fallback);
            return f;
        }
        if (MBFunction_notEqualTo.NAME.getFunctionName().equals(name)) {
            MBFunction_notEqualTo f = new MBFunction_notEqualTo();
            f.setParameters(args);
            f.setFallbackValue(fallback);
            return f;
        }
        if (MBFunction_all.NAME.getFunctionName().equals(name)) {
            MBFunction_all f = new MBFunction_all();
            f.setParameters(args);
            f.setFallbackValue(fallback);
            return f;
        }
        if (MBFunction_any.NAME.getFunctionName().equals(name)) {
            MBFunction_any f = new MBFunction_any();
            f.setParameters(args);
            f.setFallbackValue(fallback);
            return f;
        }
        if (MBFunction_case.NAME.getFunctionName().equals(name)) {
            MBFunction_case f = new MBFunction_case();
            f.setParameters(args);
            f.setFallbackValue(fallback);
            return f;
        }
        if (MBFunction_coalesce.NAME.getFunctionName().equals(name)) {
            MBFunction_coalesce f = new MBFunction_coalesce();
            f.setParameters(args);
            f.setFallbackValue(fallback);
            return f;
        }
        if (MBFunction_match.NAME.getFunctionName().equals(name)) {
            MBFunction_match f = new MBFunction_match();
            f.setParameters(args);
            f.setFallbackValue(fallback);
            return f;
        }
        if (ListSizeFunction.NAME.getFunctionName().equals(name)){
            ListSizeFunction f = new ListSizeFunction();
            f.setParameters(args);
            f.setFallbackValue(fallback);
            return f;
        }
        if (MBFunction_length.NAME.getFunctionName().equals(name)){
            MBFunction_length f = new MBFunction_length();
            f.setParameters(args);
            f.setFallbackValue(fallback);
            return f;
        }
        if (MBFunction_at.NAME.getFunctionName().equals(name)){
            MBFunction_at f = new MBFunction_at();
            f.setParameters(args);
            f.setFallbackValue(fallback);
            return f;
        }
        if (MBFunction_get.NAME.getFunctionName().equals(name)){
            MBFunction_get f = new MBFunction_get();
            f.setParameters(args);
            f.setFallbackValue(fallback);
            return f;
        }
        if (MBFunction_has.NAME.getFunctionName().equals(name)){
            MBFunction_has f = new MBFunction_has();
            f.setParameters(args);
            f.setFallbackValue(fallback);
            return f;
        }
        return null;
    }
}
