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
package org.geotools.gce.imagemosaic;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.coverage.grid.GeneralGridEnvelope;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.ReadResolutionCalculator;
import org.geotools.data.DataSourceException;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.PixelTranslation;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.geometry.util.XRectangle2D;
import org.geotools.referencing.CRS;
import org.geotools.referencing.operation.LinearTransform;
import org.geotools.referencing.operation.builder.GridToEnvelopeMapper;
import org.geotools.referencing.operation.transform.ProjectiveTransform;
import org.geotools.util.Utilities;
import org.opengis.geometry.BoundingBox;
import org.opengis.geometry.Envelope;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.datum.PixelInCell;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.MathTransform2D;
import org.opengis.referencing.operation.TransformException;

/**
 * Helper class which takes coverage's spatial information input (CRS, bbox, resolution,...) and a
 * set of request's parameters (requestedCRS, requestedBBox, requested resolution, ...) and takes
 * care of computing all auxiliary spatial variables for future computations.
 *
 * @author Daniele Romagnoli, GeoSolutions SAS
 */
public class SpatialRequestHelper {

    public SpatialRequestHelper(CoverageProperties coverageProperties) {
        super();
        this.coverageProperties = coverageProperties;
    }

    public static class CoverageProperties {
        public ReferencedEnvelope getBbox() {
            return bbox;
        }

        public void setBBox(ReferencedEnvelope bbox) {
            this.bbox = bbox;
        }

        public Rectangle getRasterArea() {
            return rasterArea;
        }

        public void setRasterArea(Rectangle rasterArea) {
            this.rasterArea = rasterArea;
        }

        public double[] getFullResolution() {
            return fullResolution;
        }

        public void setFullResolution(double[] fullResolution) {
            this.fullResolution = fullResolution;
        }

        public MathTransform2D getGridToWorld2D() {
            return gridToWorld2D;
        }

        public void setGridToWorld2D(MathTransform2D gridToWorld2D) {
            this.gridToWorld2D = gridToWorld2D;
        }

        public CoordinateReferenceSystem getCrs2D() {
            return crs2D;
        }

        public void setCrs2D(CoordinateReferenceSystem crs2d) {
            crs2D = crs2d;
        }

        public ReferencedEnvelope getGeographicBBox() {
            return geographicBBox;
        }

        public void setGeographicBBox(ReferencedEnvelope geographicBBox) {
            this.geographicBBox = geographicBBox;
        }

        public CoordinateReferenceSystem getGeographicCRS2D() {
            return geographicCRS;
        }

        public void setGeographicCRS2D(CoordinateReferenceSystem geographicCRS2D) {
            this.geographicCRS = geographicCRS2D;
        }

        // //
        // Source Coverages properties
        // //
        ReferencedEnvelope bbox;

        Rectangle rasterArea;

        double[] fullResolution;

        MathTransform2D gridToWorld2D;

        CoordinateReferenceSystem crs2D;

        ReferencedEnvelope geographicBBox;

        CoordinateReferenceSystem geographicCRS;
    }

    private static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(SpatialRequestHelper.class);

    /** The {@link BoundingBox} requested */
    private ReferencedEnvelope requestedBBox;

    /** The {@link BoundingBox} of the portion of the coverage that intersects the requested bbox */
    private ReferencedEnvelope computedBBox;

    /** The region where to fit the requested envelope */
    private Rectangle requestedRasterArea;

    /** The region of the */
    private Rectangle computedRasterArea;

    private CoordinateReferenceSystem requestCRS;

    private AffineTransform requestedGridToWorld;

    private double[] computedResolution;

    private GeneralEnvelope requestedBBOXInCoverageGeographicCRS;

    private MathTransform requestCRSToCoverageGeographicCRS2D;

    private MathTransform destinationToSourceTransform;

    private final CoverageProperties coverageProperties;

    private boolean accurateResolution;

    /**
     * Set to {@code true} if this request will produce an empty result, and the coverageResponse
     * will produce a {@code null} coverage.
     */
    private boolean emptyRequest;

    private boolean needsReprojection = false;

    private GeneralEnvelope approximateRequestedBBoInNativeCRS;

    /**
     * The final Grid To World. In case there is a reprojection involved it is not the original one.
     */
    private AffineTransform computedGridToWorld;

    private GridGeometry2D requestedGridGeometry;

