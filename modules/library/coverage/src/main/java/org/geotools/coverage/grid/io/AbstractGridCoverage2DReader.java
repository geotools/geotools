/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005 - 2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.grid.io;

import it.geosolutions.imageio.core.CoreCommonImageMetadata;
import it.geosolutions.imageio.maskband.DatasetLayout;
import it.geosolutions.imageio.maskband.DefaultDatasetLayoutImpl;
import it.geosolutions.imageio.pam.PAMDataset;
import it.geosolutions.imageio.pam.PAMParser;
import it.geosolutions.imageio.plugins.tiff.TIFFField;
import it.geosolutions.imageio.utilities.ImageIOUtilities;
import it.geosolutions.imageioimpl.plugins.tiff.TIFFImageMetadata;
import it.geosolutions.imageioimpl.plugins.tiff.gdal.GDALMetadata;
import it.geosolutions.imageioimpl.plugins.tiff.gdal.GDALMetadataParser;
import it.geosolutions.jaiext.utilities.ImageLayout2;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.ColorModel;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.spi.ImageInputStreamSpi;
import javax.imageio.stream.ImageInputStream;
import javax.media.jai.ImageLayout;
import javax.media.jai.PlanarImage;
import org.geotools.api.coverage.ColorInterpretation;
import org.geotools.api.coverage.grid.Format;
import org.geotools.api.coverage.grid.GridCoverage;
import org.geotools.api.coverage.grid.GridEnvelope;
import org.geotools.api.data.DataSourceException;
import org.geotools.api.data.FileGroupProvider.FileGroup;
import org.geotools.api.data.ResourceInfo;
import org.geotools.api.data.ServiceInfo;
import org.geotools.api.parameter.GeneralParameterValue;
import org.geotools.api.parameter.InvalidParameterNameException;
import org.geotools.api.parameter.InvalidParameterValueException;
import org.geotools.api.parameter.ParameterDescriptor;
import org.geotools.api.parameter.ParameterNotFoundException;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.datum.PixelInCell;
import org.geotools.api.referencing.operation.CoordinateOperation;
import org.geotools.api.referencing.operation.CoordinateOperationFactory;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.coverage.CoverageFactoryFinder;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.TypeMap;
import org.geotools.coverage.grid.GeneralGridEnvelope;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.io.footprint.MultiLevelROIProvider;
import org.geotools.coverage.util.CoverageUtilities;
import org.geotools.data.DefaultFileResourceInfo;
import org.geotools.data.DefaultFileServiceInfo;
import org.geotools.data.DefaultResourceInfo;
import org.geotools.data.DefaultServiceInfo;
import org.geotools.geometry.GeneralBounds;
import org.geotools.image.util.ImageUtilities;
import org.geotools.metadata.i18n.ErrorKeys;
import org.geotools.referencing.CRS;
import org.geotools.referencing.operation.builder.GridToEnvelopeMapper;
import org.geotools.referencing.operation.matrix.XAffineTransform;
import org.geotools.referencing.operation.transform.IdentityTransform;
import org.geotools.referencing.operation.transform.ProjectiveTransform;
import org.geotools.util.URLs;
import org.geotools.util.Utilities;
import org.geotools.util.factory.GeoTools;
import org.geotools.util.factory.Hints;
import org.geotools.util.logging.Logging;

/**
 * This class is a first attempt for providing a way to get more informations out of a single 2D raster datasets (x,y).
 * It is worth to remark that for the moment this is thought for 2D rasters not for 3D or 4D rasters (x,y,z,t).
 *
 * <p>The main drawback I see with the current GeoApi GridCoverageReader interface is that there is no way to get real
 * information about a raster source unless you instantiate a GridCoverage. As an instance it is impossible to know the
 * envelope, the number of overviews, the tile size. This information is needed in order to perform decimation on
 * reading or to use built-in overviews<br>
 * This really impacts the ability to exploit raster datasets in a desktop environment where caching is crucial.
 *
 * @author Simone Giannecchini, GeoSolutions
 * @since 2.3
 */
public abstract class AbstractGridCoverage2DReader implements GridCoverage2DReader {

    /** The {@link Logger} for this {@link AbstractGridCoverage2DReader}. */
    private static final Logger LOGGER = Logging.getLogger(AbstractGridCoverage2DReader.class);

    /** Small number used for double comparisons */
    protected static double EPS = 1e-6;

    /**
     * Custom tag holding a XML that can be parsed into a
     * {@link it.geosolutions.imageioimpl.plugins.tiff.gdal.GDALMetadata}
     */
    static final int GDAL_METADATA_TAG = 42112;

    /** This contains the number of overviews.aaa */
    protected int numOverviews = 0;

    /** 2DGridToWorld math transform. */
    protected MathTransform raster2Model = null;

    /** crs for this coverage */
    protected CoordinateReferenceSystem crs = null;

    /** Envelope read from file */
    protected GeneralBounds originalEnvelope = null;

    /** Coverage name */
    protected String coverageName = "geotools_coverage";

    /** Source to read from */
    protected Object source = null;

    /** Hints used by the {@link AbstractGridCoverage2DReader} subclasses. */
    protected Hints hints = GeoTools.getDefaultHints();

    /** Highest resolution available for this reader. */
    protected double[] highestRes = null;

    /** Temp variable used in many readers. */
    protected boolean closeMe;

    /** In case we are trying to read from a GZipped file this will be set to true. */
    protected boolean gzipped;

    /** The original {@link GridRange} for the {@link GridCoverage2D} of this reader. */
    protected GridEnvelope originalGridRange = null;

    /** Input stream that can be used to initialize subclasses of {@link AbstractGridCoverage2DReader}. */
    protected ImageInputStream inStream = null;

    /** Resolutions avialaible through an overviews based mechanism. */
    protected double[][] overViewResolutions = null;

    /** {@link GridCoverageFactory} instance. */
    protected GridCoverageFactory coverageFactory;

    /** scales and offsets for rescaling */
    protected Double[] scales;

    protected Double[] offsets;

    private Map<String, ArrayList<Resolution>> resolutionsLevelsMap = new HashMap<>();

    protected ImageInputStreamSpi inStreamSPI;

    private ImageLayout imageLayout;

