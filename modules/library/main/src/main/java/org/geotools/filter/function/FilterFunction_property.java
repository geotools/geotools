package org.geotools.filter.function;

/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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

import static org.geotools.filter.capability.FunctionNameImpl.*;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.capability.FunctionNameImpl;
import org.geotools.util.Converters;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.expression.VolatileFunction;

/**
 * Extracts a property from a feature, taking the property name as a parameter
 * 
 * @author Andrea Aime - GeoSolutions
 * 
 * @source $URL$
 */
public class FilterFunction_property extends FunctionExpressionImpl implements VolatileFunction {

    FilterFactory2 FF = CommonFactoryFinder.getFilterFactory2(null);

    /**
     * Cache the last PropertyName used in a thead safe way
     */
    volatile PropertyName lastPropertyName;

    public FilterFunction_property() {
        super("property");
    }

    public int getArgCount() {
        return 1;
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

        PropertyName pn = lastPropertyName;
        if (pn != null && pn.getPropertyName().equals(name)) {
            return pn.evaluate(feature);
        } else {
            pn = FF.property(name);
            Object result = pn.evaluate(feature);
            lastPropertyName = pn;
            return result;
        }
    }
}
