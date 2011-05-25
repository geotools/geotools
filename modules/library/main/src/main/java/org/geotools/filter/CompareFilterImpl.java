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
import java.util.logging.Logger;

import org.geotools.factory.CommonFactoryFinder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.FilterVisitor;


/**
 * Defines a comparison filter (can be a math comparison or generic equals).
 * This filter implements a comparison - of some sort - between two
 * expressions. The comparison may be a math comparison or a generic equals
 * comparison.  If it is a math comparison, only math expressions are allowed;
 * if it is an equals comparison, any expression types are allowed. Note that
 * this comparison does not attempt to restrict its expressions to be
 * meaningful.  This means that it considers itself a valid filter as long as
 * the expression comparison returns a valid result.  It does no checking to
 * see whether or not the expression comparison is meaningful with regard to
 * checking feature attributes.  In other words, this is a valid filter:
 * <b>52 = 92</b>, even though it will always return the same result and could
 * be simplified away.  It is up the the filter creator, therefore, to attempt
 * to simplify/make meaningful filter logic.
 *
 * @author Rob Hranac, Vision for New York
 *
 * @source $URL$
 * @version $Id$
 */
public abstract class CompareFilterImpl extends BinaryComparisonAbstract
    implements CompareFilter {
    /** The logger for the default core module. */
    static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.core");

    /**
     * Constructor with filter type.
     *
     * @param filterType The type of comparison.
     *
     * @throws IllegalFilterException Non-compare type.
     * @deprecated use {@link #CompareFilterImpl(org.opengis.filter.FilterFactory, org.opengis.filter.expression.Expression, org.opengis.filter.expression.Expression)}
     */
    protected CompareFilterImpl(short filterType) throws IllegalFilterException {
    	super(CommonFactoryFinder.getFilterFactory(null));
    	if (isCompareFilter(filterType)) {
            this.filterType = filterType;
        } else {
            throw new IllegalFilterException(
                "Attempted to create compare filter with non-compare type.");
        }
    }

    protected CompareFilterImpl(org.opengis.filter.FilterFactory factory, org.opengis.filter.expression.Expression e1, org.opengis.filter.expression.Expression e2) {
    	this(factory,e1,e2,true);
    }
    
    protected CompareFilterImpl(org.opengis.filter.FilterFactory factory, org.opengis.filter.expression.Expression e1, org.opengis.filter.expression.Expression e2, boolean matchCase ) {
    	super(factory,e1,e2,matchCase);
    }
    
    /**
     * Adds the 'left' value to this filter.
     *
     * @param leftValue Expression for 'left' value.
     *
     * @throws IllegalFilterException Filter is not internally consistent.
     *
     * @task REVISIT: immutability?
     */
    public final void addLeftValue(Expression leftValue)
        throws IllegalFilterException {
        
    	setExpression1(leftValue);
    }
    
    public void setExpression1(org.opengis.filter.expression.Expression leftValue) {
    	//Checks if this is math filter or not and handles appropriately
        if (isMathFilter(filterType)) {
            if (DefaultExpression.isMathExpression(leftValue)
                    || permissiveConstruction) {
                this.expression1 = leftValue;
            } else {
            	throw new IllegalFilterException(
                    "Attempted to add non-math expression to math filter."
				);	
            }
                
        } else {
            this.expression1 = leftValue;
        }
    }

    /**
     * Adds the 'right' value to this filter.
     *
     * @param rightValue Expression for 'right' value.
     *
     * @throws IllegalFilterException Filter is not internally consistent.
     *
     * @task REVISIT: make immutable.
     */
    public final void addRightValue(Expression rightValue)
        throws IllegalFilterException {
       
    	setExpression2(rightValue);
    }

    public void setExpression2(org.opengis.filter.expression.Expression rightValue) {
    	 // Checks if this is math filter or not and handles appropriately
        if (isMathFilter(filterType)) {
            if (DefaultExpression.isMathExpression(rightValue)
                    || permissiveConstruction) {
                this.expression2 = rightValue;
            } else {
            	
            	throw new IllegalFilterException(
                    "Attempted to add non-math expression to math filter."
    			);
                
            }
        } else {
            this.expression2 = rightValue;
        }
    }
    /**
     * Gets the left expression.
     *
     * @return The expression on the left of the comparison.
     * 
     * * @deprecated use {@link #getExpression1()}
     */
    public final Expression getLeftValue() {
        return (Expression)getExpression1();
    }

    /**
     * Gets the right expression.
     *
     * @return The expression on the right of the comparison.
     *
     * @deprecated use {@link #getExpression2()}
     */
    public final Expression getRightValue() {
        return (Expression)getExpression2();
    }

    /**
     * Determines whether or not a given feature is 'inside' this filter.
     *
     * @param feature Specified feature to examine.
     *
     * @return Flag confirming whether or not this feature is inside the
     *         filter.
     */
    public boolean evaluate(SimpleFeature feature){
    	return evaluate((Object)feature);
    }
  
    /**
     * Subclass convenience method which compares to instances of comparables
     * in a pretty lax way, converting types among String, Number, Double when 
     * appropriate.
     * 
     * @return same contract as {@link Comparable#compareTo(java.lang.Object)}.
     */
    protected int compare (Comparable leftObj, Comparable rightObj) {
    	//implements a lax compare, doing some back flips for numbers
    	if (!(leftObj instanceof Number && rightObj instanceof Number)) 
    	{	
    		//check for case of one number one string
    		if (!(leftObj.getClass() == rightObj.getClass()))  
    	    {
        		//differnt classes, if numbers lets try and match them up
    	    	if ( leftObj instanceof Number && (rightObj.getClass() == String.class) )
    	    	{
    	    		try{
    	    			rightObj = new Double( Double.parseDouble( (String) rightObj ));
    	    			leftObj  = new Double(  ((Number) leftObj).doubleValue() );
    	    		}
    	    		catch(Exception e)
    				{
    			    	leftObj = leftObj.toString();
    			    	rightObj = rightObj.toString();
    				}
    	    	}
    	    	else if ( (leftObj.getClass() == String.class) && rightObj instanceof Number )
    	    	{
    	    		try{
    	    			leftObj = new Double( Double.parseDouble( (String) leftObj ) );
    	    			rightObj  = new Double(  ((Number) rightObj).doubleValue() );
    	    		}
    	    		catch(Exception e)
    				{
    			    	leftObj = leftObj.toString();
    			    	rightObj = rightObj.toString();
    				}
    	    	}
    	    	else
    	    	{
    	    		leftObj = leftObj.toString();
    	    		rightObj = rightObj.toString();
    	    	}
    	    }
    		return leftObj.compareTo(rightObj);
    	} else {
    		//both numbers, make double
    	    double left = ((Number) leftObj).doubleValue();
    	    double right = ((Number) rightObj).doubleValue();
    	    return left > right ? 1 : (left == right ? 0 : -1);  
    	}
    }
    
    /**
     * Returns a string representation of this filter.
     *
     * @return String representation of the compare filter.
     */
    public String toString() {
        if (filterType == NULL) {
        	return "[ " + expression1 + " IS NULL ]";
        }
        
        String operator = null;

        if (filterType == COMPARE_EQUALS) {
            operator = " = ";
        }

        if (filterType == COMPARE_LESS_THAN) {
            operator = " < ";
        }

        if (filterType == COMPARE_GREATER_THAN) {
            operator = " > ";
        }

        if (filterType == COMPARE_LESS_THAN_EQUAL) {
            operator = " <= ";
        }

        if (filterType == COMPARE_GREATER_THAN_EQUAL) {
            operator = " >= ";
        }

        if (filterType == COMPARE_NOT_EQUALS) {
            operator = " != ";
        }
        
        return "[ " + expression1 + operator + expression2 + " ]";
    }

    /**
     * Compares this filter to the specified object.  Returns true  if the
     * passed in object is the same as this filter.  Checks  to make sure the
     * filter types are the same as well as both of the values.
     *
     * @param obj - the object to compare this CompareFilter against.
     *
     * @return true if specified object is equal to this filter; false
     *         otherwise.
     */
    public boolean equals(Object obj) {
        if (obj instanceof CompareFilter) {
            CompareFilter cFilter = (CompareFilter) obj;

            // todo - check for nulls here, or make immutable.
            //
            return
                filterType == cFilter.getFilterType()
            	&& (    expression1 == cFilter.getLeftValue()
            			|| (expression1 != null && expression1.equals( cFilter.getLeftValue() ) )
            	    )
            	&& (    expression2 == cFilter.getRightValue()
            			|| (expression2 != null && expression2.equals( cFilter.getRightValue() ) )
            			);            
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
        result = (37 * result)
            + ((expression1 == null) ? 0 : expression1.hashCode());
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
     public abstract Object accept(FilterVisitor visitor, Object extraData);
}
