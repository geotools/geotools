/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.styling.css.util;

import org.geotools.filter.function.EnvFunction;
import org.geotools.filter.visitor.SimplifyingFilterVisitor;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;

/**
 * A filter visitor subclass that does not consider the env function already bound, and thus,
 * assumes it's still variable
 *
 * @author Andrea Aime - GeoSolutions
 */
public class UnboundSimplifyingFilterVisitor extends SimplifyingFilterVisitor {

    @Override
    protected boolean isVolatileFunction(Function function) {
        return function instanceof EnvFunction || super.isVolatileFunction(function);
    }

    @Override
    protected boolean isConstant(Expression ex) {
        if (ex instanceof EnvFunction) {
            return false;
        } else {
            return super.isConstant(ex);
        }
    }
}
