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
 * The dimension modes supported by HANA.
 *
 * @author Stefan Uhrig, SAP SE
 */
public enum XyzmMode {
    XY(2, false, false),
    XYZ(3, true, false),
    XYM(3, false, true),
    XYZM(4, true, true);

    XyzmMode(int coordinatesPerPoint, boolean hasZ, boolean hasM) {
        this.coordinatesPerPoint = coordinatesPerPoint;
        this.hasZ = hasZ;
        this.hasM = hasM;
    }

    private int coordinatesPerPoint;

    private boolean hasZ;

    private boolean hasM;

    public int getCoordinatesPerPoint() {
        return coordinatesPerPoint;
    }

    public boolean hasZ() {
        return hasZ;
    }

    public boolean hasM() {
        return hasM;
    }
}
