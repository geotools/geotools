/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2025, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.referencing.operation;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.operation.CoordinateOperation;
import org.geotools.api.referencing.operation.CoordinateOperationFactory;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.geotools.util.factory.Hints;

/**
 * A CoordinateOperationFactory that delegates to multiple underlying factories, returning the first set of results it
 * finds from any of them.
 */
public class ManyCoordinateOperationFactory extends DefaultCoordinateOperationFactory {

    private final List<CoordinateOperationFactory> factories;

    public ManyCoordinateOperationFactory(Hints hints) {
        super(hints);

        // support attempt to recreate factories with the desired hints, which is otherwise done only
        // when asking for a single factory calling ReferencingFactoryFinder.getCoordinateOperationFactory(hints)
        Hints recreateHints = new Hints(hints);
        recreateHints.put(Hints.CREATE_FACTORIES_WITH_HINTS, Boolean.TRUE);
        List<CoordinateOperationFactory> factories =
                new ArrayList<>(ReferencingFactoryFinder.getCoordinateOperationFactories(recreateHints));
        // get rid of duplicated buffered factories, this factory is meant to be wrapped in a buffered factory itself
        for (CoordinateOperationFactory test : new ArrayList<>(factories)) {
            if (test instanceof BufferedCoordinateOperationFactory buffered) {
                CoordinateOperationFactory backing = buffered.getBackingFactory();
                if (factories.contains(backing)) factories.remove(test);
            }
        }
        this.factories = factories;
    }

    @Override
    protected CoordinateOperation createFromDatabase(
            final CoordinateReferenceSystem sourceCRS, final CoordinateReferenceSystem targetCRS) {
        Set<CoordinateOperation> operations = findFromDatabase(sourceCRS, targetCRS, 1);
        for (CoordinateOperation op : operations) {
            return op;
        }
        return null;
    }

    @Override
    public Set<CoordinateOperation> findFromDatabase(
            CoordinateReferenceSystem sourceCRS, CoordinateReferenceSystem targetCRS, int limit) {
        for (CoordinateOperationFactory factory : factories) {
            Set<CoordinateOperation> fromDatabase = factory.findFromDatabase(sourceCRS, targetCRS, limit);
            if (fromDatabase != null && !fromDatabase.isEmpty()) {
                return fromDatabase;
            }
        }

        return Set.of();
    }
}
