/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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

import java.util.Collection;
import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.capability.FunctionNameImpl;
import org.opengis.filter.capability.FunctionName;

/**
 * Return the size of a list (or other collection).
 *
 * @author Niels Charlier
 */
public class SizeFunction extends FunctionExpressionImpl {

    public static FunctionName NAME =
            new FunctionNameImpl(
                    "size",
                    parameter("result", Integer.class, "Result", "Size of collection"),
                    parameter("source", Collection.class, "Source", "Collection"));

    public SizeFunction() {
        super(NAME);
    }

    @Override
    public Object evaluate(Object feature) {
        Collection<?> source = getExpression(0).evaluate(feature, Collection.class);
        if (source == null) {
            return null;
        }

        return source.size();
    }
}
