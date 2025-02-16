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

import java.util.logging.Logger;
import org.geotools.api.filter.And;
import org.geotools.api.filter.ExcludeFilter;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.Id;
import org.geotools.api.filter.IncludeFilter;
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
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;

/**
 * Extract a maximal envelope from the provided Filter.
 *
 * <p>The maximal envelope is generated from:
 *
 * <ul>
 *   <li>all the literal geometry instances involved if spatial operations - using geom.getEnvelopeInternal().
 *   <li>Filter.EXCLUDES will result in an empty envelope
 *   <li>Filter.INCLUDES will result in a "world" envelope with range Double.NEGATIVE_INFINITY to
 *       Double.POSITIVE_INFINITY for each axis.
 *   <li>all other non spatial filters will result in a world envelope
 *   <li>combinations in and will return the intersection of the envelopes, or an empty envelope if an exclude is in the
 *       mix, or null if the and is mixing non spatial filters
 *   <li>combinations in or will return the intersection of
 * </ul>
 *
 * Since geometry literals do not contains CRS information we can only produce a ReferencedEnvelope without CRS
 * information. You can call this function with an existing ReferencedEnvelope or with your data CRS to correct for this
 * limitation. ReferencedEnvelope example:
 *
 * <pre><code>
 * ReferencedEnvelope bbox = (ReferencedEnvelope)
 *     filter.accepts(new ExtractBoundsFilterVisitor(), dataCRS );
 * </code></pre>
 *
 * You can also call this function with an existing Envelope; if you are building up bounds based on several filters.
 *
 * <p>This is a replacement for FilterConsumer.
 *
 * @author Jody Garnett
 */
public class ExtractBoundsFilterVisitor extends NullFilterVisitor {
    public static NullFilterVisitor BOUNDS_VISITOR = new ExtractBoundsFilterVisitor();

    private static Logger LOGGER = org.geotools.util.logging.Logging.getLogger(ExtractBoundsFilterVisitor.class);

    /**
     * This FilterVisitor is stateless - use ExtractBoundsFilterVisitor.BOUNDS_VISITOR.
     *
     * <p>You may also subclass in order to reuse this functionality in your own FilterVisitor implementation.
     */
    protected ExtractBoundsFilterVisitor() {}

    /**
     * Produce an ReferencedEnvelope from the provided data parameter.
     *
     * @return ReferencedEnvelope
     */
    private ReferencedEnvelope bbox(Object data) {
        if (data == null) {
            return null;
        } else if (data instanceof ReferencedEnvelope) {
            return (ReferencedEnvelope) data;
        } else if (data instanceof Envelope) {
            return new ReferencedEnvelope((Envelope) data, null);
        } else if (data instanceof CoordinateReferenceSystem) {
            return new ReferencedEnvelope((CoordinateReferenceSystem) data);
        }
        throw new ClassCastException("Could not cast data to ReferencedEnvelope");
    }

    @Override
    public Object visit(ExcludeFilter filter, Object data) {
        return new Envelope();
    }

    @Override
    public Object visit(IncludeFilter filter, Object data) {
        return infinity();
    }

