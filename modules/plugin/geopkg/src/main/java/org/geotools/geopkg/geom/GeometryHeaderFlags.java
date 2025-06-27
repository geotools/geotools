/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2010, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geopkg.geom;

import org.locationtech.jts.io.ByteOrderValues;

/**
 * The Geopackage Geometry BLOB Header Flags (see Geopackage specs).
 *
 * @author Justin Deoliveira
 * @author Niels Charlier
 */
public class GeometryHeaderFlags {

    /**
     * GeoPackage Binary Type inside Geometry Header Flags.
     *
     * @author Niels Charlier
     */
    public enum GeopackageBinaryType {
        StandardGeoPackageBinary(0),
        ExtendedGeoPackageBinary(1);
        private byte value;

        private GeopackageBinaryType(int value) {
            this.value = (byte) value;
        }

        public byte getValue() {
            return value;
        }

        public static GeopackageBinaryType valueOf(byte b) {
            for (GeopackageBinaryType et : values()) {
                if (et.value == b) return et;
            }
            return null;
        }
    }

    private byte b;

    private static byte MASK_BINARY_TYPE = (byte) 0x20; // 00100000
    private static byte MASK_EMPTY = (byte) 0x10; // 00010000
    private static byte MASK_ENVELOPE_IND = (byte) 0x0e; // 00001110
    private static byte MASK_ENDIANESS = (byte) 0x01; // 00000001

    public GeometryHeaderFlags(byte b) {
        this.b = b;
    }

    public EnvelopeType getEnvelopeIndicator() {
        return EnvelopeType.valueOf((byte) ((b & MASK_ENVELOPE_IND) >> 1));
    }

    public void setEnvelopeIndicator(EnvelopeType e) {
        b |= e.getValue() << 1 & MASK_ENVELOPE_IND;
    }

    public int getEndianess() {
        return (b & MASK_ENDIANESS) == 1 ? ByteOrderValues.LITTLE_ENDIAN : ByteOrderValues.BIG_ENDIAN;
    }

    public void setEndianess(int endian) {
        byte e = (byte) (endian == ByteOrderValues.LITTLE_ENDIAN ? 1 : 0);
        b |= e & MASK_ENDIANESS;
    }

    public boolean isEmpty() {
        return (b & MASK_EMPTY) == MASK_EMPTY;
    }

    public void setEmpty(boolean empty) {
        if (empty) {
            b |= MASK_EMPTY;
        } else {
            b &= ~MASK_EMPTY;
        }
    }

    public GeopackageBinaryType getBinaryType() {
        return GeopackageBinaryType.valueOf((byte) ((b & MASK_BINARY_TYPE) >> 1));
    }

    public void setBinaryType(GeopackageBinaryType binaryType) {
        b |= binaryType.getValue() << 1 & MASK_BINARY_TYPE;
    }

    public byte toByte() {
        return b;
    }
}
