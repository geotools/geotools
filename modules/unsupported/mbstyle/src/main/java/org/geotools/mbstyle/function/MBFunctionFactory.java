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
 * Custom functions to support the use of MBStyle, {@link ZoomLevelFunction}, {@link
 * ExponentialFunction}.
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
        functionList.add(MapBoxEqualToFunction.NAME);
        functionList.add(MapBoxNotEqualToFunction.NAME);
        functionList.add(AllFunction.NAME);
        functionList.add(AnyFunction.NAME);
        functionList.add(CaseFunction.NAME);
        functionList.add(CoalesceFunction.NAME);
        functionList.add(MatchFunction.NAME);
        functionList.add(ListSizeFunction.NAME);
        functionList.add(MapBoxLengthFunction.NAME);
        functionList.add(AtFunction.NAME);
        functionList.add(GetFunction.NAME);
        functionList.add(HasFunction.NAME);
        functionList.add(RemainderFunction.NAME);
        functionList.add(MapBoxTypeOfFunction.NAME);
        functionList.add(MapBoxTypeFunction.NAME);
        functionList.add(ToBoolFunction.NAME);
        functionList.add(ToStringFunction.NAME);
        functionList.add(ToNumberFunction.NAME);
        functionList.add(ToColorFunction.NAME);
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
        if (ToRgb.NAME.getFunctionName().equals(name)) {
            ToRgb f = new ToRgb();
            f.setParameters(args);
            f.setFallbackValue(fallback);
            return f;
        }
        if (MapBoxEqualToFunction.NAME.getFunctionName().equals(name)) {
            MapBoxEqualToFunction f = new MapBoxEqualToFunction();
            f.setParameters(args);
            f.setFallbackValue(fallback);
            return f;
        }
        if (MapBoxNotEqualToFunction.NAME.getFunctionName().equals(name)) {
            MapBoxNotEqualToFunction f = new MapBoxNotEqualToFunction();
            f.setParameters(args);
            f.setFallbackValue(fallback);
            return f;
        }
        if (AllFunction.NAME.getFunctionName().equals(name)) {
            AllFunction f = new AllFunction();
            f.setParameters(args);
            f.setFallbackValue(fallback);
            return f;
        }
        if (AnyFunction.NAME.getFunctionName().equals(name)) {
            AnyFunction f = new AnyFunction();
            f.setParameters(args);
            f.setFallbackValue(fallback);
            return f;
        }
        if (CaseFunction.NAME.getFunctionName().equals(name)) {
            CaseFunction f = new CaseFunction();
            f.setParameters(args);
            f.setFallbackValue(fallback);
            return f;
        }
        if (CoalesceFunction.NAME.getFunctionName().equals(name)) {
            CoalesceFunction f = new CoalesceFunction();
            f.setParameters(args);
            f.setFallbackValue(fallback);
            return f;
        }
        if (MatchFunction.NAME.getFunctionName().equals(name)) {
            MatchFunction f = new MatchFunction();
            f.setParameters(args);
            f.setFallbackValue(fallback);
            return f;
        }
        if (ListSizeFunction.NAME.getFunctionName().equals(name)) {
            ListSizeFunction f = new ListSizeFunction();
            f.setParameters(args);
            f.setFallbackValue(fallback);
            return f;
        }
        if (MapBoxLengthFunction.NAME.getFunctionName().equals(name)) {
            MapBoxLengthFunction f = new MapBoxLengthFunction();
            f.setParameters(args);
            f.setFallbackValue(fallback);
            return f;
        }
        if (AtFunction.NAME.getFunctionName().equals(name)) {
            AtFunction f = new AtFunction();
            f.setParameters(args);
            f.setFallbackValue(fallback);
            return f;
        }
        if (GetFunction.NAME.getFunctionName().equals(name)) {
            GetFunction f = new GetFunction();
            f.setParameters(args);
            f.setFallbackValue(fallback);
            return f;
        }
        if (HasFunction.NAME.getFunctionName().equals(name)) {
            HasFunction f = new HasFunction();
            f.setParameters(args);
            f.setFallbackValue(fallback);
            return f;
        }
        if (RemainderFunction.NAME.getFunctionName().equals(name)) {
            RemainderFunction f = new RemainderFunction();
            f.setParameters(args);
            f.setFallbackValue(fallback);
            return f;
        }
        if (MapBoxTypeOfFunction.NAME.getFunctionName().equals(name)) {
            MapBoxTypeOfFunction f = new MapBoxTypeOfFunction();
            f.setParameters(args);
            f.setFallbackValue(fallback);
            return f;
        }
        if (MapBoxTypeFunction.NAME.getFunctionName().equals(name)) {
            MapBoxTypeFunction f = new MapBoxTypeFunction();
            f.setParameters(args);
            f.setFallbackValue(fallback);
            return f;
        }
        if (ToBoolFunction.NAME.getFunctionName().equals(name)) {
            ToBoolFunction f = new ToBoolFunction();
            f.setParameters(args);
            f.setFallbackValue(fallback);
            return f;
        }
        if (ToStringFunction.NAME.getFunctionName().equals(name)) {
            ToStringFunction f = new ToStringFunction();
            f.setParameters(args);
            f.setFallbackValue(fallback);
            return f;
        }
        if (ToNumberFunction.NAME.getFunctionName().equals(name)) {
            ToNumberFunction f = new ToNumberFunction();
            f.setParameters(args);
            f.setFallbackValue(fallback);
            return f;
        }
        if (ToColorFunction.NAME.getFunctionName().equals(name)) {
            ToColorFunction f = new ToColorFunction();
            f.setParameters(args);
            f.setFallbackValue(fallback);
            return f;
        }
        return null;
    }
}
