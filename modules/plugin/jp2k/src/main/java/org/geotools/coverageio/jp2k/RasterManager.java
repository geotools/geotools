/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverageio.jp2k;

import java.awt.Rectangle;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageReadParam;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.io.OverviewPolicy;
import org.geotools.data.DataSourceException;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.image.util.ImageUtilities;
import org.geotools.metadata.iso.extent.GeographicBoundingBoxImpl;
import org.geotools.referencing.CRS;
import org.geotools.util.SoftValueHashMap;
import org.geotools.util.Utilities;
import org.geotools.util.factory.Hints;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.coverage.grid.GridEnvelope;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.datum.PixelInCell;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.MathTransform2D;
import org.opengis.referencing.operation.TransformException;

/**
 * A RasterManager allows to handle granules, decimation, overviews and spatial properties of a
 * raster.
 *
 * @author Daniele Romagnoli, GeoSolutions S.A.S.
 * @author Simone Giannecchini, GeoSolutions S.A.S.
 */
class RasterManager {
    /** Logger. */
    private static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(RasterManager.class);

    final SoftValueHashMap<String, Granule> granulesCache = new SoftValueHashMap<String, Granule>();

    /**
     * Simple support class for sorting overview resolutions
     *
     * @author Andrea Aime
     * @author Simone Giannecchini, GeoSolutions.
     * @since 2.5
     */
    static class OverviewLevel implements Comparable<OverviewLevel> {

        double scaleFactor;

        double resolutionX;
        double resolutionY;
        int imageChoice;

        public OverviewLevel(
                final double scaleFactor,
                final double resolutionX,
                final double resolutionY,
                int imageChoice) {
            this.scaleFactor = scaleFactor;
            this.resolutionX = resolutionX;
            this.resolutionY = resolutionY;
            this.imageChoice = imageChoice;
        }

        public int compareTo(final OverviewLevel other) {
            if (scaleFactor > other.scaleFactor) return 1;
            else if (scaleFactor < other.scaleFactor) return -1;
            else return 0;
        }

        @Override
        public String toString() {
            return "OverviewLevel[Choice=" + imageChoice + ",scaleFactor=" + scaleFactor + "]";
        }

        @Override
        public int hashCode() {
            int hash = Utilities.hash(imageChoice, 31);
            hash = Utilities.hash(resolutionX, hash);
            hash = Utilities.hash(resolutionY, hash);
            hash = Utilities.hash(scaleFactor, hash);
            return hash;
        }
    }

    class OverviewsController {
        final ArrayList<RasterManager.OverviewLevel> resolutionsLevels =
                new ArrayList<OverviewLevel>();

        public OverviewsController() {

            // notice that we assume what follows:
            // -highest resolution image is at level 0.
            // -all the overviews share the same envelope
            // -the aspect ratio for the overviews is constant
            // -the provided resolutions are taken directly from the grid
            resolutionsLevels.add(new OverviewLevel(1, highestRes[0], highestRes[1], 0));
            if (numberOfOverviews > 0) {
                for (int i = 0; i < overviewsResolution.length; i++)
                    resolutionsLevels.add(
                            new OverviewLevel(
                                    overviewsResolution[i][0] / highestRes[0],
                                    overviewsResolution[i][0],
                                    overviewsResolution[i][1],
                                    i + 1));
                Collections.sort(resolutionsLevels);
            }
        }

