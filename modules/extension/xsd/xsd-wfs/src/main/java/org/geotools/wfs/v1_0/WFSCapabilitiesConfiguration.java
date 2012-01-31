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
package org.geotools.wfs.v1_0;

/**
 * Parser configuration for the wfs 1.0 {@link WFSCapabilities schema}, different from the
 * {@link WFSConfiguration} because WFS 1.0 bases capabilities document and request/response
 * documents on different schemas.
 * 
 * @see WFSCapabilities
 */
public class WFSCapabilitiesConfiguration extends org.geotools.wfs.WFSConfiguration {
    /**
     * Creates a new configuration.
     * 
     * @generated
     */
    public WFSCapabilitiesConfiguration() {
        super(WFSCapabilities.getInstance());

        addDependency(new org.geotools.filter.v1_0.capabilities.OGCConfiguration());
    }

    protected void configureBindings(org.picocontainer.MutablePicoContainer container) {

        container
                .registerComponentImplementation(WFSCapabilities.Service, ServiceTypeBinding.class);
        container.registerComponentImplementation(WFSCapabilities.LatLongBoundingBox,
                LatLongBoundingBoxBinding.class);

        container.registerComponentImplementation(WFSCapabilities.DCPType, DCPTypeBinding.class);
        container.registerComponentImplementation(WFSCapabilities.Capability,
                CapabilityBinding.class);
    }
}
