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
 *
 *
 * Created on 24 October 2002, 16:16
 */
package org.geotools.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.geotools.factory.Hints;
import org.geotools.factory.Hints.Key;
import org.geotools.filter.capability.ArithmeticOperatorsImpl;
import org.geotools.filter.capability.ComparisonOperatorsImpl;
import org.geotools.filter.capability.FilterCapabilitiesImpl;
import org.geotools.filter.capability.FunctionNameImpl;
import org.geotools.filter.capability.FunctionsImpl;
import org.geotools.filter.capability.IdCapabilitiesImpl;
import org.geotools.filter.capability.OperatorImpl;
import org.geotools.filter.capability.ScalarCapabilitiesImpl;
import org.geotools.filter.capability.SpatialCapabiltiesImpl;
import org.geotools.filter.capability.SpatialOperatorImpl;
import org.geotools.filter.capability.SpatialOperatorsImpl;
import org.geotools.filter.expression.AddImpl;
import org.geotools.filter.expression.DivideImpl;
import org.geotools.filter.expression.MultiplyImpl;
import org.geotools.filter.expression.PropertyAccessorFactory;
import org.geotools.filter.expression.SubtractImpl;
import org.geotools.filter.identity.FeatureIdImpl;
import org.geotools.filter.identity.GmlObjectIdImpl;
import org.geotools.filter.spatial.BBOXImpl;
import org.geotools.filter.spatial.BeyondImpl;
import org.geotools.filter.spatial.ContainsImpl;
import org.geotools.filter.spatial.CrossesImpl;
import org.geotools.filter.spatial.DWithinImpl;
import org.geotools.filter.spatial.DisjointImpl;
import org.geotools.filter.spatial.EqualsImpl;
import org.geotools.filter.spatial.IntersectsImpl;
import org.geotools.filter.spatial.OverlapsImpl;
import org.geotools.filter.spatial.TouchesImpl;
import org.geotools.filter.spatial.WithinImpl;
import org.geotools.referencing.CRS;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.Name;
import org.opengis.filter.And;
import org.opengis.filter.Filter;
import org.opengis.filter.Id;
import org.opengis.filter.Not;
import org.opengis.filter.Or;
import org.opengis.filter.PropertyIsBetween;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.PropertyIsGreaterThan;
import org.opengis.filter.PropertyIsGreaterThanOrEqualTo;
import org.opengis.filter.PropertyIsLessThan;
import org.opengis.filter.PropertyIsLessThanOrEqualTo;
import org.opengis.filter.PropertyIsLike;
import org.opengis.filter.PropertyIsNotEqualTo;
import org.opengis.filter.PropertyIsNull;
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
import org.opengis.geometry.BoundingBox;
import org.opengis.geometry.Geometry;
import org.xml.sax.helpers.NamespaceSupport;

import com.vividsolutions.jts.geom.Envelope;


/**
 * Implementation of the FilterFactory, generates the filter implementations in
 * defaultcore.
 *
 * @author Ian Turton, CCG
 * @source $URL$
 * @version $Id$
 */
public class FilterFactoryImpl implements FilterFactory {
        
    private FunctionFinder functionFinder;

    /**
     * Creates a new instance of FilterFactoryImpl
     */
    public FilterFactoryImpl() {
        this( null );
    }
    public FilterFactoryImpl( Hints hints ){
        functionFinder = new FunctionFinder( null );
    }

    public FeatureId featureId(String id) {
        return new FeatureIdImpl( id );
    } 
    
    public GmlObjectId gmlObjectId(String id) {
        return new GmlObjectIdImpl( id );
    }
    
    public And and(Filter f, Filter g ) {
        List/*<Filter>*/ list = new ArrayList/*<Filter>*/( 2 );
        list.add( f );
        list.add( g );
        return new AndImpl( this, list );
    }
    
    public And and(List/*<Filter>*/ filters) {
        return new AndImpl( this, filters );
    }
    
