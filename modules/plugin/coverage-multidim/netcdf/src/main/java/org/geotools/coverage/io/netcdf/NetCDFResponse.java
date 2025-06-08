/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.io.netcdf;

import it.geosolutions.imageio.imageioimpl.EnhancedImageReadParam;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.media.jai.BorderExtender;
import javax.media.jai.Interpolation;
import javax.media.jai.InterpolationNearest;
import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.TileCache;
import javax.media.jai.TileScheduler;
import org.geotools.api.coverage.SampleDimension;
import org.geotools.api.coverage.grid.GridCoverage;
import org.geotools.api.data.DataSourceException;
import org.geotools.api.data.Query;
import org.geotools.api.feature.type.Name;
import org.geotools.api.filter.Filter;
import org.geotools.api.geometry.BoundingBox;
import org.geotools.api.referencing.datum.PixelInCell;
import org.geotools.api.referencing.operation.MathTransform2D;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.DimensionDescriptor;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.coverage.io.CoverageReadRequest;
import org.geotools.coverage.io.CoverageResponse;
import org.geotools.coverage.io.SpatialRequestHelper.CoverageProperties;
import org.geotools.coverage.io.impl.DefaultGridCoverageResponse;
import org.geotools.coverage.io.range.FieldType;
import org.geotools.coverage.io.range.RangeType;
import org.geotools.coverage.util.CoverageUtilities;
import org.geotools.coverage.util.FeatureUtilities;
import org.geotools.feature.visitor.FeatureCalc;
import org.geotools.feature.visitor.MaxVisitor;
import org.geotools.feature.visitor.MinVisitor;
import org.geotools.geometry.GeneralBounds;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.image.ImageWorker;
import org.geotools.image.util.ImageUtilities;
import org.geotools.imageio.netcdf.utilities.NetCDFUtilities;
import org.geotools.referencing.CRS;
import org.geotools.referencing.operation.matrix.XAffineTransform;
import org.geotools.referencing.operation.transform.AffineTransform2D;
import org.geotools.util.DateRange;
import org.geotools.util.NumberRange;
import org.geotools.util.Range;
import org.geotools.util.Utilities;
import org.geotools.util.factory.Hints;

/**
 * A RasterLayerResponse. An instance of this class is produced everytime a requestCoverage is called to a reader.
 *
 * @author Simone Giannecchini, GeoSolutions
 * @author Daniele Romagnoli, GeoSolutions
 * @author Stefan Alfons Krueger (alfonx), Wikisquare.de : Support for jar:file:foo.jar/bar.properties URLs
 */
class NetCDFResponse extends CoverageResponse {

    private static final double EPS = 1E-6;

    /** Logger. */
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(NetCDFResponse.class);

    /** The {@link NetCDFRequest} originating this response */
    private NetCDFRequest request;

    /** The coverage factory producing a {@link GridCoverage} from an image */
    private static final GridCoverageFactory COVERAGE_FACTORY = new GridCoverageFactory();

    /** The base envelope related to the input coverage */
    private GeneralBounds coverageEnvelope;

    private ReferencedEnvelope targetBBox;

    private Rectangle rasterBounds;

    private MathTransform2D finalGridToWorldCorner;

    private MathTransform2D finalWorldToGridCorner;

    private URL datasetURL;

    private EnhancedImageReadParam baseReadParameters = new EnhancedImageReadParam();

    private boolean oversampledRequest;

    private AffineTransform baseGridToWorld;

    private Hints hints;

    /**
     * Construct a {@code RasterLayerResponse} given a specific {@link RasterLayerRequest}, a
     * {@code GridCoverageFactory} to produce {@code GridCoverage}s and an {@code ImageReaderSpi} to be used for
     * instantiating an Image Reader for a read operation,
     *
     * @param request a {@link RasterLayerRequest} originating this response.
     */
    public NetCDFResponse(final NetCDFRequest request) {
        this.request = request;
        CoverageReadRequest readRequest = request.originalRequest;
        setRequest(readRequest);
        datasetURL = (URL) request.source.reader.getInput();
    }

