package org.geotools.arcsde.raster.io;

import java.io.IOException;

import org.geotools.arcsde.raster.info.RasterCellType;
import org.geotools.arcsde.session.Command;
import org.geotools.arcsde.session.ISession;

import com.esri.sde.sdk.client.SeConnection;
import com.esri.sde.sdk.client.SeException;
import com.esri.sde.sdk.client.SeRasterTile;
import com.esri.sde.sdk.client.SeRow;

/**
 * Command to fetch an {@link SeRasterTile tile}
 * 
 * @author Gabriel Roldan (OpenGeo)
 * @since 2.5.8
 * @source $URL:
 *         http://svn.osgeo.org/geotools/trunk/modules/plugin/arcsde/datastore/src/main/java/org
 *         /geotools/arcsde/raster/io/TileFetchCommand.java $
 */
class TileFetchCommand extends Command<SeRasterTile> {

    private final SeRow row;

    public TileFetchCommand(final SeRow row, final RasterCellType nativeType) {
        this.row = row;
    }

    @Override
    public SeRasterTile execute(ISession session, SeConnection connection) throws SeException,
            IOException {

        SeRasterTile tile = row.getRasterTile();

        return tile;// may be null indicating EOF
    }
}