    /** Coverage {@link DatasetLayout} containing information about Overviews and Mask management */
    protected DatasetLayout dtLayout;

    /** Default protected constructor. Useful for wrappers. */
    protected AbstractGridCoverage2DReader() {}

    /**
     * Creates a new instance of a {@link AIGReader}. I assume nothing about file extension.
     *
     * @param input Source object for which we want to build an {@link AIGReader}.
     */
    public AbstractGridCoverage2DReader(Object input) throws DataSourceException {
        this(input, null);
    }

    /**
     * Creates a new instance of a {@link AIGReader}. I assume nothing about file extension.
     *
     * @param input Source object for which we want to build an {@link AIGReader}.
     * @param hints Hints to be used by this reader throughout his life.
     */
    public AbstractGridCoverage2DReader(Object input, Hints hints) throws DataSourceException {

        //
        // basic management of hints
        //
        if (hints == null) this.hints = new Hints();
        if (hints != null) {
            this.hints = hints.clone();
        }

        // GridCoverageFactory initialization
        if (this.hints.containsKey(Hints.GRID_COVERAGE_FACTORY)) {
            final Object factory = this.hints.get(Hints.GRID_COVERAGE_FACTORY);
            if (factory != null && factory instanceof GridCoverageFactory) {
                this.coverageFactory = (GridCoverageFactory) factory;
            }
        }
        if (this.coverageFactory == null) {
            this.coverageFactory = CoverageFactoryFinder.getGridCoverageFactory(this.hints);
        }

        //
        // Setting input
        //
        if (input == null) {
            final IOException ex = new IOException(MessageFormat.format(ErrorKeys.NULL_ARGUMENT_$1, "input"));
            throw new DataSourceException(ex);
        }
        this.source = input;
    }

    /**
     * This method is responsible for checking the provided coverage name against the coverage name for this
     * {@link GridCoverage2DReader}.
     *
     * @param coverageName the coverage name to check.
     * @return <code>true</code> if this {@link GridCoverage2DReader} contains the provided coverage name, <code>false
     *     </code> otherwise.
     */
    protected boolean checkName(String coverageName) {
        Utilities.ensureNonNull("coverageName", coverageName);
        return coverageName.equalsIgnoreCase(this.coverageName);
    }

    @Override
    public GridCoverage2D read(String coverageName, GeneralParameterValue... parameters)
            throws IllegalArgumentException, IOException {
        // Default implementation for backwards compatibility
        if (coverageName.equalsIgnoreCase(this.coverageName)) {
            return read(parameters);
        }
        // Subclasses should do more checks on coverageName
        throw new IllegalArgumentException("The specified coverageName " + coverageName + "is not supported");
    }

    /**
     * Read the current grid coverage from the stream.
     *
     * <p>Example:
     *
     * <pre>
     * <code>
     * </code>
     * </pre>
     *
     * @param parameters Optional parameters matching {@link Format#getReadParameters}.
     * @return a {@linkplain GridCoverage grid coverage} from the input source.
     * @throws InvalidParameterNameException if a parameter in {@code parameters} doesn't have a recognized name.
     * @throws InvalidParameterValueException if a parameter in {@code parameters} doesn't have a valid value.
     * @throws ParameterNotFoundException if a parameter was required for the operation but was not provided in the
     *     {@code parameters} list.
     * @throws CannotCreateGridCoverageException if the coverage can't be created for a logical reason (for example an
     *     unsupported format, or an inconsistency found in the data).
     * @throws IOException if a read operation failed for some other input/output reason, including
     *     {@link FileNotFoundException} if no file with the given {@code name} can be found, or
     *     {@link javax.imageio.IIOException} if an error was thrown by the underlying image library.
     */
    @Override
    public abstract GridCoverage2D read(GeneralParameterValue... parameters)
            throws IllegalArgumentException, IOException;

    // -------------------------------------------------------------------------
    //
    // old support methods
    //
    // -------------------------------------------------------------------------

    /**
     * This method is responsible for preparing the read param for doing an {@link ImageReader#read(int,
     * ImageReadParam)}.
     *
     * <p>This method is responsible for preparing the read param for doing an {@link ImageReader#read(int,
     * ImageReadParam)}. It sets the passed {@link ImageReadParam} in terms of decimation on reading using the provided
     * requestedEnvelope and requestedDim to evaluate the needed resolution. It also returns and {@link Integer}
     * representing the index of the raster to be read when dealing with multipage raster.
     *
     * @param overviewPolicy it can be one of {@link Hints#VALUE_OVERVIEW_POLICY_IGNORE},
     *     {@link Hints#VALUE_OVERVIEW_POLICY_NEAREST}, {@link Hints#VALUE_OVERVIEW_POLICY_QUALITY} or
     *     {@link Hints#VALUE_OVERVIEW_POLICY_SPEED}. It specifies the policy to compute the overviews level upon
     *     request.
     * @param readP an instance of {@link ImageReadParam} for setting the subsampling factors.
     * @param requestedEnvelope the {@link GeneralBounds} we are requesting.
     * @param requestedDim the requested dimensions.
     * @return the index of the raster to read in the underlying data source.
     */
    protected Integer setReadParams(
            OverviewPolicy overviewPolicy,
            ImageReadParam readP,
            GeneralBounds requestedEnvelope,
            Rectangle requestedDim)
            throws IOException, TransformException {
        return setReadParams(coverageName, overviewPolicy, readP, requestedEnvelope, requestedDim);
    }

