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

import org.geotools.filter.Expression;
import org.geotools.filter.FunctionExpressionImpl;
import org.opengis.filter.expression.Literal;

public class ClassifyFunction extends FunctionExpressionImpl {

    //parameters are expression, classifier
    
    public ClassifyFunction() {
        super("classify");
    }
    
    public int getArgCount() {
        return 2;
    }

    public Classifier getClassifier(Object context) {
        Expression expr = (Expression) getParameters().get(1);
        if (expr instanceof Literal) {
            // this is not good practice! (but the other method doesn't work)
            Literal literal = (Literal) expr;
            return (Classifier) literal.getValue();
        }
        return (Classifier) expr.evaluate(context, Classifier.class);        
    }
    
    public Expression getExpression() {
        return (Expression) getParameters().get(0);
    }
    
    public Object evaluate(Object feature) {
        Classifier classifier = getClassifier( feature );
        Expression expression = getExpression();
        return new Integer(classifier.classify(expression, feature)); 
    }

}
