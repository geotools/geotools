/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.geotools.filter.FilterAttributeExtractor;
import org.opengis.feature.type.FeatureType;
import org.opengis.filter.And;
import org.opengis.filter.BinaryComparisonOperator;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.Id;
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
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.NilExpression;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.expression.VolatileFunction;
import org.opengis.filter.identity.FeatureId;
import org.opengis.filter.identity.GmlObjectId;
import org.opengis.filter.identity.Identifier;

/**
 * Takes a filter and returns a simplified, equivalent one. At the moment the filter:
 * <ul>
 * <li>simplifies out {@link Filter#INCLUDE} and {@link Filter#EXCLUDE} in logical expressions</li>
 * <li>removes double logic negations</li>
 * <li>deal with FID filter validation removing invalid fids</li>
 * <li>optimize out all non volatile functions that do not happen to use attributes, replacing them
 * with literals</li>
 * </ul>
 * <p>
 * FID filter validation is meant to wipe out non valid feature ids from {@link Id} filters. This is
 * so in order to avoid sending feature ids down to DataStores that are not valid as per the
 * specific FeatureType fid structure. Since this is structure is usually DataStore specific, some
 * times being a strategy based on how the feature type primary key is generated, fid validation is
 * abstracted out to the {@link FIDValidator} interface so when a DataStore is about to send a query
 * down to the backend it van provide this visitor with a validator specific for the feature type
 * fid structure being queried.
 * </p>
 * <p>
 * By default all feature ids are valid. DataStores that want non valid fids to be wiped out should
 * set a {@link FIDValidator} through the {@link #setFIDValidator(FIDValidator)} method.
 * </p>
 * 
 * @author Andrea Aime - OpenGeo
 * @author Gabriel Roldan (OpenGeo)
 * @since 2.5.x
 * @version $Id$
 * 
 * 
 * @source $URL$
 */
public class SimplifyingFilterVisitor extends DuplicatingFilterVisitor {

    /**
     * Defines a simple means of assessing whether a feature id in an {@link Id} filter is
     * structurally valid and hence can be send down to the backend with confidence it will not
     * cause trouble, the most common one being filtering by pk number even if the type name prefix
     * does not match.
     */
    public static interface FIDValidator {
        public boolean isValid(String fid);
    }

    /**
     * A 'null-object' fid validator that assumes any feature id in an {@link Id} filter is valid
     */
    public static final FIDValidator ANY_FID_VALID = new FIDValidator() {
        public boolean isValid(String fid) {
            return true;
        }
    };

    /**
     * A FID validator that matches the fids with a given regular expression to determine the fid's
     * validity.
     * 
     * @author Gabriel Roldan (OpenGeo)
     */
    public static class RegExFIDValidator implements FIDValidator {

        private Pattern pattern;

        /**
         * @param regularExpression a regular expression as used by the {@code java.util.regex}
         *        package
         */
        public RegExFIDValidator(String regularExpression) {
            pattern = Pattern.compile(regularExpression);
        }

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
         * @param typeName the typename that will be used for a regular expression match in the form
         *        of {@code <typename>.<number>}
         */
        public TypeNameDotNumberFidValidator(final String typeName) {
            super(typeName + "\\.\\d+");
        }
    }

    FilterAttributeExtractor attributeExtractor = new FilterAttributeExtractor();

    FeatureType featureType;

    private FIDValidator fidValidator = ANY_FID_VALID;

    public void setFIDValidator(FIDValidator validator) {
        this.fidValidator = validator == null ? ANY_FID_VALID : validator;
    }

    public void setFeatureType(FeatureType featureType) {
        this.featureType = featureType;
    }

