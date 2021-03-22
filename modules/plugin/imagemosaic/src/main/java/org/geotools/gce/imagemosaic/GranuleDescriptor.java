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

import it.geosolutions.imageio.core.CoreCommonImageMetadata;
import it.geosolutions.imageio.core.InitializingReader;
import it.geosolutions.imageio.core.SourceSPIProvider;
import it.geosolutions.imageio.imageioimpl.EnhancedImageReadParam;
import it.geosolutions.imageio.pam.PAMDataset;
import it.geosolutions.imageio.pam.PAMParser;
import it.geosolutions.imageio.utilities.ImageIOUtilities;
import it.geosolutions.jaiext.range.NoDataContainer;
import it.geosolutions.jaiext.vectorbin.ROIGeometry;
import it.geosolutions.jaiext.vectorbin.VectorBinarizeDescriptor;
import it.geosolutions.jaiext.vectorbin.VectorBinarizeRIF;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Rectangle2D;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.spi.ImageInputStreamSpi;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.stream.ImageInputStream;
import javax.media.jai.BorderExtender;
import javax.media.jai.ImageLayout;
import javax.media.jai.Interpolation;
import javax.media.jai.InterpolationNearest;
import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.ROI;
import javax.media.jai.ROIShape;
import javax.media.jai.RenderedOp;
import javax.media.jai.TileCache;
import javax.media.jai.TileScheduler;
import org.apache.commons.beanutils.MethodUtils;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.footprint.FootprintBehavior;
import org.geotools.coverage.grid.io.footprint.MultiLevelROI;
import org.geotools.coverage.grid.io.imageio.MaskOverviewProvider;
import org.geotools.coverage.grid.io.imageio.ReadType;
import org.geotools.coverage.util.CoverageUtilities;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.geometry.util.XRectangle2D;
import org.geotools.image.ImageWorker;
import org.geotools.image.io.ImageIOExt;
import org.geotools.image.jai.Registry;
import org.geotools.image.util.ImageUtilities;
import org.geotools.metadata.i18n.ErrorKeys;
import org.geotools.metadata.i18n.Errors;
import org.geotools.referencing.CRS;
import org.geotools.referencing.operation.builder.GridToEnvelopeMapper;
import org.geotools.referencing.operation.matrix.XAffineTransform;
import org.geotools.referencing.operation.transform.AffineTransform2D;
import org.geotools.util.URLs;
import org.geotools.util.factory.Hints;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.geometry.BoundingBox;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.datum.PixelInCell;
import org.opengis.referencing.operation.MathTransform2D;
import org.opengis.referencing.operation.TransformException;

/**
 * A granuleDescriptor is a single piece of the mosaic, with its own overviews and everything.
 *
 * <p>This class is responsible for caching the various size of the different levels of each single
 * granuleDescriptor since computing them each time is expensive (opening a file, looking for a
 * reader, parsing metadata,etc...).
 *
 * <p>Right now we are making the assumption that a single granuleDescriptor is made a by a single
 * file with embedded overviews, either explicit or intrinsic through wavelets like MrSID, ECW or
 * JPEG2000.
 *
 * @author Simone Giannecchini, GeoSolutions S.A.S.
 * @author Stefan Alfons Krueger (alfonx), Wikisquare.de : Support for
 *     jar:file:foo.jar/bar.properties URLs
 * @since 2.5.5
 */
public class GranuleDescriptor {

    static class PathResolver {
        PathType pathType;
        String parentLocation;

        public PathResolver(PathType pathType, String parentLocation) {
            this.pathType = pathType;
            this.parentLocation = parentLocation;
        }

        public URL resolve(String granuleLocation) {
            return pathType.resolvePath(parentLocation, granuleLocation);
        }
    }

    /** Logger. */
    private static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(GranuleDescriptor.class);

    private static final String AUXFILE_EXT = ".aux.xml";

    /** Hints to use for avoiding to search for the ImageMosaic format */
    public static final Hints EXCLUDE_MOSAIC = new Hints(Utils.EXCLUDE_MOSAIC, true);

    /**
     * Minimum portion of a single pixel the code is going to read before giving up on the read,
     * this is used to avoid reading granules that touch the reading area without actually
     * contributing anything to the output
     */
    public static final double READ_THRESHOLD =
            Double.parseDouble(System.getProperty("org.geotools.mosaic.read.threshold", "0.001"));

