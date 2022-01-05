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

package org.geotools.data.sqlserver.reader;

/** @author Anders Bakkevold, Bouvet */
public class Shape {

    private int parentOffset;
    private int figureOffset;
    private Type type;

    public Shape(int parentOffset, int figureOffset, int type) {
        this.parentOffset = parentOffset;
        this.figureOffset = figureOffset;
        this.type = Type.findType(type);
    }

    public int getParentOffset() {
        return parentOffset;
    }

    public int getFigureOffset() {
        return figureOffset;
    }

    public Type getType() {
        return type;
    }
}
