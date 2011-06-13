/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2009-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.expression;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.capability.FunctionNameImpl;
import org.geotools.geometry.DirectPosition1D;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.referencing.CRS;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.ExpressionVisitor;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;
import org.opengis.geometry.DirectPosition;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * This function converts double values to DirectPosition geometry type. This is needed when the
 * data store doesn't have geometry type columns. This function expects:
 * <ol>
 * <li>Literal: SRS_NAME (optional)
 * <li>Expression: expression of srs name if SRS_NAME is defined
 * <li>Expression: name of column pointing to first double value
 * <li>Expression: name of column pointing to second double value (optional, only for 2D)
 * </ol>
 * 
 * @author Rini Angreani (CSIRO Earth Science and Resource Engineering)
 *
 *
 *
 * @source $URL$
 */
public class ToDirectPositionFunction implements Function {

    private final List<Expression> parameters;

    private final Literal fallback;
    
    private static final String USAGE = "Usage: toDirectPosition('SRS_NAME'(optional), srsName(optional), point 1, point 2(optional))";

    public static final FunctionName NAME = new FunctionNameImpl("toDirectPosition",
            FunctionNameImpl.parameter("return", DirectPosition.class), FunctionNameImpl.parameter(
                    "parameter", Object.class, 1, 4));

    private static FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);

    public static final Expression SRS_NAME = ff.literal("SRS_NAME");

    public ToDirectPositionFunction() {
        this(new ArrayList<Expression>(), null);
    }

    public ToDirectPositionFunction(List<Expression> parameters, Literal fallback) {
        this.parameters = parameters;
        this.fallback = fallback;
    }

    public String getName() {
        return NAME.getName();
    }
    public FunctionName getFunctionName() {
        return NAME;
    }

    public List<Expression> getParameters() {
        return Collections.unmodifiableList(parameters);
    }

    public Literal getFallbackValue() {
        return fallback;
    }

    public Object accept(ExpressionVisitor visitor, Object extraData) {
        return visitor.visit(this, extraData);
    }

    public Object evaluate(Object object) {
        return evaluate(object, DirectPosition.class);
    }

    @SuppressWarnings("unchecked")
    public <T> T evaluate(Object object, Class<T> context) {
        Expression param1 = parameters.get(0);
        CoordinateReferenceSystem crs = null;

        DirectPosition geom;
        if (param1.equals(SRS_NAME)) {
            // must be followed by srsName expression, and at least 1 point
            if (parameters.size() < 3 || parameters.size() > 4) {
                throw new IllegalArgumentException(
                        "Wrong number of parameters toDirectPosition function: "
                                + parameters.toString() + ". " + USAGE);
            }
            String srsName = parameters.get(1).evaluate(object, String.class);
            try {
                crs = CRS.decode((String) srsName);
            } catch (NoSuchAuthorityCodeException e) {
                throw new IllegalArgumentException(
                        "Invalid or unsupported SRS name detected for toDirectPosition function: "
                                + srsName + ". Cause: " + e.getMessage());
            } catch (FactoryException e) {
                throw new RuntimeException("Unable to decode SRS name. Cause: " + e.getMessage());
            }
            if (parameters.size() == 3) {
                // 1D
                geom = new DirectPosition1D(crs);
            } else {
                // 2D
                geom = new DirectPosition2D(crs);
                geom.setOrdinate(1, parameters.get(3).evaluate(object, Double.class));
            }
            geom.setOrdinate(0, parameters.get(2).evaluate(object, Double.class));
        } else {
            // should only have points, 1 for 1D, 2 for 2D
            if (parameters.size() > 2) {
                throw new IllegalArgumentException(
                        "Too many parameters for toDirectPosition function: "
                                + parameters.toString() + ". " + USAGE);
            }
            if (parameters.size() == 1) {
                // 1D
                geom = new DirectPosition1D();
            } else {
                // 2D
                geom = new DirectPosition2D();
                geom.setOrdinate(1, parameters.get(1).evaluate(object, Double.class));
            }
            geom.setOrdinate(0, param1.evaluate(object, Double.class));
        }

        return (T) geom;
    }

}
