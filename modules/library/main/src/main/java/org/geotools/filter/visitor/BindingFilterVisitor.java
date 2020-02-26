/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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

import java.util.Date;
import java.util.List;
import org.opengis.feature.type.FeatureType;
import org.opengis.filter.PropertyIsBetween;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.PropertyIsGreaterThan;
import org.opengis.filter.PropertyIsGreaterThanOrEqualTo;
import org.opengis.filter.PropertyIsLessThan;
import org.opengis.filter.PropertyIsLessThanOrEqualTo;
import org.opengis.filter.PropertyIsNotEqualTo;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.InternalFunction;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.temporal.After;
import org.opengis.filter.temporal.AnyInteracts;
import org.opengis.filter.temporal.Before;
import org.opengis.filter.temporal.Begins;
import org.opengis.filter.temporal.BegunBy;
import org.opengis.filter.temporal.During;
import org.opengis.filter.temporal.EndedBy;
import org.opengis.filter.temporal.Ends;
import org.opengis.filter.temporal.Meets;
import org.opengis.filter.temporal.MetBy;
import org.opengis.filter.temporal.OverlappedBy;
import org.opengis.filter.temporal.TContains;
import org.opengis.filter.temporal.TEquals;
import org.opengis.filter.temporal.TOverlaps;
import org.opengis.parameter.Parameter;
import org.opengis.temporal.Period;

/**
 * Binds all literals in the filter to the target type they are compared to, in order to avoid the
 * usage of converters on a evaluation by evaluation basis.
 *
 * @author Andrea Aime - GeoSolutions
 */
public class BindingFilterVisitor extends DuplicatingFilterVisitor {

    FeatureType schema;

    private ExpressionTypeVisitor expressionTypeVisitor;

    /** Evaluates the */
    public BindingFilterVisitor(FeatureType schema) {
        this.schema = schema;
        this.expressionTypeVisitor = new ExpressionTypeVisitor(schema);
    }

    public Object visit(PropertyIsEqualTo filter, Object extraData) {
        Class targetType = getTargetType(filter.getExpression1(), filter.getExpression2());
        Expression expr1 = optimize(filter.getExpression1(), extraData, targetType);
        Expression expr2 = optimize(filter.getExpression2(), extraData, targetType);
        boolean matchCase = filter.isMatchingCase();
        return getFactory(extraData).equal(expr1, expr2, matchCase, filter.getMatchAction());
    }

    @Override
    public Object visit(PropertyIsNotEqualTo filter, Object extraData) {
        Class targetType = getTargetType(filter.getExpression1(), filter.getExpression2());
        Expression expr1 = optimize(filter.getExpression1(), extraData, targetType);
        Expression expr2 = optimize(filter.getExpression2(), extraData, targetType);
        boolean matchCase = filter.isMatchingCase();
        return getFactory(extraData).notEqual(expr1, expr2, matchCase, filter.getMatchAction());
    }

    @Override
    public Object visit(PropertyIsBetween filter, Object extraData) {
        Class targetType =
                getTargetType(
                        filter.getLowerBoundary(),
                        filter.getExpression(),
                        filter.getUpperBoundary());
        Expression lb = optimize(filter.getLowerBoundary(), extraData, targetType);
        Expression ex = optimize(filter.getExpression(), extraData, targetType);
        Expression ub = optimize(filter.getUpperBoundary(), extraData, targetType);
        return getFactory(extraData).between(ex, lb, ub, filter.getMatchAction());
    }

    @Override
    public Object visit(PropertyIsGreaterThan filter, Object extraData) {
        Class targetType = getTargetType(filter.getExpression1(), filter.getExpression2());
        Expression expr1 = optimize(filter.getExpression1(), extraData, targetType);
        Expression expr2 = optimize(filter.getExpression2(), extraData, targetType);
        boolean matchCase = filter.isMatchingCase();
        return getFactory(extraData).greater(expr1, expr2, matchCase, filter.getMatchAction());
    }

