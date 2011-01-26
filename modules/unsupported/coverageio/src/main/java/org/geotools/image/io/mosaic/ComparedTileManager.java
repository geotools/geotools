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

import java.util.Set;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.NoSuchElementException;
import java.io.IOException;
import java.io.PrintStream;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import javax.imageio.spi.ImageReaderSpi;

import org.geotools.util.Utilities;
import org.geotools.coverage.grid.ImageGeometry;


/**
 * Redirects all method calls to two {@linkplain TileManager tile managers} and compares their
 * results. This is used during testings and assertions only. For this reasons, comparaison
 * failures throw {@link AssertionError}.
 *
 * @since 2.5
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
final class ComparedTileManager extends TileManager {
    /**
     * For cross-version compatibility during serialization.
     */
    private static final long serialVersionUID = -6028433158360279586L;

    /**
     * The tile manager.
     */
    private final TileManager first, second;

    /**
     * Creates a comparator the grid and tree tile managers for the given tiles.
     */
    ComparedTileManager(final Tile[] tiles) throws IOException {
        second = new GridTileManager(tiles); // Must be created before TreeTileManager.
        first  = new TreeTileManager(tiles);
    }

    /**
     * Creates a comparator between the given tile managers.
     */
    ComparedTileManager(final TileManager first, final TileManager second) {
        this.first  = first;
        this.second = second;
    }

    /**
     * Ensures that the given objects are equals. Throws an {@link AssertionError}
     * on failure since this class is used for testing purpose and assertions only.
     *
     * @param  o1 The first object to compare.
     * @param  o2 The second object to compare.
     * @return The first object if both are equal.
     * @throws AssertionError if the given objects are not equal.
     */
    private <T> T assertEqual(final T o1, final T o2) throws AssertionError {
        if (!Utilities.equals(o1, o2)) {
            throw new AssertionError(
                    first.getClass().getSimpleName() + '=' + o1 + ", " +
                   second.getClass().getSimpleName() + '=' + o2);
        }
        return o1;
    }

    /**
     * Verifies that the given set equals. If they are not, the differences and printed
     * to the {@linkplain System#err standard error stream}.
     */
    private boolean equal(final Collection<Tile> c1, final Collection<Tile> c2) {
        return equals(first, c1, c2) & equals(second, c2, c1); // Really "&", not "&&".
    }

    /**
     * Implementation helper for {@link #equal}.
     */
    private static boolean equals(TileManager manager, Collection<Tile> c1, Collection<Tile> c2) {
        final Set<Tile> remainding = new LinkedHashSet<Tile>(c1);
        /*
         * Following loop is equivalent to remainding.removeAll(c2), but we do not want to invoke
         * the later because the default AbstractSet.removeAll(Collection) implementation invokes
         * c2.contains(Tile) repetively for every elements in c1. This is efficient when c2 is a
         * Set. But since our c2 collection is often an List, this leads to O(n²) computation cost.
         */
        for (final Tile tile : c2) {
            remainding.remove(tile);
        }
        if (remainding.isEmpty()) {
            return true;
        }
        final PrintStream err = System.err;
        err.print("Additional tiles from ");
        err.print(manager.getClass().getSimpleName());
        err.println(':');
        err.println(Tile.toString(remainding, 100));
        /*
         * We limit the formatting to a small number of tiles (100) because this method is
         * expected to be invoked only with small collections. Futhermore we don't want to
         * flood stderr because the difference are printed without stopping the program.
         */
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setGridToCRS(AffineTransform gridToCRS) throws IllegalStateException, IOException {
        first .setGridToCRS(gridToCRS);
        second.setGridToCRS(gridToCRS);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ImageGeometry getGridGeometry() throws IOException {
        return assertEqual(first.getGridGeometry(), second.getGridGeometry());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    Rectangle getRegion() throws IOException {
        return assertEqual(first.getRegion(), second.getRegion());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    Dimension getTileSize() throws IOException {
        return assertEqual(first.getTileSize(), second.getTileSize());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    boolean isImageTiled() throws IOException {
        return assertEqual(first.isImageTiled(), second.isImageTiled());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<ImageReaderSpi> getImageReaderSpis() throws IOException {
        return assertEqual(first.getImageReaderSpis(), second.getImageReaderSpis());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Tile createGlobalTile(ImageReaderSpi provider, Object input, int imageIndex)
            throws NoSuchElementException, IOException
    {
        return assertEqual(first.createGlobalTile(provider, input, imageIndex),
                           second.createGlobalTile(provider, input, imageIndex));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Tile> getTiles() throws IOException {
        final Collection<Tile> tiles = first.getTiles();
        if (!equal(tiles, second.getTiles())) {
            // We don't use the assert statement because we want the above line
            // to be executed in all cases, since it prints mismatchs as warnings.
            throw new AssertionError();
        }
        return tiles;
    }

    /**
     * {@inheritDoc}
     */
    public Collection<Tile> getTiles(Rectangle region, Dimension subsampling,
            boolean subsamplingChangeAllowed) throws IOException
    {
        final Dimension copy = new Dimension(subsampling);
        final Collection<Tile> tiles = first.getTiles(region, subsampling, subsamplingChangeAllowed);
        if (equal(tiles, second.getTiles(region, copy, subsamplingChangeAllowed))) {
            assertEqual(subsampling, copy);
        }
        return tiles;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean intersects(Rectangle region, Dimension subsampling) throws IOException {
        return assertEqual(first.intersects(region, subsampling),
                          second.intersects(region, subsampling));
    }

    /**
     * Returns a hash code value for this tile manager.
     */
    @Override
    public int hashCode() {
        return (int) serialVersionUID ^ (first.hashCode() + 37 * second.hashCode());
    }

    /**
     * Compares this tile manager with the specified object for equality.
     *
     * @param object The object to compare with.
     * @return If the given object is equals to this manager.
     */
    @Override
    public boolean equals(final Object object) {
        if (object instanceof ComparedTileManager) {
            final ComparedTileManager that = (ComparedTileManager) object;
            return Utilities.equals(this.first,  that.first) &&
                   Utilities.equals(this.second, that.second);
        }
        return false;
    }
}
