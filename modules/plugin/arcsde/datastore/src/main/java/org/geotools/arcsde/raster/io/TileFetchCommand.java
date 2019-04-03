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
 */
package org.geotools.arcsde.raster.io;

import com.esri.sde.sdk.client.SeConnection;
import com.esri.sde.sdk.client.SeException;
import com.esri.sde.sdk.client.SeRasterTile;
import com.esri.sde.sdk.client.SeRow;
import java.io.IOException;
import org.geotools.arcsde.raster.info.RasterCellType;
import org.geotools.arcsde.session.Command;
import org.geotools.arcsde.session.ISession;

/**
 * Command to fetch an {@link SeRasterTile tile}
 *
 * @author Gabriel Roldan (OpenGeo)
 * @since 2.5.8
 */
class TileFetchCommand extends Command<SeRasterTile> {

    private final SeRow row;

    public TileFetchCommand(final SeRow row, final RasterCellType nativeType) {
        this.row = row;
    }

    @Override
    public SeRasterTile execute(ISession session, SeConnection connection)
            throws SeException, IOException {

        SeRasterTile tile = row.getRasterTile();

        return tile; // may be null indicating EOF
    }
}
