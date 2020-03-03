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
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.util.NullProgressListener;
import org.geotools.feature.visitor.NearestVisitor;
import org.geotools.filter.FunctionImpl;
import org.geotools.filter.IllegalFilterException;
import org.geotools.filter.capability.FunctionNameImpl;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Literal;

/**
 * Finds the nearest value to the provided one in the attribute domain.
 *
 * @author Jody Garnett
 * @since 12.0
 */
public class Collection_NearestFunction extends FunctionImpl {

    /** The logger for the filter module. */
    private static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(Collection_NearestFunction.class);

    SimpleFeatureCollection previousFeatureCollection = null;

    Object match = null;

    public static FunctionName NAME =
            new FunctionNameImpl(
                    "Collection_Nearest",
                    parameter("expression", Expression.class),
                    parameter("value", Comparable.class));

    /** Creates a new instance of Collection_MaxFunction */
    public Collection_NearestFunction() {
        this.functionName = NAME;
    }

    /**
     * Calculate nearest using {@link NearestVisitor}.
     *
     * @param collection collection to calculate the maximum
     * @param expression Single Expression argument
     * @param value Attribute value used for nearest search
     * @return An object containing the maximum value of the attributes
     */
    static Object near(SimpleFeatureCollection collection, Expression expression, Object value)
            throws IllegalFilterException, IOException {
        NearestVisitor visitor = new NearestVisitor(expression, value);
        collection.accepts(visitor, new NullProgressListener());
        return visitor.getNearestMatch();
    }

    public Object evaluate(Object collection) {
        if (collection == null) {
            Literal value = getFallbackValue();
            return value == null ? null : value.getValue();
        }

        Expression expr = getParameters().get(0);
        Literal value = (Literal) getParameters().get(1);

        SimpleFeatureCollection featureCollection = (SimpleFeatureCollection) collection;
        synchronized (featureCollection) {
            if (featureCollection != previousFeatureCollection) {
                previousFeatureCollection = featureCollection;
                match = null;
                try {
                    Object result = near(featureCollection, expr, value.getValue());
                    if (result != null) {
                        match = result;
                    }
                } catch (IllegalFilterException e) {
                    LOGGER.log(Level.FINER, e.getLocalizedMessage(), e);
                } catch (IOException e) {
                    LOGGER.log(Level.FINER, e.getLocalizedMessage(), e);
                }
            }
        }
        return match;
    }
}
