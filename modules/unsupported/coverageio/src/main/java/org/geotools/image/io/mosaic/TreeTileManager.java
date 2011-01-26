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

import java.util.*; // We use really a lot of those imports.
import java.awt.Dimension;
import java.awt.Rectangle;
import java.io.IOException;
import java.io.ObjectInputStream;
import javax.imageio.spi.ImageReaderSpi;

import org.geotools.resources.UnmodifiableArrayList;
import org.geotools.resources.OptionalDependencies;
import org.geotools.util.FrequencySortedSet;
import org.geotools.util.Comparators;


/**
 * A collection of {@link Tile} objects organized in a tree.
 * <p>
 * {@code TreeTileManager}s are {@linkplain Serializable serializable} if all their tiles have a
 * serializable {@linkplain Tile#getInput input}. The {@link ImageReaderSpi} doesn't need to
 * be serializable, but its class must be known to {@link javax.imageio.spi.IIORegistry} at
 * deserialization time.
 * <p>
 * This class is thread-safe but default implementation is not scalable to a high number of
 * concurrent threads. Up to 4 concurrent calls to {@link #getTiles getTiles} should be okay,
 * more may slow down the execution.
 *
 * @since 2.5
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
final class TreeTileManager extends TileManager {
    /**
     * For cross-version compatibility during serialization.
     */
    private static final long serialVersionUID = -6070623930537957163L;

    /**
     * The expected maximal number of concurrent threads using the same {@link TreeTileManager}
     * instance. There is no risk of heratic behavior if the number of concurrent threads
     * exceed this constant - {@link TreeTileManager} would just become slower.
     * <p>
     * This number should be small because the code is rather simple and not designed for
     * scalability in highly concurrent context.
     */
    private static final int CONCURRENT_THREADS = 4;

    /**
     * The tiles sorted by {@linkplain Tile#getInput input}) first, then by
     * {@linkplain Tile#getImageIndex image index}. If an iteration must be
     * performed over every tiles, doing the iteration in this array order
     * should be more efficient than other order.
     */
    private final Tile[] tiles;

    /**
     * All tiles wrapped in an unmodifiable list.
     * <p>
     * Consider this field as final. It is not because it needs to be set by {@link #readObject}.
     * If this field become public or protected in a future version, then we should make it final
     * and use reflection like {@link org.geotools.coverage.grid.GridCoverage2D#readObject}.
     */
    private transient Collection<Tile> allTiles;

    /**
     * The {@linkplain #tiles} as a trees for faster access. The array and its elements
     * will be created only when first needed. Every elements after the first one are
     * {@linkplain RTree#clone clones}.
     * <p>
     * The work performed by {@link RTree} may be relatively expensive, so we use different
     * instance per thread if there is concurrent usage of {@link TreeTileManager}.
     */
    private transient RTree[] trees;

    /**
     * The region enclosing all tiles. Will be computed only when first needed.
     */
    private transient Rectangle region;

    /**
     * The tile dimensions. Will be computed only when first needed.
     */
    private transient Dimension tileSize;

    /**
     * Creates a manager for the given tiles. This constructor is protected for subclassing,
     * but should not be invoked directly. {@code TreeTileManager} instances should be created
     * by {@link TileManagerFactory}.
     *
     * @param tiles The tiles. This array is not cloned and elements in this array may be
     *        reordered by this constructor. The public methods in {@link TileManagerFactory}
     *        are reponsible for cloning the user-provided arrays if needed.
     */
    protected TreeTileManager(final Tile[] tiles) {
        /*
         * Puts together the tiles that use the same input. For those that use
         * different input, we will order by image index first, then (y,x) order.
         */
        Tile.ensureNonNull("tiles", tiles);
        final Map<ReaderInputPair,List<Tile>> tilesByInput =
                new LinkedHashMap<ReaderInputPair, List<Tile>>(tiles.length + tiles.length/4 + 1);
        providers = new FrequencySortedSet<ImageReaderSpi>(4, true);
        for (final Tile tile : tiles) {
            tile.checkGeometryValidity();
            final ImageReaderSpi  spi = tile.getImageReaderSpi();
            final ReaderInputPair key = new ReaderInputPair(spi, tile.getInput());
            List<Tile> sameInputs = tilesByInput.get(key);
            if (sameInputs == null) {
                /*
                 * We will usually have only one element in each list. Because we may create
                 * thousands of them, it is better to stick to such a small size. If we have
                 * more elements, the number of distinct lists will be smaller and they will
                 * be reasonably cheap to growth.
                 */
                sameInputs = new ArrayList<Tile>(1);
                tilesByInput.put(key, sameInputs);
                providers.add(spi);
            }
            sameInputs.add(tile);
        }
        providers = Collections.unmodifiableSet(providers);
        /*
         * Overwrites the tiles array with the same tiles, but ordered with same input firsts.
         */
        @SuppressWarnings("unchecked")
        final List<Tile>[] asArray = tilesByInput.values().toArray(new List[tilesByInput.size()]);
        final Comparator<List<Tile>> comparator = Comparators.forLists();
        Arrays.sort(asArray, comparator);
        int numTiles = 0;
        for (final List<Tile> sameInputs : asArray) {
            switch (sameInputs.size()) {
                case 0:  throw new AssertionError(); // Should never happen.
                case 1:  break; // Worthly optimization when we have thousands of lists of lenght 1.
                default: Collections.sort(sameInputs); break;
            }
            for (final Tile tile : sameInputs) {
                tiles[numTiles++] = tile;
            }
        }
        this.tiles = tiles;
        allTiles = UnmodifiableArrayList.wrap(tiles);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    final synchronized Rectangle getRegion() throws IOException {
        if (region == null) {
            final RTree tree = getTree();
            try {
                region = tree.getBounds();
            } finally {
                release(tree);
            }
        }
        return region;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    final synchronized Dimension getTileSize() throws IOException {
        if (tileSize == null) {
            final RTree tree = getTree();
            try {
                tileSize = tree.getTileSize();
            } finally {
                release(tree);
            }
        }
        return tileSize;
    }

    /**
     * {@inheritDoc}
     *
     * @throws IOException If an I/O operation was required and failed.
     */
    @Override
    final boolean isImageTiled() throws IOException {
        // Don't invoke 'getTiles' because we want to avoid the call to Tile.getRegion().
        return tiles.length >= 2;
    }

    /**
     * Returns the RTree, creating it if necessary. Calls to this method must be followed by a
     * {@code try ... finally} block with call to {@link #release} in the {@code finally} block.
     */
    private synchronized RTree getTree() throws IOException {
        if (trees == null) {
            final TreeNode root  = new GridNode(tiles);
            final RTree    tree  = new RTree(root);
            final RTree[]  trees = new RTree[CONCURRENT_THREADS];
            trees[0] = tree;
            assert root.containsAll(allTiles);
            this.trees = trees; // Save last so it is saved only on success.
        }
        /*
         * Returns the first instance available for use,
         * creating a new one if we hit an empty slot.
         */
        for (int i=0; i<trees.length; i++) {
            RTree tree = trees[i];
            if (tree == null) {
                trees[i] = tree = trees[0].clone();
            } else if (tree.inUse) {
                continue;
            }
            tree.inUse = true;
            return tree;
        }
        // Every instances are in use. Returns a clone to be discarted after usage.
        return trees[0].clone();
    }

    /**
     * Releases a tree acquired by {@link #getTree}. We do not synchronize
     * because {@link RTree#inUse} is declared as a volatile field.
     */
    private synchronized void release(final RTree tree) {
        // Safety for avoiding NullPointerException to be thrown in 'finally' block.
        if (tree != null) {
            tree.inUse = false;
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IOException If an I/O operation was required and failed.
     */
    @Override
    public Set<ImageReaderSpi> getImageReaderSpis() throws IOException {
        // No need to synchronize because this implementation class creates
        // the providers set at construction time or deserialization time.
        return providers;
    }

    /**
     * {@inheritDoc}
     */
    public Collection<Tile> getTiles() {
        return allTiles;
    }

    /**
     * {@inheritDoc}
     *
     * @throws IOException If it was necessary to fetch an image dimension from its
     *         {@linkplain Tile#getImageReader reader} and this operation failed.
     */
    public Collection<Tile> getTiles(final Rectangle region, final Dimension subsampling,
                                     final boolean subsamplingChangeAllowed) throws IOException
    {
        final RTree tree = getTree();
        final Collection<Tile> values;
        try {
            // Initializes the tree with the search criterions.
            tree.regionOfInterest = region;
            tree.subsampling = subsampling;
            tree.subsamplingChangeAllowed = subsamplingChangeAllowed;
            values = tree.searchTiles();
        } finally {
            tree.regionOfInterest = null; // Just as a safety (not really required).
            tree.subsampling = null;
            release(tree);
        }
        return values;
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
        final RTree tree = getTree();
        final boolean intersects;
        try {
            // Initializes the tree with the search criterions.
            tree.regionOfInterest = region;
            tree.subsampling = subsampling;
            intersects = tree.intersects();
        } finally {
            tree.regionOfInterest = null; // Just as a safety (not really required).
            tree.subsampling = null;
            release(tree);
        }
        return intersects;
    }

    /**
     * Returns a hash code value for this tile manager.
     */
    @Override
    public int hashCode() {
        return (int) serialVersionUID ^ Arrays.hashCode(tiles);
    }

    /**
     * Compares this tile manager with the specified object for equality.
     *
     * @param object The object to compare with.
     */
    @Override
    public boolean equals(final Object object) {
        if (object != null && object.getClass().equals(getClass())) {
            final TreeTileManager that = (TreeTileManager) object;
            return Arrays.equals(this.tiles, that.tiles);
        }
        return false;
    }

    /**
     * Returns a string representation of this tile manager.
     */
    @Override
    public String toString() {
        final RTree tree;
        try {
            tree = getTree();
        } catch (IOException e) {
            return super.toString();
        }
        final String string;
        try {
            string = OptionalDependencies.toString(tree.root);
        } finally {
            release(tree);
        }
        return string;
    }

    /**
     * Invoked on deserialization. Restores the transient fields that are usuly computed at
     * construction time. Doing so immediately instead of relying on lazy creation allows us
     * to avoid synchronization.
     */
    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        allTiles = UnmodifiableArrayList.wrap(tiles);
        providers = new FrequencySortedSet<ImageReaderSpi>(4, true);
        for (final Tile tile : tiles) {
            providers.add(tile.getImageReaderSpi());
        }
        providers = Collections.unmodifiableSet(providers);
    }
}
