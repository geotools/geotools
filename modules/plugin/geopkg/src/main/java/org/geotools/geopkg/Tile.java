/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2010, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geopkg;

import java.util.Arrays;

/**
 * A single tile from a geopackage tiles layer.
 *
 * @author Justin Deoliveira
 * @author Niels Charlier
 */
public class Tile {

    Integer zoom, column, row;
    byte[] data;

    public Tile() {}

    public Tile(Integer zoom, Integer column, Integer row, byte[] data) {
        super();
        this.zoom = zoom;
        this.column = column;
        this.row = row;
        this.data = data;
    }

    public Integer getZoom() {
        return zoom;
    }

    public void setZoom(Integer zoom) {
        this.zoom = zoom;
    }

    public Integer getColumn() {
        return column;
    }

    public void setColumn(Integer column) {
        this.column = column;
    }

    public Integer getRow() {
        return row;
    }

    public void setRow(Integer row) {
        this.row = row;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (column == null ? 0 : column.hashCode());
        result = prime * result + Arrays.hashCode(data);
        result = prime * result + (row == null ? 0 : row.hashCode());
        result = prime * result + (zoom == null ? 0 : zoom.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Tile other = (Tile) obj;
        if (column == null) {
            if (other.column != null) return false;
        } else if (!column.equals(other.column)) return false;
        if (!Arrays.equals(data, other.data)) return false;
        if (row == null) {
            if (other.row != null) return false;
        } else if (!row.equals(other.row)) return false;
        if (zoom == null) {
            if (other.zoom != null) return false;
        } else if (!zoom.equals(other.zoom)) return false;
        return true;
    }
}
