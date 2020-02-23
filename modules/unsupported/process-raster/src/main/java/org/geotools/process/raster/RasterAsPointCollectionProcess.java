/*
 * GeoTools - The Open Source Java GIS Toolkit
 * http://geotools.org
 *
 * (C) 2014-2015, Open Source Geospatial Foundation (OSGeo)
 * (C) 2001-2014 TOPP - www.openplans.org.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation;
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 */
package org.geotools.process.raster;

import java.awt.Rectangle;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.io.IOException;
import java.util.NoSuchElementException;
import javax.media.jai.Interpolation;
import javax.media.jai.InterpolationNearest;
import javax.media.jai.PlanarImage;
import javax.media.jai.iterator.RectIter;
import javax.media.jai.iterator.RectIterFactory;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.collection.AdaptorFeatureCollection;
import org.geotools.feature.collection.BaseSimpleFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.geometry.util.XRectangle2D;
import org.geotools.process.ProcessException;
import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.util.Utilities;
import org.geotools.util.factory.GeoTools;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.FeatureType;
import org.opengis.metadata.spatial.PixelOrientation;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.cs.AxisDirection;
import org.opengis.referencing.cs.CoordinateSystem;
import org.opengis.referencing.cs.CoordinateSystemAxis;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.MathTransform2D;

/**
 * A process that wraps a {@link GridCoverage2D} as a collection of point feature. Optional
 * parameters can be set:
 *
 * <ul>
 *   <li>targetCRS : can be used for calculating the GridConvergence Angle of each point
 *   <li>scale : can be used for scaling the input coverage
 *   <li>interpolation : can be used for setting the interpolation method when Scaling is applied
 *   <li>emisphere : forces to indicate the hemisphere for each point
 * </ul>
 *
 * @author Simone Giannecchini, GeoSolutions
 */
@DescribeProcess(
    title = "Raster As Point Collection",
    description =
            "Returns a collection of point features for the pixels of a raster.  The band values are provided as attributes."
)
public class RasterAsPointCollectionProcess implements RasterProcess {

    @DescribeResult(name = "result", description = "Point features")
    public SimpleFeatureCollection execute(
            @DescribeParameter(name = "data", description = "Input raster") GridCoverage2D gc2d,
            @DescribeParameter(
                        name = "targetCRS",
                        description = "CRS in which the points will be displayed",
                        min = 0
                    )
                    CoordinateReferenceSystem targetCRS,
            @DescribeParameter(
                        name = "scale",
                        description = "scale",
                        min = 0,
                        defaultValue = "1.0f"
                    )
                    Float scaleFactor,
            @DescribeParameter(
                        name = "interpolation",
                        description = "interpolation",
                        min = 0,
                        defaultValue = "InterpolationNearest"
                    )
                    Interpolation interpolation,
            @DescribeParameter(
                        name = "emisphere",
                        description = "Add Emishpere",
                        min = 0,
                        defaultValue = "False"
                    )
                    Boolean emisphere)
            throws ProcessException {
        if (gc2d == null) {
            throw new ProcessException("Invalid input, source grid coverage should be not null");
        }

        // Get the GridEnvelope associated to the input Raster for selecting its width and height.
        // These two values are used for check if the scale is needed
        GridEnvelope2D gridEnv = gc2d.getGridGeometry().getGridRange2D();
        double coverageWidth = gridEnv.getWidth();
        double coverageHeight = gridEnv.getHeight();

        ////
        //
        // scale if/as needed. Also check if the scale expands the coverage dimension for at least 1
        // pixel.
        //
        ////
        if (scaleFactor != null
                && (Math.abs(coverageWidth * (scaleFactor - 1f)) >= 1
                        || Math.abs(coverageHeight * (scaleFactor - 1f)) >= 1)) {
            // Selection of the interpolation parameter
            Interpolation interp =
                    interpolation != null ? interpolation : new InterpolationNearest();
            // Selection of the ScaleFactors in order to check if the final Raster has almost 1
            // pixel for Height and Width
            double scaleX = scaleFactor;
            double scaleY = scaleFactor;
            // RenderedImage associated to the coverage
            final RenderedImage imageToBescaled = gc2d.getRenderedImage();

            if (imageToBescaled != null) {
                final SampleModel sampleModel = imageToBescaled.getSampleModel();
                final int height = sampleModel.getHeight();
                final int width = sampleModel.getWidth();
                if (height * scaleFactor < 1) {
                    scaleY = 1d / height;
                }
                if (width * scaleFactor < 1) {
                    scaleX = 1d / width;
                }
            }

            // Execution of the Affine process
            gc2d =
                    new AffineProcess()
                            .execute(gc2d, scaleX, scaleY, null, null, null, null, null, interp);
        }

        // return value
        try {
            return new RasterAsPointFeatureCollection(gc2d, emisphere, targetCRS);
        } catch (IOException e) {
            throw new ProcessException("Unable to wrap provided grid coverage", e);
        }
    }

