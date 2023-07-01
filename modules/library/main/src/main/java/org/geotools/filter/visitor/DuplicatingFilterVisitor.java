/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.visitor;

import java.util.ArrayList;
import java.util.List;
import org.geotools.api.filter.And;
import org.geotools.api.filter.ExcludeFilter;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory2;
import org.geotools.api.filter.FilterVisitor;
import org.geotools.api.filter.Id;
import org.geotools.api.filter.IncludeFilter;
import org.geotools.api.filter.NativeFilter;
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
import org.geotools.api.filter.expression.Add;
import org.geotools.api.filter.expression.Divide;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.ExpressionVisitor;
import org.geotools.api.filter.expression.Function;
import org.geotools.api.filter.expression.InternalFunction;
import org.geotools.api.filter.expression.Literal;
import org.geotools.api.filter.expression.Multiply;
import org.geotools.api.filter.expression.NilExpression;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.api.filter.expression.Subtract;
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
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.util.factory.GeoTools;

/**
 * Used to duplication Filters and/or Expressions - returned object is a copy.
 *
 * <p>Extra data can be used to provide a {@link FilterFactory2} but this is NOT required. This
 * class is thread safe.
 * </ul>
 *
 * @author Jesse
 */
public class DuplicatingFilterVisitor implements FilterVisitor, ExpressionVisitor {

    protected final FilterFactory2 ff;

    public DuplicatingFilterVisitor() {
        this(CommonFactoryFinder.getFilterFactory2(GeoTools.getDefaultHints()));
    }

    public DuplicatingFilterVisitor(FilterFactory2 factory) {
        this.ff = factory;
    }

    protected FilterFactory2 getFactory(Object extraData) {
        if (extraData instanceof FilterFactory2) return (FilterFactory2) extraData;
        return ff;
    }

    @Override
    public Object visit(ExcludeFilter filter, Object extraData) {
        return filter;
    }

    @Override
    public Object visit(IncludeFilter filter, Object extraData) {
        return filter;
    }

    /** Null safe expression cloning */
    protected Expression visit(Expression expression, Object extraData) {
        if (expression == null) return null;
        return (Expression) expression.accept(this, extraData);
    }

    @Override
    public Object visit(And filter, Object extraData) {
        List<Filter> children = filter.getChildren();
        List<Filter> newChildren = new ArrayList<>();
        for (Filter child : children) {
            if (child != null) {
                Filter newChild = (Filter) child.accept(this, extraData);
                newChildren.add(newChild);
            }
        }
        return getFactory(extraData).and(newChildren);
    }

    @Override
    public Object visit(Id filter, Object extraData) {
        return getFactory(extraData).id(filter.getIdentifiers());
    }

    @Override
    public Object visit(Not filter, Object extraData) {
        return getFactory(extraData).not((Filter) filter.getFilter().accept(this, extraData));
    }

    @Override
    public Object visit(Or filter, Object extraData) {
        List<Filter> children = filter.getChildren();
        List<Filter> newChildren = new ArrayList<>();
        for (Filter child : children) {
            if (child != null) {
                Filter newChild = (Filter) child.accept(this, extraData);
                newChildren.add(newChild);
            }
        }
        return getFactory(extraData).or(newChildren);
    }

    @Override
    public Object visit(PropertyIsBetween filter, Object extraData) {
        Expression expr = visit(filter.getExpression(), extraData);
        Expression lower = visit(filter.getLowerBoundary(), extraData);
        Expression upper = visit(filter.getUpperBoundary(), extraData);
        return getFactory(extraData).between(expr, lower, upper, filter.getMatchAction());
    }

    @Override
    public Object visit(PropertyIsEqualTo filter, Object extraData) {
        Expression expr1 = visit(filter.getExpression1(), extraData);
        Expression expr2 = visit(filter.getExpression2(), extraData);
        boolean matchCase = filter.isMatchingCase();
        return getFactory(extraData).equal(expr1, expr2, matchCase, filter.getMatchAction());
    }

