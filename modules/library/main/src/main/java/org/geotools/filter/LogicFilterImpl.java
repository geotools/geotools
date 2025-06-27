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
import org.geotools.api.filter.And;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterVisitor;
import org.geotools.api.filter.Not;
import org.geotools.api.filter.Or;

/**
 * Defines a logic filter (the only filter type that contains other filters). This filter holds one or more filters
 * together and relates them logically with an internally defined type (AND, OR, NOT).
 *
 * @author Rob Hranac, TOPP
 * @version $Id$
 */
public abstract class LogicFilterImpl extends BinaryLogicAbstract {

    /**
     * Computing the hash can be expensive for large logic filters, Effective Java suggests to cache it. The object is
     * not immutable, so care should be taken to clear the cache on change.
     */
    int cachedHash = 0;

    protected LogicFilterImpl(List<org.geotools.api.filter.Filter> children) {
        super(children);
    }

    /**
     * Convenience constructor to create an AND/OR logic filter.
     *
     * @param filter1 An initial sub filter.
     * @param filter2 An initial sub filter.
     * @param filterType The final relation between all sub filters.
     * @throws IllegalFilterException Does not conform to logic filter structure
     */
    protected LogicFilterImpl(Filter filter1, Filter filter2, short filterType) throws IllegalFilterException {
        this(new ArrayList<>());

        // Push the initial filter on the stack
        children.add(filter1);

        // Add the second filter via internal method to check for illegal NOT
        this.addFilter(filter2);
    }

    /**
     * Adds a sub filter to this filter.
     *
     * @param filter Specified filter to add to the sub filter list.
     * @throws IllegalFilterException Does not conform to logic filter structure
     * @task REVISIT: make all filters immutable. This should return a new filter.
     */
    public final void addFilter(org.geotools.api.filter.Filter filter) throws IllegalFilterException {
        // reset
        cachedHash = 0;
        if (this instanceof Not && !children.isEmpty()) {
            throw new IllegalFilterException("Attempted to add an more than one filter to a NOT filter.");
        } else {
            children.add(filter);
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
     * Returns a string representation of this filter.
     *
     * @return String representation of the logic filter.
     */
    @Override
    public String toString() {
        String returnString = "[";
        String operator = "";
        Iterator iterator = children.iterator();
        if (this instanceof Or) {
            operator = " OR ";
        } else if (this instanceof And) {
            operator = " AND ";
        } else if (this instanceof Not) {
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
     * Compares this filter to the specified object. Returns true if the passed in object is the same as this filter.
     * Checks to make sure the filter types are the same, and then checks that the subFilters lists are the same size
     * and that one list contains the other. This means that logic filters with different internal orders of subfilters
     * are equal.
     *
     * @param o - the object to compare this LogicFilter against.
     * @return true if specified object is equal to this filter; false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return super.equals(o);
    }

    /**
     * Override of hashCode method.
     *
     * @return a code to hash this object by.
     */
    @Override
    public int hashCode() {
        if (cachedHash == 0) {
            int result = 17;
            result = 37 * result + getClass().hashCode();
            result = 37 * result + children.hashCode();
            cachedHash = result;
        }

        return cachedHash;
    }

    /**
     * Used by FilterVisitors to perform some action on this filter instance. Typicaly used by Filter decoders, but may
     * also be used by any thing which needs infomration from filter structure. Implementations should always call:
     * visitor.visit(this); It is importatant that this is not left to a parent class unless the parents API is
     * identical.
     *
     * @param visitor The visitor which requires access to this filter, the method must call visitor.visit(this);
     */
    @Override
    public abstract Object accept(FilterVisitor visitor, Object extraData);
}
