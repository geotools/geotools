/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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

import static org.geotools.filter.capability.FunctionNameImpl.parameter;

import java.util.Map;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.capability.FunctionNameImpl;
import org.geotools.util.Converters;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.expression.VolatileFunction;

/** Extracts a value from a map given the mapname.key identifier as a parameter */
public class FilterFunction_mapGet extends FunctionExpressionImpl implements VolatileFunction {

    FilterFactory2 FF = CommonFactoryFinder.getFilterFactory2();

    public static FunctionName NAME =
            new FunctionNameImpl(
                    "mapGet",
                    parameter("value", Object.class),
                    parameter("mapWithKey", String.class));

    public FilterFunction_mapGet() {
        super(NAME);
    }

    @Override
    public Object evaluate(Object object, Class context) {
        Object result = evaluate(object);
        if (result == null) {
            return null;
        } else {
            return Converters.convert(result, context);
        }
    }

    public Object evaluate(Object feature) {
        String name = getExpression(0).evaluate(feature, String.class);

        if (name == null) {
            return null;
        }

        String[] split = name.split("\\.");
        PropertyName pn = FF.property(split[0]);
        Object result = pn.evaluate(feature);
        if (result instanceof Map) {
            Map map = (Map) result;
            String key = split[1];
            if (map.containsKey(key)) {
                return map.get(key);
            }
        }
        return null;
    }
}
