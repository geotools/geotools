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
package org.geotools.io;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;
import org.geotools.resources.XArray;


/**
 * Writes characters to a stream while replacing various EOL by a unique string. This class
 * catches all occurrences of {@code "\r"}, {@code "\n"} and {@code "\r\n"}, and replaces them
 * by the platform depend EOL string ({@code "\r\n"} on Windows, {@code "\n"} on Unix), or any
 * other EOL explicitly set at construction time. This writer also remove trailing blanks before
 * end of lines, but this behavior can be changed by overriding {@link #isWhitespace}.
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 *
 * @since 2.0
 */
public class LineWriter extends FilterWriter {
    /**
     * The line separator for End Of Line (EOL).
     */
    private String lineSeparator;

    /**
     * Tells if the next '\n' character must be ignored. This field
     * is used in order to avoid writing two EOL in place of "\r\n".
     */
    private boolean skipCR;

    /**
     * Temporary buffer containing blanks to write. Whitespaces are put
     * in this buffer before to be written. If whitespaces are followed
     * by a character, they are written to the underlying stream before
     * the character. Otherwise, if whitespaces are followed by a line
     * separator, then they are discarted. The buffer capacity will be
     * expanded as needed.
     */
    private char[] buffer = new char[64];

    /**
     * Number of valid characters in {@link #buffer}.
     */
    private int count = 0;

    /**
     * Constructs a {@code LineWriter} object that will use the platform dependent line separator.
     *
     * @param  out A writer object to provide the underlying stream.
     * @throws IllegalArgumentException if {@code out} is {@code null}.
     */
    public LineWriter(final Writer out) {
        this(out, System.getProperty("line.separator", "\n"));
    }

