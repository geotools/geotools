/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geometry.jts;

// J2SE dependencies
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Set;

// JTS dependencies
import com.vividsolutions.jts.geom.CoordinateSequenceFactory;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;

// Geotools dependencies
import org.geotools.factory.GeoTools;
import org.geotools.factory.Hints;
import org.geotools.factory.FactoryCreator;
import org.geotools.factory.FactoryRegistry;
import org.geotools.factory.FactoryRegistryException;
import org.geotools.resources.LazySet;


/**
 * Defines static methods used to access {@linkplain GeometryFactory geometry},
 * {@linkplain CoordinateSequenceFactory coordinate sequence} or
 * {@linkplain PrecisionModel precision model} factories.
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 * @deprecated Please use JTSFactoryFinder
 */
public class FactoryFinder {
    /**
     * Do not allows any instantiation of this class.
     */
    private FactoryFinder() {
        // singleton
    }

    /**
     * Returns the first implementation of {@link GeometryFactory} matching the specified hints.
     * If no implementation matches, a new one is created if possible or an exception is thrown
     * otherwise.
     * <p>
     * Hints that may be understood includes
     * {@link Hints#JTS_COORDINATE_SEQUENCE_FACTORY JTS_COORDINATE_SEQUENCE_FACTORY},
     * {@link Hints#JTS_PRECISION_MODEL             JTS_PRECISION_MODEL} and
     * {@link Hints#JTS_SRID                        JTS_SRID}.
     *
     * @param  hints An optional map of hints, or {@code null} if none.
     * @return The first geometry factory that matches the supplied hints.
     * @throws FactoryRegistryException if no implementation was found or can be created for the
     *         {@link GeometryFactory} category and the given hints.
     */
    public static synchronized GeometryFactory getGeometryFactory(Hints hints) throws FactoryRegistryException {
        return JTSFactoryFinder.getGeometryFactory(hints);
    }

    /**
     * Returns a set of all available implementations for the {@link GeometryFactory} category.
     *
     * @return Set of available geometry factory implementations.
     */
    public static synchronized Set getGeometryFactories() {
        return JTSFactoryFinder.getGeometryFactories();
    }

    /**
     * Returns the first implementation of {@link PrecisionModel} matching the specified hints.
     * If no implementation matches, a new one is created if possible or an exception is thrown
     * otherwise.
     *
     * @param  hints An optional map of hints, or {@code null} if none.
     * @return The first precision model that matches the supplied hints.
     * @throws FactoryRegistryException if no implementation was found or can be created for the
     *         {@link PrecisionModel} category and the given hints.
     */
    public static synchronized PrecisionModel getPrecisionModel(Hints hints) throws FactoryRegistryException {
        return JTSFactoryFinder.getPrecisionModel(hints);
    }

    /**
     * Returns a set of all available implementations for the {@link PrecisionModel} category.
     *
     * @return Set of available precision model implementations.
     */
    public static synchronized Set getPrecisionModels() {
        return JTSFactoryFinder.getPrecisionModels();
    }

    /**
     * Returns the first implementation of {@link CoordinateSequenceFactory} matching the specified
     * hints. If no implementation matches, a new one is created if possible or an exception is
     * thrown otherwise.
     *
     * @param  hints An optional map of hints, or {@code null} if none.
     * @return The first coordinate sequence factory that matches the supplied hints.
     * @throws FactoryRegistryException if no implementation was found or can be created for the
     *         {@link CoordinateSequenceFactory} interface and the given hints.
     */
    public static synchronized CoordinateSequenceFactory getCoordinateSequenceFactory(Hints hints) throws FactoryRegistryException {
        return JTSFactoryFinder.getCoordinateSequenceFactory(hints);
    }

    /**
     * Returns a set of all available implementations for the {@link CoordinateSequenceFactory}
     * interface.
     *
     * @return Set of available coordinate sequence factory implementations.
     */
    public static synchronized Set getCoordinateSequenceFactories() {
        return JTSFactoryFinder.getCoordinateSequenceFactories();
    }

    /**
     * Scans for factory plug-ins on the application class path. This method is
     * needed because the application class path can theoretically change, or
     * additional plug-ins may become available. Rather than re-scanning the
     * classpath on every invocation of the API, the class path is scanned
     * automatically only on the first invocation. Clients can call this
     * method to prompt a re-scan. Thus this method need only be invoked by
     * sophisticated applications which dynamically make new plug-ins
     * available at runtime.
     */
    public static void scanForPlugins() {
        JTSFactoryFinder.scanForPlugins();
    }

}
