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
 *
 */
package org.geotools.arcsde.raster.info;

import static org.geotools.arcsde.raster.info.RasterCellType.TYPE_16BIT_S;
import static org.geotools.arcsde.raster.info.RasterCellType.TYPE_16BIT_U;
import static org.geotools.arcsde.raster.info.RasterCellType.TYPE_1BIT;
import static org.geotools.arcsde.raster.info.RasterCellType.TYPE_32BIT_REAL;
import static org.geotools.arcsde.raster.info.RasterCellType.TYPE_32BIT_S;
import static org.geotools.arcsde.raster.info.RasterCellType.TYPE_32BIT_U;
import static org.geotools.arcsde.raster.info.RasterCellType.TYPE_4BIT;
import static org.geotools.arcsde.raster.info.RasterCellType.TYPE_64BIT_REAL;
import static org.geotools.arcsde.raster.info.RasterCellType.TYPE_8BIT_U;

import com.sun.media.imageioimpl.common.BogusColorSpace;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.geom.Rectangle2D;
import java.awt.image.BandedSampleModel;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.IndexColorModel;
import java.awt.image.SampleModel;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.imageio.ImageTypeSpecifier;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.io.OverviewPolicy;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.image.util.ColorUtilities;
import org.geotools.image.util.ComponentColorModelJAI;
import org.geotools.referencing.CRS;
import org.geotools.util.NumberRange;
import org.geotools.util.logging.Logging;
import org.opengis.coverage.grid.GridEnvelope;
import org.opengis.geometry.Envelope;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.MathTransform2D;
import org.opengis.referencing.operation.NoninvertibleTransformException;
import org.opengis.referencing.operation.TransformException;

/**
 * @author Gabriel Roldan (OpenGeo)
 * @since 2.5.4
 */
@SuppressWarnings({"nls"})
public class RasterUtils {

    private static final Logger LOGGER = Logging.getLogger(RasterUtils.class);

    private RasterUtils() {
        // do nothing
    }

    /**
     * Returns the grid range specifying the matching tiles for a given pyramid level and grid
     * extent specifying the overlapping area to request in the level's pixel space.
     *
     * @return a grid range holding the coordinates in tile space that fully covers the requested
     *     pixel range for the given pyramid level, or a negative area range
     */
    private static GridEnvelope findMatchingTiles(
            final Dimension tileSize,
            int numTilesWide,
            int numTilesHigh,
            final GridEnvelope pixelRange) {

        final int minPixelX = pixelRange.getLow(0);
        final int minPixelY = pixelRange.getLow(1);

        int minTileX = (int) Math.floor(minPixelX / tileSize.getWidth());
        int minTileY = (int) Math.floor(minPixelY / tileSize.getHeight());

        int numTilesX = (int) Math.ceil(pixelRange.getSpan(0) / tileSize.getWidth());
        int numTilesY = (int) Math.ceil(pixelRange.getSpan(1) / tileSize.getHeight());

        int maxTiledX = (minTileX + numTilesX) * tileSize.width;
        int maxTiledY = (minTileY + numTilesY) * tileSize.height;

        if (maxTiledX < pixelRange.getHigh(0) && (minTileX + numTilesX) < numTilesWide) {
            numTilesX++;
        }

        if (maxTiledY < pixelRange.getHigh(1) && (minTileY + numTilesY) < numTilesHigh) {
            numTilesY++;
        }

        GridEnvelope2D matchingTiles = new GridEnvelope2D(minTileX, minTileY, numTilesX, numTilesY);
        return matchingTiles;
    }

    private static GridEnvelope getTargetGridRange(
            final MathTransform modelToRaster, final Envelope requestedEnvelope) {
        GridEnvelope levelOverlappingPixels;
        int levelMinPixelX;
        int levelMaxPixelX;
        int levelMinPixelY;
        int levelMaxPixelY;
        {
            // use a model to raster transform to find out which pixel range at the specified level
            // better match the requested extent
            GeneralEnvelope requestedPixels;
            try {
                requestedPixels = CRS.transform(modelToRaster, requestedEnvelope);
            } catch (NoninvertibleTransformException e) {
                throw new IllegalArgumentException(e);
            } catch (TransformException e) {
                throw new IllegalArgumentException(e);
            }

            levelMinPixelX = (int) Math.floor(requestedPixels.getMinimum(0));
            levelMinPixelY = (int) Math.floor(requestedPixels.getMinimum(1));

            levelMaxPixelX = (int) Math.ceil(requestedPixels.getMaximum(0));
            levelMaxPixelY = (int) Math.ceil(requestedPixels.getMaximum(1));

            final int width = levelMaxPixelX - levelMinPixelX;
            final int height = levelMaxPixelY - levelMinPixelY;
            levelOverlappingPixels =
                    new GridEnvelope2D(levelMinPixelX, levelMinPixelY, width, height);
        }
        return levelOverlappingPixels;
    }

