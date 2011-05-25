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
package org.geotools.arcsde.raster.gce;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.awt.image.renderable.ParameterBlock;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.media.jai.ImageLayout;
import javax.media.jai.InterpolationNearest;
import javax.media.jai.JAI;
import javax.media.jai.OpImage;
import javax.media.jai.ParameterBlockJAI;
import javax.media.jai.operator.FormatDescriptor;
import javax.media.jai.operator.MosaicDescriptor;

import org.geotools.arcsde.raster.info.RasterDatasetInfo;
import org.geotools.arcsde.raster.info.RasterQueryInfo;
import org.geotools.arcsde.raster.info.RasterUtils;
import org.geotools.arcsde.raster.io.RasterReaderFactory;
import org.geotools.arcsde.raster.io.TiledRasterReader;
import org.geotools.coverage.CoverageFactoryFinder;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.TypeMap;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.OverviewPolicy;
import org.geotools.data.DefaultServiceInfo;
import org.geotools.data.ServiceInfo;
import org.geotools.factory.Hints;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.util.logging.Logging;
import org.opengis.coverage.ColorInterpretation;
import org.opengis.coverage.grid.Format;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.coverage.grid.GridCoverageReader;
import org.opengis.coverage.grid.GridEnvelope;
import org.opengis.geometry.Envelope;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterNotFoundException;
import org.opengis.parameter.ParameterValue;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.datum.PixelInCell;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

/**
 * 
 * @author Gabriel Roldan (OpenGeo)
 * @since 2.5.4
 * @version $Id$
 *
 * @source $URL$
 *         http://svn.osgeo.org/geotools/trunk/modules/plugin/arcsde/datastore/src/main/java/org
 *         /geotools/arcsde/gce/ArcSDEGridCoverage2DReaderJAI.java $
 */
@SuppressWarnings({ "deprecation", "nls" })
public final class ArcSDEGridCoverage2DReaderJAI extends AbstractGridCoverage2DReader {

    private static final Logger LOGGER = Logging.getLogger("org.geotools.arcsde.gce");

    /**
     * @see LoggingHelper#log(RenderedImage, Long, String)
     */
    private static final boolean DEBUG_TO_DISK = Boolean
            .getBoolean("org.geotools.arcsde.gce.debug");

    private final ArcSDERasterFormat parent;

    private final RasterDatasetInfo rasterInfo;

    private DefaultServiceInfo serviceInfo;

    private RasterReaderFactory rasterReaderFactory;

    public ArcSDEGridCoverage2DReaderJAI(final ArcSDERasterFormat parent,
            final RasterReaderFactory rasterReaderFactory, final RasterDatasetInfo rasterInfo,
            final Hints hints) throws IOException {
        // check it's a supported format
        {
            final int bitsPerSample = rasterInfo.getBand(0, 0).getCellType().getBitsPerSample();
            if (rasterInfo.getNumBands() > 1 && (bitsPerSample == 1 || bitsPerSample == 4)) {
                throw new IllegalArgumentException(bitsPerSample
                        + "-bit rasters with more than one band are not supported");
            }
        }
        this.parent = parent;
        this.rasterReaderFactory = rasterReaderFactory;
        this.rasterInfo = rasterInfo;

        super.hints = hints;
        super.coverageFactory = CoverageFactoryFinder.getGridCoverageFactory(this.hints);
        super.crs = rasterInfo.getCoverageCrs();
        super.originalEnvelope = rasterInfo.getOriginalEnvelope(PixelInCell.CELL_CENTER);

        GridEnvelope gridRange = rasterInfo.getOriginalGridRange();
        // super.originalGridRange = new GeneralGridRange(gridRange.toRectangle());
        super.originalGridRange = gridRange;

        super.coverageName = rasterInfo.getRasterTable();
        final int numLevels = rasterInfo.getNumPyramidLevels(0);

        // level 0 is not an overview, but the raster itself
        super.numOverviews = numLevels - 1;

        // ///
        //
        // setting the higher resolution avalaible for this coverage
        //
        // ///
        highestRes = super.getResolution(originalEnvelope,
                new Rectangle(originalGridRange.getLow(0), originalGridRange.getLow(1),
                        originalGridRange.getSpan(0), originalGridRange.getSpan(1)), crs);
        // //
        //
        // get information for the successive images
        //
        // //
        // REVISIT may the different rasters in the raster dataset have different pyramid levels? I
        // guess so
        if (numOverviews > 0) {
            overViewResolutions = new double[numOverviews][2];
            for (int pyramidLevel = 1; pyramidLevel <= numOverviews; pyramidLevel++) {
                GridEnvelope levelGridRange = rasterInfo.getGridRange(0, pyramidLevel);
                GeneralEnvelope levelEnvelope = rasterInfo.getGridEnvelope(0, pyramidLevel);

                Rectangle2D levelGridRangeRect = new Rectangle2D.Double(levelGridRange.getLow(0),
                        levelGridRange.getLow(1), levelGridRange.getSpan(0),
                        levelGridRange.getSpan(1));
                overViewResolutions[pyramidLevel - 1] = super.getResolution(levelEnvelope,
                        levelGridRangeRect, crs);
            }
        } else {
            overViewResolutions = null;
        }
    }

