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
package org.geotools.filter.text.cql2;

import java.util.logging.Logger;
import org.geotools.api.filter.And;
import org.geotools.api.filter.ExcludeFilter;
import org.geotools.api.filter.FilterVisitor;
import org.geotools.api.filter.Id;
import org.geotools.api.filter.IncludeFilter;
import org.geotools.api.filter.Not;
import org.geotools.api.filter.Or;
import org.geotools.api.filter.PropertyIsBetween;
import org.geotools.api.filter.PropertyIsEqualTo;
import org.geotools.api.filter.PropertyIsGreaterThan;
import org.geotools.api.filter.PropertyIsGreaterThanOrEqualTo;
import org.geotools.api.filter.PropertyIsLessThan;
import org.geotools.api.filter.PropertyIsLessThanOrEqualTo;
import org.geotools.api.filter.PropertyIsLike;
import org.geotools.api.filter.PropertyIsNil;
import org.geotools.api.filter.PropertyIsNotEqualTo;
import org.geotools.api.filter.PropertyIsNull;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.api.filter.spatial.BBOX;
import org.geotools.api.filter.spatial.Beyond;
import org.geotools.api.filter.spatial.Contains;
import org.geotools.api.filter.spatial.Crosses;
import org.geotools.api.filter.spatial.DWithin;
import org.geotools.api.filter.spatial.Disjoint;
import org.geotools.api.filter.spatial.Equals;
import org.geotools.api.filter.spatial.Intersects;
import org.geotools.api.filter.spatial.Overlaps;
import org.geotools.api.filter.spatial.Touches;
import org.geotools.api.filter.spatial.Within;
import org.geotools.api.filter.temporal.After;
import org.geotools.api.filter.temporal.AnyInteracts;
import org.geotools.api.filter.temporal.Before;
import org.geotools.api.filter.temporal.Begins;
import org.geotools.api.filter.temporal.BegunBy;
import org.geotools.api.filter.temporal.During;
import org.geotools.api.filter.temporal.EndedBy;
import org.geotools.api.filter.temporal.Ends;
import org.geotools.api.filter.temporal.Meets;
import org.geotools.api.filter.temporal.MetBy;
import org.geotools.api.filter.temporal.OverlappedBy;
import org.geotools.api.filter.temporal.TContains;
import org.geotools.api.filter.temporal.TEquals;
import org.geotools.api.filter.temporal.TOverlaps;
import org.geotools.filter.text.commons.FilterToTextUtil;

/**
 * This is a utility class used by CQL.encode( Filter ) method to do the hard work.
 *
 * <p>Please note that this encoder is a bit more strict than you may be used to (the Common Query
 * Language for example demands Equals.getExpression1() is a PropertyName). If you used
 * FilterFactory to produce your filter you should be okay (as it only provides methods to make a
 * valid Filter); if not please expect ClassCastExceptions.
 *
 * <p>This visitor will return a StringBuilder; you can also provide a StringBuilder as the data
 * parameter in order to cut down on the number of objects created during encoding.
 *
 * <pre>
 * <code>
 * FilterToCQL toCQL = new FilterToCQL();
 * StringBuilder output = filter.accepts( toCQL, new StringBuilder() );
 * String cql = output.toString();
 * </code
 * ></pre>
 *
 * @author Johann Sorel
 */
class FilterToCQL implements FilterVisitor {
    /** Standard java logger */
    private static Logger LOGGER = org.geotools.util.logging.Logging.getLogger(FilterToCQL.class);

    /** Exclude everything; using an old SQL trick of 1=0. */
    @Override
    public Object visit(ExcludeFilter filter, Object extraData) {
        return FilterToTextUtil.buildExclude(extraData);
    }
    /** Include everything; using an old SQL trick of 1=1. */
    @Override
    public Object visit(IncludeFilter filter, Object extraData) {
        return FilterToTextUtil.buildInclude(extraData);
    }

    @Override
    public Object visit(And filter, Object extraData) {

        return FilterToTextUtil.buildBinaryLogicalOperator("AND", this, filter, extraData);
    }

