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

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.imageio.ImageReader;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.spi.IIORegistry;
import javax.imageio.spi.ImageReaderSpi;

import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.ImageGeometry;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.math.Fraction;
import org.geotools.math.XMath;
import org.geotools.referencing.operation.builder.GridToEnvelopeMapper;
import org.geotools.resources.XArray;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Errors;
import org.geotools.resources.image.ImageUtilities;
import org.opengis.geometry.Envelope;
import org.opengis.referencing.datum.PixelInCell;


/**
 * A convenience class for building tiles using the same {@linkplain ImageReader image reader}
 * and organized according some common {@linkplain TileLayout tile layout}. Optionally, this
 * builder can also write the tiles to disk from an initially untiled image.
 *
 * @since 2.5
 *
 * @source $URL$
 * @version $Id$
 * @author Cédric Briançon
 * @author Martin Desruisseaux
 */
public class MosaicBuilder {
    /**
     * The default tile size in pixels.
     */
    private static final int DEFAULT_TILE_SIZE = 512;

    /**
     * Minimum tile size when using {@link TileLayout#CONSTANT_GEOGRAPHIC_AREA} without
     * explicit subsamplings provided by user.
     */
    private static final int MIN_TILE_SIZE = 64;

    /**
     * The factory to use for creating {@link TileManager} instances.
     */
    protected final TileManagerFactory factory;

    /**
     * The desired layout.
     */
    private TileLayout layout;

    /**
     * The tile directory, or {@code null} for current directory.
     * It may be either a relative or absolute path.
     */
    private File directory;

    /**
     * The image reader provider. The initial value is {@code null}.
     * This value must be set before {@link Tile} objects are created.
     */
    private ImageReaderSpi tileReaderSpi;

    /**
     * The envelope for the mosaic as a whole, or {@code null} if none. This is optional, but
     * if specified this builder uses it for assigning values to {@link Tile#getGridToCRS}.
     */
    private GeneralEnvelope mosaicEnvelope;

    /**
     * The raster bounding box in pixel coordinates. The initial value is {@code null}.
     * This value must be set before {@link Tile} objects are created.
     */
    private Rectangle untiledBounds;

    /**
     * The desired tile size. The initial value is {@code null}.
     * This value must be set before {@link Tile} objects are created.
     */
    private Dimension tileSize;

    /**
     * The subsamplings to use when creating a new overview. Values at even index are
     * <var>x</var> subsamplings and values at odd index are <var>y</var> subsamplings.
     * If {@code null}, subsampling will be computed automatically from the image and
     * tile size in order to get only entire tiles.
     */
    private int[] subsamplings;

    /**
     * The filename formatter.
     */
    private final FilenameFormatter formatter;

    /**
     * The logging level for tiling information during reads and writes.
     */
    private Level logLevel = Level.FINE;

    /**
     * Generates tiles using the default factory.
     */
    public MosaicBuilder() {
        this(null);
    }

    /**
     * Generates tiles using the specified factory.
     *
     * @param factory The factory to use, or {@code null} for the
     *        {@linkplain TileManagerFactory#DEFAULT default} one.
     */
    public MosaicBuilder(final TileManagerFactory factory) {
        this.factory = (factory != null) ? factory : TileManagerFactory.DEFAULT;
        layout = TileLayout.CONSTANT_TILE_SIZE;
        formatter = new FilenameFormatter();
    }

    /**
     * Returns the logging level for tile information during read and write operations.
     *
     * @return The current logging level.
     */
    public Level getLogLevel() {
        return logLevel;
    }

    /**
     * Sets the logging level for tile information during read and write operations.
     * The default value is {@link Level#FINE}. A {@code null} value restore the default.
     *
     * @param level The new logging level.
     */
    public void setLogLevel(Level level) {
        if (level == null) {
            level = Level.FINE;
        }
        logLevel = level;
    }

    /**
     * Returns the tile layout. The default value is
     * {@link TileLayout#CONSTANT_TILE_SIZE CONSTANT_TILE_SIZE}, which is the most efficient
     * layout available in {@code org.geotools.image.io.mosaic} implementation.
     *
     * @return An identification of current tile layout.
     */
    public TileLayout getTileLayout() {
        return layout;
    }

    /**
     * Sets the tile layout to the specified value. Valid values are
     * {@link TileLayout#CONSTANT_TILE_SIZE CONSTANT_TILE_SIZE} and
     * {@link TileLayout#CONSTANT_GEOGRAPHIC_AREA CONSTANT_GEOGRAPHIC_AREA}.
     *
     * @param layout An identification of new tile layout.
     */
    public void setTileLayout(final TileLayout layout) {
        if (layout != null) {
            switch (layout) {
                case CONSTANT_TILE_SIZE:
                case CONSTANT_GEOGRAPHIC_AREA: {
                    this.layout = layout;
                    return;
                }
            }
        }
        throw new IllegalArgumentException(Errors.format(
                ErrorKeys.ILLEGAL_ARGUMENT_$2, "layout", layout));
    }

    /**
     * Returns the tile directory, or {@code null} for current directory. The directory
     * may be either relative or absolute. The default value is {@code null}.
     *
     * @return The current tiles directory.
     */
    public File getTileDirectory() {
        return directory;
    }

    /**
     * Sets the directory where tiles will be read or written. May be a relative or absolute
     * path, or {@code null} (the default) for current directory.
     *
     * @param directory The new tiles directory.
     */
    public void setTileDirectory(final File directory) {
        this.directory = directory;
    }

    /**
     * Returns the {@linkplain ImageReader image reader} provider to use for reading tiles.
     * The initial value is {@code null}, which means that the provider should be the same
     * than the one detected by {@link #writeFromUntiledImage writeFromUntiledImage}.
     *
     * @return The current image reader provider for tiles.
     */
    public ImageReaderSpi getTileReaderSpi() {
        return tileReaderSpi;
    }

