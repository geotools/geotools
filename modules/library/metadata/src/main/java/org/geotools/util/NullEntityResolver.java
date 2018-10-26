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
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.ext.EntityResolver2;

/**
 * NullObject implementation for {@link EntityResolver2} (used as an alternative to null checks).
 *
 * <p>This implementation returns {@code null} to request that the parser open a regular URI
 * connection to the system identifier.
 *
 * @author Jody Garnett (Boundless)
 */
public class NullEntityResolver implements EntityResolver2, Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1440071882791317708L;

    /** Singleton instance of PreventLocalEntityResolver */
    public static final NullEntityResolver INSTANCE = new NullEntityResolver();

    protected NullEntityResolver() {
        // singleton
    }

    /**
     * @return {@code null} to request that the parser open a regular URI connection to the system
     *     identifier.
     */
    @Override
    public InputSource resolveEntity(String publicId, String systemId)
            throws SAXException, IOException {
        return null;
    }

    /*
     * @return {@code null} to indicate that no external subset is provided.
     */
    @Override
    public InputSource getExternalSubset(String name, String baseURI)
            throws SAXException, IOException {
        return null;
    }

    /*
     * @return {@code null} directs the parser to resolve the system ID against the base URI and
     * open a connection to resulting URI.
     */
    @Override
    public InputSource resolveEntity(String name, String publicId, String baseURI, String systemId)
            throws SAXException, IOException {
        return null;
    }
}
