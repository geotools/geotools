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
package org.geotools.coverage.io;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.geotools.api.data.DataSourceException;
import org.geotools.api.geometry.BoundingBox;
import org.geotools.api.geometry.Bounds;
import org.geotools.api.geometry.MismatchedDimensionException;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.datum.PixelInCell;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.api.referencing.operation.MathTransform2D;
import org.geotools.api.referencing.operation.NoninvertibleTransformException;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.coverage.grid.GeneralGridEnvelope;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.geometry.GeneralBounds;
import org.geotools.geometry.PixelTranslation;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.geometry.util.XRectangle2D;
import org.geotools.metadata.i18n.ErrorKeys;
import org.geotools.referencing.CRS;
import org.geotools.referencing.operation.LinearTransform;
import org.geotools.referencing.operation.builder.GridToEnvelopeMapper;
import org.geotools.referencing.operation.matrix.XAffineTransform;
import org.geotools.referencing.operation.transform.ProjectiveTransform;
import org.geotools.util.Utilities;

/**
 * Helper class which takes coverage's spatial information input (CRS, bbox, resolution,...) and a set of request's
 * parameters (requestedCRS, requestedBBox, requested resolution, ...) and takes care of computing all auxiliary spatial
 * variables for future computations.
 *
 * @author Daniele Romagnoli, GeoSolutions SAS
 */
public class SpatialRequestHelper {

    public static class CoverageProperties {
        public ReferencedEnvelope getBbox() {
            return bbox;
        }

        public void setBbox(ReferencedEnvelope bbox) {
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
            return geographicCRS2D;
        }

        public void setGeographicCRS2D(CoordinateReferenceSystem geographicCRS2D) {
            this.geographicCRS2D = geographicCRS2D;
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

        CoordinateReferenceSystem geographicCRS2D;
    }

    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(SpatialRequestHelper.class);

    /** The {@link BoundingBox} requested */
    BoundingBox requestedBBox;

    /** The {@link BoundingBox} of the portion of the coverage that intersects the requested bbox */
    BoundingBox cropBBox;

    /** The region where to fit the requested envelope */
    Rectangle requestedRasterArea;

    /** The region of the */
    Rectangle destinationRasterArea;

    CoordinateReferenceSystem requestCRS;

    AffineTransform requestedGridToWorld;

    double[] requestedResolution;

    GeneralBounds requestedBBOXInCoverageGeographicCRS;

    MathTransform requestCRSToCoverageGeographicCRS2D;

    MathTransform destinationToSourceTransform;

    CoverageProperties coverageProperties;

    /**
     * Set to {@code true} if this request will produce an empty result, and the coverageResponse will produce a
     * {@code null} coverage.
     */
    boolean empty;

    boolean needsReprojection = false;

    GeneralBounds approximateRequestedBBoInNativeCRS;

    public void setRequestedGridGeometry(GridGeometry2D gridGeometry) {
        Utilities.ensureNonNull("girdGeometry", gridGeometry);
        requestedBBox = new ReferencedEnvelope((Bounds) gridGeometry.getEnvelope2D());
        requestedRasterArea = gridGeometry.getGridRange2D().getBounds();
        requestedGridToWorld = (AffineTransform) gridGeometry.getGridToCRS2D();
    }

    public void setCoverageProperties(final CoverageProperties coverageProperties) {
        this.coverageProperties = coverageProperties;
    }

    /**
     * Compute this specific request settings all the parameters needed by a visiting {@link RasterLayerResponse}
     * object.
     */
    public void prepare() throws DataSourceException {
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
            requestedBBox = coverageProperties.bbox;
            cropBBox = coverageProperties.bbox;
            requestedRasterArea = (Rectangle) coverageProperties.rasterArea.clone();
            destinationRasterArea = (Rectangle) coverageProperties.rasterArea.clone();
            requestedResolution = coverageProperties.fullResolution.clone();
            // TODO harmonize the various types of transformations
            requestedGridToWorld = (AffineTransform) coverageProperties.gridToWorld2D;
            return;
        }

        //
        // Adjust requested bounding box and source region in order to fall within the source
        // coverage
        //
        computeRequestSpatialElements();
    }

