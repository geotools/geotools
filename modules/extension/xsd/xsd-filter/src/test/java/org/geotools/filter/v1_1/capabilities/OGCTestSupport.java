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
package org.geotools.filter.v1_1.capabilities;

import org.geotools.filter.v1_1.OGCConfiguration;
import org.geotools.xsd.Configuration;
import org.geotools.xsd.test.XMLTestSupport;

/**
 * Base test class for the http://www.opengis.net/ogc schema.
 *
 * @generated
 */
public abstract class OGCTestSupport extends XMLTestSupport {
    protected Configuration createConfiguration() {
        return new OGCConfiguration();
    }
}