    /**
     * Creates an IndexColorModel out of a DataBuffer obtained from an ArcSDE's raster color map.
     */
    public static IndexColorModel sdeColorMapToJavaColorModel(
            final DataBuffer colorMapData, final int bitsPerSample) {
        if (colorMapData == null) {
            throw new NullPointerException("colorMapData");
        }

        if (colorMapData.getNumBanks() < 3 || colorMapData.getNumBanks() > 4) {
            throw new IllegalArgumentException(
                    "colorMapData shall have 3 or 4 banks: " + colorMapData.getNumBanks());
        }

        if (bitsPerSample != 8 && bitsPerSample != 16) {
            throw new IllegalAccessError(
                    "bits per sample shall be either 8 or 16. Got " + bitsPerSample);
        }

        final int numBanks = colorMapData.getNumBanks();
        final int mapSize = colorMapData.getSize();

        byte[] r = new byte[mapSize];
        byte[] g = new byte[mapSize];
        byte[] b = new byte[mapSize];
        byte[] a = new byte[mapSize];

        for (int i = 0; i < mapSize; i++) {
            r[i] = (byte) (colorMapData.getElem(0, i) & 0xFF);
            g[i] = (byte) (colorMapData.getElem(1, i) & 0xFF);
            b[i] = (byte) (colorMapData.getElem(2, i) & 0xFF);
            a[i] = (byte) ((numBanks == 3 ? 255 : colorMapData.getElem(3, i)) & 0xFF);
        }

        IndexColorModel colorModel = new IndexColorModel(bitsPerSample, mapSize, r, g, b, a);

        return colorModel;
    }

    public static ImageTypeSpecifier createFullImageTypeSpecifier(
            final RasterDatasetInfo rasterInfo, final int rasterIndex) {

        final int numberOfBands = rasterInfo.getNumBands();
        final RasterCellType nativePixelType = rasterInfo.getNativeCellType();
        final RasterCellType pixelType = rasterInfo.getTargetCellType(rasterIndex);

        // Prepare temporary colorModel and sample model, needed to build the final
        // ArcSDEPyramidLevel level;
        final int sampleImageWidth = 1; // rasterInfo.getImageWidth();
        final int sampleImageHeight = 1; // rasterInfo.getImageHeight();

        final ImageTypeSpecifier its;
        // treat special cases...
        final int bitsPerSample = pixelType.getBitsPerSample();
        final int dataType = pixelType.getDataBufferType();
        final boolean hasColorMap = rasterInfo.isColorMapped();

        if (hasColorMap) {
            // special case, a single band colormapped image
            IndexColorModel colorMap = rasterInfo.getColorMap(rasterIndex);
            its = createColorMappedImageSpec(colorMap, sampleImageWidth, sampleImageHeight);

        } else if (nativePixelType == TYPE_1BIT && numberOfBands == 1) {
            byte noDataValue = rasterInfo.getNoDataValue(rasterIndex, 0).byteValue();
            // special case, a single band 1-bit
            its =
                    createOneBitColorMappedImageSpec(
                            sampleImageWidth, sampleImageHeight, noDataValue);

        } else if (nativePixelType == TYPE_4BIT && numberOfBands == 1) {
            byte noDataValue = rasterInfo.getNoDataValue(rasterIndex, 0).byteValue();
            // special case, a single band 4-bit
            its =
                    createFourBitColorMappedImageSpec(
                            sampleImageWidth, sampleImageHeight, noDataValue);
        } else if (numberOfBands == 1) {
            // special case, a single band grayscale image, no matter the pixel depth
            its =
                    createGrayscaleImageSpec(
                            sampleImageWidth, sampleImageHeight, dataType, bitsPerSample);

        } else if (numberOfBands == 3 && pixelType == TYPE_8BIT_U) {
            // special case, an optimizable RGB image
            its = createRGBImageSpec(sampleImageWidth, sampleImageHeight, dataType);

        } else if (numberOfBands == 4 && pixelType == TYPE_8BIT_U) {
            // special case, an optimizable RGBA image
            its = createRGBAImageSpec(sampleImageWidth, sampleImageHeight, dataType);

        } else {
            /*
             * not an special case, go for a more generic sample model, potentially slower than the
             * special case ones, but that'll work anyway
             */

            final ColorModel colorModel;
            final SampleModel sampleModel;
            {
                final ColorSpace colorSpace;
                colorSpace = new BogusColorSpace(numberOfBands);
                int[] numBits = new int[numberOfBands];
                for (int i = 0; i < numberOfBands; i++) {
                    numBits[i] = bitsPerSample;
                }
                colorModel =
                        new ComponentColorModelJAI(
                                colorSpace, numBits, false, false, Transparency.OPAQUE, dataType);
            }
            {
                int[] bankIndices = new int[numberOfBands];
                int[] bandOffsets = new int[numberOfBands];
                // int bandOffset = (tileWidth * tileHeight * pixelType.getBitsPerSample()) / 8;
                for (int i = 0; i < numberOfBands; i++) {
                    bankIndices[i] = i;
                    bandOffsets[i] = 0; // (i * bandOffset);
                }
                sampleModel =
                        new BandedSampleModel(
                                dataType,
                                sampleImageWidth,
                                sampleImageHeight,
                                sampleImageWidth,
                                bankIndices,
                                bandOffsets);
            }
            its = new ImageTypeSpecifier(colorModel, sampleModel);
        }

        return its;
    }

