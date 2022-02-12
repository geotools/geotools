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
import org.opengis.filter.And;
import org.opengis.filter.Id;
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
import org.opengis.filter.expression.Add;
import org.opengis.filter.expression.Divide;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.Multiply;
import org.opengis.filter.expression.NilExpression;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.expression.Subtract;
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
