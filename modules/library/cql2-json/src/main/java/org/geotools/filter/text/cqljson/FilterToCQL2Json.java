/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2022, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.text.cqljson;

import java.util.List;
import org.geotools.api.filter.And;
import org.geotools.api.filter.BinaryComparisonOperator;
import org.geotools.api.filter.BinaryLogicOperator;
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
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.api.filter.spatial.BBOX;
import org.geotools.api.filter.spatial.Beyond;
import org.geotools.api.filter.spatial.BinarySpatialOperator;
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
import org.geotools.api.filter.temporal.BinaryTemporalOperator;
import org.geotools.api.filter.temporal.During;
import org.geotools.api.filter.temporal.EndedBy;
import org.geotools.api.filter.temporal.Ends;
import org.geotools.api.filter.temporal.Meets;
import org.geotools.api.filter.temporal.MetBy;
import org.geotools.api.filter.temporal.OverlappedBy;
import org.geotools.api.filter.temporal.TContains;
import org.geotools.api.filter.temporal.TEquals;
import org.geotools.api.filter.temporal.TOverlaps;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ArrayNode;
import tools.jackson.databind.node.ObjectNode;

/** This class is responsible to transform a filter to an CQL2-JSON */
public class FilterToCQL2Json implements FilterVisitor {
    private ObjectMapper objectMapper;
    protected static final String OP = "op";
    protected static final String ARGS = "args";

