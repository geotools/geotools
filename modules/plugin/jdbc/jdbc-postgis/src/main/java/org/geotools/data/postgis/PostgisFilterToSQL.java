/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2016, Open Source Geospatial Foundation (OSGeo)
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
import java.util.Date;
import org.geotools.data.jdbc.FilterToSQL;
import org.geotools.data.postgis.filter.FilterFunction_pgNearest;
import org.geotools.filter.FilterCapabilities;
import org.geotools.filter.function.InArrayFunction;
import org.geotools.jdbc.JDBCDataStore;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LinearRing;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.filter.BinaryComparisonOperator;
import org.opengis.filter.PropertyIsBetween;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.spatial.BinarySpatialOperator;
import org.opengis.filter.spatial.DistanceBufferOperator;

public class PostgisFilterToSQL extends FilterToSQL {

    FilterToSqlHelper helper;
    private boolean functionEncodingEnabled;
    protected PostGISDialect pgDialect;

    public PostgisFilterToSQL(PostGISDialect dialect) {
        helper = new FilterToSqlHelper(this);
        pgDialect = dialect;
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
    protected void visitLiteralGeometry(Literal expression) throws IOException {
        // evaluate the literal and store it for later
        Geometry geom = (Geometry) evaluateLiteral(expression, Geometry.class);

        if (geom instanceof LinearRing) {
            // postgis does not handle linear rings, convert to just a line string
            geom = geom.getFactory().createLineString(((LinearRing) geom).getCoordinateSequence());
        }

        Object typename =
                currentGeometry != null
                        ? currentGeometry.getUserData().get(JDBCDataStore.JDBC_NATIVE_TYPENAME)
                        : null;
        if ("geography".equals(typename)) {
            out.write("ST_GeogFromText('");
            out.write(geom.toText());
            out.write("')");
        } else {
            out.write("ST_GeomFromText('");
            out.write(geom.toText());
            if (currentSRID == null && currentGeometry != null) {
                // if we don't know at all, use the srid of the geometry we're comparing against
                // (much slower since that has to be extracted record by record as opposed to
                // being a constant)
                out.write("', ST_SRID(" + escapeName(currentGeometry.getLocalName()) + "))");
            } else {
                out.write("', " + currentSRID + ")");
            }
        }
    }

    @Override
    protected FilterCapabilities createFilterCapabilities() {
        return helper.createFilterCapabilities(functionEncodingEnabled);
    }

    protected Object visitBinarySpatialOperator(
            BinarySpatialOperator filter,
            PropertyName property,
            Literal geometry,
            boolean swapped,
            Object extraData) {
        helper.out = out;
        return helper.visitBinarySpatialOperator(filter, property, geometry, swapped, extraData);
    }

    protected Object visitBinarySpatialOperator(
            BinarySpatialOperator filter, Expression e1, Expression e2, Object extraData) {
        helper.out = out;
        return helper.visitBinarySpatialOperator(filter, e1, e2, extraData);
    }

    GeometryDescriptor getCurrentGeometry() {
        return currentGeometry;
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

    @Override
    public Object visit(Literal literal, Object extraData) {
        // handle BigDate udt, encode it as a long
        if (extraData instanceof Class && BigDate.class.isAssignableFrom((Class<?>) extraData)) {
            if (literal.getValue() instanceof Date) {
                return super.visit(
                        filterFactory.literal(((Date) literal.getValue()).getTime()), Long.class);
            }
        }
        return super.visit(literal, extraData);
    }

    @Override
    protected String getFunctionName(Function function) {
        return helper.getFunctionName(function);
    }

    @Override
    protected String cast(String encodedProperty, Class target) throws IOException {
        return helper.cast(encodedProperty, target);
    }

    public void setFunctionEncodingEnabled(boolean functionEncodingEnabled) {
        this.functionEncodingEnabled = functionEncodingEnabled;
    }

    @Override
    public double getDistanceInMeters(DistanceBufferOperator operator) {
        return super.getDistanceInMeters(operator);
    }

    @Override
    public double getDistanceInNativeUnits(DistanceBufferOperator operator) {
        return super.getDistanceInNativeUnits(operator);
    }

    /**
     * Overrides base behavior to handler arrays
     *
     * @param filter the comparison to be turned into SQL.
     */
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

    public Object visit(PropertyIsEqualTo filter, Object extraData) {
        helper.out = out;
        FilterFunction_pgNearest nearest = helper.getNearestFilter(filter);
        InArrayFunction inArray = helper.getInArray(filter);
        if (nearest != null) {
            return helper.visit(
                    nearest,
                    extraData,
                    new FilterToSqlHelper.NearestHelperContext(
                            pgDialect,
                            (a, b) -> {
                                try {
                                    pgDialect.encodeGeometryValue(
                                            a,
                                            helper.getFeatureTypeGeometryDimension(),
                                            helper.getFeatureTypeGeometrySRID(),
                                            b);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }));
        } else if (inArray != null) {
            return helper.visit(inArray, extraData);
        } else {
            return super.visit(filter, extraData);
        }
    }
}
