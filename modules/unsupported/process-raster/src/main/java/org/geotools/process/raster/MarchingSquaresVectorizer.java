/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.process.raster;

import it.geosolutions.jaiext.lookup.LookupTable;
import it.geosolutions.jaiext.lookup.LookupTableFactory;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.DataBuffer;
import java.awt.image.RenderedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;
import java.util.logging.Level;
import javax.media.jai.ImageLayout;
import javax.media.jai.JAI;
import javax.media.jai.ROIShape;
import javax.media.jai.iterator.RandomIter;
import javax.media.jai.iterator.RandomIterFactory;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.LiteShape2;
import org.geotools.image.DisposeStopper;
import org.geotools.image.DrawableBitSet;
import org.geotools.image.ImageWorker;
import org.geotools.image.util.ColorUtilities;
import org.geotools.image.util.ImageUtilities;
import org.geotools.referencing.CRS;
import org.geotools.referencing.operation.LinearTransform;
import org.geotools.referencing.operation.transform.ConcatenatedTransform;
import org.geotools.referencing.operation.transform.ProjectiveTransform;
import org.geotools.util.Range;
import org.geotools.util.Utilities;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.simplify.TopologyPreservingSimplifier;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.datum.PixelInCell;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

/**
 * Algorithm computing the image footprint. Some optimizations are made. When running along the
 * image perimeter, whenever a closed polygon is found, the inner area is filled to make sure we
 * won't analyze that.
 *
 * <p>It can deal with {@link GridCoverage2D} instances, returning footprint in real world
 * coordinates, as well as with {@link RenderedImage} instances, returning footprint in raster space
 * coordinates
 *
 * @author Daniele Romagnoli, GeoSolutions SAS
 * @author Simone Giannecchini, GeoSolutions SAS
 * @author Andrea Aime, GeoSolutions SAS
 */
public final class MarchingSquaresVectorizer {

    private static final double D_VALUE = 0d;

    private static final int I_VALUE = (int) D_VALUE;

    private static final double MAX_8BIT_VALUE = 255.0;

    private static final double INVALID_PIXEL_D = 255.0;

    private static final int INVALID_PIXEL_I = 255;

    private static final GeometryFactory GF = new GeometryFactory();

    private static final Geometry EMPTY_GEOMETRY = GF.createMultiPolygon(new Polygon[0]);

    private static final double MIN_AREA_TO_BE_SIMPLIFIED = 1000.0d;

    private static final double DELTA = 1E-6d;

    private static final int NO_SRID = -1;

    private static final int POSITIVE_STEP = +1;

    private static final int NEGATIVE_STEP = -1;

    private static final MathTransform TRANSLATED_TX =
            ProjectiveTransform.create(AffineTransform.getTranslateInstance(1, 1));

    private static final java.util.logging.Logger LOGGER =
            java.util.logging.Logger.getLogger("MarchingSquaresVectorizer");

    public static final List<Range<Integer>> DEFAULT_RANGES =
            Collections.singletonList(new Range<Integer>(Integer.class, 0, 0));

    public static final double DEFAULT_SIMPLIFIER_FACTOR = 2.0d;

    public static final double DEFAULT_THRESHOLD_AREA = 5.0d;

    public enum FootprintCoordinates {
        RASTER_SPACE,
        MODEL_SPACE;

        public static FootprintCoordinates getDefault() {
            return MODEL_SPACE;
        }
    }

    public enum ImageLoadingType {
        IMMEDIATE,
        DEFERRED;

        public static ImageLoadingType getDefault() {
            return IMMEDIATE;
        }
    }

    /** Main image properties holder for fields compaction. */
    static class ImageProperties {
        int height;

        int width;

        int minX;

        int minY;

        int maxX;

        int maxY;

        int minTileX;

        int minTileY;

        int maxTileX;

        int maxTileY;

        int tileWidth;

        int tileHeight;

        public void init(HashMap<String, Double> regionMap, RenderedImage inputRI) {
            height = regionMap.get(CoverageUtilities.ROWS).intValue();
            width = regionMap.get(CoverageUtilities.COLS).intValue();
            minX = regionMap.get(CoverageUtilities.MINX).intValue();
            minY = regionMap.get(CoverageUtilities.MINY).intValue();
            maxX = minX + width - 1;
            maxY = minY + height - 1;
            initTiling(inputRI);
        }

        private void initTiling(RenderedImage inputRI) {
            tileWidth = Math.min(inputRI.getTileWidth(), width);
            tileHeight = Math.min(inputRI.getTileHeight(), height);

            minTileX = minX / tileWidth - (minX < 0 ? (-minX % tileWidth > 0 ? 1 : 0) : 0);
            minTileY = minY / tileHeight - (minY < 0 ? (-minY % tileHeight > 0 ? 1 : 0) : 0);
            maxTileX = maxX / tileWidth - (maxX < 0 ? (-maxX % tileWidth > 0 ? 1 : 0) : 0);
            maxTileY = maxY / tileHeight - (maxY < 0 ? (-maxY % tileHeight > 0 ? 1 : 0) : 0);
        }

        public void init(RenderedImage inputRI) {
            height = inputRI.getHeight();
            width = inputRI.getWidth();
            minX = inputRI.getMinX();
            minY = inputRI.getMinY();
            maxX = minX + width - 1;
            maxY = minY + height - 1;
            initTiling(inputRI);
        }
    }

    /** The gridCoverage we want to scan */
    private GridCoverage2D inGeodata;

    private RenderingHints hints;

    /**
     * A stack containing images we need to dispose. We dispose them at the end of the processing to
     * avoid cache thrashing
     */
    private Stack<RenderedImage> imagesStack = new Stack<RenderedImage>();

