/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.processing.operation;

import it.geosolutions.jaiext.JAIExt;
import it.geosolutions.jaiext.range.NoDataContainer;
import it.geosolutions.jaiext.range.Range;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.DataBuffer;
import java.awt.image.IndexColorModel;
import java.awt.image.RenderedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import javax.media.jai.BorderExtender;
import javax.media.jai.ImageLayout;
import javax.media.jai.Interpolation;
import javax.media.jai.InterpolationNearest;
import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.ROI;
import javax.media.jai.RenderedOp;
import javax.media.jai.Warp;
import javax.media.jai.WarpGrid;
import javax.media.jai.operator.MosaicDescriptor;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.grid.GeneralGridEnvelope;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.InvalidGridGeometryException;
import org.geotools.coverage.processing.CannotReprojectException;
import org.geotools.coverage.processing.CoverageProcessor;
import org.geotools.coverage.util.CoverageUtilities;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.image.ImageWorker;
import org.geotools.image.util.ImageUtilities;
import org.geotools.metadata.i18n.ErrorKeys;
import org.geotools.metadata.i18n.Errors;
import org.geotools.metadata.i18n.LoggingKeys;
import org.geotools.metadata.i18n.Loggings;
import org.geotools.referencing.CRS;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.geotools.referencing.operation.AbstractCoordinateOperationFactory;
import org.geotools.referencing.operation.matrix.XAffineTransform;
import org.geotools.referencing.operation.transform.AffineTransform2D;
import org.geotools.referencing.operation.transform.DimensionFilter;
import org.geotools.referencing.operation.transform.IdentityTransform;
import org.geotools.referencing.operation.transform.WarpBuilder;
import org.geotools.referencing.util.CRSUtilities;
import org.geotools.util.Utilities;
import org.geotools.util.XArray;
import org.geotools.util.factory.Hints;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.coverage.grid.GridEnvelope;
import org.opengis.coverage.grid.GridGeometry;
import org.opengis.geometry.Envelope;
import org.opengis.metadata.spatial.PixelOrientation;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.datum.PixelInCell;
import org.opengis.referencing.operation.CoordinateOperation;
import org.opengis.referencing.operation.CoordinateOperationFactory;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.MathTransform2D;
import org.opengis.referencing.operation.MathTransformFactory;
import org.opengis.referencing.operation.NoninvertibleTransformException;
import org.opengis.referencing.operation.OperationNotFoundException;
import org.opengis.referencing.operation.TransformException;

/**
 * Implementation of the {@link Resample} operation. This implementation is provided as a separated
 * class for two purpose: avoid loading this code before needed and provide some way to check if a
 * grid coverages is a result of a resample operation.
 *
 * @since 2.2
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 * @author Simone Giannecchini, GeoSolutions SAS
 * @author Daniele Romagnoli, GeoSolutions SAS
 * @author Nicola Lagomarsini, GeoSolutions SAS
 */
final class Resampler2D extends GridCoverage2D {
    /** For compatibility during cross-version serialization. */
    private static final long serialVersionUID = -8593569923766544474L;

    /**
     * The corner to use for performing calculation. By default {@link GridGeometry#getGridToCRS()}
     * maps to pixel center (as of OGC specification). In JAI, the transforms rather map to the
     * upper left corner.
     */
    private static final PixelOrientation CORNER = PixelOrientation.UPPER_LEFT; // UPPER_LEFT;

    /**
     * When an empirical adjustement of the Warp transform seems necessary, the amount of
     * subdivisions to try.
     */
    private static final int EMPIRICAL_ADJUSTMENT_STEPS = 16;

    /** Small tolerance threshold for floating point number comparaisons. */
    private static final double EPS = 1E-6;

    /** The logging level for defails about resampling operation applied. */
    private static final Level LOGGING_LEVEL = Level.FINE;

    /**
     * Constructs a new grid coverage for the specified grid geometry.
     *
     * @param source The source for this grid coverage.
     * @param image The image.
     * @param geometry The grid geometry (including the new CRS).
     * @param sampleDimensions The sample dimensions to be given to the new coverage.
     */
    private Resampler2D(
            final GridCoverage2D source,
            final PlanarImage image,
            final GridGeometry2D geometry,
            final GridSampleDimension[] sampleDimensions,
            final Map<String, Object> properties,
            final Hints hints) {
        super(
                source.getName(),
                image,
                geometry,
                sampleDimensions,
                new GridCoverage2D[] {source},
                properties,
                hints);
    }

    /**
     * Constructs a new grid coverage for the specified grid geometry.
     *
     * @param source The source for this grid coverage.
     * @param image The image.
     * @param geometry The grid geometry (including the new CRS).
     * @param operation The operation used to resample the coverage
     * @param warp The warp configuration in case a warp is being used
     */
    private static GridCoverage2D create(
            final GridCoverage2D source,
            final PlanarImage image,
            final GridGeometry2D geometry,
            final String operation,
            final Warp warp,
            final Hints hints,
            final Map<String, Object> inProperties) {
        GridSampleDimension[] sampleDimensions = source.getSampleDimensions();

        // Ensure no SampleDimension is defined if the ColorModel must be expanded
        if (sampleDimensions != null && isColorModelExpanded(source.getRenderedImage(), hints)) {
            sampleDimensions = null;
        }

        /**
         * Build up transformation info properties: this can be used by the client to decide whether
         * it wants to actually do the resample (which might take hours with WarpAdapter on very
         * large grids) or not
         */
        Map<String, Object> properties = new HashMap<>();
        if (operation != null) {
            properties.put(Resample.OPERATION, operation);
            if (warp != null) {
                properties.put(Resample.WARP_TYPE, warp.getClass());
                if (warp instanceof WarpGrid) {
                    WarpGrid grid = (WarpGrid) warp;
                    Dimension dimension = new Dimension(grid.getXNumCells(), grid.getYNumCells());
                    properties.put(Resample.GRID_DIMENSIONS, dimension);
                }
            }
        }
        properties.putAll(inProperties);
        /*
         * The resampling may have been performed on the geophysics view.
         * Try to restore the original view.
         */
        GridCoverage2D coverage =
                new Resampler2D(source, image, geometry, sampleDimensions, properties, hints);
        return coverage;
    }

    /**
     * Creates a new coverage with a different coordinate reference reference system. If a grid
     * geometry is supplied, only its {@linkplain GridGeometry2D#getRange grid range} and
     * {@linkplain GridGeometry2D#getGridToCRS grid to CRS} transform are taken in account.
     *
     * @param sourceCoverage The source grid coverage.
     * @param targetCRS Coordinate reference system for the new grid coverage, or {@code null}.
     * @param targetGG The target grid geometry, or {@code null} for default.
     * @param interpolation The interpolation to use, or {@code null} if none.
     * @param hints The rendering hints. This is usually provided by {@link CoverageProcessor}. This
     *     method will looks for {@link Hints#COORDINATE_OPERATION_FACTORY} and {@link
     *     Hints#JAI_INSTANCE} keys.
     * @return The new grid coverage, or {@code sourceCoverage} if no resampling was needed.
     * @throws FactoryException if a transformation step can't be created.
     * @throws TransformException if a transformation failed.
     */
    public static GridCoverage2D reproject(
            GridCoverage2D sourceCoverage,
            CoordinateReferenceSystem targetCRS,
            GridGeometry2D targetGG,
            final Interpolation interpolation,
            final Hints hints)
            throws FactoryException, TransformException {
        return reproject(sourceCoverage, targetCRS, targetGG, interpolation, hints, null);
    }