    protected Envelope infinity() {
        return new Envelope(
                Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

    @Override
    public Object visit(BBOX filter, Object data) {
        ReferencedEnvelope bbox = bbox(data);

        // consider doing reprojection here into data CRS?
        Envelope bounds = new Envelope(ReferencedEnvelope.reference(filter.getBounds()));
        if (bbox != null) {
            bbox.expandToInclude(bounds);
            return bbox;
        } else {
            return bbox(bounds);
        }
    }
    /**
     * Please note we are only visiting literals involved in spatial operations.
     *
     * @param expression a literal expression, hopefully a Geometry or Envelope
     * @param data Incoming BoundingBox (or Envelope or CRS)
     * @return ReferencedEnvelope updated to reflect literal
     */
    @Override
    public Object visit(Literal expression, Object data) {
        ReferencedEnvelope bbox = bbox(data);

        Object value = expression.getValue();
        if (value instanceof Geometry) {

            Geometry geometry = (Geometry) value;
            Envelope bounds = geometry.getEnvelopeInternal();

            if (bbox != null) {
                bbox.expandToInclude(bounds);
                return bbox;
            } else {
                return bbox(bounds);
            }
        } else if (value instanceof Envelope) {
            Envelope bounds = (Envelope) value;
            if (bbox != null) {
                bbox.expandToInclude(bounds);
                return bbox;
            } else {
                return bbox(bounds);
            }

        } else {

            LOGGER.finer("LiteralExpression ignored!");
        }
        return bbox;
    }

    @Override
    public Object visit(And filter, Object data) {
        Envelope mixed = infinity();
        for (Filter f : filter.getChildren()) {
            Envelope env = (Envelope) f.accept(this, data);
            mixed = mixed.intersection(env);
        }
        return mixed;
    }

    @Override
    public Object visit(Not filter, Object data) {
        // no matter what we have to return an infinite envelope
        // rationale
        // !(finite envelope) -> an unbounded area -> infinite
        // !(non spatial filter) -> infinite (no spatial concern)
        // !(infinite) -> ... infinite, as the first infinite could be the result
        // of !(finite envelope)

        return infinity();
    }

    @Override
    public Object visit(Or filter, Object data) {
        Envelope mixed = new Envelope();
        for (Filter f : filter.getChildren()) {
            Envelope env = (Envelope) f.accept(this, data);
            mixed.expandToInclude(env);
        }
        return mixed;
    }

    @Override
    public Object visit(Beyond filter, Object data) {
        // beyond a certain distance from a finite object, no way to limit it
        return infinity();
    }

    @Override
    public Object visit(Contains filter, Object data) {
        data = filter.getExpression1().accept(this, data);
        data = filter.getExpression2().accept(this, data);
        return data;
    }

    @Override
    public Object visit(Crosses filter, Object data) {
        data = filter.getExpression1().accept(this, data);
        data = filter.getExpression2().accept(this, data);
        return data;
    }

    @Override
    public Object visit(Disjoint filter, Object data) {
        // disjoint does not define a rectangle, but a hole in the
        // Cartesian plane, no way to limit it
        return infinity();
    }

    @Override
    public Object visit(DWithin filter, Object data) {
        ReferencedEnvelope bbox = bbox(data);

        // we have to take the reference geometry bbox and
        // expand it by the distance.
        // We ignore the unit of measure for the moment
        Literal geometry = null;
        if (filter.getExpression1() instanceof PropertyName && filter.getExpression2() instanceof Literal) {
            geometry = (Literal) filter.getExpression2();
        }
        if (filter.getExpression2() instanceof PropertyName && filter.getExpression1() instanceof Literal) {
            geometry = (Literal) filter.getExpression2();
        }

        // we cannot desume a bbox from this filter
        if (geometry == null) {
            return infinity();
        }

        Geometry geom = geometry.evaluate(null, Geometry.class);
        if (geom == null) {
            return infinity();
        }

        Envelope env = geom.getEnvelopeInternal();
        env.expandBy(filter.getDistance());

        if (bbox != null) {
            bbox.expandToInclude(env);
            return bbox;
        } else {
            return bbox(env);
        }
    }

    @Override
    public Object visit(Equals filter, Object data) {
        data = filter.getExpression1().accept(this, data);
        data = filter.getExpression2().accept(this, data);
        return data;
    }

    @Override
    public Object visit(Intersects filter, Object data) {
        data = filter.getExpression1().accept(this, data);
        data = filter.getExpression2().accept(this, data);

        return data;
    }

    @Override
    public Object visit(Overlaps filter, Object data) {
        data = filter.getExpression1().accept(this, data);
        data = filter.getExpression2().accept(this, data);

        return data;
    }

    @Override
    public Object visit(Touches filter, Object data) {
        data = filter.getExpression1().accept(this, data);
        data = filter.getExpression2().accept(this, data);

        return data;
    }

    @Override
    public Object visit(Within filter, Object data) {
        data = filter.getExpression1().accept(this, data);
        data = filter.getExpression2().accept(this, data);

        return data;
    }

    @Override
    public Object visit(Add expression, Object data) {
        return infinity();
    }

    @Override
    public Object visit(Divide expression, Object data) {
        return infinity();
    }

    @Override
    public Object visit(Function expression, Object data) {
        return infinity();
    }

    @Override
    public Object visit(Id filter, Object data) {
        return infinity();
    }

    @Override
    public Object visit(Multiply expression, Object data) {
        return infinity();
    }

    @Override
    public Object visit(NilExpression expression, Object data) {
        return infinity();
    }

    @Override
    public Object visit(PropertyIsBetween filter, Object data) {
        return infinity();
    }

    @Override
    public Object visit(PropertyIsEqualTo filter, Object data) {
        return infinity();
    }

    @Override
    public Object visit(PropertyIsGreaterThan filter, Object data) {
        return infinity();
    }

    @Override
    public Object visit(PropertyIsGreaterThanOrEqualTo filter, Object data) {
        return infinity();
    }

    @Override
    public Object visit(PropertyIsLessThan filter, Object data) {
        return infinity();
    }

    @Override
    public Object visit(PropertyIsLessThanOrEqualTo filter, Object data) {
        return infinity();
    }

    @Override
    public Object visit(PropertyIsLike filter, Object data) {
        return infinity();
    }

    @Override
    public Object visit(PropertyIsNotEqualTo filter, Object data) {
        return infinity();
    }

    @Override
    public Object visit(PropertyIsNull filter, Object data) {
        return infinity();
    }

    @Override
    public Object visit(PropertyName expression, Object data) {
        return null;
    }

    @Override
    public Object visit(Subtract expression, Object data) {
        return infinity();
    }

    @Override
    public Object visitNullFilter(Object data) {
        return infinity();
    }

    @Override
    public Object visit(After after, Object data) {
        return infinity();
    }

    @Override
    public Object visit(AnyInteracts anyInteracts, Object data) {
        return infinity();
    }

    @Override
    public Object visit(Before before, Object data) {
        return infinity();
    }

    @Override
    public Object visit(Begins begins, Object data) {
        return infinity();
    }

    @Override
    public Object visit(BegunBy begunBy, Object data) {
        return infinity();
    }

    @Override
    public Object visit(During filter, Object data) {
        return infinity();
    }

    @Override
    public Object visit(EndedBy endedBy, Object data) {
        return infinity();
    }

    @Override
    public Object visit(Ends ends, Object data) {
        return infinity();
    }

    @Override
    public Object visit(Meets meets, Object data) {
        return infinity();
    }

    @Override
    public Object visit(MetBy metBy, Object data) {
        return infinity();
    }

    @Override
    public Object visit(OverlappedBy overlappedBy, Object data) {
        return infinity();
    }

    @Override
    public Object visit(TContains contains, Object data) {
        return infinity();
    }

    @Override
    public Object visit(TEquals equals, Object data) {
        return infinity();
    }

    @Override
    public Object visit(TOverlaps contains, Object data) {
        return infinity();
    }

    @Override
    public Object visit(PropertyIsNil filter, Object data) {
        return infinity();
    }
}
