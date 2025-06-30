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
import org.geotools.api.feature.type.Name;
import org.geotools.api.filter.And;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.Id;
import org.geotools.api.filter.MultiValuedFilter.MatchAction;
import org.geotools.api.filter.NativeFilter;
import org.geotools.api.filter.Not;
import org.geotools.api.filter.Or;
import org.geotools.api.filter.PropertyIsBetween;
import org.geotools.api.filter.PropertyIsEqualTo;
import org.geotools.api.filter.PropertyIsGreaterThan;
import org.geotools.api.filter.PropertyIsGreaterThanOrEqualTo;
import org.geotools.api.filter.PropertyIsLessThan;
import org.geotools.api.filter.PropertyIsLessThanOrEqualTo;
import org.geotools.api.filter.PropertyIsLike;
import org.geotools.api.filter.PropertyIsNil;
import org.geotools.api.filter.PropertyIsNotEqualTo;
import org.geotools.api.filter.PropertyIsNull;
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
import org.geotools.api.geometry.MismatchedDimensionException;
import org.geotools.api.parameter.Parameter;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.NoSuchAuthorityCodeException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.util.InternationalString;
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
import org.geotools.filter.spatial.BBOX3DImpl;
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
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope3D;
import org.geotools.referencing.CRS;
import org.geotools.util.factory.Factory;
import org.geotools.util.factory.Hints;
import org.locationtech.jts.geom.Geometry;
import org.xml.sax.helpers.NamespaceSupport;

/**
 * Implementation of the FilterFactory, generates the filter implementations in defaultcore.
 *
 * @author Ian Turton, CCG
 * @version $Id$
 */
public class FilterFactoryImpl implements Factory, org.geotools.api.filter.FilterFactory {

    private FunctionFinder functionFinder;

    /** Creates a new instance of FilterFactoryImpl */
    public FilterFactoryImpl() {
        this(null);
    }

    public FilterFactoryImpl(Hints hints) {
        functionFinder = new FunctionFinder(null);
    }

    @Override
    public FeatureId featureId(String id) {
        return new FeatureIdImpl(id);
    }

    @Override
    public GmlObjectId gmlObjectId(String id) {
        return new GmlObjectIdImpl(id);
    }

    /** Creates a new feature id with version information */
    @Override
    public FeatureId featureId(String fid, String featureVersion) {
        return new FeatureIdVersionedImpl(fid, featureVersion);
    }

    /** ResouceId for identifier based query */
    @Override
    public ResourceId resourceId(String fid, String featureVersion, Version version) {
        return new ResourceIdImpl(fid, featureVersion, version);
    }

    /** ResourceId for time based query */
    @Override
    public ResourceId resourceId(String fid, Date startTime, Date endTime) {
        return new ResourceIdImpl(fid, startTime, endTime);
    }

    @Override
    public And and(Filter f, Filter g) {
        List<Filter> list = new ArrayList<>(2);
        list.add(f);
        list.add(g);
        return new AndImpl(list);
    }

    @Override
    public And and(List<Filter> filters) {
        return new AndImpl(filters);
    }

    @Override
    public Or or(Filter f, Filter g) {
        List<Filter> list = new ArrayList<>(2);
        list.add(f);
        list.add(g);
        return new OrImpl(list);
    }

    @Override
    public Or or(List<Filter> filters) {
        return new OrImpl(filters);
    }

    /** Java 5 type narrowing used to advertise explicit implementation for chaining */
    @Override
    public Not /*NotImpl*/ not(Filter filter) {
        return new NotImpl(filter);
    }

    @Override
    public Id id(Set<? extends Identifier> id) {
        return new FidFilterImpl(id);
    }

    @Override
    public Id id(FeatureId... fids) {
        Set<FeatureId> selection = new HashSet<>();
        for (FeatureId featureId : fids) {
            if (featureId == null) continue;
            selection.add(featureId);
        }
        return id(selection);
    }

    @Override
    public PropertyName property(String name) {
        return new AttributeExpressionImpl(name);
    }

    @Override
    public PropertyIsBetween between(Expression expr, Expression lower, Expression upper) {
        return new IsBetweenImpl(lower, expr, upper);
    }

    @Override
    public PropertyIsBetween between(Expression expr, Expression lower, Expression upper, MatchAction matchAction) {
        return new IsBetweenImpl(lower, expr, upper, matchAction);
    }

