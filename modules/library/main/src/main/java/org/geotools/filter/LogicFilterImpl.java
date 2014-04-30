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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opengis.filter.Filter;
import org.opengis.filter.FilterVisitor;

/**
 * Defines a logic filter (the only filter type that contains other filters).
 * This filter holds one or more filters together and relates them logically
 * with an internally defined type (AND, OR, NOT).
 *
 * @author Rob Hranac, TOPP
 *
 *
 * @source $URL$
 * @version $Id$
 */
public abstract class LogicFilterImpl extends BinaryLogicAbstract implements LogicFilter {
    /** The logger for the default core module. */
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.core");

    @Deprecated
    protected LogicFilterImpl() {
        this(new ArrayList<org.opengis.filter.Filter>());
    }
    
    protected LogicFilterImpl(List<org.opengis.filter.Filter> children) {
        super(children);
    }
    

    /**
     * Convenience constructor to create a NOT logic filter.
     *
     * @param filter The initial sub filter.
     * @throws IllegalFilterException Does not conform to logic filter
     *         structure
     */
    @Deprecated
    protected LogicFilterImpl(Filter filter)
        throws IllegalFilterException {
        this();
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
        this();

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
        int filterType = Filters.getFilterType(this);
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
        int filterType = Filters.getFilterType(this);
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
                        + (Filters.getFilterType( logFilter ) == Filters.getFilterType( this )));
                LOGGER.finest("same size:"
                        + (logFilter.getSubFilters().size() == this.children.size())
                        + "; inner size: " + logFilter.getSubFilters().size()
                        + "; outer size: " + this.children.size());
                LOGGER.finest("contains:"
                        + logFilter.getSubFilters().containsAll(this.children));
            }

            return ((Filters.getFilterType( logFilter ) == Filters.getFilterType( this ))
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
        int filterType = Filters.getFilterType(this);
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