    public Or or(Filter f, Filter g) {
        List/*<Filter>*/ list = new ArrayList/*<Filter>*/( 2 );
        list.add( f );
        list.add( g );
        return new OrImpl( this, list );
    }    

    public Or or(List/*<Filter>*/ filters) {
        return new OrImpl( this, filters );
    }
    
    /** Java 5 type narrowing used to advertise explicit implementation for chaining */
    public Not /*NotImpl*/ not(Filter filter) {
        return new NotImpl( this, filter );
    }
    
    public Id id( Set id ){
        return new FidFilterImpl( id );
    }
    
    public PropertyName property(String name) {
        return new AttributeExpressionImpl(name);
    }

    public PropertyIsBetween between(Expression expr, Expression lower,
            Expression upper) {
        return new IsBetweenImpl(this,lower,expr,upper);
    }

    public PropertyIsEqualTo equals(Expression expr1, Expression expr2) {
        return equal( expr1,expr2,true);
    }
    
    public PropertyIsEqualTo equal(Expression expr1, Expression expr2, boolean matchCase) {
        return new IsEqualsToImpl(this,expr1,expr2,matchCase); 
    }
    
    public PropertyIsNotEqualTo notEqual(Expression expr1, Expression expr2) {
            return notEqual(expr1, expr2, false );
    }
    
    public PropertyIsNotEqualTo notEqual(Expression expr1, Expression expr2, boolean matchCase) {
        return new IsNotEqualToImpl(this,expr1,expr2,matchCase);
    }
    
    public PropertyIsGreaterThan greater(Expression expr1, Expression expr2) {
        return greater(expr1,expr2,false);
    }
    
    public PropertyIsGreaterThan greater(Expression expr1, Expression expr2, boolean matchCase) {
        return new IsGreaterThanImpl(this, expr1, expr2);
    }
    
    public PropertyIsGreaterThanOrEqualTo greaterOrEqual(Expression expr1,
            Expression expr2) {
        return greaterOrEqual(expr1,expr2,false);
    }
    
    public PropertyIsGreaterThanOrEqualTo greaterOrEqual(Expression expr1,
                        Expression expr2, boolean matchCase) {
        return new IsGreaterThanOrEqualToImpl(this,expr1,expr2,matchCase);
    }

    public PropertyIsLessThan less(Expression expr1, Expression expr2) {
        return less(expr1,expr2,false);
    }
    
    public PropertyIsLessThan less(Expression expr1, Expression expr2,
                    boolean matchCase) {
        return new IsLessThenImpl(this,expr1,expr2,matchCase);
    }

    public PropertyIsLessThanOrEqualTo lessOrEqual(Expression expr1,
            Expression expr2) {
        return lessOrEqual(expr1,expr2,false);
    }
    
    public PropertyIsLessThanOrEqualTo lessOrEqual(Expression expr1,
                        Expression expr2, boolean matchCase) {
        return new IsLessThenOrEqualToImpl(this,expr1,expr2,false);
    }
    
    public PropertyIsLike like(Expression expr, String pattern) {
        return like(expr,pattern,"*","?","\\");
    }

    public PropertyIsLike like(Expression expr, String pattern,
            String wildcard, String singleChar, String escape) {
        return like( expr, pattern, wildcard, singleChar, escape, false );
    }
    public PropertyIsLike like(Expression expr, String pattern,
                        String wildcard, String singleChar, String escape, boolean matchCase) {
            LikeFilterImpl filter = new LikeFilterImpl();
        filter.setExpression(expr);
        filter.setPattern(pattern,wildcard,singleChar,escape);
        filter.setMatchingCase( matchCase );
        
        return filter;
    }

    /**
     * XXX Java 5 type narrowing used to make generated class explicit for chaining
     */
    public PropertyIsNull /*IsNullImpl*/ isNull(Expression expr) {
        return new IsNullImpl( this, expr );
    }

