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

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * A <code>BufferedRandomReadOnlyAccessFile</code> is like a <code>RandomAccessFile</code>, 
 * but it uses a private buffer so that most operations do not require a disk access.
 * 
 * @author Alvaro Huarte
 */
class BufferedRandomAccessFile extends RandomAccessFile {
    
    // Default buffer size, 4Kb.
    public static final int DEFAULT_BUFFER_SIZE = 4096;
    
    // Buffer which will cache file blocks.
    protected byte buffer[];
    // Buffer size.
    protected int bufferSize;
    
    // File positions.
    protected long filePos = 0;
    protected int buf_end = 0;
    protected int buf_pos = 0;
    
    /**
     * Open a new <code>BufferedRandomAccessFile</code> on <code>file</code>
     * using a bufferSize <code>buffersize</code> in mode <code>mode</code>, 
     * which should be "r" for reading only, or "rw" for reading and writing.
     * <p>
     * When the <code>mode</code> enables writing the cache is unused.
     * </p>
     */
    public BufferedRandomAccessFile(String filename, String mode, int bufferSize) throws IOException {
        super(filename, mode);
        initBuffer(mode, bufferSize);
        invalidate();
    }
    /**
     * Open a new <code>BufferedRandomAccessFile</code> on <code>file</code>
     * using a bufferSize <code>buffersize</code> in mode <code>mode</code>, 
     * which should be "r" for reading only, or "rw" for reading and writing.
     * <p>
     * When the <code>mode</code> enables writing the cache is unused.
     * </p>
     */
    public BufferedRandomAccessFile(File file, String mode, int bufferSize) throws IOException {
        super(file, mode);
        initBuffer(mode, bufferSize);
        invalidate();
    }
    
    /**
     * Reads the buffer from file.
     * @throws IOException
     */
    private int fillBuffer() throws IOException {
        int n = super.read(buffer, 0, bufferSize);
        if (n >= 0) {
            filePos += n;
            buf_end = n;
            buf_pos = 0;
        }
        return n;
    }
    /**
     * Initializes the buffer in readOnly mode. 
     */
    private boolean initBuffer(String mode, int bufferSize) {
        if (bufferSize > 0 && !mode.contains("w")) {
            this.bufferSize = bufferSize;
            buffer = new byte[bufferSize];
            return true;
        }
        return false;
    }
    /**
     * Invalidates buffer state. 
     */
    private void invalidate() throws IOException {
        buf_end = 0;
        buf_pos = 0;
        filePos = super.getFilePointer();
    }

    @Override
    public final int read() throws IOException {
        if (bufferSize > 0) {
            if (buf_pos >= buf_end) {
                if (fillBuffer() < 0) return -1;
            }
            return buf_end == 0 ? -1 : (buffer[buf_pos++] & 0xFF);
        }
        return super.read();
    }
    @Override
    public int read(byte b[], int off, int len) throws IOException {
        if (bufferSize > 0) {
            int leftover = buf_end - buf_pos;
            
            if (len <= leftover) {
                System.arraycopy(buffer, buf_pos, b, off, len);
                buf_pos += len;
                return len;
            }
            for (int i = 0, c; i < len; i++) {
                c = this.read();
                if (c != -1)
                    b[off+i] = (byte)c;
                else
                    return i;
            }
            return len;
        }
        return super.read(b, off, len);
    }
    
    @Override
    public long getFilePointer() throws IOException {
        if (bufferSize > 0) {
            long l = filePos;
            return (l - buf_end + buf_pos);
        }
        return super.getFilePointer();
    }
    @Override
    public void seek(long pos) throws IOException {
        if (bufferSize > 0) {
            int n = (int)(filePos - pos);
            if (n >= 0 && n <= buf_end) {
                buf_pos = buf_end - n;
            }
            else {
                super.seek(pos);
                invalidate();
            }
            return;
        }
        super.seek(pos);
    }
}
