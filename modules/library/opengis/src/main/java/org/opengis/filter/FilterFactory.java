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

import java.util.List;
import java.util.Set;

import org.opengis.filter.capability.ArithmeticOperators;
import org.opengis.filter.capability.ComparisonOperators;
import org.opengis.filter.capability.FilterCapabilities;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.capability.Functions;
import org.opengis.filter.capability.GeometryOperand;
import org.opengis.filter.capability.IdCapabilities;
import org.opengis.filter.capability.Operator;
import org.opengis.filter.capability.ScalarCapabilities;
import org.opengis.filter.capability.SpatialCapabilities;
import org.opengis.filter.capability.SpatialOperator;
import org.opengis.filter.capability.SpatialOperators;
import org.opengis.filter.expression.Add;
import org.opengis.filter.expression.Divide;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.Multiply;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.expression.Subtract;
import org.opengis.filter.identity.FeatureId;
import org.opengis.filter.identity.GmlObjectId;
import org.opengis.filter.identity.Identifier;
import org.opengis.filter.sort.SortBy;
import org.opengis.filter.sort.SortOrder;
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
import org.opengis.geometry.Geometry;


/**
 * Interface whose methods allow the caller to create instances of the various
 * {@link Filter} and {@link Expression} subclasses.
 * <p>
 * @version <A HREF="http://www.opengis.org/docs/02-059.pdf">Implementation specification 1.0</A>
 * @author Chris Dillard (SYS Technologies)
 * @since GeoAPI 2.0
 */
public interface FilterFactory {
        /**
         * The FilterCapabilities data structure is used to describe the abilities of
         * this FilterFactory, it includes restrictions on the available spatial operations,
         * scalar operations, lists the supported functions, and describes what geometry
         * literals are understood.
         * @return FilterCapabilities describing the abilities of this FilterFactory
         */
        // FilterCapabilities getCapabilities();
        
////////////////////////////////////////////////////////////////////////////////
//
//  IDENTIFIERS
//
////////////////////////////////////////////////////////////////////////////////
    /** Creates a new feautre id from a string */
    FeatureId featureId(String id);

    /** Creates a new gml object id from a string */
    GmlObjectId gmlObjectId(String id);

////////////////////////////////////////////////////////////////////////////////
//
//  FILTERS
//
////////////////////////////////////////////////////////////////////////////////

    /** {@code AND} filter between two filters. */
    And and(Filter f, Filter g);

    /** {@code AND} filter between a list of filters. */
    And and(List<Filter> f);

    /** {@code OR} filter between two filters. */
    Or or(Filter f, Filter g);

    /** {@code OR} filter between a list of filters. */
    Or or (List<Filter> f);

    /** Reverses the logical value of a filter. */
    Not not(Filter f);

    /** Passes only for objects that have one of the IDs given to this object. */
    Id id(Set<? extends Identifier> ids);

    /** Retrieves the value of a {@linkplain org.opengis.feature.Feature feature}'s property. */
    PropertyName property(String name);

    /** A compact way of encoding a range check. */
    PropertyIsBetween between(Expression expr, Expression lower, Expression upper);

    /** Compares that two sub-expressions are equal to each other.
     * @todo should be equal (so equals can refer to geometry)
     */
    PropertyIsEqualTo equals(Expression expr1, Expression expr2);

    /** Compares that two sub-expressions are equal to eacher other */
    PropertyIsEqualTo equal(Expression expr1, Expression expr2, boolean matchCase);

    /** Checks that the first sub-expression is not equal to the second subexpression. */
    PropertyIsNotEqualTo notEqual(Expression expr1, Expression expr2 );
    
    /**
     * Checks that the first sub-expression is not equal to the second subexpression.
     * 
     * @param expr1 first expression
     * @param expr2 second expression
     * @param matchCase true if the comparison should be case insensitive
     * @return evaluates to true of expr1 not equal to expr2 
     */
    PropertyIsNotEqualTo notEqual(Expression expr1, Expression expr2, boolean matchCase);

