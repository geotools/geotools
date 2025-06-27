/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2015, Open Source Geospatial Foundation (OSGeo)
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
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.FeatureType;
import org.geotools.api.feature.type.PropertyDescriptor;
import org.geotools.api.filter.And;
import org.geotools.api.filter.BinaryComparisonOperator;
import org.geotools.api.filter.BinaryLogicOperator;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.Id;
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
import org.geotools.api.filter.expression.Literal;
import org.geotools.api.filter.expression.Multiply;
import org.geotools.api.filter.expression.NilExpression;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.api.filter.expression.SimplifiableFunction;
import org.geotools.api.filter.expression.Subtract;
import org.geotools.api.filter.expression.VolatileFunction;
import org.geotools.api.filter.identity.FeatureId;
import org.geotools.api.filter.identity.GmlObjectId;
import org.geotools.api.filter.identity.Identifier;
import org.geotools.filter.FilterAttributeExtractor;

/**
 * Takes a filter and returns a simplified, equivalent one. At the moment the filter:
 *
 * <ul>
 *   <li>simplifies out {@link Filter#INCLUDE} and {@link Filter#EXCLUDE} in logical expressions
 *   <li>removes double logic negations
 *   <li>deal with FID filter validation removing invalid fids
 *   <li>optimize out all non volatile functions that do not happen to use attributes, replacing them with literals
 * </ul>
 *
 * <p>FID filter validation is meant to wipe out non valid feature ids from {@link Id} filters. This is so in order to
 * avoid sending feature ids down to DataStores that are not valid as per the specific FeatureType fid structure. Since
 * this is structure is usually DataStore specific, some times being a strategy based on how the feature type primary
 * key is generated, fid validation is abstracted out to the {@link FIDValidator} interface so when a DataStore is about
 * to send a query down to the backend it van provide this visitor with a validator specific for the feature type fid
 * structure being queried.
 *
 * <p>By default all feature ids are valid. DataStores that want non valid fids to be wiped out should set a
 * {@link FIDValidator} through the {@link #setFIDValidator(FIDValidator)} method.
 *
 * @author Andrea Aime - OpenGeo
 * @author Gabriel Roldan (OpenGeo)
 * @since 2.5.x
 * @version $Id$
 */
public class SimplifyingFilterVisitor extends DuplicatingFilterVisitor {

    /**
     * Defines a simple means of assessing whether a feature id in an {@link Id} filter is structurally valid and hence
     * can be send down to the backend with confidence it will not cause trouble, the most common one being filtering by
     * pk number even if the type name prefix does not match.
     */
    public static interface FIDValidator {
        public boolean isValid(String fid);
    }

    /** A 'null-object' fid validator that assumes any feature id in an {@link Id} filter is valid */
    public static final FIDValidator ANY_FID_VALID = fid -> true;

    /**
     * A FID validator that matches the fids with a given regular expression to determine the fid's validity.
     *
     * @author Gabriel Roldan (OpenGeo)
     */
    public static class RegExFIDValidator implements FIDValidator {

        private Pattern pattern;

        /** @param regularExpression a regular expression as used by the {@code java.util.regex} package */
        public RegExFIDValidator(String regularExpression) {
            pattern = Pattern.compile(regularExpression);
        }

        @Override
        public boolean isValid(String fid) {
            return pattern.matcher(fid).matches();
        }
    }

    /**
     * A convenient fid validator for the common case of a feature id being a composition of a
     * {@code <typename>.<number>}
     */
    public static class TypeNameDotNumberFidValidator extends RegExFIDValidator {
        /**
         * @param typeName the typename that will be used for a regular expression match in the form of
         *     {@code <typename>.<number>}
         */
        public TypeNameDotNumberFidValidator(final String typeName) {
            super(typeName + "\\.\\d+");
        }
    }

    FilterAttributeExtractor attributeExtractor = new FilterAttributeExtractor();

    protected FeatureType featureType;

