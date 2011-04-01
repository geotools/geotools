/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.DataBuffer;
import java.awt.image.renderable.ParameterBlock;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import javax.media.jai.BorderExtender;
import javax.media.jai.BorderExtenderConstant;
import javax.media.jai.ImageLayout;
import javax.media.jai.Interpolation;
import javax.media.jai.InterpolationNearest;
import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.RenderedOp;
import javax.media.jai.Warp;
import javax.media.jai.WarpGrid;
import javax.media.jai.operator.MosaicDescriptor;

import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.grid.GeneralGridEnvelope;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.ViewType;
import org.geotools.coverage.processing.CannotReprojectException;
import org.geotools.coverage.processing.CoverageProcessor;
import org.geotools.factory.Hints;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.geotools.referencing.operation.AbstractCoordinateOperationFactory;
import org.geotools.referencing.operation.matrix.XAffineTransform;
import org.geotools.referencing.operation.transform.AffineTransform2D;
import org.geotools.referencing.operation.transform.DimensionFilter;
import org.geotools.referencing.operation.transform.IdentityTransform;
import org.geotools.referencing.operation.transform.WarpBuilder;
import org.geotools.resources.CRSUtilities;
import org.geotools.resources.XArray;
import org.geotools.resources.coverage.CoverageUtilities;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Errors;
import org.geotools.resources.i18n.LoggingKeys;
import org.geotools.resources.i18n.Loggings;
import org.geotools.resources.image.ImageUtilities;
import org.geotools.util.Utilities;
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
import org.opengis.referencing.operation.TransformException;


/**
 * Implementation of the {@link Resample} operation. This implementation is provided as a
 * separated class for two purpose: avoid loading this code before needed and provide some
 * way to check if a grid coverages is a result of a resample operation.
 *
 * @since 2.2
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 * @author Simone Giannecchini, GeoSolutions SAS
 * @author Daniele Romagnoli, GeoSolutions SAS
 */
final class Resampler2D extends GridCoverage2D {
    /**
     * For compatibility during cross-version serialization.
     */
    private static final long serialVersionUID = -8593569923766544474L;

    /**
     * The corner to use for performing calculation. By default {@link GridGeometry#getGridToCRS()}
     * maps to pixel center (as of OGC specification). In JAI, the transforms rather map to the
     * upper left corner.
     */
    private static final PixelOrientation CORNER = PixelOrientation.UPPER_LEFT; //UPPER_LEFT;

    /**
     * When an empirical adjustement of the Warp transform seems necessary, the amount of
     * subdivisions to try.
     */
    private static final int EMPIRICAL_ADJUSTMENT_STEPS = 16;

    /**
     * Small tolerance threshold for floating point number comparaisons.
     */
    private static final double EPS = 1E-6;

    /**
     * The logging level for defails about resampling operation applied.
     */
    private static final Level LOGGING_LEVEL = Level.FINE;
    
    /**
     * Constructs a new grid coverage for the specified grid geometry.
     *
     * @param source   The source for this grid coverage.
     * @param image    The image.
     * @param geometry The grid geometry (including the new CRS).
     * @param sampleDimensions The sample dimensions to be given to the new coverage.
     */
    private Resampler2D(final GridCoverage2D        source,
                        final PlanarImage           image,
                        final GridGeometry2D        geometry,
                        final GridSampleDimension[] sampleDimensions,
                        final Map<String, Serializable>                   properties,
                        final Hints                 hints)
    {
        super(source.getName(), image, geometry, sampleDimensions,
              new GridCoverage2D[] {source}, properties, hints);
    }

    /**
     * Constructs a new grid coverage for the specified grid geometry.
     *
     * @param source    The source for this grid coverage.
     * @param image     The image.
     * @param geometry  The grid geometry (including the new CRS).
     * @param operation The operation used to resample the coverage
     * @param wapr      The warp configuration in case a warp is being used
     * @param finalView The view for the target coverage.
     */
    private static GridCoverage2D create(final GridCoverage2D source,
                                         final PlanarImage    image,
                                         final GridGeometry2D geometry,
                                         final ViewType       finalView,
                                         final String         operation,
                                         final Warp           warp,
                                         final Hints          hints)
    {
        final GridSampleDimension[] sampleDimensions;
        switch (finalView) {
            case PHOTOGRAPHIC: {
                sampleDimensions = null;
                break;
            }
            default: {
                sampleDimensions = source.getSampleDimensions();
                break;
            }
        }
        /**
         * Build up transformation info properties: this can be used by the client to decide
         * whether it wants to actually do the resample (which might take hours with WarpAdapter on
         * very large grids) or not
         */
        Map<String, Serializable> properties = new HashMap<String, Serializable>();
        if(operation != null) {
            properties.put(Resample.OPERATION, operation);
            if(warp != null) {
                properties.put(Resample.WARP_TYPE, warp.getClass());
                if(warp instanceof WarpGrid) {
                    WarpGrid grid = (WarpGrid) warp;
                    Dimension dimension = new Dimension(grid.getXNumCells(), grid.getYNumCells());
                    properties.put(Resample.GRID_DIMENSIONS, dimension);
                }
            }
        }
        /*
         * The resampling may have been performed on the geophysics view.
         * Try to restore the original view.
         */
        GridCoverage2D coverage = new Resampler2D(source, image, geometry, sampleDimensions, properties, hints);
        coverage = coverage.view(finalView);
        return coverage;
    }
    
