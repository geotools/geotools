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

import java.util.Set;
import java.util.Map;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.awt.Point;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import javax.imageio.ImageReader;
import javax.imageio.spi.ImageReaderSpi;

import org.geotools.util.FrequencySortedSet;
import org.geotools.coverage.grid.ImageGeometry;
import org.geotools.referencing.operation.matrix.XAffineTransform;
import org.geotools.resources.i18n.Errors;
import org.geotools.resources.i18n.ErrorKeys;


/**
 * A collection of {@link Tile} objects to be given to {@link MosaicImageReader}. This base
 * class does not assume that the tiles are arranged in any particular order (especially grids).
 * But subclasses can make such assumption for better performances.
 *
 * @since 2.5
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
public abstract class TileManager implements Serializable {
    /**
     * For cross-version compatibility during serialization.
     */
    private static final long serialVersionUID = -7645850962821189968L;

    /**
     * The grid geometry, including the "<cite>grid to real world</cite>" transform.
     * This is provided by {@link TileManagerFactory} when this information is available.
     */
    ImageGeometry geometry;

    /**
     * All image providers used as an unmodifiable set. Computed when first needed.
     */
    transient Set<ImageReaderSpi> providers;

    /**
     * Creates a tile manager.
     */
    protected TileManager() {
    }

    /**
     * Sets the {@linkplain Tile#getGridTocRS grid to CRS} transform for every tiles. A copy of
     * the supplied affine transform is {@linkplain AffineTransform#scale scaled} according the
     * {@linkplain Tile#getSubsampling subsampling} of each tile. Tiles having the same
     * subsampling will share the same immutable instance of affine transform.
     * <p>
     * The <cite>grid to CRS</cite> transform is not necessary for proper working of {@linkplain
     * MosaicImageReader mosaic image reader}, but is provided as a convenience for users.
     * <p>
     * This method can be invoked only once.
     *
     * @param gridToCRS The "grid to CRS" transform.
     * @throws IllegalStateException if a transform was already assigned to at least one tile.
     * @throws IOException If an I/O operation was required and failed.
     */
    public synchronized void setGridToCRS(final AffineTransform gridToCRS)
            throws IllegalStateException, IOException
    {
        if (geometry != null) {
            throw new IllegalStateException();
        }
        final Map<Dimension,AffineTransform> shared = new HashMap<Dimension,AffineTransform>();
        AffineTransform at = new XAffineTransform(gridToCRS);
        shared.put(new Dimension(1,1), at);
        geometry = new ImageGeometry(getRegion(), at);
        for (final Tile tile : getInternalTiles()) {
            final Dimension subsampling = tile.getSubsampling();
            at = shared.get(subsampling);
            if (at == null) {
                at = new AffineTransform(gridToCRS);
                at.scale(subsampling.width, subsampling.height);
                at = new XAffineTransform(at);
                shared.put(subsampling, at);
            }
            tile.setGridToCRS(at);
        }
    }

    /**
     * Returns the grid geometry, including the "<cite>grid to real world</cite>" transform.
     * This information is typically available only when {@linkplain AffineTransform affine
     * transform} were explicitly given to {@linkplain Tile#Tile(ImageReaderSpi,Object,int,
     * Dimension,AffineTransform) tile constructor}.
     *
     * @return The grid geometry, or {@code null} if this information is not available.
     * @throws IOException If an I/O operation was required and failed.
     *
     * @see Tile#getGridToCRS
     */
    public synchronized ImageGeometry getGridGeometry() throws IOException {
        if (geometry == null) {
            /*
             * The gridToCRS transform is the same one than the one of the tile having origin at
             * (0,0) and subsampling of (1,1).  So we search for exactly this tile and currently
             * accept no other one. In a future version we could accept an other tile (but which
             * one?) and translate the affine transform...  But the result could be wrong if the
             * gridToCRS transform is not computed by RegionCalculator. Only the particular tile
             * searched by current implementation should be okay in all cases.
             */
            for (final Tile tile : getInternalTiles()) {
                final Dimension subsampling = tile.getSubsampling();
                if (subsampling.width != 1 || subsampling.height != 1) {
                    continue;
                }
                final Point origin = tile.getLocation();
                if (origin.x != 0 || origin.y != 0) {
                    continue;
                }
                final AffineTransform gridToCRS = tile.getGridToCRS();
                if (gridToCRS == null) {
                    continue;
                }
                geometry = new ImageGeometry(getRegion(), gridToCRS);
                break;
            }
        }
        return geometry;
    }

    /**
     * Returns the region enclosing all tiles. Subclasses will override this method with
     * a better implementation.
     *
     * @return The region. <strong>Do not modify</strong> since it may be a direct reference to
     *         internal structures.
     * @throws IOException If it was necessary to fetch an image dimension from its
     *         {@linkplain Tile#getImageReader reader} and this operation failed.
     */
    Rectangle getRegion() throws IOException {
        return getGridGeometry().getGridRange();
    }

    /**
     * Returns the tiles dimension. Subclasses will override this method with a better
     * implementation.
     *
     * @return The tiles dimension. <strong>Do not modify</strong> since it may be a direct
     *         reference to internal structures.
     * @throws IOException If it was necessary to fetch an image dimension from its
     *         {@linkplain Tile#getImageReader reader} and this operation failed.
     */
    Dimension getTileSize() throws IOException {
        return getRegion().getSize();
    }

    /**
     * Returns {@code true} if there is more than one tile. The default implementation returns
     * {@code true} in all cases.
     *
     * @return {@code true} if the image is tiled.
     * @throws IOException If an I/O operation was required and failed.
     */
    boolean isImageTiled() throws IOException {
        return true;
    }

    /**
     * Returns all image reader providers used by the tiles. The set will typically contains
     * only one element, but more are allowed. In the later case, the entries in the set are
     * sorted from the most frequently used provider to the less frequently used.
     *
     * @return The image reader providers.
     * @throws IOException If an I/O operation was required and failed.
     *
     * @see MosaicImageReader#getTileReaderSpis
     */
    public synchronized Set<ImageReaderSpi> getImageReaderSpis() throws IOException {
        if (providers == null) {
            final FrequencySortedSet<ImageReaderSpi> providers =
                    new FrequencySortedSet<ImageReaderSpi>(4, true);
            final Collection<Tile> tiles = getInternalTiles();
            int[] frequencies = null;
            if (tiles instanceof FrequencySortedSet) {
                frequencies = ((FrequencySortedSet<Tile>) tiles).frequencies();
            }
            int i = 0;
            for (final Tile tile : tiles) {
                final int n = (frequencies != null) ? frequencies[i++] : 1;
                providers.add(tile.getImageReaderSpi(), n);
            }
            this.providers = Collections.unmodifiableSet(providers);
        }
        return providers;
    }

    /**
     * Creates a tile with a {@linkplain Tile#getRegion region} big enough for containing
     * {@linkplain #getTiles every tiles}. The created tile has a {@linkplain Tile#getSubsampling
     * subsampling} of (1,1). This is sometime useful for creating a "virtual" image representing
     * the assembled mosaic as a whole.
     *
     * @param  provider
     *              The image reader provider to be given to the created tile, or {@code null} for
     *              inferring it automatically. In the later case the provider is inferred from the
     *              input suffix if any (e.g. the {@code ".png"} extension in a filename), or
     *              failing that the most frequently used provider is selected.
     * @param  input
     *              The input to be given to the created tile. It doesn't need to be an existing
     *              {@linkplain java.io.File file} or URI since this method will not attempt to
     *              read it.
     * @param  imageIndex
     *              The image index to be given to the created tile (usually 0).
     * @return A global tile big enough for containing every tiles in this manager.
     * @throws NoSuchElementException
     *              If this manager do not contains at least one tile.
     * @throws IOException
     *              If an I/O operation was required and failed.
     */
    public Tile createGlobalTile(ImageReaderSpi provider, final Object input, final int imageIndex)
            throws NoSuchElementException, IOException
    {
        if (provider == null) {
            // Following line may throw the NoSuchElementException documented in javadoc.
            provider = getImageReaderSpis().iterator().next();
            ImageReaderSpi inferred = Tile.getImageReaderSpi(input);
            if (inferred != null && inferred != provider) {
                final Collection<String> f1 = Arrays.asList(provider.getFormatNames());
                final Collection<String> f2 = Arrays.asList(inferred.getFormatNames());
                if (!f1.containsAll(f2)) {
                    provider = inferred;
                }
            }
        }
        final Tile tile;
        final ImageGeometry geometry = getGridGeometry();
        if (geometry == null) {
            tile = new LargeTile(provider, input, imageIndex, getRegion());
        } else {
            tile = new LargeTile(provider, input, imageIndex, geometry.getGridRange());
            tile.setGridToCRS(geometry.getGridToCRS());
        }
        return tile;
    }

    /**
     * Returns a reference to the tiles used internally by the tile manager. The returned collection
     * must contains only direct references to the tiles hold internally, not instances created on
     * the fly (as {@link GridTileManager} can do). This is because we want to update the state of
     * those tiles in a persistent way if this method is invoked by {@link #setGridToCRS}.
     * <p>
     * Callers of this method should not rely on the {@linkplain Tile#getInput tile input} and
     * should not attempt to read the tiles, since the inputs can be non-existant files or patterns
     * (again the case of {@link GridTileManager}). This method is not public for that reason.
     * <p>
     * The default implementation returns {@link #getTiles}.
     *
     * @return The internal tiles. If the returned collection is an instance of
     *         {@link FrequencySortedSet}, then the frequencies will be honored
     *         in methods where it matter like {@link #getImageReaderSpis}.
     * @throws IOException If an I/O operation was required and failed.
     */
    Collection<Tile> getInternalTiles() throws IOException {
        return getTiles();
    }

    /**
     * Returns all tiles.
     *
     * @return The tiles.
     * @throws IOException If an I/O operation was required and failed.
     */
    public abstract Collection<Tile> getTiles() throws IOException;

    /**
     * Returns every tiles that intersect the given region.
     *
     * @param region
     *          The region of interest (shall not be {@code null}).
     * @param subsampling
     *          On input, the number of source columns and rows to advance for each pixel. On
     *          output, the effective values to use. Those values may be different only if
     *          {@code subsamplingChangeAllowed} is {@code true}.
     * @param subsamplingChangeAllowed
     *          If {@code true}, this method is allowed to replace {@code subsampling} by the
     *          highest subsampling that overviews can handle, not greater than the given
     *          subsampling.
     * @return The tiles that intercept the given region. May be empty but never {@code null}.
     * @throws IOException If it was necessary to fetch an image dimension from its
     *         {@linkplain Tile#getImageReader reader} and this operation failed.
     */
    public abstract Collection<Tile> getTiles(Rectangle region, Dimension subsampling,
            boolean subsamplingChangeAllowed) throws IOException;

    /**
     * Returns {@code true} if at least one tile having the given subsampling or a finer
     * one intersects the given region. The default implementation returns {@code true} if
     * <code>{@linkplain #getTiles(Rectangle,Dimension,boolean) getTiles}(region, subsampling, false)</code>
     * returns a non-empty set. Subclasses are encouraged to provide a more efficient implementation.
     *
     * @param  region
     *          The region of interest (shall not be {@code null}).
     * @param  subsampling
     *          The maximal subsampling to look for.
     * @return {@code true} if at least one tile having the given subsampling or a finer one
     *          intersects the given region.
     * @throws IOException If it was necessary to fetch an image dimension from its
     *         {@linkplain Tile#getImageReader reader} and this operation failed.
     */
    public boolean intersects(Rectangle region, Dimension subsampling) throws IOException {
        return !getTiles(region, subsampling, false).isEmpty();
    }

    /**
     * Checks for file existence and image size of every tiles and reports any error found.
     *
     * @param out Where to report errors ({@code null} for default, which is the
     *            {@linkplain System#out standard output stream}).
     */
    public void printErrors(PrintWriter out) {
        if (out == null) {
            out = new PrintWriter(System.out, true);
        }
        final Collection<Tile> tiles;
        try {
            tiles = getTiles();
        } catch (IOException e) {
            e.printStackTrace(out);
            return;
        }
        for (final Tile tile : tiles) {
            final int imageIndex = tile.getImageIndex();
            ImageReader reader = null;
            String message = null;
            try {
                final Rectangle region = tile.getRegion();
                reader = tile.getImageReader();
                final int width  = reader.getWidth (imageIndex);
                final int height = reader.getHeight(imageIndex);
                if (width != region.width || height != region.height) {
                    message = Errors.format(ErrorKeys.UNEXPECTED_IMAGE_SIZE);
                }
                Tile.dispose(reader);
                reader = null;
            } catch (IOException exception) {
                message = exception.toString();
            } catch (RuntimeException exception) {
                message = exception.toString();
            }
            if (message != null) {
                out.println(tile);
                out.print("    ");
                out.println(message);
            }
            // In case an exception was thrown before Tile.dispose(reader).
            if (reader != null) {
                reader.dispose();
            }
        }
    }

    /**
     * Returns a string representation of this tile manager. The default implementation
     * formats the first tiles in a table. Subclasses may format the tiles in a tree
     * instead. Note that in both cases the result may be a quite long string.
     *
     * @return A string representation.
     */
    @Override
    public String toString() {
        final Collection<Tile> tiles;
        try {
            tiles = getTiles();
        } catch (IOException e) {
            return e.toString();
        }
        /*
         * If each lines are 100 characters long, then limiting the formatting to 10000 tiles
         * will limit memory consumption to approximatively 1 Mb.
         */
        return Tile.toString(tiles, 10000);
    }
}
