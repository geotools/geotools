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

import java.util.Set;
import org.geotools.xsd.XSD;

/**
 * XSD for wfs 1.0. capabilities document; for non capabilities use {@link WFS}, as it's based on a
 * different schema.
 *
 * @see WFSCapabilitiesConfiguration
 */
public final class WFSCapabilities extends XSD {

    /** singleton instance */
    private static final WFSCapabilities instance = new WFSCapabilities();

    /** Returns the singleton instance. */
    public static final WFSCapabilities getInstance() {
        return instance;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    protected void addDependencies(Set dependencies) {
        /// dependencies.add(org.geotools.filter.v1_0.capabilities.OGC.getInstance());
    }

    public String getSchemaLocation() {
        return getClass().getResource("wfs.xsd").toString();
    }

    @Override
    public String getNamespaceURI() {
        return "http://www.opengis.net/wfs/2.0";
    }
}
