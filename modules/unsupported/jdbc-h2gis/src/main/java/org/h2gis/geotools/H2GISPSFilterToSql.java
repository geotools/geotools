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
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.Function;
import org.geotools.api.filter.expression.Literal;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.api.filter.spatial.BinarySpatialOperator;
import org.geotools.api.filter.spatial.DistanceBufferOperator;
import org.geotools.filter.FilterCapabilities;
import org.geotools.jdbc.PreparedFilterToSQL;

/**
 * jdbc-h2gis is an extension to connect H2GIS a spatial library that brings spatial support to the H2 Java database.
 *
 * <p>H2GIS dialect filter based on prepared statements.
 *
 * @author Erwan Bocher
 */
public class H2GISPSFilterToSql extends PreparedFilterToSQL {

    private final H2GISFilterToSQLHelper helper;
    private boolean functionEncodingEnabled;

    public H2GISPSFilterToSql(H2GISPSDialect dialect) {
        super(dialect);
        helper = new H2GISFilterToSQLHelper(this);
    }

    @Override
    protected FilterCapabilities createFilterCapabilities() {
        return H2GISFilterToSQLHelper.createFilterCapabilities(
                functionEncodingEnabled, super.createFilterCapabilities());
    }

    @Override
    protected Object visitBinarySpatialOperator(
            BinarySpatialOperator filter, PropertyName property, Literal geometry, boolean swapped, Object extraData) {
        helper.out = out;
        return helper.visitBinarySpatialOperator(filter, property, geometry, swapped, extraData);
    }

    @Override
    protected Object visitBinarySpatialOperator(
            BinarySpatialOperator filter, Expression e1, Expression e2, Object extraData) {
        helper.out = out;
        return helper.visitBinarySpatialOperator(filter, e1, e2, extraData);
    }

    public void setFunctionEncodingEnabled(boolean functionEncodingEnabled) {
        this.functionEncodingEnabled = functionEncodingEnabled;
    }

    @Override
    protected String getFunctionName(Function function) {
        return H2GISFilterToSQLHelper.getFunctionName(function);
    }

    @Override
    public double getDistanceInNativeUnits(DistanceBufferOperator operator) {
        return super.getDistanceInNativeUnits(operator);
    }

    @Override
    public Object visit(Function function, Object extraData) throws RuntimeException {
        helper.out = out;
        try {
            encodingFunction = true;
            boolean encoded = helper.visitFunction(function, extraData);
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
}