    private static class ResampleState {
        static class TransformSteps {
            @SuppressWarnings("unused")
            final MathTransform step1, step2, step3, allSteps;
            final MathTransform2D allSteps2D;

            public TransformSteps(
                    MathTransform step1,
                    MathTransform step2,
                    MathTransform step3,
                    MathTransform allSteps,
                    MathTransform2D allSteps2D) {
                this.step1 = step1;
                this.step2 = step2;
                this.step3 = step3;
                this.allSteps = allSteps;
                this.allSteps2D = allSteps2D;
            }
        }

        final GridCoverage2D sourceCoverage;
        PlanarImage sourceImage;

        Hints hints;

        CoordinateReferenceSystem sourceCRS;
        CoordinateReferenceSystem targetCRS;

        Interpolation interpolation;

        boolean automaticGridRange;
        boolean automaticGridGeometry;

        GridGeometry2D sourceGridGeometry;
        GridGeometry2D targetGridGeometry;

        Map<String, Object> sourceProps;
        ROI roi;
        Range nodata;
        double[] backgroundValues;

        CoordinateOperationFactory factory;
        MathTransformFactory mtFactory;

        TransformSteps transformSteps;

        RenderedOp targetImage;
        String operationName;
        Warp warp;
        RenderingHints targetHints;

        public ResampleState(GridCoverage2D sourceCoverage) {
            this.sourceCoverage = sourceCoverage;
        }
    }

    /**
     * Creates a new coverage with a different coordinate reference reference system. If a grid
     * geometry is supplied, only its {@linkplain GridGeometry2D#getRange grid range} and
     * {@linkplain GridGeometry2D#getGridToCRS grid to CRS} transform are taken in account.
     *
     * @param sourceCoverage The source grid coverage.
     * @param targetCRS Coordinate reference system for the new grid coverage, or {@code null}.
     * @param targetGG The target grid geometry, or {@code null} for default.
     * @param interpolation The interpolation to use, or {@code null} if none.
     * @param hints The rendering hints. This is usually provided by {@link CoverageProcessor}. This
     *     method will looks for {@link Hints#COORDINATE_OPERATION_FACTORY} and {@link
     *     Hints#JAI_INSTANCE} keys.
     * @param backgroundValues The background values to be used by the underlying JAI operation, or
     *     {@code null} if none.
     * @return The new grid coverage, or {@code sourceCoverage} if no resampling was needed.
     * @throws FactoryException if a transformation step can't be created.
     * @throws TransformException if a transformation failed.
     */
    public static GridCoverage2D reproject(
            final GridCoverage2D sourceCoverage,
            final CoordinateReferenceSystem targetCRS,
            final GridGeometry2D targetGG,
            final Interpolation interpolation,
            final Hints hints,
            final double[] backgroundValues)
            throws FactoryException, TransformException {

        ResampleState state;
        GridCoverage2D targetCoverage;

        ////////////////////////////////////////////////////////////////////////////////////////
        ////                                                                                ////
        //// =======>>  STEP 1: Extracts needed informations from the parameters   <<====== ////
        ////            STEP 2: Creates the "target to source" MathTransform                ////
        ////            STEP 3: Computes the target image layout                            ////
        ////            STEP 4: Applies the JAI operation ("Affine", "Warp", etc)           ////
        ////                                                                                ////
        ////////////////////////////////////////////////////////////////////////////////////////
        state =
                resolveParameters(
                        sourceCoverage,
                        targetCRS,
                        targetGG,
                        interpolation,
                        hints,
                        backgroundValues);

        /*
         * Optimization: If the source coverage is already the result of a previous "Resample" operation,
         * go up in the chain and check if a previously computed image could fits (i.e. the requested
         * resampling may be the inverse of a previous resampling), returning immediately if a suitable image is found.
         */
        targetCoverage = reuseExistingCoverage(state);
        if (targetCoverage == null) {
            createSourcePlanarImage(state);
            resolveRoiAndNoData(state);

            //// =======>>  STEP 2: Creates the "target to source" MathTransform       <<====== ////
            createTargetToSourceTransform(state);

            //// =======>>  STEP 3: Computes the target image layout                   <<====== ////
            computeTargetImageLayout(state);

            //// =======>>  STEP 4: Applies the JAI operation ("Affine", "Warp", etc)  <<====== ////
            targetCoverage = applyJaiOpAndCreateCoverage(state);
        }
        return targetCoverage;
    }

    private static GridCoverage2D applyJaiOpAndCreateCoverage(ResampleState state)
            throws FactoryException, TransformException {

        GridCoverage2D targetCoverage = null;

        final Rectangle sourceBB = state.sourceGridGeometry.getGridRange2D();
        final Rectangle targetBB = state.targetGridGeometry.getGridRange2D();
        final PlanarImage sourceImage = state.sourceImage;
        final MathTransform allSteps = state.transformSteps.allSteps;

        ImageWorker w = new ImageWorker(sourceImage);
        w.setROI(state.roi);
        w.setBackground(state.backgroundValues);
        w.setNoData(state.nodata);
        w.setRenderingHints(state.targetHints);
        final Map<String, Object> imageProperties = new HashMap<>();

        final boolean txIsAffine = allSteps instanceof AffineTransform;
        final boolean txIsIdentity =
                allSteps.isIdentity()
                        || (txIsAffine
                                && XAffineTransform.isIdentity((AffineTransform) allSteps, EPS));
        final boolean optimizedAffinePath =
                txIsAffine && (state.automaticGridRange || targetBB.equals(sourceBB));

        if (txIsIdentity) {
            targetCoverage = prepareOrCreateIdentityTargetImage(w, state);
        } else if (optimizedAffinePath) {
            targetCoverage = prepareOrCreateAffineTargetImage(w, state);
        } else {
            targetCoverage = prepareOrCreateWarpTargetImage(w, imageProperties, state);
        }

        final boolean createdByAnOptimizedPath = targetCoverage != null;
        if (!createdByAnOptimizedPath) {
            state.targetImage = w.getRenderedOperation();
            for (Map.Entry<String, Object> entry : imageProperties.entrySet()) {
                state.targetImage.setProperty(entry.getKey(), entry.getValue());
            }

            adjustTargetGridRangeToActualGridRange(state);
            targetCoverage = createTargetCoverage(state);
            logResult(state, targetCoverage);
        }
        return targetCoverage;
    }

