/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2005 Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.filter;

import org.opengis.feature.type.Name;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.spatial.BBOX;
import org.opengis.filter.spatial.Beyond;
import org.opengis.filter.spatial.Contains;
import org.opengis.filter.spatial.Crosses;
import org.opengis.filter.spatial.DWithin;
import org.opengis.filter.spatial.Disjoint;
import org.opengis.filter.spatial.Equals;
import org.opengis.filter.spatial.Intersects;
import org.opengis.filter.spatial.Overlaps;
import org.opengis.filter.spatial.Touches;
import org.opengis.filter.spatial.Within;
import org.opengis.geometry.BoundingBox;
import org.xml.sax.helpers.NamespaceSupport;


/**
 * Allows creation of additional Filter constructs.
 * <p>
 * Why do we need this? Because not all implementations are going
 * to be using geoapi Geometry. This allows the creation of complient
 * filters with SFSQL Geometry constructs. Consider this a bridge to
 * existing projects allowing GeoAPI to be used.
 * </p>
 * @version <A HREF="http://www.opengis.org/docs/02-059.pdf">Implementation specification 1.1</A>
 * @author Jody Garnett, Refractions Research Inc.
 * @since GeoAPI 2.1
 */
public interface FilterFactory2 extends FilterFactory {
////////////////////////////////////////////////////////////////////////////////
//
//  FILTERS
//
////////////////////////////////////////////////////////////////////////////////

    /**
     * Retrieves the value of a {@linkplain org.opengis.feature.Feature feature}'s property.
     * @param name Name of attribute referenced
     * @return PropertyName
     */
    PropertyName property(Name name);
    
    /**
     * Retrieves the value of a {@linkplain org.opengis.feature.Feature feature}'s property.
     * 
     * @param xpath XPath expression (subject to the restrictions of filter specificaiton)
     * @param namespaceContext Used to interpret any namespace prefixs in above xpath expression
     * @return PropertyName
     */
     
    PropertyName property(String xpath, NamespaceSupport namespaceContext);

    /**
     * Character string comparison operator with pattern matching and specified wildcards.
     *      
     * @param expr
     * @param pattern
     * @param wildcard
     * @param singleChar
     * @param escape
     * @param matchCase
     * @return
     */
    PropertyIsLike like(Expression expr, String pattern, String wildcard, String singleChar, String escape, boolean matchCase);
    
////////////////////////////////////////////////////////////////////////////////
//
//  SPATIAL FILTERS
//
////////////////////////////////////////////////////////////////////////////////

    /** Checks if the geometry expression overlaps the specified bounding box. */
    BBOX        bbox( Expression geometry, double minx, double miny, double maxx, double maxy, String srs);

    /**
     * Checks if the bounding box of the feature's geometry overlaps the indicated bounds.
     * <p>
     * This method does not strictly confirm to the the Filter 1.0 specification, you may
     * use it to check expressions other than PropertyName.
     * </p>
     * @param geometry Expression used to access a Geometry, in order to check for interaction with bounds
     * @param bounds Indicates the bounds to check geometry against
     */
    BBOX        bbox( Expression geometry, BoundingBox bounds);


    /** Check if all of a geometry is more distant than the given distance from this object's geometry. */
    Beyond      beyond( Expression geometry1, Expression geometry2, double distance, String units);

    /** Checks if the the first geometric operand contains the second. */
    Contains    contains(Expression geometry1, Expression geometry2);

    /** Checks if the first geometric operand crosses the second. */
    Crosses     crosses(Expression geometry1, Expression geometry2);

    /** Checks if the first operand is disjoint from the second. */
    Disjoint    disjoint(Expression geometry1, Expression geometry2);

    /** Checks if any part of the first geometry lies within the given distance of the second geometry. */
    DWithin     dwithin(Expression geometry1, Expression geometry2, double distance, String units);

    /** Checks if the geometry of the two operands are equal.
     * @todo should be equals, resolve conflict with PropertyIsEqualTo equals( Expression, Expression )
     */
    Equals      equal(Expression geometry1, Expression geometry2);

    /** Checks if the two geometric operands intersect. */
    Intersects  intersects(Expression geometry1, Expression geometry2);

    /** Checks if the interior of the first geometry somewhere overlaps the interior of the second geometry. */
    Overlaps    overlaps(Expression geometry1, Expression geometry2);

    /** Checks if the feature's geometry touches, but does not overlap with the geometry held by this object. */
    Touches     touches(Expression propertyName1, Expression geometry2);

    /** Checks if the feature's geometry is completely contained by the specified constant geometry. */
    Within      within(Expression geometry1, Expression geometry2);
}
