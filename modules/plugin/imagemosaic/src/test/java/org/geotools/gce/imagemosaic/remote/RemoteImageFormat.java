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

import java.io.IOException;
import java.net.URL;
import org.geotools.TestData;
import org.geotools.api.coverage.grid.Format;
import org.geotools.api.coverage.grid.GridCoverageWriter;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.imageio.GeoToolsWriteParams;
import org.geotools.gce.image.WorldImageFormat;
import org.geotools.gce.image.WorldImageReader;
import org.geotools.gce.imagemosaic.RemoteTest;
import org.geotools.util.factory.Hints;

public class RemoteImageFormat extends AbstractGridFormat implements Format {

    private WorldImageFormat delegate = new WorldImageFormat();

    @Override
    public WorldImageReader getReader(Object source) {
        return getReader(source, null);
    }

    @Override
    public WorldImageReader getReader(Object source, Hints hints) {
        try {
            return /*new RemoteImageReader(*/ delegate.getReader(
                    TestData.file(RemoteTest.class, "remote_test" + ((URL) source).getPath()), hints) /*)*/;
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public boolean accepts(Object source, Hints hints) {
        return source instanceof URL
                && ((URL) source).getProtocol().equals("http")
                && ((URL) source).getHost().equals("localhost")
                && ((URL) source).getPort() == 8089;
    }

    @Override
    public GridCoverageWriter getWriter(Object destination) {
        throw new UnsupportedOperationException();
    }

    @Override
    public GeoToolsWriteParams getDefaultImageIOWriteParameters() {
        throw new UnsupportedOperationException();
    }

    @Override
    public GridCoverageWriter getWriter(Object destination, Hints hints) {
        throw new UnsupportedOperationException();
    }
}