    /**
     * Checks if the bounding box of the feature's geometry overlaps the
     * specified bounding box.
     * <p>
     * Similar to:
     * <code>
     * geom().disjoint( geom( bbox )).not()
     * </code>
     * </p>
     */
    public BBOX bbox(String propertyName, double minx, double miny,
            double maxx, double maxy, String srs) {
        
        PropertyName name = property(propertyName);
        return bbox(name, minx, miny, maxx, maxy, srs);
    }

    public BBOX bbox( Expression geometry, Expression bounds ) {
        return new BBOXImpl(this, geometry, bounds );
    }
    
    public BBOX bbox( Expression geometry, BoundingBox bounds ) {
        return bbox( geometry, bounds.getMinX(), bounds.getMinY(), bounds.getMaxX(), bounds.getMaxY(),
                CRS.toSRS( bounds.getCoordinateReferenceSystem() ) );
    }
    
    public BBOX bbox(Expression e, double minx, double miny, double maxx, double maxy, String srs) {
        PropertyName name = null;
        if ( e instanceof PropertyName ) {
            name = (PropertyName) e;
        }
        else {
            throw new IllegalArgumentException();
        }
        
        BBoxExpression bbox = null;
        try {
            bbox = createBBoxExpression(new Envelope(minx,maxx,miny,maxy));
        } 
        catch (IllegalFilterException ife) {
            new IllegalArgumentException().initCause(ife);
        }
        
        BBOXImpl box = new BBOXImpl(this,e,bbox);
        if (e == null) {
            // otherwise this line is redundant. Also complex
            // features with bbox request would fail as this would remove the
            // namespace from the property name
            box.setPropertyName(name.getPropertyName());
        }
        box.setSRS(srs);
        box.setMinX(minx);
        box.setMinY(miny);
        box.setMaxX(maxx);
        box.setMaxY(maxy);
        
        return box;
    }
    
    public Beyond beyond(String propertyName, Geometry geometry,
            double distance, String units) {
        
        PropertyName name = property(propertyName);
        Literal geom = literal(geometry);
        
        return beyond( name, geom, distance, units );
    }

    public Beyond beyond( 
        Expression geometry1, Expression geometry2, double distance, String units
    ) {
        
        BeyondImpl beyond = new BeyondImpl(this,geometry1,geometry2);
        beyond.setDistance(distance);
        beyond.setUnits(units);
        
        return beyond;
    }
    
    public Contains contains(String propertyName, Geometry geometry) {
        PropertyName name = property(propertyName);
        Literal geom = literal(geometry);
        
        return contains( name, geom );
    }
    
    public Contains contains(Expression geometry1, Expression geometry2) {
        return new ContainsImpl( this, geometry1, geometry2 );
    }

    public Crosses crosses(String propertyName, Geometry geometry) {
        PropertyName name = property(propertyName);
        Literal geom = literal(geometry);
        
        return crosses( name, geom );
    }
    
    public Crosses crosses(Expression geometry1, Expression geometry2) {
        return new CrossesImpl( this, geometry1, geometry2 );
    }
    

    public Disjoint disjoint(String propertyName, Geometry geometry) {
        PropertyName name = property(propertyName);
        Literal geom = literal(geometry);
        
        return disjoint( name, geom );
    }

    
    public Disjoint disjoint(Expression geometry1, Expression geometry2) {
        return new DisjointImpl( this, geometry1, geometry2 );
    }
    
    public DWithin dwithin(String propertyName, Geometry geometry,
            double distance, String units) {
        PropertyName name = property(propertyName);
        Literal geom = literal(geometry);
        
        return dwithin( name, geom, distance, units );
    }

    public DWithin dwithin(Expression geometry1, Expression geometry2, double distance, String units) {
        DWithinImpl dwithin =  new DWithinImpl( this, geometry1, geometry2 );
        dwithin.setDistance( distance );
        dwithin.setUnits( units );
        
        return dwithin;
    }
    
