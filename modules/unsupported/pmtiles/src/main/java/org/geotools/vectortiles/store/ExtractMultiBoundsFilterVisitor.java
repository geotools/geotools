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
package org.geotools.vectortiles.store;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import org.geotools.api.filter.And;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.Or;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.filter.visitor.ExtractBoundsFilterVisitor;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.locationtech.jts.geom.Envelope;

/**
 * A subclass of ExtractBoundsFilterVisitor that can return either an Envelope or a list of disjoint envelopes. Reasons
 * to develop it, was that renderer can query multiple separate bounding boxes when dealing with complex projection
 * situations (e.g. dateline or other fundamental break lines in the projection at hand) and we don't want to query all
 * the tiles in between.
 */
class ExtractMultiBoundsFilterVisitor extends ExtractBoundsFilterVisitor {

    static final ExtractMultiBoundsFilterVisitor INSTANCE = new ExtractMultiBoundsFilterVisitor();

    @SuppressWarnings("unchecked")
    public static List<ReferencedEnvelope> getBounds(Filter filter, CoordinateReferenceSystem crs) {
        Object result = filter.accept(INSTANCE, crs);
        if (result instanceof List) {
            return (List<ReferencedEnvelope>) result;
        } else if (result instanceof ReferencedEnvelope re) {
            return List.of(re);
        }
        return List.of();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object visit(And filter, Object data) {
        // collect the list of lists of envelopes. The inner list of envelops is or-ed, the
        // outer list is and-ed
        final List<Filter> children = filter.getChildren();
        List<List<ReferencedEnvelope>> envelopes = new ArrayList<>(children.size());
        for (Filter child : children) {
            Object result = child.accept(this, data);
            // when a list of envelopes is returned, they are added to the list of envelopes to
            // process
            if (result instanceof ReferencedEnvelope re) {
                envelopes.add(List.of(re));
            } else if (result instanceof List list && !list.isEmpty()) {
                envelopes.add((List<ReferencedEnvelope>) list);
            }
        }

        // no spatial filter found
        if (envelopes.isEmpty()) {
            return infinity();
        }

        List<ReferencedEnvelope> result = envelopes.get(0);
        for (int i = 1; i < envelopes.size(); i++) {
            List<ReferencedEnvelope> curr = envelopes.get(i);
            // intersect with the current set
            Set<ReferencedEnvelope> intersections = new HashSet<>();
            for (ReferencedEnvelope ce : curr) {
                for (ReferencedEnvelope re : result) {
                    if (re.intersects((Envelope) ce)) {
                        intersections.add(re.intersection(ce));
                    }
                }
            }

            if (intersections.isEmpty()) {
                return new ReferencedEnvelope();
            } else {
                result = new ArrayList<>(intersections);
            }
        }

        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object visit(Or filter, Object data) {
        List<ReferencedEnvelope> envelopes = new ArrayList<>();
        // when multiple envelopes are returned, they get or-ed
        final List<Filter> children = filter.getChildren();
        for (Filter child : children) {
            Object result = child.accept(this, data);
            if (result instanceof ReferencedEnvelope re) {
                envelopes.add(re);
            } else if (result instanceof List list && !list.isEmpty()) {
                envelopes.addAll(list);
            }
        }

        // now merge the ones that are overlapping
        List<ReferencedEnvelope> result = new ArrayList<>();
        for (ReferencedEnvelope envelope : envelopes) {
            if (result.isEmpty()) {
                result.add(envelope);
                continue;
            }

            boolean mergedAny = false;
            do {
                // Find all envelopes found so far that overlap with the current one,
                // include them in the current one and remove the from the result
                // This expansion can cause envelopes not previously matching to overlap
                // with the current envelope, so start back from the beginning until we
                // can continue to merge the results
                mergedAny = false;
                ListIterator<ReferencedEnvelope> it = result.listIterator();
                while (it.hasNext()) {
                    ReferencedEnvelope next = it.next();
                    if (next.intersects((Envelope) envelope)) {
                        it.remove();
                        envelope.expandToInclude(next);
                        mergedAny = true;
                    }
                }
            } while (mergedAny);
            // the envelope could not be merged with any of the ones in the result, add it
            result.add(envelope);
        }

        return result;
    }
}