    @Override
    public PropertyIsEqualTo equals(Expression expr1, Expression expr2) {
        return equal(expr1, expr2, true);
    }

    @Override
    public PropertyIsEqualTo equal(Expression expr1, Expression expr2, boolean matchCase) {
        return new IsEqualsToImpl(expr1, expr2, matchCase);
    }

    @Override
    public PropertyIsEqualTo equal(Expression expr1, Expression expr2, boolean matchCase, MatchAction matchAction) {
        return new IsEqualsToImpl(expr1, expr2, matchCase, matchAction);
    }

    @Override
    public PropertyIsNotEqualTo notEqual(Expression expr1, Expression expr2) {
        return notEqual(expr1, expr2, false);
    }

    @Override
    public PropertyIsNotEqualTo notEqual(Expression expr1, Expression expr2, boolean matchCase) {
        return new IsNotEqualToImpl(expr1, expr2, matchCase);
    }

    @Override
    public PropertyIsNotEqualTo notEqual(
            Expression expr1, Expression expr2, boolean matchCase, MatchAction matchAction) {
        return new IsNotEqualToImpl(expr1, expr2, matchCase, matchAction);
    }

    @Override
    public PropertyIsGreaterThan greater(Expression expr1, Expression expr2) {
        return greater(expr1, expr2, false);
    }

    @Override
    public PropertyIsGreaterThan greater(Expression expr1, Expression expr2, boolean matchCase) {
        return new IsGreaterThanImpl(expr1, expr2);
    }

    @Override
    public PropertyIsGreaterThan greater(
            Expression expr1, Expression expr2, boolean matchCase, MatchAction matchAction) {
        return new IsGreaterThanImpl(expr1, expr2, matchAction);
    }

    @Override
    public PropertyIsGreaterThanOrEqualTo greaterOrEqual(Expression expr1, Expression expr2) {
        return greaterOrEqual(expr1, expr2, false);
    }

    @Override
    public PropertyIsGreaterThanOrEqualTo greaterOrEqual(Expression expr1, Expression expr2, boolean matchCase) {
        return new IsGreaterThanOrEqualToImpl(expr1, expr2, matchCase);
    }

    @Override
    public PropertyIsGreaterThanOrEqualTo greaterOrEqual(
            Expression expr1, Expression expr2, boolean matchCase, MatchAction matchAction) {
        return new IsGreaterThanOrEqualToImpl(expr1, expr2, matchCase, matchAction);
    }

    @Override
    public PropertyIsLessThan less(Expression expr1, Expression expr2) {
        return less(expr1, expr2, false);
    }

    @Override
    public PropertyIsLessThan less(Expression expr1, Expression expr2, boolean matchCase) {
        return new IsLessThenImpl(expr1, expr2, matchCase);
    }

    @Override
    public PropertyIsLessThan less(Expression expr1, Expression expr2, boolean matchCase, MatchAction matchAction) {
        return new IsLessThenImpl(expr1, expr2, matchCase, matchAction);
    }

    @Override
    public PropertyIsLessThanOrEqualTo lessOrEqual(Expression expr1, Expression expr2) {
        return lessOrEqual(expr1, expr2, false);
    }

    @Override
    public PropertyIsLessThanOrEqualTo lessOrEqual(Expression expr1, Expression expr2, boolean matchCase) {
        return new IsLessThenOrEqualToImpl(expr1, expr2, false);
    }

    @Override
    public PropertyIsLessThanOrEqualTo lessOrEqual(
            Expression expr1, Expression expr2, boolean matchCase, MatchAction matchAction) {
        return new IsLessThenOrEqualToImpl(expr1, expr2, false, matchAction);
    }

    @Override
    public PropertyIsLike like(Expression expr, String pattern) {
        return like(expr, pattern, "*", "?", "\\");
    }

    @Override
    public PropertyIsLike like(Expression expr, String pattern, String wildcard, String singleChar, String escape) {
        return like(expr, pattern, wildcard, singleChar, escape, false);
    }

    @Override
    public PropertyIsLike like(
            Expression expr, String pattern, String wildcard, String singleChar, String escape, boolean matchCase) {
        LikeFilterImpl filter = new LikeFilterImpl();
        filter.setExpression(expr);

        filter.setLiteral(pattern);
        filter.setWildCard(wildcard);
        filter.setSingleChar(singleChar);
        filter.setEscape(escape);
        filter.setMatchingCase(matchCase);

        return filter;
    }

