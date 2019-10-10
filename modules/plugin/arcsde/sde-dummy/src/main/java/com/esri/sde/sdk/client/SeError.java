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

public class SeError {

    public static int SE_NO_PERMISSIONS = 0;
    public static int SE_TABLE_NOREGISTERED = 0;
    public static int SE_STATE_INUSE = 1;

    public String getErrDesc() {
        return null;
    }

    public int getSdeError() {
        return -1;
    }

    public String getSdeErrMsg() {
        return null;
    }

    public int getExtError() {
        return 0;
    }

    public String getExtErrMsg() {
        return null;
    }
}
