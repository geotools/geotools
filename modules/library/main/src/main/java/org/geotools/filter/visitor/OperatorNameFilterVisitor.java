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
package org.geotools.filter.visitor;

import org.opengis.filter.And;
import org.opengis.filter.ExcludeFilter;
import org.opengis.filter.FilterVisitor;
import org.opengis.filter.Id;
import org.opengis.filter.IncludeFilter;
import org.opengis.filter.NativeFilter;
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

/**
 * A FilterVisitor responsible for providing the capabilities name used to identify the provided
 * filter.
 *
 * <p>These names will match those used by FilterCapabilities Operations and may be used to verify
 * that the provided filter is supported.
 *
 * <p>
 *
 * @author Jody Garnett
 */
public class OperatorNameFilterVisitor implements FilterVisitor {
    @Override
    public Object visit(ExcludeFilter filter, Object extraData) {
        return "Exclude";
    }

    @Override
    public Object visit(IncludeFilter filter, Object extraData) {
        return "Include";
    }

    @Override
    public Object visit(And filter, Object extraData) {
        return "And";
    }

    @Override
    public Object visit(Id filter, Object extraData) {
        return "Id";
    }

    @Override
    public Object visit(Not filter, Object extraData) {
        return "Not";
    }

    @Override
    public Object visit(Or filter, Object extraData) {
        return "Or";
    }

    @Override
    public Object visit(PropertyIsBetween filter, Object extraData) {
        return PropertyIsBetween.NAME;
    }

    @Override
    public Object visit(PropertyIsEqualTo filter, Object extraData) {
        return PropertyIsEqualTo.NAME;
    }

    @Override
    public Object visit(PropertyIsNotEqualTo filter, Object extraData) {
        return PropertyIsNotEqualTo.NAME;
    }

    @Override
    public Object visit(PropertyIsGreaterThan filter, Object extraData) {
        return PropertyIsGreaterThan.NAME;
    }

    @Override
    public Object visit(PropertyIsGreaterThanOrEqualTo filter, Object extraData) {
        return PropertyIsGreaterThanOrEqualTo.NAME;
    }

    @Override
    public Object visit(PropertyIsLessThan filter, Object extraData) {
        return PropertyIsLessThan.NAME;
    }

    @Override
    public Object visit(PropertyIsLessThanOrEqualTo filter, Object extraData) {
        return PropertyIsLessThanOrEqualTo.NAME;
    }

    @Override
    public Object visit(PropertyIsLike filter, Object extraData) {
        return PropertyIsLike.NAME;
    }

    @Override
    public Object visit(PropertyIsNull filter, Object extraData) {
        return PropertyIsLike.NAME;
    }

    @Override
    public Object visit(PropertyIsNil filter, Object extraData) {
        return PropertyIsNil.NAME;
    }

    @Override
    public Object visit(BBOX filter, Object extraData) {
        return BBOX.NAME;
    }

    @Override
    public Object visit(Beyond filter, Object extraData) {
        return Beyond.NAME;
    }

    @Override
    public Object visit(Contains filter, Object extraData) {
        return Contains.NAME;
    }

    @Override
    public Object visit(Crosses filter, Object extraData) {
        return Crosses.NAME;
    }

    @Override
    public Object visit(Disjoint filter, Object extraData) {
        return Disjoint.NAME;
    }

    @Override
    public Object visit(DWithin filter, Object extraData) {
        return DWithin.NAME;
    }

    @Override
    public Object visit(Equals filter, Object extraData) {
        return Equals.NAME;
    }

    @Override
    public Object visit(Intersects filter, Object extraData) {
        return Intersects.NAME;
    }

    @Override
    public Object visit(Overlaps filter, Object extraData) {
        return Overlaps.NAME;
    }

    @Override
    public Object visit(Touches filter, Object extraData) {
        return Touches.NAME;
    }

    @Override
    public Object visit(Within filter, Object extraData) {
        return Within.NAME;
    }

    @Override
    public Object visitNullFilter(Object extraData) {
        return "null";
    }

    // temporal
    @Override
    public Object visit(After after, Object extraData) {
        return After.NAME;
    }

    @Override
    public Object visit(AnyInteracts anyInteracts, Object extraData) {
        return AnyInteracts.NAME;
    }

    @Override
    public Object visit(Before before, Object extraData) {
        return Before.NAME;
    }

    @Override
    public Object visit(Begins begins, Object extraData) {
        return Begins.NAME;
    }

    @Override
    public Object visit(BegunBy begunBy, Object extraData) {
        return BegunBy.NAME;
    }

    @Override
    public Object visit(During during, Object extraData) {
        return During.NAME;
    }

    @Override
    public Object visit(EndedBy endedBy, Object extraData) {
        return EndedBy.NAME;
    }

    @Override
    public Object visit(Ends ends, Object extraData) {
        return Ends.NAME;
    }

    @Override
    public Object visit(Meets meets, Object extraData) {
        return Meets.NAME;
    }

    @Override
    public Object visit(MetBy metBy, Object extraData) {
        return MetBy.NAME;
    }

    @Override
    public Object visit(OverlappedBy overlappedBy, Object extraData) {
        return OverlappedBy.NAME;
    }

    @Override
    public Object visit(TContains contains, Object extraData) {
        return TContains.NAME;
    }

    @Override
    public Object visit(TEquals equals, Object extraData) {
        return TEquals.NAME;
    }

    @Override
    public Object visit(TOverlaps contains, Object extraData) {
        return TOverlaps.NAME;
    }

    @Override
    public Object visit(NativeFilter nativeFilter, Object extraData) {
        return NativeFilter.NAME;
    }
}
