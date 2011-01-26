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

import com.vividsolutions.jts.geom.Envelope;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.filter.expression.Expression;
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
import org.geotools.factory.Factory;


/**
 * This specifies the interface to create filters.
 *
 * @source $URL$
 * @version $Id$
 *
 * @task TODO: This needs to be massively overhauled.  This should be the
 *       source of immutability of filters.  See {@link FeatureTypeFactory},
 *       as that provides a good example of what this should look like.  The
 *       mutable factory to create immutable objects is a good model for this.
 *       The creation methods should only create fully formed filters.  This
 *       in turn means that all the set functions in the filters should be
 *       eliminated.  When rewriting this class/package, keep in mind
 *       FilterSAXParser in the filter module, as the factory should fit
 *       cleanly with that, and should handle sax parsing without too much
 *       memory overhead.
 * @task REVISIT: resolve errors, should all throw errors?
 *
 * @deprecated use {@link org.opengis.filter.FilterFactory}
 */
public interface FilterFactory extends Factory, org.opengis.filter.FilterFactory2 {
    /**
     * Creates a logic filter from two filters and a type.
     *
     * @param filter1 the first filter to join.
     * @param filter2 the second filter to join.
     * @param filterType must be a logic type.
     *
     * @return the newly constructed logic filter.
     *
     * @throws IllegalFilterException If there were any problems creating the
     *         filter, including wrong type.
     *  @deprecated use one of {@link org.opengis.filter.FilterFactory#and(Filter, Filter)}
     *         {@link org.opengis.filter.FilterFactory#or(Filter, Filter)}
     *         {@link org.opengis.filter.FilterFactory#not(Filter)}
     */
    public LogicFilter createLogicFilter(Filter filter1, Filter filter2, short filterType)
        throws IllegalFilterException;

    /**
     * Creates an empty logic filter from a type.
     *
     * @param filterType must be a logic type.
     *
     * @return the newly constructed logic filter.
     *
     * @throws IllegalFilterException If there were any problems creating the
     *         filter, including wrong type.
     *
     * @deprecated use one of {@link org.opengis.filter.FilterFactory#and(Filter, Filter)}
     *         {@link org.opengis.filter.FilterFactory#or(Filter, Filter)}
     *         {@link org.opengis.filter.FilterFactory#not(Filter)}
     */
    public LogicFilter createLogicFilter(short filterType)
        throws IllegalFilterException;

    /**
     * Creates a logic filter with an initial filter..
     *
     * @param filter the initial filter to set.
     * @param filterType Must be a logic type.
     *
     * @return the newly constructed logic filter.
     *
     * @throws IllegalFilterException If there were any problems creating the
     *         filter, including wrong type.
     * @deprecated use one of {@link org.opengis.filter.FilterFactory#and(Filter, Filter)}
     *         {@link org.opengis.filter.FilterFactory#or(Filter, Filter)}
     *         {@link org.opengis.filter.FilterFactory#not(Filter)}
     */
    public LogicFilter createLogicFilter(Filter filter, short filterType)
        throws IllegalFilterException;

    /**
     * Creates a literal geometry expression from an envelope.
     *
     * @param env the envelope to use for this bounding box.
     *
     * @return The newly created BBoxExpression.
     * @deprecated Please use filterFactory.literal( JTS.toGeometry( bounds ) )
     * @throws IllegalFilterException if there were creation problems.
     */
    public BBoxExpression createBBoxExpression(Envelope env)
        throws IllegalFilterException;

    /**
     * Creates an Integer Literal Expression.
     *
     * @param i the int to serve as literal.
     *
     * @return The new Literal Expression
     */
    public LiteralExpression createLiteralExpression(int i);

    /**
     * Creates a Math Expression
     *
     * @return The new Math Expression
     *
     * @throws IllegalFilterException if there were creation problems.
     *
     * @deprecated use one of
     *         {@link org.opengis.filter.FilterFactory#add(Expression, Expression)}
     *         {@link org.opengis.filter.FilterFactory#subtract(Expression, Expression)}
     *         {@link org.opengis.filter.FilterFactory#multiply(Expression, Expression)}
     *         {@link org.opengis.filter.FilterFactory#divide(Expression, Expression)}
     */
    public MathExpression createMathExpression() throws IllegalFilterException;

    /**
     * Creates a new Fid Filter with no initial fids.
     *
     * @return The new Fid Filter.
     */
    public FidFilter createFidFilter();

    /**
     * Creates an AttributeExpression using the supplied xpath.
     *
     * <p>
     * The supplied xpath can be used to query a varity of content - most
     * notably Features.
     * </p>
     *
     * @param xpath XPath used to retrive value
     *
     * @return The new Attribtue Expression
     */
    public AttributeExpression createAttributeExpression(String xpath);

