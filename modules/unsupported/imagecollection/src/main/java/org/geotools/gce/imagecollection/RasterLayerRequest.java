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
package org.geotools.gce.imagecollection;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FilenameUtils;
import org.geotools.coverage.grid.GeneralGridEnvelope;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.OverviewPolicy;
import org.geotools.data.DataSourceException;
import org.geotools.factory.Hints;
import org.geotools.filter.AttributeExpressionImpl;
import org.geotools.filter.IsEqualsToImpl;
import org.geotools.gce.imagecollection.RasterManager.ImageManager;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.metadata.iso.spatial.PixelTranslation;
import org.geotools.referencing.CRS;
import org.geotools.referencing.operation.LinearTransform;
import org.geotools.referencing.operation.matrix.XAffineTransform;
import org.geotools.referencing.operation.transform.ProjectiveTransform;
import org.opengis.filter.Filter;
import org.opengis.filter.expression.Literal;
import org.opengis.geometry.BoundingBox;
import org.opengis.geometry.Envelope;
import org.opengis.metadata.Identifier;
import org.opengis.parameter.GeneralParameterDescriptor;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.parameter.ParameterValue;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.ReferenceIdentifier;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.datum.PixelInCell;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.MathTransform2D;
import org.opengis.referencing.operation.NoninvertibleTransformException;
import org.opengis.referencing.operation.TransformException;

/**
 * A class to handle coverage requests to an {@link ImageCollectionReader} reader.
 * 
 * @author Daniele Romagnoli, GeoSolutions
 */
class RasterLayerRequest {

    private final static String PATH_KEY = "PATH";
    
    /** Logger. */
    private final static Logger LOGGER = org.geotools.util.logging.Logging
            .getLogger(RasterLayerRequest.class);

    private ReadType readType = AbstractGridFormat.USE_JAI_IMAGEREAD
            .getDefaultValue() ? ReadType.JAI_IMAGEREAD : ReadType.DIRECT_READ;

    /** The {@link BoundingBox} requested */
    private BoundingBox requestedBBox;

    /**
     * The {@link BoundingBox} of the portion of the coverage that intersects
     * the requested bbox
     */
    private BoundingBox cropBBox;

    /** The desired overview Policy for this request */
    private OverviewPolicy overviewPolicy;

    /** The region where to fit the requested envelope */
    private Rectangle requestedRasterArea;

    /** The region of the */
    private Rectangle destinationRasterArea;

    /**
     * Set to {@code true} if this request will produce an empty result, and the
     * coverageResponse will produce a {@code null} coverage.
     */
    private boolean empty;

    private AffineTransform requestedGridToWorld;

    private double[] requestedResolution;

    private double[] backgroundValues;

    private RasterManager rasterManager;

    private double[] requestedRasterScaleFactors;

    private Dimension tileDimensions;

    private Filter filter = null;
    
    ImageManager imageManager;

