/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2026, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.util;

import javax.xml.XMLConstants;
import org.xml.sax.ext.EntityResolver2;

/**
 * Extends EntityResolver2 to provide additional information on protocols supported.
 *
 * <p>This information is used proactively limit EntityResolution by configuring {@code DocumentFactory},
 * {@code SchemaFactory}, etc to only use the supported protocols.
 *
 * <p>For more information see {@code gt-xml} module {@code XMLUtils}.
 *
 * @see XMLConstants#ACCESS_EXTERNAL_DTD
 * @see XMLConstants#ACCESS_EXTERNAL_SCHEMA
 */
public interface EntityResolver3 extends EntityResolver2 {
    /**
     * Comma seperated list of entity resolution protocols required for EntityResolver to function.
     *
     * <p>Common examples: {@code "http"},{@code "file"}, or combined {@code "file,http"}.
     *
     * <p>To allow all access {@code "all"}, or to suppress all access {@code ""} supported.
     *
     * @return Comma seperated list of protocols, allow all {@code "all"}, or none {@code ""}.
     */
    public String getAccess();
}
