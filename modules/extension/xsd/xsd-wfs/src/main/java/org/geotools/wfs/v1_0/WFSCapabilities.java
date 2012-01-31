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

import java.util.Set;

import javax.xml.namespace.QName;

/**
 * XSD for wfs 1.0. capabilities document; for non capabilities use {@link WFS}, as it's based on a
 * different schema.
 * 
 * @see WFSCapabilitiesConfiguration
 */
public final class WFSCapabilities extends org.geotools.wfs.WFS {

    /** singleton instance */
    private static final WFSCapabilities instance = new WFSCapabilities();

    /**
     * Returns the singleton instance.
     */
    public static final WFSCapabilities getInstance() {
        return instance;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    protected void addDependencies(Set dependencies) {
        dependencies.add(org.geotools.filter.v1_0.capabilities.OGC.getInstance());
    }

    /**
     * Returns the location of 'WFS-transaction.xsd.'.
     */
    public String getSchemaLocation() {
        return getClass().getResource("WFS-capabilities.xsd").toString();
    }

    /**
     * Returns '1.0.0'.
     */
    @Override
    public String getVersion() {
        return "1.0.0";
    }

    public static final QName Service = new QName("http://www.opengis.net/wfs", "Service");

    public static final QName LatLongBoundingBox = new QName("http://www.opengis.net/wfs",
            "LatLongBoundingBox");

    public static final QName DCPType = new QName("http://www.opengis.net/wfs", "DCPType");

    public static final QName Capability = new QName("http://www.opengis.net/wfs", "Capability");

}
