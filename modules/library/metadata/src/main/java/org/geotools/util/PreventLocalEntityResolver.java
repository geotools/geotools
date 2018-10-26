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
package org.geotools.util;

import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.util.factory.GeoTools;
import org.geotools.util.logging.Logging;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.ext.EntityResolver2;
import org.xml.sax.helpers.DefaultHandler;

/**
 * EntityResolver implementation to prevent use external entity resolution to local files.
 *
 * <p>When parsing an XML entity reference to a local file a SAXException is thrown, which can be
 * handled appropriately.
 *
 * <p>This implementation is both recommended and the default returned by {@link
 * GeoTools#getEntityResolver()}.
 *
 * @author Davide Savazzi - geo-solutions.it
 */
public class PreventLocalEntityResolver implements EntityResolver2, Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = -5689036455423814933L;

    /** Prefix used for SAXException message */
    public static final String ERROR_MESSAGE_BASE = "Entity resolution disallowed for ";

    protected static final Logger LOGGER = Logging.getLogger(PreventLocalEntityResolver.class);

    /** Singleton instance of PreventLocalEntityResolver */
    public static final PreventLocalEntityResolver INSTANCE = new PreventLocalEntityResolver();

    protected PreventLocalEntityResolver() {
        // singleton
    }

    @Override
    public InputSource resolveEntity(String publicId, String systemId)
            throws SAXException, IOException {
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

    /**
     * Tells the parser to resolve the systemId against the baseURI and read the entity text from
     * that resulting absolute URI. Note that because the older {@link DefaultHandler#resolveEntity
     * DefaultHandler.resolveEntity()}, method is overridden to call this one, this method may
     * sometimes be invoked with null <em>name</em> and <em>baseURI</em>, and with the
     * <em>systemId</em> already absolutized.
     */
    @Override
    public InputSource resolveEntity(String name, String publicId, String baseURI, String systemId)
            throws SAXException, IOException {
        if (baseURI != null && baseURI.matches("(?i)(jar:file|http|vfs)[^?#;]*\\.xsd")) {
            return null;
        }
        return resolveEntity(publicId, systemId);
    }
}
