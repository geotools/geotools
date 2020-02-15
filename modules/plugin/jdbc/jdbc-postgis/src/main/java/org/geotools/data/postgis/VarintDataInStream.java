/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
 *
 */

package org.geotools.data.postgis;

import java.io.IOException;
import org.locationtech.jts.io.InStream;

/**
 * Wrapper around a {@link InStream} that can read bytes and <a
 * href="https://developers.google.com/protocol-buffers/docs/encoding#varints">varints</a> and a <a
 * href="https://developers.google.com/protocol-buffers/docs/encoding#signed-integers">signed
 * varint</a>
 */
public class VarintDataInStream {

    InStream stream;
    private byte[] buf1 = new byte[1];

    /** Constructor allowing to set the stream later, will NPE if the stream is not set */
    public VarintDataInStream() {}

    /** Builds the varint data stream with the given stream */
    public VarintDataInStream(InStream stream) {
        this.stream = stream;
    }

    /** Sets a new InStream to delegate reads to */
    public void setInStream(InStream stream) {
        this.stream = stream;
    }

    /** Reads a byte */
    public byte readByte() throws IOException {
        stream.read(buf1);
        return buf1[0];
    }

    /** Reads an un-signed varinteger */
    public int readUnsignedInt() throws IOException {
        // try to read the first without initializing the loop (for short values, which we expect)
        stream.read(buf1);
        byte b = buf1[0];
        if ((b & 0x80) == 0) {
            return b;
        }

        // loop up to 32 bits
        int val = b & 0x7f;
        int shift = 7;
        for (; shift < 32; shift += 7) {
            stream.read(buf1);
            b = buf1[0];
            val = val | (b & 0x7f) << shift;
            if ((b & 0x80) == 0) {
                return val;
            }
        }
        // read possible extra bytes, but ignore them
        for (; shift < 64; shift += 7) {
            stream.read(buf1);
            b = buf1[0];
            if ((b & 0x80) == 0) {
                return val;
            }
        }

        throw new IllegalArgumentException("Invalid varint found, used more than 64 bits");
    }

    /** Reads an signed varinteger */
    public int readSignedInt() throws IOException {
        int val = readUnsignedInt();
        return unzigzag(val);
    }

    private int unzigzag(int n) {
        return n >>> 1 ^ -(n & 1);
    }
}
