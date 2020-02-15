/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.opengis.filter.BinaryComparisonOperator;
import org.opengis.filter.Filter;
import org.opengis.filter.MultiValuedFilter;
import org.opengis.filter.MultiValuedFilter.MatchAction;
import org.opengis.filter.PropertyIsBetween;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.PropertyIsGreaterThan;
import org.opengis.filter.PropertyIsGreaterThanOrEqualTo;
import org.opengis.filter.PropertyIsLessThan;
import org.opengis.filter.PropertyIsLessThanOrEqualTo;
import org.opengis.filter.PropertyIsNotEqualTo;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.spatial.BBOX;
import org.opengis.filter.spatial.Beyond;
import org.opengis.filter.spatial.BinarySpatialOperator;
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
 * This visitor gets rid of equations that contain literals with multiple values (collections) and
 * creates instead multiple singe value equations, replacing the ANY, ALL, ONE logic by AND, OR, NOT
 * logic
 *
 * @author Niels Charlier
 */
public class LiteralDemultiplyingFilterVisitor extends DuplicatingFilterVisitor {

    /**
     * This interface is in support of a generic function (demultiply) that gets rid of the
     * multi-valued literals, with any type of filter that takes two expressions.
     */
    protected static interface FilterReplacer<F extends MultiValuedFilter> {

        public Expression getExpression1(F filter);

        public Expression getExpression2(F filter);

        /** Replace the expressions in a filter */
        public Filter replaceExpressions(F filter, Expression expression1, Expression expression2);
    }