    /**
     * This method is responsible for preparing the read param for doing an {@link ImageReader#read(int,
     * ImageReadParam)}.
     *
     * <p>This method is responsible for preparing the read param for doing an {@link ImageReader#read(int,
     * ImageReadParam)}. It sets the passed {@link ImageReadParam} in terms of decimation on reading using the provided
     * requestedEnvelope and requestedDim to evaluate the needed resolution. It also returns and {@link Integer}
     * representing the index of the raster to be read when dealing with multipage raster.
     *
     * @param overviewPolicy it can be one of {@link Hints#VALUE_OVERVIEW_POLICY_IGNORE},
     *     {@link Hints#VALUE_OVERVIEW_POLICY_NEAREST}, {@link Hints#VALUE_OVERVIEW_POLICY_QUALITY} or
     *     {@link Hints#VALUE_OVERVIEW_POLICY_SPEED}. It specifies the policy to compute the overviews level upon
     *     request.
     * @param readP an instance of {@link ImageReadParam} for setting the subsampling factors.
     * @param requestedEnvelope the {@link GeneralBounds} we are requesting.
     * @param requestedDim the requested dimensions.
     * @return the index of the raster to read in the underlying data source.
     */
    protected Integer setReadParams(
            String coverageName,
            OverviewPolicy overviewPolicy,
            ImageReadParam readP,
            GeneralBounds requestedEnvelope,
            Rectangle requestedDim)
            throws IOException, TransformException {

        // //
        //
        // Default image index 0
        //
        // //
        Integer imageChoice = Integer.valueOf(0);

        // //
        //
        // Init overview policy
        //
        // //
        // when policy is explictly provided it overrides the policy provided
        // using hints.
        if (overviewPolicy == null) overviewPolicy = extractOverviewPolicy();

        // //
        //
        // Resolution requested. I am here computing the resolution required by
        // the user.
        //
        double[] requestedRes =
                getResolution(requestedEnvelope, requestedDim, getCoordinateReferenceSystem(coverageName));

        // Decimation on reading (done here because it alters the read params allowing subsampling)
        decimationOnReadingControl(coverageName, imageChoice, readP, requestedRes);

        if (requestedRes == null) return imageChoice;

        // //
        //
        // requested to ignore overviews
        //
        // //
        if (overviewPolicy.equals(OverviewPolicy.IGNORE)) return imageChoice;

        // //
        //
        // Am I going to decimate or to use overviews? If this file has only
        // one page we use decimation, otherwise we use the best page available.
        // Future versions should use both.
        //
        // //
        final boolean useOverviews = (numOverviews > 0) ? true : false;

        // //
        //
        // overviews or decimation
        //
        // //
        if (useOverviews) {
            int newImageChoice = pickOverviewLevel(coverageName, overviewPolicy, requestedRes);

            // if image choice has changed due to using overviews, recalculate subsamping factors
            if (imageChoice != newImageChoice) {
                imageChoice = newImageChoice; // update
                // recalculate subsampling factors if overview number has changed
                decimationOnReadingControl(coverageName, imageChoice, readP, requestedRes);
            }
        }

        return imageChoice;
    }

    /**
     * This method is responsible for checking the overview policy as defined by the provided {@link Hints}.
     *
     * @return the overview policy which can be one of {@link Hints#VALUE_OVERVIEW_POLICY_IGNORE},
     *     {@link Hints#VALUE_OVERVIEW_POLICY_NEAREST}, {@link Hints#VALUE_OVERVIEW_POLICY_SPEED},
     *     {@link Hints#VALUE_OVERVIEW_POLICY_QUALITY}. Default is {@link Hints#VALUE_OVERVIEW_POLICY_NEAREST}.
     */
    private OverviewPolicy extractOverviewPolicy() {
        OverviewPolicy overviewPolicy = null;
        // check if a policy was provided using hints (check even the
        // deprecated one)
        if (this.hints != null)
            if (this.hints.containsKey(Hints.OVERVIEW_POLICY))
                overviewPolicy = (OverviewPolicy) this.hints.get(Hints.OVERVIEW_POLICY);

        // use default if not provided. Default is nearest
        if (overviewPolicy == null) overviewPolicy = OverviewPolicy.getDefaultPolicy();
        assert overviewPolicy != null;
        return overviewPolicy;
    }

    private Integer pickOverviewLevel(String coverageName, OverviewPolicy policy, double[] requestedRes) {
        // setup policy
        if (policy == null) policy = extractOverviewPolicy();

        ArrayList<Resolution> resolutionsLevels;

        // sort resolutions from smallest pixels (higher res) to biggest pixels (higher res)
        // keeping a reference to the original image choice
        synchronized (this) {
            resolutionsLevels = resolutionsLevelsMap.get(coverageName);
            if (resolutionsLevels == null) {
                resolutionsLevels = new ArrayList<>();
                resolutionsLevelsMap.put(coverageName, resolutionsLevels);
                // note that we assume what follows:
                // -highest resolution image is at level 0.
                // -all the overviews share the same envelope
                // -the aspect ratio for the overviews is constant
                // -the provided resolutions are taken directly from the grid
                resolutionsLevels.add(new Resolution(1, getHighestRes()[0], getHighestRes()[1], 0));
                if (numOverviews > 0) {
                    for (int i = 0; i < overViewResolutions.length; i++)
                        resolutionsLevels.add(new Resolution(
                                overViewResolutions[i][0] / getHighestRes()[0],
                                overViewResolutions[i][0],
                                overViewResolutions[i][1],
                                i + 1));
                    Collections.sort(resolutionsLevels);
                }
            }
        }

        // Now search for the best matching resolution.
        // Check also for the "perfect match"... unlikely in practice unless someone
        // tunes the clients to request exactly the resolution embedded in
        // the overviews, something a perf sensitive person might do in fact

        // the requested resolutions
        final double reqx = requestedRes[0];
        final double reqy = requestedRes[1];

        // requested scale factor for least reduced axis
        final Resolution max = resolutionsLevels.get(0);
        final double requestedScaleFactorX = reqx / max.resolutionX;
        final double requestedScaleFactorY = reqy / max.resolutionY;
        final int leastReduceAxis = requestedScaleFactorX <= requestedScaleFactorY ? 0 : 1;
        final double requestedScaleFactor = leastReduceAxis == 0 ? requestedScaleFactorX : requestedScaleFactorY;

        // are we looking for a resolution even higher than the native one?
        if (requestedScaleFactor <= 1) return max.imageChoice;
        // are we looking for a resolution even lower than the smallest overview?
        final Resolution min = resolutionsLevels.get(resolutionsLevels.size() - 1);
        if (requestedScaleFactor >= min.scaleFactor) return min.imageChoice;
        // Ok, so we know the overview is between min and max, skip the first
        // and search for an overview with a resolution lower than the one requested,
        // that one and the one from the previous step will bound the searched resolution
        Resolution prev = max;
        final int size = resolutionsLevels.size();
        for (int i = 1; i < size; i++) {
            final Resolution curr = resolutionsLevels.get(i);
            // perfect match check
            if (curr.scaleFactor == requestedScaleFactor) {
                return curr.imageChoice;
            }

            // middle check. The first part of the condition should be sufficient, but
            // there are cases where the x resolution is satisfied by the lowest resolution,
            // the y by the one before the lowest (so the aspect ratio of the request is
            // different than the one of the overviews), and we would end up going out of the loop
            // since not even the lowest can "top" the request for one axis
            if (curr.scaleFactor > requestedScaleFactor || i == size - 1) {
                if (policy == OverviewPolicy.QUALITY) return prev.imageChoice;
                else if (policy == OverviewPolicy.SPEED) return curr.imageChoice;
                else if (requestedScaleFactor - prev.scaleFactor < curr.scaleFactor - requestedScaleFactor)
                    return prev.imageChoice;
                else return curr.imageChoice;
            }
            prev = curr;
        }
        // fallback
        return max.imageChoice;
    }

