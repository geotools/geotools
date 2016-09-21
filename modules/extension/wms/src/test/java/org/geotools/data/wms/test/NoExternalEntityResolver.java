/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.wms.test;

import java.io.IOException;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * A test entity resolver allowing everything but xml schemas
 */
public class NoExternalEntityResolver implements EntityResolver {

    @Override
    public InputSource resolveEntity(String publicId, String systemId)
            throws SAXException, IOException {
        
        // allow schema parsing for validation.
        if (systemId != null && systemId.matches("(?i)(jar:file|http|vfs)[^?#;]*\\.xsd")) {
            return null;
        }
        
        // do not allow external entities
        throw new SAXException("Entity resolution is not allowed! " + systemId);
    }

}
