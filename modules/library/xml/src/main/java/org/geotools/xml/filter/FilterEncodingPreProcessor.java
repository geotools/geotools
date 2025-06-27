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
import javax.xml.crypto.Data;
import org.geotools.api.filter.And;
import org.geotools.api.filter.BinaryComparisonOperator;
import org.geotools.api.filter.BinaryLogicOperator;
import org.geotools.api.filter.ExcludeFilter;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
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
import org.geotools.api.filter.identity.FeatureId;
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
import org.geotools.api.filter.spatial.SpatialOperator;
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
import org.geotools.filter.IllegalFilterException;
import org.geotools.xml.XMLHandlerHints;

/**
 * Prepares a filter for XML encoded for interoperability with another system. It will behave differently depending on
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
 * @author Jesse Eichar
 */
@SuppressWarnings("unchecked") // gigantic mess, too much untyped structures and methods to work on them
public class FilterEncodingPreProcessor implements FilterVisitor {
    private static final int LOW = 0;
    private static final int MEDIUM = 1;
    private static final int HIGH = 2;
    private int complianceInt;
    private Stack<Data> current = new Stack<>();
    FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);

    private boolean requiresPostProcessing = false;

    public FilterEncodingPreProcessor(Integer complianceLevel) {
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

        if (data.fids.isEmpty()) {
            Set<FeatureId> empty = Collections.emptySet();
            return ff.id(empty);
        } else {
            Set<FeatureId> set = new HashSet<>();
            Set<String> fids = data.fids;
            for (String fid : fids) {
                set.add(ff.featureId(fid));
            }
            return ff.id(set);
        }
    }

    /**
     * Returns the filter that can be encoded.
     *
     * @return the filter that can be encoded.
     */
    public org.geotools.api.filter.Filter getFilter() {
        if (current.isEmpty()) return Filter.EXCLUDE;
        return this.current.peek().filter;
    }

    public void visit(Filter filter) {
        if (filter instanceof PropertyIsBetween
                || filter instanceof BinaryComparisonOperator
                || filter instanceof BinarySpatialOperator
                || filter instanceof PropertyIsLike
                || filter instanceof BinaryLogicOperator
                || filter instanceof Not
                || filter instanceof PropertyIsNull
                || filter instanceof Id) {
            filter.accept(this, null);
        } else {
            current.push(new Data(filter));
        }
    }

    public void visit(PropertyIsBetween filter) {
        current.push(new Data(filter));
    }

    public void visit(BinaryComparisonOperator filter) {
        current.push(new Data(filter));
    }

    public void visit(Not filter) {
        current.push(new Data(filter));
    }

    public void visit(BinarySpatialOperator filter) {
        current.push(new Data(filter));
    }

    public void visit(PropertyIsLike filter) {
        current.push(new Data(filter));
    }

    public void visitLogicFilter(Not filter) {
        int startSize = current.size();

        try {
            switch (this.complianceInt) {
                case LOW:
                    current.push(new Data(filter));

                    break;

                case MEDIUM:
                    filter.getFilter().accept(this, null);
                    current.push(createMediumLevelLogicFilter(filter, startSize));

                    break;

                case HIGH:
                    filter.getFilter().accept(this, null);
                    current.push(createHighLevelLogicFilter(filter, startSize));

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
    }

    public void visit(BinaryLogicOperator filter) {
        int startSize = current.size();

        try {
            switch (this.complianceInt) {
                case LOW:
                    current.push(new Data(filter));

                    break;

                case MEDIUM:
                    for (org.geotools.api.filter.Filter component : filter.getChildren()) {
                        component.accept(this, null);
                    }
                    current.push(createMediumLevelLogicFilter(filter, startSize));

                    break;

                case HIGH:
                    for (org.geotools.api.filter.Filter component : filter.getChildren()) {
                        component.accept(this, null);
                    }
                    current.push(createHighLevelLogicFilter(filter, startSize));

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
    }

    private Data createMediumLevelLogicFilter(Filter filter, int startOfFilterStack) throws IllegalFilterException {
        Data resultingFilter;

        if (filter instanceof And) {
            Set fids = andFids(startOfFilterStack);
            resultingFilter = buildFilter(filter, startOfFilterStack);
            resultingFilter.fids.addAll(fids);

            if (resultingFilter.filter != Filter.EXCLUDE && !fids.isEmpty()) requiresPostProcessing = true;
        } else if (filter instanceof Or) {
            Set fids = orFids(startOfFilterStack);
            resultingFilter = buildFilter(filter, startOfFilterStack);
            resultingFilter.fids.addAll(fids);
        } else if (filter instanceof Not) {
            resultingFilter = buildFilter(filter, startOfFilterStack);
        } else {
            resultingFilter = buildFilter(filter, startOfFilterStack);
        }

        return resultingFilter;
    }

    private Set orFids(int startOfFilterStack) {
        Set set = new HashSet<>();

        for (int i = startOfFilterStack; i < current.size(); i++) {
            Data data = current.get(i);

            if (!data.fids.isEmpty()) {
                set.addAll(data.fids);
            }
        }

        return set;
    }

    private Set andFids(int startOfFilterStack) {
        if (!hasFidFilter(startOfFilterStack)) {
            return Collections.emptySet();
        }

        Set toRemove = new HashSet<>();
        List<Set> fidSet = new ArrayList<>();
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

        HashSet set = new HashSet<>();

        for (Set tmp : fidSet) {
            for (Object o : tmp) {
                String fid = (String) o;

                if (allContain(fid, fidSet)) {
                    set.add(fid);
                }
            }
        }

        return set;
    }

    private boolean allContain(String fid, List fidSets) {
        for (Object fidSet : fidSets) {
            Set tmp = (Set) fidSet;

            if (!tmp.contains(fid)) {
                return false;
            }
        }

        return true;
    }

    private Data buildFilter(Filter filter, int startOfFilterStack) throws IllegalFilterException {
        if (current.isEmpty()) {
            return Data.ALL;
        }

        if (filter instanceof Not) {
            return buildNotFilter(startOfFilterStack);
        }

        if (current.size() == startOfFilterStack + 1) {
            return current.pop();
        }

        List<org.geotools.api.filter.Filter> filterList = new ArrayList<>();

        while (current.size() > startOfFilterStack) {
            Data data = current.pop();

            if (data.filter != Filter.EXCLUDE) {
                filterList.add(data.filter);
            }
        }

        org.geotools.api.filter.Filter f;
        if (filter instanceof And) {
            f = ff.and(filterList);
        } else if (filter instanceof Or) {
            f = ff.or(filterList);
        } else {
            // not expected
            f = null;
        }
        return new Data(compressFilter(filter, (BinaryLogicOperator) f));
    }

    private org.geotools.api.filter.Filter compressFilter(Filter filter, BinaryLogicOperator f)
            throws IllegalFilterException {
        BinaryLogicOperator result;
        int added = 0;
        List<org.geotools.api.filter.Filter> resultList = new ArrayList<>();

        if (filter instanceof And) {
            if (contains(f, Filter.EXCLUDE)) {
                return Filter.EXCLUDE;
            }

            for (org.geotools.api.filter.Filter child : f.getChildren()) {
                if (child == org.geotools.api.filter.Filter.INCLUDE) {
                    continue;
                }
                added++;
                resultList.add(child);
            }

            if (resultList.isEmpty()) {
                return Filter.EXCLUDE;
            }

            result = ff.and(resultList);
        } else if (filter instanceof Or) {
            if (contains(f, Filter.INCLUDE)) {
                return Filter.INCLUDE;
            }

            for (Object item : f.getChildren()) {
                org.geotools.api.filter.Filter child = (org.geotools.api.filter.Filter) item;
                if (child == Filter.EXCLUDE) {
                    continue;
                }
                added++;
                resultList.add(child);
            }

            if (resultList.isEmpty()) {
                return Filter.EXCLUDE;
            }

            result = ff.or(resultList);

        } else {
            return Filter.EXCLUDE;
        }

        switch (added) {
            case 0:
                return Filter.EXCLUDE;

            case 1:
                return result.getChildren().iterator().next();

            default:
                return result;
        }
    }

    private boolean contains(BinaryLogicOperator f, org.geotools.api.filter.Filter toFind) {
        for (org.geotools.api.filter.Filter filter : f.getChildren()) {
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

    private Data createHighLevelLogicFilter(Filter filter, int startOfFilterStack) throws IllegalFilterException {
        if (hasFidFilter(startOfFilterStack)) {
            Set fids;

            if (filter instanceof And) {
                fids = andFids(startOfFilterStack);

                Data data = buildFilter(filter, startOfFilterStack);
                data.fids.addAll(fids);

                return data;

            } else if (filter instanceof Or) {
                if (hasNonFidFilter(startOfFilterStack)) {
                    throw new UnsupportedFilterException(
                            "Maximum compliance does not allow Logic filters to contain FidFilters");
                }

                fids = orFids(startOfFilterStack);

                pop(startOfFilterStack);

                Data data = new Data();
                data.fids.addAll(fids);

                return data;
            } else if (filter instanceof Not) {
                return buildFilter(filter, startOfFilterStack);
            } else {

                return Data.ALL;
            }

        } else {
            return buildFilter(filter, startOfFilterStack);
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

    public void visit(PropertyIsNull filter) {
        current.push(new Data(filter));
    }

    public void visit(Id filter) {
        Data data = new Data();
        data.fids.addAll(filter.getIDs());
        current.push(data);
    }

    public void visit(IncludeFilter filter) {
        current.push(new Data(filter));
    }

    public void visit(ExcludeFilter filter) {
        current.push(new Data(filter));
    }

    private static class Data {
        public static final Data NONE = new Data(Filter.EXCLUDE);
        public static final Data ALL = new Data(Filter.INCLUDE);
        final Set fids = new HashSet<>();
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

    // FilterVisitor2 methods formally from FilterVisitorFilterWrapper
    protected void visitLogicFilter(org.geotools.api.filter.Filter filter) {
        visit((BinaryLogicOperator) filter);
    }

    protected void visitCompareFilter(org.geotools.api.filter.Filter filter) {
        if (filter instanceof PropertyIsNull) {
            visit((PropertyIsNull) filter);
            return;
        }

        if (filter instanceof PropertyIsLike) {
            visit((PropertyIsLike) filter);
        }

        if (filter instanceof BinaryComparisonOperator) {
            visit((BinaryComparisonOperator) filter);
        }

        if (filter instanceof Not) {
            visit((Not) filter);
        }
    }

    protected void visitGeometryFilter(SpatialOperator filter) {
        if (filter instanceof BinarySpatialOperator) {
            visit((BinarySpatialOperator) filter);
        }
    }

    @Override
    public Object visit(And filter, Object extraData) {
        visitLogicFilter(filter);
        return extraData;
    }

    @Override
    public Object visit(Id filter, Object extraData) {
        Data data = new Data();
        data.fids.addAll(filter.getIDs());
        current.push(data);

        return extraData;
    }

    @Override
    public Object visitNullFilter(Object extraData) {
        return extraData;
    }

    @Override
    public Object visit(IncludeFilter filter, Object extraData) {
        visit(filter);
        return extraData;
    }

    @Override
    public Object visit(ExcludeFilter filter, Object extraData) {
        visit(filter);
        return extraData;
    }
    //
    // Filter Visitor Methods
    //
    @Override
    public Object visit(Not filter, Object extraData) {
        visitLogicFilter(filter);
        return extraData;
    }

    @Override
    public Object visit(Or filter, Object extraData) {
        visitLogicFilter(filter);
        return extraData;
    }

    @Override
    public Object visit(PropertyIsBetween filter, Object extraData) {
        current.push(new Data(filter));
        visitCompareFilter(filter);
        return extraData;
    }

    @Override
    public Object visit(PropertyIsEqualTo filter, Object extraData) {
        visitCompareFilter(filter);
        return extraData;
    }

    @Override
    public Object visit(PropertyIsNotEqualTo filter, Object extraData) {
        visitCompareFilter(filter);
        return extraData;
    }

    @Override
    public Object visit(PropertyIsGreaterThan filter, Object extraData) {
        visitCompareFilter(filter);
        return extraData;
    }

    @Override
    public Object visit(PropertyIsGreaterThanOrEqualTo filter, Object extraData) {
        visitCompareFilter(filter);
        return extraData;
    }

    @Override
    public Object visit(PropertyIsLessThan filter, Object extraData) {
        visitCompareFilter(filter);
        return extraData;
    }

    @Override
    public Object visit(PropertyIsLessThanOrEqualTo filter, Object extraData) {
        visitCompareFilter(filter);
        return extraData;
    }

    @Override
    public Object visit(PropertyIsLike filter, Object extraData) {
        visitCompareFilter(filter);
        return extraData;
    }

    @Override
    public Object visit(PropertyIsNull filter, Object extraData) {
        visitCompareFilter(filter);
        return extraData;
    }

    @Override
    public Object visit(PropertyIsNil filter, Object extraData) {
        visitCompareFilter(filter);
        return extraData;
    }

    @Override
    public Object visit(BBOX filter, Object extraData) {
        visitGeometryFilter(filter);
        return extraData;
    }

    @Override
    public Object visit(Beyond filter, Object extraData) {
        visitGeometryFilter(filter);
        return extraData;
    }

    @Override
    public Object visit(Contains filter, Object extraData) {
        visitGeometryFilter(filter);
        return extraData;
    }

    @Override
    public Object visit(Crosses filter, Object extraData) {
        visitGeometryFilter(filter);
        return extraData;
    }

    @Override
    public Object visit(Disjoint filter, Object extraData) {
        visitGeometryFilter(filter);
        return extraData;
    }

    @Override
    public Object visit(DWithin filter, Object extraData) {
        visitGeometryFilter(filter);
        return extraData;
    }

    @Override
    public Object visit(Equals filter, Object extraData) {
        visitGeometryFilter(filter);
        return extraData;
    }

    @Override
    public Object visit(Intersects filter, Object extraData) {
        visitGeometryFilter(filter);
        return extraData;
    }

    @Override
    public Object visit(Overlaps filter, Object extraData) {
        visitGeometryFilter(filter);
        return extraData;
    }

    @Override
    public Object visit(Touches filter, Object extraData) {
        visitGeometryFilter(filter);
        return extraData;
    }

    @Override
    public Object visit(Within filter, Object extraData) {
        visitGeometryFilter(filter);
        return extraData;
    }
    //
    // Temporal Filters (UNSUPPORTED)
    //

    @Override
    public Object visit(After after, Object extraData) {
        return visitTemporalFilter(after);
    }

    @Override
    public Object visit(AnyInteracts anyInteracts, Object extraData) {
        return visitTemporalFilter(anyInteracts);
    }

    @Override
    public Object visit(Before before, Object extraData) {
        return visitTemporalFilter(before);
    }

    @Override
    public Object visit(Begins begins, Object extraData) {
        return visitTemporalFilter(begins);
    }

    @Override
    public Object visit(BegunBy begunBy, Object extraData) {
        return visitTemporalFilter(begunBy);
    }

    @Override
    public Object visit(During during, Object extraData) {
        return visitTemporalFilter(during);
    }

    @Override
    public Object visit(EndedBy endedBy, Object extraData) {
        return visitTemporalFilter(endedBy);
    }

    @Override
    public Object visit(Ends ends, Object extraData) {
        return visitTemporalFilter(ends);
    }

    @Override
    public Object visit(Meets meets, Object extraData) {
        return visitTemporalFilter(meets);
    }

    @Override
    public Object visit(MetBy metBy, Object extraData) {
        return visitTemporalFilter(metBy);
    }

    @Override
    public Object visit(OverlappedBy overlappedBy, Object extraData) {
        return visitTemporalFilter(overlappedBy);
    }

    @Override
    public Object visit(TContains contains, Object extraData) {
        return visitTemporalFilter(contains);
    }

    @Override
    public Object visit(TEquals equals, Object extraData) {
        return visitTemporalFilter(equals);
    }

    @Override
    public Object visit(TOverlaps contains, Object extraData) {
        return visitTemporalFilter(contains);
    }

    protected Object visitTemporalFilter(BinaryTemporalOperator filter) {
        throw new UnsupportedOperationException("Temporal filters not supported");
    }
}
