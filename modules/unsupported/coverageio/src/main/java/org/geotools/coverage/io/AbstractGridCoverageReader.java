/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2001-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.io;

// J2SE dependencies
import java.awt.Dimension;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.geotools.coverage.CoverageFactoryFinder;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.grid.GeneralGridEnvelope;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.factory.Hints;
import org.geotools.image.io.RawBinaryImageReadParam;
import org.geotools.io.LineWriter;
import org.geotools.io.TableWriter;
import org.geotools.referencing.operation.builder.GridToEnvelopeMapper;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Errors;
import org.geotools.resources.i18n.Vocabulary;
import org.geotools.resources.i18n.VocabularyKeys;
import org.geotools.util.logging.Logging;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.coverage.grid.GridEnvelope;
import org.opengis.geometry.Envelope;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;


/**
 * Base class for reading {@link GridCoverage} objects. Reading is a two steps process:
 * The input file must be set first, then the actual reading is performed with the
 * {@link #getGridCoverage}. Example:
 *
 * <blockquote><pre>
 * AbstractGridCoverageReader reader = ...
 * reader.{@linkplain #setInput setInput}(new File("MyCoverage.dat"), true);
 * GridCoverage coverage = reader.{@linkplain #getGridCoverage getGridCoverage}(0);
 * </pre></blockquote>
 *
 * Subclasses needs to implements at least the following methods:
 *
 * <ul>
 *   <li>{@link #getCoordinateReferenceSystem}</li>
 *   <li>{@link #getEnvelope}</li>
 * </ul>
 *
 * The default implementation should be able to create acceptable grid
 * coverage using informations provided by the two above-mentioned methods.
 * However, other methods may be overriden too in order to get finner control
 * on the result.
 *
 * @since 2.2
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public abstract class AbstractGridCoverageReader implements GridCoverageReader {
    /**
     * The logger for the {@link #getGridCoverage} method.
     */
    static final Logger LOGGER = Logging.getLogger("org.geotools.coverage.io");

    /**
     * The format name (e.g. "PNG" or "GeoTIFF"). This format name should
     * be known to {@link ImageIO#getImageReadersByFormatName(String)},
     * unless {@link #getImageReaders} is overriden.
     */
    private final String formatName;

    /**
     * The {@link ImageReader} to use for decoding {@link RenderedImage}s. This reader is
     * initially {@code null} and lazily created the first time {@link #setInput} is invoked.
     * Once created, it is reused as much as possible. Invoking {@link #reset} dispose the
     * reader and set it back to {@code null}.
     */
    protected ImageReader reader;

    /**
     * The object to use for creating {@linkplain MathTransform math transform} from the
     * {@linkplain #getGridRange grid range} and the {@linkplain #getEnvelope envelope}.
     * Subclasses can change its configuration after {@code AbstractGridCoverageReader}
     * construction in order to change its behavior regarding axis reversal or axis
     * swapping for example.
     *
     * @see #getMathTransform
     *
     * @since 2.4
     */
    protected final GridToEnvelopeMapper gridToEnvelope = new GridToEnvelopeMapper();

    /**
     * The grid coverage factory to use.
     */
    private final GridCoverageFactory factory;

    /**
     * The input {@link File} or {@link URL}, or {@code null} if input is not set.
     */
    private Object input;

    /**
     * The input stream, or {@code null} if input is not set or if {@link #reader}
     * accepted directly {@link #input}.
     */
    private ImageInputStream stream;

    /**
     * The locale to use for formatting messages, or {@code null} for a default locale.
     */
    private Locale locale;

    /**
     * Constructs a {@code AbstractGridCoverageReader}. The {@linkplain ImageReader image reader}
     * will be determined from the file extension, if any.
     *
     * @param hints The factory hints to use.
     *
     * @since 2.4
     */
    public AbstractGridCoverageReader(final Hints hints) {
        this(hints, null);
    }

    /**
     * Constructs a {@code AbstractGridCoverageReader} for the specified format name. This
     * format name should be known to {@link ImageIO#getImageReadersByFormatName(String)}.
     *
     * @param hints The factory hints to use.
     * @param formatName The format name of the {@linkplain ImageReader image reader} to use,
     *        or {@code null} for relying on file extension instead.
     *
     * @since 2.4
     */
    public AbstractGridCoverageReader(final Hints hints, final String formatName) {
        this.factory = CoverageFactoryFinder.getGridCoverageFactory(hints);
        this.formatName = formatName;
    }

    /**
     * Restores the {@code AbstractGridCoverageReader} to its initial state.
     *
     * @throws IOException if an error occurs while disposing resources.
     */
    public synchronized void reset() throws IOException {
        clear();
        locale = null;
        if (reader != null) {
            reader.reset();
            reader.dispose();
            reader = null;
        }
    }

    /**
     * Clears this {@code AbstractGridCoverageReader}. This method is similar to {@link #reset},
     * except that it doesn't destroy the current {@link ImageReader} and keeps the locale setting.
     *
     * @throws IOException if an error occurs while disposing resources.
     */
    private void clear() throws IOException {
        assert Thread.holdsLock(this);
        input = null;
        if (reader != null) {
            reader.setInput(null);
        }
        if (stream != null) {
            stream.close();
            stream = null;
        }
    }

    /**
     * Returns a localized string for the specified error key.
     */
    final String getString(final int key) {
        return Errors.getResources(locale).getString(key);
    }

    /**
     * Returns the currently set {@link Locale}, or {@code null} if none has been set.
     */
    public Locale getLocale() {
        return locale;
    }

    /**
     * Sets the current {@linkplain Locale locale} of this coverage reader to the given value.
     * A value of {@code null} removes any previous setting, and indicates that the reader
     * should localize as it sees fit.
     */
    public synchronized void setLocale(final Locale locale) {
        this.locale = locale;
        setReaderLocale(locale);
    }

    /**
     * Set the locale for the {@link ImageReader}.
     */
    private void setReaderLocale(final Locale locale) {
        if (reader != null) {
            final Locale[] list = reader.getAvailableLocales();
            for (int i=list.length; --i>=0;) {
                if (locale.equals(list[i])) {
                    reader.setLocale(locale);
                    return;
                }
            }
            final String language = getISO3Language(locale);
            if (language != null) {
                for (int i=list.length; --i>=0;) {
                    if (language.equals(getISO3Language(list[i]))) {
                        reader.setLocale(list[i]);
                        return;
                    }
                }
            }
            reader.setLocale(null);
        }
    }

    /**
     * Returns the ISO language code for the specified locale, or {@code null} if not available.
     */
    private static String getISO3Language(final Locale locale) {
        try {
            return locale.getISO3Language();
        } catch (MissingResourceException exception) {
            return null;
        }
    }

    /**
     * Returns the specified input as a {@link File} object, or {@code null}.
     */
    private static File asFile(final Object input) {
        if (input instanceof File) {
            return (File) input;
        }
        if (input instanceof URL) {
            return new File(((URL) input).getPath());
        }
        return null;
    }

    /**
     * Returns an {@link Iterator} containing all currently registered {@link ImageReader}s
     * that claim to be able to decode the image. The default implementation returns
     * <code>ImageIO.getImageReadersByFormatName({@link #formatName})</code>.
     *
     * @param input The input source.
     */
    protected Iterator getImageReaders(final Object input) {
        if (formatName != null) {
            return ImageIO.getImageReadersByFormatName(formatName);
        }
        final File file = asFile(input);
        if (file != null) {
            final String filename = file.getName();
            final int separator = filename.lastIndexOf('.');
            if (separator >= 0) {
                final String extension = filename.substring(separator + 1);
                return ImageIO.getImageReadersBySuffix(extension);
            }
        }
        return ImageIO.getImageReaders(input);
    }

    /**
     * Sets the input source to the given object. The input is usually a
     * {@link File} or an {@link URL} object. But some other types (e.g.
     * {@link ImageInputStream}) may be accepted too.
     * <p>
     * If this method is invoked for the first time or after a call to
     * {@link #reset}, then it will queries {@link #getImageReaders} for
     * a list of {@link ImageReader}s and select the first one that accept
     * the input.
     *
     * @param  input The {@link File} or {@link URL} to be read.
     * @param  seekForwardOnly if {@code true}, grid coverages
     *         and metadata may only be read in ascending order from
     *         the input source.
     * @throws IOException if an I/O operation failed.
     * @throws IllegalArgumentException if input is not an instance
     *         of one of the classes declared by the {@link ImageReader}
     *         service provider.
     */
    public synchronized void setInput(final Object input, final boolean seekForwardOnly)
            throws IOException
    {
        clear();
        if (input != null) {
            ImageReader rdr = this.reader;
            boolean reuseLast = (rdr != null);
            for (final Iterator it=getImageReaders(input); it.hasNext();) {
                if (!reuseLast) {
                    rdr = (ImageReader) it.next();
                    setReaderLocale(locale);
                }
                reuseLast = false;
                final Class[] types = rdr.getOriginatingProvider().getInputTypes();
                if (contains(types, input.getClass())) {
                    rdr.setInput(input, seekForwardOnly);
                    this.input  = input;
                    this.reader = rdr;
                    return;
                }
                if (contains(types, ImageInputStream.class)) {
                    assert stream == null;
                    stream = ImageIO.createImageInputStream(input);
                    if (stream != null) {
                        rdr.setInput(stream, seekForwardOnly);
                        this.input  = input;
                        this.reader = rdr;
                        return;
                    }
                }
            }
            throw new IllegalArgumentException(getString(ErrorKeys.NO_IMAGE_READER));
        }
    }

    /**
     * Checks if the array {@code types} contains {@code type} or a super-class of {@code type}.
     */
    private static boolean contains(final Class[] types, final Class type) {
        for (int i=0; i<types.length; i++) {
            if (types[i].isAssignableFrom(type)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the number of images available from the current input source.
     * Note that some image formats do not specify how many images are present
     * in the stream. Thus determining the number of images will require the
     * entire stream to be scanned and may require memory for buffering.
     * The {@code allowSearch} parameter may be set to {@code false}
     * to indicate that an exhaustive search is not desired.
     *
     * @param  allowSearch If {@code true}, the true number of images will
     *         be returned even if a search is required. If {@code false},
     *         the reader may return -1 without performing the search.
     * @return The number of images, or -1 if {@code allowSearch} is
     *         {@code false} and a search would be required.
     * @throws IllegalStateException If the input source has not been set, or if
     *         the input has been specified with {@code seekForwardOnly} set to {@code true}.
     * @throws IOException If an error occurs reading the information from the input source.
     */
    public synchronized int getNumImages(final boolean allowSearch) throws IOException {
        if (reader == null) {
            throw new IllegalStateException(getString(ErrorKeys.NO_IMAGE_INPUT));
        }
        return reader.getNumImages(allowSearch);
    }

    /**
     * Ensures that the specified image index in inside the expected range. The upper limit
     * (exclusive) is given by <code>{@link #getNumImages getNumImages}(false)</code>.
     *
     * @param  imageIndex The index to check for validity.
     * @return The {@code numImages} value, as an opportunist information.
     * @throws IndexOutOfBoundsException If the index is invalid.
     * @throws IOException If an error occurs reading the information from the input source.
     */
    final int checkImageIndex(final int imageIndex) throws IOException, IndexOutOfBoundsException {
        assert Thread.holdsLock(this);
        if (reader == null) {
            throw new IllegalStateException(getString(ErrorKeys.NO_IMAGE_INPUT));
        }
        final int numImages = getNumImages(false);
        if (imageIndex<reader.getMinIndex() || (imageIndex>=numImages && numImages>=0)) {
            throw new IndexOutOfBoundsException(Errors.getResources(locale).getString(
                    ErrorKeys.ILLEGAL_ARGUMENT_$2, "imageIndex", imageIndex));
        }
        return numImages;
    }

    /**
     * Gets the {@link GridCoverage} name at the specified index. The default implementation
     * returns the input filename, or the "Untitled" string if input is not a {@link File} or
     * an {@link URL} object.
     *
     * @param  index The index of the image to be queried.
     * @return The name for the {@link GridCoverage} at the specified index.
     * @throws IllegalStateException if the input source has not been set.
     * @throws IndexOutOfBoundsException if the supplied index is out of bounds.
     * @throws IOException if an error occurs reading the information from the input source.
     */
    public synchronized String getName(final int index) throws IOException {
        final int numImages = checkImageIndex(index);
        String name;
        final File file = asFile(input);
        if (file != null) {
            name = file.getName();
        } else {
            name = Vocabulary.getResources(locale).getString(VocabularyKeys.UNTITLED);
        }
        if (numImages != 1) {
            name = name + " [" + index + ']';
        }
        return name;
    }
    
    /**
     * Returns the coordinate reference system for the {@link GridCoverage} to be read.
     *
     * @param  index The index of the image to be queried.
     * @return The coordinate reference system for the {@link GridCoverage} at the specified index.
     * @throws IllegalStateException if the input source has not been set.
     * @throws IndexOutOfBoundsException if the supplied index is out of bounds.
     * @throws IOException if an error occurs reading the width information from the input source.
     */
    public abstract CoordinateReferenceSystem getCoordinateReferenceSystem(int index) throws IOException;

    /**
     * Returns the envelope for the {@link GridCoverage} to be read.
     * The envelope must have the same number of dimensions than the
     * coordinate reference system.
     *
     * @param  index The index of the image to be queried.
     * @return The envelope for the {@link GridCoverage} at the specified index.
     * @throws IllegalStateException if the input source has not been set.
     * @throws IndexOutOfBoundsException if the supplied index is out of bounds.
     * @throws IOException if an error occurs reading the width information from the input source.
     */
    public abstract Envelope getEnvelope(int index) throws IOException;

    /**
     * Returns the grid range for the {@link GridCoverage} to be read.
     * The grid range must have the same number of dimensions than the
     * envelope.
     *
     * The default implementation construct a {@link GridRange} object
     * using information provided by {@link ImageReader#getWidth} and
     * {@link ImageReader#getHeight}.
     *
     * @param  index The index of the image to be queried.
     * @return The grid range for the {@link GridCoverage} at the specified index.
     * @throws IllegalStateException if the input source has not been set.
     * @throws IndexOutOfBoundsException if the supplied index is out of bounds.
     * @throws IOException if an error occurs reading the width information from the input source.
     */
    public synchronized GridEnvelope getGridRange(final int index) throws IOException {
        checkImageIndex(index);
        final int dimension = getCoordinateReferenceSystem(index).getCoordinateSystem().getDimension();
        final int[]   lower = new int[dimension];
        final int[]   upper = new int[dimension];
        Arrays.fill(upper, 1);
        upper[0] = reader.getWidth(index);
        upper[1] = reader.getHeight(index);
        return new GeneralGridEnvelope(lower, upper);
    }

    /**
     * Returns the transform from {@linkplain #getGridRange grid range} to {@linkplain
     * #getCoordinateReferenceSystem CRS} coordinates. The default implementation uses
     * the {@link #gridToEnvelope} mapper.
     *
     * @since 2.4
     */
    public synchronized MathTransform getMathTransform(final int index) throws IOException {
        gridToEnvelope.setGridRange(getGridRange(index));
        gridToEnvelope.setEnvelope (getEnvelope (index));
        return gridToEnvelope.createTransform();
    }

    /**
     * Returns the sample dimensions for each band of the {@link GridCoverage}
     * to be read. If sample dimensions are not known, then this method returns
     * {@code null}. The default implementation always returns {@code null}.
     *
     * @param  index The index of the image to be queried.
     * @return The category lists for the {@link GridCoverage} at the specified index.
     *         This array's length must be equals to the number of bands in {@link GridCoverage}.
     * @throws IllegalStateException if the input source has not been set.
     * @throws IndexOutOfBoundsException if the supplied index is out of bounds.
     * @throws IOException if an error occurs reading the width information from the input source.
     */
    public synchronized GridSampleDimension[] getSampleDimensions(final int index) throws IOException {
        checkImageIndex(index);
        return null;
    }

    /**
     * Reads the grid coverage. The default implementation gets the default {@link ImageReadParam}
     * and checks if it is an instance of {@link RawBinaryImageReadParam}. If it is, this method
     * then invokes {@link RawBinaryImageReadParam#setStreamImageSize} with informations provided
     * by {@link #getGridRange}. Finally, a grid coverage is constructed using informations provided
     * by {@link #getName}, {@link #getCoordinateReferenceSystem} and {@link #getEnvelope}.
     *
     * @param  index The index of the image to be queried.
     * @return The {@link GridCoverage} at the specified index.
     * @throws IllegalStateException if the input source has not been set.
     * @throws IndexOutOfBoundsException if the supplied index is out of bounds.
     * @throws IOException if an error occurs reading the width information from the input source.
     */
    public synchronized GridCoverage getGridCoverage(final int index) throws IOException {
        checkImageIndex(index);
        final ImageReadParam param = reader.getDefaultReadParam();
        if (param instanceof RawBinaryImageReadParam) {
            final RawBinaryImageReadParam rawParam = (RawBinaryImageReadParam) param;
            final GridEnvelope range = getGridRange(index);
            final Dimension  size = new Dimension(range.getSpan(0), range.getSpan(1));
            rawParam.setStreamImageSize(size);
        }
        final String                   name = getName(index);
        final MathTransform       gridToCRS = getMathTransform(index);
        final CoordinateReferenceSystem crs = getCoordinateReferenceSystem(index);
        final GridSampleDimension[]   bands = getSampleDimensions(index);
        final RenderedImage           image = reader.readAsRenderedImage(index, param);
        if (LOGGER.isLoggable(Level.FINE)) {
            /*
             * Log the arguments used for creating the GridCoverage. This is a costly logging:
             * the string representations for some argument are very long   (RenderedImage and
             * CoordinateReferenceSystem), and string representation for sample dimensions may
             * use many lines.
             */
            final Envelope   envelope = getEnvelope(index);
            final StringWriter buffer = new StringWriter(         );
            final LineWriter   trimer = new LineWriter  (buffer   );
            final TableWriter   table = new TableWriter (trimer, 1);
            final PrintWriter     out = new PrintWriter (table    );
            buffer.write("Creating GridCoverage[\"");
            buffer.write(name);
            buffer.write("\"] with:");
            buffer.write(trimer.getLineSeparator());
            table.setMultiLinesCells(true);
            final int sdCount = (bands != null) ? bands.length : 0;
            for (int i=-3; i<sdCount; i++) {
                String key = "";
                Object value;
                switch (i) {
                    case -3: key="RenderedImage";             value=image;    break;
                    case -2: key="CoordinateReferenceSystem"; value=crs;      break;
                    case -1: key="Envelope";                  value=envelope; break;
                    case  0: key="SampleDimensions"; // fall through
                    default: value = bands[i]; break;
                }
                out.print("    ");
                out.print(key   ); table.nextColumn();
                out.print('='   ); table.nextColumn();
                out.print(value ); table.nextLine();
            }
            out.flush();
            LOGGER.fine(buffer.toString());
        }
        return factory.create(name, image, crs, gridToCRS, bands, null, null);
    }
}