    public Equals equals(String propertyName, Geometry geometry) {
        PropertyName name = property(propertyName);
        Literal geom = literal(geometry);
        
        return equal( name, geom );
    }
    
    public Equals equal(Expression geometry1, Expression geometry2) {
        return new EqualsImpl( this, geometry1, geometry2 );
    }

    public Intersects intersects(String propertyName, Geometry geometry) {
        PropertyName name = property(propertyName);
        Literal geom = literal(geometry);
        
        return intersects( name, geom );
    }

    public Intersects intersects(Expression geometry1, Expression geometry2) {
        return new IntersectsImpl( this, geometry1, geometry2 );
    }
    
    public Overlaps overlaps(String propertyName, Geometry geometry) {
        PropertyName name = property(propertyName);
        Literal geom = literal(geometry);
        
        return overlaps( name, geom );
    }

    public Overlaps overlaps(Expression geometry1, Expression geometry2) {
        return new OverlapsImpl( this, geometry1, geometry2 );
    }
    
    public Touches touches(String propertyName, Geometry geometry) {
        PropertyName name = property(propertyName);
        Literal geom = literal(geometry);
        
        return touches( name, geom );
    }

    public Touches touches(Expression geometry1, Expression geometry2) {
        return new TouchesImpl(this,geometry1,geometry2);
    }
    
    public Within within(String propertyName, Geometry geometry) {
        PropertyName name = property(propertyName);
        Literal geom = literal(geometry);
        
        return within( name, geom );
    }

    public Within within(Expression geometry1, Expression geometry2) {
        return new WithinImpl( this, geometry1, geometry2 );
    }
    
    public Add add(Expression expr1, Expression expr2) {
        return new AddImpl(expr1,expr2);
    }

    public Divide divide(Expression expr1, Expression expr2) {
        return new DivideImpl(expr1,expr2);
    }

    public Multiply multiply(Expression expr1, Expression expr2) {
        return new MultiplyImpl(expr1,expr2);
    }

    public Subtract subtract(Expression expr1, Expression expr2) {
        return new SubtractImpl(expr1,expr2);
    }

    public Function function(String name, Expression[] args) {
        Function function = functionFinder.findFunction( name, Arrays.asList(args) );
        return function;
    }

    public Function function(String name, Expression arg1) {
        Function function = functionFinder.findFunction( name, Arrays.asList( new Expression[]{ arg1 } ) );
        return function;
    }

    public Function function(String name, Expression arg1, Expression arg2) {
        Function function = 
                functionFinder.findFunction( name, Arrays.asList( new Expression[]{ arg1, arg2 }) );
        return function;
    }

    /** @deprecated Pending see org.opengis.filter.Factory2 */
    public Function function(String name, List<org.opengis.filter.expression.Expression> parameters, Literal fallback ){
        Function function = 
                functionFinder.findFunction( name, parameters, fallback );
        
        return function;        
    }
    public Function function(String name, Expression arg1, Expression arg2,
            Expression arg3) {
        Function function = 
                functionFinder.findFunction( name, Arrays.asList( new Expression[]{ arg1, arg2, arg3 }) );
        
        return function;        
    }
    
    public Literal literal(Object obj) {
        try {
            return new LiteralExpressionImpl(obj);
        } 
        catch (IllegalFilterException e) {
            new IllegalArgumentException().initCause(e);
        }
        
        return null;
    }

    public Literal literal(byte b) {
        return new LiteralExpressionImpl(b);
    }

    public Literal literal(short s) {
        return new LiteralExpressionImpl(s);
    }

    public Literal literal(int i) {
        return new LiteralExpressionImpl(i);
    }

    public Literal literal(long l) {
        return new LiteralExpressionImpl(l);
    }

    public Literal literal(float f) {
        return new LiteralExpressionImpl(f);
    }

    public Literal literal(double d) {
        return new LiteralExpressionImpl(d);
    }

    public Literal literal(char c) {
        return new LiteralExpressionImpl(c);
    }

