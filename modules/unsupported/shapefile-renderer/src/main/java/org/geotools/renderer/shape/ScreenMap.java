/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.shape;

/**
 *
 * @source $URL$
 */
public class ScreenMap {
    int[] pixels;
    int width;
    int height;
    private int minx;
    private int miny;

    public ScreenMap(){}
    public ScreenMap(int x, int y, int width, int height) {
        this.width = width;
        this.height = height;
        this.minx=x;
        this.miny=y;

        int arraySize = ((width * height) / 32) + 1;
        pixels = new int[arraySize];
    }

    /**
     * Sets location at position x,y to the value.
     */
    public void set(int x, int y, boolean value) {
        if( (x-minx)<0 || (x-minx)>width-1 ||(y-miny)<0 || (y-miny)>height-1 )
            return;
        int bit = bit(x-minx, y-miny);
        int index = bit / 32;
        int offset = bit % 32;
        int mask = 1;
        mask = mask << offset;

        if (value) {
            pixels[index] = pixels[index] | mask;
        } else {
            int tmp = pixels[index];
            tmp = ~tmp;
            tmp = (tmp | mask);
            tmp = ~tmp;
            pixels[index] = tmp;
        }
    }

    /**
     * Returns true if the pixel at location x,y is set or out of bounds.
     */
    public boolean get(int x, int y) {
        if( (x-minx)<0 || (x-minx)>width-1 ||(y-miny)<0 || (y-miny)>height-1 )
            return true;
        int bit = bit(x-minx, y-miny);
        int index = bit / 32;
        int offset = bit % 32;
        int mask = 1 << offset;

        try {
            return ((pixels[index] & mask) != 0) ? true : false;
        } catch (Exception e) {

            return true;
        }
    }

    private int bit(int x, int y) {
        return (width * y) + x;
    }
}