    private static ImageTypeSpecifier createFourBitColorMappedImageSpec(
            int sampleImageWidth, int sampleImageHeight, byte noDataValue) {

        int maxValue = (int) TYPE_4BIT.getSampleValueRange().getMaximum();

        int mapSize = noDataValue > maxValue ? noDataValue : maxValue + 1;

        int[] cmap = new int[mapSize];
        ColorUtilities.expand(new Color[] {Color.BLACK, Color.WHITE}, cmap, 0, maxValue);

        for (int i = maxValue; i < mapSize; i++) {
            cmap[i] = ColorUtilities.getIntFromColor(0, 0, 0, 0);
        }

        int transparentPixel = noDataValue;
        IndexColorModel colorModel =
                new IndexColorModel(
                        8, mapSize, cmap, 0, true, transparentPixel, DataBuffer.TYPE_BYTE);

        SampleModel sampleModel =
                colorModel.createCompatibleSampleModel(sampleImageWidth, sampleImageHeight);
        ImageTypeSpecifier its = new ImageTypeSpecifier(colorModel, sampleModel);
        return its;
    }

    private static ImageTypeSpecifier createOneBitColorMappedImageSpec(
            int sampleImageWidth, int sampleImageHeight, byte noDataValue) {

        assert noDataValue == 2;

        final int FALSE = ColorUtilities.getIntFromColor(255, 255, 255, 255);
        final int TRUE = ColorUtilities.getIntFromColor(0, 0, 0, 255);
        final int NODATA = ColorUtilities.getIntFromColor(255, 255, 255, 0);

        final int mapSize = 3;
        int[] cmap = new int[mapSize];
        cmap[0] = FALSE;
        cmap[1] = TRUE;
        cmap[2] = NODATA;

        int transparentPixel = noDataValue;
        IndexColorModel colorModel =
                new IndexColorModel(
                        8, mapSize, cmap, 0, false, transparentPixel, DataBuffer.TYPE_BYTE);

        SampleModel sampleModel =
                colorModel.createCompatibleSampleModel(sampleImageWidth, sampleImageHeight);
        ImageTypeSpecifier its = new ImageTypeSpecifier(colorModel, sampleModel);
        return its;
    }

