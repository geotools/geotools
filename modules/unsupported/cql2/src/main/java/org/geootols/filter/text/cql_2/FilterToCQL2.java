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

import java.util.Iterator;
import java.util.List;
import org.geotools.filter.text.commons.ExpressionToText;
import org.geotools.filter.text.commons.FilterToTextUtil;
import org.opengis.filter.And;
import org.opengis.filter.ExcludeFilter;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterVisitor;
import org.opengis.filter.Id;
import org.opengis.filter.IncludeFilter;
import org.opengis.filter.Not;
import org.opengis.filter.Or;
import org.opengis.filter.PropertyIsBetween;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.PropertyIsGreaterThan;
import org.opengis.filter.PropertyIsGreaterThanOrEqualTo;
import org.opengis.filter.PropertyIsLessThan;
import org.opengis.filter.PropertyIsLessThanOrEqualTo;
import org.opengis.filter.PropertyIsLike;
import org.opengis.filter.PropertyIsNil;
import org.opengis.filter.PropertyIsNotEqualTo;
import org.opengis.filter.PropertyIsNull;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.spatial.BBOX;
import org.opengis.filter.spatial.Beyond;
import org.opengis.filter.spatial.Contains;
import org.opengis.filter.spatial.Crosses;
import org.opengis.filter.spatial.DWithin;
import org.opengis.filter.spatial.Disjoint;
import org.opengis.filter.spatial.Equals;
import org.opengis.filter.spatial.Intersects;
import org.opengis.filter.spatial.Overlaps;
import org.opengis.filter.spatial.Touches;
import org.opengis.filter.spatial.Within;
import org.opengis.filter.temporal.After;
import org.opengis.filter.temporal.AnyInteracts;
import org.opengis.filter.temporal.Before;
import org.opengis.filter.temporal.Begins;
import org.opengis.filter.temporal.BegunBy;
import org.opengis.filter.temporal.BinaryTemporalOperator;
import org.opengis.filter.temporal.During;
import org.opengis.filter.temporal.EndedBy;
import org.opengis.filter.temporal.Ends;
import org.opengis.filter.temporal.Meets;
import org.opengis.filter.temporal.MetBy;
import org.opengis.filter.temporal.OverlappedBy;
import org.opengis.filter.temporal.TContains;
import org.opengis.filter.temporal.TEquals;
import org.opengis.filter.temporal.TOverlaps;

/**
 * This class is responsible to transform a filter to an ECQL predicate.
 *
 * @author Mauricio Pazos
 */
final class FilterToCQL2 implements FilterVisitor {

    ExpressionToText expressionVisitor;

    public FilterToCQL2() {
        expressionVisitor = new CQL2ExpressionToText();
    }

    @Override
    public Object visitNullFilter(Object extraData) {
        throw new NullPointerException("Cannot encode null as a Filter");
    }

    @Override
    public Object visit(ExcludeFilter filter, Object extraData) {

        return FilterToTextUtil.buildExclude(extraData);
    }

    @Override
    public Object visit(IncludeFilter filter, Object extraData) {
        return FilterToTextUtil.buildInclude(extraData);
    }

    @Override
    public Object visit(And filter, Object extraData) {
        return FilterToTextUtil.buildBinaryLogicalOperator("AND", this, filter, extraData);
    }

    @Override
    public Object visit(Id filter, Object extraData) {
        throw unsupported("ID");
    }

    /** builds the Not logical operator */
    @Override
    public Object visit(Not filter, Object extraData) {
        return FilterToTextUtil.buildNot(this, filter, extraData);
    }

    /**
     * Builds the OR logical operator.
     *
     * <p>This visitor checks for {@link #isInFilter(Or)} and is willing to output CQL2 of the form
     * <code>left IN (right, right, right)</code>.
     */
    @Override
    public Object visit(Or filter, Object extraData) {
        if (isInFilter(filter)) {
            return buildIN(filter, extraData);
        }
        // default to normal OR output
        return FilterToTextUtil.buildBinaryLogicalOperator("OR", this, filter, extraData);
    }

    /** Check if this is an encoding of CQL2 IN */
    private boolean isInFilter(Or filter) {
        if (filter.getChildren() == null) {
            return false;
        }
        Expression left = null;
        for (Filter child : filter.getChildren()) {
            if (child instanceof PropertyIsEqualTo) {
                PropertyIsEqualTo equal = (PropertyIsEqualTo) child;
                if (left == null) {
                    left = equal.getExpression1();
                } else if (!left.equals(equal.getExpression1())) {
                    return false; // not IN
                }
            } else {
                return false; // not IN
            }
        }
        return true;
    }