    public Literal literal(boolean b) {
        return b ? new LiteralExpressionImpl( Boolean.TRUE ) : new LiteralExpressionImpl( Boolean.FALSE );
    }

    
    /**
     * Creates an AttributeExpression using the supplied xpath.
     * <p>
     * The supplied xpath can be used to query a varity of
     * content - most notably Features.
     * </p>
     * @return The new Attribtue Expression
     */
    public AttributeExpression createAttributeExpression( String xpath){
        return new AttributeExpressionImpl( xpath );
    }
    /**
     * Creates a Attribute Expression with an initial schema.
     *
     * @param schema the schema to create with.
     *
     * @return The new Attribute Expression.
     */
    public AttributeExpression createAttributeExpression(SimpleFeatureType schema) {
        return new AttributeExpressionImpl(schema);
    }

    /**
     * Creates a Attribute Expression given a schema and attribute path.
     *
     * @param schema the schema to get the attribute from.
     * @param path the xPath of the attribute to compare.
     *
     * @return The new Attribute Expression.
     *
     * @throws IllegalFilterException if there were creation problems.
     */
    public AttributeExpression createAttributeExpression(SimpleFeatureType schema,
            String path) throws IllegalFilterException {
            return new AttributeExpressionImpl(schema, path);
        }
    public AttributeExpression createAttributeExpression(AttributeDescriptor at) throws IllegalFilterException {
            return new AttributeExpressionImpl2(at);
        }

    /**
     * Creates a BBox Expression from an envelope.
     *
     * @param env the envelope to use for this bounding box.
     *
     * @return The newly created BBoxExpression.
     *
     * @throws IllegalFilterException if there were creation problems.
     */
    public BBoxExpression createBBoxExpression(Envelope env)
        throws IllegalFilterException {
        return new BBoxExpressionImpl(env);
    }

    /**
     * Creates an empty Between Filter.
     *
     * @return The new Between Filter.
     *
     * @throws IllegalFilterException if there were creation problems.
     */
    public BetweenFilter createBetweenFilter() throws IllegalFilterException {
        return new BetweenFilterImpl();
    }

    /**
     * Creates a new compare filter of the given type.
     *
     * @param type the type of comparison - must be a compare type.
     *
     * @return The new compare filter.
     *
     * @throws IllegalFilterException if there were creation problems.
     * 
     * @deprecated @see org.geotools.filter.FilterFactory#createCompareFilter(short)
     */
    public CompareFilter createCompareFilter(short type)
        throws IllegalFilterException {
        
        switch(type) {
                case FilterType.COMPARE_EQUALS:
                        return new IsEqualsToImpl(this);
                        
                case FilterType.COMPARE_NOT_EQUALS:
                        return new IsNotEqualToImpl(this);
                        
                case FilterType.COMPARE_GREATER_THAN:
                        return new IsGreaterThanImpl(this);
                        
                case FilterType.COMPARE_GREATER_THAN_EQUAL:
                        return new IsGreaterThanOrEqualToImpl(this);
                        
                case FilterType.COMPARE_LESS_THAN:
                        return new IsLessThenImpl(this);
                        
                case FilterType.COMPARE_LESS_THAN_EQUAL:
                        return new IsLessThenOrEqualToImpl(this);
                        
                case FilterType.BETWEEN:
                        return new BetweenFilterImpl(this);
        }
        
        throw new IllegalFilterException("Must be one of <,<=,==,>,>=,<>");
    }

    /**
     * Creates a new Fid Filter with no initial fids.
     *
     * @return The new Fid Filter.
     */
    public FidFilter createFidFilter() {
        return new FidFilterImpl();
    }

    /**
     * Creates a Fid Filter with an initial fid.
     *
     * @param fid the feature ID to create with.
     *
     * @return The new FidFilter.
     */
    public FidFilter createFidFilter(String fid) {
        return new FidFilterImpl(fid);
    }