    private static GridCoverage2D prepareOrCreateWarpTargetImage(
            ImageWorker w, Map<String, Object> imageProperties, ResampleState state)
            throws InvalidGridGeometryException, TransformException, FactoryException {

        final GridCoverage2D sourceCoverage = state.sourceCoverage;
        final PlanarImage sourceImage = state.sourceImage;

        /*
         * General case: constructs the warp transform.
         *
         * TODO: JAI 1.1.3 seems to have a bug when the target envelope is greater than
         *       the source envelope:  Warp on float values doesn't set to 'background'
         *       the points outside the envelope. The operation seems to work correctly
         *       on integer values, so as a workaround we restart the operation without
         *       interpolation (which increase the chances to get it down on integers).
         *       Remove this hack when this JAI bug will be fixed.
         *
         * TODO: Move the check for AffineTransform into WarpTransform2D.
         */
        boolean forceAdapter = false;
        if (!JAIExt.isJAIExtOperation("Warp")) {
            switch (sourceImage.getSampleModel().getTransferType()) {
                case DataBuffer.TYPE_DOUBLE:
                case DataBuffer.TYPE_FLOAT:
                    {
                        CoordinateReferenceSystem targetCRS = state.targetCRS;
                        GridGeometry2D targetGG = state.targetGridGeometry;

                        Envelope sourceBB = state.sourceGridGeometry.getEnvelope();
                        Envelope targetBB = targetGG.getEnvelope();

                        Envelope source = CRS.transform(sourceBB, targetCRS);
                        source = targetGG.reduce(source);

                        Envelope target = CRS.transform(targetBB, targetCRS);
                        target = targetGG.reduce(target);
                        if (!(new GeneralEnvelope(source).contains(target, true))) {
                            Interpolation interpolation = state.interpolation;
                            if (interpolation != null
                                    && !(interpolation instanceof InterpolationNearest)) {
                                state.hints.add(ImageUtilities.NN_INTERPOLATION_HINT);
                                return reproject(
                                        sourceCoverage,
                                        targetCRS,
                                        targetGG,
                                        null,
                                        state.hints,
                                        state.backgroundValues);
                            } else {
                                // If we were already using nearest-neighbor interpolation,
                                // force usage of WarpAdapter2D instead of WarpAffine. The
                                // price will be a slower reprojection.
                                forceAdapter = true;
                            }
                        }
                    }
            }
        }
        // -------- End of JAI bug workaround --------
        setupWarp(state, w, imageProperties, forceAdapter);
        return null;
    }

    /**
     * Special case for the affine transform. Try to use the JAI "Affine" operation instead of the
     * more general "Warp" one. JAI provides native acceleration for the affine operation.
     *
     * <p>NOTE 1: There is no need to check for "Scale" and "Translate" as special cases of "Affine"
     * since JAI already does this check for us.
     *
     * <p>NOTE 2: "Affine", "Scale", "Translate", "Rotate" and similar operations ignore the 'xmin',
     * 'ymin', 'width' and 'height' image layout. Consequently, we can't use this operation if the
     * user provided explicitly a grid range.
     *
     * <p>NOTE 3: If the user didn't specified any grid geometry, then a yet cheaper approach is to
     * just update the 'gridToCRS' value. We returns a grid coverage wrapping the SOURCE image with
     * the updated grid geometry.
     */
    private static GridCoverage2D prepareOrCreateAffineTargetImage(
            ImageWorker w, ResampleState state)
            throws NoninvertibleTransformException, FactoryException {
        if (state.automaticGridGeometry) {
            // Cheapest approach: just update 'gridToCRS'.
            MathTransform mtr;
            mtr = state.sourceGridGeometry.getGridToCRS(CORNER);
            mtr =
                    state.mtFactory.createConcatenatedTransform(
                            mtr, state.transformSteps.step2.inverse());
            state.targetGridGeometry =
                    new GridGeometry2D(
                            state.sourceGridGeometry.getGridRange(), mtr, state.targetCRS);
            /*
             * Note: do NOT use the "GridGeometry2D(sourceGridRange, targetEnvelope)"
             * constructor in the above line. We must give a MathTransform argument to
             * the constructor, not an Envelope, because the later infer a MathTransform
             * using heuristic rules. Only the constructor with a MathTransform argument
             * is fully accurate.
             */
            return create(
                    state.sourceCoverage,
                    state.sourceImage,
                    state.targetGridGeometry,
                    null,
                    null,
                    state.hints,
                    state.sourceProps);
        }
        // More general approach: apply the affine transform.
        final AffineTransform affine = (AffineTransform) state.transformSteps.allSteps.inverse();
        w.affine(affine, state.interpolation, state.backgroundValues);
        state.operationName = "Affine";
        CoverageUtilities.setROIProperty(state.sourceProps, w.getROI());
        CoverageUtilities.setNoDataProperty(state.sourceProps, w.getNoData());
        return null;
    }

    private static GridCoverage2D prepareOrCreateIdentityTargetImage(
            ImageWorker w, ResampleState state) {
        PlanarImage sourceImage = state.sourceImage;
        w.setImage(sourceImage);
        final Rectangle sourceBB = state.sourceGridGeometry.getGridRange2D();
        final Rectangle targetBB = state.targetGridGeometry.getGridRange2D();
        if (targetBB.equals(sourceBB)) {
            /*
             * Optimization in case we have nothing to do, not even a crop. Reverts to the
             * original coverage BEFORE to creates Resampler2D. Note that while there is
             * nothing to do, the target CRS is not identical to the source CRS (so we need
             * to create a new coverage) otherwise this condition would have been detected
             * sooner in this method.
             */
            return create(
                    state.sourceCoverage,
                    sourceImage,
                    state.targetGridGeometry,
                    null,
                    null,
                    state.hints,
                    state.sourceProps);
        }
        /*
         * If the user requests a new grid geometry with the same coordinate reference system,
         * and if the grid geometry is equivalents to a simple extraction of a sub-area, then
         * delegates the work to a "Crop" operation.
         */
        if (sourceBB.contains(targetBB)) {
            w.crop(targetBB.x, targetBB.y, targetBB.width, targetBB.height);
            CoverageUtilities.setROIProperty(state.sourceProps, w.getROI());
            CoverageUtilities.setNoDataProperty(state.sourceProps, w.getNoData());
            state.operationName = "Crop";
        } else {
            w.setNoData(null);
            w.mosaic(
                    new RenderedImage[] {sourceImage},
                    MosaicDescriptor.MOSAIC_TYPE_OVERLAY,
                    null,
                    new ROI[] {state.roi},
                    null,
                    state.nodata != null ? new Range[] {state.nodata} : null);
            CoverageUtilities.setROIProperty(state.sourceProps, w.getROI());
            CoverageUtilities.setNoDataProperty(state.sourceProps, w.getNoData());
            state.operationName = "Mosaic";
        }
        return null; // prepared parameters, no optimized return
    }