    /**
     * @see GridCoverageReader#getFormat()
     */
    public Format getFormat() {
        return parent;
    }

    @Override
    public ServiceInfo getInfo() {
        if (serviceInfo == null) {
            serviceInfo = new DefaultServiceInfo();
            serviceInfo.setTitle(rasterInfo.getRasterTable());
            serviceInfo.setDescription(rasterInfo.toString());
            Set<String> keywords = new HashSet<String>();
            keywords.add("ArcSDE");
            serviceInfo.setKeywords(keywords);
        }
        return serviceInfo;
    }

    /**
     * @see GridCoverageReader#read(GeneralParameterValue[])
     * @return A new {@linkplain GridCoverage grid coverage} from the input source, or {@code null}
     *         if the requested envelope is outside the data bounds
     */
    public GridCoverage2D read(GeneralParameterValue[] params) throws IOException {
        final GeneralEnvelope requestedEnvelope;
        final GridEnvelope requestedDim;
        final OverviewPolicy overviewPolicy;
        {
            final ReadParameters opParams = parseReadParams(getOriginalEnvelope(), params);
            overviewPolicy = opParams.overviewPolicy;
            requestedEnvelope = opParams.requestedEnvelope;
            requestedDim = new GridEnvelope2D(opParams.dim);
        }

        /*
         * For each raster in the raster dataset, obtain the tiles, pixel range, and resulting
         * envelope
         */
        final List<RasterQueryInfo> queries;
        queries = findMatchingRasters(requestedEnvelope, requestedDim, overviewPolicy);
        if (queries.isEmpty()) {
            if (requestedEnvelope.intersects(getOriginalEnvelope(), true)) {
                /*
                 * No matching rasters but envelopes intersect, meaning it's a raster catalog with
                 * irregular coverage and the request lies on an area with no coverage
                 */
                ImageTypeSpecifier imageTypeSpecifier;
                imageTypeSpecifier = RasterUtils.createFullImageTypeSpecifier(rasterInfo, 0);
                SampleModel sampleModel = imageTypeSpecifier.getSampleModel();
                Point location = new Point(0, 0);
                WritableRaster raster = Raster.createWritableRaster(sampleModel, location);
                GridCoverage2D emptyCoverage;
                emptyCoverage = coverageFactory.create(coverageName, raster, requestedEnvelope);
                return emptyCoverage;
            }
            /*
             * none of the rasters match the requested envelope.
             */
            return null;
        }

        final LoggingHelper log = new LoggingHelper();

        /*
         * Once we collected the matching rasters and their image subsets, find out where in the
         * overall resulting mosaic they fit. If the rasters does not share the spatial resolution,
         * the QueryInfo.resultDimension and QueryInfo.mosaicLocation width or height won't match
         */
        final GridEnvelope mosaicGeometry;
        mosaicGeometry = RasterUtils.setMosaicLocations(rasterInfo, queries);

        if (mosaicGeometry.getSpan(0) == 0 || mosaicGeometry.getSpan(1) == 0) {
            LOGGER.finer("Mosaic geometry width or height is zero,"
                    + " returning fake coverage for pixels " + mosaicGeometry);
            return null;
        }
        /*
         * Gather the rendered images for each of the rasters that match the requested envelope
         */
        final TiledRasterReader rasterReader = rasterReaderFactory.create(rasterInfo);

        try {
            readAllTiledRasters(queries, rasterReader, log);
        } finally {
            // rasterReader.dispose();
        }

        log.log(LoggingHelper.REQ_ENV);
        log.log(LoggingHelper.RES_ENV);
        log.log(LoggingHelper.MOSAIC_ENV);
        log.log(LoggingHelper.MOSAIC_EXPECTED);

        final RenderedImage coverageRaster = createMosaic(queries, mosaicGeometry, log);

        assert mosaicGeometry.getSpan(0) == coverageRaster.getWidth();
        assert mosaicGeometry.getSpan(1) == coverageRaster.getHeight();

        /*
         * BUILDING COVERAGE
         */
        GridSampleDimension[] bands = getSampleDimensions(coverageRaster);

        final GeneralEnvelope resultEnvelope = getResultEnvelope(queries, mosaicGeometry);
        log.appendLoggingGeometries(LoggingHelper.REQ_ENV, requestedEnvelope);
        log.appendLoggingGeometries(LoggingHelper.RES_ENV, resultEnvelope);

        GridCoverage2D resultCoverage = coverageFactory.create(coverageName, coverageRaster,
                resultEnvelope, bands, null, null);

        // MathTransform gridToCRS = rasterInfo.getRasterToModel(queries.get(0).getRasterIndex(),
        // queries.get(0).getPyramidLevel());
        //
        // GridGeometry2D gridGeometry = new GridGeometry2D(PixelInCell.CELL_CORNER, gridToCRS,
        // resultEnvelope, hints);
        //
        // GridCoverage[] sources = null;
        // Map<?, ?> properties = null;
        // GridCoverage2D resultCoverage = coverageFactory.create(coverageName, coverageRaster,
        // gridGeometry, bands, sources, properties);
        return resultCoverage;
    }

