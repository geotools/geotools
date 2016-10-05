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
package org.geotools.xml;

import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.util.logging.Logging;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.ext.EntityResolver2;


/**
 * EntityResolver implementation to prevent use external entity resolution to local files.
 * 
 * When parsing an XML entity reference to a local file a SAXException is thrown, which can be
 * handled appropriately.
 * 
 * @author Davide Savazzi - geo-solutions.it
 */
public class NoExternalEntityResolver implements EntityResolver2, Serializable {

    private static final long serialVersionUID = -8477717248646603150L;
    public static final String ERROR_MESSAGE_BASE = "Entity resolution disallowed for ";
    private static final Logger LOGGER = Logging.getLogger(NoExternalEntityResolver.class);
    public static final NoExternalEntityResolver INSTANCE = new NoExternalEntityResolver();
    
    protected NoExternalEntityResolver() {
        // singleton
    }
    
    @Override
    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
        if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.finest("resolveEntity request: publicId=" + publicId + ", systemId=" + systemId);
        }
        
        // allow schema parsing for validation.
        // http(s) - external schema reference
        // jar - internal schema reference
        // vfs - internal schema reference (JBOSS)
        if (systemId != null && systemId.matches("(?i)(jar:file|http|vfs)[^?#;]*\\.xsd")) {
            return null;
        }
        
        // do not allow external entities
        throw new SAXException(ERROR_MESSAGE_BASE + systemId);
    }

    @Override
    public InputSource getExternalSubset(String name, String baseURI)
            throws SAXException, IOException {
        return null;
    }

    @Override
    public InputSource resolveEntity(String name, String publicId, String baseURI, String systemId)
            throws SAXException, IOException {
        return resolveEntity(publicId, systemId);
    }
}