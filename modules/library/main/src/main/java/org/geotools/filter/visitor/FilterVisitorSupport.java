/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.visitor;

import org.geotools.api.filter.And;
import org.geotools.api.filter.BinaryComparisonOperator;
import org.geotools.api.filter.BinaryLogicOperator;
import org.geotools.api.filter.FilterVisitor;
import org.geotools.api.filter.Or;
import org.geotools.api.filter.PropertyIsEqualTo;
import org.geotools.api.filter.PropertyIsGreaterThan;
import org.geotools.api.filter.PropertyIsGreaterThanOrEqualTo;
import org.geotools.api.filter.PropertyIsLessThan;
import org.geotools.api.filter.PropertyIsLessThanOrEqualTo;
import org.geotools.api.filter.PropertyIsNotEqualTo;
import org.geotools.api.filter.spatial.BBOX;
import org.geotools.api.filter.spatial.Beyond;
import org.geotools.api.filter.spatial.BinarySpatialOperator;
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
import org.geotools.api.filter.temporal.BinaryTemporalOperator;
import org.geotools.api.filter.temporal.During;
import org.geotools.api.filter.temporal.EndedBy;
import org.geotools.api.filter.temporal.Ends;
import org.geotools.api.filter.temporal.Meets;
import org.geotools.api.filter.temporal.MetBy;
import org.geotools.api.filter.temporal.OverlappedBy;
import org.geotools.api.filter.temporal.TContains;
import org.geotools.api.filter.temporal.TEquals;
import org.geotools.api.filter.temporal.TOverlaps;

/**
 * Base filter visitor class that aggregates the individual visit methods based on filter
 * hierarchy.
 * <p>
 * Methods are grouped by the {@link BinaryLogicOperator}, {@link BinaryComparisonOperator},
 *  {@link BinarySpatialOperator}, and {@link BinaryTemporalOperator} and subclasses should
 *  implement:
 *  <ul>
 *    <li>{@link #visit(BinaryLogicOperator, Object)}
 *    <li>{@link #visit(BinaryComparisonOperator, Object)}
 *    <li>{@link #visit(BinarySpatialOperator, Object)}
 *    <li>{@link #visit(BinaryTemporalOperator, Object)
 *  </ul>
 *  For visitors looking for a base class that simply stubs out the various visit methods should use
 *  {@link DefaultFilterVisitor} instead.
 * </p>
 *
 * @author Justin Deoliveira, OpenGeo
 *
 */
public abstract class FilterVisitorSupport implements FilterVisitor {

    @Override
    public Object visit(And filter, Object extraData) {
        return visit((BinaryLogicOperator) filter, extraData);
    }

    @Override
    public Object visit(Or filter, Object extraData) {
        return visit((BinaryLogicOperator) filter, extraData);
    }

    protected abstract Object visit(BinaryLogicOperator op, Object extraData);

    @Override
    public Object visit(PropertyIsEqualTo filter, Object extraData) {
        return visit((BinaryComparisonOperator) filter, extraData);
    }

    @Override
    public Object visit(PropertyIsNotEqualTo filter, Object extraData) {
        return visit((BinaryComparisonOperator) filter, extraData);
    }

    @Override
    public Object visit(PropertyIsGreaterThan filter, Object extraData) {
        return visit((BinaryComparisonOperator) filter, extraData);
    }

    @Override
    public Object visit(PropertyIsGreaterThanOrEqualTo filter, Object extraData) {
        return visit((BinaryComparisonOperator) filter, extraData);
    }

    @Override
    public Object visit(PropertyIsLessThan filter, Object extraData) {
        return visit((BinaryComparisonOperator) filter, extraData);
    }

    @Override
    public Object visit(PropertyIsLessThanOrEqualTo filter, Object extraData) {
        return visit((BinaryComparisonOperator) filter, extraData);
    }

    protected abstract Object visit(BinaryComparisonOperator op, Object extraData);

    @Override
    public Object visit(BBOX filter, Object extraData) {
        return visit((BinarySpatialOperator) filter, extraData);
    }

    @Override
    public Object visit(Beyond filter, Object extraData) {
        return visit((BinarySpatialOperator) filter, extraData);
    }

    @Override
    public Object visit(Contains filter, Object extraData) {
        return visit((BinarySpatialOperator) filter, extraData);
    }

    @Override
    public Object visit(Crosses filter, Object extraData) {
        return visit((BinarySpatialOperator) filter, extraData);
    }

    @Override
    public Object visit(Disjoint filter, Object extraData) {
        return visit((BinarySpatialOperator) filter, extraData);
    }

    @Override
    public Object visit(DWithin filter, Object extraData) {
        return visit((BinarySpatialOperator) filter, extraData);
    }

    @Override
    public Object visit(Equals filter, Object extraData) {
        return visit((BinarySpatialOperator) filter, extraData);
    }

    @Override
    public Object visit(Intersects filter, Object extraData) {
        return visit((BinarySpatialOperator) filter, extraData);
    }

    @Override
    public Object visit(Overlaps filter, Object extraData) {
        return visit((BinarySpatialOperator) filter, extraData);
    }

    @Override
    public Object visit(Touches filter, Object extraData) {
        return visit((BinarySpatialOperator) filter, extraData);
    }

    @Override
    public Object visit(Within filter, Object extraData) {
        return visit((BinarySpatialOperator) filter, extraData);
    }

    protected abstract Object visit(BinarySpatialOperator op, Object extraData);

    @Override
    public Object visit(After filter, Object extraData) {
        return visit((BinaryTemporalOperator) filter, extraData);
    }

    @Override
    public Object visit(AnyInteracts filter, Object extraData) {
        return visit((BinaryTemporalOperator) filter, extraData);
    }

    @Override
    public Object visit(Before filter, Object extraData) {
        return visit((BinaryTemporalOperator) filter, extraData);
    }

    @Override
    public Object visit(Begins filter, Object extraData) {
        return visit((BinaryTemporalOperator) filter, extraData);
    }

    @Override
    public Object visit(BegunBy filter, Object extraData) {
        return visit((BinaryTemporalOperator) filter, extraData);
    }

    @Override
    public Object visit(During filter, Object extraData) {
        return visit((BinaryTemporalOperator) filter, extraData);
    }

    @Override
    public Object visit(EndedBy filter, Object extraData) {
        return visit((BinaryTemporalOperator) filter, extraData);
    }

    @Override
    public Object visit(Ends filter, Object extraData) {
        return visit((BinaryTemporalOperator) filter, extraData);
    }

    @Override
    public Object visit(Meets filter, Object extraData) {
        return visit((BinaryTemporalOperator) filter, extraData);
    }

    @Override
    public Object visit(MetBy filter, Object extraData) {
        return visit((BinaryTemporalOperator) filter, extraData);
    }

    @Override
    public Object visit(OverlappedBy filter, Object extraData) {
        return visit((BinaryTemporalOperator) filter, extraData);
    }

    @Override
    public Object visit(TContains filter, Object extraData) {
        return visit((BinaryTemporalOperator) filter, extraData);
    }

    @Override
    public Object visit(TEquals filter, Object extraData) {
        return visit((BinaryTemporalOperator) filter, extraData);
    }

    @Override
    public Object visit(TOverlaps filter, Object extraData) {
        return visit((BinaryTemporalOperator) filter, extraData);
    }

    protected abstract Object visit(BinaryTemporalOperator op, Object extraData);
}