    /**
     * Sets the {@linkplain ImageReader image reader} provider for each tiles to be read.
     * A {@code null} value means that the provider should be automatically detected by
     * {@link #writeFromUntiledImage writeFromUntiledImage}.
     *
     * @param provider The new image reader provider for tiles.
     */
    public void setTileReaderSpi(final ImageReaderSpi provider) {
        this.tileReaderSpi = provider;
    }

    /**
     * Sets the {@linkplain ImageReader image reader} provider by name. This convenience method
     * searchs a provider for the given name in the default {@link IIORegistry} and delegates to
     * {@link #setTileReaderSpi(ImageReaderSpi)}.
     *
     * @param format The image format name for tiles.
     * @throws IllegalArgumentException if no provider was found for the given name.
     */
    public void setTileReaderSpi(String format) throws IllegalArgumentException {
        ImageReaderSpi spi = null;
        if (format != null) {
            format = format.trim();
            final IIORegistry registry = IIORegistry.getDefaultInstance();
            final Iterator<ImageReaderSpi> it=registry.getServiceProviders(ImageReaderSpi.class, true);
            do {
                if (!it.hasNext()) {
                    throw new IllegalArgumentException(Errors.format(
                            ErrorKeys.UNKNOW_IMAGE_FORMAT_$1, format));
                }
                spi = it.next();
            } while (!XArray.contains(spi.getFormatNames(), format));
        }
        setTileReaderSpi(spi);
    }

    /**
     * Returns the envelope for the mosaic as a whole, or {@code null} if none. This is optional,
     * but if specified this builder uses it for assigning values to {@link Tile#getGridToCRS}.
     *
     * @return The current envelope, or {@code null} if none.
     */
    public Envelope getMosaicEnvelope() {
        return (mosaicEnvelope != null) ? mosaicEnvelope.clone() : null;
    }

    /**
     * Sets the envelope for the mosaic as a whole, or {@code null} if none. This is optional,
     * but if specified this builder uses it for assigning values to {@link Tile#getGridToCRS}.
     * <p>
     * This is merely a convenient way to invoke {@link TileManager#setGridToCRS} with a transform
     * computed from the envelope and the {@linkplain #getUntiledImageBounds untiled image bounds},
     * where the later may be known only at reading time. As always, creating "grid to CRS" from an
     * envelope is ambiguous, since we don't know if axis need to be interchanged, <var>y</var> axis
     * flipped, <cite>etc.</cite> Subclasses can gain more control by overriding the
     * {@link #createGridToEnvelopeMapper createGridToEnvelopeMapper} method. The default behavior
     * fits most typical cases however.
     *
     * @param envelope The new envelope, or {@code null} if none.
     *
     * @see #createGridToEnvelopeMapper
     */
    public void setMosaicEnvelope(final Envelope envelope) {
        mosaicEnvelope = (envelope != null) ? new GeneralEnvelope(envelope) : null;
    }

    /**
     * Returns the bounds of the untiled image, or {@code null} if not set. In the later case, the
     * bounds will be inferred from the input image when {@link #writeFromUntiledImage} is invoked.
     *
     * @return The current untiled image bounds.
     */
    public Rectangle getUntiledImageBounds() {
        return (untiledBounds != null) ? (Rectangle) untiledBounds.clone() : null;
    }

    /**
     * Sets the bounds of the untiled image to the specified value.
     * A {@code null} value discarts any value previously set.
     *
     * @param bounds The new untiled image bounds.
     */
    public void setUntiledImageBounds(final Rectangle bounds) {
        untiledBounds = (bounds != null) ? new Rectangle(bounds) : null;
    }

    /**
     * Returns the tile size. If no tile size has been explicitly set, then a default tile size
     * will be computed from the {@linkplain #getUntiledImageBounds untiled image bounds}. If no
     * size can be computed, then this method returns {@code null}.
     *
     * @return The current tile size.
     *
     * @see #suggestedTileSize
     */
    public Dimension getTileSize() {
        if (tileSize == null) {
            final Rectangle untiledBounds = getUntiledImageBounds();
            if (untiledBounds == null) {
                return null;
            }
            int width  = untiledBounds.width;
            int height = untiledBounds.height;
            width  = suggestedTileSize(width);
            height = (height == untiledBounds.width) ? width : suggestedTileSize(height);
            tileSize = new Dimension(width, height);
        }
        return (Dimension) tileSize.clone();
    }

    /**
     * Sets the tile size. A {@code null} value discarts any value previously set.
     *
     * @param size The new tile size.
     */
    public void setTileSize(final Dimension size) {
        if (size == null) {
            tileSize = null;
        } else {
            if (size.width < 2 || size.height < 2) {
                throw new IllegalArgumentException(Errors.format(
                        ErrorKeys.ILLEGAL_ARGUMENT_$1, "size"));
            }
            tileSize = new Dimension(size);
        }
    }

    /**
     * Returns the suggested tile size using default values.
     */
    private static int suggestedTileSize(final int imageSize) {
        return suggestedTileSize(imageSize, DEFAULT_TILE_SIZE,
                DEFAULT_TILE_SIZE - DEFAULT_TILE_SIZE/4,
                DEFAULT_TILE_SIZE + DEFAULT_TILE_SIZE/4);
    }

