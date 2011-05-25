/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.factory;


/**
 * Base class for factory finders. {@code FactoryFinder}s are cover for {@link FactoryRegistry}
 * adding type safety, default hints and synchronization for multi-thread environments.
 *
 * @since 2.4
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
public abstract class FactoryFinder {
    /**
     * A set of empty hints, which exclude any {@linkplain Geotools#getDefaultHints system hints}.
     */
    public static final Hints EMPTY_HINTS = new StrictHints.Empty();

    /**
     * Creates a new factory finder.
     */
    protected FactoryFinder() {
    }

    /**
     * Returns new hints that combine user supplied hints with the
     * {@linkplain GeoTools#getDefaultHints defaults hints}. If a hint is specified
     * in both user and default hints, then user hints have precedence.
     * <p>
     * The returned hints should live only the time needed for invoking {@link FactoryRegistry}
     * methods. No long-term reference should be held.
     *
     * @param hints The user hints, or {@code null} if none.
     * @return New hints (never {@code null}).
     */
    public static Hints mergeSystemHints(final Hints hints) {
        if (hints instanceof StrictHints) {
            /*
             * The hints have already been merged in a previous call to this method and we don't
             * want to merge them again. This case happen typically in factory constructor fetching
             * for dependencies. The constructor may have removed some hints. For example the
             * "URN:OGC" factory may looks for the "EPSG" factory with FORCE_LONGITUDE_FIRST
             * forced to FALSE.
             */
            return hints;
        }
        final Hints merged = Hints.getDefaults(true);
        if (hints != null) {
            merged.add(hints);
        }
        return merged;
    }
}