    @Override
    public PropertyIsLike like(
            Expression expr,
            String pattern,
            String wildcard,
            String singleChar,
            String escape,
            boolean matchCase,
            MatchAction matchAction) {
        LikeFilterImpl filter = new LikeFilterImpl(matchAction);
        filter.setExpression(expr);

        filter.setLiteral(pattern);
        filter.setWildCard(wildcard);
        filter.setSingleChar(singleChar);
        filter.setEscape(escape);
        filter.setMatchingCase(matchCase);

        return filter;
    }

    /** XXX Java 5 type narrowing used to make generated class explicit for chaining */
    @Override
    public PropertyIsNull /*IsNullImpl*/ isNull(Expression expr) {
        return new IsNullImpl(expr);
    }

    @Override
    public PropertyIsNil isNil(Expression expr, Object nilReason) {
        return new IsNilImpl(expr, nilReason);
    }

    /**
     * Checks if the bounding box of the feature's geometry overlaps the specified bounding box.
     *
     * <p>Similar to: <code>
     * geom().disjoint( geom( bbox )).not()
     * </code>
     */
    @Override
    public BBOX bbox(String propertyName, double minx, double miny, double maxx, double maxy, String srs) {

        PropertyName name = property(propertyName);
        return bbox(name, minx, miny, maxx, maxy, srs);
    }

    @Override
    public BBOX bbox(Expression geometry, Expression bounds) {
        return new BBOXImpl(geometry, bounds);
    }

    @Override
    public BBOX bbox(Expression geometry, Expression bounds, MatchAction machAction) {
        if (bounds instanceof Literal) {
            Object value = ((Literal) bounds).getValue();
            if (value instanceof BoundingBox3D) {
                return bbox(geometry, (BoundingBox3D) value, machAction);
            } else if (value instanceof org.locationtech.jts.geom.Geometry && geometry instanceof PropertyName) {
                org.locationtech.jts.geom.Geometry g = (org.locationtech.jts.geom.Geometry) value;
                if (g.getUserData() instanceof CoordinateReferenceSystem) {
                    CoordinateReferenceSystem crs = (CoordinateReferenceSystem) g.getUserData();
                    ReferencedEnvelope3D envelope = (ReferencedEnvelope3D) JTS.bounds(g, crs);
                    return new BBOX3DImpl((PropertyName) geometry, envelope, this);
                }
            }
        }
        return new BBOXImpl(geometry, bounds, machAction);
    }

    @Override
    public BBOX bbox(Expression geometry, BoundingBox bounds) {
        if (bounds instanceof BoundingBox3D) {
            return bbox(geometry, (BoundingBox3D) bounds);
        } else {
            return bbox2d(geometry, bounds, MatchAction.ANY);
        }
    }

    @Override
    public BBOX bbox(Expression geometry, BoundingBox bounds, MatchAction matchAction) {
        if (bounds instanceof BoundingBox3D) {
            return bbox(geometry, (BoundingBox3D) bounds);
        } else {
            return bbox2d(geometry, bounds, matchAction);
        }
    }

    private BBOXImpl bbox2d(Expression e, BoundingBox bounds, MatchAction matchAction) {
        PropertyName name = null;
        if (e instanceof PropertyName) {
            name = (PropertyName) e;
        } else {
            throw new IllegalArgumentException("BBOX requires PropertyName expression");
        }

        Literal bbox = null;
        try {
            ReferencedEnvelope env = ReferencedEnvelope.reference(bounds);
            bbox = literal(env);
        } catch (IllegalFilterException ife) {
            new IllegalArgumentException("Unable to convert to Polygon:" + bounds).initCause(ife);
        }

        return new BBOXImpl(name, bbox, matchAction);
    }

    @Override
    public BBOX bbox(
            String propertyName,
            double minx,
            double miny,
            double maxx,
            double maxy,
            String srs,
            MatchAction matchAction) {
        PropertyName name = property(propertyName);
        return bbox(name, minx, miny, maxx, maxy, srs, matchAction);
    }

    @Override
    public BBOX bbox(Expression geometry, double minx, double miny, double maxx, double maxy, String srs) {
        return bbox(geometry, minx, miny, maxx, maxy, srs, MatchAction.ANY);
    }

