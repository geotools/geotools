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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * Implements a seekable byte channel from a File.
 * 
 * @author Alvaro Huarte
 */
class MemorySeekableByteChannel implements SeekableByteChannel {
    
    private SeekableByteArrayInputStream file;
    
    /**
     * Helper class to define 'seek' method.
     */
    class SeekableByteArrayInputStream extends ByteArrayInputStream {
        
        public SeekableByteArrayInputStream(byte buffer[]) {
            super(buffer);
        }
        public void seek(int pos) {
            this.pos = pos;
        }
    };
    
    /**
     * Creates a new MemorySeekableByteChannel object.
     * @throws IOException 
     */
    public MemorySeekableByteChannel(InputStream inputStream) throws IOException {
        
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        
        int readCount;
        byte[] bytes = new byte[1024];
        
        while ((readCount = inputStream.read(bytes, 0, bytes.length)) != -1) {
            buffer.write(bytes, 0, readCount);
        }
        this.file = new SeekableByteArrayInputStream(buffer.toByteArray());
        inputStream.close();
        buffer.close();
    }
    /**
     * Closes and releases any system resources associated with the stream.
     * @throws IOException
     */
    @Override
    public void close() throws IOException {
        if (file != null) {
            file.close();
            file = null;
        }
    }
    
    /**
     * Sets the stream-pointer offset, measured from the beginning of this stream, at which the next read or write occurs.
     * @throws IOException
     */
    @Override
    public void seek(long pos) throws IOException {
        file.seek((int)pos);
    }
    
    /**
     * Reads up to <code>len</code> bytes of data from this stream into an array of bytes.
     * @throws IOException
     */
    @Override
    public int readBytes(byte bytes[], int startIndex, int byteCount) throws IOException {
        return file.read(bytes, startIndex, byteCount);
    }
    /**
     * Reads a byte of data from this stream.
     * The byte is returned as an integer in the range 0 to 255 (<code>0x00-0x0ff</code>).
     * @throws IOException
     */
    @Override
    public int readByte() throws IOException {
        return file.read();
    }
    /**
     * Reads a signed 16-bit number from this stream.
     * @throws IOException
     */
    @Override
    public short readShort() throws IOException {
        int ch1 = this.readByte();
        int ch2 = this.readByte();
        return (short)((ch1 << 8) + (ch2 << 0));
    }
    /**
     * Reads a signed 32-bit number from this stream.
     * @throws IOException
     */
    @Override
    public int readInt() throws IOException {
        int ch1 = this.readByte();
        int ch2 = this.readByte();
        int ch3 = this.readByte();
        int ch4 = this.readByte();
        return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));
    }
    /**
     * Reads a signed 64-bit number from this stream.
     * @throws IOException
     */
    @Override
    public long readLong() throws IOException {
        return ((long)(readInt()) << 32) + (readInt() & 0xFFFFFFFFL);
    }
    /**
     * Reads a double from this stream.
     * @throws IOException
     */
    @Override
    public double readDouble() throws IOException {
        return Double.longBitsToDouble(readLong());
    }
    /**
     * Reads a string from this stream.
     * @throws IOException
     */
    @Override
    public String readString(int charCount, Charset charset) throws IOException {
        byte[] bytes = new byte[charCount];
        readBytes(bytes, 0, charCount);
        while (charCount>0) if (bytes[charCount-1]==0) charCount--; else break;
        return charCount>0 ? new String(bytes, 0, charCount, charset) : "";
    }
}
