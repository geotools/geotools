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
import java.awt.image.Raster;
import java.awt.image.DataBuffer;
import java.awt.image.SampleModel;
import java.awt.image.RenderedImage;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.Closeable;
import java.io.IOException;
import javax.imageio.*; // Lot of them in this class.
import javax.imageio.spi.IIORegistry;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.spi.ImageWriterSpi;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import java.util.*; // Lot of them in this class.
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.LogRecord;
import java.util.concurrent.Future;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ExecutionException;
import java.lang.reflect.UndeclaredThrowableException;

import org.geotools.factory.GeoTools;
import org.geotools.resources.XArray;
import org.geotools.resources.i18n.Errors;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Loggings;
import org.geotools.resources.i18n.LoggingKeys;
import org.geotools.resources.i18n.Vocabulary;
import org.geotools.resources.i18n.VocabularyKeys;
import org.geotools.resources.image.ImageUtilities;
import org.geotools.resources.IndexedResourceBundle;
import org.geotools.util.logging.Logging;


/**
 * An image writer that delegates to a mosaic of other image writers. The mosaic is specified as a
 * collection of {@link Tile} objects given to {@link #setOutput(Object)} method. While this class
 * can write a {@link RenderedImage}, the preferred approach is to invoke {@link #writeFromInput}
 * with a {@link File} input in argument. This approach is non-standard but often required since
 * images to mosaic are typically bigger than {@link RenderedImage} capacity.
 *
 * @since 2.5
 * @source $URL$
 * @version $Id$
 * @author Cédric Briançon
 * @author Martin Desruisseaux
 */
public class MosaicImageWriter extends ImageWriter {
    /**
     * The value for filling empty images. The value is fixed to 0 in current implementation
     * because this is the value of newly created image, and we do not fill them at this time.
     */
    private static final int FILL_VALUE = 0;

    /**
     * The logging level for tiling information during reads and writes.
     */
    private Level level = Level.FINE;

    /**
     * The executor to use for writting tiles in background thread.
     * Will be created when first needed.
     */
    private ExecutorService executor;

    /**
     * Constructs an image writer with the default provider.
     */
    public MosaicImageWriter() {
        this(null);
    }

    /**
     * Constructs an image writer with the specified provider.
     *
     * @param spi The service provider, or {@code null} for the default one.
     */
    public MosaicImageWriter(final ImageWriterSpi spi) {
        super(spi != null ? spi : Spi.DEFAULT);
    }

    /**
     * Returns the logging level for tile information during read and write operations.
     *
     * @return The current logging level.
     */
    public Level getLogLevel() {
        return level;
    }

    /**
     * Sets the logging level for tile information during read and write operations.
     * The default value is {@link Level#FINE}. A {@code null} value restore the default.
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
     * Returns the output, which is a an array of {@linkplain TileManager tile managers}.
     * The array length is the maximum number of images that can be inserted. The element
     * at index <var>i</var> is the tile manager to use when writing at image index <var>i</var>.
     */
    @Override
    public TileManager[] getOutput() {
        final TileManager[] managers = (TileManager[]) super.getOutput();
        return (managers != null) ? managers.clone() : null;
    }