    private GeneralEnvelope toPixelCenter(double[] resolution, GeneralEnvelope pixelCornerEnv) {
        double deltaX = resolution[0] / 2;
        double deltaY = resolution[1] / 2;
        GeneralEnvelope env = new GeneralEnvelope(pixelCornerEnv.getCoordinateReferenceSystem());
        env.setEnvelope(pixelCornerEnv.getMinimum(0) + deltaX, pixelCornerEnv.getMinimum(1)
                + deltaY, pixelCornerEnv.getMaximum(0) - deltaX, pixelCornerEnv.getMaximum(1)
                - deltaY);
        return env;
    }

    private GridSampleDimension[] getSampleDimensions(final RenderedImage coverageRaster)
            throws IOException {

        GridSampleDimension[] bands = rasterInfo.getGridSampleDimensions();

        // may the image have been promoted? build the correct band info then
        final int imageBands = coverageRaster.getSampleModel().getNumBands();
        if (bands.length == 1 && imageBands > 1) {
            LOGGER.fine(coverageName + " was promoted from 1 to "
                    + coverageRaster.getSampleModel().getNumBands()
                    + " bands, returning an appropriate set of GridSampleDimension");
            // stolen from super.createCoverage:
            final ColorModel cm = coverageRaster.getColorModel();
            bands = new GridSampleDimension[imageBands];

            // setting bands names.
            for (int i = 0; i < imageBands; i++) {
                final ColorInterpretation colorInterpretation;
                colorInterpretation = TypeMap.getColorInterpretation(cm, i);
                if (colorInterpretation == null) {
                    throw new IOException("Unrecognized sample dimension type");
                }
                bands[i] = new GridSampleDimension(colorInterpretation.name()).geophysics(true);
            }
        }

        return bands;
    }

