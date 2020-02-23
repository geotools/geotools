/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2009, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.arcsde.raster.io;

import java.io.IOException;
import org.geotools.arcsde.raster.info.RasterDatasetInfo;
import org.geotools.arcsde.session.ISessionPool;

/**
 * @author Gabriel Roldan
 * @since 2.5.7
 */
public class RasterReaderFactory {

    private final ISessionPool sessionPool;

    public RasterReaderFactory(final ISessionPool connectionPool) {
        this.sessionPool = connectionPool;
    }

    /**
     * Creates a {@link TiledRasterReader} that's able to read one or more raster for the given
     * {@link RasterDatasetInfo}, depending on if {@code rasterInfo} represents a single raster or a
     * raster catalog.
     */
    public TiledRasterReader create(final RasterDatasetInfo rasterInfo) throws IOException {
        TiledRasterReader rasterReader = new DefaultTiledRasterReader(sessionPool, rasterInfo);

        return rasterReader;
    }
}
