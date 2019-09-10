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
package org.geotools.data.complex.expression;

import static org.geotools.filter.capability.FunctionNameImpl.parameter;

import java.util.ArrayList;
import java.util.List;
import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.LiteralExpressionImpl;
import org.geotools.filter.capability.FunctionNameImpl;
import org.geotools.filter.visitor.DuplicatingFilterVisitor;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.ExpressionVisitor;
import org.opengis.filter.expression.PropertyName;

/**
 * Builds a list via iteration. An index property name, a count (number of iterations) and an
 * (indexed) expression must be provided. The result is a list containing the evaluated expression
 * for each index.
 *
 * @author Niels Charlier
 */
public class LiterateFunction extends FunctionExpressionImpl {

    public static FunctionName NAME =
            new FunctionNameImpl(
                    "literate",
                    parameter(
                            "result",
                            List.class,
                            "Result",
                            "List of items resulted from iteration."),
                    parameter(
                            "index",
                            Object.class,
                            "Index",
                            "The index identifier in the expression"),
                    parameter("times", Integer.class, "Times", "Number of iterations"),
                    parameter(
                            "expression",
                            Object.class,
                            "Expression",
                            "Expression to be applied to each index of the iteration."));

    public LiterateFunction() {
        super(NAME);
    }

    @Override
    public Object evaluate(Object feature) {
        if (!(getExpression(0) instanceof PropertyName)) {
            throw new IllegalArgumentException(
                    "literate function first argument must be index property name");
        }
        PropertyName indexName = (PropertyName) getExpression(0);
        Integer size = getExpression(1).evaluate(feature, Integer.class);
        if (size == null || size <= 0) {
            throw new IllegalArgumentException(
                    "literate function requires non-null, positive size");
        }

        List<Object> result = new ArrayList<Object>();
        for (int i = 0; i < size; i++) {
            final int index = i;
            ExpressionVisitor indexVisitor =
                    new DuplicatingFilterVisitor() {
                        public Object visit(PropertyName name, Object data) {
                            if (name.equals(indexName)) {
                                return new LiteralExpressionImpl(index);
                            }
                            return name;
                        };
                    };
            Expression indexedExpression = (Expression) getExpression(2).accept(indexVisitor, null);
            result.add(indexedExpression.evaluate(feature));
        }

        return result;
    }
}