        int pickOverviewLevel(final OverviewPolicy policy, final RasterLayerRequest request) {

            // //
            //
            // If this file has only
            // one page we use decimation, otherwise we use the best page available.
            // Future versions should use both.
            //
            // //
            if (resolutionsLevels == null || resolutionsLevels.size() <= 0) return 0;

            // Now search for the best matching resolution.
            // Check also for the "perfect match"... unlikely in practice unless someone
            // tunes the clients to request exactly the resolution embedded in
            // the overviews, something a perf sensitive person might do in fact

            // requested scale factor for least reduced axis
            final OverviewLevel max = (OverviewLevel) resolutionsLevels.get(0);

            // the requested resolutions
            final double requestedScaleFactorX;
            final double requestedScaleFactorY;
            final double[] requestedRes = request.getRequestedResolution();
            if (requestedRes != null) {
                final double reqx = requestedRes[0];
                final double reqy = requestedRes[1];

                requestedScaleFactorX = reqx / max.resolutionX;
                requestedScaleFactorY = reqy / max.resolutionY;
            } else return 0;
            final int leastReduceAxis = requestedScaleFactorX <= requestedScaleFactorY ? 0 : 1;
            final double requestedScaleFactor =
                    leastReduceAxis == 0 ? requestedScaleFactorX : requestedScaleFactorY;

            // are we looking for a resolution even higher than the native one?
            if (requestedScaleFactor <= 1) return max.imageChoice;
            // are we looking for a resolution even lower than the smallest overview?
            final OverviewLevel min =
                    (OverviewLevel) resolutionsLevels.get(resolutionsLevels.size() - 1);
            if (requestedScaleFactor >= min.scaleFactor) return min.imageChoice;
            // Ok, so we know the overview is between min and max, skip the first
            // and search for an overview with a resolution lower than the one requested,
            // that one and the one from the previous step will bound the searched resolution
            OverviewLevel prev = max;
            final int size = resolutionsLevels.size();
            for (int i = 1; i < size; i++) {
                final OverviewLevel curr = resolutionsLevels.get(i);
                // perfect match check
                if (curr.scaleFactor == requestedScaleFactor) {
                    return curr.imageChoice;
                }

                // middle check. The first part of the condition should be sufficient, but
                // there are cases where the x resolution is satisfied by the lowest resolution,
                // the y by the one before the lowest (so the aspect ratio of the request is
                // different than the one of the overviews), and we would end up going out of the
                // loop
                // since not even the lowest can "top" the request for one axis
                if (curr.scaleFactor > requestedScaleFactor || i == size - 1) {
                    if (policy == OverviewPolicy.QUALITY) return prev.imageChoice;
                    else if (policy == OverviewPolicy.SPEED) return curr.imageChoice;
                    else if (requestedScaleFactor - prev.scaleFactor
                            < curr.scaleFactor - requestedScaleFactor) return prev.imageChoice;
                    else return curr.imageChoice;
                }
                prev = curr;
            }
            // fallback
            return max.imageChoice;
        }
    }

    class DecimationController {

        public DecimationController() {}

        /**
         * This method is responsible for evaluating possible subsampling factors once the best
         * resolution level has been found, in case we have support for overviews, or starting from
         * the original coverage in case there are no overviews available.
         *
         * <p>Anyhow this method should not be called directly but subclasses should make use of the
         * setReadParams method instead in order to transparently look for overviews.
         */
        void performDecimation(
                final int imageIndex,
                final ImageReadParam readParameters,
                final RasterLayerRequest request) {

            // the read parameters cannot be null
            Utilities.ensureNonNull("readParameters", readParameters);
            Utilities.ensureNonNull("request", request);

            // get the requested resolution
            final double[] requestedRes = request.getRequestedResolution();
            if (requestedRes == null) {
                // if there is no requested resolution we don't do any subsampling
                readParameters.setSourceSubsampling(1, 1, 0, 0);
                return;
            }

            double selectedRes[] = new double[2];
            final OverviewLevel level = overviewsController.resolutionsLevels.get(imageIndex);
            selectedRes[0] = level.resolutionX;
            selectedRes[1] = level.resolutionY;

            final int rasterWidth, rasterHeight;
            if (imageIndex == 0) {
                // highest resolution
                rasterWidth = spatialDomainManager.coverageRasterArea.width;
                rasterHeight = spatialDomainManager.coverageRasterArea.height;
            } else {
                // work on overviews
                // TODO this is bad side effect of how the Overviews are managed right now. There
                // are two problems here,
                // first we are assuming that we are working with LON/LAT, second is that we are
                // getting just an approximation of
                // raster dimensions. The solution is to have the rater dimensions on each level and
                // to confront raster dimensions,
                // which means working
                rasterWidth =
                        (int)
                                Math.round(
                                        spatialDomainManager.coverageBBox.getSpan(0)
                                                / selectedRes[0]);
                rasterHeight =
                        (int)
                                Math.round(
                                        spatialDomainManager.coverageBBox.getSpan(1)
                                                / selectedRes[1]);
            }
            // /////////////////////////////////////////////////////////////////////
            // DECIMATION ON READING
            // Setting subsampling factors with some checks
            // 1) the subsampling factors cannot be zero
            // 2) the subsampling factors cannot be such that the w or h are zero
            // /////////////////////////////////////////////////////////////////////
            int subSamplingFactorX = (int) Math.floor(requestedRes[0] / selectedRes[0]);
            subSamplingFactorX = subSamplingFactorX == 0 ? 1 : subSamplingFactorX;

            while (rasterWidth / subSamplingFactorX <= 0 && subSamplingFactorX >= 0)
                subSamplingFactorX--;
            subSamplingFactorX = subSamplingFactorX <= 0 ? 1 : subSamplingFactorX;

            int subSamplingFactorY = (int) Math.floor(requestedRes[1] / selectedRes[1]);
            subSamplingFactorY = subSamplingFactorY == 0 ? 1 : subSamplingFactorY;

            while (rasterHeight / subSamplingFactorY <= 0 && subSamplingFactorY >= 0)
                subSamplingFactorY--;
            subSamplingFactorY = subSamplingFactorY <= 0 ? 1 : subSamplingFactorY;

            readParameters.setSourceSubsampling(subSamplingFactorX, subSamplingFactorY, 0, 0);
        }
    }