    /**
     * Returns the actual resolution used to read the data given the specified target resolution and the specified
     * overview policy
     */
    @Override
    public double[] getReadingResolutions(OverviewPolicy policy, double[] requestedResolution) throws IOException {
        // Default implementation for backwards compatibility
        return getReadingResolutions(coverageName, policy, requestedResolution);
    }

    /**
     * Returns the actual resolution used to read the data given the specified target resolution and the specified
     * overview policy
     */
    @Override
    public double[] getReadingResolutions(String coverageName, OverviewPolicy policy, double[] requestedResolution)
            throws IOException {
        if (!checkName(coverageName)) {
            throw new IllegalArgumentException("The specified coverageName " + coverageName + "is not supported");
        }
        // find the target resolution level
        double[] result;
        if (numOverviews > 0) {
            int imageIdx = pickOverviewLevel(coverageName, policy, requestedResolution);
            result = imageIdx > 0 ? overViewResolutions[imageIdx - 1] : highestRes;
        } else {
            result = getHighestRes();
        }

        // return via cloning to protect internal state
        double[] clone = new double[result.length];
        System.arraycopy(result, 0, clone, 0, result.length);
        return clone;
    }

    /**
     * Simple support class for sorting overview resolutions
     *
     * @author Andrea Aime
     * @author Simone Giannecchini, GeoSolutions.
     * @since 2.5
     */
    private static class Resolution implements Comparable<Resolution> {
        double scaleFactor;

        double resolutionX;

        double resolutionY;

        int imageChoice;

        public Resolution(
                final double scaleFactor, final double resolutionX, final double resolutionY, int imageChoice) {
            this.scaleFactor = scaleFactor;
            this.resolutionX = resolutionX;
            this.resolutionY = resolutionY;
            this.imageChoice = imageChoice;
        }

        @Override
        public int compareTo(Resolution other) {
            if (scaleFactor > other.scaleFactor) return 1;
            else if (scaleFactor < other.scaleFactor) return -1;
            else return 0;
        }