    private static ImageTypeSpecifier createRGBAImageSpec(
            int sampleImageWidth, int sampleImageHeight, final int dataType) {

        final ImageTypeSpecifier its;

        ColorSpace colorSpace = ColorSpace.getInstance(ColorSpace.CS_sRGB);
        boolean hasAlpha = true;
        boolean isAlphaPremultiplied = false;
        int transparency = Transparency.TRANSLUCENT;
        int transferType = dataType;

        int[] nBits = {8, 8, 8, 8};
        ColorModel colorModel =
                new ComponentColorModelJAI(
                        colorSpace,
                        nBits,
                        hasAlpha,
                        isAlphaPremultiplied,
                        transparency,
                        transferType);

        /*
         * Do not use colorModel.createCompatibleSampleModel cause it creates a
         * PixelInterleavedSampleModel and we need a BandedSampleModel so it matches how the data
         * comes out of ArcSDE
         */
        SampleModel sampleModel =
                new BandedSampleModel(dataType, sampleImageWidth, sampleImageHeight, 4);

        its = new ImageTypeSpecifier(colorModel, sampleModel);
        return its;
    }

    private static ImageTypeSpecifier createRGBImageSpec(
            int sampleImageWidth, int sampleImageHeight, final int dataType) {

        final ImageTypeSpecifier its;
        ColorSpace colorSpace = ColorSpace.getInstance(ColorSpace.CS_sRGB);
        boolean hasAlpha = false;
        boolean isAlphaPremultiplied = false;
        int transparency = Transparency.OPAQUE;
        int transferType = dataType;
        ColorModel colorModel =
                new ComponentColorModel(
                        colorSpace,
                        new int[] {8, 8, 8},
                        hasAlpha,
                        isAlphaPremultiplied,
                        transparency,
                        transferType);

        SampleModel sampleModel =
                new BandedSampleModel(dataType, sampleImageWidth, sampleImageHeight, 3);

        its = new ImageTypeSpecifier(colorModel, sampleModel);
        return its;
    }

    private static ImageTypeSpecifier createGrayscaleImageSpec(
            int sampleImageWidth, int sampleImageHeight, final int dataType, int bitsPerPixel) {
        final ImageTypeSpecifier its;
        ColorSpace colorSpace = ColorSpace.getInstance(ColorSpace.CS_GRAY);
        boolean hasAlpha = false;
        boolean isAlphaPremultiplied = false;
        int transparency = Transparency.OPAQUE;
        int transferType = dataType;
        int[] nbits = {bitsPerPixel};
        ColorModel colorModel =
                new ComponentColorModelJAI(
                        colorSpace,
                        nbits,
                        hasAlpha,
                        isAlphaPremultiplied,
                        transparency,
                        transferType);

        SampleModel sampleModel =
                colorModel.createCompatibleSampleModel(sampleImageWidth, sampleImageHeight);
        its = new ImageTypeSpecifier(colorModel, sampleModel);
        return its;
    }

    private static ImageTypeSpecifier createColorMappedImageSpec(
            final IndexColorModel colorModel, int sampleImageWidth, int sampleImageHeight) {

        final SampleModel sampleModel;
        final ImageTypeSpecifier its;
        LOGGER.fine("Found single-band colormapped raster, using its index color model");
        sampleModel = colorModel.createCompatibleSampleModel(sampleImageWidth, sampleImageHeight);
        its = new ImageTypeSpecifier(colorModel, sampleModel);
        return its;
    }