    private static void setupWarp(
            ResampleState state,
            ImageWorker w,
            final Map<String, Object> imageProperties,
            boolean forceAdapter)
            throws TransformException, FactoryException {
        final MathTransform2D transform = state.transformSteps.allSteps2D;
        final CharSequence name = state.sourceCoverage.getName();
        final Rectangle sourceBB = state.sourceGridGeometry.getGridRange2D();
        final Rectangle targetBB = state.targetGridGeometry.getGridRange2D();
        state.operationName = "Warp";
        if (forceAdapter) {
            state.warp = new WarpBuilder(0.0).buildWarp(transform, sourceBB);
        } else {
            state.warp =
                    createWarp(name, sourceBB, targetBB, transform, state.mtFactory, state.hints);
        }
        // store the transformation in the properties, as we might want to retrieve and
        // chain it with affine transforms down the chain
        imageProperties.put("MathTransform", transform);
        imageProperties.put("SourceBoundingBox", sourceBB);
        w.warp(state.warp, state.interpolation);
        CoverageUtilities.setROIProperty(state.sourceProps, w.getROI());
        CoverageUtilities.setNoDataProperty(state.sourceProps, w.getNoData());
    }

    /**
     * The JAI operation sometime returns an image with a bounding box different than what we
     * expected. This is true especially for the "Affine" operation: the JAI documentation said
     * explicitly that xmin, ymin, width and height image layout hints are ignored for this one. As
     * a safety, we check the bounding box in any case. If it doesn't matches, then we will
     * reconstruct the target grid geometry.
     */
    private static void adjustTargetGridRangeToActualGridRange(ResampleState state) {
        final GridEnvelope targetGR = state.targetGridGeometry.getGridRange();
        final int[] lower = targetGR.getLow().getCoordinateValues();
        final int[] upper = targetGR.getHigh().getCoordinateValues();
        offsetUpper(upper);
        lower[state.targetGridGeometry.gridDimensionX] = state.targetImage.getMinX();
        lower[state.targetGridGeometry.gridDimensionY] = state.targetImage.getMinY();
        upper[state.targetGridGeometry.gridDimensionX] = state.targetImage.getMaxX();
        upper[state.targetGridGeometry.gridDimensionY] = state.targetImage.getMaxY();
        final GridEnvelope actualGR = new GeneralGridEnvelope(lower, upper);
        if (!targetGR.equals(actualGR)) {
            state.targetGridGeometry =
                    new GridGeometry2D(
                            actualGR,
                            state.targetGridGeometry.getGridToCRS(PixelInCell.CELL_CENTER),
                            state.targetCRS);
            if (!state.automaticGridRange) {
                log(
                        Loggings.getResources(state.sourceCoverage.getLocale())
                                .getLogRecord(
                                        Level.FINE,
                                        LoggingKeys.ADJUSTED_GRID_GEOMETRY_$1,
                                        state.sourceCoverage
                                                .getName()
                                                .toString(state.sourceCoverage.getLocale())));
            }
        }
    }

    private static void logResult(final ResampleState state, GridCoverage2D targetCoverage) {
        if (CoverageProcessor.LOGGER.isLoggable(LOGGING_LEVEL)) {
            log(
                    Loggings.getResources(state.sourceCoverage.getLocale())
                            .getLogRecord(
                                    LOGGING_LEVEL,
                                    LoggingKeys.APPLIED_RESAMPLE_$11,
                                    new Object[] {
                                        /*  {0} */ state.sourceCoverage
                                                .getName()
                                                .toString(state.sourceCoverage.getLocale()),
                                        /*  {1} */ state.sourceCoverage
                                                .getCoordinateReferenceSystem()
                                                .getName()
                                                .getCode(),
                                        /*  {2} */ state.sourceImage.getWidth(),
                                        /*  {3} */ state.sourceImage.getHeight(),
                                        /*  {4} */ targetCoverage
                                                .getCoordinateReferenceSystem()
                                                .getName()
                                                .getCode(),
                                        /*  {5} */ state.targetImage.getWidth(),
                                        /*  {6} */ state.targetImage.getHeight(),
                                        /*  {7} */ state.targetImage.getOperationName(),
                                        /*  {8} */ Integer.valueOf(1),
                                        /*  {9} */ ImageUtilities.getInterpolationName(
                                                state.interpolation),
                                        /* {10} */ (state.backgroundValues != null)
                                                ? state.backgroundValues.length == 1
                                                        ? (Double.isNaN(state.backgroundValues[0])
                                                                ? "NaN"
                                                                : Double.valueOf(
                                                                        state.backgroundValues[0]))
                                                        : XArray.toString(
                                                                state.backgroundValues,
                                                                state.sourceCoverage.getLocale())
                                                : "No background used"
                                    }));
        }
    }

    private static GridCoverage2D createTargetCoverage(final ResampleState state) {
        GridCoverage2D targetCoverage;
        /*
         * Constructs the final grid coverage, then log a message as in the following example:
         *
         *     Resampled coverage "Foo" from coordinate system "myCS" (for an image of size
         *     1000x1500) to coordinate system "WGS84" (image size 1000x1500). JAI operation
         *     is "Warp" with "Nearest" interpolation on geophysics pixels values. Background
         *     value is 255.
         */
        targetCoverage =
                create(
                        state.sourceCoverage,
                        state.targetImage,
                        state.targetGridGeometry,
                        state.operationName,
                        state.warp,
                        state.hints,
                        state.sourceProps);
        assert CRS.equalsIgnoreMetadata(
                        targetCoverage.getCoordinateReferenceSystem(), state.targetCRS)
                : state.targetGridGeometry;
        assert targetCoverage
                        .getGridGeometry()
                        .getGridRange2D()
                        .equals(state.targetImage.getBounds())
                : state.targetGridGeometry;
        return targetCoverage;
    }