    private void readAllTiledRasters(final List<RasterQueryInfo> queries,
            final TiledRasterReader rasterReader, final LoggingHelper log) throws IOException {

        for (RasterQueryInfo queryInfo : queries) {

            final Long rasterId = queryInfo.getRasterId();

            final RenderedImage rasterImage;

            try {
                final int pyramidLevel = queryInfo.getPyramidLevel();
                final GridEnvelope matchingTiles = queryInfo.getMatchingTiles();
                // final Point imageLocation = queryInfo.getTiledImageSize().getLocation();
                rasterImage = rasterReader.read(rasterId, pyramidLevel, matchingTiles);
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Fetching data for " + queryInfo.toString(), e);
                throw e;
            }

            queryInfo.setResultImage(rasterImage);

            {
                LOGGER.finer(queryInfo.toString());
                log.appendLoggingGeometries(LoggingHelper.MOSAIC_EXPECTED,
                        queryInfo.getMosaicLocation());
                log.appendLoggingGeometries(LoggingHelper.MOSAIC_ENV, queryInfo.getResultEnvelope());

                // final Rectangle tiledImageSize = queryInfo.getTiledImageSize();
                // int width = rasterImage.getWidth();
                // int height = rasterImage.getHeight();
                // if (tiledImageSize.width != width || tiledImageSize.height != height) {
                // throw new IllegalStateException(
                // "Read image is not of the expected size. Image=" + width + "x" + height
                // + ", expected: " + tiledImageSize.width + "x"
                // + tiledImageSize.height);
                // }
            }
        }
    }

    private List<RasterQueryInfo> findMatchingRasters(final GeneralEnvelope requestedEnvelope,
            final GridEnvelope requestedDim, final OverviewPolicy overviewPolicy) {

        final List<RasterQueryInfo> matchingQueries;
        matchingQueries = RasterUtils.findMatchingRasters(rasterInfo, requestedEnvelope,
                requestedDim, overviewPolicy);

        if (matchingQueries.isEmpty()) {
            return matchingQueries;
        }

        for (RasterQueryInfo match : matchingQueries) {
            RasterUtils.fitRequestToRaster(requestedEnvelope, rasterInfo, match);
        }
        return matchingQueries;
    }

    private GeneralEnvelope getResultEnvelope(final List<RasterQueryInfo> queryInfos,
            final GridEnvelope mosaicGeometry) {

        // use the same queryInfo used by setMosaicLocations
        final RasterQueryInfo baseQueryInfo = RasterUtils.findLowestResolution(queryInfos);

        GeneralEnvelope finalEnvelope = null;
        // if (queryInfos.size() == 1) {
        // finalEnvelope = queryInfos.get(0).getResultEnvelope();
        // } else {
        int rasterIndex = baseQueryInfo.getRasterIndex();
        int pyramidLevel = baseQueryInfo.getPyramidLevel();
        MathTransform rasterToModel = rasterInfo.getRasterToModel(rasterIndex, pyramidLevel);
        CoordinateReferenceSystem coverageCrs = rasterInfo.getCoverageCrs();
        GeneralEnvelope mosaicGeometryEnv = new GeneralEnvelope(coverageCrs);
        mosaicGeometryEnv.setEnvelope(mosaicGeometry.getLow(0), mosaicGeometry.getLow(1),
                1 + mosaicGeometry.getHigh(0), 1 + mosaicGeometry.getHigh(1));
        try {
            finalEnvelope = CRS.transform(rasterToModel, mosaicGeometryEnv);
            finalEnvelope.setCoordinateReferenceSystem(coverageCrs);
        } catch (TransformException e) {
            throw new RuntimeException(e);
        }
        // }

        // double[] resolution = baseQueryInfo.getResolution();
        // finalEnvelope = toPixelCenter(resolution, finalEnvelope);
        return finalEnvelope;
    }