    private Object buildIN(Or filter, Object extraData) {
        StringBuilder output = FilterToTextUtil.asStringBuilder(extraData);
        List<Filter> children = filter.getChildren();
        PropertyIsEqualTo first = (PropertyIsEqualTo) filter.getChildren().get(0);
        Expression left = first.getExpression1();
        left.accept(expressionVisitor, output);
        output.append(" IN (");
        for (Iterator<Filter> i = children.iterator(); i.hasNext(); ) {
            PropertyIsEqualTo child = (PropertyIsEqualTo) i.next();
            Expression right = child.getExpression2();
            right.accept(expressionVisitor, output);
            if (i.hasNext()) {
                output.append(",");
            }
        }
        output.append(")");
        return output;
    }

    /** builds the BETWEEN predicate */
    @Override
    public Object visit(PropertyIsBetween filter, Object extraData) {
        return FilterToTextUtil.buildBetween(filter, extraData);
    }

    /** Output EQUAL filter (will checks for CQL2 geospatial operations). */
    @Override
    public Object visit(PropertyIsEqualTo filter, Object extraData) {
        StringBuilder output = FilterToTextUtil.asStringBuilder(extraData);
        if (isRelateOperation(filter)) {
            return buildRelate(filter, output);
        } else if (isFunctionTrue(filter, "PropertyExists", 1)) {
            return buildExists(filter, output);
        }
        return FilterToTextUtil.buildComparison(filter, output, "=");
    }

