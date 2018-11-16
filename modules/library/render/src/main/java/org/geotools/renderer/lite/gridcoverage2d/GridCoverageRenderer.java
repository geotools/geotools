/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.lite.gridcoverage2d;

import it.geosolutions.jaiext.range.Range;
import it.geosolutions.jaiext.utilities.ImageLayout2;
import it.geosolutions.jaiext.vectorbin.ROIGeometry;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ImagingOpException;
import java.awt.image.IndexColorModel;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.media.jai.BorderExtender;
import javax.media.jai.ImageLayout;
import javax.media.jai.Interpolation;
import javax.media.jai.InterpolationNearest;
import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.ROI;
import javax.media.jai.operator.ConstantDescriptor;
import org.geotools.coverage.CoverageFactoryFinder;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.TypeMap;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.coverage.util.CoverageUtilities;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.image.ImageWorker;
import org.geotools.image.util.ColorUtilities;
import org.geotools.image.util.ImageUtilities;
import org.geotools.metadata.i18n.ErrorKeys;
import org.geotools.metadata.i18n.Errors;
import org.geotools.parameter.Parameter;
import org.geotools.referencing.CRS;
import org.geotools.referencing.operation.builder.GridToEnvelopeMapper;
import org.geotools.referencing.operation.matrix.XAffineTransform;
import org.geotools.referencing.operation.transform.AffineTransform2D;
import org.geotools.renderer.composite.BlendComposite;
import org.geotools.renderer.composite.BlendComposite.BlendingMode;
import org.geotools.renderer.crs.ProjectionHandler;
import org.geotools.renderer.crs.ProjectionHandlerFinder;
import org.geotools.renderer.crs.WrappingProjectionHandler;
import org.geotools.styling.ChannelSelection;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.SelectedChannelType;
import org.geotools.styling.SelectedChannelTypeImpl;
import org.geotools.util.factory.Hints;
import org.geotools.util.factory.Hints.Key;
import org.locationtech.jts.algorithm.match.HausdorffSimilarityMeasure;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.util.AffineTransformation;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.coverage.grid.GridCoverageReader;
import org.opengis.geometry.BoundingBox;
import org.opengis.metadata.spatial.PixelOrientation;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.datum.PixelInCell;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.MathTransform2D;
import org.opengis.referencing.operation.TransformException;

/**
 * A helper class for rendering {@link GridCoverage} objects.
 *
 * @author Simone Giannecchini, GeoSolutions SAS
 * @author Andrea Aime, GeoSolutions SAS
 * @author Alessio Fabiani, GeoSolutions SAS
 * @version $Id$
 */
@SuppressWarnings("deprecation")
public final class GridCoverageRenderer {

    private static final double EPS = 1e-6;

    /** Logger. */
    private static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(GridCoverageRenderer.class);

    /** IDENTITY */
    private static final AffineTransform IDENTITY = AffineTransform2D.getTranslateInstance(0, 0);

    /**
     * This variable is use for testing purposes in order to force this {@link GridCoverageRenderer}
     * to dump images at various steps on the disk.
     */
    private static boolean DEBUG =
            Boolean.getBoolean("org.geotools.renderer.lite.gridcoverage2d.debug");

    private static String DUMP_DIRECTORY;

    static {
        if (DEBUG) {
            final File tempDir = new File(System.getProperty("user.home"), "gt-renderer");
            if (!tempDir.exists()) {
                if (!tempDir.mkdir())
                    System.out.println("Unable to create debug dir, exiting application!!!");
                DEBUG = false;
                DUMP_DIRECTORY = null;
            } else {
                DUMP_DIRECTORY = tempDir.getAbsolutePath();
                System.out.println("Rendering debug dir " + DUMP_DIRECTORY);
            }
        }
    }

    /** The Display (User defined) CRS * */
    private final CoordinateReferenceSystem destinationCRS;

    /** Area we want to draw. */
    private final GeneralEnvelope destinationEnvelope;

    /** Size of the area we want to draw in pixels. */
    private final Rectangle destinationSize;

    private final AffineTransform finalGridToWorld;

    private final AffineTransform finalWorldToGrid;

    private Hints hints = new Hints();

    private Interpolation interpolation = new InterpolationNearest();

    /** {@link GridCoverageFactory} to be used within this {@link GridCoverageRenderer} instance. */
    private final GridCoverageFactory gridCoverageFactory;

    private boolean wrapEnabled = true;

    private boolean advancedProjectionHandlingEnabled = true;

    public static final String PARENT_COVERAGE_PROPERTY = "ParentCoverage";

    /** Hint's KEY specifying a custom padding */
    public static final Key PADDING = new Key(Integer.class);

    public static final String KEY_COMPOSITING = "Compositing";

    /**
     * Enables/disable map wrapping (active only when rendering off a {@link GridCoverage2DReader}
     * and when advanced projection handling has been enabled too)
     */
    public void setWrapEnabled(boolean wrapEnabled) {
        this.wrapEnabled = wrapEnabled;
    }

    /**
     * Returns true if map wrapping is enabled (active only when rendering off a {@link
     * GridCoverage2DReader} and when advanced projection handling has been enabled too)
     */
    public boolean isWrapEnabled() {
        return this.wrapEnabled;
    }

    /**
     * Enables/disables advanced projection handling (read all areas needed to make up the requested
     * map, cut them to areas where reprojection makes sense, and so on). Works only when rendering
     * off a {@link GridCoverage2DReader}.
     */
    public void setAdvancedProjectionHandlingEnabled(boolean enabled) {
        this.advancedProjectionHandlingEnabled = enabled;
    }

    /**
     * Tests if advanced projection handling is enabled (read all areas needed to make up the
     * requested map, cut them to areas where reprojection makes sense, and so on). Works only when
     * rendering off a {@link GridCoverage2DReader}.
     */
    public boolean isAdvancedProjectionHandlingEnabled() {
        return this.advancedProjectionHandlingEnabled;
    }

    /**
     * Creates a new {@link GridCoverageRenderer} object.
     *
     * @param destinationCRS the CRS of the {@link GridCoverage2D} to render.
     * @param envelope delineating the area to be rendered.
     * @param screenSize at which we want to render the source {@link GridCoverage2D}.
     * @param worldToScreen if not <code>null</code> and if it contains a rotation, this Affine
     *     Transform is used directly to convert from world coordinates to screen coordinates.
     *     Otherwise, a standard {@link GridToEnvelopeMapper} is used to calculate the affine
     *     transform.
     * @throws TransformException
     * @throws NoninvertibleTransformException
     */
    public GridCoverageRenderer(
            final CoordinateReferenceSystem destinationCRS,
            final Envelope envelope,
            Rectangle screenSize,
            AffineTransform worldToScreen)
            throws TransformException, NoninvertibleTransformException {

        this(destinationCRS, envelope, screenSize, worldToScreen, null);
    }