    /**
     * For each raster: crop->scale->translate->add to mosaic
     * 
     * @param queries
     * @param mosaicGeometry
     * @return
     * @throws IOException
     */
    private RenderedImage createMosaic(final List<RasterQueryInfo> queries,
            final GridEnvelope mosaicGeometry, final LoggingHelper log) throws IOException {

        List<RenderedImage> transformed = new ArrayList<RenderedImage>(queries.size());

        /*
         * Do we need to expand to RGB color space and then create a new colormapped image with the
         * whole mosaic?
         */
        boolean expandCM = queries.size() > 1 && rasterInfo.isColorMapped();
        if (expandCM) {
            LOGGER.fine("Creating mosaic out of " + queries.size()
                    + " colormapped rasters. The mosaic tiles will be expanded to "
                    + "\nRGB space and the resulting mosaic reduced to a new IndexColorModel");
        }

        for (RasterQueryInfo query : queries) {
            RenderedImage image = query.getResultImage();
            log.log(image, query.getRasterId(), "01_original");
            if (expandCM) {
                if (LOGGER.isLoggable(Level.FINER)) {
                    LOGGER.finer("Creating color expanded version of tile for raster #"
                            + query.getRasterId());
                }

                /*
                 * reformat the image as a 4 band rgba backed by byte data
                 */
                image = FormatDescriptor.create(image, Integer.valueOf(DataBuffer.TYPE_BYTE), null);

                log.log(image, query.getRasterId(), "04_1_colorExpanded");
            }

            image = cropToRequiredDimension(image, query.getResultGridRange());
            log.log(image, query.getRasterId(), "02_crop");

            // Raster data = image.getData();
            // image = new BufferedImage(image.getColorModel(), (WritableRaster) data, false, null);
            if (queries.size() == 1) {
                return image;
            }
            final GridEnvelope mosaicLocation = query.getMosaicLocation();
            // scale
            Float scaleX = Float.valueOf(((float) mosaicLocation.getSpan(0) / image.getWidth()));
            Float scaleY = Float.valueOf(((float) mosaicLocation.getSpan(1) / image.getHeight()));
            Float translateX = Float.valueOf(0);
            Float translateY = Float.valueOf(0);

            if (!(Float.valueOf(1.0F).equals(scaleX) && Float.valueOf(1.0F).equals(scaleY))) {
                ParameterBlock pb = new ParameterBlock();
                pb.addSource(image);
                pb.add(scaleX);
                pb.add(scaleY);
                pb.add(translateX);
                pb.add(translateY);
                pb.add(new InterpolationNearest());

                image = JAI.create("scale", pb);
                log.log(image, query.getRasterId(), "03_scale");

                int width = image.getWidth();
                int height = image.getHeight();

                assert mosaicLocation.getSpan(0) == width;
                assert mosaicLocation.getSpan(1) == height;
            }

            if (image.getMinX() != mosaicLocation.getLow(0)
                    || image.getMinY() != mosaicLocation.getLow(1)) {
                // translate
                ParameterBlock pb = new ParameterBlock();
                pb.addSource(image);
                pb.add(Float.valueOf(mosaicLocation.getLow(0) - image.getMinX()));
                pb.add(Float.valueOf(mosaicLocation.getLow(1) - image.getMinY()));
                pb.add(null);

                image = JAI.create("translate", pb);
                log.log(image, query.getRasterId(), "04_translate");

                assert image.getMinX() == mosaicLocation.getLow(0) : image.getMinX() + " != "
                        + mosaicLocation.getLow(0);
                assert image.getMinY() == mosaicLocation.getLow(1) : image.getMinY() + " != "
                        + mosaicLocation.getLow(1);
                assert image.getWidth() == mosaicLocation.getSpan(0) : image.getWidth() + " != "
                        + mosaicLocation.getSpan(0);
                assert image.getHeight() == mosaicLocation.getSpan(1) : image.getHeight() + " != "
                        + mosaicLocation.getSpan(1);
            }

            transformed.add(image);
        }

        final RenderedImage mosaic;
        if (queries.size() == 1) {
            /*
             * This is besides a very slight perf improvement needed because the JAI mosaic
             * operation truncates floating point raster values to 0 and 1. REVISIT: If there's no
             * workaround for that we should prevent raster catalogs made of floating point rasters
             * and throw an exception as we could not really support that.
             */
            mosaic = transformed.get(0);
        } else {
            /*
             * adapted from RasterLayerResponse.java in the imagemosaic module
             */
            ParameterBlockJAI mosaicParams = new ParameterBlockJAI("Mosaic");
            mosaicParams.setParameter("mosaicType", MosaicDescriptor.MOSAIC_TYPE_OVERLAY);

            // set background values to raster's no-data
            double[] backgroundValues;
            if (expandCM) {
                backgroundValues = new double[] { 0, 0, 0, 0 };
            } else {
                final int numBands = rasterInfo.getNumBands();
                backgroundValues = new double[numBands];
                final int rasterIndex = 0;
                Number noDataValue;
                for (int bn = 0; bn < numBands; bn++) {
                    noDataValue = rasterInfo.getNoDataValue(rasterIndex, bn);
                    backgroundValues[bn] = noDataValue.doubleValue();
                }
            }
            mosaicParams.setParameter("backgroundValues", backgroundValues);

            final ImageLayout layout = new ImageLayout(mosaicGeometry.getLow(0),
                    mosaicGeometry.getLow(1), mosaicGeometry.getSpan(0), mosaicGeometry.getSpan(1));
            final int tileWidth = rasterInfo.getTileDimension(0).width;
            final int tileHeight = rasterInfo.getTileDimension(0).height;
            layout.setTileWidth(tileWidth);
            layout.setTileHeight(tileHeight);

            final RenderingHints hints = new RenderingHints(JAI.KEY_IMAGE_LAYOUT, layout);

            for (RenderedImage img : transformed) {
                mosaicParams.addSource(img);
                log.appendLoggingGeometries(LoggingHelper.MOSAIC_RESULT, img);
            }
            log.log(LoggingHelper.MOSAIC_RESULT);

            LOGGER.fine("Creating mosaic out of " + queries.size() + " raster tiles");
            mosaic = JAI.create("Mosaic", mosaicParams, hints);

            log.log(mosaic, 0L, "05_mosaic_result");
        }
        return mosaic;
    }