    /**
     * Returns a suggested tile size ({@linkplain Dimension#width width} or
     * {@linkplain Dimension#height height}) for the given image size. This
     * method searchs for a value <var>x</var> inside the {@code [minSize...maxSize]}
     * range where {@code imageSize}/<var>x</var> has the largest amount of
     * {@linkplain XMath#divisors divisors}. If more than one value have the same amount
     * of divisors, then the one which is the closest to {@code tileSize} is returned.
     *
     * @param  imageSize The image size.
     * @param  tileSize  The preferred tile size. Must be inside the {@code [minSize...maxSize]} range.
     * @param  minSize   The minimum size, inclusive. Must be greater than 0.
     * @param  maxSize   The maximum size, inclusive. Must be equals or greater that {@code minSize}.
     * @return The suggested tile size. Inside the {@code [minSize...maxSize]} range except
     *         if {@code imageSize} was smaller than {@link minSize}.
     * @throws IllegalArgumentException if any argument doesn't meet the above-cited conditions.
     */
    public static int suggestedTileSize(final int imageSize, final int tileSize,
                                        final int minSize,   final int maxSize)
            throws IllegalArgumentException
    {
        if (minSize <= 1 || minSize > maxSize) {
            throw new IllegalArgumentException(Errors.format(
                    ErrorKeys.BAD_RANGE_$2, minSize, maxSize));
        }
        if (tileSize < minSize || tileSize > maxSize) {
            throw new IllegalArgumentException(Errors.format(
                    ErrorKeys.VALUE_OUT_OF_BOUNDS_$3, tileSize, minSize, maxSize));
        }
        if (imageSize <= minSize) {
            return imageSize;
        }
        int numDivisors = 0;
        int best = tileSize;
        for (int i=minSize; i<=maxSize; i++) {
            if (imageSize % i != 0) {
                continue;
            }
            // Note: Fraction rounding mode must be the same than in getSubsamplings().
            final int n = XMath.divisors(Fraction.round(imageSize, i)).length;
            if (n < numDivisors) {
                continue;
            }
            if (n == numDivisors) {
                if (Math.abs(i - tileSize) >= Math.abs(best - tileSize)) {
                    continue;
                }
            }
            best = i;
            numDivisors = n;
        }
        return best;
    }

    /**
     * Returns a suggested set of divisors of the number of tiles that can fit in an image.
     * More specifically, this method executes the following pseudo code twice, once for
     * {@linkplain Dimension#width width} and once for {@linkplain Dimension#height height},
     * resulting in two arrays of type {@code int[]}:
     *
     * <blockquote><code>
     * {@linkplain XMath#divisors divisors}({@linkplain Fraction#round round}(imageBounds / tileSize))
     * </code></blockquote>
     *
     * The two arrays are then decimated using the following procedures:
     * <p>
     * <ul>
     *   <li>If {@code multiples} is {@code true}, then the arrays are decimated in such a way
     *       that each value {@code divisors[i]} is a multiple of {@code divisors[i-1]}. This
     *       is useful for getting only tiles that can fit entirely in bigger tiles.</li>
     *   <li>The largest of the two arrays is trimmed in order to get arrays of the same
     *       length.</li>
     * <p>
     * If the number of divisors is lower than the {@code preferredCount}, then this method will
     * try again for smaller tiles until the preferred count or some other (currently undocumented)
     * stop condition is reached.
     * <p>
     * Let {@code r} be the return value. The following contract should hold in all cases:
     * <p>
     * <ul>
     *   <li>{@code r} is never {@code null}</li>
     *   <li>{@code r.length} is always 2</li>
     *   <li>{@code r[0].length} == {@code r[1].length} and this length is greater than 0</li>
     *   <li>{@code r[0][0]} == {@code r[1][0]} == 1 (i.e. the first divisor is always 1)</li>
     * </ul>
     *
     * @param  imageBounds The image size.
     * @param  tileSize The tile size.
     * @param  preferredCount The preferred minimal amount of divisors, or 0 if there is no
     *         minimal count.
     * @param  multiples If {@code true}, then the returned numbers are restricted to multiples.
     * @return Two arrays of the same length, which are respectively the divisors of
     *         image {@linkplain Rectangle#width width} and the divisors of image
     *         {@linkplain Rectangle#height height}.
     */
    public static int[][] suggestedNumTiles(final Rectangle imageBounds, final Dimension tileSize,
                                            final int preferredCount, final boolean multiples)
    {
        final int width  = tileSize.width;
        final int height = tileSize.height;
        final int[][] divisors = new int[2][];
        final int xmax = imageBounds.width  / width;
        final int ymax = imageBounds.height / height;
        final int maxScale = Math.min(tileSize.width, tileSize.height) / MIN_TILE_SIZE;
        int scale = 1;
        boolean oldTileDivideImage = false;
        do {
            final int[] oldX = divisors[0];
            final int[] oldY = divisors[1];
            final long dx = scale * (long) imageBounds.width;
            final long dy = scale * (long) imageBounds.height;
            final int  nx = (int) Fraction.round(dx, width);
            final int  ny = (int) Fraction.round(dy, height);
            final boolean tileDivideImage = (nx * width == dx) && (ny * height == dy);
            if (oldTileDivideImage && !tileDivideImage) {
                continue; // Doesn't worth to continue for this scale.
            }
            int[] sx = XMath.divisors(nx);
            int[] sy;
            if (nx == ny) {
                sx = XArray.resize(sx, decimate(sx, Math.min(xmax, ymax), multiples));
                divisors[0] = divisors[1] = sy = sx;
            } else {
                sy = XMath.divisors(ny);
                divisors[0] = sx = XArray.resize(sx, decimate(sx, xmax, multiples));
                divisors[1] = sy = XArray.resize(sy, decimate(sy, ymax, multiples));
                reduceLargest(divisors);
            }
            final int length = divisors[0].length;
            if (tileDivideImage && length >= preferredCount) {
                // In addition of the preferred count, we also favorise the results
                // computed from a tile size which is a divisor of the image bounds.
                break;
            }
            if (oldX != null) {
                if (tileDivideImage && !oldTileDivideImage) {
                    oldTileDivideImage = true;
                    continue; // Keep the new divisors.
                }
                if (length <= oldX.length) {
                    divisors[0] = oldX;
                    divisors[1] = oldY;
                }
            }
        } while (++scale <= maxScale);
        return divisors;
    }