    /**
     * Creates a new {@link GridCoverageRenderer} object.
     *
     * @param destinationCRS the CRS of the {@link GridCoverage2D} to render.
     * @param envelope delineating the area to be rendered.
     * @param screenSize at which we want to render the source {@link GridCoverage2D}.
     * @param worldToScreen if not <code>null</code> and if it contains a rotation, this Affine
     *     Transform is used directly to convert from world coordinates to screen coordinates.
     *     Otherwise, a standard {@link GridToEnvelopeMapper} is used to calculate the affine
     *     transform.
     * @param newHints {@link RenderingHints} to control this rendering process.
     * @throws TransformException
     * @throws NoninvertibleTransformException
     */
    public GridCoverageRenderer(
            final CoordinateReferenceSystem destinationCRS,
            final Envelope envelope,
            final Rectangle screenSize,
            final AffineTransform worldToScreen,
            final RenderingHints newHints)
            throws TransformException, NoninvertibleTransformException {

        // ///////////////////////////////////////////////////////////////////
        //
        // Initialize this renderer
        //
        // ///////////////////////////////////////////////////////////////////
        this.destinationSize = screenSize;
        this.destinationCRS = destinationCRS;
        if (this.destinationCRS == null) {
            throw new TransformException(
                    Errors.format(ErrorKeys.CANT_SEPARATE_CRS_$1, this.destinationCRS));
        }
        destinationEnvelope =
                new GeneralEnvelope(new ReferencedEnvelope(envelope, this.destinationCRS));
        // ///////////////////////////////////////////////////////////////////
        //
        // FINAL DRAWING DIMENSIONS AND RESOLUTION
        // I am here getting the final drawing dimensions (on the device) and
        // the resolution for this rendererbut in the CRS of the source coverage
        // since I am going to compare this info with the same info for the
        // source coverage.
        //
        // ///////////////////////////////////////////////////////////////////

        // PHUSTAD : The gridToEnvelopeMapper does not handle rotated views.
        //
        if (worldToScreen != null && XAffineTransform.getRotation(worldToScreen) != 0.0) {
            finalWorldToGrid = new AffineTransform(worldToScreen);
            finalGridToWorld = finalWorldToGrid.createInverse();
        } else {
            final GridToEnvelopeMapper gridToEnvelopeMapper = new GridToEnvelopeMapper();
            gridToEnvelopeMapper.setPixelAnchor(PixelInCell.CELL_CORNER);
            gridToEnvelopeMapper.setGridRange(new GridEnvelope2D(destinationSize));
            gridToEnvelopeMapper.setEnvelope(destinationEnvelope);
            finalGridToWorld = new AffineTransform(gridToEnvelopeMapper.createAffineTransform());
            finalWorldToGrid = finalGridToWorld.createInverse();
        }

        //
        // HINTS Management
        //
        if (newHints != null) {
            this.hints.add(newHints);
        }
        // factory as needed
        this.gridCoverageFactory = CoverageFactoryFinder.getGridCoverageFactory(this.hints);

        // Interpolation
        if (hints.containsKey(JAI.KEY_INTERPOLATION)) {
            interpolation = (Interpolation) newHints.get(JAI.KEY_INTERPOLATION);
        } else {
            hints.add(new RenderingHints(JAI.KEY_INTERPOLATION, interpolation));
        }
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Rendering using interpolation " + interpolation);
        }
        setInterpolationHints();

        // Tile Size
        if (hints.containsKey(JAI.KEY_IMAGE_LAYOUT)) {
            final ImageLayout layout = (ImageLayout) hints.get(JAI.KEY_IMAGE_LAYOUT);
            //            // only tiles are valid at this stage?? TODO
            layout.unsetImageBounds();
            layout.unsetValid(ImageLayout.COLOR_MODEL_MASK & ImageLayout.SAMPLE_MODEL_MASK);
        }
        // this prevents users from overriding lenient hint
        this.hints.put(Hints.LENIENT_DATUM_SHIFT, Boolean.TRUE);

