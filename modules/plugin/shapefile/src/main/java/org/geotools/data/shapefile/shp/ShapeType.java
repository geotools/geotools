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
package org.geotools.data.shapefile.shp;

import org.locationtech.jts.geom.GeometryFactory;

/**
 * Not much but a type safe enumeration of file types as ints and names. The descriptions can easily
 * be tied to a ResourceBundle if someone wants to do that.
 *
 * @author Ian Schneider
 */
public enum ShapeType {

    /** Represents a Null shape (id = 0). */
    NULL(0, "Null"),
    /** Represents a Point shape (id = 1). */
    POINT(1, "Point"),
    /** Represents a PointZ shape (id = 11). */
    POINTZ(11, "PointZ"),
    /** Represents a PointM shape (id = 21). */
    POINTM(21, "PointM"),
    /** Represents an Arc shape (id = 3). */
    ARC(3, "Arc"),
    /** Represents an ArcZ shape (id = 13). */
    ARCZ(13, "ArcZ"),
    /** Represents an ArcM shape (id = 23). */
    ARCM(23, "ArcM"),
    /** Represents a Polygon shape (id = 5). */
    POLYGON(5, "Polygon"),
    /** Represents a PolygonZ shape (id = 15). */
    POLYGONZ(15, "PolygonZ"),
    /** Represents a PolygonM shape (id = 25). */
    POLYGONM(25, "PolygonM"),
    /** Represents a MultiPoint shape (id = 8). */
    MULTIPOINT(8, "MultiPoint"),
    /** Represents a MultiPointZ shape (id = 18). */
    MULTIPOINTZ(18, "MultiPointZ"),
    /** Represents a MultiPointM shape (id = 28). */
    MULTIPOINTM(28, "MultiPointM"),

    /** Represents an Undefined shape (id = -1). */
    UNDEFINED(-1, "Undefined");

    /** The integer id of this ShapeType. */
    public final int id;
    /**
     * The human-readable name for this ShapeType.<br>
     * Could easily use ResourceBundle for internationialization.
     */
    public final String name;

    /**
     * Creates a new instance of ShapeType. Hidden on purpose.
     *
     * @param id The id.
     * @param name The name.
     */
    private ShapeType(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Get the name of this ShapeType.
     *
     * @return The name.
     */
    public String toString() {
        return name;
    }

    /**
     * Is this a multipoint shape? Hint- all shapes are multipoint except NULL, UNDEFINED, and the
     * POINTs.
     *
     * @return true if multipoint, false otherwise.
     */
    public boolean isMultiPoint() {
        if (this == UNDEFINED) {
            return false;
        } else if (this == NULL) {
            return false;
        } else if (this == POINT || this == POINTM || this == POINTZ) {
            return false;
        }
        return true;
    }

    public boolean isPointType() {
        return id % 10 == 1;
    }

    public boolean isLineType() {
        return id % 10 == 3;
    }

    public boolean isPolygonType() {
        return id % 10 == 5;
    }

    public boolean isMultiPointType() {
        return id % 10 == 8;
    }

    /**
     * Determine the ShapeType for the id.
     *
     * @param id The id to search for.
     * @return The ShapeType for the id.
     */
    public static ShapeType forID(int id) {
        switch (id) {
            case 0:
                return NULL;
            case 1:
                return POINT;
            case 11:
                return POINTZ;
            case 21:
                return POINTM;
            case 3:
                return ARC;
            case 13:
                return ARCZ;
            case 23:
                return ARCM;
            case 5:
                return POLYGON;
            case 15:
                return POLYGONZ;
            case 25:
                return POLYGONM;
            case 8:
                return MULTIPOINT;
            case 18:
                return MULTIPOINTZ;
            case 28:
                return MULTIPOINTM;
            default:
                return UNDEFINED;
        }
    }

    /**
     * Each ShapeType corresponds to a handler. In the future this should probably go else where to
     * allow different handlers, or something...
     *
     * @throws ShapefileException If the ShapeType is bogus.
     * @return The correct handler for this ShapeType. Returns a new one.
     */
    public ShapeHandler getShapeHandler(GeometryFactory gf) throws ShapefileException {
        switch (id) {
            case 1:
            case 11:
            case 21:
                return new PointHandler(this, gf);
            case 3:
            case 13:
            case 23:
                return new MultiLineHandler(this, gf);
            case 5:
            case 15:
            case 25:
                return new PolygonHandler(this, gf);
            case 8:
            case 18:
            case 28:
                return new MultiPointHandler(this, gf);
            default:
                return null;
        }
    }
}
