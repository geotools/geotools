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

import java.text.MessageFormat;
import org.geotools.api.referencing.NoSuchAuthorityCodeException;
import org.geotools.metadata.i18n.ErrorKeys;
import org.geotools.util.Version;

/**
 * Split an HTTP URI into its parts for {@link HTTP_URI_AuthorityFactory}. Must be immutable so synchronisation is not
 * needed in the authority factory.
 *
 * @author Martin Desruisseaux
 * @author Ben Caradoc-Davies (CSIRO Earth Science and Resource Engineering)
 */
final class HTTP_URI_Parser extends URI_Parser {

    /** The citation authority. */
    private static final String AUTHORITY = "http://www.opengis.net/def";

    /** The segment separator. */
    private static final String SEPARATOR = "/";

    /** The start of the URI that will be removed during parsing. */
    private static final String BASE_URI = AUTHORITY + SEPARATOR;

    /** The version segment of "0" is used to indicate an unversioned resource. */
    private static final String UNVERSIONED = "0";

    /**
     * Constructor.
     *
     * @param uri the full URI string
     * @param type the resource type, for example "crs"
     * @param authority the resource authority, for example "EPSG"
     * @param version the version of the resource or null if none
     * @param code the resource code
     */
    HTTP_URI_Parser(String uri, URI_Type type, String authority, Version version, String code) {
        super(uri, type, authority, version, code);
    }

    /**
     * Parses an OGC HTTP URI.
     *
     * @param uri The HTTP URI to parse.
     * @throws NoSuchAuthorityCodeException if the URI syntax is invalid.
     * @todo Implementation should be replaced by some mechanism using {@code GenericName} (at least the call to
     *     {@code String.regionMatches}) otherwise this method will fails if there is spaces around the separator.
     */
    public static HTTP_URI_Parser buildParser(final String uri) throws NoSuchAuthorityCodeException {
        String uriText = uri.trim();
        int length = BASE_URI.length();
        if (uriText.regionMatches(true, 0, BASE_URI, 0, length)) {
            String[] segments = uriText.substring(length).split(SEPARATOR);
            if (segments.length == 4 && !segments[0].isEmpty()) {
                URI_Type uriType = URI_Type.get(segments[0]);
                if (uriType != null && !segments[1].isEmpty() && !segments[2].isEmpty() && !segments[3].isEmpty()) {
                    String uriAuthority = segments[1];
                    Version uriVersion = segments[2].equals(UNVERSIONED) ? null : new Version(segments[2]);
                    String uriCode = segments[3];
                    return new HTTP_URI_Parser(uriText, uriType, uriAuthority, uriVersion, uriCode);
                }
            }
        }
        throw new NoSuchAuthorityCodeException(
                MessageFormat.format(ErrorKeys.ILLEGAL_ARGUMENT_$1, uriText), AUTHORITY, uriText);
    }
}
