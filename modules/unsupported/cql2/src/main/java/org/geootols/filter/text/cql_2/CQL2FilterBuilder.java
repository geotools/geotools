/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geootols.filter.text.cql_2;

import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import org.geotools.filter.text.commons.AbstractFilterBuilder;
import org.geotools.filter.text.commons.BuildResultStack;
import org.geotools.filter.text.commons.IToken;
import org.geotools.filter.text.commons.PeriodNode;
import org.geotools.filter.text.commons.Result;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.temporal.object.DefaultInstant;
import org.geotools.temporal.object.DefaultPeriod;
import org.geotools.temporal.object.DefaultPosition;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.Or;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.spatial.BBOX;
import org.opengis.filter.spatial.BinarySpatialOperator;
import org.opengis.filter.temporal.After;
import org.opengis.filter.temporal.Before;
import org.opengis.filter.temporal.TEquals;
import org.opengis.temporal.Period;
import org.opengis.temporal.Position;

/** Similar to ECQLFilterBuilder, but needs to handle the date literals differently */
final class CQL2FilterBuilder extends AbstractFilterBuilder {

    public CQL2FilterBuilder(String cql2Source, FilterFactory filterFactory) {
        super(cql2Source, filterFactory);
    }

    /**
     * Builds a negative Number
     *
     * @return Negative number
     */
    public Literal bulidNegativeNumber() throws CQLException {

        // retrieves the number value from stack and adds the (-) minus
        Literal literal = getResultStack().popLiteral();
        String strNumber = "-" + literal.getValue();
        Object value = literal.getValue();

        // builds the negative number
        @SuppressWarnings("unused")
        Number number = null;
        if (value instanceof Double) {
            number = Double.parseDouble(strNumber);
        } else if (value instanceof Float) {
            number = Float.parseFloat(strNumber);
        } else if (value instanceof Integer) {
            number = Integer.parseInt(strNumber);
        } else if (value instanceof Long) {
            number = Long.parseLong(strNumber);
        } else {
            assert false : "Number instnce is expected";
        }
        Literal signedNumber = getFilterFactory().literal(number);

        return signedNumber;
    }

    /**
     * builds the or filter for the in predicate. The method retrieves the list of expressions and
     * the property name from stack to make the Or filter.
     *
     * <pre>
     * Thus if the stack have the following predicate
     * propName in (expr1, expr2)
     * this method will produce:
     * (propName = expr1) or (propName = expr2)
     * </pre>
     */
    public Or buildInPredicate(final int nodeExpression) throws CQLException {
        // retrieves the expressions from stack
        List<Expression> exprList = new LinkedList<>();
        while (!getResultStack().empty()) {

            Result result = getResultStack().peek();

            int node = result.getNodeType();
            if (node != nodeExpression) {
                break;
            }
            getResultStack().popResult();

            Expression expr = getResultStack().popExpression();
            exprList.add(expr);
        }

        assert !exprList.isEmpty() : "must have one or more expressions";

        // retrieve the left hand expression from the stack
        final Expression leftHandExpr = getResultStack().popExpression();

        // makes one comparison for each expression in the expression list,
        // associated by the Or filter.
        List<Filter> filterList = new LinkedList<>();
        for (Expression expression : exprList) {
            PropertyIsEqualTo eq = getFilterFactory().equals(leftHandExpr, expression);
            filterList.add(eq);
        }
        Collections.reverse(filterList);
        Or orFilter = getFilterFactory().or(filterList);

        return orFilter;
    }

    public Coordinate buildCoordinate() throws CQLException {

        double y = getResultStack().popDoubleValue();
        double x = getResultStack().popDoubleValue();

        Coordinate coordinate = new Coordinate(x, y);

        return coordinate;
    }

    public Point buildPointText() throws CQLException {

        PointBuilder builder = new PointBuilder(getStatement(), getResultStack());

        Point point = (Point) builder.build();

        return point;
    }

