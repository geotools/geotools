/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2005 Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.geotools.api.filter;

import java.util.Date;
import java.util.List;
import java.util.Set;
import org.geotools.api.feature.type.Name;
import org.geotools.api.filter.MultiValuedFilter.MatchAction;
import org.geotools.api.filter.capability.ArithmeticOperators;
import org.geotools.api.filter.capability.ComparisonOperators;
import org.geotools.api.filter.capability.FilterCapabilities;
import org.geotools.api.filter.capability.FunctionName;
import org.geotools.api.filter.capability.Functions;
import org.geotools.api.filter.capability.GeometryOperand;
import org.geotools.api.filter.capability.IdCapabilities;
import org.geotools.api.filter.capability.Operator;
import org.geotools.api.filter.capability.ScalarCapabilities;
import org.geotools.api.filter.capability.SpatialCapabilities;
import org.geotools.api.filter.capability.SpatialOperator;
import org.geotools.api.filter.capability.SpatialOperators;
import org.geotools.api.filter.capability.TemporalCapabilities;
import org.geotools.api.filter.capability.TemporalOperator;
import org.geotools.api.filter.expression.Add;
import org.geotools.api.filter.expression.Divide;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.Function;
import org.geotools.api.filter.expression.Literal;
import org.geotools.api.filter.expression.Multiply;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.api.filter.expression.Subtract;
import org.geotools.api.filter.identity.FeatureId;
import org.geotools.api.filter.identity.GmlObjectId;
import org.geotools.api.filter.identity.Identifier;
import org.geotools.api.filter.identity.ResourceId;
import org.geotools.api.filter.identity.Version;
import org.geotools.api.filter.sort.SortBy;
import org.geotools.api.filter.sort.SortOrder;
import org.geotools.api.filter.spatial.BBOX;
import org.geotools.api.filter.spatial.BBOX3D;
import org.geotools.api.filter.spatial.Beyond;
import org.geotools.api.filter.spatial.Contains;
import org.geotools.api.filter.spatial.Crosses;
import org.geotools.api.filter.spatial.DWithin;
import org.geotools.api.filter.spatial.Disjoint;
import org.geotools.api.filter.spatial.Equals;
import org.geotools.api.filter.spatial.Intersects;
import org.geotools.api.filter.spatial.Overlaps;
import org.geotools.api.filter.spatial.Touches;
import org.geotools.api.filter.spatial.Within;
import org.geotools.api.filter.temporal.After;
import org.geotools.api.filter.temporal.AnyInteracts;
import org.geotools.api.filter.temporal.Before;
import org.geotools.api.filter.temporal.Begins;
import org.geotools.api.filter.temporal.BegunBy;
import org.geotools.api.filter.temporal.During;
import org.geotools.api.filter.temporal.EndedBy;
import org.geotools.api.filter.temporal.Ends;
import org.geotools.api.filter.temporal.Meets;
import org.geotools.api.filter.temporal.MetBy;
import org.geotools.api.filter.temporal.OverlappedBy;
import org.geotools.api.filter.temporal.TContains;
import org.geotools.api.filter.temporal.TEquals;
import org.geotools.api.filter.temporal.TOverlaps;
import org.geotools.api.geometry.BoundingBox;
import org.geotools.api.geometry.BoundingBox3D;
import org.geotools.api.parameter.Parameter;
import org.geotools.api.util.InternationalString;
import org.locationtech.jts.geom.Geometry;
import org.xml.sax.helpers.NamespaceSupport;

/**
 * Interface whose methods allow the caller to create instances of the various {@link Filter} and
 * {@link Expression} subclasses.
 *
 * <p>
 *
 * @version <A HREF="http://www.opengis.org/docs/02-059.pdf">Implementation specification 1.0</A>
 * @author Chris Dillard (SYS Technologies)
 * @since GeoAPI 2.0
 */
public interface FilterFactory {
    /**
     * The FilterCapabilities data structure is used to describe the abilities of this
     * FilterFactory, it includes restrictions on the available spatial operations, scalar
     * operations, lists the supported functions, and describes what geometry literals are
     * understood.
     *
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

    /** Creates a new feature id with version information */
    FeatureId featureId(String fid, String featureVersion);

    /** Creates a new gml object id from a string */
    GmlObjectId gmlObjectId(String id);

