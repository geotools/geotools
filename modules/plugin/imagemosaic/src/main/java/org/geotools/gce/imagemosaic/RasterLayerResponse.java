/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007 - 2016, Open Source Geospatial Foundation (OSGeo)
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

import com.sun.media.imageioimpl.common.BogusColorSpace;
import it.geosolutions.imageio.imageioimpl.EnhancedImageReadParam;
import it.geosolutions.imageio.pam.PAMDataset;
import it.geosolutions.jaiext.range.NoDataContainer;
import it.geosolutions.jaiext.range.Range;
import it.geosolutions.jaiext.range.RangeFactory;
import it.geosolutions.jaiext.utilities.ImageLayout2;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageReadParam;
import javax.measure.Unit;
import javax.media.jai.ImageLayout;
import javax.media.jai.Interpolation;
import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.ROI;
import javax.media.jai.RenderedOp;
import javax.media.jai.operator.ConstantDescriptor;
import javax.media.jai.operator.MosaicDescriptor;
import org.geotools.coverage.Category;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.TypeMap;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.GranuleSource;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.coverage.grid.io.footprint.FootprintBehavior;
import org.geotools.coverage.util.CoverageUtilities;
import org.geotools.coverage.util.FeatureUtilities;
import org.geotools.data.DataSourceException;
import org.geotools.data.DataUtilities;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.gce.imagemosaic.OverviewsController.OverviewLevel;
import org.geotools.gce.imagemosaic.catalog.GranuleCatalogVisitor;
import org.geotools.gce.imagemosaic.egr.ROIExcessGranuleRemover;
import org.geotools.gce.imagemosaic.granulecollector.DefaultSubmosaicProducer;
import org.geotools.gce.imagemosaic.granulecollector.DefaultSubmosaicProducerFactory;
import org.geotools.gce.imagemosaic.granulecollector.SubmosaicProducer;
import org.geotools.gce.imagemosaic.granulecollector.SubmosaicProducerFactory;
import org.geotools.geometry.Envelope2D;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.geometry.util.XRectangle2D;
import org.geotools.image.ImageWorker;
import org.geotools.image.util.ImageUtilities;
import org.geotools.metadata.i18n.Vocabulary;
import org.geotools.metadata.i18n.VocabularyKeys;
import org.geotools.referencing.CRS;
import org.geotools.referencing.operation.transform.AffineTransform2D;
import org.geotools.renderer.crs.ProjectionHandler;
import org.geotools.renderer.crs.ProjectionHandlerFinder;
import org.geotools.util.NumberRange;
import org.geotools.util.SimpleInternationalString;
import org.geotools.util.factory.Hints;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.util.Assert;
import org.opengis.coverage.ColorInterpretation;
import org.opengis.coverage.SampleDimension;
import org.opengis.coverage.SampleDimensionType;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.geometry.BoundingBox;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.datum.PixelInCell;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.MathTransform2D;
import org.opengis.referencing.operation.TransformException;
import org.opengis.util.InternationalString;

/**
 * A RasterLayerResponse. An instance of this class is produced everytime a requestCoverage is
 * called to a reader.
 *
 * @author Simone Giannecchini, GeoSolutions
 * @author Daniele Romagnoli, GeoSolutions
 * @author Stefan Alfons Krueger (alfonx), Wikisquare.de : Support for
 *     jar:file:foo.jar/bar.properties URLs
 */
@SuppressWarnings("rawtypes")
public class RasterLayerResponse {

    private final SubmosaicProducerFactory submosaicProducerFactory;

    class MosaicOutput {

        public MosaicOutput(MosaicElement element) {
            this.image = element.source;
            this.pamDataset = element.pamDataset;
        }

        public MosaicOutput(RenderedImage image, PAMDataset pamDataset) {
            super();
            this.image = image;
            this.pamDataset = pamDataset;
        }

        public RenderedImage getImage() {
            return image;
        }

        public void setImage(RenderedImage image) {
            this.image = image;
        }

        public PAMDataset getPamDataset() {
            return pamDataset;
        }

        public void setPamDataset(PAMDataset pamDataset) {
            this.pamDataset = pamDataset;
        }

        RenderedImage image;

        PAMDataset pamDataset;
    }

    private static final class SimplifiedGridSampleDimension extends GridSampleDimension
            implements SampleDimension {

        /** */
        private static final long serialVersionUID = 2227219522016820587L;

        private double nodata;

        private double minimum;

        private double maximum;

        private double scale;

        private double offset;

        private Unit<?> unit;

        private SampleDimensionType type;

        private ColorInterpretation color;

        public SimplifiedGridSampleDimension(
                CharSequence description,
                SampleDimensionType type,
                ColorInterpretation color,
                double nodata,
                double minimum,
                double maximum,
                double scale,
                double offset,
                Unit<?> unit) {
            super(
                    description,
                    !Double.isNaN(nodata)
                            ? new Category[] {
                                new Category(
                                        Vocabulary.formatInternational(VocabularyKeys.NODATA),
                                        new Color[] {new Color(0, 0, 0, 0)},
                                        NumberRange.create(nodata, nodata))
                            }
                            : null,
                    unit);
            this.nodata = nodata;
            this.minimum = minimum;
            this.maximum = maximum;
            this.scale = scale;
            this.offset = offset;
            this.unit = unit;
            this.type = type;
            this.color = color;
        }

        @Override
        public double getMaximumValue() {
            return maximum;
        }

        @Override
        public double getMinimumValue() {
            return minimum;
        }

        @Override
        public double[] getNoDataValues() throws IllegalStateException {
            return new double[] {nodata};
        }

        @Override
        public double getOffset() throws IllegalStateException {
            return offset;
        }

        @Override
        public NumberRange<? extends Number> getRange() {
            return super.getRange();
        }

        @Override
        public SampleDimensionType getSampleDimensionType() {
            return type;
        }

        @Override
        public Unit<?> getUnits() {
            return unit;
        }

        @Override
        public double getScale() {
            return scale;
        }

        @Override
        public ColorInterpretation getColorInterpretation() {
            return color;
        }

        @Override
        public InternationalString[] getCategoryNames() throws IllegalStateException {
            return new InternationalString[] {SimpleInternationalString.wrap("Background")};
        }
    }

