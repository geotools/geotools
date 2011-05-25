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
package org.geotools.image.io;

import java.io.*; // Many imports, including some for javadoc only.
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.stream.ImageInputStream;

import org.geotools.util.logging.Logging;
import org.geotools.resources.Classes;
import org.geotools.resources.i18n.ErrorKeys;


/**
 * Base class for simple image decoders. This class provides a {@link #getInputStream} method,
 * which returns the {@linkplain #input input} as an {@link InputStream} for convenience.
 * Different kinds of input like {@linkplain File} or {@linkplain URL} are automatically
 * handled.
 *
 * @since 2.4
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public abstract class StreamImageReader extends GeographicImageReader {
    /**
     * The stream to {@linkplain #close close} on {@link #setInput(Object,boolean,boolean)
     * setInput(...)}, {@link #reset} or {@link #dispose} method invocation. This stream is
     * typically an {@linkplain InputStream input stream} or a {@linkplain Reader reader}
     * created by {@link #getInputStream} or similar methods in subclasses.
     * <p>
     * This field is never equals to the user-specified {@linkplain #input input}, since the
     * usual {@link javax.imageio.ImageReader} contract is to <strong>not</strong> close the
     * user-provided stream. It is set to a non-null value only if a stream has been created
     * from an other user object like {@link File} or {@link URL}.
     *
     * @see #getInputStream
     * @see org.geotools.image.io.text.TextImageReader#getReader
     * @see #close
     */
    protected Closeable closeOnReset;

    /**
     * {@link #input} as an input stream, or {@code null} if none.
     *
     * @see #getInputStream
     */
    private InputStream stream;

    /**
     * The stream position when {@link #setInput} is invoked.
     */
    private long streamOrigin;

    /**
     * Constructs a new image reader.
     *
     * @param provider The {@link ImageReaderSpi} that is invoking this constructor,
     *        or {@code null} if none.
     */
    protected StreamImageReader(final ImageReaderSpi provider) {
        super(provider);
    }

    /**
     * Sets the input source to use. Input may be one of the following object:
     * {@link File}, {@link URL}, {@link Reader} (for ASCII data), {@link InputStream} or
     * {@link ImageInputStream}. If {@code input} is {@code null}, then any currently
     * set input source will be removed.
     *
     * @param input           The input object to use for future decoding.
     * @param seekForwardOnly If {@code true}, images and metadata may only be read
     *                        in ascending order from this input source.
     * @param ignoreMetadata  If {@code true}, metadata may be ignored during reads.
     *
     * @see #getInput
     * @see #getInputStream
     */
    @Override
    public void setInput(final Object  input,
                         final boolean seekForwardOnly,
                         final boolean ignoreMetadata)
    {
        closeSilently();
        super.setInput(input, seekForwardOnly, ignoreMetadata);
        if (input instanceof ImageInputStream) {
            try {
                streamOrigin = ((ImageInputStream) input).getStreamPosition();
            } catch (IOException exception) {
                streamOrigin = 0;
                Logging.unexpectedException(LOGGER, StreamImageReader.class, "setInput", exception);
            }
        }
    }

    /**
     * Returns the stream length in bytes, or {@code -1} if unknown. This method checks the
     * {@linkplain #input input} type and invokes one of {@link File#length()},
     * {@link ImageInputStream#length()} ou {@link URLConnection#getContentLength()} method
     * accordingly.
     *
     * @return The stream length, or -1 is unknown.
     * @throws IOException if an I/O error occured.
     */
    protected long getStreamLength() throws IOException {
        final Object input = getInput();
        if (input instanceof ImageInputStream) {
            long length = ((ImageInputStream) input).length();
            if (length >= 0) {
                length -= streamOrigin;
            }
            return length;
        }
        if (input instanceof File) {
            return ((File) input).length();
        }
        if (input instanceof URL) {
            return ((URL) input).openConnection().getContentLength();
        }
        if (input instanceof URLConnection) {
            return ((URLConnection) input).getContentLength();
        }
        return -1;
    }

    /**
     * Returns the {@linkplain #input input} as an {@linkplain InputStream input stream} object.
     * If the input is already an input stream, it is returned unchanged. Otherwise this method
     * creates a new {@linkplain InputStream input stream} (usually <strong>not</strong>
     * {@linkplain BufferedInputStream buffered}) from {@link File}, {@link URL},
     * {@link URLConnection} or {@link ImageInputStream} inputs.
     * <p>
     * This method creates a new {@linkplain InputStream input stream} only when first invoked.
     * All subsequent calls will returns the same instance. Consequently, the returned stream
     * should never be closed by the caller. It may be {@linkplain #close closed} automatically
     * when {@link #setInput setInput(...)}, {@link #reset()} or {@link #dispose()} methods are
     * invoked.
     *
     * @return {@link #getInput} as an {@link InputStream}. This input stream is usually
     *         not {@linkplain BufferedInputStream buffered}.
     * @throws IllegalStateException if the {@linkplain #input input} is not set.
     * @throws IOException If the input stream can't be created for an other reason.
     *
     * @see #getInput
     * @see org.geotools.image.io.text.TextImageReader#getReader
     */
    protected InputStream getInputStream() throws IllegalStateException, IOException {
        if (stream == null) {
            final Object input = getInput();
            if (input == null) {
                throw new IllegalStateException(getErrorResources().getString(ErrorKeys.NO_IMAGE_INPUT));
            }
            if (input instanceof InputStream) {
                stream = (InputStream) input;
                closeOnReset = null; // We don't own the stream, so don't close it.
            } else if (input instanceof ImageInputStream) {
                stream = new InputStreamAdapter((ImageInputStream) input);
                closeOnReset = null; // We don't own the ImageInputStream, so don't close it.
            } else if (input instanceof String) {
                stream = new FileInputStream((String) input);
                closeOnReset = stream;
            } else if (input instanceof File) {
                stream = new FileInputStream((File) input);
                closeOnReset = stream;
            } else if (input instanceof URI) {
                stream = ((URI) input).toURL().openStream();
                closeOnReset = stream;
            } else if (input instanceof URL) {
                stream = ((URL) input).openStream();
                closeOnReset = stream;
            } else if (input instanceof URLConnection) {
                stream = ((URLConnection) input).getInputStream();
                closeOnReset = stream;
            } else {
                throw new IllegalStateException(getErrorResources().getString(
                        ErrorKeys.ILLEGAL_CLASS_$2, Classes.getClass(input), InputStream.class));
            }
        }
        return stream;
    }

    /**
     * Closes the input stream created by {@link #getInputStream()}. This method does nothing
     * if the input stream is the {@linkplain #input input} instance given by the user rather
     * than a stream created by this class from a {@link File} or {@link URL} input.
     * <p>
     * This method is invoked automatically by {@link #setInput(Object,boolean,boolean)
     * setInput(...)}, {@link #reset}, {@link #dispose} or {@link #finalize} methods and
     * doesn't need to be invoked explicitly. It has protected access only in order to
     * allow overriding by subclasses.
     *
     * @throws IOException if an error occured while closing the stream.
     *
     * @see #closeOnReset
     */
    @Override
    protected void close() throws IOException {
        if (closeOnReset != null) {
            closeOnReset.close();
        }
        closeOnReset = null;
        stream       = null;
        super.close();
    }

    /**
     * Invokes {@link #close} and log the exception if any. This method is invoked from
     * methods that do not allow {@link IOException} to be thrown. Since we will not use
     * the stream anymore after closing it, it should not be a big deal if an error occured.
     */
    private void closeSilently() {
        try {
            close();
        } catch (IOException exception) {
            Logging.unexpectedException(LOGGER, getClass(), "close", exception);
        }
    }

    /**
     * Restores the {@code StreamImageReader} to its initial state. If an input stream were
     * created by a previous call to {@link #getInputStream}, it will be {@linkplain #close
     * closed} before to reset this reader.
     */
    @Override
    public void reset() {
        closeSilently();
        super.reset();
    }

    /**
     * Allows any resources held by this reader to be released. If an input stream were created
     * by a previous call to {@link #getInputStream}, it will be {@linkplain #close closed}
     * before to dispose this reader.
     */
    @Override
    public void dispose() {
        closeSilently();
        super.dispose();
    }

    /**
     * Closes the streams. This method is automatically invoked by the garbage collector.
     */
    @Override
    protected void finalize() throws Throwable {
        close();
        super.finalize();
    }




    /**
     * Service provider interface (SPI) for {@link StreamImageReader}s.
     *
     * @since 2.4
     * @source $URL$
     * @version $Id$
     * @author Martin Desruisseaux (IRD)
     */
    public static abstract class Spi extends ImageReaderSpi {
        /**
         * List of legal input types for {@link StreamImageReader}.
         */
        private static final Class[] INPUT_TYPES = new Class[] {
            File.class,
            URI.class,
            URL.class,
            URLConnection.class,
            InputStream.class,
            ImageInputStream.class,
            String.class // To be interpreted as file path.
        };

        /**
         * Constructs a quasi-blank {@code StreamImageReader.Spi}. It is up to the subclass to
         * initialize instance variables in order to provide working versions of all methods.
         * This constructor provides the following defaults:
         *
         * <ul>
         *   <li>{@link #inputTypes} = {{@link File}, {@link URL}, {@link URLConnection},
         *       {@link InputStream}, {@link ImageInputStream}, {@link String}}</li>
         * </ul>
         *
         * For efficienty reasons, the above fields are initialized to shared arrays. Subclasses
         * can assign new arrays, but should not modify the default array content.
         */
        public Spi() {
            inputTypes = INPUT_TYPES;
        }
    }
}