    /**
     * Build a new {@code CoverageRequest} given a set of input parameters.
     * 
     * @param params
     *            The {@code GeneralParameterValue}s to initialize this request
     * @param baseGridCoverage2DReader
     * @throws DataSourceException
     */
    public RasterLayerRequest(final GeneralParameterValue[] params,
            final RasterManager rasterManager) throws DataSourceException {

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
        final ParameterValueGroup readParams = this.rasterManager.parent
                .getFormat().getReadParameters();
        if (readParams == null) {
            if (LOGGER.isLoggable(Level.FINE))
                LOGGER.fine("No default values for the read parameters!");
            return;
        }
        final List<GeneralParameterDescriptor> parametersDescriptors = readParams
                .getDescriptor().descriptors();
        for (GeneralParameterDescriptor descriptor : parametersDescriptors) {

            // we canc get the default vale only with the ParameterDescriptor
            // class
            if (!(descriptor instanceof ParameterDescriptor))
                continue;

            // get name and default value
            final ParameterDescriptor desc = (ParameterDescriptor) descriptor;
            final ReferenceIdentifier name = desc.getName();
            final Object value = desc.getDefaultValue();

            // //
            //
            // Requested GridGeometry2D parameter
            //
            // //
            if (descriptor.getName().equals(
                    AbstractGridFormat.READ_GRIDGEOMETRY2D.getName())) {
                if (value == null)
                    continue;
                final GridGeometry2D gg = (GridGeometry2D) value;

                requestedBBox = new ReferencedEnvelope((Envelope) gg.getEnvelope2D());
                requestedRasterArea = gg.getGridRange2D().getBounds();
                requestedGridToWorld = (AffineTransform) gg.getGridToCRS2D();
                continue;
            }

            if (name.equals(ImageCollectionFormat.BACKGROUND_VALUES.getName())) {
                if (value == null)
                    continue;
                backgroundValues = (double[]) value;
                continue;

            }

            // //
            //
            // Use JAI ImageRead parameter
            //
            // //
            if (name.equals(AbstractGridFormat.USE_JAI_IMAGEREAD.getName())) {
                if (value == null)
                    continue;
                readType = ((Boolean) value) ? ReadType.JAI_IMAGEREAD
                        : ReadType.DIRECT_READ;
                continue;
            }

            // //
            //
            // Overview Policy parameter
            //
            // //
            if (name.equals(AbstractGridFormat.OVERVIEW_POLICY.getName())) {
                if (value == null)
                    continue;
                overviewPolicy = (OverviewPolicy) value;
                continue;
            }

            if (name.equals(ImageCollectionFormat.SUGGESTED_TILE_SIZE.getName())) {
                final String suggestedTileSize = (String) value;

                // Preliminary checks on parameter value
                if ((suggestedTileSize != null) && (suggestedTileSize.trim().length() > 0)) {

                    if (suggestedTileSize.contains(ImageCollectionFormat.TILE_SIZE_SEPARATOR)) {
                        final String[] tilesSize = suggestedTileSize.split(ImageCollectionFormat.TILE_SIZE_SEPARATOR);
                        if (tilesSize.length == 2) {
                            try {
                                // Getting suggested tile size
                                final int tileWidth = Integer.valueOf(tilesSize[0].trim());
                                final int tileHeight = Integer.valueOf(tilesSize[1].trim());
                                tileDimensions = new Dimension(tileWidth, tileHeight);
                            } catch (NumberFormatException nfe) {
                                if (LOGGER.isLoggable(Level.WARNING)) {
                                    LOGGER.log(Level.WARNING,
                                            "Unable to parse suggested tile size parameter");
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
     * @param param
     *            the input {@code ParamaterValue} object
     * @param name
     *            the name of the parameter
     */
    private void extractParameter(ParameterValue<?> param, Identifier name) {

        // //
        //
        // Requested GridGeometry2D parameter
        //
        // //
        if (name.equals(AbstractGridFormat.READ_GRIDGEOMETRY2D.getName())) {
            final Object value = param.getValue();
            if (value == null)
                return;
            final GridGeometry2D gg = (GridGeometry2D) param.getValue();
            if (gg == null) {
                return;
            }

            requestedBBox = new ReferencedEnvelope((Envelope) gg.getEnvelope2D());
            requestedRasterArea = gg.getGridRange2D().getBounds();
            requestedGridToWorld = (AffineTransform) gg.getGridToCRS2D();
            return;
        }

        // //
        //
        // Use JAI ImageRead parameter
        //
        // //
        if (name.equals(AbstractGridFormat.USE_JAI_IMAGEREAD.getName())) {
            final Object value = param.getValue();
            if (value == null)
                return;
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
            if (value == null)
                return;
            overviewPolicy = (OverviewPolicy) param.getValue();
            return;
        }

        if (name.equals(ImageCollectionFormat.BACKGROUND_VALUES.getName())) {
            final Object value = param.getValue();
            if (value == null)
                return;
            backgroundValues = (double[]) value;
            return;

        }

        if (name.equals(ImageCollectionFormat.FILTER.getName())) {
            final Object value = param.getValue();
            if (value == null)
                return;
            filter = (Filter) value;
            return;
        }

        if (name.equals(ImageCollectionFormat.SUGGESTED_TILE_SIZE.getName())) {
            final String suggestedTileSize = (String) param.getValue();

            // Preliminary checks on parameter value
            if ((suggestedTileSize != null) && (suggestedTileSize.trim().length() > 0)) {

                if (suggestedTileSize.contains(ImageCollectionFormat.TILE_SIZE_SEPARATOR)) {
                    final String[] tilesSize = suggestedTileSize.split(ImageCollectionFormat.TILE_SIZE_SEPARATOR);
                    if (tilesSize.length == 2) {
                        try {
                            // Getting suggested tile size
                            final int tileWidth = Integer.valueOf(tilesSize[0].trim());
                            final int tileHeight = Integer.valueOf(tilesSize[1].trim());
                            tileDimensions = new Dimension(tileWidth, tileHeight);
                        } catch (NumberFormatException nfe) {
                            if (LOGGER.isLoggable(Level.WARNING)) {
                                LOGGER.log(Level.WARNING, "Unable to parse suggested tile size parameter");
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Compute this specific request settings all the parameters needed by a
     * visiting {@link RasterLayerResponse} object.
     * 
     * @throws DataSourceException
     */
    private void prepare() throws DataSourceException {
        String path = null; 
        if (filter != null) {
            path = parsePath(filter);
        } else {
            if (LOGGER.isLoggable(Level.INFO)){
                LOGGER.info("No PATH have been specified through a Filter. Proceeding with default Image");
            }
            if (rasterManager.parent.defaultPath == null){
                imageManager = rasterManager.getDatasetManager(Utils.DEFAULT_IMAGE_PATH);
                return;
            } else {
                path = rasterManager.parent.defaultPath;
            }
        }
        final String storePath = rasterManager.parent.parentPath;
        
        //First normalization
        path = FilenameUtils.normalize(path);
        if (path.startsWith(storePath)){
            // Removing the store path prefix from the specified path
            // allow to deal with the case of parentPath = /home/user1/folder1/ and path = /home/user1/folder1/folder3 
            // which comes back to path = folder3
            path = path.substring(storePath.length(), path.length());
        }
        final String filePath = FilenameUtils.normalize(FilenameUtils.concat(storePath, path));
        if (!filePath.startsWith(storePath)){
            throw new DataSourceException("Possible attempt to access data outside the coverate store path:\n" 
                    + "Store Path: " + storePath + "\nSpecified File Path: " + filePath);
        }
        imageManager = rasterManager.getDatasetManager(filePath);
        
        //
        // DO WE HAVE A REQUESTED AREA?
        //
        // Check if we have something to load by intersecting the
        // requested envelope with the bounds of this data set.
        //
        if (requestedBBox == null) {

            //
            // In case we have nothing to look at we should get the whole
            // coverage
            //
            requestedBBox = imageManager.coverageBBox;
            cropBBox = imageManager.coverageBBox;
            requestedRasterArea = (Rectangle) imageManager.coverageRasterArea.clone();
            destinationRasterArea = (Rectangle) imageManager.coverageRasterArea.clone();
            requestedResolution = imageManager.coverageFullResolution.clone();
            // TODO harmonize the various types of transformations
            requestedGridToWorld = (AffineTransform) imageManager.coverageGridToWorld2D;
            return;
        }

        //
        // Adjust requested bounding box and source region in order to fall
        // within the source coverage
        //
        computeRequestSpatialElements();
    }

    /**
     * Extract a PATH from the specified filter. Filter should be an IsEqualsToImpl in order to support 
     * Filtering like CQL_FILTER=PATH='folder2/subfolder1/sample.tif'
     * @param filter
     * @return
     */
    private String parsePath(Filter filter) {
        if (filter == null){
            throw new IllegalArgumentException("The provided filter is null");
        }
        if (!(filter instanceof IsEqualsToImpl)){
            throw new IllegalArgumentException("The provided filter should be an \"equals to\" filter: \"" + PATH_KEY + "=value\"");
        }
        
        IsEqualsToImpl pathEqualTo = (IsEqualsToImpl) filter;
        AttributeExpressionImpl pathKey = (AttributeExpressionImpl) pathEqualTo.getExpression1();
        String pathK = (String) pathKey.getPropertyName();
        if (!pathK.equalsIgnoreCase(PATH_KEY)){
            throw new IllegalArgumentException("Invalid filter specified. It should be like this: \"" + PATH_KEY + "=value\" whilst the first expression is " + pathK);
        }
        
        Literal pathValue = (Literal) pathEqualTo.getExpression2();
        String pathV = (String) pathValue.getValue();

        return pathV;
    }

    /**
     * Check the type of read operation which will be performed and return
     * {@code true} if a JAI imageRead operation need to be performed or
     * {@code false} if a simple read operation is needed.
     * 
     * @return {@code true} if the read operation will use a JAI ImageRead
     *         operation instead of a simple {@code ImageReader.read(...)} call.
     */
    private void checkReadType() {

        if (readType != ReadType.UNSPECIFIED)
            return;
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
     * Return a crop region from a specified envelope, leveraging on the grid to
     * world transformation.
     * 
     * @param refinedRequestedBBox
     *            the crop envelope
     * @return a {@code Rectangle} representing the crop region.
     * @throws TransformException
     *             in case a problem occurs when going back to raster space.
     * @throws DataSourceException
     */
    private void computeCropRasterArea() throws DataSourceException {

        // we have nothing to crop
        if (cropBBox == null) {
            destinationRasterArea = null;
            return;
        }

        //
        // We need to invert the requested gridToWorld and then adjust the
        // requested raster area are accordingly
        //

        // invert the requested grid to world keeping into account the fact that
        // it is related to cell center
        // while the raster is related to cell corner
        MathTransform2D requestedWorldToGrid;
        try {
            requestedWorldToGrid = (MathTransform2D) PixelTranslation.translate(
                            ProjectiveTransform.create(requestedGridToWorld),
                            PixelInCell.CELL_CENTER, PixelInCell.CELL_CORNER)
                    .inverse();
        } catch (NoninvertibleTransformException e) {
            throw new DataSourceException(e);
        }

        // now get the requested bbox which have been already adjusted and
        // project it back to raster space
        try {
            destinationRasterArea = new GeneralGridEnvelope(CRS.transform(
                    requestedWorldToGrid, new GeneralEnvelope(cropBBox)),
                    PixelInCell.CELL_CORNER, false).toRectangle();
        } catch (IllegalStateException e) {
            throw new DataSourceException(e);
        } catch (TransformException e) {
            throw new DataSourceException(e);
        }
        // is it empty??
        if (destinationRasterArea.isEmpty()) {
            if (LOGGER.isLoggable(Level.FINE))
                LOGGER.log(Level.FINE,
                        "Requested envelope too small resulting in empty cropped raster region");
            // TODO: Future versions may define a 1x1 rectangle starting
            // from the lower coordinate
            empty = true;
            return;
        }

    }

    /**
     * @throws DataSourceException
     *             in case something bad occurs
     */
    private void computeRequestSpatialElements() throws DataSourceException {

        // Create the crop bbox in the coverage CRS for cropping it later on.
        computeCropBBOX();
        if (empty || (cropBBox != null && cropBBox.isEmpty())) {
            if (LOGGER.isLoggable(Level.FINE))
                LOGGER.log(Level.FINE, "RequestedBBox empty or null");
            // this means that we do not have anything to load at all!
            empty = true;
            return;
        }

        //
        // CROP SOURCE REGION using the refined requested envelope
        //
        computeCropRasterArea();
        if (empty || (destinationRasterArea != null && destinationRasterArea.isEmpty())) {
            if (LOGGER.isLoggable(Level.FINE))
                LOGGER.log(Level.FINE, "CropRasterArea empty or null");
            // this means that we do not have anything to load at all!
            return;
        }

        if (LOGGER.isLoggable(Level.FINE)) {
            StringBuffer sb = new StringBuffer("Adjusted Requested Envelope = ")
                    .append(requestedBBox.toString()).append("\n")
                    .append("Requested raster dimension = ")
                    .append(requestedRasterArea.toString()).append("\n")
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
     * Computes the requested resolution which is going to be used for selecting
     * overviews and or deciding decimation factors on the target coverage.
     * 
     * <p>
     * In case the requested envelope is in the same
     * {@link CoordinateReferenceSystem} of the coverage we compute the
     * resolution using the requested {@link MathTransform}. Notice that it must
     * be a {@link LinearTransform} or else we fail.
     * 
     * <p>
     * In case the requested envelope is not in the same
     * {@link CoordinateReferenceSystem} of the coverage we
     * 
     * @throws DataSourceException
     *             in case something bad happens during reprojections and/or
     *             intersections.
     */
    private void computeRequestedResolution() throws DataSourceException {

        try {

            // let's try to get the resolution from the requested gridToWorld
            if (requestedGridToWorld instanceof LinearTransform) {

                //
                // the crs of the request and the one of the coverage are
                // the
                // same, we can get the resolution from the grid to world
                //
                requestedResolution = new double[] {
                        XAffineTransform.getScaleX0(requestedGridToWorld),
                        XAffineTransform.getScaleY0(requestedGridToWorld) };
            }

            // leave
            return;
        } catch (Throwable e) {
            if (LOGGER.isLoggable(Level.INFO))
                LOGGER.log(Level.INFO, "Unable to compute requested resolution", e);
        }

        //
        // use the coverage resolution since we cannot compute the requested one
        //
        LOGGER.log(Level.WARNING, "Unable to compute requested resolution, using highest available");
        requestedResolution = imageManager.coverageFullResolution;

    }

    private void computeCropBBOX() throws DataSourceException {

        // we do not need to do anything, but we do this in order to
        // avoid problems with the envelope checks
        cropBBox = new ReferencedEnvelope(requestedBBox.getMinX(),
                requestedBBox.getMaxX(), requestedBBox.getMinY(),
                requestedBBox.getMaxY(),
                imageManager.coverageCRS);

        //
        // intersect requested BBox in with coverage bbox to get the crop bbox
        //
        if (!cropBBox.intersects((BoundingBox) imageManager.coverageBBox)) {
            cropBBox = null;
            empty = true;
            return;
        }
        // TODO XXX Optimize when referenced envelope has intersection
        // method that actually retains the CRS, this is the JTS one
        cropBBox = new ReferencedEnvelope(((ReferencedEnvelope) cropBBox).intersection(imageManager.coverageBBox),
                imageManager.coverageCRS);
        return;

    }

    public boolean isEmpty() {
        return empty;
    }

    public BoundingBox getRequestedBBox() {
        return requestedBBox;
    }

    public double[] getBackgroundValues() {
        return backgroundValues;
    }

    public OverviewPolicy getOverviewPolicy() {
        return overviewPolicy;
    }

    public Rectangle getRequestedRasterArea() {
        return (Rectangle) (requestedRasterArea != null ? requestedRasterArea
                .clone() : requestedRasterArea);
    }

    public double[] getRequestedResolution() {
        return requestedResolution != null ? requestedResolution.clone() : null;
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

    public Filter getFilter() {
        return filter;
    }

    public double[] getRequestedRasterScaleFactors() {
        return requestedRasterScaleFactors != null ? requestedRasterScaleFactors
                .clone() : requestedRasterScaleFactors;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("RasterLayerRequest description: \n");
        builder.append("\tRequestedBBox=").append(requestedBBox).append("\n");
        builder.append("\tRequestedRasterArea=").append(requestedRasterArea)
                .append("\n");
        builder.append("\tRequestedGridToWorld=").append(requestedGridToWorld)
                .append("\n");
        builder.append("\tReadType=").append(readType);
        return builder.toString();
    }
}