    /**
     * Decimates the given array in-place so that each value {@code divisors[i]} is a multiple
     * of {@code divisors[i-1]}. Value greater than {@code maximum} are trimmed.
     *
     * @param  divisors The array to decimate in-place.
     * @param  maximum The maximal value to keep, inclusive.
     * @param  multiples If {@code false}, disables the restriction to multiples.
     * @return The number of valid values in the given array.
     */
    private static int decimate(final int[] divisors, final int maximum, final boolean multiples) {
        assert XArray.isStrictlySorted(divisors);
        int n;
        if (!multiples) {
            n = Arrays.binarySearch(divisors, maximum);
            if (n < 0) {
                n = (~n) - 1;
            }
        } else {
            n = 0;
            for (int i=1; i<divisors.length; i++) {
                final int x = divisors[i];
                if (x > maximum) {
                    break;
                }
                if ((x % divisors[n]) == 0) {
                    divisors[++n] = x;
                }
            }
        }
        return ++n;
    }

    /**
     * Given two arrays which may be of different length, remove some elements from the largest
     * array in order to get the same length than the shortest array. Arrays most be sorted in
     * strictly increasing order.
     *
     * @return The divisors of image {@linkplain Rectangle#width width} and the divisors of image
     *         {@linkplain Rectangle#height height}. The largest array will be replaced by a new
     *         one.
     */
    private static void reduceLargest(final int[][] divisors) {
        int[] large = divisors[0];
        int[] small = divisors[1];
        assert XArray.isStrictlySorted(large) && XArray.isStrictlySorted(small);
        if (large.length == small.length) {
            return;
        }
        final int target;
        if (large.length >= small.length) {
            target = 0;
        } else {
            target = 1;
            final int[] tmp = large;
            large = small;
            small = tmp;
        }
        final int[] reduced = new int[small.length];
        for (int i=0; i<small.length; i++) {
            int value = small[i];
            int k = Arrays.binarySearch(large, value);
            if (k < 0) {
                k = ~k; // Really tilde, not minus operator.
                if (k != 0 && (k == large.length || (large[k] - value) >= (value - large[k-1]))) {
                    k--;
                }
                value = large[k];
            }
            reduced[i] = value;
        }
        divisors[target] = reduced;
    }

    /**
     * Returns the subsampling for overview computations. If no subsamplings were {@linkplain
     * #setSubsamplings(Dimension[]) explicitly set}, then this method computes automatically
     * some subsamplings from the {@linkplain #getUntiledImageBounds untiled image bounds} and
     * {@linkplain #getTileSize tile size}, with the following properties (note that those
     * properties are not garanteed if the subsampling was explicitly specified rather than
     * computed):
     * <p>
     * <ul>
     *   <li>The first element in the returned array is (1,1).</li>
     *   <li>Elements are sorted by increasing subsampling values.</li>
     *   <li>At most one subsampling (the last one) results in an image big enough for holding
     *       the whole mosaic.</li>
     * </ul>
     * <p>
     * If no subsampling can be computed, then this method returns {@code null}.
     *
     * @return The current subsamplings for each overview levels.
     */
    public Dimension[] getSubsamplings() {
        if (subsamplings == null) {
            final Rectangle untiledBounds = getUntiledImageBounds();
            if (untiledBounds == null) {
                return null;
            }
            final Dimension tileSize = getTileSize();
            if (tileSize == null) {
                return null;
            }
            /*
             * If the tile layout is CONSTANT_GEOGRAPHIC_AREA, increasing the subsampling will have
             * the effect of reducing the tile size by the same amount, so we are better to choose
             * subsamplings that are divisors of the tile size.
             *
             * If the tile layout is CONSTANT_TILE_SIZE, increasing the subsampling will have the
             * effect of reducing the number of tiles required for covering the whole image. So we
             * are better to choose subsamplings that are divisors of the number of tiles. If the
             * number of tiles are not integers, we round towards nearest integers in the hope that
             * we get a number closer to user's intend.
             *
             * If the tile layout is unknown, we don't really know what to choose. We fallback on
             * some values that seem reasonable, but our fallback may change in future version.
             * It doesn't hurt any code in this module - the only consequence is that tiling may
             * be suboptimal.
             */
            final boolean constantArea = TileLayout.CONSTANT_GEOGRAPHIC_AREA.equals(layout);
            int nx = tileSize.width;
            int ny = tileSize.height;
            if (!constantArea) {
                // Must performs the division in the same way than in suggestedTileSize(...).
                nx = Fraction.round(untiledBounds.width,  nx);
                ny = Fraction.round(untiledBounds.height, ny);
            }
            int[] xSubsamplings = XMath.divisors(nx);
            if (nx != ny) {
                /*
                 * Subsamplings are different along x and y axis. We need at least arrays of the
                 * same length. While not strictly required, it is better that xSubsampling and
                 * ySubsampling are equal, assuming that pixels are square (otherwise we could
                 * multiply by a height/width ratio; it may be done in a future version). Current
                 * implementation keep the union of divisors.
                 *
                 * TODO: move the code that computes the union in XArray.union(int[],int[]).
                 */
                final int[] ySubsamplings = XMath.divisors(ny);
                final int[] union  = new int[xSubsamplings.length + ySubsamplings.length];
                int nu=0;
                for (int ix=0, iy=0;;) {
                    if (ix == xSubsamplings.length) {
                        final int no = ySubsamplings.length - iy;
                        System.arraycopy(ySubsamplings, iy, union, nu, no);
                        nu += no;
                        break;
                    }
                    if (iy == ySubsamplings.length) {
                        final int no = xSubsamplings.length - ix;
                        System.arraycopy(xSubsamplings, ix, union, nu, no);
                        nu += no;
                        break;
                    }
                    final int sx = xSubsamplings[ix];
                    final int sy = ySubsamplings[iy];
                    final int s;
                    if (sx <= sy) {
                        s = sx;
                        ix++;
                        if (sx == sy) {
                            iy++;
                        }
                    } else {
                        s = sy;
                        iy++;
                    }
                    union[nu++] = s;
                }
                xSubsamplings = XArray.resize(union, nu);
            }
            /*
             * Trims the subsamplings which would produce tiles smaller than the minimum size
             * (for CONSTANT_GEOGRAPHIC_AREA layout) or which would produce more than one tile
             * enclosing the whole image (for CONSTANT_TILE_SIZE layout). First, we calculate
             * as (nx,ny) the maximum subsamplings expected (inclusive). Then we search those
             * maximum in the actual subsampling and assign to (nx,ny) the new array length.
             */
            if (constantArea) {
                nx = tileSize.width  / MIN_TILE_SIZE;
                ny = tileSize.height / MIN_TILE_SIZE;
            } else {
                nx = (untiledBounds.width  - 1) / tileSize.width  + 1;
                ny = (untiledBounds.height - 1) / tileSize.height + 1;
            }
            // Increments (++) below are inconditional (outside the "if" block).
            nx = Arrays.binarySearch(xSubsamplings, nx); if (nx < 0) nx = ~nx; nx++;
            ny = Arrays.binarySearch(xSubsamplings, ny); if (ny < 0) ny = ~ny; ny++;
            final int length = Math.min(Math.max(nx, ny), xSubsamplings.length);
            subsamplings = new int[length * 2];
            int source = 0;
            for (int i=0; i<length; i++) {
                subsamplings[source++] = xSubsamplings[i];
                subsamplings[source++] = xSubsamplings[i];
            }
        }
        final Dimension[] dimensions = new Dimension[subsamplings.length / 2];
        int source = 0;
        for (int i=0; i<dimensions.length; i++) {
            dimensions[i] = new Dimension(subsamplings[source++], subsamplings[source++]);
        }
        return dimensions;
    }