    @Override
    public Object visit(And filter, Object extraData) {
        // drill down and flatten
        List<Filter> flattened = new ArrayList<Filter>(filter.getChildren().size());
        for (Filter child : filter.getChildren()) {
            Filter cloned = (Filter) child.accept(this, extraData);

            if (cloned instanceof And) {
                And and = (And) cloned;
                flattened.addAll(and.getChildren());
            } else {
                flattened.add(cloned);
            }
        }

        // if there are nested Ors, try distribution, see if this helps reduce the
        // overall expression
        for (int i = 0; i < flattened.size(); i++) {
            Filter f = flattened.get(i);
            if (f instanceof Or) {
                Or or = ((Or) f);
                Filter reduced = null;
                boolean twoOrMore = false;
                for (Filter child : or.getChildren()) {
                    List<Filter> newList = new ArrayList<>(flattened);
                    newList.remove(or);
                    newList.add(child);
                    And and = getFactory(extraData).and(newList);
                    Filter simplified = (Filter) and.accept(this, extraData);
                    if (simplified == Filter.EXCLUDE) {
                        continue;
                    } else if (simplified == Filter.INCLUDE) {
                        return Filter.INCLUDE;
                    } else if (reduced == null) {
                        reduced = simplified;
                    } else if (!simplified.equals(reduced)) {
                        twoOrMore = true;
                    }
                }

                if (reduced == null) {
                    return Filter.EXCLUDE;
                } else if (!twoOrMore) {
                    flattened.clear();
                    if (!(reduced instanceof And)) {
                        flattened.add(reduced);
                    } else {
                        flattened.addAll(((And) reduced).getChildren());
                    }
                    // this assumes we'll never stumble into a single children or,
                    // because those are simplified out at the beginning of this procedure
                    i = 0;
                }
            }
        }

        // perform range simplifications (by intersection), if possible
        if (featureType != null) {
            RangeCombiner combiner = new RangeCombiner.And(ff, featureType, flattened);
            flattened = combiner.getReducedFilters();
        }

        // eliminate include and exclude
        List<Filter> simplified = new ArrayList<Filter>(flattened.size());
        for (Filter child : flattened) {
            // if any of the child filters is exclude,
            // the whole chain of AND is equivalent to
            // EXCLUDE
            if (child == Filter.EXCLUDE) {
                return Filter.EXCLUDE;
            }

            // these can be skipped
            if (child == Filter.INCLUDE) {
                continue;
            }

            simplified.add(child);
        }

        // see if we have dual filters that can lead to Filter.Exclude, or duplicated filters
        for (int i = 0; i < simplified.size(); i++) {
            for (int j = i + 1; j < simplified.size();) {
                Filter f1 = simplified.get(i);
                Filter f2 = simplified.get(j);
                if (f1.equals(f2)) {
                    simplified.remove(j);
                } else if (dualFilters(f1, f2)) {
                    return Filter.EXCLUDE;
                } else {
                    j++;
                }
            }
        }

        // we might end up with an empty list
        if (simplified.size() == 0) {
            return Filter.INCLUDE;
        }


        // remove the logic we have only one filter
        if (simplified.size() == 1) {
            return simplified.get(0);
        }

        // else return the cloned and simplified up list
        return getFactory(extraData).and(simplified);
    }



    /**
     * Two filters are dual if the are the negation of each other (range based logic is handled
     * separately)
     * 
     * @param f1
     * @param f2
     * @return
     */
    private boolean dualFilters(Filter f1, Filter f2) {
        if (f1 instanceof Not) {
            Not not = (Not) f1;
            return f2.equals(not.getFilter());
        } else if (f2 instanceof Not) {
            Not not = (Not) f2;
            return f1.equals(not.getFilter());
        } else if ((f1 instanceof PropertyIsEqualTo && f2 instanceof PropertyIsNotEqualTo)
                || (f1 instanceof PropertyIsNotEqualTo && f2 instanceof PropertyIsEqualTo)) {
            PropertyIsEqualTo e;
            PropertyIsNotEqualTo ne;
            if (f2 instanceof PropertyIsEqualTo) {
                e = (PropertyIsEqualTo) f2;
                ne = (PropertyIsNotEqualTo) f1;
            } else {
                e = (PropertyIsEqualTo) f1;
                ne = (PropertyIsNotEqualTo) f2;
            }
            return (e.getExpression1().equals(ne.getExpression1()) && e.getExpression2().equals(
                    ne.getExpression2()))
                    || (e.getExpression2().equals(ne.getExpression1()) && e.getExpression1()
                            .equals(ne.getExpression2()));
        }

        return false;
    }