    private static void computeTargetImageLayout(final ResampleState state) {
        state.targetHints = ImageUtilities.getRenderingHints(state.sourceImage);
        if (state.targetHints == null) {
            state.targetHints =
                    new RenderingHints(
                            JAI.KEY_INTERPOLATION,
                            Interpolation.getInstance(Interpolation.INTERP_NEAREST));
        } else {
            state.targetHints.put(
                    JAI.KEY_INTERPOLATION, Interpolation.getInstance(Interpolation.INTERP_NEAREST));
        }
        state.targetHints.put(JAI.KEY_REPLACE_INDEX_COLOR_MODEL, false);
        state.targetHints.put(JAI.KEY_TRANSFORM_ON_COLORMAP, false);
        if (state.hints != null) {
            state.targetHints.add(state.hints);
        }
        ImageLayout layout = (ImageLayout) state.targetHints.get(JAI.KEY_IMAGE_LAYOUT);
        if (layout != null) {
            layout = (ImageLayout) layout.clone();
        } else {
            layout = new ImageLayout();
            // Do not inherit the color model and sample model from the 'sourceImage';
            // Let the operation decide itself. This is necessary in case we change the
            // source, as we do if we choose the "Mosaic" operation.
        }
        final Rectangle targetBB = state.targetGridGeometry.getGridRange2D();
        if (isBoundsUndefined(layout, false)) {
            layout.setMinX(targetBB.x);
            layout.setMinY(targetBB.y);
            layout.setWidth(targetBB.width);
            layout.setHeight(targetBB.height);
        }
        if (isBoundsUndefined(layout, true)) {
            Dimension size =
                    new Dimension(
                            layout.getWidth(state.sourceImage),
                            layout.getHeight(state.sourceImage));
            size = ImageUtilities.toTileSize(size);
            layout.setTileGridXOffset(layout.getMinX(state.sourceImage));
            layout.setTileGridYOffset(layout.getMinY(state.sourceImage));
            layout.setTileWidth(size.width);
            layout.setTileHeight(size.height);
        }
        /*
         * Creates the background values array.
         */
        if (state.backgroundValues == null) {
            state.backgroundValues = CoverageUtilities.getBackgroundValues(state.sourceCoverage);
        }

        /*
         * We need to correctly manage the Hints to control the replacement of IndexColorModel.
         * It is worth to point out that setting the JAI.KEY_REPLACE_INDEX_COLOR_MODEL hint to
         * Boolean.TRUE is not enough to force the operators to do an expansion. If we explicitly
         * provide an ImageLayout built with the source image where the CM and the SM are valid.
         * those will be employed overriding a the possibility to expand the color model.
         */

        state.targetHints.put(JAI.KEY_IMAGE_LAYOUT, layout);
    }

    private static void createTargetToSourceTransform(final ResampleState state)
            throws NoninvertibleTransformException, FactoryException, TransformException,
                    OperationNotFoundException {
        /*
         * Computes the INVERSE of the math transform from [Source Grid] to [Target Grid].
         * The transform will be computed using the following path:
         *
         *      Target Grid --> Target CRS --> Source CRS --> Source Grid
         *                   ^              ^              ^
         *                 step 1         step 2         step 3
         *
         * If source and target CRS are equal, a shorter path is used. This special
         * case is needed because 'sourceCRS' and 'targetCRS' may be null.
         *
         *      Target Grid --> Common CRS --> Source Grid
         *                   ^              ^
         *                 step 1         step 3
         */
        final MathTransform step1, step2, step3, allSteps;
        if (CRS.equalsIgnoreMetadata(state.sourceCRS, state.targetCRS)) {
            /*
             * Note: targetGG should not be null, otherwise 'existingCoverage(...)' should
             *       have already detected that this resample is not doing anything.
             */
            if (!state.targetGridGeometry.isDefined(GridGeometry2D.GRID_TO_CRS_BITMASK)) {
                step1 =
                        state.sourceGridGeometry.getGridToCRS(
                                CORNER); // Really sourceGG, not targetGG
                step2 = IdentityTransform.create(step1.getTargetDimensions());
                step3 = step1.inverse();
                allSteps = IdentityTransform.create(step1.getSourceDimensions());
                state.targetGridGeometry =
                        new GridGeometry2D(
                                state.targetGridGeometry.getGridRange(), step1, state.targetCRS);
            } else {
                step1 = state.targetGridGeometry.getGridToCRS(CORNER);
                step2 = IdentityTransform.create(step1.getTargetDimensions());
                step3 = state.sourceGridGeometry.getGridToCRS(CORNER).inverse();
                allSteps = state.mtFactory.createConcatenatedTransform(step1, step3);
                if (!state.targetGridGeometry.isDefined(GridGeometry2D.GRID_RANGE_BITMASK)) {
                    /*
                     * If the target grid range was not explicitly specified, a grid range will be
                     * automatically computed in such a way that it will maps to the same envelope
                     * (at least approximatively).
                     */
                    Envelope gridRange;
                    gridRange = toEnvelope(state.sourceGridGeometry.getGridRange());
                    gridRange = CRS.transform(allSteps.inverse(), gridRange);
                    state.targetGridGeometry =
                            new GridGeometry2D(
                                    new GeneralGridEnvelope(gridRange, PixelInCell.CELL_CORNER),
                                    state.targetGridGeometry.getGridToCRS(PixelInCell.CELL_CENTER),
                                    state.targetCRS);
                }
            }
        } else {
            if (state.sourceCRS == null) {
                throw new CannotReprojectException(Errors.format(ErrorKeys.UNSPECIFIED_CRS));
            }
            CoordinateOperation operation =
                    state.factory.createOperation(state.sourceCRS, state.targetCRS);
            final CoordinateReferenceSystem compatibleSourceCRS =
                    compatibleSourceCRS(
                            state.sourceCoverage.getCoordinateReferenceSystem2D(),
                            state.sourceCRS,
                            state.targetCRS);
            final boolean force2D = (state.sourceCRS != compatibleSourceCRS);
            step2 =
                    state.factory
                            .createOperation(state.targetCRS, compatibleSourceCRS)
                            .getMathTransform();
            step3 =
                    (force2D
                                    ? state.sourceGridGeometry.getGridToCRS2D(CORNER)
                                    : state.sourceGridGeometry.getGridToCRS(CORNER))
                            .inverse();
            final Envelope sourceEnvelope =
                    state.sourceCoverage.getEnvelope(); // Don't force this one to 2D.
            final GeneralEnvelope targetEnvelope = CRS.transform(operation, sourceEnvelope);
            targetEnvelope.setCoordinateReferenceSystem(state.targetCRS);
            // 'targetCRS' may be different than the one set by CRS.transform(...).
            /*
             * If the target GridGeometry is incomplete, provides default
             * values for the missing fields. Three cases may occurs:
             *
             * - User provided no GridGeometry at all. Then, constructs an image of the same size
             *   than the source image and set an envelope big enough to contains the projected
             *   coordinates. The transform will derive from the grid range and the envelope.
             *
             * - User provided only a grid range.  Then, set an envelope big enough to contains
             *   the projected coordinates. The transform will derive from the grid range and
             *   the envelope.
             *
             * - User provided only a "grid to CRS" transform. Then, transform the projected
             *   envelope to "grid units" using the specified transform and create a grid range
             *   big enough to hold the result.
             */
            if (state.targetGridGeometry == null) {
                final GridEnvelope targetGR =
                        force2D
                                ? new GridEnvelope2D(state.sourceGridGeometry.getGridRange2D())
                                : state.sourceGridGeometry.getGridRange();
                state.targetGridGeometry = new GridGeometry2D(targetGR, targetEnvelope);
                step1 = state.targetGridGeometry.getGridToCRS(CORNER);
            } else if (!state.targetGridGeometry.isDefined(GridGeometry2D.GRID_TO_CRS_BITMASK)) {
                state.targetGridGeometry =
                        new GridGeometry2D(state.targetGridGeometry.getGridRange(), targetEnvelope);
                step1 = state.targetGridGeometry.getGridToCRS(CORNER);
            } else {
                step1 = state.targetGridGeometry.getGridToCRS(CORNER);
                if (!state.targetGridGeometry.isDefined(GridGeometry2D.GRID_RANGE_BITMASK)) {
                    GeneralEnvelope gridRange = CRS.transform(step1.inverse(), targetEnvelope);
                    // According OpenGIS specification, GridGeometry maps pixel's center.
                    state.targetGridGeometry =
                            new GridGeometry2D(
                                    new GeneralGridEnvelope(gridRange, PixelInCell.CELL_CENTER),
                                    step1,
                                    state.targetCRS);
                }
            }
            /*
             * Computes the final transform.
             */
            allSteps =
                    state.mtFactory.createConcatenatedTransform(
                            state.mtFactory.createConcatenatedTransform(step1, step2), step3);
        }
        final MathTransform allSteps2D =
                toMathTransform2D(allSteps, state.mtFactory, state.targetGridGeometry);
        if (!(allSteps2D instanceof MathTransform2D)) {
            // Should not happen with Geotools implementations. May happen
            // with some external implementations, but should stay unusual.
            throw new TransformException(Errors.format(ErrorKeys.NO_TRANSFORM2D_AVAILABLE));
        }
        state.transformSteps =
                new ResampleState.TransformSteps(
                        step1, step2, step3, allSteps, (MathTransform2D) allSteps2D);
    }