    /**
     * This class is responsible for putting together the granules for the final mosaic.
     *
     * @author Simone Giannecchini, GeoSolutions SAS
     */
    private class MosaicProducer implements GranuleCatalogVisitor {

        /** The number of granules actually dispatched to the internal collectors. */
        private int granulesNumber;

        /** The {@link MergeBehavior} indicated into the request. */
        private MergeBehavior mergeBehavior;

        /** The internal collectors for incoming granules. */
        private List<SubmosaicProducer> granuleCollectors = new ArrayList<>();

        private boolean heterogeneousCRS;

        /** Default {@link Constructor} */
        private MosaicProducer(List<SubmosaicProducer> collectors) {
            this(false, collectors);
        }

        /**
         * {@link MosaicProducer} constructor. It can be used to specify that we want to perform a
         * dry run just to count the granules we would load with the specified query.
         *
         * <p>
         *
         * <p>A dry run means: no tasks are executed.
         *
         * @param dryRun <code>true</code> for a dry run, <code>false</code> otherwise.
         */
        private MosaicProducer(final boolean dryRun, List<SubmosaicProducer> collectors) {
            this.granuleCollectors = collectors;
            this.mergeBehavior = request.getMergeBehavior();
            this.heterogeneousCRS = collectors.stream().anyMatch(c -> c.isReprojecting());
        }

        /**
         * This method accepts incming granules and dispatch them to the correct {@link
         * DefaultSubmosaicProducer} depending on the internal {@link Filter} per the dimension.
         *
         * <p>
         *
         * <p>If not {@link MergeBehavior#STACK}ing is required, we collect them all together with
         * an include filter.
         */
        public void visit(GranuleDescriptor granuleDescriptor, SimpleFeature sf) {
            //
            // load raster data
            //
            // create a granuleDescriptor loader
            final Geometry bb = JTS.toGeometry((BoundingBox) mosaicBBox);
            Geometry inclusionGeometry = granuleDescriptor.getFootprint();
            boolean intersects = false;
            if (inclusionGeometry != null) {
                CoordinateReferenceSystem granuleCRS =
                        granuleDescriptor.getGranuleEnvelope().getCoordinateReferenceSystem();
                CoordinateReferenceSystem mosaicCRS = mosaicBBox.getCoordinateReferenceSystem();
                try {
                    if (!CRS.equalsIgnoreMetadata(granuleCRS, mosaicCRS)) {
                        ProjectionHandler handler =
                                ProjectionHandlerFinder.getHandler(mosaicBBox, granuleCRS, true);
                        MathTransform mt = CRS.findMathTransform(granuleCRS, mosaicCRS);
                        if (handler != null) {
                            Geometry preProcessed = handler.preProcess(inclusionGeometry);
                            if (preProcessed != null) {
                                Geometry transformed = JTS.transform(inclusionGeometry, mt);
                                inclusionGeometry = handler.postProcess(mt.inverse(), transformed);
                            }
                        } else {
                            inclusionGeometry = JTS.transform(inclusionGeometry, mt);
                        }
                    }
                    intersects = inclusionGeometry.intersects(bb);
                } catch (FactoryException | MismatchedDimensionException | TransformException e) {
                    // in case there was a reprojection issue assume intersection
                    intersects = true;
                }
                intersects = inclusionGeometry.intersects(bb);
            }
            if (!footprintBehavior.handleFootprints()
                    || inclusionGeometry == null
                    || (footprintBehavior.handleFootprints() && intersects)) {

                // find the right filter for this granule
                boolean found = false;
                for (SubmosaicProducer submosaicProducer : granuleCollectors) {
                    if (submosaicProducer.accept(granuleDescriptor)) {
                        granulesNumber++;
                        found = true;
                        break;
                    }
                }

                // did we find a place for it? If we are doing EGR then it's ok, if we are dealing
                // with an heterogenous CRS that also happens when zooming out a lot, otherwise not
                // so much
                if (!found && getExcessGranuleRemover() == null && !heterogeneousCRS) {
                    throw new IllegalStateException(
                            "Unable to locate a granule collector accepting this granule:\n"
                                    + granuleDescriptor.toString());
                }

            } else {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.fine(
                            "We rejected for non ROI inclusion the granule "
                                    + granuleDescriptor.toString());
                }
            }
        }

        @Override
        public boolean isVisitComplete() {
            ROIExcessGranuleRemover remover = getExcessGranuleRemover();
            return remover != null && remover.isRenderingAreaComplete();
        }

        /**
         * This method is responsible for producing the final mosaic.
         *
         * <p>
         *
         * <p>Depending on whether or not a {@link MergeBehavior#STACK}ing is required, we perform 1
         * or 2 steps.
         *
         * <ol>
         *   <li>step 1 is for merging flat on each value for the dimension
         *   <li>step 2 is for merging stack on the resulting mosaics
         * </ol>
         */
        private MosaicOutput produce() throws IOException {
            // checks
            if (granulesNumber == 0) {
                LOGGER.log(Level.FINE, "Unable to load any granuleDescriptor");
                return null;
            }

            // STEP 1 collect all the mosaics from each single dimension
            LOGGER.fine("Producing the final mosaic, step 1, loop through granule collectors");
            final List<MosaicElement> mosaicInputs = new ArrayList<MosaicElement>();
            SubmosaicProducer first = null; // we take this apart to steal some val
            int size = 0;
            for (SubmosaicProducer collector : granuleCollectors) {
                if (LOGGER.isLoggable(Level.FINER)) {
                    LOGGER.fine("Submosaic producer being called: " + collector.toString());
                }
                final List<MosaicElement> preparedMosaic = collector.createMosaic();
                if (preparedMosaic.size() > 0
                        && !preparedMosaic.stream().allMatch(p -> p == null)) {
                    size += preparedMosaic.size();
                    mosaicInputs.addAll(preparedMosaic);
                    if (first == null) {
                        first = collector;
                    }
                }
            }
            LOGGER.fine("Producing the final mosaic, step 2, final mosaicking");
            // optimization
            if (size == 1) {
                // we don't need to mosaick again
                return new MosaicOutput(mosaicInputs.get(0));
            }
            // no mosaics produced, it might happen, see above
            if (size == 0) {
                return null;
            }

            MosaicInputs mosaickingInputs =
                    new MosaicInputs(
                            first.doInputTransparency(),
                            first.hasAlpha(),
                            mosaicInputs,
                            first.getSourceThreshold());
            // normal situation
            return new MosaicOutput(
                    new Mosaicker(RasterLayerResponse.this, mosaickingInputs, mergeBehavior)
                            .createMosaic());
        }