    @Override
    public Object visit(Or filter, Object extraData) {
        // scan, clone and simplify the children
        List<Filter> newChildren = new ArrayList<Filter>(filter.getChildren().size());
        for (Filter child : filter.getChildren()) {
            Filter cloned = (Filter) child.accept(this, extraData);

            // if any of the child filters is INCLUDE,
            // the whole chain of OR is equivalent to
            // INCLUDE
            if (cloned == Filter.INCLUDE) {
                return Filter.INCLUDE;
            }

            // these can be skipped
            if (cloned == Filter.EXCLUDE) {
                continue;
            }

            if (cloned instanceof Or) {
                Or or = (Or) cloned;
                newChildren.addAll(or.getChildren());
            } else {
                newChildren.add(cloned);
            }
        }
        
        // perform range simplifications (by intersection), if possible
        if (featureType != null) {
            RangeCombiner combiner = new RangeCombiner.Or(ff, featureType, newChildren);
            newChildren = combiner.getReducedFilters();
        }

        // see if we have dual filters that can lead to Filter.INCLUDE
        for (int i = 0; i < newChildren.size(); i++) {
            for (int j = i + 1; j < newChildren.size();) {
                Filter f1 = newChildren.get(i);
                Filter f2 = newChildren.get(j);
                if (f1.equals(f2)) {
                    newChildren.remove(j);
                } else if (dualFilters(f1, f2)) {
                    return Filter.INCLUDE;
                } else {
                    j++;
                }
            }
        }

        // we might end up with an empty list
        if (newChildren.size() == 0) {
            return Filter.EXCLUDE;
        }

        // remove the logic we have only one filter
        if (newChildren.size() == 1) {
            return newChildren.get(0);
        }

        // else return the cloned and simplified up list
        return getFactory(extraData).or(newChildren);
    }

    /**
     * Uses the current {@link FIDValidator} to wipe out illegal feature ids from the returned
     * filters.
     * 
     * @return a filter containing only valid fids as per the current {@link FIDValidator}, may be
     *         {@link Filter#EXCLUDE} if none matches or the filter is already empty
     */
    @Override
    public Object visit(Id filter, Object extraData) {
        // if the set of ID is empty, it's actually equivalent to Filter.EXCLUDE
        if (filter.getIDs().size() == 0) {
            return Filter.EXCLUDE;
        }

        Set<Identifier> validFids = new HashSet<Identifier>();

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
        if (validFids.size() == 0) {
            validIdFilter = Filter.EXCLUDE;
        } else {
            validIdFilter = getFactory(extraData).id(validFids);
        }
        return validIdFilter;
    }