    /**
     * Creates a Geometry Filter.
     *
     * @param filterType the type to create, must be a geometry type.
     *
     * @return The new Geometry Filter.
     *
     * @throws IllegalFilterException if the filterType is not a geometry.
     */
    public GeometryFilter createGeometryFilter(short filterType)
        throws IllegalFilterException {
        switch(filterType) {
                case FilterType.GEOMETRY_EQUALS:
                        return new EqualsImpl(this,null,null);
                        
                case FilterType.GEOMETRY_DISJOINT:
                        return new DisjointImpl(this,null,null);
                        
                case FilterType.GEOMETRY_DWITHIN:
                        return new DWithinImpl(this,null,null);
                        
                case FilterType.GEOMETRY_INTERSECTS:
                        return new IntersectsImpl(this,null,null);
                        
                case FilterType.GEOMETRY_CROSSES:
                        return new CrossesImpl(this,null,null);
                        
                case FilterType.GEOMETRY_WITHIN:
                        return new WithinImpl(this,null,null);
                        
                case FilterType.GEOMETRY_CONTAINS:
                        return new ContainsImpl(this,null,null);
                        
                case FilterType.GEOMETRY_OVERLAPS:
                        return new OverlapsImpl(this,null,null);
                        
                case FilterType.GEOMETRY_BEYOND:
                        return new BeyondImpl(this,null,null);
                        
                case FilterType.GEOMETRY_BBOX:
                        return new BBOXImpl(this,null,null);
                        
                case FilterType.GEOMETRY_TOUCHES:
                        return new TouchesImpl(this,null,null);
        }
       
        throw new IllegalFilterException("Not one of the accepted spatial filter types.");
    }

    /**
     * Creates a Geometry Distance Filter
     *
     * @param filterType the type to create, must be beyond or dwithin.
     *
     * @return The new  Expression
     *
     * @throws IllegalFilterException if the filterType is not a geometry
     *         distance type.
     */
    public GeometryDistanceFilter createGeometryDistanceFilter(short filterType)
        throws IllegalFilterException {
        
        switch(filterType) {
                        case FilterType.GEOMETRY_BEYOND:
                                return new BeyondImpl(this,null,null);
                                
                        case FilterType.GEOMETRY_DWITHIN:
                                return new DWithinImpl(this,null,null);
                        
        }
   
        throw new IllegalFilterException("Not one of the accepted spatial filter types.");
        
    }

    /**
     * Creates a Like Filter.
     *
     * @return The new Like Filter.
     */
    public LikeFilter createLikeFilter() {
        return new LikeFilterImpl();
    }

    /**
     * Creates an empty Literal Expression
     *
     * @return The new Literal Expression.
     */
    public LiteralExpression createLiteralExpression() {
        return new LiteralExpressionImpl();
    }

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
        throws IllegalFilterException {
        return new LiteralExpressionImpl(o);
    }

    /**
     * Creates an Integer Literal Expression.
     *
     * @param i the int to serve as literal.
     *
     * @return The new Literal Expression
     */
    public LiteralExpression createLiteralExpression(int i) {
        return new LiteralExpressionImpl(i);
    }

    /**
     * Creates a Double Literal Expression
     *
     * @param d the double to serve as the literal.
     *
     * @return The new Literal Expression
     */
    public LiteralExpression createLiteralExpression(double d) {
        return new LiteralExpressionImpl(d);
    }