    /** Checks that the first sub-expression is greater than the second subexpression. */
    PropertyIsGreaterThan greater(Expression expr1, Expression expr2);
    
    /**
     * Checks that the first sub-expression is greater than the second subexpression.
     * 
     * @param expr1 first expression
     * @param expr2 second expression
     * @param matchCase true if the comparison should be case insensitive
     * @return evaluates to true of expr1 is greater than expr2
     */
    PropertyIsGreaterThan greater(Expression expr1, Expression expr2, boolean matchCase);

    /** Checks that the first sub-expression is greater or equal to the second subexpression. */
    PropertyIsGreaterThanOrEqualTo greaterOrEqual(Expression expr1, Expression expr2);
    
    /** Checks that the first sub-expression is greater or equal to the second subexpression. */
    PropertyIsGreaterThanOrEqualTo greaterOrEqual(Expression expr1, Expression expr2, boolean matchCase);

    /** Checks that its first sub-expression is less than its second subexpression. */
    PropertyIsLessThan less(Expression expr1, Expression expr2);
    
    PropertyIsLessThan less(Expression expr1, Expression expr2, boolean matchCase);
    
    /** Checks that its first sub-expression is less than or equal to its second subexpression. */
    PropertyIsLessThanOrEqualTo lessOrEqual(Expression expr1, Expression expr2);
    
    PropertyIsLessThanOrEqualTo lessOrEqual(Expression expr1, Expression expr2, boolean matchCase);

    /** Character string comparison operator with pattern matching and default wildcards. */
    PropertyIsLike like(Expression expr, String pattern);

    /** Character string comparison operator with pattern matching and specified wildcards. */
    PropertyIsLike like(Expression expr, String pattern, String wildcard, String singleChar, String escape);

    /** Character string comparison operator with pattern matching and specified wildcards. */
    PropertyIsLike like(Expression expr, String pattern, String wildcard, String singleChar, String escape, boolean matchCase);
    
    /** Checks if an expression's value is {@code null}. */
    PropertyIsNull isNull(Expression expr);

////////////////////////////////////////////////////////////////////////////////
//
//  SPATIAL FILTERS
//
////////////////////////////////////////////////////////////////////////////////

    /**
     * Checks if the bounding box of the feature's geometry overlaps the indicated bounds.
     * <p>
     * This method is defined in strict accordance with the Filter 1.0 specification, you may
     * find the FilterFactory2.bbox(Expression, BoundingBox) to be easier to use.
     * </p>
     * @param propertyName Name of geometry property (for a PropertyName to access a Feature's Geometry)
     * @param minx Minimum "x" value (for a literal BoundingBox)
     * @param miny Minimum "y" value (for a literal BoundingBox)
     * @param maxx Maximum "x" value (for a literal BoundingBox)
     * @param maxy Maximum "y" value (for a literal BoundingBox)
     * @param srs Indicating the CoordianteReferenceSystem to use for a literal BoundingBox
     */
    BBOX        bbox(String propertyName, double minx, double miny, double maxx, double maxy, String srs);

    /** Check if all of a feature's geometry is more distant than the given distance from this object's geometry. */
    Beyond      beyond(String propertyName, Geometry geometry, double distance, String units);

    /** Checks if the the first geometric operand contains the second. */
    Contains    contains(String propertyName, Geometry geometry);

    /** Checks if the first geometric operand crosses the second. */
    Crosses     crosses(String propertyName, Geometry geometry);

    /** Checks if the first operand is disjoint from the second. */
    Disjoint    disjoint(String propertyName, Geometry geometry);

    /** Checks if any part of the first geometry lies within the given distance of the second geometry. */
    DWithin     dwithin(String propertyName, Geometry geometry, double distance, String units);

