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

import org.opengis.referencing.AuthorityFactory;
import org.opengis.referencing.crs.CRSAuthorityFactory;
import org.opengis.referencing.cs.AxisDirection;
import org.opengis.referencing.cs.CSAuthorityFactory;
import org.opengis.referencing.cs.RangeMeaning;
import org.opengis.referencing.datum.DatumAuthorityFactory;
import org.opengis.referencing.datum.PixelInCell;
import org.opengis.referencing.datum.VerticalDatumType;
import org.opengis.referencing.operation.CoordinateOperationAuthorityFactory;
import org.opengis.util.CodeList;

/**
 * An "object type" in a URI.
 *
 * @author Martin Desruisseaux
 * @author Ben Caradoc-Davies (CSIRO Earth Science and Resource Engineering)
 
 * @source $URL$
 */
final class URI_Type {
    /**
     * List of object types. An object type is for example {@code "crs"} in
     * <code>"urn:ogc:def:<b>crs</b>:EPSG:6.8"</code>.
     * 
     * <p>The canonical source for the list of type names is the
     * <a href="http://www.opengis.net/register/ogc-na/def-type">OGC Naming Authority register of def types</a>.</p>
     */
    private static final URI_Type[] TYPES = {
        new URI_Type("crs",                  CRSAuthorityFactory                .class),
        new URI_Type("datum",                DatumAuthorityFactory              .class),
        new URI_Type("meridian",             DatumAuthorityFactory              .class),
        new URI_Type("ellipsoid",            DatumAuthorityFactory              .class),
        new URI_Type("cs",                   CSAuthorityFactory                 .class),
        new URI_Type("axis",                 CSAuthorityFactory                 .class),
        new URI_Type("coordinateOperation",  CoordinateOperationAuthorityFactory.class), // deprecated
        new URI_Type("coordinate-operation", CoordinateOperationAuthorityFactory.class),
        new URI_Type("method",               CoordinateOperationAuthorityFactory.class),
        new URI_Type("parameter",            CoordinateOperationAuthorityFactory.class),
        new URI_Type("group",                CoordinateOperationAuthorityFactory.class),
        new URI_Type("verticalDatumType",    VerticalDatumType                  .class), // deprecated
        new URI_Type("vertical-datum-type",  VerticalDatumType                  .class),
        new URI_Type("pixelInCell",          PixelInCell                        .class), // deprecated
        new URI_Type("pixel-in-cell",        PixelInCell                        .class),
        new URI_Type("rangeMeaning",         RangeMeaning                       .class), // deprecated
        new URI_Type("range-meaning",        RangeMeaning                       .class),
        new URI_Type("axisDirection",        AxisDirection                      .class), // deprecated
        new URI_Type("axis-direction",       AxisDirection                      .class),
        new URI_Type("uom",                  CSAuthorityFactory                 .class)
    };

    /**
     * The object type name.
     */
    public final String name;

    /**
     * The factory for this type, either as a {@link AuthorityFactory} subinterface
     * or a {@link CodeList}.
     */
    public final Class<?> type;

    /**
     * Creates a new instance of {@code URN_Type}.
     */
    private URI_Type(final String name, final Class<?> type) {
        this.name = name;
        this.type = type;
    }

    /**
     * Returns an instance of the specified name (case-insensitive), or {@code null} if none.
     */
    public static URI_Type get(final String name) {
        for (int i=0; i<TYPES.length; i++) {
            final URI_Type candidate = TYPES[i];
            if (name.equalsIgnoreCase(candidate.name)) {
                return candidate;
            }
        }
        return null;
    }

    /**
     * Returns {@code true} if the specified factory is an instance of this type.
     */
    public boolean isInstance(final AuthorityFactory factory) {
        return type.isInstance(factory);
    }

    /**
     * Returns the type name, for formatting and debugging purpose.
     */
    @Override
    public String toString() {
        return name;
    }

}