    /**
     * TODO @see {@link AdaptorFeatureCollection} TODO @see {@link DefaultFeatureCollection}
     *
     * @author Simone Giannecchini, GeoSolutions
     */
    private static final class RasterAsPointFeatureCollection extends BaseSimpleFeatureCollection {

        /** The {@link GeometryFactory} cached here for building points inside iterators */
        static final GeometryFactory geometryFactory =
                JTSFactoryFinder.getGeometryFactory(GeoTools.getDefaultHints());

        /** The {@link GridCoverage2D} that we want to expose as a point feature collection. */
        final GridCoverage2D gc2d;

        /** Number of points in this collections is as many as width*height. */
        final int size;

        /** Grid to world transformation at the upper left corner of the raster space. */
        final MathTransform2D mt2D;

        /** The bounding box for this feature collection */
        private ReferencedEnvelope bounds;

        /** Raster bounds for this coverage */
        final Rectangle rasterBounds;

        /** Number of bands */
        final int numBands;

        /** Boolean indicating if the Hemisphere in which the point lies must be indicated */
        private boolean emisphere;

        /** Optional transformation from the coverage CRS to WGS84 */
        private MathTransform transformToWGS84;

        /** Index for the North Dimension of the coverage CRS */
        private int northDimension = -1;

        /** Target CRS indicating that the input Coverage has been reprojected */
        private CoordinateReferenceSystem targetCRS;

        /**
         * Transformation used for reprojecting each point from the coverage CRS to the target CRS
         */
        private MathTransform reprojectionTransformation;

        /** Boolean indicating if the calculation of the GridConvergence Angle is requested */
        private boolean gridConvergenceAngleCorrectionNeeded;

        /** Class used for calculating the GridConvergence Angle */
        private GridConvergenceAngleCalc gridConvergenceAngleManager;

        public RasterAsPointFeatureCollection(final GridCoverage2D gc2d) throws IOException {
            this(gc2d, false, gc2d.getCoordinateReferenceSystem2D());
        }

        /** */
        public RasterAsPointFeatureCollection(
                GridCoverage2D gc2d, boolean emisphere, CoordinateReferenceSystem targetCRS)
                throws IOException {
            super(
                    modify(
                            CoverageUtilities.createFeatureType(gc2d, Point.class),
                            emisphere,
                            targetCRS));
            this.gc2d = gc2d;
            this.emisphere = emisphere;
            this.targetCRS = targetCRS;

            //
            // get various elements from this coverage
            //
            // SIZE
            final RenderedImage raster = gc2d.getRenderedImage();
            size = raster.getWidth() * raster.getHeight();

            // GRID TO WORLD for transforming raster points into model points
            mt2D = gc2d.getGridGeometry().getGridToCRS2D(PixelOrientation.UPPER_LEFT);

            // BOUNDS take into account that we want to map center coordinates
            rasterBounds = PlanarImage.wrapRenderedImage(raster).getBounds();
            final XRectangle2D rasterBounds_ =
                    new XRectangle2D(
                            raster.getMinX() + 0.5,
                            raster.getMinY() + 0.5,
                            raster.getWidth() - 1,
                            raster.getHeight() - 1);
            try {
                bounds =
                        new ReferencedEnvelope(
                                CRS.transform(mt2D, rasterBounds_, null),
                                gc2d.getCoordinateReferenceSystem2D());
            } catch (Exception e) {
                final IOException ioe = new IOException();
                ioe.initCause(e);
                throw ioe;
            }

            // BANDS
            numBands = gc2d.getNumSampleDimensions();

            final CoordinateReferenceSystem coverageCRS = gc2d.getCoordinateReferenceSystem2D();
            //
            // Emisphere management
            //
            emisphereManagement(coverageCRS);

            //
            // Grid Convergence Angle correction
            //
            gridConvergenceAngle(coverageCRS);
        }

        /**
         * Prepare for Managing the GridConvergenceAngle
         *
         * @param coverageCRS the {@link GridCoverage2D} {@link CoordinateReferenceSystem}
         */
        private void gridConvergenceAngle(final CoordinateReferenceSystem coverageCRS)
                throws IOException {
            // GridCoverage Angle management is required only if the input Coverage has been
            // reprojected
            if (targetCRS != null) {

                // there is a real reprojection?
                if (!CRS.equalsIgnoreMetadata(coverageCRS, targetCRS)) {

                    // get the transformation and check if that is not the identity
                    try {
                        reprojectionTransformation =
                                CRS.findMathTransform(coverageCRS, targetCRS, true);
                        gridConvergenceAngleCorrectionNeeded =
                                !reprojectionTransformation.isIdentity();
                        if (gridConvergenceAngleCorrectionNeeded) {

                            //
                            // Instantiate calculator to use to determine convergence angle at
                            // each point for the request CRS.
                            //
                            gridConvergenceAngleManager = new GridConvergenceAngleCalc(targetCRS);
                        }

                    } catch (Exception e) {
                        throw new IOException(e);
                    }
                }
            }
        }

