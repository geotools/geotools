/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.styling;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.FactoryRegistryException;
import org.geotools.factory.GeoTools;


/**
 *
 * @source $URL$
 *
 * @deprecated Replaced by {@link org.geotools.factory.CommonFactoryFinder#getStyleFactory}.
 */
public class StyleFactoryFinder {
    private static StyleFactory factory = null;

    /**
     * Create an instance of the factory.
     *
     * @return An instance of the Factory, or null if the Factory could not be
     *         created.
     * @throws FactoryRegistryException If the factory is not found.
     */
    public static StyleFactory createStyleFactory() throws FactoryRegistryException {
        if (factory == null) {
            factory = CommonFactoryFinder.getStyleFactory( GeoTools.getDefaultHints() );
        }
        return factory;
    }
}
