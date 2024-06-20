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
import java.util.List;
import org.geotools.api.filter.capability.FunctionName;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.ExpressionVisitor;
import org.geotools.api.filter.expression.Function;
import org.geotools.api.filter.expression.Literal;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.filter.capability.FunctionNameImpl;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultVerticalCRS;
import org.geotools.referencing.cs.DefaultVerticalCS;
import org.geotools.referencing.datum.DefaultVerticalDatum;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;

/**
 * This function converts double values to 1D LineString geometry object. This is needed when the
 * data store doesn't have geometry type columns (or they want to use custom CRS). If custom SRS
 * name is used, a 1D CRS will be created. This function expects:
 *
 * <ol>
 *   <li>Expression: SRS name (could be custom SRS name)
 *   <li>Expression: name of column pointing to first double value
 *   <li>Expression: name of column pointing to second double value
 * </ol>
 *
 * @author Rini Angreani (CSIRO Earth Science and Resource Engineering)
 */
public class ToLineStringFunction implements Function {

    private final List<Expression> parameters;

    private final Literal fallback;

    private static final String USAGE = "Usage: toLineString(srsName, point 1, point 2)";

    public static final FunctionName NAME =
            new FunctionNameImpl(
                    "toLineString",
                    FunctionNameImpl.parameter("return", LineString.class),
                    FunctionNameImpl.parameter("parameter", String.class, 1, 1),
                    FunctionNameImpl.parameter("parameter", Double.class, 2, 3));

    public ToLineStringFunction() {
        this(new ArrayList<>(), null);
    }

    public ToLineStringFunction(List<Expression> parameters, Literal fallback) {
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
        return evaluate(object, LineString.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T evaluate(Object object, Class<T> context) {
        if (parameters.size() != 3
                || parameters.get(0) == null
                || parameters.get(1) == null
                || parameters.get(2) == null) {
            throw new IllegalArgumentException(
                    "Invalid parameters for toLineString function: "
                            + parameters.toString()
                            + ". "
                            + USAGE);
        }
        Object srs = parameters.get(0).evaluate(object, String.class);
        String srsName = String.valueOf(srs);
        CoordinateReferenceSystem crs = null;
        try {
            crs = CRS.decode(srsName);
        } catch (FactoryException e) {
            // custom CRS
            crs =
                    new DefaultVerticalCRS(
                            srsName, DefaultVerticalDatum.GEOIDAL, DefaultVerticalCS.DEPTH);
        }

        // just in case
        if (crs == null) {
            crs =
                    new DefaultVerticalCRS(
                            srsName, DefaultVerticalDatum.GEOIDAL, DefaultVerticalCS.DEPTH);
        }

        LineString linestring = null;

        // safe parsing and conversion for invalids
        Object pointOne = parameters.get(1).evaluate(object, String.class);
        Object pointTwo = parameters.get(2).evaluate(object, String.class);
        String stringOne = String.valueOf(pointOne);
        String stringTwo = String.valueOf(pointTwo);
        try {
            double dblOne = Double.parseDouble(stringOne);
            double dblTwo = Double.parseDouble(stringTwo);

            GeometryFactory geomFactory = new GeometryFactory();
            Coordinate[] points = new Coordinate[2];
            points[0] = new Coordinate(dblOne, Coordinate.NULL_ORDINATE, Coordinate.NULL_ORDINATE);
            points[1] = new Coordinate(dblTwo, Coordinate.NULL_ORDINATE, Coordinate.NULL_ORDINATE);

            linestring = geomFactory.createLineString(points);
            JTS.setCRS(linestring, crs);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Error converting the parameters for toLineString function: "
                            + parameters.toString()
                            + ". "
                            + USAGE,
                    e);
        }
        return (T) linestring;
    }
}
