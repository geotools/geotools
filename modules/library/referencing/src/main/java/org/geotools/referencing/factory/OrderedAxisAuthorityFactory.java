/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.factory;

import org.geotools.api.referencing.cs.AxisDirection;
import org.geotools.api.referencing.cs.CSAuthorityFactory;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.geotools.util.factory.FactoryRegistryException;
import org.geotools.util.factory.Hints;

/**
 * An authority factory which delegates all the work to an other factory, and reorder the axis in some pre-determined
 * order. This factory is mostly used by application expecting geographic coordinates in (<var>longitude</var>,
 * <var>latitude</var>) order, while most geographic CRS specified in the <A HREF="http://www.epsg.org">EPSG
 * database</A> use the opposite axis order.
 *
 * <p>See {@link OrderedAxisCRSAuthorityFactory} for details. This class also implements {@link CSAuthorityFactory}
 */
public class OrderedAxisAuthorityFactory extends OrderedAxisCRSAuthorityFactory implements CSAuthorityFactory {

    /**
     * Creates a factory which will reorder the axis of all objects created by the default authority factories. The
     * factories are fetched using {@link ReferencingFactoryFinder}. This constructor accepts the following hints:
     *
     * <p>
     *
     * <ul>
     *   <li>{@link Hints#FORCE_STANDARD_AXIS_UNITS}
     *   <li>{@link Hints#FORCE_STANDARD_AXIS_DIRECTIONS}
     *   <li>All hints understood by {@link ReferencingFactoryFinder}
     * </ul>
     *
     * @param authority The authority to wraps (example: {@code "EPSG"}). If {@code null}, then all authority factories
     *     must be explicitly specified in the set of hints.
     * @param userHints An optional set of hints, or {@code null} if none.
     * @param axisOrder An array of axis directions that determine the axis order wanted, or {@code null} for the
     *     default axis order.
     * @throws FactoryRegistryException if at least one factory can not be obtained.
     * @throws IllegalArgumentException If at least two axis directions are colinear.
     * @since 2.3
     */
    public OrderedAxisAuthorityFactory(final String authority, final Hints userHints, final AxisDirection... axisOrder)
            throws FactoryRegistryException, IllegalArgumentException {
        super(authority, userHints, axisOrder);
    }

    /**
     * Creates a factory which will reorder the axis of all objects created by the supplied factory. This constructor
     * accepts the following optional hints:
     *
     * <p>
     *
     * <ul>
     *   <li>{@link Hints#FORCE_STANDARD_AXIS_UNITS}
     *   <li>{@link Hints#FORCE_STANDARD_AXIS_DIRECTIONS}
     * </ul>
     *
     * @param factory The factory that produces objects using arbitrary axis order.
     * @param userHints An optional set of hints, or {@code null} if none.
     * @param axisOrder An array of axis directions that determine the axis order wanted, or {@code null} for the
     *     default axis order.
     * @throws IllegalArgumentException If at least two axis directions are colinear.
     * @since 2.3
     */
    public OrderedAxisAuthorityFactory(
            final AbstractAuthorityFactory factory, final Hints userHints, final AxisDirection... axisOrder)
            throws IllegalArgumentException {
        super(factory, userHints, axisOrder);
    }
}