    /** ResouceId for identifier based query */
    ResourceId resourceId(String fid, String featureVersion, Version version);

    /**
     * ResourceId for time based query.
     *
     * <p>Date range constructor for a feature id; none or one of {@code start} and {@code end} can
     * be {@code null}, making for an unconstrained date range at either of the ends.
     *
     * @param fid feature id, non null;
     * @param startTime lower end timestamp of the time range, inclusive, or {@code null} only if
     *     {@code end != null}
     * @param endTime upper end timestamp of the time range, inclusive, or {@code null} only if
     *     {@code start != null}
     */
    ResourceId resourceId(String fid, Date startTime, Date endTime);

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
    Or or(List<Filter> f);

    /** Reverses the logical value of a filter. */
    Not not(Filter f);

    /** Passes only for objects that have one of the IDs given to this object. */
    Id id(Set<? extends Identifier> ids);

    /**
     * Retrieves the value of a {@linkplain org.geotools.api.feature.Feature feature}'s property.
     */
    PropertyName property(String name);

    /** A compact way of encoding a range check. */
    PropertyIsBetween between(Expression expr, Expression lower, Expression upper);

    /** A compact way of encoding a range check. */
    PropertyIsBetween between(
            Expression expr, Expression lower, Expression upper, MatchAction matchAction);

    /**
     * Compares that two sub-expressions are equal to each other.
     *
     * @todo should be equal (so equals can refer to geometry)
     */
    PropertyIsEqualTo equals(Expression expr1, Expression expr2);

    /** Compares that two sub-expressions are equal to eacher other */
    PropertyIsEqualTo equal(Expression expr1, Expression expr2, boolean matchCase);

    /** Compares that two sub-expressions are equal to eacher other */
    PropertyIsEqualTo equal(
            Expression expr1, Expression expr2, boolean matchCase, MatchAction matchAction);

    /** Checks that the first sub-expression is not equal to the second subexpression. */
    PropertyIsNotEqualTo notEqual(Expression expr1, Expression expr2);

    /**
     * Checks that the first sub-expression is not equal to the second subexpression.
     *
     * @param expr1 first expression
     * @param expr2 second expression
     * @param matchCase true if the comparison should be case insensitive
     * @return evaluates to true of expr1 not equal to expr2
     */
    PropertyIsNotEqualTo notEqual(Expression expr1, Expression expr2, boolean matchCase);

    /**
     * Checks that the first sub-expression is not equal to the second subexpression.
     *
     * @param expr1 first expression
     * @param expr2 second expression
     * @param matchCase true if the comparison should be case insensitive
     * @param matchAction action for multi-valued properties
     * @return evaluates to true of expr1 not equal to expr2
     */
    PropertyIsNotEqualTo notEqual(
            Expression expr1, Expression expr2, boolean matchCase, MatchAction matchAction);

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

    /**
     * Checks that the first sub-expression is greater than the second subexpression.
     *
     * @param expr1 first expression
     * @param expr2 second expression
     * @param matchCase true if the comparison should be case insensitive
     * @return evaluates to true of expr1 is greater than expr2
     */
    PropertyIsGreaterThan greater(
            Expression expr1, Expression expr2, boolean matchCase, MatchAction matchAction);

    /** Checks that the first sub-expression is greater or equal to the second subexpression. */
    PropertyIsGreaterThanOrEqualTo greaterOrEqual(Expression expr1, Expression expr2);

    /** Checks that the first sub-expression is greater or equal to the second subexpression. */
    PropertyIsGreaterThanOrEqualTo greaterOrEqual(
            Expression expr1, Expression expr2, boolean matchCase);

    /** Checks that the first sub-expression is greater or equal to the second subexpression. */
    PropertyIsGreaterThanOrEqualTo greaterOrEqual(
            Expression expr1, Expression expr2, boolean matchCase, MatchAction matchAction);

    /** Checks that its first sub-expression is less than its second subexpression. */
    PropertyIsLessThan less(Expression expr1, Expression expr2);

    PropertyIsLessThan less(Expression expr1, Expression expr2, boolean matchCase);

    PropertyIsLessThan less(
            Expression expr1, Expression expr2, boolean matchCase, MatchAction matchAction);

    /** Checks that its first sub-expression is less than or equal to its second subexpression. */
    PropertyIsLessThanOrEqualTo lessOrEqual(Expression expr1, Expression expr2);