    public LineString buildLineString(final int pointNode) throws CQLException {

        LineStringBuilder builder = new LineStringBuilder(getStatement(), getResultStack());

        LineString line = (LineString) builder.build(pointNode);

        return line;
    }

    public Polygon buildPolygon(final int linestringNode) throws CQLException {

        PolygonBuilder builder = new PolygonBuilder(getStatement(), getResultStack());

        Polygon polygon = (Polygon) builder.build(linestringNode);

        return polygon;
    }

    /**
     * Retrieves all points built in previous parsing process from stack and creates the multipoint
     * geometry.
     *
     * @return a MultiPoint
     */
    public MultiPoint buildMultiPoint(int pointNode) throws CQLException {

        MultiPointBuilder builder = new MultiPointBuilder(getStatement(), getResultStack());

        MultiPoint mp = (MultiPoint) builder.build(pointNode);

        return mp;
    }

    /**
     * Retrieves all linestring built from stack and creates the multilinestring geometry
     *
     * @return a MultiLineString
     * @throws CQLException ¡
     */
    public MultiLineString buildMultiLineString(final int linestringtextNode) throws CQLException {

        MultiLineStringBuilder builder =
                new MultiLineStringBuilder(getStatement(), getResultStack());

        MultiLineString ml = (MultiLineString) builder.build(linestringtextNode);

        return ml;
    }

    /**
     * Builds a {@link MuliPolygon} using the {@link Polygon} staked in the parsing process
     *
     * @param polygontextNode .
     * @return MultiPolygon
     */
    public MultiPolygon buildMultiPolygon(final int polygontextNode) throws CQLException {

        MultiPolygonBuilder builder = new MultiPolygonBuilder(getStatement(), getResultStack());

        MultiPolygon mp = (MultiPolygon) builder.build(polygontextNode);

        return mp;
    }

    /**
     * Builds a {@link GeometryCollection}
     *
     * @return GeometryCollection
     */
    public GeometryCollection buildGeometryCollection(final int jjtgeometryliteral)
            throws CQLException {

        GeometryCollectionBuilder builder =
                new GeometryCollectionBuilder(getStatement(), getResultStack());

        GeometryCollection gc = (GeometryCollection) builder.build(jjtgeometryliteral);

        return gc;
    }

    /**
     * Builds literal geometry
     *
     * @return a Literal Geometry
     */
    public Literal buildGeometry() throws CQLException {

        Geometry geometry = getResultStack().popGeometry();

        Literal literal = getFilterFactory().literal(geometry);

        return literal;
    }

    public Literal buildGeometryLiteral() throws CQLException {
        // skip the container node
        Result result = getResultStack().popResult();
        return (Literal) result.getBuilt();
    }

    public Literal buildSimpleGeometryLiteral() throws CQLException {
        return getResultStack().popLiteral();
    }

    @Override
    public BinarySpatialOperator buildSpatialEqualFilter() throws CQLException {

        SpatialOperationBuilder builder =
                new SpatialOperationBuilder(getResultStack(), getFilterFactory());
        BinarySpatialOperator filter = builder.buildEquals();

        return filter;
    }

    @Override
    public BinarySpatialOperator buildSpatialDisjointFilter() throws CQLException {
        SpatialOperationBuilder builder =
                new SpatialOperationBuilder(getResultStack(), getFilterFactory());

        BinarySpatialOperator filter = builder.buildDisjoint();

        return filter;
    }

    @Override
    public BinarySpatialOperator buildSpatialIntersectsFilter() throws CQLException {

        SpatialOperationBuilder builder =
                new SpatialOperationBuilder(getResultStack(), getFilterFactory());

        BinarySpatialOperator filter = builder.buildIntersects();

        return filter;
    }

    @Override
    public BinarySpatialOperator buildSpatialTouchesFilter() throws CQLException {

        SpatialOperationBuilder builder =
                new SpatialOperationBuilder(getResultStack(), getFilterFactory());

        BinarySpatialOperator filter = builder.buildTouches();

        return filter;
    }

