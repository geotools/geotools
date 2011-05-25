/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
 *
 * Created on May 11, 2005, 6:21 PM
 */
package org.geotools.filter.function;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.visitor.CalcResult;
import org.geotools.feature.visitor.SumVisitor;
import org.geotools.filter.AttributeExpression;
import org.geotools.filter.Expression;
import org.geotools.filter.FunctionExpression;
import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.IllegalFilterException;
import org.geotools.filter.visitor.AbstractFilterVisitor;


/**
 * Calculates the sum value of an attribute for a given FeatureCollection
 * and Expression.
 * 
 * @author Cory Horner
 * @since 2.2M2
 *
 * @source $URL$
 */
public class Collection_SumFunction extends FunctionExpressionImpl
    implements FunctionExpression {
    /** The logger for the filter module. */
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(
            "org.geotools.filter.function");
    SimpleFeatureCollection previousFeatureCollection = null;
    Object sum = null;

    /**
     * Creates a new instance of Collection_SumFunction
     */
    public Collection_SumFunction() {
        super("Collection_Sum");
    }

    public int getArgCount() {
        return 1;
    }

    /**
     * Calculate sum (using FeatureCalc) - only one parameter is used.
     *
     * @param collection collection to calculate the sum
     * @param expression Single Expression argument
     *
     * @return An object containing the sum value of the attributes
     *
     * @throws IllegalFilterException
     * @throws IOException 
     */
    static CalcResult calculateSum(SimpleFeatureCollection collection,
        Expression expression) throws IllegalFilterException, IOException {
        SumVisitor sumVisitor = new SumVisitor(expression);
        collection.accepts(sumVisitor, null);

        return sumVisitor.getResult();
    }

    /**
     * The provided arguments are evaulated with respect to the
     * FeatureCollection.
     * 
     * <p>
     * For an aggregate function (like sum) please use the WFS mandated XPath
     * syntax to refer to featureMember content.
     * </p>
     * 
     * <p>
     * To refer to all 'X': <code>featureMember/asterisk/X</code>
     * </p>
     *
     * @param args DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setParameters(List args) {
        super.setParameters(args);
        
        Expression expr = (Expression) getExpression(0);

        // if we see "featureMembers/*/ATTRIBUTE" change to "ATTRIBUTE"
        expr.accept(new AbstractFilterVisitor() {
                public void visit(AttributeExpression expression) {
                    String xpath = expression.getAttributePath();

                    if (xpath.startsWith("featureMembers/*/")) {
                        xpath = xpath.substring(17);
                    } else if (xpath.startsWith("featureMember/*/")) {
                        xpath = xpath.substring(16);
                    }

                    try {
                        expression.setAttributePath(xpath);
                    } catch (IllegalFilterException e) {
                        // ignore
                    }
                }
            });
    }

    public Object evaluate(Object feature) {
		if (feature == null) {
			return new Integer(0); // no features were visited in the making of this answer
		}
		SimpleFeatureCollection featureCollection = (SimpleFeatureCollection) feature;
                Expression expr = (Expression) getExpression(0);
		synchronized (featureCollection) {
			if (featureCollection != previousFeatureCollection) {
				previousFeatureCollection = featureCollection;
				sum = null;
				try {
					CalcResult result = calculateSum(featureCollection, expr);
					if (result != null) {
						sum = result.getValue();
					}
				} catch (IllegalFilterException e) {
					LOGGER.log(Level.FINER, e.getLocalizedMessage(), e);
				} catch (IOException e) {
					LOGGER.log(Level.FINER, e.getLocalizedMessage(), e);
				}
			}
		}
		return sum;
    }

    public void setExpression(Expression e) {
        setParameters(Collections.singletonList(e));
    }
}