    @Override
    public Object visit(PropertyIsNotEqualTo filter, Object extraData) {
        Expression expr1 = visit(filter.getExpression1(), extraData);
        Expression expr2 = visit(filter.getExpression2(), extraData);
        boolean matchCase = filter.isMatchingCase();
        return getFactory(extraData).notEqual(expr1, expr2, matchCase, filter.getMatchAction());
    }

    @Override
    public Object visit(PropertyIsGreaterThan filter, Object extraData) {
        Expression expr1 = visit(filter.getExpression1(), extraData);
        Expression expr2 = visit(filter.getExpression2(), extraData);
        return getFactory(extraData)
                .greater(expr1, expr2, filter.isMatchingCase(), filter.getMatchAction());
    }

    @Override
    public Object visit(PropertyIsGreaterThanOrEqualTo filter, Object extraData) {
        Expression expr1 = visit(filter.getExpression1(), extraData);
        Expression expr2 = visit(filter.getExpression2(), extraData);
        return getFactory(extraData)
                .greaterOrEqual(expr1, expr2, filter.isMatchingCase(), filter.getMatchAction());
    }

    @Override
    public Object visit(PropertyIsLessThan filter, Object extraData) {
        Expression expr1 = visit(filter.getExpression1(), extraData);
        Expression expr2 = visit(filter.getExpression2(), extraData);
        return getFactory(extraData)
                .less(expr1, expr2, filter.isMatchingCase(), filter.getMatchAction());
    }

    @Override
    public Object visit(PropertyIsLessThanOrEqualTo filter, Object extraData) {
        Expression expr1 = visit(filter.getExpression1(), extraData);
        Expression expr2 = visit(filter.getExpression2(), extraData);
        return getFactory(extraData)
                .lessOrEqual(expr1, expr2, filter.isMatchingCase(), filter.getMatchAction());
    }

    @Override
    public Object visit(PropertyIsLike filter, Object extraData) {
        Expression expr = visit(filter.getExpression(), extraData);
        String pattern = filter.getLiteral();
        String wildcard = filter.getWildCard();
        String singleChar = filter.getSingleChar();
        String escape = filter.getEscape();
        boolean matchCase = filter.isMatchingCase();
        return getFactory(extraData)
                .like(
                        expr,
                        pattern,
                        wildcard,
                        singleChar,
                        escape,
                        matchCase,
                        filter.getMatchAction());
    }

    @Override
    public Object visit(PropertyIsNull filter, Object extraData) {
        Expression expr = visit(filter.getExpression(), extraData);
        return getFactory(extraData).isNull(expr);
    }

    @Override
    public Object visit(PropertyIsNil filter, Object extraData) {
        Expression expr = visit(filter.getExpression(), extraData);
        return getFactory(extraData).isNil(expr, filter.getNilReason());
    }

    @Override
    public Object visit(BBOX filter, Object extraData) {
        Expression propertyName = visit(filter.getExpression1(), extraData);
        Expression box = visit(filter.getExpression2(), extraData);
        return getFactory(extraData).bbox(propertyName, box, filter.getMatchAction());
    }

    @Override
    public Object visit(Beyond filter, Object extraData) {
        Expression geometry1 = visit(filter.getExpression1(), extraData);
        Expression geometry2 = visit(filter.getExpression2(), extraData);
        double distance = filter.getDistance();
        String units = filter.getDistanceUnits();
        return getFactory(extraData)
                .beyond(geometry1, geometry2, distance, units, filter.getMatchAction());
    }

    @Override
    public Object visit(Contains filter, Object extraData) {
        Expression geometry1 = visit(filter.getExpression1(), extraData);
        Expression geometry2 = visit(filter.getExpression2(), extraData);
        return getFactory(extraData).contains(geometry1, geometry2, filter.getMatchAction());
    }

    @Override
    public Object visit(Crosses filter, Object extraData) {
        Expression geometry1 = visit(filter.getExpression1(), extraData);
        Expression geometry2 = visit(filter.getExpression2(), extraData);
        return getFactory(extraData).crosses(geometry1, geometry2, filter.getMatchAction());
    }