    private FIDValidator fidValidator = ANY_FID_VALID;

    private boolean rangeSimplicationEnabled = false;

    public void setFIDValidator(FIDValidator validator) {
        this.fidValidator = validator == null ? ANY_FID_VALID : validator;
    }

    public void setFeatureType(FeatureType featureType) {
        this.featureType = featureType;
    }

    @Override
    public Object visit(And filter, Object extraData) {
        // drill down and flatten
        List<Filter> filters = collect(filter, And.class, extraData, new ArrayList<>());

        filters = basicAndSimplification(filters);

        filters = extraAndSimplification(extraData, filters);

        // we might end up with an empty list
        if (filters.isEmpty()) {
            return Filter.INCLUDE;
        }

        // remove the logic we have only one filter
        if (filters.size() == 1) {
            return filters.get(0);
        }

        return getFactory(extraData).and(filters);
    }

    protected List<Filter> basicAndSimplification(List<Filter> filters) {
        // perform range simplifications (by intersection), if possible
        if (rangeSimplicationEnabled && featureType != null && isSimpleFeature()) {
            RangeCombiner combiner = new RangeCombiner.And(ff, featureType, filters);
            filters = combiner.getReducedFilters();
        }

        // eliminate include and exclude
        List<Filter> simplified = new ArrayList<>(filters.size());
        for (Filter child : filters) {
            // if any of the child filters is exclude,
            // the whole chain of AND is equivalent to
            // EXCLUDE
            if (child == Filter.EXCLUDE) {
                return Arrays.asList(Filter.EXCLUDE);
            }

            // these can be skipped
            if (child == Filter.INCLUDE) {
                continue;
            }

            simplified.add(child);
        }

        // see if we have dual filters that can lead to Filter.Exclude, or duplicated filters
        for (int i = 0; i < simplified.size(); i++) {
            for (int j = i + 1; j < simplified.size(); ) {
                Filter f1 = simplified.get(i);
                Filter f2 = simplified.get(j);
                if (f1.equals(f2)) {
                    simplified.remove(j);
                } else if (dualFilters(f1, f2)) {
                    return Arrays.asList(Filter.EXCLUDE);
                } else {
                    j++;
                }
            }
        }
        return simplified;
    }

    protected <T extends BinaryLogicOperator> List<Filter> collect(
            T filter, Class<T> type, Object extraData, List<Filter> collected) {
        for (Filter child : filter.getChildren()) {
            if (type.isInstance(child)) {
                @SuppressWarnings("unchecked")
                T and = (T) child;
                collect(and, type, extraData, collected);
            } else {
                Filter cloned = (Filter) child.accept(this, extraData);
                if (type.isInstance(cloned)) {
                    @SuppressWarnings("unchecked")
                    T and = (T) cloned;
                    collect(and, type, extraData, collected);
                } else {
                    collected.add(cloned);
                }
            }
        }

        return collected;
    }

    /** Two filters are dual if the are the negation of each other (range based logic is handled separately) */
    private boolean dualFilters(Filter f1, Filter f2) {
        if (f1 instanceof Not) {
            Not not = (Not) f1;
            return f2.equals(not.getFilter());
        } else if (f2 instanceof Not) {
            Not not = (Not) f2;
            return f1.equals(not.getFilter());
        } else if (f1 instanceof PropertyIsEqualTo && f2 instanceof PropertyIsNotEqualTo
                || f1 instanceof PropertyIsNotEqualTo && f2 instanceof PropertyIsEqualTo) {
            PropertyIsEqualTo e;
            PropertyIsNotEqualTo ne;
            if (f2 instanceof PropertyIsEqualTo) {
                e = (PropertyIsEqualTo) f2;
                ne = (PropertyIsNotEqualTo) f1;
            } else {
                e = (PropertyIsEqualTo) f1;
                ne = (PropertyIsNotEqualTo) f2;
            }
            // the dual filter logic is correctly implemented only for single value attributes
            if (!isSimpleFeature()) {
                return false;
            } else {
                return e.getExpression1().equals(ne.getExpression1())
                                && e.getExpression2().equals(ne.getExpression2())
                        || e.getExpression2().equals(ne.getExpression1())
                                && e.getExpression1().equals(ne.getExpression2());
            }
        }

        return false;
    }

