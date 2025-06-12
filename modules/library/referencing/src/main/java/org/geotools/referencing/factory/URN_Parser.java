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
import org.apache.commons.lang3.StringUtils;
import org.geotools.api.referencing.NoSuchAuthorityCodeException;
import org.geotools.metadata.i18n.ErrorKeys;
import org.geotools.util.Version;

/**
 * Split a URN into its {@link #type} and {@link #version} parts for {@link URN_AuthorityFactory}. This class must be
 * immutable in order to avoid the need for synchronization in the authority factory.
 *
 * @author Martin Desruisseaux
 * @author Ben Caradoc-Davies (CSIRO Earth Science and Resource Engineering)
 */
final class URN_Parser extends URI_Parser {

    /**
     * The beginning parts of the URN, typically {@code "urn:ogc:def:"} and {@code "urn:x-ogc:def:"}. All elements in
     * the array are treated as synonymous. Those parts are up to, but do not include, the type part ({@code "crs"},
     * {@code "cs"}, {@code "datum"}, <cite>etc.</cite>). They must include a trailing (@value #URN_SEPARATOR}
     * character.
     */
    private static final String[] URN_BASES = {"urn:ogc:def:", "urn:x-ogc:def:"};

    /** The parts separator in the URN. */
    private static final char URN_SEPARATOR = ':';

    /**
     * Constructor.
     *
     * @param urn the full URN string
     * @param type the resource type, for example "crs"
     * @param authority the resource authority, for example "EPSG"
     * @param version the version of the resource or null if none
     * @param code the resource code
     */
    URN_Parser(String urn, URI_Type type, String authority, Version version, String code) {
        super(urn, type, authority, version, code);
    }

    /**
     * Parses the specified URN.
     *
     * @param urn The URN to parse.
     * @throws NoSuchAuthorityCodeException if the URN syntax is invalid.
     * @todo Implementation should be replaced by some mechanism using {@code GenericName} (at least the call to
     *     {@code String.regionMatches}) otherwise this method will fails if there is spaces around the separator.
     */
    public static URN_Parser buildParser(final String urn) throws NoSuchAuthorityCodeException {
        final String code = urn.trim();
        String type = urn; // To be really assigned later.
        for (final String urnBase : URN_BASES) {
            final int typeStart = urnBase.length();
            if (code.regionMatches(true, 0, urnBase, 0, typeStart)) {
                final int typeEnd = code.indexOf(URN_SEPARATOR, typeStart);
                if (typeEnd >= 0) {
                    type = code.substring(typeStart, typeEnd).trim();
                    final URI_Type candidate = URI_Type.get(type);
                    if (candidate != null) {
                        final int nameEnd = code.indexOf(URN_SEPARATOR, typeEnd + 1);
                        if (nameEnd >= 0) {
                            final int versionEnd = code.indexOf(URN_SEPARATOR, nameEnd + 1);
                            final int lastEnd = code.lastIndexOf(URN_SEPARATOR);
                            Version urnVersion;
                            String urnAuthority;
                            String urnCode;
                            if (versionEnd != lastEnd && versionEnd != -1) {
                                urnVersion = (lastEnd <= nameEnd)
                                        ? null
                                        : new Version(code.substring(nameEnd + 1, versionEnd));
                                urnAuthority = code.substring(typeEnd + 1, nameEnd);
                                urnCode = code.substring(versionEnd + 1).trim();
                                urnCode = urnCode.replaceAll(String.valueOf(URN_SEPARATOR), ",");
                            } else {
                                urnVersion =
                                        (lastEnd <= nameEnd) ? null : new Version(code.substring(nameEnd + 1, lastEnd));
                                urnAuthority =
                                        code.substring(typeEnd + 1, nameEnd).trim();
                                urnCode = code.substring(lastEnd + 1).trim();
                            }
                            // handle empty version
                            urnVersion = StringUtils.isEmpty(urnVersion) ? null : urnVersion;
                            if (urnCode.contains("CRS")) {
                                urnAuthority = "CRS";
                                urnCode = urnCode.substring(3);
                            } else if (urnCode.contains("AUTO")) {
                                urnAuthority = "AUTO";
                                urnCode = urnCode.substring(4);
                            }
                            URI_Type urnType = candidate;
                            return new URN_Parser(urn, urnType, urnAuthority, urnVersion, urnCode);
                        }
                    }
                }
            }
        }
        throw new NoSuchAuthorityCodeException(
                MessageFormat.format(ErrorKeys.ILLEGAL_IDENTIFIER_$1, type), "urn:ogc:def", type);
    }
}
