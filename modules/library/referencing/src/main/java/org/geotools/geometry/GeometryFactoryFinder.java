/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geometry;

import org.geotools.api.geometry.PositionFactory;
import org.geotools.api.geometry.Precision;
import org.geotools.util.factory.FactoryCreator;
import org.geotools.util.factory.FactoryFinder;
import org.geotools.util.factory.FactoryRegistry;

import java.util.Arrays;

/**
 * Defines static methods used to access the application's default geometry factory implementations.
 *
 * @since 2.5
 * @version $Id$
 * @author Jody Garnett (Refractions Research)
 */
public class GeometryFactoryFinder extends FactoryFinder {
    /** The service registry for this manager. Will be initialized only when first needed. */
    private static volatile FactoryRegistry registry;

    /** Do not allows any instantiation of this class. */
    private GeometryFactoryFinder() {
        // singleton
    }

    /**
     * Returns the service registry. The registry will be created the first time this method is
     * invoked.
     */
    private static FactoryRegistry getServiceRegistry() {
        assert Thread.holdsLock(GeometryFactoryFinder.class);
        if (registry == null) {
            registry =
                    new FactoryCreator(
                            Arrays.asList(
                                    new Class<?>[] {
                                        Precision.class,
                                        PositionFactory.class
                                    }));
        }
        return registry;
    }


}
