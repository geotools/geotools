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
import java.util.Date;
import java.util.logging.Logger;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.util.Converters;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.BinaryComparisonOperator;
import org.opengis.filter.FilterVisitor;
import org.opengis.filter.PropertyIsNull;


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
 *
 * @source $URL$
 * @version $Id$
 */
public abstract class CompareFilterImpl extends BinaryComparisonAbstract {
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
    protected CompareFilterImpl() throws IllegalFilterException {
    }

    protected CompareFilterImpl(org.opengis.filter.expression.Expression e1, org.opengis.filter.expression.Expression e2) {
    	this(e1,e2,true);
    }
    
    protected CompareFilterImpl(org.opengis.filter.expression.Expression e1, org.opengis.filter.expression.Expression e2, boolean matchCase ) {
    	super(e1,e2,matchCase);
    }
    
    public void setExpression1(org.opengis.filter.expression.Expression leftValue) {
        this.expression1 = leftValue;
    }

    public void setExpression2(org.opengis.filter.expression.Expression rightValue) {
        this.expression2 = rightValue;
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
    		    // special handling for dates, as we can do meaningful comparisons among
    		    // the class hierarchy 
    		    if(leftObj instanceof Date || rightObj instanceof Date) {
    		        // they do compare fine if they are both dates, otherwise we need to convert
    		        if(!(leftObj instanceof Date)) {
    		            leftObj = Converters.convert(leftObj, Date.class);
    		        }
                    if(!(rightObj instanceof Date)) {
                        rightObj = Converters.convert(rightObj, Date.class);
                    }
                    // if conversion failed fall back on string comparison
                    if(leftObj == null || rightObj == null) {
                        leftObj = leftObj.toString();
                        rightObj = rightObj.toString();
                    }
   		        } else if ( leftObj instanceof Number && (rightObj.getClass() == String.class) )
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
    	    } else if (leftObj instanceof String && rightObj instanceof String) {
                //Check for case of strings that can both be converted to Numbers
                try {
                    leftObj = new Double( Double.parseDouble( (String) leftObj ) );
                    rightObj = new Double( Double.parseDouble( (String) rightObj ));
                }
                catch(Exception e)
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
        int filterType = Filters.getFilterType( this );
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
        if (obj instanceof CompareFilterImpl) {
            CompareFilterImpl cFilter = (CompareFilterImpl) obj;

            // todo - check for nulls here, or make immutable.
            //
            int filterType = Filters.getFilterType(this);
            return filterType == Filters.getFilterType(cFilter)
                    && (expression1 == cFilter.getExpression1() || (expression1 != null && expression1
                            .equals(cFilter.getExpression1())))
                    && (expression2 == cFilter.getExpression2() || (expression2 != null && expression2
                            .equals(cFilter.getExpression2())));
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