        /**
         * Prepare the variables used by the iterator for checking the North Hemisphere.
         *
         * @param coverageCRS CRS of the input coverage
         */
        private void emisphereManagement(CoordinateReferenceSystem coverageCRS) throws IOException {
            // The Hemisphere is evaluated only if the associated flag is set to true
            if (emisphere) {
                // If the Coverage CRS is already in WGS84 then is is easy to get the Hemisphere
                // from the Y direction value,
                // else a reprojection is needed.
                if (!CRS.equalsIgnoreMetadata(DefaultGeographicCRS.WGS84, coverageCRS)) {
                    try {

                        // save transform
                        final MathTransform transform =
                                CRS.findMathTransform(
                                        coverageCRS, DefaultGeographicCRS.WGS84, true);
                        if (!transform.isIdentity()) {
                            this.transformToWGS84 = transform;
                        }

                        final CoordinateSystem coordinateSystem = coverageCRS.getCoordinateSystem();
                        // save also latitude axis
                        final int dimension = coordinateSystem.getDimension();
                        for (int i = 0; i < dimension; i++) {
                            CoordinateSystemAxis axis = coordinateSystem.getAxis(i);
                            if (axis.getDirection().absolute().compareTo(AxisDirection.NORTH)
                                    == 0) {
                                this.northDimension = i;
                                break;
                            }
                        }
                        // If the northDimension has not been found then an exception is thrown
                        if (northDimension < 0) {
                            final IOException ioe =
                                    new IOException(
                                            "Unable to find nort dimension in the coverage CRS+ "
                                                    + coverageCRS.toWKT());
                            throw ioe;
                        }
                    } catch (FactoryException e) {
                        throw new IOException(e);
                    }
                }
            }
        }

        /**
         * Static method which prepares the feature type associated to each Point.
         *
         * @param featureType Input {@link FeatureType} associated to the Coverage
         * @param emisphere Boolean indicating if the emisphere must be set
         * @param targetCRS CRS used if the gridConvergence Angle must be calculated
         */
        private static SimpleFeatureType modify(
                SimpleFeatureType featureType,
                boolean emisphere,
                CoordinateReferenceSystem targetCRS) {
            if (!emisphere && targetCRS == null) {
                return featureType;
            }

            // add emishpere attribute
            final SimpleFeatureTypeBuilder ftBuilder = new SimpleFeatureTypeBuilder();
            ftBuilder.setName(featureType.getName());

            // CRS
            ftBuilder.setCRS(featureType.getCoordinateReferenceSystem());

            // TYPE is as follows the_geom | band
            ftBuilder.setDefaultGeometry(featureType.getGeometryDescriptor().getLocalName());

            // copy attributes
            for (AttributeDescriptor atd : featureType.getAttributeDescriptors()) {
                ftBuilder.add(atd);
            }

            if (emisphere) {
                // add emisphere
                ftBuilder.add("emisphere", String.class); // valid values N/S
            }

            if (targetCRS != null) {
                // add gridConvergenceAngle correction
                ftBuilder.add("gridConvergenceAngleCorrection", Double.class); // degrees
            }

            return ftBuilder.buildFeatureType();
        }

        @Override
        public SimpleFeatureIterator features() {
            return new RasterAsPointFeatureIterator(this);
        }

        @Override
        public int size() {
            return size;
        }

        @Override
        public ReferencedEnvelope getBounds() {
            return new ReferencedEnvelope(bounds);
        }
    }

    private static final class RasterAsPointFeatureIterator implements SimpleFeatureIterator {

        private final double[] temp;

        private final SimpleFeatureBuilder fb;

        private final RasterAsPointFeatureCollection fc;

        private int index = 0;

        private final int size;

        private final RectIter iterator;

        private final Coordinate sourceCoordinate = new Coordinate();

        /** Position of the Point in the source CRS */
        private DirectPosition2D sourceCRSPosition;

        /** Position of the Point in the target CRS */
        private DirectPosition2D targetCRSPosition;