    @Override
    public Object visit(Disjoint filter, Object extraData) {
        Expression geometry1 = visit(filter.getExpression1(), extraData);
        Expression geometry2 = visit(filter.getExpression2(), extraData);
        return getFactory(extraData).disjoint(geometry1, geometry2, filter.getMatchAction());
    }

    @Override
    public Object visit(DWithin filter, Object extraData) {
        Expression geometry1 = visit(filter.getExpression1(), extraData);
        Expression geometry2 = visit(filter.getExpression2(), extraData);
        double distance = filter.getDistance();
        String units = filter.getDistanceUnits();
        return getFactory(extraData)
                .dwithin(geometry1, geometry2, distance, units, filter.getMatchAction());
    }

    @Override
    public Object visit(Equals filter, Object extraData) {
        Expression geometry1 = visit(filter.getExpression1(), extraData);
        Expression geometry2 = visit(filter.getExpression2(), extraData);
        return getFactory(extraData).equal(geometry1, geometry2, filter.getMatchAction());
    }

    @Override
    public Object visit(Intersects filter, Object extraData) {
        Expression geometry1 = visit(filter.getExpression1(), extraData);
        Expression geometry2 = visit(filter.getExpression2(), extraData);
        return getFactory(extraData).intersects(geometry1, geometry2, filter.getMatchAction());
    }

    @Override
    public Object visit(Overlaps filter, Object extraData) {
        Expression geometry1 = visit(filter.getExpression1(), extraData);
        Expression geometry2 = visit(filter.getExpression2(), extraData);
        return getFactory(extraData).overlaps(geometry1, geometry2, filter.getMatchAction());
    }

    @Override
    public Object visit(Touches filter, Object extraData) {
        Expression geometry1 = visit(filter.getExpression1(), extraData);
        Expression geometry2 = visit(filter.getExpression2(), extraData);
        return getFactory(extraData).touches(geometry1, geometry2, filter.getMatchAction());
    }

    @Override
    public Object visit(Within filter, Object extraData) {
        Expression geometry1 = visit(filter.getExpression1(), extraData);
        Expression geometry2 = visit(filter.getExpression2(), extraData);
        return getFactory(extraData).within(geometry1, geometry2, filter.getMatchAction());
    }

    @Override
    public Object visitNullFilter(Object extraData) {
        return null;
    }

    @Override
    public Object visit(NilExpression expression, Object extraData) {
        return expression;
    }

    @Override
    public Object visit(Add expression, Object extraData) {
        Expression expr1 = visit(expression.getExpression1(), extraData);
        Expression expr2 = visit(expression.getExpression2(), extraData);
        return getFactory(extraData).add(expr1, expr2);
    }

    @Override
    public Object visit(Divide expression, Object extraData) {
        Expression expr1 = visit(expression.getExpression1(), extraData);
        Expression expr2 = visit(expression.getExpression2(), extraData);
        return getFactory(extraData).divide(expr1, expr2);
    }

    @Override
    public Object visit(Function expression, Object extraData) {
        List<Expression> old = expression.getParameters();
        Expression[] args = new Expression[old.size()];
        int i = 0;
        for (Expression exp : old) {
            args[i++] = visit(exp, extraData);
        }
        Function duplicate;
        if (expression instanceof InternalFunction) {
            duplicate = ((InternalFunction) expression).duplicate(args);
        } else {
            duplicate = getFactory(extraData).function(expression.getName(), args);
        }
        return duplicate;
    }

    @Override
    public Object visit(Literal expression, Object extraData) {
        return getFactory(extraData).literal(expression.getValue());
    }

    @Override
    public Object visit(Multiply expression, Object extraData) {
        Expression expr1 = visit(expression.getExpression1(), extraData);
        Expression expr2 = visit(expression.getExpression2(), extraData);
        return getFactory(extraData).multiply(expr1, expr2);
    }

    @Override
    public Object visit(PropertyName expression, Object extraData) {
        // NC - namespace support
        return getFactory(extraData)
                .property(expression.getPropertyName(), expression.getNamespaceContext());
    }