        @Override
        public String toString() {
            return "Resolution[Choice=" + imageChoice + ",scaleFactor=" + scaleFactor + "]";
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
    protected final void decimationOnReadingControl(
            String coverageName, Integer imageChoice, ImageReadParam readP, double[] requestedRes) {

        int w, h;
        double[] selectedRes = new double[2];
        final int choice = imageChoice.intValue();
        if (choice == 0) {
            // highest resolution
            w = getOriginalGridRange(coverageName).getSpan(0);
            h = getOriginalGridRange(coverageName).getSpan(1);
            selectedRes[0] = getHighestRes()[0];
            selectedRes[1] = getHighestRes()[1];
        } else {
            // some overview
            selectedRes[0] = overViewResolutions[choice - 1][0];
            selectedRes[1] = overViewResolutions[choice - 1][1];
            w = (int) Math.round(getOriginalEnvelope(coverageName).getSpan(0) / selectedRes[0]);
            h = (int) Math.round(getOriginalEnvelope(coverageName).getSpan(1) / selectedRes[1]);
        }
        // decimation on reading
        if (requestedRes == null) {
            readP.setSourceSubsampling(1, 1, 0, 0);
        } else {
            ImageUtilities.setSubsamplingFactors(readP, requestedRes, selectedRes, w, h);

            if (LOGGER.isLoggable(Level.FINE))
                LOGGER.log(
                        Level.FINE,
                        String.format(
                                "coverageName:%s,imageChoice:%d,subSamplingFactorX:%d,subSamplingFactorY:%d",
                                coverageName,
                                imageChoice.intValue(),
                                readP.getSourceXSubsampling(),
                                readP.getSourceYSubsampling()));
        }
    }

    /**
     * Creates a {@link GridCoverage} for the provided {@link PlanarImage} using the {@link #originalEnvelope} that was
     * provided for this coverage.
     *
     * @param image contains the data for the coverage to create.
     * @return a {@link GridCoverage}
     */
    protected final GridCoverage createImageCoverage(PlanarImage image) throws IOException {
        return createImageCoverage(coverageName, image);
    }

    /**
     * Creates a {@link GridCoverage} for the provided {@link PlanarImage} using the {@link #originalEnvelope} that was
     * provided for this coverage.
     *
     * @param image contains the data for the coverage to create.
     * @return a {@link GridCoverage}
     */
    protected final GridCoverage createImageCoverage(String coverageName, PlanarImage image) throws IOException {
        return createImageCoverage(coverageName, image, null);
    }

    /**
     * Creates a {@link GridCoverage} for the provided {@link PlanarImage} using the {@link #raster2Model} that was
     * provided for this coverage.
     *
     * <p>This method is vital when working with coverages that have a raster to model transformation that is not a
     * simple scale and translate.
     *
     * @param image contains the data for the coverage to create.
     * @param raster2Model is the {@link MathTransform} that maps from the raster space to the model space.
     * @return a {@link GridCoverage}
     */
    protected final GridCoverage2D createImageCoverage(PlanarImage image, MathTransform raster2Model)
            throws IOException {
        return createImageCoverage(coverageName, image, raster2Model);
    }

    /**
     * Creates a {@link GridCoverage} for the provided {@link PlanarImage} using the {@link #raster2Model} that was
     * provided for this coverage.
     *
     * <p>This method is vital when working with coverages that have a raster to model transformation that is not a
     * simple scale and translate.
     *
     * @param image contains the data for the coverage to create.
     * @param raster2Model is the {@link MathTransform} that maps from the raster space to the model space.
     * @return a {@link GridCoverage}
     */
    protected final GridCoverage2D createImageCoverage(
            String coverageName, PlanarImage image, MathTransform raster2Model) throws IOException {

        // creating bands
        final SampleModel sm = image.getSampleModel();
        final ColorModel cm = image.getColorModel();
        final int numBands = sm.getNumBands();
        final GridSampleDimension[] bands = new GridSampleDimension[numBands];
        // setting bands names.
        for (int i = 0; i < numBands; i++) {
            final ColorInterpretation colorInterpretation = TypeMap.getColorInterpretation(cm, i);
            if (colorInterpretation == null) throw new IOException("Unrecognized sample dimension type");
            bands[i] = new GridSampleDimension(colorInterpretation.name());
        }
        // creating coverage
        if (raster2Model != null) {
            return coverageFactory.create(
                    coverageName, image, getCoordinateReferenceSystem(coverageName), raster2Model, bands, null, null);
        }
        return coverageFactory.create(
                coverageName, image, new GeneralBounds(getOriginalEnvelope(coverageName)), bands, null, null);
    }

    /**
     * This method is responsible for computing the resolutions in for the provided grid geometry in the provided crs.
     *
     * <p>It is worth to note that the returned resolution array is of length of 2 and it always is lon, lat for the
     * moment.<br>
     * It might be worth to remove the axes reordering code when we are confident enough with the code to handle the
     * north-up crs.
     *
     * <p>TODO use orthodromic distance?
     *
     * @param envelope the GeneralEnvelope
     */
    protected static final double[] getResolution(
            GeneralBounds envelope, Rectangle2D dim, CoordinateReferenceSystem crs) throws DataSourceException {
        double[] requestedRes = null;
        try {
            if (dim != null && envelope != null && crs != null) {
                // do we need to transform the originalEnvelope?
                final CoordinateReferenceSystem envelopeCrs2D =
                        CRS.getHorizontalCRS(envelope.getCoordinateReferenceSystem());
                if (envelopeCrs2D != null && !CRS.equalsIgnoreMetadata(crs, envelopeCrs2D)) {
                    CoordinateOperationFactory operationFactory = CRS.getCoordinateOperationFactory(true);
                    CoordinateOperation op = operationFactory.createOperation(envelopeCrs2D, crs);
                    envelope = CRS.transform(op, envelope);
                    envelope.setCoordinateReferenceSystem(crs);
                }
                requestedRes = new double[2];
                requestedRes[0] = envelope.getSpan(0) / dim.getWidth();
                requestedRes[1] = envelope.getSpan(1) / dim.getHeight();
            }
            return requestedRes;
        } catch (TransformException | FactoryException e) {
            throw new DataSourceException("Unable to get resolution", e);
        }
    }

    /**
     * Retrieves the {@link GeneralGridEnvelope} that represents the raster grid dimensions of the highest resolution
     * level in this dataset.
     *
     * @return the {@link GeneralGridEnvelope} that represents the raster grid dimensions of the highest resolution
     *     level in this dataset.
     */
    @Override
    public GridEnvelope getOriginalGridRange() {
        return getOriginalGridRange(coverageName);
    }

    /**
     * Retrieves the {@link GeneralGridEnvelope} that represents the raster grid dimensions of the highest resolution
     * level in this dataset.
     *
     * @return the {@link GeneralGridEnvelope} that represents the raster grid dimensions of the highest resolution
     *     level in this dataset.
     */
    @Override
    public GridEnvelope getOriginalGridRange(String coverageName) {
        if (!checkName(coverageName)) {
            throw new IllegalArgumentException("The specified coverageName " + coverageName + "is not supported");
        }
        assert originalGridRange.getDimension() == 2;
        return new GridEnvelope2D(
                originalGridRange.getLow(0),
                originalGridRange.getLow(1),
                originalGridRange.getSpan(0),
                originalGridRange.getSpan(1));
    }

    /**
     * Retrieves the {@link GeneralBounds} for this {@link AbstractGridCoverage2DReader}.
     *
     * @return the {@link GeneralBounds} for this {@link AbstractGridCoverage2DReader}.
     */
    @Override
    public CoordinateReferenceSystem getCoordinateReferenceSystem() {
        // Default implementation for backwards compatibility
        return getCoordinateReferenceSystem(coverageName);
    }

    /**
     * Retrieves the {@link GeneralBounds} for this {@link AbstractGridCoverage2DReader}.
     *
     * @return the {@link GeneralBounds} for this {@link AbstractGridCoverage2DReader}.
     */
    @Override
    public CoordinateReferenceSystem getCoordinateReferenceSystem(String coverageName) {
        if (!checkName(coverageName)) {
            throw new IllegalArgumentException("The specified coverageName " + coverageName + "is not supported");
        }

        return crs;
    }

    /**
     * Retrieves the {@link GeneralBounds} for this {@link AbstractGridCoverage2DReader}.
     *
     * @return the {@link GeneralBounds} for this {@link AbstractGridCoverage2DReader}.
     */
    @Override
    public GeneralBounds getOriginalEnvelope() {
        return getOriginalEnvelope(coverageName);
    }

    /**
     * Retrieves the {@link GeneralBounds} for this {@link AbstractGridCoverage2DReader}.
     *
     * @return the {@link GeneralBounds} for this {@link AbstractGridCoverage2DReader}.
     */
    @Override
    public GeneralBounds getOriginalEnvelope(String coverageName) {
        if (!checkName(coverageName)) {
            throw new IllegalArgumentException("The specified coverageName " + coverageName + "is not supported");
        }
        return new GeneralBounds(originalEnvelope);
    }

    /**
     * Retrieves the original grid to world transformation for this {@link AbstractGridCoverage2DReader}.
     *
     * @param pixInCell specifies the datum of the transformation we want.
     * @return the original grid to world transformation for this {@link AbstractGridCoverage2DReader}.
     */
    @Override
    public MathTransform getOriginalGridToWorld(final PixelInCell pixInCell) {
        // Default implementation for backwards compatibility
        return getOriginalGridToWorld(coverageName, pixInCell);
    }

    /**
     * Retrieves the original grid to world transformation for this {@link AbstractGridCoverage2DReader}.
     *
     * @param pixInCell specifies the datum of the transformation we want.
     * @return the original grid to world transformation for this {@link AbstractGridCoverage2DReader}.
     */
    @Override
    public MathTransform getOriginalGridToWorld(String coverageName, PixelInCell pixInCell) {
        if (!checkName(coverageName)) {
            throw new IllegalArgumentException("The specified coverageName " + coverageName + "is not supported");
        }
        synchronized (this) {
            if (raster2Model == null) {
                final GridToEnvelopeMapper geMapper =
                        new GridToEnvelopeMapper(getOriginalGridRange(coverageName), getOriginalEnvelope(coverageName));
                geMapper.setPixelAnchor(PixelInCell.CELL_CENTER);
                raster2Model = geMapper.createTransform();
            }
        }

        // we do not have to change the pixel datum
        if (pixInCell == PixelInCell.CELL_CENTER) return raster2Model;

        // we do have to change the pixel datum
        if (raster2Model instanceof AffineTransform) {
            final AffineTransform tr = new AffineTransform((AffineTransform) raster2Model);
            tr.concatenate(AffineTransform.getTranslateInstance(-0.5, -0.5));
            return ProjectiveTransform.create(tr);
        }
        if (raster2Model instanceof IdentityTransform) {
            final AffineTransform tr = new AffineTransform(1, 0, 0, 1, 0, 0);
            tr.concatenate(AffineTransform.getTranslateInstance(-0.5, -0.5));
            return ProjectiveTransform.create(tr);
        }
        throw new IllegalStateException("This reader's grid to world transform is invalud!");
    }

    /**
     * Retrieves the source for this {@link AbstractGridCoverage2DReader}.
     *
     * @return the source for this {@link AbstractGridCoverage2DReader}.
     */
    @Override
    public final Object getSource() {
        return source;
    }

    /**
     * Disposes this reader.
     *
     * <p>This method just tries to close the underlying {@link ImageInputStream}.
     */
    @Override
    public void dispose() {
        if (inStream != null && closeMe) {
            try {
                inStream.close();
            } catch (IOException e) {
                if (LOGGER.isLoggable(Level.FINE)) LOGGER.log(Level.FINE, e.getLocalizedMessage(), e);
            }
        }
    }

    @Override
    public String[] getGridCoverageNames() {
        return new String[] {coverageName};
    }

    @Override
    public String[] getMetadataNames(final String coverageName) {
        if (!checkName(coverageName)) {
            throw new IllegalArgumentException("The specified coverageName " + coverageName + "is not supported");
        }
        return getMetadataNames();
    }

    /** @see org.geotools.api.coverage.grid.GridCoverageReader#getMetadataNames() */
    @Override
    public String[] getMetadataNames() {
        return null;
    }

    /** @see org.geotools.api.coverage.grid.GridCoverageReader#getMetadataValue(java.lang.String) */
    @Override
    public String getMetadataValue(final String name) {
        return getMetadataValue(coverageName, name);
    }

    @Override
    public String getMetadataValue(final String coverageName, final String name) {
        if (!checkName(coverageName)) {
            throw new IllegalArgumentException("The specified coverageName " + coverageName + "is not supported");
        }
        return null;
    }

    /** @see org.geotools.api.coverage.grid.GridCoverageReader#getGridCoverageCount() */
    @Override
    public int getGridCoverageCount() {
        return 1;
    }

    /**
     * Information about this source.
     *
     * <p>Subclasses should provide additional format specific information.
     *
     * @return ServiceInfo describing getSource().
     */
    @Override
    public ServiceInfo getInfo() {
        DefaultServiceInfo info;
        try {
            List<FileGroup> files = getFiles();
            if (files != null && !files.isEmpty()) {
                info = new DefaultFileServiceInfo(files);
            } else {
                info = new DefaultServiceInfo();
            }
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Failed to locate source file list", e);
            info = new DefaultServiceInfo();
        }
        info.setDescription(source == null ? null : String.valueOf(source));
        if (source instanceof URL) {
            URL url = (URL) source;
            info.setTitle(url.getFile());
            try {
                info.setSource(url.toURI());
            } catch (URISyntaxException e) {
            }
        } else if (source instanceof File) {
            File file = (File) source;
            String filename = file.getName();
            if (filename == null || filename.length() == 0) {
                info.setTitle(file.getName());
            }
            info.setSource(file.toURI());
        }
        return info;
    }

    /**
     * Returns a list of files making up the source data for this reader (as a whole). The default implementation
     * returns the source, if it can be made into a File object, or null otherwise.
     */
    protected List<FileGroup> getFiles() throws IOException {
        File file = getSourceAsFile();

        if (file == null) {
            return null;
        } else {
            return Collections.singletonList(new FileGroup(file, null, null));
        }
    }

    /** Returns the source as a File, if it can be converted to one, and it exists */
    protected File getSourceAsFile() {
        File file = null;
        if (source instanceof File) {
            file = (File) source;
        } else if (source instanceof URL) {
            File sf = URLs.urlToFile((URL) source);
            if (sf.exists()) {
                file = sf;
            }
        }
        return file;
    }

    /**
     * Default implementation returns a FileResourceInfo containing same fileGroup list contained in the ServiceInfo
     * object.
     */
    @Override
    public ResourceInfo getInfo(String coverageName) {

        ResourceInfo info = null;
        try {
            List<FileGroup> files = getFiles();
            if (files != null && !files.isEmpty()) {
                info = new DefaultFileResourceInfo(files);
            } else {
                info = new DefaultResourceInfo();
            }
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Failed to locate source file list", e);
            info = new DefaultResourceInfo();
        }
        return info;
    }

    /**
     * Forcing disposal of this {@link AbstractGridCoverage2DReader} which may keep an {@link ImageInputStream} open.
     */
    @Override
    @SuppressWarnings("deprecation") // finalize is deprecated in Java 9
    protected void finalize() throws Throwable {
        dispose();
        super.finalize();
    }

    /**
     * Return the set of dynamic parameterDescriptors (the ones related to domains) for this reader. Default
     * implementation returns an empty set of parameters
     */
    @Override
    public Set<ParameterDescriptor<List>> getDynamicParameters() throws IOException {
        return getDynamicParameters(coverageName);
    }

    /**
     * Return the set of dynamic parameterDescriptors (the ones related to domains) for this reader. Default
     * implementation returns an empty set of parameters
     */
    @Override
    public Set<ParameterDescriptor<List>> getDynamicParameters(String coverageName) throws IOException {
        return Collections.emptySet();
    }

    @Override
    public DatasetLayout getDatasetLayout() {
        // Default implementation for backwards compatibility
        return getDatasetLayout(coverageName);
    }

    @Override
    public DatasetLayout getDatasetLayout(String coverageName) {
        if (!checkName(coverageName)) {
            throw new IllegalArgumentException("The specified coverageName " + coverageName + " is not supported");
        }
        // for compatibility with the readers not initializing the field
        if (dtLayout == null) {
            return new DefaultDatasetLayoutImpl() {
                @Override
                public int getNumInternalOverviews() {
                    return numOverviews;
                }
            };
        }
        return dtLayout;
    }

    public GridEnvelope getOverviewGridEnvelope(int overviewIndex) throws IOException {
        return getOverviewGridEnvelope(coverageName, overviewIndex);
    }

    public GridEnvelope getOverviewGridEnvelope(String coverageName, int overviewIndex) throws IOException {
        if (!checkName(coverageName)) {
            throw new IllegalArgumentException("The specified coverageName " + coverageName + "is not supported");
        }

        // Default implementation for backwards compatibility
        return null;
    }

    @Override
    public ImageLayout getImageLayout(String coverageName) throws IOException {
        if (!checkName(coverageName)) {
            throw new IllegalArgumentException("The specified coverageName " + coverageName + "is not supported");
        }

        return (ImageLayout) imageLayout.clone();
    }

    @Override
    public ImageLayout getImageLayout() throws IOException {
        // Default implementation for backwards compatibility
        return getImageLayout(coverageName);
    }

    /**
     * Extract the ImageLayout from the provided reader for the first available image.
     *
     * @param reader an istance of {@link ImageReader}
     * @throws IOException in case an error occurs
     */
    protected void setLayout(ImageReader reader) throws IOException {

        Utilities.ensureNonNull("reader", reader);
        // save ImageLayout
        ImageLayout2 layout = new ImageLayout2();
        ImageTypeSpecifier its = reader.getImageTypes(0).next();
        layout.setColorModel(its.getColorModel()).setSampleModel(its.getSampleModel());
        layout.setMinX(0).setMinY(0).setWidth(reader.getWidth(0)).setHeight(reader.getHeight(0));
        layout.setTileGridXOffset(0)
                .setTileGridYOffset(0)
                .setTileWidth(reader.getTileWidth(0))
                .setTileHeight(reader.getTileHeight(0));
        setlayout(layout);
    }

    /**
     * Set the provided layout for this {@link GridCoverage2DReader}-
     *
     * @param layout the {@link ImageLayout} to set. It must be nont null
     */
    protected void setlayout(ImageLayout layout) {
        Utilities.ensureNonNull("layout", layout);
        this.imageLayout = (ImageLayout) layout.clone();
    }

    @Override
    public double[][] getResolutionLevels() throws IOException {
        // Default implementation for backwards compatibility
        return getResolutionLevels(coverageName);
    }

    @Override
    public double[][] getResolutionLevels(String coverageName) throws IOException {
        if (!checkName(coverageName)) {
            throw new IllegalArgumentException("The specified coverageName " + coverageName + "is not supported");
        }

        final double[][] returnValue = new double[numOverviews + 1][2];
        double[] hres = getHighestRes();
        if (hres == null) {
            return null;
        } else {
            System.arraycopy(hres, 0, returnValue[0], 0, 2);
            for (int i = 1; i < returnValue.length; i++) {
                System.arraycopy(overViewResolutions[i - 1], 0, returnValue[i], 0, 2);
            }
            return returnValue;
        }
    }

    protected double[] getHighestRes(String coverageName) {
        if (!checkName(coverageName)) {
            throw new IllegalArgumentException("The specified coverageName " + coverageName + "is not supported");
        }

        return highestRes;
    }

    protected double[] getHighestRes() {
        return getHighestRes(coverageName);
    }

    /** Return the ground control points for the default coverage, or null if there are none */
    public GroundControlPoints getGroundControlPoints() {
        return null;
    }

    /** Return the ground control points for the specified, or null if there are none */
    public GroundControlPoints getGroundControlPoints(String coverageName) {
        if (!checkName(coverageName)) {
            throw new IllegalArgumentException("The specified coverageName " + coverageName + "is not supported");
        }
        return null;
    }

    /**
     * Computes the raster to model of a rescaled output raster, based on the original transform and output raster
     * scaling factor
     */
    protected AffineTransform getRescaledRasterToModel(RenderedImage coverageRaster) {
        final int ssWidth = coverageRaster.getWidth();
        final int ssHeight = coverageRaster.getHeight();
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, "Coverage read: width = " + ssWidth + " height = " + ssHeight);
        }

