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
import org.geotools.api.filter.FilterVisitor;
import org.geotools.api.filter.PropertyIsLessThanOrEqualTo;
import org.geotools.api.filter.expression.Expression;
import org.geotools.util.Converters;

/**
 * Defines a comparison filter (can be a math comparison or generic equals). This filter implements a comparison - of
 * some sort - between two expressions. The comparison may be a math comparison or a generic equals comparison. If it is
 * a math comparison, only math expressions are allowed; if it is an equals comparison, any expression types are
 * allowed. Note that this comparison does not attempt to restrict its expressions to be meaningful. This means that it
 * considers itself a valid filter as long as the expression comparison returns a valid result. It does no checking to
 * see whether or not the expression comparison is meaningful with regard to checking feature attributes. In other
 * words, this is a valid filter: <b>52 = 92</b>, even though it will always return the same result and could be
 * simplified away. It is up the the filter creator, therefore, to attempt to simplify/make meaningful filter logic.
 *
 * @author Rob Hranac, Vision for New York
 * @version $Id$
 */
public abstract class CompareFilterImpl extends BinaryComparisonAbstract {
    /** The logger for the default core module. */
    static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(CompareFilterImpl.class);

    protected CompareFilterImpl(
            org.geotools.api.filter.expression.Expression e1, org.geotools.api.filter.expression.Expression e2) {
        this(e1, e2, true);
    }

    protected CompareFilterImpl(
            org.geotools.api.filter.expression.Expression e1,
            org.geotools.api.filter.expression.Expression e2,
            boolean matchCase) {
        super(e1, e2, matchCase);
    }

    @Override
    public void setExpression1(org.geotools.api.filter.expression.Expression leftValue) {
        this.expression1 = leftValue;
    }

    @Override
    public void setExpression2(org.geotools.api.filter.expression.Expression rightValue) {
        this.expression2 = rightValue;
    }

    /**
     * Subclass convenience method which compares to instances of comparables in a pretty lax way, converting types
     * among String, Number, Double when appropriate.
     *
     * @return same contract as {@link Comparable#compareTo(java.lang.Object)}.
     */
    @SuppressWarnings("unchecked")
    protected int compare(Comparable leftObj, Comparable rightObj) {
        if (leftObj == null || rightObj == null) {
            throw new NullPointerException("Left and right objects are meant to be non null)");
        }
        // implements a lax compare, doing some back flips for numbers
        if (!(leftObj instanceof Number && rightObj instanceof Number)) {
            // check for case of one number one string
            if (!(leftObj.getClass() == rightObj.getClass())) {
                // special handling for dates, as we can do meaningful comparisons among
                // the class hierarchy
                if (leftObj instanceof Date || rightObj instanceof Date) {
                    // they do compare fine if they are both dates, otherwise we need to convert
                    Date leftConverted = null;
                    Date rightConverted = null;
                    if (!(leftObj instanceof Date)) {
                        leftConverted = Converters.convert(leftObj, Date.class);
                    }
                    if (!(rightObj instanceof Date)) {
                        rightConverted = Converters.convert(rightObj, Date.class);
                    }
                    // if conversion failed fall back on string comparison
                    if (leftConverted == null || rightConverted == null) {
                        leftObj = leftObj.toString();
                        rightObj = rightObj.toString();
                    } else {
                        leftObj = leftConverted;
                        rightObj = rightConverted;
                    }
                } else if (leftObj instanceof Number && rightObj.getClass() == String.class) {
                    try {
                        rightObj = Double.valueOf(Double.parseDouble((String) rightObj));
                        leftObj = Double.valueOf(((Number) leftObj).doubleValue());
                    } catch (Exception e) {
                        leftObj = leftObj.toString();
                        rightObj = rightObj.toString();
                    }
                } else if (leftObj.getClass() == String.class && rightObj instanceof Number) {
                    try {
                        leftObj = Double.valueOf(Double.parseDouble((String) leftObj));
                        rightObj = Double.valueOf(((Number) rightObj).doubleValue());
                    } catch (Exception e) {
                        leftObj = leftObj.toString();
                        rightObj = rightObj.toString();
                    }
                } else {
                    leftObj = leftObj.toString();
                    rightObj = rightObj.toString();
                }
            } else if (leftObj instanceof String && rightObj instanceof String) {
                // Check for case of strings that can both be converted to Numbers
                try {
                    leftObj = Double.valueOf(Double.parseDouble((String) leftObj));
                    rightObj = Double.valueOf(Double.parseDouble((String) rightObj));
                } catch (Exception e) {
                    leftObj = leftObj.toString();
                    rightObj = rightObj.toString();
                }
            }
            return leftObj.compareTo(rightObj);
        } else {
            // both numbers, make double
            double left = ((Number) leftObj).doubleValue();
            double right = ((Number) rightObj).doubleValue();
            return left > right ? 1 : left == right ? 0 : -1;
        }
    }

    /**
     * Returns a string representation of this filter.
     *
     * @return String representation of the compare filter.
     */
    @Override
    public String toString() {
        if (this instanceof IsNullImpl) {
            return "[ " + expression1 + " IS NULL ]";
        } else if (this instanceof IsNilImpl) {
            return "[ " + expression1 + " IS NIL ]";
        }
        String operator = null;
        if (this instanceof IsEqualsToImpl) {
            operator = " = ";
        } else if (this instanceof IsLessThenImpl) {
            operator = " < ";
        } else if (this instanceof IsGreaterThanImpl) {
            operator = " > ";
        } else if (this instanceof PropertyIsLessThanOrEqualTo) {
            operator = " <= ";
        } else if (this instanceof IsGreaterThanOrEqualToImpl) {
            operator = " >= ";
        } else if (this instanceof IsNotEqualToImpl) {
            operator = " != ";
        }

        return "[ " + expression1 + operator + expression2 + " ]";
    }

    /**
     * Compares this filter to the specified object. Returns true if the passed in object is the same as this filter.
     * Checks to make sure the filter types are the same as well as both of the values.
     *
     * @param obj - the object to compare this CompareFilter against.
     * @return true if specified object is equal to this filter; false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }

        if (obj.getClass().equals(this.getClass())) {
            CompareFilterImpl cFilter = (CompareFilterImpl) obj;

            // todo - check for nulls here, or make immutable.
            //
            final Expression cfe1 = cFilter.getExpression1();
            final Expression cfe2 = cFilter.getExpression2();
            return (expression1 == cfe1 || expression1 != null && expression1.equals(cfe1))
                    && (expression2 == cfe2 || expression2 != null && expression2.equals(cfe2));
        } else {
            return false;
        }
    }

    /**
     * Override of hashCode method.
     *
     * @return a code to hash this object by.
     */
    @Override
    public int hashCode() {
        int result = 17;
        result = 37 * result + this.getClass().hashCode();
        result = 37 * result + (expression1 == null ? 0 : expression1.hashCode());
        result = 37 * result + (expression2 == null ? 0 : expression2.hashCode());

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
    public abstract Object accept(FilterVisitor visitor, Object extraData);
}
