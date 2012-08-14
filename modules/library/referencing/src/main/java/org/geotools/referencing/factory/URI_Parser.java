/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2012, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.referencing.factory;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.geotools.resources.Classes;
import org.geotools.resources.i18n.LoggingKeys;
import org.geotools.resources.i18n.Loggings;
import org.geotools.util.Version;
import org.opengis.referencing.AuthorityFactory;


/**
 * Split a URI into its {@link #type} and {@link #version} parts for {@link Abstract_URI_AuthorityFactory}.
 * This class must be immutable in order to avoid the need for synchronization in the authority
 * factory.
 *
 * @author Martin Desruisseaux
 * @author Ben Caradoc-Davies (CSIRO Earth Science and Resource Engineering)
 * 
 * @source $URL$
 */
abstract class URI_Parser {

    /**
     * Used to join the authority and code in {@link #getAuthorityCode()}getAuthorityCode.
     */
    private static final char AUTHORITY_CODE_SEPARATOR = ':';

    /**
     * The parsed code as full URI.
     */
    public final String uri;

    /**
     * The type part of the URN ({@code "crs"}, {@code "cs"}, {@code "datum"}, <cite>etc</cite>).
     */
    public final URI_Type type;

    /**
     * The authority part of the URI (typically {@code "EPSG"}).
     */
    public final String authority;

    /**
     * The version part of the URI, or {@code null} if none.
     */
    public final Version version;

    /**
     * The code part of the URI.
     */
    public final String code;

    /**
     * Constructor.
     * 
     * @param uri the full URI string
     * @param type the resource type, for example "crs"
     * @param authority the resource authority, for example "EPSG"
     * @param version the version of the resource or null if none
     * @param code the resource code
     */
    protected URI_Parser(String uri, URI_Type type, String authority, Version version, String code) {
        this.uri = uri;
        this.type = type;
        this.authority = authority;
        this.version = version;
        this.code = code;
    }

    /**
     * Returns the concatenation of the {@linkplain #authority} and the {@linkplain #code},
     * separated by {@link #AUTHORITY_CODE_SEPARATOR}.
     */
    public String getAuthorityCode() {
        return authority + AUTHORITY_CODE_SEPARATOR + code;
    }

    /**
     * Checks if the type is compatible with the expected one. This method is used as a safety
     * by {@code getFooAuthorityFactory(String)} methods in {@link URN_AuthorityFactory}. If a
     * mismatch is found, a warning is logged but no exception is thrown since it doesn't prevent
     * the class to work in a predictable way. It is just an indication for the user that his URN
     * may be wrong.
     */
    final void logWarningIfTypeMismatch(AuthorityFactory authorityFactory,
            final Class<? extends AuthorityFactory> expected) {
        if (!expected.isAssignableFrom(type.type)) {
            // Build a simplified URN, omitting "urn:ogc:def" and version number.
            final LogRecord record = Loggings.format(Level.WARNING,
                    LoggingKeys.MISMATCHED_URI_TYPE_$1, uri);
            // Set the source to the public or protected method.
            record.setSourceClassName(authorityFactory.getClass().getName());
            record.setSourceMethodName("get" + Classes.getShortName(expected));
            final Logger logger = AbstractAuthorityFactory.LOGGER;
            record.setLoggerName(logger.getName());
            logger.log(record);
        }
    }

    /**
     * Returns the URN.
     */
    @Override
    public String toString() {
        return uri;
    }
}