    /**
     * Crops the image representing a full tile set to the required dimension and returns it, but
     * keeps minx and miny being zero.
     * 
     * @param fullTilesRaster
     * @param tiledImageGridRange
     * @param cropTo
     * @return
     */
    private RenderedImage cropToRequiredDimension(final RenderedImage fullTilesRaster,
            final GridEnvelope cropTo) {
        GridEnvelope2D crop = new GridEnvelope2D(cropTo.getLow(0), cropTo.getLow(1),
                cropTo.getSpan(0), cropTo.getSpan(1));

        GridEnvelope2D origDim = new GridEnvelope2D(fullTilesRaster.getMinX(),
                fullTilesRaster.getMinY(), fullTilesRaster.getWidth(), fullTilesRaster.getHeight());
        if (!origDim.contains(crop)) {
            throw new IllegalArgumentException("Original image (" + origDim
                    + ") does not contain desired dimension (" + crop + ")");
        } else if (origDim.equals(crop)) {
            if (LOGGER.isLoggable(Level.FINER)) {
                LOGGER.finer("No need to crop image, full tiled dimension and target one "
                        + "do match: original: " + fullTilesRaster.getWidth() + "x"
                        + fullTilesRaster.getHeight() + ", target: " + crop.getSpan(0) + "x"
                        + crop.getSpan(1));
            }
            return fullTilesRaster;
        }

        ParameterBlock cropParams = new ParameterBlock();

        cropParams.addSource(fullTilesRaster);// Source
        // cropParams.add(Float.valueOf(cropTo.x - tiledImageGridRange.x)); // x origin for each
        // band
        // cropParams.add(Float.valueOf(cropTo.y - tiledImageGridRange.y)); // y origin for each
        // band
        cropParams.add(Float.valueOf(crop.getLow(0))); // x origin for each band
        cropParams.add(Float.valueOf(crop.getLow(1))); // y origin for each band
        cropParams.add(Float.valueOf(crop.getSpan(0)));// width for each band
        cropParams.add(Float.valueOf(crop.getSpan(1)));// height for each band

        final RenderingHints hints = new RenderingHints(JAI.KEY_OPERATION_BOUND,
                OpImage.OP_NETWORK_BOUND);
        RenderedImage image = JAI.create("Crop", cropParams, hints);

        // assert cropTo.x - tiledImageGridRange.x == image.getMinX();
        // assert cropTo.y - tiledImageGridRange.y == image.getMinY();
        assert crop.getLow(0) == image.getMinX();
        assert crop.getLow(1) == image.getMinY();
        assert crop.getSpan(0) == image.getWidth();
        assert crop.getSpan(1) == image.getHeight();

        // assert cropTo.x == image.getMinX();
        // assert cropTo.y == image.getMinY();
        // assert cropTo.width == image.getWidth();
        // assert cropTo.height == image.getHeight();
        return image;
    }

    static class ReadParameters {
        GeneralEnvelope requestedEnvelope;

        Rectangle dim;

        OverviewPolicy overviewPolicy;
    }

