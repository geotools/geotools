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

public class SeShape {

    public static int TYPE_NIL = -1;
    public static int TYPE_POINT = 0;
    public static int TYPE_MULTI_POINT = 1;
    public static int TYPE_LINE = 2;
    public static int TYPE_SIMPLE_LINE = 3;
    public static int TYPE_MULTI_LINE = 4;
    public static int TYPE_MULTI_SIMPLE_LINE = 5;
    public static int TYPE_POLYGON = 6;
    public static int TYPE_MULTI_POLYGON = 7;

    public SeShape() {}

    public SeShape(SeCoordinateReference s) throws SeException {}

    public boolean isNil() {
        return false;
    }

    public double[][][] getAllCoords() {
        return null;
    }

    public void generatePoint(int i, SDEPoint[] p) throws SeException {}

    public void generateLine(int i, int j, int[] k, SDEPoint[] p) throws SeException {}

    public void generatePolygon(int i, int j, int[] k, SDEPoint[] p) throws SeException {}

    public void generateRectangle(SeExtent x) throws SeException {}

    public int getType() {
        return 0;
    }

    public SeObjectId getFeatureId() throws SeException {
        return null;
    }

    public int getNumOfPoints() {
        return 0;
    }

    public void generateSimpleLine(
            int numPts, int numParts, int[] partOffsets, SDEPoint[] ptArray) {}
}