    /**
     * Given a collection of {@link RasterQueryInfo} instances holding information about how a
     * request fits for each individual raster composing a catalog, figure out where their resulting
     * images fit into the overall mosaic that's gonna be the result of the request.
     */
    public static GridEnvelope setMosaicLocations(
            final RasterDatasetInfo rasterInfo, final List<RasterQueryInfo> results) {
        final MathTransform modelToRaster;
        final MathTransform rasterToModel;
        int minx = Integer.MAX_VALUE;
        int miny = Integer.MAX_VALUE;
        int maxx = Integer.MIN_VALUE;
        int maxy = Integer.MIN_VALUE;
        {
            /*
             * Of all the rasters that match the requested envelope, chose the one with the lowest
             * resolution as the base to compute the final mosaic layout, so we avoid JAI upsamples,
             * which are buggy and produce repeated patterns over the x axis instead of just scaling
             * up the image.
             */
            RasterQueryInfo dimensionChoice = findLowestResolution(results);
            Long rasterId = dimensionChoice.getRasterId();
            int pyramidLevel = dimensionChoice.getPyramidLevel();
            int rasterIndex = rasterInfo.getRasterIndex(rasterId);
            rasterToModel = rasterInfo.getRasterToModel(rasterIndex, pyramidLevel);
            try {
                modelToRaster = rasterToModel.inverse();
            } catch (NoninvertibleTransformException e) {
                throw new RuntimeException(e);
            }
        }

        for (RasterQueryInfo rasterResultInfo : results) {
            final GeneralEnvelope rasterResultEnvelope = rasterResultInfo.getResultEnvelope();

            final GridEnvelope targetRasterGridRange;
            targetRasterGridRange = getTargetGridRange(modelToRaster, rasterResultEnvelope);

            rasterResultInfo.setMosaicLocation(targetRasterGridRange);

            minx = Math.min(minx, targetRasterGridRange.getLow(0));
            miny = Math.min(miny, targetRasterGridRange.getLow(1));
            maxx = Math.max(maxx, targetRasterGridRange.getHigh(0));
            maxy = Math.max(maxy, targetRasterGridRange.getHigh(1));
        }

        final GridEnvelope2D mosaicDimension;
        mosaicDimension = new GridEnvelope2D(minx, miny, 1 + (maxx - minx), 1 + (maxy - miny));
        return mosaicDimension;
    }

    public static RasterQueryInfo findLowestResolution(List<RasterQueryInfo> results) {
        double[] prev = {Double.MIN_VALUE, Double.MIN_VALUE};
        RasterQueryInfo lowestResQuery = null;

        double[] curr;
        for (RasterQueryInfo query : results) {
            curr = query.getResolution();
            if (curr[0] > prev[0]) {
                prev = curr;
                lowestResQuery = query;
            }
        }
        return lowestResQuery;
    }

    /**
     * Find out the raster ids and their pyramid levels in the raster dataset for the rasters whose
     * envelope overlaps the requested one
     */
    public static List<RasterQueryInfo> findMatchingRasters(
            final RasterDatasetInfo rasterInfo,
            final GeneralEnvelope requestedEnvelope,
            final GridEnvelope requestedDim,
            final OverviewPolicy overviewPolicy) {

        final int numRasters = rasterInfo.getNumRasters();
        List<RasterQueryInfo> matchingRasters = new ArrayList<RasterQueryInfo>(numRasters);

        int optimalPyramidLevel;
        GeneralEnvelope gridEnvelope;
        for (int rasterN = 0; rasterN < numRasters; rasterN++) {
            optimalPyramidLevel =
                    rasterInfo.getOptimalPyramidLevel(
                            rasterN, overviewPolicy, requestedEnvelope, requestedDim);
            gridEnvelope = rasterInfo.getGridEnvelope(rasterN, optimalPyramidLevel);
            final boolean edgesInclusive = true;
            if (requestedEnvelope.intersects(gridEnvelope, edgesInclusive)) {
                RasterQueryInfo match = new RasterQueryInfo();
                match.setRequestedEnvelope(requestedEnvelope);
                match.setRequestedDim(requestedDim);

                match.setRasterId(rasterInfo.getRasterId(rasterN));
                match.setRasterIndex(rasterN);
                match.setPyramidLevel(optimalPyramidLevel);
                match.setResolution(rasterInfo.getResolution(rasterN, optimalPyramidLevel));
                matchingRasters.add(match);
            }
        }
        return matchingRasters;
    }

