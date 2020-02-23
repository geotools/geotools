/*
 *    GeoTools - The Open Source Java GIS Toolkit
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
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.visitor.AverageVisitor;
import org.geotools.feature.visitor.CalcResult;
import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.IllegalFilterException;
import org.geotools.filter.capability.FunctionNameImpl;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.expression.Expression;

/**
 * Calculates the average value of an attribute for a given FeatureCollection and Expression.
 *
 * @author Cory Horner
 * @since 2.2M2
 * @version 8.0
 */
public class Collection_AverageFunction extends FunctionExpressionImpl {
    /** The logger for the filter module. */
    private static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(Collection_AverageFunction.class);

    FeatureCollection<FeatureType, Feature> previousFeatureCollection = null;
    Object average = null;
    Expression expr;

    public static FunctionName NAME =
            new FunctionNameImpl(
                    "Collection_Average",
                    parameter("average", Number.class),
                    parameter("expression", Number.class));

    /** Creates a new instance of Collection_AverageFunction */
    public Collection_AverageFunction() {
        super(NAME);
    }

    /**
     * Calculate average (using FeatureCalc) - only one parameter is used.
     *
     * @param collection collection to calculate the average
     * @param expression Single Expression argument
     * @return An object containing the average value of the attributes
     */
    static CalcResult calculateAverage(
            FeatureCollection<? extends FeatureType, ? extends Feature> collection,
            Expression expression)
            throws IllegalFilterException, IOException {
        AverageVisitor averageVisitor = new AverageVisitor(expression);
        collection.accepts(averageVisitor, null);

        return averageVisitor.getResult();
    }

    /**
     * The provided arguments are evaulated with respect to the FeatureCollection.
     *
     * <p>For an aggregate function (like average) please use the WFS mandated XPath syntax to refer
     * to featureMember content.
     *
     * <p>To refer to all 'X': <code>featureMember/asterisk/X</code>
     *
     * @param params function parameters
     */
    public void setParameters(List params) {
        super.setParameters(params);
        if (params.size() != 1) {
            throw new IllegalArgumentException("Require a single argument for average");
        }
        expr = (Expression) getExpression(0);
        // if we see "featureMembers/*/ATTRIBUTE" change to "ATTRIBUTE"
        expr = (Expression) expr.accept(new CollectionFeatureMemberFilterVisitor(), null);
    }

    @SuppressWarnings("unchecked")
    public Object evaluate(Object feature) {
        if (feature == null) {
            return Integer.valueOf(0); // no features were visited in the making of this answer
        }
        FeatureCollection<FeatureType, Feature> featureCollection =
                (FeatureCollection<FeatureType, Feature>) feature;
        synchronized (featureCollection) {
            if (featureCollection != previousFeatureCollection) {
                previousFeatureCollection = featureCollection;
                average = null;
                try {
                    CalcResult result = calculateAverage(featureCollection, expr);
                    if (result != null) {
                        average = result.getValue();
                    }
                } catch (IllegalFilterException e) {
                    LOGGER.log(Level.FINER, e.getLocalizedMessage(), e);
                } catch (IOException e) {
                    LOGGER.log(Level.FINER, e.getLocalizedMessage(), e);
                }
            }
        }
        return average;
    }

    public void setExpression(Expression e) {
        setParameters(Collections.singletonList(e));
    }

    /**
     * Return this function as a string.
     *
     * @return String representation of this average function.
     */
    public String toString() {
        return "Collection_Average(" + expr + ")";
    }
}
