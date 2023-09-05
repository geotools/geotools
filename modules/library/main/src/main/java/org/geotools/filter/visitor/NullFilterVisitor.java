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

import org.geotools.api.filter.And;
import org.geotools.api.filter.ExcludeFilter;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterVisitor;
import org.geotools.api.filter.Id;
import org.geotools.api.filter.IncludeFilter;
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
import org.geotools.api.filter.expression.Add;
import org.geotools.api.filter.expression.Divide;
import org.geotools.api.filter.expression.ExpressionVisitor;
import org.geotools.api.filter.expression.Function;
import org.geotools.api.filter.expression.Literal;
import org.geotools.api.filter.expression.Multiply;
import org.geotools.api.filter.expression.NilExpression;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.api.filter.expression.Subtract;
import org.geotools.api.filter.spatial.BBOX;
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

/**
 * Abstract implementation of FilterVisitor simple returns the provided data.
 *
 * <p>This class can be used as is as a placeholder that does nothing:
 *
 * <pre><code>
 * Integer one = (Integer) filter.accepts( NullFilterVisitor.NULL_VISITOR, 1 );
 * </code></pre>
 *
 * The class can also be used as an alternative to DefaultFilterVisitor if you want to only walk
 * part of the data structure:
 *
 * <pre><code>
 * FilterVisitor allFids = new NullFilterVisitor(){
 *     public Object visit( Id filter, Object data ) {
 *         if( data == null) return null;
 *         Set set = (Set) data;
 *         set.addAll(filter.getIDs());
 *         return set;
 *     }
 * };
 * Set set = (Set) myFilter.accept(allFids, new HashSet());
 * Set set2 = (Set) myFilter.accept(allFids, null ); // set2 will be null
 * </code></pre>
 *
 * The base class provides implementations for:
 *
 * <ul>
 *   <li>walking And, Or, and Not data structures, returning null at any point will exit early
 *   <li>a default implementation for every other construct that will return the provided data
 * </ul>
 *
 * @author Jody Garnett (Refractions Research)
 */
public abstract class NullFilterVisitor implements FilterVisitor, ExpressionVisitor {
    public static NullFilterVisitor NULL_VISITOR =
            new NullFilterVisitor() {
                @Override
                public Object visit(And filter, Object data) {
                    return data;
                }

                @Override
                public Object visit(Or filter, Object data) {
                    return data;
                }

                @Override
                public Object visit(Not filter, Object data) {
                    return data;
                }
            };

    public NullFilterVisitor() {}

    @Override
    public Object visit(ExcludeFilter filter, Object data) {
        return data;
    }

    @Override
    public Object visit(IncludeFilter filter, Object data) {
        return data;
    }

    @Override
    public Object visit(And filter, Object data) {
        if (data == null) return null;
        if (filter.getChildren() != null) {
            for (Filter child : filter.getChildren()) {
                data = child.accept(this, data);
                if (data == null) return null;
            }
        }
        return data;
    }

    @Override
    public Object visit(Id filter, Object data) {
        return data;
    }

    @Override
    public Object visit(Not filter, Object data) {
        if (data == null) return data;

        Filter child = filter.getFilter();
        if (child != null) {
            data = child.accept(this, data);
        }
        return data;
    }

    @Override
    public Object visit(Or filter, Object data) {
        if (data == null) return null;
        if (filter.getChildren() != null) {
            for (Filter child : filter.getChildren()) {
                data = child.accept(this, data);
                if (data == null) return null;
            }
        }
        return data;
    }

    @Override
    public Object visit(PropertyIsBetween filter, Object data) {
        return data;
    }

    @Override
    public Object visit(PropertyIsEqualTo filter, Object data) {
        return data;
    }

    @Override
    public Object visit(PropertyIsNotEqualTo filter, Object data) {
        return data;
    }

    @Override
    public Object visit(PropertyIsGreaterThan filter, Object data) {
        return data;
    }

    @Override
    public Object visit(PropertyIsGreaterThanOrEqualTo filter, Object data) {
        return data;
    }

    @Override
    public Object visit(PropertyIsLessThan filter, Object data) {
        return data;
    }

    @Override
    public Object visit(PropertyIsLessThanOrEqualTo filter, Object data) {
        return data;
    }

    @Override
    public Object visit(PropertyIsLike filter, Object data) {
        return data;
    }

    @Override
    public Object visit(PropertyIsNull filter, Object data) {
        return data;
    }

    @Override
    public Object visit(PropertyIsNil filter, Object data) {
        return data;
    }

    @Override
    public Object visit(final BBOX filter, Object data) {
        return data;
    }

    @Override
    public Object visit(Beyond filter, Object data) {
        return data;
    }

    @Override
    public Object visit(Contains filter, Object data) {
        return data;
    }

    @Override
    public Object visit(Crosses filter, Object data) {
        return data;
    }

    @Override
    public Object visit(Disjoint filter, Object data) {
        return data;
    }

    @Override
    public Object visit(DWithin filter, Object data) {
        return data;
    }

    @Override
    public Object visit(Equals filter, Object data) {
        return data;
    }

    @Override
    public Object visit(Intersects filter, Object data) {
        return data;
    }

    @Override
    public Object visit(Overlaps filter, Object data) {
        return data;
    }

    @Override
    public Object visit(Touches filter, Object data) {
        return data;
    }

    @Override
    public Object visit(Within filter, Object data) {
        return data;
    }

    @Override
    public Object visitNullFilter(Object data) {
        return data;
    }

    @Override
    public Object visit(NilExpression expression, Object data) {
        return null;
    }

    @Override
    public Object visit(Add expression, Object data) {
        return data;
    }

    @Override
    public Object visit(Divide expression, Object data) {
        return data;
    }

    @Override
    public Object visit(Function expression, Object data) {
        return data;
    }

    @Override
    public Object visit(Literal expression, Object data) {
        return data;
    }

    @Override
    public Object visit(Multiply expression, Object data) {
        return data;
    }

    @Override
    public Object visit(PropertyName expression, Object data) {
        return data;
    }

    @Override
    public Object visit(Subtract expression, Object data) {
        return data;
    }

    @Override
    public Object visit(After after, Object data) {
        return data;
    }

    @Override
    public Object visit(AnyInteracts anyInteracts, Object data) {
        return data;
    }

    @Override
    public Object visit(Before before, Object data) {
        return data;
    }

    @Override
    public Object visit(Begins begins, Object data) {
        return data;
    }

    @Override
    public Object visit(BegunBy begunBy, Object data) {
        return data;
    }

    @Override
    public Object visit(During during, Object data) {
        return data;
    }

    @Override
    public Object visit(EndedBy endedBy, Object data) {
        return data;
    }

    @Override
    public Object visit(Ends ends, Object data) {
        return data;
    }

    @Override
    public Object visit(Meets meets, Object data) {
        return data;
    }

    @Override
    public Object visit(MetBy metBy, Object data) {
        return data;
    }

    @Override
    public Object visit(OverlappedBy overlappedBy, Object data) {
        return data;
    }

    @Override
    public Object visit(TContains contains, Object data) {
        return data;
    }

    @Override
    public Object visit(TEquals equals, Object data) {
        return data;
    }

    @Override
    public Object visit(TOverlaps contains, Object data) {
        return data;
    }

    @Override
    public Object visit(NativeFilter nativeFilter, Object data) {
        return data;
    }
}
