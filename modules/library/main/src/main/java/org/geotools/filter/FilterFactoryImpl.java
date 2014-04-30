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
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.geotools.factory.Factory;
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
import org.geotools.filter.capability.TemporalCapabilitiesImpl;
import org.geotools.filter.capability.TemporalOperatorImpl;
import org.geotools.filter.expression.AddImpl;
import org.geotools.filter.expression.DivideImpl;
import org.geotools.filter.expression.MultiplyImpl;
import org.geotools.filter.expression.SubtractImpl;
import org.geotools.filter.identity.FeatureIdImpl;
import org.geotools.filter.identity.FeatureIdVersionedImpl;
import org.geotools.filter.identity.GmlObjectIdImpl;
import org.geotools.filter.identity.ResourceIdImpl;
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
import org.geotools.filter.temporal.AfterImpl;
import org.geotools.filter.temporal.AnyInteractsImpl;
import org.geotools.filter.temporal.BeforeImpl;
import org.geotools.filter.temporal.BeginsImpl;
import org.geotools.filter.temporal.BegunByImpl;
import org.geotools.filter.temporal.DuringImpl;
import org.geotools.filter.temporal.EndedByImpl;
import org.geotools.filter.temporal.EndsImpl;
import org.geotools.filter.temporal.MeetsImpl;
import org.geotools.filter.temporal.MetByImpl;
import org.geotools.filter.temporal.OverlappedByImpl;
import org.geotools.filter.temporal.TContainsImpl;
import org.geotools.filter.temporal.TEqualsImpl;
import org.geotools.filter.temporal.TOverlapsImpl;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope3D;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.Name;
import org.opengis.filter.And;
import org.opengis.filter.Filter;
import org.opengis.filter.Id;
import org.opengis.filter.MultiValuedFilter.MatchAction;
import org.opengis.filter.Not;
import org.opengis.filter.Or;
import org.opengis.filter.PropertyIsBetween;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.PropertyIsGreaterThan;
import org.opengis.filter.PropertyIsGreaterThanOrEqualTo;
import org.opengis.filter.PropertyIsLessThan;
import org.opengis.filter.PropertyIsLessThanOrEqualTo;
import org.opengis.filter.PropertyIsLike;
import org.opengis.filter.PropertyIsNil;
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
import org.opengis.filter.capability.TemporalCapabilities;
import org.opengis.filter.capability.TemporalOperator;
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
import org.opengis.filter.identity.ResourceId;
import org.opengis.filter.identity.Version;
import org.opengis.filter.sort.SortBy;
import org.opengis.filter.sort.SortOrder;
import org.opengis.filter.spatial.BBOX;
import org.opengis.filter.spatial.BBOX3D;
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
import org.opengis.filter.temporal.After;
import org.opengis.filter.temporal.AnyInteracts;
import org.opengis.filter.temporal.Before;
import org.opengis.filter.temporal.Begins;
import org.opengis.filter.temporal.BegunBy;
import org.opengis.filter.temporal.During;
import org.opengis.filter.temporal.EndedBy;
import org.opengis.filter.temporal.Ends;
import org.opengis.filter.temporal.Meets;
import org.opengis.filter.temporal.MetBy;
import org.opengis.filter.temporal.OverlappedBy;
import org.opengis.filter.temporal.TContains;
import org.opengis.filter.temporal.TEquals;
import org.opengis.filter.temporal.TOverlaps;
import org.opengis.geometry.BoundingBox;
import org.opengis.geometry.BoundingBox3D;
import org.opengis.geometry.Geometry;
import org.opengis.parameter.Parameter;
import org.opengis.util.InternationalString;
import org.xml.sax.helpers.NamespaceSupport;
import org.geotools.filter.spatial.BBOX3DImpl;

import com.vividsolutions.jts.geom.Envelope;


/**
 * Implementation of the FilterFactory, generates the filter implementations in
 * defaultcore.
 *
 * @author Ian Turton, CCG
 *
 *
 * @source $URL$
 * @version $Id$
 */