    /**
     * Creates a new coverage with a different coordinate reference reference system. If a
     * grid geometry is supplied, only its {@linkplain GridGeometry2D#getRange grid range}
     * and {@linkplain GridGeometry2D#getGridToCRS grid to CRS} transform are taken in account.
     *
     * @param sourceCoverage
     *          The source grid coverage.
     * @param targetCRS
     *          Coordinate reference system for the new grid coverage, or {@code null}.
     * @param targetGG
     *          The target grid geometry, or {@code null} for default.
     * @param interpolation
     *          The interpolation to use, or {@code null} if none.
     * @param hints
     *          The rendering hints. This is usually provided by {@link CoverageProcessor}.
     *          This method will looks for {@link Hints#COORDINATE_OPERATION_FACTORY} and
     *          {@link Hints#JAI_INSTANCE} keys.
     * @return
     *          The new grid coverage, or {@code sourceCoverage} if no resampling was needed.
     * @throws FactoryException
     *          if a transformation step can't be created.
     * @throws TransformException
     *          if a transformation failed.
     */
    public static GridCoverage2D reproject(GridCoverage2D            sourceCoverage,
                                           CoordinateReferenceSystem targetCRS,
                                           GridGeometry2D            targetGG,
                                           final Interpolation       interpolation,
                                           final Hints               hints)
            throws FactoryException, TransformException {
        return reproject(sourceCoverage, targetCRS, targetGG, interpolation, hints, null);
    }

