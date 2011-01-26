package org.geotools.arcsde.raster.io;

import org.geotools.arcsde.raster.info.RasterDatasetInfo;
import org.geotools.arcsde.session.ISessionPool;
import org.opengis.coverage.grid.GridEnvelope;

public class TileReaderFactory {

    /**
     * 
     * @param preparedQuery
     * @param row
     * @param nativeType
     * @param targetType
     * @param noDataValues
     * @param numberOfBands
     * @param tileRange
     * @param tileSize
     * @return
     */
    public static TileReader getInstance(final ISessionPool sessionPool,
            final RasterDatasetInfo rasterInfo, final long rasterId, final int pyramidLevel,
            final GridEnvelope tileRange) {

        final TileReader tileReader;

        TileReader nativeTileReader = new NativeTileReader(sessionPool, rasterInfo, rasterId,
                pyramidLevel, tileRange);

        tileReader = nativeTileReader;

        return tileReader;
    }
}
