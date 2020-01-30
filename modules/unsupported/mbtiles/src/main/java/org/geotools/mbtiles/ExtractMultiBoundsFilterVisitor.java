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
package org.geotools.mbtiles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import org.geotools.filter.visitor.ExtractBoundsFilterVisitor;
import org.locationtech.jts.geom.Envelope;
import org.opengis.filter.And;
import org.opengis.filter.Filter;
import org.opengis.filter.Or;

/**
 * A subclass of ExtractBoundsFilterVisitor that can return either an Envelope or a list of disjoint
 * envelopes. Reasons to develop it, was that renderer can query multiple separate bounding boxes
 * when dealing with complex projection situations (e.g. dateline or other fundamental break lines
 * in the projection at hand) and we don't want to query all the tiles in between.
 */
class ExtractMultiBoundsFilterVisitor extends ExtractBoundsFilterVisitor {

    static final ExtractMultiBoundsFilterVisitor INSTANCE = new ExtractMultiBoundsFilterVisitor();

    public static List<Envelope> getBounds(Filter filter) {
        Object result = filter.accept(INSTANCE, null);
        if (result instanceof List) {
            return (List<Envelope>) result;
        } else if (result instanceof Envelope) {
            return Collections.singletonList((Envelope) result);
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public Object visit(And filter, Object data) {
        // collect the list of lists of envelopes. The inner list of envelops is or-ed, the
        // outer list is and-ed
        List<List<Envelope>> envelopes = new ArrayList<>();
        for (Filter f : filter.getChildren()) {
            Object result = f.accept(this, data);
            // when a list of envelopes is returned, they are added to the list of envelopes to
            // process
            if (result instanceof Envelope) {
                envelopes.add(Collections.singletonList((Envelope) result));
            } else if (result instanceof List) {
                envelopes.add((List) result);
            }
        }

        // should not happen, but to be on the safe side, consider the case of no results
        if (envelopes.isEmpty()) {
            return new Envelope();
        }

        List<Envelope> result = null;
        for (List<Envelope> curr : envelopes) {
            if (result == null) {
                result = curr;
                continue;
            }

            // intersect with the current set
            Set<Envelope> intersections = new HashSet<>();
            for (Envelope ce : curr) {
                for (Envelope re : result) {
                    if (re.intersects(ce)) {
                        intersections.add(re.intersection(ce));
                    }
                }
            }

            if (intersections.isEmpty()) {
                return new Envelope();
            } else {
                result = new ArrayList<>(intersections);
            }
        }

        return result;
    }

    @Override
    public Object visit(Or filter, Object data) {
        List<Envelope> envelopes = new ArrayList<>();
        // when multiple envelopes are returned, they get or-ed
        for (Filter f : filter.getChildren()) {
            Object result = f.accept(this, data);
            if (result instanceof Envelope) {
                envelopes.add((Envelope) result);
            } else if (result instanceof List) {
                envelopes.addAll((List) result);
            }
        }

        // now merge the ones that are overlapping
        List<Envelope> result = new ArrayList<>();
        for (Envelope envelope : envelopes) {
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
                ListIterator<Envelope> it = result.listIterator();
                while (it.hasNext()) {
                    Envelope next = it.next();
                    if (next.intersects(envelope)) {
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
