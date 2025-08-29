/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
 *
 */
package org.geotools.filter.function;

import org.geotools.api.feature.Attribute;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.filter.capability.FunctionName;
import org.geotools.api.filter.expression.VolatileFunction;
import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.capability.FunctionNameImpl;

/**
 * Allow access to the value of Feature.getID() as an expression
 *
 * @author Jody Garnett
 * @since 2.2, 2.5
 */
public class IDFunction extends FunctionExpressionImpl implements VolatileFunction {

    public static FunctionName NAME = new FunctionNameImpl("id", String.class);

    public IDFunction() {
        super(NAME);
    }

    @Override
    public String toString() {
        return "ID()";
    }

    @Override
    public Object evaluate(Object obj) {
        if (obj instanceof SimpleFeature feature) {
            return feature.getID();
        }
        if (obj instanceof Attribute attribute) {
            return attribute.getIdentifier().getID();
        }
        return ""; // no ID
    }
}
