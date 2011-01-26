/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
import java.util.ArrayList;
import java.util.Collection;
import java.io.IOException;
import java.awt.geom.AffineTransform; // For javadoc

import org.geotools.factory.Hints;
import org.geotools.factory.AbstractFactory;
import org.geotools.coverage.grid.ImageGeometry;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Errors;
import org.geotools.util.logging.Logging;


/**
 * Creates {@link TileManager} instances from a collection of tiles.
 *
 * @since 2.5
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
public class TileManagerFactory extends AbstractFactory {
    /**
     * The default instance.
     */
    public static final TileManagerFactory DEFAULT = new TileManagerFactory(null);

    /**
     * Creates a new factory from the specified hints.
     *
     * @param hints Optional hints, or {@code null} if none.
     */
    protected TileManagerFactory(final Hints hints) {
        // We have no usage for those hints at this time, but some may be added later.
    }

    /**
     * Creates tile managers from the specified object, which may be {@code null}. If non-null, the
     * object shall be an instance of {@code TileManager[]}, {@code TileManager}, {@code Tile[]} or
     * {@code Collection<Tile>}.
     *
     * @param  tiles The tiles, or {@code null}.
     * @return The tile managers, or {@code null} if {@code tiles} was null.
     * @throws IllegalArgumentException if {@code tiles} is not an instance of a valid class,
     *         or if it is an array or a collection containing null elements.
     * @throws IOException If an I/O operation was required and failed.
     */
    public TileManager[] createFromObject(final Object tiles)
            throws IOException, IllegalArgumentException
    {
        final TileManager[] managers;
        if (tiles == null) {
            managers = null;
        } else if (tiles instanceof TileManager[]) {
            managers = ((TileManager[]) tiles).clone();
        } else if (tiles instanceof TileManager) {
            managers = new TileManager[] {(TileManager) tiles};
        } else if (tiles instanceof Tile[]) {
            managers = create((Tile[]) tiles);
        } else if (tiles instanceof Collection) {
            @SuppressWarnings("unchecked") // create(Collection) will checks indirectly.
            final Collection<Tile> c = (Collection<Tile>) tiles;
            managers = create(c);
        } else {
            throw new IllegalArgumentException(Errors.format(
                    ErrorKeys.ILLEGAL_CLASS_$2, tiles.getClass(), TileManager.class));
        }
        if (managers != null) {
            for (int i=0; i<managers.length; i++) {
                if (managers[i] == null) {
                    throw new IllegalArgumentException(Errors.format(
                            ErrorKeys.NULL_ARGUMENT_$1, "input[" + i + ']'));
                }
            }
        }
        return managers;
    }

    /**
     * Creates tile managers from the specified array of tiles.
     * This method usually returns a single tile manager, but more could be
     * returned if this factory has been unable to put every tiles in a single mosaic
     * (for example if the ratio between {@linkplain AffineTransform affine transform} given to
     * {@linkplain Tile#Tile(ImageReaderSpi,Object,int,Dimension,AffineTransform) tile constructor}
     * would lead to fractional {@linkplain Tile#getSubsampling subsampling}).
     *
     * @param  tiles The tiles to give to a tile manager.
     * @return A tile manager created from the given tiles.
     * @throws IOException If an I/O operation was required and failed.
     */
    public TileManager[] create(final Tile[] tiles) throws IOException {
        // The default called invokes Collection.toArray(), which will copy the array.
        return create(Arrays.asList(tiles));
    }

    /**
     * Creates tile managers from the specified collection of tiles.
     * This method usually returns a single tile manager, but more could be
     * returned if this factory has been unable to put every tiles in a single mosaic
     * (for example if the ratio between {@linkplain AffineTransform affine transform} given to
     * {@linkplain Tile#Tile(ImageReaderSpi,Object,int,Dimension,AffineTransform) tile constructor}
     * would lead to fractional {@linkplain Tile#getSubsampling subsampling}).
     *
     * @param  tiles The tiles to give to a tile manager.
     * @return A tile manager created from the given tiles.
     * @throws IOException If an I/O operation was required and failed.
     */
    public TileManager[] create(Collection<Tile> tiles) throws IOException {
        int count = 0;
        final TileManager[] managers;
        if (!hasPendingGridToCRS(tiles)) {
            /*
             * There is no tile having a "gridToCRS" transform pending RegionCalculator work. So we
             * can create (at the end of this method) a single TileManager using all those tiles.
             */
            if (!tiles.isEmpty()) {
                count = 1;
            }
            managers = new TileManager[count];
        } else {
            /*
             * At least one tile have a pending "gridToCRS" transform (actually we should have
             * more than one - typically all of them - otherwise the RegionCalculator work will
             * be useless). Computes their region now. Note that we could execute this block
             * inconditionnaly. The 'hasPendingGridToCRS' check we just for avoiding the cost
             * of creating RegionCalculator in the common case where it is not needed. So it is
             * not a big deal if 'hasPendingGridToCRS' conservatively returned 'true'.
             */
            final Collection<Tile> remainings = new ArrayList<Tile>(Math.min(16, tiles.size()));
            final RegionCalculator calculator = new RegionCalculator();
            for (final Tile tile : tiles) {
                if (!calculator.add(tile)) {
                    remainings.add(tile);
                }
            }
            if (!remainings.isEmpty()) {
                count = 1;
            }
            final Map<ImageGeometry,Tile[]> split = calculator.tiles();
            managers = new TileManager[split.size() + count];
            for (final Map.Entry<ImageGeometry,Tile[]> entry : split.entrySet()) {
                final TileManager manager = createGeneric(entry.getValue());
                manager.geometry = entry.getKey();
                managers[count++] = manager;
            }
            tiles = remainings;
        }
        if (!tiles.isEmpty()) {
            managers[0] = createGeneric(tiles.toArray(new Tile[tiles.size()]));
        }
        return managers;
    }

    /**
     * Returns {@code true} if at least one tile in the given collection has at "<cite>grid to
     * real world</cite>" transform waiting to be processed by {@link RegionCalculator}. It is
     * okay to conservatively returns {@code true} in situations where we would have got
     * {@code false} if synchronization was performed on every tiles.
     */
    private static boolean hasPendingGridToCRS(final Collection<Tile> tiles) {
        for (final Tile tile : tiles) {
            if (tile.getPendingGridToCRS(false) != null) {
                return true;
            }
        }
        return false;
    }

    /**
     * Creates a single {@linkplain TileManager tile manager} from the given array
     * of tiles. This method is automatically invoked by {@code create} methods.
     * The tile array has already been cloned and can be stored directly by the
     * tile manager constructors.
     * <p>
     * Subclasses can override this method if they want to create other kinds of tile managers.
     *
     * @param tiles A copy of user-supplied tiles.
     * @return The tile manager for the given tiles.
     * @throws IOException If an I/O operation was required and failed.
     */
    protected TileManager createGeneric(final Tile[] tiles) throws IOException {
        TileManager manager;
        try {
            manager = new GridTileManager(tiles);
        } catch (IllegalArgumentException e) {
            // Failed to created the instance optimized for grid.
            // Fallback on the more generic instance using RTree.
            Logging.recoverableException(TileManagerFactory.class, "createGeneric", e);
            return new TreeTileManager(tiles);
        }
        // Intentional side effect: use ComparedTileManager only if assertions are enabled.
        assert (manager = new ComparedTileManager(manager, new TreeTileManager(tiles))) != null;
        return manager;
    }
}
