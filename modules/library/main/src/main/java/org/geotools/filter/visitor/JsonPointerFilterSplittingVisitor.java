/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2023, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.visitor;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.FilterAttributeExtractor;
import org.geotools.filter.FilterCapabilities;
import org.geotools.filter.function.JsonPointerFunction;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;

public class JsonPointerFilterSplittingVisitor extends PostPreProcessFilterSplittingVisitor {

    public JsonPointerFilterSplittingVisitor(
            FilterCapabilities fcs,
            SimpleFeatureType parent,
            ClientTransactionAccessor transactionAccessor) {
        super(fcs, parent, transactionAccessor);
    }

    @Override
    public Object visit(Function expression, Object notUsed) {
        if (expression instanceof JsonPointerFunction) {
            // takes the json pointer param to check if
            // can be encoded
            Expression param = expression.getParameters().get(1);
            if (!(param instanceof Literal)) {
                expression = constantParameterToLiteral(expression, param, 1);
            }
        }
        return super.visit(expression, notUsed);
    }

    @Override
    protected boolean supports(Object value) {
        if (value instanceof JsonPointerFunction) {
            Expression param = ((Function) value).getParameters().get(1);
            return param instanceof Literal;
        } else return super.supports(value);
    }

    private Function constantParameterToLiteral(
            Function expression, Expression param, int paramIdx) {
        FilterAttributeExtractor extractor = new FilterAttributeExtractor();
        param.accept(extractor, null);
        if (extractor.isConstantExpression()) {
            // defensive copy of filter before manipulating it
            DuplicatingFilterVisitor duplicating = new DuplicatingFilterVisitor();
            Function duplicated = (Function) expression.accept(duplicating, null);
            // if constant can encode
            Object result = param.evaluate(null);
            FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
            // setting constant expression evaluated to literal
            duplicated.getParameters().set(paramIdx, ff.literal(result));
            return duplicated;
        }
        return expression;
    }
}
