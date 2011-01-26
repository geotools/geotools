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
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.File;
import java.io.IOException;
import java.util.*; // Lot of imports used in this class.
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import javax.imageio.IIOException;
import javax.imageio.IIOParamController;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.stream.ImageInputStream;

import org.geotools.io.TableWriter;
import org.geotools.factory.GeoTools;
import org.geotools.image.io.metadata.MetadataMerge;
import org.geotools.resources.Classes;
import org.geotools.resources.i18n.Errors;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Vocabulary;
import org.geotools.resources.i18n.VocabularyKeys;
import org.geotools.util.FrequencySortedSet;
import org.geotools.util.logging.Logging;


/**
 * An image reader built from a mosaic of other image readers. The mosaic is specified as a
 * collection of {@link Tile} objects, organized in a {@link TileManager}.
 *
 * @since 2.5
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
public class MosaicImageReader extends ImageReader {
    /**
     * {@code true} for disabling operations that may corrupt data values,
     * or {@code false} if only the visual effect matter.
     */
    private static final boolean PRESERVE_DATA = true;

    /**
     * Type arguments made of a single {@code int} value. Used with reflections in order to check
     * if a method has been overriden (knowing that it is not the case allows some optimizations).
     */
    private static final Class<?>[] INTEGER_ARGUMENTS = {
        int.class
    };

    /**
     * An unmodifiable view of {@link #readers} keys.
     */
    private final Set<ImageReaderSpi> providers;

    /**
     * The image reader created for each provider. Keys must be the union of
     * {@link TileManager#getImageReaderSpis} at every image index and must be computed right at
     * {@link #setInput} invocation time.
     */
    private final Map<ImageReaderSpi,ImageReader> readers;

    /**
     * The input given to each image reader. Values are {@linkplain Tile#getInput tile input}
     * before they have been wrapped in an {@linkplain ImageInputStream image input stream}.
     *
     * @see MosaicImageReadParam#readers
     */
    private final Map<ImageReader,Object> readerInputs;

    /**
     * The reader currently under process of reading, or {@code null} if none. Used by
     * {@link #abort} only. Changes must be performed inside a {@code synchronized(this)} block.
     */
    private transient ImageReader reading;

    /**
     * The logging level for tiling information during reads. Note that we choose a default
     * level slightly higher than the intermediate results logged by {@link RTree}.
     */
    private Level level = Level.FINE;

    /**
     * Constructs an image reader with the default provider.
     */
    public MosaicImageReader() {
        this(null);
    }

    /**
     * Constructs an image reader with the specified provider.
     *
     * @param spi The image reader provider, or {@code null} for the default one.
     */
    public MosaicImageReader(final ImageReaderSpi spi) {
        super(spi != null ? spi : Spi.DEFAULT);
        readers = new HashMap<ImageReaderSpi,ImageReader>();
        readerInputs = new IdentityHashMap<ImageReader,Object>();
        providers = Collections.unmodifiableSet(readers.keySet());
    }

    /**
     * Returns the logging level for tile information during reads.
     *
     * @return The current logging level.
     */
    public Level getLogLevel() {
        return level;
    }

    /**
     * Sets the logging level for tile information during reads. The default
     * value is {@link Level#FINE}. A {@code null} value restore the default.
     *
     * @param level The new logging level, or {@code null} for the default.
     */
    public void setLogLevel(Level level) {
        if (level == null) {
            level = Level.FINE;
        }
        this.level = level;
    }

    /**
     * Returns the tiles manager, making sure that it is set.
     *
     * @param imageIndex The image index, from 0 inclusive to {@link #getNumImages} exclusive.
     * @return The tile manager for image at the given index.
     */
    private TileManager getTileManager(final int imageIndex) throws IOException {
        if (input instanceof TileManager[]) {
            final TileManager[] tiles = (TileManager[]) input;
            if (imageIndex < 0 || imageIndex >= tiles.length) {
                throw new IndexOutOfBoundsException(Errors.format(
                        ErrorKeys.INDEX_OUT_OF_BOUNDS_$1, imageIndex));
            }
            return tiles[imageIndex];
        }
        throw new IllegalStateException(Errors.format(ErrorKeys.NO_IMAGE_INPUT));
    }

    /**
     * Returns the input, which is a an array of {@linkplain TileManager tile managers}.
     * The array length is the {@linkplain #getNumImages number of images}. The element
     * at index <var>i</var> is the tile manager to use when reading at image index <var>i</var>.
     */
    @Override
    public TileManager[] getInput() {
        final TileManager[] managers = (TileManager[]) super.getInput();
        return (managers != null) ? managers.clone() : null;
    }

    /**
     * Sets the input source, which is expected to be an array of
     * {@linkplain TileManager tile managers}. If the given input is a singleton, an array or a
     * {@linkplain Collection collection} of {@link Tile} objects, then it will be wrapped in an
     * array of {@link TileManager}s.
     *
     * @param input The input.
     * @param seekForwardOnly if {@code true}, images and metadata may only be read in ascending
     *        order from this input source.
     * @param ignoreMetadata if {@code true}, metadata may be ignored during reads.
     * @throws IllegalArgumentException if {@code input} is not an instance of one of the
     *         expected classes, or if the input can not be used because of an I/O error
     *         (in which case the exception has a {@link IOException} as its
     *         {@linkplain IllegalArgumentException#getCause cause}).
     */
    @Override
    public void setInput(Object input, final boolean seekForwardOnly, final boolean ignoreMetadata)
            throws IllegalArgumentException
    {
        final TileManager[] managers;
        try {
            managers = TileManagerFactory.DEFAULT.createFromObject(input);
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getLocalizedMessage(), e);
        }
        final int numImages = (managers != null) ? managers.length : 0;
        super.setInput(input=managers, seekForwardOnly, ignoreMetadata);
        availableLocales = null; // Will be computed by getAvailableLocales() when first needed.
        /*
         * For every tile readers, closes the stream and disposes the ones that are not needed
         * anymore for the new input. The image readers that may still useful will be recycled.
         * We keep their streams open since it is possible that the new input uses the same ones
         * (the old streams will be closed later if appears to not be used).
         */
        Set<ImageReaderSpi> providers = Collections.emptySet();
        try {
            switch (numImages) {
                case 0: {
                    // Keep the empty provider set.
                    break;
                }
                case 1: {
                    providers = managers[0].getImageReaderSpis();
                    break;
                }
                default: {
                    providers = new HashSet<ImageReaderSpi>(managers[0].getImageReaderSpis());
                    for (int i=1; i<numImages; i++) {
                        providers.addAll(managers[i].getImageReaderSpis());
                    }
                    break;
                }
            }
        } catch (IOException e) {
            /*
             * Failed to get the set of providers.  This is not a big issue; the only consequence
             * is that we will dispose more readers than necessary, which means that we will need
             * to recreate them later. Note that the set of providers may be partially filled.
             */
            Logging.unexpectedException(MosaicImageReader.class, "setInput", e);
        }
        final Iterator<Map.Entry<ImageReaderSpi,ImageReader>> it = readers.entrySet().iterator();
        while (it.hasNext()) {
            final Map.Entry<ImageReaderSpi,ImageReader> entry = it.next();
            if (!providers.contains(entry.getKey())) {
                final ImageReader reader = entry.getValue();
                if (reader != null) {
                    /*
                     * Closes previous streams, if any. It is not a big deal if this operation
                     * fails, since we will not use anymore the old streams anyway. However it
                     * is worth to log.
                     */
                    final Object rawInput = readerInputs.remove(reader);
                    final Object tileInput = reader.getInput();
                    if (rawInput != tileInput) try {
                        Tile.close(tileInput);
                    } catch (IOException exception) {
                        Logging.unexpectedException(MosaicImageReader.class, "setInput", exception);
                    }
                    reader.dispose();
                }
                it.remove();
            }
        }
        for (final ImageReaderSpi provider : providers) {
            if (!readers.containsKey(provider)) {
                readers.put(provider, null);
            }
        }
        assert providers.equals(this.providers);
        assert readers.values().containsAll(readerInputs.keySet());
    }

    /**
     * Returns the <cite>Service Provider Interfaces</cite> (SPI) of every
     * {@linkplain ImageReader image readers} to be used for reading tiles.
     * This method returns an empty set if no input has been set.
     *
     * @return The service providers for tile readers.
     *
     * @see TileManager#getImageReaderSpis
     */
    public Set<ImageReaderSpi> getTileReaderSpis() {
        return providers;
    }

    /**
     * Creates a new {@link ImageReader} from the specified provider. This method do not
     * check the cache and do not store the result in the cache. It should be invoked by
     * {@link #getTileReader} and {@link #getTileReaders} methods only.
     * <p>
     * It is technically possible to return the same {@link ImageReader} instance from
     * different {@link ImageReaderSpi}. It would broke the usual {@code ImageReaderSpi}
     * contract for no obvious reason, but technically this class should work correctly
     * even in such case.
     *
     * @param  provider The provider. Must be a member of {@link #getTileReaderSpis}.
     * @return The image reader for the given provider.
     * @throws IOException if the image reader can not be created.
     */
    private ImageReader createReaderInstance(final ImageReaderSpi provider) throws IOException {
        final ImageReader reader = provider.createReaderInstance();
        if (locale != null) {
            try {
                reader.setLocale(locale);
            } catch (IllegalArgumentException e) {
                // Invalid locale. Ignore this exception since it will not prevent the image
                // reader to work mostly as expected (warning messages may be in a different
                // locale, which is not a big deal).
                Logging.recoverableException(MosaicImageReader.class, "getTileReader", e);
            }
        }
        return reader;
    }

    /**
     * Returns the image reader for the given provider.
     *
     * @param  provider The provider. Must be a member of {@link #getTileReaderSpis}.
     * @return The image reader for the given provider.
     * @throws IOException if the image reader can not be created.
     */
    final ImageReader getTileReader(final ImageReaderSpi provider) throws IOException {
        assert readers.containsKey(provider); // Key should exists even if the value is null.
        ImageReader reader = readers.get(provider);
        if (reader == null) {
            reader = createReaderInstance(provider);
            readers.put(provider, reader);
        }
        return reader;
    }

    /**
     * Returns every readers used for reading tiles. New readers may be created on the fly
     * by this method.  However failure to create them will be logged rather than trown as
     * an exception. In such case the information obtained by the caller may be incomplete
     * and the exception may be thrown later when {@link #getTileReader} will be invoked.
     */
    final Set<ImageReader> getTileReaders() {
        for (final Map.Entry<ImageReaderSpi,ImageReader> entry : readers.entrySet()) {
            ImageReader reader = entry.getValue();
            if (reader == null) {
                final ImageReaderSpi provider = entry.getKey();
                try {
                    reader = createReaderInstance(provider);
                } catch (IOException exception) {
                    Logging.unexpectedException(MosaicImageReader.class, "getTileReaders", exception);
                    continue;
                }
                entry.setValue(reader);
            }
            if (!readerInputs.containsKey(reader)) {
                readerInputs.put(reader, null);
            }
        }
        assert readers.values().containsAll(readerInputs.keySet());
        return readerInputs.keySet();
    }

    /**
     * Returns a reader for the tiles, or {@code null}. This method tries to returns an instance
     * of the most specific reader class. If no suitable instance is found, then it returns
     * {@code null}.
     * <p>
     * This method is typically invoked for fetching an instance of {@code ImageReadParam}. We
     * look for the most specific class because it may contains additional parameters that are
     * ignored by super-classes. If we fail to find a suitable instance, then the caller shall
     * fallback on the {@link ImageReader} default implementation.
     */
    final ImageReader getTileReader() {
        final Set<ImageReader> readers = getTileReaders();
        Class<?> type = Classes.specializedClass(readers);
        while (type!=null && ImageReader.class.isAssignableFrom(type)) {
            for (final ImageReader candidate : readers) {
                if (type.equals(candidate.getClass())) {
                    return candidate;
                }
            }
            type = type.getSuperclass();
        }
        return null;
    }

    /**
     * From the given set of tiles, select one tile to use as a prototype.
     * This method tries to select the tile which use the most specific reader.
     *
     * @return The most specific tile, or {@code null} if none.
     */
    private Tile getSpecificTile(final Collection<Tile> tiles) {
        Tile fallback = null;
        final Set<ImageReader> readers = getTileReaders();
        Class<?> type = Classes.specializedClass(readers);
        while (type!=null && ImageReader.class.isAssignableFrom(type)) {
            for (final ImageReader reader : readers) {
                if (type.equals(reader.getClass())) {
                    final ImageReaderSpi provider = reader.getOriginatingProvider(); // May be null
                    for (final Tile tile : tiles) {
                        /*
                         * We give precedence to ImageReaderSpi.equals(ImageReaderSpi) over
                         * ImageReaderSpi.isOwnReader(ImageReader) because we need consistency
                         * with the 'readers' HashMap. However the later will be used as a
                         * fallback if no exact match has been found.
                         */
                        final ImageReaderSpi candidate = tile.getImageReaderSpi(); // Never null
                        if (candidate.equals(provider)) {
                            return tile;
                        }
                        if (fallback == null && candidate.isOwnReader(reader)) {
                            fallback = tile;
                        }
                    }
                }
            }
            type = type.getSuperclass();
        }
        return fallback;
    }

    /**
     * Returns an array of locales that may be used to localize warning listeners. The default
     * implementations returns the union of the locales supported by this reader and every
     * {@linkplain Tile#getImageReader tile readers}.
     *
     * @return An array of supported locales, or {@code null}.
     */
    @Override
    public Locale[] getAvailableLocales() {
        if (availableLocales == null) {
            final Set<Locale> locales = new LinkedHashSet<Locale>();
            for (final ImageReader reader : getTileReaders()) {
                final Locale[] additional = reader.getAvailableLocales();
                if (additional != null) {
                    for (final Locale locale : additional) {
                        locales.add(locale);
                    }
                }
            }
            if (locales.isEmpty()) {
                return null;
            }
            availableLocales = locales.toArray(new Locale[locales.size()]);
        }
        return availableLocales.clone();
    }

    /**
     * Sets the current locale of this image reader and every
     * {@linkplain Tile#getImageReader tile readers}.
     *
     * @param locale the desired locale, or {@code null}.
     * @throws IllegalArgumentException if {@code locale} is non-null but is not
     *         one of the {@linkplain #getAvailableLocales available locales}.
     */
    @Override
    public void setLocale(final Locale locale) throws IllegalArgumentException {
        super.setLocale(locale); // May thrown an exception.
        for (final ImageReader reader : readers.values()) {
            try {
                reader.setLocale(locale);
            } catch (IllegalArgumentException e) {
                // Locale not supported by the reader. It may occurs
                // if not all readers support the same set of locales.
                Logging.recoverableException(MosaicImageReader.class, "setLocale", e);
            }
        }
    }

    /**
     * Returns the number of images, not including thumbnails.
     *
     * @throws IOException If an error occurs reading the information from the input source.
     */
    public int getNumImages(final boolean allowSearch) throws IOException {
        return (input instanceof TileManager[]) ? ((TileManager[]) input).length : 0;
    }

    /**
     * Returns {@code true} if there is more than one tile for the given image index.
     *
     * @param  imageIndex The index of the image to be queried.
     * @return {@code true} If there is at least two tiles.
     * @throws IOException If an error occurs reading the information from the input source.
     */
    @Override
    public boolean isImageTiled(final int imageIndex) throws IOException {
        return getTileManager(imageIndex).isImageTiled();
    }

    /**
     * Returns the width in pixels of the given image within the input source.
     *
     * @param  imageIndex The index of the image to be queried.
     * @return The width of the image.
     * @throws IOException If an error occurs reading the information from the input source.
     */
    public int getWidth(final int imageIndex) throws IOException {
        return getTileManager(imageIndex).getRegion().width;
    }

    /**
     * Returns the height in pixels of the given image within the input source.
     *
     * @param  imageIndex The index of the image to be queried.
     * @return The height of the image.
     * @throws IOException If an error occurs reading the information from the input source.
     */
    public int getHeight(final int imageIndex) throws IOException {
        return getTileManager(imageIndex).getRegion().height;
    }

    /**
     * Returns the width of a tile in the given image.
     *
     * @param  imageIndex The index of the image to be queried.
     * @return The width of a tile.
     * @throws IOException If an error occurs reading the information from the input source.
     */
    @Override
    public int getTileWidth(final int imageIndex) throws IOException {
        return getTileManager(imageIndex).getTileSize().width;
    }

    /**
     * Returns the height of a tile in the given image.
     *
     * @param  imageIndex The index of the image to be queried.
     * @return The height of a tile.
     * @throws IOException If an error occurs reading the information from the input source.
     */
    @Override
    public int getTileHeight(final int imageIndex) throws IOException {
        return getTileManager(imageIndex).getTileSize().height;
    }

    /**
     * Returns {@code true} if every image reader uses the default implementation for the given
     * method. Some methods may avoid costly file seeking when this method returns {@code true}.
     * <p>
     * This method always returns {@code true} if there is no tiles.
     */
    private boolean useDefaultImplementation(final String methodName, final Class<?>[] parameterTypes) {
        for (final ImageReader reader : getTileReaders()) {
            Class<?> type = reader.getClass();
            try {
                type = type.getMethod(methodName, parameterTypes).getDeclaringClass();
            } catch (NoSuchMethodException e) {
                Logging.unexpectedException(MosaicImageReader.class, "useDefaultImplementation", e);
                return false; // Conservative value.
            }
            if (!type.equals(ImageReader.class)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns {@code true} if there is only one tile in the given collection and if that singleton
     * tile encloses fully the given source region. In such case, {@code MosaicImageReader} can
     * delegates directly the reading process to the reader used by that tile.
     *
     * @param  tiles The tile collection.
     * @param  sourceRegion The source region to be read, as computed by {@link #getSourceRegion}.
     * @return {@code true} if {@code MosaicImageReader} can delegates the reading process to the
     *         singleton tile contained in the given collection.
     * @throws IOException If an I/O operation was requiered and failed.
     */
    private static boolean canDelegate(final Collection<Tile> tiles, final Rectangle sourceRegion)
            throws IOException
    {
        final Iterator<Tile> it = tiles.iterator();
        if (it.hasNext()) {
            final Tile tile = it.next();
            if (!it.hasNext()) {
                return tile.getRegion().contains(sourceRegion);
            }
        }
        return false;
    }

    /**
     * Returns {@code true} if the storage format of the given image places no inherent impediment
     * on random access to pixels. The default implementation returns {@code true} if the input of
     * every tiles is a {@link File} and {@code isRandomAccessEasy} returned {@code true} for all
     * tile readers.
     *
     * @throws IOException If an error occurs reading the information from the input source.
     */
    @Override
    public boolean isRandomAccessEasy(final int imageIndex) throws IOException {
        if (useDefaultImplementation("isRandomAccessEasy", INTEGER_ARGUMENTS)) {
            return super.isRandomAccessEasy(imageIndex);
        }
        for (final Tile tile : getTileManager(imageIndex).getTiles()) {
            final Object input = tile.getInput();
            if (!(input instanceof File)) {
                return false;
            }
            final ImageReader reader = tile.getImageReader(this, true, true);
            if (!reader.isRandomAccessEasy(tile.getImageIndex())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns the aspect ratio. If all tiles have the same aspect ratio, then that ratio is
     * returned. Otherwise the {@linkplain ImageReader#getAspectRatio default value} is returned.
     *
     * @param  imageIndex The index of the image to be queried.
     * @throws IOException If an error occurs reading the information from the input source.
     */
    @Override
    public float getAspectRatio(final int imageIndex) throws IOException {
        if (!useDefaultImplementation("getAspectRatio", INTEGER_ARGUMENTS)) {
            float ratio = Float.NaN;
            for (final Tile tile : getTileManager(imageIndex).getTiles()) {
                final ImageReader reader = tile.getImageReader(this, true, true);
                final float candidate = reader.getAspectRatio(tile.getImageIndex());
                if (candidate == ratio || Float.isNaN(candidate)) {
                    // Same ratio or unspecified ratio.
                    continue;
                }
                if (!Float.isNaN(ratio)) {
                    // The ratio is different for different tile. Fall back on default.
                    return super.getAspectRatio(imageIndex);
                }
                ratio = candidate;
            }
            if (!Float.isNaN(ratio)) {
                return ratio;
            }
        }
        return super.getAspectRatio(imageIndex);
    }

    /**
     * Returns the image type policy from the specified parameter.
     * Fallback on the default policy if the parameter to not specify any.
     */
    private ImageTypePolicy getImageTypePolicy(final ImageReadParam param) {
        if (param instanceof MosaicImageReadParam) {
            final ImageTypePolicy policy = ((MosaicImageReadParam) param).getImageTypePolicy();
            if (policy != null) {
                return policy;
            }
        }
        return getDefaultImageTypePolicy();
    }

    /**
     * Returns the policy for {@link #getImageTypes computing image types}. This is also
     * the policy used by {@linkplain #read read} method when none has been explicitly
     * {@linkplain MosaicImageReadParam#setImageTypePolicy set in read parameters}.
     * <p>
     * The default implementation makes the following choice based on the number of
     * {@linkplain #getTileReaderSpis reader providers}:
     * <ul>
     *   <li>{@link ImageTypePolicy#SUPPORTED_BY_ALL SUPPORTED_BY_ALL} if two or more</li>
     *   <li>{@link ImageTypePolicy#SUPPORTED_BY_ONE SUPPORTED_BY_ONE} if exactly one</li>
     *   <li>{@link ImageTypePolicy#ALWAYS_ARGB ALWAYS_ARGB} if none.</li>
     * </ul>
     * <p>
     * Note that {@link ImageTypePolicy#SUPPORTED_BY_ONE SUPPORTED_BY_ONE} is <strong>not</strong>
     * a really safe choice even if there is only one provider, because the image type can also
     * depends on {@linkplain Tile#getInput tile input}. However the safest choice in all cases
     * ({@link ImageTypePolicy#SUPPORTED_BY_ALL SUPPORTED_BY_ALL}) is costly and often not
     * necessary. The current implementation is a compromize between safety and performance.
     * <p>
     * If Java assertions are enabled, this reader will verify that {@code SUPPORTED_BY_ONE}
     * produces the same result than {@code SUPPORTED_BY_ALL}.
     * <p>
     * Subclasses can override this method if they want a different policy.
     *
     * @return The default image type policy.
     */
    public ImageTypePolicy getDefaultImageTypePolicy() {
        switch (providers.size()) {
            default: return ImageTypePolicy.SUPPORTED_BY_ALL;
            case 1:  return ImageTypePolicy.SUPPORTED_BY_ONE;
            case 0:  return ImageTypePolicy.ALWAYS_ARGB;
        }
    }

    /**
     * Returns type image type specifier for policy of pre-defined types.
     * More types may be added in future GeoTools versions.
     */
    private static ImageTypeSpecifier getPredefinedImageType(final ImageTypePolicy policy) {
        final int type;
        switch (policy) {
            case ALWAYS_ARGB: type = BufferedImage.TYPE_INT_ARGB; break;
            default: throw new IllegalArgumentException(policy.toString());
        }
        return ImageTypeSpecifier.createFromBufferedImageType(type);
    }

    /**
     * Returns an image type which most closely represents the "raw" internal format of the image.
     * The default implementation depends on the {@linkplain #getDefaultImageTypePolicy default
     * image type policy}:
     * <ul>
     *   <li>For {@link ImageTypePolicy#SUPPORTED_BY_ONE SUPPORTED_BY_ONE}, this method delegates
     *       directly to the reader of an arbitrary tile (typically the first one).</li>
     *   <li>For {@link ImageTypePolicy#SUPPORTED_BY_ALL SUPPORTED_BY_ALL}, this method invokes
     *       {@code getRawImageType} for every tile readers, ommits the types that are not declared
     *       in <code>{@linkplain ImageReader#getImageTypes getImageTypes}(imageIndex)</code> for
     *       every tile readers, and returns the most common remainding value. If none is found,
     *       then some {@linkplain ImageReader#getRawImageType default specifier} is returned.</li>
     * </ul>
     *
     * @param  imageIndex The image index, from 0 inclusive to {@link #getNumImages} exclusive.
     * @return A raw image type specifier.
     * @throws IOException If an error occurs reading the information from the input source.
     */
    @Override
    public ImageTypeSpecifier getRawImageType(final int imageIndex) throws IOException {
        ImageTypeSpecifier type;
        final ImageTypePolicy policy = getDefaultImageTypePolicy();
        switch (policy) {
            default: {
                type = getPredefinedImageType(policy);
                break;
            }
            case SUPPORTED_BY_ONE: {
                final Collection<Tile> tiles = getTileManager(imageIndex).getTiles();
                final Tile tile = getSpecificTile(tiles);
                if (tile != null) {
                    type = tile.getImageReader(this, true, true).getRawImageType(imageIndex);
                    assert type.equals(getRawImageType(tiles)) : incompatibleImageType(tile);
                } else {
                    type = super.getRawImageType(imageIndex);
                }
                break;
            }
            case SUPPORTED_BY_ALL: {
                final Collection<Tile> tiles = getTileManager(imageIndex).getTiles();
                type = getRawImageType(tiles);
                if (type == null) {
                    type = super.getRawImageType(imageIndex);
                }
                break;
            }
        }
        return type;
    }

    /**
     * Returns an image type which most closely represents the "raw" internal format of the
     * given set of tiles. If none is found, returns {@code null}.
     * <p>
     * If there is more than one supported types, this method will give preference to the type
     * having transparency. We do that because we have no garantee that a tile exists for every
     * area in an image to be read, and the empty area typically need to remain transparent.
     *
     * @param  tiles The tiles to iterate over.
     * @return A raw image type specifier acceptable for all tiles, or {@code null} if none.
     * @throws IOException If an error occurs reading the information from the input source.
     */
    private ImageTypeSpecifier getRawImageType(final Collection<Tile> tiles) throws IOException {
        // Gets the list of every raw image types, with the most frequent type first.
        final Set<ImageTypeSpecifier> rawTypes = new FrequencySortedSet<ImageTypeSpecifier>(true);
        final Set<ImageTypeSpecifier> allowed = getImageTypes(tiles, rawTypes);
        rawTypes.retainAll(allowed);
        boolean transparent = true;
        do {
            Iterator<ImageTypeSpecifier> it = rawTypes.iterator();
            while (it.hasNext()) {
                final ImageTypeSpecifier type = it.next();
                if (!transparent || isTransparent(type)) {
                    return type;
                }
            }
            // No raw image reader type. Returns the first allowed type even if it is not "raw".
            it = allowed.iterator();
            while (it.hasNext()) {
                final ImageTypeSpecifier type = it.next();
                if (!transparent || isTransparent(type)) {
                    return type;
                }
            }
            // If no type was found and if we were looking for a transparent
            // type, searchs again for a type no matter its transparency.
        } while ((transparent = !transparent) == false);
        return null;
    }

    /**
     * Returns the possible image types to which the given image may be decoded. This method
     * invokes <code>{@linkplain ImageReader#getImageTypes getImageTypes}(imageIndex)</code>
     * on every tile readers and returns the intersection of all sets (i.e. only the types
     * that are supported by every readers).
     *
     * @param  tiles       The tiles to iterate over.
     * @param  rawTypes    If non-null, a collection where to store the raw image types.
     *                     No filtering is applied on this collection.
     * @return The image type specifiers that are common to all tiles.
     * @throws IOException If an error occurs reading the information from the input source.
     */
    private Set<ImageTypeSpecifier> getImageTypes(final Collection<Tile> tiles,
                                                  final Collection<ImageTypeSpecifier> rawTypes)
            throws IOException
    {
        int pass = 0;
        final Map<ImageTypeSpecifier,Integer> types = new LinkedHashMap<ImageTypeSpecifier,Integer>();
        for (final Tile tile : tiles) {
            final ImageReader reader = tile.getImageReader(this, true, true);
            final int imageIndex = tile.getImageIndex();
            if (rawTypes != null) {
                rawTypes.add(reader.getRawImageType(imageIndex));
            }
            final Iterator<ImageTypeSpecifier> toAdd = reader.getImageTypes(imageIndex);
            while (toAdd.hasNext()) {
                final ImageTypeSpecifier type = toAdd.next();
                final Integer old = types.put(type, pass);
                if (old == null && pass != 0) {
                    // Just added a type that did not exists in previous tiles, so remove it.
                    types.remove(type);
                }
            }
            // Remove all previous types not found in this pass.
            for (final Iterator<Integer> it=types.values().iterator(); it.hasNext();) {
                if (it.next().intValue() != pass) {
                    it.remove();
                }
            }
            pass++;
        }
        return types.keySet();
    }

    /**
     * Returns possible image types to which the given image may be decoded. The default
     * implementation depends on the {@linkplain #getDefaultImageTypePolicy default image
     * type policy}:
     * <ul>
     *   <li>For {@link ImageTypePolicy#SUPPORTED_BY_ONE SUPPORTED_BY_ONE}, this method delegates
     *       directly to the reader of an arbitrary tile (typically the first one).</li>
     *   <li>For {@link ImageTypePolicy#SUPPORTED_BY_ALL SUPPORTED_BY_ALL}, this method invokes
     *       <code>{@linkplain ImageReader#getImageTypes getImageTypes}(imageIndex)</code> on
     *       every tile readers and returns the intersection of all sets (i.e. only the types
     *       that are supported by every readers).</li>
     * </ul>
     *
     * @param  imageIndex  The image index, from 0 inclusive to {@link #getNumImages} exclusive.
     * @return The image type specifiers that are common to all tiles.
     * @throws IOException If an error occurs reading the information from the input source.
     */
    public Iterator<ImageTypeSpecifier> getImageTypes(final int imageIndex) throws IOException {
        Iterator<ImageTypeSpecifier> types;
        final ImageTypePolicy policy = getDefaultImageTypePolicy();
        switch (policy) {
            default: {
                types = Collections.singleton(getPredefinedImageType(policy)).iterator();
                break;
            }
            case SUPPORTED_BY_ONE: {
                final Collection<Tile> tiles = getTileManager(imageIndex).getTiles();
                final Tile tile = getSpecificTile(tiles);
                if (tile == null) {
                    final Collection<ImageTypeSpecifier> t = Collections.emptySet();
                    return t.iterator();
                }
                types = tile.getImageReader(this, true, true).getImageTypes(imageIndex);
                assert (types = containsAll(getImageTypes(tiles, null), types)) != null : incompatibleImageType(tile);
                break;
            }
            case SUPPORTED_BY_ALL: {
                final Collection<Tile> tiles = getTileManager(imageIndex).getTiles();
                types = getImageTypes(tiles, null).iterator();
                break;
            }
        }
        return types;
    }

    /**
     * Helper method for assertions only.
     */
    private static Iterator<ImageTypeSpecifier> containsAll(
            final Collection<ImageTypeSpecifier> expected, final Iterator<ImageTypeSpecifier> types)
    {
        final List<ImageTypeSpecifier> asList = new ArrayList<ImageTypeSpecifier>(expected.size());
        while (types.hasNext()) {
            asList.add(types.next());
        }
        return expected.containsAll(asList) ? asList.iterator() : null;
    }

    /**
     * Returns {@code true} if the given type has transparency.
     */
    private static boolean isTransparent(final ImageTypeSpecifier type) {
        return type.getColorModel().getTransparency() != ColorModel.OPAQUE;
    }

    /**
     * Helper method for assertions only.
     */
    private static String incompatibleImageType(final Tile tile) {
        return "Image type computed by " + ImageTypePolicy.SUPPORTED_BY_ONE +
                " policy using " +  tile + " is incompatible with type computed by " +
                ImageTypePolicy.SUPPORTED_BY_ALL + " policy.";
    }

    /**
     * Returns default parameters appropriate for this format.
     */
    @Override
    public MosaicImageReadParam getDefaultReadParam() {
        return new MosaicImageReadParam(this);
    }

    /**
     * Returns the metadata associated with the input source as a whole, or {@code null}.
     * The default implementation tries to {@linkplain IIOMetadata#mergeTree merge} the
     * metadata from every tiles.
     *
     * @throws IOException if an error occurs during reading.
     */
    public IIOMetadata getStreamMetadata() throws IOException {
        IIOMetadata metadata = null;
        if (input instanceof TileManager[]) {
            final Set<ReaderInputPair> done = new HashSet<ReaderInputPair>();
            for (final TileManager manager : (TileManager[]) input) {
                for (final Tile tile : manager.getTiles()) {
                    final ImageReader reader = tile.getImageReader(this, true, ignoreMetadata);
                    final Object input = reader.getInput();
                    if (done.add(new ReaderInputPair(reader, input))) {
                        final IIOMetadata candidate = reader.getStreamMetadata();
                        metadata = MetadataMerge.merge(candidate, metadata);
                    }
                }
            }
        }
        return metadata;
    }

    /**
     * Returns the stream metadata for the given format and nodes, or {@code null}.
     * The default implementation tries to {@linkplain IIOMetadata#mergeTree merge}
     * the metadata from every tiles.
     *
     * @throws IOException if an error occurs during reading.
     */
    @Override
    public IIOMetadata getStreamMetadata(final String formatName, final Set<String> nodeNames)
            throws IOException
    {
        IIOMetadata metadata = null;
        if (input instanceof TileManager[]) {
            final Set<ReaderInputPair> done = new HashSet<ReaderInputPair>();
            for (final TileManager manager : (TileManager[]) input) {
                for (final Tile tile : manager.getTiles()) {
                    final ImageReader reader = tile.getImageReader(this, true, ignoreMetadata);
                    final Object input = reader.getInput();
                    if (done.add(new ReaderInputPair(reader, input))) {
                        final IIOMetadata candidate = reader.getStreamMetadata(formatName, nodeNames);
                        metadata = MetadataMerge.merge(candidate, metadata);
                    }
                }
            }
        }
        return metadata;
    }

    /**
     * Returns the metadata associated with the given image, or {@code null}. The
     * default implementation tries to {@linkplain IIOMetadata#mergeTree merge}
     * the metadata from every tiles.
     *
     * @param  imageIndex the index of the image whose metadata is to be retrieved.
     * @return The metadata, or {@code null}.
     * @throws IllegalStateException if the input source has not been set.
     * @throws IndexOutOfBoundsException if the supplied index is out of bounds.
     * @throws IOException if an error occurs during reading.
     */
    public IIOMetadata getImageMetadata(final int imageIndex) throws IOException {
        IIOMetadata metadata = null;
        for (final Tile tile : getTileManager(imageIndex).getTiles()) {
            final ImageReader reader = tile.getImageReader(this, true, ignoreMetadata);
            final IIOMetadata candidate = reader.getImageMetadata(tile.getImageIndex());
            metadata = MetadataMerge.merge(candidate, metadata);
        }
        return metadata;
    }

    /**
     * Returns the image metadata for the given format and nodes, or {@code null}.
     * The default implementation tries to {@linkplain IIOMetadata#mergeTree merge}
     * the metadata from every tiles.
     *
     * @throws IOException if an error occurs during reading.
     */
    @Override
    public IIOMetadata getImageMetadata(final int imageIndex,
            final String formatName, final Set<String> nodeNames) throws IOException
    {
        IIOMetadata metadata = null;
        for (final Tile tile : getTileManager(imageIndex).getTiles()) {
            final ImageReader reader = tile.getImageReader(this, true, ignoreMetadata);
            final IIOMetadata candidate = reader.getImageMetadata(tile.getImageIndex(), formatName, nodeNames);
            metadata = MetadataMerge.merge(candidate, metadata);
        }
        return metadata;
    }

    /**
     * Reads the image indexed by {@code imageIndex} using a supplied parameters.
     * <strong>See {@link MosaicImageReadParam} for a performance recommandation</strong>.
     * If the parameters allow subsampling changes, then the subsampling effectively used
     * will be written back in the given parameters.
     *
     * @param  imageIndex The index of the image to be retrieved.
     * @param  param The parameters used to control the reading process, or {@code null}.
     *         An instance of {@link MosaicImageReadParam} is expected but not required.
     * @return The desired portion of the image.
     * @throws IOException if an error occurs during reading.
     */
    public BufferedImage read(final int imageIndex, final ImageReadParam param) throws IOException {
        clearAbortRequest();
        processImageStarted(imageIndex);
        final Dimension subsampling = new Dimension(1,1);
        boolean subsamplingChangeAllowed = false;
        MosaicImageReadParam mosaicParam = null;
        boolean nullForEmptyImage = false;
        if (param != null) {
            subsampling.width  = param.getSourceXSubsampling();
            subsampling.height = param.getSourceYSubsampling();
            if (param instanceof MosaicImageReadParam) {
                mosaicParam = (MosaicImageReadParam) param;
                subsamplingChangeAllowed = mosaicParam.isSubsamplingChangeAllowed();
                nullForEmptyImage = mosaicParam.getNullForEmptyImage();
            }
            // Note: we don't extract subsampling offsets because they will be taken in account
            //       in the 'sourceRegion' to be calculated by ImageReader.computeRegions(...).
        }
        final int srcWidth  = getWidth (imageIndex);
        final int srcHeight = getHeight(imageIndex);
        final Rectangle sourceRegion = getSourceRegion(param, srcWidth, srcHeight);
        final Collection<Tile> tiles = getTileManager(imageIndex)
                .getTiles(sourceRegion, subsampling, subsamplingChangeAllowed);
        if (nullForEmptyImage && tiles.isEmpty()) {
            processImageComplete();
            return null;
        }
        /*
         * If the subsampling changed as a result of TileManager.getTiles(...) call,
         * stores the new subsampling values in the parameters. Note that the source
         * region will need to be computed again, which we will do later.
         */
        final int xSubsampling = subsampling.width;
        final int ySubsampling = subsampling.height;
        if (subsamplingChangeAllowed) {
            if (param.getSourceXSubsampling() != xSubsampling ||
                param.getSourceYSubsampling() != ySubsampling)
            {
                final int xOffset = param.getSubsamplingXOffset() % xSubsampling;
                final int yOffset = param.getSubsamplingYOffset() % ySubsampling;
                param.setSourceSubsampling(xSubsampling, ySubsampling, xOffset, yOffset);
            } else {
                subsamplingChangeAllowed = false;
            }
        }
        /*
         * If there is exactly one image to read, we will left the image reference to null. It will
         * be understood later as an indication to delegate directly to the sole image reader as an
         * optimization (no search for raw data type). Otherwise, we need to create the destination
         * image here. Note that this is the only image ever to be created during a mosaic read,
         * unless some underlying ImageReader do not honor our ImageReadParam.setDestination(image)
         * setting. In such case, the default behavior is to thrown an exception.
         */
        BufferedImage image = null;
        final Rectangle destRegion;
        final Point destinationOffset;
        ImageTypePolicy policy = null;
        if (canDelegate(tiles, sourceRegion) && (policy = getImageTypePolicy(param)).canDelegate) {
            destRegion = null;
            if (subsamplingChangeAllowed) {
                sourceRegion.setBounds(getSourceRegion(param, srcWidth, srcHeight));
            }
            destinationOffset = (param != null) ? param.getDestinationOffset() : new Point();
        } else {
            if (param != null) {
                image = param.getDestination();
            }
            destRegion = new Rectangle(); // Computed by the following method call.
            computeRegions(param, srcWidth, srcHeight, image, sourceRegion, destRegion);
            if (image == null) {
                /*
                 * If no image was explicitly specified, creates one using a raw image type
                 * acceptable for all tiles. An exception will be thrown if no such raw type
                 * was found. Note that this fallback may be a little bit costly since it may
                 * imply to open, close and reopen later some streams.
                 */
                ImageTypeSpecifier imageType = null;
                if (param != null) {
                    imageType = param.getDestinationType();
                }
                if (imageType == null) {
                    if (policy == null) {
                        policy = getImageTypePolicy(param);
                    }
                    switch (policy) {
                        default: {
                            imageType = getPredefinedImageType(policy);
                            break;
                        }
                        case SUPPORTED_BY_ONE: {
                            final Tile tile = getSpecificTile(tiles);
                            if (tile != null) {
                                imageType = tile.getImageReader(this, true, true).getRawImageType(imageIndex);
                                assert imageType.equals(getRawImageType(tiles)) : incompatibleImageType(tile);
                            }
                            break;
                        }
                        case SUPPORTED_BY_ALL: {
                            imageType = getRawImageType(tiles);
                            break;
                        }
                    }
                    if (imageType == null) {
                        /*
                         * This case occurs if the tiles collection is empty.  We want to produce
                         * a fully transparent (or empty) image in such case. Remember that tiles
                         * are not required to exist everywhere in the mosaic bounds,  so the set
                         * of tiles in a particular sub-area is allowed to be empty.
                         */
                        imageType = getRawImageType(imageIndex);
                    }
                }
                final int width  = destRegion.x + destRegion.width;
                final int height = destRegion.y + destRegion.height;
                image = imageType.createBufferedImage(width, height);
                computeRegions(param, srcWidth, srcHeight, image, sourceRegion, destRegion);
            }
            destinationOffset = destRegion.getLocation();
        }
        /*
         * Gets a MosaicImageReadParam instance to be used for caching Tile parameters. There is
         * no need to invokes 'getDefaultReadParam()' since we are interrested only in the cache
         * that MosaicImageReadParam provide.
         */
        MosaicController controller = null;
        if (mosaicParam == null) {
            mosaicParam = new MosaicImageReadParam();
        } else if (mosaicParam.hasController()) {
            final IIOParamController candidate = mosaicParam.getController();
            if (candidate instanceof MosaicController) {
                controller = (MosaicController) candidate;
            }
        }
        /*
         * If logging are enabled, we will format the tiles that we read in a table and logs
         * the table as one log record before the actual reading. If there is nothing to log,
         * then the table will be left to null. If non-null, the table will be completed in
         * the 'do' loop below.
         */
        final Logger logger = Logging.getLogger(MosaicImageReader.class);
        TableWriter table = null;
        if (logger.isLoggable(level)) {
            table = new TableWriter(null, TableWriter.SINGLE_VERTICAL_LINE);
            table.writeHorizontalSeparator();
            table.write("Reader\tTile\tIndex\tSize\tSource\tDestination\tSubsampling");
            table.writeHorizontalSeparator();
        }
        /*
         * Now read every tiles... If logging is disabled, then this loop will be executed exactly
         * once. If logging is enabled, then this loop will be executed twice where the first pass
         * is used only in order to format the table to be logged. In every cases, the last pass is
         * the one where the actual reading occur. We do this two pass approach in order to get the
         * table logged before loading rather than after. This is more useful in case of exception.
         */
        do {
            for (final Tile tile : tiles) {
                if (abortRequested()) {
                    processReadAborted();
                    break;
                }
                final Rectangle tileRegion = tile.getAbsoluteRegion();
                final Rectangle regionToRead = tileRegion.intersection(sourceRegion);
                /*
                 * Computes the location of the region to read relative to the source region
                 * requested by the user, and make sure that this location is a multiple of
                 * subsampling (if any). The region to read may become bigger by one pixel
                 * (in tile units) as a result of this calculation.
                 */
                int xOffset = (regionToRead.x - sourceRegion.x) % xSubsampling;
                int yOffset = (regionToRead.y - sourceRegion.y) % ySubsampling;
                if (xOffset != 0) {
                    regionToRead.x     -= xOffset;
                    regionToRead.width += xOffset;
                    if (regionToRead.x < tileRegion.x) {
                        regionToRead.x = tileRegion.x;
                        if (regionToRead.width > tileRegion.width) {
                            regionToRead.width = tileRegion.width;
                        }
                    }
                }
                if (yOffset != 0) {
                    regionToRead.y      -= yOffset;
                    regionToRead.height += yOffset;
                    if (regionToRead.y < tileRegion.y) {
                        regionToRead.y = tileRegion.y;
                        if (regionToRead.height > tileRegion.height) {
                            regionToRead.height = tileRegion.height;
                        }
                    }
                }
                if (regionToRead.isEmpty()) {
                    continue;
                }
                /*
                 * Now that the offset is a multiple of subsampling, computes the destination offset.
                 * Then translate the region to read from "this image reader" space to "tile" space.
                 */
                if (destRegion != null) {
                    xOffset = (regionToRead.x - sourceRegion.x) / xSubsampling;
                    yOffset = (regionToRead.y - sourceRegion.y) / ySubsampling;
                    destinationOffset.x = destRegion.x + xOffset;
                    destinationOffset.y = destRegion.y + yOffset;
                }
                assert tileRegion.contains(regionToRead) : regionToRead;
                regionToRead.translate(-tileRegion.x, -tileRegion.y);
                /*
                 * Sets the parameters to be given to the tile reader. We don't use any subsampling
                 * offset because it has already been calculated in the region to read. Note that
                 * the tile subsampling should be a divisor of image subsampling; this condition must
                 * have been checked by the tile manager when it selected the tiles to be returned.
                 */
                subsampling.setSize(tile.getSubsampling());
                assert xSubsampling % subsampling.width  == 0 : subsampling;
                assert ySubsampling % subsampling.height == 0 : subsampling;
                /*
                 * Transform the region to read from "absolute" coordinates to "relative to tile"
                 * coordinates. We want to round x and y toward negative infinity, which require
                 * special processing for negative numbers since integer arithmetic round toward
                 * zero. The xOffset and yOffset values are the remainding of the division which
                 * will be added to the width and height in order to get (xmax, ymax) unchanged.
                 */
                xOffset = regionToRead.x % subsampling.width;
                yOffset = regionToRead.y % subsampling.height;
                regionToRead.x /= subsampling.width;
                regionToRead.y /= subsampling.height;
                if (xOffset < 0) {
                    regionToRead.x--;
                    xOffset = subsampling.width - xOffset;
                }
                if (yOffset < 0) {
                    regionToRead.y--;
                    yOffset = subsampling.height - yOffset;
                }
                regionToRead.width  += xOffset;
                regionToRead.height += yOffset;
                regionToRead.width  /= subsampling.width;
                regionToRead.height /= subsampling.height;
                subsampling.width  = xSubsampling / subsampling.width;
                subsampling.height = ySubsampling / subsampling.height;
                final int tileIndex = tile.getImageIndex();
                if (table != null) {
                    /*
                     * We are only logging - we are not going to read in this first pass.
                     */
                    table.write(Tile.toString(tile.getImageReaderSpi()));
                    table.nextColumn();
                    table.write(tile.getInputName());
                    table.nextColumn();
                    table.write(String.valueOf(tileIndex));
                    format(table, regionToRead.width,  regionToRead.height);
                    format(table, regionToRead.x,      regionToRead.y);
                    format(table, destinationOffset.x, destinationOffset.y);
                    format(table, subsampling.width,   subsampling.height);
                    table.nextLine();
                    continue;
                }
                final ImageReader reader = tile.getImageReader(this, true, true);
                final ImageReadParam tileParam = mosaicParam.getCachedTileParameters(reader);
                tileParam.setDestinationType(null);
                tileParam.setDestination(image); // Must be after setDestinationType and may be null.
                tileParam.setDestinationOffset(destinationOffset);
                if (tileParam.canSetSourceRenderSize()) {
                    tileParam.setSourceRenderSize(null); // TODO.
                }
                tileParam.setSourceRegion(regionToRead);
                tileParam.setSourceSubsampling(subsampling.width, subsampling.height, 0, 0);
                if (controller != null) {
                    controller.configure(tile, tileParam);
                }
                final BufferedImage output;
                synchronized (this) {  // Same lock than ImageReader.abort()
                    reading = reader;
                }
                try {
                    output = reader.read(tileIndex, tileParam);
                } finally {
                    synchronized (this) {  // Same lock than ImageReader.abort()
                        reading = null;
                    }
                }
                if (image == null) {
                    image = output;
                } else if (output != image) {
                    /*
                     * The read operation ignored our destination image.  By default we treat that
                     * as an error since the SampleModel may be incompatible and changing it would
                     * break the geophysics meaning of pixel values. However if we are interrested
                     * only in the visual aspect, we can copy the data (slow, consumes memory) and
                     * let Java2D performs the required color conversions. Note that it should not
                     * occur anyway if we choose correctly the raw image type in the code above.
                     */
                    if (PRESERVE_DATA) {
                        throw new IIOException("Incompatible data format."); // TODO: localize
                    }
                    final AffineTransform at = AffineTransform.getTranslateInstance(
                            destinationOffset.x, destinationOffset.y);
                    final Graphics2D graphics = image.createGraphics();
                    graphics.drawRenderedImage(output, at);
                    graphics.dispose();
                }
            }
            /*
             * Finished a pass. If it was the reading pass, then we are done. If it was the logging
             * pass, then send the log and redo the look a second time for the actual reading.
             */
            if (table == null) {
                break;
            }
            table.writeHorizontalSeparator();
            final StringBuilder message = new StringBuilder();
            message.append('[').append(sourceRegion.x).append(',').append(sourceRegion.y).
                    append(" - ").append(sourceRegion.x + sourceRegion.width).append(',').
                    append(sourceRegion.y + sourceRegion.height).append(']');
            final String area = message.toString();
            message.setLength(0);
            message.append(Vocabulary.format(VocabularyKeys.LOADING_$1, area)).
                    append(System.getProperty("line.separator", "\n")).append(table);
            final LogRecord record = new LogRecord(level, message.toString());
            record.setSourceClassName(MosaicImageReader.class.getName());
            record.setSourceMethodName("read");
            record.setLoggerName(logger.getName());
            logger.log(record);
            table = null;
        } while (true);
        processImageComplete();
        return image;
    }

    /**
     * Reads the tile indicated by the {@code tileX} and {@code tileY} arguments.
     *
     * @param  imageIndex The index of the image to be retrieved.
     * @param  tileX The column index (starting with 0) of the tile to be retrieved.
     * @param  tileY The row index (starting with 0) of the tile to be retrieved.
     * @return The desired tile.
     * @throws IOException if an error occurs during reading.
     */
    @Override
    public BufferedImage readTile(final int imageIndex, final int tileX, final int tileY)
            throws IOException
    {
        final int width  = getTileWidth (imageIndex);
        final int height = getTileHeight(imageIndex);
        final Rectangle sourceRegion = new Rectangle(tileX*width, tileY*height, width, height);
        final ImageReadParam param = getDefaultReadParam();
        param.setSourceRegion(sourceRegion);
        return read(imageIndex, param);
    }

    /**
     * Formats a (x,y) value pair. A call to {@link TableWriter#nextColumn} is performed first.
     */
    private static void format(final TableWriter table, final int x, final int y) {
        table.nextColumn();
        table.write('(');
        table.write(String.valueOf(x));
        table.write(',');
        table.write(String.valueOf(y));
        table.write(')');
    }

    /**
     * Requests that any current read operation be aborted.
     */
    @Override
    public synchronized void abort() {
        super.abort();
        if (reading != null) {
            reading.abort();
        }
    }

    /**
     * Returns the raw input (<strong>not</strong> wrapped in an image input stream) for the
     * given reader. This method is invoked by {@link Tile#getImageReader} only.
     */
    final Object getRawInput(final ImageReader reader) {
        return readerInputs.get(reader);
    }

    /**
     * Sets the raw input (<strong>not</strong> wrapped in an image input stream) for the
     * given reader. The input can be set to {@code null}. This method is invoked by
     * {@link Tile#getImageReader} only.
     */
    final void setRawInput(final ImageReader reader, final Object input) {
        readerInputs.put(reader, input);
    }

    /**
     * Closes any image input streams thay may be held by tiles.
     * The streams will be opened again when they will be first needed.
     *
     * @throws IOException if error occured while closing a stream.
     */
    public void close() throws IOException {
        for (final Map.Entry<ImageReader,Object> entry : readerInputs.entrySet()) {
            final ImageReader reader = entry.getKey();
            final Object    rawInput = entry.getValue();
            final Object       input = reader.getInput();
            entry .setValue(null);
            reader.setInput(null);
            if (input != rawInput) {
                Tile.close(input);
            }
        }
    }

    /**
     * Allows any resources held by this reader to be released. The default implementation
     * closes any image input streams thay may be held by tiles, then disposes every
     * {@linkplain Tile#getImageReader tile image readers}.
     */
    @Override
    public void dispose() {
        input = null;
        try {
            close();
        } catch (IOException e) {
            Logging.unexpectedException(MosaicImageReader.class, "dispose", e);
        }
        readerInputs.clear();
        for (final ImageReader reader : readers.values()) {
            reader.dispose();
        }
        readers.clear();
        super.dispose();
    }

    /**
     * Service provider for {@link MosaicImageReader}.
     *
     * @since 2.5
     * @source $URL$
     * @version $Id$
     * @author Martin Desruisseaux
     */
    public static class Spi extends ImageReaderSpi {
        /**
         * The format names. This array is shared with {@link MosaicImageWriter.Spi}.
         */
        static final String[] NAMES = new String[] {
            "mosaic"
        };

        /**
         * The input types. This array is shared with {@link MosaicImageWriter.Spi}.
         */
        static final Class<?>[] INPUT_TYPES = new Class[] {
            TileManager[].class,
            TileManager.class,
            Tile[].class,
            Collection.class
        };

        /**
         * The default instance.
         */
        public static final Spi DEFAULT = new Spi();

        /**
         * Creates a default provider.
         */
        public Spi() {
            vendorName      = "GeoTools";
            version         = GeoTools.getVersion().toString();
            names           = NAMES;
            inputTypes      = INPUT_TYPES;
            pluginClassName = "org.geotools.image.io.mosaic.MosaicImageReader";
        }

        /**
         * Returns {@code true} if the image reader can decode the given input. The default
         * implementation returns {@code true} if the given object is non-null and an instance
         * of an {@linkplain #inputTypes input types}, or {@code false} otherwise.
         *
         * @throws IOException If an I/O operation was required and failed.
         */
        public boolean canDecodeInput(final Object source) throws IOException {
            if (source != null) {
                final Class<?> type = source.getClass();
                for (final Class<?> inputType : inputTypes) {
                    if (inputType.isAssignableFrom(type)) {
                        return true;
                    }
                }
            }
            return false;
        }

        /**
         * Returns a new {@link MosaicImageReader}.
         *
         * @throws IOException If an I/O operation was required and failed.
         */
        public ImageReader createReaderInstance(final Object extension) throws IOException {
            return new MosaicImageReader(this);
        }

        /**
         * Returns a brief, human-readable description of this service provider.
         *
         * @todo Localize.
         */
        public String getDescription(final Locale locale) {
            return "Mosaic Image Reader";
        }
    }
}
