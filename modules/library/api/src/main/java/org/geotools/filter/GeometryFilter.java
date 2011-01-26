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

import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.spatial.BinarySpatialOperator;


/**
 * Implements a geometry filter.
 *
 * <p>
 * This filter implements a relationship - of some sort -  between two geometry
 * expressions. Note that this comparison does not attempt to restict its
 * expressions to be meaningful.  This means that it considers itself a valid
 * filter as long as it contains two <b>geometry</b> sub-expressions. It is
 * also slightly less  restrictive than the OGC Filter specification because
 * it does not require that one sub-expression be an geometry attribute and
 * the other be a geometry literal.
 * </p>
 *
 * <p>
 * In other words, you may use this filter to compare two geometries in the
 * same feature, such as: attributeA inside attributeB?  You may also compare
 * two literal geometries, although this is fairly meaningless, since it could
 * be reduced (ie. it is always either true or false).  This approach is very
 * similar to that taken in the FilterCompare class.
 * </p>
 *
 * @author Rob Hranac, TOPP
 * @author Chris Holmes, TOPP
 * @source $URL$
 * @version $Id$
 *
 * @todo REVISIT: make this class (and all filters) immutable, implement
 *       cloneable and return new filters when calling addLeftGeometry and
 *       addRightG Issues to think through: would be cleaner immutability to
 *       have constructor called with left and right Geometries, but this does
 *       not jive with SAX parsing, which is one of the biggest uses of
 *       filters.  But the alternative is not incredibly efficient either, as
 *       there will be two filters that  are just thrown away every time we
 *       make a full geometry filter.  These issues extend to most filters, as
 *       just about all of them are mutable when creating them.  Other issue
 *       is that lots of code will need to  be changed for immutability.
 *       (comments by cholmes) - MUTABLE FACTORIES!  Sax and immutability.
 *
 *  @deprecated use {@link org.opengis.filter.spatial.BinarySpatialOperator}.
 */
public interface GeometryFilter extends Filter, BinarySpatialOperator {
    /**
     * Adds the 'right' value to this filter.
     *
     * @param rightGeometry Expression for 'right' value.
     *
     * @throws IllegalFilterException Filter is not internally consistent.
     *
     * @deprecated use {@link BinarySpatialOperator#setExpression2(Expression)}
     */
    void addRightGeometry(Expression rightGeometry) throws IllegalFilterException;

    /**
     * Adds the 'left' value to this filter.
     *
     * @param leftGeometry Expression for 'left' value.
     *
     * @throws IllegalFilterException Filter is not internally consistent.
     *
     * @task REVISIT: make all filters immutable.
     *
     * @deprecated use {@link BinarySpatialOperator#setExpression1(Expression)}
     */
    void addLeftGeometry(Expression leftGeometry) throws IllegalFilterException;

    /**
     * Determines whether the given feature's geometry passes the geometric
     * relationship of this filter.
     *
     * @param feature the feature to inspect.
     *
     * @return whether the feature meets the conditions of filtering imposed by
     *         this geometry filter.
     */
    boolean contains(SimpleFeature feature);

    /**
     * Retrieves the expression on the right side of the spatial comparison.
     *
     * @return the geometry expression on the right.
     *
     * @deprecated use {@link BinarySpatialOperator#getExpression2()}.
     */
    Expression getRightGeometry();

    /**
     * Retrieves the expression on the left side of the spatial comparison.
     *
     * @return the geometry expression on the left.
     *
     * @deprecated use {@link BinarySpatialOperator#getExpression1()}.
     */
    Expression getLeftGeometry();
}
