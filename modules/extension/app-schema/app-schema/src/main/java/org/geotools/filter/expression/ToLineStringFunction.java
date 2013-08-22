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

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
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
 *
 * @source $URL$
 */
public class ToLineStringFunction implements Function {

    private final List<Expression> parameters;

    private final Literal fallback;
    
    private static final String USAGE = "Usage: toLineString(point 1, point 2)";

    public static final FunctionName NAME = new FunctionNameImpl("toLineString",
            FunctionNameImpl.parameter("return", LineString.class), FunctionNameImpl.parameter(
                    "parameter", Double.class, 2, 2));

    public ToLineStringFunction() {
        this(new ArrayList<Expression>(), null);
    }

    public ToLineStringFunction(List<Expression> parameters, Literal fallback) {
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
        return evaluate(object, LineString.class);
    }

    @SuppressWarnings("unchecked")
    public <T> T evaluate(Object object, Class<T> context) {
        if (parameters.size() != 2 || parameters.get(0) == null || parameters.get(1) == null) {
            throw new IllegalArgumentException("Invalid parameters for toLineString function: "
                    + parameters.toString() + ". " + USAGE);
        }
        // safe parsing and conversion for invalids
        Object pointOne = parameters.get(0).evaluate(object, String.class);
        Object pointTwo = parameters.get(1).evaluate(object, String.class);
        String stringOne = String.valueOf(pointOne);
        String stringTwo = String.valueOf(pointTwo);
        try {
            double dblOne = Double.parseDouble(stringOne);
            double dblTwo = Double.parseDouble(stringTwo);

            GeometryFactory geomFactory = new GeometryFactory();
            Coordinate[] points = new Coordinate[2];
            points[0] = new Coordinate(dblOne, Coordinate.NULL_ORDINATE, Coordinate.NULL_ORDINATE);
            points[1] = new Coordinate(dblTwo, Coordinate.NULL_ORDINATE, Coordinate.NULL_ORDINATE);

            return (T) geomFactory.createLineString(points);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Error converting the parameters for toLineString function: "
                            + parameters.toString() + ". " + USAGE, e);
        }
    }

}
