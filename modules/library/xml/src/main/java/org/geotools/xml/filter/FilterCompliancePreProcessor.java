/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.xml.filter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import org.geotools.api.filter.And;
import org.geotools.api.filter.BinaryLogicOperator;
import org.geotools.api.filter.ExcludeFilter;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
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
import org.geotools.api.filter.identity.FeatureId;
import org.geotools.api.filter.identity.Identifier;
import org.geotools.api.filter.spatial.BBOX;
import org.geotools.api.filter.spatial.Beyond;
import org.geotools.api.filter.spatial.Contains;
import org.geotools.api.filter.spatial.Crosses;
import org.geotools.api.filter.spatial.DWithin;
import org.geotools.api.filter.spatial.Disjoint;
import org.geotools.api.filter.spatial.DistanceBufferOperator;
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
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.FilterType;
import org.geotools.filter.IllegalFilterException;
import org.geotools.xml.XMLHandlerHints;

/**
 * Prepares a filter for xml encoded for interoperability with another system. It will behave differently depeding on
 * the compliance level chosen. A new request will have to be made and the features will have to be tested again on the
 * client side if there are any FidFilters in the filter. Consider the following to understand why:
 *
 * <pre>
 * and {
 *   nullFilter
 *   or{
 *    fidFilter
 *    nullFilter
 *   }
 * }
 * </pre>
 *
 * for strict it would throw an exception, for low it would be left alone, but for Medium it would end up as:
 *
 * <pre>
 * and{
 *         nullFilter
 *         nullFilter
 * }
 * </pre>
 *
 * and getFids() would return the fids in the fidFilter.
 *
 * <p>So the final filter would (this is not standard but a common implementation) return the results of the and filter
 * as well as all the features that match the fids. Which is more than the original filter would accept.
 *
 * <p>The XML Document writer can operate at different levels of compliance. The geotools level is extremely flexible
 * and forgiving.
 *
 * <p>All NOT(FidFilter) are changed to Filter.INCLUDE. So make sure that the filter is processed again on the client
 * with the original filter For a description of the difference Compliance levels that can be used see
 *
 * <ul>
 *   <li>{@link XMLHandlerHints#VALUE_FILTER_COMPLIANCE_LOW}
 *   <li>{@link XMLHandlerHints#VALUE_FILTER_COMPLIANCE_MEDIUM}
 *   <li>{@link XMLHandlerHints#VALUE_FILTER_COMPLIANCE_HIGH}
 * </ul>
 *
 * @author Jody
 */
public class FilterCompliancePreProcessor implements FilterVisitor {

    private static final int LOW = 0; // XMLHandlerHints.VALUE_FILTER_COMPLIANCE_LOW

    private static final int MEDIUM = 1; // XMLHandlerHints.VALUE_FILTER_COMPLIANCE_MEDIUM

    private static final int HIGH = 2; // XMLHandlerHints.VALUE_FILTER_COMPLIANCE_HIGH

    private int complianceInt;

    /** Data collected during traversal */
    private Stack<Data> current = new Stack<>();

    FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);

    private boolean requiresPostProcessing = false;

    public FilterCompliancePreProcessor(Integer complianceLevel) {
        if (complianceLevel != LOW && complianceLevel != MEDIUM && complianceLevel != HIGH) {
            throw new IllegalArgumentException(
                    "compliance level must be one of: XMLHandlerHints.VALUE_FILTER_COMPLIANCE_LOOSE "
                            + "XMLHandlerHints.VALUE_FILTER_COMPLIANCE_MEDIUM or "
                            + "XMLHandlerHints.VALUE_FILTER_COMPLIANCE_MAXIMUM");
        }
        this.complianceInt = complianceLevel.intValue();
    }

    /**
     * Gets the fid filter that contains all the fids.
     *
     * @return the fid filter that contains all the fids.
     */
    public Id getFidFilter() {
        if (current.isEmpty()) {
            Set<FeatureId> empty = Collections.emptySet();
            return ff.id(empty);
        }
        Data data = current.peek();
        if (!data.fids.isEmpty()) {
            Set<FeatureId> set = new HashSet<>();
            Set<String> fids = data.fids;
            for (String fid : fids) {
                set.add(ff.featureId(fid));
            }
            return ff.id(set);
        } else {
            Set<FeatureId> empty = Collections.emptySet();
            return ff.id(empty);
        }
    }

    /**
     * Returns the filter that can be encoded.
     *
     * @return the filter that can be encoded.
     */
    public org.geotools.api.filter.Filter getFilter() {
        if (current.isEmpty()) {
            return Filter.EXCLUDE;
        }
        return this.current.peek().filter;
    }

    // between
    @Override
    public Object visit(PropertyIsBetween filter, Object extraData) {
        current.push(new Data(filter));
        return extraData;
    }

    // BinaryComparisonOperator
    @Override
    public Object visit(PropertyIsEqualTo filter, Object extraData) {
        current.push(new Data(filter));
        return extraData;
    }

    @Override
    public Object visit(PropertyIsGreaterThan filter, Object extraData) {
        current.push(new Data(filter));
        return extraData;
    }

    @Override
    public Object visit(PropertyIsGreaterThanOrEqualTo filter, Object extraData) {
        current.push(new Data(filter));
        return extraData;
    }

    @Override
    public Object visit(PropertyIsLessThan filter, Object extraData) {
        current.push(new Data(filter));
        return extraData;
    }

    @Override
    public Object visit(PropertyIsLessThanOrEqualTo filter, Object extraData) {
        current.push(new Data(filter));
        return extraData;
    }

    @Override
    public Object visit(PropertyIsNotEqualTo filter, Object extraData) {
        current.push(new Data(filter));
        return extraData;
    }

    // GeometryFilter
    @Override
    public Object visit(BBOX filter, Object extraData) {
        current.push(new Data(filter));
        return extraData;
    }

    @Override
    public Object visit(Contains filter, Object extraData) {
        current.push(new Data(filter));
        return extraData;
    }

    @Override
    public Object visit(Crosses filter, Object extraData) {
        current.push(new Data(filter));
        return extraData;
    }

    @Override
    public Object visit(Disjoint filter, Object extraData) {
        current.push(new Data(filter));
        return extraData;
    }

    public Object visit(DistanceBufferOperator filter, Object extraData) {
        current.push(new Data(filter));
        return extraData;
    }

    @Override
    public Object visit(Equals filter, Object extraData) {
        current.push(new Data(filter));
        return extraData;
    }

    @Override
    public Object visit(Intersects filter, Object extraData) {
        current.push(new Data(filter));
        return extraData;
    }

    @Override
    public Object visit(Overlaps filter, Object extraData) {
        current.push(new Data(filter));
        return extraData;
    }

    @Override
    public Object visit(Touches filter, Object extraData) {
        current.push(new Data(filter));
        return extraData;
    }

    @Override
    public Object visit(Within filter, Object extraData) {
        current.push(new Data(filter));
        return extraData;
    }

    @Override
    public Object visit(Beyond filter, Object extraData) {
        current.push(new Data(filter));
        return extraData;
    }

    @Override
    public Object visit(DWithin filter, Object extraData) {
        current.push(new Data(filter));
        return extraData;
    }

    // LikeFilter
    @Override
    public Object visit(PropertyIsLike filter, Object extraData) {
        current.push(new Data(filter));
        return extraData;
    }

    // LogicFilter
    @Override
    public Object visit(And filter, Object extraData) {
        int startSize = current.size();
        try {
            switch (this.complianceInt) {
                case FilterCompliancePreProcessor.LOW:
                    current.push(new Data(filter));
                    break;

                case MEDIUM:
                    for (Filter child : filter.getChildren()) {
                        extraData = child.accept(this, extraData);
                    }
                    Data mediumFilter = createMediumLevelLogicFilter(FilterType.LOGIC_AND, startSize);
                    current.push(mediumFilter);
                    break;

                case HIGH:
                    for (Filter child : filter.getChildren()) {
                        extraData = child.accept(this, extraData);
                    }
                    Data highFilter = createHighLevelLogicFilter(FilterType.LOGIC_AND, startSize);
                    current.push(highFilter);

                    break;

                default:
                    break;
            }
        } catch (Exception e) {
            if (e instanceof UnsupportedFilterException) {
                throw (UnsupportedFilterException) e;
            }
            throw new UnsupportedFilterException("Exception creating filter", e);
        }
        return extraData;
    }

    @Override
    public Object visit(Or filter, Object extraData) {
        int startSize = current.size();
        try {
            switch (this.complianceInt) {
                case LOW:
                    current.push(new Data(filter));
                    break;

                case MEDIUM:
                    for (Filter child : filter.getChildren()) {
                        extraData = child.accept(this, extraData);
                    }
                    Data mediumFilter = createMediumLevelLogicFilter(FilterType.LOGIC_OR, startSize);
                    current.push(mediumFilter);
                    break;

                case HIGH:
                    for (Filter child : filter.getChildren()) {
                        extraData = child.accept(this, extraData);
                    }
                    Data highFilter = createHighLevelLogicFilter(FilterType.LOGIC_OR, startSize);
                    current.push(highFilter);

                    break;

                default:
                    break;
            }
        } catch (Exception e) {
            if (e instanceof UnsupportedFilterException) {
                throw (UnsupportedFilterException) e;
            }
            throw new UnsupportedFilterException("Exception creating filter", e);
        }
        return extraData;
    }

    @Override
    public Object visit(Not filter, Object extraData) {
        int startSize = 1;
        Filter child;
        try {
            switch (this.complianceInt) {
                case LOW:
                    current.push(new Data(filter));
                    break;

                case MEDIUM:
                    child = filter.getFilter();
                    extraData = child.accept(this, extraData);

                    Data mediumFilter = createMediumLevelLogicFilter(FilterType.LOGIC_NOT, startSize);
                    current.push(mediumFilter);
                    break;

                case HIGH:
                    child = filter.getFilter();
                    extraData = child.accept(this, extraData);

                    Data highFilter = createHighLevelLogicFilter(FilterType.LOGIC_NOT, startSize);
                    current.push(highFilter);

                    break;

                default:
                    break;
            }
        } catch (Exception e) {
            if (e instanceof UnsupportedFilterException) {
                throw (UnsupportedFilterException) e;
            }
            throw new UnsupportedFilterException("Exception creating filter", e);
        }
        return extraData;
    }

    private Data createMediumLevelLogicFilter(short filterType, int startOfFilterStack) throws IllegalFilterException {
        Data resultingFilter;

        switch (filterType) {
            case FilterType.LOGIC_AND: {
                Set<String> fids = andFids(startOfFilterStack);
                resultingFilter = buildFilter(filterType, startOfFilterStack);
                resultingFilter.fids.addAll(fids);

                if (resultingFilter.filter != Filter.EXCLUDE && !fids.isEmpty()) requiresPostProcessing = true;
                break;
            }

            case FilterType.LOGIC_OR: {
                Set<String> fids = orFids(startOfFilterStack);
                resultingFilter = buildFilter(filterType, startOfFilterStack);
                resultingFilter.fids.addAll(fids);
                break;
            }

            case FilterType.LOGIC_NOT:
                resultingFilter = buildFilter(filterType, startOfFilterStack);
                break;

            default:
                resultingFilter = buildFilter(filterType, startOfFilterStack);

                break;
        }

        return resultingFilter;
    }

    private Set<String> orFids(int startOfFilterStack) {
        Set<String> set = new HashSet<>();

        for (int i = startOfFilterStack; i < current.size(); i++) {
            Data data = current.get(i);

            if (!data.fids.isEmpty()) {
                set.addAll(data.fids);
            }
        }

        return set;
    }

    private Set<String> andFids(int startOfFilterStack) {
        if (!hasFidFilter(startOfFilterStack)) {
            return Collections.emptySet();
        }

        Set<Data> toRemove = new HashSet<>();
        List<Set<String>> fidSet = new ArrayList<>();
        boolean doRemove = true;

        for (int i = startOfFilterStack; i < current.size(); i++) {
            Data data = current.get(i);

            if (data.fids.isEmpty()) {
                toRemove.add(data);
            } else {
                fidSet.add(data.fids);
                if (data.filter != Filter.EXCLUDE) {
                    doRemove = false;
                }
            }
        }

        if (doRemove) {
            current.removeAll(toRemove);
        }

        if (fidSet.isEmpty()) {
            return Collections.emptySet();
        }

        if (fidSet.size() == 1) {
            return fidSet.get(0);
        }

        HashSet<String> set = new HashSet<>();

        for (Set<String> tmp : fidSet) {
            for (String fid : tmp) {
                if (allContain(fid, fidSet)) {
                    set.add(fid);
                }
            }
        }

        return set;
    }

    private boolean allContain(String fid, List<Set<String>> fidSets) {
        for (Set<String> tmp : fidSets) {
            if (!tmp.contains(fid)) {
                return false;
            }
        }

        return true;
    }
    /**
     * @param filterType LOGIC_NOT, LOGIC_AND or LOGIC_OR
     * @return Data Stack data representing the genrated filter
     */
    private Data buildFilter(short filterType, int startOfFilterStack) throws IllegalFilterException {
        if (current.isEmpty()) {
            return Data.ALL;
        }

        if (filterType == FilterType.LOGIC_NOT) {
            return buildNotFilter(startOfFilterStack);
        }

        if (current.size() == startOfFilterStack + 1) {
            return current.pop();
        }

        List<Filter> filterList = new ArrayList<>();

        while (current.size() > startOfFilterStack) {
            Data data = current.pop();
            if (data.filter != Filter.EXCLUDE) {
                filterList.add(data.filter);
            }
        }

        Filter f;
        if (filterType == FilterType.LOGIC_AND) {
            f = ff.and(filterList);
        } else if (filterType == FilterType.LOGIC_OR) {
            f = ff.or(filterList);
        } else {
            // not expected
            f = null;
        }
        return new Data(compressFilter(filterType, f));
    }

    private Filter compressFilter(short filterType, Filter f) throws IllegalFilterException {
        Filter result;
        int added = 0;
        List<org.geotools.api.filter.Filter> resultList = new ArrayList<>();

        switch (filterType) {
            case FilterType.LOGIC_AND:
                if (contains((And) f, Filter.EXCLUDE)) {
                    return Filter.EXCLUDE;
                }

                for (Filter item : ((And) f).getChildren()) {
                    org.geotools.api.filter.Filter filter = item;
                    if (filter == Filter.INCLUDE) {
                        continue;
                    }
                    added++;
                    resultList.add(filter);
                }

                if (resultList.isEmpty()) {
                    return Filter.EXCLUDE;
                }

                result = ff.and(resultList);
                break;

            case FilterType.LOGIC_OR:
                if (contains((Or) f, Filter.INCLUDE)) {
                    return Filter.INCLUDE;
                }

                for (Object item : ((Or) f).getChildren()) {
                    org.geotools.api.filter.Filter filter = (org.geotools.api.filter.Filter) item;
                    if (filter == org.geotools.api.filter.Filter.EXCLUDE) {
                        continue;
                    }
                    added++;
                    resultList.add(filter);
                }

                if (resultList.isEmpty()) {
                    return Filter.EXCLUDE;
                }

                result = ff.or(resultList);

                break;

            default:
                return Filter.EXCLUDE;
        }

        switch (added) {
            case 0:
                return Filter.EXCLUDE;

            case 1:
                if (result instanceof Not) {
                    return ((Not) result).getFilter();
                } else {
                    return ((BinaryLogicOperator) result).getChildren().get(0);
                }
            default:
                return result;
        }
    }

    private boolean contains(BinaryLogicOperator f, org.geotools.api.filter.Filter toFind) {
        for (Filter filter : f.getChildren()) {
            if (toFind.equals(filter)) {
                return true;
            }
        }
        return false;
    }

    private Data buildNotFilter(int startOfFilterStack) {
        if (current.size() > startOfFilterStack + 1) {
            throw new UnsupportedFilterException("A not filter cannot have more than one filter");
        } else {
            Data tmp = current.pop();

            Data data = new Data(ff.not(tmp.filter));

            if (!tmp.fids.isEmpty()) {
                data.filter = Filter.INCLUDE;
                data.fids.clear();
                requiresPostProcessing = true;
            }

            return data;
        }
    }

    private Data createHighLevelLogicFilter(short filterType, int startOfFilterStack) throws IllegalFilterException {
        if (hasFidFilter(startOfFilterStack)) {
            Set<String> fids;

            switch (filterType) {
                case FilterType.LOGIC_AND:
                    fids = andFids(startOfFilterStack);

                    Data filter = buildFilter(filterType, startOfFilterStack);
                    filter.fids.addAll(fids);

                    return filter;

                case FilterType.LOGIC_OR: {
                    if (hasNonFidFilter(startOfFilterStack)) {
                        throw new UnsupportedFilterException(
                                "Maximum compliance does not allow Logic filters to contain FidFilters");
                    }

                    fids = orFids(startOfFilterStack);

                    pop(startOfFilterStack);

                    Data data = new Data();
                    data.fids.addAll(fids);

                    return data;
                }

                case FilterType.LOGIC_NOT:
                    return buildFilter(filterType, startOfFilterStack);

                default:
                    return Data.ALL;
            }
        } else {
            return buildFilter(filterType, startOfFilterStack);
        }
    }

    private void pop(int startOfFilterStack) {
        while (current.size() > startOfFilterStack) current.pop();
    }

    private boolean hasNonFidFilter(int startOfFilterStack) {
        for (int i = startOfFilterStack; i < current.size(); i++) {
            Data data = current.get(i);

            if (data.filter != Filter.EXCLUDE) {
                return true;
            }
        }

        return false;
    }

    private boolean hasFidFilter(int startOfFilterStack) {
        for (int i = startOfFilterStack; i < current.size(); i++) {
            Data data = current.get(i);

            if (!data.fids.isEmpty()) {
                return true;
            }
        }

        return false;
    }

    // NullFilter
    @Override
    public Object visit(PropertyIsNull filter, Object extraData) {
        current.push(new Data(filter));
        return extraData;
    }
    // NilFilter
    @Override
    public Object visit(PropertyIsNil filter, Object extraData) {
        current.push(new Data(filter));
        return extraData;
    }

    // FidFilter
    @Override
    public Object visit(Id filter, Object extraData) {
        Data data = new Data();
        for (Identifier identifier : filter.getIdentifiers()) {
            FeatureId featureIdentifier = (FeatureId) identifier;
            data.fids.add(featureIdentifier.getID());
        }
        current.push(data);
        return extraData;
    }
    // Include
    @Override
    public Object visit(IncludeFilter filter, Object extraData) {
        current.push(new Data(filter));
        return extraData;
    }
    // Exclude
    @Override
    public Object visit(ExcludeFilter filter, Object extraData) {
        current.push(new Data(filter));
        return extraData;
    }

    @Override
    public Object visitNullFilter(Object extraData) {
        // we will ignore null!
        return extraData;
    }

    // Temporal
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
        current.push(new Data(filter));
        return data;
    }

    private static class Data {
        public static final Data NONE = new Data(Filter.EXCLUDE);

        public static final Data ALL = new Data(Filter.INCLUDE);

        final Set<String> fids = new HashSet<>();

        org.geotools.api.filter.Filter filter;

        public Data() {
            this(Filter.EXCLUDE);
        }

        public Data(Filter f) {
            filter = f;
        }

        @Override
        public String toString() {
            return filter + ":" + fids;
        }
    }

    /**
     * Returns true if the filter was one where the request to the server is more general than the actual filter. See
     * {@link XMLHandlerHints#VALUE_FILTER_COMPLIANCE_MEDIUM} and example of when this can happen.
     *
     * @return true if the filter was one where the request to the server is more general than the actual filter.
     */
    public boolean requiresPostProcessing() {
        return requiresPostProcessing;
    }

    @Override
    public Object visit(NativeFilter nativeFilter, Object extraData) {
        throw new UnsupportedOperationException("XML encoding of native filters is not supported.");
    }
}