    /** Simple image properties holder to have all imageProperties grouped into a single place */
    private ImageProperties imageProperties = new ImageProperties();

    /** An helper class used to store some information acquired during scan */
    static class ScanInfo {
        /**
         * A trigger which is internally enabled when no polygons having area greater than the
         * threshold have been found. In that case, the algorithm will return the first polygon
         * found.
         */
        boolean takeFirst = false;

        /** Reports that at least a polygon has been found */
        boolean firstFound = false;

        /** Reference positions of the first polygon */
        int refColumn = Integer.MIN_VALUE;

        int refRow = Integer.MIN_VALUE;

        /** Reports that the scan area fully contains valid pixels */
        boolean fullyCovered = false;

        /** Reports that the scan area fully contains invalid pixels */
        boolean fullyInvalid = false;

        private void isFullyInvalid(
                List<Polygon> geometriesList, RenderedImage inputRI, RenderingHints localHints)
                throws Exception {
            if (geometriesList.size() == 0) {
                // Must be a fully "invalid-Pixels" image, or an error occurred
                ImageWorker w = new ImageWorker(inputRI);

                final double[] extrema = w.getMinimums();

                if (!areEqual(extrema[0], INVALID_PIXEL_D)) {
                    Exception ex = new Exception("Unknown MarchingSquares processing error");
                    ImageAnalysisResultThdLocal.set(ex);
                    throw ex;
                }
                fullyInvalid = true;
            }
        }
    }

    /** The simplifier factor to be used when computing the simplified version of the footprint */
    private double simplifierFactor = DEFAULT_SIMPLIFIER_FACTOR;

    /** The area of the simplified polygon */
    private double polygonArea;

    /** A reference area. Polygons having area smaller than the threshold will be discarded */
    private double thresholdArea;

    private double xRes;

    private double yRes;

    /** The gridGeometry of the input GridCoverage (if any) */
    private GridGeometry2D gridGeometry;

    /** The simplified version of the footprint */
    private Geometry simplifiedFootprint;

    /** The resulting footprint */
    private Geometry footprint;

    /** An ROIShape version of the footprint, in raster coordinates */
    private ROIShape roiShape;

    /** The input renderedImage (if any) */
    private RenderedImage inputRenderedImage;

    /** true in case the biggest polygon should be simplified */
    private boolean computeSimplifiedFootprint = true;

    /** true in case collinear points should be removed */
    private boolean removeCollinear = true;

    /** true in case forcing polygon validity should be applied */
    private boolean forceValid = true;

    /** A DrawableBitSet to fill the area contained within an already scan perimeter. */
    private DrawableBitSet bitSet;

    /** The coordinate reference system of the underlying gridCoverage */
    private CoordinateReferenceSystem crs;

    /** The type of imageLoading to be used (DEFERRED vs IMMEDIATE) */
    private ImageLoadingType imageLoadingType;

    /**
     * Ranges of values to be excluded from the valid polygon search. MarchingSquare is born to
     * extract polygons from NOT-ZERO pixels. For RGB images we compute luminance and we work on
     * NOT-ZERO luminance. The exclusion range allows to specify different range of values to be
     * excluded. This may be helpful when you want to exclude "Dark" pixels from the output, where
     * "Dark" means having a luminance between 0 and a reference value (As an instance: 10) as well
     * as "white pixels" such as clouds (adding a range like 254, 255).
     */
    private List<Range<Integer>> exclusionLuminanceRanges = DEFAULT_RANGES;

    /** Specifies if we want footprint in model coordinates or raster coordinates */
    private FootprintCoordinates footprintCoordinates = FootprintCoordinates.getDefault();

    /**
     * Main Constructor using {@link GridCoverage2D} as input.
     *
     * @param inGeodata the input {@link GridCoverage2D}
     * @param hints hints to be used by inner processing, it usually contains tile caches,
     *     schedulers
     * @param thresholdArea the minimum area required by a polygon to be included in the result
     * @param simplifierFactor the simplifier factor to be applied to compute the simplified version
     *     of the biggest polygon.
     * @param imageLoadingType the type of imageLoading (DEFERRED vs IMMEDIATE).
     * @param exclusionLuminanceRanges the ranges of luminance values to be excluded by the
     *     computation.
     */
    public MarchingSquaresVectorizer(
            GridCoverage2D inGeodata,
            RenderingHints hints,
            double thresholdArea,
            double simplifierFactor,
            ImageLoadingType imageLoadingType,
            List<Range<Integer>> exclusionLuminanceRanges) {
        this.inGeodata = inGeodata;
        this.thresholdArea = thresholdArea;
        this.simplifierFactor = simplifierFactor;
        this.exclusionLuminanceRanges = exclusionLuminanceRanges;

        RenderingHints localHints = (hints != null) ? (RenderingHints) hints.clone() : null;
        if ((localHints != null) && localHints.containsKey(JAI.KEY_IMAGE_LAYOUT)) {
            Object l = localHints.get(JAI.KEY_IMAGE_LAYOUT);
            if ((l != null) && (l instanceof ImageLayout)) {
                final ImageLayout layout = (ImageLayout) ((ImageLayout) l).clone();
                localHints.put(JAI.KEY_IMAGE_LAYOUT, layout);
            }
        }
        this.hints = localHints;
        this.imageLoadingType = imageLoadingType;
    }

