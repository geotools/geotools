/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.visitor.EqualAreaListVisitor;
import org.geotools.feature.visitor.FeatureCalc;
import org.geotools.filter.capability.FunctionNameImpl;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;

/**
 * Breaks a SimpleFeatureCollection into classes with (roughtly) equal total items area in each
 * class
 *
 * @author Andrea Aime - GeoSolutions
 */
public class EqualAreaFunction extends AbstractQuantityClassificationFunction {

    private static final FilterFactory2 FF = CommonFactoryFinder.getFilterFactory2();

    public static FunctionName NAME =
            new FunctionNameImpl(
                    "EqualArea",
                    RangedClassifier.class,
                    parameter("value", Double.class),
                    parameter("classes", Integer.class),
                    parameter("areaFunction", Double.class, 0, 1));

    public EqualAreaFunction() {
        super(NAME);
    }

    /**
     * The default area is computed as a cartesian area of the data (will work reasonably on
     * geodetic dataset over small areas, but won't work properly over large areas) However, it is
     * to be remembered that these classification functions are trying to get a certain evennes on
     * the display, so if the display is in plate caree, then computing area over lon/lat is
     * actually the right thing to do.
     */
    public static Function getCartesianAreaFunction() {
        // Would have loved to keep this as static, but cannot be done since the equal area
        // function class is created while the function lookup is being initialized
        return FF.function("area2", FF.property(""));
    }

    protected FeatureCalc getListVisitor() {
        Expression areaFunction;
        if (getParameters().size() > 2) {
            areaFunction = getParameters().get(2);
        } else {
            areaFunction = getCartesianAreaFunction();
        }
        return new EqualAreaListVisitor(getParameters().get(0), areaFunction, getClasses());
    }
}
