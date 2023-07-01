/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.lite;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import org.geotools.filter.visitor.DefaultFilterVisitor;
import org.geotools.api.filter.And;
import org.geotools.api.filter.Id;
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
 * Collects and returns expressions that do repeat in the given filter/express, used to locate which
 * parts of a filter tree to memoize, leaving the rest alone. see also {@link MemoryFilterOptimizer}
 * and {@link FilterMemoizer}
 */
class RepeatedFilterVisitor extends DefaultFilterVisitor {

    Map<Object, AtomicInteger> objectCounter = new HashMap<>();

    private void collect(Object object) {
        AtomicInteger counter = objectCounter.get(object);
        if (counter == null) {
            counter = new AtomicInteger(1);
            objectCounter.put(object, counter);
        } else {
            counter.incrementAndGet();
        }
    }

    public Set<Object> getRepeatedObjects() {
        return objectCounter.entrySet().stream()
                .filter(e -> e.getValue().get() > 1)
                .map(e -> e.getKey())
                .collect(Collectors.toSet());
    }

    @Override
    public Object visit(And filter, Object data) {
        collect(filter);
        return super.visit(filter, data);
    }

    @Override
    public Object visit(Id filter, Object data) {
        collect(filter);
        return super.visit(filter, data);
    }

    @Override
    public Object visit(Not filter, Object data) {
        collect(filter);
        return super.visit(filter, data);
    }

    @Override
    public Object visit(Or filter, Object data) {
        collect(filter);
        return super.visit(filter, data);
    }

    @Override
    public Object visit(PropertyIsBetween filter, Object data) {
        collect(filter);
        return super.visit(filter, data);
    }

    @Override
    public Object visit(PropertyIsEqualTo filter, Object data) {
        collect(filter);
        return super.visit(filter, data);
    }

    @Override
    public Object visit(PropertyIsNotEqualTo filter, Object data) {
        collect(filter);
        return super.visit(filter, data);
    }

    @Override
    public Object visit(PropertyIsGreaterThan filter, Object data) {
        collect(filter);
        return super.visit(filter, data);
    }

    @Override
    public Object visit(PropertyIsGreaterThanOrEqualTo filter, Object data) {
        collect(filter);
        return super.visit(filter, data);
    }

    @Override
    public Object visit(PropertyIsLessThan filter, Object data) {
        collect(filter);
        return super.visit(filter, data);
    }

    @Override
    public Object visit(PropertyIsLessThanOrEqualTo filter, Object data) {
        collect(filter);
        return super.visit(filter, data);
    }

    @Override
    public Object visit(PropertyIsLike filter, Object data) {
        collect(filter);
        return super.visit(filter, data);
    }

    @Override
    public Object visit(PropertyIsNull filter, Object data) {
        collect(filter);
        return super.visit(filter, data);
    }

    @Override
    public Object visit(PropertyIsNil filter, Object data) {
        collect(filter);
        return super.visit(filter, data);
    }

    @Override
    public Object visit(BBOX filter, Object data) {
        collect(filter);
        return super.visit(filter, data);
    }

    @Override
    public Object visit(Beyond filter, Object data) {
        collect(filter);
        return super.visit(filter, data);
    }

    @Override
    public Object visit(Contains filter, Object data) {
        collect(filter);
        return super.visit(filter, data);
    }

    @Override
    public Object visit(Crosses filter, Object data) {
        collect(filter);
        return super.visit(filter, data);
    }

    @Override
    public Object visit(Disjoint filter, Object data) {
        collect(filter);
        return super.visit(filter, data);
    }

    @Override
    public Object visit(DWithin filter, Object data) {
        collect(filter);
        return super.visit(filter, data);
    }

    @Override
    public Object visit(Equals filter, Object data) {
        collect(filter);
        return super.visit(filter, data);
    }

    @Override
    public Object visit(Intersects filter, Object data) {
        collect(filter);
        return super.visit(filter, data);
    }

    @Override
    public Object visit(Overlaps filter, Object data) {
        collect(filter);
        return super.visit(filter, data);
    }

    @Override
    public Object visit(Touches filter, Object data) {
        collect(filter);
        return super.visit(filter, data);
    }

    @Override
    public Object visit(Within filter, Object data) {
        collect(filter);
        return super.visit(filter, data);
    }

    @Override
    public Object visitNullFilter(Object data) {
        collect(data);
        return super.visitNullFilter(data);
    }

    @Override
    public Object visit(NilExpression expression, Object data) {
        collect(expression);
        return super.visit(expression, data);
    }

    @Override
    public Object visit(Add expression, Object data) {
        collect(expression);
        return super.visit(expression, data);
    }

    @Override
    public Object visit(Divide expression, Object data) {
        collect(expression);
        return super.visit(expression, data);
    }

    @Override
    public Object visit(Function expression, Object data) {
        collect(expression);
        return super.visit(expression, data);
    }

    @Override
    public Object visit(Literal expression, Object data) {
        collect(expression);
        return super.visit(expression, data);
    }

    @Override
    public Object visit(Multiply expression, Object data) {
        collect(expression);
        return super.visit(expression, data);
    }

    @Override
    public Object visit(PropertyName expression, Object data) {
        collect(expression);
        return super.visit(expression, data);
    }

    @Override
    public Object visit(Subtract expression, Object data) {
        collect(expression);
        return super.visit(expression, data);
    }

    @Override
    public Object visit(After after, Object data) {
        collect(after);
        return super.visit(after, data);
    }

    @Override
    public Object visit(AnyInteracts anyInteracts, Object data) {
        collect(anyInteracts);
        return super.visit(anyInteracts, data);
    }

    @Override
    public Object visit(Before before, Object data) {
        collect(before);
        return super.visit(before, data);
    }

    @Override
    public Object visit(Begins begins, Object data) {
        collect(begins);
        return super.visit(begins, data);
    }

    @Override
    public Object visit(BegunBy begunBy, Object data) {
        collect(begunBy);
        return super.visit(begunBy, data);
    }

    @Override
    public Object visit(During during, Object data) {
        collect(during);
        return super.visit(during, data);
    }

    @Override
    public Object visit(EndedBy endedBy, Object data) {
        collect(endedBy);
        return super.visit(endedBy, data);
    }

    @Override
    public Object visit(Ends ends, Object data) {
        collect(ends);
        return super.visit(ends, data);
    }

    @Override
    public Object visit(Meets meets, Object data) {
        collect(meets);
        return super.visit(meets, data);
    }

    @Override
    public Object visit(MetBy metBy, Object data) {
        collect(metBy);
        return super.visit(metBy, data);
    }

    @Override
    public Object visit(OverlappedBy overlappedBy, Object data) {
        collect(overlappedBy);
        return super.visit(overlappedBy, data);
    }

    @Override
    public Object visit(TContains contains, Object data) {
        collect(contains);
        return super.visit(contains, data);
    }

    @Override
    public Object visit(TEquals equals, Object data) {
        collect(equals);
        return super.visit(equals, data);
    }

    @Override
    public Object visit(TOverlaps contains, Object data) {
        collect(contains);
        return super.visit(contains, data);
    }

    @Override
    public Object visit(NativeFilter filter, Object data) {
        collect(filter);
        return super.visit(filter, data);
    }
}