public class FilterFactoryImpl implements Factory, org.opengis.filter.FilterFactory2 {
        
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
    
    /** Creates a new feature id with version information */
    public FeatureId featureId(String fid, String featureVersion){
        return new FeatureIdVersionedImpl(fid, featureVersion);
    }

    /** ResouceId for identifier based query */
    public ResourceId resourceId(String fid, String featureVersion, Version version ){
        return new ResourceIdImpl(fid, featureVersion, version);
    }
    
    /** ResourceId for time based query */
    public ResourceId resourceId(String fid, Date startTime, Date endTime){
        return new ResourceIdImpl(fid, startTime, endTime );
    }
    
    public And and(Filter f, Filter g ) {
        List/*<Filter>*/ list = new ArrayList/*<Filter>*/( 2 );
        list.add( f );
        list.add( g );
        return new AndImpl(list );
    }
    
    public And and(List/*<Filter>*/ filters) {
        return new AndImpl(filters );
    }
    
    public Or or(Filter f, Filter g) {
        List/*<Filter>*/ list = new ArrayList/*<Filter>*/( 2 );
        list.add( f );
        list.add( g );
        return new OrImpl(list );
    }    

    public Or or(List/*<Filter>*/ filters) {
        return new OrImpl(filters );
    }
    
    /** Java 5 type narrowing used to advertise explicit implementation for chaining */
    public Not /*NotImpl*/ not(Filter filter) {
        return new NotImpl(filter );
    }
    
    public Id id( Set id ){
        return new FidFilterImpl( id );
    }
    
    public Id id(FeatureId... fids) {
        Set<FeatureId> selection = new HashSet<FeatureId>();
        for( FeatureId featureId : fids ){
            if( featureId == null ) continue;
            selection.add( featureId );
        }
        return id( selection );
    }
    
    public PropertyName property(String name) {
        return new AttributeExpressionImpl(name);
    }

    public PropertyIsBetween between(Expression expr, Expression lower,
            Expression upper) {
        return new IsBetweenImpl(lower,expr,upper);
    }
    
    public PropertyIsBetween between(Expression expr, Expression lower, Expression upper,
            MatchAction matchAction) {
        return new IsBetweenImpl(lower,expr,upper, matchAction);
    }    

    public PropertyIsEqualTo equals(Expression expr1, Expression expr2) {
        return equal( expr1,expr2,true);
    }
    
    public PropertyIsEqualTo equal(Expression expr1, Expression expr2, boolean matchCase) {
        return new IsEqualsToImpl(expr1,expr2,matchCase); 
    }
    
    public PropertyIsEqualTo equal(Expression expr1, Expression expr2, boolean matchCase,
            MatchAction matchAction) {
        return new IsEqualsToImpl(expr1,expr2,matchCase,matchAction);
    }
    
    public PropertyIsNotEqualTo notEqual(Expression expr1, Expression expr2) {
            return notEqual(expr1, expr2, false );
    }
    
    public PropertyIsNotEqualTo notEqual(Expression expr1, Expression expr2, boolean matchCase) {
        return new IsNotEqualToImpl(expr1,expr2,matchCase);
    }
    
    public PropertyIsNotEqualTo notEqual(Expression expr1, Expression expr2, boolean matchCase,
            MatchAction matchAction) {
        return new IsNotEqualToImpl(expr1,expr2,matchCase,matchAction);
    }
    
    public PropertyIsGreaterThan greater(Expression expr1, Expression expr2) {
        return greater(expr1,expr2,false);
    }
    
    public PropertyIsGreaterThan greater(Expression expr1, Expression expr2, boolean matchCase) {
        return new IsGreaterThanImpl(expr1, expr2);
    }
    
    public PropertyIsGreaterThan greater(Expression expr1, Expression expr2, boolean matchCase,
            MatchAction matchAction) {
        return new IsGreaterThanImpl( expr1, expr2, matchAction);
    }
    
