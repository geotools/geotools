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
 */
package org.geotools.filter;

import java.util.List;

import org.opengis.filter.expression.Literal;

/**
 * A placeholder class used to track a function the user requested
 * that is not supported by our java implementation.
 * <p>
 * This can be used to construct expressions that are to be executed
 * by another systems (say as SQL or as a WFS request).
 * 
 * @author Jody Garnett
 *
 *
 * @source $URL$
 */
public class FallbackFunction extends FunctionExpressionImpl {

    public FallbackFunction(String name, List params, Literal fallback) {
        super(name, fallback);
        this.setParameters(params);
    }
    public int getArgCount() {
        return 0;
    }
    @Override
    public Object evaluate(Object object) {
        return fallback.evaluate(object);
    }
    @SuppressWarnings("unchecked")
    @Override
    public Object evaluate(Object object, Class context) {
        return fallback.evaluate( object, context );
    }
}
