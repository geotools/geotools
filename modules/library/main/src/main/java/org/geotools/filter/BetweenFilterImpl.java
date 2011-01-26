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


/**
 * Defines a 'between' filter (which is a specialized compare filter). A
 * between filter is just shorthand for a less-than-or-equal filter ANDed with
 * a greater-than-or-equal filter.  Arguably, this would be better handled
 * using those constructs, but the OGC filter specification creates its own
 * object for this, so we do as well.  An important note here is that a
 * between filter is actually a math filter, so its outer (left and right)
 * expressions must be math expressions.  This is enforced by the
 * FilterAbstract class, which considers a BETWEEN operator to be a math
 * filter.
 *
 * @author Rob Hranac, TOPP
 * @source $URL$
 * @version $Id$
 *
 * @task REVISIT: I think AbstractFilter right now does not consider between a
 *       math filter. ch
 *       
 * @deprecated use {@link org.geotools.filter.IsBetweenImpl}
 */
public class BetweenFilterImpl extends CompareFilterImpl
    implements BetweenFilter {
    /** The 'middle' value, which must be an attribute expression. */
    protected org.opengis.filter.expression.Expression middleValue = null;

    protected BetweenFilterImpl(org.opengis.filter.FilterFactory factory) {
    	super(factory,null,null);
    	this.filterType = BETWEEN;
    }
    
    /**
     * Constructor which flags the operator as between.
     *
     * @throws IllegalFilterException Should never happen.
     */
    protected BetweenFilterImpl() throws IllegalFilterException {
        super(BETWEEN);
    }

    /**
     * Sets the values to be compared as between the left and right values.
     *
     * @param middleValue The expression to be compared.
     * 
     * @deprecated use {@link #setExpression(org.opengis.filter.expression.Expression)}
     */
    public final void addMiddleValue(Expression middleValue) {
        setExpression(middleValue);
    }
    
    /**
     * Sets the expression or middle value.
     */
    public void setExpression(org.opengis.filter.expression.Expression expression) {
    	this.middleValue = expression;
    	
    }

    /**
     * Gets the middle value of the between.
     *
     * @return The expression in the middle.
     * 
     * @deprecated use {@link #getExpression()}
     */
    public final Expression getMiddleValue() {
        return (Expression) getExpression();
    }

    /**
     * Gets the middle value of the between.
     *
     * @return The expression in the middle.
     */
    public org.opengis.filter.expression.Expression getExpression() {
    	return middleValue;
    }
    
    /**
     * Returns the left,lower, or first expression.
     */
    public org.opengis.filter.expression.Expression getLowerBoundary() {
    	return getExpression1();
    }
    
    /**
     * Sets the left,lower, or first expression.
     */
    public void setLowerBoundary(org.opengis.filter.expression.Expression lowerBounds) {
    	setExpression1(lowerBounds);
    }
    
    /**
     * Returns the right,upper, or second expression.
     */
    public org.opengis.filter.expression.Expression getUpperBoundary() {
    	return getExpression2();
    }
    
    /**
     * Sets the right,upper, or second expression.
     */
    public void setUpperBoundary(org.opengis.filter.expression.Expression upperBounds) {
    	setExpression2(upperBounds);
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
        if (middleValue == null) {
            return false;
        } else {
	    /*Object leftObj = leftValue.getValue(feature);
	    Object rightObj = rightValue.getValue(feature);
	    Object middleObj = middleValue.getValue(feature);
	    //if (leftObj instanceof Number &&
	    //middleObj instanceof Number &&
	    //rightObj instanceof Number) {
		double left = ((Number) leftObj).doubleValue();
		double right = ((Number) rightObj).doubleValue();
		double mid = ((Number) middleObj).doubleValue();
		
		return (left <= mid) && (right >= mid);
	    */
        Object middleObj = eval(middleValue, feature);
        //evaluate the between wrt the class of the middle object
	    Object leftObj = eval(expression1, feature, middleObj.getClass());
	    Object rightObj = eval(expression2, feature, middleObj.getClass());
	    
	    
	    if (leftObj instanceof Number &&
		middleObj instanceof Number &&
		rightObj instanceof Number) {
		double left = ((Number) leftObj).doubleValue();
		double right = ((Number) rightObj).doubleValue();
		double mid = ((Number) middleObj).doubleValue();
		
		return (left <= mid) && (right >= mid);
		//instanceof Comparator?  And same object type?
	    
		//this may miss variations on similar classes that actually
		//could be compared, but I think AttributeType parsing may
		//have helped us out before we get here.
	    } else if (leftObj.getClass() == middleObj.getClass() &&
		       rightObj.getClass() == middleObj.getClass() &&
		       //I don't think we need to check all for comparator
		       //if they are all the same class?
		       leftObj instanceof Comparable) {
		return (((Comparable)leftObj).compareTo(middleObj) <= 0 &&
			((Comparable)middleObj).compareTo(rightObj) <= 0);
	    } else {
		String mesg = "Supplied between values are either not " +
		    "compatible or not supported for comparison: " + leftObj + 
		    " <= " + middleObj + " <= " + rightObj;
		throw new IllegalArgumentException(mesg);
	    }			 

		
        }
    }

    /**
     * Returns a string representation of this filter.
     *
     * @return String representation of the between filter.
     */
    public String toString() {
        return "[ " + expression1.toString() + " < " + middleValue.toString()
        + " < " + expression2.toString() + " ]";
    }

    /**
     * Returns true if the passed in object is the same as this filter.  Checks
     * to make sure the filter types are the same as well as all three of the
     * values.
     *
     * @param oFilter the filter to test for eqaulity.
     *
     * @return True if the objects are equal.
     */
    public boolean equals(Object oFilter) {
        if (oFilter.getClass() == this.getClass()) {
            BetweenFilterImpl bFilter = (BetweenFilterImpl) oFilter;

            return ((bFilter.getFilterType() == this.filterType)
            && bFilter.getLeftValue().equals(this.expression1)
            && bFilter.getMiddleValue().equals(this.middleValue)
            && bFilter.getRightValue().equals(this.expression2));
        } else {
            return false;
        }
    }

    /**
     * Override of hashCode method.
     *
     * @return a code to hash this filter by.
     */
    public int hashCode() {
        int result = 17;

        result = (37 * result)
            + ((expression1 == null) ? 0 : expression1.hashCode());
        result = (37 * result)
            + ((middleValue == null) ? 0 : middleValue.hashCode());
        result = (37 * result)
            + ((expression2 == null) ? 0 : expression2.hashCode());

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
