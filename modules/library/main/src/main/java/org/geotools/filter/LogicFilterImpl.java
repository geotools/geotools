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
package org.geotools.filter;


// Geotools dependencies
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.factory.CommonFactoryFinder;
import org.opengis.filter.And;
import org.opengis.filter.FilterVisitor;
import org.opengis.filter.Or;



/**
 * Defines a logic filter (the only filter type that contains other filters).
 * This filter holds one or more filters together and relates them logically
 * with an internally defined type (AND, OR, NOT).
 *
 * @author Rob Hranac, TOPP
 *
 * @source $URL$
 * @version $Id$
 */
public abstract class LogicFilterImpl extends BinaryLogicAbstract implements LogicFilter {
    /** The logger for the default core module. */
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.core");

      protected LogicFilterImpl(org.opengis.filter.FilterFactory factory) {
        this(factory,new ArrayList());
    }
    
    protected LogicFilterImpl(org.opengis.filter.FilterFactory factory, List children) {
        super(factory,children);
    }
    
    /**
     * Constructor with type (must be valid).
     *
     * @param filterType The final relation between all sub filters.
     *
     * @throws IllegalFilterException If the filtertype is not a logic type.
     * @deprecated Consructing with type constants should be replaced with 
     * an actual java type.
     */
    protected LogicFilterImpl(short filterType) throws IllegalFilterException {
        super(CommonFactoryFinder.getFilterFactory(null), new ArrayList());
        if( LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.finest("filtertype " + filterType);
        }

        if (isLogicFilter(filterType)) {
            this.filterType = filterType;
        } else {
            throw new IllegalFilterException(
                "Attempted to create logic filter with non-logic type.");
        }
    }

    /**
     * Convenience constructor to create a NOT logic filter.
     *
     * @param filter The initial sub filter.
     * @param filterType The final relation between all sub filters.
     *
     * @throws IllegalFilterException Does not conform to logic filter
     *         structure
     */
    protected LogicFilterImpl(Filter filter, short filterType)
        throws IllegalFilterException {
        
        super(CommonFactoryFinder.getFilterFactory(null), new ArrayList());
        if (isLogicFilter(filterType)) {
            this.filterType = filterType;
        } else {
            throw new IllegalFilterException(
                "Attempted to create logic filter with non-logic type.");
        }

        children.add(filter);
    }

    /**
     * Convenience constructor to create an AND/OR logic filter.
     *
     * @param filter1 An initial sub filter.
     * @param filter2 An initial sub filter.
     * @param filterType The final relation between all sub filters.
     *
     * @throws IllegalFilterException Does not conform to logic filter
     *         structure
     */
    protected LogicFilterImpl(Filter filter1, Filter filter2, short filterType)
        throws IllegalFilterException {
        super(CommonFactoryFinder.getFilterFactory(null), new ArrayList());
        
        if (isLogicFilter(filterType)) {
            this.filterType = filterType;
        } else {
            throw new IllegalFilterException(
                "Attempted to create logic filter with non-logic type.");
        }

        // Push the initial filter on the stack
        children.add(filter1);
       
        // Add the second filter via internal method to check for illegal NOT
        this.addFilter(filter2);
    }

    /**
     * Adds a sub filter to this filter.
     *
     * @param filter Specified filter to add to the sub filter list.
     *
     * @throws IllegalFilterException Does not conform to logic filter
     *         structure
     *
     * @task REVISIT: make all filters immutable.  This should return a new
     *       filter.
     */
    public final void addFilter(org.opengis.filter.Filter filter) throws IllegalFilterException {
        if ((filterType != LOGIC_NOT) || (children.size() == 0)) {
            children.add(filter);
        } else {
            throw new IllegalFilterException(
                "Attempted to add an more than one filter to a NOT filter.");
        }
    }

    /**
     * Gets an iterator for the filters held by this logic filter.
     *
     * @return the iterator of the filters.
     */
    public Iterator getFilterIterator() {
        return children.iterator();
    }
   
    /**
     * Implements a logical OR with this filter and returns the merged filter.
     *
     * @param filter Parent of the filter: must implement GMLHandlerGeometry.
     *
     * @return ORed filter.
     *
     * @task REVISIT: make immutable, should not modify the subfilters of the
     *       filter being ored.
     */
    public Filter or(org.opengis.filter.Filter filter) {
        // Just makes sure that we are not creating unnecessary new filters
        //  by popping onto stack if current filter is OR
        //HACK: not sure what should be returned by this method
        //HACK: assuming it is the result of each method
        //REVISIT: should return a new copy, must implement cloneable to do so.
        if (filterType == super.LOGIC_OR) {
            if( filter instanceof Or ){
                Or more = (Or) filter;
                children.addAll( more.getChildren() );
            }
            else {
                children.add(filter);
            }
            return this;
        } else {
            return super.or(filter);
        }
    }