    public PropertyIsGreaterThanOrEqualTo greaterOrEqual(Expression expr1,
            Expression expr2) {
        return greaterOrEqual(expr1,expr2,false);
    }
    
    public PropertyIsGreaterThanOrEqualTo greaterOrEqual(Expression expr1,
                        Expression expr2, boolean matchCase) {
        return new IsGreaterThanOrEqualToImpl(expr1,expr2,matchCase);
    }
    
    public PropertyIsGreaterThanOrEqualTo greaterOrEqual(Expression expr1, Expression expr2,
            boolean matchCase, MatchAction matchAction) {
        return new IsGreaterThanOrEqualToImpl(expr1,expr2,matchCase,matchAction);
    }

    public PropertyIsLessThan less(Expression expr1, Expression expr2) {
        return less(expr1,expr2,false);
    }
    
    public PropertyIsLessThan less(Expression expr1, Expression expr2,
                    boolean matchCase) {
        return new IsLessThenImpl(expr1,expr2,matchCase);
    }
    
    public PropertyIsLessThan less(Expression expr1, Expression expr2,
            boolean matchCase, MatchAction matchAction) {
        return new IsLessThenImpl(expr1,expr2,matchCase,matchAction);
    }

    public PropertyIsLessThanOrEqualTo lessOrEqual(Expression expr1,
            Expression expr2) {
        return lessOrEqual(expr1,expr2,false);
    }
    
    public PropertyIsLessThanOrEqualTo lessOrEqual(Expression expr1,
                        Expression expr2, boolean matchCase) {
        return new IsLessThenOrEqualToImpl(expr1,expr2,false);
    }
    
