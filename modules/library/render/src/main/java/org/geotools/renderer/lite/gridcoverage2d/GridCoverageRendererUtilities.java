/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2015, Open Source Geospatial Foundation (OSGeo)
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
import it.geosolutions.jaiext.scale.Scale2OpImage;
import it.geosolutions.jaiext.vectorbin.ROIGeometry;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.RenderedImage;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.jai.ImageLayout;
import javax.media.jai.Interpolation;
import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.ROI;
import org.geotools.api.coverage.grid.GridCoverage;
import org.geotools.api.coverage.grid.GridEnvelope;
import org.geotools.api.geometry.BoundingBox;
import org.geotools.api.metadata.spatial.PixelOrientation;
import org.geotools.api.parameter.ParameterValueGroup;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.operation.CoordinateOperation;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.api.referencing.operation.MathTransform2D;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.TypeMap;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.processing.CoverageProcessor;
import org.geotools.coverage.processing.operation.Crop;
import org.geotools.coverage.processing.operation.Mosaic;
import org.geotools.coverage.processing.operation.Resample;
import org.geotools.coverage.util.CoverageUtilities;
import org.geotools.geometry.GeneralBounds;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.image.ImageWorker;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.renderer.crs.ProjectionHandler;
import org.geotools.renderer.i18n.ErrorKeys;
import org.geotools.util.factory.Hints;
import org.locationtech.jts.algorithm.match.HausdorffSimilarityMeasure;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.util.AffineTransformation;

/** @author Simone Giannecchini, GeoSolutions */
public final class GridCoverageRendererUtilities {

    private static final double EPS = 1e-6;

    /** Logger. */
    private static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(GridCoverageRendererUtilities.class);

    private static final CoverageProcessor processor =
            CoverageProcessor.getInstance(new Hints(Hints.LENIENT_DATUM_SHIFT, Boolean.TRUE));

    /**
     * Checks whether the provided object is null or not. If it is null it throws an {@link IllegalArgumentException}
     * exception.
     *
     * @param source the object to check.
     * @param name the operation we are trying to run.
     */
    static void ensureNotNull(final Object source, final String name) {
        if (source == null) throw new IllegalArgumentException(MessageFormat.format(ErrorKeys.NULL_ARGUMENT_$1, name));
    }

    /**
     * Checks whether the provided source object is null or not. If it is null it throws an
     * {@link IllegalArgumentException} exception.
     *
     * @param source the object to check.
     * @param name the operation we are trying to run.
     */
    static void ensureSourceNotNull(final Object source, final String name) {
        if (source == null)
            throw new IllegalArgumentException(MessageFormat.format(ErrorKeys.SOURCE_CANT_BE_NULL_$1, name));
    }

    /** Reprojecting the input coverage using the provided parameters. */
    static GridCoverage2D resample(
            final GridCoverage2D gc,
            CoordinateReferenceSystem crs,
            final Interpolation interpolation,
            final GeneralBounds destinationEnvelope,
            final double[] bkgValues,
            final Hints hints)
            throws FactoryException {
        // paranoiac check
        assert CRS.equalsIgnoreMetadata(destinationEnvelope.getCoordinateReferenceSystem(), crs)
                || CRS.findMathTransform(destinationEnvelope.getCoordinateReferenceSystem(), crs)
                        .isIdentity();

        final ParameterValueGroup param =
                processor.getOperation("Resample").getParameters().clone();
        param.parameter("source").setValue(gc);
        param.parameter("CoordinateReferenceSystem").setValue(crs);
        param.parameter("InterpolationType").setValue(interpolation);
        if (bkgValues != null) {
            param.parameter("BackgroundValues").setValue(bkgValues);
        }
        return (GridCoverage2D) ((Resample) processor.getOperation("Resample")).doOperation(param, hints);
    }

    /** Reproject the specified list of coverages */
    public static List<GridCoverage2D> reproject(
            List<GridCoverage2D> coverages,
            CoordinateReferenceSystem destinationCRS,
            final Interpolation interpolation,
            final GeneralBounds destinationEnvelope,
            final double[] bkgValues,
            GridCoverageFactory gridCoverageFactory,
            final Hints hints)
            throws FactoryException {
        List<GridCoverage2D> reprojectedCoverages = new ArrayList<>();
        for (GridCoverage2D coverage : coverages) {
            if (coverage == null) {
                continue;
            }
            final CoordinateReferenceSystem coverageCRS = coverage.getCoordinateReferenceSystem();
            if (!CRS.isEquivalent(coverageCRS, destinationCRS)) {
                GridCoverage2D reprojected = reproject(
                        coverage,
                        destinationCRS,
                        interpolation,
                        destinationEnvelope,
                        bkgValues,
                        gridCoverageFactory,
                        hints);
                if (reprojected != null) {
                    reprojectedCoverages.add(reprojected);
                }
            } else {
                reprojectedCoverages.add(coverage);
            }
        }
        return reprojectedCoverages;
    }