    /**
     * Sets the output, which is expected to be an array of {@linkplain TileManager tile managers}.
     * If the given input is a singleton, an array or a {@linkplain Collection collection} of
     * {@link Tile} objects, then it will be wrapped in an array of {@link TileManager}s.
     *
     * @param  output The output.
     * @throws IllegalArgumentException if {@code output} is not an instance of one of the
     *         expected classes, or if the output can not be used because of an I/O error
     *         (in which case the exception has a {@link IOException} as its
     *         {@linkplain IllegalArgumentException#getCause cause}).
     */
    @Override
    public void setOutput(final Object output) throws IllegalArgumentException {
        final TileManager[] managers;
        try {
            managers = TileManagerFactory.DEFAULT.createFromObject(output);
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getLocalizedMessage(), e);
        }
        super.setOutput(managers);
    }

    /**
     * Returns default parameters appropriate for this format.
     */
    @Override
    public MosaicImageWriteParam getDefaultWriteParam() {
        return new MosaicImageWriteParam();
    }

    /**
     * Writes the specified image as a set of tiles. The default implementation copies the image in
     * a temporary file, then invokes {@link #writeFromInput}. This somewhat inefficient approach
     * may be changed in a future version.
     *
     * @param  metadata The stream metadata.
     * @param  image    The image to write.
     * @param  param    The parameter for the image to write.
     * @throws IOException if an error occured while writing the image.
     */
    public void write(final IIOMetadata metadata, final IIOImage image, ImageWriteParam param)
            throws IOException
    {
        /*
         * We could check for 'output' before to create the temporary file in order to avoid
         * creating the file if we are going to fail anyway,  but we don't because users are
         * allowed to override the 'filter' methods and set the output there (undocumented
         * but possible, and TileBuilder do something like that).
         *
         * Uses the PNG format, which is lossless and bundled in standard Java distributions.
         */
        final Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("png");
        while (writers.hasNext()) {
            final ImageWriter writer = writers.next();
            if (!filter(writer)) {
                continue;
            }
            final File file = File.createTempFile("MIW", ".png");
            try {
                final ImageOutputStream output = ImageIO.createImageOutputStream(file);
                writer.setOutput(output);
                writer.write(metadata, image, param);
                output.close();
                /*
                 * We don't want to take in account parameters like source region, subsampling, etc.
                 * since they were already handled by the writing process above. But we want to take
                 * in account the parameters specific to MosaicImageWriteParam. So retain only them.
                 */
                if (param instanceof MosaicImageWriteParam) {
                    param = new MosaicImageWriteParam((MosaicImageWriteParam) param);
                } else {
                    param = null;
                }
                writeFromInput(file, 0, param);
            } finally {
                file.delete();
            }
            return;
        }
        throw new IIOException(Errors.format(ErrorKeys.NO_IMAGE_WRITER));
    }

    /**
     * Reads the image from the given input and writes it as a set of tiles. The input is typically
     * a {@link File} object, but other kind of inputs may be accepted depending on available image
     * readers. The output files and tiling layout can be specified as a collection of {@link Tile}
     * objects given to {@link #setOutput(Object)} method.
     *
     * @param  input The image input, typically as a {@link File}.
     * @param  inputIndex The image index to read from the given input file.
     * @param  param The write parameters, or {@code null} for the default.
     * @return {@code true} on success, or {@code false} if the process has been aborted.
     * @throws IOException If an error occured while reading or writing.
     *
     * @todo Current implementation do not yet supports source region and subsampling settings.
     *       An exception will be thrown if any of those parameters are set.
     */
    public boolean writeFromInput(final Object input, final int inputIndex,
                                  final ImageWriteParam param) throws IOException
    {
        final boolean success;
        final ImageReader reader = getImageReader(input);
        try {
            success = writeFromReader(reader, inputIndex, param);
            close(reader.getInput(), input);
        } finally {
            reader.dispose();
        }
        return success;
    }

    /**
     * Reads the image from the given reader and writes it as a set of tiles.
     * It is the caller responsability to dispose the reader when writing is done.
     */
    private boolean writeFromReader(final ImageReader reader, final int inputIndex,
                                    final ImageWriteParam writeParam) throws IOException
    {
        clearAbortRequest();
        final int outputIndex;
        final TileWritingPolicy policy;
        if (writeParam instanceof MosaicImageWriteParam) {
            final MosaicImageWriteParam param = (MosaicImageWriteParam) writeParam;
            outputIndex = param.getOutputIndex();
            policy = param.getTileWritingPolicy();
        } else {
            outputIndex = 0;
            policy = TileWritingPolicy.OVERWRITE;
        }
        processImageStarted(outputIndex);
        /*
         * Gets the reader first - especially before getOutput() - because the user may have
         * overriden filter(ImageReader) and set the output accordingly. TileBuilder do that.
         */
        final TileManager[] managers = getOutput();
        if (managers == null) {
            throw new IllegalStateException(Errors.format(ErrorKeys.NO_IMAGE_OUTPUT));
        }
        final List<Tile> tiles;
        final int bytesPerPixel;
        if (policy.equals(TileWritingPolicy.NO_WRITE)) {
            tiles = Collections.emptyList();
            bytesPerPixel = 1;
        } else {
            tiles = new LinkedList<Tile>(managers[outputIndex].getTiles());
            /*
             * Computes an estimation of the amount of memory to be required for each pixel.
             * This estimation may not be accurate especially for image packing many pixels
             * per byte, but a value too high is probably better than a value too low.
             */
            final SampleModel model = reader.getRawImageType(inputIndex).getSampleModel();
            bytesPerPixel = Math.max(1, model.getNumBands() *
                    DataBuffer.getDataTypeSize(model.getDataType()) / Byte.SIZE);
        }
        final int initialTileCount = tiles.size();
        /*
         * If the user do not wants to overwrite existing tiles (for faster processing when this
         * write process is started again after a previous failure), removes from the collection
         * every tiles which already exist.
         */
        if (!policy.overwrite) {
            for (final Iterator<Tile> it=tiles.iterator(); it.hasNext();) {
                final Tile tile = it.next();
                final Object input = tile.getInput();
                if (input instanceof File) {
                    final File file = (File) input;
                    if (file.isFile()) {
                        it.remove();
                    }
                }
            }
        }
        /*
         * Creates now the various other objects to be required in the loop. This include a
         * RTree initialized with the tiles remaining after the removal in the previous block.
         */
        if (executor == null) {
            executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        }
        final List<Future<?>> tasks     = new ArrayList<Future<?>>();
        final TreeNode        tree      = new GridNode(tiles.toArray(new Tile[tiles.size()]));
        final ImageReadParam  readParam = reader.getDefaultReadParam();
        final Logger          logger    = Logging.getLogger(MosaicImageWriter.class);
        final boolean         logWrites = logger.isLoggable(level);
        final boolean         logReads  = !(reader instanceof MosaicImageReader);
        if (!logReads) {
            ((MosaicImageReader) reader).setLogLevel(level);
        }
        if (writeParam != null) {
            if (writeParam.getSourceXSubsampling() != 1 || writeParam.getSubsamplingXOffset() != 0 ||
                writeParam.getSourceYSubsampling() != 1 || writeParam.getSubsamplingYOffset() != 0 ||
                writeParam.getSourceRegion() != null)
            {
                // TODO: Not yet supported. May be supported in a future version
                // if we have time to implement such support.
                throw new IllegalArgumentException(Errors.format(
                        ErrorKeys.UNEXPECTED_ARGUMENT_FOR_INSTRUCTION_$1, "writeFromInput"));
            }
            readParam.setSourceBands(writeParam.getSourceBands());
        }
        final long maximumMemory = getMaximumMemoryAllocation();
        int maximumPixelCount = (int) (maximumMemory / bytesPerPixel);
        BufferedImage image = null;
        while (!tiles.isEmpty()) {
            if (abortRequested()) {
                processWriteAborted();
                return false;
            }
            processImageProgress((initialTileCount - tiles.size()) * 100f / initialTileCount);
            /*
             * Gets the source region for some initial tile from the list. We will write as many
             * tiles as we can using a single image. The tiles successfully written will be removed
             * from the list, so next iterations will process only the remaining tiles.
             */
            final Dimension imageSubsampling = new Dimension(); // Computed by next line.
            final Tile imageTile = getEnclosingTile(tiles, tree, imageSubsampling,
                    (image != null) ? new Dimension(image.getWidth(), image.getHeight()) : null,
                    maximumPixelCount);
            final Rectangle imageRegion = imageTile.getAbsoluteRegion();
            awaitTermination(tasks); // Must be invoked before we touch to the image.
            if (image != null) {
                final int width  = imageRegion.width  / imageSubsampling.width;
                final int height = imageRegion.height / imageSubsampling.height;
                if (width == image.getWidth() && height == image.getHeight()) {
                    ImageUtilities.fill(image, FILL_VALUE);
                    assert isEmpty(image, new Rectangle(image.getWidth(), image.getHeight()));
                } else {
                    image = null;
                }
                /*
                 * Next iteration may try to allocate bigger images. We do that inconditionnaly,
                 * even if current image fit, because current image may be small due to memory
                 * constaint during a previous iteration.
                 */
                maximumPixelCount = (int) (maximumMemory / bytesPerPixel);
            }
            readParam.setDestination(image);
            readParam.setSourceRegion(imageRegion);
            readParam.setSourceSubsampling(imageSubsampling.width, imageSubsampling.height, 0, 0);
            if (readParam instanceof MosaicImageReadParam) {
                ((MosaicImageReadParam) readParam).setNullForEmptyImage(true);
            }
            if (logReads) {
                final LogRecord record = getLogRecord(false, VocabularyKeys.LOADING_$1, imageTile);
                record.setLoggerName(logger.getName());
                logger.log(record);
            }
            /*
             * Before to attempt image loading, ask explicitly for a garbage collection cycle.
             * In theory we should not do that, but experience suggests that it really prevent
             * OutOfMemoryError when creating large images. If we still get OutOfMemoryError,
             * we will try again with smaller value of 'maximumPixelCount'.
             */
            System.gc();
            /*
             * Now process to the image loading. If we fails with an OutOfMemoryError (which
             * typically happen while creating the large BufferedImage), reduces the amount
             * of memory that we are allowed to use and try again.
             */
            try {
                image = reader.read(inputIndex, readParam);
            } catch (OutOfMemoryError error) {
                maximumPixelCount >>>= 1;
                if (maximumPixelCount == 0) {
                    throw error;
                }
                if (logWrites) {
                    final LogRecord record = getLogRecord(true, LoggingKeys.RECOVERABLE_OUT_OF_MEMORY_$1,
                            ((float) imageRegion.width * imageRegion.height) / (1024 * 1024f));
                    record.setLoggerName(logger.getName());
                    logger.log(record);
                }
                continue;
            }
            assert (image == null) || // Image can be null if MosaicImageReader found no tiles.
                   (image.getWidth()  * imageSubsampling.width  == imageRegion.width &&
                    image.getHeight() * imageSubsampling.height == imageRegion.height) : imageTile;
            /*
             * Searchs tiles inside the same region with a resolution which is equals or lower by
             * an integer ratio. If such tiles are found we can write them using the image loaded
             * above instead of loading subimages.  Giving that loading even a portion from a big
             * file may be long, the performance enhancement of doing so is significant.
             */
            assert tiles.contains(imageTile) : imageTile;
            for (final Iterator<Tile> it=tiles.iterator(); it.hasNext();) {
                final Tile tile = it.next();
                final Dimension subsampling = tile.getSubsampling();
                if (!isDivisor(subsampling, imageSubsampling)) {
                    continue;
                }
                final Rectangle sourceRegion = tile.getAbsoluteRegion();
                if (!imageRegion.contains(sourceRegion)) {
                    continue;
                }
                final int xSubsampling = subsampling.width  / imageSubsampling.width;
                final int ySubsampling = subsampling.height / imageSubsampling.height;
                sourceRegion.translate(-imageRegion.x, -imageRegion.y);
                sourceRegion.x      /= imageSubsampling.width;
                sourceRegion.y      /= imageSubsampling.height;
                sourceRegion.width  /= imageSubsampling.width;
                sourceRegion.height /= imageSubsampling.height;
                if (image != null && (policy.includeEmpty || !isEmpty(image, sourceRegion))) {
                    final ImageWriter writer = getImageWriter(tile, image);
                    final ImageWriteParam wp = writer.getDefaultWriteParam();
                    onTileWrite(tile, wp);
                    wp.setSourceRegion(sourceRegion);
                    wp.setSourceSubsampling(xSubsampling, ySubsampling, 0, 0);
                    final IIOImage iioImage = new IIOImage(image, null, null);
                    final Object tileInput = tile.getInput();
                    tasks.add(executor.submit(new Callable<Object>() {
                        public Object call() throws IOException {
                            if (!abortRequested()) {
                                if (logWrites) {
                                    final LogRecord record =
                                            getLogRecord(false, VocabularyKeys.SAVING_$1, tile);
                                    record.setLoggerName(logger.getName());
                                    logger.log(record);
                                }
                                boolean success = false;
                                try {
                                    writer.write(null, iioImage, wp);
                                    close(writer.getOutput(), tileInput);
                                    success = true;
                                } finally {
                                    if (success) {
                                        writer.dispose();
                                    } else {
                                        final Object output = writer.getOutput();
                                        writer.dispose();
                                        close(output, null); // Unconditional close.
                                        if (tileInput instanceof File) {
                                            ((File) tileInput).delete();
                                        }
                                    }
                                }
                            }
                            return null;
                        }
                    }));
                }
                it.remove();
                if (!tree.remove(tile)) {
                    throw new AssertionError(tile); // Should never happen.
                }
            }
            assert !tiles.contains(imageTile) : imageTile;
        }
        awaitTermination(tasks);
        if (abortRequested()) {
            processWriteAborted();
            return false;
        } else {
            processImageComplete();
            return true;
        }
    }

    /**
     * Closes the given stream if it is different than the user object. When different, the
     * output is not the {@link File} (or whatever object) given by {@link Tile}. It is probably
     * an {@link ImageOutputStream} created by {@link #getImageWriter}, so we need to close it.
     */
    private static void close(final Object stream, final Object user) throws IOException {
        if (stream != user) {
            if (stream instanceof Closeable) {
                ((Closeable) stream).close();
            } else if (stream instanceof ImageInputStream) {
                ((ImageInputStream) stream).close();
            }
            // Note: ImageOutputStream extends ImageInputStream, so the above check is suffisient.
        }
    }

    /**
     * Waits for every tasks to be completed. The tasks collection is emptied by this method.
     *
     * @throws IOException if at least one task threw a {@code IOException}.
     */
    private final void awaitTermination(final List<Future<?>> tasks) throws IOException {
        Throwable exception = null;
        for (int i=0; i<tasks.size(); i++) {
            Future<?> task = tasks.get(i);
            try {
                task.get();
                continue;
            } catch (ExecutionException e) {
                if (exception == null) {
                    exception = e.getCause();
                }
                // Abort after the catch block.
            } catch (InterruptedException e) {
                // Abort after the catch block.
            }
            abort();
            for (int j=tasks.size(); --j>i;) {
                task = tasks.get(j);
                if (task.cancel(false)) {
                    tasks.remove(j);
                }
            }
        }
        tasks.clear();
        if (exception != null) {
            if (exception instanceof IOException) {
                throw (IOException) exception;
            }
            if (exception instanceof RuntimeException) {
                throw (RuntimeException) exception;
            }
            if (exception instanceof Error) {
                throw (Error) exception;
            }
            throw new UndeclaredThrowableException(exception);
        }
    }

    /**
     * Returns a log message for the given tile.
     */
    private LogRecord getLogRecord(final boolean log, final int key, final Object arg) {
        final IndexedResourceBundle bundle = log ?
                Loggings.getResources(locale) : Vocabulary.getResources(locale);
        final LogRecord record = bundle.getLogRecord(level, key, arg);
        record.setSourceClassName(MosaicImageWriter.class.getName());
        record.setSourceMethodName("writeFromInput");
        return record;
    }

    /**
     * Returns a tile which enclose other tiles at finer resolution. Some tile layouts have a few
     * big tiles with low resolution covering the same geographic area than many smaller tiles with
     * finer resolution. If such overlapping is found, then this method returns one of the big tiles
     * and sets {@code imageSubsampling} to some subsampling that may be finer than usual for the
     * returned tile. Reading the big tile with that subsampling allows {@code MosaicImageWriter}
     * to use the same {@link RenderedImage} for writing both the big tile and the finer ones.
     * Example:
     *
     * <blockquote>
     *   +-----------------------+          +-----------+-----------+
     *   |                       |          |Subsampling|Subsampling|
     *   |      Subsampling      |          |  = (2,2)  |  = (2,2)  |
     *   |        = (4,4)        |          +-----------+-----------+
     *   |                       |          |Subsampling|Subsampling|
     *   |                       |          |  = (2,2)  |  = (2,2)  |
     *   +-----------------------+          +-----------+-----------+
     * </blockquote>
     *
     * Given the above, this method will returns the tile illustrated on the left side and set
     * {@code imageSubsampling} to (2,2).
     * <p>
     * The algorithm implemented in this method is far from optimal and surely doesn't return
     * the best tile in all case. It is a compromize attempting to reduce the amount of image
     * data to load without too much CPU cost.
     *
     * @param tiles The tiles to examine. This collection is not modified.
     * @param tree Same as {@code tiles}, but as a tree for faster searchs.
     * @param imageSubsampling Where to store the subsampling to use for reading the tile.
     *        This is an output parameter only.
     */
    private static Tile getEnclosingTile(final List<Tile> tiles, final TreeNode tree,
            final Dimension imageSubsampling, Dimension preferredSize, final int maximumPixelCount)
            throws IOException
    {
        if (preferredSize != null) {
            final int area = preferredSize.width * preferredSize.height;
            if (area < maximumPixelCount / 2) {
                // The image size was small, probably due to memory constraint in a previous
                // iteration. Allow this method to try more aggresive memory usage.
                preferredSize = null;
            }
        }
        final Set<Dimension> subsamplingDone = tiles.size() > 24 ? new HashSet<Dimension>() : null;
        boolean selectedHasPreferredSize = false;
        int     selectedCount = 0;
        Tile    selectedTile  = null;
        Tile    fallbackTile  = null; // Used only if we failed to select a tile.
        long    fallbackArea  = Long.MAX_VALUE;
        assert tree.containsAll(tiles);
search: for (final Tile tile : tiles) {
            /*
             * Gets the collection of tiles in the same area than the tile we are examinating.
             * We will retain the tile with the largest filtered collection. Filtering will be
             * performed only if there is some chance to get a larger collection than the most
             * sucessful one so far.
             */
            final Rectangle region = tile.getAbsoluteRegion();
            final Dimension subsampling = tile.getSubsampling();
            if (subsamplingDone != null && !subsamplingDone.add(subsampling)) {
                /*
                 * In order to speedup this method, examine only one tile for each subsampling
                 * value. This is a totally arbitrary choice but work well for the most common
                 * tile layouts (constant area & constant size). Without such reduction, the
                 * execution time of this method is too long for large tile collections. For
                 * smaller collections, we can afford a systematic examination of all tiles.
                 */
                continue;
            }
            // Reminder: Collection in next line will be modified, so it needs to be mutable.
            final Collection<Tile> enclosed = tree.containedIn(region);
            assert enclosed.contains(tile) : tile;
            if (enclosed.size() <= selectedCount) {
                assert selectedTile != null : selectedCount;
                continue; // Already a smaller collection - no need to do more in this iteration.
            }
            /*
             * Found a collection that may be larger than the most successful one so far. First,
             * gets the smallest subsampling. We will require tiles at the finest resolution to
             * be written in the first pass before to go up in the pyramid. If they were written
             * last, those small tiles would be read one-by-one, which defeat the purpose of this
             * method (to read a bunch of tiles at once).
             */
            Dimension finestSubsampling = subsampling;
            int smallestPixelArea = subsampling.width * subsampling.height;
            for (final Tile subtile : enclosed) {
                final Dimension s = subtile.getSubsampling();
                final int pixelArea = s.width * s.height;
                if (pixelArea < smallestPixelArea) {
                    smallestPixelArea = pixelArea;
                    finestSubsampling = s;
                }
            }
            long area = (long) region.width * (long) region.height;
            area /= smallestPixelArea;
            if (area > maximumPixelCount) {
                if (area < fallbackArea) {
                    fallbackArea = area;
                    fallbackTile = tile;
                }
                continue;
            }
            /*
             * Found a subsampling that may be finer than the tile subsampling. Now removes
             * every tiles which would consume too much memory if we try to read them using
             * that subsampling. If the tiles to be removed are the finest ones, search for
             * an other tile to write because we really want the finest resolution to be
             * written first (see previous comment).
             */
            for (final Iterator<Tile> it=enclosed.iterator(); it.hasNext();) {
                final Tile subtile = it.next();
                final Dimension s = subtile.getSubsampling();
                if (!isDivisor(subsampling, s)) {
                    it.remove();
                    if (s.equals(finestSubsampling)) {
                        continue search;
                    }
                    continue;
                }
                final Rectangle subregion = subtile.getAbsoluteRegion();
                area = (long) subregion.width * (long) subregion.height;
                area /= smallestPixelArea;
                if (area > maximumPixelCount) {
                    it.remove();
                    if (s.equals(finestSubsampling)) {
                        continue search;
                    }
                }
            }
            /*
             * Retains the tile with the largest filtered collection of sub-tiles.
             * A special case is made for tile having the preferred size, in order
             * to recycle the existing BufferedImage.
             */
            final int tileCount = enclosed.size();
            final boolean isPreferredSize = (preferredSize != null) &&
                    region.width  / finestSubsampling.width  == preferredSize.width &&
                    region.height / finestSubsampling.height == preferredSize.height;
            if (selectedTile == null || tileCount > selectedCount ||
                    (isPreferredSize && !selectedHasPreferredSize))
            {
                selectedTile = tile;
                selectedCount = tileCount;
                selectedHasPreferredSize = isPreferredSize;
                imageSubsampling.setSize(finestSubsampling);
            }
        }
        /*
         * The selected tile may still null if 'maximumPixelCount' is so small than even the
         * smallest tile doesn't fit. We will return the smallest tile anyway, maybe letting
         * a OutOfMemoryError to occurs in the caller if really the tile can't hole in the
         * available memory. We perform this try anyway because estimation of available memory
         * in Java is only approximative.
         */
        if (selectedTile == null) {
            selectedTile = fallbackTile;
            imageSubsampling.setSize(fallbackTile.getSubsampling());
        }
        return selectedTile;
    }

    /**
     * Returns {@code true} if the given denominator is a divisor of the given numerator
     * for both {@linkplain Dimension#width width} and {@linkplain Dimension#height height}.
     */
    private static boolean isDivisor(final Dimension numerator, final Dimension denominator) {
        return (numerator.width  % denominator.width ) == 0 &&
               (numerator.height % denominator.height) == 0;
    }

    /**
     * Returns the maximal amount of memory that {@link #writeFromInput writeFromInput} is allowed
     * to use. The default implementation computes a value from the amount of memory available in
     * the current JVM. Subclasses can override this method for returning a different value.
     * <p>
     * The returned value will be considered on a <cite>best effort</cite> basis. There is no
     * garantee that no more memory than the returned value will be used.
     *
     * @return An estimation of the maximum amount of memory allowed for allocation, in bytes.
     */
    public long getMaximumMemoryAllocation() {
        final Runtime runtime = Runtime.getRuntime();
        runtime.gc();
        final long usedMemory = runtime.totalMemory() - runtime.freeMemory();
        return Math.min(128L*1024*1024, runtime.maxMemory() - 2*usedMemory);
    }

    /**
     * Invoked after {@code MosaicImageWriter} has created a reader and
     * {@linkplain ImageReader#setInput(Object) set the input}. Users can override this method
     * for performing additional configuration and may returns {@code false} if the given reader
     * is not suitable. The default implementation returns {@code true} in all case.
     *
     * @param  reader The image reader created and configured by {@code MosaicImageWriter}.
     * @return {@code true} If the given reader is ready for use, or {@code false} if an other
     *         reader should be fetched.
     * @throws IOException if an error occured while inspecting or configuring the reader.
     */
    protected boolean filter(final ImageReader reader) throws IOException {
        return true;
    }

    /**
     * Invoked after {@code MosaicImageWriter} has created a writer and
     * {@linkplain ImageWriter#setOutput(Object) set the output}. Users can override this method
     * for performing additional configuration and may returns {@code false} if the given writer
     * is not suitable. The default implementation returns {@code true} in all case.
     *
     * @param  writer The image writer created and configured by {@code MosaicImageWriter}.
     * @return {@code true} If the given writer is ready for use, or {@code false} if an other
     *         writer should be fetched.
     * @throws IOException if an error occured while inspecting or configuring the writer.
     */
    protected boolean filter(final ImageWriter writer) throws IOException {
        return true;
    }

    /**
     * Returns {@code true} if the given region in the given image contains only fill values.
     *
     * @param image The image to test.
     * @param region The region in the image to test.
     */
    private boolean isEmpty(final BufferedImage image, final Rectangle region) {
        int[] data = null;
        final Raster raster = image.getRaster();
        final int xmax = region.x + region.width;
        final int ymax = region.y + region.height;
        for (int y=region.y; y<ymax; y++) {
            for (int x=region.x; x<xmax; x++) {
                data = raster.getPixel(x, y, data);
                for (int i=data.length; --i>=0;) {
                    if (data[i] != FILL_VALUE) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Invoked automatically when a tile is about to be written. The default implementation does
     * nothing. Subclasses can override this method in order to set custom write parameters.
     * <p>
     * The {@linkplain ImageWriteParam#setSourceRegion source region} and
     * {@linkplain ImageWriteParam#setSourceSubsampling source subsampling} parameters can not be
     * set through this method. Their setting will be overwritten by the caller because their
     * values depend on the strategy choosen by {@code MosaicImageWriter} for reading images,
     * which itself depends on the amount of available memory.
     *
     * @param  tile The tile to be written.
     * @param  parameters The parameters to be given to the {@linkplain ImageWriter image writer}.
     *         This method is allowed to change the parameter values.
     * @throws IOException if an I/O operation was required and failed.
     */
    protected void onTileWrite(final Tile tile, ImageWriteParam parameters) throws IOException {
    }

    /**
     * Gets and initializes an {@linkplain ImageReader image reader} that can decode the specified
     * input. The returned reader has its {@linkplain ImageReader#setInput input} already set. If
     * the reader input is different than the specified one, then it is probably an {@linkplain
     * ImageInputStream image input stream} and closing it is caller's responsability.
     *
     * @param  input The input to read.
     * @return The image reader that seems to be the most appropriated (never {@code null}).
     * @throws IOException If no suitable image reader has been found or if an error occured
     *         while creating an image reader or initiazing it.
     */
    private ImageReader getImageReader(final Object input) throws IOException {
        /*
         * First check if the input type is one of those accepted by MosaicImageReader.  We
         * perform this check in a dedicaced code since MosaicImageReader is not registered
         * as a SPI, because it does not comply to Image I/O contract which requires that
         * readers accept ImageInputStream.
         */
        for (final Class<?> type : MosaicImageReader.Spi.INPUT_TYPES) {
            if (type.isInstance(input)) {
                final ImageReader reader = new MosaicImageReader();
                reader.setInput(input);
                if (filter(reader)) {
                    return reader;
                }
                break;
            }
        }
        ImageInputStream stream = null;
        boolean createStream = false;
        /*
         * The following loop will be executed at most twice. The first iteration tries the given
         * input directly. The second iteration tries the input wrapped in an ImageInputStream.
         */
        do {
            final Object candidate;
            if (createStream) {
                stream = ImageIO.createImageInputStream(input);
                if (stream == null) {
                    continue;
                }
                candidate = stream;
            } else {
                candidate = input;
            }
            final Iterator<ImageReader> readers = ImageIO.getImageReaders(candidate);
            while (readers.hasNext()) {
                final ImageReader reader = readers.next();
                reader.setInput(candidate);
                // If there is any more advanced check to perform, we should do it here.
                // For now we accept the reader inconditionnaly.
                if (filter(reader)) {
                    return reader;
                }
                reader.dispose();
            }
        } while ((createStream = !createStream) == true);
        if (stream != null) {
            stream.close();
        }
        throw new IIOException(Errors.format(ErrorKeys.NO_IMAGE_READER));
    }

    /**
     * Gets and initializes an {@linkplain ImageWriter image writer} that can encode the specified
     * image. The returned writer has its {@linkplain ImageWriter#setOutput output} already set.
     * If the output is different than the {@linkplain Tile#getInput tile input}, then it is
     * probably an {@linkplain ImageOutputStream image output stream} and closing it is caller's
     * responsability.
     * <p>
     * This method must returns a new instance. We are not allowed to cache and recycle writers,
     * because more than one writer may be used simultaneously.
     *
     * @param  tile The tile to encode.
     * @param  image The image associated to the specified tile.
     * @return The image writer that seems to be the most appropriated (never {@code null}).
     * @throws IOException If no suitable image writer has been found or if an error occured
     *         while creating an image writer or initiazing it.
     */
    private ImageWriter getImageWriter(final Tile tile, final RenderedImage image)
            throws IOException
    {
        // Note: we rename "Tile.input" as "output" because we want to write in it.
        final Object         output      = tile.getInput();
        final Class<?>       outputType  = output.getClass();
        final ImageReaderSpi readerSpi   = tile.getImageReaderSpi();
        final String[]       formatNames = readerSpi.getFormatNames();
        final String[]       spiNames    = readerSpi.getImageWriterSpiNames();
        ImageOutputStream    stream      = null; // Created only if needed.
        /*
         * The search will be performed at most twice. In the first try (code below) we check the
         * plugins specified in 'spiNames' since we assume that they will encode the image in the
         * best suited format for the reader. In the second try (to be run if the first one found
         * no suitable writer), we look for providers by their name.
         */
        if (spiNames != null) {
            final IIORegistry registry = IIORegistry.getDefaultInstance();
            final ImageWriterSpi[] providers = new ImageWriterSpi[spiNames.length];
            int count = 0;
            for (final String name : spiNames) {
                final Class<?> spiType;
                try {
                    spiType = Class.forName(name);
                } catch (ClassNotFoundException e) {
                    // May be normal.
                    continue;
                }
                /*
                 * For each image writers, checks if at least one format name is an expected one
                 * and check if the writer can encode the image. If a suitable writter is found
                 * and is capable to encode the output, returns it immediately. Otherwise the
                 * writers are stored in an array as we found it, in order to try them again with
                 * an ImageOutputStream after this loop.
                 */
                final Object candidate = registry.getServiceProviderByClass(spiType);
                if (candidate instanceof ImageWriterSpi) {
                    final ImageWriterSpi spi = (ImageWriterSpi) candidate;
                    final String[] names = spi.getFormatNames();
                    if (XArray.intersects(formatNames, names) && spi.canEncodeImage(image)) {
                        providers[count++] = spi;
                        for (final Class<?> legalType : spi.getOutputTypes()) {
                            if (legalType.isAssignableFrom(outputType)) {
                                final ImageWriter writer = spi.createWriterInstance();
                                writer.setOutput(output);
                                if (filter(writer)) {
                                    return writer;
                                }
                                writer.dispose();
                                break;
                            }
                        }
                    }
                }
            }
            /*
             * No provider accepts the output directly. This output is typically a File or URL.
             * Creates an image output stream from it and try again.
             */
            if (count != 0) {
                stream = ImageIO.createImageOutputStream(output);
                if (stream != null) {
                    final Class<? extends ImageOutputStream> streamType = stream.getClass();
                    for (int i=0; i<count; i++) {
                        final ImageWriterSpi spi = providers[i];
                        for (final Class<?> legalType : spi.getOutputTypes()) {
                            if (legalType.isAssignableFrom(streamType)) {
                                final ImageWriter writer = spi.createWriterInstance();
                                writer.setOutput(stream);
                                if (filter(writer)) {
                                    return writer;
                                }
                                writer.dispose();
                                break;
                            }
                        }
                    }
                }
            }
        }
        /*
         * No suitable writer found from 'spiNames'. Try again using format name.
         * At the difference of the previous try, this one works on ImageWriters
         * instead of ImageWriterSpi instances.
         */
        for (final String name : formatNames) {
            final List<ImageWriter> writers = new ArrayList<ImageWriter>();
            final Iterator<ImageWriter> it = ImageIO.getImageWritersByFormatName(name);
            while (it.hasNext()) {
                final ImageWriter writer = it.next();
                final ImageWriterSpi spi = writer.getOriginatingProvider();
                if (spi == null || !spi.canEncodeImage(image)) {
                    writer.dispose();
                    continue;
                }
                writers.add(writer);
                for (final Class<?> legalType : spi.getOutputTypes()) {
                    if (legalType.isAssignableFrom(outputType)) {
                        writer.setOutput(output);
                        if (filter(writer)) {
                            return writer;
                        }
                        // Do not dispose the writer since we will try it again later.
                        break;
                    }
                }
            }
            if (!writers.isEmpty()) {
                if (stream == null) {
                    stream = ImageIO.createImageOutputStream(output);
                    if (stream == null) {
                        break;
                    }
                }
                final Class<? extends ImageOutputStream> streamType = stream.getClass();
                for (final ImageWriter writer : writers) {
                    final ImageWriterSpi spi = writer.getOriginatingProvider();
                    for (final Class<?> legalType : spi.getOutputTypes()) {
                        if (legalType.isAssignableFrom(streamType)) {
                            writer.setOutput(stream);
                            if (filter(writer)) {
                                return writer;
                            }
                            break;
                        }
                    }
                    writer.dispose();
                }
            }
        }
        if (stream != null) {
            stream.close();
        }
        throw new IIOException(Errors.format(ErrorKeys.NO_IMAGE_WRITER));
    }

    /**
     * Returns the default stream metadata, or {@code null} if none.
     * The default implementation returns {@code null} in all cases.
     */
    public IIOMetadata getDefaultStreamMetadata(ImageWriteParam param) {
        return null;
    }

    /**
     * Returns the default image metadata, or {@code null} if none.
     * The default implementation returns {@code null} in all cases.
     */
    public IIOMetadata getDefaultImageMetadata(ImageTypeSpecifier imageType, ImageWriteParam param) {
        return null;
    }

    /**
     * Returns stream metadata initialized to the specified state, or {@code null}.
     * The default implementation returns {@code null} in all cases since this plugin
     * doesn't provide metadata encoding capabilities.
     */
    public IIOMetadata convertStreamMetadata(IIOMetadata inData, ImageWriteParam param) {
        return null;
    }

    /**
     * Returns image metadata initialized to the specified state, or {@code null}.
     * The default implementation returns {@code null} in all cases since this plugin
     * doesn't provide metadata encoding capabilities.
     */
    public IIOMetadata convertImageMetadata(IIOMetadata inData, ImageTypeSpecifier imageType, ImageWriteParam param) {
        return null;
    }

    /**
     * Disposes resources held by this writter. This method should be invoked when this
     * writer is no longer in use, in order to release some threads created by the writer.
     */
    @Override
    public void dispose() {
        if (executor != null) {
            executor.shutdown();
            executor = null;
        }
        super.dispose();
    }

    // It is not strictly necessary to override finalize() since ThreadPoolExecutor
    // already invokes shutdown() in its own finalize() method.

    /**
     * Service provider for {@link MosaicImageWriter}.
     *
     * @since 2.5
     * @source $URL$
     * @version $Id$
     * @author Cédric Briançon
     * @author Martin Desruisseaux
     */
    public static class Spi extends ImageWriterSpi {
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
            names           = MosaicImageReader.Spi.NAMES;
            outputTypes     = MosaicImageReader.Spi.INPUT_TYPES;
            pluginClassName = "org.geotools.image.io.mosaic.MosaicImageWriter";
        }

        /**
         * Returns {@code true} if this writer is likely to be able to encode images with the given
         * layout. The default implementation returns {@code true} in all cases. The capability to
         * encode images depends on the tile format specified in {@link Tile} objects, which are
         * not known to this provider.
         */
        public boolean canEncodeImage(ImageTypeSpecifier type) {
            return true;
        }

        /**
         * Returns a new {@link MosaicImageWriter}.
         *
         * @throws IOException If an I/O operation was required and failed.
         */
        public ImageWriter createWriterInstance(Object extension) throws IOException {
            return new MosaicImageWriter(this);
        }

        /**
         * Returns a brief, human-readable description of this service provider.
         *
         * @todo Localize.
         */
        public String getDescription(final Locale locale) {
            return "Mosaic Image Writer";
        }
    }
}
