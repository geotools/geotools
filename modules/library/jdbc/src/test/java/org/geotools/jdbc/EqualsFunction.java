/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.jdbc;

import org.geotools.filter.FunctionImpl;
import org.geotools.util.Converters;
import org.opengis.filter.expression.PropertyName;

/**
 * A test function used for jdbc tests.
 * 
 * @author Justin Deoliveira, OpenGeo
 *
 */
public class EqualsFunction extends FunctionImpl {

    public EqualsFunction() {
        setName("__equals");
    }
    
    @Override
    public Object evaluate(Object object) {
        PropertyName name = (PropertyName) getParameters().get(0);
        Object literal = getParameters().get(1).evaluate(null);
        
        Object o = name.evaluate(object);
        return o.equals(Converters.convert(literal, o.getClass()));
    }

}
