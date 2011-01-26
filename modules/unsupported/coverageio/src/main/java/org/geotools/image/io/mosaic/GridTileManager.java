/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.image.io.mosaic;

import java.util.Map;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.AbstractSet;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.io.IOException;
import org.geotools.util.Utilities;
import org.geotools.util.FrequencySortedSet;


/**
 * A tile manager for the particular case of tile distributed on a regular grid.
 *
 * @since 2.5
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
final class GridTileManager extends TileManager {
    /**
     * For cross-version interoperability.
     */
    private static final long serialVersionUID = -3140767174475649400L;

    /**
     * The root level of overviews. This is the level with highest subsampling.
     * It is also the root of a linked list toward finest levels.
     */
    private final OverviewLevel root;

    /**
     * The region enclosing all tiles in absolute coordinates. This is the coordinates
     * relative to the tiles having a subsampling of 1.
     */
    private final Rectangle region;

    /**
     * The number of tiles.
     */
    private final int count;

    /**
     * A view over all tiles as a set. Will be created only when first needed.
     */
    private transient Collection<Tile> tiles;

    /**
     * Creates a new tile manager from an existing collection of overviews.
     *
     * @param root The root overview to assign to the tile manageR.
     */
    GridTileManager(final OverviewLevel root) {
        this.root = root;
        region = new Rectangle(-1, -1);
        int count = 0;
        OverviewLevel level = root;
        do {
            region.add(level.getAbsoluteRegion());
            count += level.getNumTiles();
            level = level.getFinerLevel();
        } while (level != null);
        this.count = count;
    }

    /**
     * Creates a new tile manager for the given tiles, which must be distributed on a grid.
     * This constructor is protected for subclassing, but should not be invoked directly.
     * {@code GridTileManager} instances should be created by {@link TileManagerFactory}.
     *
     * @param  tiles The tiles.
     * @throws IOException if an I/O operation was required and failed.
     * @throws IllegalArgumentException if this class can not handle the given tiles.
     */
    protected GridTileManager(final Tile[] tiles)
            throws IOException, IllegalArgumentException
    {
        Tile.ensureNonNull("tiles", tiles);
        Tile[] modifiedOrder = tiles; // May be modified later.
        final Map<Dimension,OverviewLevel> levelsBySubsampling = new HashMap<Dimension,OverviewLevel>();
        for (int i=0; i<modifiedOrder.length; i++) {
            Tile tile = modifiedOrder[i];
            Dimension subsampling = tile.getSubsampling();
            OverviewLevel level = levelsBySubsampling.get(subsampling);
            if (level == null) {
                /*
                 * We are about to create a new OverviewLevel. We need to know the grid cell size.
                 * We assume that this is the size of the largest tiles. Since the last row and the
                 * last column may contain smaller tiles, and since the order of tiles in the user-
                 * supplied array may be random, we search for larger tiles now.
                 */
                for (int j=i; ++j<modifiedOrder.length;) {
                    final Tile candidate = modifiedOrder[j];
                    if (candidate.isLargerThan(tile)) {
                        if (modifiedOrder == tiles) {
                            modifiedOrder = modifiedOrder.clone();
                        }
                        modifiedOrder[j] = tile;
                        tile = candidate;
                        subsampling = tile.getSubsampling();
                    }
                }
                level = new OverviewLevel(tile, subsampling);
                levelsBySubsampling.put(subsampling, level);
            } else {
                level.add(tile, subsampling);
            }
        }
        final OverviewLevel[] levels;
        levels = levelsBySubsampling.values().toArray(new OverviewLevel[levelsBySubsampling.size()]);
        Arrays.sort(levels);
        region = new Rectangle(-1, -1);
        int count = 0;
        for (int i=0; i<levels.length; i++) {
            final OverviewLevel level = levels[i];
            level.createLinkedList(i, (i != 0) ? levels[i-1] : null);
            region.add(level.getAbsoluteRegion());
            count += level.getNumTiles();
        }
        this.count = count;
        root = (levels.length != 0) ? levels[levels.length - 1] : null;
    }

    /**
     * Returns the region enclosing all tiles.
     *
     * @return The region. <strong>Do not modify</strong> since it is a direct reference to
     *         internal structures.
     */
    @Override
    final Rectangle getRegion() {
        return region;
    }

    /**
     * Returns an estimation of tiles dimension. This method looks only to the first level
     * having more than 1 tile.
     *
     * @return The tiles dimension.
     */
    @Override
    final Dimension getTileSize() {
        for (OverviewLevel level=root; level!=null; level=level.getFinerLevel()) {
            final Dimension size = level.getTileSize();
            if (size != null) {
                return size;
            }
        }
        return region.getSize();
    }

    /**
     * Returns {@code true} if there is more than one tile.
     *
     * @return {@code true} if the image is tiled.
     * @throws IOException If an I/O operation was required and failed.
     */
    @Override
    final boolean isImageTiled() throws IOException {
        return count >= 2;
    }

    /**
     * Returns a reference to the tiles used internally by this tile manager.
     * This implementation returns an instance of {@link FrequencySortedSet} whith
     * {@linkplain FrequencySortedSet#frequencies frequency values} greater than 1
     * for the tiles that actually represent a pattern.
     */
    @Override
    final Collection<Tile> getInternalTiles() {
        final FrequencySortedSet<Tile> tiles = new FrequencySortedSet<Tile>();
            for (OverviewLevel level=root; level!=null; level=level.getFinerLevel()) {
            level.getInternalTiles(tiles);
        }
        return tiles;
    }

    /**
     * Returns all tiles as an unmodifiable set. The tiles are generated on the fly
     * during the iteration.
     */
    public synchronized Collection<Tile> getTiles() {
        if (tiles == null) {
            tiles = new AbstractSet<Tile>() {
                public int size() {
                    return count;
                }

                public Iterator<Tile> iterator() {
                    return new GridTileIterator(root);
                }

                @Override
                public boolean contains(final Object object) {
                    return (object instanceof Tile) && OverviewLevel.contains(root, (Tile) object);
                }
            };
        }
        return tiles;
    }

    /**
     * Returns every tiles that intersect the given region.
     *
     * @throws IOException If it was necessary to fetch an image dimension from its
     *         {@linkplain Tile#getImageReader reader} and this operation failed.
     */
    public Collection<Tile> getTiles(final Rectangle region, final Dimension subsampling,
                                     final boolean subsamplingChangeAllowed) throws IOException
    {
        for (OverviewLevel level=root; level!=null; level=level.getFinerLevel()) {
            final Tile sample = level.getSampleTile();
            final Dimension doable = sample.getSubsamplingFloor(subsampling);
            if (doable == null) {
                // The current level can not handle the given subsampling or any finer one.
                continue;
            }
            if (doable != subsampling) {
                if (!subsamplingChangeAllowed) {
                    // The current level can not handle the given subsampling
                    // and we are not allowed to use a finer one.
                    continue;
                }
            }
            /*
             * Gets the tiles at current level and checks if the cost of reading them is lower
             * than the cost of reading the tiles at the previous (coarser) level. They could
             * be lower if the region to read is small enough so that reading smaller tiles
             * compensate the cost of applying a higher subsampling.
             */
            final ArrayList<Tile> tiles = new ArrayList<Tile>();
            level.getTiles(tiles, region, subsampling, Long.MAX_VALUE);
            // TODO: The search in finer levels is not yet implemented.
            subsampling.setSize(doable);
            return tiles;
        }
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @throws IOException If it was necessary to fetch an image dimension from its
     *         {@linkplain Tile#getImageReader reader} and this operation failed.
     */
    @Override
    public boolean intersects(final Rectangle region, final Dimension subsampling)
            throws IOException
    {
        for (OverviewLevel level=root; level!=null; level=level.getFinerLevel()) {
            final int c = level.compareTo(subsampling);
            if (c <= 0 && level.intersects(region)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns a hash code value for this tile manager.
     */
    @Override
    public int hashCode() {
        return (int) serialVersionUID ^ root.hashCode();
    }

    /**
     * Compares this tile manager with the specified object for equality.
     *
     * @param object The object to compare with.
     */
    @Override
    public boolean equals(final Object object) {
        if (object != null && object.getClass().equals(getClass())) {
            final GridTileManager that = (GridTileManager) object;
            return count == that.count &&
                   Utilities.equals(region, that.region) &&
                   Utilities.equals(root,   that.root); // Tests last since it may be expansive.
        }
        return false;
    }
}