    /**
     * Encoding an Id filter is not supported by CQL.
     *
     * <p>This is because in the Catalog specification retreiving an object by an id is a distinct
     * operation seperate from a filter based query.
     */
    @Override
    public Object visit(Id filter, Object extraData) {
        throw new IllegalStateException("Cannot encode an Id as legal CQL");
    }

    @Override
    public Object visit(Not filter, Object extraData) {
        LOGGER.finer("exporting Not filter");

        return FilterToTextUtil.buildNot(this, filter, extraData);
    }

    @Override
    public Object visit(Or filter, Object extraData) {
        LOGGER.finer("exporting Or filter");

        return FilterToTextUtil.buildBinaryLogicalOperator("OR", this, filter, extraData);
    }

    @Override
    public Object visit(PropertyIsBetween filter, Object extraData) {

        return FilterToTextUtil.buildBetween(filter, extraData);
    }

    @Override
    public Object visit(PropertyIsEqualTo filter, Object extraData) {

        checkLeftExpressionIsProperty(filter.getExpression1());
        return FilterToTextUtil.buildComparison(filter, extraData, "=");
    }

    @Override
    public Object visit(PropertyIsNotEqualTo filter, Object extraData) {

        checkLeftExpressionIsProperty(filter.getExpression1());
        return FilterToTextUtil.buildComparison(filter, extraData, "<>");
    }

    @Override
    public Object visit(PropertyIsGreaterThan filter, Object extraData) {

        checkLeftExpressionIsProperty(filter.getExpression1());
        return FilterToTextUtil.buildComparison(filter, extraData, ">");
    }

    @Override
    public Object visit(PropertyIsGreaterThanOrEqualTo filter, Object extraData) {
        checkLeftExpressionIsProperty(filter.getExpression1());
        return FilterToTextUtil.buildComparison(filter, extraData, ">=");
    }

    @Override
    public Object visit(PropertyIsLessThan filter, Object extraData) {

        checkLeftExpressionIsProperty(filter.getExpression1());
        return FilterToTextUtil.buildComparison(filter, extraData, "<");
    }

    @Override
    public Object visit(PropertyIsLessThanOrEqualTo filter, Object extraData) {
        checkLeftExpressionIsProperty(filter.getExpression1());
        return FilterToTextUtil.buildComparison(filter, extraData, "<=");
    }

    @Override
    public Object visit(PropertyIsLike filter, Object extraData) {

        checkLeftExpressionIsProperty(filter.getExpression());
        return FilterToTextUtil.buildIsLike(filter, extraData);
    }

    private void checkLeftExpressionIsProperty(Expression expr) {
        if (!(expr instanceof PropertyName)) {
            throw new RuntimeException("CQL requires a PropertyName");
        }
    }

    @Override
    public Object visit(PropertyIsNull filter, Object extraData) {

        return FilterToTextUtil.buildIsNull(filter, extraData);
    }

    @Override
    public Object visit(PropertyIsNil filter, Object extraData) {
        throw new UnsupportedOperationException("isNil not supported");
    }

    @Override
    public Object visit(BBOX filter, Object extraData) {

        return FilterToTextUtil.buildBBOX(filter, extraData);
    }

    @Override
    public Object visit(Beyond filter, Object extraData) {

        checkLeftExpressionIsProperty(filter.getExpression1());
        return FilterToTextUtil.buildDistanceBufferOperation("BEYOND", filter, extraData);
    }

    @Override
    public Object visit(Contains filter, Object extraData) {

        checkLeftExpressionIsProperty(filter.getExpression1());
        return FilterToTextUtil.buildBinarySpatialOperator("CONTAINS", filter, extraData);
    }

    @Override
    public Object visit(Crosses filter, Object extraData) {
        checkLeftExpressionIsProperty(filter.getExpression1());
        return FilterToTextUtil.buildBinarySpatialOperator("CROSSES", filter, extraData);
    }

    @Override
    public Object visit(Disjoint filter, Object extraData) {
        checkLeftExpressionIsProperty(filter.getExpression1());
        return FilterToTextUtil.buildBinarySpatialOperator("DISJOINT", filter, extraData);
    }