    private static void createSourcePlanarImage(ResampleState state) {
        PlanarImage sourceImage =
                PlanarImage.wrapRenderedImage(state.sourceCoverage.getRenderedImage());
        assert state.sourceCoverage.getCoordinateReferenceSystem() == state.sourceCRS
                : state.sourceCoverage;
        state.sourceImage = sourceImage;
        state.factory = ReferencingFactoryFinder.getCoordinateOperationFactory(state.hints);
        state.mtFactory = findMathTransformFactory(state.factory, state.hints);
    }

    private static ResampleState resolveParameters(
            final GridCoverage2D providedSourceCoverage,
            final CoordinateReferenceSystem providedTargetCRS,
            final GridGeometry2D providedTargetGG,
            final Interpolation providedInterpolation,
            final Hints providedHints,
            double[] providedBackgroundValues)
            throws FactoryException {

        Utilities.ensureNonNull("sourceCoverage", providedSourceCoverage);

        ResampleState state = new ResampleState(providedSourceCoverage);
        state.hints = providedHints == null ? new Hints() : new Hints(providedHints);
        state.sourceCRS = state.sourceCoverage.getCoordinateReferenceSystem();
        state.targetCRS = resolveTargetCRS(state.sourceCRS, providedTargetCRS, providedTargetGG);
        state.interpolation = prepareHints(providedInterpolation, state.hints);
        state.backgroundValues = providedBackgroundValues;

        state.sourceGridGeometry = state.sourceCoverage.getGridGeometry();
        state.automaticGridRange = shallComputeGridRange(providedTargetGG);
        state.automaticGridGeometry =
                shallComputeGridGeometry(providedTargetGG, state.automaticGridRange);
        state.targetGridGeometry =
                computeTargetGridGeometry(
                        state.sourceCoverage, providedTargetGG, state.automaticGridRange);

        @SuppressWarnings("unchecked")
        Map<String, Object> sourceProps = state.sourceCoverage.getProperties();
        state.sourceProps = sourceProps != null ? new HashMap<>(sourceProps) : new HashMap<>();

        return state;
    }

    private static ResampleState resolveRoiAndNoData(ResampleState state) {
        /*
         * The projection are usually applied on floating-point values, in order
         * to gets maximal precision and to handle correctly the special case of
         * NaN values. However, we can apply the projection on integer values if
         * the interpolation type is "nearest neighbor", since this is not really
         * an interpolation.
         *
         * If this condition is met, then we verify if an "integer version" of the image
         * is available as a source of the source coverage (i.e. the floating-point image
         * is derived from the integer image, not the converse).
         */
        // Getting optional ROI and NoData as properties
        Object roiProp = state.sourceProps.get("GC_ROI");
        NoDataContainer nodataProp = CoverageUtilities.getNoDataProperty(state.sourceCoverage);
        state.roi = (roiProp != null && roiProp instanceof ROI) ? (ROI) roiProp : null;
        state.nodata = nodataProp != null ? nodataProp.getAsRange() : null;

        return state;
    }

    private static MathTransformFactory findMathTransformFactory(
            CoordinateOperationFactory factory, Hints hints) {
        if (factory instanceof AbstractCoordinateOperationFactory) {
            return ((AbstractCoordinateOperationFactory) factory).getMathTransformFactory();
        }
        return ReferencingFactoryFinder.getMathTransformFactory(hints);
    }

    private static GridGeometry2D computeTargetGridGeometry(
            GridCoverage2D sourceCoverage, GridGeometry2D targetGG, final boolean automaticGR) {
        if (targetGG == null) {
            return null;
        }
        /*
         * Grid range and "grid to CRS" transform are the only grid geometry informations used
         * by this method. If they are not available, this is equivalent to not providing grid
         * geometry at all. In such case set to 'targetGG' reference to null, since null value
         * is what the remaining code will check for.
         */
        if (!automaticGR || targetGG.isDefined(GridGeometry2D.GRID_TO_CRS_BITMASK)) {
            return targetGG;
        }
        /*
         * Before to abandon the grid geometry, checks if it contains an envelope (note:
         * we really want it in sourceCRS, not targetCRS - the reprojection will be done
         * later in this method). If so, we will recreate a new grid geometry from the
         * envelope using the same "grid to CRS" transform than the original coverage.
         * The result may be an image with a different size.
         */
        if (targetGG.isDefined(GridGeometry2D.ENVELOPE_BITMASK)) {
            final Envelope envelope = targetGG.getEnvelope();
            final GridGeometry2D sourceGG = sourceCoverage.getGridGeometry();
            final MathTransform gridToCRS;
            switch (envelope.getDimension()) {
                case 2:
                    gridToCRS = sourceGG.getGridToCRS2D(CORNER);
                    break;
                default:
                    gridToCRS = sourceGG.getGridToCRS(CORNER);
                    break;
            }
            return new GridGeometry2D(PixelInCell.CELL_CENTER, gridToCRS, envelope, null);
        }
        return null;
    }

    /** Whether the target GridRange should be computed automatically. */
    private static boolean shallComputeGridRange(GridGeometry2D targetGG) {
        return null == targetGG || !targetGG.isDefined(GridGeometry2D.GRID_RANGE_BITMASK);
    }