    /**
     * Implements a logical AND with this filter and returns the merged filter.
     *
     * @param filter Parent of the filter: must implement GMLHandlerGeometry.
     *
     * @return ANDed filter.
     *
     * @task REVISIT: make immutable, should not modify the subfilters of the
     *       filter being anded.
     */
    public Filter and(org.opengis.filter.Filter filter) {
        // Just makes sure that we are not creating unnecessary new filters
        //  by popping onto stack if current filter is AND
        //HACK: not sure what should be returned by this method
        //HACK: assuming it is the result of each method
        if (filterType == super.LOGIC_AND) {
            if( filter instanceof And ){
                And more = (And) filter;
                children.addAll( more.getChildren() );
            }
            else {
                children.add(filter);
            }
            return this;
        } else {
            return super.and(filter);
        }
    }

    /**
     * Implements a logical NOT with this filter and returns the merged filter.
     *
     * @return NOTed filter.
     */
    public Filter not() {
        // Just makes sure that we are not creating unnecessary new filters
        //  by popping off sub filter if current filter is NOT
        //HACK: not sure what should be returned by this method
        //HACK: assuming it is the result of each method
        if (filterType == super.LOGIC_NOT) {
            return (Filter) children.get(0);
        } else {
            return super.not();
        }
    }

    /**
     * package private method to get the internal storage of filters.
     *
     * @return the internal sub filter list.
     * 
     * @deprecated use {@link #getChildren()}
     */
    List getSubFilters() {
        return children;
    }
    
    /**
     * Returns a string representation of this filter.
     *
     * @return String representation of the logic filter.
     */
    public String toString() {
        String returnString = "[";
        String operator = "";
        Iterator iterator = children.iterator();

        if (filterType == LOGIC_OR) {
            operator = " OR ";
        } else if (filterType == LOGIC_AND) {
            operator = " AND ";
        } else if (filterType == LOGIC_NOT) {
            return "[ NOT " + iterator.next().toString() + " ]";
        }

        while (iterator.hasNext()) {
            returnString = returnString + iterator.next().toString();

            if (iterator.hasNext()) {
                returnString = returnString + operator;
            }
        }

        return returnString + "]";
    }

    /**
     * Compares this filter to the specified object.  Returns true  if the
     * passed in object is the same as this filter.  Checks  to make sure the
     * filter types are the same, and then checks that the subFilters lists
     * are the same size and that one list contains the other.  This means
     * that logic filters with different internal orders of subfilters are
     * equal.
     *
     * @param obj - the object to compare this LogicFilter against.
     *
     * @return true if specified object is equal to this filter; false
     *         otherwise.
     */
    public boolean equals(Object obj) {
        if (obj == this )
            return true;
        if ((obj != null) && (obj.getClass() == this.getClass())) {
            LogicFilterImpl logFilter = (LogicFilterImpl) obj;
            if( LOGGER.isLoggable(Level.FINEST)) {
                LOGGER.finest("filter type match:"
                        + (logFilter.getFilterType() == this.filterType));
                LOGGER.finest("same size:"
                        + (logFilter.getSubFilters().size() == this.children.size())
                        + "; inner size: " + logFilter.getSubFilters().size()
                        + "; outer size: " + this.children.size());
                LOGGER.finest("contains:"
                        + logFilter.getSubFilters().containsAll(this.children));
            }

            return ((logFilter.getFilterType() == this.filterType)
            && (logFilter.getSubFilters().size() == this.children.size())
            && logFilter.getSubFilters().containsAll(this.children));
        } else {
            return false;
        }
    }

    /**
     * Override of hashCode method.
     *
     * @return a code to hash this object by.
     */
    public int hashCode() {
        int result = 17;
        result = (37 * result) + filterType;
        result = (37 * result) + children.hashCode();

        return result;
    }

    /**
     * Used by FilterVisitors to perform some action on this filter instance.
     * Typicaly used by Filter decoders, but may also be used by any thing
     * which needs infomration from filter structure. Implementations should
     * always call: visitor.visit(this); It is importatant that this is not
     * left to a parent class unless the parents API is identical.
     *
     * @param visitor The visitor which requires access to this filter, the
     *        method must call visitor.visit(this);
     */
    public abstract Object accept(FilterVisitor visitor, Object extraData);
}
