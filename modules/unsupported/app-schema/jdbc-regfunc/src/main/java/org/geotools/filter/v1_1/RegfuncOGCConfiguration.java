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

package org.geotools.filter.v1_1;

import org.geotools.filter.FilterFactoryImpl;
import org.geotools.filter.RegfuncFilterFactoryImpl;
import org.geotools.xml.Configuration;
import org.opengis.filter.FilterFactory;
import org.picocontainer.MutablePicoContainer;

/**
 * {@link Configuration} to add registered function support.
 * 
 * 
 * <p>
 * 
 * This class overrides the {@link FilterFactory} component used by {@link OGCConfiguration},
 * replacing it with {@link RegfuncFilterFactoryImpl}, thus enabling support of registered
 * functions.
 * 
 * <p>
 * 
 * TODO: this class should be obsolete in the future, when {@link RegfuncFilterFactoryImpl} is
 * merged into {@link FilterFactoryImpl}.
 * 
 * @author Ben Caradoc-Davies, CSIRO Exploration and Mining
 * @version $Id$
 * @source $URL$
 * @since 2.4
 */
public class RegfuncOGCConfiguration extends OGCConfiguration {

    /**
     * Configures the filter context. {@link RegfuncFilterFactoryImpl} is registered under
     * {@link FilterFactory}, after the formerly registered component is unregistered.
     * 
     * @see org.geotools.filter.v1_1.OGCConfiguration#configureContext(org.picocontainer.MutablePicoContainer)
     */
    // @Override
    public void configureContext(MutablePicoContainer container) {
        super.configureContext(container);
        // must first remove old FilterFactory
        container.unregisterComponent(FilterFactory.class);
        container.registerComponentImplementation(FilterFactory.class,
                RegfuncFilterFactoryImpl.class);
    }

}
