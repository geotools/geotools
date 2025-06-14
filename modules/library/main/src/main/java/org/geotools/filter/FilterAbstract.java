/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.geotools.api.feature.Property;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.feature.NameImpl;

/**
 * Abstract implementation for Filter.
 *
 * @author Jody Garnett
 */
public abstract class FilterAbstract implements org.geotools.api.filter.Filter {

    protected FilterAbstract() {}

    /**
     * Subclass should overrride.
     *
     * <p>Default value is false
     */
    /*force subclass to implement
    public boolean evaluate(Object object) {
    	return false;
    }
           */

    /** Straight call throught to: evaulate( feature ) */
    public boolean accepts(SimpleFeature feature) {
        return evaluate(feature);
    }

    /** Unpacks a value from an attribute container */
    private Object unpack(Object value) {

        if (value instanceof org.geotools.api.feature.ComplexAttribute attribute) {
            Property simpleContent = attribute.getProperty(new NameImpl("simpleContent"));
            if (simpleContent == null) {
                return null;
            } else {
                return simpleContent.getValue();
            }
        }

        if (value instanceof org.geotools.api.feature.Attribute attribute) {
            return attribute.getValue();
        }

        return value;
    }

    /**
     * Helper method for subclasses to reduce null checks and automatically unpack values from attributes and
     * collections
     *
     * @return value or null
     */
    @SuppressWarnings("unchecked")
    protected Object eval(org.geotools.api.filter.expression.Expression expression, Object object) {
        if (expression == null) return null;
        Object value = expression.evaluate(object);

        if (value instanceof Collection) {
            // unpack all elements
            List<Object> list = new ArrayList<>();
            for (Object member : (Collection<Object>) value) {
                list.add(unpack(member));
            }
            return list;
        }

        return unpack(value);
    }
    /**
     * Helper method for subclasses to reduce null checks
     *
     * @return value or null
     */
    protected Object eval(org.geotools.api.filter.expression.Expression expression, Object object, Class<?> context) {
        if (expression == null) return null;
        return expression.evaluate(object, context);
    }
}
