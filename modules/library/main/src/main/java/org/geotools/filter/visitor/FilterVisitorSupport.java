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

import org.opengis.filter.And;
import org.opengis.filter.BinaryComparisonOperator;
import org.opengis.filter.BinaryLogicOperator;
import org.opengis.filter.FilterVisitor;
import org.opengis.filter.Or;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.PropertyIsGreaterThan;
import org.opengis.filter.PropertyIsGreaterThanOrEqualTo;
import org.opengis.filter.PropertyIsLessThan;
import org.opengis.filter.PropertyIsLessThanOrEqualTo;
import org.opengis.filter.PropertyIsNotEqualTo;
import org.opengis.filter.spatial.BBOX;
import org.opengis.filter.spatial.Beyond;
import org.opengis.filter.spatial.BinarySpatialOperator;
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
import org.opengis.filter.temporal.BinaryTemporalOperator;
import org.opengis.filter.temporal.During;
import org.opengis.filter.temporal.EndedBy;
import org.opengis.filter.temporal.Ends;
import org.opengis.filter.temporal.Meets;
import org.opengis.filter.temporal.MetBy;
import org.opengis.filter.temporal.OverlappedBy;
import org.opengis.filter.temporal.TContains;
import org.opengis.filter.temporal.TEquals;
import org.opengis.filter.temporal.TOverlaps;

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

    public Object visit(And filter, Object extraData) {
        return visit((BinaryLogicOperator)filter, extraData);
    }

    public Object visit(Or filter, Object extraData) {
        return visit((BinaryLogicOperator)filter, extraData);
    }

    protected abstract Object visit(BinaryLogicOperator op, Object extraData);

    public Object visit(PropertyIsEqualTo filter, Object extraData) {
        return visit((BinaryComparisonOperator)filter, extraData);
    }

    public Object visit(PropertyIsNotEqualTo filter, Object extraData) {
        return visit((BinaryComparisonOperator)filter, extraData);
    }

    public Object visit(PropertyIsGreaterThan filter, Object extraData) {
        return visit((BinaryComparisonOperator)filter, extraData);
    }

    public Object visit(PropertyIsGreaterThanOrEqualTo filter, Object extraData) {
        return visit((BinaryComparisonOperator)filter, extraData);
    }

    public Object visit(PropertyIsLessThan filter, Object extraData) {
        return visit((BinaryComparisonOperator)filter, extraData);
    }

    public Object visit(PropertyIsLessThanOrEqualTo filter, Object extraData) {
        return visit((BinaryComparisonOperator)filter, extraData);
    }

    protected abstract Object visit(BinaryComparisonOperator op, Object extraData);

    public Object visit(BBOX filter, Object extraData) {
        return visit((BinarySpatialOperator)filter, extraData);
    }

    public Object visit(Beyond filter, Object extraData) {
        return visit((BinarySpatialOperator)filter, extraData);
    }

    public Object visit(Contains filter, Object extraData) {
        return visit((BinarySpatialOperator)filter, extraData);
    }

    public Object visit(Crosses filter, Object extraData) {
        return visit((BinarySpatialOperator)filter, extraData);
    }

    public Object visit(Disjoint filter, Object extraData) {
        return visit((BinarySpatialOperator)filter, extraData);
    }

    public Object visit(DWithin filter, Object extraData) {
        return visit((BinarySpatialOperator)filter, extraData);
    }

    public Object visit(Equals filter, Object extraData) {
        return visit((BinarySpatialOperator)filter, extraData);
    }

    public Object visit(Intersects filter, Object extraData) {
        return visit((BinarySpatialOperator)filter, extraData);
    }

    public Object visit(Overlaps filter, Object extraData) {
        return visit((BinarySpatialOperator)filter, extraData);
    }

    public Object visit(Touches filter, Object extraData) {
        return visit((BinarySpatialOperator)filter, extraData);
    }

    public Object visit(Within filter, Object extraData) {
        return visit((BinarySpatialOperator)filter, extraData);
    }

    protected abstract Object visit(BinarySpatialOperator op, Object extraData);
    
    public Object visit(After filter, Object extraData) {
        return visit((BinaryTemporalOperator)filter, extraData);
    }

    public Object visit(AnyInteracts filter, Object extraData) {
        return visit((BinaryTemporalOperator)filter, extraData);
    }

    public Object visit(Before filter, Object extraData) {
        return visit((BinaryTemporalOperator)filter, extraData);
    }

    public Object visit(Begins filter, Object extraData) {
        return visit((BinaryTemporalOperator)filter, extraData);
    }

    public Object visit(BegunBy filter, Object extraData) {
        return visit((BinaryTemporalOperator)filter, extraData);
    }

    public Object visit(During filter, Object extraData) {
        return visit((BinaryTemporalOperator)filter, extraData);
    }

    public Object visit(EndedBy filter, Object extraData) {
        return visit((BinaryTemporalOperator)filter, extraData);
    }

    public Object visit(Ends filter, Object extraData) {
        return visit((BinaryTemporalOperator)filter, extraData);
    }

    public Object visit(Meets filter, Object extraData) {
        return visit((BinaryTemporalOperator)filter, extraData);
    }

    public Object visit(MetBy filter, Object extraData) {
        return visit((BinaryTemporalOperator)filter, extraData);
    }

    public Object visit(OverlappedBy filter, Object extraData) {
        return visit((BinaryTemporalOperator)filter, extraData);
    }

    public Object visit(TContains filter, Object extraData) {
        return visit((BinaryTemporalOperator)filter, extraData);
    }

    public Object visit(TEquals filter, Object extraData) {
        return visit((BinaryTemporalOperator)filter, extraData);
    }

    public Object visit(TOverlaps filter, Object extraData) {
        return visit((BinaryTemporalOperator)filter, extraData);
    }

    protected abstract Object visit(BinaryTemporalOperator op, Object extraData);
}
