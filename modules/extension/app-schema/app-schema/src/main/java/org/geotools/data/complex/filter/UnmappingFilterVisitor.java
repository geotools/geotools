/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2011, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.data.complex.filter;

import static org.geotools.data.complex.util.ComplexFeatureConstants.DEFAULT_GEOMETRY_LOCAL_NAME;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import org.geotools.appschema.filter.NestedAttributeExpression;
import org.geotools.data.complex.AttributeMapping;
import org.geotools.data.complex.FeatureTypeMapping;
import org.geotools.data.complex.NestedAttributeMapping;
import org.geotools.data.complex.spi.CustomImplementationsFinder;
import org.geotools.data.complex.util.XPathUtil.Step;
import org.geotools.data.complex.util.XPathUtil.StepList;
import org.geotools.factory.CommonFactoryFinder;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.Name;
import org.opengis.filter.And;
import org.opengis.filter.BinaryComparisonOperator;
import org.opengis.filter.BinaryLogicOperator;
import org.opengis.filter.ExcludeFilter;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.Id;
import org.opengis.filter.IncludeFilter;
import org.opengis.filter.MultiValuedFilter.MatchAction;
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
import org.opengis.filter.expression.Add;
import org.opengis.filter.expression.BinaryExpression;
import org.opengis.filter.expression.Divide;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.ExpressionVisitor;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.Multiply;
import org.opengis.filter.expression.NilExpression;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.expression.Subtract;
import org.opengis.filter.identity.Identifier;
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
import org.xml.sax.helpers.NamespaceSupport;

/**
 * A Filter visitor that traverse a Filter or Expression made against a complex FeatureType, and
 * that uses the attribute and type mapping information given by a {@linkplain
 * org.geotools.data.complex.FeatureTypeMapping} object to produce an equivalent Filter that
 * operates against the original FeatureType.
 *
 * <p>Usage:
 *
 * <pre>
 * &lt;code&gt;
 *    Filter filterOnTargetType = ...
 *    FeatureTypeMappings schemaMapping = ....
 *
 *    UnMappingFilterVisitor visitor = new UnmappingFilterVisitor(schemaMapping);
 *    Filter filterOnSourceType = (Filter)filterOnTargetType.accept(visitor, null);
 *
 * &lt;/code&gt;
 * </pre>
 *
 * @author Gabriel Roldan (Axios Engineering)
 * @author Rini Angreani (CSIRO Earth Science and Resource Engineering)
 * @since 2.4
 */
public class UnmappingFilterVisitor implements org.opengis.filter.FilterVisitor, ExpressionVisitor {
    private static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(UnmappingFilterVisitor.class);

    protected FeatureTypeMapping mappings;

