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

import java.awt.Point;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.net.URL;
import java.net.URI;
import java.net.URISyntaxException;
import java.io.*; // We use a lot of those imports.
import java.util.Arrays;
import java.util.Iterator;
import java.util.Collection;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.spi.IIORegistry;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.FileImageInputStream;

import org.geotools.io.TableWriter;
import org.geotools.util.Utilities;
import org.geotools.util.logging.Logging;
import org.geotools.resources.XArray;
import org.geotools.resources.Classes;
import org.geotools.resources.i18n.Errors;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Vocabulary;
import org.geotools.resources.i18n.VocabularyKeys;

import static java.lang.Math.min;
import static java.lang.Math.max;


/**
 * A tile to be read by {@link MosaicImageReader}. Each tile must contains the following:
 * <p>
 * <ul>
 *   <li><p><b>An {@link ImageReaderSpi} instance</b>. The same provider is typically used for every
 *   tiles, but this is not mandatory. An {@linkplain ImageReader image reader} will be instantiated
 *   and the {@linkplain #getInput input} will be assigned to it before a tile is read.</p></li>
 *
 *   <li><p><b>An input</b>, typically a {@linkplain File file}, {@linkplain URL}, {@linkplain URI}
 *   or {@linkplain String}. The input is typically different for every tile to be read, but this
 *   is not mandatory. For example different tiles could be stored at different
 *   {@linkplain #getImageIndex image index} in the same file.</p></li>
 *
 *   <li><p><b>An image index</b> to be given to {@link ImageReader#read(int)} for reading the
 *   tile. This index is often 0.</p></li>
 *
 *   <li><p><b>The upper-left corner</b> in the destination image as a {@linkplain Point point},
 *   or the upper-left corner together with the image size as a {@linkplain Rectangle rectangle}.
 *   If the upper-left corner has been given as a {@linkplain Point point}, then the
 *   {@linkplain ImageReader#getWidth width} and {@linkplain ImageReader#getHeight height} will
 *   be obtained from the image reader when first needed, which may have a slight performance cost.
 *   If the upper-left corner has been given as a {@linkplain Rectangle rectangle} instead, then
 *   this performance cost is avoided but the user is responsible for the accuracy of the
 *   information provided.
 *
 *     <blockquote><font size=2>
 *     <b>NOTE:</b> The upper-left corner is the {@linkplain #getLocation location} of this tile
 *     in the {@linkplain javax.imageio.ImageReadParam#setDestination destination image} when no
 *     {@linkplain javax.imageio.ImageReadParam#setDestinationOffset destination offset} are
 *     specified. If the user specified a destination offset, then the tile location will be
 *     translated accordingly for the image being read.
 *     </font></blockquote></p></li>
 *
 *   <li><p><b>The subsampling relative to the tile having the best resolution.</b> This is not
 *   the subsampling to apply when reading this tile, but rather the subsampling that we would
 *   need to apply on the tile having the finest resolution in order to produce an image equivalent
 *   to this tile. The subsampling is (1,1) for the tile having the finest resolution, (2,3) for an
 *   overview having half the width and third of the height for the same geographic extent,
 *   <cite>etc.</cite> (note that overviews are not required to have the same geographic extent -
 *   the above is just an example).</p>
 *
 *     <blockquote><font size=2>
 *     <b>NOTE 1:</b> The semantic assumes that overviews are produced by subsampling, not by
 *     interpolation or pixel averaging. The later are not prohibed, but doing so introduce
 *     some subsampling-dependant variations in images produced by {@link MosaicImageReader},
 *     which would not be what we would expect from a strictly compliant {@link ImageReader}.
 *     <br><br>
 *     <b>NOTE 2:</b> Tile {@linkplain #getLocation location} and {@linkplain #getRegion region}
 *     coordinates should be specified in the overview pixel units - they should <em>not</em> be
 *     pre-multiplied by subsampling. This multiplication will be performed automatically by
 *     {@link TileManager} when comparing regions from tiles at different subsampling levels.
 *     </font></blockquote></p></li>
 * </ul>
 * <p>
 * The tiles are not required to be arranged on a regular grid, but performances may be
 * better if they are. {@link TileManagerFactory} is responsible for analysing the layout
 * of a collection of tiles and instantiate {@link TileManager} subclasses optimized for
 * the layout geometry.
 * <p>
 * {@code Tile}s can be considered as immutable after construction. However some properties
 * may be available only after this tile has been given to a {@link TileManagerFactory}.
 * <p>
 * {@code Tile}s are {@linkplain Serializable serializable} if their {@linkplain #getInput input}
 * given at construction time are serializable too. The {@link ImageReaderSpi} doesn't need to be
 * serializable, but its class must be known to {@link IIORegistry} at deserialization time.
 *
 * @since 2.5
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
public class Tile implements Comparable<Tile>, Serializable {
    /**
     * For cross-version compatibility during serialization.
     */
    private static final long serialVersionUID = -5417183834232374962L;

    /*
     * IMPLEMENTATION NOTE: Try to keep Tile as compact as possible memory-wise (i.e. put as few
     * non-static fields as possible).  Big mosaics may contain thousands of Tile instances, and
     * OutOfMemoryError tends to occur. The GridTileManager subclass can keep the number of Tile
     * instances low (generating them on the fly as needed), but sometime we have to fallback on
     * the more generic TreeTileManager, which stores a reference to every Tiles.
     */

    /**
     * Mask to apply on every unsigned short values (16 bits) in order to get the 32 bits
     * integer to work with. This is also the maximal value allowed.
     */
    static final int MASK = 0xFFFF;

    /**
     * The provider to use. The same provider is typically given to every {@code Tile} objects
     * to be given to the same {@link TileManager} instance, but this is not mandatory.
     * <p>
     * Consider this field as final. It is not because it needs to be set by {@link #readObject}.
     * If this field become public or protected in a future version, then we should make it final
     * and use reflection like {@link org.geotools.coverage.grid.GridCoverage2D#readObject}.
     */
    private transient ImageReaderSpi provider;

    /**
     * The input to be given to the image reader. If the reader can not read that input
     * directly, it will be wrapped in an {@linkplain ImageInputStream image input stream}.
     * Note that this field must stay the <em>unwrapped</em> input. If the wrapped input is
     * wanted, use {@link ImageReader#getInput} instead.
     */
    private final Object input;

    /**
     * The image index to be given to the image reader for reading this tile.
     * Stored as a unsigned short (i.e. must be used with {@code & MASK}).
     */
    private final short imageIndex;

    /**
     * The subsampling relative to the tile having the finest resolution. If this tile is the
     * one with finest resolution, then the value shall be 1. Should never be 0 or negative,
     * except if its value has not yet been computed.
     * <p>
     * Values are stored as unsigned shorts (i.e. must be used with {@code & MASK}).
     * <p>
     * This field should be considered as final. It is not final only because
     * {@link RegionCalculator} may computes its value automatically.
     */
    private short xSubsampling, ySubsampling;

    /**
     * The upper-left corner in the destination image. Should be considered as final, since
     * this class is supposed to be mostly immutable. However the value can be changed by
     * {@link #translate} before an instance is made public.
     */
    private int x, y;

    /**
     * The size of the image to be read, or 0 if not yet computed. Values are stored
     * as <strong>unsigned</strong> shorts:  they must be casted to {@code int} with
     * {@code s & MASK}. We assume that the {@code [0 .. 65535]} range is suffisient
     * on the basis that tiles need to be reasonably small for being useful. Furthermore
     * tiles are usually square and an image of size 32767&times;32767 reachs the limit
     * of Java Image I/O library anyway, since image area must hold in an {@code int}.
     */
    private short width, height;

    /**
     * The "grid to real world" transform, used by {@link RegionCalculator} in order to compute
     * the {@linkplain #getRegion region} for this tile. This field is set to {@code null} when
     * {@link RegionCalculator}'s work is in progress, and set to a new value on completion.
     * <p>
     * <b>Note:</b> {@link RegionCalculator} really needs a new instance for each tile.
     * No caching allowed before {@code RegionCalculator} processing. Caching allowed
     * <em>after</em> {@code RegionCalculator} processing.
     */
    private AffineTransform gridToCRS;

    /**
     * Creates a new tile which is a copy of the given one except for input and region.
     * The subsampling (and consequently the <cite>grid to CRS</cite> transform), image
     * index and image reader SPI are copied unchanged.
     * <p>
     * This method is not public because copying the field values may not be suffisient
     * if the given class is not of the same class than {@code this}. For example if it
     * is a {@link LargeTile}, then the width and height may be too small. This is okay
     * for {@link OverviewLevel} which use this constructor only with {@link Tile} instances.
     *
     * @param tile
     *          The tile to copy.
     * @param input
     *          The input to be given to the image reader, or {@code null} for the same input
     *          than the given tile.
     * @param region
     *          The region in the destination image, or {@code null} for the same region than
     *          the given tile. If non-null, then the {@linkplain Rectangle#width width} and
     *          {@linkplain Rectangle#height height} should match the image size.
     * @throws IllegalArgumentException
     *          If a required argument is {@code null} or some argument has an invalid value.
     */
    Tile(final Tile tile, final Object input, final Rectangle region)
            throws IllegalArgumentException
    {
        ensureNonNull("tile", tile);
        if (region != null) {
            if (region.isEmpty()) {
                throw new IllegalArgumentException(Errors.format(ErrorKeys.BAD_RECTANGLE_$1, region));
            }
            x = region.x;
            y = region.y;
            setSize(region.width, region.height);
        } else {
            x      = tile.x;
            y      = tile.y;
            width  = tile.width;
            height = tile.height;
        }
        this.input   = (input != null) ? input : tile.input;
        provider     = tile.provider;
        imageIndex   = tile.imageIndex;
        xSubsampling = tile.xSubsampling;
        ySubsampling = tile.ySubsampling;
        gridToCRS    = tile.gridToCRS;
    }

    /**
     * Creates a tile for the given provider, input and location. This constructor can be used when
     * the size of the image to be read by the supplied reader is unknown. This size will be
     * fetched automatically the first time {@link #getRegion} is invoked.
     *
     * @param provider
     *          The image reader provider to use. The same provider is typically given to every
     *          {@code Tile} objects to be given to the same {@link TileManager} instance, but
     *          this is not mandatory. If {@code null}, the provider will be inferred from the
     *          input. If it can't be inferred, then an exception is thrown.
     * @param input
     *          The input to be given to the image reader.
     * @param imageIndex
     *          The image index to be given to the image reader for reading this tile.
     * @param location
     *          The upper-left corner in the destination image.
     * @param subsampling
     *          The subsampling relative to the tile having the finest resolution, or {@code null}
     *          if none. If non-null, width and height should be strictly positive. This argument
     *          if of {@linkplain Dimension dimension} kind because it can also be understood as
     *          relative "pixel size".
     *
     * @throws IllegalArgumentException
     *          If a required argument is {@code null} or some argument has an invalid value.
     */
    public Tile(ImageReaderSpi provider, final Object input, final int imageIndex,
                final Point location, final Dimension subsampling)
                throws IllegalArgumentException
    {
        if (provider == null) {
            provider = getImageReaderSpi(input);
        }
        ensureNonNull("provider", provider);
        ensureNonNull("input",    input);
        ensureNonNull("location", location);
        this.provider   = provider;
        this.input      = input;
        this.imageIndex = ensurePositive(imageIndex);
        this.x          = location.x;
        this.y          = location.y;
        if (subsampling != null) {
            xSubsampling = ensureStrictlyPositive(subsampling.width);
            ySubsampling = ensureStrictlyPositive(subsampling.height);
        } else {
            xSubsampling = ySubsampling = 1;
        }
    }

    /**
     * Creates a tile for the given provider, input and region. This constructor can be used when
     * the size of the image to be read by the supplied reader is known. It avoid the cost of
     * fetching the size from the reader when {@link #getRegion} will be invoked.
     *
     * @param provider
     *          The image reader provider to use. The same provider is typically given to every
     *          {@code Tile} objects to be given to the same {@link TileManager} instance, but
     *          this is not mandatory. If {@code null}, the provider will be inferred from the
     *          input. If it can't be inferred, then an exception is thrown.
     * @param input
     *          The input to be given to the image reader.
     * @param imageIndex
     *          The image index to be given to the image reader for reading this tile.
     * @param region
     *          The region in the destination image. The {@linkplain Rectangle#width width} and
     *          {@linkplain Rectangle#height height} should match the image size.
     * @param subsampling
     *          The subsampling relative to the tile having the finest resolution, or {@code null}
     *          if none. If non-null, width and height should be strictly positive. This argument
     *          if of {@linkplain Dimension dimension} kind because it can also be understood as
     *          relative "pixel size".
     *
     * @throws IllegalArgumentException
     *          If a required argument is {@code null} or some argument has an invalid value.
     */
    public Tile(ImageReaderSpi provider, final Object input, final int imageIndex,
                final Rectangle region, final Dimension subsampling)
                throws IllegalArgumentException
    {
        if (provider == null) {
            provider = getImageReaderSpi(input);
        }
        ensureNonNull("provider", provider);
        ensureNonNull("input",    input);
        ensureNonNull("region",   region);
        if (region.isEmpty()) {
            throw new IllegalArgumentException(Errors.format(ErrorKeys.BAD_RECTANGLE_$1, region));
        }
        this.provider   = provider;
        this.input      = input;
        this.imageIndex = ensurePositive(imageIndex);
        this.x          = region.x;
        this.y          = region.y;
        setSize(region.width, region.height);
        if (subsampling != null) {
            xSubsampling = ensureStrictlyPositive(subsampling.width);
            ySubsampling = ensureStrictlyPositive(subsampling.height);
        } else {
            xSubsampling = ySubsampling = 1;
        }
    }

    /**
     * Creates a tile for the given provider, input and "<cite>grid to real world</cite>" transform.
     * This constructor can be used when the {@linkplain #getLocation location} of the image to be
     * read by the supplied reader is unknown. The definitive location and the subsampling will be
     * computed automatically when this tile will be given to a {@link TileManagerFactory}.
     * <p>
     * When using this constructor, the {@link #getLocation}, {@link #getRegion} and
     * {@link #getSubsampling} methods will throw an {@link IllegalStateException} until this tile
     * has been given to a {@link TileManager}, which will compute those values automatically.
     *
     * @param provider
     *          The image reader provider to use. The same provider is typically given to every
     *          {@code Tile} objects to be given to the same {@link TileManager} instance, but
     *          this is not mandatory. If {@code null}, the provider will be inferred from the
     *          input. If it can't be inferred, then an exception is thrown.
     * @param input
     *          The input to be given to the image reader.
     * @param imageIndex
     *          The image index to be given to the image reader for reading this tile.
     * @param region
     *          The tile region, or {@code null} if unknown. The (<var>x</var>,<var>y</var>)
     *          location of this region is typically (0,0). The definitive location will be
     *          computed when this tile will be given to a {@link TileManagerFactory}.
     * @param gridToCRS
     *          The "<cite>grid to real world</cite>" transform.
     *
     * @throws IllegalArgumentException
     *          If a required argument is {@code null} or some argument has an invalid value.
     */
    public Tile(ImageReaderSpi provider, final Object input, final int imageIndex,
                final Rectangle region, final AffineTransform gridToCRS)
                throws IllegalArgumentException
    {
        if (provider == null) {
            provider = getImageReaderSpi(input);
        }
        ensureNonNull("provider",  provider);
        ensureNonNull("input",     input);
        ensureNonNull("gridToCRS", gridToCRS);
        this.provider   = provider;
        this.input      = input;
        this.imageIndex = ensurePositive(imageIndex);
        if (region != null) {
            this.x = region.x;
            this.y = region.y;
            if (!region.isEmpty()) {
                setSize(region.width, region.height);
            }
        }
        this.gridToCRS = new AffineTransform(gridToCRS); // Really needs a new instance - no cache
    }

    /**
     * Creates a tile for the given region with default subsampling. This constructor is
     * provided for avoiding compile-tile ambiguity between null <cite>subsampling</cite>
     * and null <cite>affine transform</cite> (the former is legal, the later is not).
     *
     * @param provider
     *          The image reader provider to use. The same provider is typically given to every
     *          {@code Tile} objects to be given to the same {@link TileManager} instance, but
     *          this is not mandatory. If {@code null}, the provider will be inferred from the
     *          input. If it can't be inferred, then an exception is thrown.
     * @param input
     *          The input to be given to the image reader.
     * @param imageIndex
     *          The image index to be given to the image reader for reading this tile.
     * @param region
     *          The region in the destination image. The {@linkplain Rectangle#width width} and
     *          {@linkplain Rectangle#height height} should match the image size.
     *
     * @throws IllegalArgumentException
     *          If a required argument is {@code null} or some argument has an invalid value.
     */
    public Tile(final ImageReaderSpi provider, final Object input, final int imageIndex, final Rectangle region)
                throws IllegalArgumentException
    {
        this(provider, input, imageIndex, region, (Dimension) null);
    }

    /**
     * Ensures that the given argument is non-null.
     */
    static void ensureNonNull(final String argument, final Object value) {
        if (value == null) {
            throw new IllegalArgumentException(Errors.format(ErrorKeys.NULL_ARGUMENT_$1, argument));
        }
    }

    /**
     * Ensures that the given value is positive and in the range of 16 bits number.
     * Returns the value casted to an unsigned {@code short} type.
     */
    private static short ensurePositive(final int n) throws IllegalArgumentException {
        if (n < 0 || n > MASK) {
            throw new IllegalArgumentException(Errors.format(
                    ErrorKeys.VALUE_OUT_OF_BOUNDS_$3, n, 0, MASK));
        }
        return (short) n;
    }

    /**
     * Ensures that the subsampling is strictly positive. This method is invoked for checking
     * user-supplied arguments, as opposed to {@link #checkGeometryValidity} which checks if
     * the subsampling has been computed. Both methods differ in exception type for that reason.
     */
    static short ensureStrictlyPositive(final int n) throws IllegalArgumentException {
        if (n < 1) {
            throw new IllegalArgumentException(Errors.format(ErrorKeys.NOT_GREATER_THAN_ZERO_$1, n));
        }
        return ensurePositive(n);
    }

    /**
     * Checks if the location, region, and subsampling can be returned. Throw an exception if this
     * tile has been {@linkplain #Tile(ImageReaderSpi, Object, int, Dimension, AffineTransform)
     * created without location} and not yet processed by {@link TileManagerFactory}.
     * <p>
     * <b>Note:</b> It is not strictly necessary to synchronize this method since update to a
     * {@code int} field is atomic according Java language specification, the {@link #xSubsampling} and
     * {@link #ySubsampling} fields do not change anymore as soon as they have a non-zero value (this is
     * checked by setSubsampling(Dimension) implementation) and this method succed only if both
     * fields are set. Most callers are already synchronized anyway, except {@link TileManager}
     * constructor which invoke this method only has a sanity check. It is okay to conservatively
     * get the exception in situations where a synchronized block would not have thrown it.
     *
     * @todo Localize the exception message.
     */
    final void checkGeometryValidity() throws IllegalStateException {
        if (xSubsampling == 0 || ySubsampling == 0) {
            throw new IllegalStateException("Tile must be processed by TileManagerFactory.");
        }
    }

    /**
     * Closes the specified stream, if it is closeable.
     *
     * @param  input The stream to close.
     * @throws IOException if an error occured while closing the input stream.
     */
    static void close(final Object input) throws IOException {
        if (input instanceof ImageInputStream) {
            ((ImageInputStream) input).close();
        } else if (input instanceof Closeable) {
            ((Closeable) input).close();
        }
    }

    /**
     * Dispose the given reader after closing its {@linkplain ImageReader#getInput input stream}.
     *
     * @param  reader The reader to dispose.
     * @throws IOException if an error occured while closing the input stream.
     */
    static void dispose(final ImageReader reader) throws IOException {
        final Object input = reader.getInput();
        reader.dispose();
        close(input);
    }

    /**
     * Returns {@code true} if the specified input is valid for the given array of input types.
     */
    private static boolean isValidInput(final Class<?>[] types, final Object input) {
        if (types != null) {
            for (final Class<?> type : types) {
                if (type!=null && type.isInstance(input)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns a reader created by the {@linkplain #getImageReaderSpi provider} and setup for
     * reading the image from the {@linkplain #getInput input}. If a reader is already setup with
     * the right input, then it is returned immediately. Otherwise if the image reader can accept
     * the {@linkplain #getInput input} directly, than that input is given to the image reader.
     * Otherwise the input is wrapped in an {@linkplain ImageInputStream image input stream}.
     * <p>
     * This method is invoked automatically by {@link MosaicImageReader} and should not needs
     * to be invoked directly. If an {@linkplain ImageInputStream image input stream} has been
     * created, it will be closed automatically when needed.
     * <p>
     * Note that this method will typically returns an instance to be shared by every tiles in
     * the given {@link MosaicImageReader}. Callers should not {@linkplain ImageReader#dispose
     * dispose} the reader or change its configuration, unless the {@code mosaic} argument was
     * null.
     *
     * @param mosaic          The caller, or {@code null} if none.
     * @param seekForwardOnly If {@code true}, images and metadata may only be read
     *                        in ascending order from the input source.
     * @param ignoreMetadata  If {@code true}, metadata may be ignored during reads.
     * @return An image reader with its {@linkplain ImageReader#getInput input} set.
     * @throws IOException if the image reader can't be initialized.
     */
    protected ImageReader getImageReader(final MosaicImageReader mosaic,
                                         final boolean seekForwardOnly,
                                         final boolean ignoreMetadata)
            throws IOException
    {
        final ImageReaderSpi provider = getImageReaderSpi();
        final ImageReader reader;
        final Object currentInput;
        if (mosaic != null) {
            reader = mosaic.getTileReader(provider);
            currentInput = mosaic.getRawInput(reader);
        } else {
            reader = provider.createReaderInstance();
            currentInput = null;
        }
        /*
         * If the current reader input is suitable, we will keep it in order to preserve
         * any data that may be cached in the ImageReader instance. Only if the input is
         * not suitable, we will invoke ImageReader.setInput(...).
         */
        final Object input = getInput();
        final boolean sameInput = Utilities.equals(input, currentInput);
        if ( !sameInput                                      ||
            ( getImageIndex() <  reader.getMinIndex())       ||
            (!seekForwardOnly && reader.isSeekForwardOnly()) ||
            (!ignoreMetadata  && reader.isIgnoringMetadata()))
        {
            Object actualInput = reader.getInput();
            reader.setInput(null); // Necessary for releasing the stream, in case it holds it.
            if (mosaic != null) {
                mosaic.setRawInput(reader, null); // For keeping the map consistent.
            }
            ImageInputStream stream = null;
            if (actualInput instanceof ImageInputStream) {
                stream = (ImageInputStream) actualInput;
            }
            final ImageReaderSpi spi = reader.getOriginatingProvider();
            if (spi == null || isValidInput(spi.getInputTypes(), input)) {
                // We are allowed to use the input directly. Closes the stream
                // as a paranoiac safety (it should not be opened anyway).
                if (stream != null) {
                    stream.close();
                    stream = null;
                }
                actualInput = input;
            } else {
                // We are not allowed to use the input directly. Creates a new input
                // stream, or reuse the previous one if it still useable.
                if (stream != null) {
                    if (sameInput) try {
                        stream.seek(0);
                    } catch (IndexOutOfBoundsException e) {
                        // We tried to reuse the same stream in order to preserve cached data, but it was
                        // not possible to seek to the begining. Closes it; we will open a new one later.
                        Logging.recoverableException(Tile.class, "getImageReader", e);
                        stream.close();
                        stream = null;
                    } else {
                        stream.close();
                        stream = null;
                    }
                }
                if (stream == null) {
                    stream = getInputStream();
                }
                actualInput = stream;
            }
            reader.setInput(actualInput, seekForwardOnly, ignoreMetadata);
            if (mosaic != null) {
                mosaic.setRawInput(reader, input);
            }
        }
        return reader;
    }

    /**
     * Returns a new reader created by the {@linkplain #getImageReaderSpi provider} and setup for
     * reading the image from the {@linkplain #getInput input}. This method returns a new reader
     * on each invocation.
     * <p>
     * It is the user's responsability to close the {@linkplain ImageReader#getInput reader input}
     * after usage.
     *
     * @return An image reader with its {@linkplain ImageReader#getInput input} set.
     * @throws IOException if the image reader can't be initialized.
     */
    public ImageReader getImageReader() throws IOException {
        return getImageReader(null, true, true);
    }

    /**
     * Returns the image reader provider (never {@code null}). This is the provider used for
     * creating the {@linkplain ImageReader image reader} to be used for reading this tile.
     *
     * @return The image reader provider.
     *
     * @see ImageReaderSpi#createReaderInstance()
     */
    public ImageReaderSpi getImageReaderSpi() {
        return provider;
    }

    /**
     * Returns an image reader provider inferred from the given input,
     * or {@code null} if none can be found without ambiguity.
     */
    static ImageReaderSpi getImageReaderSpi(final Object input) {
        ImageReaderSpi provider = null;
        final String path = getInputName(input);
        if (path != null) {
            final int split = path.indexOf('.', path.lastIndexOf('/') + 1);
            if (split >= 0) {
                final String suffix = path.substring(split + 1).trim();
                String[] suffixes = null;
                final Iterator<ImageReaderSpi> it = IIORegistry.getDefaultInstance()
                        .getServiceProviders(ImageReaderSpi.class, true);
                while (it.hasNext()) {
                    final ImageReaderSpi candidate = it.next();
                    final String[] candidateSuffixes = candidate.getFileSuffixes();
                    if (XArray.containsIgnoreCase(candidateSuffixes, suffix)) {
                        if (provider != null) {
                            if (Arrays.equals(candidateSuffixes, suffixes)) {
                                // E.g. we may have both CLIB and JSE version of PNG reader.
                                continue;
                            }
                            // We have an ambiguity - Returns null so we don't make a choice.
                            return null;
                        }
                        provider = candidate;
                        suffixes = candidateSuffixes;
                        // Continue the search for making sure that we don't have an ambiguity.
                    }
                }
            }
        }
        return provider;
    }

    /**
     * Creates an image input stream from the input. If no suitable input stream can be created,
     * then this method throws an exception. This method never returns {@code null}.
     *
     * @return The image input stream.
     * @throws IOException if an error occured while creating the input stream.
     */
    private ImageInputStream getInputStream() throws IOException {
        final Object input = getInput();
        ImageInputStream stream = ImageIO.createImageInputStream(input);
        if (stream != null) {
            return stream;
        }
        /*
         * We tried the input directly in case the user provided some SPI for String
         * objects. If we have not been able to create a stream from a plain string,
         * create a URL or a File object from the string and try again.
         */
        if (input instanceof CharSequence) {
            final String path = input.toString();
            final Object url;
            if (path.indexOf("://") > 0) {
                url = new URL(path);
            } else {
                url = new File(path);
            }
            stream = ImageIO.createImageInputStream(url);
            if (stream != null) {
                return stream;
            }
        }
        /*
         * In theory ImageIO.createImageInputStream(Object) should have accepted a File input,
         * so the following check is useless. However if ImageIO.createImageInputStream(Object)
         * failed, it just returns null; we have no idea why it failed. One possible cause is
         * "Too many open files", in which case throwing a FileNotFoundException is misleading.
         * So we try here to create a FileImageInputStream directly, which is likely to fail as
         * well but this time with a more accurate error message.
         */
        if (input instanceof File) {
            stream = new FileImageInputStream((File) input);
            return stream;
        }
        throw new FileNotFoundException(Errors.format(
                ErrorKeys.FILE_DOES_NOT_EXIST_$1, input));
    }

    /**
     * Returns the input to be given to the image reader for reading this tile.
     *
     * @return The image input.
     *
     * @see ImageReader#setInput
     */
    public Object getInput() {
        return input;
    }

    /**
     * Returns a short string representation of the {@linkplain #getInput input}. The
     * default implementation returns the following:
     * <p>
     * <ul>
     *   <li>For {@linkplain CharSequence Character sequence}, returns the
     *       {@linkplain CharSequence#toString string} form.</li>
     *   <li>For {@linkplain File}, returns only the {@linkplain File#getName name} part.</li>
     *   <li>For {@linkplain URL} or {@linkplain URI}, returns the path without the protocol or
     *       query parts.</li>
     *   <li>For other classes, returns {@code "class"} followed by the unqualified class name.</li>
     * </ul>
     *
     * @return A short string representation of the input (never {@code null}).
     */
    public String getInputName() {
        String name = getInputName(getInput());
        if (name == null) {
            name = "class " + Classes.getShortClassName(input);
        }
        return name;
    }

    /**
     * Returns a short string representation of the given input,
     * or {@code null} if the input can not be formatted.
     */
    private static String getInputName(final Object input) {
        if (input instanceof File) {
            return ((File) input).getName();
        }
        if (input instanceof URI) {
            return ((URI) input).getPath();
        }
        if (input instanceof URL) {
            return ((URL) input).getPath();
        }
        if (input instanceof CharSequence) {
            return input.toString();
        }
        return null;
    }

    /**
     * Returns a format name inferred from the {@linkplain #getImageReaderSpi provider}.
     *
     * @return The format name.
     */
    public String getFormatName() {
        return toString(getImageReaderSpi());
    }

    /**
     * Returns the image index to be given to the image reader for reading this tile.
     *
     * @return The image index, numbered from 0.
     *
     * @see ImageReader#read(int)
     */
    public int getImageIndex() {
        return imageIndex & MASK;
    }

    /**
     * If the user-supplied transform is waiting for a processing by {@link RegionCalculator},
     * returns it. Otherwise returns {@code null}. This method is for internal usage by
     * {@link RegionCalculator} only.
     * <p>
     * See {@link #checkGeometryValidity} for a note about synchronization. When {@code clear}
     * is {@code false} (i.e. this method is invoked just in order to get a hint), it is okay
     * to conservatively return a non-null value in situations where a synchronized block would
     * have returned {@code null}.
     *
     * @param clear If {@code true}, clears the {@link #gridToCRS} field before to return. This
     *              is a way to tell that processing is in progress, and also a safety against
     *              transform usage while it may become invalid.
     * @return The transform, or {@code null} if none. This method does not clone the returned
     *         value - {@link RegionCalculator} will reference and modify directly that transform.
     */
    final AffineTransform getPendingGridToCRS(final boolean clear) {
        assert !clear || Thread.holdsLock(this); // Lock required only if 'clear' is true.
        if (xSubsampling != 0 || ySubsampling != 0) {
            // No transform waiting to be processed.
            return null;
        }
        final AffineTransform gridToCRS = this.gridToCRS;
        if (clear) {
            this.gridToCRS = null;
        }
        return gridToCRS;
    }

    /**
     * Returns the "<cite>grid to real world</cite>" transform, or {@code null} if unknown.
     * This transform is derived from the value given to the constructor, but may not be
     * identical since it may have been {@linkplain AffineTransform#translate translated}
     * in order to get a uniform grid geometry for every tiles in a {@link TileManager}.
     *
     * @return The "grid to real world" transform, or {@code null} if undefined.
     * @throws IllegalStateException If this tile has been {@linkplain #Tile(ImageReaderSpi,
     *         Object, int, Dimension, AffineTransform) created without location} and not yet
     *         processed by {@link TileManagerFactory}.
     *
     * @see TileManager#getGridGeometry
     */
    public synchronized AffineTransform getGridToCRS() throws IllegalStateException {
        checkGeometryValidity();
        return gridToCRS; // No need to clone since TileManagerFactory assigned an immutable instance.
    }

    /**
     * Sets the new "<cite>grid to real world</cite>" transform to use after the translation
     * performed by {@link #translate}, if any. Should be an immutable instance because it will
     * not be cloned.
     *
     * @throws IllegalStateException if an other transform was already assigned to this tile.
     */
    final synchronized void setGridToCRS(final AffineTransform at) throws IllegalStateException {
        if (gridToCRS != null) {
            if (!gridToCRS.equals(at)) {
                throw new IllegalStateException();
            }
        } else {
            gridToCRS = at;
        }
    }

    /**
     * Returns the subsampling relative to the tile having the finest resolution. This method never
     * returns {@code null}, and the width & height shall never be smaller than 1. The return type
     * is of {@linkplain Dimension dimension} kind because the value can also be interpreted as
     * relative "pixel size".
     *
     * @return The subsampling along <var>x</var> and <var>y</var> axis.
     * @throws IllegalStateException If this tile has been {@linkplain #Tile(ImageReaderSpi,
     *         Object, int, Dimension, AffineTransform) created without location} and not yet
     *         processed by {@link TileManagerFactory}.
     *
     * @see javax.imageio.ImageReadParam#setSourceSubsampling
     */
    public synchronized Dimension getSubsampling() throws IllegalStateException {
        checkGeometryValidity();
        return new Dimension(xSubsampling & MASK, ySubsampling & MASK);
    }

    /**
     * Invoked by {@link RegionCalculator} only. No other caller allowed.
     */
    final void setSubsampling(final Dimension subsampling) throws IllegalStateException {
        assert Thread.holdsLock(this);
        if (xSubsampling != 0 || ySubsampling != 0) {
            throw new IllegalStateException(); // Should never happen.
        }
        xSubsampling = ensureStrictlyPositive(subsampling.width);
        ySubsampling = ensureStrictlyPositive(subsampling.height);
    }

    /**
     * Returns the highest subsampling that this tile can handle, not greater than the given
     * subsampling. Special cases:
     * <p>
     * <ul>
     *   <li>If the given subsampling is {@code null}, then this method returns {@code null}.</li>
     *   <li>Otherwise if the given subsampling is {@code (0,0)}, then this method returns the
     *       same {@code subsampling} reference unchanged. Callers can test using the identity
     *       ({@code ==}) operator.</li>
     *   <li>Otherwise if this tile can handle exactly the given subsampling, then this method
     *       returns the same {@code subsampling} reference unchanged. Callers can test using
     *       the identity ({@code ==}) operator.</li>
     *   <li>Otherwise if there is no subsampling that this tile could handle,
     *       then this method returns {@code null}.</li>
     *   <li>Otherwise this method returns a new {@link Dimension} set to the greatest subsampling
     *       that this tile can handle, not greater than the given subsampling.</li>
     * </ul>
     *
     * @param  subsampling The subsampling along <var>x</var> and <var>y</var> axis.
     * @return A subsampling equals or finer than the given one.
     * @throws IllegalStateException If this tile has been {@linkplain #Tile(ImageReaderSpi,
     *         Object, int, Dimension, AffineTransform) created without location} and not yet
     *         processed by {@link TileManagerFactory}.
     */
    public Dimension getSubsamplingFloor(final Dimension subsampling) throws IllegalStateException {
        if (subsampling != null) {
            final int dx, dy;
            try {
                dx = subsampling.width  % (xSubsampling & MASK);
                dy = subsampling.height % (ySubsampling & MASK);
            } catch (ArithmeticException e) {
                throw new IllegalStateException("Tile must be processed by TileManagerFactory.", e);
            }
            if (dx != 0 || dy != 0) {
                final int sourceXSubsampling = subsampling.width  - dx;
                final int sourceYSubsampling = subsampling.height - dy;
                if (sourceXSubsampling != 0 && sourceYSubsampling != 0) {
                    return new Dimension(sourceXSubsampling, sourceYSubsampling);
                } else {
                    return null;
                }
            }
        }
        return subsampling;
    }

    /**
     * Returns {@code true} if this tile subsampling is finer than the specified value
     * for at least one dimension. For internal usage by {@link RTree#searchTiles} only.
     */
    final boolean isFinerThan(final Dimension subsampling) {
        return (xSubsampling & MASK) < subsampling.width ||
               (ySubsampling & MASK) < subsampling.height;
    }

    /**
     * Returns {@code true} if the subsampling of this tile is equals to the subsampling of
     * the given tile, but this tile cover a greater area than the given tile.
     *
     * @param other The other tile to compare with.
     * @return {@code true} if both tiles have the same subsampling and this tile is larger.
     */
    final boolean isLargerThan(final Tile other) {
        return xSubsampling == other.xSubsampling && ySubsampling == other.ySubsampling &&
                (width & MASK) * (height & MASK) > (other.width & MASK) * (other.height & MASK);
    }

    /**
     * Returns the upper-left corner in the
     * {@linkplain javax.imageio.ImageReadParam#setDestination destination image}. This is the
     * location when no {@linkplain javax.imageio.ImageReadParam#setDestinationOffset destination
     * offset} are specified. If the user specified a destination offset, then the tile location
     * will be translated accordingly for the image being read.
     *
     * @return The tile upper-left corner.
     * @throws IllegalStateException If this tile has been {@linkplain #Tile(ImageReaderSpi,
     *         Object, int, Dimension, AffineTransform) created without location} and not yet
     *         processed by {@link TileManagerFactory}.
     *
     * @see javax.imageio.ImageReadParam#setDestinationOffset
     */
    public synchronized Point getLocation() throws IllegalStateException {
        checkGeometryValidity();
        return new Point(x,y);
    }

    /**
     * Returns {@code true} if the tile size is equals to the given dimension.
     * This method should be invoked when we know that this instance is not a
     * subclass of {@link Tile}, otherwise we should use {@link #getRegion} in
     * case the user overriden the method.
     */
    final boolean isSizeEquals(final int dx, final int dy) {
        assert getClass().equals(Tile.class) && (width != 0) && (height != 0) : this;
        return (width & MASK) == dx && (height & MASK) == dy;
    }

    /**
     * Returns the upper-left corner in the destination image, with the image size. If this tile
     * has been created with the {@linkplain #Tile(ImageReader,Object,int,Rectangle,Dimension)
     * constructor expecting a rectangle}, a copy of the specified rectangle is returned.
     * Otherwise the image {@linkplain ImageReader#getWidth width} and
     * {@linkplain ImageReader#getHeight height} are read from the image reader and cached for
     * future usage.
     *
     * @return The region in the destination image.
     * @throws IllegalStateException If this tile has been {@linkplain #Tile(ImageReaderSpi,
     *         Object, int, Dimension, AffineTransform) created without location} and not yet
     *         processed by {@link TileManagerFactory}.
     * @throws IOException if it was necessary to fetch the image dimension from the
     *         {@linkplain #getImageReader reader} and this operation failed.
     *
     * @see javax.imageio.ImageReadParam#setSourceRegion
     */
    public synchronized Rectangle getRegion() throws IllegalStateException, IOException {
        checkGeometryValidity();
        if (width == 0 && height == 0) {
            final int imageIndex = getImageIndex();
            final ImageReader reader = getImageReader();
            setSize(reader.getWidth(imageIndex), reader.getHeight(imageIndex));
            dispose(reader);
        }
        return new Rectangle(x, y, width & MASK, height & MASK);
    }

    /**
     * Returns the {@linkplain #getRegion region} multiplied by the subsampling.
     * This is this tile coordinates in the units of the tile having the finest
     * resolution, as opposed to the default public methods which are always in
     * units relative to this tile.
     */
    final Rectangle getAbsoluteRegion() throws IOException {
        final Rectangle region = getRegion();
        final int sx = xSubsampling & MASK;
        final int sy = ySubsampling & MASK;
        region.x      *= sx;
        region.y      *= sy;
        region.width  *= sx;
        region.height *= sy;
        return region;
    }

    /**
     * Invoked by {@link RegionCalculator} only. No other caller allowed.
     * {@link #setSubsampling} must be invoked prior this method.
     * <p>
     * Note that invoking this method usually invalidate {@link #gridToCRS}. Calls to this method
     * should be closely followed by calls to {@link #translate} for fixing the "gridToCRS" value.
     *
     * @param region The region to assign to this tile.
     * @throws ArithmeticException if {@link #setSubsampling} has not be invoked.
     */
    final void setAbsoluteRegion(final Rectangle region) throws ArithmeticException {
        assert Thread.holdsLock(this);
        final int sx = xSubsampling & MASK;
        final int sy = ySubsampling & MASK;
        assert (region.width % sx) == 0 && (region.height % sy) == 0 : region;
        x = region.x / sx;
        y = region.y / sy;
        setSize(region.width / sx, region.height / sy);
    }

    /**
     * Sets the tile size to the given values, making sure that they can be stored as unsigned
     * short. This method is overriden by {@link LargeTile} but should never been invoked by
     * anyone else than {@link Tile}.
     *
     * @param dx The tile width.
     * @param dy The tile height.
     * @throws IllegalArgumentException if the given size can't be stored as unsigned short.
     */
    void setSize(final int dx, final int dy) throws IllegalArgumentException {
        width  = (short) dx;
        height = (short) dy;
        final String name;
        final int value;
        if ((width & MASK) != dx) {
            name = "width";
            value = dx;
        } else if ((height & MASK) != dy) {
            name = "height";
            value = dy;
        } else {
            return;
        }
        width  = 0;
        height = 0;
        throw new IllegalArgumentException(Errors.format(
                ErrorKeys.VALUE_OUT_OF_BOUNDS_$3, name + '=' + value, 0, MASK));
    }

    /**
     * Converts to given rectangle from absolute to relative coordinates.
     * Coordinates are rounded to the smallest box enclosing fully the given region.
     *
     * @param region The rectangle to converts. Values are replaced in-place.
     * @throws ArithmeticException if {@link #setSubsampling} has not be invoked.
     */
    final void absoluteToRelative(final Rectangle region) throws ArithmeticException {
        final int sx = xSubsampling & MASK;
        final int sy = ySubsampling & MASK;
        int xmin = region.x;
        int xmax = region.width  + xmin;
        int ymin = region.y;
        int ymax = region.height + ymin;
        if (xmin < 0) xmin -= (sx - 1);
        if (xmax > 0) xmax += (sx - 1);
        if (ymin < 0) ymin -= (sy - 1);
        if (ymax > 0) ymax += (sy - 1);
        xmin /= sx;
        xmax /= sx;
        ymin /= sy;
        ymax /= sy;
        region.x = xmin;
        region.y = ymin;
        region.width  = xmax - xmin;
        region.height = ymax - ymin;
    }

    /**
     * Translates this tile. For internal usage by {@link RegionCalculator} only.
     * This method is invoked slightly after {@link #setRegion} for final adjustment.
     * <p>
     * Reminder: {@link #setGridToCRS(AffineTransform)} should be invoked after this method.
     *
     * @param xSubsampling The translation to apply on <var>x</var> values (often 0).
     * @param ySubsampling The translation to apply on <var>y</var> values (often 0).
     */
    final synchronized void translate(final int dx, final int dy) {
        x += dx;
        y += dy;
        gridToCRS = null;
    }

    /**
     * Returns the amount of pixels in this tile that would be useless if reading the given region
     * at the given subsampling. This method is invoked by {@link TileManager} when two or more
     * tile overlaps, in order to choose the tiles that would minimize the amount of pixels to
     * read. The default implementation computes the sum of:
     * <ul>
     *   <li>the amount of tile pixels skipped because of the given subsampling</li>
     *   <li>the amount of pixels in this {@linkplain #getRegion tile region} that are outside
     *       the given region, including the pixels below the bottom.</li>
     * </ul>
     * The later is conservative since many file formats will stop reading as soon as they reach
     * the region bottom. We may consider allowing overriding in order to alter this calculation
     * if a subclass is sure that pixels below the region have no disk seed cost.
     *
     * @param  toRead The region to read, in the same units than {@link #getAbsoluteRegion}.
     * @param  subsampling The number of columns and rows to advance between pixels
     *         in the given region. Must be strictly positive (not zero).
     * @return The amount of pixels which would be unused if the reading was performed on this
     *         tile. Smaller number is better.
     * @throws IOException if it was necessary to fetch the image dimension from the
     *         {@linkplain #getImageReader reader} and this operation failed.
     */
    final int countUnwantedPixelsFromAbsolute(final Rectangle toRead, final Dimension subsampling)
            throws IOException
    {
        final int sx = xSubsampling & MASK;
        final int sy = ySubsampling & MASK;
        assert subsampling.width >= sx && subsampling.height >= sy : subsampling;
        final Rectangle region = getRegion();
        /*
         * Converts the tile region to absolute coordinates and clips it to the region to read.
         */
        final long xmin, ymin, xmax, ymax;
        xmin = max((long) toRead.x,                 sx * ((long) region.x));
        ymin = max((long) toRead.y,                 sy * ((long) region.y));
        xmax = min((long) toRead.x + toRead.width,  sx * ((long) region.x + region.width));
        ymax = min((long) toRead.y + toRead.height, sy * ((long) region.y + region.height));
        /*
         * Computes the amount of pixels to keep for the given region and subsampling.
         */
        long count = max(xmax - xmin, 0) * max(ymax - ymin, 0);
        count /= (subsampling.width * subsampling.height);
        /*
         * Computes the amount of pixels from the current tile that would be unused. Note that
         * we are substracting a quantity derived from the absolute space from a quantity in the
         * relative space. The result should be positive anyway because we divided the former by
         * (s.width * s.height), which should be greater than (xSubsampling * ySubsampling).
         */
        count = (region.width * region.height) - count;
        assert count >= 0 && count <= Integer.MAX_VALUE : count;
        return (int) count;
    }

    /**
     * Converts {@link URL} to {@link URI} and {@link CharSequence} to {@link String} for
     * comparaison purpose. {@link File}, {@link URI} and {@link String} are not converted
     * because they are already {@linkplain Comparable comparable}.
     */
    private static Object toComparable(Object input) {
        if (input instanceof URL) try {
            input = ((URL) input).toURI();
        } catch (URISyntaxException exception) {
            // Ignores - we will keep it as a URL. Logs with "compare" as source method
            // name, since it is the public API that invoked this private method.
            Logging.recoverableException(Tile.class, "compare", exception);
        } else if (input instanceof CharSequence) {
            input = input.toString();
        }
        return input;
    }

    /**
     * Tries to converts the given input into something that can be compared to the given base.
     * Returns the input unchanged if this method doesn't know how to convert it.
     */
    private static Object toCompatible(Object input, final Object target) {
        if (target instanceof URI) {
            if (input instanceof File) {
                input = ((File) input).toURI();
            }
        } else if (target instanceof String) {
            if (input instanceof File || input instanceof URI) {
                input = input.toString();
            }
        }
        return input;
    }

    /**
     * Compares two inputs for order. {@link String}, {@link File} and {@link URI} are comparable.
     * {@link URL} are not but can be converted to {@link URI} for comparaison purpose.
     */
    @SuppressWarnings("unchecked")
    private static int compareInputs(Object input1, Object input2) {
        if (Utilities.equals(input1, input2)) {
            return 0;
        }
        input1 = toComparable(input1);
        input2 = toComparable(input2); // Must be before 'toCompatible'.
        input1 = toCompatible(input1, input2);
        input2 = toCompatible(input2, input1);
        if (input1 instanceof Comparable && input1.getClass().isInstance(input2)) {
            return ((Comparable) input1).compareTo(input2);
        }
        if (input2 instanceof Comparable && input2.getClass().isInstance(input1)) {
            return -((Comparable) input2).compareTo(input1);
        }
        int c = input1.getClass().getName().compareTo(input2.getClass().getName());
        if (c != 0) {
            return c;
        }
        /*
         * Following is an unconvenient comparaison criterion, but this fallback should never
         * occurs in typical use cases. We use it on a "better than nothing" basis. It should
         * be consistent in a given running JVM, but it not likely to be consistent when comparing
         * the same tiles in two different JVM executions. In addition there is also a slight risk
         * that this code returns 0 while we would like to return a non-zero value.
         */
        return System.identityHashCode(input2) - System.identityHashCode(input1);
    }

    /**
     * Compares two tiles for optimal order in sequential reads. Default implementation sorts by
     * {@linkplain #getInput input} first, then increasing {@linkplain #getImageIndex image index}.
     * This ordering allows efficient access for tiles that use the same
     * {@linkplain #getImageReader image reader}.
     * <p>
     * For tiles having the same input and index, additional criterions are used like increasing
     * subsampling, increasing <var>y</var> then increasing <var>x</var> coordinates. But the
     * actual set of additional criterions may change.
     * <p>
     * This method is consistent with {@link #equals} in the most common case where every
     * tiles to be compared (typically every tiles given to a {@link TileManager} instance)
     * have inputs of the same kind (preferrably {@link File}, {@link URL}, {@link URI} or
     * {@link String}), and there is no duplicated ({@linkplain #getInput input},
     * {@linkplain #getImageIndex image index}) pair.
     *
     * @param  other The tile to compare with.
     * @return -1 if this tile should be read before {@code other}, +1 if it should be read
     *         after or 0 if equals.
     */
    public final int compareTo(final Tile other) {
        int c = compareInputs(input, other.input);
        if (c == 0) {
            c = (imageIndex & MASK) - (other.imageIndex & MASK);
            if (c == 0) {
                /*
                 * From this point it doesn't matter much for disk access. But we continue to
                 * define criterions for consistency with 'equals(Object)' method. We compare
                 * subsampling first because it may be undefined while it is needed for (x,y)
                 * ordering. Undefined subsampling will be ordered first (this is arbitrary).
                 */
                final int sy =  this.ySubsampling & MASK;
                final int oy = other.ySubsampling & MASK;
                c = sy - oy;
                if (c == 0) {
                    final int sx =  this.xSubsampling & MASK;
                    final int ox = other.xSubsampling & MASK;
                    c = sx - ox;
                    if (c == 0) {
                        c = (y * sy) - (other.y * oy);
                        if (c == 0) {
                            c = (x * sx) - (other.x * ox);
                        }
                    }
                }
            }
        }
        return c;
    }

    /**
     * Compares this tile with the specified one for equality. Two tiles are considered equal
     * if they have the same {@linkplain #getImageReaderSpi provider}, {@linkplain #getInput
     * input}, {@linkplain #getImageIndex image index}, {@linkplain #getRegion region} and
     * {@linkplain #getSubsampling subsampling}.
     *
     * @param object The object to compare with.
     * @return {@code true} if both objects are equal.
     */
    @Override
    public boolean equals(final Object object) {
        if (object == this) {
            return true;
        }
        if (object != null && object.getClass().equals(getClass())) {
            final Tile that = (Tile) object;
            if (this.x == that.x  &&  this.y == that.y    &&
                this.xSubsampling == that.xSubsampling    &&
                this.ySubsampling == that.ySubsampling    &&
                this.imageIndex   == that.imageIndex      &&
                Utilities.equals(provider, that.provider) &&
                Utilities.deepEquals(input, that.input))
            {
                /*
                 * Compares width and height only if they are defined in both tiles.  We do not
                 * invoke 'getRegion()' because it may be expensive and useless anyway: If both
                 * tiles have the same image reader, image index and input, then logically they
                 * must have the same size - invoking 'getRegion()' would read exactly the same
                 * image twice.
                 */
                return (width  == 0 || that.width  == 0 || width  == that.width) &&
                       (height == 0 || that.height == 0 || height == that.height);
            }
        }
        return false;
    }

    /**
     * Returns a hash code value for this tile. The default implementation uses the
     * {@linkplain #getImageReader reader}, {@linkplain #getInput input} and {@linkplain
     * #getImageIndex image index}, which should be suffisient for uniquely distinguish
     * every tiles.
     */
    @Override
    public int hashCode() {
        return provider.hashCode() + Utilities.deepHashCode(input) + 37*imageIndex;
    }

    /**
     * Returns the name of the given provider, for {@link #toString} purpose only.
     * May returns {@code null} if the name is unknown.
     */
    static String toString(final ImageReaderSpi provider) {
        String name = null;
        if (provider != null) {
            final String[] formats = provider.getFormatNames();
            if (formats != null) {
                int length = 0;
                for (int i=0; i<formats.length; i++) {
                    final String candidate = formats[i];
                    if (candidate != null) {
                        final int lg = candidate.length();
                        if (lg > length) {
                            length = lg;
                            name = candidate;
                        }
                    }
                }
            }
        }
        return name;
    }

    /**
     * Returns a string representation of this tile. The default implementation uses only the
     * public getter methods, so if a subclass override them the effect should be visible in
     * the returned string.
     */
    @Override
    public String toString() {
        final StringBuilder buffer = new StringBuilder(Classes.getShortClassName(this)).append('[');
        buffer.append("format=\"").append(getFormatName())
              .append("\", input=\"").append(getInputName())
              .append("\", index=").append(getImageIndex());
        if (xSubsampling != 0 || ySubsampling != 0) {
            buffer.append(", location=(");
            if (width == 0 && height == 0) {
                final Point location = getLocation();
                buffer.append(location.x).append(',').append(location.y);
            } else try {
                final Rectangle region = getRegion();
                buffer.append(region.x).append(',').append(region.y)
                      .append("), size=(").append(region.width).append(',').append(region.height);
            } catch (IOException e) {
                // Should not happen since we checked that 'getRegion' should be easy.
                // If it happen anyway, put the exception message at the place where
                // coordinates were supposed to appear, so we can debug.
                buffer.append(e);
            }
            final Dimension subsampling = getSubsampling();
            buffer.append("), subsampling=(").append(subsampling.width)
                  .append(',').append(subsampling.height).append(')');
        } else {
            /*
             * Location and subsampling not yet computed, so don't display it. We can not
             * invoke 'getRegion()' neither since it would throw an IllegalStateException.
             * Since we have to read the fields directly, make sure that this instance is
             * not a subclass like LargeTile, otherwise those values may be wrong.
             */
            if ((width != 0 || height != 0) && getClass().equals(Tile.class)) {
                buffer.append(", size=(").append(width & MASK)
                      .append(',').append(height & MASK).append(')');
            }
        }
        return buffer.append(']').toString();
    }

    /**
     * Returns a string representation of a collection of tiles. The tiles are formatted in a
     * table in iteration order. Tip: consider sorting the tiles before to invoke this method;
     * tiles are {@linkplain Comparable comparable} for this purpose.
     * <p>
     * This method is not public because it can consume a large amount of memory (the underlying
     * {@link StringBuffer} can be quite large). Users are encouraged to use the method expecting
     * a {@link Writer}, which may be expensive too but less than this method.
     *
     * @param tiles
     *          The tiles to format in a table.
     * @param maximum
     *          The maximum number of tiles to format. If there is more tiles, a message will be
     *          formatted below the table. A reasonable value like 5000 is recommanded since
     *          attempt to format millions of tiles leads to {@link OutOfMemoryError}.
     * @return A string representation of the given tiles as a table.
     *
     * @see java.util.Collections#sort(List)
     */
    static String toString(final Collection<Tile> tiles, final int maximum) {
        final StringWriter writer = new StringWriter();
        try {
            writeTable(tiles, writer, maximum);
        } catch (IOException e) {
            // Should never happen since we are writing to a StringWriter.
            throw new AssertionError(e);
        }
        return writer.toString();
    }

    /**
     * Formats a collection of tiles in a table. The tiles are appended in iteration
     * order. Tip: consider sorting the tiles before to invoke this method; tiles are
     * {@linkplain Comparable comparable} for this purpose.
     *
     * @param tiles
     *          The tiles to format in a table.
     * @param out
     *          Where to write the table.
     * @param maximum
     *          The maximum number of tiles to format. If there is more tiles, a message will be
     *          formatted below the table. A reasonable value like 5000 is recommanded since
     *          attempt to format millions of tiles leads to {@link OutOfMemoryError}.
     * @throws IOException
     *          If an error occured while writing to the given writer.
     *
     * @see java.util.Collections#sort(List)
     */
    public static void writeTable(final Collection<Tile> tiles, final Writer out, final int maximum)
            throws IOException
    {
        int remaining = maximum;
        final TableWriter table = new TableWriter(out);
        table.nextLine(TableWriter.DOUBLE_HORIZONTAL_LINE);
        table.write("Format\tInput\tindex\tx\ty\twidth\theight\tdx\tdy\n");
        table.nextLine(TableWriter.SINGLE_HORIZONTAL_LINE);
        table.setMultiLinesCells(true);
        for (final Tile tile : tiles) {
            if (--remaining < 0) {
                break;
            }
            table.setAlignment(TableWriter.ALIGN_LEFT);
            final String format = tile.getFormatName();
            if (format != null) {
                table.write(format);
            }
            table.nextColumn();
            table.write(tile.getInputName());
            table.nextColumn();
            table.setAlignment(TableWriter.ALIGN_RIGHT);
            table.write(String.valueOf(tile.getImageIndex()));
            table.nextColumn();
            /*
             * Extracts now the tile information that we are going to format, but those
             * informations may be overriden later if the current tile is some subclass
             * of Tile. We format Tile instances in a special way since it allows us to
             * left a blank for subsampling and tile size if they are not yet computed,
             * rather than throwing an exception.
             */
            int x            = tile.x;
            int y            = tile.y;
            int width        = tile.width        & MASK;
            int height       = tile.height       & MASK;
            int xSubsampling = tile.xSubsampling & MASK;
            int ySubsampling = tile.ySubsampling & MASK;
            if (!tile.getClass().equals(Tile.class)) {
                final Dimension subsampling = tile.getSubsampling();
                xSubsampling = subsampling.width;
                ySubsampling = subsampling.height;
                try {
                    final Rectangle region = tile.getRegion();
                    x      = region.x;
                    y      = region.y;
                    width  = region.width;
                    height = region.height;
                } catch (IOException e) {
                    // The (x,y) are likely to be correct since only (width,height) are read
                    // from the image file. So set only (width,height) to "unknown" and keep
                    // the remaining, with (x,y) obtained from direct access to Tile fields.
                    width  = 0;
                    height = 0;
                }
            }
            table.write(String.valueOf(x));
            table.nextColumn();
            table.write(String.valueOf(y));
            if (width != 0 || height != 0) {
                table.nextColumn();
                table.write(String.valueOf(width));
                table.nextColumn();
                table.write(String.valueOf(height));
            } else {
                table.nextColumn();
                table.nextColumn();
            }
            if (xSubsampling != 0 || ySubsampling != 0) {
                table.nextColumn();
                table.write(String.valueOf(xSubsampling));
                table.nextColumn();
                table.write(String.valueOf(ySubsampling));
            }
            table.nextLine();
        }
        table.nextLine(TableWriter.DOUBLE_HORIZONTAL_LINE);
        /*
         * Table completed. Flushs to the writer and appends additional text if we have
         * not formatted every tiles. IOException may be trown starting from this point
         * (the above code is not expected to thrown any IOException).
         */
        table.flush();
        if (remaining < 0) {
            out.write(Vocabulary.format(VocabularyKeys.MORE_$1, tiles.size() - maximum));
            out.write(System.getProperty("line.separator", "\n"));
        }
    }

    /**
     * Invoked on serialization. Serialization of {@linkplain #provider} is replaced by
     * serialization of its class name only. The actual provider instance will be fetch
     * from ImageIO registry on deserialization.
     */
    private void writeObject(final ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(Classes.getClass(provider));
    }

    /**
     * Invoked on deserialization. The provider is fetch from currently registered providers
     * in the {@link IIORegistry}. The search is performed by classname.
     */
    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        final Object candidate = in.readObject();
        final IIORegistry registry = IIORegistry.getDefaultInstance();
        Class<?> type = candidate.getClass(); // Initialized in case of failure on next line.
        try {
            type = (Class<?>) candidate;
            provider = (ImageReaderSpi) registry.getServiceProviderByClass(type);
        } catch (ClassCastException cause) {
            InvalidClassException e = new InvalidClassException(type.getName(),
                    Errors.format(ErrorKeys.ILLEGAL_CLASS_$2, type, ImageReaderSpi.class));
            e.initCause(cause);
            throw e;
        }
        if (provider == null) {
            throw new ClassNotFoundException(type.getName());
        }
    }
}
