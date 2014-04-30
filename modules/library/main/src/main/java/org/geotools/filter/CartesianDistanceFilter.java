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

import org.opengis.filter.expression.Expression;
import org.opengis.filter.spatial.Beyond;
import org.opengis.filter.spatial.DWithin;
import org.opengis.filter.spatial.DistanceBufferOperator;


/**
 * Defines geometry filters with a distance element.
 * 
 * <p>
 * These filters are defined in the filter spec by the DistanceBufferType,
 * which contains an additioinal field for a distance.  The two filters that
 * use the distance buffer type are Beyond and DWithin.
 * </p>
 * 
 * <p>
 * From the spec: The spatial operators DWithin and Beyond test whether the
 * value of a geometric property is within or beyond a specified distance of
 * the specified literal geometric value.  Distance values are expressed using
 * the Distance element.
 * </p>
 * 
 * <p>
 * For now this code does not take into account the units of distance,  we will
 * assume that the filter units are the same as the geometry being filtered,
 * and that they are cartesian.
 * </p>
 * 
 * <p></p>
 *
 * @author Chris Holmes, TOPP
 *
 *
 * @source $URL$
 * @version $Id$
 *
 * @task REVISIT: add units for distance.
 */
public abstract class CartesianDistanceFilter extends GeometryFilterImpl
    implements DistanceBufferOperator {
    /** The distance value */
    private double distance;
    /** the distnace units */
    private String units;
    
    @Deprecated
    protected CartesianDistanceFilter() {
    }
    
    protected CartesianDistanceFilter( Expression e1,Expression e2) {
    	super(e1,e2);
    }
    
    protected CartesianDistanceFilter( Expression e1,Expression e2, MatchAction matchAction) {
        super(e1,e2, matchAction);
    }
    
    /**
     * Sets the distance allowed by this filter.
     *
     * @param distance the length beyond which this filter is valid or not.
     */
    public void setDistance(double distance) {
        this.distance = distance;
    }

    /**
     * Gets the distance allowed by this filter.
     *
     * @return distance the length beyond which this filter is valid or not.
     */
    public double getDistance() {
        return distance;
    }
    
    public String getDistanceUnits() {
    	return units;
    }
    
    public void setUnits(String units) {
		this.units = units;
	}
 
    /**
     * Returns a string representation of this filter.
     *
     * @return String representation of the between filter.
     */
    public String toString() {
        String operator = null;

        // Handles all normal geometry cases
        if (this instanceof Beyond) {
            operator = " beyond ";
        } else if (this instanceof DWithin) {
            operator = " dwithin ";
        }

        String distStr = ", distance: " + distance;

        org.opengis.filter.expression.Expression leftGeometry = getExpression1();
        org.opengis.filter.expression.Expression rightGeometry = getExpression2();
        
        if ((leftGeometry == null) && (rightGeometry == null)) {
            return "[ " + "null" + operator + "null" + distStr + " ]";
        } else if (leftGeometry == null) {
            return "[ " + "null" + operator + rightGeometry.toString()
            + distStr + " ]";
        } else if (rightGeometry == null) {
            return "[ " + leftGeometry.toString() + operator + "null" + distStr
            + " ]";
        }

        return "[ " + leftGeometry.toString() + operator
        + rightGeometry.toString() + distStr + " ]";
    }

    /**
     * Returns true if the passed in object is the same as this filter.  Checks
     * to make sure the filter types are the same as well as all three of the
     * values.
     *
     * @param oFilter The filter to test equality against.
     *
     * @return True if the objects are equal.
     */
    public boolean equals(Object oFilter) {
        return super.equals(oFilter) && (distance == distance);
    }

    /**
     * Override of hashCode method.
     *
     * @return a code to hash this object by.
     */
    public int hashCode() {
        int result = super.hashCode();
        long bits = Double.doubleToLongBits(distance);
        result = (37 * result) + (int) (bits ^ (bits >>> 32));

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
}
