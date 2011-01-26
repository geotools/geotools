/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2001-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.factory.epsg;

import org.geotools.factory.Hints;


/**
 * Base class for EPSG factories to be registered in {@link GeometryFactoryFinder}.
 *
 * @since 2.1
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 *
 * @deprecated Please use {@link ThreadedEpsgFactory} instead,
 *             the name {@code DefaultFactory} was viewed as confusing
 */
public class DefaultFactory extends ThreadedEpsgFactory {
    /**
     * Constructs an authority factory using the default set of factories.
     */
    public DefaultFactory() {
    }

    /**
     * Constructs an authority factory with the default priority.
     */
    public DefaultFactory(final Hints userHints) {
        super( userHints );
    }

    /**
     * Constructs an authority factory using a set of factories created from the specified hints.
     *
     * @since 2.4
     */
    public DefaultFactory(final Hints userHints, final int priority) {
        super(userHints, priority);
    }
}