    private static final FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);

    /**
     * visit(*Expression) holds the unmapped expression here. Package visible just for unit tests
     */
    public UnmappingFilterVisitor(FeatureTypeMapping mappings) {
        this.mappings = mappings;
    }

    /**
     * Used by methods that visited a filter that produced one or more filters over the surrogate
     * feature type to combine them in an Or filter if necessary.
     */
    private Filter combineOred(List<Filter> combinedFilters) {
        switch (combinedFilters.size()) {
            case 0:
                throw new IllegalArgumentException("No filters to combine");
            case 1:
                return combinedFilters.get(0);
            default:
                return ff.or(combinedFilters);
        }
    }

    /**
     * Returns a CompareFilter of the same type than <code>filter</code>, but built on the unmapped
     * expressions pointing to the surrogate type attributes.
     *
     * @return the scalar product of the evaluation of both expressions
     */
    public Expression[][] visitBinaryComparisonOperator(BinaryComparisonOperator filter) {
        Expression left = filter.getExpression1();
        Expression right = filter.getExpression2();

        List leftExpressions = (List) left.accept(this, null);
        List rightExpressions = (List) right.accept(this, null);

        if (leftExpressions.isEmpty()) {
            throw new IllegalStateException(left + " mapping not found");
        }

        if (rightExpressions.isEmpty()) {
            throw new IllegalStateException(right + " mapping not found");
        }

        Expression[][] product = buildExpressionsMatrix(leftExpressions, rightExpressions);

        return product;
    }

    private Expression[][] buildExpressionsMatrix(List leftExpressions, List rightExpressions) {
        Expression left;
        Expression right;
        Expression[][] product =
                new Expression[leftExpressions.size() * rightExpressions.size()][2];

        int index = 0;
        for (Object leftExpression : leftExpressions) {
            left = (Expression) leftExpression;
            int rightIndex = 0;
            for (Object rightExpression : rightExpressions) {
                index = index + rightIndex;
                right = (Expression) rightExpression;
                product[index][0] = left;
                product[index][1] = right;
                rightIndex++;
            }
            index++;
        }
        return product;
    }

    public Expression[][] visitBinarySpatialOp(BinarySpatialOperator filter) {
        Expression left = filter.getExpression1();
        Expression right = filter.getExpression2();

        List leftExpressions = (List) left.accept(this, null);
        List rightExpressions = (List) right.accept(this, null);

        if (leftExpressions.isEmpty()) {
            throw new IllegalStateException(left + " mapping not found");
        }

        if (rightExpressions.isEmpty()) {
            throw new IllegalStateException(right + " mapping not found");
        }

        Expression[][] product = buildExpressionsMatrix(leftExpressions, rightExpressions);

        return product;
    }

    public List<Filter> visitBinaryLogicOp(BinaryLogicOperator filter) {

        List<Filter> unrolledFilers = new ArrayList<>();
        try {
            for (Filter next : filter.getChildren()) {
                Filter unrolled = (Filter) next.accept(this, null);
                unrolledFilers.add(unrolled);
            }
        } catch (Exception e) {
            throw (RuntimeException) new RuntimeException().initCause(e);
        }
        return unrolledFilers;
    }

    public Expression[][] visitBinaryExpression(BinaryExpression expression) {
        UnmappingFilterVisitor.LOGGER.finest(expression.toString());

        Expression left = expression.getExpression1();
        Expression right = expression.getExpression2();

        List leftExpressions = (List) left.accept(this, null);
        List rightExpressions = (List) right.accept(this, null);

        Expression[][] product = buildExpressionsMatrix(leftExpressions, rightExpressions);

        return product;
    }

    @Override
    public Object visit(ExcludeFilter filter, Object arg1) {
        return filter;
    }

    @Override
    public Object visit(IncludeFilter filter, Object arg1) {
        return filter;
    }

    @Override
    public Object visit(And filter, Object arg1) {
        List<Filter> list = visitBinaryLogicOp(filter);
        Filter unrolled = ff.and(list);
        return unrolled;
    }

    @Override
    public Object visit(Id filter, Object arg1) {
        Set fids = filter.getIdentifiers();

        Name target = mappings.getTargetFeature().getName();
        String namespace = target.getNamespaceURI();
        if (namespace == null) {
            namespace = XMLConstants.NULL_NS_URI;
        }
        String name = target.getLocalPart();

        Expression fidExpression = null;

        for (AttributeMapping attMapping : mappings.getAttributeMappings()) {
            StepList targetXPath = attMapping.getTargetXPath();
            if (targetXPath.size() > 1) {
                continue;
            }

            Step step = targetXPath.get(0);
            QName stepName = step.getName();
            if (namespace.equals(stepName.getNamespaceURI())) {
                if (name.equals(stepName.getLocalPart())) {
                    fidExpression = attMapping.getIdentifierExpression();
                    break;
                }
            }
        }

        if (fidExpression == null) {
            throw new IllegalStateException(
                    "No FID expression found for type "
                            + target
                            + ". Did you mean Expression.NIL?");
        }

        if (Expression.NIL.equals(fidExpression)) {
            return filter;
        }

        if (fidExpression instanceof Function) {
            Function fe = (Function) fidExpression;
            if ("getID".equalsIgnoreCase(fe.getName())) {
                LOGGER.finest("Fid mapping points to same ID as source");
                return filter;
            }
        }

        UnmappingFilterVisitor.LOGGER.finest("fid mapping expression is " + fidExpression);
        Filter unrolled = null;
        List<Filter> filters = new ArrayList<>();
        try {
            for (Object o : fids) {
                Identifier fid = (Identifier) o;
                Filter comparison =
                        UnmappingFilterVisitor.ff.equals(
                                fidExpression, UnmappingFilterVisitor.ff.literal(fid.toString()));
                UnmappingFilterVisitor.LOGGER.finest("Adding unmapped fid filter " + comparison);
                filters.add(comparison);
            }
            unrolled = UnmappingFilterVisitor.ff.or(filters);

        } catch (Exception e) {
            UnmappingFilterVisitor.LOGGER.log(Level.SEVERE, "unrolling " + filter, e);
            throw new RuntimeException(e.getMessage());
        }

        UnmappingFilterVisitor.LOGGER.finer("unrolled fid filter is " + unrolled);
        return unrolled;
    }

    @Override
    public Object visit(Not filter, Object arg1) {
        Filter unrolled = (Filter) filter.getFilter().accept(this, null);
        unrolled = ff.not(unrolled);
        return unrolled;
    }

    @Override
    public Object visit(Or filter, Object arg1) {
        List<Filter> list = visitBinaryLogicOp(filter);
        Filter unrolled = ff.or(list);
        return unrolled;
    }

    @Override
    public Object visit(PropertyIsBetween filter, Object arg1) {
        Expression expression = filter.getExpression();
        Expression lower = filter.getLowerBoundary();
        Expression upper = filter.getUpperBoundary();

        List expressions = (List) expression.accept(this, null);
        List lowerExpressions = (List) lower.accept(this, null);
        List upperExpressions = (List) upper.accept(this, null);

        final int combinedSize =
                expressions.size() * lowerExpressions.size() * upperExpressions.size();
        List<Filter> combinedFilters = new ArrayList<>(combinedSize);

        for (Object lowerExpression : lowerExpressions) {
            Expression floor = (Expression) lowerExpression;
            for (Object o : expressions) {
                Expression prop = (Expression) o;
                for (Object upperExpression : upperExpressions) {
                    Expression roof = (Expression) upperExpression;
                    Filter newFilter = ff.between(prop, floor, roof, filter.getMatchAction());
                    combinedFilters.add(newFilter);
                }
            }
        }

        Filter unrolled = combineOred(combinedFilters);
        return unrolled;
    }

    @Override
    public Object visit(PropertyIsEqualTo filter, Object arg1) {
        Expression[][] expressions = visitBinaryComparisonOperator(filter);

        List<Filter> combinedFilters = new ArrayList<>(expressions.length);

        for (Expression[] expression : expressions) {
            Expression left = expression[0];
            Expression right = expression[1];
            Filter unrolled =
                    ff.equal(left, right, filter.isMatchingCase(), filter.getMatchAction());
            combinedFilters.add(unrolled);
        }

        Filter unrolled = combineOred(combinedFilters);
        return unrolled;
    }

    @Override
    public Object visit(PropertyIsNotEqualTo filter, Object arg1) {
        Expression[][] expressions = visitBinaryComparisonOperator(filter);

        List<Filter> combinedFilters = new ArrayList<>(expressions.length);

        for (Expression[] expression : expressions) {
            Expression left = expression[0];
            Expression right = expression[1];
            Filter unrolled =
                    ff.notEqual(left, right, filter.isMatchingCase(), filter.getMatchAction());
            combinedFilters.add(unrolled);
        }

        Filter unrolled = combineOred(combinedFilters);
        return unrolled;
    }

    @Override
    public Object visit(PropertyIsGreaterThan filter, Object arg1) {
        Expression[][] expressions = visitBinaryComparisonOperator(filter);

        List<Filter> combinedFilters = new ArrayList<>(expressions.length);

        for (Expression[] expression : expressions) {
            Expression left = expression[0];
            Expression right = expression[1];
            Filter unrolled =
                    ff.greater(left, right, filter.isMatchingCase(), filter.getMatchAction());
            combinedFilters.add(unrolled);
        }

        Filter unrolled = combineOred(combinedFilters);
        return unrolled;
    }

    @Override
    public Object visit(PropertyIsGreaterThanOrEqualTo filter, Object arg1) {
        Expression[][] expressions = visitBinaryComparisonOperator(filter);

        List<Filter> combinedFilters = new ArrayList<>(expressions.length);

        for (Expression[] expression : expressions) {
            Expression left = expression[0];
            Expression right = expression[1];
            Filter unrolled =
                    ff.greaterOrEqual(
                            left, right, filter.isMatchingCase(), filter.getMatchAction());
            combinedFilters.add(unrolled);
        }

        Filter unrolled = combineOred(combinedFilters);
        return unrolled;
    }

    @Override
    public Object visit(PropertyIsLessThan filter, Object arg1) {
        Expression[][] expressions = visitBinaryComparisonOperator(filter);

        List<Filter> combinedFilters = new ArrayList<>(expressions.length);

        for (Expression[] expression : expressions) {
            Expression left = expression[0];
            Expression right = expression[1];
            Filter unrolled =
                    ff.less(left, right, filter.isMatchingCase(), filter.getMatchAction());
            combinedFilters.add(unrolled);
        }

        Filter unrolled = combineOred(combinedFilters);
        return unrolled;
    }

    @Override
    public Object visit(PropertyIsLessThanOrEqualTo filter, Object arg1) {
        Expression[][] expressions = visitBinaryComparisonOperator(filter);

        List<Filter> combinedFilters = new ArrayList<>(expressions.length);

        for (Expression[] expression : expressions) {
            Expression left = expression[0];
            Expression right = expression[1];
            Filter unrolled =
                    ff.lessOrEqual(left, right, filter.isMatchingCase(), filter.getMatchAction());
            combinedFilters.add(unrolled);
        }

        Filter unrolled = combineOred(combinedFilters);
        return unrolled;
    }

    @Override
    public Object visit(PropertyIsLike filter, Object arg1) {
        Expression value = filter.getExpression();
        @SuppressWarnings("unchecked")
        List<Expression> unrolledValues = (List) value.accept(this, null);

        String literal = filter.getLiteral();
        String wildcard = filter.getWildCard();
        String single = filter.getSingleChar();
        String escape = filter.getEscape();
        boolean matchCase = filter.isMatchingCase();
        MatchAction matchAction = filter.getMatchAction();

        List<Filter> combined = new ArrayList<>(unrolledValues.size());
        for (Expression sourceValue : unrolledValues) {
            Filter newFilter =
                    ff.like(sourceValue, literal, wildcard, single, escape, matchCase, matchAction);
            combined.add(newFilter);
        }
        Filter unrolled = combineOred(combined);
        return unrolled;
    }

    @Override
    public Object visit(PropertyIsNull filter, Object arg1) {
        Expression nullCheck = filter.getExpression();
        @SuppressWarnings("unchecked")
        List<Expression> sourceChecks = (List) nullCheck.accept(this, null);

        List<Filter> combined = new ArrayList<>(sourceChecks.size());

        for (Expression sourceValue : sourceChecks) {
            Filter newFilter = ff.isNull(sourceValue);
            combined.add(newFilter);
        }

        Filter unrolled = combineOred(combined);
        return unrolled;
    }

    @Override
    public Object visit(PropertyIsNil filter, Object extraData) {
        throw new UnsupportedOperationException("visit(PropertyIsNil filter, Object extraData)");
    }

    @Override
    public Object visit(BBOX filter, Object arg1) {
        String propertyName = ((PropertyName) filter.getExpression1()).getPropertyName();
        if (propertyName.length() < 1) {
            // see GetFeatureKvpRequestReader bboxFilter()
            // propertyName is meant to be empty, and it will get it from the feature
            // later (if not available, it will use feature's default geometry)
            // instead of always use the default geometry from the feature type
            return filter;
        }
        Expression name = ff.property(propertyName);
        @SuppressWarnings("unchecked")
        final List<Expression> sourceNames = (List) name.accept(this, null);

        final List<Filter> combined = new ArrayList<>(sourceNames.size());

        for (Expression sourceName : sourceNames) {
            Filter unrolled = ff.bbox(sourceName, filter.getBounds(), filter.getMatchAction());
            combined.add(unrolled);
        }

        Filter unrolled = combineOred(combined);

        return unrolled;
    }

    @Override
    public Object visit(Beyond filter, Object arg1) {
        Expression[][] exps = visitBinarySpatialOp(filter);

        List<Filter> combinedFilters = new ArrayList<>(exps.length);

        for (Expression[] exp : exps) {
            Expression left = exp[0];
            Expression right = exp[1];

            Filter unrolled =
                    ff.beyond(
                            left,
                            right,
                            filter.getDistance(),
                            filter.getDistanceUnits(),
                            filter.getMatchAction());
            combinedFilters.add(unrolled);
        }

        Filter unrolled = combineOred(combinedFilters);
        return unrolled;
    }

    @Override
    public Object visit(Contains filter, Object arg1) {
        Expression[][] exps = visitBinarySpatialOp(filter);

        List<Filter> combinedFilters = new ArrayList<>(exps.length);

        for (Expression[] exp : exps) {
            Expression left = exp[0];
            Expression right = exp[1];

            Filter unrolled = ff.contains(left, right, filter.getMatchAction());
            combinedFilters.add(unrolled);
        }

        Filter unrolled = combineOred(combinedFilters);
        return unrolled;
    }

    @Override
    public Object visit(Crosses filter, Object arg1) {
        Expression[][] exps = visitBinarySpatialOp(filter);

        List<Filter> combinedFilters = new ArrayList<>(exps.length);

        for (Expression[] exp : exps) {
            Expression left = exp[0];
            Expression right = exp[1];

            Filter unrolled = ff.crosses(left, right, filter.getMatchAction());
            combinedFilters.add(unrolled);
        }

        Filter unrolled = combineOred(combinedFilters);
        return unrolled;
    }

    @Override
    public Object visit(Disjoint filter, Object arg1) {
        Expression[][] exps = visitBinarySpatialOp(filter);

        List<Filter> combinedFilters = new ArrayList<>(exps.length);

        for (Expression[] exp : exps) {
            Expression left = exp[0];
            Expression right = exp[1];

            Filter unrolled = ff.disjoint(left, right, filter.getMatchAction());
            combinedFilters.add(unrolled);
        }

        Filter unrolled = combineOred(combinedFilters);
        return unrolled;
    }

    @Override
    public Object visit(DWithin filter, Object arg1) {
        Expression[][] exps = visitBinarySpatialOp(filter);

        List<Filter> combinedFilters = new ArrayList<>(exps.length);

        for (Expression[] exp : exps) {
            Expression left = exp[0];
            Expression right = exp[1];

            Filter unrolled =
                    ff.dwithin(
                            left,
                            right,
                            filter.getDistance(),
                            filter.getDistanceUnits(),
                            filter.getMatchAction());
            combinedFilters.add(unrolled);
        }

        Filter unrolled = combineOred(combinedFilters);
        return unrolled;
    }

    @Override
    public Object visit(Equals filter, Object arg1) {
        Expression[][] exps = visitBinarySpatialOp(filter);

        List<Filter> combinedFilters = new ArrayList<>(exps.length);

        for (Expression[] exp : exps) {
            Expression left = exp[0];
            Expression right = exp[1];

            Filter unrolled = ff.equal(left, right, filter.getMatchAction());
            combinedFilters.add(unrolled);
        }

        Filter unrolled = combineOred(combinedFilters);
        return unrolled;
    }

    @Override
    public Object visit(Intersects filter, Object arg1) {
        Expression[][] exps = visitBinarySpatialOp(filter);

        List<Filter> combinedFilters = new ArrayList<>(exps.length);

        for (Expression[] exp : exps) {
            Expression left = exp[0];
            Expression right = exp[1];

            Filter unrolled = ff.intersects(left, right, filter.getMatchAction());
            combinedFilters.add(unrolled);
        }

        Filter unrolled = combineOred(combinedFilters);
        return unrolled;
    }

    @Override
    public Object visit(Overlaps filter, Object arg1) {
        Expression[][] exps = visitBinarySpatialOp(filter);

        List<Filter> combinedFilters = new ArrayList<>(exps.length);

        for (Expression[] exp : exps) {
            Expression left = exp[0];
            Expression right = exp[1];

            Filter unrolled = ff.overlaps(left, right, filter.getMatchAction());
            combinedFilters.add(unrolled);
        }

        Filter unrolled = combineOred(combinedFilters);
        return unrolled;
    }

    @Override
    public Object visit(Touches filter, Object arg1) {
        Expression[][] exps = visitBinarySpatialOp(filter);

        List<Filter> combinedFilters = new ArrayList<>(exps.length);

        for (Expression[] exp : exps) {
            Expression left = exp[0];
            Expression right = exp[1];

            Filter unrolled = ff.touches(left, right, filter.getMatchAction());
            combinedFilters.add(unrolled);
        }

        Filter unrolled = combineOred(combinedFilters);
        return unrolled;
    }

    @Override
    public Object visit(Within filter, Object arg1) {
        Expression[][] exps = visitBinarySpatialOp(filter);

        List<Filter> combinedFilters = new ArrayList<>(exps.length);

        for (Expression[] exp : exps) {
            Expression left = exp[0];
            Expression right = exp[1];

            Filter unrolled = ff.within(left, right, filter.getMatchAction());
            combinedFilters.add(unrolled);
        }

        Filter unrolled = combineOred(combinedFilters);
        return unrolled;
    }

    @Override
    public Object visitNullFilter(Object arg0) {
        return Filter.EXCLUDE;
    }

    @Override
    public Object visit(NilExpression expr, Object arg1) {
        return Collections.singletonList(expr);
    }

    @Override
    public Object visit(Add expr, Object arg1) {
        Expression[][] expressions = visitBinaryExpression(expr);

        List<Expression> combinedExpressions = new ArrayList<>(expressions.length);

        for (Expression[] expression : expressions) {
            Expression left = expression[0];
            Expression right = expression[1];
            Expression sourceExpression = ff.add(left, right);
            combinedExpressions.add(sourceExpression);
        }

        return combinedExpressions;
    }

    @Override
    public Object visit(Divide expr, Object arg1) {
        Expression[][] expressions = visitBinaryExpression(expr);

        List<Expression> combinedExpressions = new ArrayList<>(expressions.length);

        for (Expression[] expression : expressions) {
            Expression left = expression[0];
            Expression right = expression[1];
            Expression sourceExpression = ff.divide(left, right);
            combinedExpressions.add(sourceExpression);
        }

        return combinedExpressions;
    }

    /**
     * @todo: support function arguments that map to more than one source expression. For example,
     *     if the argumen <code>gml:name</code> maps to source expressions <code>name</code> and
     *     <code>description</code> because the mapping has attribute mappings for both <code>
     *     gml:name[1] = name</code> and <code>gml:name[2] = description</code>.
     */
    @Override
    public Object visit(Function function, Object arg1) {

        final List<Expression> expressions = function.getParameters();

        List<Expression> arguments = new ArrayList<>(expressions.size());

        for (Expression mappingExpression : expressions) {
            List sourceExpressions = (List) mappingExpression.accept(this, null);
            if (sourceExpressions.size() > 1) {
                throw new UnsupportedOperationException(
                        "unrolling function arguments "
                                + "that map to more than one source expressions "
                                + "is not supported yet");
            }
            Expression unrolledExpression = (Expression) sourceExpressions.get(0);
            arguments.add(unrolledExpression);
        }

        Expression[] unmapped = new Expression[arguments.size()];
        unmapped = arguments.toArray(unmapped);

        Function unmappedFunction = ff.function(function.getName(), unmapped);

        return Collections.singletonList(unmappedFunction);
    }

    @Override
    public Object visit(Literal expr, Object arg1) {
        return Collections.singletonList(expr);
    }

    @Override
    public Object visit(Multiply expr, Object arg1) {
        Expression[][] expressions = visitBinaryExpression(expr);

        List<Expression> combinedExpressions = new ArrayList<>(expressions.length);

        for (Expression[] expression : expressions) {
            Expression left = expression[0];
            Expression right = expression[1];
            Expression sourceExpression = ff.multiply(left, right);
            combinedExpressions.add(sourceExpression);
        }

        return combinedExpressions;
    }

    @Override
    public List<Expression> visit(PropertyName expr, Object arg1) {

        String targetXPath = expr.getPropertyName();
        // replace the artificial DEFAULT_GEOMETRY property with the actual one
        if (DEFAULT_GEOMETRY_LOCAL_NAME.equals(targetXPath)) {
            targetXPath = mappings.getDefaultGeometryXPath();
        }

        NamespaceSupport namespaces = mappings.getNamespaces();
        AttributeDescriptor root = mappings.getTargetFeature();

        List<NestedAttributeMapping> nestedMappings = mappings.getNestedMappings();
        // break into single steps
        StepList simplifiedSteps = XPath.steps(root, targetXPath, namespaces);

        List<Expression> matchingMappings = mappings.findMappingsFor(simplifiedSteps, false);
        Iterator<Expression> it = matchingMappings.iterator();
        while (it.hasNext()) {
            if (it.next() == null) {
                // remove spurious null values, which are returned by findMappingsFor only to notify
                // the caller that joining for simple content should go to the post filter
                it.remove();
            }
        }

        if (!nestedMappings.isEmpty()) {
            // means some attributes are mapped separately in feature chaining
            for (NestedAttributeMapping nestedMapping : nestedMappings) {
                if (simplifiedSteps.startsWith(nestedMapping.getTargetXPath())) {
                    Expression nestedAttributeExpression =
                            CustomImplementationsFinder.find(
                                    mappings, simplifiedSteps, nestedMapping);
                    if (nestedAttributeExpression != null) {
                        matchingMappings.add(nestedAttributeExpression);
                    } else {
                        matchingMappings.add(
                                new NestedAttributeExpression(simplifiedSteps, nestedMapping));
                    }
                }
            }
        }

        matchingMappings.remove(Expression.NIL);

        if (matchingMappings.isEmpty()) {
            throw new IllegalArgumentException("Can't find source expression for: " + targetXPath);
        }

        return matchingMappings;
    }

    @Override
    public Object visit(Subtract expr, Object arg1) {
        Expression[][] expressions = visitBinaryExpression(expr);

        List<Expression> combinedExpressions = new ArrayList<>(expressions.length);

        for (Expression[] expression : expressions) {
            Expression left = expression[0];
            Expression right = expression[1];
            Expression sourceExpression = ff.subtract(left, right);
            combinedExpressions.add(sourceExpression);
        }

        return combinedExpressions;
    }

    // temporal filters
    @Override
    public Object visit(After after, Object extraData) {
        return visit((BinaryTemporalOperator) after, extraData);
    }

    @Override
    public Object visit(AnyInteracts anyInteracts, Object extraData) {
        return visit((BinaryTemporalOperator) anyInteracts, extraData);
    }

    @Override
    public Object visit(Before before, Object extraData) {
        return visit((BinaryTemporalOperator) before, extraData);
    }

    @Override
    public Object visit(Begins begins, Object extraData) {
        return visit((BinaryTemporalOperator) begins, extraData);
    }

    @Override
    public Object visit(BegunBy begunBy, Object extraData) {
        return visit((BinaryTemporalOperator) begunBy, extraData);
    }

    @Override
    public Object visit(During during, Object extraData) {
        return visit((BinaryTemporalOperator) during, extraData);
    }

    @Override
    public Object visit(EndedBy endedBy, Object extraData) {
        return visit((BinaryTemporalOperator) endedBy, extraData);
    }

    @Override
    public Object visit(Ends ends, Object extraData) {
        return visit((BinaryTemporalOperator) ends, extraData);
    }

    @Override
    public Object visit(Meets meets, Object extraData) {
        return visit((BinaryTemporalOperator) meets, extraData);
    }

    @Override
    public Object visit(MetBy metBy, Object extraData) {
        return visit((BinaryTemporalOperator) metBy, extraData);
    }

    @Override
    public Object visit(OverlappedBy overlappedBy, Object extraData) {
        return visit((BinaryTemporalOperator) overlappedBy, extraData);
    }

    @Override
    public Object visit(TContains contains, Object extraData) {
        return visit((BinaryTemporalOperator) contains, extraData);
    }

    @Override
    public Object visit(TEquals equals, Object extraData) {
        return visit((BinaryTemporalOperator) equals, extraData);
    }

    @Override
    public Object visit(TOverlaps contains, Object extraData) {
        return visit((BinaryTemporalOperator) contains, extraData);
    }

    protected Object visit(BinaryTemporalOperator filter, Object data) {
        throw new UnsupportedOperationException("Temporal filters not supported");
    }
}