    /**
     * Sets the subsamplings for overview computations. The number of overview levels created
     * by this {@code MosaicBuilder} will be equals to the {@code subsamplings} array length.
     * <p>
     * Subsamplings most be explicitly provided for {@link TileLayout#CONSTANT_GEOGRAPHIC_AREA},
     * but is optional for {@link TileLayout#CONSTANT_TILE_SIZE}. In the later case subsamplings
     * may be {@code null} (the default), in which case they will be automatically computed from
     * the {@linkplain #getUntiledImageBounds untiled image bounds} and {@linkplain #getTileSize
     * tile size} in order to have only entire tiles (i.e. tiles in last columns and last rows
     * don't need to be cropped).
     *
     * @param subsamplings The new subsamplings for each overview levels.
     */
    public void setSubsamplings(final Dimension[] subsamplings) {
        final int[] newSubsamplings;
        if (subsamplings == null) {
            newSubsamplings = null;
        } else {
            int target = 0;
            newSubsamplings = new int[subsamplings.length * 2];
            for (int i=0; i<subsamplings.length; i++) {
                final Dimension subsampling = subsamplings[i];
                final int xSubsampling = subsampling.width;
                final int ySubsampling = subsampling.height;
                if (xSubsampling < 1 || ySubsampling < 1) {
                    throw new IllegalArgumentException(Errors.format(
                            ErrorKeys.ILLEGAL_ARGUMENT_$1, "subsamplings[" + i + ']'));
                }
                newSubsamplings[target++] = xSubsampling;
                newSubsamplings[target++] = ySubsampling;
            }
        }
        this.subsamplings = newSubsamplings;
    }

    /**
     * Sets uniform subsamplings for overview computations. This convenience method delegates to
     * {@link #setSubsamplings(Dimension[])} with the same value affected to both
     * {@linkplain Dimension#width width} and {@linkplain Dimension#height height}.
     *
     * @param subsamplings The new subsamplings for each overview levels.
     */
    public void setSubsamplings(final int[] subsamplings) {
        final Dimension[] newSubsamplings;
        if (subsamplings == null) {
            newSubsamplings = null;
        } else {
            newSubsamplings = new Dimension[subsamplings.length];
            for (int i=0; i<subsamplings.length; i++) {
                final int subsampling = subsamplings[i];
                newSubsamplings[i] = new Dimension(subsampling, subsampling);
            }
        }
        // Delegates to setSubsamplings(Dimension[]) instead of performing the same work in-place
        // (which would have been more efficient) because the user may have overriden the former.
        setSubsamplings(newSubsamplings);
    }

    /**
     * Creates a tile manager from the informations supplied in above setters.
     * The following methods must be invoked prior this one:
     * <p>
     * <ul>
     *   <li>{@link #setUntiledImageBounds}</li>
     *   <li>{@link #setTileReaderSpi}</li>
     * </ul>
     * <p>
     * The other setter methods are optional.
     *
     * @return The tile manager created from the information returned by getter methods.
     * @throws IOException if an I/O operation was required and failed. The default implementation
     *         does not perform any I/O, but subclasses are allowed to do so.
     */
    public TileManager createTileManager() throws IOException {
        return createFromInput(null);
    }

