/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015-2017, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.tile;

import org.geotools.tile.impl.ZoomLevel;

/**
 * A TileIdentifier locates a tile in the grid space of a given tile server by giving its column, row and zoom level.
 * The main responsibility of a TileIdentifier is to translate the grid values (zoom, x, y) into a "code" using an
 * algorithm which denotes the tile in a given server implementation.
 *
 * <p>For example, OpenStreetMap identifies the tile by z/x/y.png; Bing Maps uses a quad key representation. <br>
 * This class formerly known as "WMTTileName".
 *
 * @author Tobias Sauerwein
 * @author Ugo Taddei
 * @since 12
 */
public abstract class TileIdentifier {

    private int x;

    private int y;

    private ZoomLevel zoomLevel;

    private String serviceName;

    public TileIdentifier(int x, int y, ZoomLevel zoomLevel, String serviceName) {

        setX(x);
        setY(y);
        setZomLevel(zoomLevel);
        setServiceName(serviceName);
    }

    private void setServiceName(String serviceName) {
        if (serviceName == null) {
            throw new IllegalArgumentException("Service name cannot be null");
        }
        this.serviceName = serviceName;
    }

    private void setX(int x) {
        if (x < 0) {
            throw new IllegalArgumentException("X must be >= 0 (" + x + ")");
        }
        this.x = x;
    }

    private void setY(int y) {
        if (y < 0) {
            throw new IllegalArgumentException("Y must be >= 0 (" + y + ")");
        }
        this.y = y;
    }

    private void setZomLevel(ZoomLevel zoomLevel) {
        if (zoomLevel == null) {
            throw new IllegalArgumentException("Zoom level cannot be null");
        }
        this.zoomLevel = zoomLevel;
    }

    /**
     * Gets the zoom level (aka "level of detail").
     *
     * <p>Most tile services offer zoom level in the range between 0 (whole world) to 22 (street level). The exact range
     * depends on the service implementation
     *
     * @return the zoom level
     */
    public int getZ() {
        return this.zoomLevel.getZoomLevel();
    }

    /** Gets the column value of a tile. */
    public int getX() {
        return x;
    }

    /** Gets the row value of a tile. */
    public int getY() {
        return y;
    }

    /** Gets the row value of a tile. */
    public ZoomLevel getZoomLevel() {
        return this.zoomLevel;
    }

    public String getServiceName() {
        return this.serviceName;
    }

    /**
     * Arithmetic implementation of modulo, as the Java implementation of modulo can return negative values.
     *
     * <pre>
     * arithmeticMod(-1, 8) = 7
     * </pre>
     *
     * @return the positive remainder
     */
    public static final int arithmeticMod(int a, int b) {
        return a >= 0 ? a % b : a % b + b;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof TileIdentifier)) {
            return false;
        }

        return this.getId().equals(((TileIdentifier) other).getId());
    }

    @Override
    public int hashCode() {
        return this.getId().hashCode();
    }

    @Override
    public String toString() {
        return this.getId();
    }

    /**
     * Gets the id of a tile, which can be used for caching purposes.
     *
     * <p>The id is a file-friendly name (that is, should contains no special characters such as ".", "/", etc. The id
     * should be build from the code (which also uniquely identifies a tile, but, in some service implementation may
     * contain file-unfriendly characters (e.g. OpenStreetMap: 5/16/10.png).
     *
     * <p>When building an id, you should use the service name as a prefix (e.g. for OpenStreetMap: "Mapnik",
     * "CycleMap"; Bing Maps: "Road", "Hybrid"; etc) and suffix the id with a file-friendly string (e.g. OpenStreetMap:
     * "Mapnik_X_Y_Z").
     */
    public abstract String getId();

    /**
     * Gets the code of a tile.
     *
     * <p>The id is a string which uniquely identifies a tile. In some service implementations this is a quadkey (e.g.
     * Bing Maps: "03123") or the fragment of the tile image (e.g. OpenStreetMap: 5/16/10.png, for Z/X/Y.png).
     *
     * <p>
     *
     * @return the code
     */
    public abstract String getCode();

    public abstract TileIdentifier getRightNeighbour();

    public abstract TileIdentifier getLowerNeighbour();
}
