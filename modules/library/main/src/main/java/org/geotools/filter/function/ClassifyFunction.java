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
package org.geotools.filter.function;

import static org.geotools.filter.capability.FunctionNameImpl.parameter;

import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.capability.FunctionNameImpl;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.expression.Literal;

public class ClassifyFunction extends FunctionExpressionImpl {

    // parameters are expression, classifier
    public static FunctionName NAME =
            new FunctionNameImpl(
                    "classify",
                    parameter("value", Object.class),
                    parameter("expression", Object.class),
                    parameter("classifer", Classifier.class));

    public ClassifyFunction() {
        super(NAME);
    }

    public Classifier getClassifier(Object context) {
        org.opengis.filter.expression.Expression expr = getParameters().get(1);
        if (expr instanceof Literal) {
            // this is not good practice! (but the other method doesn't work)
            Literal literal = (Literal) expr;
            return (Classifier) literal.getValue();
        }
        return (Classifier) expr.evaluate(context, Classifier.class);
    }

    public org.opengis.filter.expression.Expression getExpression() {
        return (org.opengis.filter.expression.Expression) getParameters().get(0);
    }

    public Object evaluate(Object feature) {
        Classifier classifier = getClassifier(feature);
        org.opengis.filter.expression.Expression expression = getExpression();
        return Integer.valueOf(classifier.classify(expression, feature));
    }
}