    /**
     * This class is responsible for putting together all the 2D spatial information needed for a
     * certain raster.
     *
     * <p>Notice that when this structure will be extended to work in ND this will become much more
     * complex or as an alternative a sibling TemporalDomainManager will be created.
     *
     * @author Simone Giannecchini, GeoSolutions SAS
     */
    class SpatialDomainManager {

        public SpatialDomainManager() throws TransformException, FactoryException {
            setBaseParameters();
            prepareCoverageSpatialElements();
        }
        /** The base envelope 2D */
        ReferencedEnvelope coverageBBox;
        /** The CRS for the coverage */
        CoordinateReferenceSystem coverageCRS;
        /** The CRS related to the base envelope 2D */
        CoordinateReferenceSystem coverageCRS2D;
        // ////////////////////////////////////////////////////////////////////////
        //
        // Base coverage properties
        //
        // ////////////////////////////////////////////////////////////////////////
        /** The base envelope read from file */
        GeneralEnvelope coverageEnvelope = null;

        double[] coverageFullResolution;
        /** WGS84 envelope 2D for this coverage */
        ReferencedEnvelope coverageGeographicBBox;

        CoordinateReferenceSystem coverageGeographicCRS2D;
        MathTransform2D coverageGridToWorld2D;
        /** The base grid range for the coverage */
        Rectangle coverageRasterArea;
        /** Initialize the 2D properties (CRS and Envelope) of this coverage */
        private void prepareCoverageSpatialElements() throws TransformException, FactoryException {
            //
            // basic initialization
            //
            coverageGeographicBBox =
                    ImageUtilities.getReferencedEnvelopeFromGeographicBoundingBox(
                            new GeographicBoundingBoxImpl(coverageEnvelope));
            coverageGeographicCRS2D = coverageGeographicBBox.getCoordinateReferenceSystem();

            //
            // Get the original envelope 2d and its spatial reference system
            //
            coverageCRS2D = CRS.getHorizontalCRS(coverageCRS);
            assert coverageCRS2D.getCoordinateSystem().getDimension() == 2;
            if (coverageCRS.getCoordinateSystem().getDimension() != 2) {
                final MathTransform transform =
                        CRS.findMathTransform(
                                coverageCRS, (CoordinateReferenceSystem) coverageCRS2D);
                final GeneralEnvelope bbox = CRS.transform(transform, coverageEnvelope);
                bbox.setCoordinateReferenceSystem(coverageCRS2D);
                coverageBBox = new ReferencedEnvelope(bbox);
            } else {
                // it is already a bbox
                coverageBBox = new ReferencedEnvelope(coverageEnvelope);
            }
        }
        /**
         * Set the main parameters of this coverage request, getting basic information from the
         * reader.
         */
        private void setBaseParameters() {
            this.coverageEnvelope = RasterManager.this.getCoverageEnvelope().clone();
            this.coverageRasterArea = ((GridEnvelope2D) RasterManager.this.getCoverageGridrange());
            this.coverageCRS = RasterManager.this.getCoverageCRS();
            this.coverageGridToWorld2D = (MathTransform2D) RasterManager.this.getRaster2Model();
            this.coverageFullResolution = new double[2];
            final OverviewLevel highestLevel =
                    RasterManager.this.overviewsController.resolutionsLevels.get(0);
            coverageFullResolution[0] = highestLevel.resolutionX;
            coverageFullResolution[1] = highestLevel.resolutionY;
        }
    }
    /** The CRS of the input coverage */
    private CoordinateReferenceSystem coverageCRS;
    /** The base envelope related to the input coverage */
    private GeneralEnvelope coverageEnvelope;

    /** The coverage factory producing a {@link GridCoverage} from an image */
    private GridCoverageFactory coverageFactory;