    public FilterToCQL2Json(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Object visitNullFilter(Object extraData) {
        throw new NullPointerException("Cannot encode null as a Filter");
    }

    @Override
    public Object visit(ExcludeFilter filter, Object extraData) {
        throw unsupported("EXCLUDE");
    }

    @Override
    public Object visit(IncludeFilter filter, Object extraData) {
        throw unsupported("INCLUDE");
    }

    @Override
    public Object visit(And filter, Object extraData) {
        return buildBinaryLogicalOperator("and", this, filter, extraData);
    }

    @Override
    public Object visit(Id filter, Object extraData) {
        throw unsupported("ID");
    }

    @Override
    public Object visit(Not filter, Object extraData) {
        ObjectNode output = asObjectNode(extraData);
        ArrayNode args = objectMapper.createArrayNode();
        ObjectNode arg = objectMapper.createObjectNode();
        filter.getFilter().accept(this, arg);
        args.add(arg);
        output.put(OP, "not");
        output.set(ARGS, args);
        return output;
    }

    @Override
    public Object visit(Or filter, Object extraData) {
        if (isInFilter(filter)) {
            return buildIN(filter, extraData);
        }
        // default to normal OR output
        return buildBinaryLogicalOperator("or", this, filter, extraData);
    }

    private Object buildBinaryLogicalOperator(
            final String operator, FilterVisitor visitor, BinaryLogicOperator filter, Object extraData) {

        ObjectNode output = asObjectNode(extraData);
        ArrayNode args = objectMapper.createArrayNode();
        List<Filter> children = filter.getChildren();
        if (children != null) {
            for (Filter child : children) {
                ObjectNode arg = objectMapper.createObjectNode();
                child.accept(visitor, arg);
                args.add(arg);
            }
        }
        output.put(OP, operator);
        output.set(ARGS, args);
        return output;
    }

    private Object buildBinarySpatialOperator(String spatialOperator, BinarySpatialOperator filter, Object extraData) {
        ExpressionToCQL2Json expVisitor = new ExpressionToCQL2Json(objectMapper);
        ObjectNode output = asObjectNode(extraData);
        ArrayNode args = objectMapper.createArrayNode();
        Expression expr1 = filter.getExpression1();
        expr1.accept(expVisitor, args);
        Expression expr2 = filter.getExpression2();
        expr2.accept(expVisitor, args);
        output.put(OP, spatialOperator);
        output.set(ARGS, args);
        return output;
    }

    private Object buildBinaryTemporalOperator(BinaryTemporalOperator op, Object extraData, String opName) {
        ExpressionToCQL2Json expVisitor = new ExpressionToCQL2Json(objectMapper);
        ObjectNode output = asObjectNode(extraData);
        ArrayNode args = objectMapper.createArrayNode();
        op.getExpression1().accept(expVisitor, args);
        op.getExpression2().accept(expVisitor, args);
        output.put(OP, opName);
        output.set(ARGS, args);
        return output;
    }

    private Object buildIN(Or filter, Object extraData) {
        ExpressionToCQL2Json exprVisitor = new ExpressionToCQL2Json(objectMapper);
        ObjectNode output = asObjectNode(extraData);
        ArrayNode args = objectMapper.createArrayNode();
        List<Filter> children = filter.getChildren();
        PropertyIsEqualTo first = (PropertyIsEqualTo) filter.getChildren().get(0);
        Expression left = first.getExpression1();
        left.accept(exprVisitor, args);
        ArrayNode inMembers = objectMapper.createArrayNode();
        for (Filter child : children) {
            PropertyIsEqualTo propertyIsEqualTo = (PropertyIsEqualTo) child;
            Expression right = propertyIsEqualTo.getExpression2();
            right.accept(exprVisitor, inMembers);
        }
        args.add(inMembers);
        output.put(OP, "in");
        output.set(ARGS, args);
        return output;
    }

    @Override
    public Object visit(PropertyIsBetween filter, Object extraData) {
        ExpressionToCQL2Json exprVisitor = new ExpressionToCQL2Json(objectMapper);
        ObjectNode output = asObjectNode(extraData);
        ArrayNode args = objectMapper.createArrayNode();
        PropertyName propertyName = (PropertyName) filter.getExpression();
        propertyName.accept(exprVisitor, args);
        filter.getLowerBoundary().accept(exprVisitor, args);
        filter.getUpperBoundary().accept(exprVisitor, args);
        output.put(OP, "between");
        output.set(ARGS, args);
        return output;
    }

    @Override
    public Object visit(PropertyIsEqualTo filter, Object extraData) {
        ObjectNode output = asObjectNode(extraData);
        if (isRelateOperation(filter)) {
            return buildRelate(filter, output);
        } else if (isFunctionTrue(filter, "PropertyExists", 1)) {
            return buildExists(filter, output);
        }
        return buildComparison(filter, output, "=");
    }

    private Object buildRelate(PropertyIsEqualTo filter, ObjectNode output) {
        ObjectNode functionDetails = objectMapper.createObjectNode();
        Function function = (Function) filter.getExpression1();
        List<Expression> parameters = function.getParameters();
        Expression arg1 = parameters.get(0);
        Expression arg2 = parameters.get(1);
        Literal arg3 = (Literal) parameters.get(2);
        functionDetails.put("name", "RELATE");
        ArrayNode args = objectMapper.createArrayNode();
        ExpressionToCQL2Json visitor = new ExpressionToCQL2Json(objectMapper);
        arg1.accept(visitor, args);
        arg2.accept(visitor, args);
        args.add(arg3.getValue().toString());
        functionDetails.set(ARGS, args);
        output.set("function", functionDetails);
        return output;
    }

    private Object buildExists(PropertyIsEqualTo filter, ObjectNode output) {
        ObjectNode functionDetails = objectMapper.createObjectNode();
        Function function = (Function) filter.getExpression1();
        List<Expression> parameters = function.getParameters();
        Literal arg = (Literal) parameters.get(0);
        functionDetails.put("name", "EXISTS");
        ArrayNode args = objectMapper.createArrayNode();
        args.add(arg.getValue().toString());
        functionDetails.set(ARGS, args);
        output.set("function", functionDetails);
        return output;
    }

    private Object buildComparison(BinaryComparisonOperator filter, ObjectNode output, String operation) {
        output.put(OP, operation);
        ExpressionToCQL2Json visitor = new ExpressionToCQL2Json(objectMapper);
        ArrayNode args = objectMapper.createArrayNode();
        filter.getExpression1().accept(visitor, args);
        filter.getExpression2().accept(visitor, args);
        output.set(ARGS, args);
        return output;
    }

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

    @Override
    public Object visit(PropertyIsNotEqualTo filter, Object extraData) {
        ObjectNode output = asObjectNode(extraData);
        return buildComparison(filter, output, "<>");
    }

    @Override
    public Object visit(PropertyIsGreaterThan filter, Object extraData) {
        ObjectNode output = asObjectNode(extraData);
        return buildComparison(filter, output, ">");
    }

    @Override
    public Object visit(PropertyIsGreaterThanOrEqualTo filter, Object extraData) {
        ObjectNode output = asObjectNode(extraData);
        return buildComparison(filter, output, ">=");
    }

    @Override
    public Object visit(PropertyIsLessThan filter, Object extraData) {
        ObjectNode output = asObjectNode(extraData);
        return buildComparison(filter, output, "<");
    }

    @Override
    public Object visit(PropertyIsLessThanOrEqualTo filter, Object extraData) {
        ObjectNode output = asObjectNode(extraData);
        return buildComparison(filter, output, "<=");
    }

    @Override
    public Object visit(PropertyIsLike filter, Object extraData) {
        ObjectNode output = asObjectNode(extraData);
        output.put(OP, "like");

        ArrayNode args = objectMapper.createArrayNode();
        Expression expr = filter.getExpression();
        expr.accept(new ExpressionToCQL2Json(objectMapper), args);
        final String pattern = filter.getLiteral();
        args.add(pattern);
        output.set(ARGS, args);

        return output;
    }

    @Override
    public Object visit(PropertyIsNull filter, Object extraData) {
        ObjectNode output = asObjectNode(extraData);
        output.put(OP, "isNull");

        ArrayNode args = objectMapper.createArrayNode();
        Expression expr = filter.getExpression();
        expr.accept(new ExpressionToCQL2Json(objectMapper), args);
        output.set(ARGS, args);

        return output;
    }

    @Override
    public Object visit(PropertyIsNil filter, Object extraData) {
        ObjectNode output = asObjectNode(extraData);
        output.put(OP, "isNull");

        ArrayNode args = objectMapper.createArrayNode();
        Expression expr = filter.getExpression();
        expr.accept(new ExpressionToCQL2Json(objectMapper), args);
        output.set(ARGS, args);

        return output;
    }

    @Override
    public Object visit(BBOX filter, Object extraData) {
        return buildBinarySpatialOperator("s_intersects", filter, extraData);
    }

    @Override
    public Object visit(Beyond filter, Object extraData) {
        return buildBinarySpatialOperator("s_crosses", filter, extraData);
    }

    @Override
    public Object visit(Contains filter, Object extraData) {
        return buildBinarySpatialOperator("s_contains", filter, extraData);
    }

    @Override
    public Object visit(Crosses filter, Object extraData) {
        return buildBinarySpatialOperator("s_crosses", filter, extraData);
    }

    @Override
    public Object visit(Disjoint filter, Object extraData) {
        return buildBinarySpatialOperator("s_disjoint", filter, extraData);
    }

    @Override
    public Object visit(DWithin filter, Object extraData) {
        return buildBinarySpatialOperator("s_within", filter, extraData);
    }

    @Override
    public Object visit(Equals filter, Object extraData) {
        return buildBinarySpatialOperator("s_equals", filter, extraData);
    }

    @Override
    public Object visit(Intersects filter, Object extraData) {
        return buildBinarySpatialOperator("s_intersects", filter, extraData);
    }

    @Override
    public Object visit(Overlaps filter, Object extraData) {
        return buildBinarySpatialOperator("s_overlaps", filter, extraData);
    }

    @Override
    public Object visit(Touches filter, Object extraData) {
        return buildBinarySpatialOperator("s_touches", filter, extraData);
    }

    @Override
    public Object visit(Within filter, Object extraData) {
        return buildBinarySpatialOperator("s_within", filter, extraData);
    }

    @Override
    public Object visit(After after, Object extraData) {
        return buildBinaryTemporalOperator(after, extraData, "t_after");
    }

    @Override
    public Object visit(AnyInteracts anyInteracts, Object extraData) {
        return buildBinaryTemporalOperator(anyInteracts, extraData, "t_intersects");
    }

    @Override
    public Object visit(Before before, Object extraData) {
        return buildBinaryTemporalOperator(before, extraData, "t_before");
    }

    @Override
    public Object visit(Begins begins, Object extraData) {
        return buildBinaryTemporalOperator(begins, extraData, "t_starts");
    }

    @Override
    public Object visit(BegunBy begunBy, Object extraData) {
        return buildBinaryTemporalOperator(begunBy, extraData, "t_startedBy");
    }

    @Override
    public Object visit(During during, Object extraData) {
        return buildBinaryTemporalOperator(during, extraData, "t_during");
    }

    @Override
    public Object visit(EndedBy endedBy, Object extraData) {
        return buildBinaryTemporalOperator(endedBy, extraData, "t_finishedBy");
    }

    @Override
    public Object visit(Ends ends, Object extraData) {
        return buildBinaryTemporalOperator(ends, extraData, "t_finishes");
    }

    @Override
    public Object visit(Meets meets, Object extraData) {
        return buildBinaryTemporalOperator(meets, extraData, "t_meets");
    }

    @Override
    public Object visit(MetBy metBy, Object extraData) {
        return buildBinaryTemporalOperator(metBy, extraData, "t_metBy");
    }

    @Override
    public Object visit(OverlappedBy overlappedBy, Object extraData) {
        return buildBinaryTemporalOperator(overlappedBy, extraData, "t_overlappedBy");
    }

    @Override
    public Object visit(TContains contains, Object extraData) {
        return buildBinaryTemporalOperator(contains, extraData, "t_contains");
    }

    @Override
    public Object visit(TEquals equals, Object extraData) {
        return buildBinaryTemporalOperator(equals, extraData, "t_equals");
    }

    @Override
    public Object visit(TOverlaps contains, Object extraData) {
        return buildBinaryTemporalOperator(contains, extraData, "t_overlaps");
    }

    private ObjectNode asObjectNode(Object extraData) {
        if (extraData instanceof ObjectNode node) {
            return node;
        }
        return objectMapper.createObjectNode();
    }

    /**
     * New instance of unsupported exception with the name of filter
     *
     * @param filterName filter unsupported
     * @return UnsupportedOperationException
     */
    private static UnsupportedOperationException unsupported(final String filterName) {
        return new UnsupportedOperationException("The" + filterName + " has not an CQL2 expression");
    }
}
