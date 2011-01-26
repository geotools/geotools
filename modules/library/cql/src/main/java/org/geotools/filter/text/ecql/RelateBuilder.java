/*
 *    GeoTools - The Open Source Java GIS Tookit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.filter.text.ecql;

import org.geotools.filter.text.commons.BuildResultStack;
import org.geotools.filter.text.cql2.CQLException;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;

/**
 * Makes a relate filter function
 *
 * @author Mauricio Pazos (Axios Engineering)
 * @since 2.6
 */
final class RelateBuilder extends FunctionBuilder{


    public RelateBuilder(BuildResultStack resultStack,
            FilterFactory filterFactory) {

        super(resultStack, filterFactory);
    }

    @Override
    public Function build() throws CQLException {
        
        Expression[] args = buildParameters();
        
        Function relate = getFilterFactory().function("relate", args);         

        return relate;
    }

    private Expression[] buildParameters() throws CQLException {

        Expression[] args =new Expression[2];

        args[1] = getResultStack().popExpression();
        
        args[0] = getResultStack().popExpression();

        return args;
    }
    

}
