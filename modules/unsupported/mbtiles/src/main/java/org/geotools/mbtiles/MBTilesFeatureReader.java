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
 */
package org.geotools.mbtiles;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureReader;
import org.geotools.util.logging.Logging;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

class MBTilesFeatureReader implements SimpleFeatureReader {

    static final Logger LOGGER = Logging.getLogger(MBTilesFeatureReader.class);

    private final MBTilesFile.TileIterator tiles;
    private final SimpleFeatureType schema;
    private final MBtilesCache cache;
    private final Set<MBTilesTileLocation> skipLocations;
    private SimpleFeatureIterator currentIterator;

    public MBTilesFeatureReader(
            MBTilesFile.TileIterator tiles,
            SimpleFeatureType schema,
            MBtilesCache cache,
            Set<MBTilesTileLocation> skipLocations) {
        this.tiles = tiles;
        this.schema = schema;
        this.cache = cache;
        this.skipLocations = skipLocations;
    }

    @Override
    public SimpleFeatureType getFeatureType() {
        return schema;
    }

    @Override
    public SimpleFeature next()
            throws IOException, IllegalArgumentException, NoSuchElementException {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        SimpleFeature sf = currentIterator.next();
        if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.log(Level.FINEST, "Returning tiled feature " + sf.toString());
        }
        return sf;
    }

    @Override
    public boolean hasNext() throws IOException {
        if (currentIterator != null && currentIterator.hasNext()) {
            return true;
        }

        while (tiles.hasNext()) {
            MBTilesTile nextTile = tiles.next();
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "Moving to tile: " + nextTile);
            }
            if (skipLocations.contains(nextTile)) {
                continue;
            }
            SimpleFeatureCollection features = cache.getFeatures(nextTile, schema.getTypeName());
            // Was the layer not found in the tile?
            // Can happen, some layers show up only at certain zoom levels
            if (features == null) {
                continue;
            }
            currentIterator = features.features();
            if (currentIterator != null && currentIterator.hasNext()) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void close() throws IOException {
        tiles.close();
    }
}