        // //
        //
        // setting new coefficients to define a new affineTransformation
        // to be applied to the grid to world transformation
        // -----------------------------------------------------------------------------------
        //
        // With respect to the original envelope, the obtained planarImage
        // needs to be rescaled. The scaling factors are computed as the
        // ratio between the output raster sizes and the original sizes
        // (this correctly accounts for odd sized overviews)
        // //
        final double scaleX = originalGridRange.getSpan(0) / (1.0 * ssWidth);
        final double scaleY = originalGridRange.getSpan(1) / (1.0 * ssHeight);
        final AffineTransform tempRaster2Model = new AffineTransform((AffineTransform) raster2Model);
        AffineTransform scale = new AffineTransform(scaleX, 0, 0, scaleY, 0, 0);
        if (!XAffineTransform.isIdentity(scale, EPS)) {
            // the transformation includes the pixel is center shift, we need to
            // remove it before rescaling, and then apply it back later
            tempRaster2Model.concatenate(CoverageUtilities.CENTER_TO_CORNER);
            tempRaster2Model.concatenate(scale);
            tempRaster2Model.concatenate(CoverageUtilities.CORNER_TO_CENTER);
        }

        return tempRaster2Model;
    }

    /** Retrieves the sibling of the specified file, if available, or null otherwise */
    protected static File getSibling(final File file, String extension) {
        String parentPath = file.getParent();
        String filename = file.getName();
        final int i = filename.lastIndexOf('.');
        filename = (i == -1) ? filename : filename.substring(0, i);

        // getting name and extension
        final String base = (parentPath != null)
                ? new StringBuilder(parentPath)
                        .append(File.separator)
                        .append(filename)
                        .toString()
                : filename;

        // We can now construct the baseURL from this string.
        File file2Parse = new File(new StringBuilder(base).append(extension).toString());
        if (file2Parse.exists()) {
            return file2Parse;
        } else {
            return null;
        }
    }

