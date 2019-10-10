/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
 *
 */

package com.esri.sde.sdk.client;

public class SeExtent extends SeServerObj {

    public SeExtent() {}

    public SeExtent(double i, double j, double k, double l) {}

    public SeExtent(double i, double j, double k, double l, double m, double n) {}

    public double getMinX() {
        return 0.0;
    }

    public double getMaxX() {
        return 0.0;
    }

    public double getMinY() {
        return 0.0;
    }

    public double getMaxY() {
        return 0.0;
    }

    public double getMinZ() {
        return 0.0;
    }

    public double getMaxZ() {
        return 0.0;
    }

    public void setMinX(double d) {}

    public void setMaxX(double d) {}

    public void setMinY(double d) {}

    public void setMaxY(double d) {}

    public void setMinZ(double d) {}

    public void setMaxZ(double d) {}

    public boolean isEmpty() {
        return true;
    }
}