    @Override
    public Object visit(PropertyIsGreaterThanOrEqualTo filter, Object extraData) {
        Class targetType = getTargetType(filter.getExpression1(), filter.getExpression2());
        Expression expr1 = optimize(filter.getExpression1(), extraData, targetType);
        Expression expr2 = optimize(filter.getExpression2(), extraData, targetType);
        boolean matchCase = filter.isMatchingCase();
        return getFactory(extraData)
                .greaterOrEqual(expr1, expr2, matchCase, filter.getMatchAction());
    }

    @Override
    public Object visit(PropertyIsLessThan filter, Object extraData) {
        Class targetType = getTargetType(filter.getExpression1(), filter.getExpression2());
        Expression expr1 = optimize(filter.getExpression1(), extraData, targetType);
        Expression expr2 = optimize(filter.getExpression2(), extraData, targetType);
        boolean matchCase = filter.isMatchingCase();
        return getFactory(extraData).less(expr1, expr2, matchCase, filter.getMatchAction());
    }

    @Override
    public Object visit(Function expression, Object extraData) {
        List old = expression.getParameters();
        FunctionName functionName = expression.getFunctionName();
        List<Parameter<?>> arguments = null;
        if (functionName != null) {
            arguments = functionName.getArguments();
        }
        Expression[] args = new Expression[old.size()];
        for (int i = 0; i < old.size(); i++) {
            Expression exp = (Expression) old.get(i);
            if (arguments != null && i < arguments.size()) {
                args[i] = optimize(exp, extraData, arguments.get(i).getType());
            } else {
                args[i] = visit(exp, extraData);
            }
        }
        Function duplicate;
        if (expression instanceof InternalFunction) {
            duplicate = ((InternalFunction) expression).duplicate(args);
        } else {
            duplicate = getFactory(extraData).function(expression.getName(), args);
        }
        return duplicate;
    }

    public Object visit(After after, Object extraData) {
        Expression expr1 = optimizeTime(after.getExpression1(), extraData);
        Expression expr2 = optimizeTime(after.getExpression2(), extraData);

        return getFactory(extraData).after(expr1, expr2, after.getMatchAction());
    }

    public Object visit(AnyInteracts anyInteracts, Object extraData) {
        Expression expr1 = optimizeTime(anyInteracts.getExpression1(), extraData);
        Expression expr2 = optimizeTime(anyInteracts.getExpression2(), extraData);

        return getFactory(extraData).anyInteracts(expr1, expr2, anyInteracts.getMatchAction());
    };

    public Object visit(Before before, Object extraData) {
        Expression expr1 = optimizeTime(before.getExpression1(), extraData);
        Expression expr2 = optimizeTime(before.getExpression2(), extraData);

        return getFactory(extraData).before(expr1, expr2, before.getMatchAction());
    }

    public Object visit(Begins begins, Object extraData) {
        Expression expr1 = optimizeTime(begins.getExpression1(), extraData);
        Expression expr2 = optimizeTime(begins.getExpression2(), extraData);

        return getFactory(extraData).begins(expr1, expr2, begins.getMatchAction());
    };

    public Object visit(BegunBy begunBy, Object extraData) {
        Expression expr1 = optimizeTime(begunBy.getExpression1(), extraData);
        Expression expr2 = optimizeTime(begunBy.getExpression2(), extraData);

        return getFactory(extraData).begins(expr1, expr2, begunBy.getMatchAction());
    }

    public Object visit(During during, Object extraData) {
        Expression expr1 = optimizeTime(during.getExpression1(), extraData);
        Expression expr2 = optimizeTime(during.getExpression2(), extraData);

        return getFactory(extraData).during(expr1, expr2, during.getMatchAction());
    }

    public Object visit(EndedBy endedBy, Object extraData) {
        Expression expr1 = optimizeTime(endedBy.getExpression1(), extraData);
        Expression expr2 = optimizeTime(endedBy.getExpression2(), extraData);

        return getFactory(extraData).endedBy(expr1, expr2, endedBy.getMatchAction());
    }

    public Object visit(Ends ends, Object extraData) {
        Expression expr1 = optimizeTime(ends.getExpression1(), extraData);
        Expression expr2 = optimizeTime(ends.getExpression2(), extraData);

        return getFactory(extraData).ends(expr1, expr2, ends.getMatchAction());
    }