    /**
     * Creates a Attribute Expression given a schema and attribute path.
     *
     * <p>
     * If you supply a schema, it will be used as a sanitch check for the
     * provided path.
     * </p>
     *
     * @param schema the schema to get the attribute from, or null
     * @param xpath the xPath of the attribute to compare.
     *
     * @return The new Attribute Expression.
     *
     * @throws IllegalFilterException if there were creation problems.
     *
     * @deprecated use createAttributeExpression( xpath ), will be removed for
     *             GeoTools 2.3
     */
    public AttributeExpression createAttributeExpression(SimpleFeatureType schema, String xpath)
        throws IllegalFilterException;

    /**
     * Shortcut the process - will only grab values matching AttributeType.
     *
     * @param at
     *
     * @return The new Attribtue Expression
     *
     * @throws IllegalFilterException if there were creation problems
     *
     * @deprecated use createAttributeExpression( at ), will be removed for
     *             GeoTools 2.3
     */
    public AttributeExpression createAttributeExpression(AttributeDescriptor at)
        throws IllegalFilterException;

    /**
     * Creates a Literal Expression from an Object.
     *
     * @param o the object to serve as the literal.
     *
     * @return The new Literal Expression
     *
     * @throws IllegalFilterException if there were creation problems.
     */
    public LiteralExpression createLiteralExpression(Object o)
        throws IllegalFilterException;

    /**
     * Creates a new compare filter of the given type.
     *
     * @param type the type of comparison - must be a compare type.
     *
     * @return The new compare filter.
     *
     * @throws IllegalFilterException if there were creation problems.
     *
     * @deprecated use one of {@link org.opengis.filter.FilterFactory#less(Expression, Expression)}
     *         {@link org.opengis.filter.FilterFactory#lessOrEqual(Expression, Expression)}
     *         {@link org.opengis.filter.FilterFactory#equals(Expression, Expression)}
     *         {@link org.opengis.filter.FilterFactory#greater(Expression, Expression)}
     *         {@link org.opengis.filter.FilterFactory#greaterOrEqual(Expression, Expression)}
     *         {@link org.opengis.filter.FilterFactory#between(Expression, Expression, Expression)}
     */
    public CompareFilter createCompareFilter(short type)
        throws IllegalFilterException;

    /**
     * Creates an empty Literal Expression
     *
     * @return The new Literal Expression.
     */
    public LiteralExpression createLiteralExpression();

    /**
     * Creates a String Literal Expression
     *
     * @param s the string to serve as the literal.
     *
     * @return The new Literal Expression
     */
    public LiteralExpression createLiteralExpression(String s);

    /**
     * Creates a Double Literal Expression
     *
     * @param d the double to serve as the literal.
     *
     * @return The new Literal Expression
     */
    public LiteralExpression createLiteralExpression(double d);

    /**
     * Creates a Attribute Expression with an initial schema.
     *
     * @param schema the schema to create with.
     *
     * @return The new Attribute Expression.
     * @deprecated use {@link #createAttributeExpression(String)} instead.
     */
    public AttributeExpression createAttributeExpression(SimpleFeatureType schema);

    /**
     * Creates a Math Expression of the given type.
     *
     * @param expressionType must be a math expression type.
     *
     * @return The new Math Expression.
     *
     * @throws IllegalFilterException if there were creation problems.
     *
     * @deprecated use one of
     *  {@link org.opengis.filter.FilterFactory#add(Expression, Expression)}
     *         {@link org.opengis.filter.FilterFactory#subtract(Expression, Expression)}
     *         {@link org.opengis.filter.FilterFactory#multiply(Expression, Expression)}
     *         {@link org.opengis.filter.FilterFactory#divide(Expression, Expression)}
     */
    public MathExpression createMathExpression(short expressionType)
        throws IllegalFilterException;

    /**
     * Creates an empty Null Filter.
     *
     * @return The new Null Filter.
     */
    public NullFilter createNullFilter();

    /**
     * Creates an empty Between Filter.
     *
     * @return The new Between Filter.
     *
     * @throws IllegalFilterException if there were creation problems.
     */
    public BetweenFilter createBetweenFilter() throws IllegalFilterException;

