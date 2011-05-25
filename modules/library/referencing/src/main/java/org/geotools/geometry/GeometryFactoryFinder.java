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

import java.util.Arrays;

import org.geotools.factory.FactoryCreator;
import org.geotools.factory.FactoryFinder;
import org.geotools.factory.FactoryRegistry;
import org.geotools.factory.FactoryRegistryException;
import org.geotools.factory.Hints;
import org.opengis.geometry.PositionFactory;
import org.opengis.geometry.Precision;
import org.opengis.geometry.aggregate.AggregateFactory;
import org.opengis.geometry.complex.ComplexFactory;
import org.opengis.geometry.coordinate.GeometryFactory;
import org.opengis.geometry.primitive.PrimitiveFactory;


/**
 * Defines static methods used to access the application's default geometry factory implementations.
 *
 * @since 2.5
 *
 * @source $URL$
 * @version $Id$
 * @author Jody Garnett (Refractions Research)
 */
public class GeometryFactoryFinder extends FactoryFinder {
    /**
     * The service registry for this manager. Will be initialized only when first needed.
     */
    private static FactoryRegistry registry;

    /**
     * Do not allows any instantiation of this class.
     */
    private GeometryFactoryFinder() {
        // singleton
    }

    /**
     * Returns the service registry. The registry will be created the first
     * time this method is invoked.
     */
    private static FactoryRegistry getServiceRegistry() {
        assert Thread.holdsLock(GeometryFactoryFinder.class);
        if (registry == null) {
            registry = new FactoryCreator(Arrays.asList(new Class<?>[] {
                    Precision.class,
                    PositionFactory.class,
                    GeometryFactory.class,
                    ComplexFactory.class,
                    AggregateFactory.class,
                    PrimitiveFactory.class}));
        }
        return registry;
    }

    public static synchronized Precision getPrecision(Hints hints) throws FactoryRegistryException {
        hints = mergeSystemHints(hints);
        return getServiceRegistry().getServiceProvider( Precision.class, null, hints, Hints.PRECISION );
    }

    public static synchronized PositionFactory getPositionFactory( Hints hints) throws FactoryRegistryException {
        hints = mergeSystemHints(hints);
        return getServiceRegistry().getServiceProvider( PositionFactory.class, null, hints, Hints.POSITION_FACTORY );
    }
    /**
     * An implementation of {@link GeometryFactory} for the provided crs.
     *
     * @param hints A set of hints that *must* include a Hints.CRS key
     * @return a GeometryFactory set up to work with the indicated CRS
     * @throws FactoryRegistryException if no implementation was found or can be created for the
     *         {@link GeometryFactory} interface.
     */
    public static synchronized GeometryFactory getGeometryFactory( Hints hints) throws FactoryRegistryException {
        hints = mergeSystemHints(hints);
        return getServiceRegistry().getServiceProvider( GeometryFactory.class, null, hints, Hints.GEOMETRY_FACTORY );
    }

    public static synchronized ComplexFactory getComplexFactory(Hints hints) throws FactoryRegistryException {
        hints = mergeSystemHints(hints);
        return getServiceRegistry().getServiceProvider( ComplexFactory.class, null, hints, Hints.COMPLEX_FACTORY );
    }

    public static synchronized AggregateFactory getAggregateFactory(Hints hints) throws FactoryRegistryException {
        hints = mergeSystemHints(hints);
        return getServiceRegistry().getServiceProvider( AggregateFactory.class, null, hints, Hints.AGGREGATE_FACTORY );
    }

    public static synchronized PrimitiveFactory getPrimitiveFactory(Hints hints) throws FactoryRegistryException {
        hints = mergeSystemHints(hints);
        return getServiceRegistry().getServiceProvider( PrimitiveFactory.class, null, hints, Hints.PRIMITIVE_FACTORY );
    }

}