    /** Reproject a coverage to the specified destination, eventually adding a ROI if missing */
    public static GridCoverage2D reproject(
            GridCoverage2D coverage,
            CoordinateReferenceSystem destinationCRS,
            final Interpolation interpolation,
            final GeneralBounds destinationEnvelope,
            final double[] bkgValues,
            GridCoverageFactory gridCoverageFactory,
            final Hints hints)
            throws FactoryException {

        // always have a ROI to account for pixels outside the image
        coverage = addRoiIfMissing(coverage, gridCoverageFactory);
        coverage = resample(coverage, destinationCRS, interpolation, destinationEnvelope, bkgValues, hints);
        if (LOGGER.isLoggable(Level.FINE)) LOGGER.fine("Reprojecting to crs " + destinationCRS.toString());

        return coverage;
    }

    /** Forces adding ROI to the coverage in case it's missing. It will use the renderer image footprint. */
    private static GridCoverage2D addRoiIfMissing(GridCoverage2D coverage, GridCoverageFactory gridCoverageFactory) {
        RenderedImage input = coverage.getRenderedImage();
        Object roiObject = input.getProperty("ROI");
        Object gcRoiObject = coverage.getProperty("GC_ROI");
        if (!(roiObject instanceof ROI) && !(gcRoiObject instanceof ROI)) {
            Envelope env = new Envelope(
                    input.getMinX(),
                    input.getMinX() + input.getWidth(),
                    input.getMinY(),
                    input.getMinY() + input.getHeight());
            ROI roi = new ROI(new ROIGeometry(JTS.toGeometry(env)).getAsImage());
            PlanarImage pi = PlanarImage.wrapRenderedImage(input);
            pi.setProperty("ROI", roi);
            @SuppressWarnings("unchecked")
            final Map<String, Object> sourceProperties = coverage.getProperties();
            Map<String, Object> properties =
                    sourceProperties == null ? new HashMap<>() : new HashMap<>(sourceProperties);
            properties.put("GC_ROI", roi);
            return gridCoverageFactory.create(
                    coverage.getName(),
                    pi,
                    coverage.getGridGeometry(),
                    coverage.getSampleDimensions(),
                    new GridCoverage2D[] {coverage},
                    properties);
        } else {
            return coverage;
        }
    }

    /** Cropping the provided coverage to the requested geographic area. */
    static GridCoverage2D coverageCrop(
            GridCoverage2D gc, GeneralBounds envelope, double[] background, final Hints hints) {
        final GeneralBounds oldEnvelope = (GeneralBounds) gc.getEnvelope();
        // intersect the envelopes in order to prepare for cropping the coverage
        // down to the neded resolution
        final GeneralBounds intersectionEnvelope = new GeneralBounds(envelope);
        intersectionEnvelope.setCoordinateReferenceSystem(envelope.getCoordinateReferenceSystem());
        intersectionEnvelope.intersect(oldEnvelope);

        // Do we have something to show? After the crop I could get a null
        // coverage which would mean nothing to show.
        if (intersectionEnvelope.isEmpty()) {
            return null;
        }

        // crop
        final ParameterValueGroup param =
                processor.getOperation("CoverageCrop").getParameters().clone();
        param.parameter("source").setValue(gc);
        param.parameter("Envelope").setValue(intersectionEnvelope);
        return (GridCoverage2D) ((Crop) processor.getOperation("CoverageCrop")).doOperation(param, hints);
    }

