/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2018, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.postgis;

import java.io.IOException;
import org.geotools.api.feature.type.GeometryDescriptor;
import org.geotools.api.filter.BinaryComparisonOperator;
import org.geotools.api.filter.PropertyIsBetween;
import org.geotools.api.filter.PropertyIsEqualTo;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.Function;
import org.geotools.api.filter.expression.Literal;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.api.filter.spatial.BinarySpatialOperator;
import org.geotools.api.filter.spatial.DistanceBufferOperator;
import org.geotools.filter.FilterCapabilities;
import org.geotools.jdbc.PreparedFilterToSQL;
import org.geotools.util.Version;

public class PostgisPSFilterToSql extends PreparedFilterToSQL {

    FilterToSqlHelper helper;
    boolean functionEncodingEnabled;

    public PostgisPSFilterToSql(PostGISPSDialect dialect) {
        super(dialect);
        helper = new FilterToSqlHelper(this);
    }

    public PostgisPSFilterToSql(PostGISPSDialect dialect, Version pgVersion) {
        super(dialect);
        helper = new FilterToSqlHelper(this, pgVersion);
    }

    public boolean isLooseBBOXEnabled() {
        return helper.looseBBOXEnabled;
    }

    public void setLooseBBOXEnabled(boolean looseBBOXEnabled) {
        helper.looseBBOXEnabled = looseBBOXEnabled;
    }

    public boolean isEncodeBBOXFilterAsEnvelope(boolean encodeBBOXFilterAsEnvelope) {
        return helper.encodeBBOXFilterAsEnvelope;
    }

    public void setEncodeBBOXFilterAsEnvelope(boolean encodeBBOXFilterAsEnvelope) {
        helper.encodeBBOXFilterAsEnvelope = encodeBBOXFilterAsEnvelope;
    }

    @Override
    protected FilterCapabilities createFilterCapabilities() {
        return helper.createFilterCapabilities(functionEncodingEnabled);
    }

    @Override
    protected Object visitBinarySpatialOperator(
            BinarySpatialOperator filter,
            PropertyName property,
            Literal geometry,
            boolean swapped,
            Object extraData) {
        helper.out = out;
        return helper.visitBinarySpatialOperator(filter, property, geometry, swapped, extraData);
    }

    @Override
    protected Object visitBinarySpatialOperator(
            BinarySpatialOperator filter, Expression e1, Expression e2, Object extraData) {
        helper.out = out;
        return helper.visitBinarySpatialOperator(filter, e1, e2, extraData);
    }

    GeometryDescriptor getCurrentGeometry() {
        return currentGeometry;
    }

    public void setFunctionEncodingEnabled(boolean functionEncodingEnabled) {
        this.functionEncodingEnabled = functionEncodingEnabled;
    }

    @Override
    protected String getFunctionName(Function function) {
        return helper.getFunctionName(function);
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

    /**
     * Overrides base behavior to handler arrays
     *
     * @param filter the comparison to be turned into SQL.
     */
    @Override
    protected void visitBinaryComparisonOperator(BinaryComparisonOperator filter, Object extraData)
            throws RuntimeException {
        Expression left = filter.getExpression1();
        Expression right = filter.getExpression2();
        Class rightContext = super.getExpressionType(left);
        Class leftContext = super.getExpressionType(right);

        // array comparison in PostgreSQL is strict, need to know the base type, that info is
        // available only in the property name userdata
        String type = (String) extraData;
        if ((helper.isArray(rightContext) || helper.isArray(leftContext))
                && (left instanceof PropertyName || right instanceof PropertyName)) {
            helper.out = out;
            helper.visitArrayComparison(filter, left, right, rightContext, leftContext, type);
        } else {
            super.visitBinaryComparisonOperator(filter, extraData);
        }
    }

    /**
     * Writes the SQL for the PropertyIsBetween Filter.
     *
     * @param filter the Filter to be visited.
     * @throws RuntimeException for io exception with writer
     */
    @Override
    public Object visit(PropertyIsBetween filter, Object extraData) throws RuntimeException {
        LOGGER.finer("exporting PropertyIsBetween");

        Expression expr = filter.getExpression();
        Class context = super.getExpressionType(expr);
        if (helper.isArray(context)) {
            helper.out = out;
            helper.visitArrayBetween(filter, context.getComponentType(), extraData);
            return extraData;
        } else {
            return super.visit(filter, extraData);
        }
    }

    @Override
    public Object visit(PropertyIsEqualTo filter, Object extraData) {
        helper.out = out;
        if (helper.isSupportedEqualFunction(filter)) {
            return helper.visitSupportedEqualFunction(
                    filter,
                    dialect,
                    (a, b) -> {
                        try {
                            ((PostGISPSDialect) dialect)
                                    .encodeGeometryValue(
                                            a,
                                            helper.getFeatureTypeGeometryDimension(),
                                            helper.getFeatureTypeGeometrySRID(),
                                            b);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    },
                    extraData);
        }
        return super.visit(filter, extraData);
    }
}
