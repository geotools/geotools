/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.shapefile;

import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;

/**
 * A ReadableByteChannel that delegates all calls to the underlying
 * ReadableByteChannel but for {@link #close()} it also calls
 * ShapefileFiles.unlock method to release the lock on the URL.
 * 
 * @author jesse
 *
 * @source $URL$
 */
public class ReadableByteChannelDecorator implements ReadableByteChannel {

    private final ReadableByteChannel wrapped;
    private final ShpFiles shapefileFiles;
    private final URL url;
    private final FileReader requestor;
    private boolean closed;

    public ReadableByteChannelDecorator(ReadableByteChannel newChannel,
            ShpFiles shapefileFiles, URL url, FileReader requestor) {
        this.wrapped = newChannel;
        this.shapefileFiles = shapefileFiles;
        this.url = url;
        this.requestor = requestor;
        this.closed = false;
    }

    public void close() throws IOException {
        try {
            wrapped.close();
        } finally {
            if (!closed) {
                closed = true;
                shapefileFiles.unlockRead(url, requestor);
            }
        }
    }

    public boolean isOpen() {
        return wrapped.isOpen();
    }

    public int read(ByteBuffer dst) throws IOException {
        return wrapped.read(dst);
    }

}