    @Override
    public Object visit(Or filter, Object extraData) {
        // scan, clone and simplify the children
        List<Filter> filters = collect(filter, Or.class, extraData, new ArrayList<>());

        filters = basicOrSimplification(filters);

        filters = extraOrSimplification(extraData, filters);

        // we might end up with an empty list
        if (filters.isEmpty()) {
            return Filter.EXCLUDE;
        }

        // remove the logic we have only one filter
        if (filters.size() == 1) {
            return filters.get(0);
        }

        // else return the cloned and simplified up list
        return getFactory(extraData).or(filters);
    }

    protected List<Filter> basicOrSimplification(List<Filter> filters) {
        // perform range simplifications (by intersection), if possible
        if (rangeSimplicationEnabled && featureType != null && isSimpleFeature()) {
            RangeCombiner combiner = new RangeCombiner.Or(ff, featureType, filters);
            filters = combiner.getReducedFilters();
        }

        // eliminate include and exclude
        List<Filter> simplified = new ArrayList<>(filters.size());
        for (Filter child : filters) {
            // if any of the child filters is INCLUDE,
            // the whole chain of OR is equivalent to
            // INCLUDE
            if (child == Filter.INCLUDE) {
                return Arrays.asList(Filter.INCLUDE);
            }

            // these can be skipped
            if (child == Filter.EXCLUDE) {
                continue;
            }

            simplified.add(child);
        }

        // see if we have dual filters that can lead to Filter.Exclude, or duplicated filters
        for (int i = 0; i < simplified.size(); i++) {
            for (int j = i + 1; j < simplified.size(); ) {
                Filter f1 = simplified.get(i);
                Filter f2 = simplified.get(j);
                if (f1.equals(f2)) {
                    simplified.remove(j);
                } else if (dualFilters(f1, f2)) {
                    return Arrays.asList(Filter.INCLUDE);
                } else {
                    j++;
                }
            }
        }
        return simplified;
    }

    protected List<Filter> extraAndSimplification(Object extraData, List<Filter> filters) {
        return filters;
    }

    protected List<Filter> extraOrSimplification(Object extraData, List<Filter> filters) {
        return filters;
    }

    /**
     * Uses the current {@link FIDValidator} to wipe out illegal feature ids from the returned filters.
     *
     * @return a filter containing only valid fids as per the current {@link FIDValidator}, may be
     *     {@link Filter#EXCLUDE} if none matches or the filter is already empty
     */
    @Override
    public Object visit(Id filter, Object extraData) {
        // if the set of ID is empty, it's actually equivalent to Filter.EXCLUDE
        if (filter.getIDs().isEmpty()) {
            return Filter.EXCLUDE;
        }

        Set<Identifier> validFids = new HashSet<>();

        for (Identifier id : filter.getIdentifiers()) {
            if (id instanceof FeatureId || id instanceof GmlObjectId) {
                // both FeatureId an GmlObjectId.getID() return String, but Identifier.getID()
                // returns Object. Yet, FeatureId and GmlObjectId are the only known subclasses of
                // Identifier that apply to Feature land
                if (fidValidator.isValid((String) id.getID())) {
                    validFids.add(id);
                }
            }
        }

        Filter validIdFilter;
        if (validFids.isEmpty()) {
            validIdFilter = Filter.EXCLUDE;
        } else {
            validIdFilter = getFactory(extraData).id(validFids);
        }
        return validIdFilter;
    }

