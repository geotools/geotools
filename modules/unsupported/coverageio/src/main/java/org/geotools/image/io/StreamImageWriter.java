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
import java.net.URL;
import java.net.URLConnection;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.spi.ImageWriterSpi;
import javax.imageio.stream.ImageOutputStream;

import org.geotools.util.logging.Logging;
import org.geotools.resources.Classes;
import org.geotools.resources.i18n.ErrorKeys;


/**
 * Base class for simple image encoders. This class provides a {@link #getOutputStream} method,
 * which returns the {@linkplain #output output} as an {@link OutputStream} for convenience.
 * Different kinds of output like {@linkplain File} or {@linkplain URL} are automatically
 * handled.
 *
 * @since 2.4
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public abstract class StreamImageWriter extends GeographicImageWriter {
    /**
     * The stream to {@linkplain #close close} on {@link #setOutput}, {@link #reset} or
     * {@link #dispose} method invocation. This stream is typically an
     * {@linkplain OutputStream output stream} or a {@linkplain Writer writer}
     * created by {@link #getOutputStream} or similar methods in subclasses.
     * <p>
     * This field is never equals to the user-specified {@linkplain #output output}, since the
     * usual {@link javax.imageio.ImageWriter} contract is to <strong>not</strong> close the
     * user-provided stream. It is set to a non-null value only if a stream has been created
     * from an other user object like {@link File} or {@link URL}.
     *
     * @see #getOutputStream
     * @see org.geotools.image.io.text.TextImageWriter#getWriter
     * @see #close
     */
    protected Closeable closeOnReset;

    /**
     * {@link #output} as an output stream, or {@code null} if none.
     *
     * @see #getOutputStream
     */
    private OutputStream stream;

    /**
     * Constructs a new image writer.
     *
     * @param provider The {@link ImageWriterSpi} that is invoking this constructor,
     *        or {@code null} if none.
     */
    protected StreamImageWriter(final ImageWriterSpi provider) {
        super(provider);
    }

    /**
     * Sets the output source to use. Output may be one of the following object:
     * {@link File}, {@link URL}, {@link Writer} (for ASCII data), {@link OutputStream} or
     * {@link ImageOutputStream}. If {@code output} is {@code null}, then any currently
     * set output source will be removed.
     *
     * @param output The output object to use for future writing.
     *
     * @see #getOutput
     * @see #getOutputStream
     */
    @Override
    public void setOutput(final Object output) {
        closeSilently();
        super.setOutput(output);
    }

    /**
     * Returns the {@linkplain #output output} as an {@linkplain OutputStream output stream} object.
     * If the output is already an output stream, it is returned unchanged. Otherwise this method
     * creates a new {@linkplain OutputStream output stream} (usually <strong>not</strong>
     * {@linkplain BufferedOutputStream buffered}) from {@link File}, {@link URL},
     * {@link URLConnection} or {@link ImageOutputStream} outputs.
     * <p>
     * This method creates a new {@linkplain OutputStream output stream} only when first invoked.
     * All subsequent calls will returns the same instance. Consequently, the returned stream
     * should never be closed by the caller. It may be {@linkplain #close closed} automatically
     * when {@link #setOutput}, {@link #reset()} or {@link #dispose()} methods are invoked.
     *
     * @return {@link #getOutput} as an {@link OutputStream}. This output stream is usually
     *         not {@linkplain BufferedOutputStream buffered}.
     * @throws IllegalStateException if the {@linkplain #output output} is not set.
     * @throws IOException If the output stream can't be created for an other reason.
     *
     * @see #getOutput
     * @see org.geotools.image.io.text.TextImageWriter#getWriter
     */
    protected OutputStream getOutputStream() throws IllegalStateException, IOException {
        if (stream == null) {
            final Object output = getOutput();
            if (output == null) {
                // TODO: Adds the localized message.
                throw new IllegalStateException(/*getErrorResources().getString(ErrorKeys.NO_IMAGE_OUTPUT)*/);
            }
            if (output instanceof OutputStream) {
                stream = (OutputStream) output;
                closeOnReset = null; // We don't own the stream, so don't close it.
            } else if (output instanceof ImageOutputStream) {
                stream = new OutputStreamAdapter((ImageOutputStream) output);
                closeOnReset = null; // We don't own the ImageOutputStream, so don't close it.
            } else if (output instanceof String) {
                stream = new FileOutputStream((String) output);
                closeOnReset = stream;
            } else if (output instanceof File) {
                stream = new FileOutputStream((File) output);
                closeOnReset = stream;
            } else if (output instanceof URL) {
                stream = ((URL) output).openConnection().getOutputStream();
                closeOnReset = stream;
            } else if (output instanceof URLConnection) {
                stream = ((URLConnection) output).getOutputStream();
                closeOnReset = stream;
            } else {
                throw new IllegalStateException(getErrorResources().getString(
                        ErrorKeys.ILLEGAL_CLASS_$2, Classes.getClass(output), OutputStream.class));
            }
        }
        return stream;
    }

    /**
     * Closes the output stream created by {@link #getOutputStream()}. This method does nothing
     * if the output stream is the {@linkplain #output output} instance given by the user rather
     * than a stream created by this class from a {@link File} or {@link URL} output.
     * <p>
     * This method is invoked automatically by {@link #setOutput}, {@link #reset}, {@link #dispose}
     * or {@link #finalize} methods and doesn't need to be invoked explicitly. It has protected
     * access only in order to allow overriding by subclasses.
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
     * Restores the {@code StreamImageWriter} to its initial state. If an output stream were
     * created by a previous call to {@link #getOutputStream}, it will be {@linkplain #close
     * closed} before to reset this writer.
     */
    @Override
    public void reset() {
        closeSilently();
        super.reset();
    }

    /**
     * Allows any resources held by this writer to be released. If an output stream were created
     * by a previous call to {@link #getOutputStream}, it will be {@linkplain #close closed}
     * before to dispose this writer.
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
     * Service provider interface (SPI) for {@link StreamImageWriter}s.
     *
     * @since 2.4
     * @source $URL$
     * @version $Id$
     * @author Martin Desruisseaux (IRD)
     */
    public static abstract class Spi extends ImageWriterSpi {
        /**
         * List of legal output types for {@link StreamImageWriter}.
         */
        private static final Class[] OUTPUT_TYPES = new Class[] {
            File.class,
            URL.class,
            URLConnection.class,
            OutputStream.class,
            ImageOutputStream.class,
            String.class // To be interpreted as file path.
        };

        /**
         * Constructs a quasi-blank {@code StreamImageWriter.Spi}. It is up to the subclass to
         * initialize instance variables in order to provide working versions of all methods.
         * This constructor provides the following defaults:
         *
         * <ul>
         *   <li>{@link #outputTypes} = {{@link File}, {@link URL}, {@link URLConnection},
         *       {@link OutputStream}, {@link ImageOutputStream}, {@link String}}</li>
         * </ul>
         *
         * For efficienty reasons, the above fields are initialized to shared arrays. Subclasses
         * can assign new arrays, but should not modify the default array content.
         */
        public Spi() {
            outputTypes = OUTPUT_TYPES;
        }

        /**
         * Returns {@code true} if the image writer implementation associated with this service
         * provider is able to encode an image with the given layout. The default implementation
         * returns always {@code true}, which is accurate if the writer will fetch pixel values
         * with the help of an {@linkplain GeographicImageWriter#createRectIter iterator}.
         */
        public boolean canEncodeImage(final ImageTypeSpecifier type) {
            return true;
        }
    }
}
