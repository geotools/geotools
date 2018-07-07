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

import static org.geotools.filter.capability.FunctionNameImpl.*;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.visitor.CalcResult;
import org.geotools.feature.visitor.CountVisitor;
import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.IllegalFilterException;
import org.geotools.filter.capability.FunctionNameImpl;
import org.opengis.filter.capability.FunctionName;

/**
 * Calculates the count value of an attribute for a given SimpleFeatureCollection and Expression.
 *
 * @author Cory Horner
 * @since 2.2M2
 * @source $URL$
 */
public class Collection_CountFunction extends FunctionExpressionImpl {
    /** The logger for the filter module. */
    private static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger("org.geotools.filter.function");

    SimpleFeatureCollection previousFeatureCollection = null;

    Object count = null;

    public static FunctionName NAME =
            new FunctionNameImpl(
                    "Collection_Count",
                    parameter("count", Number.class),
                    parameter("features", Object.class));

    /** Creates a new instance of Collection_CountFunction */
    public Collection_CountFunction() {
        super(NAME);
    }

    /**
     * Calculate count (using FeatureCalc) - only one parameter is used.
     *
     * @param collection collection to calculate the count
     * @return An object containing the count value of the attributes
     * @throws IllegalFilterException
     * @throws IOException
     */
    static CalcResult calculateCount(SimpleFeatureCollection collection)
            throws IllegalFilterException, IOException {
        CountVisitor countVisitor = new CountVisitor();
        collection.accepts(countVisitor, null);
        return countVisitor.getResult();
    }

    public Object evaluate(Object feature) {
        if (feature == null) {
            return new Integer(0); // no features were visited in the making of this answer
        }
        SimpleFeatureCollection featureCollection = (SimpleFeatureCollection) feature;
        synchronized (featureCollection) {
            if (featureCollection != previousFeatureCollection) {
                previousFeatureCollection = featureCollection;
                count = null;
                try {
                    CalcResult result = calculateCount(featureCollection);
                    if (result != null) {
                        count = result.getValue();
                    }
                } catch (IllegalFilterException e) {
                    LOGGER.log(Level.FINER, e.getLocalizedMessage(), e);
                } catch (IOException e) {
                    LOGGER.log(Level.FINER, e.getLocalizedMessage(), e);
                }
            }
        }
        return count;
    }
}
