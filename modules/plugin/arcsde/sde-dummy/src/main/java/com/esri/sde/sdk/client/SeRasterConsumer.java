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

public interface SeRasterConsumer {

    public static int COMPLETETILES = 0;
    public static int SINGLEFRAMEDONE = 1;
    public static int STATICIMAGEDONE = 2;
    public static int IMAGEERROR = 3;

    public void setHints(int h);

    public void setScanLines(int l, byte[] d, byte[] b);

    public void rasterComplete(int status);
}