    public void setRequestedGridGeometry(GridGeometry2D gridGeometry) {
        Utilities.ensureNonNull("girdGeometry", gridGeometry);
        requestedBBox = new ReferencedEnvelope((Envelope) gridGeometry.getEnvelope2D());
        requestedRasterArea = gridGeometry.getGridRange2D().getBounds();
        requestedGridGeometry = gridGeometry;
        requestedGridToWorld = (AffineTransform) gridGeometry.getGridToCRS2D();
    }

    /**
     * Compute this specific request settings all the parameters needed by a visiting {@link
     * RasterLayerResponse} object.
     */
    public void compute() throws DataSourceException {
        //
        // DO WE HAVE A REQUESTED AREA?
        //
        // Check if we have something to load by intersecting the
        // requested envelope with the bounds of this data set.
        //
        if (requestedBBox == null) {

            //
            // In case we have nothing to look at we should get the whole coverage
            //
            requestedBBox =
                    new ReferencedEnvelope(coverageProperties.bbox, coverageProperties.crs2D);
            requestedRasterArea = (Rectangle) coverageProperties.rasterArea.clone();
            computedResolution = coverageProperties.fullResolution.clone();
            computedBBox =
                    new ReferencedEnvelope(coverageProperties.bbox, coverageProperties.crs2D);
            computedRasterArea = (Rectangle) coverageProperties.rasterArea.clone();
            // TODO harmonize the various types of transformations
            computedGridToWorld =
                    requestedGridToWorld = (AffineTransform) coverageProperties.gridToWorld2D;
            // account for an empty coverage --> set request empty
            if (requestedBBox.isEmpty()) emptyRequest = true;
            return;
        }

        //
        // WE DO HAVE A REQUESTED AREA!
        //

        //
        // Inspect the request and precompute transformation between CRS. We
        // also check if we can simply adjust the requested GG in case the
        // request CRS is different from the coverage native CRS but the
        // transformation is simply an affine transformation.
        //
        // In such a case we can simplify our work by adjusting the
        // requested grid to world, preconcatenating the coordinate
        // operation to change CRS
        //
        inspectCoordinateReferenceSystems();

        //
        // Create the CROP BBOX in the coverage CRS for cropping it later on.
        //
        computeCropBBOX();
        if (emptyRequest || computedBBox == null) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "RequestedBBox empty or null");
            }
            return;
        }

        //
        // CROP SOURCE REGION using the refined requested envelope
        //
        computeRasterArea();
        if (emptyRequest || computedRasterArea == null) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "CropRasterArea empty or null");
            }
            // this means that we do not have anything to load at all!
            return;
        }

        if (LOGGER.isLoggable(Level.FINER)) {
            StringBuilder sb =
                    new StringBuilder("Adjusted Requested Envelope = ")
                            .append(requestedBBox.toString())
                            .append("\n")
                            .append("Requested raster dimension = ")
                            .append(requestedRasterArea.toString())
                            .append("\n")
                            .append("Corresponding raster source region = ")
                            .append(computedRasterArea.toString())
                            .append("\n")
                            .append("Corresponding source Envelope = ")
                            .append(computedBBox.toString());
            LOGGER.log(Level.FINER, sb.toString());
        }

        //
        // Compute the request resolution from the request
        //
        computeResolution();
    }

    private void inspectCoordinateReferenceSystems() throws DataSourceException {
        // get the crs for the requested bbox
        requestCRS = CRS.getHorizontalCRS(requestedBBox.getCoordinateReferenceSystem());

        //
        // Check if the request CRS is different from the coverage native CRS
        //
        if (!CRS.equalsIgnoreMetadata(requestCRS, coverageProperties.crs2D))
            try {
                destinationToSourceTransform =
                        CRS.findMathTransform(requestCRS, coverageProperties.crs2D, true);
            } catch (FactoryException e) {
                throw new DataSourceException("Unable to inspect request CRS", e);
            }
        // now transform the requested envelope to source crs
        if (destinationToSourceTransform != null) {
            if (destinationToSourceTransform.isIdentity()) {

                // the CRS is basically the same
                destinationToSourceTransform = null;

                // we need to use the coverage one for the requested bbox to avoid problems later on
                this.requestedBBox =
                        new ReferencedEnvelope(requestedBBox, coverageProperties.crs2D);
            } else {
                // we do need to reproject
                needsReprojection = true;

                //
                // k, the transformation between the various CRS is not null or the
                // Identity, let's see if it is an affine transform, which case we
                // can incorporate it into the requested grid to world
                if (destinationToSourceTransform instanceof AffineTransform) {

                    //
                    // we should not have any problems with regards to BBOX reprojection
                    // update the requested grid to world transformation by pre concatenating the
                    // destination to source transform
                    AffineTransform mutableTransform =
                            (AffineTransform) requestedGridToWorld.clone();
                    mutableTransform.preConcatenate((AffineTransform) destinationToSourceTransform);

                    // update the requested envelope
                    try {
                        final MathTransform tempTransform =
                                PixelTranslation.translate(
                                        ProjectiveTransform.create(mutableTransform),
                                        PixelInCell.CELL_CENTER,
                                        PixelInCell.CELL_CORNER);
                        requestedBBox =
                                new ReferencedEnvelope(
                                        CRS.transform(
                                                tempTransform,
                                                new GeneralEnvelope(requestedRasterArea)));

                    } catch (Exception e) {
                        throw new DataSourceException("Unable to inspect request CRS", e);
                    }

                    // now clean up all the traces of the transformations
                    destinationToSourceTransform = null;
                    needsReprojection = false;
                }
            }
        }
    }

    /**
     * Return a crop region from a specified envelope, leveraging on the grid to world
     * transformation.
     */
    private void computeRasterArea() throws DataSourceException {

        // we have nothing to crop
        if (emptyRequest || computedBBox == null) {
            throw new IllegalStateException(
                    "IllegalState, unable to compute raster area for null bbox");
        }

        try {
            //
            // We need to invert the requested gridToWorld and then adjust the requested raster area
            // are accordingly
            //

            // invert the requested grid to world keeping into account the fact that it is related
            // to cell center
            // while the raster is related to cell corner
            MathTransform2D requestedWorldToGrid =
                    (MathTransform2D)
                            PixelTranslation.translate(
                                            ProjectiveTransform.create(requestedGridToWorld),
                                            PixelInCell.CELL_CENTER,
                                            PixelInCell.CELL_CORNER)
                                    .inverse();

            if (!needsReprojection) {

                // now get the requested bbox which have been already adjusted and project it back
                // to raster space
                computedRasterArea =
                        new GeneralGridEnvelope(
                                        CRS.transform(
                                                requestedWorldToGrid,
                                                new GeneralEnvelope(computedBBox)),
                                        PixelInCell.CELL_CORNER,
                                        false)
                                .toRectangle();

            } else {
                //
                // reproject the crop bbox back in the requested crs and then crop, notice that we
                // are imposing
                // the same raster area somehow
                //
                Rectangle computedRasterArea =
                        computeRasterArea(computedBBox, requestedWorldToGrid);
                this.computedRasterArea = computedRasterArea;
            }
        } catch (Exception e) {
            throw new DataSourceException(e);
        }

        // is it empty??
        if (computedRasterArea.isEmpty()) {

            // TODO: Future versions may define a 1x1 rectangle starting
            // from the lower coordinate
            emptyRequest = true;
            return;
        }
    }

    private Rectangle computeRasterArea(
            ReferencedEnvelope computedBBox, MathTransform2D requestedWorldToGrid)
            throws TransformException, FactoryException {
        final ReferencedEnvelope cropBBOXInRequestCRS =
                Utils.reprojectEnvelope(computedBBox, requestCRS, requestedBBox);
        // make sure it falls within the requested envelope
        cropBBOXInRequestCRS.intersection((org.locationtech.jts.geom.Envelope) requestedBBox);

        // now go back to raster space
        Rectangle computedRasterArea =
                new GeneralGridEnvelope(
                                CRS.transform(requestedWorldToGrid, cropBBOXInRequestCRS),
                                PixelInCell.CELL_CORNER,
                                false)
                        .toRectangle();
        // intersect with the original requested raster space to be sure that we stay within
        // the requested raster area
        XRectangle2D.intersect(computedRasterArea, requestedRasterArea, computedRasterArea);
        return computedRasterArea;
    }

    /**
     * Computes the requested resolution which is going to be used for selecting overviews and or
     * deciding decimation factors on the target coverage.
     *
     * <p>In case the requested envelope is in the same {@link CoordinateReferenceSystem} of the
     * coverage we compute the resolution using the requested {@link MathTransform}. Notice that it
     * must be a {@link LinearTransform} or else we fail.
     *
     * <p>In case the requested envelope is not in the same {@link CoordinateReferenceSystem} of the
     * coverage we do an in place reprojection.
     */
    private void computeResolution() {

        try {

            //
            // the crs of the request and the one of the coverage are NOT the
            // same and the conversion is not , we can get the resolution from envelope + raster
            // directly
            //
            GridGeometry2D gridGeometry;
            if (needsReprojection) {
                final GridToEnvelopeMapper geMapper =
                        new GridToEnvelopeMapper(
                                new GridEnvelope2D(computedRasterArea), computedBBox);
                computedGridToWorld = geMapper.createAffineTransform();
                if (accurateResolution) {
                    gridGeometry = requestedGridGeometry;
                } else {
                    gridGeometry =
                            new GridGeometry2D(
                                    new GridEnvelope2D(computedRasterArea), computedBBox);
                }
            } else {
                gridGeometry = requestedGridGeometry;
                computedGridToWorld = requestedGridToWorld;
            }

            ReadResolutionCalculator calculator =
                    new ReadResolutionCalculator(
                            gridGeometry,
                            coverageProperties.crs2D,
                            coverageProperties.fullResolution);
            calculator.setAccurateResolution(accurateResolution);
            computedResolution =
                    calculator.computeRequestedResolution(
                            ReferencedEnvelope.reference(computedBBox));

            // leave
            return;
        } catch (Throwable e) {
            if (LOGGER.isLoggable(Level.INFO))
                LOGGER.log(Level.INFO, "Unable to compute requested resolution", e);
        }

        //
        // use the coverage resolution since we cannot compute the requested one, this can be
        // problematic but at least keep us going
        //
        LOGGER.log(
                Level.WARNING, "Unable to compute requested resolution, using highest available");
        computedResolution = coverageProperties.fullResolution;
    }

    private void computeCropBBOX() throws DataSourceException {

        try {

            //
            // The destination to source transform has been computed (and eventually erased) already
            // by inspectCoordinateSystem()

            // now transform the requested envelope to source crs
            if (needsReprojection) {
                try {
                    this.computedBBox =
                            Utils.reprojectEnvelope(
                                    requestedBBox,
                                    coverageProperties.crs2D,
                                    coverageProperties.bbox);
                } catch (FactoryException e) {
                    throw new DataSourceException(e);
                }
            } else {
                // we do not need to do anything, but we do this in order to avoid problems with the
                // envelope checks
                computedBBox = new ReferencedEnvelope(requestedBBox);
            }

            // intersect requested BBox in native CRS with coverage native bbox to get the crop bbox
            // intersect the requested area with the bounds of this layer in native crs
            if (!computedBBox.intersects((BoundingBox) coverageProperties.bbox)) {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.fine(
                            new StringBuilder("The computed CropBoundingBox ")
                                    .append(computedBBox)
                                    .append(" Doesn't intersect the coverage BoundingBox ")
                                    .append(coverageProperties.bbox)
                                    .append(" resulting in an empty request")
                                    .toString());
                }
                computedBBox = null;
                emptyRequest = true;
                return;
            }

            // TODO XXX Optimize when referenced envelope has intersection method that actually
            // retains the CRS, this is the JTS one
            computedBBox =
                    new ReferencedEnvelope(
                            computedBBox.intersection(coverageProperties.bbox),
                            coverageProperties.crs2D);
            if (computedBBox.isEmpty()) {
                // this means that we do not have anything to load at all!
                emptyRequest = true;
            }
            return;
        } catch (TransformException te) {
            // something bad happened while trying to transform this
            // envelope. let's try with wgs84
            if (LOGGER.isLoggable(Level.FINE)) LOGGER.log(Level.FINE, te.getLocalizedMessage(), te);
        }

        try {
            // can we proceed? Do we have geo stuff to do all these operations?
            if (coverageProperties.geographicCRS != null
                    && coverageProperties.geographicBBox != null) {

                //
                // If we can not reproject the requested envelope to the native CRS,
                // we go back to reproject in the geographic crs of the native
                // coverage since this usually happens for conversions between CRS
                // whose area of definition is different
                //

                // STEP 1 reproject the requested envelope to the coverage geographic bbox
                if (!CRS.equalsIgnoreMetadata(coverageProperties.geographicCRS, requestCRS)) {
                    // try to convert the requested bbox to the coverage geocrs
                    requestedBBOXInCoverageGeographicCRS =
                            CRS.transform(requestedBBox, coverageProperties.geographicCRS);
                    requestedBBOXInCoverageGeographicCRS.setCoordinateReferenceSystem(
                            coverageProperties.geographicCRS);
                }
                if (requestedBBOXInCoverageGeographicCRS == null) {
                    requestedBBOXInCoverageGeographicCRS = new GeneralEnvelope(requestCRS);
                }

                // STEP 2 intersection with the geographic bbox for this coverage
                if (!requestedBBOXInCoverageGeographicCRS.intersects(
                        coverageProperties.geographicBBox, true)) {
                    computedBBox = null;
                    emptyRequest = true;
                    return;
                }
                // intersect with the coverage native geographic bbox
                // note that for the moment we got to use general envelope since there is no
                // intersection otherwise
                requestedBBOXInCoverageGeographicCRS.intersect(coverageProperties.geographicBBox);
                requestedBBOXInCoverageGeographicCRS.setCoordinateReferenceSystem(
                        coverageProperties.geographicCRS);

                // now go back to the coverage native CRS in order to compute an approximate
                // requested resolution
                approximateRequestedBBoInNativeCRS =
                        CRS.transform(
                                requestedBBOXInCoverageGeographicCRS, coverageProperties.crs2D);
                approximateRequestedBBoInNativeCRS.setCoordinateReferenceSystem(
                        coverageProperties.crs2D);
                computedBBox = new ReferencedEnvelope(approximateRequestedBBoInNativeCRS);
                return;
            }

        } catch (Exception e) {
            // something bad happened while trying to transform this
            // envelope. let's try with wgs84
            if (LOGGER.isLoggable(Level.FINE)) LOGGER.log(Level.FINE, e.getLocalizedMessage(), e);
        }

        LOGGER.log(
                Level.INFO,
                "We did not manage to crop the requested envelope, we fall back onto loading the whole coverage.");
        computedBBox = null;
    }

    public boolean isEmpty() {
        return emptyRequest;
    }

    public boolean isNeedsReprojection() {
        return needsReprojection;
    }

    public boolean isAccurateResolution() {
        return accurateResolution;
    }

    public void setAccurateResolution(boolean accurateResolution) {
        this.accurateResolution = accurateResolution;
    }

    public double[] getComputedResolution() {
        return computedResolution != null ? computedResolution.clone() : null;
    }

    public Rectangle getComputedRasterArea() {
        return (Rectangle)
                (computedRasterArea != null ? computedRasterArea.clone() : computedRasterArea);
    }

    public BoundingBox getComputedBBox() {
        return computedBBox;
    }

    public BoundingBox getCoverageBBox() {
        return coverageProperties.getBbox();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("SpatialRequestHelper [");
        if (requestedBBox != null) {
            builder.append("requestedBBox=");
            builder.append(requestedBBox);
            builder.append(", ");
        }
        if (computedBBox != null) {
            builder.append("cropBBox=");
            builder.append(computedBBox);
            builder.append(", ");
        }
        if (requestedRasterArea != null) {
            builder.append("requestedRasterArea=");
            builder.append(requestedRasterArea);
            builder.append(", ");
        }
        if (computedRasterArea != null) {
            builder.append("destinationRasterArea=");
            builder.append(computedRasterArea);
            builder.append(", ");
        }
        if (requestCRS != null) {
            builder.append("requestCRS=");
            builder.append(requestCRS);
            builder.append(", ");
        }
        if (requestedGridToWorld != null) {
            builder.append("requestedGridToWorld=");
            builder.append(requestedGridToWorld);
            builder.append(", ");
        }
        if (computedResolution != null) {
            builder.append("requestedResolution=");
            builder.append(Arrays.toString(computedResolution));
            builder.append(", ");
        }
        if (requestedBBOXInCoverageGeographicCRS != null) {
            builder.append("requestedBBOXInCoverageGeographicCRS=");
            builder.append(requestedBBOXInCoverageGeographicCRS);
            builder.append(", ");
        }
        if (requestCRSToCoverageGeographicCRS2D != null) {
            builder.append("requestCRSToCoverageGeographicCRS2D=");
            builder.append(requestCRSToCoverageGeographicCRS2D);
            builder.append(", ");
        }
        if (destinationToSourceTransform != null) {
            builder.append("destinationToSourceTransform=");
            builder.append(destinationToSourceTransform);
            builder.append(", ");
        }
        if (coverageProperties != null) {
            builder.append("coverageProperties=");
            builder.append(coverageProperties);
            builder.append(", ");
        }
        builder.append("accurateResolution=");
        builder.append(accurateResolution);
        builder.append(", empty=");
        builder.append(emptyRequest);
        builder.append(", needsReprojection=");
        builder.append(needsReprojection);
        builder.append(", ");
        if (approximateRequestedBBoInNativeCRS != null) {
            builder.append("approximateRequestedBBoInNativeCRS=");
            builder.append(approximateRequestedBBoInNativeCRS);
        }
        builder.append("]");
        return builder.toString();
    }

    public AffineTransform getComputedGridToWorld() {
        return computedGridToWorld;
    }
}
