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

import com.esri.sde.sdk.pe.PeCoordinateSystem;

public class SeCoordinateReference {

    public void setCoordSysByDescription(String s) {}

    public String getCoordSysDescription() {
        return null;
    }

    public String getProjectionDescription() {
        return null;
    }

    public double getXYUnits() {
        return 0;
    }

    public SeExtent getXYEnvelope() throws SeException {
        return null;
    }

    public PeCoordinateSystem getCoordSys() {
        return null;
    }

    public void setXY(double i, double j, double k) {}

    public void setXYByEnvelope(SeExtent s) {}

    public void setPrecision(int precision) {}

    public void setCoordSysByID(SeObjectId seObjectId) {
        // TODO Auto-generated method stub

    }
}