    public static void fitRequestToRaster(
            final GeneralEnvelope requestedEnvelope,
            final RasterDatasetInfo rasterInfo,
            final RasterQueryInfo query) {

        GridEnvelope resultGridRange;
        GeneralEnvelope resultEnvelope;
        // GridEnvelope resultDimensionInsideTiledImage;
        GridEnvelope tiledImageGridRange;
        // GridEnvelope levelTileRange;
        GridEnvelope matchingTiles;

        int rasterIndex = query.getRasterIndex();
        int pyramidLevel = query.getPyramidLevel();
        MathTransform2D rasterToModel =
                (MathTransform2D) rasterInfo.getRasterToModel(rasterIndex, pyramidLevel);
        MathTransform2D modelToRaster;

        try {
            modelToRaster = (MathTransform2D) rasterToModel.inverse();
            final GeneralEnvelope adjustedGRange = CRS.transform(modelToRaster, requestedEnvelope);

            int xmin = (int) Math.floor(adjustedGRange.getMinimum(0));
            int ymin = (int) Math.floor(adjustedGRange.getMinimum(1));
            int xmax = (int) Math.ceil(adjustedGRange.getMaximum(0));
            int ymax = (int) Math.ceil(adjustedGRange.getMaximum(1));

            final GridEnvelope levelRange = rasterInfo.getGridRange(rasterIndex, pyramidLevel);

            xmin = Math.max(xmin, levelRange.getLow(0));
            ymin = Math.max(ymin, levelRange.getLow(1));
            xmax = Math.min(xmax, levelRange.getHigh(0) + 1); // 1+ because getHigh is inclusive
            ymax = Math.min(ymax, levelRange.getHigh(1) + 1); // 1+ because getHigh is inclusive

            int width = xmax - xmin;
            int height = ymax - ymin;

            resultGridRange = new GridEnvelope2D(xmin, ymin, width, height);
            Rectangle2D finalEnvelope =
                    CRS.transform(
                            rasterToModel, (Rectangle2D) resultGridRange, new Rectangle2D.Double());

            resultEnvelope = new GeneralEnvelope(finalEnvelope);
            CoordinateReferenceSystem crs = rasterInfo.getCoverageCrs();
            resultEnvelope.setCoordinateReferenceSystem(crs);
        } catch (TransformException e) {
            throw new RuntimeException(e);
        }

        matchingTiles = findMatchingTiles(rasterInfo, rasterIndex, pyramidLevel, resultGridRange);
        tiledImageGridRange =
                getTiledImageGridRange(rasterInfo.getTileDimension(rasterIndex), matchingTiles);

        query.setResultEnvelope(resultEnvelope);
        query.setResultGridRange(resultGridRange);
        query.setMatchingTiles(matchingTiles);
        query.setTiledImageGridRange(tiledImageGridRange);

        // query.setResultDimensionInsideTiledImage(resultDimensionInsideTiledImage);
        // query.setLevelTileRange(levelTileRange);
    }

    private static GridEnvelope getTiledImageGridRange(
            Dimension tileSize, GridEnvelope matchingTiles) {

        int tiledImageMinX = (matchingTiles.getLow(0) * tileSize.width);
        int tiledImageMinY = (matchingTiles.getLow(1) * tileSize.height);
        int tiledWidth = (matchingTiles.getSpan(0) * tileSize.width);
        int tiledHeight = (matchingTiles.getSpan(1) * tileSize.height);

        GridEnvelope2D tiledImageGridRange;
        tiledImageGridRange =
                new GridEnvelope2D(tiledImageMinX, tiledImageMinY, tiledWidth, tiledHeight);
        return tiledImageGridRange;
    }

    private static GridEnvelope findMatchingTiles(
            RasterDatasetInfo rasterInfo,
            int rasterIndex,
            int pyramidLevel,
            GridEnvelope resultGridRange) {
        final Dimension tileSize = rasterInfo.getTileDimension(rasterIndex);
        final int numTilesWide = rasterInfo.getNumTilesWide(rasterIndex, pyramidLevel);
        final int numTilesHigh = rasterInfo.getNumTilesHigh(rasterIndex, pyramidLevel);
        GridEnvelope matchingTiles;
        matchingTiles = findMatchingTiles(tileSize, numTilesWide, numTilesHigh, resultGridRange);
        return matchingTiles;
    }