    PropertyIsLessThanOrEqualTo lessOrEqual(Expression expr1, Expression expr2, boolean matchCase);

    PropertyIsLessThanOrEqualTo lessOrEqual(
            Expression expr1, Expression expr2, boolean matchCase, MatchAction matchAction);

    /** Character string comparison operator with pattern matching and default wildcards. */
    PropertyIsLike like(Expression expr, String pattern);

    /** Character string comparison operator with pattern matching and specified wildcards. */
    PropertyIsLike like(
            Expression expr, String pattern, String wildcard, String singleChar, String escape);

    /** Character string comparison operator with pattern matching and specified wildcards. */
    PropertyIsLike like(
            Expression expr,
            String pattern,
            String wildcard,
            String singleChar,
            String escape,
            boolean matchCase);

    /** Character string comparison operator with pattern matching and specified wildcards. */
    PropertyIsLike like(
            Expression expr,
            String pattern,
            String wildcard,
            String singleChar,
            String escape,
            boolean matchCase,
            MatchAction matchAction);

    /** Checks if an expression's value is {@code null}. */
    PropertyIsNull isNull(Expression expr);

    /** Checks if an expression's value is nil. */
    PropertyIsNil isNil(Expression expr, Object nilReason);

    ////////////////////////////////////////////////////////////////////////////////
    //
    //  SPATIAL FILTERS
    //
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * Checks if the bounding box of the feature's geometry overlaps the indicated bounds.
     *
     * <p>This method is defined in strict accordance with the Filter 1.0 specification, you may
     * find the FilterFactory.bbox(Expression, BoundingBox) to be easier to use.
     *
     * @param propertyName Name of geometry property (for a PropertyName to access a Feature's
     *     Geometry)
     * @param minx Minimum "x" value (for a literal BoundingBox)
     * @param miny Minimum "y" value (for a literal BoundingBox)
     * @param maxx Maximum "x" value (for a literal BoundingBox)
     * @param maxy Maximum "y" value (for a literal BoundingBox)
     * @param srs Indicating the CoordinateReferenceSystem to use for a literal BoundingBox
     */
    BBOX bbox(String propertyName, double minx, double miny, double maxx, double maxy, String srs);

    BBOX bbox(Expression propertyName, Expression bounds);

    BBOX bbox(Expression propertyName, Expression bounds, MatchAction matchAction);

    BBOX3D bbox(String propertyName, BoundingBox3D env);

    BBOX3D bbox(String propertyName, BoundingBox3D env, MatchAction matchAction);

    /**
     * Checks if the bounding box of the feature's geometry overlaps the indicated bounds.
     *
     * <p>This method is defined in strict accordance with the Filter 1.0 specification, you may
     * find the FilterFactory.bbox(Expression, BoundingBox) to be easier to use.
     *
     * @param propertyName Name of geometry property (for a PropertyName to access a Feature's
     *     Geometry)
     * @param minx Minimum "x" value (for a literal BoundingBox)
     * @param miny Minimum "y" value (for a literal BoundingBox)
     * @param maxx Maximum "x" value (for a literal BoundingBox)
     * @param maxy Maximum "y" value (for a literal BoundingBox)
     * @param srs Indicating the CoordinateReferenceSystem to use for a literal BoundingBox
     */
    BBOX bbox(
            String propertyName,
            double minx,
            double miny,
            double maxx,
            double maxy,
            String srs,
            MatchAction matchAction);

    /**
     * Check if all of a feature's geometry is more distant than the given distance from this
     * object's geometry.
     */
    Beyond beyond(String propertyName, Geometry geometry, double distance, String units);

    /**
     * Check if all of a feature's geometry is more distant than the given distance from this
     * object's geometry.
     */
    Beyond beyond(
            String propertyName,
            Geometry geometry,
            double distance,
            String units,
            MatchAction matchAction);

    /** Checks if the the first geometric operand contains the second. */
    Contains contains(String propertyName, Geometry geometry);

    /** Checks if the the first geometric operand contains the second. */
    Contains contains(String propertyName, Geometry geometry, MatchAction matchAction);

    /** Checks if the first geometric operand crosses the second. */
    Crosses crosses(String propertyName, Geometry geometry);

    /** Checks if the first geometric operand crosses the second. */
    Crosses crosses(String propertyName, Geometry geometry, MatchAction matchAction);

