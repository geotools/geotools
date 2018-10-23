/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.imagemosaic.remote;

import it.geosolutions.imageio.stream.input.spi.FileImageInputStreamExtImplSpi;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import javax.imageio.spi.ImageInputStreamSpi;
import javax.imageio.stream.ImageInputStream;
import org.geotools.TestData;
import org.geotools.gce.imagemosaic.RemoteTest;

public class RemoteImageInputStreamSPI extends ImageInputStreamSpi {

    private FileImageInputStreamExtImplSpi delegate = new FileImageInputStreamExtImplSpi();

    public RemoteImageInputStreamSPI() {
        super("geotools", "0.0.1", URL.class);
    }

    @Override
    public ImageInputStream createInputStreamInstance(Object input, boolean useCache, File cacheDir)
            throws IOException {
        try {
            return delegate.createInputStreamInstance(
                    TestData.file(RemoteTest.class, "remote_test" + ((URL) input).getPath()),
                    useCache,
                    cacheDir);
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    @Override
    public String getDescription(Locale locale) {
        return "Remote Test Image InputStream SPI";
    }
}
