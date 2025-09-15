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
package org.geotools.filter.text.ecql;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import org.geotools.api.filter.And;
import org.geotools.api.filter.ExcludeFilter;
import org.geotools.api.filter.Filter;
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
import org.geotools.api.filter.expression.Function;
import org.geotools.api.filter.expression.Literal;
import org.geotools.api.filter.identity.Identifier;
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
import org.geotools.filter.text.commons.ExpressionToText;
import org.geotools.filter.text.commons.FilterToTextUtil;
import org.geotools.util.factory.Hints;

/**
 * This class is responsible to transform a filter to an ECQL predicate.
 *
 * @author Mauricio Pazos
 */
final class FilterToECQL implements FilterVisitor {

    /** Pattern used to match Id filters for output as plain number. */
    private static Pattern NUMBER = Pattern.compile("[0-9]+");

    ExpressionToText expressionVisitor;

    /** Default constructor. The behavior of EWKT encoding is controlled by the {@link Hints#ENCODE_EWKT} hint */
    public FilterToECQL() {
        this(ECQL.isEwktEncodingEnabled());
    }

    public FilterToECQL(boolean encodeEwkt) {
        expressionVisitor = new ExpressionToText(encodeEwkt);
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

    /** builds a ecql id expression: in (id1, id2, ...) */
    @Override
    public Object visit(Id filter, Object extraData) {

        StringBuilder ecql = FilterToTextUtil.asStringBuilder(extraData);
        ecql.append("IN (");

        Iterator<Identifier> iter = filter.getIdentifiers().iterator();
        while (iter.hasNext()) {
            Identifier identifier = iter.next();
            String id = identifier.toString();

            boolean needsQuotes = !NUMBER.matcher(id).matches();

            if (needsQuotes) {
                ecql.append('\'');
            }
            ecql.append(identifier);
            if (needsQuotes) {
                ecql.append('\'');
            }

            if (iter.hasNext()) {
                ecql.append(",");
            }
        }
        ecql.append(")");
        return ecql;
    }

    /** builds the Not logical operator */
    @Override
    public Object visit(Not filter, Object extraData) {
        return FilterToTextUtil.buildNot(this, filter, extraData);
    }

    /**
     * Builds the OR logical operator.
     *
     * <p>This visitor checks for {@link #isInFilter(Or)} and is willing to output ECQL of the form <code>
     * left IN (right, right, right)</code>.
     */
    @Override
    public Object visit(Or filter, Object extraData) {
        if (isInFilter(filter)) {
            return buildIN(filter, extraData);
        }
        // default to normal OR output
        return FilterToTextUtil.buildBinaryLogicalOperator("OR", this, filter, extraData);
    }

    /** Check if this is an encoding of ECQL IN */
    private boolean isInFilter(Or filter) {
        if (filter.getChildren() == null) {
            return false;
        }
        Expression left = null;
        for (Filter child : filter.getChildren()) {
            if (child instanceof PropertyIsEqualTo equal) {
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

    /** Output EQUAL filter (will checks for ECQL geospatial operations). */
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

    /** Check if this is an encoding of ECQL geospatial operation */
    private boolean isFunctionTrue(PropertyIsEqualTo filter, String operation, int numberOfArguments) {
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

    /** Check if this is an encoding of ECQL geospatial operation */
    private boolean isRelateOperation(PropertyIsEqualTo filter) {
        if (isFunctionTrue(filter, "relatePattern", 3)) {
            Function function = (Function) filter.getExpression1();
            List<Expression> parameters = function.getParameters();
            Expression param3 = parameters.get(2);
            if (param3 instanceof Literal literal) {
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
        return FilterToTextUtil.buildDistanceBufferOperation("BEYOND", filter, extraData, expressionVisitor);
    }

    @Override
    public Object visit(Contains filter, Object extraData) {
        return FilterToTextUtil.buildBinarySpatialOperator("CONTAINS", filter, extraData, expressionVisitor);
    }

    @Override
    public Object visit(Crosses filter, Object extraData) {
        return FilterToTextUtil.buildBinarySpatialOperator("CROSSES", filter, extraData, expressionVisitor);
    }

    @Override
    public Object visit(Disjoint filter, Object extraData) {
        return FilterToTextUtil.buildBinarySpatialOperator("DISJOINT", filter, extraData, expressionVisitor);
    }

    @Override
    public Object visit(DWithin filter, Object extraData) {
        return FilterToTextUtil.buildDWithin(filter, extraData, expressionVisitor);
    }

    @Override
    public Object visit(Equals filter, Object extraData) {
        return FilterToTextUtil.buildBinarySpatialOperator("EQUALS", filter, extraData, expressionVisitor);
    }

    @Override
    public Object visit(Intersects filter, Object extraData) {
        return FilterToTextUtil.buildBinarySpatialOperator("INTERSECTS", filter, extraData, expressionVisitor);
    }

    @Override
    public Object visit(Overlaps filter, Object extraData) {
        return FilterToTextUtil.buildBinarySpatialOperator("OVERLAPS", filter, extraData, expressionVisitor);
    }

    @Override
    public Object visit(Touches filter, Object extraData) {
        return FilterToTextUtil.buildBinarySpatialOperator("TOUCHES", filter, extraData, expressionVisitor);
    }

    @Override
    public Object visit(Within filter, Object extraData) {
        return FilterToTextUtil.buildBinarySpatialOperator("WITHIN", filter, extraData, expressionVisitor);
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
    public Object visit(AnyInteracts anyInteracts, Object extraData) {
        throw ecqlUnsupported("AnyInteracts");
    }

    @Override
    public Object visit(Begins begins, Object extraData) {
        throw ecqlUnsupported("Begins");
    }

    @Override
    public Object visit(BegunBy begunBy, Object extraData) {
        throw ecqlUnsupported("BegunBy");
    }

    /**
     * New instance of unsupported exception with the name of filter
     *
     * @param filterName filter unsupported
     * @return UnsupportedOperationException
     */
    private static UnsupportedOperationException ecqlUnsupported(final String filterName) {
        return new UnsupportedOperationException("The" + filterName + " has not an ECQL expression");
    }

    @Override
    public Object visit(During during, Object extraData) {
        return FilterToTextUtil.buildDuring(during, extraData);
    }

    @Override
    public Object visit(EndedBy endedBy, Object extraData) {
        throw ecqlUnsupported("EndedBy");
    }

    @Override
    public Object visit(Ends ends, Object extraData) {
        throw ecqlUnsupported("EndedBy");
    }

    @Override
    public Object visit(Meets meets, Object extraData) {
        throw ecqlUnsupported("Meets");
    }

    @Override
    public Object visit(MetBy metBy, Object extraData) {
        throw ecqlUnsupported("MetBy");
    }

    @Override
    public Object visit(OverlappedBy overlappedBy, Object extraData) {
        throw ecqlUnsupported("OverlappedBy");
    }

    @Override
    public Object visit(TContains contains, Object extraData) {
        throw ecqlUnsupported("TContains");
    }

    @Override
    public Object visit(TEquals equals, Object extraData) {
        throw ecqlUnsupported("TContains");
    }

    @Override
    public Object visit(TOverlaps contains, Object extraData) {
        throw ecqlUnsupported("TContains");
    }
}