    @Override
    public Object visit(Not filter, Object extraData) {
        FilterFactory ff = getFactory(extraData);
        Filter inner = filter.getFilter();
        if (inner instanceof Not) {
            // simplify out double negation
            Not innerNot = (Not) inner;
            return innerNot.getFilter().accept(this, extraData);
        } else if (inner instanceof And) {
            // De Morgan
            And and = (And) inner;
            List<Filter> children = and.getChildren();
            List<Filter> negatedChildren = new ArrayList<>();
            for (Filter child : children) {
                negatedChildren.add((Filter) ff.not(child).accept(this, extraData));
            }
            return ff.or(negatedChildren);
        } else if (inner instanceof Or) {
            // De Morgan
            Or or = (Or) inner;
            List<Filter> children = or.getChildren();
            List<Filter> negatedChildren = new ArrayList<>();
            for (Filter child : children) {
                negatedChildren.add((Filter) ff.not(child).accept(this, extraData));
            }
            return ff.and(negatedChildren);
        } else if (isSimpleFeature()) {
            Filter simplified = (Filter) inner.accept(this, extraData);
            if (simplified == Filter.INCLUDE) {
                return Filter.EXCLUDE;
            } else if (simplified == Filter.EXCLUDE) {
                return Filter.INCLUDE;
            } else if (simplified instanceof PropertyIsBetween) {
                List<Filter> orFilters = new ArrayList<>();
                PropertyIsBetween pb = (PropertyIsBetween) simplified;
                Filter lt = ff.less(pb.getExpression(), pb.getLowerBoundary());
                Filter gt = ff.greater(pb.getExpression(), pb.getUpperBoundary());
                orFilters.add(lt);
                orFilters.add(gt);
                PropertyName pn = (PropertyName) pb.getExpression();
                String pName = pn.getPropertyName();
                if (isNillable(pName)) {
                    // the property may have a NULL value, we need to vouch for it
                    orFilters.add(ff.isNull(pb.getExpression()));
                }
                return ff.or(orFilters);
            } else if (simplified instanceof PropertyIsEqualTo) {
                PropertyIsEqualTo pe = (PropertyIsEqualTo) simplified;
                return ff.notEqual(pe.getExpression1(), pe.getExpression2(), pe.isMatchingCase());
            } else if (simplified instanceof PropertyIsNotEqualTo) {
                PropertyIsNotEqualTo pe = (PropertyIsNotEqualTo) simplified;
                return ff.equal(pe.getExpression1(), pe.getExpression2(), pe.isMatchingCase());
            } else if (simplified instanceof PropertyIsGreaterThan) {
                PropertyIsGreaterThan pg = (PropertyIsGreaterThan) simplified;
                return ff.lessOrEqual(pg.getExpression1(), pg.getExpression2(), pg.isMatchingCase());
            } else if (simplified instanceof PropertyIsGreaterThanOrEqualTo) {
                PropertyIsGreaterThanOrEqualTo pg = (PropertyIsGreaterThanOrEqualTo) simplified;
                return ff.less(pg.getExpression1(), pg.getExpression2(), pg.isMatchingCase());
            } else if (simplified instanceof PropertyIsLessThan) {
                PropertyIsLessThan pl = (PropertyIsLessThan) simplified;
                return ff.greaterOrEqual(pl.getExpression1(), pl.getExpression2(), pl.isMatchingCase());
            } else if (simplified instanceof PropertyIsLessThanOrEqualTo) {
                PropertyIsLessThanOrEqualTo pl = (PropertyIsLessThanOrEqualTo) simplified;
                return ff.greater(pl.getExpression1(), pl.getExpression2(), pl.isMatchingCase());
            }
        }
        // fallback, cannot do anything "intelligent"
        return super.visit(filter, extraData);
    }

    /** Returns true if the target feature type is a simple feature one */
    protected boolean isSimpleFeature() {
        return featureType instanceof SimpleFeatureType;
    }

