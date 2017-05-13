/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.shapefile.dbf.index;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Provides a channel that reads and seeks bytes from the given stream.
 * 
 * @author Alvaro Huarte
 */
public interface SeekableByteChannel {
    
    /**
     * Sets the stream-pointer offset, measured from the beginning of this stream, at which the next read or write occurs.
     * @throws IOException
     */
    void seek(long pos) throws IOException;
    
    /**
     * Reads up to <code>len</code> bytes of data from this stream into an array of bytes.
     * @throws IOException
     */
    int readBytes(byte bytes[], int startIndex, int byteCount) throws IOException;
    
    /**
     * Reads a byte of data from this stream.
     * The byte is returned as an integer in the range 0 to 255 (<code>0x00-0x0ff</code>).
     * @throws IOException
     */
    int readByte() throws IOException;
    
    /**
     * Reads a signed 16-bit number from this stream.
     * @throws IOException
     */
    short readShort() throws IOException;
    
    /**
     * Reads a signed 32-bit number from this stream.
     * @throws IOException
     */
    int readInt() throws IOException;
    
    /**
     * Reads a signed 64-bit number from this stream.
     * @throws IOException
     */
    long readLong() throws IOException;
    
    /**
     * Reads a double from this stream.
     * @throws IOException
     */
    double readDouble() throws IOException;
    
    /**
     * Reads a string from this stream.
     * @throws IOException
     */
    String readString(int charCount, Charset charset) throws IOException;
    
    /**
     * Closes and releases any system resources associated with the stream.
     * @throws IOException
     */
    void close() throws IOException;
}