        public RasterAsPointFeatureIterator(final RasterAsPointFeatureCollection fc) {

            // checks
            Utilities.ensureNonNull("fc", fc);

            // get elements
            this.fc = fc;
            this.fb = new SimpleFeatureBuilder(fc.getSchema());
            this.size = fc.size;

            // create an iterator that only goes forward, it is the fastest one
            iterator = RectIterFactory.create(fc.gc2d.getRenderedImage(), null);

            //
            // start the iterator
            //
            iterator.startLines();
            if (iterator.finishedLines()) {
                throw new NoSuchElementException("Index beyond size:" + index + ">" + size);
            }
            iterator.startPixels();
            if (iterator.finishedPixels()) {
                throw new NoSuchElementException("Index beyond size:" + index + ">" + size);
            }

            // appo
            temp = new double[fc.numBands];

            // grid convergence angle manager
            if (fc.gridConvergenceAngleCorrectionNeeded) {
                sourceCRSPosition = new DirectPosition2D();
                targetCRSPosition = new DirectPosition2D(fc.targetCRS);
            }
        }

        /** Closes this iterator */
        public void close() {
            // NO OP
        }

        /** Tells us whether or not we have more elements to iterate on. */
        public boolean hasNext() {
            return index < size;
        }

        public SimpleFeature next() throws NoSuchElementException {

            if (!hasNext()) {
                throw new NoSuchElementException("Index beyond size:" + index + ">" + size);
            }

            // iterate
            if (iterator.finishedPixels()) {
                throw new NoSuchElementException("Index beyond size:" + index + ">" + size);
            }
            if (iterator.finishedLines()) {
                throw new NoSuchElementException("Index beyond size:" + index + ">" + size);
            }

            // ID
            final int id = index;

            // POINT
            // can we reuse the coord?
            sourceCoordinate.x = 0.5 + fc.rasterBounds.x + index % fc.rasterBounds.width;
            sourceCoordinate.y = 0.5 + fc.rasterBounds.y + index / fc.rasterBounds.width;
            Point point =
                    RasterAsPointFeatureCollection.geometryFactory.createPoint(sourceCoordinate);
            try {
                point = (Point) JTS.transform(point, fc.mt2D);
                fb.add(point);

                // VALUES
                // loop on bands
                iterator.getPixel(temp);
                for (double d : temp) {
                    // I exploit the internal converters to go from double to whatever the type is
                    // TODO is this correct or we can do more.
                    fb.add(d);
                }

                // Hemisphere management
                emisphereAttributeManagement(point);

                // grid convergence angle correction
                gridConvergenceAngleManagement(point);

            } catch (Exception e) {
                final NoSuchElementException nse = new NoSuchElementException();
                nse.initCause(e);
                throw nse;
            }

            // do we need to wrap the line??
            if (iterator.nextPixelDone()) {
                if (!iterator.nextLineDone()) iterator.startPixels();
            }

            // return
            final SimpleFeature returnValue = fb.buildFeature(String.valueOf(id));

            // increase index and iterator
            index++;

            return returnValue;
        }

        /**
         * This method adds the GridConvergence Angle attribute to the Point feature if it is
         * needed.
         *
         * @param point Input Point to handle.
         */
        private void gridConvergenceAngleManagement(Point point) throws Exception {
            // If the GridConvergence Angle correction is not requested, then nothing is done
            if (fc.gridConvergenceAngleCorrectionNeeded) {

                //
                // Calculate convergence angle
                //
                sourceCRSPosition.setLocation(point.getX(), point.getY());
                fc.reprojectionTransformation.transform(sourceCRSPosition, targetCRSPosition);
                // Calculation of the Angle
                double convAngle =
                        fc.gridConvergenceAngleManager.getConvergenceAngle(targetCRSPosition);
                fb.add(convAngle);
            }
        }

        /**
         * This method adds the Hemisphere attribute to the Point feature if requested.
         *
         * @param point Input Point to handle.
         */
        private void emisphereAttributeManagement(final Point point) throws IOException {
            // If the Hemisphere flag is set to false no calculation is performed
            if (fc.emisphere) {
                // If the Coverage CRS is WGS84 then the Y coordinate value indicates the associated
                // Hemisphere
                if (fc.transformToWGS84 == null) {
                    if (point.getY() >= 0) {
                        fb.add("N");
                    } else {
                        fb.add("S");
                    }
                } else {
                    try {
                        // Else the point must be reprojected previously in order to define the
                        // Hemisphere
                        Point wgs84Point = (Point) JTS.transform(point, fc.transformToWGS84);
                        if (wgs84Point.getCoordinate().getOrdinate(fc.northDimension) >= 0) {
                            fb.add("N");
                        } else {
                            fb.add("S");
                        }
                    } catch (Exception e) {
                        throw new IOException(e);
                    }
                }
            }
        }
    }
}