    /**
     * Main Constructor using {@link RenderedImage} as input. Returned footprint coordinates will be
     * in raster space.
     *
     * @param ri the input {@link RenderedImage}
     * @param hints hints to be used by inner processing, it usually contains tile caches,
     *     schedulers
     * @param thresholdArea the minimum area required by a polygon to be included in the result
     * @param imageLoadingType the type of imageLoading (DEFERRED vs IMMEDIATE).
     * @param exclusionLuminanceRanges the range of luminance values to be excluded by the
     *     computation.
     */
    public MarchingSquaresVectorizer(
            final RenderedImage ri,
            final RenderingHints hints,
            final double thresholdArea,
            ImageLoadingType imageLoadingType,
            final List<Range<Integer>> exclusionLuminanceRanges) {
        this.inputRenderedImage = ri;
        this.footprintCoordinates = FootprintCoordinates.RASTER_SPACE;
        this.computeSimplifiedFootprint = false;
        this.thresholdArea = thresholdArea;
        this.exclusionLuminanceRanges = exclusionLuminanceRanges;

        RenderingHints localHints = (hints != null) ? (RenderingHints) hints.clone() : null;
        if ((localHints != null) && localHints.containsKey(JAI.KEY_IMAGE_LAYOUT)) {
            Object l = localHints.get(JAI.KEY_IMAGE_LAYOUT);
            if ((l != null) && (l instanceof ImageLayout)) {
                final ImageLayout layout = (ImageLayout) ((ImageLayout) l).clone();
                localHints.put(JAI.KEY_IMAGE_LAYOUT, layout);
            }
        }
        this.hints = localHints;
        this.imageLoadingType = imageLoadingType;
    }

    public MarchingSquaresVectorizer() {}

    /** Return the ROIShape version of the footprint after computation */
    public ROIShape getRoiShape() {
        return roiShape;
    }

    /** Return the area of the simplified footprint after computation */
    public double getPolygonArea() {
        return polygonArea;
    }

    /** Return the simplified footprint */
    public Geometry getSimplifiedFootprint() {
        return simplifiedFootprint;
    }

    /** Return the precise footprint */
    public Geometry getFootprint() {
        return footprint;
    }

    /** When set to {@code true} (the default) will perform collinear vertices removal */
    public void setRemoveCollinear(boolean removeCollinear) {
        this.removeCollinear = removeCollinear;
    }

    /**
     * When set to {@code true} (the default) will perform extra checks on the output polygons to
     * make sure they are valid geometries
     */
    public void setForceValid(boolean forceValid) {
        this.forceValid = forceValid;
    }

    /** When set to {@code true}, a simplified version of the footprint will be returned too */
    public void setComputeSimplifiedFootprint(boolean computeSimplifiedFootprint) {
        this.computeSimplifiedFootprint = computeSimplifiedFootprint;
    }

    /**
     * Specifies which type of imageLoading ({@link ImageLoadingType}) to be used, {@link
     * ImageLoadingType#DEFERRED} vs {@link ImageLoadingType#IMMEDIATE}
     */
    public void setImageLoadingType(ImageLoadingType imageLoadingType) {
        this.imageLoadingType = imageLoadingType;
    }

    public ImageLoadingType getImageLoadingType() {
        return imageLoadingType;
    }

    /** Executes the MarchingSquares algorithm to find the footprint. */
    public void process() throws Exception {
        int sampleDataType = -1;
        RandomIter iter = null;
        RenderedImage inputRI = null;

        try {
            if (inGeodata != null) {
                inputRI = inGeodata.getRenderedImage();
            } else {
                inputRI = inputRenderedImage;
            }

            // SG14112011
            // wrap the input renderedimage so that we cannot release it
            inputRI = new DisposeStopper(inputRI);

            // setting up hints
            final ImageLayout layout = new ImageLayout(inputRI);
            RenderingHints localHints = new RenderingHints(JAI.KEY_IMAGE_LAYOUT, layout);
            if (hints != null) {
                localHints.add(hints);
            }

            inputRI = computeLuminance(inputRI, localHints);

            // Prepare data to find inner area by binarizing and inverting so that if input is
            // in the exclusion range become 255, otherwise it gets turned into 0 which is
            // the value we are looking for
            inputRI = prepareMaskingLookup(inputRI, localHints);

            imagesStack.push(inputRI);
            iter = RandomIterFactory.create(inputRI, null);

            if (inGeodata != null) {
                HashMap<String, Double> regionMap =
                        CoverageUtilities.getRegionParamsFromGridCoverage(inGeodata);
                imageProperties.init(regionMap, inputRI);
                xRes = regionMap.get(CoverageUtilities.XRES);
                yRes = regionMap.get(CoverageUtilities.YRES);
                gridGeometry = inGeodata.getGridGeometry();
                crs = inGeodata.getCoordinateReferenceSystem2D();
            } else {
                imageProperties.init(inputRI);
            }

            sampleDataType = inputRI.getSampleModel().getDataType();
            bitSet = new DrawableBitSet(imageProperties.width, imageProperties.height);

            List<Polygon> geometriesList = new ArrayList<Polygon>();

            final ScanInfo scanInfo = new ScanInfo();
            identifyGeometries(iter, sampleDataType, geometriesList, scanInfo);
            scanInfo.isFullyInvalid(geometriesList, inputRI, localHints);

            // Geometries validation
            geometriesList = validateGeometries(geometriesList);
            if (noGeometries(geometriesList, scanInfo.fullyInvalid)) {
                return;
            }

            // Setup transformation to provide coordinates in the requested space
            // (MODEL_SPACE vs RASTER_SPACE)
            MathTransform transform = null;
            if (footprintCoordinates == FootprintCoordinates.MODEL_SPACE) {
                transform = gridGeometry.getGridToCRS(PixelInCell.CELL_CORNER);

                LinearTransform translation = ProjectiveTransform.createTranslation(2, 1);
                transform = ConcatenatedTransform.create(translation, transform);
            }

            double area = 0;
            int polygonIndex = 0;
            int index = 0;

            // Looking for the biggest polygon
            for (Polygon polygon : geometriesList) {
                double polygonArea = polygon.getArea();
                if (polygonArea > area) {
                    polygonIndex = index;
                    area = polygonArea;
                }
                index++;
            }

            // //
            //
            // Compute the footprint
            //
            // //
            computeFootprint(geometriesList, transform);

            // //
            //
            // Compute the simplified footprint if needed, using the biggest one
            //
            // //
            computeSimplifiedFootprint(geometriesList, transform, polygonIndex, area);
        } catch (Exception ex) {
            ImageAnalysisResultThdLocal.set(ex);
            throw ex;
        } finally {
            // release iterator
            if (iter != null) {
                iter.done();
                iter = null;
            }
        }
    }

