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

import junit.framework.TestCase;

import org.geotools.filter.RegfuncFilterFactoryImpl;
import org.opengis.filter.FilterFactory;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.defaults.DefaultPicoContainer;

/**
 * Test for {@link RegfuncOGCConfiguration}.
 * 
 * @author Ben Caradoc-Davies, CSIRO Exploration and Mining
 * @version $Id$
 *
 * @source $URL$
 * @since 2.4
 */
public class RegfuncOGCConfigurationTest extends TestCase {

    /**
     * Test that {@link RegfuncOGCConfiguration} configures container to return a
     * {@link RegfuncFilterFactoryImpl} when queried for a {@link FilterFactory}.
     */
    public void testConfigureContext() {
        OGCConfiguration configuration = new RegfuncOGCConfiguration();
        MutablePicoContainer container = new DefaultPicoContainer();
        configuration.configureContext(container);
        assertEquals(RegfuncFilterFactoryImpl.class, container.getComponentInstanceOfType(
                FilterFactory.class).getClass());
    }

}
