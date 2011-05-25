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

import org.geotools.feature.FeatureCollection;
import org.geotools.feature.visitor.BoundsVisitor;
import org.geotools.feature.visitor.CalcResult;
import org.geotools.filter.AttributeExpression;
import org.geotools.filter.Expression;
import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.IllegalFilterException;
import org.geotools.filter.visitor.AbstractFilterVisitor;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;


/**
 * Calculates the bounds of an attribute for a given FeatureCollection
 * and Expression.
 * 
 * @author Cory Horner
 * @since 2.2M2
 *
 * @source $URL$
 */
public class Collection_BoundsFunction extends FunctionExpressionImpl{
    /** The logger for the filter module. */
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(
            "org.geotools.filter.function");
    FeatureCollection<FeatureType, Feature> previousFeatureCollection = null;
    Object bounds = null;

    /**
     * Creates a new instance of Collection_BoundsFunction
     */
    public Collection_BoundsFunction() {
        super("Collection_Bounds");
    }

    public int getArgCount() {
        return 1;
    }

    /**
     * Calculate unique (using FeatureCalc) - only one parameter is used.
     *
     * @param collection collection to calculate the unique
     *
     * @return An object containing the unique value of the attributes
     *
     * @throws IllegalFilterException
     * @throws IOException 
     */
    static CalcResult calculateBounds(
            FeatureCollection<? extends FeatureType, ? extends Feature> collection)
            throws IllegalFilterException, IOException {
        BoundsVisitor boundsVisitor = new BoundsVisitor();
        collection.accepts(boundsVisitor, null);

        return boundsVisitor.getResult();
    }

    /**
     * The provided arguments are evaulated with respect to the
     * FeatureCollection.
     * 
     * <p>
     * For an aggregate function (like unique) please use the WFS mandated XPath
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
        if (args.size() != 1) {
            throw new IllegalArgumentException(
                "Require a single argument for unique");
        }

        //HACK: remove cast once the move to geoapi is complete
        Expression expr;
        expr = (Expression) getExpression(0);

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

    @SuppressWarnings("unchecked")
    public Object evaluate(Object feature) {
		if (feature == null) {
			return new Integer(0); // no features were visited in the making of this answer
		}
		FeatureCollection<FeatureType, Feature> featureCollection = (FeatureCollection<FeatureType, Feature>) feature;
		synchronized (featureCollection) {
			if (featureCollection != previousFeatureCollection) {
				previousFeatureCollection = featureCollection;
				bounds = null;
				try {
					CalcResult result = calculateBounds(featureCollection);
					if (result != null) {
						bounds = result.getValue();
					}
				} catch (IllegalFilterException e) {
					LOGGER.log(Level.FINER, e.getLocalizedMessage(), e);
				} catch (IOException e) {
					LOGGER.log(Level.FINER, e.getLocalizedMessage(), e);
				}
			}
		}
		return bounds;
    }

    public void setExpression(Expression e) {
        setParameters(Collections.singletonList(e));
    }

}
