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
package org.geotools.image.io.text;

import java.awt.image.BufferedImage;
import java.io.*; // Many imports, including some for javadoc only.
import java.text.ParseException;
import javax.imageio.ImageReadParam;

import org.geotools.io.LineFormat;
import org.geotools.resources.XArray;


/**
 * A dummy implementation of {@link TextImageReader} used only by default implementation
 * of {@link TextImageReader.Spi#canDecodeInput}. This class is more lightweight than
 * loading the real image reader implementation.
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
final class TestReader extends TextImageReader {
    /**
     * The input stream to {@linkplain InputStream#reset reset} after
     * {@linkplain InputStream#mark mark}.
     */
    private InputStream marked;

    /**
     * Creates a new reader for the specified provider.
     */
    public TestReader(final TextImageReader.Spi provider) {
        super(provider);
    }

    /**
     * Returns a null width.
     */
    public int getWidth(int imageIndex) {
        return 0;
    }

    /**
     * Returns a null height.
     */
    public int getHeight(int imageIndex) {
        return 0;
    }

    /**
     * Throws an {@link UnsupportedOperationException}.
     */
    public BufferedImage read(final int imageIndex, final ImageReadParam param) {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the {@linkplain #input input} as a {@linkplain Reader reader}, which doesn't need to
     * be {@linkplain BufferedReader buffered}. If the reader is an instance supplied explicitly by
     * the user, then it will be {@linkplain Reader#mark marked} with the specified read ahead limit.
     *
     * @return {@link #getInput} as a {@link Reader}, or {@code null} if this method
     *         can't provide a reader suitable for {@code canDecode}.
     * @throws IllegalStateException if the {@linkplain #input input} is not set.
     * @throws IOException If the input stream can't be created for an other reason.
     */
    private Reader getReader(final int readAheadLimit) throws IllegalStateException, IOException {
        final Object input = getInput();
        if (input instanceof Reader) {
            final Reader reader = (Reader) input;
            if (!reader.markSupported()) {
                return null;
            }
            reader.mark(readAheadLimit);
            return reader;
            // Do not set 'closeOnReset' since we don't own the reader.
        }
        final InputStream stream = getInputStream();
        if (closeOnReset == null) {
            // If we are not allowed to close and reopen a new stream on ImageReader.read, then
            // we must be able to mark the stream otherwise we will not support canDecode(...).
            if (!stream.markSupported()) {
                return null;
            }
            stream.mark(readAheadLimit);
        }
        final Reader reader = getInputStreamReader(stream);
        if (closeOnReset == stream) {
            closeOnReset = reader;
        }
        return reader;
    }

    /**
     * Checks if the {@linkplain #getReader reader} seems to contains a readeable ASCII file.
     * This method tries to read the first few lines. The caller is responsable for invoking
     * {@link #close} after this method.
     *
     * @param  readAheadLimit Maximum number of characters to read. If this amount is reached
     *         but this method still unable to make a choice, then it returns {@code null}.
     * @return {@code true} if the source <em>seems</em> readable, {@code false} otherwise.
     * @throws IOException If an error occured during reading.
     */
    final boolean canDecode(final int readAheadLimit) throws IOException {
        final Reader input = getReader(readAheadLimit);
        if (input == null) {
            return false;
        }
        final TextImageReader.Spi spi = (TextImageReader.Spi) originatingProvider;
        final char[]     buffer = new char[readAheadLimit];
        final int        length = input.read(buffer);
        final LineFormat parser = getLineFormat(0);
        double[][] rows = new double[16][];
        int rowCount = 0;
        int lower = 0;
scan:   while (lower < readAheadLimit) {
            // Skip line feeds at the begining of the line.
            // They may be a rest from the previous line.
            char c = buffer[lower];
            if (c == '\r' || c == 'n') {
                lower++;
                continue;
            }
            // Search the end of line. If we reach the end of the buffer,
            // do not attempt to parse that last line since it is incomplete.
            int upper = lower;
            while ((c = buffer[upper]) != '\r' && c != '\n') {
                if (++upper >= readAheadLimit) {
                    break scan;
                }
            }
            // Try to parse a line.
            final String line = new String(buffer, lower, upper-lower);
            if (!isComment(line)) {
                try {
                    if (parser.setLine(line) != 0) {
                        if (rowCount == rows.length) {
                            rows = XArray.resize(rows, rows.length * 2);
                        }
                        rows[rowCount] = parser.getValues(rows[rowCount]);
                        rowCount++;
                    }
                } catch (ParseException exception) {
                    return false;
                }
            }
            lower = upper;
        }
        if (originatingProvider instanceof TextImageReader.Spi) {
            rows = XArray.resize(rows, rowCount);
            return ((TextImageReader.Spi) originatingProvider).isValidContent(rows);
        }
        return true;
    }

    /**
     * Closes the reader created by this class, or {@linkplain Reader#reset reset} the
     * user's reader to its original position.
     */
    @Override
    protected void close() throws IOException {
        if (marked != null) {
            marked.reset();
            marked = null;
        }
        super.close();
    }
}