    /** Adds all the siblings that could be found to exist to the given file list */
    protected void addAllSiblings(final File file, List<File> files, String... extensions) {
        String parentPath = file.getParent();
        String filename = file.getName();
        final int i = filename.lastIndexOf('.');
        filename = (i == -1) ? filename : filename.substring(0, i);

        // getting name and extension
        final String base = (parentPath != null)
                ? new StringBuilder(parentPath)
                        .append(File.separator)
                        .append(filename)
                        .toString()
                : filename;

        // We can now construct the baseURL from this string.
        for (String extension : extensions) {
            File file2Parse = new File(new StringBuilder(base).append(extension).toString());
            if (file2Parse.exists()) {
                files.add(file2Parse);
            }
        }
    }

    /** Adds the specified siblings, if not null, and existing */
    protected void addSiblings(List<File> files, File... siblings) {
        for (File sibling : siblings) {
            if (sibling != null && sibling.exists()) {
                files.add(sibling);
            }
        }
    }

    protected MultiLevelROIProvider getMultiLevelROIProvider(String coverageName) {
        throw new UnsupportedOperationException("The abstract reader doesn't implement this method yet");
    }

    /** Collects the scales and offsets for value rescaling from the metadata, if present */
    protected void collectScaleOffset(IIOMetadata iioMetadata) {
        if (iioMetadata instanceof CoreCommonImageMetadata) {
            CoreCommonImageMetadata ccm = (CoreCommonImageMetadata) iioMetadata;
            this.scales = ccm.getScales();
            this.offsets = ccm.getOffsets();
        }
    }

