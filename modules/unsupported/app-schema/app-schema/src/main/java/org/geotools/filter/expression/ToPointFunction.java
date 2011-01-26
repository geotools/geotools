/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2009, Open Source Geospatial Foundation (OSGeo)
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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.geotools.factory.CommonFactoryFinder;
import org.geotools.referencing.CRS;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.ExpressionVisitor;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.PrecisionModel;

/**
 * This function converts double values to a 2D Point geometry type. This is needed when the data
 * store doesn't have geometry type columns. This function expects:
 * <ol>
 * <li>Literal: SRS_NAME (optional)
 * <li>Expression: expression of SRS_NAME if SRS_NAME is defined
 * <li>Expression: name of column pointing to first double value
 * <li>Expression: name of column pointing to second double value
 * <li>Expression: expression of gml:id (optional)
 * </ol>
 * 
 * @author Rini Angreani, Curtin University of Technology
 *
 * @source $URL$
 */
public class ToPointFunction implements Function {
    private final List<Expression> parameters;

    private final Literal fallback;

    /**
     * Make the instance of FunctionName available in a consistent spot.
     */
    public static final FunctionName NAME = new Name();

    private static FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);

    public static final Expression GML_ID = ff.literal("gml:id");

    /**
     * Describe how this function works. (should be available via FactoryFinder lookup...)
     */
    public static class Name implements FunctionName {

        public int getArgumentCount() {
            return 2; // 2 minimum, if gml:id and srsName aren't defined
        }

        public List<String> getArgumentNames() {
            return Arrays.asList(new String[] { "toPoint", "SRS_NAME", "srsName value",
                    "double value 1", "double value 2", "gml:id expression" });
        }

        public String getName() {
            return "toPoint";
        }
    };

    public ToPointFunction() {
        this(new ArrayList<Expression>(), null);
    }

    public ToPointFunction(List<Expression> parameters, Literal fallback) {
        this.parameters = parameters;
        this.fallback = fallback;
    }

    public String getName() {
        return "toPoint";
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
        return evaluate(object, Point.class);
    }

    public <T> T evaluate(Object object, Class<T> context) {
        Point point;
        Expression param1 = parameters.get(0);

        if (param1.equals(ToDirectPositionFunction.SRS_NAME)) {

            if (parameters.size() > 5 || parameters.size() < 4) {
                throw new IllegalArgumentException(
                        "Wrong number of parameters for toPoint function: "
                                + parameters.toString()
                                + ". Usage: toPoint('SRS_NAME'(optional), srsName(optional), point 1, point 2, gml:id(optional))");
            }
            CoordinateReferenceSystem crs = null;
            String srsName = parameters.get(1).evaluate(object, String.class);
            try {
                crs = CRS.decode((String) srsName);
            } catch (NoSuchAuthorityCodeException e) {
                throw new IllegalArgumentException(
                        "Invalid or unsupported SRS name detected for toPoint function: " + srsName
                                + ". Cause: " + e.getMessage());
            } catch (FactoryException e) {
                throw new RuntimeException("Unable to decode SRS name. Cause: " + e.getMessage());
            }
            GeometryFactory fac = new GeometryFactory(new PrecisionModel());
            point = fac.createPoint(new Coordinate(
                    parameters.get(2).evaluate(object, Double.class), parameters.get(3).evaluate(
                            object, Double.class)));
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
                                + ". Usage: toPoint('SRS_NAME'(optional), srsName(optional), point 1, point 2, gml:id(optional))");
            }
            GeometryFactory fac = new GeometryFactory();

            point = fac.createPoint(new Coordinate(param1.evaluate(object, Double.class),
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
     * @param point
     *            The point object
     * @param crs
     *            Coordinate System object
     * @param srsName
     *            srs name
     * @param gmlId
     *            gml:id value
     */
    private void setUserData(Point geom, CoordinateReferenceSystem crs, String gmlId) {
        Map<Object, Object> userData = new HashMap<Object, Object>();
        if (gmlId != null) {
            userData.put("gml:id", gmlId);
        }
        if (crs != null) {
            userData.put(CoordinateReferenceSystem.class, crs);
        }
        geom.setUserData(userData);
    }

}