    @Override
    public Object visit(DWithin filter, Object extraData) {

        checkLeftExpressionIsProperty(filter.getExpression1());
        return FilterToTextUtil.buildDWithin(filter, extraData);
    }

    @Override
    public Object visit(Equals filter, Object extraData) {
        checkLeftExpressionIsProperty(filter.getExpression1());
        return FilterToTextUtil.buildBinarySpatialOperator("EQUALS", filter, extraData);
    }

    @Override
    public Object visit(Intersects filter, Object extraData) {

        checkLeftExpressionIsProperty(filter.getExpression1());
        return FilterToTextUtil.buildBinarySpatialOperator("INTERSECTS", filter, extraData);
    }

    @Override
    public Object visit(Overlaps filter, Object extraData) {
        checkLeftExpressionIsProperty(filter.getExpression1());
        return FilterToTextUtil.buildBinarySpatialOperator("OVERLAPS", filter, extraData);
    }

    @Override
    public Object visit(Touches filter, Object extraData) {
        checkLeftExpressionIsProperty(filter.getExpression1());
        return FilterToTextUtil.buildBinarySpatialOperator("TOUCHES", filter, extraData);
    }

    @Override
    public Object visit(Within filter, Object extraData) {
        checkLeftExpressionIsProperty(filter.getExpression1());
        return FilterToTextUtil.buildBinarySpatialOperator("WITHIN", filter, extraData);
    }
    /**
     * A filter has not been provided.
     *
     * <p>In general this is a bad situtation which we ask people to represent with Filter.INCLUDES
     * or Filter.EXCLUDES depending on what behaviour they want to see happen - in this case
     * literally <code>null</code> was provided.
     *
     * <p>
     */
    @Override
    public Object visitNullFilter(Object extraData) {
        throw new NullPointerException("Cannot encode null as a Filter");
    }

    @Override
    public Object visit(After after, Object extraData) {

        return FilterToTextUtil.buildBinaryTemporalOperator("AFTER", after, extraData);
    }

    @Override
    public Object visit(Before before, Object extraData) {
        return FilterToTextUtil.buildBinaryTemporalOperator("BEFORE", before, extraData);
    }

    @Override
    public Object visit(During during, Object extraData) {

        return FilterToTextUtil.buildDuring(during, extraData);
    }

    @Override
    public Object visit(AnyInteracts anyInteracts, Object extraData) {
        throw new UnsupportedOperationException(
                "Temporal filter AnyInteracts has not a CQL expression");
    }

    @Override
    public Object visit(Begins begins, Object extraData) {
        throw new UnsupportedOperationException("Temporal filter Begins has not a CQL expression");
    }

    @Override
    public Object visit(BegunBy begunBy, Object extraData) {
        throw new UnsupportedOperationException("Temporal filter BegunBy has not a CQL expression");
    }

    @Override
    public Object visit(EndedBy endedBy, Object extraData) {
        throw new UnsupportedOperationException("Temporal filter EndedBy has not a CQL expression");
    }

    @Override
    public Object visit(Ends ends, Object extraData) {
        throw new UnsupportedOperationException("Temporal filter Ends has not a CQL expression");
    }

    @Override
    public Object visit(Meets meets, Object extraData) {
        throw new UnsupportedOperationException("Temporal filter Meets has not a CQL expression");
    }

    @Override
    public Object visit(MetBy metBy, Object extraData) {
        throw new UnsupportedOperationException("Temporal filter MetBy has not a CQL expression");
    }

    @Override
    public Object visit(OverlappedBy overlappedBy, Object extraData) {
        throw new UnsupportedOperationException("Temporal filter OverlappedBy not implemented");
    }

    @Override
    public Object visit(TContains contains, Object extraData) {
        throw new UnsupportedOperationException(
                "Temporal filter TContains has not a CQL expression");
    }

    @Override
    public Object visit(TEquals equals, Object extraData) {
        throw new UnsupportedOperationException("Temporal filter TEquals has not a CQL expression");
    }

    @Override
    public Object visit(TOverlaps contains, Object extraData) {
        throw new UnsupportedOperationException(
                "Temporal filter TOverlaps has not a CQL expression");
    }
}