    /**
     * Method that looks for an external {@link PAMDataset} first, and if not found, checks for an internal
     * {@link it.geosolutions.imageioimpl.plugins.tiff.gdal.GDALMetadata} inside a custom tag.
     *
     * <p>The method is tolerant to invalid metadata contents and will log at INFO level in case of invalid metadata
     * structure: there might be files with invalid metadata that used to be read just fine before PAM dataset reading
     * was implemented.
     */
    public static PAMDataset getPamDataset(File sourceFile, IIOMetadata metadata) {
        if (sourceFile != null && !ImageIOUtilities.isSkipExternalFilesLookup()) {
            File pamFile = new File(sourceFile.getParent(), sourceFile.getName() + ".aux.xml");
            if (pamFile.exists()) {
                PAMParser pamParser = PAMParser.getInstance();

                try {
                    return pamParser.parsePAM(pamFile);
                } catch (Exception e) {
                    LOGGER.log(Level.INFO, "GDAL aux.xml metadata file could not be parsed", e);
                }
            }
        }

        // So far supported only by TIFF files, we can consider moving up to CoreCommonImageMetadata
        // if more examples show up in different formats
        if (metadata instanceof TIFFImageMetadata) {
            PAMDataset gdalMetadata = getPamDataset((TIFFImageMetadata) metadata);
            if (gdalMetadata != null) return gdalMetadata;
        }

        return null;
    }

    /** If available, parses the GDAL_METADATA tag contents and transforms it into a PAMDataset */
    public static PAMDataset getPamDataset(TIFFImageMetadata metadata) {
        TIFFImageMetadata tm = metadata;
        TIFFField f = tm.getTIFFField(GDAL_METADATA_TAG);
        if (f != null) {
            try {
                String xml = f.getAsString(0);
                GDALMetadata gdalMetadata = GDALMetadataParser.parse(xml);
                return toPamDataset(gdalMetadata);
            } catch (Exception e) {
                LOGGER.log(Level.INFO, "GDAL_METADATA tag contents could not be parsed", e);
            }
        }
        return null;
    }

    /**
     * Part of the information contained in {@link GDALMetadata} can be trasformed in a semantically equivalent PAM
     * dataset
     */
    private static PAMDataset toPamDataset(GDALMetadata metadata) {
        PAMDataset pam = new PAMDataset();
        Map<Integer, PAMDataset.PAMRasterBand> bands = new TreeMap<>();
        metadata.getItems().stream().filter(i -> i.getSample() != null).forEach(i -> collectItemIntoBands(bands, i));
        pam.getPAMRasterBand().addAll(bands.values());
        return pam;
    }

    private static void collectItemIntoBands(Map<Integer, PAMDataset.PAMRasterBand> bands, GDALMetadata.Item i) {
        int bandNumber = i.getSample() + 1;
        PAMDataset.PAMRasterBand band = bands.computeIfAbsent(bandNumber, n -> getRasterBand(n));
        PAMDataset.PAMRasterBand.Metadata.MDI mdi = new PAMDataset.PAMRasterBand.Metadata.MDI();
        mdi.setKey(i.getName());
        mdi.setValue(i.getValue());
        if (band.getMetadata() == null) band.setMetadata(new PAMDataset.PAMRasterBand.Metadata());
        band.getMetadata().getMDI().add(mdi);
    }

    private static PAMDataset.PAMRasterBand getRasterBand(Integer n) {
        PAMDataset.PAMRasterBand result = new PAMDataset.PAMRasterBand();
        result.setBand(n);
        return result;
    }
}
