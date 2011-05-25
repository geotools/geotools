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
package org.geotools.filter.text.commons;

import java.util.EmptyStackException;
import java.util.Stack;

import org.geotools.filter.text.cql2.CQLException;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;

import com.vividsolutions.jts.geom.Geometry;


/**
 * Holds the results of the building process in a stack
 * <p>
 * Warning: This component is not published. It is part of module implementation. 
 * Client module should not use this feature.
 * </p>
 * @author Mauricio Pazos - Axios Engineering
 * @author Gabriel Roldan - Axios Engineering
 *
 *
 * @source $URL$
 * @version $Id$
 * @since 2.4
 */
public final class BuildResultStack {

    private final Stack<Result> stack = new Stack<Result>();

    private final String cqlSource;

    public BuildResultStack(final String cqlSource) {
        assert cqlSource != null;
        
        this.cqlSource  = cqlSource;
    }


    public Result peek() {
        return stack.peek();
    }

    public boolean empty() {
        return stack.empty();
    }

    public Result popResult() throws CQLException {
        return stack.pop();
    }

    public org.opengis.filter.expression.Expression popExpression()
        throws CQLException {
        Result item = null;

        try {
            item = stack.pop();

            return (org.opengis.filter.expression.Expression) item.getBuilt();
        } catch (ClassCastException cce) {
            throw new CQLException("Expecting Expression",
                item.getToken(), cce, this.cqlSource);
        } catch (EmptyStackException ese) {
            throw new CQLException("No items on stack");
        }
    }
    public Literal popLiteral() throws CQLException {
        Result item = null;

        try {
            item =  stack.pop();

            return (Literal) item.getBuilt();
        } catch (ClassCastException cce) {
            throw new CQLException("Expecting Literal",
                item.getToken(), cce, this.cqlSource);
        } catch (EmptyStackException ese) {
            throw new CQLException("No items on stack");
        }
    }

    public PropertyName popPropertyName() throws CQLException {
        Result item = null;

        try {
            item = stack.pop();

            return (PropertyName) item.getBuilt();
        } catch (ClassCastException cce) {
            throw new CQLException("Expecting Property",
                item.getToken(), cce, this.cqlSource);
        } catch (EmptyStackException ese) {
            throw new CQLException("No items on stack");
        }
    }

    public org.opengis.filter.Filter popFilter() throws CQLException {
        Result item = null;

        try {
            item = stack.pop();

            return (org.opengis.filter.Filter) item.getBuilt();
        } catch (ClassCastException cce) {
            throw new CQLException("Expecting Filter",
                item.getToken(), cce, this.cqlSource);
        } catch (EmptyStackException ese) {
            throw new CQLException("No items on stack");
        }
    }

    public PeriodNode popPeriod() throws CQLException {
        Result item = null;

        try {
            item = stack.pop();

            return (PeriodNode) item.getBuilt();
        } catch (ClassCastException cce) {
            throw new CQLException("Expecting Period",
                item.getToken(), cce, this.cqlSource);
        } catch (EmptyStackException ese) {
            throw new CQLException("No items on stack");
        }
    }

    public double popDoubleValue() throws CQLException {
        try {
            Literal expr = this.popLiteral();
            Double number = new Double(expr.getValue().toString());

            return number.doubleValue();
        } catch (ClassCastException cce) {
            throw new CQLException("Expected double");
        }
    }

    public int popIntegerValue() throws CQLException {
        try {
            Literal expr = this.popLiteral();
            Integer number = (Integer) expr.getValue();

            return number.intValue();
        } catch (ClassCastException cce) {
            throw new CQLException("Expected double");
        }
    }

    public String popStringValue() throws CQLException {
        Literal literal = this.popLiteral();

        return literal.toString();
    }

    public String popIdentifierPart() throws CQLException {
        try {
            Result resultPart = stack.pop();
            IToken token = resultPart.getToken();

            return token.toString();
        } catch (ClassCastException e) {
            throw new CQLException("identifier part is expected");
        }
    }

    public String popIdentifier() throws CQLException {
        try {
            Result result = (Result) stack.pop();
            String identifier = (String) result.getBuilt();

            return identifier;
        } catch (ClassCastException e) {
            throw new CQLException("fail in identifier parsing");
        }
    }

    public Geometry popGeometry() throws CQLException {
        try {
            Result result = (Result) stack.pop();
            Geometry g = (Geometry) result.getBuilt();

            return g;
            
        } catch (ClassCastException e) {
            throw new CQLException("fail in geometry parsing");
        }
    }

    public void push(Result item) {
        stack.push(item);
    }

    public int size() {
        return stack.size();
    }


}