    /**
     * Evaluates the requested envelope and builds a new adjusted version of it fitting this coverage envelope.
     *
     * <p>While adjusting the requested envelope this methods also compute the source region as a rectangle which is
     * suitable for a successive read operation with {@link ImageIO} to do crop-on-read.
     *
     * @throws DataSourceException in case something bad occurs
     */
    private void computeRequestSpatialElements() throws DataSourceException {

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
        // WE DO HAVE A REQUESTED AREA!
        //

        //
        // Create the crop bbox in the coverage CRS for cropping it later on.
        //
        computeCropBBOX();
        if (empty || cropBBox != null && cropBBox.isEmpty()) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "RequestedBBox empty or null");
            }
            // this means that we do not have anything to load at all!
            empty = true;
            return;
        }

        //
        // CROP SOURCE REGION using the refined requested envelope
        //
        computeCropRasterArea();
        if (empty || destinationRasterArea != null && destinationRasterArea.isEmpty()) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "CropRasterArea empty or null");
            }
            // this means that we do not have anything to load at all!
            return;
        }

        if (LOGGER.isLoggable(Level.FINER)) {
            StringBuilder sb = new StringBuilder("Adjusted Requested Envelope = ")
                    .append(requestedBBox.toString())
                    .append("\n")
                    .append("Requested raster dimension = ")
                    .append(requestedRasterArea.toString())
                    .append("\n")
                    .append("Corresponding raster source region = ")
                    .append(requestedRasterArea.toString());
            LOGGER.log(Level.FINER, sb.toString());
        }

        //
        // Compute the request resolution from the request
        //
        computeRequestedResolution();
    }

    private void inspectCoordinateReferenceSystems() throws DataSourceException {
        // get the crs for the requested bbox
        requestCRS = CRS.getHorizontalCRS(requestedBBox.getCoordinateReferenceSystem());

        //
        // Check if the request CRS is different from the coverage native CRS
        //
        if (!CRS.equalsIgnoreMetadata(requestCRS, coverageProperties.crs2D))
            try {
                destinationToSourceTransform = CRS.findMathTransform(requestCRS, coverageProperties.crs2D, true);
            } catch (FactoryException e) {
                throw new DataSourceException("Unable to inspect request CRS", e);
            }
        // now transform the requested envelope to source crs
        if (destinationToSourceTransform != null && destinationToSourceTransform.isIdentity()) {
            destinationToSourceTransform = null; // the CRS is basically the same
        } else {
            needsReprojection = true;
            if (destinationToSourceTransform instanceof AffineTransform) {
                //
                // k, the transformation between the various CRS is not null or the
                // Identity, let's see if it is an affine transform, which case we
                // can incorporate it into the requested grid to world
                //
                // we should not have any problems with regards to BBOX reprojection
                // update the requested grid to world transformation by pre concatenating the
                // destination to source transform
                AffineTransform mutableTransform = (AffineTransform) requestedGridToWorld.clone();
                mutableTransform.preConcatenate((AffineTransform) destinationToSourceTransform);

                // update the requested envelope
                try {
                    final MathTransform tempTransform = PixelTranslation.translate(
                            ProjectiveTransform.create(mutableTransform),
                            PixelInCell.CELL_CENTER,
                            PixelInCell.CELL_CORNER);
                    requestedBBox = new ReferencedEnvelope(
                            CRS.transform(tempTransform, new GeneralBounds(requestedRasterArea)));

                } catch (MismatchedDimensionException | TransformException e) {
                    throw new DataSourceException("Unable to inspect request CRS", e);
                }

                // now clean up all the traces of the transformations
                destinationToSourceTransform = null;
                needsReprojection = false;
            }
        }
    }

    /**
     * Return a crop region from a specified envelope, leveraging on the grid to world transformation.
     *
     * @throws TransformException in case a problem occurs when going back to raster space.
     */
    private void computeCropRasterArea() throws DataSourceException {

        // we have nothing to crop
        if (cropBBox == null) {
            destinationRasterArea = null;
            return;
        }

        //
        // We need to invert the requested gridToWorld and then adjust the requested raster area are
        // accordingly
        //

        // invert the requested grid to world keeping into account the fact that it is related to
        // cell center
        // while the raster is related to cell corner
        MathTransform2D requestedWorldToGrid;
        try {
            requestedWorldToGrid = (MathTransform2D) PixelTranslation.translate(
                            ProjectiveTransform.create(requestedGridToWorld),
                            PixelInCell.CELL_CENTER,
                            PixelInCell.CELL_CORNER)
                    .inverse();
        } catch (NoninvertibleTransformException e) {
            throw new DataSourceException(e);
        }

        if (destinationToSourceTransform == null || destinationToSourceTransform.isIdentity()) {

            // now get the requested bbox which have been already adjusted and project it back to
            // raster space
            try {
                destinationRasterArea = new GeneralGridEnvelope(
                                CRS.transform(requestedWorldToGrid, new GeneralBounds(cropBBox)),
                                PixelInCell.CELL_CORNER,
                                false)
                        .toRectangle();
            } catch (IllegalStateException | TransformException e) {
                throw new DataSourceException(e);
            }
        } else {
            //
            // reproject the crop bbox back and then crop, notice that we are imposing
            //
            try {
                final GeneralBounds cropBBOXInRequestCRS =
                        CRS.transform(cropBBox, requestedBBox.getCoordinateReferenceSystem());
                cropBBOXInRequestCRS.setCoordinateReferenceSystem(requestedBBox.getCoordinateReferenceSystem());
                // make sure it falls within the requested envelope
                cropBBOXInRequestCRS.intersect(requestedBBox);

                // now go back to raster space
                destinationRasterArea = new GeneralGridEnvelope(
                                CRS.transform(requestedWorldToGrid, cropBBOXInRequestCRS),
                                PixelInCell.CELL_CORNER,
                                false)
                        .toRectangle();
                // intersect with the original requested raster space to be sure that we stay within
                // the requested raster area
                XRectangle2D.intersect(destinationRasterArea, requestedRasterArea, destinationRasterArea);
            } catch (TransformException e) {
                throw new DataSourceException(e);
            }
        }
        // is it empty??
        if (destinationRasterArea.isEmpty()) {
            if (LOGGER.isLoggable(Level.FINE))
                LOGGER.log(
                        Level.FINE,
                        "Requested envelope too small resulting in empty cropped raster region. cropBbox:" + cropBBox);
            // TODO: Future versions may define a 1x1 rectangle starting
            // from the lower coordinate
            empty = true;
        }
    }

    /**
     * Computes the requested resolution which is going to be used for selecting overviews and or deciding decimation
     * factors on the target coverage.
     *
     * <p>In case the requested envelope is in the same {@link CoordinateReferenceSystem} of the coverage we compute the
     * resolution using the requested {@link MathTransform}. Notice that it must be a {@link LinearTransform} or else we
     * fail.
     *
     * <p>In case the requested envelope is not in the same {@link CoordinateReferenceSystem} of the coverage we
     *
     * @throws DataSourceException in case something bad happens during reprojections and/or intersections.
     */
    private void computeRequestedResolution() throws DataSourceException {

        try {

            // let's try to get the resolution from the requested gridToWorld
            if (requestedGridToWorld instanceof LinearTransform) {

                //
                // the crs of the request and the one of the coverage are NOT the
                // same and the conversion is not , we can get the resolution from envelope + raster
                // directly
                //
                if (destinationToSourceTransform != null && !destinationToSourceTransform.isIdentity()) {

                    //
                    // compute the approximated resolution in the request crs, notice that we are
                    // assuming a reprojection that keeps the raster area unchanged hence
                    // the effect is a degradation of quality, but we might take that into account
                    // emprically
                    //
                    requestedResolution = null;
                    //
                    // // compute the raster that correspond to the crop bbox at the highest
                    // resolution
                    // final Rectangle sourceRasterArea = new GeneralGridEnvelope(
                    // CRS.transform(
                    // PixelTranslation.translate(rasterManager.getRaster2Model(),PixelInCell.CELL_CENTER,PixelInCell.CELL_CORNER).inverse(),
                    // cropBBox),PixelInCell.CELL_CORNER,false).toRectangle();
                    // XRectangle2D.intersect(sourceRasterArea,
                    // rasterManager.spatialDomainManager.coverageRasterArea, sourceRasterArea);
                    // if(sourceRasterArea.isEmpty())
                    // throw new DataSourceException("The request source raster area is empty");

                    final GridToEnvelopeMapper geMapper =
                            new GridToEnvelopeMapper(new GridEnvelope2D(destinationRasterArea), cropBBox);
                    final AffineTransform tempTransform = geMapper.createAffineTransform();
                    // final double scaleX=XAffineTransform.getScaleX0((AffineTransform)
                    // requestedGridToWorld)/XAffineTransform.getScaleX0(tempTransform);
                    // final double scaleY=XAffineTransform.getScaleY0((AffineTransform)
                    // requestedGridToWorld)/XAffineTransform.getScaleY0(tempTransform);
                    // //
                    // // empiric adjustment to get a finer resolution to have better quality when
                    // reprojecting
                    // // TODO make it parametric
                    // //
                    // requestedRasterScaleFactors= new double[2];
                    // requestedRasterScaleFactors[0]=scaleX*1.0;
                    // requestedRasterScaleFactors[1]=scaleY*1.0;

                    requestedResolution = new double[] {
                        XAffineTransform.getScaleX0(tempTransform), XAffineTransform.getScaleY0(tempTransform)
                    };

                } else {

                    // the crs of the request and the one of the coverage are the
                    // same, we can get the resolution from the grid to world
                    requestedResolution = new double[] {
                        XAffineTransform.getScaleX0(requestedGridToWorld),
                        XAffineTransform.getScaleY0(requestedGridToWorld)
                    };
                }
            } else
            // should not happen
            {
                final Object arg0 = requestedGridToWorld.toString();
                throw new UnsupportedOperationException(MessageFormat.format(ErrorKeys.UNSUPPORTED_OPERATION_$1, arg0));
            }

            // leave
            return;
        } catch (Throwable e) {
            if (LOGGER.isLoggable(Level.INFO)) LOGGER.log(Level.INFO, "Unable to compute requested resolution", e);
        }

        //
        // use the coverage resolution since we cannot compute the requested one
        //
        LOGGER.log(Level.WARNING, "Unable to compute requested resolution, using highest available");
        requestedResolution = coverageProperties.fullResolution;
    }

    private void computeCropBBOX() throws DataSourceException {

        // get the crs for the requested bbox
        if (requestCRS == null) requestCRS = CRS.getHorizontalCRS(requestedBBox.getCoordinateReferenceSystem());
        try {

            //
            // The destination to source transform has been computed (and eventually erased) already
            // by inspectCoordinateSystem()

            // now transform the requested envelope to source crs
            if (destinationToSourceTransform != null && !destinationToSourceTransform.isIdentity()) {
                final GeneralBounds temp = new GeneralBounds(CRS.transform(requestedBBox, coverageProperties.crs2D));
                temp.setCoordinateReferenceSystem(coverageProperties.crs2D);
                cropBBox = new ReferencedEnvelope(temp);
                needsReprojection = true;

            } else {
                // we do not need to do anything, but we do this in order to aboid problems with the
                // envelope checks
                cropBBox = new ReferencedEnvelope(
                        requestedBBox.getMinX(),
                        requestedBBox.getMaxX(),
                        requestedBBox.getMinY(),
                        requestedBBox.getMaxY(),
                        coverageProperties.crs2D);
            }

            // intersect requested BBox in native CRS with coverage native bbox to get the crop bbox
            // intersect the requested area with the bounds of this layer in native crs
            if (!cropBBox.intersects(coverageProperties.bbox)) {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.fine(new StringBuilder("The computed CropBoundingBox ")
                            .append(cropBBox)
                            .append(" Doesn't intersect the coverage BoundingBox ")
                            .append(coverageProperties.bbox)
                            .append(" resulting in an empty request")
                            .toString());
                }
                cropBBox = null;
                empty = true;
                return;
            }
            // TODO XXX Optimize when referenced envelope has intersection method that actually
            // retains the CRS, this is the JTS one
            cropBBox = new ReferencedEnvelope(
                    ((ReferencedEnvelope) cropBBox).intersection(coverageProperties.bbox), coverageProperties.crs2D);

            return;
        } catch (TransformException te) {
            // something bad happened while trying to transform this
            // envelope. let's try with wgs84
            if (LOGGER.isLoggable(Level.FINE)) LOGGER.log(Level.FINE, te.getLocalizedMessage(), te);
        }

        try {
            // can we proceed? Do we have geo stuff to do all these operations?
            if (coverageProperties.geographicCRS2D != null && coverageProperties.geographicBBox != null) {

                //
                // If we can not reproject the requested envelope to the native CRS,
                // we go back to reproject in the geographic crs of the native
                // coverage since this usually happens for conversions between CRS
                // whose area of definition is different
                //

                // STEP 1 reproject the requested envelope to the coverage geographic bbox
                if (!CRS.equalsIgnoreMetadata(coverageProperties.geographicCRS2D, requestCRS)) {
                    // try to convert the requested bbox to the coverage geocrs
                    requestCRSToCoverageGeographicCRS2D =
                            CRS.findMathTransform(requestCRS, coverageProperties.geographicCRS2D, true);
                    if (!requestCRSToCoverageGeographicCRS2D.isIdentity()) {
                        requestedBBOXInCoverageGeographicCRS =
                                CRS.transform(requestedBBox, coverageProperties.geographicCRS2D);
                        requestedBBOXInCoverageGeographicCRS.setCoordinateReferenceSystem(
                                coverageProperties.geographicCRS2D);
                    }
                }
                if (requestedBBOXInCoverageGeographicCRS == null) {
                    requestedBBOXInCoverageGeographicCRS = new GeneralBounds(requestCRS);
                }

                // STEP 2 intersection with the geographic bbox for this coverage
                if (!requestedBBOXInCoverageGeographicCRS.intersects(coverageProperties.geographicBBox, true)) {
                    cropBBox = null;
                    empty = true;
                    return;
                }
                // intersect with the coverage native geographic bbox
                // note that for the moment we got to use general envelope since there is no
                // intersection otherwise
                requestedBBOXInCoverageGeographicCRS.intersect(coverageProperties.geographicBBox);
                requestedBBOXInCoverageGeographicCRS.setCoordinateReferenceSystem(coverageProperties.geographicCRS2D);

                // now go back to the coverage native CRS in order to compute an approximate
                // requested resolution
                approximateRequestedBBoInNativeCRS =
                        CRS.transform(requestedBBOXInCoverageGeographicCRS, coverageProperties.crs2D);
                approximateRequestedBBoInNativeCRS.setCoordinateReferenceSystem(coverageProperties.crs2D);
                cropBBox = new ReferencedEnvelope(approximateRequestedBBoInNativeCRS);
                return;
            }

        } catch (TransformException | FactoryException te) {
            // something bad happened while trying to transform this
            // envelope. let's try with wgs84
            if (LOGGER.isLoggable(Level.FINE)) LOGGER.log(Level.FINE, te.getLocalizedMessage(), te);
        }

        LOGGER.log(
                Level.INFO,
                "We did not manage to crop the requested envelope, we fall back onto loading the whole coverage.");
        cropBBox = null;
    }

    public boolean isEmpty() {
        return empty;
    }

    public boolean isNeedsReprojection() {
        return needsReprojection;
    }

    public BoundingBox getRequestedBBox() {
        return requestedBBox;
    }

    public Rectangle getRequestedRasterArea() {
        return (Rectangle) (requestedRasterArea != null ? requestedRasterArea.clone() : requestedRasterArea);
    }

    public double[] getRequestedResolution() {
        return requestedResolution != null ? requestedResolution.clone() : null;
    }

    public Rectangle getDestinationRasterArea() {
        return destinationRasterArea;
    }

    public BoundingBox getCropBBox() {
        return cropBBox;
    }

    public AffineTransform getRequestedGridToWorld() {
        return requestedGridToWorld;
    }

    public void setRequestedBBox(BoundingBox requestedBBox) {
        this.requestedBBox = requestedBBox;
    }

    public void setRequestedRasterArea(Rectangle requestedRasterArea) {
        this.requestedRasterArea = requestedRasterArea;
    }

    public void setRequestedGridToWorld(AffineTransform requestedGridToWorld) {
        this.requestedGridToWorld = requestedGridToWorld;
    }

    public CoverageProperties getCoverageProperties() {
        return coverageProperties;
    }

    @Override
    public String toString() {
        return "SpatialRequestHelper [requestedBBox="
                + requestedBBox
                + ", cropBBox="
                + cropBBox
                + ", requestedRasterArea="
                + requestedRasterArea
                + ", destinationRasterArea="
                + destinationRasterArea
                + ", requestCRS="
                + requestCRS
                + ", requestedGridToWorld="
                + requestedGridToWorld
                + ", requestedResolution="
                + Arrays.toString(requestedResolution)
                + ", requestedBBOXInCoverageGeographicCRS="
                + requestedBBOXInCoverageGeographicCRS
                + ", requestCRSToCoverageGeographicCRS2D="
                + requestCRSToCoverageGeographicCRS2D
                + ", destinationToSourceTransform="
                + destinationToSourceTransform
                + ", coverageProperties="
                + coverageProperties
                + ", empty="
                + empty
                + ", needsReprojection="
                + needsReprojection
                + ", approximateRequestedBBoInNativeCRS="
                + approximateRequestedBBoInNativeCRS
                + "]";
    }
}
