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

public class SeColumnDefinition {

    public SeColumnDefinition(String s, int i, int j, int k, boolean b) throws SeException {}

    public static /* GEOT-947 final*/ int TYPE_SMALLINT = 0;
    public static /* GEOT-947 final*/ int TYPE_INTEGER = 1;
    public static /* GEOT-947 final*/ int TYPE_FLOAT = 2;
    public static /* GEOT-947 final*/ int TYPE_DOUBLE = 3;
    public static /* GEOT-947 final*/ int TYPE_STRING = 4;
    public static /* GEOT-947 final*/ int TYPE_DATE = 5;
    public static /* GEOT-947 final*/ int TYPE_SHAPE = 6;
    public static /* GEOT-947 final*/ int TYPE_INT16 = 7;
    public static /* GEOT-947 final*/ int TYPE_INT32 = 8;
    public static /* GEOT-947 final*/ int TYPE_INT64 = 9;
    public static /* GEOT-947 final*/ int TYPE_FLOAT32 = 10;
    public static /* GEOT-947 final*/ int TYPE_FLOAT64 = 11;
    public static /* GEOT-947 final*/ int TYPE_BLOB = 12;
    public static /* GEOT-947 final*/ int TYPE_RASTER = 13;
    public static /* GEOT-947 final*/ int TYPE_NSTRING = 14;
    public static /* GEOT-947 final*/ int TYPE_UUID = 15;
    public static int TYPE_CLOB = 1;
    public static int TYPE_NCLOB = 1;

    public String getName() {
        return null;
    }

    public int getType() {
        return 0;
    }

    public int getSize() {
        return 0;
    }

    public short getScale() {
        return 0;
    }

    public short getRowIdType() {
        return 0;
    }

    public boolean allowsNulls() {
        return false;
    }
}
