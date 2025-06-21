/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.grid.io.imageio;

import it.geosolutions.imageio.core.SourceSPIProvider;
import it.geosolutions.imageio.maskband.DatasetLayout;
import it.geosolutions.imageio.utilities.ImageIOUtilities;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.spi.ImageInputStreamSpi;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.stream.ImageInputStream;
import javax.media.jai.ROI;
import org.geotools.image.ImageWorker;
import org.geotools.image.io.ImageIOExt;
import org.geotools.util.URLs;

/**
 * Helper class used for handling Internal/External overviews and masks for a File
 *
 * @author Nicola Lagomarsini GeoSolutions
 */
public class MaskOverviewProvider {

    private static final boolean DEFAULT_SKIP_EXTERNAL_FILES_LOOKUP = ImageIOUtilities.isSkipExternalFilesLookup();

    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(MaskOverviewProvider.class);

    public static final String OVR_EXTENSION = ".ovr";

    private ImageReaderSpi overviewReaderSpi;

    private ImageInputStreamSpi overviewStreamSpi;

    private SourceSPIProvider sourceSpiProvider;

    private DatasetLayout layout;

    private URL fileURL;

    private URL ovrURL;

    private int numOverviews;

    private final boolean hasDatasetLayout;

    private int numInternalOverviews;

    private int numExternalOverviews;

    private int numInternalMasks;

    private int numExternalMasks;

    private int numExternalMasksOverviews;

    private boolean hasExternalMasks;

    private boolean hasExternalMasksOverviews;

    private URL maskURL;

    private ImageInputStreamSpi maskStreamSpi;

    private ImageReaderSpi maskReaderSpi;

    private URL maskOvrURL;

    private ImageInputStreamSpi maskOvrStreamSpi;

    private ImageReaderSpi maskOvrReaderSpi;

    private boolean skipExternalLookup;

    public MaskOverviewProvider(DatasetLayout layout, File inputFile) throws IOException {
        this(layout, inputFile, (ImageReaderSpi) null);
    }

    public MaskOverviewProvider(DatasetLayout layout, File inputFile, ImageReaderSpi suggestedSPI) throws IOException {
        this(layout, inputFile, new SpiHelper(URLs.fileToUrl(inputFile), suggestedSPI));
    }

    public MaskOverviewProvider(DatasetLayout layout, URL inputUrl) throws IOException {
        this(layout, inputUrl, (ImageReaderSpi) null);
    }

    public MaskOverviewProvider(DatasetLayout layout, URL inputUrl, ImageReaderSpi suggestedSPI) throws IOException {
        this(layout, inputUrl, new SpiHelper(inputUrl, suggestedSPI), DEFAULT_SKIP_EXTERNAL_FILES_LOOKUP);
    }

    public MaskOverviewProvider(DatasetLayout layout, File inputFile, SpiHelper spiHelper) throws IOException {
        this(layout, URLs.fileToUrl(inputFile), spiHelper, DEFAULT_SKIP_EXTERNAL_FILES_LOOKUP);
    }

    public MaskOverviewProvider(DatasetLayout layout, URL inputUrl, SpiHelper spiHelper) throws IOException {
        this(layout, inputUrl, spiHelper, DEFAULT_SKIP_EXTERNAL_FILES_LOOKUP);
    }

    public MaskOverviewProvider(DatasetLayout dtLayout, File inputFile, boolean skipOverviews) throws IOException {
        this(dtLayout, URLs.fileToUrl(inputFile), new SpiHelper(URLs.fileToUrl(inputFile), null), skipOverviews);
    }