    static GridCoverage2D displace(
            GridCoverage2D coverage, double tx, double ty, GridCoverageFactory gridCoverageFactory) {
        // let's compute the new grid geometry
        GridGeometry2D originalGG = coverage.getGridGeometry();
        GridEnvelope gridRange = originalGG.getGridRange();
        ReferencedEnvelope envelope = originalGG.getEnvelope2D();

        double minx = envelope.getMinX() + tx;
        double miny = envelope.getMinY() + ty;
        double maxx = envelope.getMaxX() + tx;
        double maxy = envelope.getMaxY() + ty;
        ReferencedEnvelope translatedEnvelope =
                new ReferencedEnvelope(minx, maxx, miny, maxy, envelope.getCoordinateReferenceSystem());

        GridGeometry2D translatedGG = new GridGeometry2D(gridRange, translatedEnvelope);

        GridCoverage2D translatedCoverage = gridCoverageFactory.create(
                coverage.getName(),
                coverage.getRenderedImage(),
                translatedGG,
                coverage.getSampleDimensions(),
                new GridCoverage2D[] {coverage},
                coverage.getProperties());
        return translatedCoverage;
    }

    /** Mosaicking the provided coverages to the requested geographic area. */
    static GridCoverage2D mosaic(
            List<GridCoverage2D> coverages,
            List<GridCoverage2D> alphas,
            GeneralBounds renderingEnvelope,
            final Hints hints,
            double[] background) {

        // setup the grid geometry
        try {
            // find the intersection between the target envelope and the coverages one
            ReferencedEnvelope targetEnvelope = ReferencedEnvelope.reference(renderingEnvelope);
            ReferencedEnvelope coveragesEnvelope = null;
            for (GridCoverage2D coverage : coverages) {
                ReferencedEnvelope re = ReferencedEnvelope.reference(coverage.getEnvelope2D());
                if (coveragesEnvelope == null) {
                    coveragesEnvelope = re;
                } else {
                    coveragesEnvelope.expandToInclude(re);
                }
            }
            targetEnvelope = new ReferencedEnvelope(
                    targetEnvelope.intersection(coveragesEnvelope), renderingEnvelope.getCoordinateReferenceSystem());
            if (targetEnvelope.isEmpty() || targetEnvelope.isNull()) {
                return null;
            }

            // Use the first one as a reference so that it will not be resampled, the others will
            // likely be
            GridCoverage2D firstCoverage = coverages.get(0);
            GridGeometry2D referenceGG = firstCoverage.getGridGeometry();
            MathTransform2D mt = referenceGG.getCRSToGrid2D();
            Rectangle rasterSpaceEnvelope =
                    CRS.transform(mt, targetEnvelope).toRectangle2D().getBounds();
            GridEnvelope2D gridRange = new GridEnvelope2D(rasterSpaceEnvelope);
            GridGeometry2D gridGeometry = new GridGeometry2D(
                    gridRange, referenceGG.getGridToCRS(), firstCoverage.getCoordinateReferenceSystem2D());

            // mosaic
            final ParameterValueGroup param =
                    processor.getOperation("Mosaic").getParameters().clone();
            param.parameter("sources").setValue(coverages);
            param.parameter("geometry").setValue(gridGeometry);

            if (background != null) {
                param.parameter(Mosaic.OUTNODATA_NAME).setValue(background);
            }
            if (!alphas.isEmpty()) {
                param.parameter(Mosaic.ALPHA_NAME).setValue(alphas);
            }
            return (GridCoverage2D) ((Mosaic) processor.getOperation("Mosaic")).doOperation(param, hints);
        } catch (Exception e) {
            throw new RuntimeException("Failed to mosaic the input coverages", e);
        }
    }

    /** */
    static GeneralBounds reprojectEnvelope(GeneralBounds inputEnvelope, final CoordinateReferenceSystem outputCRS)
            throws Exception {

        GeneralBounds outputEnvelope = null;
        CoordinateReferenceSystem inputCRS = inputEnvelope.getCoordinateReferenceSystem();
        if (!CRS.equalsIgnoreMetadata(inputCRS, outputCRS)) {
            outputEnvelope = CRS.transform(inputEnvelope, outputCRS);
            outputEnvelope.setCoordinateReferenceSystem(outputCRS);
        }
        // simple copy
        if (outputEnvelope == null) {
            outputEnvelope = new GeneralBounds(inputEnvelope);
            outputEnvelope.setCoordinateReferenceSystem(inputCRS);
        }
        return null;
    }

