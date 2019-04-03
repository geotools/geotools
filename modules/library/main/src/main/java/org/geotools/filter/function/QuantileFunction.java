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
 */
package org.geotools.filter.function;

import static org.geotools.filter.capability.FunctionNameImpl.parameter;

import org.geotools.feature.visitor.QuantileListVisitor;
import org.geotools.filter.capability.FunctionNameImpl;
import org.opengis.filter.capability.FunctionName;

/**
 * Breaks a SimpleFeatureCollection into classes with an equal number of items in each.
 *
 * @author Cory Horner, Refractions Research Inc.
 */
public class QuantileFunction extends AbstractQuantityClassificationFunction {

    public static FunctionName NAME =
            new FunctionNameImpl(
                    "Quantile",
                    RangedClassifier.class,
                    parameter("value", Double.class),
                    parameter("classes", Integer.class));

    public QuantileFunction() {
        super(NAME);
    }

    protected QuantileListVisitor getListVisitor() {
        // use a visitor to find the values in each bin
        return new QuantileListVisitor(getParameters().get(0), getClasses());
    }
}