    /** Checks if the first operand is disjoint from the second. */
    Disjoint disjoint(String propertyName, Geometry geometry);

    /** Checks if the first operand is disjoint from the second. */
    Disjoint disjoint(String propertyName, Geometry geometry, MatchAction matchAction);

    /**
     * Checks if any part of the first geometry lies within the given distance of the second
     * geometry.
     */
    DWithin dwithin(String propertyName, Geometry geometry, double distance, String units);

    /**
     * Checks if any part of the first geometry lies within the given distance of the second
     * geometry.
     */
    DWithin dwithin(
            String propertyName,
            Geometry geometry,
            double distance,
            String units,
            MatchAction matchAction);

    /** Checks if the geometry of the two operands are equal. */
    Equals equals(String propertyName, Geometry geometry);

    /** Checks if the geometry of the two operands are equal. */
    Equals equals(String propertyName, Geometry geometry, MatchAction matchAction);

    /** Checks if the two geometric operands intersect. */
    Intersects intersects(String propertyName, Geometry geometry);

    /** Checks if the two geometric operands intersect. */
    Intersects intersects(String propertyName, Geometry geometry, MatchAction matchAction);

    /**
     * Checks if the interior of the first geometry somewhere overlaps the interior of the second
     * geometry.
     */
    Overlaps overlaps(String propertyName, Geometry geometry);

    /**
     * Checks if the interior of the first geometry somewhere overlaps the interior of the second
     * geometry.
     */
    Overlaps overlaps(String propertyName, Geometry geometry, MatchAction matchAction);

    /**
     * Checks if the feature's geometry touches, but does not overlap with the geometry held by this
     * object.
     */
    Touches touches(String propertyName, Geometry geometry);

    /**
     * Checks if the feature's geometry touches, but does not overlap with the geometry held by this
     * object.
     */
    Touches touches(String propertyName, Geometry geometry, MatchAction matchAction);

    /**
     * Checks if the feature's geometry is completely contained by the specified constant geometry.
     */
    Within within(String propertyName, Geometry geometry);

    /**
     * Checks if the feature's geometry is completely contained by the specified constant geometry.
     */
    Within within(String propertyName, Geometry geometry, MatchAction matchAction);

    /////////////////////////////////////////////////////////////////////////////////
    //
    //  TEMPORAL FILTERS
    //
    ////////////////////////////////////////////////////////////////////////////////

    /** Checks if one expression is temporally after another */
    After after(Expression expr1, Expression expr2);

    /** Checks if one expression is temporally after another */
    After after(Expression expr1, Expression expr2, MatchAction matchAction);

    /** Checks if one expression temporally interacts in any way with another */
    AnyInteracts anyInteracts(Expression expr1, Expression expr2);

    /** Checks if one expression temporally interacts in any way with another */
    AnyInteracts anyInteracts(Expression expr1, Expression expr2, MatchAction matchAction);

    /** Checks if one expression is temporally before another */
    Before before(Expression expr1, Expression expr2);

    /** Checks if one expression is temporally before another */
    Before before(Expression expr1, Expression expr2, MatchAction matchAction);

    /** Checks if one expression temporally begins another */
    Begins begins(Expression expr1, Expression expr2);

    /** Checks if one expression temporally begins another */
    Begins begins(Expression expr1, Expression expr2, MatchAction matchAction);

    /** Checks if one expression is temporally begun by another */
    BegunBy begunBy(Expression expr1, Expression expr2);

    /** Checks if one expression is temporally begun by another */
    BegunBy begunBy(Expression expr1, Expression expr2, MatchAction matchAction);

    /** Checks if one expression is temporally during another */
    During during(Expression expr1, Expression expr2);

    /** Checks if one expression is temporally during another */
    During during(Expression expr1, Expression expr2, MatchAction matchAction);

    /** Checks if one expression is temporally ended by another */
    EndedBy endedBy(Expression expr1, Expression expr2);

    /** Checks if one expression is temporally ended by another */
    EndedBy endedBy(Expression expr1, Expression expr2, MatchAction matchAction);

    /** Checks if one expression temporally ends by another */
    Ends ends(Expression expr1, Expression expr2);

    /** Checks if one expression temporally ends by another */
    Ends ends(Expression expr1, Expression expr2, MatchAction matchAction);

    /** Checks if one expression temporally meets another */
    Meets meets(Expression expr1, Expression expr2);

