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
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.geotools.api.filter.spatial.BBOX;
import org.geotools.api.filter.spatial.Beyond;
import org.geotools.api.filter.spatial.BinarySpatialOperator;
import org.geotools.api.filter.spatial.Contains;
import org.geotools.api.filter.spatial.Crosses;
import org.geotools.api.filter.spatial.Disjoint;
import org.geotools.api.filter.spatial.Equals;
import org.geotools.api.filter.spatial.Intersects;
import org.geotools.api.filter.spatial.Overlaps;
import org.geotools.api.filter.spatial.Touches;
import org.geotools.api.filter.spatial.Within;
import org.geotools.util.Converters;
import org.locationtech.jts.geom.Geometry;

/**
 * Implements a geometry filter.
 *
 * <p>This filter implements a relationship - of some sort - between two geometry expressions. Note that this comparison
 * does not attempt to restict its expressions to be meaningful. This means that it considers itself a valid filter as
 * long as it contains two <b>geometry</b> sub-expressions. It is also slightly less restrictive than the OGC Filter
 * specification because it does not require that one sub-expression be an geometry attribute and the other be a
 * geometry literal.
 *
 * <p>In other words, you may use this filter to compare two geometries in the same feature, such as: attributeA inside
 * attributeB? You may also compare two literal geometries, although this is fairly meaningless, since it could be
 * reduced (ie. it is always either true or false). This approach is very similar to that taken in the FilterCompare
 * class.
 *
 * @author Rob Hranac, TOPP
 * @version $Id$
 * @task REVISIT: make this class (and all filters) immutable, implement cloneable and return new filters when calling
 *     addLeftGeometry and addRightG Issues to think through: would be cleaner immutability to have constructor called
 *     with left and right Geometries, but this does not jive with SAX parsing, which is one of the biggest uses of
 *     filters. But the alternative is not incredibly efficient either, as there will be two filters that are just
 *     thrown away every time we make a full geometry filter. These issues extend to most filters, as just about all of
 *     them are mutable when creating them. Other issue is that lots of code will need to be changed for immutability.
 *     (comments by cholmes) - MUTABLE FACTORIES! Sax and immutability.
 */
public abstract class GeometryFilterImpl extends BinaryComparisonAbstract implements BinarySpatialOperator {

    protected MatchAction matchAction;

    protected GeometryFilterImpl(MatchAction matchAction) {
        this.matchAction = matchAction;
    }

    protected GeometryFilterImpl(
            org.geotools.api.filter.expression.Expression e1,
            org.geotools.api.filter.expression.Expression e2,
            MatchAction matchAction) {
        super(e1, e2);
        this.matchAction = matchAction;
    }

    protected GeometryFilterImpl(org.geotools.api.filter.FilterFactory factory) {
        this(MatchAction.ANY);
    }

    protected GeometryFilterImpl(
            org.geotools.api.filter.expression.Expression e1, org.geotools.api.filter.expression.Expression e2) {
        this(e1, e2, MatchAction.ANY);
    }

    /**
     * NC - support for multiple values Convenience method for returning expression as either a geometry or a list of
     * geometries.
     */
    protected static Object getGeometries(org.geotools.api.filter.expression.Expression expr, Object feature) {

        Object o = expr.evaluate(feature);

        if (o instanceof Collection) {
            List<Geometry> list = new ArrayList<>();
            @SuppressWarnings("unchecked")
            Collection<Object> cast = (Collection<Object>) o;
            for (Object item : cast) {
                Geometry geometry = Converters.convert(item, Geometry.class);
                if (geometry != null) {
                    list.add(geometry);
                }
            }
            return list.isEmpty() ? null : list;
        }

        return Converters.convert(o, Geometry.class);
    }

