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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.FilterType;
import org.geotools.filter.Filters;
import org.geotools.filter.FunctionExpression;
import org.geotools.filter.IllegalFilterException;
import org.geotools.filter.LogicFilter;
import org.geotools.xml.XMLHandlerHints;
import org.opengis.filter.And;
import org.opengis.filter.BinaryComparisonOperator;
import org.opengis.filter.BinaryLogicOperator;
import org.opengis.filter.ExcludeFilter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.Filter;
import org.opengis.filter.Id;
import org.opengis.filter.IncludeFilter;
import org.opengis.filter.FilterVisitor;
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
import org.opengis.filter.identity.FeatureId;
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
import org.opengis.filter.spatial.SpatialOperator;
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
 * Prepares a filter for XML encoded for interoperability with another system.  It will behave differently depending on
 * the compliance level chosen.  A new request will have to be made and the features will have
 * to be tested again on the client side if there are any FidFilters in the filter.  Consider the following to understand why:
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
 * <pre>
 * and{
 *         nullFilter
 *         nullFilter
 * }
 * </pre>
 * and getFids() would return the fids in the fidFilter.
 *
 * So the final filter would (this is not standard but a common implementation) return the results of the and filter as well as
 * all the features that match the fids.  Which is more than the original filter would accept.
 *
 * The XML Document writer can operate at different levels of compliance. The
 * geotools level is extremely flexible and forgiving.
 *
 * <p>
 * All NOT(FidFilter) are changed to Filter.INCLUDE.  So make sure that the filter is processed again on the client with the original
 * filter
 * </p>
 *
 * For a description of the difference Compliance levels that can be used see
 * <ul>
 * <li>{@link XMLHandlerHints#VALUE_FILTER_COMPLIANCE_LOW}</li>
 * <li>{@link XMLHandlerHints#VALUE_FILTER_COMPLIANCE_MEDIUM}</li>
 * <li>{@link XMLHandlerHints#VALUE_FILTER_COMPLIANCE_HIGH}</li>
 * </ul>
 *
 * @author Jesse Eichar
 *
 * @source $URL$
 */
public class FilterEncodingPreProcessor implements FilterVisitor {
    private static final int LOW = 0;
    private static final int MEDIUM = 1;
    private static final int HIGH = 2;
    private int complianceInt;
    private Stack<Data> current = new Stack<Data>();
    FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
    
    private boolean requiresPostProcessing=false;

    public FilterEncodingPreProcessor(Integer complianceLevel) {
        if ((complianceLevel != LOW) && (complianceLevel != MEDIUM) && (complianceLevel != HIGH)) {
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
            return (Id) ff.id(empty);
        }

        Data data = (Data) current.peek();

        if (data.fids.size() > 0) {            
            Set<FeatureId> set = new HashSet<FeatureId>();
            Set<String> fids = data.fids;
            for( String fid : fids ){
                set.add( ff.featureId(fid));
            }
            return (Id) ff.id(set);
        }
        else {
            Set<FeatureId> empty = Collections.emptySet();
            return (Id) ff.id(empty);
        }
    }

    /**
     * Returns the filter that can be encoded.
     *
     * @return the filter that can be encoded.
     */
    public org.opengis.filter.Filter getFilter() {
        if( current.isEmpty() )
            return Filter.EXCLUDE;
        return ((Data) this.current.peek()).filter;
    }

    public void visit(Filter filter) {
        if (filter instanceof PropertyIsBetween
                || filter instanceof BinaryComparisonOperator
                || filter instanceof BinarySpatialOperator
                || filter instanceof PropertyIsLike
                || filter instanceof BinaryLogicOperator
                || filter instanceof Not
                || filter instanceof PropertyIsNull || filter instanceof Id) {
            filter.accept(this,null);
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

    public void visit(LogicFilter filter) {
        int startSize = current.size();

        try {
            switch (this.complianceInt) {
            case LOW:
                current.push(new Data(filter));

                break;

            case MEDIUM:

                for (org.opengis.filter.Filter component : filter.getChildren()) {
                    component.accept(this, null );
                }
                current.push(createMediumLevelLogicFilter(
                        Filters.getFilterType(filter), startSize));

                break;

            case HIGH:

                for (org.opengis.filter.Filter component : filter.getChildren()) {
                    component.accept(this,null);
                }
                current.push(createHighLevelLogicFilter(
                        Filters.getFilterType(filter), startSize));

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

    private Data createMediumLevelLogicFilter(short filterType,
        int startOfFilterStack) throws IllegalFilterException {
        Data resultingFilter;

        switch (filterType) {
        case FilterType.LOGIC_AND: {
            Set fids = andFids(startOfFilterStack);
            resultingFilter = buildFilter(filterType, startOfFilterStack);
            resultingFilter.fids.addAll(fids);
            
            if( resultingFilter.filter!=Filter.EXCLUDE && !fids.isEmpty() )
                requiresPostProcessing=true;
            break;
        }

        case FilterType.LOGIC_OR: {
            Set fids = orFids(startOfFilterStack);
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

    private Set orFids(int startOfFilterStack) {
        Set set = new HashSet();

        for (int i = startOfFilterStack; i < current.size(); i++) {
            Data data = (Data) current.get(i);

            if (!data.fids.isEmpty()) {
                set.addAll(data.fids);
            }
        }

        return set;
    }

    private Set andFids(int startOfFilterStack) {
        if (!hasFidFilter(startOfFilterStack)) {
            return Collections.EMPTY_SET;
        }

        Set toRemove = new HashSet();
        List fidSet = new ArrayList();
        boolean doRemove = true;

        for (int i = startOfFilterStack; i < current.size(); i++) {
            Data data = (Data) current.get(i);

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

        if (fidSet.size() == 0) {
            return Collections.EMPTY_SET;
        }

        if (fidSet.size() == 1) {
            return (Set) fidSet.get(0);
        }

        HashSet set = new HashSet();

        for (int i = 0; i < fidSet.size(); i++) {
            Set tmp = (Set) fidSet.get(i);

            for (Iterator iter = tmp.iterator(); iter.hasNext();) {
                String fid = (String) iter.next();

                if (allContain(fid, fidSet)) {
                    set.add(fid);
                }
            }
        }

        return set;
    }

    private boolean allContain(String fid, List fidSets) {
        for (int i = 0; i < fidSets.size(); i++) {
            Set tmp = (Set) fidSets.get(i);

            if (!tmp.contains(fid)) {
                return false;
            }
        }

        return true;
    }

    private Data buildFilter(short filterType, int startOfFilterStack)
        throws IllegalFilterException {
        if (current.isEmpty()) {
            return Data.ALL;
        }

        if (filterType == FilterType.LOGIC_NOT) {
            return buildNotFilter(startOfFilterStack);
        }

        if (current.size() == (startOfFilterStack + 1)) {
            return (Data) current.pop();
        }
        
        List<org.opengis.filter.Filter> filterList = new ArrayList<org.opengis.filter.Filter>();
        
        while (current.size() > startOfFilterStack) {
            Data data = (Data) current.pop();

            if (data.filter != Filter.EXCLUDE) {
                filterList.add( data.filter);
            }
        }
        
        org.opengis.filter.Filter f;
        if( filterType == FilterType.LOGIC_AND ){
            f = ff.and( filterList );
        }
        else if (filterType == FilterType.LOGIC_OR ){
            f = ff.or(filterList );
        }
        else {
            // not expected
            f = null;
        }        
        return new Data(compressFilter(filterType, (LogicFilter) f));
    }

    private org.opengis.filter.Filter compressFilter(short filterType, LogicFilter f)
        throws IllegalFilterException {
        LogicFilter result;
        int added = 0;
        List<org.opengis.filter.Filter> resultList = new ArrayList<org.opengis.filter.Filter>();
        
        switch (filterType) {
        case FilterType.LOGIC_AND:
            
            if (contains(f, Filter.EXCLUDE)) {
                return Filter.EXCLUDE;
            }

            for (org.opengis.filter.Filter filter : f.getChildren() ) {
                if (filter == org.opengis.filter.Filter.INCLUDE) {
                    continue;
                }
                added++;
                resultList.add( filter );
            }

            if (resultList.isEmpty()) {
                return Filter.EXCLUDE;
            }

            result = (LogicFilter) ff.and(resultList);
            break;

        case FilterType.LOGIC_OR:

            if (contains(f, Filter.INCLUDE)) {
                return Filter.INCLUDE;
            }

            for (Object item : f.getChildren() ) {
                org.opengis.filter.Filter filter = (org.opengis.filter.Filter) item;
                if (filter == Filter.EXCLUDE) {
                    continue;
                }
                added++;
                resultList.add( filter );
            }

            if (resultList.isEmpty()) {
                return Filter.EXCLUDE;
            }

            result = (LogicFilter) ff.or(resultList);
            
            break;

        default:
            return Filter.EXCLUDE;
        }

        switch (added) {
        case 0:
            return Filter.EXCLUDE;

        case 1:
            return (Filter) result.getChildren().iterator().next();

        default:
            return result;
        }
    }

    private boolean contains( LogicFilter f, org.opengis.filter.Filter toFind) {
        for (Iterator iter = f.getChildren().iterator(); iter.hasNext();) {
            if( toFind.equals( iter.next() ) ){
                return true;
            }
        }
        return false;
    }

    private Data buildNotFilter(int startOfFilterStack) {
        if (current.size() > (startOfFilterStack + 1)) {
            throw new UnsupportedFilterException(
                "A not filter cannot have more than one filter");
        } else {
            Data tmp = (Data) current.pop();

            
            Data data = new Data(  ff.not( tmp.filter) );

            if (!tmp.fids.isEmpty()) {
                data.filter = Filter.INCLUDE;
                data.fids.clear();
                requiresPostProcessing=true;
            }

            return data;
        }
    }

    private Data createHighLevelLogicFilter(short filterType,
        int startOfFilterStack) throws IllegalFilterException {
        if (hasFidFilter(startOfFilterStack)) {
            Set fids;

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
        while (current.size() > startOfFilterStack)
            current.pop();
    }

    private boolean hasNonFidFilter(int startOfFilterStack) {
        for (int i = startOfFilterStack; i < current.size(); i++) {
            Data data = (Data) current.get(i);

            if (data.filter != Filter.EXCLUDE) {
                return true;
            }
        }

        return false;
    }

    private boolean hasFidFilter(int startOfFilterStack) {
        for (int i = startOfFilterStack; i < current.size(); i++) {
            Data data = (Data) current.get(i);

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
        final public static Data NONE = new Data(Filter.EXCLUDE);
        final public static Data ALL = new Data(Filter.INCLUDE);
        final Set fids = new HashSet();
        org.opengis.filter.Filter filter;

        public Data() {
            this( Filter.EXCLUDE);
        }

        public Data(Filter f ){
            filter = f;
        }

        public String toString() {
            return filter + ":" + fids;
        }
    }

    /**
     * Returns true if the filter was one where the request to the server is more general than the actual filter.  
     * See {@link XMLHandlerHints#VALUE_FILTER_COMPLIANCE_MEDIUM} and example of when this can happen.
     * 
     * @return true if the filter was one where the request to the server is more general than the actual filter.
     */
    public boolean requiresPostProcessing() {
        return requiresPostProcessing;
    }
    
    // FilterVisitor2 methods formally from FilterVisitorFilterWrapper
    protected void visitLogicFilter(org.opengis.filter.Filter filter) {
        if (filter instanceof LogicFilter) {
            visit((LogicFilter) filter);
        }
    }

protected void visitCompareFilter(org.opengis.filter.Filter filter) {
        if (filter instanceof PropertyIsNull) {
                visit((PropertyIsNull)filter);
                return;
        }
        
        if (filter instanceof PropertyIsLike) {
                visit((PropertyIsLike)filter);
        }
        
        if (filter instanceof BinaryComparisonOperator) {
                visit((BinaryComparisonOperator)filter);
        }
        
        if (filter instanceof Not) {
            visit((Not)filter);
        }
}

protected void visitGeometryFilter(SpatialOperator filter) {
        if (filter instanceof BinarySpatialOperator) {
                visit((BinarySpatialOperator)filter);
        }
}

public Object visit(And filter, Object extraData) {
        visitLogicFilter(filter);
        return extraData;
}

public Object visit( Id filter, Object extraData) {
    Data data = new Data();
    data.fids.addAll( filter.getIDs() );
    current.push(data);
        
    return extraData;
}

    public Object visitNullFilter( Object extraData) {        
        return extraData;
    }
    public Object visit( IncludeFilter filter, Object extraData) {
        visit(filter);
        return extraData;
    }
    public Object visit( ExcludeFilter filter, Object extraData) {   
        visit(filter);
        return extraData;
    }
    //
    // Filter Visitor Methods
    //
    public Object visit(Not filter, Object extraData) {
            visitLogicFilter(filter);
            return extraData;
    }
    
    public Object visit(Or filter, Object extraData) {
            visitLogicFilter(filter);
            return extraData;
    }
    
    public Object visit(PropertyIsBetween filter, Object extraData) {
        current.push(new Data(filter));    
        visitCompareFilter(filter);
            return extraData;
    }
    
    public Object visit(PropertyIsEqualTo filter, Object extraData) {
            visitCompareFilter(filter);
            return extraData;
    }
    
    public Object visit(PropertyIsNotEqualTo filter, Object extraData) {
            visitCompareFilter(filter);
            return extraData;
    }
    
    public Object visit(PropertyIsGreaterThan filter, Object extraData) {
            visitCompareFilter(filter);
            return extraData;
    }
    
    public Object visit(PropertyIsGreaterThanOrEqualTo filter, Object extraData) {
            visitCompareFilter(filter);
            return extraData;
    }
    
    public Object visit(PropertyIsLessThan filter, Object extraData) {
            visitCompareFilter(filter);
            return extraData;
    }
    
    public Object visit(PropertyIsLessThanOrEqualTo filter, Object extraData) {
            visitCompareFilter(filter);
            return extraData;
    }
    
    public Object visit(PropertyIsLike filter, Object extraData) {
            visitCompareFilter(filter);
            return extraData;
    }
    
    public Object visit(PropertyIsNull filter, Object extraData) {
            visitCompareFilter(filter);
            return extraData;
    }
    
    public Object visit(PropertyIsNil filter, Object extraData) {
    visitCompareFilter(filter);
    return extraData;
    }
    
    public Object visit(BBOX filter, Object extraData) {
            visitGeometryFilter(filter);
            return extraData;
    }
    
    public Object visit(Beyond filter, Object extraData) {
            visitGeometryFilter(filter);
            return extraData;
    }
    
    public Object visit(Contains filter, Object extraData) {
            visitGeometryFilter(filter);
            return extraData;
    }
    
    public Object visit(Crosses filter, Object extraData) {
            visitGeometryFilter(filter);
            return extraData;
    }
    
    public Object visit(Disjoint filter, Object extraData) {
            visitGeometryFilter(filter);
            return extraData;
    }
    
    public Object visit(DWithin filter, Object extraData) {
            visitGeometryFilter(filter);
            return extraData;
    }
    
    public Object visit(Equals filter, Object extraData) {
            visitGeometryFilter(filter);
            return extraData;
    }
    
    public Object visit(Intersects filter, Object extraData) {
            visitGeometryFilter(filter);
            return extraData;
    }
    
    public Object visit(Overlaps filter, Object extraData) {
            visitGeometryFilter(filter);
            return extraData;
    }

    public Object visit(Touches filter, Object extraData) {
            visitGeometryFilter(filter);
            return extraData;
    }

    public Object visit(Within filter, Object extraData) {
            visitGeometryFilter(filter);
            return extraData;
    }
    //
    // Temporal Filters (UNSUPPORTED)
    //

    public Object visit(After after, Object extraData) {
        return visitTemporalFilter((BinaryTemporalOperator) after);
    }

    public Object visit(AnyInteracts anyInteracts, Object extraData) {
        return visitTemporalFilter((BinaryTemporalOperator) anyInteracts);
    }

    public Object visit(Before before, Object extraData) {
        return visitTemporalFilter((BinaryTemporalOperator) before);
    }

    public Object visit(Begins begins, Object extraData) {
        return visitTemporalFilter((BinaryTemporalOperator) begins);
    }

    public Object visit(BegunBy begunBy, Object extraData) {
        return visitTemporalFilter((BinaryTemporalOperator) begunBy);
    }

    public Object visit(During during, Object extraData) {
        return visitTemporalFilter((BinaryTemporalOperator) during);
    }

    public Object visit(EndedBy endedBy, Object extraData) {
        return visitTemporalFilter((BinaryTemporalOperator) endedBy);
    }

    public Object visit(Ends ends, Object extraData) {
        return visitTemporalFilter((BinaryTemporalOperator) ends);
    }

    public Object visit(Meets meets, Object extraData) {
        return visitTemporalFilter((BinaryTemporalOperator) meets);
    }

    public Object visit(MetBy metBy, Object extraData) {
        return visitTemporalFilter((BinaryTemporalOperator) metBy);
    }

    public Object visit(OverlappedBy overlappedBy, Object extraData) {
        return visitTemporalFilter((BinaryTemporalOperator) overlappedBy);
    }

    public Object visit(TContains contains, Object extraData) {
        return visitTemporalFilter((BinaryTemporalOperator) contains);
    }

    public Object visit(TEquals equals, Object extraData) {
        return visitTemporalFilter((BinaryTemporalOperator) equals);
    }

    public Object visit(TOverlaps contains, Object extraData) {
        return visitTemporalFilter((BinaryTemporalOperator) contains);
    }

    protected Object visitTemporalFilter(BinaryTemporalOperator filter) {
        throw new UnsupportedOperationException("Temporal filters not supported");
    }

}