    /** */
    static GeneralBounds reprojectEnvelopeWithWGS84Pivot(
            GeneralBounds inputEnvelope, CoordinateReferenceSystem targetCRS) throws Exception {

        GridCoverageRendererUtilities.ensureNotNull(inputEnvelope, "destinationEnvelope");
        GridCoverageRendererUtilities.ensureNotNull(targetCRS, "coverageCRS");
        final CoordinateReferenceSystem destinationCRS = inputEnvelope.getCoordinateReferenceSystem();

        // //
        //
        // Try to convert the destination envelope in the source crs. If
        // this fails we pass through WGS84 as an intermediate step
        //
        // //
        try {
            // convert the destination envelope to the source coverage
            // native crs in order to try and crop it. If we get an error we
            // try to
            // do this in two steps using WGS84 as a pivot. This introduces
            // some erros (it usually
            // increases the envelope we want to check) but it is still
            // useful.
            CoordinateOperation operation =
                    CRS.getCoordinateOperationFactory(true).createOperation(destinationCRS, targetCRS);
            GeneralBounds output = CRS.transform(operation, inputEnvelope);
            output.setCoordinateReferenceSystem(targetCRS);
            return output;
        } catch (TransformException te) {
            // //
            //
            // Convert the destination envelope to WGS84 if needed for safer
            // comparisons later on with the original crs of this coverage.
            //
            // //
            final GeneralBounds destinationEnvelopeWGS84 =
                    GridCoverageRendererUtilities.reprojectEnvelope(inputEnvelope, DefaultGeographicCRS.WGS84);

            // //
            //
            // Convert the destination envelope from WGS84 to the source crs
            // for cropping the provided coverage.
            //
            // //
            return GridCoverageRendererUtilities.reprojectEnvelope(destinationEnvelopeWGS84, targetCRS);
        }
    }

    /** */
    public static double[] colorToArray(Color color) {
        if (color == null) {
            return null;
        }

        return new double[] {color.getRed(), color.getGreen(), color.getBlue()};
    }

    /**
     * After reprojection or displacement we could have some coverage that are completely out of the destination area
     * (due to numerical issues their source bbox was interesting the request area, but their reprojected version does
     * not). Cleanup the ones completely out.
     */
    public static void removeNotIntersecting(List<GridCoverage2D> coverages, GeneralBounds destinationEnvelope) {
        for (Iterator<GridCoverage2D> it = coverages.iterator(); it.hasNext(); ) {
            GridCoverage2D coverage = it.next();
            ReferencedEnvelope re = ReferencedEnvelope.reference(coverage.getEnvelope2D());
            if (!destinationEnvelope.intersects(re, false)) {
                it.remove();
            }
        }
    }

    /** Displaces a list of coverages, using a ProjectionHandler to eventually split into proper envelopes. */
    public static List<GridCoverage2D> displace(
            List<GridCoverage2D> coverages,
            ProjectionHandler handler,
            GeneralBounds destinationEnvelope,
            CoordinateReferenceSystem sourceCRS,
            CoordinateReferenceSystem targetCRS,
            GridCoverageFactory gridCoverageFactory)
            throws FactoryException, TransformException {
        if (handler == null) {
            return coverages;
        }
        List<GridCoverage2D> displacedCoverages = new ArrayList<>();
        Envelope testEnvelope = ReferencedEnvelope.reference(destinationEnvelope);
        MathTransform mt = CRS.findMathTransform(sourceCRS, targetCRS);
        PolygonExtractor polygonExtractor = new PolygonExtractor();
        for (GridCoverage2D coverage : coverages) {
            org.locationtech.jts.geom.Polygon polygon = JTS.toGeometry((BoundingBox) coverage.getEnvelope2D());
            Geometry postProcessed = handler.postProcess(mt, polygon);
            // extract sub-polygons and displace
            List<org.locationtech.jts.geom.Polygon> polygons = polygonExtractor.getPolygons(postProcessed);
            for (Polygon displaced : polygons) {
                // check we are really inside the display area before moving one
                Envelope intersection = testEnvelope.intersection(displaced.getEnvelopeInternal());
                if (intersection == null || intersection.isNull() || intersection.getArea() == 0) {
                    continue;
                }
                if (displaced.equals(polygon)) {
                    displacedCoverages.add(coverage);
                } else {
                    double[] tx = getTranslationFactors(polygon, displaced);
                    if (tx != null) {
                        GridCoverage2D displacedCoverage =
                                GridCoverageRendererUtilities.displace(coverage, tx[0], tx[1], gridCoverageFactory);
                        displacedCoverages.add(displacedCoverage);
                    }
                }
            }
        }
        return displacedCoverages;
    }