    public Object visit(Meets meets, Object extraData) {
        Expression expr1 = optimizeTime(meets.getExpression1(), extraData);
        Expression expr2 = optimizeTime(meets.getExpression2(), extraData);

        return getFactory(extraData).meets(expr1, expr2, meets.getMatchAction());
    }

    public Object visit(MetBy metBy, Object extraData) {
        Expression expr1 = optimizeTime(metBy.getExpression1(), extraData);
        Expression expr2 = optimizeTime(metBy.getExpression2(), extraData);

        return getFactory(extraData).metBy(expr1, expr2, metBy.getMatchAction());
    }

    public Object visit(OverlappedBy overlappedBy, Object extraData) {
        Expression expr1 = optimizeTime(overlappedBy.getExpression1(), extraData);
        Expression expr2 = optimizeTime(overlappedBy.getExpression2(), extraData);

        return getFactory(extraData).overlappedBy(expr1, expr2, overlappedBy.getMatchAction());
    }

    public Object visit(TContains contains, Object extraData) {
        Expression expr1 = optimizeTime(contains.getExpression1(), extraData);
        Expression expr2 = optimizeTime(contains.getExpression2(), extraData);

        return getFactory(extraData).tcontains(expr1, expr2, contains.getMatchAction());
    }

    public Object visit(TEquals equals, Object extraData) {
        Expression expr1 = optimizeTime(equals.getExpression1(), extraData);
        Expression expr2 = optimizeTime(equals.getExpression2(), extraData);

        return getFactory(extraData).tequals(expr1, expr2, equals.getMatchAction());
    }

    public Object visit(TOverlaps contains, Object extraData) {
        Expression expr1 = optimizeTime(contains.getExpression1(), extraData);
        Expression expr2 = optimizeTime(contains.getExpression2(), extraData);

        return getFactory(extraData).toverlaps(expr1, expr2, contains.getMatchAction());
    }

    @Override
    public Object visit(PropertyIsLessThanOrEqualTo filter, Object extraData) {
        Class targetType = getTargetType(filter.getExpression1(), filter.getExpression2());
        Expression expr1 = optimize(filter.getExpression1(), extraData, targetType);
        Expression expr2 = optimize(filter.getExpression2(), extraData, targetType);
        boolean matchCase = filter.isMatchingCase();
        return getFactory(extraData).lessOrEqual(expr1, expr2, matchCase, filter.getMatchAction());
    }

    protected Expression optimize(Expression expression, Object extraData, Class targetType) {
        if (expression instanceof Literal && targetType != null) {
            // perform the conversion and return the optimized literal
            Object converted = expression.evaluate(null, targetType);
            if (converted != null) {
                return ff.literal(converted);
            }
        }

        // in case we could not optimize, just duplicate
        return visit(expression, extraData);
    }

    protected Expression optimizeTime(Expression expression, Object extraData) {
        if (expression instanceof Literal) {
            // time based operators try period first,
            Object converted = expression.evaluate(null, Period.class);
            if (converted != null) {
                return ff.literal(converted);
            }

            // ideally this would be Istant, but there is very little
            // in the GT codebase able to deal with instants, so we
            // convert to date, and accept a little overhead to convert from Date to Instant
            // (simple object wrapping, no complex parsing)
            converted = expression.evaluate(null, Date.class);
            if (converted != null) {
                return ff.literal(converted);
            }
        }

        // in case we could not optimize, just duplicate
        return visit(expression, extraData);
    }

    private Class getTargetType(Expression... expressions) {
        Class result = null;
        for (Expression expression : expressions) {
            if (!(expression instanceof Literal)) {
                Class target = (Class) expression.accept(expressionTypeVisitor, null);
                if (target == null) {
                    // could not find a target type, let the evaluation be dynamic
                    return target;
                } else if (result == null) {
                    result = target;
                } else if (!target.equals(result)) {
                    // if we have two inconsistent properties being
                    // compared, give up and handle this dynamically
                    return null;
                }
            }
        }

        return result;
    }
}
