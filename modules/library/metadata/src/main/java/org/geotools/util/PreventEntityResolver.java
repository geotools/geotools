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
import java.io.Serial;
import java.io.Serializable;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.ext.EntityResolver2;

/**
 * {@link EntityResolver2} implementation that disallows access to any external entity.
 *
 * @author Jody Garnett (GeoCat)
 */
public class PreventEntityResolver implements EntityResolver3, Serializable {

    /** serialVersionUID */
    @Serial
    private static final long serialVersionUID = 1440071882791317718L;

    /** Singleton instance of PreventLocalEntityResolver */
    public static final PreventEntityResolver INSTANCE = new PreventEntityResolver();

    protected PreventEntityResolver() {
        // singleton
    }

    /**
     * PreventEntityResolver does not allow access to any protocols.
     *
     * <p>This encourages XMLUtils to disable all factory external entity settings.
     *
     * @return {@code ""}
     */
    @Override
    public String getAccess() {
        return "";
    }

    /** @return {@code null} to request that the parser open a regular URI connection to the system identifier. */
    @Override
    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
        throw new SAXException(PreventLocalEntityResolver.ERROR_MESSAGE_BASE + systemId);
    }

    /*
     * @return {@code null} to indicate that no external subset is provided.
     */
    @Override
    public InputSource getExternalSubset(String name, String baseURI) throws SAXException, IOException {
        throw new SAXException(PreventLocalEntityResolver.ERROR_MESSAGE_BASE + baseURI + " subset " + name);
    }

    /*
     * @return {@code null} directs the parser to resolve the system ID against the base URI and
     * open a connection to resulting URI.
     */
    @Override
    public InputSource resolveEntity(String name, String publicId, String baseURI, String systemId)
            throws SAXException, IOException {
        throw new SAXException(PreventLocalEntityResolver.ERROR_MESSAGE_BASE + systemId);
    }

    @Override
    public String toString() {
        return "PreventEntityResolver";
    }
}
