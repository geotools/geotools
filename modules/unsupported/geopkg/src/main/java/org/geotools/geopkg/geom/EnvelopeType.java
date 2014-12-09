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

/**
 * EnvelopeType specified in the header of a Geometry (see Geopackage specs)
 * 
 * @author Justin Deoliveira
 * @author Niels Charlier
 */
public enum EnvelopeType {
    NONE(0, 0), XY(1, 32), XYZ(2, 48), XYM(3, 48), XYZM(4, 64);

    private byte value;
    private byte length;

    private EnvelopeType(int value, int length) {
        this.value = (byte) value;
        this.length = (byte) length;
    }

    public static EnvelopeType valueOf(byte b) {
        for (EnvelopeType et : values()) {
            if (et.value == b) return et;
        }
        return null;
    }

    public byte getValue() {
        return value;
    }

    public byte getLength() {
        return length;
    }
}
