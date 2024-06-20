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
package org.geotools.appschema.filter.expression;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.capability.FunctionName;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.ExpressionVisitor;
import org.geotools.api.filter.expression.Function;
import org.geotools.api.filter.expression.Literal;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.NoSuchAuthorityCodeException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.capability.FunctionNameImpl;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

/**
 * This function converts double values to a 2D Point geometry type. This is needed when the data
 * store doesn't have geometry type columns. This function expects:
 *
 * <ol>
 *   <li>Literal: SRS_NAME (optional)
 *   <li>Expression: expression of SRS_NAME if SRS_NAME is defined
 *   <li>Expression: name of column pointing to first double value
 *   <li>Expression: name of column pointing to second double value
 *   <li>Expression: expression of gml:id (optional)
 * </ol>
 *
 * @author Rini Angreani (CSIRO Earth Science and Resource Engineering)
 */
public class ToPointFunction implements Function {

    private final List<Expression> parameters;

    private final Literal fallback;

    private static final String USAGE =
            "Usage: toPoint('SRS_NAME'(optional), srsName(optional), point 1, point 2, gml:id(optional))";

    public static final FunctionName NAME =
            new FunctionNameImpl(
                    "toPoint",
                    FunctionNameImpl.parameter("return", Point.class),
                    FunctionNameImpl.parameter("parameter", Object.class, 2, 5));

    private static FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);

    public static final Expression GML_ID = ff.literal("gml:id");

    public ToPointFunction() {
        this(new ArrayList<>(), null);
    }

    public ToPointFunction(List<Expression> parameters, Literal fallback) {
        this.parameters = parameters;
        this.fallback = fallback;
    }

    @Override
    public String getName() {
        return NAME.getName();
    }

    @Override
    public FunctionName getFunctionName() {
        return NAME;
    }

    @Override
    public List<Expression> getParameters() {
        return Collections.unmodifiableList(parameters);
    }

    @Override
    public Literal getFallbackValue() {
        return fallback;
    }

    @Override
    public Object accept(ExpressionVisitor visitor, Object extraData) {
        return visitor.visit(this, extraData);
    }

    @Override
    public Object evaluate(Object object) {
        return evaluate(object, Point.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T evaluate(Object object, Class<T> context) {
        Point point;
        Expression param1 = parameters.get(0);

        if (param1.equals(ToDirectPositionFunction.SRS_NAME)) {

            if (parameters.size() > 5 || parameters.size() < 4) {
                throw new IllegalArgumentException(
                        "Wrong number of parameters for toPoint function: "
                                + parameters.toString()
                                + ". "
                                + USAGE);
            }
            CoordinateReferenceSystem crs = null;
            String srsName = parameters.get(1).evaluate(object, String.class);
            try {
                crs = CRS.decode(srsName);
            } catch (NoSuchAuthorityCodeException e) {
                throw new IllegalArgumentException(
                        "Invalid or unsupported SRS name detected for toPoint function: "
                                + srsName
                                + ". Cause: "
                                + e.getMessage());
            } catch (FactoryException e) {
                throw new RuntimeException("Unable to decode SRS name. Cause: " + e.getMessage());
            }
            GeometryFactory fac = new GeometryFactory(new PrecisionModel());
            point =
                    fac.createPoint(
                            new Coordinate(
                                    parameters.get(2).evaluate(object, Double.class),
                                    parameters.get(3).evaluate(object, Double.class)));
            // set attributes
            String gmlId = null;
            if (parameters.size() == 5) {
                gmlId = parameters.get(4).evaluate(object, String.class);
            }
            setUserData(point, crs, gmlId);
        } else {

            if (parameters.size() > 3 || parameters.size() < 2) {
                throw new IllegalArgumentException(
                        "Wrong number of parameters for toPoint function: "
                                + parameters.toString()
                                + ". "
                                + USAGE);
            }
            GeometryFactory fac = new GeometryFactory();

            point =
                    fac.createPoint(
                            new Coordinate(
                                    param1.evaluate(object, Double.class),
                                    parameters.get(1).evaluate(object, Double.class)));

            if (parameters.size() == 3) {
                String gmlId = parameters.get(2).evaluate(object, String.class);
                setUserData(point, null, gmlId);
            }
        }
        return (T) point;
    }

    /**
     * Set point attributes into its user data to be encoded in Geoserver: gml:id, srsName,
     * srsDimension, axisLabels and uomLabels.
     *
     * @param point The point object
     * @param crs Coordinate System object
     * @param gmlId gml:id value
     */
    private void setUserData(Point point, CoordinateReferenceSystem crs, String gmlId) {
        if (gmlId != null) {
            Map<Object, Object> userData = new HashMap<>();
            userData.put("gml:id", gmlId);
            point.setUserData(userData);
        }
        if (crs != null) {
            JTS.setCRS(point, crs);
        }
    }
}