    /** The name of the input coverage TODO consider URI */
    private String coverageIdentifier;

    private double[] highestRes;
    /** The hints to be used to produce this coverage */
    private Hints hints;

    private URL inputURL;
    private int numberOfOverviews;
    private double[][] overviewsResolution;
    // ////////////////////////////////////////////////////////////////////////
    //
    // Information obtained by the coverageRequest instance
    //
    // ////////////////////////////////////////////////////////////////////////
    /** The coverage grid to world transformation */
    private MathTransform raster2Model;

    OverviewsController overviewsController;
    private GridEnvelope coverageGridrange;
    OverviewPolicy overviewPolicy;
    DecimationController decimationController;
    JP2KReader parent;
    boolean expandMe;
    SpatialDomainManager spatialDomainManager;

    public RasterManager(final JP2KReader reader) throws DataSourceException {

        Utilities.ensureNonNull("JP2KReader", reader);
        this.parent = reader;
        this.expandMe = parent.expandMe;
        inputURL = reader.sourceURL;
        coverageIdentifier = reader.getName();
        hints = reader.getHints();
        coverageEnvelope = reader.getOriginalEnvelope();
        coverageGridrange = reader.getOriginalGridRange();
        coverageCRS = reader.getCoordinateReferenceSystem();
        raster2Model = reader.getOriginalGridToWorld(PixelInCell.CELL_CENTER);
        this.coverageIdentifier = reader.getName();
        this.coverageFactory = reader.getGridCoverageFactory();

        // resolution values
        highestRes = reader.getHighestRes();
        numberOfOverviews = reader.getNumberOfOverviews();
        overviewsResolution = reader.getOverviewsResolution();

        // instantiating controller for subsampling and overviews
        overviewsController = new OverviewsController();
        decimationController = new DecimationController();
        try {
            spatialDomainManager = new SpatialDomainManager();
        } catch (TransformException e) {
            throw new DataSourceException(e);
        } catch (FactoryException e) {
            throw new DataSourceException(e);
        }
        extractOverviewPolicy();
    }

    /**
     * This method is responsible for checking the overview policy as defined by the provided {@link
     * Hints}.
     *
     * @return the overview policy which can be one of {@link Hints#VALUE_OVERVIEW_POLICY_IGNORE},
     *     {@link Hints#VALUE_OVERVIEW_POLICY_NEAREST}, {@link Hints#VALUE_OVERVIEW_POLICY_SPEED},
     *     {@link Hints#VALUE_OVERVIEW_POLICY_QUALITY}. Default is {@link
     *     Hints#VALUE_OVERVIEW_POLICY_NEAREST}.
     */
    private OverviewPolicy extractOverviewPolicy() {

        if (this.hints != null)
            if (this.hints.containsKey(Hints.OVERVIEW_POLICY))
                overviewPolicy = (OverviewPolicy) this.hints.get(Hints.OVERVIEW_POLICY);

        // use default if not provided. Default is nearest
        if (overviewPolicy == null) overviewPolicy = OverviewPolicy.getDefaultPolicy();

        assert overviewPolicy != null;
        return overviewPolicy;
    }

    public Collection<GridCoverage2D> read(final GeneralParameterValue[] params)
            throws IOException {

        // create a request
        final RasterLayerRequest request = new RasterLayerRequest(params, this);
        if (request.isEmpty()) {
            if (LOGGER.isLoggable(Level.FINE))
                LOGGER.log(Level.FINE, "Request is empty: " + request.toString());
            return Collections.emptyList();
        }

        // create a response for the provided request
        final RasterLayerResponse response = new RasterLayerResponse(request, this);

        // execute the request
        final GridCoverage2D elem = response.createResponse();
        if (elem != null) return Collections.singletonList(elem);
        return Collections.emptyList();
    }

    public void dispose() {}

    public URL getInputURL() {
        return inputURL;
    }

    public String getCoverageIdentifier() {
        return coverageIdentifier;
    }

    public Hints getHints() {
        return hints;
    }

    public CoordinateReferenceSystem getCoverageCRS() {
        return coverageCRS;
    }

    public GeneralEnvelope getCoverageEnvelope() {
        return coverageEnvelope;
    }

    public GridCoverageFactory getCoverageFactory() {
        return coverageFactory;
    }

    public MathTransform getRaster2Model() {
        return raster2Model;
    }

    public GridEnvelope getCoverageGridrange() {
        return coverageGridrange;
    }
}
