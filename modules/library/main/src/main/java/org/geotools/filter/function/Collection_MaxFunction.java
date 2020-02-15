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
import org.geotools.feature.visitor.MaxVisitor;
import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.IllegalFilterException;
import org.geotools.filter.capability.FunctionNameImpl;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.expression.Expression;

/**
 * Calculates the maximum value of an attribute for a given FeatureCollection and Expression.
 *
 * @author Cory Horner
 * @since 2.2M2
 */
public class Collection_MaxFunction extends FunctionExpressionImpl {

    /** The logger for the filter module. */
    private static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(Collection_MaxFunction.class);

    SimpleFeatureCollection previousFeatureCollection = null;
    Object max = null;

    // public static FunctionName NAME = new FunctionNameImpl("Collection_Max","value");
    public static FunctionName NAME =
            new FunctionNameImpl(
                    "Collection_Max",
                    parameter("max", Comparable.class),
                    parameter("expression", Comparable.class));

    /** Creates a new instance of Collection_MaxFunction */
    public Collection_MaxFunction() {
        super(NAME);
    }

    /**
     * Calculate maximum (using FeatureCalc) - only one parameter is used.
     *
     * @param collection collection to calculate the maximum
     * @param expression Single Expression argument
     * @return An object containing the maximum value of the attributes
     */
    static CalcResult calculateMax(SimpleFeatureCollection collection, Expression expression)
            throws IllegalFilterException, IOException {
        MaxVisitor maxVisitor = new MaxVisitor(expression);
        collection.accepts(maxVisitor, null);

        return maxVisitor.getResult();
    }

    /**
     * The provided arguments are evaulated with respect to the FeatureCollection.
     *
     * <p>For an aggregate function (like max) please use the WFS mandated XPath syntax to refer to
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
                max = null;
                try {
                    CalcResult result = calculateMax(featureCollection, expr);
                    if (result != null) {
                        max = result.getValue();
                    }
                } catch (IllegalFilterException e) {
                    LOGGER.log(Level.FINER, e.getLocalizedMessage(), e);
                } catch (IOException e) {
                    LOGGER.log(Level.FINER, e.getLocalizedMessage(), e);
                }
            }
        }
        return max;
    }

    public void setExpression(Expression e) {
        setParameters(Collections.singletonList(e));
    }
}