    static {
        try {
            Registry.registerRIF(
                    JAI.getDefaultInstance(),
                    new VectorBinarizeDescriptor(),
                    new VectorBinarizeRIF(),
                    Registry.JAI_TOOLS_PRODUCT);
        } catch (Exception e) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, e.getLocalizedMessage());
            }
        }
    }

    OverviewsController overviewsController;

    private GeneralEnvelope granuleEnvelope;
    private AbstractGridFormat format;
    private Hints hints;

    public GeneralEnvelope getGranuleEnvelope() {
        return granuleEnvelope;
    }

    public void setGranuleEnvelope(GeneralEnvelope granuleEnvelope) {
        this.granuleEnvelope = granuleEnvelope;
    }

    /**
     * This class represent an overview level in a single granuleDescriptor.
     *
     * <p>Notice that the internal transformations for the various levels are reffered to the
     * corner, rather than to the centre.
     *
     * @author Simone Giannecchini, GeoSolutions S.A.S.
     */
    class GranuleOverviewLevelDescriptor {

        final double scaleX;

        final double scaleY;

        final int width;

        final int height;

        final AffineTransform2D baseToLevelTransform;

        final AffineTransform2D gridToWorldTransformCorner;

        final Rectangle rasterDimensions;

        public AffineTransform getBaseToLevelTransform() {
            return baseToLevelTransform;
        }

        public double getScaleX() {
            return scaleX;
        }

        public double getScaleY() {
            return scaleY;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        public GranuleOverviewLevelDescriptor(
                final double scaleX, final double scaleY, final int width, final int height) {
            this.scaleX = scaleX;
            this.scaleY = scaleY;
            this.baseToLevelTransform =
                    new AffineTransform2D(XAffineTransform.getScaleInstance(scaleX, scaleY, 0, 0));

            final AffineTransform gridToWorldTransform_ = new AffineTransform(baseToLevelTransform);
            gridToWorldTransform_.preConcatenate(CoverageUtilities.CENTER_TO_CORNER);
            gridToWorldTransform_.preConcatenate(baseGridToWorld);
            this.gridToWorldTransformCorner = new AffineTransform2D(gridToWorldTransform_);
            this.width = width;
            this.height = height;
            this.rasterDimensions = new Rectangle(0, 0, width, height);
        }

        public Rectangle getBounds() {
            return (Rectangle) rasterDimensions.clone();
        }

        public AffineTransform2D getGridToWorldTransform() {
            return gridToWorldTransformCorner;
        }

        @Override
        public String toString() {
            // build a decent representation for this level
            final StringBuilder buffer = new StringBuilder();
            buffer.append("Description of a granuleDescriptor level")
                    .append("\n")
                    .append("width:\t\t")
                    .append(width)
                    .append("\n")
                    .append("height:\t\t")
                    .append(height)
                    .append("\n")
                    .append("scaleX:\t\t")
                    .append(scaleX)
                    .append("\n")
                    .append("scaleY:\t\t")
                    .append(scaleY)
                    .append("\n")
                    .append("baseToLevelTransform:\t\t")
                    .append(baseToLevelTransform.toString())
                    .append("\n")
                    .append("gridToWorldTransform:\t\t")
                    .append(gridToWorldTransformCorner.toString())
                    .append("\n");
            return buffer.toString();
        }
    }

    /**
     * Simple placeholder class to store the result of a Granule Loading which comprises of a raster
     * as well as a {@link ROIShape} for its footprint.
     *
     * @author Daniele Romagnoli, GeoSolutions S.A.S.
     */
    public static class GranuleLoadingResult {

        RenderedImage loadedImage;

        ROI footprint;

        URL granuleUrl;

        boolean doFiltering;

        PAMDataset pamDataset;

        GranuleDescriptor granuleDescriptor;

        public ROI getFootprint() {
            return footprint;
        }

        public RenderedImage getRaster() {
            return loadedImage;
        }

        public URL getGranuleUrl() {
            return granuleUrl;
        }

        public PAMDataset getPamDataset() {
            return pamDataset;
        }

        public void setPamDataset(PAMDataset pamDataset) {
            this.pamDataset = pamDataset;
        }

        public boolean isDoFiltering() {
            return doFiltering;
        }

        public GranuleDescriptor getGranuleDescriptor() {
            return granuleDescriptor;
        }

        GranuleLoadingResult(
                RenderedImage loadedImage,
                ROI footprint,
                URL granuleUrl,
                final boolean doFiltering,
                final PAMDataset pamDataset,
                GranuleDescriptor granuleDescriptor) {
            this.loadedImage = loadedImage;
            Object roi = loadedImage.getProperty("ROI");
            if (roi instanceof ROI) {
                this.footprint = (ROI) roi;
            }
            this.granuleUrl = granuleUrl;
            this.doFiltering = doFiltering;
            this.pamDataset = pamDataset;
            this.granuleDescriptor = granuleDescriptor;
        }
    }

    private static PAMParser pamParser = PAMParser.getInstance();

    ReferencedEnvelope granuleBBOX;

    MultiLevelROI roiProvider;

    URL granuleUrl;

    int maxDecimationFactor = -1;

    final Map<Integer, GranuleOverviewLevelDescriptor> granuleLevels =
            Collections.synchronizedMap(new HashMap<>());

    AffineTransform baseGridToWorld;

    ImageReaderSpi cachedReaderSPI;

    private SimpleFeature originator;

    PAMDataset pamDataset;

    boolean handleArtifactsFiltering = false;

    boolean filterMe = false;

    ImageInputStreamSpi cachedStreamSPI;

    /** {@link MaskOverviewProvider} used for handling external ROIs and Overviews */
    private MaskOverviewProvider ovrProvider;

    private GranuleAccessProvider granuleAccessProvider;

    private NoDataContainer noData;

    private Double[] scales;
    private Double[] offsets;

    @SuppressWarnings("PMD.UseTryWithResources") // ImageInputStream initialized in multiple places
    protected void init(
            final BoundingBox granuleBBOX,
            final URL granuleUrl,
            final AbstractGridFormat suggestedFormat,
            ImageReaderSpi suggestedSPI,
            ImageInputStreamSpi suggestedIsSPI,
            final MultiLevelROI roiProvider,
            final boolean heterogeneousGranules,
            final boolean handleArtifactsFiltering,
            final Hints hints) {
        this.granuleBBOX = ReferencedEnvelope.reference(granuleBBOX);
        this.granuleUrl = granuleUrl;
        this.roiProvider = roiProvider;
        this.handleArtifactsFiltering = handleArtifactsFiltering;
        this.hints = new Hints(hints);
        filterMe = handleArtifactsFiltering && roiProvider != null;

        Object input = granuleUrl;
        AbstractGridCoverage2DReader gcReader = null;
        ImageInputStream inStream = null;
        ImageReader imageReader = null;

        // Check for an externally specified provider
        try {
            Object providerHint =
                    Utils.getHintIfAvailable(hints, GranuleAccessProvider.GRANULE_ACCESS_PROVIDER);
            if (providerHint != null) {
                granuleAccessProvider = ((GranuleAccessProvider) providerHint).copyProviders();
                granuleAccessProvider.setGranuleInput(granuleUrl);
            } else {
                granuleAccessProvider =
                        getDefaultProvider(
                                input, suggestedFormat, suggestedSPI, suggestedIsSPI, hints);
            }

            this.format = granuleAccessProvider.getFormat();
            gcReader = granuleAccessProvider.getGridCoverageReader();
            ovrProvider = granuleAccessProvider.getMaskOverviewsProvider();

            if (heterogeneousGranules) {
                // do not trust the index, use the reader instead (reprojection might be involved)
                this.granuleBBOX = ReferencedEnvelope.reference(gcReader.getOriginalEnvelope());
            }

            if (granuleAccessProvider instanceof GranuleDescriptorModifier) {
                ((GranuleDescriptorModifier) granuleAccessProvider).update(this, hints);
            }

            // get a stream
            if (cachedStreamSPI == null) {
                cachedStreamSPI = granuleAccessProvider.getInputStreamSpi();
            }
            assert cachedStreamSPI != null : "no cachedStreamSPI available!";
            if (inStream == null) {
                inStream = granuleAccessProvider.getImageInputStream();
            }

            // get a reader and try to cache the suggested SPI first
            if (cachedReaderSPI == null) {
                cachedReaderSPI = granuleAccessProvider.getImageReaderSpi();
            }
            if (cachedReaderSPI == null) {
                throw new IllegalArgumentException(
                        "Unable to get a ReaderSPI for the provided input: "
                                + granuleUrl.toString());
            }
            imageReader = granuleAccessProvider.getImageReader();
            boolean ignoreMetadata = false;
            if (imageReader instanceof InitializingReader) {
                ignoreMetadata = ((InitializingReader) imageReader).init(hints);
            }
            imageReader.setInput(inStream, false, ignoreMetadata);
            // get selected level and base level dimensions
            final Rectangle originalDimension = Utils.getDimension(0, imageReader);

            // build the g2W for this tile, in principle we should get it
            // somehow from the tile itself or from the index, but at the moment
            // we do not have such info, hence we assume that it is a simple
            // scale and translate
            GridToEnvelopeMapper geMapper =
                    new GridToEnvelopeMapper(
                            new GridEnvelope2D(originalDimension), this.granuleBBOX);
            geMapper.setPixelAnchor(PixelInCell.CELL_CENTER); // this is the default behavior but it
            // is nice to write
            // it down anyway
            this.baseGridToWorld = geMapper.createAffineTransform();

            // add the base level
            this.granuleLevels.put(
                    Integer.valueOf(0),
                    new GranuleOverviewLevelDescriptor(
                            1, 1, originalDimension.width, originalDimension.height));

            ////////////////////// Setting overviewController ///////////////////////
            if (heterogeneousGranules) {
                // //
                //
                // Right now we are setting up overviewsController by assuming that
                // overviews are internal images as happens in TIFF images
                // We can improve this by leveraging on coverageReaders
                //
                // //

                // Getting the first level descriptor
                final GranuleOverviewLevelDescriptor baseOverviewLevelDescriptor =
                        granuleLevels.get(0);

                // Variables initialization
                final int numberOfOvervies = ovrProvider.getNumOverviews();
                final AffineTransform2D baseG2W =
                        baseOverviewLevelDescriptor.getGridToWorldTransform();
                final int width = baseOverviewLevelDescriptor.getWidth();
                final int height = baseOverviewLevelDescriptor.getHeight();
                final double resX = AffineTransform2D.getScaleX0(baseG2W);
                final double resY = AffineTransform2D.getScaleY0(baseG2W);
                final double[] highestRes = {resX, resY};

                // Populating overviews and initializing overviewsController
                final double[][] overviewsResolution =
                        ovrProvider.getOverviewResolutions(
                                highestRes[0] * width, highestRes[1] * height);
                overviewsController =
                        new OverviewsController(highestRes, numberOfOvervies, overviewsResolution);
            }
            //////////////////////////////////////////////////////////////////////////

            if (hints != null && hints.containsKey(Utils.CHECK_AUXILIARY_METADATA)) {
                boolean checkAuxiliaryMetadata =
                        (Boolean) hints.get(Utils.CHECK_AUXILIARY_METADATA);
                if (checkAuxiliaryMetadata) {
                    checkPamDataset();
                }
            }

            // handle the nodata and rescaling if available
            initFromImageMetadata(imageReader);
        } catch (IllegalStateException | IOException e) {
            throw new IllegalArgumentException(e);

        } finally {
            // close/dispose stream and readers
            try {
                if (inStream != null) {
                    inStream.close();
                }
            } catch (Throwable e) {
                throw new IllegalArgumentException(e);
            } finally {
                if (imageReader != null) {
                    imageReader.dispose();
                }
            }
            if (gcReader != null) {
                try {
                    gcReader.dispose();
                } catch (Throwable t) {
                    // Ignore it
                }
            }
        }
    }

    private GranuleAccessProvider getDefaultProvider(
            Object input,
            AbstractGridFormat suggestedFormat,
            ImageReaderSpi suggestedSPI,
            ImageInputStreamSpi suggestedIsSPI,
            Hints hints)
            throws IOException {
        Hints suggestedObjectHints = new Hints(hints);
        if (suggestedFormat != null) {
            suggestedObjectHints.put(GranuleAccessProvider.SUGGESTED_FORMAT, suggestedFormat);
        }
        if (suggestedSPI != null) {
            suggestedObjectHints.put(GranuleAccessProvider.SUGGESTED_READER_SPI, suggestedSPI);
        }
        if (suggestedIsSPI != null) {
            suggestedObjectHints.put(GranuleAccessProvider.SUGGESTED_STREAM_SPI, suggestedIsSPI);
        }
        // When looking for formats which may parse this file, make sure to exclude the
        // ImageMosaicFormat as return
        suggestedObjectHints.add(EXCLUDE_MOSAIC);
        DefaultGranuleAccessProvider provider =
                new DefaultGranuleAccessProvider(suggestedObjectHints);
        provider.setGranuleInput(input);
        return provider;
    }

    private void initFromImageMetadata(ImageReader reader) throws IOException {
        // grabbing the nodata if possible
        int index = 0;
        if (originator != null) {
            Object imageIndex = originator.getAttribute("imageindex");
            if (imageIndex instanceof Integer) {
                index = (Integer) imageIndex;
            }
        }
        try {
            IIOMetadata metadata = reader.getImageMetadata(index);
            if (metadata instanceof CoreCommonImageMetadata) {
                CoreCommonImageMetadata ccm = (CoreCommonImageMetadata) metadata;

                double[] noData = ccm.getNoData();
                if (noData != null) {
                    this.noData = new NoDataContainer(noData);
                }

                this.scales = ccm.getScales();
                this.offsets = ccm.getOffsets();
            }
        } catch (UnsupportedOperationException e) {
            // some imageio-ext plugin throw this because they do not support getting the metadata
            // instead of returning null
            if (LOGGER.isLoggable(Level.FINER)) {
                LOGGER.log(
                        Level.FINER,
                        "Failed to gather metadata, might not be fatal, some readers do not support it",
                        e);
            }
        }
    }

    public OverviewsController getOverviewsController() {
        return overviewsController;
    }

    /** Look for GDAL Auxiliary File and unmarshall it to setup a PamDataset if available */
    private void checkPamDataset() throws IOException {
        final File file = URLs.urlToFile(granuleUrl);
        final String path = file.getCanonicalPath();
        final String auxFile = path + AUXFILE_EXT;
        pamDataset = pamParser.parsePAM(auxFile);
    }

    public GranuleDescriptor(
            final String granuleLocation,
            final BoundingBox granuleBBox,
            final AbstractGridFormat suggestedFormat,
            final ImageReaderSpi suggestedSPI,
            final ImageInputStreamSpi suggestedIsSPI,
            final MultiLevelROI roiProvider) {
        this(
                granuleLocation,
                granuleBBox,
                suggestedFormat,
                suggestedSPI,
                suggestedIsSPI,
                roiProvider,
                -1,
                false);
    }

    public GranuleDescriptor(
            final String granuleLocation,
            final BoundingBox granuleBBox,
            final AbstractGridFormat suggestedFormat,
            final ImageReaderSpi suggestedSPI,
            final ImageInputStreamSpi suggestedIsSPI,
            final MultiLevelROI roiProvider,
            final boolean heterogeneousGranules) {
        this(
                granuleLocation,
                granuleBBox,
                suggestedFormat,
                suggestedSPI,
                suggestedIsSPI,
                roiProvider,
                -1,
                heterogeneousGranules);
    }

    public GranuleDescriptor(
            final String granuleLocation,
            final BoundingBox granuleBBox,
            final AbstractGridFormat suggestedFormat,
            final ImageReaderSpi suggestedSPI,
            final ImageInputStreamSpi suggestedIsSPI,
            final MultiLevelROI roiProvider,
            final int maxDecimationFactor) {
        this(
                granuleLocation,
                granuleBBox,
                suggestedFormat,
                suggestedSPI,
                suggestedIsSPI,
                roiProvider,
                maxDecimationFactor,
                false);
    }

    public GranuleDescriptor(
            final String granuleLocation,
            final BoundingBox granuleBBox,
            final AbstractGridFormat suggestedFormat,
            final ImageReaderSpi suggestedSPI,
            final ImageInputStreamSpi suggestedIsSPI,
            final MultiLevelROI roiProvider,
            final int maxDecimationFactor,
            final boolean heterogeneousGranules) {
        this(
                granuleLocation,
                granuleBBox,
                suggestedFormat,
                suggestedSPI,
                suggestedIsSPI,
                roiProvider,
                maxDecimationFactor,
                heterogeneousGranules,
                false);
    }

    public GranuleDescriptor(
            final String granuleLocation,
            final BoundingBox granuleBBox,
            final AbstractGridFormat suggestedFormat,
            final ImageReaderSpi suggestedSPI,
            final ImageInputStreamSpi suggestedIsSPI,
            final MultiLevelROI roiProvider,
            final int maxDecimationFactor,
            final boolean heterogeneousGranules,
            final boolean handleArtifactsFiltering) {

        this(
                granuleLocation,
                granuleBBox,
                suggestedFormat,
                suggestedSPI,
                suggestedIsSPI,
                roiProvider,
                maxDecimationFactor,
                heterogeneousGranules,
                handleArtifactsFiltering,
                null);
    }

    public GranuleDescriptor(
            final String granuleLocation,
            final BoundingBox granuleBBox,
            final AbstractGridFormat suggestedFormat,
            final ImageReaderSpi suggestedSPI,
            final ImageInputStreamSpi suggestedIsSPI,
            final MultiLevelROI roiProvider,
            final int maxDecimationFactor,
            final boolean heterogeneousGranules,
            final boolean handleArtifactsFiltering,
            final Hints hints) {
        this.maxDecimationFactor = maxDecimationFactor;
        URL rasterGranule = extractRasterGranule(hints, granuleLocation, null, false);
        if (rasterGranule == null) {
            return;
        }

        if (LOGGER.isLoggable(Level.FINER)) {
            LOGGER.finer("File found " + granuleLocation);
        }

        this.originator = null;
        init(
                granuleBBox,
                rasterGranule,
                suggestedFormat,
                suggestedSPI,
                suggestedIsSPI,
                roiProvider,
                heterogeneousGranules,
                handleArtifactsFiltering,
                hints);
    }

    /** */
    public GranuleDescriptor(
            final SimpleFeature feature,
            final ImageReaderSpi suggestedSPI,
            final AbstractGridFormat suggestedFormat,
            final ImageInputStreamSpi suggestedIsSPI,
            final PathType pathType,
            final String locationAttribute,
            final String parentLocation) {
        this(
                feature,
                suggestedFormat,
                suggestedSPI,
                suggestedIsSPI,
                pathType,
                locationAttribute,
                parentLocation,
                false);
    }

    public GranuleDescriptor(
            SimpleFeature feature,
            AbstractGridFormat suggestedFormat,
            ImageReaderSpi suggestedSPI,
            final ImageInputStreamSpi suggestedIsSPI,
            PathType pathType,
            String locationAttribute,
            String parentLocation,
            boolean heterogeneousGranules,
            Hints hints) {
        this(
                feature,
                suggestedFormat,
                suggestedSPI,
                suggestedIsSPI,
                pathType,
                locationAttribute,
                parentLocation,
                null,
                heterogeneousGranules,
                hints);
    }

    public GranuleDescriptor(
            SimpleFeature feature,
            AbstractGridFormat suggestedFormat,
            ImageReaderSpi suggestedSPI,
            final ImageInputStreamSpi suggestedIsSPI,
            PathType pathType,
            String locationAttribute,
            String parentLocation,
            boolean heterogeneousGranules) {
        this(
                feature,
                suggestedFormat,
                suggestedSPI,
                suggestedIsSPI,
                pathType,
                locationAttribute,
                parentLocation,
                heterogeneousGranules,
                null);
    }

    /**
     * Constructor for the {@link GranuleDescriptor} assuming it doesn't belong to an heterogeneous
     * granules set.
     *
     * @param feature a {@link SimpleFeature} referring to that granule
     * @param suggestedSPI the suggested {@link ImageReaderSpi} to be used to get a reader to handle
     *     this granule.
     * @param pathType A {@link PathType} identifying if the granule location should be resolved as
     *     a relative or an absolute path.
     * @param locationAttribute the attribute containing the granule location.
     * @param parentLocation the location of the parent of that granule.
     */
    public GranuleDescriptor(
            SimpleFeature feature,
            ImageReaderSpi suggestedSPI,
            PathType pathType,
            final AbstractGridFormat suggestedFormat,
            final ImageInputStreamSpi suggestedIsSPI,
            final String locationAttribute,
            final String parentLocation,
            final MultiLevelROI roiProvider) {
        this(
                feature,
                suggestedFormat,
                suggestedSPI,
                suggestedIsSPI,
                pathType,
                locationAttribute,
                parentLocation,
                roiProvider,
                false,
                null);
    }

    /**
     * Constructor for the {@link GranuleDescriptor}
     *
     * @param feature a {@link SimpleFeature} referring to that granule
     * @param suggestedSPI the suggested {@link ImageReaderSpi} to be used to get a reader to handle
     *     this granule.
     * @param pathType A {@link PathType} identifying if the granule location should be resolved as
     *     a relative or an absolute path.
     * @param locationAttribute the attribute containing the granule location.
     * @param parentLocation the location of the parent of that granule.
     * @param heterogeneousGranules if {@code true}, this granule belongs to a set of heterogeneous
     *     granules
     */
    public GranuleDescriptor(
            final SimpleFeature feature,
            final AbstractGridFormat suggestedFormat,
            final ImageReaderSpi suggestedSPI,
            final ImageInputStreamSpi suggestedIsSPI,
            final PathType pathType,
            final String locationAttribute,
            final String parentLocation,
            final MultiLevelROI roiProvider,
            final boolean heterogeneousGranules,
            final Hints hints) {
        // Get location and envelope of the image to load.
        final String granuleLocation = (String) feature.getAttribute(locationAttribute);
        final ReferencedEnvelope granuleBBox = getFeatureBounds(feature);

        PathResolver pathResolver = new PathResolver(pathType, parentLocation);
        URL rasterGranule = extractRasterGranule(hints, granuleLocation, pathResolver, true);

        if (LOGGER.isLoggable(Level.FINE))
            LOGGER.fine("Proceeding with granule: " + granuleLocation);

        this.originator = feature;
        init(
                granuleBBox,
                rasterGranule,
                suggestedFormat,
                suggestedSPI,
                suggestedIsSPI,
                roiProvider,
                heterogeneousGranules,
                false,
                hints);
    }

    private URL extractRasterGranule(
            Hints hints,
            String granuleLocation,
            PathResolver pathResolver,
            boolean exceptionOnNullGranule) {
        boolean hasCustomGranuleProvider =
                hints != null && hints.containsKey(GranuleAccessProvider.GRANULE_ACCESS_PROVIDER);
        URL rasterGranule = null;
        if (!hasCustomGranuleProvider) {
            // If the granuleDescriptor is not there, dump a message and continue
            if (pathResolver != null) {
                rasterGranule = pathResolver.resolve(granuleLocation);
            } else {
                rasterGranule = URLs.fileToUrl(new File(granuleLocation));
            }

        } else {
            try {
                rasterGranule = new URL(granuleLocation);
            } catch (MalformedURLException e) {
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.warning(
                            "Unable to set an URL from the provided granuleLocation: "
                                    + granuleLocation);
                }
                rasterGranule = null;
            }
        }
        if (rasterGranule == null && exceptionOnNullGranule) {
            throw new IllegalArgumentException(
                    Errors.format(
                            ErrorKeys.ILLEGAL_ARGUMENT_$2, "granuleLocation", granuleLocation));
        }
        return rasterGranule;
    }

    /**
     * Extracts the referenced envelope of the default geometry (used to be feature.getBounds, but
     * that method returns the bounds of all geometries in the feature)
     */
    private ReferencedEnvelope getFeatureBounds(final SimpleFeature feature) {
        Geometry g = (Geometry) feature.getDefaultGeometry();
        if (g == null) {
            return null;
        }
        CoordinateReferenceSystem crs = feature.getFeatureType().getCoordinateReferenceSystem();
        ReferencedEnvelope granuleBBox = new ReferencedEnvelope(g.getEnvelopeInternal(), crs);
        return granuleBBox;
    }

    private static class RequestParams {
        final ImageReadParam imageReadParameters;
        final int index;
        final ReferencedEnvelope cropBBox;
        final MathTransform2D mosaicWorldToGrid;
        final RasterLayerRequest request;
        final Hints hints;
        final Geometry inclusionGeometry;
        final ReferencedEnvelope intersection;

        public RequestParams(
                ImageReadParam imageReadParameters,
                int index,
                ReferencedEnvelope cropBBox,
                MathTransform2D mosaicWorldToGrid,
                RasterLayerRequest request,
                Hints hints,
                Geometry inclusionGeometry,
                ReferencedEnvelope intersection) {
            this.imageReadParameters = imageReadParameters;
            this.index = index;
            this.cropBBox = cropBBox;
            this.mosaicWorldToGrid = mosaicWorldToGrid;
            this.request = request;
            this.hints = hints;
            this.inclusionGeometry = inclusionGeometry;
            this.intersection = intersection;
        }
    }

    private static class ReaderState {
        boolean proceed;
        boolean cleanup;

        ImageInputStream inStream;
        ImageReader reader;
        int imageIndex;
        ImageReadParam readParameters;
        URL granuleURL;
        GranuleOverviewLevelDescriptor selectedlevel;
        Rectangle sourceArea;
        int ovrIndex;

        public void dispose() throws IOException {
            if (cleanup) {
                try {
                    if (inStream != null) {
                        inStream.close();
                    }
                } finally {
                    if (reader != null) {
                        reader.dispose();
                    }
                }
            }
        }
    }

    private static class RasterState {
        boolean proceed;
        RenderedImage raster;
        AffineTransform finalRaster2Model;
    }

    /**
     * Load a specified a raster as a portion of the granule describe by this {@link
     * GranuleDescriptor}.
     *
     * @param imageReadParameters the {@link ImageReadParam} to use for reading.
     * @param index the index to use for the {@link ImageReader}.
     * @param cropBBox the bbox to use for cropping.
     * @param mosaicWorldToGrid the cropping grid to world transform.
     * @param request the incoming request to satisfy.
     * @param hints {@link Hints} to be used for creating this raster.
     * @return a specified a raster as a portion of the granule describe by this {@link
     *     GranuleDescriptor}.
     * @throws IOException in case an error occurs.
     */
    public GranuleLoadingResult loadRaster(
            final ImageReadParam imageReadParameters,
            final int index,
            final ReferencedEnvelope cropBBox,
            final MathTransform2D mosaicWorldToGrid,
            final RasterLayerRequest request,
            final Hints hints)
            throws IOException {

        final RequestParams requestParams =
                prepareRequest(
                        imageReadParameters, index, cropBBox, mosaicWorldToGrid, request, hints);

        if (!boundsOverlap(requestParams)) {
            return null;
        }

        GranuleLoadingResult result = null;

        // eventually gets closed in finally block, if possible (not deferred loading)
        final ReaderState readerState = prepareReader(requestParams);
        try {
            if (readerState.proceed) {
                final RasterState rasterState = loadAndPrepareRaster(requestParams, readerState);
                if (rasterState.proceed) {
                    result = createGranuleLoadingResult(requestParams, readerState, rasterState);
                }
            }
        } catch (RuntimeException e) {
            warnLoadRaster(e, request);
        } finally {
            readerState.dispose();
        }
        return result;
    }

    private GranuleLoadingResult createGranuleLoadingResult(
            final RequestParams requestParams,
            ReaderState readerState,
            final RasterState rasterState) {

        final URL granuleURL = readerState.granuleURL;

        final boolean doFiltering = shallDoFiltering(requestParams.inclusionGeometry);

        // apply the affine transform conserving indexed color model
        final AffineTransform finalRaster2Model = rasterState.finalRaster2Model;
        if (finalRaster2ModelIsIdentity(finalRaster2Model)) {
            RenderedImage raster = rasterState.raster;
            return createIdentityLoadingResult(raster, granuleURL, doFiltering);
        }

        RenderedImage finalRenderedImage = buildRenderedImage(requestParams, rasterState);
        if (finalRenderedImage == null) {
            return null;
        }

        return new GranuleLoadingResult(
                finalRenderedImage, null, granuleURL, doFiltering, pamDataset, this);
    }

    private RequestParams prepareRequest(
            ImageReadParam imageReadParameters,
            int index,
            ReferencedEnvelope cropBBox,
            MathTransform2D mosaicWorldToGrid,
            RasterLayerRequest request,
            Hints hints) {

        if (LOGGER.isLoggable(Level.FINER))
            LOGGER.finer(
                    String.format(
                            "Thread: %s Loading raster data for granuleDescriptor %s",
                            Thread.currentThread().getName(), this));

        final Geometry inclusionGeometry = resolveInclusionGeometry(request);

        // intersection of this tile bound with the current crop bbox
        final ReferencedEnvelope intersection = cropBBoxIntersection(inclusionGeometry, cropBBox);

        return new RequestParams(
                imageReadParameters,
                index,
                cropBBox,
                mosaicWorldToGrid,
                request,
                hints,
                inclusionGeometry,
                intersection);
    }

    private void warnLoadRaster(final Exception e, final RasterLayerRequest request) {
        final String msg =
                "Unable to load raster for granuleDescriptor %s with request %s Resulting in no granule loaded: Empty result";
        LOGGER.log(Level.WARNING, e, () -> String.format(msg, this, request));
    }

    private void warnLoadReader(final Exception e, final RasterLayerRequest request) {
        if (LOGGER.isLoggable(Level.WARNING)) {
            final String msg =
                    "Unable to get reader for granuleDescriptor %s with request %s Resulting in no granule loaded: Empty result";
            if (e == null) {
                LOGGER.log(Level.WARNING, () -> String.format(msg, this, request));
            } else {
                LOGGER.log(Level.WARNING, e, () -> String.format(msg, this, request));
            }
        }
    }

    private ReaderState prepareReader(final RequestParams params) throws IOException {

        final RasterLayerRequest request = params.request;

        ReaderState state = new ReaderState();
        state.proceed = false;
        state.cleanup = request.getReadType() != ReadType.JAI_IMAGEREAD;
        state.imageIndex = params.index;
        state.readParameters = params.imageReadParameters;
        // Define a new URL to use (it may change if using external overviews)
        state.granuleURL = granuleUrl;

        // Checking for heterogeneous granules and if the mosaic is not multidimensional
        if (request.isHeterogeneousGranules() && !mosaicIsMultidimensional()) {
            state.readParameters = new ImageReadParam();
            try {
                state.imageIndex = overrideOverviewsForBaseLayer(request, state.readParameters);
            } catch (RuntimeException | TransformException e) {
                warnLoadReader(e, request);
                return state;
            }
        }

        final boolean isExternal = ovrProvider.isExternalOverview(state.imageIndex);
        if (isExternal) {
            prepareExternalReader(request, state);
        } else {
            prepareReader(request, state);
        }
        if (state.inStream == null || state.reader == null) {
            warnLoadReader(null, request);
            return state;
        }
        // set input
        setReaderInput(state.reader, state.inStream, hints);

        // check if the reader wants to be aware of the current request
        setRasterLayerRequest(state.reader, request);

        // Defining an Overview Index value
        state.ovrIndex = ovrProvider.getOverviewIndex(state.imageIndex);

        // get selected level and base level dimensions
        state.selectedlevel = getLevel(state.ovrIndex, state.reader, state.imageIndex);
        try {
            state.sourceArea =
                    calcSourceArea(
                            request,
                            params.intersection,
                            state.imageIndex,
                            state.readParameters,
                            state.selectedlevel);
        } catch (TransformException e) {
            warnLoadRaster(e, request);
            return state;
        }
        if (state.sourceArea == null || state.sourceArea.isEmpty()) {
            state.cleanup = state.sourceArea == null;
        } else {
            // set the source region
            state.readParameters.setSourceRegion(state.sourceArea);

            // Setting subsampling
            setSourceSubsamplingFactors(state.readParameters);

            if (ignoreBands(request, state.reader, state.ovrIndex, state.readParameters)) {
                ((EnhancedImageReadParam) state.readParameters).setBands(null);
            }
            state.proceed = true;
        }

        return state;
    }

    private void prepareReader(final RasterLayerRequest request, ReaderState state)
            throws IOException {
        // get a stream from the granuleAccessProvider
        assert cachedStreamSPI != null : "no cachedStreamSPI available!";
        state.inStream = granuleAccessProvider.getImageInputStream();
        if (state.inStream != null) {
            // get a reader and try to cache the relevant SPI
            if (cachedReaderSPI == null) {
                state.reader = ImageIOExt.getImageioReader(state.inStream);
                if (state.reader != null) cachedReaderSPI = state.reader.getOriginatingProvider();
            } else {
                state.reader = granuleAccessProvider.getImageReader();
            }
        }
    }

    private void prepareExternalReader(final RasterLayerRequest request, ReaderState state)
            throws IOException {
        // If the file is external we must update the Granule elements
        state.granuleURL = ovrProvider.getOvrURL();
        assert ovrProvider.getExternalOverviewInputStreamSpi() != null
                : "no cachedStreamSPI available for external overview!";
        SourceSPIProvider sourceSpiProvider =
                ovrProvider.getSourceSpiProvider().getCompatibleSourceProvider(state.granuleURL);
        state.inStream = sourceSpiProvider.getStream();
        // get a reader and try to cache the relevant SPI
        state.reader = sourceSpiProvider.getReader();
        if (state.reader != null) {
            // set input
            boolean ignoreMetadata = false;
            if (state.reader instanceof InitializingReader) {
                ignoreMetadata = ((InitializingReader) state.reader).init(hints);
            }
            state.reader.setInput(state.inStream, false, ignoreMetadata);
        }
    }

    // don't pass down the band selection if the original color model is indexed and
    // color expansion is enabled
    private boolean ignoreBands(
            RasterLayerRequest request,
            ImageReader reader,
            int ovrIndex,
            ImageReadParam readParameters) {
        final boolean expandToRGB = request.getRasterManager().isExpandMe();
        if (expandToRGB
                && getRawColorModel(reader, ovrIndex) instanceof IndexColorModel
                && readParameters instanceof EnhancedImageReadParam) {
            return true;
        }
        return false;
    }

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
    private RenderedImage applyVirtualNativeResolution(
            final RasterLayerRequest request,
            RenderedImage raster,
            final Hints hints,
            final GranuleOverviewLevelDescriptor selectedlevel) {
        final double[] virtualNativeResolution = request.getVirtualNativeResolution();
        if (virtualNativeResolution != null
                && !Double.isNaN(virtualNativeResolution[0])
                && !Double.isNaN(virtualNativeResolution[1])) {
            // Note that virtualNativeResolution may have been set to NaN by
            // overviewController
            raster =
                    forceVirtualNativeResolution(
                            raster, request, virtualNativeResolution, selectedlevel, hints);
        }
        return raster;
    }

    private AffineTransform computeFinalRasterToModel(
            RenderedImage raster,
            final GranuleOverviewLevelDescriptor selectedlevel,
            final Rectangle sourceArea) {
        final AffineTransform finalRaster2Model;
        {
            final double decimationScaleX = ((1.0 * sourceArea.width) / raster.getWidth());
            final double decimationScaleY = ((1.0 * sourceArea.height) / raster.getHeight());
            final AffineTransform decimationScaleTranform =
                    XAffineTransform.getScaleInstance(decimationScaleX, decimationScaleY);

            // keep into account translation to work into the selected level raster space
            final AffineTransform afterDecimationTranslateTranform =
                    XAffineTransform.getTranslateInstance(sourceArea.x, sourceArea.y);

            // now we need to go back to the base level raster space
            final AffineTransform backToBaseLevelScaleTransform =
                    selectedlevel.baseToLevelTransform;

            // now create the overall transform
            finalRaster2Model = new AffineTransform(baseGridToWorld);
            finalRaster2Model.concatenate(CoverageUtilities.CENTER_TO_CORNER);

            if (!XAffineTransform.isIdentity(
                    backToBaseLevelScaleTransform, CoverageUtilities.AFFINE_IDENTITY_EPS))
                finalRaster2Model.concatenate(backToBaseLevelScaleTransform);
            if (!XAffineTransform.isIdentity(
                    afterDecimationTranslateTranform, CoverageUtilities.AFFINE_IDENTITY_EPS))
                finalRaster2Model.concatenate(afterDecimationTranslateTranform);
            if (!XAffineTransform.isIdentity(
                    decimationScaleTranform, CoverageUtilities.AFFINE_IDENTITY_EPS))
                finalRaster2Model.concatenate(decimationScaleTranform);
        }
        return finalRaster2Model;
    }

    private int overrideOverviewsForBaseLayer(
            final RasterLayerRequest request, ImageReadParam readParameters)
            throws IOException, TransformException {
        int imageIndex;
        final double[] virtualNativeResolution = request.getVirtualNativeResolution();

        // override the overviews controller for the base layer
        imageIndex =
                ReadParamsController.setReadParams(
                        request.spatialRequestHelper.getComputedResolution(),
                        request.getOverviewPolicy(),
                        request.getDecimationPolicy(),
                        readParameters,
                        request.rasterManager,
                        overviewsController,
                        virtualNativeResolution);
        return imageIndex;
    }

    private boolean mosaicIsMultidimensional() {
        return originator != null && originator.getAttribute("imageindex") != null;
    }

    /**
     * Perform band selection if necessary, so far netcdf is the only low level reader that handles
     * bands selection, if more readers start to support it a decent approach should be used to know
     * if the low level reader already performed the bands selection or if image mosaic is
     * responsible for do it
     *
     * @param request
     */
    private RenderedImage performBandSelection(
            RasterLayerRequest request, final Hints hints, RenderedImage raster, ImageReader reader)
            throws IOException {

        final int[] bands = request.getBands();
        final boolean expandToRGB = request.getRasterManager().isExpandMe();
        if (bands != null && !reader.getFormatName().equalsIgnoreCase("netcdf")) {
            // if we are expanding the color model, do so before selecting the bands
            if (raster.getColorModel() instanceof IndexColorModel && expandToRGB) {
                raster = new ImageWorker(raster).forceComponentColorModel().getRenderedImage();
            }

            // delegate the band selection operation on JAI BandSelect operation
            raster = new ImageWorker(raster).retainBands(bands).getRenderedImage();
            ColorModel colorModel = raster.getColorModel();
            if (colorModel == null) {
                ImageLayout layout = (ImageLayout) hints.get(JAI.KEY_IMAGE_LAYOUT);
                if (layout == null) {
                    layout = new ImageLayout();
                }
                ColorModel newColorModel =
                        ImageIOUtilities.createColorModel(raster.getSampleModel());
                if (newColorModel != null) {
                    layout.setColorModel(newColorModel);
                    raster =
                            new ImageWorker(raster)
                                    .setRenderingHints(hints)
                                    .format(raster.getSampleModel().getDataType())
                                    .getRenderedImage();
                }
            }
        }
        return raster;
    }

    private RenderedImage applyRescaling(
            final RasterLayerRequest request, final Hints hints, RenderedImage raster) {
        if (request.isRescalingEnabled()) {
            if (noData != null && request.getReadType() == ReadType.JAI_IMAGEREAD) {
                // Force nodata settings since JAI ImageRead may lost that
                // We have to make sure that noData pixels won't be rescaled
                PlanarImage t = PlanarImage.wrapRenderedImage(raster);
                t.setProperty(NoDataContainer.GC_NODATA, noData);
                raster = t;
            }
            final int[] bands = request.getBands();
            raster = rescale(raster, hints, bands);
        }
        return raster;
    }

    private RasterState loadAndPrepareRaster(
            final RequestParams params, final ReaderState readerState) throws IOException {

        final RasterLayerRequest request = params.request;

        RasterState state = new RasterState();
        state.proceed = false;

        final ImageReadParam readParameters = readerState.readParameters;
        final int ovrIndex = readerState.ovrIndex;
        final URL granuleURLUpdated = readerState.granuleURL;
        final GranuleOverviewLevelDescriptor selectedlevel = readerState.selectedlevel;
        final ImageReader reader = readerState.reader;
        final boolean closeElements = false;
        // read
        final ReadType readType = request.getReadType();
        state.raster =
                readType.read(
                        readParameters,
                        ovrIndex,
                        granuleURLUpdated,
                        selectedlevel.rasterDimensions,
                        reader,
                        hints,
                        closeElements);

        state.raster = performBandSelection(request, hints, state.raster, readerState.reader);

        // apply rescaling
        state.raster = applyRescaling(request, hints, state.raster);

        // use fixed source area
        readerState.sourceArea.setRect(readerState.readParameters.getSourceRegion());

        state.raster =
                applyVirtualNativeResolution(
                        request, state.raster, hints, readerState.selectedlevel);
        state.finalRaster2Model =
                computeFinalRasterToModel(
                        state.raster, readerState.selectedlevel, readerState.sourceArea);
        // adjust roi
        final boolean useFootprint = null != params.inclusionGeometry;
        if (useFootprint) {
            state.raster =
                    adjustROI(
                            request,
                            readerState.imageIndex,
                            readerState.readParameters,
                            state.raster,
                            state.finalRaster2Model);
            if (state.raster == null) {
                return state;
            }
        }
        // keep into account translation factors to place this tile
        state.finalRaster2Model.preConcatenate((AffineTransform) params.mosaicWorldToGrid);
        final Interpolation interpolation = request.getInterpolation();

        // paranoic check to avoid that JAI freaks out when computing its internal layout on
        // images that are too small
        final Rectangle2D finalLayout =
                finalLayout(state.finalRaster2Model, state.raster, interpolation);
        if (!finalLayout.isEmpty()) {
            state.proceed = true;
        }

        return state;
    }

    private RenderedImage adjustROI(
            final RasterLayerRequest request,
            final int imageIndex,
            final ImageReadParam readParameters,
            RenderedImage raster,
            final AffineTransform finalRaster2Model) {
        ROI transformed;
        try {
            // Getting Image Bounds
            Rectangle imgBounds =
                    new Rectangle(
                            raster.getMinX(),
                            raster.getMinY(),
                            raster.getWidth(),
                            raster.getHeight());
            // Getting Transformed ROI
            transformed =
                    roiProvider.getTransformedROI(
                            finalRaster2Model.createInverse(),
                            imageIndex,
                            imgBounds,
                            readParameters,
                            request.getReadType());
            // Check for vectorial ROI
            if (transformed instanceof ROIGeometry
                    && ((ROIGeometry) transformed).getAsGeometry().isEmpty()) {
                // inset might have killed the geometry fully
                return null;
            }
            // Check for Raster ROI
            if (transformed == null || transformed.getBounds().isEmpty()) {
                // ROI is less than a pixel big, it happens when zooming out
                if (LOGGER.isLoggable(java.util.logging.Level.FINE))
                    LOGGER.fine(
                            String.format(
                                    "Unable to create a granuleDescriptor %s due to a problem when managing the ROI",
                                    this));
                return null;
            }

            PlanarImage pi = PlanarImage.wrapRenderedImage(raster);
            if (!transformed.intersects(pi.getBounds())) {
                return null;
            }
            pi.setProperty("ROI", transformed);
            if (pi instanceof RenderedOp) {
                // For some reason the "ROI" property is sometime lost
                // when getting the rendering
                PlanarImage theImage = ((RenderedOp) pi).getRendering();
                theImage.setProperty("ROI", transformed);
            }
            raster = pi;

        } catch (NoninvertibleTransformException e) {
            if (LOGGER.isLoggable(java.util.logging.Level.INFO))
                LOGGER.info(
                        String.format(
                                "Unable to create a granuleDescriptor %s due to a problem when managing the ROI",
                                this));
            return null;
        }
        return raster;
    }

    private Rectangle2D finalLayout(
            AffineTransform finalRaster2Model, RenderedImage raster, Interpolation interpolation) {
        final Rectangle2D finalLayout =
                ImageUtilities.layoutHelper(
                        raster,
                        (float) finalRaster2Model.getScaleX(),
                        (float) finalRaster2Model.getScaleY(),
                        (float) finalRaster2Model.getTranslateX(),
                        (float) finalRaster2Model.getTranslateY(),
                        interpolation);
        if (finalLayout.isEmpty()) {
            if (LOGGER.isLoggable(java.util.logging.Level.FINE))
                LOGGER.fine(
                        String.format(
                                "Unable to create a granuleDescriptor %s due to jai scale bug creating a null source area",
                                this));
        }
        return finalLayout;
    }

    private void setRasterLayerRequest(ImageReader reader, final RasterLayerRequest request) {
        if (MethodUtils.getAccessibleMethod(
                        reader.getClass(), "setRasterLayerRequest", RasterLayerRequest.class)
                != null) {
            try {
                MethodUtils.invokeMethod(reader, "setRasterLayerRequest", request);
            } catch (Exception exception) {
                throw new RuntimeException(
                        "Error setting raster layer request on reader.", exception);
            }
        }
    }

    private void setReaderInput(ImageReader reader, ImageInputStream inStream, final Hints hints) {
        if (reader instanceof InitializingReader) {
            ((InitializingReader) reader).init(hints);
        }
        reader.setInput(inStream);
    }

    private Rectangle calcSourceArea(
            final RasterLayerRequest request,
            final ReferencedEnvelope intersection,
            final int imageIndex,
            final ImageReadParam readParameters,
            final GranuleOverviewLevelDescriptor selectedlevel)
            throws org.opengis.referencing.operation.NoninvertibleTransformException,
                    TransformException {
        final Rectangle sourceArea;
        // now create the crop grid to world which can be used to decide
        // which source area we need to crop in the selected level taking
        // into account the scale factors imposed by the selection of this
        // level together with the base level grid to world transformation
        AffineTransform2D cropGridToWorld =
                new AffineTransform2D(selectedlevel.gridToWorldTransformCorner);
        AffineTransform2D cropWorldToGrid = (AffineTransform2D) cropGridToWorld.inverse();
        // computing the crop source area which lives into the
        // selected level raster space, NOTICE that at the end we need to
        // take into account the fact that we might also decimate therefore
        // we cannot just use the crop grid to world but we need to correct
        // it.
        Rectangle2D r2d = CRS.transform(cropWorldToGrid, intersection).toRectangle2D();
        // if we are reading basically nothing, bail out immediately
        if (r2d.getWidth() < READ_THRESHOLD || r2d.getHeight() < READ_THRESHOLD) {
            return null;
        }
        sourceArea = r2d.getBounds();
        // gutter
        if (selectedlevel.baseToLevelTransform.isIdentity()) {
            sourceArea.grow(2, 2);
        }
        XRectangle2D.intersect(
                sourceArea, // src1
                selectedlevel.rasterDimensions, // src2
                sourceArea); // dest. make sure roundings don't bother us
        // is it empty??
        if (sourceArea.isEmpty()) {
            if (LOGGER.isLoggable(java.util.logging.Level.FINE)) {
                LOGGER.fine(
                        String.format(
                                "Got empty area for granuleDescriptor %s with request %s Resulting in no granule loaded: Empty result",
                                this, request));
            }
        } else if (LOGGER.isLoggable(java.util.logging.Level.FINER)) {
            LOGGER.finer(
                    String.format(
                            "Loading level %d with source region: %s subsampling: %d,%d for granule: %s",
                            imageIndex,
                            sourceArea,
                            readParameters.getSourceXSubsampling(),
                            readParameters.getSourceYSubsampling(),
                            granuleUrl));
        }
        return sourceArea;
    }

    private void setSourceSubsamplingFactors(final ImageReadParam readParameters) {
        int newSubSamplingFactor = 0;
        final String pluginName = cachedReaderSPI.getPluginClassName();
        if (pluginName != null && pluginName.equals(ImageUtilities.DIRECT_KAKADU_PLUGIN)) {
            final int ssx = readParameters.getSourceXSubsampling();
            final int ssy = readParameters.getSourceYSubsampling();
            newSubSamplingFactor = ImageIOUtilities.getSubSamplingFactor2(ssx, ssy);
            if (newSubSamplingFactor != 0) {
                if (newSubSamplingFactor > maxDecimationFactor && maxDecimationFactor != -1) {
                    newSubSamplingFactor = maxDecimationFactor;
                }
                readParameters.setSourceSubsampling(
                        newSubSamplingFactor, newSubSamplingFactor, 0, 0);
            }
        }
    }

    private RenderedImage buildRenderedImage(RequestParams params, RasterState rasterState) {

        final RasterLayerRequest request = params.request;
        final Interpolation interpolation = request.getInterpolation();
        final double[] virtualNativeResolution = request.getVirtualNativeResolution();
        final Hints hints = params.hints;
        final RenderedImage raster = rasterState.raster;
        final AffineTransform finalRaster2Model = rasterState.finalRaster2Model;
        final boolean useFootprint = null != params.inclusionGeometry;

        ImageWorker iw =
                prepareImageWorker(
                        request,
                        hints,
                        raster,
                        virtualNativeResolution,
                        finalRaster2Model,
                        interpolation);
        RenderedImage renderedImage = iw.getRenderedImage();
        if (useFootprint && roiIsEmpty(renderedImage)) {
            // JAI not only transforms the ROI, but may also apply clipping to the image
            // boundary. this results in an empty ROI in some edge cases
            return null;
        }
        // Propagate NoData
        if (iw.getNoData() != null) {
            PlanarImage t = PlanarImage.wrapRenderedImage(renderedImage);
            t.setProperty(NoDataContainer.GC_NODATA, new NoDataContainer(iw.getNoData()));
            renderedImage = t;
        } else if (this.noData != null) {
            // on deferred loading we cannot get the noData from the image, but we might
            // have read it
            // at the beginning
            PlanarImage t = PlanarImage.wrapRenderedImage(renderedImage);
            t.setProperty(NoDataContainer.GC_NODATA, noData);
            renderedImage = t;
        }
        return renderedImage;
    }

    private boolean roiIsEmpty(RenderedImage renderedImage) {
        Object roi = renderedImage.getProperty("ROI");
        return (roi instanceof ROIGeometry && ((ROIGeometry) roi).getAsGeometry().isEmpty())
                || (roi instanceof ROI && ((ROI) roi).getBounds().isEmpty());
    }

    private ImageWorker prepareImageWorker(
            final RasterLayerRequest request,
            final Hints hints,
            RenderedImage raster,
            final double[] virtualNativeResolution,
            final AffineTransform finalRaster2Model,
            final Interpolation interpolation) {
        ImageWorker iw;
        {
            final RenderingHints localHints =
                    new RenderingHints(
                            JAI.KEY_REPLACE_INDEX_COLOR_MODEL,
                            interpolation instanceof InterpolationNearest
                                    ? Boolean.FALSE
                                    : Boolean.TRUE);
            //
            // In case we are asked to use certain tile dimensions we tile
            // also at this stage in case the read type is Direct since
            // buffered images comes up untiled and this can affect the
            // performances of the subsequent affine operation.
            //
            final Dimension tileDimensions = request.getTileDimensions();
            if (tileDimensions != null && request.getReadType().equals(ReadType.DIRECT_READ)) {
                final ImageLayout layout = new ImageLayout();
                layout.setTileHeight(tileDimensions.width).setTileWidth(tileDimensions.height);
                localHints.add(new RenderingHints(JAI.KEY_IMAGE_LAYOUT, layout));
            } else {
                ImageLayout layout = Utils.getImageLayoutHint(hints);
                if (layout != null) {
                    localHints.add(new RenderingHints(JAI.KEY_IMAGE_LAYOUT, layout.clone()));
                }
            }
            final TileCache cache = Utils.getTileCacheHint(hints);
            if (cache != null) {
                localHints.add(new RenderingHints(JAI.KEY_TILE_CACHE, cache));
            }
            final TileScheduler scheduler = Utils.getTileSchedulerHint(hints);
            if (scheduler != null) {
                localHints.add(new RenderingHints(JAI.KEY_TILE_SCHEDULER, scheduler));
            }

            final BorderExtender extender = Utils.getBorderExtenderHint(hints);
            if (extender != null) {
                localHints.add(new RenderingHints(JAI.KEY_BORDER_EXTENDER, extender));
            } else {
                localHints.add(ImageUtilities.BORDER_EXTENDER_HINTS);
            }
            iw = new ImageWorker(raster);
            if (virtualNativeResolution != null
                    && !Double.isNaN(virtualNativeResolution[0])
                    && !Double.isNaN(virtualNativeResolution[1])) {
                localHints.add(new RenderingHints(ImageWorker.PRESERVE_CHAINED_AFFINES, true));
            }

            iw.setRenderingHints(localHints);
            if (iw.getNoData() == null && this.noData != null) {
                iw.setNoData(this.noData.getAsRange());
            }
            iw.affine(finalRaster2Model, interpolation, request.getBackgroundValues());
        }
        return iw;
    }

    private boolean boundsOverlap(RequestParams params) {

        final boolean thisTileIntersectsCropBBox = !params.intersection.isEmpty();
        if (!thisTileIntersectsCropBBox) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine(
                        String.format(
                                "Got empty intersection for granule %s with request %s Resulting in no granule loaded: Empty result",
                                this, params.request));
            }
            return false;
        }
        // check if the requested bbox intersects or overlaps the requested area
        final boolean useFootprint = params.inclusionGeometry != null;
        final boolean requestedBboxOverlapsRequestedArea =
                !(useFootprint
                        && params.inclusionGeometry != null
                        && !JTS.toGeometry(params.cropBBox).intersects(params.inclusionGeometry));
        if (!requestedBboxOverlapsRequestedArea) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine(
                        String.format(
                                "Got empty intersection for granule %s with request %s Resulting in no granule loaded: Empty result",
                                this, params.request));
            }
            return false;
        }
        return true;
    }

    private ReferencedEnvelope cropBBoxIntersection(
            final Geometry inclusionGeometry, final ReferencedEnvelope cropBBox) {

        final ReferencedEnvelope bbox =
                inclusionGeometry == null
                        ? granuleBBOX
                        : new ReferencedEnvelope(
                                granuleBBOX.intersection(inclusionGeometry.getEnvelopeInternal()),
                                granuleBBOX.getCoordinateReferenceSystem());

        ReferencedEnvelope cropIntersection = bbox.intersection(cropBBox);
        CoordinateReferenceSystem cropCrs = cropBBox.getCoordinateReferenceSystem();
        return new ReferencedEnvelope(cropIntersection, cropCrs);
    }

    private Geometry resolveInclusionGeometry(final RasterLayerRequest request) {
        final boolean useFootprint =
                roiProvider != null && request.getFootprintBehavior() != FootprintBehavior.None;
        return useFootprint ? roiProvider.getFootprint() : null;
    }

    private boolean finalRaster2ModelIsIdentity(AffineTransform finalRaster2Model) {
        return XAffineTransform.isIdentity(
                finalRaster2Model, CoverageUtilities.AFFINE_IDENTITY_EPS);
    }

    private GranuleLoadingResult createIdentityLoadingResult(
            RenderedImage raster, URL granuleURLUpdated, final boolean doFiltering) {
        if (noData != null) {
            PlanarImage t = PlanarImage.wrapRenderedImage(raster);
            t.setProperty(NoDataContainer.GC_NODATA, noData);
            raster = t;
        }
        return new GranuleLoadingResult(
                raster, null, granuleURLUpdated, doFiltering, pamDataset, this);
    }

    private boolean shallDoFiltering(Geometry inclusionGeometry) {
        boolean useFootprint = inclusionGeometry != null;
        if (filterMe && useFootprint)
            return Utils.areaIsDifferent(inclusionGeometry, baseGridToWorld, granuleBBOX);
        return false;
    }

    private RenderedImage rescale(RenderedImage raster, Hints hints, int[] bands) {
        Double[] rescalingScales = scales;
        Double[] rescalingOffsets = offsets;

        // make sure to update scales and offsets if there is a band-selection
        if (bands != null && (scales != null || offsets != null)) {
            rescalingScales = new Double[bands.length];
            rescalingOffsets = new Double[bands.length];
            int i = 0;
            for (int bandIndex : bands) {
                rescalingScales[i] = scales != null ? scales[bandIndex] : null;
                rescalingOffsets[i++] = offsets != null ? offsets[bandIndex] : null;
            }
        }
        raster = ImageUtilities.applyRescaling(rescalingScales, rescalingOffsets, raster, hints);
        return raster;
    }

    private RenderedImage forceVirtualNativeResolution(
            RenderedImage raster,
            final RasterLayerRequest request,
            final double[] virtualNativeResolution,
            final GranuleOverviewLevelDescriptor selectedlevel,
            final Hints hints) {

        // Setup affine transformation to force the read raster to the requested virtual native
        // resolution
        final AffineTransform virtualTransform =
                XAffineTransform.getScaleInstance(
                        XAffineTransform.getScaleX0(selectedlevel.gridToWorldTransformCorner)
                                / virtualNativeResolution[0],
                        XAffineTransform.getScaleY0(selectedlevel.gridToWorldTransformCorner)
                                / virtualNativeResolution[1]);

        final Dimension tileDimensions = request.getTileDimensions();
        RenderingHints localHints = null;
        if (tileDimensions != null && request.getReadType().equals(ReadType.DIRECT_READ)) {
            final ImageLayout layout = new ImageLayout();
            layout.setTileHeight(tileDimensions.width).setTileWidth(tileDimensions.height);
            localHints = new RenderingHints(JAI.KEY_IMAGE_LAYOUT, layout);
        } else {
            final ImageLayout originalLayout = Utils.getImageLayoutHint(hints);
            if (originalLayout != null) {
                final ImageLayout localLayout = new ImageLayout();
                localLayout.setTileHeight(originalLayout.getTileHeight(null));
                localLayout.setTileWidth(originalLayout.getTileWidth(null));
                localHints = new RenderingHints(JAI.KEY_IMAGE_LAYOUT, localLayout);
            }
        }
        if (localHints == null) {
            localHints = new RenderingHints(null);
        }
        updateLocalHints(hints, localHints);
        localHints.add(ImageUtilities.BORDER_EXTENDER_HINTS);
        ImageWorker worker = new ImageWorker(raster).setRenderingHints(localHints);
        return worker.affine(
                        virtualTransform, request.getInterpolation(), request.getBackgroundValues())
                .getRenderedImage();
    }

    private void updateLocalHints(Hints hints, RenderingHints localHints) {
        final TileCache cache = Utils.getTileCacheHint(hints);
        if (cache != null) localHints.add(new RenderingHints(JAI.KEY_TILE_CACHE, cache));
        final TileScheduler scheduler = Utils.getTileSchedulerHint(hints);
        if (scheduler != null)
            localHints.add(new RenderingHints(JAI.KEY_TILE_SCHEDULER, scheduler));
    }

    /** Returns the raw color model of the reader at the specified image index */
    private ColorModel getRawColorModel(ImageReader reader, int imageIndex) {
        try {
            ImageTypeSpecifier imageType = reader.getRawImageType(imageIndex);
            if (imageType == null) {
                return null;
            }
            ColorModel cm = imageType.getColorModel();
            return cm;
        } catch (Exception e) {
            LOGGER.log(Level.FINE, "Failed to determine the native color model of the reader", e);
        }

        return null;
    }

    private GranuleOverviewLevelDescriptor getLevel(
            final int index, final ImageReader reader, final int imageIndex) {

        final boolean isExternal = ovrProvider.isExternalOverview(imageIndex);
        return getLevel(index, reader, imageIndex, isExternal);
    }

    private GranuleOverviewLevelDescriptor getLevel(
            final int index,
            final ImageReader reader,
            final int imageIndex,
            final boolean external) {
        // Level index may change if using external overviews
        int indexValue = external ? imageIndex : index;
        if (reader == null)
            throw new NullPointerException(
                    "Null reader passed to the internal GranuleOverviewLevelDescriptor method");
        synchronized (granuleLevels) {
            if (granuleLevels.containsKey(Integer.valueOf(indexValue)))
                return granuleLevels.get(Integer.valueOf(indexValue));
            else {
                // load level
                // create the base grid to world transformation
                try {
                    //
                    // get info about the raster we have to read
                    //

                    // get selected level and base level dimensions
                    final Rectangle levelDimension = Utils.getDimension(index, reader);

                    final GranuleOverviewLevelDescriptor baseLevel = granuleLevels.get(0);
                    final double scaleX = baseLevel.width / (1.0 * levelDimension.width);
                    final double scaleY = baseLevel.height / (1.0 * levelDimension.height);

                    // add the base level
                    final GranuleOverviewLevelDescriptor newLevel =
                            new GranuleOverviewLevelDescriptor(
                                    scaleX, scaleY, levelDimension.width, levelDimension.height);
                    this.granuleLevels.put(Integer.valueOf(indexValue), newLevel);

                    return newLevel;

                } catch (IllegalStateException | IOException e) {
                    throw new IllegalArgumentException(e);
                }
            }
        }
    }

    GranuleOverviewLevelDescriptor getLevel(final int index) {

        // load level
        // create the base grid to world transformation
        ImageReader reader = null;
        try (ImageInputStream inStream =
                cachedStreamSPI.createInputStreamInstance(
                        granuleUrl, ImageIO.getUseCache(), ImageIO.getCacheDirectory())) {

            // get a stream
            assert cachedStreamSPI != null : "no cachedStreamSPI available!";

            if (inStream == null)
                throw new IllegalArgumentException(
                        "Unable to create an inputstream for the granuleurl:"
                                + (granuleUrl != null ? granuleUrl : "null"));

            // get a reader and try to cache the relevant SPI
            if (cachedReaderSPI == null) {
                reader = ImageIOExt.getImageioReader(inStream);
                if (reader != null) cachedReaderSPI = reader.getOriginatingProvider();
            } else reader = cachedReaderSPI.createReaderInstance();
            if (reader == null)
                throw new IllegalArgumentException(
                        "Unable to get an ImageReader for the provided file "
                                + granuleUrl.toString());

            reader.setInput(inStream, false, false);

            // call internal method which will close everything
            return getLevel(index, reader, index, false);

        } catch (IllegalStateException | IOException e) {

            // clean up
            if (reader != null) reader.dispose();

            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public String toString() {
        // build a decent representation for this level
        final StringBuilder buffer = new StringBuilder();
        buffer.append("Description of a granuleDescriptor ").append("\n");
        buffer.append("BBOX:\t\t").append(granuleBBOX.toString()).append("\n");
        buffer.append("file:\t\t").append(granuleUrl).append("\n");
        buffer.append("gridToWorld:\t\t").append(baseGridToWorld).append("\n");
        int i = 1;
        for (final GranuleOverviewLevelDescriptor granuleOverviewLevelDescriptor :
                granuleLevels.values()) {
            i++;
            buffer.append("Description of level ").append(i++).append("\n");
            buffer.append(granuleOverviewLevelDescriptor.toString()).append("\n");
        }
        return buffer.toString();
    }

    public BoundingBox getGranuleBBOX() {
        return granuleBBOX;
    }

    public URL getGranuleUrl() {
        return granuleUrl;
    }

    public SimpleFeature getOriginator() {
        return originator;
    }

    public Geometry getFootprint() {
        if (roiProvider == null) {
            return null;
        } else {
            return roiProvider.getFootprint();
        }
    }

    /**
     * Returns a new instance of the AbstractGridCoverage2DReader associated with this descriptor.
     * It's the responsibility of the caller to dispose of it.
     */
    public AbstractGridCoverage2DReader getReader() {
        return this.format.getReader(granuleUrl, hints);
    }

    /** @return */
    public MultiLevelROI getRoiProvider() {
        return this.roiProvider;
    }
}