        public SubmosaicProducerFactory getGranuleCollectorsFactory() {
            return new DefaultSubmosaicProducerFactory();
        }
    }

    /** Logger. */
    private static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(RasterLayerResponse.class);

    /** The GridCoverage produced after a {@link #createResponse()} method call */
    private GridCoverage2D gridCoverage;

    /** The {@link RasterLayerRequest} originating this response */
    private RasterLayerRequest request;

    /** The coverage factory producing a {@link GridCoverage} from an image */
    private GridCoverageFactory coverageFactory;

    /** The base envelope related to the input coverage */
    private GeneralEnvelope coverageEnvelope;

    private RasterManager rasterManager;

    private Color finalTransparentColor;

    private ReferencedEnvelope mosaicBBox;

    private Rectangle rasterBounds;

    private MathTransform2D finalGridToWorldCorner;

    private MathTransform2D finalWorldToGridCorner;

    private int imageChoice = 0;

    private EnhancedImageReadParam baseReadParameters = new EnhancedImageReadParam();

    private boolean multithreadingAllowed;

    private FootprintBehavior footprintBehavior = FootprintBehavior.None;

    private int defaultArtifactsFilterThreshold = Integer.MIN_VALUE;

    private double artifactsFilterPTileThreshold =
            ImageMosaicFormat.DEFAULT_ARTIFACTS_FILTER_PTILE_THRESHOLD;

    private boolean oversampledRequest;

    private MathTransform baseGridToWorld;

    private double[] virtualNativeResolution;

    private Geometry geometryMask;

    private double maskingBufferPixels;

    private boolean setRoiProperty;

    private boolean heterogeneousCRS;

    private double[] backgroundValues;

    private Hints hints;

    private String granulesPaths;

    /** See {@link GridCoverage2DReader#SOURCE_URL_PROPERTY}. */
    private URL sourceUrl;

    private ROIExcessGranuleRemover excessGranuleRemover;

    /**
     * Construct a {@code RasterLayerResponse} given a specific {@link RasterLayerRequest}, a {@code
     * GridCoverageFactory} to produce {@code GridCoverage}s and an {@code ImageReaderSpi} to be
     * used for instantiating an Image Reader for a read operation,
     *
     * @param request a {@link RasterLayerRequest} originating this response.
     * @param rasterManager raster manager being used
     */
    public RasterLayerResponse(
            final RasterLayerRequest request,
            final RasterManager rasterManager,
            SubmosaicProducerFactory collectorsFactory) {
        this.request = request;
        coverageEnvelope = rasterManager.spatialDomainManager.coverageEnvelope;
        this.coverageFactory = rasterManager.getCoverageFactory();
        this.rasterManager = rasterManager;
        this.hints = rasterManager.getHints();
        this.submosaicProducerFactory = collectorsFactory;
        baseGridToWorld = rasterManager.spatialDomainManager.coverageGridToWorld2D;
        finalTransparentColor = request.getOutputTransparentColor();
        // are we doing multithreading?
        multithreadingAllowed = request.isMultithreadingAllowed();
        footprintBehavior = request.getFootprintBehavior();
        backgroundValues = request.getBackgroundValues();
        defaultArtifactsFilterThreshold = request.getDefaultArtifactsFilterThreshold();
        artifactsFilterPTileThreshold = request.getArtifactsFilterPTileThreshold();
        virtualNativeResolution = request.getVirtualNativeResolution();
        geometryMask = request.getGeometryMask();
        maskingBufferPixels = request.getMaskingBufferPixels();
        setRoiProperty = request.isSetRoiProperty();
    }

    /**
     * Compute the coverage request and produce a grid coverage which will be returned by {@link
     * #createResponse()}. The produced grid coverage may be {@code null} in case of empty request.
     *
     * @return the {@link GridCoverage} produced as computation of this response using the {@link
     *     #createResponse()} method.
     * @uml.property name="gridCoverage"
     */
    public GridCoverage2D createResponse() throws IOException {
        processRequest();
        return gridCoverage;
    }

    /**
     * @return the {@link RasterLayerRequest} originating this response.
     * @uml.property name="request"
     */
    public RasterLayerRequest getOriginatingCoverageRequest() {
        return request;
    }

    /**
     * This method creates the GridCoverage2D from the underlying file given a specified envelope,
     * and a requested dimension.
     *
     * @throws java.io.IOException
     */
    private void processRequest() throws IOException {

        if (request.isEmpty()) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "Request is empty: " + request.toString());
            }
            this.gridCoverage = null;
            return;
        }

        // add extra parameters to image parameters reader
        baseReadParameters.setBands(request.getBands());

        // assemble granules
        final MosaicOutput mosaic = prepareResponse();
        if (mosaic == null || mosaic.image == null) {
            this.gridCoverage = null;
            return;
        }

        // postproc
        MosaicOutput finalMosaic = postProcessRaster(mosaic);
        // create the coverage
        gridCoverage = prepareCoverage(finalMosaic);
    }

    private MosaicOutput postProcessRaster(MosaicOutput mosaickedImage) {
        // alpha on the final mosaic
        if (finalTransparentColor != null) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("Support for alpha on final mosaic");
            }
            // Keep ROI as property
            ImageWorker imageWorker = new ImageWorker(mosaickedImage.image);
            imageWorker.makeColorTransparent(finalTransparentColor);
            RenderedOp image = imageWorker.getRenderedOperation();
            if (imageWorker.getROI() != null) {
                image.setProperty("ROI", imageWorker.getROI());
            }
            return new MosaicOutput(image, mosaickedImage.pamDataset);
        }

        return mosaickedImage;
    }

    /**
     * This method loads the granules which overlap the requested {@link GeneralEnvelope} using the
     * provided values for alpha and input ROI.
     *
     * @return the mosaic output for the request
     */
    private MosaicOutput prepareResponse() throws DataSourceException {

        try {
            // === select overview
            chooseOverview();

            // === extract bbox
            initBBOX();

            // === init transformations
            initTransformations();

            // === init raster bounds
            initRasterBounds();

            // === init excess granule removal if needed
            initExcessGranuleRemover();

            // === create query and basic BBOX filtering
            MosaicQueryBuilder queryBuilder = new MosaicQueryBuilder(request, mosaicBBox);
            final Query query = queryBuilder.build();

            // === collect granules
            final MosaicProducer visitor =
                    new MosaicProducer(
                            submosaicProducerFactory.createProducers(
                                    this.getRequest(), this.getRasterManager(), this, false));
            rasterManager.getGranuleDescriptors(query, visitor);

            // get those granules and create the final mosaic
            heterogeneousCRS = visitor.heterogeneousCRS;
            MosaicOutput returnValue = visitor.produce();

            //
            // Did we actually load anything?? Notice that it might happen that
            // either we have holes inside the definition area for the mosaic
            // or we had some problem with missing tiles, therefore it might
            // happen that for some bboxes we don't have anything to load.
            //

            //
            // Create the mosaic image by doing a crop if necessary and also
            // managing the transparent color if applicable. Be aware that
            // management of the transparent color involves removing
            // transparency information from the input images.
            //
            if (returnValue != null) {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.fine(
                            "Loaded bbox "
                                    + mosaicBBox.toString()
                                    + " while crop bbox "
                                    + request.spatialRequestHelper.getComputedBBox().toString());
                }
                return returnValue;
            }

            if (visitor.granulesNumber == 0) {
                // Redo the query without filter to check whether we got no granules due
                // to a filter. In that case we need to return null
                // Notice that we are using a dryRun visitor to make sure we don't
                // spawn any loading tasks, we also ensure we get only 1 feature at most
                // to make this blazing fast
                LOGGER.fine("We got no granules, let's do a dry run with no filters");
                List<SubmosaicProducer> collectors =
                        submosaicProducerFactory.createProducers(
                                this.getRequest(), this.getRasterManager(), this, true);
                final MosaicProducer dryRunVisitor = new MosaicProducer(true, collectors);
                final Utils.BBOXFilterExtractor bboxExtractor = new Utils.BBOXFilterExtractor();
                query.getFilter().accept(bboxExtractor, null);
                query.setFilter(
                        FeatureUtilities.DEFAULT_FILTER_FACTORY.bbox(
                                FeatureUtilities.DEFAULT_FILTER_FACTORY.property(
                                        rasterManager
                                                .getGranuleCatalog()
                                                .getType(rasterManager.getTypeName())
                                                .getGeometryDescriptor()
                                                .getName()),
                                bboxExtractor.getBBox()));
                query.setMaxFeatures(1);
                rasterManager.getGranuleDescriptors(query, dryRunVisitor);
                if (dryRunVisitor.granulesNumber > 0) {
                    LOGGER.fine(
                            "Dry run got a target granule, returning null as the additional filters did filter all the granules out");
                    // It means the previous lack of granule was due to a filter excluding all the
                    // results. Then we return null
                    return null;
                }
            }

            // do we return a null (outside of the coverage) or a blank? The choice is "hard" as we
            // might be in a hole of the coverage and not know it
            if (!mosaicBBox.intersects((BoundingBox) ReferencedEnvelope.reference(coverageEnvelope))
                    && !mosaicBBox.intersects(
                            (BoundingBox)
                                    ReferencedEnvelope.reference(
                                            rasterManager.spatialDomainManager.coverageBBox))) {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.fine(
                            "Could not locate any granule in the requested bbox, returning null as it does not "
                                    + "match the cached bbox of the mosaic");
                }
                return null;
            } else {
                // prepare a blank response
                return createBlankResponse();
            }

        } catch (Exception e) {
            throw new DataSourceException("Unable to create this mosaic", e);
        }
    }

    private void initExcessGranuleRemover() {
        if (request.getExcessGranuleRemovalPolicy() == ExcessGranulePolicy.ROI) {
            Dimension tileDimensions = request.getTileDimensions();
            int tileWidth, tileHeight;
            if (tileDimensions != null) {
                tileWidth = (int) tileDimensions.getWidth();
                tileHeight = (int) tileDimensions.getHeight();
            } else {
                tileWidth = tileHeight = ROIExcessGranuleRemover.DEFAULT_TILE_SIZE;
            }
            excessGranuleRemover =
                    new ROIExcessGranuleRemover(
                            rasterBounds,
                            tileWidth,
                            tileHeight,
                            rasterManager.getConfiguration().getCrs());
        }
    }

    /**
     * This method is responsible for computing the raster bounds of the final mosaic.
     *
     * @throws TransformException In case transformation fails during the process.
     */
    private void initRasterBounds() throws TransformException {
        final GeneralEnvelope tempRasterBounds = CRS.transform(finalWorldToGridCorner, mosaicBBox);
        rasterBounds = tempRasterBounds.toRectangle2D().getBounds();

        // SG using the above may lead to problems since the reason is that may be a little (1 px)
        // bigger
        // than what we need. The code below is a bit better since it uses a proper logic (see
        // GridEnvelope
        // Javadoc)
        rasterBounds =
                new GridEnvelope2D(new Envelope2D(tempRasterBounds), PixelInCell.CELL_CORNER);
        if (rasterBounds.width == 0) rasterBounds.width++;
        if (rasterBounds.height == 0) rasterBounds.height++;
        if (oversampledRequest) rasterBounds.grow(2, 2);

        // make sure the expanded bounds are still within the reach of the granule bounds, not
        // larger
        // (the above expansion might have made them so)
        final GeneralEnvelope levelRasterArea_ =
                CRS.transform(
                        finalWorldToGridCorner, request.spatialRequestHelper.getCoverageBBox());
        final GridEnvelope2D levelRasterArea =
                new GridEnvelope2D(new Envelope2D(levelRasterArea_), PixelInCell.CELL_CORNER);
        XRectangle2D.intersect(levelRasterArea, rasterBounds, rasterBounds);
    }

    /**
     * This method is responsible for initializing transformations g2w and back
     *
     * @throws Exception in case we don't manage to instantiate some of them.
     */
    private void initTransformations() throws Exception {
        // compute final world to grid
        // base grid to world for the center of pixels
        final AffineTransform g2w;
        final SpatialRequestHelper spatialRequestHelper = request.spatialRequestHelper;
        if (!request.isHeterogeneousGranules()) {
            final OverviewLevel baseLevel =
                    rasterManager.overviewsController.resolutionsLevels.get(0);
            final OverviewLevel selectedLevel =
                    rasterManager.overviewsController.resolutionsLevels.get(imageChoice);
            final double resX = baseLevel.resolutionX;
            final double resY = baseLevel.resolutionY;
            final double[] requestRes = spatialRequestHelper.getComputedResolution();

            BoundingBox computedBBox = spatialRequestHelper.getComputedBBox();
            GeneralEnvelope requestedRasterArea =
                    CRS.transform(baseGridToWorld.inverse(), computedBBox);
            double minxRaster = Math.round(requestedRasterArea.getMinimum(0));
            double minyRaster = Math.round(requestedRasterArea.getMinimum(1));

            // rebase the grid to world location to a position close to the requested one to
            // avoid JAI playing with very large raster coordinates
            // This can be done because the final computation generates the coordinates of the
            // output coverage based on the output raster bounds and this very transform
            final AffineTransform at = (AffineTransform) baseGridToWorld;
            Point2D src = new Point2D.Double(minxRaster, minyRaster);
            Point2D dst = new Point2D.Double();
            at.transform(src, dst);
            g2w =
                    new AffineTransform(
                            at.getScaleX(),
                            at.getShearX(),
                            at.getShearY(),
                            at.getScaleY(),
                            dst.getX(),
                            dst.getY());
            g2w.concatenate(CoverageUtilities.CENTER_TO_CORNER);

            if ((requestRes[0] < resX || requestRes[1] < resY)) {
                // Using the best available resolution
                oversampledRequest = true;
            }
            if (virtualNativeResolution != null
                    && !Double.isNaN(virtualNativeResolution[0])
                    && !Double.isNaN(virtualNativeResolution[1])) {
                if (virtualNativeResolution[0] < resX || virtualNativeResolution[1] < resY) {
                    oversampledRequest = true;
                } else {
                    oversampledRequest = false;
                }
            }
            if (!oversampledRequest) {
                // SG going back to working on a per level basis to do the composition
                // g2w = new AffineTransform(request.getRequestedGridToWorld());
                g2w.concatenate(
                        AffineTransform.getScaleInstance(
                                selectedLevel.scaleFactor, selectedLevel.scaleFactor));
                g2w.concatenate(
                        AffineTransform.getScaleInstance(
                                baseReadParameters.getSourceXSubsampling(),
                                baseReadParameters.getSourceYSubsampling()));
            }
        } else {
            g2w = new AffineTransform(spatialRequestHelper.getComputedGridToWorld());
            g2w.concatenate(CoverageUtilities.CENTER_TO_CORNER);
        }
        // move it to the corner
        finalGridToWorldCorner = new AffineTransform2D(g2w);
        finalWorldToGridCorner = finalGridToWorldCorner.inverse(); // compute raster bounds
    }

    /**
     * This method is responsible for initializing the bbox for the mosaic produced by this
     * response.
     */
    private void initBBOX() {
        // ok we got something to return, let's load records from the index
        final BoundingBox cropBBOX = request.spatialRequestHelper.getComputedBBox();
        if (cropBBOX != null) {
            mosaicBBox = ReferencedEnvelope.reference(cropBBOX);
        } else {
            mosaicBBox = new ReferencedEnvelope(coverageEnvelope);
        }
    }

    /**
     * This method encloses the standard behavior for the selection of the proper overview level.
     *
     * <p>See {@link ReadParamsController}
     */
    private void chooseOverview() throws IOException, TransformException {
        //
        // prepare the params for executing a mosaic operation.
        //
        // It might important to set the mosaic type to blend otherwise
        // sometimes strange results jump in.

        // select the relevant overview, notice that at this time we have
        // relaxed a bit the requirement to have the same exact resolution
        // for all the overviews, but still we do not allow for reading the
        // various grid to world transform directly from the input files,
        // therefore we are assuming that each granuleDescriptor has a scale and
        // translate only grid to world that can be deduced from its base
        // level dimension and envelope. The grid to world transforms for
        // the other levels can be computed accordingly knowing the scale
        // factors.
        if (request.spatialRequestHelper.getComputedBBox() != null
                && request.spatialRequestHelper.getComputedRasterArea() != null
                && !request.isHeterogeneousGranules()) {
            imageChoice =
                    ReadParamsController.setReadParams(
                            request.spatialRequestHelper.getComputedResolution(),
                            request.getOverviewPolicy(),
                            request.getDecimationPolicy(),
                            baseReadParameters,
                            request.rasterManager,
                            request.rasterManager.overviewsController,
                            virtualNativeResolution); // use general overviews controller
        } else {
            imageChoice = 0;
        }
        assert imageChoice >= 0;
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine(
                    "Loading level "
                            + imageChoice
                            + " with subsampling factors "
                            + baseReadParameters.getSourceXSubsampling()
                            + " "
                            + baseReadParameters.getSourceYSubsampling());
        }
    }

    /**
     * This method is responsible for creating a blank image as a reponse to the query as it seems
     * we got a no data area.
     *
     * @return a blank {@link RenderedImage} initialized using the background values
     */
    private MosaicOutput createBlankResponse() {
        // if we get here that means that we do not have anything to load
        // but still we are inside the definition area for the mosaic,
        // therefore we create a fake coverage using the background values,
        // if provided (defaulting to 0), as well as the compute raster
        // bounds, envelope and grid to world.
        LOGGER.fine("Creating constant image for area with no data");

        final ImageLayout2 il = new ImageLayout2();
        ColorModel cm = rasterManager.defaultCM;
        SampleModel sm = rasterManager.defaultSM;
        int[] bands = baseReadParameters.getBands();

        if (bands != null && cm != null && bands.length != cm.getNumComponents()) {
            final int nBands = bands.length;
            ColorSpace cs = null;
            switch (nBands) {
                case 1:
                    cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);
                    break;
                case 3:
                    cs = ColorSpace.getInstance(ColorSpace.CS_sRGB);
                    break;
                default:
                    cs = new BogusColorSpace(nBands);
            }
            cm =
                    new ComponentColorModel(
                            cs,
                            cm.hasAlpha(),
                            cm.isAlphaPremultiplied(),
                            cm.getTransparency(),
                            cm.getTransferType());
            sm = cm.createCompatibleSampleModel(sm.getWidth(), sm.getHeight());
        }

        il.setColorModel(cm);
        Dimension tileSize = request.getTileDimensions();
        if (tileSize == null) {
            tileSize = JAI.getDefaultTileSize();
        }

        il.setTileGridXOffset(0)
                .setTileGridYOffset(0)
                .setTileWidth((int) tileSize.getWidth())
                .setTileHeight((int) tileSize.getHeight());
        final RenderingHints renderingHints = new RenderingHints(JAI.KEY_IMAGE_LAYOUT, il);

        final Number[] values =
                ImageUtilities.getBackgroundValues(rasterManager.defaultSM, backgroundValues);
        RenderedImage finalImage;
        if (ImageUtilities.isMediaLibAvailable()) {
            // create a constant image with a proper layout
            finalImage =
                    ConstantDescriptor.create(
                            (float) rasterBounds.width,
                            (float) rasterBounds.height,
                            values,
                            renderingHints);
            if (rasterBounds.x != 0 || rasterBounds.y != 0) {
                ImageWorker w = new ImageWorker(finalImage);
                w.translate(
                        (float) rasterBounds.x,
                        (float) rasterBounds.y,
                        Interpolation.getInstance(Interpolation.INTERP_NEAREST));
                finalImage = w.getRenderedImage();
            }

            // impose the color model and samplemodel as the constant operation does not take them
            // into account!
            if (cm != null) {
                il.setColorModel(cm);
                il.setSampleModel(cm.createCompatibleSampleModel(tileSize.width, tileSize.height));
                finalImage =
                        new ImageWorker(finalImage)
                                .setRenderingHints(renderingHints)
                                .format(il.getSampleModel(null).getDataType())
                                .getRenderedImage();
            }
        } else {
            il.setWidth(rasterBounds.width).setHeight(rasterBounds.height);
            if (rasterBounds.x != 0 || rasterBounds.y != 0) {
                il.setMinX(rasterBounds.x).setMinY(rasterBounds.y);
            }
            // impose the color model and samplemodel as the constant operation does not take them
            // into account!
            if (cm == null) {
                byte[] arr = {(byte) 0, (byte) 0xff};
                cm = new IndexColorModel(1, 2, arr, arr, arr);
            }
            il.setColorModel(cm);
            il.setSampleModel(cm.createCompatibleSampleModel(tileSize.width, tileSize.height));

            final double[] bkgValues;
            if (bands != null && bands.length != 0) {
                // Extract the background values
                bkgValues = new double[bands.length];
                for (int k = 0; k < bands.length; k++) {
                    int index = k > values.length ? 0 : bands[k];
                    bkgValues[k] = values[index].doubleValue();
                }
            } else {
                bkgValues = new double[values.length];
                for (int i = 0; i < values.length; i++) {
                    bkgValues[i] = values[i].doubleValue();
                }
            }
            Assert.isTrue(
                    il.isValid(
                            ImageLayout.WIDTH_MASK
                                    | ImageLayout.HEIGHT_MASK
                                    | ImageLayout.SAMPLE_MODEL_MASK));
            ImageWorker w = new ImageWorker(renderingHints);
            w.setBackground(bkgValues);
            w.mosaic(
                    new RenderedImage[0],
                    MosaicDescriptor.MOSAIC_TYPE_OVERLAY,
                    null,
                    null,
                    new double[][] {
                        {
                            CoverageUtilities.getMosaicThreshold(
                                    il.getSampleModel(null).getDataType())
                        }
                    },
                    new Range[] {RangeFactory.create(0, 0)});
            // there was really nothing here, so make sure this comes out in the output
            Double noData = rasterManager.getConfiguration().getNoData();
            if (noData != null) {
                w.setNoData(RangeFactory.create(noData, noData));
            }
            finalImage = w.getRenderedImage();
        }
        //
        // TRANSPARENT COLOR MANAGEMENT
        //
        Color inputTransparentColor = request.getInputTransparentColor();
        boolean hasAlpha;
        if (inputTransparentColor != null
                && (footprintBehavior == null || !footprintBehavior.handleFootprints())) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("Support for alpha on blank image");
            }
            finalImage =
                    new ImageWorker(finalImage)
                            .makeColorTransparent(inputTransparentColor)
                            .getRenderedImage();
            hasAlpha = finalImage.getColorModel().hasAlpha();
            if (!hasAlpha) {
                // if the resulting image has no transparency (can happen with IndexColorModel then
                // we need to try component
                // color model
                finalImage =
                        new ImageWorker(finalImage)
                                .forceComponentColorModel(true)
                                .makeColorTransparent(inputTransparentColor)
                                .getRenderedImage();
                hasAlpha = finalImage.getColorModel().hasAlpha();
            }
            assert hasAlpha;

        } else if (footprintBehavior != null) {
            finalImage = footprintBehavior.postProcessBlankResponse(finalImage, renderingHints);
        }

        return new MosaicOutput(finalImage, null);
    }

    /**
     * This method is responsible for creating a coverage from the supplied {@link RenderedImage}.
     */
    private GridCoverage2D prepareCoverage(MosaicOutput mosaicOutput) throws IOException {

        // creating bands
        final RenderedImage image = mosaicOutput.image;
        final SampleModel sm = image.getSampleModel();
        final ColorModel cm = image.getColorModel();
        final int numBands =
                request.getBands() == null ? sm.getNumBands() : request.getBands().length;
        // quick check the possible provided bands names are equal the number of bands
        if (rasterManager.providedBandsNames != null
                && rasterManager.providedBandsNames.length != numBands) {
            // let's see if bands have been selected
            if (request.getBands() == null) {
                // no definitively there is something wrong
                throw new IllegalArgumentException(
                        "The number of provided bands names is different from the number of bands.");
            }
        }
        final GridSampleDimension[] bands = new GridSampleDimension[numBands];
        Set<String> bandNames = new HashSet<String>();
        // setting bands names.
        for (int i = 0; i < numBands; i++) {
            ColorInterpretation colorInterpretation = null;
            String bandName = null;

            // checking if bands names are provided, typical case for multiple bands dimensions
            if (rasterManager.providedBandsNames != null) {
                // we need to take in consideration if bands were selected
                if (request.getBands() == null) {
                    bandName = rasterManager.providedBandsNames[i];
                } else {
                    // using the selected band index to retrieve the correct provided name
                    bandName = rasterManager.providedBandsNames[request.getBands()[i]];
                }
            }

            if (cm != null) {
                // === color interpretation
                colorInterpretation = TypeMap.getColorInterpretation(cm, i);
                if (colorInterpretation == null) {
                    throw new IOException("Unrecognized sample dimension type");
                }

                if (bandName == null) {
                    bandName = colorInterpretation.name();
                    if (colorInterpretation == ColorInterpretation.UNDEFINED
                            || bandNames.contains(
                                    bandName)) { // make sure we create no duplicate band names
                        bandName = "Band" + (i + 1);
                    }
                }
            } else { // no color model
                if (bandName == null) {
                    bandName = "Band" + (i + 1);
                }
                colorInterpretation = ColorInterpretation.UNDEFINED;
            }

            // sample dimension type
            final SampleDimensionType st = TypeMap.getSampleDimensionType(sm, i);

            // set some no data values, as well as Min and Max values
            final double noData;
            double min = -Double.MAX_VALUE, max = Double.MAX_VALUE;
            Double noDataAsProperty = getNoDataProperty(image);
            if (noDataAsProperty != null) {
                noData = noDataAsProperty.doubleValue();
            } else if (backgroundValues != null) {
                // sometimes background values are not specified as 1 per each band, therefore we
                // need to be careful
                noData = backgroundValues[backgroundValues.length > i ? i : 0];
            } else {
                if (st.compareTo(SampleDimensionType.REAL_32BITS) == 0) noData = Float.NaN;
                else if (st.compareTo(SampleDimensionType.REAL_64BITS) == 0) noData = Double.NaN;
                else if (st.compareTo(SampleDimensionType.SIGNED_16BITS) == 0) {
                    noData = Short.MIN_VALUE;
                    min = Short.MIN_VALUE;
                    max = Short.MAX_VALUE;
                } else if (st.compareTo(SampleDimensionType.SIGNED_32BITS) == 0) {
                    noData = Integer.MIN_VALUE;

                    min = Integer.MIN_VALUE;
                    max = Integer.MAX_VALUE;
                } else if (st.compareTo(SampleDimensionType.SIGNED_8BITS) == 0) {
                    noData = -128;
                    min = -128;
                    max = 127;
                } else {
                    // unsigned
                    noData = 0;
                    min = 0;

                    // compute max
                    if (st.compareTo(SampleDimensionType.UNSIGNED_1BIT) == 0) max = 1;
                    else if (st.compareTo(SampleDimensionType.UNSIGNED_2BITS) == 0) max = 3;
                    else if (st.compareTo(SampleDimensionType.UNSIGNED_4BITS) == 0) max = 7;
                    else if (st.compareTo(SampleDimensionType.UNSIGNED_8BITS) == 0) max = 255;
                    else if (st.compareTo(SampleDimensionType.UNSIGNED_16BITS) == 0) max = 65535;
                    else if (st.compareTo(SampleDimensionType.UNSIGNED_32BITS) == 0)
                        max = Math.pow(2, 32) - 1;
                }
            }
            bands[i] =
                    new SimplifiedGridSampleDimension(
                            bandName,
                            st,
                            colorInterpretation,
                            noData,
                            min,
                            max,
                            1, // no scale
                            0, // no offset
                            null);
        }

        // creating the final coverage by keeping into account the fact that we
        Map<String, Object> properties = new HashMap<String, Object>();
        if (granulesPaths != null) {
            properties.put(GridCoverage2DReader.FILE_SOURCE_PROPERTY, granulesPaths);
        }
        if (sourceUrl != null) {
            properties.put(GridCoverage2DReader.SOURCE_URL_PROPERTY, sourceUrl);
        }
        if (mosaicOutput.pamDataset != null) {
            properties.put(Utils.PAM_DATASET, mosaicOutput.pamDataset);
        }
        // Setting NoData as the NoData for the first Band
        ImageWorker w = new ImageWorker(image);
        CoverageUtilities.setNoDataProperty(properties, w.getNoData());
        // Setting ROI property
        Object property = image.getProperty("ROI");
        if (property != null && property instanceof ROI) {
            CoverageUtilities.setROIProperty(properties, (ROI) property);
        }

        return coverageFactory.create(
                rasterManager.getCoverageIdentifier(),
                image,
                new GridGeometry2D(
                        new GridEnvelope2D(PlanarImage.wrapRenderedImage(image).getBounds()),
                        PixelInCell.CELL_CORNER,
                        finalGridToWorldCorner,
                        this.mosaicBBox.getCoordinateReferenceSystem(),
                        hints),
                bands,
                null,
                properties);
    }

    private Double getNoDataProperty(RenderedImage image) {
        if (image != null) {
            Object obj = image.getProperty(NoDataContainer.GC_NODATA);
            if (obj != null) {
                if (obj instanceof NoDataContainer) {
                    return ((NoDataContainer) obj).getAsSingleValue();
                } else if (obj instanceof Double) {
                    return (Double) obj;
                }
            }
        }
        return null;
    }

    public RasterLayerRequest getRequest() {
        return request;
    }

    public FootprintBehavior getFootprintBehavior() {
        return footprintBehavior;
    }

    public ImageReadParam getBaseReadParameters() {
        return baseReadParameters;
    }

    public MathTransform2D getFinalGridToWorldCorner() {
        return finalGridToWorldCorner;
    }

    public MathTransform2D getFinalWorldToGridCorner() {
        return finalWorldToGridCorner;
    }

    public ReferencedEnvelope getMosaicBBox() {
        return mosaicBBox;
    }

    public Color getFinalTransparentColor() {
        return finalTransparentColor;
    }

    public Rectangle getRasterBounds() {
        return rasterBounds;
    }

    public MathTransform getBaseGridToWorld() {
        return baseGridToWorld;
    }

    public int getImageChoice() {
        return imageChoice;
    }

    public void setImageChoice(int imageChoice) {
        this.imageChoice = imageChoice;
    }

    public boolean isMultithreadingAllowed() {
        return multithreadingAllowed;
    }

    public RasterManager getRasterManager() {
        return rasterManager;
    }

    public Hints getHints() {
        return hints;
    }

    public void setGranulesPaths(String granulesPaths) {
        this.granulesPaths = granulesPaths;
    }

    /** See {@link GridCoverage2DReader#SOURCE_URL_PROPERTY}. */
    public void setSourceUrl(URL sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public int getDefaultArtifactsFilterThreshold() {
        return defaultArtifactsFilterThreshold;
    }

    public double getArtifactsFilterPTileThreshold() {
        return artifactsFilterPTileThreshold;
    }

    public double[] getBackgroundValues() {
        return backgroundValues;
    }

    public ROIExcessGranuleRemover getExcessGranuleRemover() {
        return excessGranuleRemover;
    }

    public Geometry getGeometryMask() {
        return geometryMask;
    }

    public double getMaskingBufferPixels() {
        return maskingBufferPixels;
    }

    public boolean isSetRoiProperty() {
        return setRoiProperty;
    }

    public boolean isHeterogeneousCRS() {
        return heterogeneousCRS;
    }

    /** Builds an alternate view of request/response/manager based on a template descriptor */
    public RasterLayerResponse reprojectTo(GranuleDescriptor templateDescriptor) throws Exception {
        // optimization in case the granule CRS and the mosaic CRS correspond
        CoordinateReferenceSystem granuleCRS =
                templateDescriptor.getGranuleEnvelope().getCoordinateReferenceSystem();
        if (CRS.equalsIgnoreMetadata(
                rasterManager.spatialDomainManager.coverageCRS2D, granuleCRS)) {
            return this;
        }

        // rebuild
        RasterLayerRequest originalRequest = this.getRequest();
        RasterManager originalRasterManager = originalRequest.getRasterManager();
        RasterManager manager =
                originalRasterManager.getForGranuleCRS(templateDescriptor, this.mosaicBBox);
        RasterLayerRequest request =
                new RasterLayerRequest(originalRequest.getParams(), manager) {
                    @Override
                    protected ReferencedEnvelope computeCoverageBoundingBox(
                            RasterManager rasterManager) throws IOException {
                        // in case of filtering we are re-computing the bbox from the data, it gets
                        // back in the mosaic CRS instead of the desired one. Force it to use the
                        // whole
                        // thing, we already used the filter
                        // TODO: add projection handler support
                        if (filter != null && !Filter.INCLUDE.equals(filter)) {
                            // limit it to the filtered granules bounding box by full enumeration,
                            // to avoid
                            // imprecise datastore optimizations (e.g., loose bounds)
                            GranuleSource granules = rasterManager.getGranuleSource(true, null);
                            String crsAttribute = manager.getCrsAttribute();
                            String granuleCRSCode =
                                    (String)
                                            templateDescriptor
                                                    .getOriginator()
                                                    .getAttribute(crsAttribute);
                            FilterFactory2 ff = FeatureUtilities.DEFAULT_FILTER_FACTORY;
                            PropertyIsEqualTo crsFilter =
                                    ff.equal(
                                            ff.property(crsAttribute),
                                            ff.literal(granuleCRSCode),
                                            false);
                            Filter composite = ff.and(crsFilter, filter);

                            Query query = new Query(granules.getSchema().getTypeName(), composite);
                            // ... load only the default geometry if possible
                            final GeometryDescriptor gd =
                                    granules.getSchema().getGeometryDescriptor();
                            if (gd != null) {
                                query.setPropertyNames(new String[] {gd.getLocalName()});
                            }
                            SimpleFeatureCollection features = granules.getGranules(query);
                            ReferencedEnvelope envelope = DataUtilities.bounds(features);
                            if (envelope != null && !envelope.isEmpty()) {
                                try {
                                    return envelope.transform(granuleCRS, true);
                                } catch (TransformException | FactoryException e) {
                                    LOGGER.log(
                                            Level.FINE,
                                            "Could not transform filtered envelope into target granule CRS, falling back on mosaic");
                                }
                            }
                        }

                        // fallback
                        return rasterManager.spatialDomainManager.coverageBBox;
                    }
                };
        // if the output needs to have transparent footprint behavior, we need to preserve the
        // various sub-mosaic ROIs until the final mosaicking
        if (request.getFootprintBehavior() == FootprintBehavior.Transparent) {
            request.setFootprintBehavior(FootprintBehavior.Cut);
        }
        if (request.spatialRequestHelper.isEmpty()) {
            return null;
        }
        RasterLayerResponse response =
                new RasterLayerResponse(request, manager, this.submosaicProducerFactory);
        // initialize enough info without actually running the output computation
        response.chooseOverview();
        response.initBBOX();
        response.initTransformations();
        response.initRasterBounds();

        return response;
    }
}
