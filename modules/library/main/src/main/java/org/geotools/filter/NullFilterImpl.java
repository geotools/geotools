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


import org.opengis.filter.FilterVisitor;
import org.opengis.filter.PropertyIsNull;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.PropertyName;

/**
 * Defines a null filter, which checks to see if an attribute is null.
 *
 * @author Rob Hranac, Vision for New York
 *
 *
 * @source $URL$
 * @version $Id$
 */
public class NullFilterImpl extends AbstractFilter implements PropertyIsNull {
    /** The null check value, which must be an attribute expression. */
    private org.opengis.filter.expression.Expression nullCheck = null;

    /**
     * Constructor which sets the type as null check.
     * @deprecated No argument constructor for use with SAX parser
     */
    protected NullFilterImpl() {
    }
    protected NullFilterImpl(org.opengis.filter.expression.Expression expresion ){
        setExpression(expresion);
    }

    /**
     * Returns the expression which represents the null check.
     */
    public Expression getExpression() {
    	return nullCheck;
    }
   
    /**
     * Sets the expression which represents the null check.
     */
    public void setExpression(Expression nullCheck) {
    	if (nullCheck != null && nullCheck instanceof PropertyName) {
            this.nullCheck = nullCheck;
        } else {
            throw new IllegalFilterException("PropertyName expression required");
        }
    }
    
    /**
     * Determines whether or not a given feature is 'inside' this filter.
     *
     * @param feature Specified feature to examine.
     *
     * @return Flag confirming whether or not this feature is inside the
     *         filter.
     */
    public boolean evaluate(Object feature) {
    	if (nullCheck == null) {
            return false;
        } else {
            return (nullCheck.evaluate(feature) == null);
        }
    }

    /**
     * Returns a string representation of this filter.
     *
     * @return String representation of the null filter.
     */
    public String toString() {
        return "[ " + nullCheck.toString() + " is null ]";
    }

    /**
     * Compares this filter to the specified object.  Returns true  if the
     * passed in object is the same as this filter.  Checks  to make sure the
     * filter types, and the NullCheckValue are the same.
     *
     * @param obj - the object to compare this LikeFilter against.
     *
     * @return true if specified object is equal to this filter; false
     *         otherwise.
     */
    public boolean equals(Object obj) {
        if (obj!=null && obj.getClass() == this.getClass()) {
            NullFilterImpl nullFilter = (NullFilterImpl) obj;

            return ((Filters.getFilterType(nullFilter) == Filters.getFilterType(this)) && nullFilter
                    .getExpression().equals(this.nullCheck));
        } else {
            return false;
        }
    }

    /**
     * Override of hashCode method.
     *
     * @return a hash code value for this geometry filter.
     */
    public int hashCode() {
        int result = 17;
        result = (37 * result)
            + ((nullCheck == null) ? 0 : nullCheck.hashCode());

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
    public Object accept(FilterVisitor visitor, Object extraData) {
    	return visitor.visit(this,extraData);
    }
}
