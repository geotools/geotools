/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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


/**
 * An "object type" in a URN.
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
final class URN_Type {
    /**
     * List of object types. An object type is for example {@code "crs"} in
     * <code>"urn:ogc:def:<b>crs</b>:EPSG:6.8"</code>.
     */
    private static final URN_Type[] TYPES = {
        new URN_Type("crs",                 CRSAuthorityFactory                .class),
        new URN_Type("datum",               DatumAuthorityFactory              .class),
        new URN_Type("meridian",            DatumAuthorityFactory              .class),
        new URN_Type("ellipsoid",           DatumAuthorityFactory              .class),
        new URN_Type("cs",                  CSAuthorityFactory                 .class),
        new URN_Type("axis",                CSAuthorityFactory                 .class),
        new URN_Type("coordinateOperation", CoordinateOperationAuthorityFactory.class),
        new URN_Type("method",              CoordinateOperationAuthorityFactory.class),
        new URN_Type("parameter",           CoordinateOperationAuthorityFactory.class),
        new URN_Type("group",               CoordinateOperationAuthorityFactory.class),
//      new URN_Type("derivedCRSType",      ),
        new URN_Type("verticalDatumType",   VerticalDatumType                  .class),
        new URN_Type("pixelInCell",         PixelInCell                        .class),
        new URN_Type("rangeMeaning",        RangeMeaning                       .class),
        new URN_Type("axisDirection",       AxisDirection                      .class),
        new URN_Type("uom",                 CSAuthorityFactory                 .class)
    };

    /**
     * Subset of {@link #TYPES} for the main ones.
     */
    static final URN_Type[] MAIN = {
        TYPES[0], TYPES[1], TYPES[4], TYPES[6]
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
    private URN_Type(final String name, final Class<?> type) {
        this.name = name;
        this.type = type;
    }

    /**
     * Returns an instance of the specified name (case-insensitive), or {@code null} if none.
     */
    public static URN_Type get(final String name) {
        for (int i=0; i<TYPES.length; i++) {
            final URN_Type candidate = TYPES[i];
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