    @Override
    public Object visit(Subtract expression, Object extraData) {
        Expression expr1 = visit(expression.getExpression1(), extraData);
        Expression expr2 = visit(expression.getExpression2(), extraData);
        return getFactory(extraData).subtract(expr1, expr2);
    }

    @Override
    public Object visit(After after, Object extraData) {
        return getFactory(extraData)
                .after(
                        visit(after.getExpression1(), extraData),
                        visit(after.getExpression2(), extraData),
                        after.getMatchAction());
    }

    @Override
    public Object visit(AnyInteracts anyInteracts, Object extraData) {
        return getFactory(extraData)
                .anyInteracts(
                        visit(anyInteracts.getExpression1(), extraData),
                        visit(anyInteracts.getExpression2(), extraData),
                        anyInteracts.getMatchAction());
    };

    @Override
    public Object visit(Before before, Object extraData) {
        return getFactory(extraData)
                .before(
                        visit(before.getExpression1(), extraData),
                        visit(before.getExpression2(), extraData),
                        before.getMatchAction());
    }

    @Override
    public Object visit(Begins begins, Object extraData) {
        return getFactory(extraData)
                .begins(
                        visit(begins.getExpression1(), extraData),
                        visit(begins.getExpression2(), extraData),
                        begins.getMatchAction());
    };

    @Override
    public Object visit(BegunBy begunBy, Object extraData) {
        return getFactory(extraData)
                .begunBy(
                        visit(begunBy.getExpression1(), extraData),
                        visit(begunBy.getExpression2(), extraData),
                        begunBy.getMatchAction());
    }

    @Override
    public Object visit(During during, Object extraData) {
        return getFactory(extraData)
                .during(
                        visit(during.getExpression1(), extraData),
                        visit(during.getExpression2(), extraData),
                        during.getMatchAction());
    }

    @Override
    public Object visit(EndedBy endedBy, Object extraData) {
        return getFactory(extraData)
                .endedBy(
                        visit(endedBy.getExpression1(), extraData),
                        visit(endedBy.getExpression2(), extraData),
                        endedBy.getMatchAction());
    }

    @Override
    public Object visit(Ends ends, Object extraData) {
        return getFactory(extraData)
                .ends(
                        visit(ends.getExpression1(), extraData),
                        visit(ends.getExpression2(), extraData),
                        ends.getMatchAction());
    }

    @Override
    public Object visit(Meets meets, Object extraData) {
        return getFactory(extraData)
                .meets(
                        visit(meets.getExpression1(), extraData),
                        visit(meets.getExpression2(), extraData),
                        meets.getMatchAction());
    }

    @Override
    public Object visit(MetBy metBy, Object extraData) {
        return getFactory(extraData)
                .metBy(
                        visit(metBy.getExpression1(), extraData),
                        visit(metBy.getExpression2(), extraData),
                        metBy.getMatchAction());
    }

    @Override
    public Object visit(OverlappedBy overlappedBy, Object extraData) {
        return getFactory(extraData)
                .overlappedBy(
                        visit(overlappedBy.getExpression1(), extraData),
                        visit(overlappedBy.getExpression2(), extraData),
                        overlappedBy.getMatchAction());
    }

    @Override
    public Object visit(TContains contains, Object extraData) {
        return getFactory(extraData)
                .tcontains(
                        visit(contains.getExpression1(), extraData),
                        visit(contains.getExpression2(), extraData),
                        contains.getMatchAction());
    }

    @Override
    public Object visit(TEquals equals, Object extraData) {
        return getFactory(extraData)
                .tequals(
                        visit(equals.getExpression1(), extraData),
                        visit(equals.getExpression2(), extraData),
                        equals.getMatchAction());
    }

    @Override
    public Object visit(TOverlaps contains, Object extraData) {
        return getFactory(extraData)
                .tcontains(
                        visit(contains.getExpression1(), extraData),
                        visit(contains.getExpression2(), extraData),
                        contains.getMatchAction());
    }

    @Override
    public Object visit(NativeFilter filter, Object extraData) {
        return getFactory(extraData).nativeFilter(filter.getNative());
    }
}