    @Override
    public BBOX bbox(
            Expression e, double minx, double miny, double maxx, double maxy, String srs, MatchAction matchAction) {

        BBOXImpl box = null;
        try {
            CoordinateReferenceSystem crs;
            if (srs == null || srs.isEmpty()) {
                crs = null;
            } else {
                try {
                    crs = CRS.decode(srs);
                } catch (MismatchedDimensionException ex) {
                    throw new RuntimeException(ex);
                } catch (NoSuchAuthorityCodeException ex) {
                    crs = CRS.parseWKT(srs);
                }
            }
            box = bbox2d(e, new ReferencedEnvelope(minx, maxx, miny, maxy, crs), matchAction);
        } catch (FactoryException e1) {
            throw new RuntimeException("Failed to setup bbox SRS", e1);
        }

        return box;
    }

    @Override
    public BBOX3D bbox(String propertyName, BoundingBox3D env) {
        return bbox(property(propertyName), env, MatchAction.ANY);
    }

    @Override
    public BBOX3D bbox(String propertyName, BoundingBox3D env, MatchAction matchAction) {
        return bbox(property(propertyName), env, matchAction);
    }

    @Override
    public BBOX3D bbox(Expression geometry, BoundingBox3D env) {
        return bbox(geometry, env, MatchAction.ANY);
    }

    @Override
    public BBOX3D bbox(Expression e, BoundingBox3D env, MatchAction matchAction) {
        PropertyName name = null;
        if (e instanceof PropertyName) {
            name = (PropertyName) e;
        } else {
            throw new IllegalArgumentException();
        }

        return new BBOX3DImpl(name, new ReferencedEnvelope3D(env), this);
    }

    @Override
    public Beyond beyond(String propertyName, Geometry geometry, double distance, String units) {

        PropertyName name = property(propertyName);
        Literal geom = literal(geometry);

        return beyond(name, geom, distance, units);
    }

    @Override
    public Beyond beyond(
            String propertyName, Geometry geometry, double distance, String units, MatchAction matchAction) {
        PropertyName name = property(propertyName);
        Literal geom = literal(geometry);

        return beyond(name, geom, distance, units, matchAction);
    }

    @Override
    public Beyond beyond(Expression geometry1, Expression geometry2, double distance, String units) {

        BeyondImpl beyond = new BeyondImpl(geometry1, geometry2);
        beyond.setDistance(distance);
        beyond.setUnits(units);

        return beyond;
    }

    @Override
    public Beyond beyond(
            Expression geometry1, Expression geometry2, double distance, String units, MatchAction matchAction) {
        BeyondImpl beyond = new BeyondImpl(geometry1, geometry2, matchAction);
        beyond.setDistance(distance);
        beyond.setUnits(units);

        return beyond;
    }

    @Override
    public Contains contains(String propertyName, Geometry geometry) {
        PropertyName name = property(propertyName);
        Literal geom = literal(geometry);

        return contains(name, geom);
    }

    @Override
    public Contains contains(Expression geometry1, Expression geometry2) {
        return new ContainsImpl(geometry1, geometry2);
    }

    @Override
    public Contains contains(Expression geometry1, Expression geometry2, MatchAction matchAction) {
        return new ContainsImpl(geometry1, geometry2, matchAction);
    }

    @Override
    public Contains contains(String propertyName, Geometry geometry, MatchAction matchAction) {
        PropertyName name = property(propertyName);
        Literal geom = literal(geometry);

        return contains(name, geom, matchAction);
    }

    @Override
    public Crosses crosses(String propertyName, Geometry geometry) {
        PropertyName name = property(propertyName);
        Literal geom = literal(geometry);

        return crosses(name, geom);
    }

    @Override
    public Crosses crosses(String propertyName, Geometry geometry, MatchAction matchAction) {
        PropertyName name = property(propertyName);
        Literal geom = literal(geometry);

        return crosses(name, geom, matchAction);
    }

    @Override
    public Crosses crosses(Expression geometry1, Expression geometry2) {
        return new CrossesImpl(geometry1, geometry2);
    }

    @Override
    public Crosses crosses(Expression geometry1, Expression geometry2, MatchAction matchAction) {
        return new CrossesImpl(geometry1, geometry2, matchAction);
    }

    @Override
    public Disjoint disjoint(String propertyName, Geometry geometry) {
        PropertyName name = property(propertyName);
        Literal geom = literal(geometry);

        return disjoint(name, geom);
    }