        // SG add hints for the border extender
        this.hints.add(
                new RenderingHints(
                        JAI.KEY_BORDER_EXTENDER,
                        BorderExtender.createInstance(BorderExtender.BORDER_COPY)));
    }

    /** */
    private void setInterpolationHints() {
        if (interpolation instanceof InterpolationNearest) {
            this.hints.add(new RenderingHints(JAI.KEY_REPLACE_INDEX_COLOR_MODEL, Boolean.FALSE));
            this.hints.add(new RenderingHints(JAI.KEY_TRANSFORM_ON_COLORMAP, Boolean.TRUE));
        } else {
            this.hints.add(new RenderingHints(JAI.KEY_REPLACE_INDEX_COLOR_MODEL, Boolean.TRUE));
            this.hints.add(new RenderingHints(JAI.KEY_TRANSFORM_ON_COLORMAP, Boolean.FALSE));
        }
    }

    /**
     * Write the provided {@link RenderedImage} in the debug directory with the provided file name.
     *
     * @param raster the {@link RenderedImage} that we have to write.
     * @param fileName a {@link String} indicating where we should write it.
     */
    static void writeRenderedImage(final RenderedImage raster, final String fileName) {
        if (DUMP_DIRECTORY == null)
            throw new NullPointerException(
                    "Unable to write the provided coverage in the debug directory");
        if (DEBUG == false)
            throw new IllegalStateException(
                    "Unable to write the provided coverage since we are not in debug mode");
        try {
            ImageIO.write(raster, "tiff", new File(DUMP_DIRECTORY, fileName + ".tiff"));
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
    }

    /**
     * Turns the coverage into a rendered image applying the necessary transformations and the
     * symbolizer
     *
     * <p>Builds a (RenderedImage, AffineTransform) pair that can be used for rendering onto a
     * {@link Graphics2D} or as the basis to build a final image. Will return null if there is
     * nothing to render.
     *
     * @param gridCoverage
     * @param symbolizer
     * @return The transformed image, or null if the coverage does not lie within the rendering
     *     bounds
     * @throws Exception
     */
    public RenderedImage renderImage(
            final GridCoverage2D gridCoverage,
            final RasterSymbolizer symbolizer,
            final double[] bkgValues)
            throws Exception {

        final GridCoverage2D symbolizerGC = renderCoverage(gridCoverage, symbolizer, bkgValues);
        // symbolizerGC will be null if the coverage is outside of the view area.  Returning null
        // here is handled appropriately
        // by the calling method
        return getImageFromParentCoverage(symbolizerGC);
    }

    private GridCoverage2D renderCoverage(
            final GridCoverage2D gridCoverage,
            final RasterSymbolizer symbolizer,
            final double[] bkgValues)
            throws FactoryException {
        // Initial checks
        GridCoverageRendererUtilities.ensureNotNull(gridCoverage, "gridCoverage");
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Drawing coverage " + gridCoverage.toString());
        }

        // //
        //
        // REPROJECTION NEEDED?
        //
        // //
        boolean doReprojection = false;
        final CoordinateReferenceSystem coverageCRS = gridCoverage.getCoordinateReferenceSystem2D();
        if (!CRS.equalsIgnoreMetadata(coverageCRS, destinationCRS)) {
            final MathTransform transform =
                    CRS.findMathTransform(coverageCRS, destinationCRS, true);
            doReprojection = !transform.isIdentity();
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("Reproject needed for rendering provided coverage");
            }
        }

        // /////////////////////////////////////////////////////////////////////
        //
        // CROP
        //
        // /////////////////////////////////////////////////////////////////////
        final GridCoverage2D preReprojection =
                crop(gridCoverage, destinationEnvelope, doReprojection, bkgValues);
        if (preReprojection == null) {
            // nothing to render, the AOI does not overlap
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("Skipping current coverage because crop failed");
            }
            return null;
        }

        // /////////////////////////////////////////////////////////////////////
        //
        // REPROJECTION if needed
        //
        // /////////////////////////////////////////////////////////////////////
        final GridCoverage2D afterReprojection =
                reproject(preReprojection, doReprojection, bkgValues);

        // symbolizer
        return symbolize(afterReprojection, symbolizer, bkgValues);
    }

    private GridCoverage2D symbolize(
            final GridCoverage2D coverage,
            final RasterSymbolizer symbolizer,
            final double[] bkgValues) {
        // ///////////////////////////////////////////////////////////////////
        //
        // FINAL AFFINE
        //
        // ///////////////////////////////////////////////////////////////////
        final GridCoverage2D preSymbolizer = affine(coverage, bkgValues, symbolizer);
        if (preSymbolizer == null) {
            return null;
        }

        // In case of high oversampling the preSymbolizer image might be huge,
        // consider that case and crop if needed
        GridCoverage2D sanitized = preSymbolizer;
        RenderedImage preSymbolizerImage = preSymbolizer.getRenderedImage();
        RenderedImage preAffineImage = coverage.getRenderedImage();
        if (preSymbolizerImage.getWidth() > (preAffineImage.getWidth() * 2)
                || preSymbolizerImage.getHeight() > (preAffineImage.getHeight() * 2)) {
            sanitized = crop(preSymbolizer, destinationEnvelope, false, bkgValues);
        }

        // ///////////////////////////////////////////////////////////////////
        //
        // RASTERSYMBOLIZER
        //
        // ///////////////////////////////////////////////////////////////////
        GridCoverage2D symbolizerGC;
        if (symbolizer != null) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("Applying Raster Symbolizer ");
            }
            final RasterSymbolizerHelper rsp = new RasterSymbolizerHelper(sanitized, this.hints);
            rsp.visit(symbolizer);
            symbolizerGC = (GridCoverage2D) rsp.getOutput();
            symbolizerGC = lookForCompositing(symbolizerGC);
        } else {
            symbolizerGC = preSymbolizer;
        }
        if (DEBUG) {
            writeRenderedImage(symbolizerGC.getRenderedImage(), "postSymbolizer");
        }
        return symbolizerGC;
    }

    /**
     * @param preResample
     * @param doReprojection
     * @param bkgValues
     * @return
     * @throws FactoryException
     */
    private GridCoverage2D reproject(
            GridCoverage2D preResample, boolean doReprojection, double[] bkgValues)
            throws FactoryException {
        GridCoverage2D afterReprojection = null;
        try {
            if (doReprojection) {
                // always have a ROI to account for pixels outside the image
                preResample = addRoiIfMissing(preResample);
                afterReprojection =
                        GridCoverageRendererUtilities.reproject(
                                preResample,
                                destinationCRS,
                                interpolation,
                                destinationEnvelope,
                                bkgValues,
                                hints);
                if (LOGGER.isLoggable(Level.FINE))
                    LOGGER.fine("Reprojecting to crs " + destinationCRS.toString());
            } else {
                afterReprojection = preResample;
            }
            return afterReprojection;
        } finally {
            if (DEBUG) {
                if (afterReprojection != null) {
                    writeRenderedImage(afterReprojection.getRenderedImage(), "afterReprojection");
                }
            }
        }
    }

    /**
     * @param destinationEnvelope
     * @param backgroundValues
     * @param gridCoverage
     * @return
     */
    private GridCoverage2D crop(
            final GridCoverage2D inputCoverage,
            final GeneralEnvelope destinationEnvelope,
            final boolean doReprojection,
            double[] backgroundValues) {

        // //
        //
        // CREATING THE CROP ENVELOPE
        //
        // //
        GridCoverage2D outputCoverage = inputCoverage;
        final GeneralEnvelope coverageEnvelope = (GeneralEnvelope) inputCoverage.getEnvelope();
        final CoordinateReferenceSystem coverageCRS =
                inputCoverage.getCoordinateReferenceSystem2D();

        try {
            GeneralEnvelope renderingEnvelopeInCoverageCRS = null;
            if (doReprojection) {
                renderingEnvelopeInCoverageCRS =
                        GridCoverageRendererUtilities.reprojectEnvelopeWithWGS84Pivot(
                                destinationEnvelope, coverageCRS);
            } else {
                // NO REPROJECTION
                renderingEnvelopeInCoverageCRS = new GeneralEnvelope(destinationEnvelope);
            }
            final GeneralEnvelope cropEnvelope =
                    new GeneralEnvelope(renderingEnvelopeInCoverageCRS);
            cropEnvelope.intersect(coverageEnvelope);
            if (cropEnvelope.isEmpty() || cropEnvelope.isNull()) {
                if (LOGGER.isLoggable(Level.INFO)) {
                    LOGGER.info(
                            "The destination envelope does not intersect the envelope of the source coverage.");
                }
                return null;
            }

            ////
            //
            // Cropping for real
            //
            /////
            outputCoverage =
                    GridCoverageRendererUtilities.crop(
                            inputCoverage, cropEnvelope, backgroundValues, hints);
        } catch (Throwable t) {
            ////
            //
            // If it happens that the crop fails we try to proceed since the crop does only an
            // optimization. Things might
            // work out anyway.
            //
            //// {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "Crop Failed for reason: " + t.getLocalizedMessage(), t);
            }
            outputCoverage = inputCoverage;
        }

        if (DEBUG) {
            writeRenderedImage(outputCoverage.getRenderedImage(), "crop");
        }
        return outputCoverage;
    }

    /**
     * @param bkgValues
     * @param preResample
     * @return
     */
    private GridCoverage2D affine(
            GridCoverage2D input, double[] bkgValues, RasterSymbolizer symbolizer) {
        // NOTICE that at this stage the image we get should be 8 bits, either RGB, RGBA, Gray,
        // GrayA
        // either multiband or indexed. It could also be 16 bits indexed!!!!
        final RenderedImage finalImage = input.getRenderedImage();
        final GridGeometry2D preSymbolizerGridGeometry = (input.getGridGeometry());
        // I need to translate half of a pixel since in wms the envelope
        // map to the corners of the raster space not to the center of the
        // pixels.
        final MathTransform2D finalGCTransform =
                preSymbolizerGridGeometry.getGridToCRS2D(PixelOrientation.UPPER_LEFT);
        if (!(finalGCTransform instanceof AffineTransform)) {
            throw new UnsupportedOperationException(
                    "Non-affine transformations not yet implemented"); // TODO
        }
        final AffineTransform finalGCgridToWorld =
                new AffineTransform((AffineTransform) finalGCTransform);

        // Getting NOData adn ROI
        Range noData =
                CoverageUtilities.getNoDataProperty(input) != null
                        ? CoverageUtilities.getNoDataProperty(input).getAsRange()
                        : null;
        ROI roi = CoverageUtilities.getROIProperty(input);

        // //
        //
        // I am going to concatenate the final world to grid transform for the
        // screen area with the grid to world transform of the input coverage.
        //
        // This way i right away position the coverage at the right place in the
        // area of interest for the device.
        //
        // //
        final AffineTransform finalRasterTransformation =
                (AffineTransform) finalWorldToGrid.clone();
        finalRasterTransformation.concatenate(finalGCgridToWorld);

        // paranoiac check to avoid that JAI freaks out when computing its internal layouT on images
        // that are too small
        Rectangle2D finalLayout =
                GridCoverageRendererUtilities.layoutHelper(
                        finalImage,
                        (float) Math.abs(finalRasterTransformation.getScaleX()),
                        (float) Math.abs(finalRasterTransformation.getScaleY()),
                        (float) finalRasterTransformation.getTranslateX(),
                        (float) finalRasterTransformation.getTranslateY(),
                        interpolation);
        if (finalLayout.isEmpty()) {
            if (LOGGER.isLoggable(java.util.logging.Level.FINE))
                LOGGER.fine(
                        "Unable to create a granuleDescriptor "
                                + this.toString()
                                + " due to jai scale bug");
            return null;
        }

        RenderedImage im = null;
        try {
            // if we have a color map don't expand the index color model
            Hints localHints = new Hints();
            localHints.putAll(hints);
            if (symbolizer != null && symbolizer.getColorMap() != null) {
                localHints.put(JAI.KEY_REPLACE_INDEX_COLOR_MODEL, false);
            }
            ImageWorker iw = new ImageWorker(finalImage);
            iw.setRenderingHints(localHints);
            iw.setROI(roi);
            iw.setNoData(noData);
            iw.affine(finalRasterTransformation, interpolation, bkgValues);
            im = iw.getRenderedImage();
            roi = iw.getROI();
            noData = iw.extractNoDataProperty(im);
        } finally {
            if (DEBUG) {
                writeRenderedImage(im, "postAffine");
            }
        }
        // recreate gridCoverage
        int numBands = im.getSampleModel().getNumBands();
        GridSampleDimension[] sd = new GridSampleDimension[numBands];
        for (int i = 0; i < numBands; i++) {
            sd[i] =
                    new GridSampleDimension(
                            TypeMap.getColorInterpretation(im.getColorModel(), i).name());
        }

        Map properties = input.getProperties();
        if (properties == null) {
            properties = new HashMap<>();
        }
        CoverageUtilities.setNoDataProperty(properties, noData);
        CoverageUtilities.setROIProperty(properties, roi);

        // create a new grid coverage but preserve as much input as possible
        return this.gridCoverageFactory.create(
                input.getName(),
                im,
                new GridGeometry2D(
                        new GridEnvelope2D(PlanarImage.wrapRenderedImage(im).getBounds()),
                        input.getEnvelope()),
                sd,
                new GridCoverage[] {input},
                properties);
    }

    /**
     * Turns the coverage into a rendered image applying the necessary transformations and the
     * symbolizer
     *
     * @param gridCoverage
     * @param symbolizer
     * @return The transformed image, or null if the coverage does not lie within the rendering
     *     bounds
     * @throws FactoryException
     * @throws TransformException
     * @throws NoninvertibleTransformException @Deprecated
     */
    public RenderedImage renderImage(
            final GridCoverage2D gridCoverage,
            final RasterSymbolizer symbolizer,
            final Interpolation interpolation,
            final Color background,
            final int tileSizeX,
            final int tileSizeY)
            throws FactoryException, TransformException, NoninvertibleTransformException {

        GridCoverage2D coverage =
                renderCoverage(
                        gridCoverage, symbolizer, interpolation, background, tileSizeX, tileSizeY);
        return getImageFromParentCoverage(coverage);
    }

    private GridCoverage2D renderCoverage(
            final GridCoverage2D gridCoverage,
            final RasterSymbolizer symbolizer,
            final Interpolation interpolation,
            final Color background,
            final int tileSizeX,
            final int tileSizeY)
            throws FactoryException {
        final Hints oldHints = this.hints.clone();

        setupTilingHints(tileSizeX, tileSizeY);
        setupInterpolationHints(interpolation);

        try {
            return renderCoverage(
                    gridCoverage,
                    symbolizer,
                    GridCoverageRendererUtilities.colorToArray(background));
        } catch (Exception e) {
            throw new FactoryException(e);
        } finally {
            this.hints = oldHints;
        }
    }

    private RenderedImage getImageFromParentCoverage(GridCoverage2D parentCoverage) {
        if (parentCoverage == null) {
            return null;
        }
        RenderedImage ri = parentCoverage.getRenderedImage();
        if (ri != null) {
            PlanarImage pi = PlanarImage.wrapRenderedImage(ri);
            pi.setProperty(PARENT_COVERAGE_PROPERTY, parentCoverage);
            ri = pi;
        }
        return ri;
    }

    private void setupTilingHints(final int tileSizeX, final int tileSizeY) {
        ////
        //
        // TILING
        //
        ////
        if (tileSizeX > 0 && tileSizeY > 0) {
            // Tile Size
            final ImageLayout layout = new ImageLayout2();
            layout.setTileGridXOffset(0)
                    .setTileGridYOffset(0)
                    .setTileHeight(tileSizeY)
                    .setTileWidth(tileSizeX);
            hints.add(new RenderingHints(JAI.KEY_IMAGE_LAYOUT, layout));
        }
    }

    private void setupInterpolationHints(final Interpolation interpolation) {
        ////
        //
        // INTERPOLATION
        //
        ////
        if (interpolation != null) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("Rendering using interpolation " + interpolation);
            }
            this.interpolation = interpolation;
            hints.add(new RenderingHints(JAI.KEY_INTERPOLATION, this.interpolation));
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("Rendering using interpolation " + interpolation);
            }
            setInterpolationHints();
        }
    }

    public RenderedImage renderImage(
            final GridCoverage2DReader reader,
            GeneralParameterValue[] readParams,
            final RasterSymbolizer symbolizer,
            final Interpolation interpolation,
            final Color background,
            final int tileSizeX,
            final int tileSizeY)
            throws FactoryException, TransformException, NoninvertibleTransformException,
                    IOException {
        // setup the hints
        setupTilingHints(tileSizeX, tileSizeY);
        setupInterpolationHints(interpolation);

        return renderImage(reader, readParams, symbolizer, interpolation, background);
    }

    private RenderedImage renderImage(
            final GridCoverage2DReader reader,
            GeneralParameterValue[] readParams,
            final RasterSymbolizer symbolizer,
            final Interpolation interpolation,
            final Color background)
            throws FactoryException, IOException, TransformException {
        // see if we have a projection handler
        CoordinateReferenceSystem sourceCRS = reader.getCoordinateReferenceSystem();
        CoordinateReferenceSystem targetCRS = destinationEnvelope.getCoordinateReferenceSystem();

        // Check if reader supports band selection, and rearrange raster channels order in
        // symbolizer. Reader should have taken care o proper channel order, based on initial
        // symbolizer channel definition
        RasterSymbolizer finalSymbolizer = symbolizer;
        if (symbolizer != null && isBandsSelectionApplicable(reader, symbolizer)) {
            readParams = applyBandsSelectionParameter(reader, readParams, symbolizer);
            finalSymbolizer = setupSymbolizerForBandsSelection(symbolizer);
        }

        ProjectionHandler handler = null;
        List<GridCoverage2D> coverages;
        // read all the coverages we need, cut and whatnot
        GridCoverageReaderHelper rh =
                new GridCoverageReaderHelper(
                        reader,
                        destinationSize,
                        ReferencedEnvelope.reference(destinationEnvelope),
                        interpolation,
                        hints);
        // are we dealing with a remote service wrapped in a reader, one that can handle
        // reprojection
        // by itself?
        if (GridCoverageReaderHelper.isReprojectingReader(reader)) {
            GridCoverage2D coverage = rh.readCoverage(readParams);
            coverages = new ArrayList<>();
            coverages.add(coverage);
        } else {
            if (advancedProjectionHandlingEnabled) {
                handler =
                        ProjectionHandlerFinder.getHandler(
                                rh.getReadEnvelope(), sourceCRS, wrapEnabled);
                if (handler instanceof WrappingProjectionHandler) {
                    // raster data is monolithic and can cover the whole world, disable
                    // the geometry wrapping heuristic
                    ((WrappingProjectionHandler) handler).setDatelineWrappingCheckEnabled(false);
                }
            }
            coverages = rh.readCoverages(readParams, handler, gridCoverageFactory);
        }

        // check if we have to reproject
        boolean reprojectionNeeded = false;
        for (GridCoverage2D coverage : coverages) {
            if (coverage == null) {
                continue;
            }
            final CoordinateReferenceSystem coverageCRS = coverage.getCoordinateReferenceSystem();
            if (!CRS.equalsIgnoreMetadata(coverageCRS, destinationCRS)) {
                reprojectionNeeded = true;
                break;
            }
        }

        // establish the background values, and expand palettes if the bgcolor cannot be represented
        double[] bgValues = GridCoverageRendererUtilities.colorToArray(background);
        // If coverage is out of view area, coverages has size 1 but the first element is null
        if (!coverages.isEmpty() && coverages.get(0) != null) {
            ColorModel cm = coverages.get(0).getRenderedImage().getColorModel();
            if (cm instanceof IndexColorModel && background != null) {
                IndexColorModel icm = (IndexColorModel) cm;
                int idx = ColorUtilities.findColorIndex(background, icm);
                if (idx < 0) {
                    // not found, we have to expand
                    for (int i = 0; i < coverages.size(); i++) {
                        GridCoverage2D coverage = coverages.get(i);
                        ImageWorker iw = new ImageWorker(coverage.getRenderedImage());
                        iw.forceComponentColorModel();
                        GridCoverage2D expandedCoverage =
                                gridCoverageFactory.create(
                                        coverage.getName(),
                                        iw.getRenderedImage(),
                                        coverage.getGridGeometry(),
                                        null,
                                        new GridCoverage2D[] {coverage},
                                        coverage.getProperties());
                        coverages.set(i, expandedCoverage);
                    }
                }
            } else {
                bgValues = GridCoverageRendererUtilities.colorToArray(background);
            }
        }

        // if we need to reproject, we need to ensure that none of the pixels go out of
        // the projection valid area, not even slightly
        if (reprojectionNeeded && handler != null && handler.getValidAreaBounds() != null) {
            List<GridCoverage2D> cropped = new ArrayList<>();
            ReferencedEnvelope validArea = handler.getValidAreaBounds();
            GridGeometryReducer reducer = new GridGeometryReducer(validArea);
            for (GridCoverage2D coverage : coverages) {
                GridGeometry2D gg = coverage.getGridGeometry();
                GridGeometry2D reduced = reducer.reduce(gg);
                if (!reduced.equals(gg)) {
                    GeneralEnvelope cutEnvelope = reducer.getCutEnvelope(reduced);
                    GridCoverage2D croppedCoverage = crop(coverage, cutEnvelope, false, bgValues);
                    if (croppedCoverage != null) {
                        cropped.add(croppedCoverage);
                    }
                } else {
                    cropped.add(coverage);
                }
            }
            coverages = cropped;
        }

        // reproject if needed
        List<GridCoverage2D> reprojectedCoverages = new ArrayList<GridCoverage2D>();
        for (GridCoverage2D coverage : coverages) {
            if (coverage == null) {
                continue;
            }
            final CoordinateReferenceSystem coverageCRS = coverage.getCoordinateReferenceSystem();
            if (!CRS.equalsIgnoreMetadata(coverageCRS, destinationCRS)) {
                GridCoverage2D reprojected = reproject(coverage, true, bgValues);
                if (reprojected != null) {
                    reprojectedCoverages.add(reprojected);
                }
            } else {
                reprojectedCoverages.add(coverage);
            }
        }

        // displace them if needed via a projection handler
        List<GridCoverage2D> displacedCoverages = new ArrayList<GridCoverage2D>();
        if (handler != null) {
            Envelope testEnvelope = ReferencedEnvelope.reference(destinationEnvelope);
            MathTransform mt = CRS.findMathTransform(sourceCRS, targetCRS);
            PolygonExtractor polygonExtractor = new PolygonExtractor();
            for (GridCoverage2D coverage : reprojectedCoverages) {
                // Check on the alpha band
                Polygon polygon = JTS.toGeometry((BoundingBox) coverage.getEnvelope2D());
                Geometry postProcessed = handler.postProcess(mt, polygon);
                // extract sub-polygons and displace
                List<Polygon> polygons = polygonExtractor.getPolygons(postProcessed);
                for (Polygon displaced : polygons) {
                    // check we are really inside the display area before moving one
                    Envelope intersection =
                            testEnvelope.intersection(displaced.getEnvelopeInternal());
                    if (intersection == null
                            || intersection.isNull()
                            || intersection.getArea() == 0) {
                        continue;
                    }
                    if (displaced.equals(polygon)) {
                        displacedCoverages.add(coverage);
                    } else {
                        double[] tx = getTranslationFactors(polygon, displaced);
                        if (tx != null) {
                            GridCoverage2D displacedCoverage =
                                    GridCoverageRendererUtilities.displace(
                                            coverage, tx[0], tx[1], gridCoverageFactory);
                            displacedCoverages.add(displacedCoverage);
                        }
                    }
                }
            }
        } else {
            displacedCoverages.addAll(reprojectedCoverages);
        }

        // after reprojection and displacement we could have some coverage
        // that are completely out of the destination area (due to numerical issues
        // their source bbox was interesting the request area, but their reprojected version does
        // not
        for (Iterator<GridCoverage2D> it = displacedCoverages.iterator(); it.hasNext(); ) {
            GridCoverage2D coverage = it.next();
            ReferencedEnvelope re = ReferencedEnvelope.reference(coverage.getEnvelope2D());
            if (!destinationEnvelope.intersects(re, false)) {
                it.remove();
            }
        }

        // symbolize each bit (done here to make sure we can perform the warp/affine reduction)
        List<GridCoverage2D> symbolizedCoverages = new ArrayList<>();
        int ii = 0;
        for (GridCoverage2D displaced : displacedCoverages) {
            GridCoverage2D symbolized = symbolize(displaced, finalSymbolizer, bgValues);
            if (symbolized != null) {
                symbolizedCoverages.add(symbolized);
            }
            ii++;
        }

        // Parameters used for taking into account an optional removal of the alpha band
        // and an optional reindexing after color expansion

        // if more than one coverage, mosaic
        GridCoverage2D mosaicked = null;
        if (symbolizedCoverages.size() == 0) {
            return null;
        } else if (symbolizedCoverages.size() == 1) {
            mosaicked = symbolizedCoverages.get(0);
        } else {
            // sort the coverages by size, avoid having a sliver coverage 1-2 px large or high
            // first as it will have a skewed grid to world that will then be applied
            // to all members of the mosaic
            Comparator<GridCoverage2D> sliverComparator =
                    (c1, c2) -> {
                        RenderedImage r1 = c1.getRenderedImage();
                        RenderedImage r2 = c2.getRenderedImage();
                        // area2 - area1, largest first
                        long areaDiff =
                                ((long) r2.getWidth()) * r2.getHeight()
                                        - ((long) r1.getWidth()) * r1.getHeight();
                        return (int) Math.signum(areaDiff);
                    };
            Collections.sort(symbolizedCoverages, sliverComparator);

            // do not expand index color models, we know they are all the same
            Hints mosaicHints = new Hints(this.hints);
            mosaicHints.put(JAI.KEY_REPLACE_INDEX_COLOR_MODEL, false);
            mosaicked =
                    GridCoverageRendererUtilities.mosaic(
                            symbolizedCoverages,
                            new ArrayList<GridCoverage2D>(),
                            destinationEnvelope,
                            mosaicHints,
                            bgValues);
        }

        // the mosaicking can cut off images that are just slightly out of the
        // request (effect of the read buffer + a request touching the actual data area)
        if (mosaicked == null) {
            return null;
        }

        // at this point, we might have a coverage that's still slightly larger
        // than the one requested, crop as needed
        GridCoverage2D cropped = crop(mosaicked, destinationEnvelope, false, bgValues);
        return getImageFromParentCoverage(cropped);
    }

    /**
     * Forces adding ROI to the coverage in case it's missing. It will use the renderer image
     * footprint.
     *
     * @param coverage
     * @return
     */
    private GridCoverage2D addRoiIfMissing(GridCoverage2D coverage) {
        RenderedImage input = coverage.getRenderedImage();
        Object roiObject = input.getProperty("ROI");
        Object gcRoiObject = coverage.getProperty("GC_ROI");
        if (!(roiObject instanceof ROI) && !(gcRoiObject instanceof ROI)) {
            Envelope env =
                    new Envelope(
                            input.getMinX(),
                            input.getMinX() + input.getWidth(),
                            input.getMinY(),
                            input.getMinY() + input.getHeight());
            ROI roi = new ROI(new ROIGeometry(JTS.toGeometry(env)).getAsImage());
            PlanarImage pi = PlanarImage.wrapRenderedImage(input);
            pi.setProperty("ROI", roi);
            final Map sourceProperties = coverage.getProperties();
            Map properties =
                    sourceProperties == null ? new HashMap() : new HashMap(sourceProperties);
            properties.put("GC_ROI", roi);
            return gridCoverageFactory.create(
                    coverage.getName(),
                    pi,
                    coverage.getGridGeometry(),
                    null,
                    new GridCoverage2D[] {coverage},
                    properties);
        } else {
            return coverage;
        }
    }

    /**
     * Method for creating an alpha band to use for mosaiking
     *
     * @param coverage
     * @return a new GridCoverage containing a single band with the same size of the input Coverage
     */
    private GridCoverage2D createAlphaBand(GridCoverage2D coverage) {
        // Getting input image
        RenderedImage input = coverage.getRenderedImage();
        // Define image layout
        ImageLayout layout = new ImageLayout();
        layout.setMinX(input.getMinX());
        layout.setMinY(input.getMinY());
        layout.setWidth(input.getWidth());
        layout.setHeight(input.getHeight());
        layout.setTileHeight(input.getTileHeight());
        layout.setTileWidth(input.getTileWidth());
        // Define rendering hints
        RenderingHints renderHints = new RenderingHints(JAI.KEY_IMAGE_LAYOUT, layout);
        // Create an image with all 255
        RenderedImage alpha =
                ConstantDescriptor.create(
                        Float.valueOf(input.getWidth()),
                        Float.valueOf(input.getHeight()),
                        new Byte[] {(byte) 255},
                        renderHints);
        // Format Image
        ImageWorker w = new ImageWorker(alpha).format(input.getSampleModel().getDataType());

        // Create the GridCoverage
        GridCoverage2D newCoverage =
                gridCoverageFactory.create(
                        coverage.getName() + "Alpha",
                        w.getRenderedImage(),
                        coverage.getGridGeometry(),
                        null,
                        new GridCoverage2D[] {coverage},
                        coverage.getProperties());
        return newCoverage;
    }

    private double[] getTranslationFactors(Polygon reference, Polygon displaced) {
        // compare the two envelopes
        Envelope re = reference.getEnvelopeInternal();
        Envelope de = displaced.getEnvelopeInternal();
        double dw = Math.abs(re.getWidth() - de.getWidth());
        double dh = Math.abs(re.getHeight() - de.getHeight());
        if (dw > EPS * re.getWidth() || dh > EPS * re.getWidth()) {
            // this was not just a translation
            return null;
        }

        // compute the translation
        double dx = de.getMinX() - re.getMinX();
        double dy = de.getMinY() - re.getMinY();

        Polygon cloned = (Polygon) displaced.copy();
        cloned.apply(AffineTransformation.translationInstance(-dx, -dy));
        if (1 - new HausdorffSimilarityMeasure().measure(cloned, reference) > EPS) {
            return null;
        } else {
            return new double[] {dx, dy};
        }
    }

    /**
     * Paint this grid coverage. The caller must ensure that <code>graphics</code> has an affine
     * transform mapping "real world" coordinates in the coordinate system given by {@link
     * #getCoordinateSystem}.
     *
     * @param graphics the {@link Graphics2D} context in which to paint.
     * @param metaBufferedEnvelope
     * @throws Exception
     * @throws UnsupportedOperationException if the transformation from grid to coordinate system in
     *     the GridCoverage is not an AffineTransform
     */
    public void paint(
            final Graphics2D graphics,
            final GridCoverage2D gridCoverage,
            final RasterSymbolizer symbolizer)
            throws Exception {
        paint(graphics, gridCoverage, symbolizer, null);
    }

    /**
     * Paint this grid coverage. The caller must ensure that <code>graphics</code> has an affine
     * transform mapping "real world" coordinates in the coordinate system given by {@link
     * #getCoordinateSystem}.
     *
     * @param graphics the {@link Graphics2D} context in which to paint.
     * @param metaBufferedEnvelope
     * @throws Exception
     * @throws UnsupportedOperationException if the transformation from grid to coordinate system in
     *     the GridCoverage is not an AffineTransform
     */
    public void paint(
            final Graphics2D graphics,
            final GridCoverage2D gridCoverage,
            final RasterSymbolizer symbolizer,
            final double[] bkgValues)
            throws Exception {

        //
        // Initial checks
        //
        if (graphics == null) {
            throw new NullPointerException(Errors.format(ErrorKeys.NULL_ARGUMENT_$1, "graphics"));
        }
        if (gridCoverage == null) {
            throw new NullPointerException(
                    Errors.format(ErrorKeys.NULL_ARGUMENT_$1, "gridCoverage"));
        }

        if (LOGGER.isLoggable(Level.FINE))
            LOGGER.fine(
                    new StringBuilder("Drawing coverage ")
                            .append(gridCoverage.toString())
                            .toString());

        // Build the final image and the transformation
        RenderedImage finalImage = renderImage(gridCoverage, symbolizer, bkgValues);
        paintImage(graphics, finalImage, symbolizer);
    }

    /**
     * Paint the coverage read from the reader (using advanced projection handling). The caller must
     * ensure that <code>graphics</code> has an affine transform mapping "real world" coordinates in
     * the coordinate system given by {@link #getCoordinateSystem}.
     *
     * @param graphics the {@link Graphics2D} context in which to paint.
     * @param metaBufferedEnvelope
     * @throws Exception
     * @throws UnsupportedOperationException if the transformation from grid to coordinate system in
     *     the GridCoverage is not an AffineTransform
     */
    public void paint(
            final Graphics2D graphics,
            final GridCoverage2DReader gridCoverageReader,
            GeneralParameterValue[] readParams,
            final RasterSymbolizer symbolizer,
            Interpolation interpolation,
            final Color background)
            throws Exception {

        //
        // Initial checks
        //
        if (graphics == null) {
            throw new NullPointerException(Errors.format(ErrorKeys.NULL_ARGUMENT_$1, "graphics"));
        }
        if (gridCoverageReader == null) {
            throw new NullPointerException(
                    Errors.format(ErrorKeys.NULL_ARGUMENT_$1, "gridCoverageReader"));
        }

        if (LOGGER.isLoggable(Level.FINE))
            LOGGER.fine(
                    new StringBuilder("Drawing reader ")
                            .append(gridCoverageReader.toString())
                            .toString());

        setupInterpolationHints(interpolation);

        // Build the final image and the transformation
        RenderedImage finalImage =
                renderImage(gridCoverageReader, readParams, symbolizer, interpolation, background);
        if (finalImage != null) {
            try {
                paintImage(graphics, finalImage, symbolizer);
            } finally {
                if (finalImage instanceof PlanarImage) {
                    ImageUtilities.disposePlanarImageChain((PlanarImage) finalImage);
                }
            }
        }
    }

    private void paintImage(
            final Graphics2D graphics,
            RenderedImage inputImage,
            final RasterSymbolizer symbolizer) {
        final RenderingHints oldHints = graphics.getRenderingHints();
        graphics.setRenderingHints(this.hints);

        // nothing to do if the input image is null
        if (inputImage == null) {
            return;
        }

        // force transparency on NODATA and ROI
        RenderedImage transparentImage =
                new ImageWorker(inputImage).prepareForRendering().getRenderedImage();

        try {
            // debug
            if (DEBUG) {
                writeRenderedImage(transparentImage, "final");
            }

            final boolean multiply =
                    symbolizer.getShadedRelief() != null
                            && symbolizer.getShadedRelief().isBrightnessOnly();
            if (multiply) {
                graphics.setComposite(BlendComposite.getInstance(BlendingMode.MULTIPLY, 1f));
                transparentImage = Compositing.forceToRGB(transparentImage, true);
            } else {
                // force solid alpha, the transparency has already been
                // dealt with in the image preparation, and we have to make
                // sure previous vector rendering code did not leave a non solid alpha
                graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
            }

            // //
            // Drawing the Image
            // //
            graphics.drawRenderedImage(transparentImage, GridCoverageRenderer.IDENTITY);

        } catch (Throwable t) {
            try {
                // log the error
                if (LOGGER.isLoggable(Level.FINE))
                    LOGGER.log(Level.FINE, t.getLocalizedMessage(), t);

                // /////////////////////////////////////////////////////////////
                // this is a workaround for a bug in Java2D, we need to convert
                // the image to component color model to make it work just fine.
                // /////////////////////////////////////////////////////////////
                if (t instanceof IllegalArgumentException) {
                    if (DEBUG) {
                        writeRenderedImage(transparentImage, "preWORKAROUND1");
                    }
                    final RenderedImage componentImage =
                            new ImageWorker(transparentImage)
                                    .forceComponentColorModel(true)
                                    .getRenderedImage();

                    if (DEBUG) {
                        writeRenderedImage(componentImage, "WORKAROUND1");
                    }
                    graphics.drawRenderedImage(componentImage, GridCoverageRenderer.IDENTITY);

                } else if (t instanceof ImagingOpException)
                // /////////////////////////////////////////////////////////////
                // this is a workaround for a bug in Java2D
                // (see bug 4723021
                // http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4723021).
                //
                // AffineTransformOp.filter throws a
                // java.awt.image.ImagingOpException: Unable to tranform src
                // image when a PixelInterleavedSampleModel is used.
                //
                // CUSTOMER WORKAROUND :
                // draw the BufferedImage into a buffered image of type ARGB
                // then perform the affine transform. THIS OPERATION WASTES
                // RESOURCES BY PERFORMING AN ALLOCATION OF MEMORY AND A COPY ON
                // LARGE IMAGES.
                // /////////////////////////////////////////////////////////////
                {
                    BufferedImage buf =
                            transparentImage.getColorModel().hasAlpha()
                                    ? new BufferedImage(
                                            transparentImage.getWidth(),
                                            transparentImage.getHeight(),
                                            BufferedImage.TYPE_4BYTE_ABGR)
                                    : new BufferedImage(
                                            transparentImage.getWidth(),
                                            transparentImage.getHeight(),
                                            BufferedImage.TYPE_3BYTE_BGR);
                    if (DEBUG) {
                        writeRenderedImage(buf, "preWORKAROUND2");
                    }
                    final Graphics2D g = (Graphics2D) buf.getGraphics();
                    final int translationX = transparentImage.getMinX(),
                            translationY = transparentImage.getMinY();
                    g.drawRenderedImage(
                            transparentImage,
                            AffineTransform.getTranslateInstance(-translationX, -translationY));
                    g.dispose();
                    if (DEBUG) {
                        writeRenderedImage(buf, "WORKAROUND2");
                    }
                    GridCoverageRenderer.IDENTITY.concatenate(
                            AffineTransform.getTranslateInstance(translationX, translationY));
                    graphics.drawImage(buf, GridCoverageRenderer.IDENTITY, null);
                    // release
                    buf.flush();
                    buf = null;
                } else
                // log the error
                if (LOGGER.isLoggable(Level.WARNING))
                    LOGGER.log(
                            Level.WARNING,
                            "Unable to renderer this raster, no workaround found",
                            t);

            } catch (Throwable t1) {
                // if the workaround fails again, there is really nothing to do
                // :-(
                LOGGER.log(Level.WARNING, t1.getLocalizedMessage(), t1);
            } finally {
                // ///////////////////////////////////////////////////////////////////
                //
                // Restore old hints
                //
                // ///////////////////////////////////////////////////////////////////
                graphics.setRenderingHints(oldHints);
            }
        }
    }

    private GeneralParameterValue[] applyBandsSelectionParameter(
            GridCoverageReader reader,
            GeneralParameterValue[] readParams,
            RasterSymbolizer symbolizer) {
        int[] bandIndices =
                ChannelSelectionUpdateStyleVisitor.getBandIndicesFromSelectionChannels(symbolizer);
        Parameter<int[]> bandIndicesParam = null;
        bandIndicesParam = (Parameter<int[]>) AbstractGridFormat.BANDS.createValue();
        bandIndicesParam.setValue(bandIndices);
        List<GeneralParameterValue> paramList = new ArrayList<GeneralParameterValue>();
        if (readParams != null) {
            paramList.addAll(Arrays.asList(readParams));
        }
        paramList.add(bandIndicesParam);
        return paramList.toArray(new GeneralParameterValue[paramList.size()]);
    }

    /**
     * Takes into account that the band selection has been delegated down to the reader by producing
     * a new channel selection
     *
     * @param symbolizer
     * @return
     */
    public static RasterSymbolizer setupSymbolizerForBandsSelection(RasterSymbolizer symbolizer) {
        ChannelSelection selection = symbolizer.getChannelSelection();
        final SelectedChannelType[] originalChannels = selection.getSelectedChannels();
        if (originalChannels != null) {
            int i = 0;
            SelectedChannelType[] channels = new SelectedChannelType[originalChannels.length];
            for (SelectedChannelType originalChannel : originalChannels) {
                // Remember, channel indices start from 1
                SelectedChannelTypeImpl channel = new SelectedChannelTypeImpl();
                channel.setChannelName(Integer.toString(i + 1));
                channel.setContrastEnhancement(originalChannel.getContrastEnhancement());
                channels[i] = channel;
                i++;
            }
            ChannelSelectionUpdateStyleVisitor channelsUpdateVisitor =
                    new ChannelSelectionUpdateStyleVisitor(channels);
            symbolizer.accept(channelsUpdateVisitor);
            return (RasterSymbolizer) channelsUpdateVisitor.getCopy();
        }
        return symbolizer;
    }

    /**
     * Checks if band selection is present, and can be delegated down to the reader
     *
     * @param reader
     * @param symbolizer
     * @return
     */
    public static boolean isBandsSelectionApplicable(
            GridCoverageReader reader, RasterSymbolizer symbolizer) {
        int[] bandIndices =
                ChannelSelectionUpdateStyleVisitor.getBandIndicesFromSelectionChannels(symbolizer);
        return reader.getFormat() != null
                && reader.getFormat()
                        .getReadParameters()
                        .getDescriptor()
                        .descriptors()
                        .contains(AbstractGridFormat.BANDS)
                && bandIndices != null;
    }

    /**
     * Check whether this source GridCoverage comes with a {@link Compositing} object which need to
     * be applied.
     */
    private GridCoverage2D lookForCompositing(GridCoverage2D source) {
        Object compositing = source.getProperty(KEY_COMPOSITING);
        if (compositing != null && compositing instanceof Compositing) {
            return ((Compositing) compositing)
                    .composeGridCoverage(
                            source, CoverageFactoryFinder.getGridCoverageFactory(hints));
        }
        return source;
    }
}
