/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2012, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.util;

import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.filter.capability.FunctionName;
import org.geotools.api.filter.expression.VolatileFunction;
import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.capability.FunctionNameImpl;

/**
 * Function to identify if a feature is a wrapped coverage or not
 *
 * @author Davide Savazzi - GeoSolutions
 */
public class FilterFunction_isCoverage extends FunctionExpressionImpl implements VolatileFunction {

    public static FunctionName NAME = new FunctionNameImpl("isCoverage", Boolean.class);

    public FilterFunction_isCoverage() {
        super(NAME);
    }

    public Object evaluate(SimpleFeature feature) {
        return FeatureUtilities.isWrappedCoverageReader(feature.getFeatureType())
                || FeatureUtilities.isWrappedCoverage(feature.getFeatureType());
    }

    @Override
    public Object evaluate(Object object) {
        if (object instanceof SimpleFeature feature) {
            return evaluate(feature);
        } else {
            return Boolean.FALSE;
        }
    }
}