    private static ArcSDEGridCoverage2DReaderJAI.ReadParameters parseReadParams(
            final GeneralEnvelope coverageEnvelope, final GeneralParameterValue[] params)
            throws IllegalArgumentException {
        if (params == null) {
            throw new IllegalArgumentException("No GeneralParameterValue given to read operation");
        }

        GeneralEnvelope reqEnvelope = null;
        Rectangle dim = null;
        OverviewPolicy overviewPolicy = null;

        // /////////////////////////////////////////////////////////////////////
        //
        // Checking params
        //
        // /////////////////////////////////////////////////////////////////////
        for (int i = 0; i < params.length; i++) {
            final ParameterValue<?> param = (ParameterValue<?>) params[i];
            final String name = param.getDescriptor().getName().getCode();
            if (name.equals(AbstractGridFormat.READ_GRIDGEOMETRY2D.getName().toString())) {
                final GridGeometry2D gg = (GridGeometry2D) param.getValue();
                reqEnvelope = new GeneralEnvelope((Envelope) gg.getEnvelope2D());

                CoordinateReferenceSystem nativeCrs = coverageEnvelope
                        .getCoordinateReferenceSystem();
                CoordinateReferenceSystem requestCrs = reqEnvelope.getCoordinateReferenceSystem();
                if (!CRS.equalsIgnoreMetadata(nativeCrs, requestCrs)) {
                    LOGGER.fine("Request CRS and native CRS differ, "
                            + "reprojecting request envelope to native CRS");
                    ReferencedEnvelope nativeCrsEnv;
                    nativeCrsEnv = toNativeCrs(reqEnvelope, nativeCrs);
                    reqEnvelope = new GeneralEnvelope(nativeCrsEnv);
                }

                dim = gg.getGridRange2D().getBounds();
                continue;
            }
            if (name.equals(AbstractGridFormat.OVERVIEW_POLICY.getName().toString())) {
                overviewPolicy = (OverviewPolicy) param.getValue();
                continue;
            }
        }

        if (dim == null && reqEnvelope == null) {
            throw new ParameterNotFoundException("Parameter is mandatory and shall provide "
                    + "the extent and dimension to request", AbstractGridFormat.READ_GRIDGEOMETRY2D
                    .getName().toString());
        }

        if (!reqEnvelope.intersects(coverageEnvelope, true)) {
            throw new IllegalArgumentException(
                    "The requested extend does not overlap the coverage extent: "
                            + coverageEnvelope);
        }

        if (dim.width <= 0 || dim.height <= 0) {
            throw new IllegalArgumentException("The requested coverage dimension can't be null: "
                    + dim);
        }

        if (overviewPolicy == null) {
            overviewPolicy = OverviewPolicy.NEAREST;
            LOGGER.finer("No overview policy requested, defaulting to " + overviewPolicy);
        }
        LOGGER.fine("Overview policy is " + overviewPolicy);

        LOGGER.fine("Reading raster for " + dim.getWidth() + "x" + dim.getHeight()
                + " requested dim and " + reqEnvelope.getMinimum(0) + ","
                + reqEnvelope.getMaximum(0) + " - " + reqEnvelope.getMinimum(1)
                + reqEnvelope.getMaximum(1) + " requested extent");

        ArcSDEGridCoverage2DReaderJAI.ReadParameters parsedParams = new ArcSDEGridCoverage2DReaderJAI.ReadParameters();
        parsedParams.requestedEnvelope = reqEnvelope;
        parsedParams.dim = dim;
        parsedParams.overviewPolicy = overviewPolicy;
        return parsedParams;
    }

    private static ReferencedEnvelope toNativeCrs(final GeneralEnvelope requestedEnvelope,
            final CoordinateReferenceSystem nativeCRS) throws IllegalArgumentException {

        ReferencedEnvelope reqEnv = toReferencedEnvelope(requestedEnvelope);

        if (!CRS.equalsIgnoreMetadata(nativeCRS, reqEnv.getCoordinateReferenceSystem())) {
            // we're being reprojected. We'll need to reproject reqEnv into
            // our native coordsys
            try {
                // ReferencedEnvelope origReqEnv = reqEnv;
                reqEnv = reqEnv.transform(nativeCRS, true);
            } catch (FactoryException fe) {
                // unable to reproject?
                throw new IllegalArgumentException("Unable to find a reprojection from requested "
                        + "coordsys to native coordsys for this request", fe);
            } catch (TransformException te) {
                throw new IllegalArgumentException("Unable to perform reprojection from requested "
                        + "coordsys to native coordsys for this request", te);
            }
        }
        return reqEnv;
    }