    @Override
    public Disjoint disjoint(String propertyName, Geometry geometry, MatchAction matchAction) {
        PropertyName name = property(propertyName);
        Literal geom = literal(geometry);

        return disjoint(name, geom, matchAction);
    }

    @Override
    public Disjoint disjoint(Expression geometry1, Expression geometry2, MatchAction matchAction) {
        return new DisjointImpl(geometry1, geometry2, matchAction);
    }

    @Override
    public Disjoint disjoint(Expression geometry1, Expression geometry2) {
        return new DisjointImpl(geometry1, geometry2);
    }

    @Override
    public DWithin dwithin(String propertyName, Geometry geometry, double distance, String units) {
        PropertyName name = property(propertyName);
        Literal geom = literal(geometry);

        return dwithin(name, geom, distance, units);
    }

    @Override
    public DWithin dwithin(
            String propertyName, Geometry geometry, double distance, String units, MatchAction matchAction) {
        PropertyName name = property(propertyName);
        Literal geom = literal(geometry);

        return dwithin(name, geom, distance, units, matchAction);
    }

    @Override
    public DWithin dwithin(
            Expression geometry1, Expression geometry2, double distance, String units, MatchAction matchAction) {
        DWithinImpl dwithin = new DWithinImpl(geometry1, geometry2, matchAction);
        dwithin.setDistance(distance);
        dwithin.setUnits(units);

        return dwithin;
    }

    @Override
    public DWithin dwithin(Expression geometry1, Expression geometry2, double distance, String units) {
        DWithinImpl dwithin = new DWithinImpl(geometry1, geometry2);
        dwithin.setDistance(distance);
        dwithin.setUnits(units);

        return dwithin;
    }

    @Override
    public Equals equals(String propertyName, Geometry geometry) {
        PropertyName name = property(propertyName);
        Literal geom = literal(geometry);

        return equal(name, geom);
    }

    @Override
    public Equals equals(String propertyName, Geometry geometry, MatchAction matchAction) {
        PropertyName name = property(propertyName);
        Literal geom = literal(geometry);

        return equal(name, geom, matchAction);
    }

    @Override
    public Equals equal(Expression geometry1, Expression geometry2) {
        return new EqualsImpl(geometry1, geometry2);
    }

    @Override
    public Equals equal(Expression geometry1, Expression geometry2, MatchAction matchAction) {
        return new EqualsImpl(geometry1, geometry2, matchAction);
    }

    @Override
    public Intersects intersects(String propertyName, Geometry geometry) {
        PropertyName name = property(propertyName);
        Literal geom = literal(geometry);

        return intersects(name, geom);
    }

    @Override
    public Intersects intersects(String propertyName, Geometry geometry, MatchAction matchAction) {
        PropertyName name = property(propertyName);
        Literal geom = literal(geometry);

        return intersects(name, geom, matchAction);
    }

    @Override
    public Intersects intersects(Expression geometry1, Expression geometry2) {
        return new IntersectsImpl(geometry1, geometry2);
    }

    @Override
    public Intersects intersects(Expression geometry1, Expression geometry2, MatchAction matchAction) {
        return new IntersectsImpl(geometry1, geometry2, matchAction);
    }

    @Override
    public Overlaps overlaps(String propertyName, Geometry geometry) {
        PropertyName name = property(propertyName);
        Literal geom = literal(geometry);

        return overlaps(name, geom);
    }

    @Override
    public Overlaps overlaps(String propertyName, Geometry geometry, MatchAction matchAction) {
        PropertyName name = property(propertyName);
        Literal geom = literal(geometry);

        return overlaps(name, geom, matchAction);
    }

    @Override
    public Overlaps overlaps(Expression geometry1, Expression geometry2) {
        return new OverlapsImpl(geometry1, geometry2);
    }

    @Override
    public Overlaps overlaps(Expression geometry1, Expression geometry2, MatchAction matchAction) {
        return new OverlapsImpl(geometry1, geometry2, matchAction);
    }

    @Override
    public Touches touches(String propertyName, Geometry geometry) {
        PropertyName name = property(propertyName);
        Literal geom = literal(geometry);

        return touches(name, geom);
    }

    @Override
    public Touches touches(String propertyName, Geometry geometry, MatchAction matchAction) {
        PropertyName name = property(propertyName);
        Literal geom = literal(geometry);

        return touches(name, geom, matchAction);
    }