    @Override
    public BinarySpatialOperator buildSpatialCrossesFilter() throws CQLException {

        SpatialOperationBuilder builder =
                new SpatialOperationBuilder(getResultStack(), getFilterFactory());

        BinarySpatialOperator filter = builder.buildCrosses();

        return filter;
    }

    /**
     * Checks the correctness of pattern and makes a literal with this pattern;
     *
     * @return a Literal with the pattern
     * @throws CQLException if the pattern has not one of the following characters:T,F,*,0,1,2
     */
    public Literal buildPattern9IM() throws CQLException {

        // retrieves the pattern from stack
        Result resut = getResultStack().popResult();
        IToken token = resut.getToken();

        Literal built = (Literal) resut.getBuilt();
        final String pattern = (String) built.getValue();

        // validates the length
        if (pattern.length() != 9) {
            throw new CQLException(
                    "the pattern DE-9IM must have nine (9) characters", token, getStatement());
        }

        // validates that the pattern has only the characters T,F,*,0,1,2
        String patternUC = pattern.toUpperCase();

        char[] validFlags = {'T', 'F', '*', '0', '1', '2'};
        for (int i = 0; i < validFlags.length; i++) {
            char character = patternUC.charAt(i);

            boolean found = false;
            for (char validFlag : validFlags) {
                if (validFlag == character) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                throw new CQLException(
                        "the pattern DE-9IM must have only the following characters: T, F, *, 0, 1, 2",
                        token,
                        getStatement());
            }
        }

        Literal patternExpr = getFilterFactory().literal(pattern);

        return patternExpr;
    }

    @Override
    public BinarySpatialOperator buildSpatialWithinFilter() throws CQLException {

        SpatialOperationBuilder builder =
                new SpatialOperationBuilder(getResultStack(), getFilterFactory());

        BinarySpatialOperator filter = builder.buildWithin();

        return filter;
    }

    @Override
    public BinarySpatialOperator buildSpatialContainsFilter() throws CQLException {

        SpatialOperationBuilder builder =
                new SpatialOperationBuilder(getResultStack(), getFilterFactory());

        BinarySpatialOperator filter = builder.buildContains();

        return filter;
    }

    @Override
    public BinarySpatialOperator buildSpatialOverlapsFilter() throws CQLException {

        SpatialOperationBuilder builder =
                new SpatialOperationBuilder(getResultStack(), getFilterFactory());

        BinarySpatialOperator filter = builder.buildOverlaps();

        return filter;
    }

    @Override
    public BBOX buildBBox() throws CQLException {

        SpatialOperationBuilder builder =
                new SpatialOperationBuilder(getResultStack(), getFilterFactory());

        BBOX filter = builder.buildBBox();

        return filter;
    }

    @Override
    public BBOX buildBBoxWithCRS() throws CQLException {

        SpatialOperationBuilder builder =
                new SpatialOperationBuilder(getResultStack(), getFilterFactory());

        BBOX filter = builder.buildBBoxWithCRS();

        return filter;
    }

    @Override
    public Literal buildDateExpression(final IToken token) throws CQLException {
        String date = unwrapDateFunction(token);
        return asLiteralDate(date);
    }

    private String unwrapDateFunction(IToken token) {
        String full = token.toString();
        int from = full.indexOf('\'');
        int to = full.lastIndexOf('\'');
        String date = full.substring(from + 1, to);
        return date;
    }

    @Override
    public Before buildBeforePeriod() throws CQLException {
        return super.buildBeforePeriod();
    }

    @Override
    public Literal buildDateTimeExpression(IToken token) throws CQLException {
        return asLiteralDateTime(unwrapDateFunction(token));
    }

    private Expression popTimeExpression() throws CQLException {
        Result result = getResultStack().popResult();
        Object built = result.getBuilt();
        if (built instanceof PeriodNode) {
            PeriodNode pn = (PeriodNode) built;
            Position start = new DefaultPosition(pn.getBeginning().evaluate(null, Date.class));
            Position end = new DefaultPosition(pn.getEnding().evaluate(null, Date.class));
            return filterFactory.literal(
                    new DefaultPeriod(new DefaultInstant(start), new DefaultInstant(end)));
        }
        return (Expression) built;
    }

