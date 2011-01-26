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

import org.geotools.util.Utilities;


/**
 * Writes characters to a stream while expanding tabs ({@code '\t'}) into spaces.
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 *
 * @since 2.0
 */
public class ExpandedTabWriter extends FilterWriter {
    /**
     * Tab width (in number of spaces).
     */
    private int tabWidth = 8;

    /**
     * Current column position. Columns are numbered from 0.
     */
    private int column = 0;

    /**
     * Constructs a filter which replaces tab characters ({@code '\t'})
     * by spaces. Tab widths default to 8 characters.
     *
     * @param out A writer object to provide the underlying stream.
     */
    public ExpandedTabWriter(final Writer out) {
        super(out);
    }

    /**
     * Constructs a filter which replaces tab characters ({@code '\t'})
     * by spaces, using the specified tab width.
     *
     * @param  out A writer object to provide the underlying stream.
     * @param  tabWidth The tab width. Must be greater than 0.
     * @throws IllegalArgumentException if {@code tabWidth} is not greater than 0.
     */
    public ExpandedTabWriter(final Writer out, final int tabWidth) throws IllegalArgumentException {
        super(out);
        setTabWidth(tabWidth);
    }

    /**
     * Sets the tab width.
     *
     * @param  tabWidth The tab width. Must be greater than 0.
     * @throws IllegalArgumentException if {@code tabWidth} is not greater than 0.
     */
    public void setTabWidth(final int tabWidth) throws IllegalArgumentException {
        synchronized (lock) {
            if (tabWidth > 0) {
                this.tabWidth = tabWidth;
            } else {
                throw new IllegalArgumentException(Integer.toString(tabWidth));
            }
        }
    }

    /**
     * Returns the tab width.
     *
     * @return The tabulation width.
     */
    public int getTabWidth() {
        return tabWidth;
    }

    /**
     * Writes spaces for a tab character.
     *
     * @throws IOException If an I/O error occurs.
     */
    private void expand() throws IOException {
        final int width = tabWidth - (column % tabWidth);
        out.write(Utilities.spaces(width));
        column += width;
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
                case '\r': // fall through
                case '\n': column=0; break;
                case '\t': expand(); return;
                default  : column++; break;
            }
            out.write(c);
        }
    }

    /**
     * Writes a portion of an array of characters.
     *
     * @param  buffer  Buffer of characters to be written
     * @param  offset  Offset from which to start reading characters
     * @param  length  Number of characters to be written
     * @throws IOException If an I/O error occurs.
     */
    @Override
    public void write(final char[] buffer, final int offset, int length) throws IOException {
        synchronized (lock) {
            int start = offset;
            length += offset;
            for (int end=offset; end<length; end++) {
                final char c = buffer[end];
                switch (c) {
                    case '\r': // fall through
                    case '\n': column = 0;
                               break;

                    case '\t': out.write(buffer, start, end-start);
                               start = end+1;
                               expand();
                               break;

                    default  : column++;
                               break;
                }
            }
            out.write(buffer, start, length-start);
        }
    }

    /**
     * Writes a portion of a string.
     *
     * @param  string  String to be written
     * @param  offset  Offset from which to start reading characters
     * @param  length  Number of characters to be written
     * @throws IOException If an I/O error occurs.
     */
    @Override
    public void write(final String string, final int offset, int length) throws IOException {
        synchronized (lock) {
            int start = offset;
            length += offset;
            for (int end=offset; end<length; end++) {
                final char c = string.charAt(end);
                switch (c) {
                    case '\r': // fall through
                    case '\n': column = 0;
                               break;

                    case '\t': out.write(string, start, end-start);
                               start = end+1;
                               expand();
                               break;

                    default  : column++;
                               break;
                }
            }
            out.write(string, start, length-start);
        }
    }
}