    @Override
    public Touches touches(Expression geometry1, Expression geometry2) {
        return new TouchesImpl(geometry1, geometry2);
    }

    @Override
    public Touches touches(Expression geometry1, Expression geometry2, MatchAction matchAction) {
        return new TouchesImpl(geometry1, geometry2, matchAction);
    }

    @Override
    public Within within(String propertyName, Geometry geometry) {
        PropertyName name = property(propertyName);
        Literal geom = literal(geometry);

        return within(name, geom);
    }

    @Override
    public Within within(String propertyName, Geometry geometry, MatchAction matchAction) {
        PropertyName name = property(propertyName);
        Literal geom = literal(geometry);

        return within(name, geom, matchAction);
    }

    @Override
    public Within within(Expression geometry1, Expression geometry2) {
        return new WithinImpl(geometry1, geometry2);
    }

    @Override
    public Within within(Expression geometry1, Expression geometry2, MatchAction matchAction) {
        return new WithinImpl(geometry1, geometry2, matchAction);
    }

    @Override
    public Add add(Expression expr1, Expression expr2) {
        return new AddImpl(expr1, expr2);
    }

    @Override
    public Divide divide(Expression expr1, Expression expr2) {
        return new DivideImpl(expr1, expr2);
    }

    @Override
    public Multiply multiply(Expression expr1, Expression expr2) {
        return new MultiplyImpl(expr1, expr2);
    }

    @Override
    public Subtract subtract(Expression expr1, Expression expr2) {
        return new SubtractImpl(expr1, expr2);
    }

    @Override
    public Function function(String name, Expression... args) {
        Function function = functionFinder.findFunction(name, Arrays.asList(args));
        return function;
    }

    @Override
    public Function function(Name name, Expression... args) {
        Function function = functionFinder.findFunction(name, Arrays.asList(args));
        return function;
    }

    public Function function(String name, Expression arg1) {
        Function function = functionFinder.findFunction(name, Arrays.asList(new Expression[] {arg1}));
        return function;
    }

    public Function function(String name, Expression arg1, Expression arg2) {
        Function function = functionFinder.findFunction(name, Arrays.asList(new Expression[] {arg1, arg2}));
        return function;
    }

    public Function function(String name, Expression arg1, Expression arg2, Expression arg3) {
        Function function = functionFinder.findFunction(name, Arrays.asList(new Expression[] {arg1, arg2, arg3}));

        return function;
    }

    @Override
    public Literal literal(Object obj) {
        try {
            return new LiteralExpressionImpl(obj);
        } catch (IllegalFilterException e) {
            new IllegalArgumentException().initCause(e);
        }

        return null;
    }

    @Override
    public Literal literal(byte b) {
        return new LiteralExpressionImpl(b);
    }

    @Override
    public Literal literal(short s) {
        return new LiteralExpressionImpl(s);
    }

    @Override
    public Literal literal(int i) {
        return new LiteralExpressionImpl(i);
    }

    @Override
    public Literal literal(long l) {
        return new LiteralExpressionImpl(l);
    }

    @Override
    public Literal literal(float f) {
        return new LiteralExpressionImpl(f);
    }

    @Override
    public Literal literal(double d) {
        return new LiteralExpressionImpl(d);
    }

    @Override
    public Literal literal(char c) {
        return new LiteralExpressionImpl(c);
    }

    @Override
    public Literal literal(boolean b) {
        return b ? new LiteralExpressionImpl(Boolean.TRUE) : new LiteralExpressionImpl(Boolean.FALSE);
    }

    @Override
    public Map<java.awt.RenderingHints.Key, ?> getImplementationHints() {
        return Collections.emptyMap();
    }

    @Override
    public SortBy sort(String propertyName, SortOrder order) {
        return new SortByImpl(property(propertyName), order);
    }

    @Override
    public After after(Expression expr1, Expression expr2) {
        return new AfterImpl(expr1, expr2);
    }

    @Override
    public After after(Expression expr1, Expression expr2, MatchAction matchAction) {
        return new AfterImpl(expr1, expr2, matchAction);
    }

    @Override
    public AnyInteracts anyInteracts(Expression expr1, Expression expr2) {
        return new AnyInteractsImpl(expr1, expr2);
    }

    @Override
    public AnyInteracts anyInteracts(Expression expr1, Expression expr2, MatchAction matchAction) {
        return new AnyInteractsImpl(expr1, expr2, matchAction);
    }