    /**
     * Creates a String Literal Expression
     *
     * @param s the string to serve as the literal.
     *
     * @return The new Literal Expression
     */
    public LiteralExpression createLiteralExpression(String s) {
        return new LiteralExpressionImpl(s);
    }

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
     *  {@link org.opengis.filter.FilterFactory#or(Filter, Filter)}
     *  {@link org.opengis.filter.FilterFactory#not(Filter)}
     */
    public LogicFilter createLogicFilter(short filterType)
        throws IllegalFilterException {
        
        List children = new ArrayList();
        switch (filterType) {
                case FilterType.LOGIC_AND:
                        return new AndImpl(this,children);
                case FilterType.LOGIC_OR:
                        return new OrImpl(this,children);
                case FilterType.LOGIC_NOT:
                        return new NotImpl(this);
        }       
        throw new IllegalFilterException("Must be one of AND,OR,NOT.");
    }

    /**
     * Creates a logic filter with an initial filter.
     *
     * @param filter the initial filter to set.
     * @param filterType Must be a logic type.
     *
     * @return the newly constructed logic filter.
     *
     * @throws IllegalFilterException If there were any problems creating the
     *         filter, including wrong type.
     *         
     * @deprecated use one of {@link org.opengis.filter.FilterFactory#and(Filter, Filter)}
     *  {@link org.opengis.filter.FilterFactory#or(Filter, Filter)}
     *  {@link org.opengis.filter.FilterFactory#not(Filter)}
     */
    public LogicFilter createLogicFilter(org.geotools.filter.Filter filter, short filterType)
        throws IllegalFilterException {
        
        List children = new ArrayList();
        children.add(filter);
        
        switch (filterType) {
                case FilterType.LOGIC_AND:
                        return new AndImpl(this,children);
                case FilterType.LOGIC_OR:
                        return new OrImpl(this,children);
                case FilterType.LOGIC_NOT:
                        return new NotImpl(this,filter);
        }
        
        throw new IllegalFilterException("Must be one of AND,OR,NOT.");
    }

    
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
     *         
     * @deprecated use one of {@link org.opengis.filter.FilterFactory#and(Filter, Filter)}
     *  {@link org.opengis.filter.FilterFactory#or(Filter, Filter)}
     *  {@link org.opengis.filter.FilterFactory#not(Filter)}
     */
    public LogicFilter createLogicFilter(org.geotools.filter.Filter  filter1, org.geotools.filter.Filter filter2,
        short filterType) throws IllegalFilterException {
        
        List children = new ArrayList();
        children.add(filter1);
        children.add(filter2);
        
        switch (filterType) {
                case FilterType.LOGIC_AND:
                        return new AndImpl(this,children);
                case FilterType.LOGIC_OR:
                        return new OrImpl(this,children);
                case FilterType.LOGIC_NOT:
                        //TODO: perhaps throw an exception here?
                        return new NotImpl(this,filter1);
        }
        
        throw new IllegalFilterException("Must be one of AND,OR,NOT.");
    }
    
    

    /**
     * Creates a Math Expression
     *
     * @return The new Math Expression
     * 
     * @deprecated use one of
     *  {@link org.opengis.filter.FilterFactory#add(Expression, Expression)}
     *  {@link org.opengis.filter.FilterFactory#subtract(Expression, Expression)}
     *  {@link org.opengis.filter.FilterFactory#multiply(Expression, Expression)}
     *  {@link org.opengis.filter.FilterFactory#divide(Expression, Expression)}
     * 
     */
    public MathExpression createMathExpression() {
        throw new UnsupportedOperationException();
    }

    /**
     * Creates a Math Expression of the given type.
     *
     * @param expressionType must be a math expression type.
     *
     * @return The new Math Expression.
     *
     * @throws IllegalFilterException if there were creation problems.
     */
    public MathExpression createMathExpression(short expressionType)
        throws IllegalFilterException {
        
        switch(expressionType) {
                case ExpressionType.MATH_ADD:
                        return new AddImpl(null,null);
                case ExpressionType.MATH_SUBTRACT:
                        return new SubtractImpl(null,null);
                case ExpressionType.MATH_MULTIPLY:
                        return new MultiplyImpl(null,null);
                case ExpressionType.MATH_DIVIDE:
                        return new DivideImpl(null,null);
        }       
        
        throw new IllegalFilterException("Unsupported math expression");
    }

    /**
     * Creates a Function Expression.
     *
     * @param name the function name.
     *
     * @return The new Function Expression.
     */
    public FunctionExpression createFunctionExpression(String name) {
        return (FunctionExpression) functionFinder.findFunction( name );
    }

    /**
     * Creates an empty Null Filter.
     *
     * @return The new Null Filter.
     */
    public NullFilter createNullFilter() {
        return new NullFilterImpl();
    }
    
    public EnvironmentVariable createEnvironmentVariable(String name){
        if(name.equalsIgnoreCase("MapScaleDenominator")){
            return new MapScaleDenominatorImpl();
        }
         throw new RuntimeException("Unknown environment variable:" + name);
    }

        public Map getImplementationHints() {
                return Collections.EMPTY_MAP;
        }
        
        public SortBy sort(String propertyName, SortOrder order) {
                return new SortByImpl( property( propertyName ), order );
        }

    public org.geotools.filter.Filter and( org.geotools.filter.Filter filter1, org.geotools.filter.Filter filter2 ) {
        return (org.geotools.filter.Filter) and( (Filter) filter1, (Filter) filter2 );         
    } 

    public org.geotools.filter.Filter not( org.geotools.filter.Filter filter ) {
        return (org.geotools.filter.Filter) not( (Filter) filter );
    }

    public org.geotools.filter.Filter or( org.geotools.filter.Filter filter1, org.geotools.filter.Filter filter2 ) {
        return (org.geotools.filter.Filter) or( (Filter) filter1, (Filter) filter2 );
    }
    
    public Beyond beyond( Expression geometry1, Geometry geometry2, double distance, String units ) {
        return beyond( geometry1, literal( geometry2), distance, units );        
    }
    public PropertyName property( Name name ) {
        return new AttributeExpressionImpl( name );
    }
    public PropertyName property( String name, NamespaceSupport namespaceContext ) {
        if (namespaceContext == null) {
            return property(name);
        }
        return new AttributeExpressionImpl(name, namespaceContext );
    }
    public Within within( Expression geometry1, Geometry geometry2 ) {
        return within( geometry1, literal( geometry2 ));
    }
    
    public Operator operator(String name) {
        return new OperatorImpl( name );
    }
    
    public SpatialOperator spatialOperator(
            String name, GeometryOperand[] geometryOperands) {
        return new SpatialOperatorImpl( name, geometryOperands );
    }
    
    public FunctionName functionName(String name, int nargs) {
        return new FunctionNameImpl( name, nargs );
    }
    
    public Functions functions(FunctionName[] functionNames) {
        return new FunctionsImpl( functionNames );
    }
    
    public SpatialOperators spatialOperators(SpatialOperator[] spatialOperators) {
        return new SpatialOperatorsImpl( spatialOperators );
    }
    
    public ArithmeticOperators arithmeticOperators(boolean simple, Functions functions ) {
        return new ArithmeticOperatorsImpl( simple, functions );
    }
    
    public ComparisonOperators comparisonOperators(Operator[] comparisonOperators) {
        return new ComparisonOperatorsImpl( comparisonOperators );
    }
    
    public FilterCapabilities capabilities(String version,
            ScalarCapabilities scalar, SpatialCapabilities spatial,
            IdCapabilities id) {
        return new FilterCapabilitiesImpl( version, scalar, spatial, id );
    }
    
    public ScalarCapabilities scalarCapabilities(
            ComparisonOperators comparison, ArithmeticOperators arithmetic,
            boolean logicalOperators) {
        return new ScalarCapabilitiesImpl( comparison, arithmetic, logicalOperators );
    }
    
    public SpatialCapabilities spatialCapabilities(
            GeometryOperand[] geometryOperands,
            SpatialOperators spatial) {
        return new SpatialCapabiltiesImpl( geometryOperands, spatial );
    }
    
    public IdCapabilities idCapabilities(boolean eid, boolean fid) {
        return new IdCapabilitiesImpl( eid, fid );
    }
        
}