    /**
     * Constructs a {@code LineWriter} object that will use the specified line separator.
     *
     * @param  out A writer object to provide the underlying stream.
     * @param  lineSeparator String to use as line separator.
     * @throws IllegalArgumentException if {@code out} or {@code lineSeparator} is {@code null}.
     */
    public LineWriter(final Writer out, final String lineSeparator) {
        super(out);
        this.lineSeparator = lineSeparator;
        if (out==null || lineSeparator==null) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Returns the current line separator.
     *
     * @return The current line separator.
     */
    public String getLineSeparator() {
        return lineSeparator;
    }

    /**
     * Changes the line separator. This is the string to insert in place of every occurences of
     * {@code "\r"}, {@code "\n"} or {@code "\r\n"}.
     *
     * @param lineSeparator The new line separator.
     * @throws IllegalArgumentException If {@code lineSeparator} is {@code null}.
     */
    public void setLineSeparator(final String lineSeparator) {
        if (lineSeparator == null) {
            throw new IllegalArgumentException();
        }
        synchronized (lock) {
            this.lineSeparator = lineSeparator;
        }
    }

    /**
     * Writes a line separator.
     *
     * @throws IOException If an I/O error occurs.
     */
    private void writeEOL() throws IOException {
        assert count == 0 : count;
        // Do NOT call super.write(String).
        out.write(lineSeparator);
    }

    /**
     * Returns {@code true} if {@link #buffer} contains only white spaces. It should
     * always be the case. This method is used for assertions only.
     */
    private boolean bufferBlank() throws IOException {
        for (int i=count; --i>=0;) {
            if (!isWhitespace(buffer[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * Flushs the content of {@link #buffer} to the underlying stream.
     *
     * @throws IOException If an I/O error occurs.
     */
    private void flushBuffer() throws IOException {
        assert bufferBlank();
        if (count != 0) {
            out.write(buffer, 0, count);
            count = 0;
        }
    }

    /**
     * Writes a portion of an array of characters. This portion must
     * <strong>not</strong> contains any line separator.
     */
    private void writeLine(final char[] cbuf, final int lower, int upper) throws IOException {
        while (upper != lower) {
            final char c = cbuf[upper-1];
            assert (c!='\r' && c!='\n');
            if (isWhitespace(c)) {
                upper--;
                continue;
            }
            flushBuffer();
            out.write(cbuf, lower, upper-lower);
            return;
        }
        assert bufferBlank();
        count = 0;
    }

    /**
     * Writes a portion of an array of characters. This portion must
     * <strong>not</strong> contains any line separator.
     */
    private void writeLine(final String str, final int lower, int upper) throws IOException {
        while (upper != lower) {
            final char c = str.charAt(upper-1);
            assert (c!='\r' && c!='\n');
            if (isWhitespace(c)) {
                upper--;
                continue;
            }
            flushBuffer();
            out.write(str, lower, upper-lower);
            return;
        }
        assert bufferBlank();
        count = 0;
    }

    /**
     * Writes a single character.
     *
     * @throws IOException If an I/O error occurs.
     */
    @Override
    public void write(final int c) throws IOException {
        synchronized (lock) {
            switch (c) {
                case '\r': {
                    assert bufferBlank();
                    count = 0; // Discard whitespaces
                    writeEOL();
                    skipCR = true;
                    break;
                }
                case '\n': {
                    if (!skipCR) {
                        assert bufferBlank();
                        count = 0; // Discard whitespaces
                        writeEOL();
                    }
                    skipCR = false;
                    break;
                }
                default: {
                    if (c>=Character.MIN_VALUE && c<=Character.MAX_VALUE && isWhitespace((char)c)) {
                        if (count >= buffer.length) {
                            buffer = XArray.resize(buffer, count + Math.min(8192, count));
                        }
                        buffer[count++] = (char)c;
                    } else {
                        flushBuffer();
                        out.write(c);
                    }
                    skipCR = false;
                    break;
                }
            }
        }
    }

    /**
     * Writes a portion of an array of characters.
     *
     * @param  cbuf    Buffer of characters to be written.
     * @param  offset  Offset from which to start reading characters.
     * @param  length  Number of characters to be written.
     * @throws IOException If an I/O error occurs.
     */
    @Override
    public void write(final char cbuf[], int offset, int length) throws IOException {
        if (offset<0 || length<0 || (offset+length)>cbuf.length) {
            throw new IndexOutOfBoundsException();
        }
        if (length == 0) {
            return;
        }
        synchronized (lock) {
            if (skipCR && cbuf[offset]=='\n') {
                offset++;
                length--;
            }
            int upper = offset;
            for (; length!=0; length--) {
                switch (cbuf[upper++]) {
                    case '\r': {
                        writeLine(cbuf, offset, upper-1);
                        writeEOL();
                        if (length > 1 && cbuf[upper] == '\n') {
                            upper++;
                            length--;
                        }
                        offset = upper;
                        break;
                    }
                    case '\n': {
                        writeLine(cbuf, offset, upper-1);
                        writeEOL();
                        offset = upper;
                        break;
                    }
                }
            }
            skipCR = (cbuf[upper-1] == '\r');
            /*
             * Write the remainding characters and
             * put trailing blanks into the buffer.
             */
            for (int i=upper; --i>=offset;) {
                if (!isWhitespace(cbuf[i])) {
                    writeLine(cbuf, offset, offset=i+1);
                    break;
                }
            }
            length = upper - offset;
            final int newCount = count + length;
            if (newCount > buffer.length) {
                buffer = XArray.resize(buffer, newCount);
            }
            System.arraycopy(cbuf, offset, buffer, count, length);
            count = newCount;
        }
    }

    /**
     * Writes a portion of an array of a string.
     *
     * @param  string  String to be written.
     * @param  offset  Offset from which to start reading characters.
     * @param  length  Number of characters to be written.
     * @throws IOException If an I/O error occurs.
     */
    @Override
    public void write(final String string, int offset, int length) throws IOException {
        if (offset<0 || length<0 || (offset+length)>string.length()) {
            throw new IndexOutOfBoundsException();
        }
        if (length == 0) {
            return;
        }
        synchronized (lock) {
            if (skipCR && string.charAt(offset)=='\n') {
                offset++;
                length--;
            }
            int upper = offset;
            for (; length!=0; length--) {
                switch (string.charAt(upper++)) {
                    case '\r': {
                        writeLine(string, offset, upper-1);
                        writeEOL();
                        if (length > 1 && string.charAt(upper) == '\n') {
                            upper++;
                            length--;
                        }
                        offset=upper;
                        break;
                    }
                    case '\n': {
                        writeLine(string, offset, upper-1);
                        writeEOL();
                        offset=upper;
                        break;
                    }
                }
            }
            skipCR = (string.charAt(upper-1) == '\r');
            /*
             * Write the remainding characters and
             * put trailing blanks into the buffer.
             */
            for (int i=upper; --i>=offset;) {
                if (!isWhitespace(string.charAt(i))) {
                    writeLine(string, offset, offset=i+1);
                    break;
                }
            }
            length = upper - offset;
            final int newCount = count+length;
            if (newCount > buffer.length) {
                buffer = XArray.resize(buffer, newCount);
            }
            while (--length>=0) {
                buffer[count++] = string.charAt(offset++);
            }
            assert count == newCount : newCount;
        }
    }

    /**
     * Flushs the stream's content to the underlying stream. This method flush completly
     * all internal buffers, including any whitespace characters that should have been
     * skipped if the next non-blank character is a line separator.
     *
     * @throws IOException If an I/O error occurs.
     */
    @Override
    public void flush() throws IOException {
        synchronized (lock) {
            flushBuffer();
            super.flush();
        }
    }

    /**
     * Returns {@code true} if the specified character is a white space that can be ignored
     * on end of line. The default implementation returns {@link Character#isSpaceChar(char)}.
     * Subclasses can override this method in order to change the criterion.
     *
     * @param  c The character to test.
     * @return {@code true} if {@code c} is a character that can be ignored on end of line.
     * @throws IOException if this method can not determine if the character is ignoreable.
     *
     * @since 2.5
     */
    protected boolean isWhitespace(final char c) throws IOException {
        return Character.isSpaceChar(c);
    }
}