    public CoverageResponse createResponse() throws IOException {
        processRequest();
        return this;
    }

    /**
     * This method creates the GridCoverage2D from the underlying file given a specified envelope, and a requested
     * dimension.
     *
     * @throws java.io.IOException
     */
    private void processRequest() throws IOException {

        // is this query empty?
        if (request.spatialRequestHelper.isEmpty()) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "Request is empty: " + request.toString());
            }
            return;
        }

        // assemble granules
        prepareParams();
        String timeFilterAttribute = null;
        String elevationFilterAttribute = null;
        CoverageReadRequest readRequest = (CoverageReadRequest) getRequest();
        RangeType rangeType = request.source.getRangeType(null);
        List<DimensionDescriptor> dimensionDescriptors = request.source.getDimensionDescriptors();
        for (DimensionDescriptor dimensionDescriptor : dimensionDescriptors) {
            if (dimensionDescriptor.getName().equalsIgnoreCase(NetCDFUtilities.ELEVATION_DIM)) {
                // TODO Update this with ranged attributes
                elevationFilterAttribute = dimensionDescriptor.getStartAttribute();
            } else if (dimensionDescriptor.getName().equalsIgnoreCase(NetCDFUtilities.TIME_DIM)) {
                // TODO Update this with ranged attributes
                timeFilterAttribute = dimensionDescriptor.getStartAttribute();
            }
        }

        // set the destination bands based on the bands parameter
        baseReadParameters.setBands(readRequest.getBands());

        Set<DateRange> temporalSubset = readRequest.getTemporalSubset();
        Set<NumberRange<Double>> verticalSubset = readRequest.getVerticalSubset();
        RangeType requestedRange = readRequest.getRangeSubset();
        Set<FieldType> fieldTypes = requestedRange.getFieldTypes();

        //
        // adding GridCoverages to the results list
        //
        // //
        Set<SampleDimension> sampleDims = null;
        for (FieldType fieldType : fieldTypes) {
            final Name name = fieldType.getName();
            sampleDims = fieldType.getSampleDimensions();
            if (rangeType != null) {
                final FieldType ft = rangeType.getFieldType(name.getLocalPart());
                if (ft != null) sampleDims = ft.getSampleDimensions();
            }
        }

        // when calculating the sample dimensions we need to take in account the bands parameter
        GridSampleDimension[] sampleDimensions = sampleDims != null
                ? sampleDims.toArray(new GridSampleDimension[sampleDims.size()])
                : new GridSampleDimension[0];
        int[] bands = readRequest.getBands();
        if (bands != null) {
            int maxBandIndex = Arrays.stream(bands).max().getAsInt();
            if (bands.length > 0 && (sampleDims == null || maxBandIndex > sampleDims.size())) {
                throw new IllegalArgumentException("Invalid bands parameter provided.");
            }
            GridSampleDimension[] updatedSampleDimensions = new GridSampleDimension[bands.length];
            for (int i = 0; i < bands.length; i++) {
                updatedSampleDimensions[i] = sampleDimensions[bands[i]];
            }
            sampleDimensions = updatedSampleDimensions;
        }

        // Forcing creation of subsets (even with a single null element)
        Set<DateRange> tempSubset = null;
        if (!temporalSubset.isEmpty()) {
            tempSubset = temporalSubset;
        } else {
            tempSubset = new HashSet<>();
            tempSubset.add(null);
        }

        Set<NumberRange<Double>> vertSubset = null;
        if (!verticalSubset.isEmpty()) {
            vertSubset = verticalSubset;
        } else {
            vertSubset = new HashSet<>();
            vertSubset.add(null);
        }

        Map<String, Set<?>> domainsSubset = readRequest.getAdditionalDomainsSubset();
        Filter requestFilter = request.originalRequest.getFilter();
        double[] noData = null;
        if (sampleDimensions != null && sampleDimensions.length > 0) {
            GridSampleDimension sampleDimension = sampleDimensions[0];
            noData = sampleDimension.getNoDataValues();
        }

        // handling date and time
        for (DateRange timeRange : tempSubset) {
            for (NumberRange<Double> elevation : vertSubset) {

                Query query = new Query();
                // handle time and elevation
                createTimeElevationQuery(
                        timeRange, elevation, query, requestFilter, timeFilterAttribute, elevationFilterAttribute);

                // handle additional params
                additionalParamsManagement(query, domainsSubset, dimensionDescriptors);

                // set query typename
                query.setTypeName(request.name);

                // handle default params
                if (timeRange == null && timeFilterAttribute != null) {
                    Query(query, timeFilterAttribute);
                }
                if (elevation == null && elevationFilterAttribute != null) {
                    Query(query, elevationFilterAttribute);
                }
                defaultParamsManagement(query, domainsSubset, dimensionDescriptors);

                // bbox
                query.setFilter(FeatureUtilities.DEFAULT_FILTER_FACTORY.and(
                        query.getFilter(),
                        FeatureUtilities.DEFAULT_FILTER_FACTORY.bbox(
                                FeatureUtilities.DEFAULT_FILTER_FACTORY.property("the_geom"), targetBBox)));

                List<Integer> indexes = request.source.reader.getImageIndex(query);
                if (indexes == null || indexes.isEmpty()) {
                    if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.fine(" No indexes found for this query: " + query.toString());
                    }
                    continue;
                }
                int imageIndex = indexes.get(0);
                final RenderedImage image =
                        loadRaster(baseReadParameters, imageIndex, targetBBox, finalWorldToGridCorner, hints, noData);

                // create the coverage
                GridCoverage2D gridCoverage = prepareCoverage(image, sampleDimensions, noData);

                // Adding coverage domain
                if (gridCoverage != null) {
                    GridCoverage gcResponse = new DefaultGridCoverageResponse(gridCoverage, timeRange, elevation);
                    addResult(gcResponse);
                }
            }
        }

        // success
        setStatus(Status.SUCCESS);
    }

    /** */
    private void additionalParamsManagement(
            Query query, Map<String, Set<?>> domainsSubset, List<DimensionDescriptor> dimensionDescriptors)
            throws IOException {
        if (domainsSubset.isEmpty()) {
            return;
        }
        Filter filter = query.getFilter();
        for (Entry<String, Set<?>> entry : domainsSubset.entrySet()) {
            Set<?> values = entry.getValue();
            String attribute = null;
            for (DimensionDescriptor dim : dimensionDescriptors) {
                if (dim.getName().toUpperCase().equalsIgnoreCase(entry.getKey())) {
                    attribute = dim.getStartAttribute();
                    break;
                }
            }
            for (Object value : values) {
                if (value instanceof Range) {
                    throw new UnsupportedOperationException();
                } else {
                    filter = FeatureUtilities.DEFAULT_FILTER_FACTORY.and(
                            filter,
                            FeatureUtilities.DEFAULT_FILTER_FACTORY.equals(
                                    FeatureUtilities.DEFAULT_FILTER_FACTORY.property(attribute),
                                    FeatureUtilities.DEFAULT_FILTER_FACTORY.literal(value)));
                }
            }
        }
        query.setFilter(filter);
    }

    /**
     * Create the query to retrive the imageIndex related to the specified time (if any) and the specified elevation (if
     * any)
     */
    private void createTimeElevationQuery(
            DateRange time,
            NumberRange<Double> elevation,
            Query query,
            Filter requestFilter,
            String timeFilterAttribute,
            String elevationFilterAttribute) {
        final List<Filter> filters = new ArrayList<>();

        // //
        // Setting up time filter
        // //
        if (time != null) {
            final Range range = time;
            // schema with only one time attribute. Consider adding code for schema with begin,end
            // attributes
            filters.add(FeatureUtilities.DEFAULT_FILTER_FACTORY.and(
                    FeatureUtilities.DEFAULT_FILTER_FACTORY.lessOrEqual(
                            FeatureUtilities.DEFAULT_FILTER_FACTORY.property(timeFilterAttribute),
                            FeatureUtilities.DEFAULT_FILTER_FACTORY.literal(range.getMaxValue())),
                    FeatureUtilities.DEFAULT_FILTER_FACTORY.greaterOrEqual(
                            FeatureUtilities.DEFAULT_FILTER_FACTORY.property(timeFilterAttribute),
                            FeatureUtilities.DEFAULT_FILTER_FACTORY.literal(range.getMinValue()))));
        }

        // //
        // Setting up elevation filter
        // //
        if (elevation != null) {
            final Range range = elevation;
            // schema with only one elevation attribute. Consider adding code for schema with
            // begin,end attributes
            filters.add(FeatureUtilities.DEFAULT_FILTER_FACTORY.and(
                    FeatureUtilities.DEFAULT_FILTER_FACTORY.lessOrEqual(
                            FeatureUtilities.DEFAULT_FILTER_FACTORY.property(elevationFilterAttribute),
                            FeatureUtilities.DEFAULT_FILTER_FACTORY.literal(range.getMaxValue())),
                    FeatureUtilities.DEFAULT_FILTER_FACTORY.greaterOrEqual(
                            FeatureUtilities.DEFAULT_FILTER_FACTORY.property(elevationFilterAttribute),
                            FeatureUtilities.DEFAULT_FILTER_FACTORY.literal(range.getMinValue()))));
        }

        if (requestFilter != null) {
            filters.add(requestFilter);
        }

        Filter filter = FeatureUtilities.DEFAULT_FILTER_FACTORY.and(filters);
        query.setFilter(filter);
    }

    private Object findDefaultValue(Query query, String attribute) {
        FeatureCalc aggFunc;
        switch (NetCDFUtilities.getParameterBehaviour(attribute)) {
            case MAX:
                aggFunc = new MaxVisitor(attribute);
                break;
            case MIN:
                aggFunc = new MinVisitor(attribute);
                break;
            default:
                return null;
        }
        try {
            request.source.reader.getCatalog().computeAggregateFunction(query, aggFunc);
            return aggFunc.getResult().getValue();
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, e.getMessage(), e);
            return null;
        }
    }

    private void Query(Query query, String filterAttribute) {
        Object value = findDefaultValue(query, filterAttribute);
        if (value != null) {
            Filter filter = query.getFilter();
            filter = FeatureUtilities.DEFAULT_FILTER_FACTORY.and(
                    filter,
                    FeatureUtilities.DEFAULT_FILTER_FACTORY.equals(
                            FeatureUtilities.DEFAULT_FILTER_FACTORY.property(filterAttribute),
                            FeatureUtilities.DEFAULT_FILTER_FACTORY.literal(value)));
            query.setFilter(filter);
        }
    }

    private void defaultParamsManagement(
            Query query, Map<String, Set<?>> domainsSubset, List<DimensionDescriptor> dimensionDescriptors) {
        for (DimensionDescriptor dim : dimensionDescriptors) {
            boolean notPresent = true;
            for (String key : domainsSubset.keySet()) {
                if (dim.getName().toUpperCase().equalsIgnoreCase(key)) {
                    notPresent = false;
                }
            }
            if (notPresent) {
                Query(query, dim.getStartAttribute());
            }
        }
    }

    private void prepareParams() throws DataSourceException {

        try {
            baseReadParameters = new EnhancedImageReadParam();
            performDecimation(baseReadParameters);

            // === extract bbox
            initBBOX();

            // === init transformations
            initTransformations();

            // === init raster bounds
            initRasterBounds();

        } catch (Exception e) {
            throw new DataSourceException("Unable to create this mosaic", e);
        }
    }

    /**
     * This method is responsible for computing the raster bounds of the final mosaic.
     *
     * @throws TransformException In case transformation fails during the process.
     */
    private void initRasterBounds() throws TransformException {
        final GeneralBounds tempRasterBounds = CRS.transform(finalWorldToGridCorner, targetBBox);
        rasterBounds = tempRasterBounds.toRectangle2D().getBounds();

        // SG using the above may lead to problems since the reason is that may be a little (1 px)
        // bigger
        // than what we need. The code below is a bit better since it uses a proper logic (see
        // GridEnvelope
        // Javadoc)
        // rasterBounds = new GridEnvelope2D(new Envelope2D(tempRasterBounds),
        // PixelInCell.CELL_CORNER);
        if (rasterBounds.width == 0) {
            rasterBounds.width++;
        }
        if (rasterBounds.height == 0) {
            rasterBounds.height++;
        }
        if (oversampledRequest) {
            rasterBounds.grow(2, 2);
        }
    }

    /**
     * This method is responsible for initializing transformations g2w and back
     *
     * @throws Exception in case we don't manage to instantiate some of them.
     */
    private void initTransformations() throws Exception {
        // compute final world to grid
        // base grid to world for the center of pixels
        CoverageProperties properties = request.spatialRequestHelper.getCoverageProperties();
        baseGridToWorld = (AffineTransform) properties.getGridToWorld2D();
        double[] coverageFullResolution = properties.getFullResolution();
        final double resX = coverageFullResolution[0];
        final double resY = coverageFullResolution[1];
        final double[] requestRes = request.spatialRequestHelper.getRequestedResolution();

        final AffineTransform g2w = new AffineTransform(baseGridToWorld);
        g2w.concatenate(CoverageUtilities.CENTER_TO_CORNER);

        if (requestRes[0] < resX || requestRes[1] < resY) {
            // Using the best available resolution
            oversampledRequest = true;
        } else {

            // SG going back to working on a per level basis to do the composition
            // g2w = new AffineTransform(request.getRequestedGridToWorld());
            g2w.concatenate(AffineTransform.getScaleInstance(
                    baseReadParameters.getSourceXSubsampling(), baseReadParameters.getSourceYSubsampling()));
        }
        // move it to the corner
        finalGridToWorldCorner = new AffineTransform2D(g2w);
        finalWorldToGridCorner = finalGridToWorldCorner.inverse(); // compute raster bounds
    }

    /** This method is responsible for initializing the bbox for the mosaic produced by this response. */
    private void initBBOX() {
        // ok we got something to return, let's load records from the index
        final BoundingBox cropBBOX = request.spatialRequestHelper.getCropBBox();
        if (cropBBOX != null) {
            targetBBox = ReferencedEnvelope.reference(cropBBOX);
        } else {
            targetBBox = new ReferencedEnvelope(coverageEnvelope);
        }
    }

    /** This method is responsible for creating a coverage from the supplied {@link RenderedImage}. */
    private GridCoverage2D prepareCoverage(RenderedImage image, GridSampleDimension[] sampleDimensions, double[] noData)
            throws IOException {
        Map<String, Object> properties = new HashMap<>();
        if (noData != null && noData.length > 0) {
            CoverageUtilities.setNoDataProperty(properties, noData[0]);
        }
        properties.put(GridCoverage2DReader.SOURCE_URL_PROPERTY, datasetURL);
        return COVERAGE_FACTORY.create(
                request.name,
                image,
                new GridGeometry2D(
                        new GridEnvelope2D(PlanarImage.wrapRenderedImage(image).getBounds()),
                        PixelInCell.CELL_CORNER,
                        finalGridToWorldCorner,
                        this.targetBBox.getCoordinateReferenceSystem(),
                        hints),
                sampleDimensions,
                null,
                properties);
    }

    /**
     * Load a specified a raster as a portion of the granule describe by this {@link DefaultGranuleDescriptor}.
     *
     * @param imageReadParameters the {@link ImageReadParam} to use for reading.
     * @param index the index to use for the {@link ImageReader}.
     * @param cropBBox the bbox to use for cropping.
     * @param mosaicWorldToGrid the cropping grid to world transform.
     * @param hints {@link Hints} to be used for creating this raster.
     * @return a specified a raster as a portion of the granule describe by this {@link DefaultGranuleDescriptor}.
     * @throws IOException in case an error occurs.
     */
    private RenderedImage loadRaster(
            final ImageReadParam imageReadParameters,
            final int index,
            final ReferencedEnvelope cropBBox,
            final MathTransform2D mosaicWorldToGrid,
            final Hints hints,
            double[] noData)
            throws IOException {

        if (LOGGER.isLoggable(java.util.logging.Level.FINER)) {
            final String name = Thread.currentThread().getName();
            LOGGER.finer("Thread:" + name + " Loading raster data for granuleDescriptor " + this.toString());
        }
        ImageReadParam readParameters = null;
        int imageIndex;
        final ReferencedEnvelope bbox =
                request.spatialRequestHelper.getCoverageProperties().getBbox();

        // intersection of this tile bound with the current crop bbox
        final ReferencedEnvelope intersection =
                new ReferencedEnvelope(bbox.intersection(cropBBox), cropBBox.getCoordinateReferenceSystem());
        if (intersection.isEmpty()) {
            if (LOGGER.isLoggable(java.util.logging.Level.FINE)) {
                LOGGER.fine(new StringBuilder("Got empty intersection for granule ")
                        .append(this.toString())
                        .append(" with request ")
                        .append(request.toString())
                        .append(" Resulting in no granule loaded: Empty result")
                        .toString());
            }
            return null;
        }
        try {

            // What about thread safety?

            imageIndex = index;
            readParameters = imageReadParameters;

            // now create the crop grid to world which can be used to decide
            // which source area we need to crop in the selected level taking
            // into account the scale factors imposed by the selection of this
            // level together with the base level grid to world transformation

            final AffineTransform gridToWorldTransform_ = new AffineTransform();
            gridToWorldTransform_.preConcatenate(CoverageUtilities.CENTER_TO_CORNER);
            gridToWorldTransform_.preConcatenate(baseGridToWorld);
            AffineTransform2D cropWorldToGrid = new AffineTransform2D(gridToWorldTransform_);
            cropWorldToGrid = (AffineTransform2D) cropWorldToGrid.inverse();
            // computing the crop source area which lives into the
            // selected level raster space, NOTICE that at the end we need to
            // take into account the fact that we might also decimate therefore
            // we cannot just use the crop grid to world but we need to correct
            // it.
            Rectangle sourceArea =
                    CRS.transform(cropWorldToGrid, intersection).toRectangle2D().getBounds();
            // Selection of the source original area for cropping the computed source area
            // (may have negative values for the approximation)
            final Rectangle initialArea = request.source
                    .getSpatialDomain()
                    .getRasterElements(true, null)
                    .iterator()
                    .next()
                    .toRectangle();
            sourceArea = sourceArea.intersection(initialArea);

            if (sourceArea.isEmpty()) {
                if (LOGGER.isLoggable(java.util.logging.Level.FINE)) {
                    LOGGER.fine("Got empty area for granuleDescriptor "
                            + this.toString()
                            + " with request "
                            + request.toString()
                            + " Resulting in no granule loaded: Empty result");
                }
                return null;

            } else if (LOGGER.isLoggable(java.util.logging.Level.FINER)) {
                LOGGER.finer("Loading level "
                        + imageIndex
                        + " with source region: "
                        + sourceArea
                        + " subsampling: "
                        + readParameters.getSourceXSubsampling()
                        + ","
                        + readParameters.getSourceYSubsampling()
                        + " for granule:"
                        + datasetURL);
            }

            // set the source region
            readParameters.setSourceRegion(sourceArea);
            final RenderedImage raster;
            try {
                // read
                raster = request.readType.read(
                        readParameters,
                        imageIndex,
                        datasetURL,
                        request.spatialRequestHelper.getCoverageProperties().getRasterArea(),
                        request.source.reader,
                        hints,
                        false);

            } catch (Throwable e) {
                if (LOGGER.isLoggable(java.util.logging.Level.FINE)) {
                    LOGGER.log(
                            java.util.logging.Level.FINE,
                            "Unable to load raster for granuleDescriptor "
                                    + this.toString()
                                    + " with request "
                                    + request.toString()
                                    + " Resulting in no granule loaded: Empty result",
                            e);
                }
                return null;
            }

            // use fixed source area
            sourceArea.setRect(readParameters.getSourceRegion());

            //
            // setting new coefficients to define a new affineTransformation
            // to be applied to the grid to world transformation
            // -----------------------------------------------------------------------------------
            //
            // With respect to the original envelope, the obtained planarImage
            // needs to be rescaled. The scaling factors are computed as the
            // ratio between the cropped source region sizes and the read
            // image sizes.
            //
            // place it in the mosaic using the coords created above;
            double decimationScaleX = 1.0 * sourceArea.width / raster.getWidth();
            double decimationScaleY = 1.0 * sourceArea.height / raster.getHeight();
            final AffineTransform decimationScaleTranform =
                    XAffineTransform.getScaleInstance(decimationScaleX, decimationScaleY);

            // keep into account translation to work into the selected level raster space
            final AffineTransform afterDecimationTranslateTranform =
                    XAffineTransform.getTranslateInstance(sourceArea.x, sourceArea.y);

            // // now we need to go back to the base level raster space
            // final AffineTransform backToBaseLevelScaleTransform
            // =selectedlevel.baseToLevelTransform;
            //
            // now create the overall transform
            final AffineTransform finalRaster2Model = new AffineTransform(baseGridToWorld);
            finalRaster2Model.concatenate(CoverageUtilities.CENTER_TO_CORNER);

            if (!XAffineTransform.isIdentity(afterDecimationTranslateTranform, EPS))
                finalRaster2Model.concatenate(afterDecimationTranslateTranform);
            if (!XAffineTransform.isIdentity(decimationScaleTranform, EPS))
                finalRaster2Model.concatenate(decimationScaleTranform);

            // keep into account translation factors to place this tile
            finalRaster2Model.preConcatenate((AffineTransform) mosaicWorldToGrid);

            final Interpolation interpolation = request.getInterpolation();
            // paranoiac check to avoid that JAI freaks out when computing its internal layouT on
            // images that are too small
            Rectangle2D finalLayout = ImageUtilities.layoutHelper(
                    raster,
                    (float) finalRaster2Model.getScaleX(),
                    (float) finalRaster2Model.getScaleY(),
                    (float) finalRaster2Model.getTranslateX(),
                    (float) finalRaster2Model.getTranslateY(),
                    interpolation);
            if (finalLayout.isEmpty()) {
                if (LOGGER.isLoggable(java.util.logging.Level.INFO))
                    LOGGER.info("Unable to create a granuleDescriptor "
                            + this.toString()
                            + " due to jai scale bug creating a null source area");
                return null;
            }
            // apply the affine transform conserving indexed color model
            final RenderingHints localHints = new RenderingHints(
                    JAI.KEY_REPLACE_INDEX_COLOR_MODEL,
                    interpolation instanceof InterpolationNearest ? Boolean.FALSE : Boolean.TRUE);
            //
            // In case we are asked to use certain tile dimensions we tile
            // also at this stage in case the read type is Direct since
            // buffered images comes up untiled and this can affect the
            // performances of the subsequent affine operation.
            //
            // final Dimension tileDimensions=request.getTileDimensions();
            // if(tileDimensions!=null&&request.getReadType().equals(ReadType.DIRECT_READ)) {
            // final ImageLayout layout = new ImageLayout();
            // layout.setTileHeight(tileDimensions.width).setTileWidth(tileDimensions.height);
            // localHints.add(new RenderingHints(JAI.KEY_IMAGE_LAYOUT,layout));
            // } else {
            // if (hints != null && hints.containsKey(JAI.KEY_IMAGE_LAYOUT)) {
            // final Object layout = hints.get(JAI.KEY_IMAGE_LAYOUT);
            // if (layout != null && layout instanceof ImageLayout) {
            // localHints.add(new RenderingHints(JAI.KEY_IMAGE_LAYOUT, ((ImageLayout)
            // layout).clone()));
            // }
            // }
            // }
            if (hints != null && hints.containsKey(JAI.KEY_TILE_CACHE)) {
                final Object cache = hints.get(JAI.KEY_TILE_CACHE);
                if (cache != null && cache instanceof TileCache)
                    localHints.add(new RenderingHints(JAI.KEY_TILE_CACHE, cache));
            }
            if (hints != null && hints.containsKey(JAI.KEY_TILE_SCHEDULER)) {
                final Object scheduler = hints.get(JAI.KEY_TILE_SCHEDULER);
                if (scheduler != null && scheduler instanceof TileScheduler)
                    localHints.add(new RenderingHints(JAI.KEY_TILE_SCHEDULER, scheduler));
            }
            boolean addBorderExtender = true;
            if (hints != null && hints.containsKey(JAI.KEY_BORDER_EXTENDER)) {
                final Object extender = hints.get(JAI.KEY_BORDER_EXTENDER);
                if (extender != null && extender instanceof BorderExtender) {
                    localHints.add(new RenderingHints(JAI.KEY_BORDER_EXTENDER, extender));
                    addBorderExtender = false;
                }
            }
            // border extender
            if (addBorderExtender) {
                localHints.add(ImageUtilities.BORDER_EXTENDER_HINTS);
            }

            ImageWorker iw = new ImageWorker(raster);
            iw.setRenderingHints(localHints);
            iw.affine(finalRaster2Model, interpolation, noData);
            return iw.getRenderedImage();

        } catch (org.geotools.api.referencing.operation.NoninvertibleTransformException e) {
            if (LOGGER.isLoggable(java.util.logging.Level.WARNING)) {
                LOGGER.log(
                        java.util.logging.Level.WARNING,
                        new StringBuilder("Unable to load raster for granuleDescriptor ")
                                .append(this.toString())
                                .append(" with request ")
                                .append(request.toString())
                                .append(" Resulting in no granule loaded: Empty result")
                                .toString(),
                        e);
            }
            return null;
        } catch (IllegalStateException | TransformException e) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.log(
                        Level.WARNING,
                        new StringBuilder("Unable to load raster for granuleDescriptor ")
                                .append(this.toString())
                                .append(" with request ")
                                .append(request.toString())
                                .append(" Resulting in no granule loaded: Empty result")
                                .toString(),
                        e);
            }
            return null;
        }
    }

    /**
     * This method is responsible for evaluating possible subsampling factors once the best resolution level has been
     * found, in case we have support for overviews, or starting from the original coverage in case there are no
     * overviews available.
     *
     * <p>Anyhow this method should not be called directly but subclasses should make use of the setReadParams method
     * instead in order to transparently look for overviews.
     */
    private void performDecimation(ImageReadParam readParameters) {

        final double[] requestedResolution = request.spatialRequestHelper.getRequestedResolution();
        final Rectangle coverageRasterArea =
                request.spatialRequestHelper.getCoverageProperties().getRasterArea();
        final double[] fullResolution =
                request.spatialRequestHelper.getCoverageProperties().getFullResolution();
        // the read parameters cannot be null
        Utilities.ensureNonNull("readParameters", readParameters);

        // get the requested resolution
        if (requestedResolution == null) {
            // if there is no requested resolution we don't do any
            // subsampling
            readParameters.setSourceSubsampling(1, 1, 0, 0);
            return;
        }

        // highest resolution
        final int rasterWidth = coverageRasterArea.width;
        final int rasterHeight = coverageRasterArea.height;

        ImageUtilities.setSubsamplingFactors(
                readParameters, requestedResolution, fullResolution, rasterWidth, rasterHeight);
    }
}
