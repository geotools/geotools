/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.vpf.ifc;

/**
 * DataTypesDefinition.java Created: Thu Jan 02 17:26:02 2003
 *
 * @author <a href="mailto:kobit@users.sourceforge.net">Artur Hefczyc</a>
 * @source $URL$
 * @version $Id$
 */
public interface DataTypesDefinition {
    // Byte order codes

    /**
     * <code>LEAST_SIGNIF_FIRST</code> stores code for indicator of byte order
     * <code>least-significant-byte-first</code> used during creating table.
     * It is little-endian byte order used on Intel x86 based PCs but not in
     * JVM.
     */
    public static final char LEAST_SIGNIF_FIRST = 'L';

    /** Variable constant <code>LITTLE_ENDIAN_ORDER</code> keeps value of */
    public static final char LITTLE_ENDIAN_ORDER = LEAST_SIGNIF_FIRST;

    /**
     * <code>MOST_SIGNIF_FIRST</code> stores code for indicator of byte order
     * <code>most-significant-byte-first</code> used during creating table. It
     * is big-endian byte order used on Motorola CPU based machines and in
     * JVM.
     */
    public static final char MOST_SIGNIF_FIRST = 'M';

    /** Variable constant <code>BIG_ENDIAN_ORDER</code> keeps value of */
    public static final char BIG_ENDIAN_ORDER = MOST_SIGNIF_FIRST;

    // Data type codes

    /** Variable constant <code>DATA_TEXT</code> keeps value of */
    public static final char DATA_TEXT = 'T';

    /** Variable constant <code>DATA_LEVEL1_TEXT</code> keeps value of */
    public static final char DATA_LEVEL1_TEXT = 'L';

    /** Variable constant <code>DATA_LEVEL2_TEXT</code> keeps value of */
    public static final char DATA_LEVEL2_TEXT = 'N';

    /** Variable constant <code>DATA_LEVEL3_TEXT</code> keeps value of */
    public static final char DATA_LEVEL3_TEXT = 'M';

    /** Variable constant <code>DATA_SHORT_FLOAT</code> keeps value of */
    public static final char DATA_SHORT_FLOAT = 'F';

    /** Variable constant <code>DATA_LONG_FLOAT</code> keeps value of */
    public static final char DATA_LONG_FLOAT = 'R';

    /** Variable constant <code>DATA_SHORT_INTEGER</code> keeps value of */
    public static final char DATA_SHORT_INTEGER = 'S';

    /** Variable constant <code>DATA_LONG_INTEGER</code> keeps value of */
    public static final char DATA_LONG_INTEGER = 'I';

    /** Variable constant <code>DATA_2_COORD_F</code> keeps value of */
    public static final char DATA_2_COORD_F = 'C';

    /** Variable constant <code>DATA_2_COORD_R</code> keeps value of */
    public static final char DATA_2_COORD_R = 'B';

    /** Variable constant <code>DATA_3_COORD_F</code> keeps value of */
    public static final char DATA_3_COORD_F = 'Z';

    /** Variable constant <code>DATA_3_COORD_R</code> keeps value of */
    public static final char DATA_3_COORD_R = 'Y';

    /** Variable constant <code>DATA_DATE_TIME</code> keeps value of */
    public static final char DATA_DATE_TIME = 'D';

    /** Variable constant <code>DATA_NULL_FIELD</code> keeps value of */
    public static final char DATA_NULL_FIELD = 'X';

    /** Variable constant <code>DATA_TRIPLET_ID</code> keeps value of */
    public static final char DATA_TRIPLET_ID = 'K';

    /** Variable constant <code>DATA_SHORT_FLOAT_LEN</code> keeps value of */
    public static final int DATA_SHORT_FLOAT_LEN = 4;

    /** Variable constant <code>DATA_LONG_FLOAT_LEN</code> keeps value of */
    public static final int DATA_LONG_FLOAT_LEN = 8;

    /** Variable constant <code>DATA_SHORT_INTEGER_LEN</code> keeps value of */
    public static final int DATA_SHORT_INTEGER_LEN = 2;

    /** Variable constant <code>DATA_LONG_INTEGER_LEN</code> keeps value of */
    public static final int DATA_LONG_INTEGER_LEN = 4;

    /** Variable constant <code>DATA_DATE_TIME_LEN</code> keeps value of */
    public static final int DATA_DATE_TIME_LEN = 20;

    /** Variable constant <code>DATA_2_COORD_F_LEN</code> keeps value of */
    public static final int DATA_2_COORD_F_LEN = 8;

    /** Variable constant <code>DATA_2_COORD_R_LEN</code> keeps value of */
    public static final int DATA_2_COORD_R_LEN = 16;

    /** Variable constant <code>DATA_3_COORD_F_LEN</code> keeps value of */
    public static final int DATA_3_COORD_F_LEN = 12;

    /** Variable constant <code>DATA_3_COORD_R_LEN</code> keeps value of */
    public static final int DATA_3_COORD_R_LEN = 24;

    /** Variable constant <code>DATA_TRIPLET_ID_LEN</code> keeps value of */
    public static final int DATA_TRIPLET_ID_LEN = 1;

    /** Variable constant <code>DATA_NULL_FIELD_LEN</code> keeps value of */
    public static final int DATA_NULL_FIELD_LEN = 0;

    /** Variable constant <code>STRING_NULL_VALUE</code> keeps value of */
    public static final String STRING_NULL_VALUE = "-";

    /** Variable constant <code>STRING_NULL_VALUES</code> keeps value of */
    public static final String[] STRING_NULL_VALUES = { "-", "--", "N/A" };

    /** Variable constant <code>CHAR_NULL_VALUE</code> keeps value of */
    public static final char CHAR_NULL_VALUE = '-';
}

// DataTypesDefinition