    /**
     * Creates a new coverage with a different coordinate reference reference system. If a
     * grid geometry is supplied, only its {@linkplain GridGeometry2D#getRange grid range}
     * and {@linkplain GridGeometry2D#getGridToCRS grid to CRS} transform are taken in account.
     *
     * @param sourceCoverage
     *          The source grid coverage.
     * @param targetCRS
     *          Coordinate reference system for the new grid coverage, or {@code null}.
     * @param targetGG
     *          The target grid geometry, or {@code null} for default.
     * @param interpolation
     *          The interpolation to use, or {@code null} if none.
     * @param hints
     *          The rendering hints. This is usually provided by {@link CoverageProcessor}.
     *          This method will looks for {@link Hints#COORDINATE_OPERATION_FACTORY} and
     *          {@link Hints#JAI_INSTANCE} keys.
     * @param backgroundValues 
     *          The background values to be used by the underlying JAI operation, or 
     *          {@code null} if none. 
     * @return
     *          The new grid coverage, or {@code sourceCoverage} if no resampling was needed.
     * @throws FactoryException
     *          if a transformation step can't be created.
     * @throws TransformException
     *          if a transformation failed.
     */
    public static GridCoverage2D reproject(GridCoverage2D            sourceCoverage,
                                           CoordinateReferenceSystem targetCRS,
                                           GridGeometry2D            targetGG,
                                           Interpolation             interpolation,
                                           final Hints               hints,
                                           final double[]            backgroundValues)
            throws FactoryException, TransformException
    {
        ////////////////////////////////////////////////////////////////////////////////////////
        ////                                                                                ////
        //// =======>>  STEP 1: Extracts needed informations from the parameters   <<====== ////
        ////            STEP 2: Creates the "target to source" MathTransform                ////
        ////            STEP 3: Computes the target image layout                            ////
        ////            STEP 4: Applies the JAI operation ("Affine", "Warp", etc)           ////
        ////                                                                                ////
        ////////////////////////////////////////////////////////////////////////////////////////
        Utilities.ensureNonNull("sourceCoverage", sourceCoverage);
        // CRS
        final CoordinateReferenceSystem sourceCRS = sourceCoverage.getCoordinateReferenceSystem();
        if (targetCRS == null) {
            // in case the TargetCRS has not been set we try to use the CRS that was part of the TargetGG
            if(targetGG!=null&&targetGG.isDefined(GridGeometry2D.CRS_BITMASK)){
                targetCRS=targetGG.getCoordinateReferenceSystem();
            }else{
                // in case the TargetCRS is not set and either the TargetGG is not set
                // or it has not CRS inside, we reuse sourceCRS 
                targetCRS = sourceCRS;
            }
            // From this point, consider "targetCRS" as final.
        }else{
            // in case the targetCRS is set we should check that it is compatible
            // with the targetGG crs, otherwise we throw an exception
            if(targetGG!=null&&targetGG.isDefined(GridGeometry2D.CRS_BITMASK)){
                final CoordinateReferenceSystem targetGGCRS=targetGG.getCoordinateReferenceSystem();
                if(!CRS.equalsIgnoreMetadata(targetCRS, targetGGCRS)&&!CRS.findMathTransform(targetCRS, targetGGCRS).isIdentity()){
                    throw new IllegalArgumentException(Errors.format(ErrorKeys.ILLEGAL_ARGUMENT_$1,"TargetCRS must be compatible with TargetGG CRS"));
                }
                
            }
        }
        
        //
        // INTERPOLATION MANAGEMENT as well as BORDER_EXTENDER
        //
        if(interpolation==null){

            //if we did not the interpolation, let's try to get it from hints
            if(hints!=null){
                // JAI interpolation 
                if(hints.containsKey(JAI.KEY_INTERPOLATION))
                        interpolation=(Interpolation) hints.get(JAI.KEY_INTERPOLATION);
            }
        }else{
            // we have been provided with interpolation, let's override hints
            hints.put(JAI.KEY_INTERPOLATION,interpolation);
        }
        if (!hints.containsKey(JAI.KEY_BORDER_EXTENDER)) {
            hints.put(JAI.KEY_BORDER_EXTENDER, BorderExtender.createInstance(BorderExtender.BORDER_COPY));
        }
        
        /*
         * The following will tell us if the target GridRange (GR) and GridGeometry (GG) should
         * be computed automatically, or if we should follow strictly what the user said. Note
         * that "automaticGG" implies "automaticGR" but the converse is not necessarily true.
         */
        final boolean automaticGG, automaticGR;
        
        /*
         * Grid range and "grid to CRS" transform are the only grid geometry informations used
         * by this method. If they are not available, this is equivalent to not providing grid
         * geometry at all. In such case set to 'targetGG' reference to null, since null value
         * is what the remaining code will check for.
         */
        if (targetGG == null) {
            automaticGG = true;
            automaticGR = true;
        } else {
            automaticGR = !targetGG.isDefined(GridGeometry2D.GRID_RANGE_BITMASK);
            if (!automaticGR || targetGG.isDefined(GridGeometry2D.GRID_TO_CRS_BITMASK)) {
                automaticGG = false;
            } else {
                /*
                 * Before to abandon the grid geometry, checks if it contains an envelope (note:
                 * we really want it in sourceCRS, not targetCRS - the reprojection will be done
                 * later in this method). If so, we will recreate a new grid geometry from the
                 * envelope using the same "grid to CRS" transform than the original coverage.
                 * The result may be an image with a different size.
                 */
                if (targetGG.isDefined(GridGeometry2D.ENVELOPE_BITMASK)) {
                    final Envelope       envelope = targetGG.getEnvelope();
                    final GridGeometry2D sourceGG = sourceCoverage.getGridGeometry();
                    final MathTransform  gridToCRS;
                    switch (envelope.getDimension()) {
                        case 2:  gridToCRS = sourceGG.getGridToCRS2D(CORNER); break;
                        default: gridToCRS = sourceGG.getGridToCRS(CORNER);   break;
                    }
                    targetGG = new GridGeometry2D(PixelInCell.CELL_CENTER, gridToCRS, envelope, null);
                    automaticGG = false;
                } else {
                    targetGG = null;
                    automaticGG = true;
                }
            }
        }
        /*
         * If the source coverage is already the result of a previous "Resample" operation,
         * go up in the chain and check if a previously computed image could fits (i.e. the
         * requested resampling may be the inverse of a previous resampling). This method
         * may stop immediately if a suitable image is found.
         */
        GridCoverage2D targetCoverage = existingCoverage(sourceCoverage, targetCRS, targetGG);
        if (targetCoverage != null) {
            return targetCoverage;
        }
        final GridGeometry2D sourceGG = sourceCoverage.getGridGeometry();
        final CoordinateReferenceSystem compatibleSourceCRS = compatibleSourceCRS(
                sourceCoverage.getCoordinateReferenceSystem2D(), sourceCRS, targetCRS);
        /*
         * The projection are usually applied on floating-point values, in order
         * to gets maximal precision and to handle correctly the special case of
         * NaN values. However, we can apply the projection on integer values if
         * the interpolation type is "nearest neighbor", since this is not really
         * an interpolation.
         *
         * If this condition is meets, then we verify if an "integer version" of the image
         * is available as a source of the source coverage (i.e. the floating-point image
         * is derived from the integer image, not the converse).
         */
        final ViewType processingView = CoverageUtilities.preferredViewForOperation(
                                        sourceCoverage, interpolation, false, hints);
        final ViewType finalView = CoverageUtilities.preferredViewAfterOperation(sourceCoverage);
        sourceCoverage = sourceCoverage.view(processingView);
        PlanarImage sourceImage = PlanarImage.wrapRenderedImage(sourceCoverage.getRenderedImage());
        assert sourceCoverage.getCoordinateReferenceSystem() == sourceCRS : sourceCoverage;
        // From this point, consider 'sourceCoverage' as final.

        ////////////////////////////////////////////////////////////////////////////////////////
        ////                                                                                ////
        ////            STEP 1: Extracts needed informations from the parameters            ////
        //// =======>>  STEP 2: Creates the "target to source" MathTransform       <<====== ////
        ////            STEP 3: Computes the target image layout                            ////
        ////            STEP 4: Applies the JAI operation ("Affine", "Warp", etc)           ////
        ////                                                                                ////
        ////////////////////////////////////////////////////////////////////////////////////////

        final CoordinateOperationFactory factory =
                ReferencingFactoryFinder.getCoordinateOperationFactory(hints);
        final MathTransformFactory mtFactory;
        if (factory instanceof AbstractCoordinateOperationFactory) {
            mtFactory = ((AbstractCoordinateOperationFactory) factory).getMathTransformFactory();
        } else {
            mtFactory = ReferencingFactoryFinder.getMathTransformFactory(hints);
        }
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
        final MathTransform step1, step2, step3, allSteps, allSteps2D;
        if (CRS.equalsIgnoreMetadata(sourceCRS, targetCRS)) {
            /*
             * Note: targetGG should not be null, otherwise 'existingCoverage(...)' should
             *       have already detected that this resample is not doing anything.
             */
            if (!targetGG.isDefined(GridGeometry2D.GRID_TO_CRS_BITMASK)) {
                step1    = sourceGG.getGridToCRS(CORNER); // Really sourceGG, not targetGG
                step2    = IdentityTransform.create(step1.getTargetDimensions());
                step3    = step1.inverse();
                allSteps = IdentityTransform.create(step1.getSourceDimensions());
                targetGG = new GridGeometry2D(targetGG.getGridRange(), step1, targetCRS);
            } else {
                step1    = targetGG.getGridToCRS(CORNER);
                step2    = IdentityTransform.create(step1.getTargetDimensions());
                step3    = sourceGG.getGridToCRS(CORNER).inverse();
                allSteps = mtFactory.createConcatenatedTransform(step1, step3);
                if (!targetGG.isDefined(GridGeometry2D.GRID_RANGE_BITMASK)) {
                    /*
                     * If the target grid range was not explicitly specified, a grid range will be
                     * automatically computed in such a way that it will maps to the same envelope
                     * (at least approximatively).
                     */
                    Envelope gridRange;
                    gridRange = toEnvelope(sourceGG.getGridRange());
                    gridRange = CRS.transform(allSteps.inverse(), gridRange);
                    targetGG  = new GridGeometry2D(new GeneralGridEnvelope(gridRange,PixelInCell.CELL_CORNER), targetGG.getGridToCRS(PixelInCell.CELL_CENTER), targetCRS);
                }
            }
        } else {
            if (sourceCRS == null) {
                throw new CannotReprojectException(Errors.format(ErrorKeys.UNSPECIFIED_CRS));
            }
            final Envelope        sourceEnvelope;
            final GeneralEnvelope targetEnvelope;
            final CoordinateOperation operation = factory.createOperation(sourceCRS, targetCRS);
            final boolean force2D = (sourceCRS != compatibleSourceCRS);
            step2          = factory.createOperation(targetCRS, compatibleSourceCRS).getMathTransform();
            step3          = (force2D ? sourceGG.getGridToCRS2D(CORNER) : sourceGG.getGridToCRS(CORNER)).inverse();
            sourceEnvelope = sourceCoverage.getEnvelope(); // Don't force this one to 2D.
            targetEnvelope = CRS.transform(operation, sourceEnvelope);
            targetEnvelope.setCoordinateReferenceSystem(targetCRS);
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
            if (targetGG == null) {
                final GridEnvelope targetGR;
                targetGR = force2D ? new GridEnvelope2D(sourceGG.getGridRange2D()) : sourceGG.getGridRange();
                targetGG = new GridGeometry2D(targetGR, targetEnvelope);
                step1    = targetGG.getGridToCRS(CORNER);
            } else if (!targetGG.isDefined(GridGeometry2D.GRID_TO_CRS_BITMASK)) {
                targetGG = new GridGeometry2D(targetGG.getGridRange(), targetEnvelope);
                step1    = targetGG.getGridToCRS(CORNER);
            } else {
                step1 = targetGG.getGridToCRS(CORNER);
                if (!targetGG.isDefined(GridGeometry2D.GRID_RANGE_BITMASK)) {
                    GeneralEnvelope gridRange = CRS.transform(step1.inverse(), targetEnvelope);
                    // According OpenGIS specification, GridGeometry maps pixel's center.
                    targetGG = new GridGeometry2D(new GeneralGridEnvelope(gridRange, PixelInCell.CELL_CENTER), step1, targetCRS);
                }
            }
            /*
             * Computes the final transform.
             */
            if (step1.equals(step3.inverse())) {
                allSteps = step2;
            } else {
                allSteps = mtFactory.createConcatenatedTransform(
                           mtFactory.createConcatenatedTransform(step1, step2), step3);
            }
        }
        allSteps2D = toMathTransform2D(allSteps, mtFactory, targetGG);
        if (!(allSteps2D instanceof MathTransform2D)) {
            // Should not happen with Geotools implementations. May happen
            // with some external implementations, but should stay unusual.
            throw new TransformException(Errors.format(ErrorKeys.NO_TRANSFORM2D_AVAILABLE));
        }

        ////////////////////////////////////////////////////////////////////////////////////////
        ////                                                                                ////
        ////            STEP 1: Extracts needed informations from the parameters            ////
        ////            STEP 2: Creates the "target to source" MathTransform                ////
        //// =======>>  STEP 3: Computes the target image layout                   <<====== ////
        ////            STEP 4: Applies the JAI operation ("Affine", "Warp", etc)           ////
        ////                                                                                ////
        ////////////////////////////////////////////////////////////////////////////////////////

        final RenderingHints targetHints = processingView.getRenderingHints(sourceImage);
        if (hints != null) {
            targetHints.add(hints);
        }
        ImageLayout layout = (ImageLayout) targetHints.get(JAI.KEY_IMAGE_LAYOUT);
        if (layout != null) {
            layout = (ImageLayout) layout.clone();
        } else {
            layout = new ImageLayout();
            // Do not inherit the color model and sample model from the 'sourceImage';
            // Let the operation decide itself. This is necessary in case we change the
            // source, as we do if we choose the "Mosaic" operation.
        }
        final Rectangle sourceBB = sourceGG.getGridRange2D();
        final Rectangle targetBB = targetGG.getGridRange2D();
        if (isBoundsUndefined(layout, false)) {
            layout.setMinX  (targetBB.x);
            layout.setMinY  (targetBB.y);
            layout.setWidth (targetBB.width);
            layout.setHeight(targetBB.height);
        }
        if (isBoundsUndefined(layout, true)) {
            Dimension size = new Dimension(layout.getWidth (sourceImage),
                                           layout.getHeight(sourceImage));
            size = ImageUtilities.toTileSize(size);
            layout.setTileGridXOffset(layout.getMinX(sourceImage));
            layout.setTileGridYOffset(layout.getMinY(sourceImage));
            layout.setTileWidth (size.width);
            layout.setTileHeight(size.height);
        }
        /*
         * Creates the background values array. 
         */
        final double[] background = backgroundValues != null ? backgroundValues : CoverageUtilities.getBackgroundValues(sourceCoverage);

        
        /*
         * We need to correctly manage the Hints to control the replacement of IndexColorModel.
         * It is worth to point out that setting the JAI.KEY_REPLACE_INDEX_COLOR_MODEL hint to
         * Boolean.TRUE is not enough to force the operators to do an expansion. If we explicitly
         * provide an ImageLayout built with the source image where the CM and the SM are valid.
         * those will be employed overriding a the possibility to expand the color model.
         */
        if (ViewType.PHOTOGRAPHIC.equals(processingView)) {
            layout.unsetValid(ImageLayout.COLOR_MODEL_MASK | ImageLayout.SAMPLE_MODEL_MASK);
        }
        targetHints.put(JAI.KEY_IMAGE_LAYOUT, layout);

        ////////////////////////////////////////////////////////////////////////////////////////
        ////                                                                                ////
        ////            STEP 1: Extracts needed informations from the parameters            ////
        ////            STEP 2: Creates the "target to source" MathTransform                ////
        ////            STEP 3: Computes the target image layout                            ////
        //// =======>>  STEP 4: Applies the JAI operation ("Affine", "Warp", etc)  <<====== ////
        ////                                                                                ////
        ////////////////////////////////////////////////////////////////////////////////////////
        /*
         * If the user requests a new grid geometry with the same coordinate reference system,
         * and if the grid geometry is equivalents to a simple extraction of a sub-area, then
         * delegates the work to a "Crop" operation.
         */
        final String operation;
        final ParameterBlock paramBlk = new ParameterBlock().addSource(sourceImage);
        Warp warp = null;
        if (allSteps.isIdentity() || (allSteps instanceof AffineTransform &&
                XAffineTransform.isIdentity((AffineTransform) allSteps, EPS)))
        {
            /*
             * Since there is no interpolation to perform, use the native view (which may be
             * packed or geophysics - it is just the view which is closest to original data).
             */
            sourceCoverage = sourceCoverage.view(ViewType.NATIVE);
            sourceImage = PlanarImage.wrapRenderedImage(sourceCoverage.getRenderedImage());
            paramBlk.removeSources();
            paramBlk.addSource(sourceImage);
            if (targetBB.equals(sourceBB)) {
                /*
                 * Optimization in case we have nothing to do, not even a crop. Reverts to the
                 * original coverage BEFORE to creates Resampler2D. Note that while there is
                 * nothing to do, the target CRS is not identical to the source CRS (so we need
                 * to create a new coverage) otherwise this condition would have been detected
                 * sooner in this method.
                 */
                sourceCoverage = sourceCoverage.view(finalView);
                sourceImage = PlanarImage.wrapRenderedImage(sourceCoverage.getRenderedImage());
                return create(sourceCoverage, sourceImage, targetGG, ViewType.SAME, null, null, hints);
            }
            if (sourceBB.contains(targetBB)) {
                operation = "Crop";
                paramBlk.add(Float.valueOf(targetBB.x))
                        .add(Float.valueOf(targetBB.y))
                        .add(Float.valueOf(targetBB.width))
                        .add(Float.valueOf(targetBB.height));
            } else {
                operation = "Mosaic";
                paramBlk.add(MosaicDescriptor.MOSAIC_TYPE_OVERLAY)
                        .add(null).add(null).add(null).add(background);
            }
        } else {
            /*
             * Special case for the affine transform. Try to use the JAI "Affine" operation
             * instead of the more general "Warp" one. JAI provides native acceleration for
             * the affine operation.
             *
             * NOTE 1: There is no need to check for "Scale" and "Translate" as special cases
             *         of "Affine" since JAI already does this check for us.
             *
             * NOTE 2: "Affine", "Scale", "Translate", "Rotate" and similar operations ignore
             *         the 'xmin', 'ymin', 'width' and 'height' image layout. Consequently, we
             *         can't use this operation if the user provided explicitly a grid range.
             *
             * NOTE 3: If the user didn't specified any grid geometry, then a yet cheaper approach
             *         is to just update the 'gridToCRS' value. We returns a grid coverage wrapping
             *         the SOURCE image with the updated grid geometry.
             */
            if ((automaticGR || targetBB.equals(sourceBB)) && allSteps instanceof AffineTransform) {
                if (automaticGG) {
                    // Cheapest approach: just update 'gridToCRS'.
                    MathTransform mtr;
                    mtr = sourceGG.getGridToCRS(CORNER);
                    mtr = mtFactory.createConcatenatedTransform(mtr,  step2.inverse());
                    targetGG = new GridGeometry2D(sourceGG.getGridRange(), mtr, targetCRS);
                    /*
                     * Note: do NOT use the "GridGeometry2D(sourceGridRange, targetEnvelope)"
                     * constructor in the above line. We must give a MathTransform argument to
                     * the constructor, not an Envelope, because the later infer a MathTransform
                     * using heuristic rules. Only the constructor with a MathTransform argument
                     * is fully accurate.
                     */
                    return create(sourceCoverage, sourceImage, targetGG, finalView, null, null, hints);
                }
                // More general approach: apply the affine transform.
                operation = "Affine";
                final AffineTransform affine = (AffineTransform) allSteps.inverse();
                paramBlk.add(affine).add(interpolation).add(background);
            } else {
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
                switch (sourceImage.getSampleModel().getTransferType()) {
                    case DataBuffer.TYPE_DOUBLE:
                    case DataBuffer.TYPE_FLOAT: {
                        Envelope source = CRS.transform(sourceGG.getEnvelope(), targetCRS);
                        Envelope target = CRS.transform(targetGG.getEnvelope(), targetCRS);
                        source = targetGG.reduce(source);
                        target = targetGG.reduce(target);
                        if (!(new GeneralEnvelope(source).contains(target, true))) {
                            if (interpolation != null && !(interpolation instanceof InterpolationNearest)) {
                                return reproject(sourceCoverage, targetCRS, targetGG, null, hints, background);
                            } else {
                                // If we were already using nearest-neighbor interpolation, force
                                // usage of WarpAdapter2D instead of WarpAffine. The price will be
                                // a slower reprojection.
                                forceAdapter = true;
                            }
                        }
                    }
                }
                // -------- End of JAI bug workaround --------
                final MathTransform2D transform = (MathTransform2D) allSteps2D;
                final CharSequence name = sourceCoverage.getName();
                operation = "Warp";
                if (forceAdapter) {
                    warp = new WarpBuilder(0.0).buildWarp(transform, sourceBB);
                } else {
                    warp = createWarp(name, sourceBB, targetBB, transform, mtFactory, hints);
                }
                paramBlk.add(warp).add(interpolation).add(background);
            }
        }
        final RenderedOp targetImage = getJAI(hints).createNS(operation, paramBlk, targetHints);
        final Locale locale = sourceCoverage.getLocale();  // For logging purpose.
        /*
         * The JAI operation sometime returns an image with a bounding box different than what we
         * expected. This is true especially for the "Affine" operation: the JAI documentation said
         * explicitly that xmin, ymin, width and height image layout hints are ignored for this one.
         * As a safety, we check the bounding box in any case. If it doesn't matches, then we will
         * reconstruct the target grid geometry.
         */
        final GridEnvelope targetGR = targetGG.getGridRange();
        final int[] lower = targetGR.getLow().getCoordinateValues();
        final int[] upper = targetGR.getHigh().getCoordinateValues();
        for (int i=0; i<upper.length; i++) {
            upper[i]++; // Make them exclusive.
        }
        lower[targetGG.gridDimensionX] = targetImage.getMinX();
        lower[targetGG.gridDimensionY] = targetImage.getMinY();
        upper[targetGG.gridDimensionX] = targetImage.getMaxX();
        upper[targetGG.gridDimensionY] = targetImage.getMaxY();
        final GridEnvelope actualGR = new GeneralGridEnvelope(lower, upper);
        if (!targetGR.equals(actualGR)) {
        	targetGG = new GridGeometry2D(actualGR, targetGG.getGridToCRS(PixelInCell.CELL_CENTER),targetCRS);
            if (!automaticGR) {
                log(Loggings.getResources(locale).getLogRecord(Level.FINE,
                    LoggingKeys.ADJUSTED_GRID_GEOMETRY_$1, sourceCoverage.getName().toString(locale)));
            }
        }
        /*
         * Constructs the final grid coverage, then log a message as in the following example:
         *
         *     Resampled coverage "Foo" from coordinate system "myCS" (for an image of size
         *     1000x1500) to coordinate system "WGS84" (image size 1000x1500). JAI operation
         *     is "Warp" with "Nearest" interpolation on geophysics pixels values. Background
         *     value is 255.
         */
        targetCoverage = create(sourceCoverage, targetImage, targetGG, finalView, operation, warp, hints);
        assert CRS.equalsIgnoreMetadata(targetCoverage.getCoordinateReferenceSystem(), targetCRS) : targetGG;
        assert targetCoverage.getGridGeometry().getGridRange2D().equals(targetImage.getBounds())  : targetGG;
        if (CoverageProcessor.LOGGER.isLoggable(LOGGING_LEVEL)) {
            log(Loggings.getResources(locale).getLogRecord(LOGGING_LEVEL,
                LoggingKeys.APPLIED_RESAMPLE_$11, new Object[] {
                /*  {0} */ sourceCoverage.getName().toString(locale),
                /*  {1} */ sourceCoverage.getCoordinateReferenceSystem().getName().getCode(),
                /*  {2} */ sourceImage.getWidth(),
                /*  {3} */ sourceImage.getHeight(),
                /*  {4} */ targetCoverage.getCoordinateReferenceSystem().getName().getCode(),
                /*  {5} */ targetImage.getWidth(),
                /*  {6} */ targetImage.getHeight(),
                /*  {7} */ targetImage.getOperationName(),
                /*  {8} */ Integer.valueOf(sourceCoverage == sourceCoverage.view(ViewType.GEOPHYSICS) ? 1 : 0),
                /*  {9} */ ImageUtilities.getInterpolationName(interpolation),
                /* {10} */ (background != null) ? background.length == 1 ? (Double.isNaN(background[0]) ? (Object) "NaN"
                                 : (Object) Double.valueOf(background[0]))
                                 : (Object) XArray.toString(background, locale)
                                 : "No background used" }));
        }
        
        return targetCoverage;
    }

