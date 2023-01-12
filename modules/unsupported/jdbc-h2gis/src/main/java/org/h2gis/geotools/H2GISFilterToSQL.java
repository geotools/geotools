/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2023, Open Source Geospatial Foundation (OSGeo)
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
 */
package org.h2gis.geotools;

import java.io.IOException;
import org.geotools.data.jdbc.FilterToSQL;
import org.geotools.filter.FilterCapabilities;
import org.locationtech.jts.geom.Geometry;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.spatial.BinarySpatialOperator;

/**
 * jdbc-h2gis is an extension to connect H2GIS a spatial library that brings spatial support to the
 * H2 Java database.
 *
 * <p>H2GIS filter wrapper for H2GIS database.
 *
 * @author Erwan Bocher
 */
public class H2GISFilterToSQL extends FilterToSQL {

    H2GISFilterToSQLHelper h2GISFilterToSQLHelper;
    private boolean functionEncodingEnabled;

    public H2GISFilterToSQL() {
        h2GISFilterToSQLHelper = new H2GISFilterToSQLHelper(this);
    }

    @Override
    protected void visitLiteralGeometry(Literal expression) throws IOException {
        // evaluate the literal and store it for later
        Geometry geom = (Geometry) evaluateLiteral(expression, Geometry.class);
        out.write("ST_GeomFromText('");
        out.write(geom.toText());
        if (currentSRID == null && currentGeometry != null) {
            // if we don't know at all, use the srid of the geometry we're comparing against
            // (much slower since that has to be extracted record by record as opposed to
            // being a constant)
            out.write("', ST_SRID(\"" + currentGeometry.getLocalName() + "\"))");
        } else {
            out.write("', " + currentSRID + ")");
        }
    }

    @Override
    protected FilterCapabilities createFilterCapabilities() {
        return H2GISFilterToSQLHelper.createFilterCapabilities(
                functionEncodingEnabled, super.createFilterCapabilities());
    }

    @Override
    protected Object visitBinarySpatialOperator(
            BinarySpatialOperator filter,
            PropertyName property,
            Literal geometry,
            boolean swapped,
            Object extraData) {
        h2GISFilterToSQLHelper.out = out;
        return h2GISFilterToSQLHelper.visitBinarySpatialOperator(
                filter, property, geometry, swapped, extraData);
    }

    @Override
    protected Object visitBinarySpatialOperator(
            BinarySpatialOperator filter, Expression e1, Expression e2, Object extraData) {
        h2GISFilterToSQLHelper.out = out;
        return h2GISFilterToSQLHelper.visitBinarySpatialOperator(filter, e1, e2, extraData);
    }

    @Override
    public Object visit(Function function, Object extraData) throws RuntimeException {
        h2GISFilterToSQLHelper.out = out;
        try {
            encodingFunction = true;
            boolean encoded = h2GISFilterToSQLHelper.visitFunction(function, extraData);
            encodingFunction = false;
            if (encoded) {
                return extraData;
            } else {
                return super.visit(function, extraData);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected String getFunctionName(Function function) {
        return H2GISFilterToSQLHelper.getFunctionName(function);
    }

    @Override
    protected String cast(String encodedProperty, Class target) {
        return h2GISFilterToSQLHelper.cast(encodedProperty, target);
    }

    /** @param functionEncodingEnabled */
    public void setFunctionEncodingEnabled(boolean functionEncodingEnabled) {
        this.functionEncodingEnabled = functionEncodingEnabled;
    }
}