    @Override
    public Object visit(org.geotools.api.filter.expression.Function function, Object extraData) {
        // can't optimize out volatile functions
        if (isVolatileFunction(function)) {
            return super.visit(function, extraData);
        }

        // stable function, is it using attributes?
        if (attributeExtractor == null) {
            attributeExtractor = new FilterAttributeExtractor();
        } else {
            attributeExtractor.clear();
        }
        function.accept(attributeExtractor, null);

        // if so we can replace it with a literal
        if (attributeExtractor.isConstantExpression()) {
            Object result = function.evaluate(null);
            return ff.literal(result);
        }

        // perform simplifying copy, the arguments will be simplified if possible
        Object result = super.visit(function, extraData);

        // past that, we can try to ask the function to simplify itself
        if (result instanceof SimplifiableFunction) {
            return ((SimplifiableFunction) result).simplify(ff, featureType);
        }

        return result;
    }

    /**
     * Checks if a function is volatile in this context. By default it checks if the function implements the
     * {@link VolatileFunction} interface, subclasses can override
     */
    protected boolean isVolatileFunction(org.geotools.api.filter.expression.Function function) {
        return function instanceof VolatileFunction;
    }

    /** Tries to simplify the filter if it's not already a simple one. */
    public static Filter simplify(Filter filter) {
        return simplify(filter, null);
    }

    /** Tries to simplify the filter if it's not already a simple one */
    public static Filter simplify(Filter filter, FeatureType featureType) {
        // if already as simple as possible, or cannot be simplified anyways
        if (filter == Filter.INCLUDE || filter == Filter.EXCLUDE || filter == null) {
            return filter;
        }
        SimplifyingFilterVisitor visitor = new SimplifyingFilterVisitor();
        visitor.setFeatureType(featureType);
        return (Filter) filter.accept(visitor, null);
    }

    protected boolean isConstant(Expression ex) {
        // quick common cases first
        if (ex instanceof Literal) {
            return true;
        } else if (ex instanceof NilExpression) {
            return true;
        } else if (ex instanceof PropertyName) {
            return false;
        }
        // ok, check for attribute dependencies and volatile functions then
        attributeExtractor.clear();
        ex.accept(attributeExtractor, null);
        return attributeExtractor.isConstantExpression();
    }

    @Override
    public Object visit(PropertyIsBetween filter, Object extraData) {
        PropertyIsBetween clone = (PropertyIsBetween) super.visit(filter, extraData);
        if (isConstant(clone.getExpression())
                && isConstant(clone.getLowerBoundary())
                && isConstant(clone.getUpperBoundary())) {
            return staticFilterEvaluate(clone);
        } else {
            return clone;
        }
    }

    private Object staticFilterEvaluate(Filter filter) {
        if (filter.evaluate(null)) {
            return Filter.INCLUDE;
        } else {
            return Filter.EXCLUDE;
        }
    }

    @Override
    public Object visit(PropertyIsEqualTo filter, Object extraData) {
        return simplifyBinaryComparisonOperator((BinaryComparisonOperator) super.visit(filter, extraData));
    }

    private Object simplifyBinaryComparisonOperator(BinaryComparisonOperator clone) {
        if (isConstant(clone.getExpression1()) && isConstant(clone.getExpression2())) {
            return staticFilterEvaluate(clone);
        } else {
            return clone;
        }
    }

    @Override
    public Object visit(PropertyIsNotEqualTo filter, Object extraData) {
        return simplifyBinaryComparisonOperator((BinaryComparisonOperator) super.visit(filter, extraData));
    }

    @Override
    public Object visit(PropertyIsGreaterThan filter, Object extraData) {
        return simplifyBinaryComparisonOperator((BinaryComparisonOperator) super.visit(filter, extraData));
    }

    @Override
    public Object visit(PropertyIsGreaterThanOrEqualTo filter, Object extraData) {
        return simplifyBinaryComparisonOperator((BinaryComparisonOperator) super.visit(filter, extraData));
    }

