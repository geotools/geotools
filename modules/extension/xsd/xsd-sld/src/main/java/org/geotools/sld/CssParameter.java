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
package org.geotools.sld;

import java.util.ArrayList;
import java.util.List;

import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;


/**
 * A Cascading Style Sheet parameter.
 * <p>
 * This class is internal to the sld binding project. It should be replaced
 * with a geotools styling model object if one becomes available.
 * </p>
 * @author Justin Deoliveira, The Open Planning Project
 *
 *
 *
 * @source $URL$
 */
public class CssParameter {
    String name;
    Expression expression;

    public CssParameter(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    
    public Expression getExpression() {
        return expression;
    }
    
    public void setExpression(Expression expression) {
        this.expression = expression;
    }

}
