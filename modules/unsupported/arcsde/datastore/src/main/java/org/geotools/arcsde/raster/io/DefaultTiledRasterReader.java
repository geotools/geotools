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

import com.esri.sde.sdk.client.SeQuery;
import java.awt.image.ColorModel;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.io.IOException;
import javax.imageio.ImageTypeSpecifier;
import org.geotools.arcsde.raster.info.RasterDatasetInfo;
import org.geotools.arcsde.raster.jai.ArcSDEPlanarImage;
import org.geotools.arcsde.session.ISessionPool;
import org.opengis.coverage.grid.GridEnvelope;

/**
 * The default implementation for {@link TiledRasterReader}.
 *
 * <p>This implementation holds a connection and an open {@link SeQuery query} until the reader is
 * exhausted or {@link #dispose()} is called.
 *
 * @author Gabriel Roldan
 * @version $Id$
 * @since 2.5.7
 */
class DefaultTiledRasterReader implements TiledRasterReader {

    private RasterDatasetInfo rasterInfo;

    private final ISessionPool sessionPool;

    /**
     * Creates an {@link DefaultTiledRasterReader} that uses the given connection to fetch raster
     * data for the given {@link RasterDatasetInfo rasterInfo}.
     *
     * <p>
     *
     * @param sessionPool where to grab sessions from to query the rasters described by {@code
     *     rasterInfo}
     */
    public DefaultTiledRasterReader(
            final ISessionPool sessionPool, final RasterDatasetInfo rasterInfo) throws IOException {
        this.sessionPool = sessionPool;
        this.rasterInfo = rasterInfo;
    }

    /** @see org.geotools.arcsde.raster.io.TiledRasterReader#read */
    public RenderedImage read(
            final long rasterId, final int pyramidLevel, final GridEnvelope tileRange)
            throws IOException {

        final TileReader tileReader;
        tileReader =
                TileReaderFactory.getInstance(
                        sessionPool, rasterInfo, rasterId, pyramidLevel, tileRange);

        // covers an area of full tiles
        final RenderedImage fullTilesRaster;

        /*
         * Create the tiled raster covering the full area of the matching tiles
         */

        fullTilesRaster = createTiledRaster(tileReader, tileRange, rasterId);

        return fullTilesRaster;
    }

    /**
     * Creates an image representing the whole pyramid level but with a tile reader ready to read
     * only the required tiles, the calling code is responsible for cropping it as needed
     */
    private RenderedImage createTiledRaster(
            final TileReader tileReader, final GridEnvelope tileRange, final long rasterId)
            throws IOException {
        // Prepare temporary colorModel and sample model, needed to build the final
        // ArcSDEPyramidLevel level;
        final ColorModel colorModel;
        final SampleModel sampleModel;
        final int tileWidth = rasterInfo.getTileWidth(rasterId);
        final int tileHeight = rasterInfo.getTileHeight(rasterId);
        final int rasterIndex = rasterInfo.getRasterIndex(rasterId);
        final int pyramidLevel = tileReader.getPyramidLevel();
        final int numTilesWide = rasterInfo.getNumTilesWide(rasterIndex, pyramidLevel);
        final int numTilesHigh = rasterInfo.getNumTilesHigh(rasterIndex, pyramidLevel);
        final int tiledImageWidth = numTilesWide * tileWidth;
        final int tiledImageHeight = numTilesHigh * tileHeight;
        {
            final ImageTypeSpecifier fullImageSpec = rasterInfo.getRenderedImageSpec(rasterId);
            colorModel = fullImageSpec.getColorModel();
            sampleModel =
                    fullImageSpec
                            .getSampleModel()
                            .createCompatibleSampleModel(tileWidth, tileHeight);
        }

        {
            RenderedImage image;
            {
                int minX = 0; // gridRange.x;
                int minY = 0; // gridRange.y;
                int width = tiledImageWidth; // gridRange.width;
                int height = tiledImageHeight; // gridRange.height;
                int tileGridXOffset = 0;
                int tileGridYOffset = 0;
                SampleModel tileSampleModel = sampleModel;

                image =
                        new ArcSDEPlanarImage(
                                tileReader,
                                minX,
                                minY,
                                width,
                                height,
                                tileGridXOffset,
                                tileGridYOffset,
                                tileSampleModel,
                                colorModel);
            }

            return image;
        }
    }
}