    @Override
    public Before before(Expression expr1, Expression expr2) {
        return new BeforeImpl(expr1, expr2);
    }

    @Override
    public Before before(Expression expr1, Expression expr2, MatchAction matchAction) {
        return new BeforeImpl(expr1, expr2, matchAction);
    }

    @Override
    public Begins begins(Expression expr1, Expression expr2) {
        return new BeginsImpl(expr1, expr2);
    }

    @Override
    public Begins begins(Expression expr1, Expression expr2, MatchAction matchAction) {
        return new BeginsImpl(expr1, expr2, matchAction);
    }

    @Override
    public BegunBy begunBy(Expression expr1, Expression expr2) {
        return new BegunByImpl(expr1, expr2);
    }

    @Override
    public BegunBy begunBy(Expression expr1, Expression expr2, MatchAction matchAction) {
        return new BegunByImpl(expr1, expr2, matchAction);
    }

    @Override
    public During during(Expression expr1, Expression expr2) {
        return new DuringImpl(expr1, expr2);
    }

    @Override
    public During during(Expression expr1, Expression expr2, MatchAction matchAction) {
        return new DuringImpl(expr1, expr2, matchAction);
    }

    @Override
    public EndedBy endedBy(Expression expr1, Expression expr2) {
        return new EndedByImpl(expr1, expr2);
    }

    @Override
    public EndedBy endedBy(Expression expr1, Expression expr2, MatchAction matchAction) {
        return new EndedByImpl(expr1, expr2, matchAction);
    }

    @Override
    public Ends ends(Expression expr1, Expression expr2) {
        return new EndsImpl(expr1, expr2);
    }

    @Override
    public Ends ends(Expression expr1, Expression expr2, MatchAction matchAction) {
        return new EndsImpl(expr1, expr2, matchAction);
    }

    @Override
    public Meets meets(Expression expr1, Expression expr2) {
        return new MeetsImpl(expr1, expr2);
    }

    @Override
    public Meets meets(Expression expr1, Expression expr2, MatchAction matchAction) {
        return new MeetsImpl(expr1, expr2, matchAction);
    }

    @Override
    public MetBy metBy(Expression expr1, Expression expr2) {
        return new MetByImpl(expr1, expr2);
    }

    @Override
    public MetBy metBy(Expression expr1, Expression expr2, MatchAction matchAction) {
        return new MetByImpl(expr1, expr2, matchAction);
    }

    @Override
    public OverlappedBy overlappedBy(Expression expr1, Expression expr2) {
        return new OverlappedByImpl(expr1, expr2);
    }

    @Override
    public OverlappedBy overlappedBy(Expression expr1, Expression expr2, MatchAction matchAction) {
        return new OverlappedByImpl(expr1, expr2, matchAction);
    }

    @Override
    public TContains tcontains(Expression expr1, Expression expr2) {
        return new TContainsImpl(expr1, expr2);
    }

    @Override
    public TContains tcontains(Expression expr1, Expression expr2, MatchAction matchAction) {
        return new TContainsImpl(expr1, expr2, matchAction);
    }

    @Override
    public TEquals tequals(Expression expr1, Expression expr2) {
        return new TEqualsImpl(expr1, expr2);
    }

    @Override
    public TEquals tequals(Expression expr1, Expression expr2, MatchAction matchAction) {
        return new TEqualsImpl(expr1, expr2, matchAction);
    }

    @Override
    public TOverlaps toverlaps(Expression expr1, Expression expr2) {
        return new TOverlapsImpl(expr1, expr2);
    }

    @Override
    public TOverlaps toverlaps(Expression expr1, Expression expr2, MatchAction matchAction) {
        return new TOverlapsImpl(expr1, expr2, matchAction);
    }

    //    public org.geotools.filter.Filter and( org.geotools.filter.Filter filter1,
    // org.geotools.filter.Filter filter2 ) {
    //        return (org.geotools.filter.Filter) and( (Filter) filter1, (Filter) filter2 );
    //    }
    //
    //    public org.geotools.filter.Filter not( org.geotools.filter.Filter filter ) {
    //        return (org.geotools.filter.Filter) not( (Filter) filter );
    //    }
    //
    //    public org.geotools.filter.Filter or( org.geotools.filter.Filter filter1,
    // org.geotools.filter.Filter filter2 ) {
    //        return (org.geotools.filter.Filter) or( (Filter) filter1, (Filter) filter2 );
    //    }