    /**
     * An implementation for Binary Comparison Operators Takes the method name in the FilterFactory
     * to create the filter
     */
    protected class BinaryComparisonOperatorReplacer
            implements FilterReplacer<BinaryComparisonOperator> {

        protected Method method;

        public BinaryComparisonOperatorReplacer(String methodName) {

            try {
                method =
                        ff.getClass()
                                .getMethod(
                                        methodName,
                                        Expression.class,
                                        Expression.class,
                                        boolean.class,
                                        MatchAction.class);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public Expression getExpression1(BinaryComparisonOperator filter) {
            return filter.getExpression1();
        }

        @Override
        public Expression getExpression2(BinaryComparisonOperator filter) {
            return filter.getExpression2();
        }

        @Override
        public Filter replaceExpressions(
                BinaryComparisonOperator filter, Expression expression1, Expression expression2) {
            try {
                return (Filter)
                        method.invoke(
                                ff,
                                expression1,
                                expression2,
                                filter.isMatchingCase(),
                                filter.getMatchAction());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * An implementation for Binary Spatial Operators Takes the method name in the FilterFactory to
     * create the filter
     */
    protected class BinarySpatialOperatorReplacer implements FilterReplacer<BinarySpatialOperator> {

        protected Method method;

        public BinarySpatialOperatorReplacer(String methodName) {

            try {
                method =
                        ff.getClass()
                                .getMethod(
                                        methodName,
                                        Expression.class,
                                        Expression.class,
                                        MatchAction.class);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public Expression getExpression1(BinarySpatialOperator filter) {
            return filter.getExpression1();
        }

        @Override
        public Expression getExpression2(BinarySpatialOperator filter) {
            return filter.getExpression2();
        }

        @Override
        public Filter replaceExpressions(
                BinarySpatialOperator filter, Expression expression1, Expression expression2) {
            try {
                return (Filter)
                        method.invoke(ff, expression1, expression2, filter.getMatchAction());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * An implementation for Binary Temporal Operators Takes the method name in the FilterFactory to
     * create the filter
     */
    protected class BinaryTemporalOperatorReplacer
            implements FilterReplacer<BinaryTemporalOperator> {

        protected Method method;

        public BinaryTemporalOperatorReplacer(String methodName) {

            try {
                method =
                        ff.getClass()
                                .getMethod(
                                        methodName,
                                        Expression.class,
                                        Expression.class,
                                        MatchAction.class);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public Expression getExpression1(BinaryTemporalOperator filter) {
            return filter.getExpression1();
        }

        @Override
        public Expression getExpression2(BinaryTemporalOperator filter) {
            return filter.getExpression2();
        }

        @Override
        public Filter replaceExpressions(
                BinaryTemporalOperator filter, Expression expression1, Expression expression2) {
            try {
                return (Filter)
                        method.invoke(ff, expression1, expression2, filter.getMatchAction());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * demultiplies the first expression
     *
     * @param filter The filter
     * @param replacer The filter replacer
     * @return the new filter
     */
    protected <T extends MultiValuedFilter> Filter demultiplyFirst(
            T filter, FilterReplacer<T> replacer) {

        Expression one = replacer.getExpression1(filter);
        Expression two = replacer.getExpression2(filter);

        if (one instanceof Literal) {
            Literal l = (Literal) one;
            Object value = l.getValue();
            if (value instanceof Collection) { // demultiplying is necessary
                List<Filter> filters = new ArrayList<Filter>(); // list of all filters
                for (Object valueElement : (Collection) value) {
                    // create a single-valued new filter
                    filters.add(replacer.replaceExpressions(filter, ff.literal(valueElement), two));
                }
                // merge the filters depending on match action
                if (filter.getMatchAction() == MatchAction.ANY) {
                    return ff.or(filters);
                } else if (filter.getMatchAction() == MatchAction.ALL) {
                    return ff.and(filters);
                } else if (filter.getMatchAction() == MatchAction.ONE) {
                    List<Filter> filters2 = new ArrayList<Filter>();
                    for (int i = 0; i < filters.size(); i++) {
                        List<Filter> filters3 = new ArrayList<Filter>();
                        for (int j = 0; j < filters.size(); j++) {
                            if (i == j) {
                                filters3.add(filters.get(j));
                            } else {
                                filters3.add(ff.not(filters.get(j)));
                            }
                        }
                        filters2.add(ff.and(filters3));
                    }
                    return ff.or(filters2);
                }
            }
        }

        return filter;
    }

    /** Demultiplies first and second expression */
    protected <T extends MultiValuedFilter> Filter demultiply(
            T filter, FilterReplacer<T> replacer) {

        Expression one = replacer.getExpression1(filter);
        Expression two = replacer.getExpression2(filter);

        if (two instanceof Literal) {
            Literal l = (Literal) two;
            Object value = l.getValue();
            if (value instanceof Collection) { // demultiplying is necessary
                List<Filter> filters = new ArrayList<Filter>(); // list of all filters
                for (Object valueElement : (Collection) value) {
                    // create a single-valued new filter
                    filters.add(
                            demultiplyFirst(
                                    (T)
                                            replacer.replaceExpressions(
                                                    filter, one, ff.literal(valueElement)),
                                    replacer));
                }
                // merge the filters depending on match action
                if (filter.getMatchAction() == MatchAction.ANY) {
                    return ff.or(filters);
                } else if (filter.getMatchAction() == MatchAction.ALL) {
                    return ff.and(filters);
                } else if (filter.getMatchAction() == MatchAction.ONE) {
                    List<Filter> filters2 = new ArrayList<Filter>();
                    for (int i = 0; i < filters.size(); i++) {
                        List<Filter> filters3 = new ArrayList<Filter>();
                        for (int j = 0; j < filters.size(); j++) {
                            if (i == j) {
                                filters3.add(filters.get(j));
                            } else {
                                filters3.add(ff.not(filters.get(j)));
                            }
                        }
                        filters2.add(ff.and(filters3));
                    }
                    return ff.or(filters2);
                }
            }
        }

        return demultiplyFirst(filter, replacer);
    }

    @Override
    public Object visit(PropertyIsBetween filter, Object extraData) {
        // TODO: support ProperyIsBetween (there are three expressions here)
        return super.visit(filter, extraData);
    }

    @Override
    public Object visit(PropertyIsEqualTo filter, Object extraData) {
        return demultiply(filter, new BinaryComparisonOperatorReplacer("equal"));
    }

    @Override
    public Object visit(PropertyIsNotEqualTo filter, Object extraData) {
        return demultiply(filter, new BinaryComparisonOperatorReplacer("notEqual"));
    }

    @Override
    public Object visit(PropertyIsGreaterThan filter, Object extraData) {
        return demultiply(filter, new BinaryComparisonOperatorReplacer("greater"));
    }

    @Override
    public Object visit(PropertyIsGreaterThanOrEqualTo filter, Object extraData) {
        return demultiply(filter, new BinaryComparisonOperatorReplacer("greaterOrEqual"));
    }

    @Override
    public Object visit(PropertyIsLessThan filter, Object extraData) {
        return demultiply(filter, new BinaryComparisonOperatorReplacer("less"));
    }

    @Override
    public Object visit(PropertyIsLessThanOrEqualTo filter, Object extraData) {
        return demultiply(filter, new BinaryComparisonOperatorReplacer("lessOrEqual"));
    }

    @Override
    public Object visit(BBOX filter, Object extraData) {
        return demultiply(filter, new BinarySpatialOperatorReplacer("bbox"));
    }

    @Override
    public Object visit(Beyond filter, Object extraData) {
        return demultiply(
                filter,
                new FilterReplacer<
                        Beyond>() { // beyond filter takes extra properties, therefore needs its own
                    // filterreplacer

                    @Override
                    public Expression getExpression1(Beyond filter) {
                        return filter.getExpression1();
                    }

                    @Override
                    public Expression getExpression2(Beyond filter) {
                        return filter.getExpression2();
                    }

                    @Override
                    public Filter replaceExpressions(
                            Beyond filter, Expression expression1, Expression expression2) {
                        return ff.beyond(
                                expression1,
                                expression2,
                                filter.getDistance(),
                                filter.getDistanceUnits(),
                                filter.getMatchAction());
                    }
                });
    }

    @Override
    public Object visit(Contains filter, Object extraData) {
        return demultiply(filter, new BinarySpatialOperatorReplacer("contains"));
    }

    @Override
    public Object visit(Crosses filter, Object extraData) {
        return demultiply(filter, new BinarySpatialOperatorReplacer("crosses"));
    }

    @Override
    public Object visit(Disjoint filter, Object extraData) {
        return demultiply(filter, new BinarySpatialOperatorReplacer("disjoint"));
    }

    @Override
    public Object visit(DWithin filter, Object extraData) {
        return demultiply(
                filter,
                new FilterReplacer<
                        DWithin>() { // DWithin filter takes extra properties, therefore needs its
                    // own filterreplacer

                    @Override
                    public Expression getExpression1(DWithin filter) {
                        return filter.getExpression1();
                    }

                    @Override
                    public Expression getExpression2(DWithin filter) {
                        return filter.getExpression2();
                    }

                    @Override
                    public Filter replaceExpressions(
                            DWithin filter, Expression expression1, Expression expression2) {
                        return ff.dwithin(
                                expression1,
                                expression2,
                                filter.getDistance(),
                                filter.getDistanceUnits(),
                                filter.getMatchAction());
                    }
                });
    }

    @Override
    public Object visit(Equals filter, Object extraData) {
        return demultiply(filter, new BinarySpatialOperatorReplacer("equal"));
    }

    @Override
    public Object visit(Intersects filter, Object extraData) {
        return demultiply(filter, new BinarySpatialOperatorReplacer("intersects"));
    }

    @Override
    public Object visit(Overlaps filter, Object extraData) {
        return demultiply(filter, new BinarySpatialOperatorReplacer("overlaps"));
    }

    @Override
    public Object visit(Touches filter, Object extraData) {
        return demultiply(filter, new BinarySpatialOperatorReplacer("touches"));
    }

    @Override
    public Object visit(Within filter, Object extraData) {
        return demultiply(filter, new BinarySpatialOperatorReplacer("within"));
    }

    @Override
    public Object visit(After after, Object extraData) {
        return demultiply(after, new BinaryTemporalOperatorReplacer("after"));
    }

    @Override
    public Object visit(AnyInteracts anyInteracts, Object extraData) {
        return demultiply(anyInteracts, new BinaryTemporalOperatorReplacer("anyInteracts"));
    }

    @Override
    public Object visit(Before before, Object extraData) {
        return demultiply(before, new BinaryTemporalOperatorReplacer("before"));
    }

    @Override
    public Object visit(Begins begins, Object extraData) {
        return demultiply(begins, new BinaryTemporalOperatorReplacer("begins"));
    }

    @Override
    public Object visit(BegunBy begunBy, Object extraData) {
        return demultiply(begunBy, new BinaryTemporalOperatorReplacer("begunBy"));
    }

    @Override
    public Object visit(During during, Object extraData) {
        return demultiply(during, new BinaryTemporalOperatorReplacer("during"));
    }

    @Override
    public Object visit(EndedBy endedBy, Object extraData) {
        return demultiply(endedBy, new BinaryTemporalOperatorReplacer("endedBy"));
    }

    @Override
    public Object visit(Ends ends, Object extraData) {
        return demultiply(ends, new BinaryTemporalOperatorReplacer("ends"));
    }

    @Override
    public Object visit(Meets meets, Object extraData) {
        return demultiply(meets, new BinaryTemporalOperatorReplacer("meets"));
    }

    @Override
    public Object visit(MetBy metBy, Object extraData) {
        return demultiply(metBy, new BinaryTemporalOperatorReplacer("metBy"));
    }

    @Override
    public Object visit(OverlappedBy overlappedBy, Object extraData) {
        return demultiply(overlappedBy, new BinaryTemporalOperatorReplacer("overlappedBy"));
    }

    @Override
    public Object visit(TContains contains, Object extraData) {
        return demultiply(contains, new BinaryTemporalOperatorReplacer("tcontains"));
    }

    @Override
    public Object visit(TEquals equals, Object extraData) {
        return demultiply(equals, new BinaryTemporalOperatorReplacer("tequals"));
    }

    @Override
    public Object visit(TOverlaps overlaps, Object extraData) {
        return demultiply(overlaps, new BinaryTemporalOperatorReplacer("toverlaps"));
    }
}