    /** Checks if one expression temporally meets another */
    Meets meets(Expression expr1, Expression expr2, MatchAction matchAction);

    /** Checks if one expression is temporally met by another */
    MetBy metBy(Expression expr1, Expression expr2);

    /** Checks if one expression is temporally met by another */
    MetBy metBy(Expression expr1, Expression expr2, MatchAction matchAction);

    /** Checks if one expression is temporally overlapped by another */
    OverlappedBy overlappedBy(Expression expr1, Expression expr2);

    /** Checks if one expression is temporally overlapped by another */
    OverlappedBy overlappedBy(Expression expr1, Expression expr2, MatchAction matchAction);

    /** Checks if one expression temporally overlaps another */
    TOverlaps toverlaps(Expression expr1, Expression expr2);

    /** Checks if one expression temporally overlaps another */
    TOverlaps toverlaps(Expression expr1, Expression expr2, MatchAction matchAction);

    /** Checks if one expression temporally contains another */
    TContains tcontains(Expression expr1, Expression expr2);

    /** Checks if one expression temporally contains another */
    TContains tcontains(Expression expr1, Expression expr2, MatchAction matchAction);

    /** Checks if one expression temporally equals another */
    TEquals tequals(Expression expr1, Expression expr2);

    /** Checks if one expression temporally equals another */
    TEquals tequals(Expression expr1, Expression expr2, MatchAction matchAction);

    ////////////////////////////////////////////////////////////////////////////////
    //
    //  EXPRESSIONS
    //
    ////////////////////////////////////////////////////////////////////////////////

    /** Computes the numeric addition of the first and second operand. */
    Add add(Expression expr1, Expression expr2);

    /** Computes the numeric quotient resulting from dividing the first operand by the second. */
    Divide divide(Expression expr1, Expression expr2);

    /** Computes the numeric product of their first and second operand. */
    Multiply multiply(Expression expr1, Expression expr2);

    /** Computes the numeric difference between the first and second operand. */
    Subtract subtract(Expression expr1, Expression expr2);

    /** Call into some implementation-specific function. */
    Function function(String name, Expression... args);

    /** Call into some implementation-specific function. */
    Function function(Name name, Expression... args);

    /** A constant, literal value that can be used in expressions. */
    Literal literal(Object obj);

    /** A constant, literal {@link Byte} value that can be used in expressions. */
    Literal literal(byte b);

    /** A constant, literal {@link Short} value that can be used in expressions. */
    Literal literal(short s);

    /** A constant, literal {@link Integer} value that can be used in expressions. */
    Literal literal(int i);

    /** A constant, literal {@link Long} value that can be used in expressions. */
    Literal literal(long l);

    /** A constant, literal {@link Float} value that can be used in expressions. */
    Literal literal(float f);

    /** A constant, literal {@link Double} value that can be used in expressions. */
    Literal literal(double d);

    /** A constant, literal {@link Character} value that can be used in expressions. */
    Literal literal(char c);

    /** A constant, literal {@link Boolean} value that can be used in expressions. */
    Literal literal(boolean b);

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
    SpatialOperator spatialOperator(String name, GeometryOperand... geometryOperands);

    /** temporal operator */
    TemporalOperator temporalOperator(String name);

    /** function name */
    FunctionName functionName(String name, int nargs);

    /** function name */
    FunctionName functionName(Name name, int nargs);

    /** functions */
    Functions functions(FunctionName... functionNames);

    /** spatial operators */
    SpatialOperators spatialOperators(SpatialOperator... spatialOperators);

    /** comparison operators */
    ComparisonOperators comparisonOperators(Operator... comparisonOperators);

    /** arithmetic operators */
    ArithmeticOperators arithmeticOperators(boolean simple, Functions functions);

    /** scalar capabilities */
    ScalarCapabilities scalarCapabilities(
            ComparisonOperators comparison, ArithmeticOperators arithmetic, boolean logical);

    /** spatial capabilities */
    SpatialCapabilities spatialCapabilities(
            GeometryOperand[] geometryOperands, SpatialOperators spatial);

    /** id capabilities */
    IdCapabilities idCapabilities(boolean eid, boolean fid);

    /** temporal capabilities */
    TemporalCapabilities temporalCapabilities(TemporalOperator... temporalOperators);