    private static ReferencedEnvelope toReferencedEnvelope(GeneralEnvelope envelope) {
        double minx = envelope.getMinimum(0);
        double maxx = envelope.getMaximum(0);
        double miny = envelope.getMinimum(1);
        double maxy = envelope.getMaximum(1);
        CoordinateReferenceSystem crs = envelope.getCoordinateReferenceSystem();

        ReferencedEnvelope refEnv = new ReferencedEnvelope(minx, maxx, miny, maxy, crs);
        return refEnv;
    }

    /**
     * A simple helper class to guard and easy logging the mosaic geometries in both geographical
     * and pixel ranges
     */
    private static class LoggingHelper {

        private static final File debugDir = new File(System.getProperty("user.home")
                + File.separator + "arcsde_test");

        static {
            if (DEBUG_TO_DISK) {
                debugDir.mkdir();
            }
        }

        public Level GEOM_LEVEL = Level.FINER;

        public static String REQ_ENV = "Requested envelope";

        public static String RES_ENV = "Resulting envelope";

        public static String MOSAIC_ENV = "Resulting mosaiced envelopes";

        public static String MOSAIC_EXPECTED = "Expected mosaic layout (in pixels)";

        public static String MOSAIC_RESULT = "Resulting image mosaic layout (in pixels)";

        private Map<String, StringBuilder> geoms = null;

        LoggingHelper() {
            // not much to to
        }

        private StringBuilder getGeom(String geomName) {
            if (geoms == null) {
                geoms = new HashMap<String, StringBuilder>();
            }
            StringBuilder sb = geoms.get(geomName);
            if (sb == null) {
                sb = new StringBuilder("MULTIPOLYGON(\n");
                geoms.put(geomName, sb);
            }
            return sb;
        }

        public void appendLoggingGeometries(String geomName, RenderedImage img) {
            if (LOGGER.isLoggable(GEOM_LEVEL)) {
                appendLoggingGeometries(geomName, new GridEnvelope2D(img.getMinX(), img.getMinY(),
                        img.getWidth(), img.getHeight()));
            }
        }

        public void appendLoggingGeometries(String geomName, GridEnvelope env) {
            if (LOGGER.isLoggable(GEOM_LEVEL)) {
                appendLoggingGeometries(geomName,
                        new GeneralEnvelope(new Rectangle2D.Double(env.getLow(0), env.getLow(1),
                                env.getSpan(0), env.getSpan(1))));
            }
        }

        public void appendLoggingGeometries(String geomName, GeneralEnvelope env) {
            if (LOGGER.isLoggable(GEOM_LEVEL)) {
                StringBuilder sb = getGeom(geomName);
                sb.append("  ((" + env.getMinimum(0) + " " + env.getMinimum(1) + ", "
                        + env.getMaximum(0) + " " + env.getMinimum(1) + ", " + env.getMaximum(0)
                        + " " + env.getMaximum(1) + ", " + env.getMinimum(0) + " "
                        + env.getMaximum(1) + ", " + env.getMinimum(0) + " " + env.getMinimum(1)
                        + ")),");
            }
        }

        public void log(String geomName) {
            if (LOGGER.isLoggable(GEOM_LEVEL)) {
                StringBuilder sb = getGeom(geomName);
                sb.setLength(sb.length() - 1);
                sb.append("\n)");
                LOGGER.log(GEOM_LEVEL, geomName + ":\n" + sb.toString());
            }
        }

        public void log(RenderedImage image, Long rasterId, String fileName) {
            if (DEBUG_TO_DISK) {
                LOGGER.warning("BEWARE THE DEBUG FLAG IS TURNED ON! "
                        + "IF IN PRODUCTION THIS IS A SEVERE MISTAKE!!!");
                // ImageIO.write(FormatDescriptor.create(image,
                // Integer.valueOf(DataBuffer.TYPE_BYTE),
                // null), "TIFF", new File(debugDir, rasterId.longValue() + fileName + ".tiff"));

                try {
                    ImageIO.write(image, "TIFF", new File(debugDir, rasterId.longValue() + fileName
                            + ".tiff"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
