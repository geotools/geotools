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

import java.util.Objects;
import org.geotools.api.filter.FilterVisitor;
import org.geotools.api.filter.PropertyIsNull;
import org.geotools.api.filter.expression.Expression;

/**
 * Defines a null filter, which checks to see if an attribute is null.
 *
 * @author Rob Hranac, Vision for New York
 * @version $Id$
 */
public class NullFilterImpl extends AbstractFilter implements PropertyIsNull {
    /** The null check value, which must be an attribute expression. */
    private org.geotools.api.filter.expression.Expression nullCheck = null;

    protected NullFilterImpl(org.geotools.api.filter.expression.Expression expresion) {
        setExpression(expresion);
    }

    /** Returns the expression which represents the null check. */
    @Override
    public Expression getExpression() {
        return nullCheck;
    }

    /** Sets the expression which represents the null check. */
    public void setExpression(Expression nullCheck) {
        if (nullCheck != null) {
            this.nullCheck = nullCheck;
        } else {
            throw new IllegalFilterException("Expression required");
        }
    }

    /**
     * Determines whether or not a given feature is 'inside' this filter.
     *
     * @param feature Specified feature to examine.
     * @return Flag confirming whether or not this feature is inside the filter.
     */
    @Override
    public boolean evaluate(Object feature) {
        if (nullCheck == null) {
            return false;
        } else {
            return nullCheck.evaluate(feature) == null;
        }
    }

    /**
     * Returns a string representation of this filter.
     *
     * @return String representation of the null filter.
     */
    @Override
    public String toString() {
        return "[ " + nullCheck.toString() + " is null ]";
    }

    /**
     * Compares this filter to the specified object. Returns true if the passed in object is the same as this filter.
     * Checks to make sure the filter types, and the NullCheckValue are the same.
     *
     * @param o - the object to compare this LikeFilter against.
     * @return true if specified object is equal to this filter; false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NullFilterImpl that = (NullFilterImpl) o;
        return Objects.equals(nullCheck, that.nullCheck);
    }

    /**
     * Override of hashCode method.
     *
     * @return a hash code value for this geometry filter.
     */
    @Override
    public int hashCode() {
        int result = 17;
        result = 37 * result + (nullCheck == null ? 0 : nullCheck.hashCode());

        return result;
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
    public Object accept(FilterVisitor visitor, Object extraData) {
        return visitor.visit(this, extraData);
    }
}