    /**
     * Returns a color model based on {@code colorMap} that's guaranteed to have at least one
     * transparent pixel whose index can be used as no-data value for colormapped rasters, even if
     * the returned IndexColorModel needs to be of a higher sample depth (ie, 16 instead of 8 bit)
     * to satisfy that.
     *
     * @param colorMap the raster's native color map the returned one will be based on
     * @return the same {@code colorMap} if it has a transparent pixel, another, possibly of a
     *     higher depth one if not, containing all the colors from {@code colorMap} and a newly
     *     allocated cell for the transparent pixel if necessary
     */
    public static IndexColorModel ensureNoDataPixelIsAvailable(final IndexColorModel colorMap) {
        int transparentPixel = colorMap.getTransparentPixel();
        if (transparentPixel > -1) {
            return colorMap;
        }

        final int transferType = colorMap.getTransferType();
        final int mapSize = colorMap.getMapSize();
        final int maxSize = 65536; // true for either transfer type

        if (mapSize == maxSize) {
            LOGGER.fine(
                    "There's no room for a new transparent pixel, "
                            + "returning the original colorMap as is");
            return colorMap;
        }

        /*
         * The original map size is lower than the maximum allowed by a UShort color map, so expand
         * the colormap by one and make that new entry transparent
         */
        final int newMapSize = mapSize + 1;
        final int[] argb = new int[newMapSize];
        colorMap.getRGBs(argb);

        // set the last entry as transparent
        argb[newMapSize - 1] = ColorUtilities.getIntFromColor(0, 0, 0, 0);

        IndexColorModel targetColorModel;
        final int significantBits;
        final int newTransferType;

        {
            if (DataBuffer.TYPE_BYTE == transferType && newMapSize <= 256) {
                /*
                 * REVISIT: check if this needs to be promoted depending on whether I decide to
                 * treat 1 and 4 bit images as indexed with 1 and 4 significant bits respectively
                 */
                significantBits = colorMap.getPixelSize();
                newTransferType = DataBuffer.TYPE_BYTE;
            } else if (DataBuffer.TYPE_BYTE == transferType && newMapSize == 257) {
                // it's being promoted. significantBits = 9 makes for a 512 color model instead of a
                // 65535 one saving a good bit of memory, specially for color mapped raster catalogs
                // where the colormodel for each raster in the catalog is to be held in memory
                significantBits = 9;
                newTransferType = DataBuffer.TYPE_USHORT;
            } else {
                // already was 16-bit
                significantBits = 16;
                newTransferType = DataBuffer.TYPE_USHORT;
            }
        }

        final int transparentPixelIndex = newMapSize - 1;
        final boolean hasalpha = true;
        final int startIndex = 0;

        targetColorModel =
                new IndexColorModel(
                        significantBits,
                        newMapSize,
                        argb,
                        startIndex,
                        hasalpha,
                        transparentPixelIndex,
                        newTransferType);

        return targetColorModel;
    }

    /**
     * For a color-mapped raster, the no-data value is set to the {@link
     * IndexColorModel#getTransparentPixel() transparent pixel}
     *
     * @return the index in the colorMap that's the transparent pixel as is to be used as no-data
     *     value
     */
    public static Number determineNoDataValue(IndexColorModel colorMap) {
        int noDataPixel = colorMap.getTransparentPixel();
        if (-1 == noDataPixel) {
            // there were no room for a transparent pixel, find out the closest match
            noDataPixel = ColorUtilities.getTransparentPixel(colorMap);
        }
        return Integer.valueOf(noDataPixel);
    }

    /**
     * @param numBands number of bands in the raster dataset for the band whose nodata value is to
     *     be determined. Might be useful to treat special cases where some assumptions are made
     *     depending on the cell type and number of bands
     * @param statsMin the minimum sample value for the band as reported by the band's statistics,
     *     or {@code NaN}
     * @param statsMax the maximum sample value for the band as reported by the band's statistics,
     *     or {@code NaN}
     * @param nativeCellType the band's native cell type
     */
    public static Number determineNoDataValue(
            final int numBands,
            final double statsMin,
            final double statsMax,
            final RasterCellType nativeCellType) {

        final Number nodata;

        if (nativeCellType == TYPE_32BIT_REAL) {
            LOGGER.fine("no data value is Float.NaN");
            return Float.valueOf(Float.NaN);
        } else if (nativeCellType == TYPE_64BIT_REAL) {
            LOGGER.fine("no data value is Double.NaN");
            return Double.valueOf(Double.NaN);
        } else if (nativeCellType == TYPE_1BIT) {
            LOGGER.fine(
                    "1BIT images no-data value is set to 2,"
                            + " regardless of the raster statistics");
            return Double.valueOf(2);
        } else if (nativeCellType == TYPE_4BIT) {
            LOGGER.fine(
                    "4BIT images no-data value is set to 16,"
                            + " regardless of the raster statistics");
            return Double.valueOf(16);
        } else if (!isGeoPhysics(numBands, nativeCellType)) {
            LOGGER.fine(
                    "3 or 4 band, 8 bit unsigned image, assumed to be "
                            + "RGB or RGBA respectively and nodata value hardcoded to 255");
            return (Number) nativeCellType.getSampleValueRange().getMaxValue();
        }

        final NumberRange<?> sampleValueRange = nativeCellType.getSampleValueRange();

        final double minimumSample = sampleValueRange.getMinimum(true);
        final double maximumSample = sampleValueRange.getMaximum(true);

        double lower;
        double greater;
        if (Double.isNaN(statsMin) || Double.isNaN(statsMax)) {
            lower = Math.ceil(minimumSample - 1);
            greater = Math.floor(maximumSample + 1);
        } else {
            lower = Math.ceil(statsMin - 1);
            greater = Math.floor(statsMax + 1);
        }

        final boolean isUnsigned = minimumSample == 0;

        if (sampleValueRange.contains((Number) Double.valueOf(lower))) {
            // lower is ok
            nodata = lower;
        } else if (sampleValueRange.contains((Number) Double.valueOf(greater))) {
            // upper is ok
            nodata = greater;
        } else if (isUnsigned) {
            // need to set no-data to the higher value, floor is zero
            nodata = greater;
            // if (cellType == TYPE_1BIT || cellType == TYPE_4BIT) {
            // nodata = greater;
            // } else {
            // // best guess without promoting. We don't actually want to promote a raster that is
            // // non
            // // colormapped and either has no statistics or it's range is full to preserve the
            // // cases
            // // were it may affect badly the visualization (for example, a 3 band 8bit raster
            // // promoted to 3 band 16bit is gonna look almost black
            // nodata = maximumSample;
            // }
        } else {
            // no-data as the lower value is ok, floor is non zero (the celltype is signed)
            nodata = lower;
        }

        return nodata;
    }