    @Override
    public Before buildBeforeDate() throws CQLException {
        Expression ex2 = popTimeExpression();
        Expression ex1 = popTimeExpression();

        // should really go in like this, but it's a code path we have not experimented before,
        // relative to native encoding and in memory execution.
        // build a simpler expression in case ex1 is an attribute and ex2 a period
        if (ex1 instanceof PropertyName && ex2 instanceof Literal) {
            Object value = ex2.evaluate(null);
            if (value instanceof Period) {
                return filterFactory.before(
                        ex1, filterFactory.literal(getBeginDate((Period) value)));
            }
        }
        return filterFactory.before(ex1, ex2);
    }

    private Date getBeginDate(Period value) {
        return value.getBeginning().getPosition().getDate();
    }

    private Date getEndDate(Period value) {
        return value.getEnding().getPosition().getDate();
    }

    @Override
    public After buildAfterDate() throws CQLException {
        Expression ex2 = popTimeExpression();
        Expression ex1 = popTimeExpression();

        // same reasoning as in buildBeforeDate
        if (ex1 instanceof PropertyName && ex2 instanceof Literal) {
            Object value = ex2.evaluate(null);
            if (value instanceof Period) {
                return filterFactory.after(ex1, filterFactory.literal(getEndDate((Period) value)));
            }
        }

        return filterFactory.after(ex1, ex2);
    }

    @Override
    public TEquals buildTEquals() throws CQLException {
        Expression right = popTimeExpression();
        Expression left = popTimeExpression();

        return this.filterFactory.tequals(left, right);
    }

    /**
     * Returns the Envelope. Uses CQL2 axis order.
     *
     * @return Literal
     */
    @Override
    public Literal buildEnvelope(IToken token) throws CQLException {
        String source = scanExpression(token);

        final String ENVELOPE_TYPE = "ENVELOPE";

        int cur = source.indexOf(ENVELOPE_TYPE);

        // transforms CQL envelop envelop(West,East,North,South) to
        // GS84 West=minX, East=maxX, North=maxY, South=minY

        cur = cur + ENVELOPE_TYPE.length() + 1;

        String argument = source.substring(cur, source.length() - 1);

        final String comma = ",";
        cur = 0;

        int end = argument.indexOf(comma, cur);
        String west = argument.substring(cur, end);
        double minX = Double.parseDouble(west);

        cur = end + 1;
        end = argument.indexOf(comma, cur);

        String south = argument.substring(cur, end);
        double minY = Double.parseDouble(south);

        cur = end + 1;
        end = argument.indexOf(comma, cur);

        String east = argument.substring(cur, end);
        double maxX = Double.parseDouble(east);

        cur = end + 1;

        String north = argument.substring(cur);
        double maxY = Double.parseDouble(north);

        // ReferencedEnvelope envelope = new
        // ReferencedEnvelope(DefaultGeographicCRS.WGS84);
        // envelope.init(minX, minY, maxX, maxY);
        GeometryFactory gf = new GeometryFactory();

        Coordinate[] coords = {
            new Coordinate(minX, minY),
            new Coordinate(minX, maxY),
            new Coordinate(maxX, maxY),
            new Coordinate(maxX, minY),
            new Coordinate(minX, minY)
        };
        LinearRing shell = gf.createLinearRing(coords);
        Polygon bbox = gf.createPolygon(shell, null);
        bbox.setUserData(DefaultGeographicCRS.WGS84);

        Literal literal = filterFactory.literal(bbox);

        // the four numbers are also on the parse stack, remove them
        BuildResultStack stack = getResultStack();
        stack.popResult();
        stack.popResult();
        stack.popResult();
        stack.popResult();

        return literal;
    }
}