    public Beyond beyond(Expression geometry1, Geometry geometry2, double distance, String units) {
        return beyond(geometry1, literal(geometry2), distance, units);
    }

    @Override
    public PropertyName property(Name name) {
        return new AttributeExpressionImpl(name);
    }

    @Override
    public PropertyName property(String name, NamespaceSupport namespaceContext) {
        if (namespaceContext == null) {
            return property(name);
        }
        return new AttributeExpressionImpl(name, namespaceContext);
    }

    public Within within(Expression geometry1, Geometry geometry2) {
        return within(geometry1, literal(geometry2));
    }

    @Override
    public Operator operator(String name) {
        return new OperatorImpl(name);
    }

    @Override
    public SpatialOperator spatialOperator(String name, GeometryOperand... geometryOperands) {
        return new SpatialOperatorImpl(name, geometryOperands);
    }

    @Override
    public TemporalOperator temporalOperator(String name) {
        return new TemporalOperatorImpl(name);
    }

    @Override
    public <T> Parameter<T> parameter(
            String name,
            Class<T> type,
            InternationalString title,
            InternationalString description,
            boolean required,
            int minOccurs,
            int maxOccurs,
            T defaultValue) {
        return new org.geotools.api.data.Parameter<>(
                name, type, title, description, required, minOccurs, maxOccurs, defaultValue, null);
    }

    @Override
    public FunctionName functionName(String name, int nargs) {
        return new FunctionNameImpl(name, nargs);
    }

    @Override
    public FunctionName functionName(Name name, int nargs) {
        return new FunctionNameImpl(name, nargs);
    }

    @Override
    public FunctionName functionName(String name, int nargs, List<String> argNames) {
        return new FunctionNameImpl(name, nargs, argNames);
    }

    @Override
    public FunctionName functionName(Name name, int nargs, List<String> argNames) {
        return new FunctionNameImpl(name, nargs, argNames);
    }

    @Override
    public FunctionName functionName(String name, List<Parameter<?>> args, Parameter<?> ret) {
        return new FunctionNameImpl(name, ret, args);
    }

    @Override
    public FunctionName functionName(Name name, List<Parameter<?>> args, Parameter<?> ret) {
        return new FunctionNameImpl(name, ret, args);
    }

    @Override
    public Functions functions(FunctionName... functionNames) {
        return new FunctionsImpl(functionNames);
    }

    @Override
    public SpatialOperators spatialOperators(SpatialOperator... spatialOperators) {
        return new SpatialOperatorsImpl(spatialOperators);
    }

    @Override
    public ArithmeticOperators arithmeticOperators(boolean simple, Functions functions) {
        return new ArithmeticOperatorsImpl(simple, functions);
    }

    @Override
    public ComparisonOperators comparisonOperators(Operator... comparisonOperators) {
        return new ComparisonOperatorsImpl(comparisonOperators);
    }

    @Override
    public FilterCapabilities capabilities(
            String version, ScalarCapabilities scalar, SpatialCapabilities spatial, IdCapabilities id) {
        return new FilterCapabilitiesImpl(version, scalar, spatial, id);
    }

    @Override
    public FilterCapabilities capabilities(
            String version,
            ScalarCapabilities scalar,
            SpatialCapabilities spatial,
            IdCapabilities id,
            TemporalCapabilities temporal) {
        return new FilterCapabilitiesImpl(version, scalar, spatial, id, temporal);
    }

    @Override
    public ScalarCapabilities scalarCapabilities(
            ComparisonOperators comparison, ArithmeticOperators arithmetic, boolean logicalOperators) {
        return new ScalarCapabilitiesImpl(comparison, arithmetic, logicalOperators);
    }

    @Override
    public SpatialCapabilities spatialCapabilities(GeometryOperand[] geometryOperands, SpatialOperators spatial) {
        return new SpatialCapabiltiesImpl(geometryOperands, spatial);
    }

    @Override
    public IdCapabilities idCapabilities(boolean eid, boolean fid) {
        return new IdCapabilitiesImpl(eid, fid);
    }

    @Override
    public TemporalCapabilities temporalCapabilities(TemporalOperator... temporalOperators) {
        return new TemporalCapabilitiesImpl(Arrays.asList(temporalOperators));
    }

    @Override
    public NativeFilter nativeFilter(String nativeFilter) {
        return new NativeFilterImpl(nativeFilter);
    }
}
