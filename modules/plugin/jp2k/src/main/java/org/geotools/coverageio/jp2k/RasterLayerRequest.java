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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.text.MessageFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.geotools.api.data.DataSourceException;
import org.geotools.api.geometry.BoundingBox;
import org.geotools.api.geometry.Bounds;
import org.geotools.api.geometry.MismatchedDimensionException;
import org.geotools.api.metadata.Identifier;
import org.geotools.api.parameter.GeneralParameterDescriptor;
import org.geotools.api.parameter.GeneralParameterValue;
import org.geotools.api.parameter.ParameterDescriptor;
import org.geotools.api.parameter.ParameterValue;
import org.geotools.api.parameter.ParameterValueGroup;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.ReferenceIdentifier;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.crs.SingleCRS;
import org.geotools.api.referencing.datum.PixelInCell;
import org.geotools.api.referencing.operation.CoordinateOperation;
import org.geotools.api.referencing.operation.CoordinateOperationFactory;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.api.referencing.operation.MathTransform2D;
import org.geotools.api.referencing.operation.NoninvertibleTransformException;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.coverage.grid.GeneralGridEnvelope;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.OverviewPolicy;
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
import org.geotools.util.factory.Hints;

/**
 * A class to handle coverage requests to a reader for a single 2D layer..
 *
 * @author Daniele Romagnoli, GeoSolutions
 * @author Simone Giannecchini, GeoSolutions
 */
class RasterLayerRequest {

    /** Logger. */
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(RasterLayerRequest.class);

    private ReadType readType =
            AbstractGridFormat.USE_JAI_IMAGEREAD.getDefaultValue() ? ReadType.JAI_IMAGEREAD : ReadType.DIRECT_READ;

    /** The {@link BoundingBox} requested */
    private BoundingBox requestedBBox;

    /** The {@link BoundingBox} of the portion of the coverage that intersects the requested bbox */
    private BoundingBox cropBBox;

    /** The desired overview Policy for this request */
    private OverviewPolicy overviewPolicy;

    /** The region where to fit the requested envelope */
    private Rectangle requestedRasterArea;

    /** The region of the destination area */
    private Rectangle destinationRasterArea;

    /** Requesting a multithreading read */
    private boolean useMultithreading;

    public boolean isUseMultithreading() {
        return useMultithreading;
    }

    /**
     * Set to {@code true} if this request will produce an empty result, and the coverageResponse will produce a
     * {@code null} coverage.
     */
    private boolean empty;

    private Color inputTransparentColor = JP2KFormat.INPUT_TRANSPARENT_COLOR.getDefaultValue();

    private AffineTransform requestedGridToWorld;

    private double[] requestedResolution;

    private RasterManager rasterManager;

    private MathTransform destinationToSourceTransform;

    private GeneralBounds requestedBBOXInCoverageGeographicCRS;

    private GeneralBounds approximateRequestedBBoInNativeCRS;

    private Dimension tileDimensions;

    private SingleCRS requestCRS;

    /**
     * Build a new {@code CoverageRequest} given a set of input parameters.
     *
     * @param params The {@code GeneralParameterValue}s to initialize this request
     */
    public RasterLayerRequest(final GeneralParameterValue[] params, final RasterManager rasterManager)
            throws DataSourceException {

        // //
        //
        // Setting default parameters
        //
        // //
        this.rasterManager = rasterManager;
        setDefaultParameterValues();

        // //
        //
        // Parsing parameter that can be used to control this request
        //
        // //
        if (params != null) {
            for (GeneralParameterValue gParam : params) {
                if (gParam instanceof ParameterValue<?>) {
                    final ParameterValue<?> param = (ParameterValue<?>) gParam;
                    final ReferenceIdentifier name = param.getDescriptor().getName();
                    extractParameter(param, name);
                }
            }
        }

        // //
        //
        // Set specific imageIO parameters: type of read operation,
        // imageReadParams
        //
        // //
        checkReadType();
        prepare();
    }