    public MaskOverviewProvider(DatasetLayout layout, URL inputFile, SpiHelper spiHelper, boolean skipExternalLookup)
            throws IOException {

        sourceSpiProvider = spiHelper.getSourceSpiProvider();
        ImageReaderSpi readerSpi = sourceSpiProvider.getReaderSpi();
        ImageInputStreamSpi streamSpi = sourceSpiProvider.getStreamSpi();
        this.fileURL = sourceSpiProvider.getSourceUrl();
        this.layout = layout;
        this.skipExternalLookup = skipExternalLookup;

        // Handling Overviews
        hasDatasetLayout = layout != null;
        boolean hasExternalOverviews = false;
        SourceSPIProvider ovrProvider = null;

        // Getting number of Overviews
        if (hasDatasetLayout) {
            numInternalOverviews = layout.getNumInternalOverviews();
            // layout.getNumExternalOverviews() may return -1 when no external file is present
            if (!skipExternalLookup) {
                numExternalOverviews = layout.getNumExternalOverviews() > 0 ? layout.getNumExternalOverviews() : 0;
                hasExternalOverviews = externalOverviewsCheck(inputFile, streamSpi, readerSpi) != null;
            }
        } else if (!spiHelper.isMultidim()) {
            // Reading image number
            numInternalOverviews = getNumOverviews(sourceSpiProvider);

            // Let's look for external overviews only if no internal overviews are found
            if (numInternalOverviews == 0 && !skipExternalLookup) {
                ovrProvider = externalOverviewsCheck(inputFile, streamSpi, readerSpi);
                hasExternalOverviews = ovrProvider != null;
            }

            numExternalOverviews = 0;
            if (hasExternalOverviews) {
                // adding +1 since the base level of the external overview is an overview in itself
                numExternalOverviews = ovrProvider != null
                        ? getNumOverviews(ovrProvider) + 1
                        : getNumOverviews(ovrURL, this.overviewStreamSpi, this.overviewReaderSpi) + 1;
            }
        }
        int numOverviews = numInternalOverviews + numExternalOverviews;
        if (numOverviews < 0) {
            numOverviews = 0;
        }
        // Setting overviews Number
        this.numOverviews = numOverviews;

        // Mask Management
        if (layout != null) {
            numInternalMasks = layout.getNumInternalMasks();
            numExternalMasks = layout.getNumExternalMasks() > 0 ? layout.getNumExternalMasks() : 0;
            numExternalMasksOverviews =
                    layout.getNumExternalMaskOverviews() > 0 ? layout.getNumExternalMaskOverviews() : 0;
            hasExternalMasks = numExternalMasks > 0;
            hasExternalMasksOverviews = hasExternalMasks && numExternalMasksOverviews > 0;
            if (hasExternalMasks) {
                // Mask URL
                maskURL = URLs.fileToUrl(layout.getExternalMasks());
                // Creating cached SPIs
                maskStreamSpi = getInputStreamSPIFromURL(maskURL);

                try (ImageInputStream maskStream = maskStreamSpi.createInputStreamInstance(
                        maskURL, ImageIO.getUseCache(), ImageIO.getCacheDirectory())) {
                    maskReaderSpi = getReaderSpiFromStream(readerSpi, maskStream);
                } catch (Exception e) {
                    if (LOGGER.isLoggable(Level.WARNING)) {
                        LOGGER.log(Level.WARNING, "Unable to create a Reader for File: " + maskURL, e);
                    }
                    throw new IllegalArgumentException(e);
                }
                // Handling external mask overviews
                if (hasExternalMasksOverviews) {
                    // Mask URL
                    maskOvrURL = URLs.fileToUrl(layout.getExternalMaskOverviews());
                    // Creating cached SPIs
                    maskOvrStreamSpi = getInputStreamSPIFromURL(maskOvrURL);
                    try (ImageInputStream maskOvrStream = maskOvrStreamSpi.createInputStreamInstance(
                            maskOvrURL, ImageIO.getUseCache(), ImageIO.getCacheDirectory())) {
                        maskOvrReaderSpi = getReaderSpiFromStream(readerSpi, maskOvrStream);
                    } catch (Exception e) {
                        if (LOGGER.isLoggable(Level.WARNING)) {
                            LOGGER.log(Level.WARNING, "Unable to create a Reader for File: " + maskOvrURL, e);
                        }
                        throw new IllegalArgumentException(e);
                    }
                } else {
                    // No Mask Overview file so we fall back to the original mask spis
                    maskOvrStreamSpi = maskStreamSpi;
                    maskOvrReaderSpi = maskReaderSpi;
                }
            } else {
                // No Mask file so we fall back to the original file spis
                maskStreamSpi = streamSpi;
                maskReaderSpi = readerSpi;
            }
        }
    }

