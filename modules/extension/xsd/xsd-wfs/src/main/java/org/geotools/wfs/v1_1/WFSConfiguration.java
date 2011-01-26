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
package org.geotools.wfs.v1_1;

import org.geotools.filter.v1_1.OGCConfiguration;
import org.geotools.ows.OWSConfiguration;

/**
 * Parser configuration for the wfs 1.1 schema.
 *
 * @generated
 *
 * @source $URL$
 */
public class WFSConfiguration extends org.geotools.wfs.WFSConfiguration {
    /**
     * Creates a new configuration.
     *
     * @generated
     */
    public WFSConfiguration() {
        super(WFS.getInstance());
        
        addDependency(new OWSConfiguration());
        addDependency(new OGCConfiguration());
    }
}
