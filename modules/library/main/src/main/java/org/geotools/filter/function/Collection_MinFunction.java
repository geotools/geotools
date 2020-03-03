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

import static org.geotools.filter.capability.FunctionNameImpl.parameter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.visitor.CalcResult;
import org.geotools.feature.visitor.MinVisitor;
import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.IllegalFilterException;
import org.geotools.filter.capability.FunctionNameImpl;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.expression.Expression;

/**
 * Calculates the minimum value of an attribute for a given FeatureCollection and Expression.
 *
 * @author James
 */
public class Collection_MinFunction extends FunctionExpressionImpl {
    /** The logger for the filter module. */
    private static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(Collection_MinFunction.class);

    SimpleFeatureCollection previousFeatureCollection = null;
    Object min = null;

    // public static FunctionName NAME = new FunctionNameImpl("Collection_Min","expression");
    public static FunctionName NAME =
            new FunctionNameImpl(
                    "Collection_Min",
                    parameter("min", Comparable.class),
                    parameter("expression", Comparable.class));

    /** Creates a new instance of Collection_MinFunction */
    public Collection_MinFunction() {
        super(NAME);
    }

    /**
     * Calculate minimum (using FeatureCalc) - only one parameter is used.
     *
     * @param collection collection to calculate the minimum
     * @param expression Single Expression argument
     * @return An object containing the minimum value of the attributes
     */
    static CalcResult calculateMin(SimpleFeatureCollection collection, Expression expression)
            throws IllegalFilterException, IOException {
        MinVisitor minVisitor = new MinVisitor(expression);
        collection.accepts(minVisitor, null);
        return minVisitor.getResult();
    }

    /**
     * The provided arguments are evaulated with respect to the FeatureCollection.
     *
     * <p>For an aggregate function (like min) please use the WFS mandated XPath syntax to refer to
     * featureMember content.
     *
     * <p>To refer to all 'X': <code>featureMember/asterisk/X</code>
     */
    public void setParameters(List args) {
        // if we see "featureMembers/*/ATTRIBUTE" change to "ATTRIBUTE"
        org.opengis.filter.expression.Expression expr =
                (org.opengis.filter.expression.Expression) args.get(0);
        expr =
                (org.opengis.filter.expression.Expression)
                        expr.accept(new CollectionFeatureMemberFilterVisitor(), null);
        args.set(0, expr);
        super.setParameters(args);
    }

    public Object evaluate(Object feature) {
        if (feature == null) {
            return Integer.valueOf(0); // no features were visited in the making of this answer
        }
        Expression expr = (Expression) getExpression(0);
        SimpleFeatureCollection featureCollection = (SimpleFeatureCollection) feature;
        synchronized (featureCollection) {
            if (featureCollection != previousFeatureCollection) {
                previousFeatureCollection = featureCollection;
                min = null;
                try {
                    CalcResult result = calculateMin(featureCollection, expr);
                    if (result != null) {
                        min = result.getValue();
                    }
                } catch (IllegalFilterException e) {
                    LOGGER.log(Level.FINER, e.getLocalizedMessage(), e);
                } catch (IOException e) {
                    LOGGER.log(Level.FINER, e.getLocalizedMessage(), e);
                }
            }
        }
        return min;
    }

    public void setExpression(Expression e) {
        setParameters(Collections.singletonList(e));
    }
}
