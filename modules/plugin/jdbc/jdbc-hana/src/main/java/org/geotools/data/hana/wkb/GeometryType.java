/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.hana.wkb;

/**
 * The geometry types supported by HANA.
 *
 * @author Stefan Uhrig, SAP SE
 */
public enum GeometryType {
    POINT(1),
    LINESTRING(2),
    POLYGON(3),
    MULTIPOINT(4),
    MULTILINESTRING(5),
    MULTIPOLYGON(6),
    GEOMETRYCOLLECTION(7),
    CIRCULARSTRING(8);

    GeometryType(int typeCode) {
        this.typeCode = typeCode;
    }

    private int typeCode;

    public int getTypeCode() {
        return typeCode;
    }

    public static GeometryType getFromCode(int code) {
        for (GeometryType type : GeometryType.values()) {
            if (type.typeCode == code) {
                return type;
            }
        }
        return null;
    }
}