    /** filter capabilities */
    FilterCapabilities capabilities(
            String version,
            ScalarCapabilities scalar,
            SpatialCapabilities spatial,
            IdCapabilities id);

    /** filter capabilities */
    FilterCapabilities capabilities(
            String version,
            ScalarCapabilities scalar,
            SpatialCapabilities spatial,
            IdCapabilities id,
            TemporalCapabilities temporal);

    ////////////////////////////////////////////////////////////////////////////////
    //
    //  CAPABILITIES
    //
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * Creates a parameter of a function.
     *
     * @param name Parameter name
     * @param type Parameter type/class
     * @param title Human readable title of the parameter
     * @param description Extended description of the parameter
     * @param required Flag indicating if the parameter is required or not
     * @param minOccurs The minimum number of occurrences of the parameter
     * @param maxOccurs The maximum number of occurrences of the parameter
     * @param defaultValue Default value for the parameter
     */
    <T> Parameter<T> parameter(
            String name,
            Class<T> type,
            InternationalString title,
            InternationalString description,
            boolean required,
            int minOccurs,
            int maxOccurs,
            T defaultValue);

    /**
     * FunctionName used to describe an available function.
     *
     * @param name name of function
     * @param nargs number of arguments, use a negative number to indicate a minimum if the function
     *     supports an open ended number of arguments
     * @param argNames Optional list of argument names
     */
    FunctionName functionName(String name, int nargs, List<String> argNames);

    /**
     * FunctionName used to describe an available function.
     *
     * @param name qualified name of function
     * @param nargs number of arguments, use a negative number to indicate a minimum if the function
     *     supports an open ended number of arguments
     * @param argNames Optional list of argument names
     */
    FunctionName functionName(Name name, int nargs, List<String> argNames);

    /**
     * FunctionName used to describe an available function.
     *
     * @param name name of function
     * @param args Parameters describing function arguments.
     * @param ret Parameter describing function return.
     */
    FunctionName functionName(String name, List<Parameter<?>> args, Parameter<?> ret);

    /**
     * FunctionName used to describe an available function.
     *
     * @param name qualified name of function
     * @param args Parameters describing function arguments.
     * @param ret Parameter describing function return.
     */
    FunctionName functionName(Name name, List<Parameter<?>> args, Parameter<?> ret);

    ////////////////////////////////////////////////////////////////////////////////
    //
    //  FILTERS
    //
    ////////////////////////////////////////////////////////////////////////////////

    Id id(FeatureId... fids);

    /**
     * Retrieves the value of a {@linkplain org.geotools.api.feature.Feature feature}'s property.
     *
     * @param name Name of attribute referenced
     * @return PropertyName
     */
    PropertyName property(Name name);

    /**
     * Retrieves the value of a {@linkplain org.geotools.api.feature.Feature feature}'s property.
     *
     * @param xpath XPath expression (subject to the restrictions of filter specificaiton)
     * @param namespaceContext Used to interpret any namespace prefixs in above xpath expression
     * @return PropertyName
     */
    PropertyName property(String xpath, NamespaceSupport namespaceContext);

    ////////////////////////////////////////////////////////////////////////////////
    //
    //  SPATIAL FILTERS
    //
    ////////////////////////////////////////////////////////////////////////////////

    /** Checks if the geometry expression overlaps the specified bounding box. */
    BBOX bbox(Expression geometry, double minx, double miny, double maxx, double maxy, String srs);

    /** Checks if the geometry expression overlaps the specified bounding box. */
    BBOX bbox(
            Expression geometry,
            double minx,
            double miny,
            double maxx,
            double maxy,
            String srs,
            MatchAction matchAction);

    /** Checks if the geometry expression overlaps the specified bounding box. */
    BBOX3D bbox(Expression geometry, BoundingBox3D env);

    /** Checks if the geometry expression overlaps the specified bounding box. */
    BBOX3D bbox(Expression geometry, BoundingBox3D env, MatchAction matchAction);

    /**
     * Checks if the bounding box of the feature's geometry overlaps the indicated bounds.
     *
     * <p>This method does not strictly confirm to the the Filter 1.0 specification, you may use it
     * to check expressions other than PropertyName.
     *
     * @param geometry Expression used to access a Geometry, in order to check for interaction with
     *     bounds
     * @param bounds Indicates the bounds to check geometry against
     */
    BBOX bbox(Expression geometry, BoundingBox bounds);

