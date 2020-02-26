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

import java.util.ArrayList;
import java.util.List;
import org.geotools.geometry.jts.ReferencedEnvelope;

/**
 * Tiles Entry inside a GeoPackage.
 *
 * @author Justin Deoliveira
 * @author Niels Charlier
 */
public class TileEntry extends Entry {

    List<TileMatrix> tileMatricies = new ArrayList();

    ReferencedEnvelope tileMatrixSetBounds;

    public TileEntry() {
        setDataType(DataType.Tile);
    }

    public List<TileMatrix> getTileMatricies() {
        return tileMatricies;
    }

    void setTileMatricies(List<TileMatrix> tileMatricies) {
        this.tileMatricies = tileMatricies;
    }

    void init(TileEntry e) {
        super.init(e);
        setTileMatricies(e.getTileMatricies());
        this.tileMatrixSetBounds =
                e.tileMatrixSetBounds == null
                        ? null
                        : new ReferencedEnvelope(e.tileMatrixSetBounds);
    }

    /**
     * Returns the tile matrix set bounds. The bounds are expressed in the same CRS as the entry,
     * but they might differ in extent (if null, then the tile matrix bounds are supposed to be the
     * same as the entry)
     */
    public ReferencedEnvelope getTileMatrixSetBounds() {
        return tileMatrixSetBounds != null ? tileMatrixSetBounds : bounds;
    }

    public void setTileMatrixSetBounds(ReferencedEnvelope tileMatrixSetBounds) {
        this.tileMatrixSetBounds = tileMatrixSetBounds;
    }
}