    /** Compute the I (Luminance) component of (HSI) from the RGB image */
    private RenderedImage computeLuminance(RenderedImage inputRI, RenderingHints localHints) {
        int numBands = inputRI.getSampleModel().getNumBands();
        final int tr = inputRI.getColorModel().getTransparency();

        if (numBands != 1) {
            ImageWorker worker = new ImageWorker(inputRI).setRenderingHints(localHints);
            if (numBands == 3) {
                worker.bandCombine(ImageUtilities.RGB_TO_GRAY_MATRIX);
            } else {
                // do we have transparency combination matrix

                final double fillValue =
                        (tr == Transparency.OPAQUE) ? (1.0 / numBands) : (1.0 / (numBands - 1));
                final double[][] matrix = new double[1][numBands + 1];
                for (int i = 0; i < numBands; i++) {
                    matrix[0][i] = fillValue;
                }
                worker.bandCombine(matrix);
            }
            inputRI = worker.getRenderedImage();
            imagesStack.push(worker.getRenderedImage());
            numBands = inputRI.getSampleModel().getNumBands();
            assert numBands == 1;
        }

        // fix imagelayout to set gray color model
        final ImageLayout layout2 = (ImageLayout) localHints.get(JAI.KEY_IMAGE_LAYOUT);
        layout2.setColorModel(ColorUtilities.GRAY_CM);
        layout2.setSampleModel(
                ColorUtilities.GRAY_CM.createCompatibleSampleModel(
                        inputRI.getTileWidth(), inputRI.getTileHeight()));

        return inputRI;
    }

    /**
     * Check if the provided geometries list is empty. In case the reference raster doesn't contain
     * any valid points (isAllZeros), then return an empty GeometryCollection
     */
    private boolean noGeometries(final List<Polygon> geometriesList, final boolean isAllZeros) {
        if (geometriesList.size() == 0) {
            if (isAllZeros) {
                footprint = EMPTY_GEOMETRY;
                simplifiedFootprint = EMPTY_GEOMETRY;
            } else {
                simplifiedFootprint = null;
                footprint = null;
            }

            return true;
        }
        return false;
    }

    /**
     * If validation is requested, scan the geometries and build valid polygons (in case they
     * aren't) by also removing holes.
     */
    private List<Polygon> validateGeometries(List<Polygon> geometriesList) {
        if (forceValid && (geometriesList.size() > 0)) {
            List<Polygon> validated = new ArrayList<Polygon>(geometriesList.size());
            for (int i = 0; i < geometriesList.size(); i++) {
                Polygon polygon = geometriesList.get(i);
                if (!polygon.isValid()) {
                    List<Polygon> validPolygons = JTS.makeValid(polygon, true);
                    validated.addAll(validPolygons);
                } else {
                    validated.add(polygon);
                }
            }
            geometriesList = validated;
        }
        return geometriesList;
    }

    /**
     * Compute the footprint.
     *
     * @param geometriesList the List of all the geometries found across the dataset
     */
    private void computeFootprint(List<Polygon> geometriesList, MathTransform transform)
            throws MismatchedDimensionException, TransformException, FactoryException {
        // Creating the final multipolygon
        Polygon[] polArray = new Polygon[geometriesList.size()];
        Polygon[] polygons = geometriesList.toArray(polArray);
        final Geometry innerGeometry = new MultiPolygon(polygons, GF);

        if (footprintCoordinates == FootprintCoordinates.MODEL_SPACE) {
            this.footprint = JTS.transform(innerGeometry, transform);
        } else {
            this.footprint = innerGeometry;
            innerGeometry.setSRID(NO_SRID);
        }
        // Compute the ROIShape
        if (!innerGeometry.isEmpty()) {
            LiteShape2 shape = new LiteShape2(innerGeometry, TRANSLATED_TX, null, false);
            roiShape = (ROIShape) new ROIShape(shape);
        }
    }

