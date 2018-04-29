/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2014, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.wfs.v2_0;

/**
 * Parser configuration for the wfs 1.0 {@link WFSCapabilities schema}, different from the {@link
 * WFSConfiguration} because WFS 1.0 bases capabilities document and request/response documents on
 * different schemas.
 *
 * @see WFSCapabilities
 */
public class WFSCapabilitiesConfiguration extends WFSConfiguration {
    /**
     * Creates a new configuration.
     *
     * @generated
     */
    public WFSCapabilitiesConfiguration() {
        addDependency(new org.geotools.filter.v2_0.FESConfiguration());
        addDependency(new org.geotools.ows.v1_1.OWSConfiguration());
    }

    protected void configureBindings(org.picocontainer.MutablePicoContainer container) {}
}
