package org.geotools.mbstyle.function;
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

import static org.geotools.filter.capability.FunctionNameImpl.parameter;

import java.util.Collection;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.capability.FunctionNameImpl;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.expression.Function;

/** Returns the size of a list or the length of a string */
public class MapBoxLengthFunction extends FunctionExpressionImpl {
    public FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
    public static FunctionName NAME =
            new FunctionNameImpl(
                    "mbLength",
                    parameter("object", Object.class),
                    parameter("fallback", Object.class));

    public MapBoxLengthFunction() {
        super(NAME);
    }

    @Override
    public Object evaluate(Object feature) {
        Object arg0;
        Function f = null;

        try { // attempt to get value and perform conversion
            arg0 = getExpression(0).evaluate(feature);
            if (arg0 instanceof Collection) {
                f = ff.function("listSize", getExpression(0));
            }
            if (arg0 instanceof String) {
                f = ff.function("strLength", getExpression(0));
            }
        } catch (Exception e) { // probably a type error
            throw new IllegalArgumentException(
                    "Filter Function problem for function \"mbLength\" argument #0 - expected type List");
        }
        if (f != null) {
            return f.evaluate(feature);
        } else {
            throw new IllegalArgumentException("Cannot evaluate args");
        }
    }
}
