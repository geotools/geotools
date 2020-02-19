/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.sqlserver.reader;

/** Segment enumeration */
public enum Segment {
    LINE(0),
    ARC(1),
    FIRST_LINE(2),
    FIRST_ARC(3);

    private int value;

    private Segment(int value) {
        this.value = value;
    }

    public static Segment findSegment(int value) {
        for (Segment segment : Segment.values()) {
            if (segment.value == value) {
                return segment;
            }
        }
        throw new IllegalArgumentException();
    }
}