    /**
     * Checks if the bounding box of the feature's geometry overlaps the indicated bounds.
     *
     * <p>This method does not strictly confirm to the the Filter 1.0 specification, you may use it
     * to check expressions other than PropertyName.
     *
     * @param geometry Expression used to access a Geometry, in order to check for interaction with
     *     bounds
     * @param bounds Indicates the bounds to check geometry against
     * @param matchAction Match Action
     */
    BBOX bbox(Expression geometry, BoundingBox bounds, MatchAction matchAction);

    /**
     * Check if all of a geometry is more distant than the given distance from this object's
     * geometry.
     */
    Beyond beyond(Expression geometry1, Expression geometry2, double distance, String units);

    /**
     * Check if all of a geometry is more distant than the given distance from this object's
     * geometry.
     */
    Beyond beyond(
            Expression geometry1,
            Expression geometry2,
            double distance,
            String units,
            MatchAction matchAction);

    /** Checks if the the first geometric operand contains the second. */
    Contains contains(Expression geometry1, Expression geometry2);

    /** Checks if the the first geometric operand contains the second. */
    Contains contains(Expression geometry1, Expression geometry2, MatchAction matchAction);

    /** Checks if the first geometric operand crosses the second. */
    Crosses crosses(Expression geometry1, Expression geometry2);

    /** Checks if the first geometric operand crosses the second. */
    Crosses crosses(Expression geometry1, Expression geometry2, MatchAction matchAction);

    /** Checks if the first operand is disjoint from the second. */
    Disjoint disjoint(Expression geometry1, Expression geometry2);

    /** Checks if the first operand is disjoint from the second. */
    Disjoint disjoint(Expression geometry1, Expression geometry2, MatchAction matchAction);

    /**
     * Checks if any part of the first geometry lies within the given distance of the second
     * geometry.
     */
    DWithin dwithin(Expression geometry1, Expression geometry2, double distance, String units);

    /**
     * Checks if any part of the first geometry lies within the given distance of the second
     * geometry.
     */
    DWithin dwithin(
            Expression geometry1,
            Expression geometry2,
            double distance,
            String units,
            MatchAction matchAction);

    /**
     * Checks if the geometry of the two operands are equal.
     *
     * @todo should be equals, resolve conflict with PropertyIsEqualTo equals( Expression,
     *     Expression )
     */
    Equals equal(Expression geometry1, Expression geometry2);

    /**
     * Checks if the geometry of the two operands are equal.
     *
     * @todo should be equals, resolve conflict with PropertyIsEqualTo equals( Expression,
     *     Expression )
     */
    Equals equal(Expression geometry1, Expression geometry2, MatchAction matchAction);

    /** Checks if the two geometric operands intersect. */
    Intersects intersects(Expression geometry1, Expression geometry2);

    /** Checks if the two geometric operands intersect. */
    Intersects intersects(Expression geometry1, Expression geometry2, MatchAction matchAction);

    /**
     * Checks if the interior of the first geometry somewhere overlaps the interior of the second
     * geometry.
     */
    Overlaps overlaps(Expression geometry1, Expression geometry2);

    /**
     * Checks if the interior of the first geometry somewhere overlaps the interior of the second
     * geometry.
     */
    Overlaps overlaps(Expression geometry1, Expression geometry2, MatchAction matchAction);

    /**
     * Checks if the feature's geometry touches, but does not overlap with the geometry held by this
     * object.
     */
    Touches touches(Expression propertyName1, Expression geometry2);

    /**
     * Checks if the feature's geometry touches, but does not overlap with the geometry held by this
     * object.
     */
    Touches touches(Expression propertyName1, Expression geometry2, MatchAction matchAction);

    /**
     * Checks if the feature's geometry is completely contained by the specified constant geometry.
     */
    Within within(Expression geometry1, Expression geometry2);

    /**
     * Checks if the feature's geometry is completely contained by the specified constant geometry.
     */
    Within within(Expression geometry1, Expression geometry2, MatchAction matchAction);

    /**
     * Builds a new native filter, which will should be delegated to the data store.
     *
     * @param nativeFilter the native filter
     * @return the build native filter
     */
    default NativeFilter nativeFilter(String nativeFilter) {
        throw new UnsupportedOperationException(
                "Native filter building is not supported by this filter factory.");
    }
}
