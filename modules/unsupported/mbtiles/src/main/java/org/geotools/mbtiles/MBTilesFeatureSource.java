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
import java.sql.SQLException;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;
import org.geotools.data.FeatureReader;
import org.geotools.data.Query;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.filter.visitor.ExtractBoundsFilterVisitor;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.util.factory.Hints;
import org.geotools.util.logging.Logging;
import org.locationtech.jts.geom.Envelope;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;

class MBTilesFeatureSource extends ContentFeatureSource {

    static final Logger LOGGER = Logging.getLogger(MBTilesFeatureSource.class);

    private final MBTilesFile mbtiles;
    private final MBtilesCache tileCache;

    public MBTilesFeatureSource(
            ContentEntry entry,
            SimpleFeatureType schema,
            MBTilesFile mbtiles,
            MBtilesCache tileCache) {
        super(entry, null);
        this.mbtiles = mbtiles;
        this.schema = schema;
        this.tileCache = tileCache;
    }

    @Override
    protected void addHints(Set<Hints.Key> hints) {
        hints.add(Hints.GEOMETRY_SIMPLIFICATION);
    }

    @Override
    protected ReferencedEnvelope getBoundsInternal(Query query) throws IOException {
        // all mbtiles likely have a root tile that covers the world, the real intended
        // bound is normally found at the max zoom level. However, the latest zoom level is the one
        // with the most tiles, this takes ages on large mbtiles,
        // so best to have it disabled... or maybe make it optional later
        return null;
    }

    @Override
    protected int getCountInternal(Query query) throws IOException {
        // no reasonable way to count quickly, each tile can contain a different number of features
        return -1;
    }

    @Override
    protected SimpleFeatureType buildFeatureType() throws IOException {
        // feature types are built parsing the json metadata entry in the geopackage, here returning
        // the result of that process
        return schema;
    }

    @Override
    protected FeatureReader<SimpleFeatureType, SimpleFeature> getReaderInternal(Query query)
            throws IOException {
        try {
            long z = getTargetZLevel(query);
            RectangleLong tileBounds = getTileBoundsFor(query, z);
            MBTilesFile.TileIterator tiles =
                    mbtiles.tiles(
                            z,
                            tileBounds.getMinX(),
                            tileBounds.getMinY(),
                            tileBounds.getMaxX(),
                            tileBounds.getMaxY());
            return new MBTilesFeatureReader(tiles, getSchema(), tileCache);
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

    private long getTargetZLevel(Query query) throws SQLException {
        return Optional.ofNullable(query)
                .map(Query::getHints)
                .map(h -> h.get(Hints.GEOMETRY_SIMPLIFICATION))
                .map(
                        d -> {
                            try {
                                return mbtiles.getZoomLevel((Double) d);
                            } catch (SQLException e) {
                                throw new RuntimeException(
                                        "Failed to compute the best zoom level for rendering", e);
                            }
                        })
                .orElse(mbtiles.maxZoom());
    }

    protected RectangleLong getTileBoundsFor(Query query, long z) throws SQLException {
        if (query == null || query.getFilter() == null || query.getFilter() == Filter.INCLUDE) {
            return mbtiles.getTileBounds(z, false);
        }
        Envelope envelope =
                (Envelope)
                        query.getFilter().accept(ExtractBoundsFilterVisitor.BOUNDS_VISITOR, null);
        if (envelope == null || Double.isInfinite(envelope.getWidth())) {
            return mbtiles.getTileBounds(z, false);
        }
        return mbtiles.toTilesRectangle(envelope, z);
    }
}
