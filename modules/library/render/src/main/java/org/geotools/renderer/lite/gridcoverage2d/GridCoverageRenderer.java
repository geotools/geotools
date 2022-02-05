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

import it.geosolutions.jaiext.utilities.ImageLayout2;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ImagingOpException;
import java.awt.image.IndexColorModel;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import javax.media.jai.BorderExtender;
import javax.media.jai.ImageLayout;
import javax.media.jai.Interpolation;
import javax.media.jai.InterpolationNearest;
import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import org.geotools.coverage.CoverageFactoryFinder;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.geometry.GeneralEnvelope;
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
import org.locationtech.jts.geom.Envelope;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.coverage.grid.GridCoverageReader;
import org.opengis.coverage.grid.GridEnvelope;
import org.opengis.metadata.spatial.PixelOrientation;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.datum.PixelInCell;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

/**
 * A helper class for rendering {@link GridCoverage} objects.
 *
 * @author Simone Giannecchini, GeoSolutions SAS
 * @author Andrea Aime, GeoSolutions SAS
 * @author Alessio Fabiani, GeoSolutions SAS
 * @version $Id$
 */
public final class GridCoverageRenderer {

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
                    LOGGER.severe("Unable to create debug dir, exiting application!!!");
                DEBUG = false;
                DUMP_DIRECTORY = null;
            } else {
                DUMP_DIRECTORY = tempDir.getAbsolutePath();
                LOGGER.info("Rendering debug dir " + DUMP_DIRECTORY);
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
     * @return The transformed image, or null if the coverage does not lie within the rendering
     *     bounds
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
        Hints warpAffineHints = getReprojectionHints(hints, preReprojection);
        GridCoverage2D afterReprojection = preReprojection;
        if (doReprojection) {

            afterReprojection =
                    GridCoverageRendererUtilities.reproject(
                            preReprojection,
                            destinationCRS,
                            interpolation,
                            destinationEnvelope,
                            bkgValues,
                            gridCoverageFactory,
                            warpAffineHints);
        }

        if (DEBUG) {
            if (afterReprojection != null) {
                writeRenderedImage(afterReprojection.getRenderedImage(), "afterReprojection");
            }
        }

        // symbolizer
        GridCoverage2D symbolized = afterReprojection;
        if (afterReprojection != null) {
            symbolized = symbolize(afterReprojection, symbolizer, bkgValues, warpAffineHints);
        }
        return symbolized;
    }

    private GridCoverage2D symbolize(
            final GridCoverage2D coverage,
            final RasterSymbolizer symbolizer,
            final double[] bkgValues,
            final Hints hints) {
        // ///////////////////////////////////////////////////////////////////
        //
        // FINAL AFFINE
        //
        // ///////////////////////////////////////////////////////////////////
        final GridCoverage2D preSymbolizer = affine(coverage, bkgValues, symbolizer, hints);
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

    /** */
    private GridCoverage2D crop(
            final GridCoverage2D inputCoverage,
            final GeneralEnvelope destinationEnvelope,
            final boolean doReprojection,
            double[] backgroundValues) {

        GridCoverage2D outputCoverage =
                GridCoverageRendererUtilities.crop(
                        inputCoverage,
                        destinationEnvelope,
                        doReprojection,
                        backgroundValues,
                        hints);
        if (DEBUG && outputCoverage != null) {
            writeRenderedImage(outputCoverage.getRenderedImage(), "crop");
        }
        return outputCoverage;
    }

    /** */
    private GridCoverage2D affine(
            GridCoverage2D input, double[] bkgValues, RasterSymbolizer symbolizer, Hints hints) {
        // NOTICE that at this stage the image we get should be 8 bits, either RGB, RGBA, Gray,
        // GrayA either multiband or indexed. It could also be 16 bits indexed!!!!

        Hints localHints = new Hints();
        localHints.putAll(hints);
        if (symbolizer != null && symbolizer.getColorMap() != null) {
            localHints.put(JAI.KEY_REPLACE_INDEX_COLOR_MODEL, false);
        }
        // Preserve the sample dimensions names when no symbolizer get used
        // Styles using GridCoverage's named properties may not find them if renamed
        final boolean useInputSampleDimensions = symbolizer == null;
        GridCoverage2D gc =
                GridCoverageRendererUtilities.affine(
                        input,
                        interpolation,
                        finalWorldToGrid,
                        bkgValues,
                        useInputSampleDimensions,
                        gridCoverageFactory,
                        localHints);
        if (DEBUG && gc != null && gc.getRenderedImage() != null) {
            writeRenderedImage(gc.getRenderedImage(), "postAffine");
        }
        return gc;
    }

    /**
     * Turns the coverage into a rendered image applying the necessary transformations and the
     * symbolizer
     *
     * @return The transformed image, or null if the coverage does not lie within the rendering
     *     bounds
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
        logCoverages("read", coverages);

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
        coverages =
                GridCoverageRendererUtilities.forceToValidBounds(
                        coverages, handler, bgValues, destinationCRS, hints);
        logCoverages("cropped", coverages);

        // reproject if needed
        Hints warpAffineHints =
                coverages.isEmpty() ? hints : getReprojectionHints(hints, coverages.get(0));
        List<GridCoverage2D> reprojectedCoverages =
                GridCoverageRendererUtilities.reproject(
                        coverages,
                        destinationCRS,
                        interpolation,
                        destinationEnvelope,
                        bgValues,
                        gridCoverageFactory,
                        warpAffineHints);
        logCoverages("reprojected", reprojectedCoverages);

        // displace them if needed via a projection handler
        List<GridCoverage2D> displacedCoverages =
                GridCoverageRendererUtilities.displace(
                        reprojectedCoverages,
                        handler,
                        destinationEnvelope,
                        sourceCRS,
                        targetCRS,
                        gridCoverageFactory);

        GridCoverageRendererUtilities.removeNotIntersecting(
                displacedCoverages, destinationEnvelope);
        logCoverages("displaced", displacedCoverages);

        // symbolize each bit (done here to make sure we can perform the warp/affine reduction)
        List<GridCoverage2D> symbolizedCoverages = new ArrayList<>();
        if (finalSymbolizer != null) {
            for (GridCoverage2D displaced : displacedCoverages) {
                GridCoverage2D symbolized =
                        symbolize(displaced, finalSymbolizer, bgValues, warpAffineHints);
                if (symbolized != null) {
                    symbolizedCoverages.add(symbolized);
                }
            }
        } else if (!coverages.isEmpty()
                && !CRS.equalsIgnoreMetadata(
                        coverages.get(0).getCoordinateReferenceSystem2D(), destinationCRS)) {
            // do the affine step to allow warp/affine merging, in order to best preserve rotations
            // in the warp in case of oversampling
            for (GridCoverage2D displaced : displacedCoverages) {
                final GridCoverage2D affined =
                        affine(displaced, bgValues, symbolizer, warpAffineHints);
                if (affined != null) {
                    symbolizedCoverages.add(affined);
                }
            }
        } else {
            symbolizedCoverages.addAll(displacedCoverages);
        }

        logCoverages("symbolized", symbolizedCoverages);

        // Parameters used for taking into account an optional removal of the alpha band
        // and an optional reindexing after color expansion

        // if more than one coverage, mosaic
        GridCoverage2D mosaicked =
                GridCoverageRendererUtilities.mosaicSorted(
                        symbolizedCoverages, destinationEnvelope, bgValues, this.hints);

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
     * Computes the reprojection tolerance considering an eventual oversampling. In case of
     * oversampling, the reprojection tolerance needs to be altered, because pixels will be expanded
     * by an affine transform after reprojection, magnifying the linearization error.
     */
    private Hints getReprojectionHints(Hints hints, GridCoverage2D gridCoverage2D) {
        if (gridCoverage2D == null) return hints;

        // reprojection is done without a target GG, the resampling machinery will preserve
        // the pixel structure and go towards the target envelope
        final GridGeometry2D sourceGG = gridCoverage2D.getGridGeometry();
        final GridEnvelope targetGR = sourceGG.getGridRange2D();
        final GridGeometry2D targetGG = new GridGeometry2D(targetGR, this.destinationEnvelope);
        MathTransform targetMT = targetGG.getGridToCRS(PixelOrientation.UPPER_LEFT);

        // should always be an affine, but best be ready for alternatives
        if (!(targetMT instanceof AffineTransform2D)) {
            LOGGER.log(
                    Level.FINE,
                    "Cannot check if oversampling is happening, the grid to CRS "
                            + "transformation is not an Affine2D: {0}",
                    targetMT);
            return hints;
        }

        // compute the scale factors
        AffineTransform2D targetAT = (AffineTransform2D) targetMT;
        double scaleX = Math.abs(targetAT.getScaleX() / finalGridToWorld.getScaleX());
        double scaleY = Math.abs(targetAT.getScaleY() / finalGridToWorld.getScaleY());
        double scale = Math.max(scaleX, scaleY);
        if (scale <= 1) return hints;

        // oversampling detected... tried to just reduce the tolerance based on the scale, but
        // small artifacts kept on creeping on at high oversampling factors. Settled for no
        // linearization at all instead, just use the full math on the few pixels that need
        // to be reprojected instead
        Hints result = new Hints(hints);
        result.put(Hints.RESAMPLE_TOLERANCE, 0d);
        return result;
    }

    private void logCoverages(String name, List<GridCoverage2D> coverages) {
        if (LOGGER.isLoggable(Level.FINE)) {
            String message =
                    "GridCoverageRenderer coverages: " + name + "\n" + coverages == null
                            ? "none"
                            : coverages.stream()
                                    .map(c -> c.toString())
                                    .collect(Collectors.joining(","));
            LOGGER.log(Level.FINE, message);
        }
    }

    /**
     * Paint this grid coverage. The caller must ensure that <code>graphics</code> has an affine
     * transform mapping "real world" coordinates in the coordinate system given by {@link
     * #getCoordinateSystem}.
     *
     * @param graphics the {@link Graphics2D} context in which to paint.
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
        Parameter<int[]> bandIndicesParam =
                (Parameter<int[]>) AbstractGridFormat.BANDS.createValue();
        bandIndicesParam.setValue(bandIndices);
        List<GeneralParameterValue> paramList = new ArrayList<>();
        if (readParams != null) {
            paramList.addAll(Arrays.asList(readParams));
        }
        paramList.add(bandIndicesParam);
        return paramList.toArray(new GeneralParameterValue[paramList.size()]);
    }

    /**
     * Takes into account that the band selection has been delegated down to the reader by producing
     * a new channel selection
     */
    public static RasterSymbolizer setupSymbolizerForBandsSelection(RasterSymbolizer symbolizer) {
        ChannelSelection selection = symbolizer.getChannelSelection();
        SelectedChannelType[] originalChannels = selection.getRGBChannels();
        if (originalChannels == null && selection.getGrayChannel() != null) {
            originalChannels = new SelectedChannelType[] {selection.getGrayChannel()};
        }
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

    /** Checks if band selection is present, and can be delegated down to the reader */
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