    /**
     * Return this filter as a string.
     *
     * @return String representation of this geometry filter.
     */
    @Override
    public String toString() {
        String operator = null;

        // Handles all normal geometry cases
        if (this instanceof Equals) {
            operator = " equals ";
        } else if (this instanceof Disjoint) {
            operator = " disjoint ";
        } else if (this instanceof Intersects) {
            operator = " intersects ";
        } else if (this instanceof Touches) {
            operator = " touches ";
        } else if (this instanceof Crosses) {
            operator = " crosses ";
        } else if (this instanceof Within) {
            operator = " within ";
        } else if (this instanceof Contains) {
            operator = " contains ";
        } else if (this instanceof Overlaps) {
            operator = " overlaps ";
        } else if (this instanceof Beyond) {
            operator = " beyond ";
        } else if (this instanceof BBOX) {
            operator = " bbox ";
        }

        org.geotools.api.filter.expression.Expression leftGeometry = getExpression1();
        org.geotools.api.filter.expression.Expression rightGeometry = getExpression2();

        if (expression1 == null && rightGeometry == null) {
            return "[ " + "null" + operator + "null" + " ]";
        } else if (leftGeometry == null) {
            return "[ " + "null" + operator + rightGeometry.toString() + " ]";
        } else if (rightGeometry == null) {
            return "[ " + leftGeometry.toString() + operator + "null" + " ]";
        }

        return "[ " + leftGeometry.toString() + operator + rightGeometry.toString() + " ]";
    }

    /**
     * Compares this filter to the specified object. Returns true if the passed in object is the same as this filter.
     * Checks to make sure the filter types are the same as well as the left and right geometries.
     *
     * @param obj - the object to compare this GeometryFilter against.
     * @return true if specified object is equal to this filter; else false
     */
    @Override
    public boolean equals(Object obj) {
        if (this.getClass().isInstance(obj)) {
            GeometryFilterImpl geomFilter = (GeometryFilterImpl) obj;
            return Objects.equals(geomFilter.expression1, expression1)
                    && Objects.equals(geomFilter.expression2, expression2);
        } else {
            return false;
        }
    }

    /**
     * Override of hashCode method.
     *
     * @return a hash code value for this geometry filter.
     */
    @Override
    public int hashCode() {
        org.geotools.api.filter.expression.Expression leftGeometry = getExpression1();
        org.geotools.api.filter.expression.Expression rightGeometry = getExpression2();

        int result = 17;
        result = 37 * result + getClass().hashCode();
        result = 37 * result + (leftGeometry == null ? 0 : leftGeometry.hashCode());
        result = 37 * result + (rightGeometry == null ? 0 : rightGeometry.hashCode());

        return result;
    }

    @Override
    public MatchAction getMatchAction() {
        return matchAction;
    }

    @Override
    public final boolean evaluate(Object feature) {

        Object object1 = getGeometries(getExpression1(), feature);
        Object object2 = getGeometries(getExpression2(), feature);

        if (object1 == null || object2 == null) {
            // default behaviour: if the geometry that is to be filtered is not
            // there we default to not returning anything
            return false;
        }

        if (!(object1 instanceof Collection) && !(object2 instanceof Collection)) {
            return evaluateInternal((Geometry) object1, (Geometry) object2);
        }

        @SuppressWarnings("unchecked")
        Collection<Geometry> leftValues = object1 instanceof Collection
                ? (Collection<Geometry>) object1
                : Collections.singletonList((Geometry) object1);
        @SuppressWarnings("unchecked")
        Collection<Geometry> rightValues = object2 instanceof Collection
                ? (Collection<Geometry>) object2
                : Collections.singletonList((Geometry) object2);

        int count = 0;
        for (Geometry leftValue : leftValues) {
            for (Geometry rightValue : rightValues) {

                boolean temp = evaluateInternal(leftValue, rightValue);
                if (temp) {
                    count++;
                }

                switch (matchAction) {
                    case ONE:
                        if (count > 1) return false;
                        break;
                    case ALL:
                        if (!temp) return false;
                        break;
                    case ANY:
                        if (temp) return true;
                        break;
                }
            }
        }

        switch (matchAction) {
            case ONE:
                return count == 1;
            case ALL:
                return true;
            case ANY:
                return false;
            default:
                return false;
        }
    }

    /**
     * Performs the calculation on the two geometries.
     *
     * @param left the geometry on the left of the equations (the geometry obtained from evaluating Expression1)
     * @param right the geometry on the right of the equations (the geometry obtained from evaluating Expression2)
     * @return true if the filter evaluates to true for the two geometries
     */
    protected abstract boolean evaluateInternal(Geometry left, Geometry right);
}