    /*
     * If the source coverage is already the result of a previous "Resample" operation,
     * go up in the chain and check if a previously computed image could fits (i.e. the
     * requested resampling may be the inverse of a previous resampling).
     */
    private static GridCoverage2D existingCoverage(GridCoverage2D coverage,
              CoordinateReferenceSystem targetCRS, GridGeometry2D targetGG)
    {
        while (!equivalent(coverage.getGridGeometry(), targetGG) ||
              (!CRS.equalsIgnoreMetadata(targetCRS, coverage.getCoordinateReferenceSystem()) &&
               !CRS.equalsIgnoreMetadata(targetCRS, coverage.getCoordinateReferenceSystem2D())))
        {
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
     * Gets the JAI instance to use from the rendering hints.
     */
    private static JAI getJAI(final Hints hints) {
        if (hints != null) {
            final Object property = hints.get(Hints.JAI_INSTANCE);
            if (property instanceof JAI) {
                return (JAI) property;
            }
        }
        return JAI.getDefaultInstance();
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
            mask = ImageLayout.TILE_GRID_X_OFFSET_MASK | ImageLayout.TILE_WIDTH_MASK |
                   ImageLayout.TILE_GRID_Y_OFFSET_MASK | ImageLayout.TILE_HEIGHT_MASK;
        } else {
            mask = ImageLayout.MIN_X_MASK | ImageLayout.WIDTH_MASK |
                   ImageLayout.MIN_Y_MASK | ImageLayout.HEIGHT_MASK;
        }
        return (layout.getValidMask() & mask) == 0;
    }

    /**
     * Returns a source CRS compatible with the given target CRS. This method try to returns
     * a CRS which would not thrown an {@link NoninvertibleTransformException} if attempting
     * to transform from "target" to "source" (reminder: Warp works on <strong>inverse</strong>
     * transforms).
     *
     * @param sourceCRS2D
     *          The two-dimensional source CRS. Actually, this method accepts arbitrary dimension
     *          provided that are not greater than {@code sourceCRS}, but in theory it is 2D.
     * @param sourceCRS
     *          The n-dimensional source CRS.
     * @param targetCRS
     *          The n-dimensional target CRS.
     */
    private static CoordinateReferenceSystem compatibleSourceCRS(
             final CoordinateReferenceSystem sourceCRS2D,
             final CoordinateReferenceSystem sourceCRS,
             final CoordinateReferenceSystem targetCRS)
    {
        final int dim2D = sourceCRS2D.getCoordinateSystem().getDimension();
        return (targetCRS.getCoordinateSystem().getDimension() == dim2D &&
                sourceCRS.getCoordinateSystem().getDimension()  > dim2D) ? sourceCRS2D : sourceCRS;
    }

    /**
     * Returns the math transform for the two specified dimensions of the specified transform.
     *
     * @param  transform The transform.
     * @param  mtFactory The factory to use for extracting the sub-transform.
     * @param  sourceGG  The grid geometry which is the source of the <strong>transform</strong>.
     *                   This is {@code targetGG} in the {@link #reproject} method, because the
     *                   later computes a transform from target to source grid geometry.
     * @return The {@link MathTransform2D} part of {@code transform}.
     * @throws FactoryException If {@code transform} is not separable.
     */
    private static MathTransform2D toMathTransform2D(final MathTransform        transform,
                                                     final MathTransformFactory mtFactory,
                                                     final GridGeometry2D       sourceGG)
            throws FactoryException
    {
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
     * Checks if two geometries are equal, ignoring unspecified fields. If one or both
     * geometries has no "gridToCRS" transform, then this properties is not taken in account.
     * Same apply for the grid range.
     *
     * @param  sourceGG The source geometry (never {@code null}).
     * @param  targetGG The target geometry. May be {@code null}, which is considered as equivalent.
     * @return {@code true} if the two geometries are equal, ignoring unspecified fields.
     */
    private static boolean equivalent(final GridGeometry2D sourceGG, final GridGeometry2D targetGG) {
        if (targetGG == null || targetGG.equals(sourceGG)) {
            return true;
        }
        if (targetGG.isDefined(GridGeometry2D.GRID_RANGE_BITMASK) &&
            sourceGG.isDefined(GridGeometry2D.GRID_RANGE_BITMASK))
        {
            if (!targetGG.getGridRange().equals(sourceGG.getGridRange())) {
                return false;
            }
        }
        if (targetGG.isDefined(GridGeometry2D.GRID_TO_CRS_BITMASK) &&
            sourceGG.isDefined(GridGeometry2D.GRID_TO_CRS_BITMASK))
        {
            // No needs to ask for a transform relative to a corner
            // since we will not apply a transformation here.
            if (!targetGG.getGridToCRS().equals(sourceGG.getGridToCRS())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Casts the specified grid range into an envelope. This is used before to transform
     * the envelope using {@link CRSUtilities#transform(MathTransform, Envelope)}.
     */
    private static Envelope toEnvelope(final GridEnvelope gridRange) {
        final int dimension = gridRange.getDimension();
        final double[] lower = new double[dimension];
        final double[] upper = new double[dimension];
        for (int i=0; i<dimension; i++) {
            lower[i] = gridRange.getLow(i);
            upper[i] = gridRange.getHigh(i) + 1;
        }
        return new GeneralEnvelope(lower, upper);
    }
    
    /**
     * Creates a warp for the given transform. This method performs some empirical adjustment
     * for working around the {@link ArrayIndexOutOfBoundsException} which occurs sometime in
     * {@code MlibWarpPolynomialOpImage.computeTile(...)}.
     *
     * @param  name       The coverage name, for information purpose.
     * @param  sourceBB   Bounding box of source image, or {@code null}.
     * @param  targetBB   Bounding box of target image, or {@code null}.
     * @param  allSteps2D Transform from target to source CRS.
     * @param  mtFactory  A math transform factory in case new transforms need to be created.
     * @return The warp.
     * @throws FactoryException if the warp can't be created.
     * @throws TransformException if the warp can't be created.
     */
    private static Warp createWarp(final CharSequence name, final Rectangle sourceBB, final Rectangle targetBB,
                                   final MathTransform2D allSteps2D, final MathTransformFactory mtFactory, Hints hints)
            throws FactoryException, TransformException
    {
        Double tolerance = (Double) hints.get(Hints.RESAMPLE_TOLERANCE);
        if(tolerance == null) {
            tolerance = (Double) Hints.getSystemDefault(Hints.RESAMPLE_TOLERANCE);
        } 
        if(tolerance == null) {
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
                final double scaleX     = 1 - ((double) sourceBB.width  / (double) actualBB.width);
                final double scaleY     = 1 - ((double) sourceBB.height / (double) actualBB.height);
                final double translateX = sourceBB.x - actualBB.x;
                final double translateY = sourceBB.y - actualBB.y;
                final double factor = (double) step / (double) EMPIRICAL_ADJUSTMENT_STEPS;
                final AffineTransform2D adjustment = new AffineTransform2D(
                        1 - scaleX*factor, 0, 0, 1 - scaleY*factor, translateX*factor, translateY*factor);
                transform = (MathTransform2D) mtFactory.createConcatenatedTransform(allSteps2D, adjustment);
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
            if(true) {
                return warp;
            }
            
//            // remainder is disabled for now since it break Geoserver build.
//            if (sourceBB == null || targetBB == null) {
//                return warp;
//            }
//            actualBB = warp.mapSourceRect(sourceBB); // May be null
//            if (actualBB != null && targetBB.contains(sourceBB)) {
//                return warp;
//            }
//            actualBB = warp.mapDestRect(targetBB); // Should never be null.
//            if (actualBB != null && targetBB.contains(sourceBB)) {
//                return warp;
//            }
//            // The loop below intentionally tries one more iteration than the constant in case we need
//            // to apply slightly more than the above scale and translation because of rounding errors.
        } while (step++ <= EMPIRICAL_ADJUSTMENT_STEPS);
        throw new FactoryException(Errors.format(ErrorKeys.CANT_REPROJECT_$1, name));
    }

    /**
     * Logs a message.
     */
    private static void log(final LogRecord record) {
        record.setSourceClassName("Resample");
        record.setSourceMethodName("doOperation");
        final Logger logger = CoverageProcessor.LOGGER;
        record.setLoggerName(logger.getName());
        logger.log(record);
    }
}