    public Object visit(Not filter, Object extraData) {
        FilterFactory2 ff = getFactory(extraData);
        Filter inner = filter.getFilter();
		if (inner instanceof Not) {
            // simplify out double negation
            Not innerNot = (Not) inner;
            return innerNot.getFilter().accept(this, extraData);
        } else if(inner == Filter.INCLUDE) {
        	return Filter.EXCLUDE;
        } else if(inner == Filter.EXCLUDE) {
        	return Filter.INCLUDE;
        } else if (inner instanceof PropertyIsBetween) {
            PropertyIsBetween pb = (PropertyIsBetween) inner.accept(this, extraData);
            Filter lt = ff.less(pb.getExpression(), pb.getLowerBoundary());
            Filter gt = ff.greater(pb.getExpression(), pb.getUpperBoundary());
            return ff.or(lt, gt);
        } else if (inner instanceof PropertyIsEqualTo) {
            PropertyIsEqualTo pe = (PropertyIsEqualTo) inner.accept(this, extraData);
            return ff.notEqual(pe.getExpression1(), pe.getExpression2(), pe.isMatchingCase());
        } else if (inner instanceof PropertyIsNotEqualTo) {
            PropertyIsNotEqualTo pe = (PropertyIsNotEqualTo) inner.accept(this, extraData);
            return ff.equal(pe.getExpression1(), pe.getExpression2(), pe.isMatchingCase());
        } else if (inner instanceof PropertyIsGreaterThan) {
            PropertyIsGreaterThan pg = (PropertyIsGreaterThan) inner.accept(this, extraData);
            return ff.lessOrEqual(pg.getExpression1(), pg.getExpression2(), pg.isMatchingCase());
        } else if (inner instanceof PropertyIsGreaterThanOrEqualTo) {
            PropertyIsGreaterThanOrEqualTo pg = (PropertyIsGreaterThanOrEqualTo) inner.accept(this,
                    extraData);
            return ff.less(pg.getExpression1(), pg.getExpression2(), pg.isMatchingCase());
        } else if (inner instanceof PropertyIsLessThan) {
            PropertyIsLessThan pl = (PropertyIsLessThan) inner.accept(this, extraData);
            return ff.greaterOrEqual(pl.getExpression1(), pl.getExpression2(), pl.isMatchingCase());
        } else if (inner instanceof PropertyIsLessThanOrEqualTo) {
            PropertyIsLessThanOrEqualTo pl = (PropertyIsLessThanOrEqualTo) inner.accept(this,
                    extraData);
            return ff.greater(pl.getExpression1(), pl.getExpression2(), pl.isMatchingCase());
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
        } else {
            return super.visit(filter, extraData);
        }
    }

    public Object visit(org.opengis.filter.expression.Function function, Object extraData) {
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
        } else {
            return super.visit(function, extraData);
        }
    }

    /**
     * Checks if a function is volatile in this context. By default it checks if the function
     * implements the {@link VolatileFunction} interface, subclasses can override
     * 
     * @param function
     * @return
     */
    protected boolean isVolatileFunction(org.opengis.filter.expression.Function function) {
        return function instanceof VolatileFunction;
    }

    /**
     * Tries to simplify the filter if it's not already a simple one
     * 
     * @param filter
     * @return
     */
    public static Filter simplify(Filter filter) {
        // if already as simple as possible, or cannot be simplified anyways
        if (filter == Filter.INCLUDE || filter == Filter.EXCLUDE || filter == null) {
            return filter;
        }
        // other filters might involve non volatile functions, so we need to look into them
        SimplifyingFilterVisitor visitor = new SimplifyingFilterVisitor();
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

    public Object visit(PropertyIsBetween filter, Object extraData) {
        PropertyIsBetween clone = (PropertyIsBetween) super.visit(filter, extraData);
        if (isConstant(clone.getExpression()) && isConstant(clone.getLowerBoundary())
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

    public Object visit(PropertyIsEqualTo filter, Object extraData) {
        return simplifyBinaryComparisonOperator((BinaryComparisonOperator) super.visit(filter,
                extraData));
    }

    private Object simplifyBinaryComparisonOperator(BinaryComparisonOperator clone) {
        if (isConstant(clone.getExpression1()) && isConstant(clone.getExpression2())) {
            return staticFilterEvaluate(clone);
        } else {
            return clone;
        }
    }

    public Object visit(PropertyIsNotEqualTo filter, Object extraData) {
        return simplifyBinaryComparisonOperator((BinaryComparisonOperator) super.visit(filter,
                extraData));
    }

    public Object visit(PropertyIsGreaterThan filter, Object extraData) {
        return simplifyBinaryComparisonOperator((BinaryComparisonOperator) super.visit(filter,
                extraData));
    }

    public Object visit(PropertyIsGreaterThanOrEqualTo filter, Object extraData) {
        return simplifyBinaryComparisonOperator((BinaryComparisonOperator) super.visit(filter,
                extraData));
    }

    public Object visit(PropertyIsLessThan filter, Object extraData) {
        return simplifyBinaryComparisonOperator((BinaryComparisonOperator) super.visit(filter,
                extraData));
    }

    public Object visit(PropertyIsLessThanOrEqualTo filter, Object extraData) {
        return simplifyBinaryComparisonOperator((BinaryComparisonOperator) super.visit(filter,
                extraData));
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

}