    /**
     * Implementation of {@link #createTileManager()} with a given input. This method is not
     * public because it expects an argument controlling the behavior of tile writting, while
     * this method actually does not write anything to disk. The policy is used in order to
     * determine whatever this method should skip empty tiles or not. Skipping empty tiles are
     * usually performed when reading the original untiled image, because we know only at that
     * time which tiles are going to contain non-zero pixels. However it is possible to skip the
     * tiles that do not intersect any input tile. This is incomplete since some of the remaining
     * tiles may need to be skipped as well (we will do that later, during the write process),
     * but doing this early pre-filtering here can improve a lot the performance and memory usage.
     *
     * @param  input
     *          The tile manager for the input tiles, or {@code null} if none. If non-null, this is
     *          used only in order to filter the output tiles to the ones that intersect the input
     *          tiles. This value should be {@code null} if no such filtering should be applied.
     * @return The tile manager created from the information returned by getter methods.
     * @throws IOException if an I/O operation was required and failed.
     */
    @SuppressWarnings("fallthrough")
    private TileManager createFromInput(final TileManager input) throws IOException {
        tileReaderSpi = getTileReaderSpi();
        if (tileReaderSpi == null) {
            // TODO: We may try to detect automatically the Spi in a future version.
            throw new IllegalStateException(Errors.format(ErrorKeys.NO_IMAGE_READER));
        }
        untiledBounds = getUntiledImageBounds(); // Forces computation, if any.
        if (untiledBounds == null) {
            throw new IllegalStateException(Errors.format(ErrorKeys.UNSPECIFIED_IMAGE_SIZE));
        }
        tileSize = getTileSize(); // Forces computation
        if (tileSize == null) {
            tileSize = ImageUtilities.toTileSize(untiledBounds.getSize());
        }
        formatter.initialize(tileReaderSpi);
        final TileManager output;
        /*
         * Delegates to a method using an algorithm appropriate for the requested layout.
         */
        boolean constantArea = false;
        switch (layout) {
            case CONSTANT_GEOGRAPHIC_AREA: {
                constantArea = true;
                // Fall through
            }
            case CONSTANT_TILE_SIZE: {
                output = createFromInput(constantArea, canUsePattern(), input);
                break;
            }
            default: {
                throw new IllegalStateException(layout.toString());
            }
        }
        /*
         * After TileManager creation, computes the "grid to CRS" transform
         * if an envelope was given to this builder.
         */
        if (mosaicEnvelope != null && !mosaicEnvelope.isNull()) {
            final GridToEnvelopeMapper mapper = createGridToEnvelopeMapper(output);
            mapper.setGridRange(new GridEnvelope2D(untiledBounds));
            mapper.setEnvelope(mosaicEnvelope);
            output.setGridToCRS((AffineTransform) mapper.createTransform());
        }
        return output;
    }

    /**
     * Creates tiles for the following cases:
     * <ul>
     *   <li>covering a constant geographic region. The tile size will reduce as we progress into
     *       overviews levels. The {@link #minimumTileSize} value is the stop condition - no smaller
     *       tiles will be created.</li>
     *   <li>tiles of constant size in pixels. The stop condition is when a single tile cover
     *       the whole image.</li>
     * </ul>
     *
     * @param  constantArea
     *          {@code true} for constant area layout, or {@code false} for constant
     *          tile size layout.
     * @param  usePattern
     *          {@code true} for creating tiles using a pattern instead of creating
     *          individual instance of every tiles.
     * @param  input
     *          The tile manager for the input tiles, or {@code null} if none. If non-null, this is
     *          used only in order to filter the output tiles to the ones that intersect the input
     *          tiles. This value should be {@code null} if no such filtering should be applied.
     * @return The tile manager.
     * @throws IOException if an I/O operation was requested and failed.
     */
    private TileManager createFromInput(final boolean constantArea, final boolean usePattern,
                                        final TileManager input)
            throws IOException
    {
        final Dimension tileSize      = this.tileSize;      // Paranoiac compile-time safety against
        final Rectangle untiledBounds = this.untiledBounds; // unwanted reference assignments.
        final Rectangle imageBounds   = new Rectangle(untiledBounds);
        final Rectangle tileBounds    = new Rectangle(tileSize);
        Dimension[] subsamplings = getSubsamplings();
        if (subsamplings == null) {
            final int n;
            if (constantArea) {
                n = Math.max(tileBounds.width, tileBounds.height) / MIN_TILE_SIZE;
            } else {
                n = Math.max(imageBounds.width  / tileBounds.width,
                             imageBounds.height / tileBounds.height);
            }
            subsamplings = new Dimension[n];
            for (int i=1; i<=n; i++) {
                subsamplings[i-1] = new Dimension(i,i);
            }
        }
        final List<Tile> tiles;
        final OverviewLevel[] levels;
        if (usePattern) {
            tiles  = null;
            levels = new OverviewLevel[subsamplings.length];
        } else {
            tiles  = new ArrayList<Tile>();
            levels = null;
        }
        final Rectangle absoluteBounds = new Rectangle();
        /*
         * For each overview level, computes the size of tiles and the size of the mosaic as
         * a whole. The 'tileBounds' and 'imageBounds' rectangles are overwritten during each
         * iteration. The filename formatter is configured according the expected number of
         * tiles computed from the bounds.
         */
        formatter.computeLevelFieldSize(subsamplings.length);
        for (int level=0; level<subsamplings.length; level++) {
            final Dimension subsampling = subsamplings[level];
            final int xSubsampling = subsampling.width;
            final int ySubsampling = subsampling.height;
            imageBounds.setBounds(untiledBounds.x      / xSubsampling,
                                  untiledBounds.y      / ySubsampling,
                                  untiledBounds.width  / xSubsampling,
                                  untiledBounds.height / ySubsampling);
            tileBounds.setBounds(imageBounds);
            tileBounds.setSize(tileSize);
            if (constantArea) {
                tileBounds.width  /= xSubsampling;
                tileBounds.height /= ySubsampling;
            } else {
                if (tileBounds.width  > imageBounds.width)  tileBounds.width  = imageBounds.width;
                if (tileBounds.height > imageBounds.height) tileBounds.height = imageBounds.height;
            }
            formatter.computeFieldSizes(imageBounds, tileBounds);
            /*
             * If we are allowed to use a pattern, create directly the pattern string.
             * Example of pattern: "File:directory/L{level:1}_{column:2}{row:2}.png".
             * It will take much less memory than creating every individual tiles, but
             * is possible only if the user didn't customized too much the tiles creation.
             */
            if (usePattern) {
                String pattern = formatter.toString();
                pattern = new File(directory, pattern).getPath();
                pattern = "File:" + pattern;
                final Tile tile = new Tile(tileReaderSpi, pattern, 0, tileBounds, subsampling);
                final OverviewLevel ol = new OverviewLevel(tile, imageBounds);
                ol.createLinkedList(level, (level != 0) ? levels[level - 1] : null);
                if (input != null) {
                    final int nx = ol.getNumXTiles();
                    final int ny = ol.getNumYTiles();
                    absoluteBounds.width  = xSubsampling * tileBounds.width;
                    absoluteBounds.height = ySubsampling * tileBounds.height;
                    absoluteBounds.y      = ySubsampling * tileBounds.y;
                    for (int y=0; y<ny; y++) {
                        absoluteBounds.x = xSubsampling * tileBounds.x;
                        for (int x=0; x<nx; x++) {
                            if (!input.intersects(absoluteBounds, subsampling)) {
                                ol.removeTile(x, y);
                            }
                            absoluteBounds.x += absoluteBounds.width;
                        }
                        absoluteBounds.y += absoluteBounds.height;
                    }
                }
                levels[level] = ol;
            } else {
                /*
                 * If we are not allowed to use a pattern, enumerate every tiles individually.
                 * We will let TileManagerFactory tries to figure out a layout from them. Note
                 * that the factory may create a GridTileManager instance anyway, but the later
                 * will typically be more customized than the one created in the 'usePattern' case.
                 */
                final int xmin = imageBounds.x;
                final int ymin = imageBounds.y;
                final int xmax = imageBounds.width  + xmin;
                final int ymax = imageBounds.height + ymin;
                final int dx   = tileBounds.width;
                final int dy   = tileBounds.height;
                absoluteBounds.width  = xSubsampling * dx;
                absoluteBounds.height = ySubsampling * dy;
                int y = 0;
                for (tileBounds.y = ymin; tileBounds.y < ymax; tileBounds.y += dy, y++) {
                    int x = 0;
                    absoluteBounds.y = ySubsampling * tileBounds.y;
                    for (tileBounds.x = xmin; tileBounds.x < xmax; tileBounds.x += dx, x++) {
                        if (input != null) {
                            absoluteBounds.x = xSubsampling * tileBounds.x;
                            if (!input.intersects(absoluteBounds, subsampling)) {
                                continue;
                            }
                        }
                        Rectangle clippedBounds = tileBounds.intersection(imageBounds);
                        File file = new File(directory, generateFilename(level, x, y));
                        Tile tile = new Tile(tileReaderSpi, file, 0, clippedBounds, subsampling);
                        tiles.add(tile);
                    }
                }
            }
        }
        /*
         * Creates the tile manager. If assertions are enabled, the manager created using
         * patterns will be compared to the manager created by enumerating every tiles.
         */
        final TileManager manager;
        if (usePattern) {
            manager = new GridTileManager(levels[levels.length - 1]);
            /*
             * Following assertion creates a new TileManager by enumerating every tiles
             * (instead than using the pattern) and makes sure that we get the same set
             * of tiles. The later comparaison is trigged by the call to getTiles().
             */
            assert !(new ComparedTileManager(manager,
                    createFromInput(constantArea, false, input)).getTiles().isEmpty());
        } else {
            final TileManager[] managers = factory.create(tiles);
            manager = managers[0];
        }
        return manager;
    }