    /** Checks if the geometry of the two operands are equal. */
    Equals      equals(String propertyName, Geometry geometry);

    /** Checks if the two geometric operands intersect. */
    Intersects  intersects(String propertyName, Geometry geometry);

    /** Checks if the interior of the first geometry somewhere overlaps the interior of the second geometry. */
    Overlaps    overlaps(String propertyName, Geometry geometry);

    /** Checks if the feature's geometry touches, but does not overlap with the geometry held by this object. */
    Touches     touches(String propertyName, Geometry geometry);

    /** Checks if the feature's geometry is completely contained by the specified constant geometry. */
    Within      within(String propertyName, Geometry geometry);

////////////////////////////////////////////////////////////////////////////////
//
//  EXPRESSIONS
//
////////////////////////////////////////////////////////////////////////////////

    /** Computes the numeric addition of the first and second operand. */
    Add       add(Expression expr1, Expression expr2);

    /** Computes the numeric quotient resulting from dividing the first operand by the second. */
    Divide    divide(Expression expr1, Expression expr2);

    /** Computes the numeric product of their first and second operand. */
    Multiply  multiply(Expression expr1, Expression expr2);

    /** Computes the numeric difference between the first and second operand. */
    Subtract  subtract(Expression expr1, Expression expr2);

    /** Call into some implementation-specific function. */
    Function  function(String name, Expression ... args);

    /** A constant, literal value that can be used in expressions. */
    Literal  literal(Object obj);

    /** A constant, literal {@link Byte} value that can be used in expressions. */
    Literal  literal(byte b);

    /** A constant, literal {@link Short} value that can be used in expressions. */
    Literal  literal(short s);

    /** A constant, literal {@link Integer} value that can be used in expressions. */
    Literal  literal(int i);

    /** A constant, literal {@link Long} value that can be used in expressions. */
    Literal  literal(long l);

    /** A constant, literal {@link Float} value that can be used in expressions. */
    Literal  literal(float f);

    /** A constant, literal {@link Double} value that can be used in expressions. */
    Literal  literal(double d);

    /** A constant, literal {@link Character} value that can be used in expressions. */
    Literal  literal(char c);

    /** A constant, literal {@link Boolean} value that can be used in expressions. */
    Literal  literal(boolean b);

    ////////////////////////////////////////////////////////////////////////////////
    //
    //  SORT BY
    //
    //////////////////////////////////////////////////////////////////////////////    //
    /** Indicates an property by which contents should be sorted, along with intended order. */
    SortBy sort(String propertyName, SortOrder order);

    ////////////////////////////////////////////////////////////////////////////////
    //
    //  CAPABILITIES
    //
    ////////////////////////////////////////////////////////////////////////////////
    /** operators */
    Operator operator(String name);

    /** spatial operator */
    SpatialOperator spatialOperator(String name, GeometryOperand[] geometryOperands);

    /** function name */
    FunctionName functionName(String name, int nargs);

    /** functions */
    Functions functions(FunctionName[] functionNames);

    /** spatial operators */
    SpatialOperators spatialOperators(SpatialOperator[] spatialOperators);

    /** comparison operators */
    ComparisonOperators comparisonOperators(Operator[] comparisonOperators);

    /** arithmetic operators */
    ArithmeticOperators arithmeticOperators(boolean simple, Functions functions);

    /** scalar capabilities */
    ScalarCapabilities scalarCapabilities(ComparisonOperators comparison,
        ArithmeticOperators arithmetic, boolean logical);

    /** spatial capabilities */
    SpatialCapabilities spatialCapabilities(GeometryOperand[] geometryOperands,
        SpatialOperators spatial);

    /** id capabilities */
    IdCapabilities idCapabilities(boolean eid, boolean fid);

    /** filter capabilities */
    FilterCapabilities capabilities(String version, ScalarCapabilities scalar,
        SpatialCapabilities spatial, IdCapabilities id);
}
