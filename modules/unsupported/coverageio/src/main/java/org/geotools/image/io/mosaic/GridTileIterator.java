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

import java.util.Iterator;
import java.net.MalformedURLException;
import java.util.NoSuchElementException;
import org.geotools.util.logging.Logging;


/**
 * An iterator over the tiles at a given level. This iterator continue automatically to tiles
 * at finer levels once the iterator on the level given at construction time is done.
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
final class GridTileIterator implements Iterator<Tile> {
    /**
     * The level on which we are doing the iteration,
     * or {@code null} if the iteration is done.
     */
    private OverviewLevel level;

    /**
     * The coordinates of next tile to fetch.
     */
    private int x, y;

    /**
     * The next tile, or {@code null} if the iteration is over.
     */
    private Tile tile;

    /**
     * Creates an iterator starting at the given root level.
     */
    GridTileIterator(final OverviewLevel root) {
        level = root;
        advance();
    }

    /**
     * Updates {@link #tile} to the next tile to be returned, or set it to {@code null} if the
     * iteration is over. In case of failure while creating a tile (which should never occurs),
     * logs a warning and searchs for an other tile. It may violate the collection contract in
     * that such iterator would return less than {@link java.util.Collection#size} entries, but
     * it should not occurs since the URL are supposed to be valid before they are stored as a
     * pattern. Nevertheless we log instead than throwing an unchecked exception in the hope to
     * get something partially working in case of such error.
     */
    private void advance() {
        while (level != null) {
            while (y < level.getNumYTiles()) {
                while (x < level.getNumXTiles()) {
                    try {
                        tile = level.getTile(x++, y);
                    } catch (MalformedURLException e) {
                        Logging.unexpectedException(GridTileIterator.class, "next", e);
                        continue; // Search for an other tile.
                    }
                    if (tile != null) {
                        return;
                    }
                }
                x = 0;
                y++;
            }
            y = 0;
            level = level.getFinerLevel();
        }
        tile = null;
    }

    /**
     * Returns {@code true} if there is more tiles to returns.
     *
     * @return {@code true} if there is at least one more tile to returns.
     */
    public boolean hasNext() {
        return level != null;
    }

    /**
     * Returns the next tile.
     *
     * @return The next tile (never {@code null}).
     * @throws NoSuchElementException if there is no more tile to return.
     */
    public Tile next() throws NoSuchElementException {
        final Tile tile = this.tile;
        if (tile == null) {
            throw new NoSuchElementException();
        }
        advance();
        return tile;
    }

    /**
     * Unsupported operation since the backed collection is immutable.
     *
     * @throws UnsupportedOperationException Always thrown.
     */
    public void remove() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }
}