    /**
     * The mosaic image writer to be used by {@link MosaicBuilder#writeFromUntiledImage}.
     */
    private final class Writer extends MosaicImageWriter {
        /**
         * The tile writing policy. Should be identical to the value given to
         * {@link MosaicImageWriteParam#setTileWritingPolicy}.
         */
        private final TileWritingPolicy policy;

        /**
         * Index of the untiled image to read.
         */
        private final int inputIndex;

        /**
         * The input tile managers, or {@code null} if none.
         */
        TileManager[] inputTiles;

        /**
         * The tiles created by {@link MosaicBuilder#createTileManager}.
         * Will be set by {@link #filter} and read by {@link MosaicBuilder}.
         */
        TileManager outputTiles;

        /**
         * Creates a writer for an untiled image to be read at the given index.
         */
        Writer(final int inputIndex, final TileWritingPolicy policy) {
            this.inputIndex = inputIndex;
            this.policy = policy;
        }

        /**
         * Creates the tiles for the specified untiled images.
         */
        @Override
        protected boolean filter(ImageReader reader) throws IOException {
            final Rectangle bounds = new Rectangle();
            bounds.width  = reader.getWidth (inputIndex);
            bounds.height = reader.getHeight(inputIndex);
            TileManager input = null;
            // Sets only after successful reading of image size.
            if (reader instanceof MosaicImageReader) {
                final MosaicImageReader mosaic = (MosaicImageReader) reader;
                inputTiles = mosaic.getInput(); // Should not be null as of filter(...) contract.
                if (inputTiles.length > inputIndex && (policy != null && !policy.includeEmpty)) {
                    input = inputTiles[inputIndex];
                }
                reader = mosaic.getTileReader();
            }
            if (reader != null) { // May be null as a result of above line.
                final ImageReaderSpi spi = reader.getOriginatingProvider();
                if (spi != null && getTileReaderSpi() == null) {
                    setTileReaderSpi(spi);
                }
            }
            setUntiledImageBounds(bounds);
            outputTiles = createFromInput(input);
            try {
                setOutput(outputTiles);
            } catch (IllegalArgumentException exception) {
                final Throwable cause = exception.getCause();
                if (cause instanceof IOException) {
                    throw (IOException) cause;
                }
                throw exception;
            }
            return true;
        }

        /**
         * Invoked when a tile is about to be written. Delegates to a method that users can
         * override.
         */
        @Override
        protected void onTileWrite(Tile tile, ImageWriteParam parameters) throws IOException {
            MosaicBuilder.this.onTileWrite(tile, parameters);
        }
    }