    /**
     * Whether the target GridGeometry should be computed automatically, or if we should follow
     * strictly what the user said. Note that "automaticGG" implies "automaticGR" but the converse
     * is not necessarily true.
     */
    private static boolean shallComputeGridGeometry(GridGeometry2D targetGG, boolean automaticGR) {
        if (targetGG == null) {
            return true;
        }
        if (!automaticGR || targetGG.isDefined(GridGeometry2D.GRID_TO_CRS_BITMASK)) {
            return false;
        }
        if (targetGG.isDefined(GridGeometry2D.ENVELOPE_BITMASK)) {
            return false;
        }
        return true;
    }

    // INTERPOLATION MANAGEMENT as well as BORDER_EXTENDER
    private static Interpolation prepareHints(Interpolation interpolation, final Hints hints) {
        if (interpolation == null) {

            // if we did not the interpolation, let's try to get it from hints
            if (hints != null) {
                // JAI interpolation
                if (hints.containsKey(JAI.KEY_INTERPOLATION))
                    interpolation = (Interpolation) hints.get(JAI.KEY_INTERPOLATION);
            }
        } else {
            // we have been provided with interpolation, let's override hints
            hints.put(JAI.KEY_INTERPOLATION, interpolation);
        }
        if (hints != null && !hints.containsKey(JAI.KEY_BORDER_EXTENDER)) {
            hints.put(
                    JAI.KEY_BORDER_EXTENDER,
                    BorderExtender.createInstance(BorderExtender.BORDER_COPY));
        }
        return interpolation;
    }

    private static CoordinateReferenceSystem resolveTargetCRS(
            final CoordinateReferenceSystem sourceCRS,
            CoordinateReferenceSystem targetCRS,
            GridGeometry2D targetGG)
            throws FactoryException {
        if (targetCRS == null) {
            // in case the TargetCRS has not been set we try to use the CRS that was part of the
            // TargetGG
            if (targetGG != null && targetGG.isDefined(GridGeometry2D.CRS_BITMASK)) {
                targetCRS = targetGG.getCoordinateReferenceSystem();
            } else {
                // in case the TargetCRS is not set and either the TargetGG is not set
                // or it has not CRS inside, we reuse sourceCRS
                targetCRS = sourceCRS;
            }
            // From this point, consider "targetCRS" as final.
        } else {
            // in case the targetCRS is set we should check that it is compatible
            // with the targetGG crs, otherwise we throw an exception
            if (targetGG != null && targetGG.isDefined(GridGeometry2D.CRS_BITMASK)) {
                final CoordinateReferenceSystem targetGGCRS =
                        targetGG.getCoordinateReferenceSystem();
                if (!CRS.equalsIgnoreMetadata(targetCRS, targetGGCRS)
                        && !CRS.findMathTransform(targetCRS, targetGGCRS).isIdentity()) {
                    throw new IllegalArgumentException(
                            Errors.format(
                                    ErrorKeys.ILLEGAL_ARGUMENT_$1,
                                    "TargetCRS must be compatible with TargetGG CRS"));
                }
            }
        }
        return targetCRS;
    }

    @SuppressWarnings("PMD.ForLoopCanBeForeach")
    private static void offsetUpper(int[] upper) {
        for (int i = 0; i < upper.length; i++) {
            upper[i]++; // Make them exclusive.
        }
    }

    /*
     * If the source coverage is already the result of a previous "Resample" operation,
     * go up in the chain and check if a previously computed image could fits (i.e. the
     * requested resampling may be the inverse of a previous resampling).
     */
    private static GridCoverage2D reuseExistingCoverage(ResampleState state) {
        GridCoverage2D coverage = state.sourceCoverage;
        CoordinateReferenceSystem targetCRS = state.targetCRS;
        GridGeometry2D targetGG = state.targetGridGeometry;

        // NoData and ROI must be handled
        ROI roiProp = CoverageUtilities.getROIProperty(coverage);
        Object nodataProp = CoverageUtilities.getNoDataProperty(coverage);
        boolean hasROI = (roiProp != null);
        boolean hasNoData = (nodataProp != null);
        if (hasROI || hasNoData) {
            return null;
        }
        while (!equivalent(coverage.getGridGeometry(), targetGG)
                || (!CRS.equalsIgnoreMetadata(targetCRS, coverage.getCoordinateReferenceSystem())
                        && !CRS.equalsIgnoreMetadata(
                                targetCRS, coverage.getCoordinateReferenceSystem2D()))) {
            if (!(coverage instanceof Resampler2D)) {
                return null;
            }

            final List<GridCoverage> sources = coverage.getSources();
            assert sources.size() == 1 : sources;
            coverage = (GridCoverage2D) sources.get(0);
        }
        return coverage;
    }

    /**
     * Returns {@code true} if the image or tile location and size are totally undefined.
     *
     * @param layout The image layout to query.
     * @param tile {@code true} for testing tile bounds, or {@code false} for testing image bounds.
     */
    private static boolean isBoundsUndefined(final ImageLayout layout, final boolean tile) {
        final int mask;
        if (tile) {
            mask =
                    ImageLayout.TILE_GRID_X_OFFSET_MASK
                            | ImageLayout.TILE_WIDTH_MASK
                            | ImageLayout.TILE_GRID_Y_OFFSET_MASK
                            | ImageLayout.TILE_HEIGHT_MASK;
        } else {
            mask =
                    ImageLayout.MIN_X_MASK
                            | ImageLayout.WIDTH_MASK
                            | ImageLayout.MIN_Y_MASK
                            | ImageLayout.HEIGHT_MASK;
        }
        return (layout.getValidMask() & mask) == 0;
    }

    /**
     * Returns {@code true} if the image colormodel must be expanded for the operation.
     *
     * @param image The image to reproject.
     * @param hints The {@link Hints} used during the reprojection.
     */
    private static boolean isColorModelExpanded(final RenderedImage image, final Hints hints) {

        if (image.getColorModel() instanceof IndexColorModel) {
            if (hints != null && hints.containsKey(JAI.KEY_REPLACE_INDEX_COLOR_MODEL)) {
                Boolean replace = (Boolean) hints.get(JAI.KEY_REPLACE_INDEX_COLOR_MODEL);
                if (replace != null) {
                    return replace;
                }
            }
        }
        return false;
    }

    /**
     * Returns a source CRS compatible with the given target CRS. This method try to returns a CRS
     * which would not thrown an {@link NoninvertibleTransformException} if attempting to transform
     * from "target" to "source" (reminder: Warp works on <strong>inverse</strong> transforms).
     *
     * @param sourceCRS2D The two-dimensional source CRS. Actually, this method accepts arbitrary
     *     dimension provided that are not greater than {@code sourceCRS}, but in theory it is 2D.
     * @param sourceCRS The n-dimensional source CRS.
     * @param targetCRS The n-dimensional target CRS.
     */
    private static CoordinateReferenceSystem compatibleSourceCRS(
            final CoordinateReferenceSystem sourceCRS2D,
            final CoordinateReferenceSystem sourceCRS,
            final CoordinateReferenceSystem targetCRS) {
        final int dim2D = sourceCRS2D.getCoordinateSystem().getDimension();
        return (targetCRS.getCoordinateSystem().getDimension() == dim2D
                        && sourceCRS.getCoordinateSystem().getDimension() > dim2D)
                ? sourceCRS2D
                : sourceCRS;
    }

