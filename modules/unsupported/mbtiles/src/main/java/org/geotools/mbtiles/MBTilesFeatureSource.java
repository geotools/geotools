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

import static org.geotools.mbtiles.MBTilesDataStore.DEFAULT_CRS;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.geotools.data.DataUtilities;
import org.geotools.data.EmptyFeatureReader;
import org.geotools.data.FeatureReader;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureReader;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.mbtiles.CompositeSimpleFeatureReader.ReaderSupplier;
import org.geotools.referencing.CRS;
import org.geotools.util.factory.Hints;
import org.geotools.util.logging.Logging;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.referencing.operation.TransformException;

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
        hints.add(Hints.GEOMETRY_GENERALIZATION);
        hints.add(Hints.GEOMETRY_DISTANCE);
        hints.add(Hints.GEOMETRY_CLIP);
        hints.add(Hints.GEOMETRY_CLIP);
    }

    @Override
    protected ReferencedEnvelope getBoundsInternal(Query query) throws IOException {
        Filter f = query.getFilter();
        if (f == null || f.equals(Filter.INCLUDE)) {
            try {
                return new ReferencedEnvelope(
                        CRS.transform(mbtiles.loadMetaData().getBounds(), DEFAULT_CRS));
            } catch (TransformException e) {
                throw new RuntimeException("Unable to retrieve bounds from mbtiles metadata", e);
            }
        } else {
            // summing up all feature bounds would be expensive.
            // returning null
            return null;
        }
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
            List<RectangleLong> tileBounds = getTileBoundsFor(query, z);
            List<ReaderSupplier> suppliers =
                    tileBounds.stream()
                            .flatMap(tb -> getReaderSuppliersFor(z, tb).stream())
                            .collect(Collectors.toList());

            SimpleFeatureReader reader;
            if (suppliers.isEmpty()) {
                reader = DataUtilities.simple(new EmptyFeatureReader<>(getSchema()));
            } else {
                reader = new CompositeSimpleFeatureReader(getSchema(), suppliers);
            }

            return reader;

        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

    protected List<ReaderSupplier> getReaderSuppliersFor(long z, RectangleLong tb) {
        List<ReaderSupplier> result = new ArrayList<>();

        // grab the locations in the rectangle, and for each in memory collection build a supplier
        Map<MBTilesTileLocation, SimpleFeatureCollection> memoryTiles =
                tileCache.getCachedFeatures(z, tb, getSchema().getTypeName());
        result.addAll(getMemorySuppliers(memoryTiles.values()));

        // check if any tile still needs to be read
        RectangleLong unreadRect = getUnreadLocationBounds(z, tb, memoryTiles.keySet());
        if (unreadRect != null && !unreadRect.isNull()) {
            // add the tiles that really need to be read
            result.add(getDatabaseSupplier(z, unreadRect, memoryTiles.keySet()));
        }

        return result;
    }

    /**
     * Computes the minimum bounds that need to be still read in order to get the missing tiles,
     * without allocating each one in a in-memory list: with large bounds at high zoom levels, the
     * list could easily cause a OOM
     */
    protected RectangleLong getUnreadLocationBounds(
            long z, RectangleLong bounds, Set<MBTilesTileLocation> readLocations) {
        RectangleLong result = new RectangleLong();
        MBTilesTileLocation location = new MBTilesTileLocation(z, 0, 0);
        bounds.forEach(
                (x, y) -> {
                    location.setTileColumn(x);
                    location.setTileRow(y);
                    if (!readLocations.contains(location)) {
                        result.expandToInclude(location);
                    }
                });

        return result;
    }

    protected ReaderSupplier getDatabaseSupplier(
            long z, RectangleLong unreadRect, Set<MBTilesTileLocation> skipLocations) {
        return () -> {
            try {
                MBTilesFile.TileIterator tiles =
                        mbtiles.tiles(
                                z,
                                unreadRect.getMinX(),
                                unreadRect.getMinY(),
                                unreadRect.getMaxX(),
                                unreadRect.getMaxY());
                return new MBTilesFeatureReader(tiles, getSchema(), tileCache, skipLocations);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        };
    }

    protected List<ReaderSupplier> getMemorySuppliers(Collection<SimpleFeatureCollection> values) {
        return values.stream()
                .map(fc -> (ReaderSupplier) () -> DataUtilities.reader(fc))
                .collect(Collectors.toList());
    }

    private long getTargetZLevel(Query query) throws SQLException {
        return Optional.ofNullable(query)
                .map(Query::getHints)
                .map(
                        h -> {
                            if (h.get(Hints.GEOMETRY_GENERALIZATION) != null) {
                                return h.get(Hints.GEOMETRY_GENERALIZATION);
                            } else if (h.get(Hints.GEOMETRY_SIMPLIFICATION) != null) {
                                return h.get(Hints.GEOMETRY_SIMPLIFICATION);
                            } else {
                                return h.get(Hints.GEOMETRY_DISTANCE);
                            }
                        })
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

    protected List<RectangleLong> getTileBoundsFor(Query query, long z) throws SQLException {
        RectangleLong levelBounds = mbtiles.getTileBounds(z, false);
        if (query == null || query.getFilter() == null || query.getFilter() == Filter.INCLUDE) {
            return Collections.singletonList(levelBounds);
        }

        // Get the bounds from the query, map them to tile space, intersect with the level bounds
        // and remove the empty results. At the end, the list might be empty.
        List<RectangleLong> rectangles =
                Optional.ofNullable(ExtractMultiBoundsFilterVisitor.getBounds(query.getFilter()))
                        .map(o -> o.stream())
                        .orElse(Stream.empty())
                        .filter(e -> !Double.isInfinite(e.getWidth()))
                        .map(
                                e -> {
                                    try {
                                        return mbtiles.toTilesRectangle(e, z);
                                    } catch (SQLException ex) {
                                        throw new RuntimeException(ex);
                                    }
                                })
                        .map(tr -> tr.intersection(levelBounds))
                        // don't filter empty intersections, or will return the whole world for them
                        .collect(Collectors.toList());
        if (rectangles.isEmpty()) {
            return Collections.singletonList(levelBounds);
        }

        // bounds expand into tile references, what might not have been overlapping now is so,
        // need to reconcile
        List<RectangleLong> result = new ArrayList<>();
        for (RectangleLong rect : rectangles) {
            if (result.isEmpty()) {
                result.add(rect);
                continue;
            }

            boolean mergedAny = false;
            do {
                // Find all rectangles found so far that overlap with the current one,
                // include them in the current one and remove the from the result
                // This expansion can cause envelopes not previously matching to overlap
                // with the current rect, so start back from the beginning until we
                // can continue to merge the results
                mergedAny = false;
                ListIterator<RectangleLong> it = result.listIterator();
                while (it.hasNext()) {
                    RectangleLong next = it.next();
                    if (next.intersects(rect)) {
                        it.remove();
                        rect.expandToInclude(next);
                        mergedAny = true;
                    }
                }
            } while (mergedAny);
            // the rect could not be merged with any of the ones in the result, add it
            result.add(rect);
        }

        return result;
    }
}