    @Override
    public Object visit(PropertyIsLessThan filter, Object extraData) {
        return simplifyBinaryComparisonOperator((BinaryComparisonOperator) super.visit(filter, extraData));
    }

    @Override
    public Object visit(PropertyIsLessThanOrEqualTo filter, Object extraData) {
        return simplifyBinaryComparisonOperator((BinaryComparisonOperator) super.visit(filter, extraData));
    }

    @Override
    public Object visit(PropertyIsLike filter, Object extraData) {
        PropertyIsLike clone = (PropertyIsLike) super.visit(filter, extraData);
        if (isConstant(clone.getExpression())) {
            return staticFilterEvaluate(clone);
        } else {
            return clone;
        }
    }

    @Override
    public Object visit(PropertyIsNil filter, Object extraData) {
        PropertyIsNil clone = (PropertyIsNil) super.visit(filter, extraData);
        if (isConstant(clone.getExpression())) {
            return staticFilterEvaluate(clone);
        } else {
            return clone;
        }
    }

    @Override
    public Object visit(PropertyIsNull filter, Object extraData) {
        PropertyIsNull clone = (PropertyIsNull) super.visit(filter, extraData);
        if (isConstant(clone.getExpression())) {
            return staticFilterEvaluate(clone);
        } else {
            return clone;
        }
    }

    @Override
    public Object visit(Add expression, Object extraData) {
        Expression expr1 = visit(expression.getExpression1(), extraData);
        Expression expr2 = visit(expression.getExpression2(), extraData);
        Add result = getFactory(extraData).add(expr1, expr2);
        if (expr1 instanceof Literal && expr2 instanceof Literal) {
            return getFactory(extraData).literal(result.evaluate(null));
        }
        return result;
    }

    @Override
    public Object visit(Divide expression, Object extraData) {
        Expression expr1 = visit(expression.getExpression1(), extraData);
        Expression expr2 = visit(expression.getExpression2(), extraData);
        Divide result = getFactory(extraData).divide(expr1, expr2);
        if (expr1 instanceof Literal && expr2 instanceof Literal) {
            return getFactory(extraData).literal(result.evaluate(null));
        }
        return result;
    }

    @Override
    public Object visit(Subtract expression, Object extraData) {
        Expression expr1 = visit(expression.getExpression1(), extraData);
        Expression expr2 = visit(expression.getExpression2(), extraData);
        Subtract result = getFactory(extraData).subtract(expr1, expr2);
        if (expr1 instanceof Literal && expr2 instanceof Literal) {
            return getFactory(extraData).literal(result.evaluate(null));
        }
        return result;
    }

    @Override
    public Object visit(Multiply expression, Object extraData) {
        Expression expr1 = visit(expression.getExpression1(), extraData);
        Expression expr2 = visit(expression.getExpression2(), extraData);
        Multiply result = getFactory(extraData).multiply(expr1, expr2);
        if (expr1 instanceof Literal && expr2 instanceof Literal) {
            return getFactory(extraData).literal(result.evaluate(null));
        }
        return result;
    }

    public boolean isRangeSimplicationEnabled() {
        return rangeSimplicationEnabled;
    }

    /**
     * Enables/disable range simplification. Range simplification can figure out that the logic combination of multiple
     * ranges against the same property can be turned into a single range, a INCLUDE, or a EXCLUDE, but it requires the
     * range boundaries to be of the same type as the
     */
    public void setRangeSimplicationEnabled(boolean rangeSimplicationEnabled) {
        this.rangeSimplicationEnabled = rangeSimplicationEnabled;
    }

    /**
     * Returns if a property can contain null values, or not. If we don't have the featureType information, or we don't
     * know the property, we are going to assume the property is nillable to stay on the safe side
     */
    private boolean isNillable(String name) {
        if (featureType == null) {
            return true;
        }
        PropertyDescriptor descriptor = featureType.getDescriptor(name);
        return descriptor == null || descriptor.isNillable();
    }
}