    /**
     * Forces an input list of coverages to the valid bounds of the provided target CRS, when a reprojection is needed,
     * using a ProjectionHandler.
     */
    public static List<GridCoverage2D> forceToValidBounds(
            List<GridCoverage2D> coverages,
            ProjectionHandler handler,
            double[] bgValues,
            CoordinateReferenceSystem targetCRS,
            Hints hints) {
        // check if we have to reproject
        boolean reprojectionNeeded = false;
        for (GridCoverage2D coverage : coverages) {
            if (coverage == null) {
                continue;
            }
            final CoordinateReferenceSystem coverageCRS = coverage.getCoordinateReferenceSystem();
            if (!CRS.isEquivalent(coverageCRS, targetCRS)) {
                reprojectionNeeded = true;
                break;
            }
        }

        if (reprojectionNeeded && handler != null && handler.getValidAreaBounds() != null) {
            List<GridCoverage2D> cropped = new ArrayList<>();
            ReferencedEnvelope validArea = handler.getValidAreaBounds();
            GridGeometryReducer reducer = new GridGeometryReducer(validArea);
            for (GridCoverage2D coverage : coverages) {
                GridGeometry2D gg = coverage.getGridGeometry();
                GridGeometry2D reduced = reducer.reduce(gg);
                if (!reduced.equals(gg)) {
                    GeneralBounds cutEnvelope = reducer.getCutEnvelope(reduced);
                    GridCoverage2D croppedCoverage = crop(coverage, cutEnvelope, false, bgValues, hints);
                    if (croppedCoverage != null) {
                        cropped.add(croppedCoverage);
                    }
                } else {
                    cropped.add(coverage);
                }
            }
            coverages = cropped;
        }
        return coverages;
    }

