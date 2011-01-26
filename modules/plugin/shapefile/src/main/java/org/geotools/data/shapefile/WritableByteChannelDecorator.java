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
import java.nio.channels.WritableByteChannel;

/**
 * A WritableByteChannel that delegates all calls to the underlying
 * WritableByteChannel but for {@link #close()} it also calls
 * ShapefileFiles.unlock method to release the lock on the URL.
 * 
 * @author jesse
 *
 * @source $URL$
 */
public class WritableByteChannelDecorator implements WritableByteChannel {

    private final WritableByteChannel wrapped;
    private final ShpFiles shapefileFiles;
    private final URL url;
    private final FileWriter requestor;
    private boolean closed;

    public WritableByteChannelDecorator(WritableByteChannel newChannel,
            ShpFiles shapefileFiles, URL url, FileWriter requestor) {
        this.wrapped = newChannel;
        this.shapefileFiles = shapefileFiles;
        this.url = url;
        this.requestor = requestor;
        closed = false;
    }

    public int write(ByteBuffer src) throws IOException {
        return wrapped.write(src);
    }

    public void close() throws IOException {
        try {
            wrapped.close();
        } finally {
            if (!closed) {
                closed = true;
                shapefileFiles.unlockWrite(url, requestor);
            }
        }
    }

    public boolean isOpen() {
        return wrapped.isOpen();
    }

}
