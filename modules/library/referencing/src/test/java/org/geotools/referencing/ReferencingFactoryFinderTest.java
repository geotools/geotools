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

package org.geotools.referencing;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.Set;
import org.geotools.api.referencing.operation.CoordinateOperationFactory;
import org.geotools.util.factory.GeoTools;
import org.geotools.util.factory.Hints;
import org.junit.Test;

public class ReferencingFactoryFinderTest {

    @Test
    public void testGetCoordinateOperationFactories() {
        final Hints hints = GeoTools.getDefaultHints();
        Set<CoordinateOperationFactory> factories = ReferencingFactoryFinder.getCoordinateOperationFactories(hints);
        assertNotNull(factories);
        assertFalse(factories.isEmpty());
    }

    @Test
    public void testGetCoordinateOperationFactoriesLenient() {
        final Hints hints = GeoTools.getDefaultHints();
        hints.put(Hints.LENIENT_DATUM_SHIFT, Boolean.TRUE);
        hints.put(Hints.CREATE_FACTORIES_WITH_HINTS, Boolean.TRUE);
        Set<CoordinateOperationFactory> factories = ReferencingFactoryFinder.getCoordinateOperationFactories(hints);
        assertNotNull(factories);
        assertFalse(factories.isEmpty());
    }

    @Test
    public void testGetCoordinateOperationFactoryLenient() {
        final Hints hints = GeoTools.getDefaultHints();
        hints.put(Hints.LENIENT_DATUM_SHIFT, Boolean.TRUE);
        CoordinateOperationFactory factory = ReferencingFactoryFinder.getCoordinateOperationFactory(hints);
        assertNotNull(factory);
    }
}