    public static boolean isGeoPhysics(final int numBands, final RasterCellType nativeCellType) {
        boolean geophysics = true;
        if (nativeCellType == TYPE_8BIT_U && (numBands == 3 || numBands == 4)) {
            geophysics = false;
        }
        return geophysics;
    }

    public static RasterCellType determineTargetCellType(
            final RasterCellType nativeCellType, final List<Number> noDataValues) {

        if (TYPE_32BIT_REAL == nativeCellType || TYPE_64BIT_REAL == nativeCellType) {
            // no data value is NaN, so no need to promote. For other types NaN is not available
            for (Number nodata : noDataValues) {
                if (!Double.isNaN(nodata.doubleValue())) {
                    throw new IllegalArgumentException(
                            "no data values for float and "
                                    + "double cell types shall be NaN: "
                                    + nodata);
                }
            }
            return nativeCellType;
        }

        // find a cell type that's deep enough for all the bands in the given raster
        double noDataMin = Double.POSITIVE_INFINITY, noDataMax = Double.NEGATIVE_INFINITY;
        {
            for (Number noData : noDataValues) {
                noDataMin = Math.min(noDataMin, noData.doubleValue());
                noDataMax = Math.max(noDataMax, noData.doubleValue());
            }
        }
        final NumberRange<Double> sampleValueRange;
        sampleValueRange = nativeCellType.getSampleValueRange().castTo(Double.class);

        final RasterCellType targetCellType;

        if (sampleValueRange.contains((Number) Double.valueOf(noDataMin))
                && sampleValueRange.contains((Number) Double.valueOf(noDataMax))) {
            /*
             * The native cell type can hold the no-data values for all bands in the raster
             */
            targetCellType = nativeCellType;
        } else {
            targetCellType = promote(nativeCellType);
        }
        return targetCellType;
    }

    private static RasterCellType promote(final RasterCellType nativeCellType) {
        switch (nativeCellType) {
            case TYPE_1BIT:
            case TYPE_4BIT:
                return TYPE_8BIT_U;
            case TYPE_8BIT_U:
                return TYPE_16BIT_U;
            case TYPE_8BIT_S:
                return TYPE_16BIT_S;
            case TYPE_16BIT_U:
                return TYPE_32BIT_U;
            case TYPE_16BIT_S:
                return TYPE_32BIT_S;
            case TYPE_32BIT_S:
            case TYPE_32BIT_REAL:
            case TYPE_32BIT_U:
                return TYPE_64BIT_REAL;
            default:
                throw new IllegalArgumentException(
                        "Can't promote a raster of type 64-bit-real, there's "
                                + "no higher pixel depth than that!");
        }
    }
}