    /**
     * Creates a Geometry Filter.
     *
     * @param filterType the type to create, must be a geometry type.
     *
     * @return The new Geometry Filter.
     *
     * @throws IllegalFilterException if the filterType is not a geometry.
     *
     * @deprecated use one of
     *         {@link org.opengis.filter.FilterFactory#bbox(String, double, double, double, double, String)}
     *  {@link org.opengis.filter.FilterFactory#beyond(String, Geometry, double, String)}
     *  {@link org.opengis.filter.FilterFactory#contains(String, Geometry)}
     *  {@link org.opengis.filter.FilterFactory#crosses(String, Geometry)}
     *  {@link org.opengis.filter.FilterFactory#disjoint(String, Geometry)}
     *  {@link org.opengis.filter.FilterFactory#dwithin(String, Geometry, double, String)}
     *  {@link org.opengis.filter.FilterFactory#equals(String, Geometry)}
     *  {@link org.opengis.filter.FilterFactory#intersects(String, Geometry)}
     *  {@link org.opengis.filter.FilterFactory#overlaps(String, Geometry)}
     *  {@link org.opengis.filter.FilterFactory#touches(String, Geometry)}
     *  {@link org.opengis.filter.FilterFactory#within(String, Geometry)}
     */
    public GeometryFilter createGeometryFilter(short filterType)
        throws IllegalFilterException;

    /**
     * Creates a Geometry Distance Filter
     *
     * @param filterType the type to create, must be beyond or dwithin.
     *
     * @return The new  Expression
     *
     * @throws IllegalFilterException if the filterType is not a geometry
     *         distance type.
     *
     * @deprecated use one of
     *  {@link org.opengis.filter.FilterFactory#beyond(String, Geometry, double, String)}
     *         {@link org.opengis.filter.FilterFactory#dwithin(String, Geometry, double, String)}
     *
     */
    public GeometryDistanceFilter createGeometryDistanceFilter(short filterType)
        throws IllegalFilterException;

    /**
     * Creates a Fid Filter with an initial fid.
     *
     * @param fid the feature ID to create with.
     *
     * @return The new FidFilter.
     */
    public FidFilter createFidFilter(String fid);

    /**
     * Creates a Like Filter.
     *
     * @return The new Like Filter.
     */
    public LikeFilter createLikeFilter();

    /**
     * Creates a Function Expression.
     *
     * @param name the function name.
     *
     * @return The new Function Expression.
     */
    public FunctionExpression createFunctionExpression(String name);

    /**
     * Creates an Environment Variable
     *
     * @param name the function name.
     *
     * @return The new Function Expression.
     */
    public EnvironmentVariable createEnvironmentVariable(String name);

    /**
     * @deprecated use {@link org.opengis.filter.FilterFactory#or(org.opengis.filter.Filter, org.opengis.filter.Filter)}
     */
    public Filter or(Filter f1, Filter f2);

    /**
     * @deprecated use {@link org.opengis.filter.FilterFactory#and(org.opengis.filter.Filter, org.opengis.filter.Filter)}
     */
    public Filter and(Filter f1, Filter f2);

    /**
     * @deprecated use {@link org.opengis.filter.FilterFactory#not(org.opengis.filter.Filter)}
     */
    public Filter not(Filter f);

    ////////////////////////////////////////////////////////////////////////////////
    //
    //      	SPATIAL FILTERS
    //
    ////////////////////////////////////////////////////////////////////////////////

    /** Checks if the geometry expression overlaps the specified bounding box. */
    BBOX bbox(Expression geometry, double minx, double miny, double maxx, double maxy, String srs);

    /** Check if all of a geometry is more distant than the given distance from this object's geometry. */
    Beyond beyond(Expression geometry1, Expression geometry2, double distance, String units);

    /** Checks if the the first geometric operand contains the second. */
    Contains contains(Expression geometry1, Expression geometry2);

    /** Checks if the first geometric operand crosses the second. */
    Crosses crosses(Expression geometry1, Expression geometry2);

    /** Checks if the first operand is disjoint from the second. */
    Disjoint disjoint(Expression geometry1, Expression geometry2);

    /** Checks if any part of the first geometry lies within the given distance of the second geometry. */
    DWithin dwithin(Expression geometry1, Expression geometry2, double distance, String units);

    /** Checks if the geometry of the two operands are equal.
     * @todo should be equals, resolve conflict with PropertyIsEqualTo equals( Expression, Expression )
     */
    Equals equal(Expression geometry1, Expression geometry2);

    /** Checks if the two geometric operands intersect. */
    Intersects intersects(Expression geometry1, Expression geometry2);

    /** Checks if the interior of the first geometry somewhere overlaps the interior of the second geometry. */
    Overlaps overlaps(Expression geometry1, Expression geometry2);

    /** Checks if the feature's geometry touches, but does not overlap with the geometry held by this object. */
    Touches touches(Expression propertyName1, Expression geometry2);

    /** Checks if the feature's geometry is completely contained by the specified constant geometry. */
    Within within(Expression geometry1, Expression geometry2);
}
