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
import java.util.List;
import java.util.Optional;
import org.geotools.data.postgis.filter.FilterFunction_pgNearest;
import org.geotools.filter.FilterCapabilities;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.PreparedFilterToSQL;
import org.geotools.jdbc.PrimaryKeyColumn;
import org.geotools.util.factory.Hints;
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

public class PostgisPSFilterToSql extends PreparedFilterToSQL {

    FilterToSqlHelper helper;
    boolean functionEncodingEnabled;

    public PostgisPSFilterToSql(PostGISPSDialect dialect) {
        super(dialect);
        helper = new FilterToSqlHelper(this);
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
    public double getDistanceInMeters(DistanceBufferOperator operator) {
        return super.getDistanceInMeters(operator);
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
     * @param extraData
     * @throws RuntimeException
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
        Optional<FilterFunction_pgNearest> nearestOpt = getNearestFilter(filter);
        if (nearestOpt.isPresent()) {
            return visit(nearestOpt.get(), extraData);
        } else {
            return super.visit(filter, extraData);
        }
    }

    /**
     * Detects and return a FilterFunction_pgNearest if found, otherwise an empty optional
     *
     * @param filter filter to evaluate
     * @return optional of FilterFunction_pgNearest if found
     */
    private Optional<FilterFunction_pgNearest> getNearestFilter(PropertyIsEqualTo filter) {
        Expression expr1 = filter.getExpression1();
        Expression expr2 = filter.getExpression2();
        // if expr2 is nearest filter, switch positions
        if (expr2 instanceof FilterFunction_pgNearest) {
            Expression tmp = expr1;
            expr1 = expr2;
            expr2 = tmp;
        }
        if (expr1 instanceof FilterFunction_pgNearest) {
            if (!(expr2 instanceof Literal)) {
                throw new UnsupportedOperationException(
                        "Unsupported usage of Nearest Operator: it can be compared only to a Boolean \"true\" value");
            }
            Boolean nearest = (Boolean) evaluateLiteral((Literal) expr2, Boolean.class);
            if (nearest == null || !nearest.booleanValue()) {
                throw new UnsupportedOperationException(
                        "Unsupported usage of Nearest Operator: it can be compared only to a Boolean \"true\" value");
            }
            return Optional.of((FilterFunction_pgNearest) expr1);
        } else {
            return Optional.empty();
        }
    }

    private Object visit(FilterFunction_pgNearest filter, Object extraData) {
        Expression geometryExp = getParameter(filter, 0, true);
        Expression numNearest = getParameter(filter, 1, true);
        try {
            List<PrimaryKeyColumn> pkColumns = getPrimaryKey().getColumns();
            if (pkColumns == null || pkColumns.size() == 0) {
                throw new UnsupportedOperationException(
                        "Unsupported usage of Postgis Nearest Operator: table with no primary key");
            }

            String pkColumnsAsString = getPrimaryKeyColumnsAsCommaSeparatedList(pkColumns);
            StringBuffer sb = new StringBuffer();
            sb.append(" (")
                    .append(pkColumnsAsString)
                    .append(")")
                    .append(" in (select ")
                    .append(pkColumnsAsString)
                    .append(" from ");
            if (getDatabaseSchema() != null) {
                dialect.encodeSchemaName(getDatabaseSchema(), sb);
                sb.append(".");
            }
            dialect.encodeTableName(getPrimaryKey().getTableName(), sb);
            sb.append(" order by ");
            // geometry column name
            dialect.encodeColumnName(null, featureType.getGeometryDescriptor().getLocalName(), sb);
            sb.append(" <-> ");
            // reference geometry
            Geometry geomValue = (Geometry) evaluateLiteral((Literal) geometryExp, Geometry.class);
            bufferGeom(geomValue, sb);
            // num of features
            sb.append(" limit ");
            int numFeatures = numNearest.evaluate(null, Number.class).intValue();
            sb.append(numFeatures);
            sb.append(")");

            out.write(sb.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return extraData;
    }

    private String getPrimaryKeyColumnsAsCommaSeparatedList(List<PrimaryKeyColumn> pkColumns) {
        StringBuffer sb = new StringBuffer();
        boolean first = true;
        for (PrimaryKeyColumn c : pkColumns) {
            if (first) {
                first = false;
            } else {
                sb.append(",");
            }
            dialect.encodeColumnName(c.getName(), sb);
        }
        return sb.toString();
    }

    private Integer getFeatureTypeGeometrySRID() {
        return (Integer)
                featureType
                        .getGeometryDescriptor()
                        .getUserData()
                        .get(JDBCDataStore.JDBC_NATIVE_SRID);
    }

    private Integer getFeatureTypeGeometryDimension() {
        GeometryDescriptor descriptor = featureType.getGeometryDescriptor();
        return (Integer) descriptor.getUserData().get(Hints.COORDINATE_DIMENSION);
    }

    private void bufferGeom(Geometry geom, StringBuffer sb) {
        if (geom instanceof LinearRing) {
            // postgis does not handle linear rings, convert to just a line string
            geom = geom.getFactory().createLineString(((LinearRing) geom).getCoordinateSequence());
        }

        Object typename =
                featureType
                        .getGeometryDescriptor()
                        .getUserData()
                        .get(JDBCDataStore.JDBC_NATIVE_TYPENAME);
        if ("geography".equals(typename)) {
            sb.append("ST_GeogFromText('");
            sb.append(geom.toText());
            sb.append("')");
        } else {
            sb.append("ST_GeomFromText('");
            sb.append(geom.toText());
            sb.append("', " + getFeatureTypeGeometrySRID() + ")");
        }
    }
}