    /**
     * Compute the simplified version of the footprint, starting from a specific polygon.
     *
     * @param geometriesList the list of available geometries
     * @param polygonIndex the index of the polygon to be simplified.
     * @param area the area of the reference polygon.
     */
    private void computeSimplifiedFootprint(
            final List<Polygon> geometriesList,
            final MathTransform transform,
            final int polygonIndex,
            final double area)
            throws MismatchedDimensionException, TransformException {
        // Looking for the bigger polygon
        if (computeSimplifiedFootprint && !geometriesList.isEmpty()) {
            Geometry simplifiedFootprintGeometry = geometriesList.get(polygonIndex);
            Geometry finalSimplifiedFootprint = null;
            if (footprintCoordinates == FootprintCoordinates.MODEL_SPACE) {
                finalSimplifiedFootprint = JTS.transform(simplifiedFootprintGeometry, transform);
            } else {
                simplifiedFootprintGeometry.setSRID(NO_SRID);
                finalSimplifiedFootprint = simplifiedFootprintGeometry;
            }

            this.polygonArea = finalSimplifiedFootprint.getArea();

            // reduce
            final double tolerance = Math.max(xRes, yRes) * simplifierFactor;

            // Avoid simplification on small polygons
            simplifiedFootprintGeometry =
                    (area > MIN_AREA_TO_BE_SIMPLIFIED)
                            ? TopologyPreservingSimplifier.simplify(
                                    finalSimplifiedFootprint, tolerance)
                            : finalSimplifiedFootprint;

            if (simplifiedFootprintGeometry == null) {
                throw new IllegalStateException("No simplified Footprint can be computed");
            }

            // proceed
            try {
                final Integer srid = CRS.lookupEpsgCode(crs, false);
                if (footprintCoordinates == FootprintCoordinates.MODEL_SPACE) {
                    if (srid != null) {
                        simplifiedFootprintGeometry.setSRID(srid);
                    }
                } else {
                    simplifiedFootprintGeometry.setSRID(NO_SRID);
                }
            } catch (FactoryException fe) {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.fine("Unable to lookup an EPSG code for the provided CRS");
                }
            }

            this.simplifiedFootprint = simplifiedFootprintGeometry;

        } else {
            simplifiedFootprint = null;
        }
    }

    private void identifyGeometries(
            final RandomIter iter,
            final int sampleDataType,
            final List<Polygon> geometriesList,
            ScanInfo scanInfo)
            throws TransformException {

        // Preliminar check
        if (sampleDataType == DataBuffer.TYPE_DOUBLE) {
            scanInfo.fullyCovered = checkFullyCovered(iter, D_VALUE, geometriesList);
        } else if (sampleDataType == DataBuffer.TYPE_BYTE) {
            scanInfo.fullyCovered = checkFullyCovered(iter, I_VALUE, geometriesList);
        }
        boolean firstRef = true;
        java.awt.Polygon awtPolygon = new java.awt.Polygon();

        // Looking for polygons
        // To a tile based scan here, the iterator keeps a strong reference to a tile, we want
        // to exhaust it fully before moving on to the next tile instead of doing a simple row
        // based scan, which only requires two loops, but throws away a tile after consuming just
        // one row out of it (over large rasters it's unlikely that we'll have the old tiles
        // still in the cache once we finished scanning the first row and moved to the next one)

        if (!scanInfo.fullyCovered) {
            // Initialize search area
            final int minX = imageProperties.minX;
            final int minY = imageProperties.minY;
            final int maxX = imageProperties.maxX;
            final int maxY = imageProperties.maxY;
            final int minTileY = imageProperties.minTileY;
            final int minTileX = imageProperties.minTileX;
            final int maxTileY = imageProperties.maxTileY;
            final int maxTileX = imageProperties.maxTileX;
            final int tileWidth = imageProperties.tileWidth;
            final int tileHeight = imageProperties.tileHeight;

            if (sampleDataType == DataBuffer.TYPE_DOUBLE) {
                for (int tileY = minTileY; tileY <= maxTileY; tileY++) {
                    for (int tileX = minTileX; tileX <= maxTileX; tileX++) {
                        for (int trow = 0; trow < tileHeight; trow++) {
                            int row = (tileY * tileHeight) + trow;
                            if ((row >= minY) && (row <= maxY)) {
                                for (int tcol = 0; tcol < tileWidth; tcol++) {
                                    int col = (tileX * tileWidth) + tcol;
                                    if ((col >= minX) && (col <= maxX)) {
                                        double value = iter.getSampleDouble(col, row, 0);
                                        if (!bitSet.get(col - minX, row - minY)
                                                && !Double.isNaN(value)) {
                                            if (areEqual(value, D_VALUE)) {
                                                Polygon polygon =
                                                        identifyPerimeter(
                                                                iter,
                                                                col,
                                                                row,
                                                                awtPolygon,
                                                                sampleDataType,
                                                                scanInfo);
                                                if (polygon != null) {
                                                    if (removeCollinear) {
                                                        if (LOGGER.isLoggable(Level.FINE)) {
                                                            LOGGER.fine(
                                                                    "Removing collinear points");
                                                        }
                                                        polygon =
                                                                (Polygon)
                                                                        JTS.removeCollinearVertices(
                                                                                polygon);
                                                    }
                                                    bitSet.set(polygon);
                                                    geometriesList.add(polygon);
                                                } else if (scanInfo.firstFound && firstRef) {
                                                    // Taking note of the coordinates of the
                                                    // first polygon found which is smaller
                                                    // than the threshold area
                                                    scanInfo.refColumn = col;
                                                    scanInfo.refRow = row;
                                                    firstRef = false;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else if (sampleDataType == DataBuffer.TYPE_BYTE) {
                for (int tileY = minTileY; tileY <= maxTileY; tileY++) {
                    for (int tileX = minTileX; tileX <= maxTileX; tileX++) {
                        for (int trow = 0; trow < tileHeight; trow++) {
                            int row = (tileY * tileHeight) + trow;
                            if ((row >= minY) && (row <= maxY)) {
                                for (int tcol = 0; tcol < tileWidth; tcol++) {
                                    int col = (tileX * tileWidth) + tcol;
                                    if ((col >= minX) && (col <= maxX)) {
                                        int value = iter.getSample(col, row, 0);
                                        if (!bitSet.get(col - minX, row - minY)) {
                                            if (value == I_VALUE) {
                                                Polygon polygon =
                                                        identifyPerimeter(
                                                                iter,
                                                                col,
                                                                row,
                                                                awtPolygon,
                                                                sampleDataType,
                                                                scanInfo);
                                                if (polygon != null) {
                                                    if (removeCollinear) {
                                                        if (LOGGER.isLoggable(Level.FINE)) {
                                                            LOGGER.fine(
                                                                    "Removing collinear points");
                                                        }
                                                        polygon =
                                                                (Polygon)
                                                                        JTS.removeCollinearVertices(
                                                                                polygon);
                                                    }
                                                    geometriesList.add(polygon);
                                                    bitSet.set(polygon);
                                                } else if (scanInfo.firstFound && firstRef) {
                                                    // Taking note of the coordinates of the
                                                    // first polygon found which is smaller
                                                    // than the threshold area
                                                    scanInfo.refColumn = col;
                                                    scanInfo.refRow = row;
                                                    firstRef = false;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (!scanInfo.fullyCovered
                && geometriesList.isEmpty()
                && (scanInfo.refColumn != Integer.MIN_VALUE)
                && (scanInfo.refColumn != Integer.MAX_VALUE)) {
            // We didn't find any polygon bigger than threshold area.
            // Let's take the first one we found, smaller than that threshold area
            scanInfo.takeFirst = true;

            Polygon polygon =
                    identifyPerimeter(
                            iter,
                            scanInfo.refColumn,
                            scanInfo.refRow,
                            awtPolygon,
                            sampleDataType,
                            scanInfo);
            geometriesList.add(polygon);
        }
    }

    /**
     * Rescale the image to byte/ushort and setup a lookup which maps valid values to zero. The
     * algorithm will indeed looks for zero (after the lookup mapping), which means valid pixels
     */
    private RenderedImage prepareMaskingLookup(RenderedImage inputRI, RenderingHints localHints) {
        final int dataType = inputRI.getSampleModel().getDataType();
        double scale = 1;
        double offset = 0;
        ImageWorker worker = new ImageWorker(inputRI);
        worker.setRenderingHints(localHints);
        if (dataType != DataBuffer.TYPE_BYTE) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine(
                        "Rescaling dynamic to fit BYTE datatype from "
                                + ImageUtilities.getDatabufferTypeName(dataType));
            }

            switch (dataType) {
                case DataBuffer.TYPE_USHORT:
                    inputRI =
                            worker.lookup(
                                            createLookupTableUShort(
                                                    exclusionLuminanceRanges, dataType))
                                    .getRenderedImage();
                    break;
                case DataBuffer.TYPE_SHORT:
                    scale = MAX_8BIT_VALUE / Short.MAX_VALUE;
                    offset = MAX_8BIT_VALUE * Short.MIN_VALUE / (Short.MIN_VALUE - Short.MAX_VALUE);
                    worker.rescale(new double[] {scale}, new double[] {offset});
                    imagesStack.push(worker.getRenderedImage());
                    inputRI =
                            worker.lookup(createLookupTableByte(exclusionLuminanceRanges, dataType))
                                    .getRenderedImage();
                    break;
                case DataBuffer.TYPE_INT:
                    scale = MAX_8BIT_VALUE / Integer.MAX_VALUE;
                    offset =
                            MAX_8BIT_VALUE
                                    * Integer.MIN_VALUE
                                    / (Integer.MIN_VALUE - Integer.MAX_VALUE);
                    worker.rescale(new double[] {scale}, new double[] {offset});
                    imagesStack.push(worker.getRenderedImage());
                    inputRI =
                            worker.lookup(createLookupTableByte(exclusionLuminanceRanges, dataType))
                                    .getRenderedImage();
                    break;
                default:
                    throw new UnsupportedOperationException("Wrong data type:" + dataType);
            }

            assert inputRI.getSampleModel().getDataType() == DataBuffer.TYPE_BYTE;
        } else {
            inputRI =
                    worker.lookup(createLookupTableByte(exclusionLuminanceRanges, dataType))
                            .getRenderedImage();
        }
        return inputRI;
    }

    /** Check if the image is fully covered by only valid values */
    private boolean checkFullyCovered(
            RandomIter iter, final int refValue, final List<Polygon> geometriesList) {
        int[] yvals = new int[] {imageProperties.minY, imageProperties.maxY};
        for (int y : yvals) {
            for (int x = imageProperties.minX; x <= imageProperties.maxX; x++) {
                int value = iter.getSample(x, y, 0);
                if (value != refValue) {
                    return false;
                }
            }
        }

        int[] xvals = new int[] {imageProperties.minX, imageProperties.maxX};
        for (int x : xvals) {
            for (int y = imageProperties.minY; y <= imageProperties.maxY; y++) {
                int value = iter.getSample(x, y, 0);
                if (value != refValue) {
                    return false;
                }
            }
        }

        addFullAreaPolygon(geometriesList);

        return true;
    }

    /** Check if the image is fully covered by only valid values */
    private boolean checkFullyCovered(
            RandomIter iter, double refValue, List<Polygon> geometriesList) {
        for (int y = imageProperties.minY;
                y <= imageProperties.maxY;
                y += (imageProperties.maxY - imageProperties.minY)) {
            for (int x = imageProperties.minX; x <= imageProperties.maxX; x++) {
                double value = iter.getSample(x, y, 0);
                if (value != refValue) {
                    return false;
                }
            }
        }

        for (int x = imageProperties.minX;
                x <= imageProperties.maxX;
                x += (imageProperties.maxX - imageProperties.minX)) {
            for (int y = imageProperties.minY;
                    y <= imageProperties.maxY;
                    y += (imageProperties.maxY - imageProperties.minY)) {
                double value = iter.getSample(x, y, 0);
                if (value != refValue) {
                    return false;
                }
            }
        }

        addFullAreaPolygon(geometriesList);

        return true;
    }

    private void addFullAreaPolygon(List<Polygon> geometriesList) {
        Coordinate[] coordinateArray = new Coordinate[5];
        coordinateArray[0] = new Coordinate(imageProperties.minX - 1, imageProperties.minY - 1);
        coordinateArray[1] = new Coordinate(imageProperties.maxX, imageProperties.minY - 1);
        coordinateArray[2] = new Coordinate(imageProperties.maxX, imageProperties.maxY);
        coordinateArray[3] = new Coordinate(imageProperties.minX - 1, imageProperties.maxY);
        coordinateArray[4] = new Coordinate(imageProperties.minX - 1, imageProperties.minY - 1);

        LinearRing linearRing = GF.createLinearRing(coordinateArray);
        Polygon polygon = GF.createPolygon(linearRing, null);

        geometriesList.add(polygon);
    }

    /** */
    private Polygon identifyPerimeter(
            final RandomIter iter,
            final int initialX,
            final int initialY,
            java.awt.Polygon awtPolygon,
            final int sampleDataType,
            final ScanInfo scanInfo)
            throws TransformException {
        if ((initialX < imageProperties.minX)
                || (initialX > imageProperties.maxX)
                || (initialY < imageProperties.minY)
                || (initialY > imageProperties.maxY)) {
            throw new IllegalArgumentException("Coordinate outside the bounds.");
        }

        int initialValue = value(iter, initialX, initialY, sampleDataType, false);
        if (initialValue == 0) {
            throw new IllegalArgumentException(
                    String.format(
                            "Supplied initial coordinates (%d, %d) do not lie on a perimeter.",
                            initialX, initialY));
        }
        if (initialValue == 15) {
            // not a border pixel
            return null;
        }

        final Point2D worldPosition = new Point2D.Double(initialX - 1, initialY - 1);
        Coordinate startCoordinate = new Coordinate(worldPosition.getX(), worldPosition.getY());
        List<Coordinate> coordinateList = new ArrayList<Coordinate>(200);

        int x = initialX;
        int y = initialY;
        awtPolygon.reset();
        awtPolygon.addPoint(x, y);

        boolean previousWentNorth = false;
        boolean previousWentEast = true;
        int v = value(iter, x, y, sampleDataType, true);
        do {
            int dx = 0;
            int dy = 0;
            switch (v) {
                case 1:
                    dy = NEGATIVE_STEP; // N
                    previousWentNorth = true;
                    break;
                case 2:
                    dx = POSITIVE_STEP; // E
                    previousWentEast = true;
                    break;
                case 3:
                    dx = POSITIVE_STEP; // E
                    previousWentEast = true;
                    break;
                case 4:
                    dx = NEGATIVE_STEP; // W
                    previousWentEast = false;
                    break;
                case 5:
                    dy = NEGATIVE_STEP; // N
                    previousWentNorth = true;
                    break;
                case 6:
                    if (!previousWentNorth) // W
                    {
                        dx = NEGATIVE_STEP;
                        previousWentEast = false;
                    } else {
                        dx = POSITIVE_STEP; // E
                        previousWentEast = true;
                    }
                    break;
                case 7:
                    dx = POSITIVE_STEP; // E
                    previousWentEast = true;
                    break;
                case 8:
                    dy = POSITIVE_STEP; // S
                    previousWentNorth = false;
                    break;
                case 9:
                    if (previousWentEast) {
                        if (isLowerCorner(iter, x, y, sampleDataType)) {
                            dy = POSITIVE_STEP; // S
                            previousWentNorth = false;
                        } else {
                            dy = NEGATIVE_STEP; // N
                            previousWentNorth = true;
                        }
                    } else {
                        if (isLowerCorner(iter, x, y, sampleDataType)) {
                            dy = NEGATIVE_STEP; // N
                            previousWentNorth = true;
                        } else {
                            dy = POSITIVE_STEP; // S
                            previousWentNorth = false;
                        }
                    }
                    break;
                case 10:
                    dy = POSITIVE_STEP; // S
                    previousWentNorth = false;
                    break;
                case 11:
                    dy = POSITIVE_STEP; // S
                    previousWentNorth = false;
                    break;
                case 12:
                    dx = NEGATIVE_STEP; // W
                    previousWentEast = false;
                    break;
                case 13:
                    dy = NEGATIVE_STEP; // N
                    previousWentNorth = true;
                    break;
                case 14:
                    dx = NEGATIVE_STEP; // W
                    previousWentEast = false;
                    break;
                default:
                    throw new IllegalStateException("Illegal state: " + v);
            }

            Coordinate direction = new Coordinate(x - 1, y - 1);
            coordinateList.add(direction);
            x = x + dx;
            y = y + dy;
            v = value(iter, x, y, sampleDataType, true);
            awtPolygon.addPoint(x, y);
        } while ((x != initialX) || (y != initialY));

        double polygonArea =
                getPolygonArea(awtPolygon.xpoints, awtPolygon.ypoints, awtPolygon.npoints - 1);
        if (polygonArea < thresholdArea) {
            if (!scanInfo.firstFound) {
                // Taking note that at least a polygon have
                // been found even if smaller than the threshold area
                scanInfo.firstFound = true;
            }
            if (!scanInfo.takeFirst) {
                // This check allow to return this polygon in case no others have been found
                // at the end of the scan and we are looking back for the first one.
                // This may be useful when each polygon is smaller than the threshold
                // but we want to return at least one of them, which may happen when
                // scanning a valid image resulting from a request with very big subsampling or
                // decimation
                return null;
            }
        }

        coordinateList.add(startCoordinate);

        Coordinate[] coordinateArray =
                (Coordinate[]) coordinateList.toArray(new Coordinate[coordinateList.size()]);

        LinearRing linearRing = GF.createLinearRing(coordinateArray);
        Polygon polygon = GF.createPolygon(linearRing, null);

        return polygon;
    }

    /**
     * Simple utility method checking if the tested pixel belongs to a lower corner 2*2 checker
     * board.
     */
    private boolean isLowerCorner(RandomIter iter, int x, int y, int sampleDataType) {
        return (value(iter, x + 1, y, sampleDataType, false) == 4)
                && (value(iter, x, y + 1, sampleDataType, false) == 2)
                && (value(iter, x + 1, y + 1, sampleDataType, false) == 1);
    }

    private int value(
            RandomIter iter, int x, int y, final int dataType, final boolean forceSetting) {
        int sum = 0;

        if (isSet(iter, x - 1, y - 1, dataType, forceSetting)) // UL
        {
            sum |= 1;
        }
        if (isSet(iter, x, y - 1, dataType, forceSetting)) // UR
        {
            sum |= 2;
        }
        if (isSet(iter, x - 1, y, dataType, forceSetting)) // LL
        {
            sum |= 4;
        }
        if (isSet(iter, x, y, dataType, forceSetting)) // LR
        {
            sum |= 8;
        }

        if (sum == 0) {
            if (LOGGER.isLoggable(Level.INFO)) {
                LOGGER.info("x " + x + " y" + y);
            }
        }

        return sum;
    }

    private boolean isSet(
            RandomIter iter, int x, int y, final int dataType, final boolean forceSetting) {
        boolean isOutsideGrid =
                (x < imageProperties.minX)
                        || (x > imageProperties.maxX)
                        || (y < imageProperties.minY)
                        || (y > imageProperties.maxY);
        if (isOutsideGrid) {
            return false;
        }
        if (dataType == DataBuffer.TYPE_DOUBLE) {
            double value = iter.getSampleDouble(x, y, 0);
            if (!Double.isNaN(value)) {
                // mark the used position
                if (forceSetting || (x == imageProperties.maxX)) {
                    bitSet.set(x - imageProperties.minX, y - imageProperties.minY);
                }
            } else {
                return false;
            }
            if (areEqual(value, D_VALUE)) {
                return true;
            }
        } else {
            int value = iter.getSample(x, y, 0);
            // mark the used position
            if (forceSetting || (x == imageProperties.maxX)) {
                bitSet.set(x - imageProperties.minX, y - imageProperties.minY);
            }
            if (value == I_VALUE) {
                return true;
            }
        }

        return false;
    }

    /** Simple check returning where two double values are equal */
    public static final boolean areEqual(double value, double pValue) {
        return Math.abs(value - pValue) < DELTA;
    }

    public void dispose() {

        bitSet = null;

        while (!imagesStack.isEmpty()) {
            RenderedImage removeMe = imagesStack.pop();
            if (removeMe != null) {
                ImageUtilities.disposeImage(removeMe);
            }
        }
    }

    public static class ImageAnalysisResultThdLocal {

        private static final InheritableThreadLocal<Exception> tl =
                new InheritableThreadLocal<Exception>() {
                    @Override
                    protected Exception initialValue() {

                        return null;
                    }
                };

        public static Exception get() {
            return tl.get();
        }

        public static void set(Exception ex) {
            tl.set(ex);
        }

        public static void clear() {
            tl.remove();
        }

        private ImageAnalysisResultThdLocal() {}
    }

    /**
     * Calculates the area of a polygon from the coordinates.
     *
     * @return the area of the polygon.
     */
    public static double getPolygonArea(List<Coordinate> coordinateList) {
        Utilities.ensureNonNull("coordinateList", coordinateList);

        final int N = coordinateList.size() - 1;

        double area = 0;

        for (int i = 0; i < N; i++) {
            int j = (i + 1) % N;
            Coordinate cj = coordinateList.get(j);
            Coordinate ci = coordinateList.get(i);
            area += ci.x * cj.y;
            area -= ci.y * cj.x;
        }

        area /= 2;

        return ((area < 0) ? -area : area);
    }

    /**
     * Calculates the area of a polygon from its vertices.
     *
     * @param x the array of x coordinates.
     * @param y the array of y coordinates.
     * @param N the number of sides of the polygon.
     * @return the area of the polygon.
     */
    public static double getPolygonArea(int[] x, int[] y, int N) {
        double area = 0;

        for (int i = 0; i < N; i++) {
            int j = (i + 1) % N;
            area += x[i] * y[j];
            area -= y[i] * x[j];
        }

        area /= 2;

        return ((area < 0) ? -area : area);
    }

    /**
     * Create a simple polygon (no holes).
     *
     * @param coords the coords of the polygon.
     * @return the {@link Polygon}.
     */
    public static Polygon createSimplePolygon(Coordinate[] coords) {
        return GF.createPolygon(GF.createLinearRing(coords), null);
    }

    private LookupTable createLookupTableByte(List<Range<Integer>> exclusionValues, int dataType) {
        final byte[] b = new byte[256];
        Arrays.fill(b, (byte) 0);
        for (Range<Integer> exclusionValue : exclusionValues) {
            final int minValue = exclusionValue.getMinValue();
            final int maxValue = exclusionValue.getMaxValue();
            for (int i = minValue; i <= maxValue; i++) {
                b[i] = (byte) INVALID_PIXEL_I;
            }
        }

        return LookupTableFactory.create(b, dataType);
    }

    private LookupTable createLookupTableUShort(
            List<Range<Integer>> exclusionValues, int dataType) {
        final byte[] bUShort = new byte[65536];
        Arrays.fill(bUShort, (byte) 0);
        for (Range<Integer> exclusionValue : exclusionValues) {
            final int minValue = exclusionValue.getMinValue();
            final int maxValue = exclusionValue.getMaxValue();
            for (int i = minValue; i <= maxValue; i++) {
                bUShort[i] = (byte) INVALID_PIXEL_I;
            }
        }
        return LookupTableFactory.create(bUShort, dataType);
    }
}