    /** Check if this is an encoding of CQL2 geospatial operation */
    private boolean isFunctionTrue(
            PropertyIsEqualTo filter, String operation, int numberOfArguments) {
        if (filter.getExpression1() instanceof Function) {
            Function function = (Function) filter.getExpression1();
            List<Expression> parameters = function.getParameters();
            if (parameters == null) {
                return false;
            }
            String name = function.getName();
            if (!operation.equalsIgnoreCase(name) || parameters.size() != numberOfArguments) {
                return false;
            }
        } else {
            return false;
        }
        if (filter.getExpression2() instanceof Literal) {
            Literal literal = (Literal) filter.getExpression2();
            Boolean value = literal.evaluate(null, Boolean.class);
            if (value == null || value == false) {
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    private Object buildExists(PropertyIsEqualTo filter, StringBuilder output) {
        Function function = (Function) filter.getExpression1();
        List<Expression> parameters = function.getParameters();
        Literal arg = (Literal) parameters.get(0);

        output.append(arg.getValue());
        output.append(" EXISTS");
        return output;
    }

    /** Check if this is an encoding of CQL2 geospatial operation */
    private boolean isRelateOperation(PropertyIsEqualTo filter) {
        if (isFunctionTrue(filter, "relatePattern", 3)) {
            Function function = (Function) filter.getExpression1();
            List<Expression> parameters = function.getParameters();
            Expression param3 = parameters.get(2);
            if (param3 instanceof Literal) {
                Literal literal = (Literal) param3;
                Object value = literal.getValue();
                if (!(value instanceof String)) {
                    return false; // not a relate
                }
            }
        } else {
            return false;
        }
        return true;
    }

    private Object buildRelate(PropertyIsEqualTo filter, StringBuilder output) {
        Function operation = (Function) filter.getExpression1();
        output.append("RELATE(");
        List<Expression> parameters = operation.getParameters();
        Expression arg1 = parameters.get(0);
        Expression arg2 = parameters.get(1);
        Literal arg3 = (Literal) parameters.get(2);

        arg1.accept(expressionVisitor, output);
        output.append(",");
        arg2.accept(expressionVisitor, output);
        output.append(",");
        output.append(arg3.getValue());
        output.append(")");
        return output;
    }

    @Override
    public Object visit(PropertyIsNotEqualTo filter, Object extraData) {
        return FilterToTextUtil.buildComparison(filter, extraData, "<>");
    }

    @Override
    public Object visit(PropertyIsGreaterThan filter, Object extraData) {
        return FilterToTextUtil.buildComparison(filter, extraData, ">");
    }

    @Override
    public Object visit(PropertyIsGreaterThanOrEqualTo filter, Object extraData) {
        return FilterToTextUtil.buildComparison(filter, extraData, ">=");
    }

    @Override
    public Object visit(PropertyIsLessThan filter, Object extraData) {
        return FilterToTextUtil.buildComparison(filter, extraData, "<");
    }

    @Override
    public Object visit(PropertyIsLessThanOrEqualTo filter, Object extraData) {
        return FilterToTextUtil.buildComparison(filter, extraData, "<=");
    }

    @Override
    public Object visit(PropertyIsLike filter, Object extraData) {
        return FilterToTextUtil.buildIsLike(filter, extraData);
    }

    @Override
    public Object visit(PropertyIsNull filter, Object extraData) {
        return FilterToTextUtil.buildIsNull(filter, extraData);
    }

    @Override
    public Object visit(PropertyIsNil filter, Object extraData) {
        throw new UnsupportedOperationException("PropertyIsNil not supported");
    }

    @Override
    public Object visit(BBOX filter, Object extraData) {
        return FilterToTextUtil.buildBBOX(filter, extraData);
    }

    @Override
    public Object visit(Beyond filter, Object extraData) {
        throw unsupported("BEYOND");
    }

    @Override
    public Object visit(Contains filter, Object extraData) {
        return FilterToTextUtil.buildBinarySpatialOperator(
                "S_CONTAINS", filter, extraData, expressionVisitor);
    }

    @Override
    public Object visit(Crosses filter, Object extraData) {
        return FilterToTextUtil.buildBinarySpatialOperator(
                "S_CROSSES", filter, extraData, expressionVisitor);
    }

    @Override
    public Object visit(Disjoint filter, Object extraData) {
        return FilterToTextUtil.buildBinarySpatialOperator(
                "S_DISJOINT", filter, extraData, expressionVisitor);
    }

    @Override
    public Object visit(DWithin filter, Object extraData) {
        throw unsupported("DWITHIN");
    }

    @Override
    public Object visit(Equals filter, Object extraData) {
        return FilterToTextUtil.buildBinarySpatialOperator(
                "S_EQUALS", filter, extraData, expressionVisitor);
    }

    @Override
    public Object visit(Intersects filter, Object extraData) {
        return FilterToTextUtil.buildBinarySpatialOperator(
                "S_INTERSECTS", filter, extraData, expressionVisitor);
    }

    @Override
    public Object visit(Overlaps filter, Object extraData) {
        return FilterToTextUtil.buildBinarySpatialOperator(
                "S_OVERLAPS", filter, extraData, expressionVisitor);
    }

    @Override
    public Object visit(Touches filter, Object extraData) {
        return FilterToTextUtil.buildBinarySpatialOperator(
                "S_TOUCHES", filter, extraData, expressionVisitor);
    }

    @Override
    public Object visit(Within filter, Object extraData) {
        return FilterToTextUtil.buildBinarySpatialOperator(
                "S_WITHIN", filter, extraData, expressionVisitor);
    }

    @Override
    public Object visit(After after, Object extraData) {
        return buildBinaryTemporalOperator(after, extraData, "T_AFTER");
    }

    @Override
    public Object visit(Before before, Object extraData) {
        return buildBinaryTemporalOperator(before, extraData, "T_BEFORE");
    }

    private Object buildBinaryTemporalOperator(
            BinaryTemporalOperator op, Object extraData, String opName) {
        StringBuilder output = FilterToTextUtil.asStringBuilder(extraData);

        output.append(opName).append("(");

        ExpressionToText visitor = new CQL2ExpressionToText();
        op.getExpression1().accept(visitor, output);
        output.append(", ");
        op.getExpression2().accept(visitor, output);

        output.append(")");

        return output;
    }

    @Override
    public Object visit(AnyInteracts anyInteracts, Object extraData) {
        throw unsupported("AnyInteracts");
    }

    @Override
    public Object visit(Begins begins, Object extraData) {
        throw unsupported("Begins");
    }

    @Override
    public Object visit(BegunBy begunBy, Object extraData) {
        throw unsupported("BegunBy");
    }

    /**
     * New instance of unsupported exception with the name of filter
     *
     * @param filterName filter unsupported
     * @return UnsupportedOperationException
     */
    private static UnsupportedOperationException unsupported(final String filterName) {
        return new UnsupportedOperationException(
                "The" + filterName + " has not an CQL2 expression");
    }

    @Override
    public Object visit(During during, Object extraData) {
        ExpressionToText visitor = new CQL2ExpressionToText();

        StringBuilder output = FilterToTextUtil.asStringBuilder(extraData);
        output.append("T_DURING(");
        during.getExpression1().accept(visitor, output);
        output.append(", ");
        during.getExpression2().accept(visitor, output);
        output.append(")");

        return output;
    }

    @Override
    public Object visit(EndedBy endedBy, Object extraData) {
        throw unsupported("EndedBy");
    }

    @Override
    public Object visit(Ends ends, Object extraData) {
        throw unsupported("EndedBy");
    }

    @Override
    public Object visit(Meets meets, Object extraData) {
        throw unsupported("Meets");
    }

    @Override
    public Object visit(MetBy metBy, Object extraData) {
        throw unsupported("MetBy");
    }

    @Override
    public Object visit(OverlappedBy overlappedBy, Object extraData) {
        throw unsupported("OverlappedBy");
    }

    @Override
    public Object visit(TContains contains, Object extraData) {
        throw unsupported("TContains");
    }

    @Override
    public Object visit(TEquals equals, Object extraData) {
        throw unsupported("TContains");
    }

    @Override
    public Object visit(TOverlaps contains, Object extraData) {
        throw unsupported("TContains");
    }
}
