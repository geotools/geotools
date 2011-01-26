/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.io;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;


/**
 * A writer that copy all output to an other stream. This writer can be used for perfoming
 * an exact copy of what is sent to an other writer. For example, it may be used for echoing
 * to the standard output the content sent to a file. This writer is usefull for debugging
 * purpose.
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 *
 * @since 2.1
 */
public class EchoWriter extends FilterWriter {
    /**
     * The echo writer.
     */
    private final Writer echo;

    /**
     * Creates a writer that will echo to the {@linkplain System#out standard output}.
     * Each line to that standard output will be {@linkplain NumberedLineWriter numbered}.
     *
     * @param main The main stream.
     */
    public EchoWriter(final Writer main) {
        super(main);
        this.echo = NumberedLineWriter.OUT;
    }

    /**
     * Creates a copy writter for the specified stream.
     *
     * @param main The main stream.
     * @param echo The echo stream.
     */
    public EchoWriter(final Writer main, final Writer echo) {
        super(main);
        this.echo = echo;
    }

    /**
     * Writes a single character.
     *
     * @param  c The character to write.
     * @throws IOException If an I/O error occurs.
     */
    @Override
    public void write(final int c) throws IOException {
        synchronized (lock) {
            out .write(c);
            echo.write(c);
        }
    }

    /**
     * Writes an array of characters.
     *
     * @param  cbuf Buffer of characters to be written.
     * @throws IOException  If an I/O error occurs.
     */
    @Override
    public void write(final char[] cbuf) throws IOException {
        synchronized (lock) {
            out .write(cbuf);
            echo.write(cbuf);
        }
    }

    /**
     * Writes a portion of an array of characters.
     *
     * @param  cbuf   Buffer of characters to be written.
     * @param  offset Offset from which to start reading characters.
     * @param  length Number of characters to be written.
     * @throws IOException If an I/O error occurs.
     */
    @Override
    public void write(final char[] cbuf, final int offset, final int length) throws IOException {
        synchronized (lock) {
            out .write(cbuf, offset, length);
            echo.write(cbuf, offset, length);
        }
    }

    /**
     * Writes a string.
     *
     * @param  string String to be written.
     * @throws IOException If an I/O error occurs.
     */
    @Override
    public void write(final String string) throws IOException {
        synchronized (lock) {
            out .write(string);
            echo.write(string);
        }
    }

    /**
     * Writes a portion of a string.
     *
     * @param  string String to be written.
     * @param  offset Offset from which to start writing characters.
     * @param  length Number of characters to write.
     * @throws IOException If an I/O error occurs.
     */
    @Override
    public void write(final String string, final int offset, final int length) throws IOException {
        synchronized (lock) {
            out .write(string, offset, length);
            echo.write(string, offset, length);
        }
    }

    /**
     * Flushs both streams.
     *
     * @throws IOException If an I/O error occurs.
     */
    @Override
    public void flush() throws IOException {
        synchronized (lock) {
            out .flush();
            echo.flush();
        }
    }

    /**
     * Closes the main stream and the echo stream. In the particular case of writers created
     * with the {@linkplain #EchoWriter(Writer) one argument constructor}, the echo stream
     * will not be closed since it maps to the {@linkplain System#out standard output}.
     *
     * @throws IOException If an I/O error occurs.
     */
    @Override
    public void close() throws IOException {
        synchronized (lock) {
            out .close();
            echo.close(); // Overridden with an uncloseable version for System.out.
        }
    }
}