    /**
     * Returns the math transform for the two specified dimensions of the specified transform.
     *
     * @param transform The transform.
     * @param mtFactory The factory to use for extracting the sub-transform.
     * @param sourceGG The grid geometry which is the source of the <strong>transform</strong>. This
     *     is {@code targetGG} in the {@link #reproject} method, because the later computes a
     *     transform from target to source grid geometry.
     * @return The {@link MathTransform2D} part of {@code transform}.
     * @throws FactoryException If {@code transform} is not separable.
     */
    private static MathTransform2D toMathTransform2D(
            final MathTransform transform,
            final MathTransformFactory mtFactory,
            final GridGeometry2D sourceGG)
            throws FactoryException {
        final DimensionFilter filter = new DimensionFilter(mtFactory);
        filter.addSourceDimension(sourceGG.axisDimensionX);
        filter.addSourceDimension(sourceGG.axisDimensionY);
        MathTransform candidate = filter.separate(transform);
        if (candidate instanceof MathTransform2D) {
            return (MathTransform2D) candidate;
        }
        filter.addTargetDimension(sourceGG.axisDimensionX);
        filter.addTargetDimension(sourceGG.axisDimensionY);
        candidate = filter.separate(transform);
        if (candidate instanceof MathTransform2D) {
            return (MathTransform2D) candidate;
        }
        throw new FactoryException(Errors.format(ErrorKeys.NO_TRANSFORM2D_AVAILABLE));
    }

    /**
     * Checks if two geometries are equal, ignoring unspecified fields. If one or both geometries
     * has no "gridToCRS" transform, then this properties is not taken in account. Same apply for
     * the grid range.
     *
     * @param sourceGG The source geometry (never {@code null}).
     * @param targetGG The target geometry. May be {@code null}, which is considered as equivalent.
     * @return {@code true} if the two geometries are equal, ignoring unspecified fields.
     */
    private static boolean equivalent(
            final GridGeometry2D sourceGG, final GridGeometry2D targetGG) {
        if (targetGG == null || targetGG.equals(sourceGG)) {
            return true;
        }
        if (targetGG.isDefined(GridGeometry2D.GRID_RANGE_BITMASK)
                && sourceGG.isDefined(GridGeometry2D.GRID_RANGE_BITMASK)) {
            if (!targetGG.getGridRange().equals(sourceGG.getGridRange())) {
                return false;
            }
        }
        if (targetGG.isDefined(GridGeometry2D.GRID_TO_CRS_BITMASK)
                && sourceGG.isDefined(GridGeometry2D.GRID_TO_CRS_BITMASK)) {
            // No needs to ask for a transform relative to a corner
            // since we will not apply a transformation here.
            if (!targetGG.getGridToCRS().equals(sourceGG.getGridToCRS())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Casts the specified grid range into an envelope. This is used before to transform the
     * envelope using {@link CRSUtilities#transform(MathTransform, Envelope)}.
     */
    private static Envelope toEnvelope(final GridEnvelope gridRange) {
        final int dimension = gridRange.getDimension();
        final double[] lower = new double[dimension];
        final double[] upper = new double[dimension];
        for (int i = 0; i < dimension; i++) {
            lower[i] = gridRange.getLow(i);
            upper[i] = gridRange.getHigh(i) + 1;
        }
        return new GeneralEnvelope(lower, upper);
    }

    /**
     * Creates a warp for the given transform. This method performs some empirical adjustment for
     * working around the {@link ArrayIndexOutOfBoundsException} which occurs sometime in {@code
     * MlibWarpPolynomialOpImage.computeTile(...)}.
     *
     * @param name The coverage name, for information purpose.
     * @param sourceBB Bounding box of source image, or {@code null}.
     * @param targetBB Bounding box of target image, or {@code null}.
     * @param allSteps2D Transform from target to source CRS.
     * @param mtFactory A math transform factory in case new transforms need to be created.
     * @return The warp.
     * @throws FactoryException if the warp can't be created.
     * @throws TransformException if the warp can't be created.
     */
    private static Warp createWarp(
            final CharSequence name,
            final Rectangle sourceBB,
            final Rectangle targetBB,
            final MathTransform2D allSteps2D,
            final MathTransformFactory mtFactory,
            Hints hints)
            throws FactoryException, TransformException {
        Double tolerance = (Double) hints.get(Hints.RESAMPLE_TOLERANCE);
        if (tolerance == null) {
            tolerance = (Double) Hints.getSystemDefault(Hints.RESAMPLE_TOLERANCE);
        }
        if (tolerance == null) {
            tolerance = 0.333;
        }
        WarpBuilder wb = new WarpBuilder(tolerance);

        MathTransform2D transform = allSteps2D;
        Rectangle actualBB = null;
        int step = 0;
        do {
            /*
             * Following block is usually not executed, unless we detected after the Warp object
             * creation that we need to perform some empirical adjustment. The difference between
             * the actual and expected bounding boxes should be only 1 pixel.
             */
            if (actualBB != null) {
                final double scaleX = 1 - ((double) sourceBB.width / (double) actualBB.width);
                final double scaleY = 1 - ((double) sourceBB.height / (double) actualBB.height);
                final double translateX = sourceBB.x - actualBB.x;
                final double translateY = sourceBB.y - actualBB.y;
                final double factor = (double) step / (double) EMPIRICAL_ADJUSTMENT_STEPS;
                final AffineTransform2D adjustment =
                        new AffineTransform2D(
                                1 - scaleX * factor,
                                0,
                                0,
                                1 - scaleY * factor,
                                translateX * factor,
                                translateY * factor);
                transform =
                        (MathTransform2D)
                                mtFactory.createConcatenatedTransform(allSteps2D, adjustment);
            }
            /*
             * Creates the warp object, trying to optimize to WarpAffine if possible. The transform
             * should have been computed in such a way that the target rectangle, when transformed,
             * matches exactly the source rectangle. Checks if the bounding boxes calculated by the
             * Warp object match the expected ones. In the usual case where they do, we are done.
             * Otherwise we assume that the difference is caused by rounding error and we will try
             * progressive empirical adjustment in order to get the rectangles to fit.
             */
            final Warp warp = wb.buildWarp(transform, targetBB);
            return warp;
        } while (step++ <= EMPIRICAL_ADJUSTMENT_STEPS);
    }

    /** Logs a message. */
    private static void log(final LogRecord record) {
        record.setSourceClassName("Resample");
        record.setSourceMethodName("doOperation");
        final Logger logger = CoverageProcessor.LOGGER;
        record.setLoggerName(logger.getName());
        logger.log(record);
    }
}