    private void setDefaultParameterValues() {
        final ParameterValueGroup readParams =
                this.rasterManager.parent.getFormat().getReadParameters();
        if (readParams == null) {
            if (LOGGER.isLoggable(Level.FINE)) LOGGER.fine("No default values for the read parameters!");
            return;
        }
        final List<GeneralParameterDescriptor> parametersDescriptors =
                readParams.getDescriptor().descriptors();
        for (GeneralParameterDescriptor descriptor : parametersDescriptors) {

            // we canc get the default vale only with the ParameterDescriptor class
            if (!(descriptor instanceof ParameterDescriptor)) continue;

            // get name and default value
            final ParameterDescriptor desc = (ParameterDescriptor) descriptor;
            final ReferenceIdentifier name = desc.getName();
            final Object value = desc.getDefaultValue();

            // //
            //
            // Requested GridGeometry2D parameter
            //
            // //
            if (descriptor.getName().equals(AbstractGridFormat.READ_GRIDGEOMETRY2D.getName())) {
                if (value == null) continue;
                final GridGeometry2D gg = (GridGeometry2D) value;

                requestedBBox = new ReferencedEnvelope((Bounds) gg.getEnvelope2D());
                requestedRasterArea = gg.getGridRange2D().getBounds();
                requestedGridToWorld = (AffineTransform) gg.getGridToCRS2D();
                continue;
            }

            // //
            //
            // Use JAI ImageRead parameter
            //
            // //
            if (name.equals(AbstractGridFormat.USE_JAI_IMAGEREAD.getName())) {
                if (value == null) continue;
                readType = (Boolean) value ? ReadType.JAI_IMAGEREAD : ReadType.DIRECT_READ;
                continue;
            }

            // //
            //
            // Use Multithreading parameter
            //
            // //
            if (name.equals(JP2KFormat.USE_MULTITHREADING.getName())) {
                if (value == null) continue;
                useMultithreading = ((Boolean) value).booleanValue();
                continue;
            }

            // //
            //
            // Overview Policy parameter
            //
            // //
            if (name.equals(AbstractGridFormat.OVERVIEW_POLICY.getName())) {
                if (value == null) continue;
                overviewPolicy = (OverviewPolicy) value;
                continue;
            }

            if (name.equals(JP2KFormat.INPUT_TRANSPARENT_COLOR.getName())) {
                if (value == null) continue;
                inputTransparentColor = (Color) value;
                // paranoiac check on the provided transparent color
                inputTransparentColor = new Color(
                        inputTransparentColor.getRed(),
                        inputTransparentColor.getGreen(),
                        inputTransparentColor.getBlue());
                continue;
            }

            // //
            //
            // Suggested tile size parameter. It must be specified with
            // the syntax: "TileWidth,TileHeight" (without quotes where TileWidth
            // and TileHeight are integer values)
            //
            // //
            if (name.equals(JP2KFormat.SUGGESTED_TILE_SIZE.getName())) {
                final String suggestedTileSize = (String) value;

                // Preliminary checks on parameter value
                if (suggestedTileSize != null && suggestedTileSize.trim().length() > 0) {

                    if (suggestedTileSize.contains(JP2KFormat.TILE_SIZE_SEPARATOR)) {
                        final String[] tilesSize = suggestedTileSize.split(JP2KFormat.TILE_SIZE_SEPARATOR);
                        if (tilesSize.length == 2) {
                            try {
                                // Getting suggested tile size
                                final int tileWidth = Integer.valueOf(tilesSize[0].trim());
                                final int tileHeight = Integer.valueOf(tilesSize[1].trim());
                                tileDimensions = new Dimension(tileWidth, tileHeight);
                            } catch (NumberFormatException nfe) {
                                if (LOGGER.isLoggable(Level.WARNING)) {
                                    LOGGER.log(Level.WARNING, "Unable to parse " + "suggested tile size parameter");
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Set proper fields from the specified input parameter.
     *
     * @param param the input {@code ParamaterValue} object
     * @param name the name of the parameter
     */
    private void extractParameter(ParameterValue<?> param, Identifier name) {

        // //
        //
        // Requested GridGeometry2D parameter
        //
        // //
        if (name.equals(AbstractGridFormat.READ_GRIDGEOMETRY2D.getName())) {
            final Object value = param.getValue();
            if (value == null) return;
            final GridGeometry2D gg = (GridGeometry2D) param.getValue();
            if (gg == null) {
                return;
            }

            requestedBBox = new ReferencedEnvelope((Bounds) gg.getEnvelope2D());
            requestedRasterArea = gg.getGridRange2D().getBounds();
            requestedGridToWorld = (AffineTransform) gg.getGridToCRS2D();
            return;
        }

        // //
        //
        // Use Multithreading parameter
        //
        // //
        if (name.equals(JP2KFormat.USE_MULTITHREADING.getName())) {
            useMultithreading = param.booleanValue();
            return;
        }

        // //
        //
        // Use JAI ImageRead parameter
        //
        // //
        if (name.equals(AbstractGridFormat.USE_JAI_IMAGEREAD.getName())) {
            final Object value = param.getValue();
            if (value == null) return;
            readType = param.booleanValue() ? ReadType.JAI_IMAGEREAD : ReadType.DIRECT_READ;
            return;
        }

        // //
        //
        // Overview Policy parameter
        //
        // //
        if (name.equals(AbstractGridFormat.OVERVIEW_POLICY.getName())) {
            final Object value = param.getValue();
            if (value == null) return;
            overviewPolicy = (OverviewPolicy) param.getValue();
            return;
        }

        if (name.equals(JP2KFormat.INPUT_TRANSPARENT_COLOR.getName())) {
            final Object value = param.getValue();
            if (value == null) return;
            inputTransparentColor = (Color) param.getValue();
            // paranoiac check on the provided transparent color
            inputTransparentColor = new Color(
                    inputTransparentColor.getRed(), inputTransparentColor.getGreen(), inputTransparentColor.getBlue());
            return;
        }

        // //
        //
        // Suggested tile size parameter. It must be specified with
        // the syntax: "TileWidth,TileHeight" (without quotes where TileWidth
        // and TileHeight are integer values)
        //
        // //
        if (name.equals(JP2KFormat.SUGGESTED_TILE_SIZE.getName())) {
            final String suggestedTileSize = (String) param.getValue();

            // Preliminary checks on parameter value
            if (suggestedTileSize != null && suggestedTileSize.trim().length() > 0) {

                if (suggestedTileSize.contains(JP2KFormat.TILE_SIZE_SEPARATOR)) {
                    final String[] tilesSize = suggestedTileSize.split(JP2KFormat.TILE_SIZE_SEPARATOR);
                    if (tilesSize.length == 2) {
                        try {
                            // Getting suggested tile size
                            final int tileWidth = Integer.valueOf(tilesSize[0].trim());
                            final int tileHeight = Integer.valueOf(tilesSize[1].trim());
                            tileDimensions = new Dimension(tileWidth, tileHeight);
                        } catch (NumberFormatException nfe) {
                            if (LOGGER.isLoggable(Level.WARNING)) {
                                LOGGER.log(Level.WARNING, "Unable to parse " + "suggested tile size parameter");
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Compute this specific request settings all the parameters needed by a visiting {@link RasterLayerResponse}
     * object.
     */
    private void prepare() throws DataSourceException {
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
            requestedBBox = rasterManager.spatialDomainManager.coverageBBox;
            cropBBox = rasterManager.spatialDomainManager.coverageBBox;
            requestedRasterArea = (Rectangle) rasterManager.spatialDomainManager.coverageRasterArea.clone();
            destinationRasterArea = (Rectangle) rasterManager.spatialDomainManager.coverageRasterArea.clone();
            requestedResolution = rasterManager.spatialDomainManager.coverageFullResolution.clone();
            // TODO harmonize the various types of transformations
            requestedGridToWorld = (AffineTransform) rasterManager.spatialDomainManager.coverageGridToWorld2D;
            return;
        }

        //
        // Adjust requested bounding box and source region in order to fall within the source
        // coverage
        //
        computeRequestSpatialElements();
    }

    private void inspectCoordinateReferenceSystems() throws DataSourceException {
        // get the crs for the requested bbox
        requestCRS = CRS.getHorizontalCRS(requestedBBox.getCoordinateReferenceSystem());

        //
        // Check if the request CRS is different from the coverage native CRS
        //
        if (!CRS.equalsIgnoreMetadata(requestCRS, rasterManager.spatialDomainManager.coverageCRS2D))
            try {
                destinationToSourceTransform =
                        CRS.findMathTransform(requestCRS, rasterManager.spatialDomainManager.coverageCRS2D, true);
            } catch (FactoryException e) {
                throw new DataSourceException("Unable to inspect request CRS", e);
            }
        // now transform the requested envelope to source crs
        if (destinationToSourceTransform != null && destinationToSourceTransform.isIdentity())
            destinationToSourceTransform = null; // the CRS is basically the same
        else if (destinationToSourceTransform instanceof AffineTransform) {
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
                        ProjectiveTransform.create(requestedGridToWorld),
                        PixelInCell.CELL_CENTER,
                        PixelInCell.CELL_CORNER);
                requestedBBox =
                        new ReferencedEnvelope(CRS.transform(tempTransform, new GeneralBounds(requestedRasterArea)));

            } catch (MismatchedDimensionException | TransformException e) {
                throw new DataSourceException("Unable to inspect request CRS", e);
            }

            // now clean up all the traces of the transformations
            destinationToSourceTransform = null;
        }
    }

    /**
     * Check the type of read operation which will be performed and return {@code true} if a JAI imageRead operation
     * need to be performed or {@code false} if a simple read operation is needed.
     */
    private void checkReadType() {
        // //
        //
        // First of all check if the ReadType was already set as part the
        // request parameters
        //
        // //
        if (readType != ReadType.UNSPECIFIED) {
            if (readType == ReadType.JAI_IMAGEREAD && useMultithreading) {
                readType = ReadType.JAI_IMAGEREAD_MT;
            }
            return;
        }

        // //
        //
        // Ok, the request did not explicitly set the read type, let's check the
        // hints.
        //
        // //
        final Hints hints = rasterManager.getHints();
        if (hints != null) {
            final Object o = hints.get(Hints.USE_JAI_IMAGEREAD);
            if (o != null) {
                readType = (ReadType) o;
                return;
            }
        }

        // //
        //
        // Last chance is to use the default read type.
        //
        // //
        readType = ReadType.getDefault();
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
                LOGGER.log(Level.FINE, "Requested envelope too small resulting in empty cropped raster region");
            // TODO: Future versions may define a 1x1 rectangle starting
            // from the lower coordinate
            empty = true;
        }
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
        // transformation is simpl ean affine transformation.
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
        // Create the crop bbox in the coverage CRS for cropping it later
        // on.
        //
        computeCropBBOX();
        if (empty || cropBBox != null && cropBBox.isEmpty()) {
            if (LOGGER.isLoggable(Level.FINE)) LOGGER.log(Level.FINE, "RequestedBBox empty or null");
            // this means that we do not have anything to load at all!
            empty = true;
            return;
        }

        //
        // CROP SOURCE REGION using the refined requested envelope
        //
        computeCropRasterArea();
        if (empty || destinationRasterArea != null && destinationRasterArea.isEmpty()) {
            if (LOGGER.isLoggable(Level.FINE)) LOGGER.log(Level.FINE, "CropRasterArea empty or null");
            // this means that we do not have anything to load at all!
            return;
        }

        if (LOGGER.isLoggable(Level.FINE)) {
            StringBuffer sb = new StringBuffer("Adjusted Requested Envelope = ")
                    .append(requestedBBox.toString())
                    .append("\n")
                    .append("Requested raster dimension = ")
                    .append(requestedRasterArea.toString())
                    .append("\n")
                    .append("Corresponding raster source region = ")
                    .append(requestedRasterArea.toString());
            LOGGER.log(Level.FINE, sb.toString());
        }
        //
        // Compute the request resolution from the request
        //
        computeRequestedResolution();
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
                    // the effect is a degradation of quality, but we take that into account
                    // emprically
                    //
                    requestedResolution = null;

                    final GridToEnvelopeMapper geMapper =
                            new GridToEnvelopeMapper(new GridEnvelope2D(destinationRasterArea), cropBBox);
                    final AffineTransform tempTransform = geMapper.createAffineTransform();
                    requestedResolution = new double[] {
                        XAffineTransform.getScaleX0(tempTransform), XAffineTransform.getScaleY0(tempTransform)
                    };
                } else {

                    //
                    // the crs of the request and the one of the coverage are the
                    // same, we can get the resolution from the grid to world
                    //
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
        requestedResolution = rasterManager.spatialDomainManager.coverageFullResolution;
    }

    private void computeCropBBOX() throws DataSourceException {

        // get the crs for the requested bbox
        if (requestCRS == null) requestCRS = CRS.getHorizontalCRS(requestedBBox.getCoordinateReferenceSystem());
        try {

            //
            // If this approach succeeds, either the request crs is the same of
            // the coverage crs or the request bbox can be reprojected to that
            // crs
            //

            // STEP 1: reproject requested BBox to native CRS if needed
            if (!CRS.equalsIgnoreMetadata(requestCRS, rasterManager.spatialDomainManager.coverageCRS2D))
                destinationToSourceTransform =
                        CRS.findMathTransform(requestCRS, rasterManager.spatialDomainManager.coverageCRS2D, true);
            // now transform the requested envelope to source crs
            if (destinationToSourceTransform != null && !destinationToSourceTransform.isIdentity()) {
                final GeneralBounds temp =
                        CRS.transform(requestedBBox, rasterManager.spatialDomainManager.coverageCRS2D);
                temp.setCoordinateReferenceSystem(rasterManager.spatialDomainManager.coverageCRS2D);
                cropBBox = new ReferencedEnvelope(temp);

            } else {
                // we do not need to do anything, but we do this in order to aboid problems with the
                // envelope checks
                cropBBox = new ReferencedEnvelope(
                        requestedBBox.getMinX(),
                        requestedBBox.getMaxX(),
                        requestedBBox.getMinY(),
                        requestedBBox.getMaxY(),
                        rasterManager.spatialDomainManager.coverageCRS2D);
            }

            //
            // STEP 2: intersect requested BBox in native CRS with coverage native bbox to get the
            // crop bbox
            //
            // intersect the requested area with the bounds of this
            // layer in native crs
            if (!cropBBox.intersects(rasterManager.spatialDomainManager.coverageBBox)) {
                cropBBox = null;
                empty = true;
                return;
            }
            // TODO XXX Optimize when referenced envelope has intersection method that actually
            // retains the CRS, this is the JTS one
            cropBBox = new ReferencedEnvelope(
                    ((ReferencedEnvelope) cropBBox).intersection(rasterManager.spatialDomainManager.coverageBBox),
                    rasterManager.spatialDomainManager.coverageCRS2D);

            return;
        } catch (TransformException | FactoryException te) {
            // something bad happened while trying to transform this
            // envelope. let's try with wgs84
            if (LOGGER.isLoggable(Level.FINE)) LOGGER.log(Level.FINE, te.getLocalizedMessage(), te);
        }

        try {

            //
            // If we can not reproject the requested envelope to the native CRS,
            // we go back to reproject in the geographic crs of the native
            // coverage since this usually happens for conversions between CRS
            // whose area of definition is different
            //

            // STEP 1 reproject the requested envelope to the coverage geographic bbox
            if (!CRS.equalsIgnoreMetadata(rasterManager.spatialDomainManager.coverageGeographicCRS2D, requestCRS)) {
                // try to convert the requested bbox to the coverage geocrs
                CoordinateOperationFactory factory = CRS.getCoordinateOperationFactory(true);
                CoordinateOperation op =
                        factory.createOperation(requestCRS, rasterManager.spatialDomainManager.coverageGeographicCRS2D);
                requestedBBOXInCoverageGeographicCRS = CRS.transform(op, requestedBBox);
                requestedBBOXInCoverageGeographicCRS.setCoordinateReferenceSystem(
                        rasterManager.spatialDomainManager.coverageGeographicCRS2D);
            }
            if (requestedBBOXInCoverageGeographicCRS == null)
                requestedBBOXInCoverageGeographicCRS = new GeneralBounds(requestCRS);

            // STEP 2 intersection with the geographic bbox for this coverage
            if (!requestedBBOXInCoverageGeographicCRS.intersects(
                    rasterManager.spatialDomainManager.coverageGeographicBBox, true)) {
                cropBBox = null;
                empty = true;
                return;
            }
            // intersect with the coverage native geographic bbox
            // note that for the moment we got to use general envelope since there is no
            // intersection otherwise
            requestedBBOXInCoverageGeographicCRS.intersect(rasterManager.spatialDomainManager.coverageGeographicBBox);
            requestedBBOXInCoverageGeographicCRS.setCoordinateReferenceSystem(
                    rasterManager.spatialDomainManager.coverageGeographicCRS2D);

            // now go back to the coverage native CRS in order to compute an approximate requested
            // resolution
            approximateRequestedBBoInNativeCRS = CRS.transform(
                    requestedBBOXInCoverageGeographicCRS, rasterManager.spatialDomainManager.coverageCRS2D);
            approximateRequestedBBoInNativeCRS.setCoordinateReferenceSystem(
                    rasterManager.spatialDomainManager.coverageCRS2D);
            cropBBox = new ReferencedEnvelope(approximateRequestedBBoInNativeCRS);

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

    public BoundingBox getRequestedBBox() {
        return requestedBBox;
    }

    public OverviewPolicy getOverviewPolicy() {
        return overviewPolicy;
    }

    public Rectangle getRequestedRasterArea() {
        return (Rectangle) (requestedRasterArea != null ? requestedRasterArea.clone() : requestedRasterArea);
    }

    public double[] getRequestedResolution() {
        return requestedResolution != null ? requestedResolution.clone() : null;
    }

    public Color getInputTransparentColor() {
        return inputTransparentColor;
    }

    public ReadType getReadType() {
        return readType;
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

    public Dimension getTileDimensions() {
        return tileDimensions;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("RasterLayerRequest description: \n");
        builder.append("\tRequestedBBox=").append(requestedBBox).append("\n");
        builder.append("\tRequestedRasterArea=").append(requestedRasterArea).append("\n");
        builder.append("\tRequestedGridToWorld=").append(requestedGridToWorld).append("\n");
        builder.append("\tReadType=").append(readType);
        return builder.toString();
    }
}
