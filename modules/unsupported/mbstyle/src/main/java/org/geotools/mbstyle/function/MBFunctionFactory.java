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
import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.FunctionFactory;
import org.geotools.filter.FunctionImpl;
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
        functionList.add(MapBoxAnchorFunction.NAME);
        functionList.add(FontAlternativesFunction.NAME);
        return Collections.unmodifiableList(functionList);
    }

    @Override
    public Function function(String name, List<Expression> args, Literal fallback) {
        return function(new NameImpl(name), args, fallback);
    }

    @Override
    public Function function(Name name, List<Expression> args, Literal fallback) {
        Function f = null;
        if (ZoomLevelFunction.NAME.getFunctionName().equals(name)) {
            f = new ZoomLevelFunction();
        } else if (ExponentialFunction.NAME.getFunctionName().equals(name)) {
            f = new ExponentialFunction();
        } else if (CSSFunction.NAME.getFunctionName().equals(name)) {
            f = new CSSFunction();
        } else if (DefaultIfNullFunction.NAME.getFunctionName().equals(name)) {
            f = new DefaultIfNullFunction();
        } else if (StringTransformFunction.NAME.getFunctionName().equals(name)) {
            f = new StringTransformFunction();
        } else if (ToRgb.NAME.getFunctionName().equals(name)) {
            f = new ToRgb();
        } else if (MapBoxEqualToFunction.NAME.getFunctionName().equals(name)) {
            f = new MapBoxEqualToFunction();
        } else if (MapBoxNotEqualToFunction.NAME.getFunctionName().equals(name)) {
            f = new MapBoxNotEqualToFunction();
        } else if (AllFunction.NAME.getFunctionName().equals(name)) {
            f = new AllFunction();
        } else if (AnyFunction.NAME.getFunctionName().equals(name)) {
            f = new AnyFunction();
        } else if (CaseFunction.NAME.getFunctionName().equals(name)) {
            f = new CaseFunction();
        } else if (CoalesceFunction.NAME.getFunctionName().equals(name)) {
            f = new CoalesceFunction();
        } else if (MatchFunction.NAME.getFunctionName().equals(name)) {
            f = new MatchFunction();
        } else if (ListSizeFunction.NAME.getFunctionName().equals(name)) {
            f = new ListSizeFunction();
        } else if (MapBoxLengthFunction.NAME.getFunctionName().equals(name)) {
            f = new MapBoxLengthFunction();
        } else if (AtFunction.NAME.getFunctionName().equals(name)) {
            f = new AtFunction();
        } else if (GetFunction.NAME.getFunctionName().equals(name)) {
            f = new GetFunction();
        } else if (HasFunction.NAME.getFunctionName().equals(name)) {
            f = new HasFunction();
        } else if (RemainderFunction.NAME.getFunctionName().equals(name)) {
            f = new RemainderFunction();
        } else if (MapBoxTypeOfFunction.NAME.getFunctionName().equals(name)) {
            f = new MapBoxTypeOfFunction();
        } else if (MapBoxTypeFunction.NAME.getFunctionName().equals(name)) {
            f = new MapBoxTypeFunction();
        } else if (ToBoolFunction.NAME.getFunctionName().equals(name)) {
            f = new ToBoolFunction();
        } else if (ToStringFunction.NAME.getFunctionName().equals(name)) {
            f = new ToStringFunction();
        } else if (ToNumberFunction.NAME.getFunctionName().equals(name)) {
            f = new ToNumberFunction();
        } else if (ToColorFunction.NAME.getFunctionName().equals(name)) {
            f = new ToColorFunction();
        } else if (MapBoxAnchorFunction.NAME.getFunctionName().equals(name)) {
            f = new MapBoxAnchorFunction();
        } else if (FontAlternativesFunction.NAME.getFunctionName().equals(name)) {
            f = new FontAlternativesFunction();
        } else if (MapBoxFontBaseNameFunction.NAME.getFunctionName().equals(name)) {
            f = new MapBoxFontBaseNameFunction();
        } else if (MapBoxFontStyleFunction.NAME.getFunctionName().equals(name)) {
            f = new MapBoxFontStyleFunction();
        } else if (MapBoxFontWeightFunction.NAME.getFunctionName().equals(name)) {
            f = new MapBoxFontWeightFunction();
        }

        if (f instanceof FunctionImpl) {
            FunctionImpl fi = (FunctionImpl) f;
            fi.setParameters(args);
            fi.setFallbackValue(fallback);
            return f;
        } else if (f instanceof FunctionExpressionImpl) {
            FunctionExpressionImpl fei = (FunctionExpressionImpl) f;
            fei.setParameters(args);
            fei.setFallbackValue(fallback);
            return f;
        }

        return null;
    }
}