    /**
     * Creates a tile manager from an untiled image. The {@linkplain #getUntiledImageBounds
     * untiled image bounds} and {@linkplain #getTileReaderSpi tile reader SPI} are inferred
     * from the input, unless they were explicitly specified.
     * <p>
     * This method does not write any tile to disk.
     *
     * @param input The image input, typically as a {@link File} or an other {@link TileManager}.
     * @return The tiles, or {@code null} if the process has been aborted.
     * @throws IOException if an error occured while reading the untiled image.
     */
    public TileManager createTileManager(final Object input) throws IOException {
        return createTileManager(input, 0, TileWritingPolicy.NO_WRITE);
    }

    /**
     * Creates a tile manager from an untiled image. The {@linkplain #getUntiledImageBounds
     * untiled image bounds} and {@linkplain #getTileReaderSpi tile reader SPI} are inferred
     * from the input, unless they were explicitly specified.
     * <p>
     * Optionnaly if the tile writing policy is anything else than
     * {@link TileWritingPolicy#NO_WRITE NO_WRITE}, then pixel values are read from the untiled
     * images, organized in tiles as specified by the {@link TileManager} to be returned and saved
     * to disk. This work is done using a default {@link MosaicImageWriter}.
     *
     * @param  input The image input, typically as a {@link File} or an other {@link TileManager}.
     * @param  inputIndex Index of image to read, typically 0.
     * @param  policy Sets whatever tiles are created and saved to disk.
     * @return The tiles, or {@code null} if the process has been aborted while writing tiles.
     * @throws IOException if an error occured while reading the untiled image or (only if
     *         {@code writeTiles} is {@code true}) while writting the tiles to disk.
     */
    public TileManager createTileManager(final Object input, final int inputIndex,
                                         final TileWritingPolicy policy) throws IOException
    {
        formatter.ensurePrefixSet(input);
        final Writer writer = new Writer(inputIndex, policy);
        writer.setLogLevel(getLogLevel());
        final MosaicImageWriteParam param = writer.getDefaultWriteParam();
        param.setTileWritingPolicy(policy);
        try {
            if (!writer.writeFromInput(input, inputIndex, param)) {
                return null;
            }
        } finally {
            writer.dispose();
        }
        TileManager tiles = writer.outputTiles;
        /*
         * Before to return the tile manager, if no geometry has been inferred from the target
         * tiles (typically because no setEnvelope(...) has not been invoked), then inherit the
         * geometry from the source tile, if there is any. This operation is conservative and
         * performed only on a "best effort" basis.
         */
        if (tiles.geometry == null) {
            if (writer.inputTiles != null) {
                for (final TileManager candidate : writer.inputTiles) {
                    final ImageGeometry geometry = candidate.getGridGeometry();
                    if (geometry != null) {
                        tiles.setGridToCRS(geometry.getGridToCRS());
                        break;
                    }
                }
            }
        }
        return tiles;
    }

    /**
     * Returns {@code true} if we can create {@link TileManager} using a regular pattern instead
     * than enumerating every tiles. This method returns {@code true} if {@link #generateFilename}
     * has not be overriden, otherwise we can't guess at this stage the pattern that the user is
     * applying.
     */
    private boolean canUsePattern() {
        final Class<?>[] parameters = new Class[3];
        Arrays.fill(parameters, Integer.TYPE);
        Class<?> classe = getClass();
        Method method;
        do try {
            method = classe.getDeclaredMethod("generateFilename", parameters);
            return method.getDeclaringClass().equals(MosaicBuilder.class);
        } catch (NoSuchMethodException e) {
            classe = classe.getSuperclass();
        } while (classe != null);
        // Would be a programming error. The method we are looking for is just below.
        throw new AssertionError();
    }

    /**
     * Generates a filename for the current tile based on the position of this tile in the raster.
     * For example, a tile in the first overview level, which is localized on the 5th column and
     * 2nd row may have a name like "{@code L1_E2.png}".
     * <p>
     * Subclasses may override this method if they want more control on generated tile filenames.
     *
     * @param  level     The level of overview. First level is 0.
     * @param  column    The index of columns. First column is 0.
     * @param  row       The index of rows. First row is 0.
     * @return A filename based on the position of the tile in the whole raster.
     */
    protected String generateFilename(final int level, final int column, final int row) {
        return formatter.generateFilename(level, column, row);
    }

    /**
     * Invoked automatically when a "<cite>grid to CRS</cite>" transform needs to be computed. The
     * default implementation returns a new {@link GridToEnvelopeMapper} instance in its default
     * configuration, except for the {@linkplain GridToEnvelopeMapper#setPixelAnchor pixel anchor}
     * which is set to {@link PixelInCell#CELL_CORNER CELL_CORNER} (OGC specification maps pixel
     * center, while Java I/O maps pixel upper-left corner).
     * <p>
     * Subclasses may override this method in order to configure the mapper in an other way.
     *
     * @param tiles The tiles for which a "<cite>grid to CRS</cite>" transform needs to be computed.
     * @return An "grid to envelope" mapper having the desired configuration.
     *
     * @see #setMosaicEnvelope
     */
    protected GridToEnvelopeMapper createGridToEnvelopeMapper(final TileManager tiles) {
        final GridToEnvelopeMapper mapper = new GridToEnvelopeMapper();
        mapper.setPixelAnchor(PixelInCell.CELL_CORNER);
        return mapper;
    }

    /**
     * Invoked automatically when a tile is about to be written. The default implementation does
     * nothing. Subclasses can override this method in order to set custom write parameters. The
     * {@linkplain ImageWriteParam#setSourceRegion source region} and
     * {@linkplain ImageWriteParam#setSourceSubsampling source subsampling} should not be set
     * since they will be inconditionnaly overwritten by the caller.
     *
     * @param  tile The tile to be written.
     * @param  parameters The parameters to be given to the {@linkplain ImageWriter image writer}.
     * @throws IOException if an I/O operation was required and failed.
     */
    protected void onTileWrite(Tile tile, ImageWriteParam parameters) throws IOException {
    }
}
