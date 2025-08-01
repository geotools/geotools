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
import it.geosolutions.imageioimpl.plugins.tiff.TIFFImageMetadata;
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
import java.awt.image.SampleModel;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Arrays;
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
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.geometry.BoundingBox;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.datum.PixelInCell;
import org.geotools.api.referencing.operation.MathTransform2D;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.footprint.FootprintBehavior;
import org.geotools.coverage.grid.io.footprint.MultiLevelROI;
import org.geotools.coverage.grid.io.imageio.MaskOverviewProvider;
import org.geotools.coverage.grid.io.imageio.ReadType;
import org.geotools.coverage.util.CoverageUtilities;
import org.geotools.geometry.GeneralBounds;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.geometry.util.XRectangle2D;
import org.geotools.image.ImageWorker;
import org.geotools.image.io.ImageIOExt;
import org.geotools.image.jai.Registry;
import org.geotools.image.util.ImageUtilities;
import org.geotools.metadata.i18n.ErrorKeys;
import org.geotools.referencing.CRS;
import org.geotools.referencing.operation.builder.GridToEnvelopeMapper;
import org.geotools.referencing.operation.matrix.XAffineTransform;
import org.geotools.referencing.operation.transform.AffineTransform2D;
import org.geotools.util.URLs;
import org.geotools.util.factory.Hints;
import org.locationtech.jts.geom.Geometry;