    public PropertyIsLessThanOrEqualTo lessOrEqual(Expression expr1, Expression expr2,
            boolean matchCase, MatchAction matchAction) {
        return new IsLessThenOrEqualToImpl(expr1,expr2,false, matchAction);        
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
    
    public PropertyIsLike like(Expression expr, String pattern, String wildcard, String singleChar,
            String escape, boolean matchCase, MatchAction matchAction) {
        LikeFilterImpl filter = new LikeFilterImpl(matchAction);
        filter.setExpression(expr);
        filter.setPattern(pattern,wildcard,singleChar,escape);
        filter.setMatchingCase( matchCase );
        
        return filter;
    }

    /**
     * XXX Java 5 type narrowing used to make generated class explicit for chaining
     */
    public PropertyIsNull /*IsNullImpl*/ isNull(Expression expr) {
        return new IsNullImpl(expr );
    }

    public PropertyIsNil isNil(Expression expr, Object nilReason) {
        return new IsNilImpl(expr, nilReason);
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
        return new BBOXImpl(geometry, bounds );
    }
    
    public BBOX bbox( Expression geometry, BoundingBox bounds ) {
    	if (bounds instanceof BoundingBox3D) {
    		return bbox(geometry, (BoundingBox3D) bounds);
    	} else {
	        return bbox2d( geometry, bounds, MatchAction.ANY );
    	}
    }
    
    public BBOX bbox(Expression geometry, BoundingBox bounds, MatchAction matchAction) {
    	if (bounds instanceof BoundingBox3D) {
    		return bbox(geometry, (BoundingBox3D) bounds);
    	} else {
    		return bbox2d( geometry, bounds, matchAction );
    	}
                
    }
    
    private BBOXImpl bbox2d (Expression e, BoundingBox bounds, MatchAction matchAction) {    	       
    	PropertyName name = null;
        if ( e instanceof PropertyName ) {
            name = (PropertyName) e;
        }
        else {
            throw new IllegalArgumentException("BBOX requires PropertyName expression");
        }
        
        Literal bbox = null;
        try {
            ReferencedEnvelope env = ReferencedEnvelope.reference(bounds);
            bbox = literal(BBOXImpl.boundingPolygon( env ) );
        } 
        catch (IllegalFilterException ife) {
            new IllegalArgumentException("Unable to convert to Polygon:"+bounds).initCause(ife);
        }
        
        return new BBOXImpl(name,bbox, matchAction);
    }
    
    public BBOX bbox(String propertyName, double minx, double miny, double maxx, double maxy,
            String srs, MatchAction matchAction) {
        PropertyName name = property(propertyName);
        return bbox(name, minx, miny, maxx, maxy, srs, matchAction);
    }
    
    public BBOX bbox(Expression geometry, double minx, double miny, double maxx, double maxy, String srs) {
        return bbox(geometry, minx, miny, maxx, maxy, srs, MatchAction.ANY);
    }
    

    public BBOX bbox(Expression e, double minx, double miny, double maxx, double maxy, String srs, MatchAction matchAction) {
    	
    	BBOXImpl box = bbox2d (e, new ReferencedEnvelope(minx,maxx,miny,maxy,null), matchAction);        
        box.setSRS(srs);
        
        return box;
    }
        
    public BBOX3D bbox(String propertyName, BoundingBox3D env) {
        return bbox(property(propertyName), env, MatchAction.ANY);
    }
    
    public BBOX3D bbox(String propertyName, BoundingBox3D env, MatchAction matchAction) {
    	return bbox(property(propertyName), env, matchAction);
    }
        
    public BBOX3D bbox(Expression geometry, BoundingBox3D env) {
        return bbox(geometry, env, MatchAction.ANY);
    }
           
    public BBOX3D bbox(Expression e, BoundingBox3D env, MatchAction matchAction) {
    	PropertyName name = null;
        if ( e instanceof PropertyName ) {
            name = (PropertyName) e;
        }
        else {
            throw new IllegalArgumentException();
        }
        
        return new BBOX3DImpl(name, new ReferencedEnvelope3D(env), this);  
    }
    
    public Beyond beyond(String propertyName, Geometry geometry,
            double distance, String units) {
        
        PropertyName name = property(propertyName);
        Literal geom = literal(geometry);
          
        return beyond( name, geom, distance, units );
    }

    public Beyond beyond(String propertyName, Geometry geometry, double distance, String units,
                MatchAction matchAction) {
        PropertyName name = property(propertyName);
        Literal geom = literal(geometry);
          
        return beyond( name, geom, distance, units, matchAction );
    }
    
    public Beyond beyond( 
        Expression geometry1, Expression geometry2, double distance, String units
    ) {
        
        BeyondImpl beyond = new BeyondImpl(geometry1,geometry2);
        beyond.setDistance(distance);
        beyond.setUnits(units);
        
        return beyond;
    }
    
    public Beyond beyond(Expression geometry1, Expression geometry2, double distance, String units,
            MatchAction matchAction) {
        BeyondImpl beyond = new BeyondImpl(geometry1,geometry2,matchAction);
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
        return new ContainsImpl(geometry1, geometry2 );
    }
    
    public Contains contains(Expression geometry1, Expression geometry2, MatchAction matchAction) {
        return new ContainsImpl(geometry1, geometry2, matchAction );
    }
    
    public Contains contains(String propertyName, Geometry geometry, MatchAction matchAction) {
        PropertyName name = property(propertyName);
        Literal geom = literal(geometry);
        
        return contains( name, geom, matchAction );
    }

    public Crosses crosses(String propertyName, Geometry geometry) {
        PropertyName name = property(propertyName);
        Literal geom = literal(geometry);
        
        return crosses( name, geom );
    }
    
    public Crosses crosses(String propertyName, Geometry geometry, MatchAction matchAction) {
        PropertyName name = property(propertyName);
        Literal geom = literal(geometry);
        
        return crosses( name, geom, matchAction );
    }
    
    public Crosses crosses(Expression geometry1, Expression geometry2) {
        return new CrossesImpl(geometry1, geometry2 );
    }
    
    public Crosses crosses(Expression geometry1, Expression geometry2, MatchAction matchAction) {
        return new CrossesImpl(geometry1, geometry2 , matchAction );
    }

    public Disjoint disjoint(String propertyName, Geometry geometry) {
        PropertyName name = property(propertyName);
        Literal geom = literal(geometry);
        
        return disjoint( name, geom );
    }
    
    public Disjoint disjoint(String propertyName, Geometry geometry, MatchAction matchAction) {
        PropertyName name = property(propertyName);
        Literal geom = literal(geometry);
        
        return disjoint( name, geom, matchAction );
    }
    
    public Disjoint disjoint(Expression geometry1, Expression geometry2, MatchAction matchAction) {
        return new DisjointImpl(geometry1, geometry2, matchAction );
    }
    
    public Disjoint disjoint(Expression geometry1, Expression geometry2) {
        return new DisjointImpl(geometry1, geometry2 );
    }
    
    public DWithin dwithin(String propertyName, Geometry geometry,
            double distance, String units) {
        PropertyName name = property(propertyName);
        Literal geom = literal(geometry);
        
        return dwithin( name, geom, distance, units );
    }
    
    public DWithin dwithin(String propertyName, Geometry geometry, double distance, String units,
            MatchAction matchAction) {
        PropertyName name = property(propertyName);
        Literal geom = literal(geometry);
        
        return dwithin( name, geom, distance, units, matchAction );
    }
    
    public DWithin dwithin(Expression geometry1, Expression geometry2, double distance,
            String units, MatchAction matchAction) {
        DWithinImpl dwithin =  new DWithinImpl(geometry1, geometry2, matchAction );
        dwithin.setDistance( distance );
        dwithin.setUnits( units );
        
        return dwithin;
    }

    public DWithin dwithin(Expression geometry1, Expression geometry2, double distance, String units) {
        DWithinImpl dwithin =  new DWithinImpl(geometry1, geometry2 );
        dwithin.setDistance( distance );
        dwithin.setUnits( units );
        
        return dwithin;
    }
    
    public Equals equals(String propertyName, Geometry geometry) {
        PropertyName name = property(propertyName);
        Literal geom = literal(geometry);
        
        return equal( name, geom );
    }
    
    public Equals equals(String propertyName, Geometry geometry, MatchAction matchAction) {
        PropertyName name = property(propertyName);
        Literal geom = literal(geometry);
        
        return equal( name, geom, matchAction );
    }
    
    public Equals equal(Expression geometry1, Expression geometry2) {
        return new EqualsImpl(geometry1, geometry2 );
    }
    
    public Equals equal(Expression geometry1, Expression geometry2, MatchAction matchAction) {
        return new EqualsImpl(geometry1, geometry2, matchAction );
    }

    public Intersects intersects(String propertyName, Geometry geometry) {
        PropertyName name = property(propertyName);
        Literal geom = literal(geometry);
        
        return intersects( name, geom );
    }
    
    public Intersects intersects(String propertyName, Geometry geometry, MatchAction matchAction) {
        PropertyName name = property(propertyName);
        Literal geom = literal(geometry);
        
        return intersects( name, geom, matchAction );
    }

    public Intersects intersects(Expression geometry1, Expression geometry2) {
        return new IntersectsImpl(geometry1, geometry2 );
    }
    
    public Intersects intersects(Expression geometry1, Expression geometry2, MatchAction matchAction) {
        return new IntersectsImpl(geometry1, geometry2, matchAction );
    }
    
    public Overlaps overlaps(String propertyName, Geometry geometry) {
        PropertyName name = property(propertyName);
        Literal geom = literal(geometry);
        
        return overlaps( name, geom );
    }
    
    public Overlaps overlaps(String propertyName, Geometry geometry, MatchAction matchAction) {
        PropertyName name = property(propertyName);
        Literal geom = literal(geometry);
        
        return overlaps( name, geom, matchAction );
    }

    public Overlaps overlaps(Expression geometry1, Expression geometry2) {
        return new OverlapsImpl(geometry1, geometry2 );
    }
    
    public Overlaps overlaps(Expression geometry1, Expression geometry2, MatchAction matchAction) {
        return new OverlapsImpl(geometry1, geometry2, matchAction );
    }
    
    public Touches touches(String propertyName, Geometry geometry) {
        PropertyName name = property(propertyName);
        Literal geom = literal(geometry);
        
        return touches( name, geom );
    }
    
    public Touches touches(String propertyName, Geometry geometry, MatchAction matchAction) {
        PropertyName name = property(propertyName);
        Literal geom = literal(geometry);
        
        return touches( name, geom, matchAction );
    }

    public Touches touches(Expression geometry1, Expression geometry2) {
        return new TouchesImpl(geometry1,geometry2);
    }
    
    public Touches touches(Expression geometry1, Expression geometry2, MatchAction matchAction) {
        return new TouchesImpl(geometry1,geometry2, matchAction);
    }
    
    public Within within(String propertyName, Geometry geometry) {
        PropertyName name = property(propertyName);
        Literal geom = literal(geometry);
        
        return within( name, geom );
    }
    
    public Within within(String propertyName, Geometry geometry, MatchAction matchAction) {
        PropertyName name = property(propertyName);
        Literal geom = literal(geometry);
        
        return within( name, geom, matchAction );
    }

    public Within within(Expression geometry1, Expression geometry2) {
        return new WithinImpl(geometry1, geometry2 );
    }
    
    public Within within(Expression geometry1, Expression geometry2, MatchAction matchAction) {
        return new WithinImpl(geometry1, geometry2, matchAction );
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

    public Function function(Name name, Expression... args) {
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

    
    public Map getImplementationHints() {
                return Collections.EMPTY_MAP;
        }
        
        public SortBy sort(String propertyName, SortOrder order) {
                return new SortByImpl( property( propertyName ), order );
        }

    public After after(Expression expr1, Expression expr2) {
        return new AfterImpl(expr1, expr2);
    }

    public After after(Expression expr1, Expression expr2, MatchAction matchAction) {
        return new AfterImpl(expr1, expr2, matchAction);
    }

    public AnyInteracts anyInteracts(Expression expr1, Expression expr2) {
        return new AnyInteractsImpl(expr1, expr2);
    }

    public AnyInteracts anyInteracts(Expression expr1, Expression expr2, MatchAction matchAction) {
        return new AnyInteractsImpl(expr1, expr2, matchAction);
    }

    public Before before(Expression expr1, Expression expr2) {
        return new BeforeImpl(expr1, expr2);
    }

    public Before before(Expression expr1, Expression expr2, MatchAction matchAction) {
        return new BeforeImpl(expr1, expr2, matchAction);
    }

    public Begins begins(Expression expr1, Expression expr2) {
        return new BeginsImpl(expr1, expr2);
    }

    public Begins begins(Expression expr1, Expression expr2, MatchAction matchAction) {
        return new BeginsImpl(expr1, expr2, matchAction);
    }

    public BegunBy begunBy(Expression expr1, Expression expr2) {
        return new BegunByImpl(expr1, expr2);
    }

    public BegunBy begunBy(Expression expr1, Expression expr2, MatchAction matchAction) {
        return new BegunByImpl(expr1, expr2, matchAction);
    }

    public During during(Expression expr1, Expression expr2) {
        return new DuringImpl(expr1, expr2);
    }

    public During during(Expression expr1, Expression expr2, MatchAction matchAction) {
        return new DuringImpl(expr1, expr2, matchAction);
    }

    public EndedBy endedBy(Expression expr1, Expression expr2) {
        return new EndedByImpl(expr1, expr2);
    }

    public EndedBy endedBy(Expression expr1, Expression expr2, MatchAction matchAction) {
        return new EndedByImpl(expr1, expr2, matchAction);
    }

    public Ends ends(Expression expr1, Expression expr2) {
        return new EndsImpl(expr1, expr2);
    }

    public Ends ends(Expression expr1, Expression expr2, MatchAction matchAction) {
        return new EndsImpl(expr1, expr2, matchAction);
    }

    public Meets meets(Expression expr1, Expression expr2) {
        return new MeetsImpl(expr1, expr2);
    }

    public Meets meets(Expression expr1, Expression expr2, MatchAction matchAction) {
        return new MeetsImpl(expr1, expr2, matchAction);
    }

    public MetBy metBy(Expression expr1, Expression expr2) {
        return new MetByImpl(expr1, expr2);
    }

    public MetBy metBy(Expression expr1, Expression expr2, MatchAction matchAction) {
        return new MetByImpl(expr1, expr2, matchAction);
    }

    public OverlappedBy overlappedBy(Expression expr1, Expression expr2) {
        return new OverlappedByImpl(expr1, expr2);
    }

    public OverlappedBy overlappedBy(Expression expr1, Expression expr2, MatchAction matchAction) {
        return new OverlappedByImpl(expr1, expr2, matchAction);
    }

    public TContains tcontains(Expression expr1, Expression expr2) {
        return new TContainsImpl(expr1, expr2);
    }

    public TContains tcontains(Expression expr1, Expression expr2, MatchAction matchAction) {
        return new TContainsImpl(expr1, expr2, matchAction);
    }

    public TEquals tequals(Expression expr1, Expression expr2) {
        return new TEqualsImpl(expr1, expr2);
    }

    public TEquals tequals(Expression expr1, Expression expr2, MatchAction matchAction) {
        return new TEqualsImpl(expr1, expr2, matchAction);
    }

    public TOverlaps toverlaps(Expression expr1, Expression expr2) {
        return new TOverlapsImpl(expr1, expr2);
    }

    public TOverlaps toverlaps(Expression expr1, Expression expr2, MatchAction matchAction) {
        return new TOverlapsImpl(expr1, expr2, matchAction);
    }

//    public org.geotools.filter.Filter and( org.geotools.filter.Filter filter1, org.geotools.filter.Filter filter2 ) {
//        return (org.geotools.filter.Filter) and( (Filter) filter1, (Filter) filter2 );         
//    } 
//
//    public org.geotools.filter.Filter not( org.geotools.filter.Filter filter ) {
//        return (org.geotools.filter.Filter) not( (Filter) filter );
//    }
//
//    public org.geotools.filter.Filter or( org.geotools.filter.Filter filter1, org.geotools.filter.Filter filter2 ) {
//        return (org.geotools.filter.Filter) or( (Filter) filter1, (Filter) filter2 );
//    }
    
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
    
    public TemporalOperator temporalOperator(String name) {
        return new TemporalOperatorImpl(name); 
    }

    public <T> Parameter<T> parameter(String name, Class<T> type, InternationalString title, 
        InternationalString description, boolean required, int minOccurs, int maxOccurs, T defaultValue) {
        return new org.geotools.data.Parameter<T>(name, type, title, description, required, 
            minOccurs, maxOccurs, defaultValue, null);
    };

    public FunctionName functionName(String name, int nargs) {
        return new FunctionNameImpl( name, nargs );
    }

    @Override
    public FunctionName functionName(Name name, int nargs) {
        return new FunctionNameImpl( name, nargs );
    }

    public FunctionName functionName(String name, int nargs, List<String> argNames ){
        return new FunctionNameImpl( name, nargs, argNames );
    }

    @Override
    public FunctionName functionName(Name name, int nargs, List<String> argNames) {
        return new FunctionNameImpl( name, nargs, argNames );
    }
    
    public FunctionName functionName(String name, List<Parameter<?>> args, Parameter<?> ret) {
        return new FunctionNameImpl( name, ret, args );
    }

    @Override
    public FunctionName functionName(Name name, List<Parameter<?>> args, Parameter<?> ret) {
        return new FunctionNameImpl( name, ret, args );
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

    public FilterCapabilities capabilities(String version, ScalarCapabilities scalar,
            SpatialCapabilities spatial, IdCapabilities id, TemporalCapabilities temporal) {
        return new FilterCapabilitiesImpl(version, scalar, spatial, id, temporal);
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

    public TemporalCapabilities temporalCapabilities(TemporalOperator[] temporalOperators) {
        return new TemporalCapabilitiesImpl(Arrays.asList(temporalOperators));
    }
}