    /** Crop a coverage on a specified destination Envelope */
    public static GridCoverage2D crop(
            GridCoverage2D coverage,
            GeneralBounds destinationEnvelope,
            boolean doReprojection,
            double[] backgroundValues,
            Hints hints) {
        // CREATING THE CROP ENVELOPE
        GridCoverage2D outputCoverage = coverage;
        final GeneralBounds coverageEnvelope = (GeneralBounds) coverage.getEnvelope();
        final CoordinateReferenceSystem coverageCRS = coverage.getCoordinateReferenceSystem2D();

        try {
            GeneralBounds renderingEnvelopeInCoverageCRS = null;
            if (doReprojection) {
                renderingEnvelopeInCoverageCRS =
                        GridCoverageRendererUtilities.reprojectEnvelopeWithWGS84Pivot(destinationEnvelope, coverageCRS);
            } else {
                // NO REPROJECTION
                renderingEnvelopeInCoverageCRS = new GeneralBounds(destinationEnvelope);
            }
            final GeneralBounds cropEnvelope = new GeneralBounds(renderingEnvelopeInCoverageCRS);
            cropEnvelope.intersect(coverageEnvelope);
            if (cropEnvelope.isEmpty() || cropEnvelope.isNull()) {
                if (LOGGER.isLoggable(Level.INFO)) {
                    LOGGER.info("The destination envelope does not intersect the envelope of the source coverage.");
                }
                return null;
            }

            // Cropping for real
            outputCoverage = coverageCrop(coverage, cropEnvelope, backgroundValues, hints);
        } catch (Throwable t) {
            // If it happens that the crop fails we try to proceed since the crop does only an
            // optimization. Things might work out anyway.
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "Crop Failed for reason: " + t.getLocalizedMessage(), t);
            }
            outputCoverage = coverage;
        }
        return outputCoverage;
    }

    /** Mosaic coverages with a preliminary sorting by size */
    public static GridCoverage2D mosaicSorted(
            List<GridCoverage2D> coverages, GeneralBounds destinationEnvelope, double[] bgValues, Hints hints) {
        GridCoverage2D mosaicked = null;
        if (coverages.isEmpty()) {
            return null;
        } else if (coverages.size() == 1) {
            mosaicked = coverages.get(0);
        } else {
            // sort the coverages by size, avoid having a sliver coverage 1-2 px large or high
            // first as it will have a skewed grid to world that will then be applied
            // to all members of the mosaic
            Comparator<GridCoverage2D> sliverComparator = (c1, c2) -> {
                RenderedImage r1 = c1.getRenderedImage();
                RenderedImage r2 = c2.getRenderedImage();
                // area2 - area1, largest first
                long areaDiff = (long) r2.getWidth() * r2.getHeight() - (long) r1.getWidth() * r1.getHeight();
                return (int) Math.signum(areaDiff);
            };
            Collections.sort(coverages, sliverComparator);

            // do not expand index color models, we know they are all the same
            Hints mosaicHints = new Hints(hints);
            mosaicHints.put(JAI.KEY_REPLACE_INDEX_COLOR_MODEL, false);
            mosaicked = mosaic(coverages, new ArrayList<>(), destinationEnvelope, mosaicHints, bgValues);
        }
        return mosaicked;
    }

    /** Apply an affineTransformation to a GridCoverage */
    public static GridCoverage2D affine(
            GridCoverage2D coverage,
            Interpolation interpolation,
            AffineTransform affineTransform,
            double[] bkgValues,
            boolean useInputSampleDimensions,
            GridCoverageFactory gridCoverageFactory,
            Hints hints) {
        final RenderedImage finalImage = coverage.getRenderedImage();
        final GridGeometry2D gridGeometry = coverage.getGridGeometry();
        // I need to translate half of a pixel since in wms the envelope map
        // to the corners of the raster space not to the center of the pixels
        final MathTransform2D finalGCTransform = gridGeometry.getGridToCRS2D(PixelOrientation.UPPER_LEFT);
        if (!(finalGCTransform instanceof AffineTransform)) {
            throw new UnsupportedOperationException("Non-affine transformations not yet implemented"); // TODO
        }
        final AffineTransform finalGCgridToWorld = new AffineTransform((AffineTransform) finalGCTransform);

        // Getting NOData and ROI
        Range noData = CoverageUtilities.getNoDataProperty(coverage) != null
                ? CoverageUtilities.getNoDataProperty(coverage).getAsRange()
                : null;
        ROI roi = CoverageUtilities.getROIProperty(coverage);

        // //
        //
        // I am going to concatenate the final world to grid transform
        // with the grid to world transform of the input coverage.
        //
        // This way i right away position the coverage at the right place in the
        // area of interest for the device.
        //
        // //
        final AffineTransform finalRasterTransformation = (AffineTransform) affineTransform.clone();
        finalRasterTransformation.concatenate(finalGCgridToWorld);

        // paranoiac check to avoid that JAI freaks out when computing its internal
        // layout on images that are too small
        ImageLayout finalLayout = Scale2OpImage.layoutHelper(
                finalImage,
                (float) Math.abs(finalRasterTransformation.getScaleX()),
                (float) Math.abs(finalRasterTransformation.getScaleY()),
                (float) finalRasterTransformation.getTranslateX(),
                (float) finalRasterTransformation.getTranslateY(),
                interpolation,
                null);
        if (finalLayout.getWidth(null) < 1 || finalLayout.getHeight(null) < 1) {
            if (LOGGER.isLoggable(java.util.logging.Level.FINE))
                LOGGER.fine("Unable to create a granuleDescriptor due to jai scale bug");
            return null;
        }

        ImageWorker iw = new ImageWorker(finalImage);
        iw.setRenderingHints(hints);
        iw.setROI(roi);
        iw.setNoData(noData);
        iw.affine(finalRasterTransformation, interpolation, bkgValues);
        RenderedImage im = iw.getRenderedImage();
        roi = iw.getROI();
        noData = iw.extractNoDataProperty(im);

        // recreate gridCoverage
        int numBands = im.getSampleModel().getNumBands();
        GridSampleDimension[] sd = new GridSampleDimension[numBands];
        for (int i = 0; i < numBands; i++) {
            sd[i] = new GridSampleDimension(
                    useInputSampleDimensions
                            ? coverage.getSampleDimension(i).getDescription()
                            : TypeMap.getColorInterpretation(im.getColorModel(), i)
                                    .name());
        }

        @SuppressWarnings("unchecked")
        Map<String, Object> properties = coverage.getProperties();
        if (properties == null) {
            properties = new HashMap<>();
        }
        CoverageUtilities.setNoDataProperty(properties, noData);
        CoverageUtilities.setROIProperty(properties, roi);

        // create a new grid coverage but preserve as much input as possible
        return gridCoverageFactory.create(
                coverage.getName(),
                im,
                new GridGeometry2D(
                        new GridEnvelope2D(PlanarImage.wrapRenderedImage(im).getBounds()), coverage.getEnvelope()),
                sd,
                new GridCoverage[] {coverage},
                properties);
    }

    private static double[] getTranslationFactors(Polygon reference, Polygon displaced) {
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
}