/**
 * A granuleDescriptor is a single piece of the mosaic, with its own overviews and everything.
 *
 * <p>This class is responsible for caching the various size of the different levels of each single granuleDescriptor
 * since computing them each time is expensive (opening a file, looking for a reader, parsing metadata,etc...).
 *
 * <p>Right now we are making the assumption that a single granuleDescriptor is made a by a single file with embedded
 * overviews, either explicit or intrinsic through wavelets like MrSID, ECW or JPEG2000.
 *
 * @author Simone Giannecchini, GeoSolutions S.A.S.
 * @author Stefan Alfons Krueger (alfonx), Wikisquare.de : Support for jar:file:foo.jar/bar.properties URLs
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
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(GranuleDescriptor.class);

    private static final String AUXFILE_EXT = ".aux.xml";

    /** Hints to use for avoiding to search for the ImageMosaic format */
    public static final Hints EXCLUDE_MOSAIC = new Hints(Utils.EXCLUDE_MOSAIC, true);

    /**
     * Minimum portion of a single pixel the code is going to read before giving up on the read, this is used to avoid
     * reading granules that touch the reading area without actually contributing anything to the output
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
        } catch (Throwable e) {
            LOGGER.log(Level.SEVERE, "Error when registering RIF for GranuleDescriptor.", e);
        }
    }

    OverviewsController overviewsController;

    private GeneralBounds granuleEnvelope;
    private AbstractGridFormat format;

    private boolean nativeBandSelection;
    private Hints hints;

    public GeneralBounds getGranuleEnvelope() {
        return granuleEnvelope;
    }

    public void setGranuleEnvelope(GeneralBounds granuleEnvelope) {
        this.granuleEnvelope = granuleEnvelope;
    }

    /**
     * This class represent an overview level in a single granuleDescriptor.
     *
     * <p>Notice that the internal transformations for the various levels are reffered to the corner, rather than to the
     * centre.
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
            this.baseToLevelTransform = new AffineTransform2D(XAffineTransform.getScaleInstance(scaleX, scaleY, 0, 0));

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
     * Simple placeholder class to store the result of a Granule Loading which comprises of a raster as well as a
     * {@link ROIShape} for its footprint.
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

    private static PAMParser pamParser;

    ReferencedEnvelope granuleBBOX;

    MultiLevelROI roiProvider;

    URL granuleUrl;

    int maxDecimationFactor = -1;

    final Map<Integer, GranuleOverviewLevelDescriptor> granuleLevels = Collections.synchronizedMap(new HashMap<>());

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

    private SampleModel sampleModel;

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
            Object providerHint = Utils.getHintIfAvailable(hints, GranuleAccessProvider.GRANULE_ACCESS_PROVIDER);
            if (providerHint != null) {
                granuleAccessProvider = ((GranuleAccessProvider) providerHint).copyProviders();
                granuleAccessProvider.setGranuleInput(granuleUrl);
            } else {
                granuleAccessProvider =
                        getDefaultProvider(input, suggestedFormat, suggestedSPI, suggestedIsSPI, false, hints);
            }

            this.format = granuleAccessProvider.getFormat();
            this.nativeBandSelection = supportsNativeBandSelection();
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
                        "Unable to get a ReaderSPI for the provided input: " + granuleUrl.toString());
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
                    new GridToEnvelopeMapper(new GridEnvelope2D(originalDimension), this.granuleBBOX);
            geMapper.setPixelAnchor(PixelInCell.CELL_CENTER); // this is the default behavior but it is nice to write
            // it down anyway
            this.baseGridToWorld = geMapper.createAffineTransform();

            // add the base level
            this.granuleLevels.put(
                    Integer.valueOf(0),
                    new GranuleOverviewLevelDescriptor(1, 1, originalDimension.width, originalDimension.height));

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
                final GranuleOverviewLevelDescriptor baseOverviewLevelDescriptor = granuleLevels.get(0);

                // Variables initialization
                final int numberOfOvervies = ovrProvider.getNumOverviews();
                final AffineTransform2D baseG2W = baseOverviewLevelDescriptor.getGridToWorldTransform();
                final int width = baseOverviewLevelDescriptor.getWidth();
                final int height = baseOverviewLevelDescriptor.getHeight();
                final double resX = AffineTransform2D.getScaleX0(baseG2W);
                final double resY = AffineTransform2D.getScaleY0(baseG2W);
                final double[] highestRes = {resX, resY};

                // Populating overviews and initializing overviewsController
                final double[][] overviewsResolution =
                        ovrProvider.getOverviewResolutions(highestRes[0] * width, highestRes[1] * height);
                overviewsController = new OverviewsController(highestRes, numberOfOvervies, overviewsResolution);
            }
            //////////////////////////////////////////////////////////////////////////

            boolean checkAuxiliaryMetadata = checkAuxiliaryMetadata(hints);
            if (checkAuxiliaryMetadata) {
                checkPamDataset();
            }

            // handle the nodata and rescaling if available
            initFromImageMetadata(imageReader, checkAuxiliaryMetadata);
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

    private boolean checkAuxiliaryMetadata(Hints hints) {
        if (hints != null && hints.containsKey(Utils.CHECK_AUXILIARY_METADATA))
            return (Boolean) hints.get(Utils.CHECK_AUXILIARY_METADATA);
        return false;
    }

    private boolean supportsNativeBandSelection() {
        try {
            return format.getReadParameters().values().stream()
                    .anyMatch(p -> p.getDescriptor().getName().equals(AbstractGridFormat.BANDS.getName()));
        } catch (UnsupportedOperationException e) {
            // AbstractGridFormat may throw this exception if there are no read params (happens
            // with the test class RemoteImageFormat for example)
            return false;
        }
    }

    private GranuleAccessProvider getDefaultProvider(
            Object input,
            AbstractGridFormat suggestedFormat,
            ImageReaderSpi suggestedSPI,
            ImageInputStreamSpi suggestedIsSPI,
            boolean skipExternalOverviews,
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
        if (skipExternalOverviews) {
            suggestedObjectHints.put(Hints.SKIP_EXTERNAL_OVERVIEWS, skipExternalOverviews);
        }
        // When looking for formats which may parse this file, make sure to exclude the
        // ImageMosaicFormat as return
        suggestedObjectHints.add(EXCLUDE_MOSAIC);
        DefaultGranuleAccessProvider provider = new DefaultGranuleAccessProvider(suggestedObjectHints);
        provider.setGranuleInput(input);
        return provider;
    }

    private void initFromImageMetadata(ImageReader reader, boolean readPam) throws IOException {
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

                double[] noDataArray = null;
                if (ccm.getNoDataValues() != null) {
                    noDataArray = Arrays.stream(ccm.getNoDataValues())
                            .mapToDouble(d -> d == null ? Double.NaN : d)
                            .toArray();
                    this.noData = new NoDataContainer(noDataArray);
                }
                this.scales = ccm.getScales();
                this.offsets = ccm.getOffsets();
            }
            if (readPam && this.pamDataset == null && metadata instanceof TIFFImageMetadata) {
                TIFFImageMetadata tiffMetadata = (TIFFImageMetadata) metadata;
                this.pamDataset = AbstractGridCoverage2DReader.getPamDataset(tiffMetadata);
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
        // store sample model to compute band-selected sub-image type for deferred loading
        // (it needs to know in advance or it will use the image native structure)
        this.sampleModel = reader.getImageTypes(index).next().getSampleModel();
    }

    public OverviewsController getOverviewsController() {
        return overviewsController;
    }

    /** Look for GDAL Auxiliary File and unmarshall it to setup a PamDataset if available */
    private void checkPamDataset() throws IOException {
        final File file = URLs.urlToFile(granuleUrl);
        final String path = file.getCanonicalPath();
        final File auxFile = new File(path + AUXFILE_EXT);
        if (auxFile.exists()) {
            if (pamParser == null) {
                try {
                    pamParser = PAMParser.getInstance();
                } catch (Throwable e) {
                    throw new RuntimeException("Couldn't initialize PAM parser.", e);
                }
            }
            pamDataset = pamParser.parsePAM(auxFile);
        }
    }

    public GranuleDescriptor(
            final String granuleLocation,
            final BoundingBox granuleBBox,
            final AbstractGridFormat suggestedFormat,
            final ImageReaderSpi suggestedSPI,
            final ImageInputStreamSpi suggestedIsSPI,
            final MultiLevelROI roiProvider) {
        this(granuleLocation, granuleBBox, suggestedFormat, suggestedSPI, suggestedIsSPI, roiProvider, -1, false);
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
     * Constructor for the {@link GranuleDescriptor} assuming it doesn't belong to an heterogeneous granules set.
     *
     * @param feature a {@link SimpleFeature} referring to that granule
     * @param suggestedSPI the suggested {@link ImageReaderSpi} to be used to get a reader to handle this granule.
     * @param pathType A {@link PathType} identifying if the granule location should be resolved as a relative or an
     *     absolute path.
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
     * @param suggestedSPI the suggested {@link ImageReaderSpi} to be used to get a reader to handle this granule.
     * @param pathType A {@link PathType} identifying if the granule location should be resolved as a relative or an
     *     absolute path.
     * @param locationAttribute the attribute containing the granule location.
     * @param parentLocation the location of the parent of that granule.
     * @param heterogeneousGranules if {@code true}, this granule belongs to a set of heterogeneous granules
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
        final String granuleLocation = (String) Utils.getAttribute(feature, locationAttribute);
        final ReferencedEnvelope granuleBBox = getFeatureBounds(feature);

        PathResolver pathResolver = new PathResolver(pathType, parentLocation);
        URL rasterGranule = extractRasterGranule(hints, granuleLocation, pathResolver, true);

        if (LOGGER.isLoggable(Level.FINE)) LOGGER.fine("Proceeding with granule: " + granuleLocation);

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
            Hints hints, String granuleLocation, PathResolver pathResolver, boolean exceptionOnNullGranule) {
        GranuleAccessProvider accessProvider =
                (GranuleAccessProvider) Utils.getHintIfAvailable(hints, GranuleAccessProvider.GRANULE_ACCESS_PROVIDER);

        URL rasterGranule = null;
        if (accessProvider instanceof CogGranuleAccessProvider) {
            try {
                CogGranuleAccessProvider cap = (CogGranuleAccessProvider) accessProvider;
                CogGranuleAccessProvider copy = (CogGranuleAccessProvider) cap.copyProviders();
                copy.setGranuleInput(granuleLocation);
                rasterGranule = copy.getInputURL();
            } catch (Exception e) {
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.log(
                            Level.WARNING,
                            "Unable to set an URL from the provided granuleLocation: " + granuleLocation,
                            e);
                }
                rasterGranule = null;
            }
        } else {
            // If the granuleDescriptor is not there, dump a message and continue
            if (pathResolver != null) {
                rasterGranule = pathResolver.resolve(granuleLocation);
            } else {
                rasterGranule = URLs.fileToUrl(new File(granuleLocation));
            }
        }
        if (rasterGranule == null && exceptionOnNullGranule) {
            throw new IllegalArgumentException(
                    MessageFormat.format(ErrorKeys.ILLEGAL_ARGUMENT_$2, "granuleLocation", granuleLocation));
        }
        return rasterGranule;
    }

    /**
     * Extracts the referenced envelope of the default geometry (used to be feature.getBounds, but that method returns
     * the bounds of all geometries in the feature)
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

    /**
     * Load a specified a raster as a portion of the granule describe by this {@link GranuleDescriptor}.
     *
     * @param imageReadParameters the {@link ImageReadParam} to use for reading.
     * @param index the index to use for the {@link ImageReader}.
     * @param cropBBox the bbox to use for cropping.
     * @param mosaicWorldToGrid the cropping grid to world transform.
     * @param request the incoming request to satisfy.
     * @param hints {@link Hints} to be used for creating this raster.
     * @return a specified a raster as a portion of the granule describe by this {@link GranuleDescriptor}.
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

        if (LOGGER.isLoggable(java.util.logging.Level.FINER)) {
            final String name = Thread.currentThread().getName();
            LOGGER.finer("Thread:" + name + " Loading raster data for granuleDescriptor " + this);
        }
        ImageReadParam readParameters = null;
        int imageIndex;
        final double[] virtualNativeResolution = request.getVirtualNativeResolution();
        final boolean useFootprint = roiProvider != null && request.getFootprintBehavior() != FootprintBehavior.None;
        Geometry inclusionGeometry = useFootprint ? roiProvider.getFootprint() : null;
        final ReferencedEnvelope bbox = useFootprint
                ? new ReferencedEnvelope(
                        granuleBBOX.intersection(inclusionGeometry.getEnvelopeInternal()),
                        granuleBBOX.getCoordinateReferenceSystem())
                : granuleBBOX;
        boolean doFiltering = false;
        if (filterMe && useFootprint) {
            doFiltering = Utils.areaIsDifferent(inclusionGeometry, baseGridToWorld, granuleBBOX);
        }

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

        // check if the requested bbox intersects or overlaps the requested area
        if (useFootprint
                && inclusionGeometry != null
                && !JTS.toGeometry(cropBBox).intersects(inclusionGeometry)) {
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

        // eventually gets closed in finally block, if possible (not deferred loading)
        @SuppressWarnings("PMD.CloseResource")
        ImageInputStream inStream = null;
        ImageReader reader = null;
        boolean cleanupInFinally = request.getReadType() != ReadType.JAI_IMAGEREAD;
        try { // NOPMD for UseTryWithResources, closure is conditional
            //
            // get info about the raster we have to read
            //

            // Checking for heterogeneous granules and if the mosaic is not multidimensional
            if (request.isHeterogeneousGranules()
                    && (originator == null || originator.getAttribute("imageindex") == null)) {
                // create read parameters
                readParameters = new EnhancedImageReadParam();

                // override the overviews controller for the base layer
                imageIndex = ReadParamsController.setReadParams(
                        request.spatialRequestHelper.getComputedResolution(),
                        request.getOverviewPolicy(),
                        request.getDecimationPolicy(),
                        readParameters,
                        request.rasterManager,
                        overviewsController,
                        virtualNativeResolution);

                if (imageReadParameters instanceof EnhancedImageReadParam) {
                    EnhancedImageReadParam erp = (EnhancedImageReadParam) imageReadParameters;
                    if (erp.getBands() != null) ((EnhancedImageReadParam) readParameters).setBands(erp.getBands());
                }
            } else {
                imageIndex = index;
                readParameters = imageReadParameters;
            }
            // Defining an Overview Index value
            int ovrIndex = imageIndex;
            boolean isExternal = ovrProvider.isExternalOverview(imageIndex);
            // Define a new URL to use (it may change if using external overviews)
            URL granuleURLUpdated = granuleUrl;
            // If the file is external we must update the Granule elements
            if (isExternal) {
                granuleURLUpdated = ovrProvider.getOvrURL();
                assert ovrProvider.getExternalOverviewInputStreamSpi() != null
                        : "no cachedStreamSPI available for external overview!";
                SourceSPIProvider sourceSpiProvider =
                        ovrProvider.getSourceSpiProvider().getCompatibleSourceProvider(granuleURLUpdated);
                inStream = sourceSpiProvider.getStream();
                // get a reader and try to cache the relevant SPI
                reader = sourceSpiProvider.getReader();
                if (reader == null) {
                    if (LOGGER.isLoggable(java.util.logging.Level.WARNING)) {
                        LOGGER.warning(new StringBuilder("Unable to get s reader for granuleDescriptor ")
                                .append(this.toString())
                                .append(" with request ")
                                .append(request.toString())
                                .append(" Resulting in no granule loaded: Empty result")
                                .toString());
                    }
                    return null;
                }
                // set input
                boolean ignoreMetadata = false;
                if (reader instanceof InitializingReader) {
                    ignoreMetadata = ((InitializingReader) reader).init(hints);
                }
                reader.setInput(inStream, false, ignoreMetadata);
                // External Overview index
                ovrIndex = ovrProvider.getOverviewIndex(imageIndex);

            } else {
                ovrIndex = ovrProvider.getOverviewIndex(imageIndex);

                // get a stream from the granuleAccessProvider
                assert cachedStreamSPI != null : "no cachedStreamSPI available!";
                inStream = granuleAccessProvider.getImageInputStream();

                if (inStream == null) return null;

                // get a reader and try to cache the relevant SPI
                if (cachedReaderSPI == null) {
                    reader = ImageIOExt.getImageioReader(inStream);
                    if (reader != null) cachedReaderSPI = reader.getOriginatingProvider();
                } else {
                    reader = granuleAccessProvider.getImageReader();
                }
                if (reader == null) {
                    if (LOGGER.isLoggable(java.util.logging.Level.WARNING)) {
                        LOGGER.warning(new StringBuilder("Unable to get a reader for granuleDescriptor ")
                                .append(this.toString())
                                .append(" with request ")
                                .append(request.toString())
                                .append(" Resulting in no granule loaded: Empty result")
                                .toString());
                    }
                    return null;
                }
            }
            // set input
            if (reader instanceof InitializingReader) {
                ((InitializingReader) reader).init(hints);
            }
            reader.setInput(inStream);

            // check if the reader wants to be aware of the current request
            if (MethodUtils.getAccessibleMethod(reader.getClass(), "setRasterLayerRequest", RasterLayerRequest.class)
                    != null) {
                try {
                    MethodUtils.invokeMethod(reader, "setRasterLayerRequest", request);
                } catch (Exception exception) {
                    throw new RuntimeException("Error setting raster layer request on reader.", exception);
                }
            }

            // get selected level and base level dimensions
            final GranuleOverviewLevelDescriptor selectedlevel = getLevel(ovrIndex, reader, imageIndex, isExternal);

            // now create the crop grid to world which can be used to decide
            // which source area we need to crop in the selected level taking
            // into account the scale factors imposed by the selection of this
            // level together with the base level grid to world transformation
            AffineTransform2D cropGridToWorld = new AffineTransform2D(selectedlevel.gridToWorldTransformCorner);
            AffineTransform2D cropWorldToGrid = (AffineTransform2D) cropGridToWorld.inverse();
            // computing the crop source area which lives into the
            // selected level raster space, NOTICE that at the end we need to
            // take into account the fact that we might also decimate therefore
            // we cannot just use the crop grid to world but we need to correct
            // it.
            Rectangle2D r2d = CRS.transform(cropWorldToGrid, intersection).toRectangle2D();
            // if we are reading basically nothing, bail out immediately
            if (r2d.getWidth() < READ_THRESHOLD || r2d.getHeight() < READ_THRESHOLD) {
                cleanupInFinally = true;
                return null;
            }
            final Rectangle sourceArea = r2d.getBounds();
            // gutter
            if (selectedlevel.baseToLevelTransform.isIdentity()) {
                sourceArea.grow(2, 2);
            }
            XRectangle2D.intersect(
                    sourceArea, selectedlevel.rasterDimensions, sourceArea); // make sure roundings don't bother us
            // is it empty??
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
                        + granuleUrl);
            }

            // Setting subsampling
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
                    readParameters.setSourceSubsampling(newSubSamplingFactor, newSubSamplingFactor, 0, 0);
                }
            }

            // set the source region
            readParameters.setSourceRegion(sourceArea);

            // don't pass down the band selection if the original color model is indexed and
            // color expansion is enabled
            final boolean expandToRGB = request.getRasterManager().isExpandMe();
            if (expandToRGB
                    && getRawColorModel(reader, ovrIndex) instanceof IndexColorModel
                    && readParameters instanceof EnhancedImageReadParam) {
                EnhancedImageReadParam erp = (EnhancedImageReadParam) readParameters;
                erp.setBands(null);
            }

            // deferred loading needs a target image type to work, otherwise it's going
            // to use the native image number of bands
            if (nativeBandSelection
                    && readParameters instanceof EnhancedImageReadParam
                    && ((EnhancedImageReadParam) readParameters).getBands() != null
                    && request.getReadType() == ReadType.JAI_IMAGEREAD) {
                int[] bands = ((EnhancedImageReadParam) readParameters).getBands();
                ImageTypeSpecifier selected = ImageIOUtilities.getBandSelectedType(bands.length, sampleModel);
                readParameters.setDestinationType(selected);
            }

            RenderedImage raster;
            try {
                // read
                raster = request.getReadType()
                        .read(
                                readParameters,
                                ovrIndex,
                                granuleURLUpdated,
                                selectedlevel.rasterDimensions,
                                reader,
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

            // perform band selection if necessary, so far netcdf is the only low level reader that
            // handles bands selection, if more readers start to support it a decent approach should
            // be used to know if the low level reader already performed the bands selection or if
            // image mosaic is responsible for do it
            int[] bands = request.getBands();
            if (bands != null && (!nativeBandSelection || expandToRGB)) {
                raster = selectBands(hints, expandToRGB, raster, bands);
            }

            // apply rescaling
            if (request.isRescalingEnabled()) {
                raster = rescaleRaster(request, hints, raster, bands);
            }

            if (virtualNativeResolution != null
                    && !Double.isNaN(virtualNativeResolution[0])
                    && !Double.isNaN(virtualNativeResolution[1])) {
                // Note that virtualNativeResolution may have been set to NaN by overviewController
                raster = forceVirtualNativeResolution(raster, request, virtualNativeResolution, selectedlevel, hints);
            }

            // use fixed source area
            sourceArea.setRect(readParameters.getSourceRegion());

            final AffineTransform finalRaster2Model = setupRaster2Model(selectedlevel, sourceArea, raster);

            // adjust roi
            if (useFootprint) {
                try {
                    // Getting Image Bounds
                    Rectangle imgBounds =
                            new Rectangle(raster.getMinX(), raster.getMinY(), raster.getWidth(), raster.getHeight());
                    // Getting Transformed ROI
                    ROI transformed = roiProvider.getTransformedROI(
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
                            LOGGER.fine("Unable to create a granuleDescriptor "
                                    + this.toString()
                                    + " due to a problem when managing the ROI");
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
                        LOGGER.info("Unable to create a granuleDescriptor "
                                + this.toString()
                                + " due to a problem when managing the ROI");
                    return null;
                }
            }
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
                if (LOGGER.isLoggable(java.util.logging.Level.FINE))
                    LOGGER.fine("Unable to create a granuleDescriptor "
                            + this.toString()
                            + " due to jai scale bug creating a null source area");
                return null;
            }

            // apply the affine transform conserving indexed color model
            final RenderingHints localHints = new RenderingHints(
                    JAI.KEY_REPLACE_INDEX_COLOR_MODEL,
                    interpolation instanceof InterpolationNearest ? Boolean.FALSE : Boolean.TRUE);
            if (XAffineTransform.isIdentity(finalRaster2Model, CoverageUtilities.AFFINE_IDENTITY_EPS)) {
                if (noData != null) {
                    PlanarImage t = PlanarImage.wrapRenderedImage(raster);
                    t.setProperty(NoDataContainer.GC_NODATA, noData);
                    raster = t;
                }
                return new GranuleLoadingResult(raster, null, granuleURLUpdated, doFiltering, pamDataset, this);
            } else {
                RenderedImage image = loadTiled(
                        request,
                        hints,
                        virtualNativeResolution,
                        useFootprint,
                        raster,
                        finalRaster2Model,
                        interpolation,
                        localHints);
                if (image == null) return null;
                return new GranuleLoadingResult(image, null, granuleURLUpdated, doFiltering, pamDataset, this);
            }

        } catch (org.geotools.api.referencing.operation.NoninvertibleTransformException e) {
            if (LOGGER.isLoggable(java.util.logging.Level.WARNING)) {
                LOGGER.log(
                        Level.WARNING,
                        "Unable to load raster for granuleDescriptor "
                                + this
                                + " with request "
                                + request
                                + " Resulting in no granule loaded: Empty result",
                        e);
            }
            return null;
        } catch (IllegalStateException | TransformException e) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.log(
                        Level.WARNING,
                        "Unable to load raster for granuleDescriptor "
                                + this
                                + " with request "
                                + request
                                + " Resulting in no granule loaded: Empty result",
                        e);
            }
            return null;
        } finally {
            try {
                if (cleanupInFinally && inStream != null) {
                    inStream.close();
                }
            } finally {
                if (cleanupInFinally && reader != null) {
                    reader.dispose();
                }
            }
        }
    }

    /**
     * In case we are asked to use certain tile dimensions we tile also at this stage in case the read type is Direct
     * since buffered images comes up untiled and this can affect the performances of the subsequent affine operation.
     */
    private RenderedImage loadTiled(
            RasterLayerRequest request,
            Hints hints,
            double[] virtualNativeResolution,
            boolean useFootprint,
            RenderedImage raster,
            AffineTransform finalRaster2Model,
            Interpolation interpolation,
            RenderingHints localHints) {
        final Dimension tileDimensions = request.getTileDimensions();
        ImageLayout layout = null;
        if (tileDimensions != null && request.getReadType().equals(ReadType.DIRECT_READ)) {
            layout = new ImageLayout();
            layout.setTileHeight(tileDimensions.width).setTileWidth(tileDimensions.height);
        } else {
            layout = Utils.getImageLayoutHint(hints);
        }
        // make sure the tiling is not going to make the image size balloon out of control
        if (layout != null) {
            layout.setTileWidth(Math.min(layout.getTileWidth(null), raster.getWidth()));
            layout.setTileHeight(Math.min(layout.getTileHeight(null), raster.getHeight()));
            localHints.add(new RenderingHints(JAI.KEY_IMAGE_LAYOUT, layout));
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

        ImageWorker iw = new ImageWorker(raster);
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
        RenderedImage renderedImage = iw.getRenderedImage();
        Object roi = renderedImage.getProperty("ROI");
        if (useFootprint
                        && roi instanceof ROIGeometry
                        && ((ROIGeometry) roi).getAsGeometry().isEmpty()
                || roi instanceof ROI && ((ROI) roi).getBounds().isEmpty()) {
            // JAI not only transforms the ROI, but may also apply clipping to the image
            // boundary.  this results in an empty ROI in some edge cases
            return null;
        }
        // Propagate NoData
        if (iw.getNoData() != null) {
            PlanarImage t = PlanarImage.wrapRenderedImage(renderedImage);
            t.setProperty(NoDataContainer.GC_NODATA, new NoDataContainer(iw.getNoData()));
            renderedImage = t;
        } else if (this.noData != null) {
            // on deferred loading we cannot get the noData from the image, but we might
            // have read it at the beginning
            PlanarImage t = PlanarImage.wrapRenderedImage(renderedImage);
            t.setProperty(NoDataContainer.GC_NODATA, noData);
            renderedImage = t;
        }
        return renderedImage;
    }

    private AffineTransform setupRaster2Model(
            GranuleOverviewLevelDescriptor selectedlevel, Rectangle sourceArea, RenderedImage raster) {
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

        // now we need to go back to the base level raster space
        final AffineTransform backToBaseLevelScaleTransform = selectedlevel.baseToLevelTransform;

        // now create the overall transform
        final AffineTransform finalRaster2Model = new AffineTransform(baseGridToWorld);
        finalRaster2Model.concatenate(CoverageUtilities.CENTER_TO_CORNER);

        if (!XAffineTransform.isIdentity(backToBaseLevelScaleTransform, CoverageUtilities.AFFINE_IDENTITY_EPS))
            finalRaster2Model.concatenate(backToBaseLevelScaleTransform);
        if (!XAffineTransform.isIdentity(afterDecimationTranslateTranform, CoverageUtilities.AFFINE_IDENTITY_EPS))
            finalRaster2Model.concatenate(afterDecimationTranslateTranform);
        if (!XAffineTransform.isIdentity(decimationScaleTranform, CoverageUtilities.AFFINE_IDENTITY_EPS))
            finalRaster2Model.concatenate(decimationScaleTranform);
        return finalRaster2Model;
    }

    private RenderedImage rescaleRaster(RasterLayerRequest request, Hints hints, RenderedImage raster, int[] bands) {
        if (noData != null && request.getReadType() == ReadType.JAI_IMAGEREAD) {
            // Force nodata settings since JAI ImageRead may lost that
            // We have to make sure that noData pixels won't be rescaled
            PlanarImage t = PlanarImage.wrapRenderedImage(raster);
            t.setProperty(NoDataContainer.GC_NODATA, noData);
            raster = t;
        }

        raster = rescale(raster, hints, bands);
        return raster;
    }

    private RenderedImage selectBands(Hints hints, boolean expandToRGB, RenderedImage raster, int[] bands) {
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
            ColorModel newColorModel = ImageIOUtilities.createColorModel(raster.getSampleModel());
            if (newColorModel != null) {
                layout.setColorModel(newColorModel);
                raster = new ImageWorker(raster)
                        .setRenderingHints(hints)
                        .format(raster.getSampleModel().getDataType())
                        .getRenderedImage();
            }
        }
        return raster;
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
        final AffineTransform virtualTransform = XAffineTransform.getScaleInstance(
                XAffineTransform.getScaleX0(selectedlevel.gridToWorldTransformCorner) / virtualNativeResolution[0],
                XAffineTransform.getScaleY0(selectedlevel.gridToWorldTransformCorner) / virtualNativeResolution[1]);

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
        return worker.affine(virtualTransform, request.getInterpolation(), request.getBackgroundValues())
                .getRenderedImage();
    }

    private void updateLocalHints(Hints hints, RenderingHints localHints) {
        final TileCache cache = Utils.getTileCacheHint(hints);
        if (cache != null) localHints.add(new RenderingHints(JAI.KEY_TILE_CACHE, cache));
        final TileScheduler scheduler = Utils.getTileSchedulerHint(hints);
        if (scheduler != null) localHints.add(new RenderingHints(JAI.KEY_TILE_SCHEDULER, scheduler));
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
            final int index, final ImageReader reader, final int imageIndex, final boolean external) {
        // Level index may change if using external overviews
        int indexValue = external ? imageIndex : index;
        if (reader == null)
            throw new NullPointerException("Null reader passed to the internal GranuleOverviewLevelDescriptor method");
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
                    final GranuleOverviewLevelDescriptor newLevel = new GranuleOverviewLevelDescriptor(
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
        try (ImageInputStream inStream = cachedStreamSPI.createInputStreamInstance(
                granuleUrl, ImageIO.getUseCache(), ImageIO.getCacheDirectory())) {

            // get a stream
            assert cachedStreamSPI != null : "no cachedStreamSPI available!";

            if (inStream == null)
                throw new IllegalArgumentException("Unable to create an inputstream for the granuleurl:"
                        + (granuleUrl != null ? granuleUrl : "null"));

            // get a reader and try to cache the relevant SPI
            if (cachedReaderSPI == null) {
                reader = ImageIOExt.getImageioReader(inStream);
                if (reader != null) cachedReaderSPI = reader.getOriginatingProvider();
            } else reader = cachedReaderSPI.createReaderInstance();
            if (reader == null)
                throw new IllegalArgumentException(
                        "Unable to get an ImageReader for the provided file " + granuleUrl.toString());

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
        for (final GranuleOverviewLevelDescriptor granuleOverviewLevelDescriptor : granuleLevels.values()) {
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
     * Returns a new instance of the AbstractGridCoverage2DReader associated with this descriptor. It's the
     * responsibility of the caller to dispose of it.
     */
    public AbstractGridCoverage2DReader getReader() {
        // COG readers need special treament
        if (granuleAccessProvider instanceof CogGranuleAccessProvider) {
            try {
                return granuleAccessProvider.getGridCoverageReader();
            } catch (IOException e) {
                throw new RuntimeException("Unable to access the reader", e);
            }
        }

        // for everything else we can use direct lookup via URL
        return this.format.getReader(granuleUrl, hints);
    }

    /** @return */
    public MultiLevelROI getRoiProvider() {
        return this.roiProvider;
    }

    public MaskOverviewProvider getMaskOverviewProvider() {
        return ovrProvider;
    }
}