    private SourceSPIProvider externalOverviewsCheck(
            URL inputFile, ImageInputStreamSpi streamSpi, ImageReaderSpi readerSpi) throws IOException {
        ovrURL = new URL(inputFile.toString() + OVR_EXTENSION);
        if (hasDatasetLayout && layout.getExternalOverviews() != null) {
            ovrURL = URLs.fileToUrl(layout.getExternalOverviews());
        }
        // Creating overview file URL
        overviewStreamSpi = streamSpi == null ? getInputStreamSPIFromURL(ovrURL) : streamSpi;
        SourceSPIProvider ovrProvider = sourceSpiProvider.getCompatibleSourceProvider(ovrURL);
        try (ImageInputStream ovrStream = ovrProvider.getStream()) {
            if (ovrStream == null) {
                // No Overview file so we fall back to the original file spis
                overviewStreamSpi = streamSpi;
                overviewReaderSpi = readerSpi;
                ovrProvider = null;
            } else {
                overviewReaderSpi = getReaderSpiFromStream(null, ovrStream);
                ovrProvider.setReaderSpi(overviewReaderSpi);
            }
        } catch (Exception e) {
            // COG readers might throw an exception while creating the stream, as they do I/O
            // to fill header. So tolerate the exception and return no overview file found.
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "Unable to create a reader for overview: " + ovrURL, e);
            }
            overviewStreamSpi = streamSpi;
            overviewReaderSpi = readerSpi;
            this.ovrURL = null;
            return null;
        }
        return ovrProvider;
    }

    public int getNumOverviews(SourceSPIProvider sourceSpiProvider) {
        ImageReader reader = null;
        int numOverviews;
        try (ImageInputStream imageStream = sourceSpiProvider.getStream()) {
            // Creating reader
            reader = sourceSpiProvider.getReader();
            // Setting input
            reader.setInput(imageStream, false, false);
            // Getting number of images
            numOverviews = reader.getNumImages(true) - 1;
        } catch (Exception e) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.log(Level.WARNING, "Unable to create a Reader for: " + sourceSpiProvider.getSource(), e);
            }
            throw new IllegalArgumentException(e);
        } finally {
            if (reader != null) {
                reader.dispose();
            }
        }
        return numOverviews;
    }

    public int getNumOverviews(URL inputFile, ImageInputStreamSpi streamSpi, ImageReaderSpi readerSpi) {
        ImageReader reader = null;
        int numOverviews;
        try (ImageInputStream imageStream =
                streamSpi.createInputStreamInstance(inputFile, ImageIO.getUseCache(), ImageIO.getCacheDirectory())) {
            // Creating reader
            reader = readerSpi.createReaderInstance();
            // Setting input
            reader.setInput(imageStream, false, false);
            // Getting number of images
            numOverviews = reader.getNumImages(true) - 1;
        } catch (Exception e) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.log(Level.WARNING, "Unable to create a Reader for File: " + inputFile, e);
            }
            throw new IllegalArgumentException(e);
        } finally {
            if (reader != null) {
                reader.dispose();
            }
        }
        return numOverviews;
    }

    /** Returns the external/internal overview image index based on the initial imageindex value */
    public int getOverviewIndex(int imageIndex) {
        if (numExternalOverviews > 0 && imageIndex >= numInternalOverviews + 1) {
            return imageIndex - numInternalOverviews - 1;
        }
        if (layout != null) {
            return layout.getInternalOverviewImageIndex(imageIndex);
        }
        return imageIndex;
    }

    /**
     * Returns a new {@link MaskInfo} instance containing all the parameters to set for accessing the desired image
     * index
     */
    public MaskInfo getMaskInfo(int imageIndex, Rectangle imageBounds, ImageReadParam originalParams) {
        MaskInfo info = null;
        if (numInternalMasks + numExternalMasks > 0) {
            // Create a new MaskInfo instance
            info = new MaskInfo();
            // Parameter definiton
            ImageReadParam readParam = new ImageReadParam();
            readParam.setSourceSubsampling(
                    originalParams.getSourceXSubsampling(),
                    originalParams.getSourceYSubsampling(),
                    originalParams.getSubsamplingXOffset(),
                    originalParams.getSubsamplingYOffset());

            Rectangle sourceRegion = imageBounds;
            if (originalParams.getSourceRegion() != null) {
                sourceRegion = originalParams.getSourceRegion();
            }
            readParam.setSourceRegion(sourceRegion);
            info.readParameters = readParam;
            // Checks on the overviews
            if (imageIndex > 0) {
                // Check if the ImageChoice is contained inside internal or external masks
                if (imageIndex < numInternalMasks) {
                    info.file = URLs.urlToFile(fileURL);
                    info.readerSpi = sourceSpiProvider.getReaderSpi();
                    info.streamSpi = sourceSpiProvider.getStreamSpi();
                    info.index = layout.getInternalMaskImageIndex(imageIndex) - 1;
                } else if (hasExternalMasks) {
                    if (imageIndex < numExternalMasks) {
                        info.file = URLs.urlToFile(maskURL);
                        info.readerSpi = maskReaderSpi;
                        info.streamSpi = maskStreamSpi;
                        info.index = imageIndex;
                    } else if (imageIndex < numExternalMasks + numExternalMasksOverviews) {
                        info.file = URLs.urlToFile(maskOvrURL);
                        info.readerSpi = maskOvrReaderSpi;
                        info.streamSpi = maskOvrStreamSpi;
                        info.index = imageIndex - numExternalMasks;
                    } else {
                        // Read a bigger image
                        if (numExternalMasksOverviews > 0) {
                            // reading External Mask External Overviews
                            info.file = URLs.urlToFile(maskOvrURL);
                            info.readerSpi = maskOvrReaderSpi;
                            info.streamSpi = maskOvrStreamSpi;
                            info.index = numExternalMasksOverviews - 1;
                        } else {
                            // reading External Mask Overviews
                            info.file = URLs.urlToFile(maskURL);
                            info.readerSpi = maskReaderSpi;
                            info.streamSpi = maskStreamSpi;
                            info.index = numExternalMasks - 1;
                        }
                    }
                } else {
                    // Reading Internal Mask Overview
                    info.file = URLs.urlToFile(fileURL);
                    info.readerSpi = sourceSpiProvider.getReaderSpi();
                    info.streamSpi = sourceSpiProvider.getStreamSpi();
                    info.index = numInternalMasks - 1;
                }
                // Checks on the native image data
            } else if (imageIndex == 0) {
                // Checking for external Masks
                if (numInternalMasks == 0 && hasExternalMasks) {
                    // reading External Mask Overviews
                    info.file = URLs.urlToFile(maskURL);
                    info.readerSpi = maskReaderSpi;
                    info.streamSpi = maskStreamSpi;
                    info.index = 0;
                    // Check for internal Masks
                } else if (numInternalMasks > 0) {
                    // Reading Internal Mask Overview
                    info.file = URLs.urlToFile(fileURL);
                    info.readerSpi = sourceSpiProvider.getReaderSpi();
                    info.streamSpi = sourceSpiProvider.getStreamSpi();
                    info.index = layout.getInternalMaskImageIndex(0);
                }
            }
        }

        return info;
    }

    /** Returns true if there is a mask at the same resolution of the requested one */
    public boolean hasMaskIndexForOverview(int imageIndex) {
        // Checks on the overviews
        if (imageIndex > 0) {
            // Check if the ImageChoice is contained inside internal or external masks
            if (imageIndex < numInternalMasks) {
                return true;
            } else if (hasExternalMasks && imageIndex <= numExternalMasks + numExternalMasksOverviews - 1) {
                return true;
            }
            // Checks on the 0 level
        } else if (imageIndex == 0 && numInternalMasks > 0 || hasExternalMasks) {
            return true;
        }
        return false;
    }

    /** Returns true if the defined index is related to an external image overview index */
    public boolean isExternalOverview(int imageIndex) {
        if (numExternalOverviews <= 0) {
            return false;
        }
        return imageIndex > numInternalOverviews;
    }

    /** Returns true if the defined index is related to an external image mask index */
    public boolean isExternalMask(int imageIndex) {
        if (numExternalMasks <= 0) {
            return false;
        }
        return hasExternalMasks && imageIndex > (numInternalMasks > 0 ? numInternalMasks + 1 : 0);
    }

    /** Returns true if the defined index is related to an external image mask overview index */
    public boolean isExternalMaskOverviews(int imageIndex) {
        if (numExternalMasksOverviews <= 0) {
            return false;
        }
        return isExternalMask(imageIndex)
                && hasExternalMasksOverviews
                && imageIndex > (numInternalMasks > 0 ? numInternalMasks + numExternalMasks + 2 : numExternalMasks + 1);
    }

    public boolean hasExternalMasks() {
        return hasExternalMasks;
    }

    public boolean hasExternalMasksOverviews() {
        return hasExternalMasksOverviews;
    }

    /** Returns a double[][] containing the resolutions for all the overviews */
    // optional resources, readers not closeables, try-with-resources would not help much here
    @SuppressWarnings("PMD.UseTryWithResources")
    public double[][] getOverviewResolutions(double span0, double span1) {
        double[][] overviewsResolution = null;
        if (numOverviews > 0) {
            ImageInputStream stream = null;
            ImageInputStream streamOvr = null;
            ImageReader reader = null;
            ImageReader readerOvr = null;
            try {
                // Instantiating Stream
                stream = sourceSpiProvider.getStream();
                reader = sourceSpiProvider.getReader();
                reader.setInput(stream, false, false);
                if (ovrURL != null) {
                    SourceSPIProvider ovrProvider = sourceSpiProvider.getCompatibleSourceProvider(ovrURL);
                    streamOvr = ovrProvider.getStream();
                    readerOvr = overviewReaderSpi.createReaderInstance();
                    readerOvr.setInput(streamOvr, false, false);
                }

                overviewsResolution = new double[numOverviews][2];
                // Populating overviews
                for (int i = 0; i < numOverviews; i++) {
                    // Handling internal and external overviews
                    if (numExternalOverviews > 0 && i >= numInternalOverviews) {
                        int index = i - numInternalOverviews;
                        overviewsResolution[i][0] = span0 / readerOvr.getWidth(index);
                        overviewsResolution[i][1] = span1 / readerOvr.getHeight(index);
                    } else {
                        int index = hasDatasetLayout ? layout.getInternalOverviewImageIndex(i + 1) : i + 1;
                        overviewsResolution[i][0] = span0 / reader.getWidth(index);
                        overviewsResolution[i][1] = span1 / reader.getHeight(index);
                    }
                }
            } catch (Exception e) {
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.log(Level.WARNING, "Unable to create a Reader for File: " + fileURL, e);
                }
                throw new IllegalArgumentException(e);
            } finally {
                // Closing stream and readers
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (Exception e) {
                        if (LOGGER.isLoggable(Level.SEVERE)) {
                            LOGGER.log(Level.SEVERE, e.getMessage(), e);
                        }
                    } finally {
                        if (reader != null) {
                            reader.dispose();
                        }
                    }
                }
                if (streamOvr != null) {
                    try {
                        streamOvr.close();
                    } catch (Exception e) {
                        if (LOGGER.isLoggable(Level.SEVERE)) {
                            LOGGER.log(Level.SEVERE, e.getMessage(), e);
                        }
                    } finally {
                        if (readerOvr != null) {
                            readerOvr.dispose();
                        }
                    }
                }
            }
        }
        return overviewsResolution;
    }

    public ImageReaderSpi getExternalOverviewReaderSpi() {
        return overviewReaderSpi;
    }

    public ImageReaderSpi getImageReaderSpi() {
        return sourceSpiProvider.getReaderSpi();
    }

    public ImageInputStreamSpi getExternalOverviewInputStreamSpi() {
        return overviewStreamSpi;
    }

    public ImageInputStreamSpi getInputStreamSpi() {
        return sourceSpiProvider.getStreamSpi();
    }

    public SourceSPIProvider getSourceSpiProvider() {
        return sourceSpiProvider;
    }

    public ImageInputStreamSpi getMaskStreamSpi() {
        return maskStreamSpi;
    }

    public ImageReaderSpi getMaskReaderSpi() {
        return maskReaderSpi;
    }

    public ImageInputStreamSpi getMaskOvrStreamSpi() {
        return maskOvrStreamSpi;
    }

    public ImageReaderSpi getMaskOvrReaderSpi() {
        return maskOvrReaderSpi;
    }

    public DatasetLayout getLayout() {
        return layout;
    }

    public int getNumOverviews() {
        return numOverviews;
    }

    public int getNumInternalOverviews() {
        return numInternalOverviews;
    }

    public int getNumExternalOverviews() {
        return numExternalOverviews;
    }

    public int getNumInternalMasks() {
        return numInternalMasks;
    }

    public int getNumExternalMasks() {
        return numExternalMasks;
    }

    public int getNumExternalMasksOverviews() {
        return numExternalMasksOverviews;
    }

    public URL getFileURL() {
        return fileURL;
    }

    public URL getOvrURL() {
        return ovrURL;
    }

    public URL getMaskURL() {
        return maskURL;
    }

    public URL getMaskOvrURL() {
        return maskOvrURL;
    }

    public boolean isSkipExternalLookup() {
        return skipExternalLookup;
    }

    /** Returns an {@link ImageInputStreamSpi} instance for the input {@link URL} */
    public static ImageInputStreamSpi getInputStreamSPIFromURL(URL fileURL) throws IOException {

        ImageInputStreamSpi streamSPI = ImageIOExt.getImageInputStreamSPI(fileURL, true);
        if (streamSPI == null) {
            final File file = URLs.urlToFile(fileURL);
            if (file != null) {
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.log(Level.WARNING, file.getCanonicalPath());
                }
            }
            throw new IllegalArgumentException(
                    "Unable to get an input stream for the provided source " + fileURL.toString());
        }
        return streamSPI;
    }

    /**
     * Returns an {@link ImageReaderSpi} instance for the input {@link ImageInputStream} and the optional suggested spi.
     */
    public static ImageReaderSpi getReaderSpiFromStream(ImageReaderSpi suggestedSPI, ImageInputStream inStream)
            throws IOException {
        ImageReaderSpi readerSPI = null;
        // get a reader and try to use the suggested SPI first
        inStream.mark();
        if (suggestedSPI != null && suggestedSPI.canDecodeInput(inStream)) {
            readerSPI = suggestedSPI;
            inStream.reset();
        } else {
            inStream.mark();
            ImageReader reader = ImageIOExt.getImageioReader(inStream);
            if (reader != null) readerSPI = reader.getOriginatingProvider();
            inStream.reset();
        }
        return readerSPI;
    }

    /** Returns a {@link ROI} object based on the input {@link RenderedImage} representing ROI */
    public static ROI scaleROI(RenderedImage roiRaster, Rectangle bounds) {
        if (roiRaster == null) {
            return null;
        }
        int x = bounds.x;
        int y = bounds.y;
        int w = bounds.width;
        int h = bounds.height;
        // Scale factors for input data
        final double scaleX = w / (1.0 * roiRaster.getWidth());
        final double scaleY = h / (1.0 * roiRaster.getHeight());
        AffineTransform tr = AffineTransform.getScaleInstance(scaleX, scaleY);
        // Translation Factors
        final int transX = x;
        final int transY = y;
        tr.concatenate(AffineTransform.getTranslateInstance(transX, transY));
        // Log the Scale/Translate operation
        if (!tr.isIdentity()) {
            LOGGER.fine("Scaling ROI");
        }
        // Input Mask is scaled to the image size, rescaled to Bytes and then used as ROI
        return new ImageWorker(roiRaster).affine(tr, null, null).binarize(1).getImageAsROI();
    }

    /**
     * Returns an {@link ImageInputStream} instance for the input {@link MaskInfo} instance.
     *
     * @param info the {@link MaskInfo} instance
     * @return the {@link ImageInputStream} instance
     * @throws IOException if an error occurs while creating the {@link ImageInputStream} instance
     */
    public ImageInputStream getMaskStream(MaskInfo info) throws IOException {
        if (info == null) return null;

        // Mask from local file system? (sidecar to the source file)
        if (info.file != null) {
            return info.streamSpi.createInputStreamInstance(
                    URLs.fileToUrl(info.file), ImageIO.getUseCache(), ImageIO.getCacheDirectory());
        }

        // external mask?
        if (isExternalMask(info.index)) {
            return getSourceSpiProvider()
                    .getCompatibleSourceProvider(getMaskURL())
                    .getStream();
        }

        // internal mask
        return getSourceSpiProvider().getStream();
    }

    /**
     * Helper class used for storing information to use for read the mask data.
     *
     * @author Nicola Lagomarsini GeoSolutions
     */
    public static class MaskInfo {

        public File file;

        public int index;

        public ImageReadParam readParameters;

        public ImageReaderSpi readerSpi;

        public ImageInputStreamSpi streamSpi;
    }

    /** Helper class containing previous code used to get streamSPI and readerSPI for an input file. */
    public static class SpiHelper {

        private static final Set<String> MULTIDIM_SERVICE_PROVIDERS;

        static {
            MULTIDIM_SERVICE_PROVIDERS = new HashSet<>();
            MULTIDIM_SERVICE_PROVIDERS.add("org.geotools.imageio.netcdf.NetCDFImageReaderSpi");
        }

        private URL fileURL;

        private SourceSPIProvider sourceSpiProvider;

        /**
         * Reporting whether the SPI is for a multidim reader or not. GRIB/NetCDF and other multidim format doesn't have
         * overviews
         */
        private boolean isMultidim;

        public SpiHelper(URL input, ImageReaderSpi suggestedSPI) throws IOException {
            this(input, suggestedSPI, null);
        }

        public SpiHelper(URL input, ImageReaderSpi suggestedSPI, ImageInputStreamSpi suggestedStreamSpi)
                throws IOException {

            this.fileURL = input;

            // Creating cached SPIs
            ImageInputStreamSpi streamSpi = null;
            ImageReaderSpi readerSpi = null;
            if (suggestedStreamSpi == null) {
                streamSpi = getInputStreamSPIFromURL(fileURL);
            } else {
                streamSpi = suggestedStreamSpi;
            }

            try (ImageInputStream stream =
                    streamSpi.createInputStreamInstance(fileURL, ImageIO.getUseCache(), ImageIO.getCacheDirectory())) {
                readerSpi = getReaderSpiFromStream(suggestedSPI, stream);
                isMultidim = readerSpi != null
                        && MULTIDIM_SERVICE_PROVIDERS.contains(
                                readerSpi.getClass().getName());
                sourceSpiProvider = new SourceSPIProvider(this.fileURL, readerSpi, streamSpi);
            } catch (Exception e) {
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.log(Level.WARNING, "Unable to create a Reader for File: " + input, e);
                }
                throw new IllegalArgumentException(e);
            }
        }

        public SpiHelper(SourceSPIProvider provider) throws IOException {
            this.sourceSpiProvider = provider;
        }

        public boolean isMultidim() {
            return isMultidim;
        }

        public SourceSPIProvider getSourceSpiProvider() {
            return sourceSpiProvider;
        }
    }
}
