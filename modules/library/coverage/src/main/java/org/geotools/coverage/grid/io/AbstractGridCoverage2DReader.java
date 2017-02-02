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

import it.geosolutions.imageio.maskband.DatasetLayout;

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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.spi.ImageInputStreamSpi;
import javax.imageio.stream.ImageInputStream;
import javax.media.jai.ImageLayout;
import javax.media.jai.PlanarImage;

import org.geotools.coverage.CoverageFactoryFinder;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.TypeMap;
import org.geotools.coverage.grid.GeneralGridEnvelope;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.io.footprint.MultiLevelROIProvider;
import org.geotools.data.DataSourceException;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultFileResourceInfo;
import org.geotools.data.DefaultFileServiceInfo;
import org.geotools.data.DefaultResourceInfo;
import org.geotools.data.DefaultServiceInfo;
import org.geotools.data.FileGroupProvider.FileGroup;
import org.geotools.data.ResourceInfo;
import org.geotools.data.ServiceInfo;
import org.geotools.factory.GeoTools;
import org.geotools.factory.Hints;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.operation.builder.GridToEnvelopeMapper;
import org.geotools.referencing.operation.matrix.XAffineTransform;
import org.geotools.referencing.operation.transform.IdentityTransform;
import org.geotools.referencing.operation.transform.ProjectiveTransform;
import org.geotools.resources.coverage.CoverageUtilities;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Errors;
import org.geotools.util.Utilities;
import org.geotools.util.logging.Logging;
import it.geosolutions.jaiext.utilities.ImageLayout2;
import org.opengis.coverage.ColorInterpretation;
import org.opengis.coverage.grid.Format;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.coverage.grid.GridEnvelope;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.InvalidParameterNameException;
import org.opengis.parameter.InvalidParameterValueException;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.parameter.ParameterNotFoundException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.datum.PixelInCell;
import org.opengis.referencing.operation.CoordinateOperation;
import org.opengis.referencing.operation.CoordinateOperationFactory;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

/**
 * This class is a first attempt for providing a way to get more informations out of a single 2D raster datasets (x,y). It is worth to remark that for
 * the moment this is thought for 2D rasters not for 3D or 4D rasters (x,y,z,t).
 * 
 * <p>
 * The main drawback I see with the current GeoApi GridCoverageReader interface is that there is no way to get real information about a raster source
 * unless you instantiate a GridCoverage. As an instance it is impossible to know the envelope, the number of overviews, the tile size. This
 * information is needed in order to perform decimation on reading or to use built-in overviews<br>
 * This really impacts the ability to exploit raster datasets in a desktop environment where caching is crucial.
 * 
 * @author Simone Giannecchini, GeoSolutions
 * @since 2.3
 * 
 * 
 * @source $URL$
 */
public abstract class AbstractGridCoverage2DReader implements GridCoverage2DReader {

    /** The {@link Logger} for this {@link AbstractGridCoverage2DReader}. */
    private final static Logger LOGGER = Logging.getLogger("org.geotools.data.coverage.grid");

    /**
     * Small number used for double comparisons
     */
    protected static double EPS = 1e-6;

    /**
     * This contains the number of overviews.aaa
     */
    protected int numOverviews = 0;

    /** 2DGridToWorld math transform. */
    protected MathTransform raster2Model = null;

    /** crs for this coverage */
    protected CoordinateReferenceSystem crs = null;

    /** Envelope read from file */
    protected GeneralEnvelope originalEnvelope = null;

    /** Coverage name */
    protected String coverageName = "geotools_coverage";

    /** Source to read from */
    protected Object source = null;

    /** Hints used by the {@link AbstractGridCoverage2DReader} subclasses. */
    protected Hints hints = GeoTools.getDefaultHints();

    /**
     * Highest resolution availaible for this reader.
     */
    protected double[] highestRes = null;

    /** Temp variable used in many readers. */
    protected boolean closeMe;

    /**
     * In case we are trying to read from a GZipped file this will be set to true.
     */
    protected boolean gzipped;

    /**
     * The original {@link GridRange} for the {@link GridCoverage2D} of this reader.
     */
    protected GridEnvelope originalGridRange = null;

    /**
     * Input stream that can be used to initialize subclasses of {@link AbstractGridCoverage2DReader}.
     */
    protected ImageInputStream inStream = null;

    /** Resolutions avialaible through an overviews based mechanism. */
    protected double[][] overViewResolutions = null;

    /**
     * {@link GridCoverageFactory} instance.
     */
    protected GridCoverageFactory coverageFactory;

    private Map<String,ArrayList<Resolution>> resolutionsLevelsMap = new HashMap<String,ArrayList<Resolution>>();

    protected ImageInputStreamSpi inStreamSPI;

    private ImageLayout imageLayout;
    
    /** Coverage {@link DatasetLayout} containing information about Overviews and Mask management*/
    protected DatasetLayout dtLayout;

    /**
     * Default protected constructor. Useful for wrappers.
     */
    protected AbstractGridCoverage2DReader() {

    }

    /**
     * Creates a new instance of a {@link AIGReader}. I assume nothing about file extension.
     * 
     * @param input Source object for which we want to build an {@link AIGReader}.
     * @throws DataSourceException
     */
    public AbstractGridCoverage2DReader(Object input) throws DataSourceException {
        this(input, null);
    }

    /**
     * Creates a new instance of a {@link AIGReader}. I assume nothing about file extension.
     * 
     * @param input Source object for which we want to build an {@link AIGReader}.
     * @param hints Hints to be used by this reader throughout his life.
     * @throws DataSourceException
     */
    public AbstractGridCoverage2DReader(Object input, Hints hints) throws DataSourceException {

        //
        // basic management of hints
        //
        if (hints == null)
            this.hints = new Hints();
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
            final IOException ex = new IOException(Errors.format(ErrorKeys.NULL_ARGUMENT_$1,
                    "input"));
            throw new DataSourceException(ex);
        }
        this.source = input;

    }

    /**
     * This method is responsible for checking the provided coverage name against the coverage name for this {@link GridCoverage2DReader}.
     * 
     * @param coverageName the coverage name to check.
     * @return <code>true</code> if this {@link GridCoverage2DReader} contains the provided coverage name, <code>false</code> otherwise.
     */
    protected boolean checkName(String coverageName) {
        Utilities.ensureNonNull("coverageName", coverageName);
        return coverageName.equalsIgnoreCase(this.coverageName);
    }

    @Override
    public GridCoverage2D read(String coverageName, GeneralParameterValue[] parameters)
            throws IllegalArgumentException, IOException {
        // Default implementation for backwards compatibility
        if (coverageName.equalsIgnoreCase(this.coverageName)) {
            return read(parameters);
        }
        // Subclasses should do more checks on coverageName
        throw new IllegalArgumentException("The specified coverageName " + coverageName
                + "is not supported");
    }

    /**
     * Read the current grid coverage from the stream.
     * <p>
     * Example:
     * 
     * <pre>
     * <code>
     * </code>
     * </pre>
     * 
     * The method {@link #hasMoreGridCoverages} should be invoked first in order to verify that a coverage is available.
     * 
     * @param parameters Optional parameters matching {@link Format#getReadParameters}.
     * @return a {@linkplain GridCoverage grid coverage} from the input source.
     * @throws InvalidParameterNameException if a parameter in {@code parameters} doesn't have a recognized name.
     * @throws InvalidParameterValueException if a parameter in {@code parameters} doesn't have a valid value.
     * @throws ParameterNotFoundException if a parameter was required for the operation but was not provided in the {@code parameters} list.
     * @throws CannotCreateGridCoverageException if the coverage can't be created for a logical reason (for example an unsupported format, or an
     *         inconsistency found in the data).
     * @throws IOException if a read operation failed for some other input/output reason, including {@link FileNotFoundException} if no file with the
     *         given {@code name} can be found, or {@link javax.imageio.IIOException} if an error was thrown by the underlying image library.
     */
    public abstract GridCoverage2D read(GeneralParameterValue[] parameters)
            throws IllegalArgumentException, IOException;

    // -------------------------------------------------------------------------
    //
    // old support methods
    //
    // -------------------------------------------------------------------------

    /**
     * This method is responsible for preparing the read param for doing an {@link ImageReader#read(int, ImageReadParam)}.
     * 
     * 
     * <p>
     * This method is responsible for preparing the read param for doing an {@link ImageReader#read(int, ImageReadParam)}. It sets the passed
     * {@link ImageReadParam} in terms of decimation on reading using the provided requestedEnvelope and requestedDim to evaluate the needed
     * resolution. It also returns and {@link Integer} representing the index of the raster to be read when dealing with multipage raster.
     * 
     * @param overviewPolicy it can be one of {@link Hints#VALUE_OVERVIEW_POLICY_IGNORE}, {@link Hints#VALUE_OVERVIEW_POLICY_NEAREST},
     *        {@link Hints#VALUE_OVERVIEW_POLICY_QUALITY} or {@link Hints#VALUE_OVERVIEW_POLICY_SPEED}. It specifies the policy to compute the
     *        overviews level upon request.
     * @param readP an instance of {@link ImageReadParam} for setting the subsampling factors.
     * @param requestedEnvelope the {@link GeneralEnvelope} we are requesting.
     * @param requestedDim the requested dimensions.
     * @return the index of the raster to read in the underlying data source.
     * @throws IOException
     * @throws TransformException
     */
    protected Integer setReadParams(OverviewPolicy overviewPolicy, ImageReadParam readP,
            GeneralEnvelope requestedEnvelope, Rectangle requestedDim) throws IOException, TransformException {
        return setReadParams(coverageName, overviewPolicy, readP, requestedEnvelope, requestedDim);
    }

    /**
     * This method is responsible for preparing the read param for doing an {@link ImageReader#read(int, ImageReadParam)}.
     * 
     * 
     * <p>
     * This method is responsible for preparing the read param for doing an {@link ImageReader#read(int, ImageReadParam)}. It sets the passed
     * {@link ImageReadParam} in terms of decimation on reading using the provided requestedEnvelope and requestedDim to evaluate the needed
     * resolution. It also returns and {@link Integer} representing the index of the raster to be read when dealing with multipage raster.
     * 
     * @param overviewPolicy it can be one of {@link Hints#VALUE_OVERVIEW_POLICY_IGNORE}, {@link Hints#VALUE_OVERVIEW_POLICY_NEAREST},
     *        {@link Hints#VALUE_OVERVIEW_POLICY_QUALITY} or {@link Hints#VALUE_OVERVIEW_POLICY_SPEED}. It specifies the policy to compute the
     *        overviews level upon request.
     * @param readP an instance of {@link ImageReadParam} for setting the subsampling factors.
     * @param requestedEnvelope the {@link GeneralEnvelope} we are requesting.
     * @param requestedDim the requested dimensions.
     * @return the index of the raster to read in the underlying data source.
     * @throws IOException
     * @throws TransformException
     */
    protected Integer setReadParams(String coverageName, OverviewPolicy overviewPolicy,
            ImageReadParam readP, GeneralEnvelope requestedEnvelope, Rectangle requestedDim)
            throws IOException, TransformException {

        // //
        //
        // Default image index 0
        //
        // //
        Integer imageChoice = new Integer(0);

        // //
        //
        // Init overview policy
        //
        // //
        // when policy is explictly provided it overrides the policy provided
        // using hints.
        if (overviewPolicy == null)
            overviewPolicy = extractOverviewPolicy();

        // //
        //
        // default values for subsampling
        //
        // //
        readP.setSourceSubsampling(1, 1, 0, 0);

        // //
        //
        // requested to ignore overviews
        //
        // //
        if (overviewPolicy.equals(OverviewPolicy.IGNORE))
            return imageChoice;

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
        // Resolution requested. I am here computing the resolution required by
        // the user.
        //
        // //
        double[] requestedRes = getResolution(requestedEnvelope, requestedDim,
                getCoordinateReferenceSystem(coverageName));
        if (requestedRes == null)
            return imageChoice;

        // //
        //
        // overviews or decimation
        //
        // //
        if (useOverviews)
            imageChoice = pickOverviewLevel(coverageName, overviewPolicy, requestedRes);

        // /////////////////////////////////////////////////////////////////////
        // DECIMATION ON READING
        // /////////////////////////////////////////////////////////////////////
        decimationOnReadingControl(coverageName, imageChoice, readP, requestedRes);
        return imageChoice;

    }

    /**
     * This method is responsible for checking the overview policy as defined by the provided {@link Hints}.
     * 
     * @return the overview policy which can be one of {@link Hints#VALUE_OVERVIEW_POLICY_IGNORE}, {@link Hints#VALUE_OVERVIEW_POLICY_NEAREST},
     *         {@link Hints#VALUE_OVERVIEW_POLICY_SPEED}, {@link Hints#VALUE_OVERVIEW_POLICY_QUALITY}. Default is
     *         {@link Hints#VALUE_OVERVIEW_POLICY_NEAREST}.
     */
    private OverviewPolicy extractOverviewPolicy() {
        OverviewPolicy overviewPolicy = null;
        // check if a policy was provided using hints (check even the
        // deprecated one)
        if (this.hints != null)
            if (this.hints.containsKey(Hints.OVERVIEW_POLICY))
                overviewPolicy = (OverviewPolicy) this.hints.get(Hints.OVERVIEW_POLICY);

        // use default if not provided. Default is nearest
        if (overviewPolicy == null)
            overviewPolicy = OverviewPolicy.getDefaultPolicy();
        assert overviewPolicy != null;
        return overviewPolicy;
    }

    private Integer pickOverviewLevel(String coverageName, OverviewPolicy policy, double[] requestedRes) {
        // setup policy
        if (policy == null)
            policy = extractOverviewPolicy();
        
        ArrayList<Resolution> resolutionsLevels;

        // sort resolutions from smallest pixels (higher res) to biggest pixels (higher res)
        // keeping a reference to the original image choice
        synchronized (this) {
        	resolutionsLevels = resolutionsLevelsMap.get(coverageName);
            if (resolutionsLevels == null) {
                resolutionsLevels = new ArrayList<Resolution>();
                resolutionsLevelsMap.put(coverageName, resolutionsLevels);
                // note that we assume what follows:
                // -highest resolution image is at level 0.
                // -all the overviews share the same envelope
                // -the aspect ratio for the overviews is constant
                // -the provided resolutions are taken directly from the grid
                resolutionsLevels.add(new Resolution(1, getHighestRes()[0], getHighestRes()[1], 0));
                if (numOverviews > 0) {
                    for (int i = 0; i < overViewResolutions.length; i++)
                        resolutionsLevels.add(new Resolution(overViewResolutions[i][0]
                                / getHighestRes()[0], overViewResolutions[i][0],
                                overViewResolutions[i][1], i + 1));
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
        final double requestedScaleFactor = leastReduceAxis == 0 ? requestedScaleFactorX
                : requestedScaleFactorY;

        // are we looking for a resolution even higher than the native one?
        if (requestedScaleFactor <= 1)
            return max.imageChoice;
        // are we looking for a resolution even lower than the smallest overview?
        final Resolution min = resolutionsLevels.get(resolutionsLevels.size() - 1);
        if (requestedScaleFactor >= min.scaleFactor)
            return min.imageChoice;
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
                if (policy == OverviewPolicy.QUALITY)
                    return prev.imageChoice;
                else if (policy == OverviewPolicy.SPEED)
                    return curr.imageChoice;
                else if (requestedScaleFactor - prev.scaleFactor < curr.scaleFactor
                        - requestedScaleFactor)
                    return prev.imageChoice;
                else
                    return curr.imageChoice;
            }
            prev = curr;
        }
        // fallback
        return max.imageChoice;
    }

    /**
     * Returns the actual resolution used to read the data given the specified target resolution and the specified overview policy
     * 
     * @param policy
     * @param resolutions
     * @return
     * @throws IOException 
     */
    public double[] getReadingResolutions(OverviewPolicy policy, double[] requestedResolution) throws IOException {
        // Default implementation for backwards compatibility
        return getReadingResolutions(coverageName, policy, requestedResolution);        
    }    

    /**
     * Returns the actual resolution used to read the data given the specified target resolution and the specified overview policy
     * 
     * @param policy
     * @param resolutions
     * @return
     */
    @Override
    public double[] getReadingResolutions(String coverageName, OverviewPolicy policy,
            double[] requestedResolution) throws IOException {
        if (!checkName(coverageName)) {
            throw new IllegalArgumentException("The specified coverageName " + coverageName
                    + "is not supported");
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

        public Resolution(final double scaleFactor, final double resolutionX,
                final double resolutionY, int imageChoice) {
            this.scaleFactor = scaleFactor;
            this.resolutionX = resolutionX;
            this.resolutionY = resolutionY;
            this.imageChoice = imageChoice;
        }

        public int compareTo(Resolution other) {
            if (scaleFactor > other.scaleFactor)
                return 1;
            else if (scaleFactor < other.scaleFactor)
                return -1;
            else
                return 0;
        }

        public String toString() {
            return "Resolution[Choice=" + imageChoice + ",scaleFactor=" + scaleFactor + "]";
        }
    }
    
    protected final void decimationOnReadingControl(Integer imageChoice, ImageReadParam readP, double[] requestedRes) {
        decimationOnReadingControl(imageChoice, readP, requestedRes);
    }

    /**
     * This method is responsible for evaluating possible subsampling factors once the best resolution level has been found, in case we have support
     * for overviews, or starting from the original coverage in case there are no overviews available.
     * 
     * Anyhow this method should not be called directly but subclasses should make use of the setReadParams method instead in order to transparently
     * look for overviews.
     * 
     * @param imageChoice
     * @param readP
     * @param requestedRes
     */
    protected final void decimationOnReadingControl(String coverageName, Integer imageChoice, ImageReadParam readP, double[] requestedRes) {
        {
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
            // /////////////////////////////////////////////////////////////////////
            // DECIMATION ON READING
            // Setting subsampling factors with some checkings
            // 1) the subsampling factors cannot be zero
            // 2) the subsampling factors cannot be such that the w or h are
            // zero
            // /////////////////////////////////////////////////////////////////////
            if (requestedRes == null) {
                readP.setSourceSubsampling(1, 1, 0, 0);

            } else {
                int subSamplingFactorX = (int) Math.floor(requestedRes[0] / selectedRes[0]);
                subSamplingFactorX = subSamplingFactorX == 0 ? 1 : subSamplingFactorX;

                while (w / subSamplingFactorX <= 0 && subSamplingFactorX >= 0)
                    subSamplingFactorX--;
                subSamplingFactorX = subSamplingFactorX == 0 ? 1 : subSamplingFactorX;

                int subSamplingFactorY = (int) Math.floor(requestedRes[1] / selectedRes[1]);
                subSamplingFactorY = subSamplingFactorY == 0 ? 1 : subSamplingFactorY;

                while (h / subSamplingFactorY <= 0 && subSamplingFactorY >= 0)
                    subSamplingFactorY--;
                subSamplingFactorY = subSamplingFactorY == 0 ? 1 : subSamplingFactorY;

                readP.setSourceSubsampling(subSamplingFactorX, subSamplingFactorY, 0, 0);
            }

        }
    }

    /**
     * Creates a {@link GridCoverage} for the provided {@link PlanarImage} using the {@link #originalEnvelope} that was provided for this coverage.
     * 
     * @param image contains the data for the coverage to create.
     * @return a {@link GridCoverage}
     * @throws IOException
     */
    protected final GridCoverage createImageCoverage(PlanarImage image) throws IOException {
        return createImageCoverage(coverageName, image);
    }

    /**
     * Creates a {@link GridCoverage} for the provided {@link PlanarImage} using the {@link #originalEnvelope} that was provided for this coverage.
     * 
     * @param coverageName
     * @param image contains the data for the coverage to create.
     * @return a {@link GridCoverage}
     * @throws IOException
     */
    protected final GridCoverage createImageCoverage(String coverageName, PlanarImage image)
            throws IOException {
        return createImageCoverage(coverageName, image, null);
    }

    /**
     * Creates a {@link GridCoverage} for the provided {@link PlanarImage} using the {@link #raster2Model} that was provided for this coverage.
     * 
     * <p>
     * This method is vital when working with coverages that have a raster to model transformation that is not a simple scale and translate.
     * 
     * @param image contains the data for the coverage to create.
     * @param raster2Model is the {@link MathTransform} that maps from the raster space to the model space.
     * @return a {@link GridCoverage}
     * @throws IOException
     */
    protected final GridCoverage2D createImageCoverage(PlanarImage image, MathTransform raster2Model)
            throws IOException {
        return createImageCoverage(coverageName, image, raster2Model);
    }

    /**
     * Creates a {@link GridCoverage} for the provided {@link PlanarImage} using the {@link #raster2Model} that was provided for this coverage.
     * 
     * <p>
     * This method is vital when working with coverages that have a raster to model transformation that is not a simple scale and translate.
     * 
     * @param coverageName
     * @param image contains the data for the coverage to create.
     * @param raster2Model is the {@link MathTransform} that maps from the raster space to the model space.
     * @return a {@link GridCoverage}
     * @throws IOException
     */
    protected final GridCoverage2D createImageCoverage(String coverageName, PlanarImage image,
            MathTransform raster2Model) throws IOException {

        // creating bands
        final SampleModel sm = image.getSampleModel();
        final ColorModel cm = image.getColorModel();
        final int numBands = sm.getNumBands();
        final GridSampleDimension[] bands = new GridSampleDimension[numBands];
        // setting bands names.
        for (int i = 0; i < numBands; i++) {
            final ColorInterpretation colorInterpretation = TypeMap.getColorInterpretation(cm, i);
            if (colorInterpretation == null)
                throw new IOException("Unrecognized sample dimension type");
            bands[i] = new GridSampleDimension(colorInterpretation.name());
        }
        // creating coverage
        if (raster2Model != null) {
            return coverageFactory.create(coverageName, image,
                    getCoordinateReferenceSystem(coverageName), raster2Model, bands, null, null);
        }
        return coverageFactory.create(coverageName, image, new GeneralEnvelope(getOriginalEnvelope(coverageName)),
                bands, null, null);

    }

    /**
     * This method is responsible for computing the resolutions in for the provided grid geometry in the provided crs.
     * 
     * <P>
     * It is worth to note that the returned resolution array is of length of 2 and it always is lon, lat for the moment.<br>
     * It might be worth to remove the axes reordering code when we are confident enough with the code to handle the north-up crs.
     * <p>
     * TODO use orthodromic distance?
     * 
     * @param envelope the GeneralEnvelope
     * @param dim
     * @param crs
     * @throws DataSourceException
     */
    protected final static double[] getResolution(GeneralEnvelope envelope, Rectangle2D dim,
            CoordinateReferenceSystem crs) throws DataSourceException {
        double[] requestedRes = null;
        try {
            if (dim != null && envelope != null && crs != null) {
                // do we need to transform the originalEnvelope?
                final CoordinateReferenceSystem envelopeCrs2D = CRS.getHorizontalCRS(envelope
                        .getCoordinateReferenceSystem());
                if (envelopeCrs2D != null && !CRS.equalsIgnoreMetadata(crs, envelopeCrs2D)) {
                    CoordinateOperationFactory operationFactory = CRS
                            .getCoordinateOperationFactory(true);
                    CoordinateOperation op = operationFactory.createOperation(envelopeCrs2D, crs);
                    envelope = CRS.transform(op, envelope);
                    envelope.setCoordinateReferenceSystem(crs);
                }
                requestedRes = new double[2];
                requestedRes[0] = envelope.getSpan(0) / dim.getWidth();
                requestedRes[1] = envelope.getSpan(1) / dim.getHeight();
            }
            return requestedRes;
        } catch (TransformException e) {
            throw new DataSourceException("Unable to get resolution", e);
        } catch (FactoryException e) {
            throw new DataSourceException("Unable to get resolution", e);
        }
    }

    /**
     * Retrieves the {@link CoordinateReferenceSystem} for dataset pointed by this {@link AbstractGridCoverage2DReader}.
     * 
     * @return the {@link CoordinateReferenceSystem} for dataset pointed by this {@link AbstractGridCoverage2DReader}.
     * @deprecated use {@link #getCoordinateReferenceSystem()}
     */
    public final CoordinateReferenceSystem getCrs() {
        return getCoordinateReferenceSystem();
    }

    /**
     * Retrieves the {@link GeneralGridEnvelope} that represents the raster grid dimensions of the highest resolution level in this dataset.
     * 
     * @return the {@link GeneralGridEnvelope} that represents the raster grid dimensions of the highest resolution level in this dataset.
     */
    public GridEnvelope getOriginalGridRange() {
        return getOriginalGridRange(coverageName);
    }
    
    /**
     * Retrieves the {@link GeneralGridEnvelope} that represents the raster grid dimensions of the highest resolution level in this dataset.
     * 
     * @return the {@link GeneralGridEnvelope} that represents the raster grid dimensions of the highest resolution level in this dataset.
     */
    @Override
    public GridEnvelope getOriginalGridRange(String coverageName) {
        if (!checkName(coverageName)) {
            throw new IllegalArgumentException("The specified coverageName " + coverageName
                    + "is not supported");
        }
        assert originalGridRange.getDimension() == 2;
        return new GridEnvelope2D(originalGridRange.getLow(0), originalGridRange.getLow(1),
                originalGridRange.getSpan(0), originalGridRange.getSpan(1));

    }
    
    /**
     * Retrieves the {@link GeneralEnvelope} for this {@link AbstractGridCoverage2DReader}.
     * 
     * @return the {@link GeneralEnvelope} for this {@link AbstractGridCoverage2DReader}.
     */
    @Override
    public CoordinateReferenceSystem getCoordinateReferenceSystem() {
        // Default implementation for backwards compatibility
        return getCoordinateReferenceSystem(coverageName);
    }
    
    /**
     * Retrieves the {@link GeneralEnvelope} for this {@link AbstractGridCoverage2DReader}.
     * 
     * @return the {@link GeneralEnvelope} for this {@link AbstractGridCoverage2DReader}.
     */
    @Override
    public CoordinateReferenceSystem getCoordinateReferenceSystem(String coverageName) {
        if (!checkName(coverageName)) {
            throw new IllegalArgumentException("The specified coverageName " + coverageName
                    + "is not supported");
        }

        return crs;
    }

    /**
     * Retrieves the {@link GeneralEnvelope} for this {@link AbstractGridCoverage2DReader}.
     * 
     * @return the {@link GeneralEnvelope} for this {@link AbstractGridCoverage2DReader}.
     */
    public GeneralEnvelope getOriginalEnvelope() {
        return getOriginalEnvelope(coverageName);
    }

    /**
     * Retrieves the {@link GeneralEnvelope} for this {@link AbstractGridCoverage2DReader}.
     * 
     * @return the {@link GeneralEnvelope} for this {@link AbstractGridCoverage2DReader}.
     */
    @Override
    public GeneralEnvelope getOriginalEnvelope(String coverageName) {
        if (!checkName(coverageName)) {
            throw new IllegalArgumentException("The specified coverageName " + coverageName
                    + "is not supported");
        }
        return new GeneralEnvelope(originalEnvelope);
    }

    /**
     * Retrieves the original grid to world transformation for this {@link AbstractGridCoverage2DReader}.
     * 
     * @param pixInCell specifies the datum of the transformation we want.
     * @return the original grid to world transformation for this {@link AbstractGridCoverage2DReader}.
     */
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
            throw new IllegalArgumentException("The specified coverageName " + coverageName
                    + "is not supported");
        }
        synchronized (this) {
            if (raster2Model == null) {
                final GridToEnvelopeMapper geMapper = new GridToEnvelopeMapper(
                        getOriginalGridRange(coverageName), getOriginalEnvelope(coverageName));
                geMapper.setPixelAnchor(PixelInCell.CELL_CENTER);
                raster2Model = geMapper.createTransform();
            }
        }

        // we do not have to change the pixel datum
        if (pixInCell == PixelInCell.CELL_CENTER)
            return raster2Model;

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
    public final Object getSource() {
        return source;
    }

    /**
     * Disposes this reader.
     * 
     * <p>
     * This method just tries to close the underlying {@link ImageInputStream}.
     */
    public void dispose() {
        if (inStream != null && closeMe) {
            try {
                inStream.close();
            } catch (IOException e) {
                if (LOGGER.isLoggable(Level.FINE))
                    LOGGER.log(Level.FINE, e.getLocalizedMessage(), e);
            }
        }

    }

    /**
     * @see org.opengis.coverage.grid.GridCoverageReader#skip()
     * @deprecated no replacement for that method
     */
    public void skip() {
        throw new UnsupportedOperationException("Unsupported operation.");
    }

    /**
     * @see org.opengis.coverage.grid.GridCoverageReader#hasMoreGridCoverages()
     * @deprecated no replacement for that method
     */
    public boolean hasMoreGridCoverages() {
        throw new UnsupportedOperationException("Unsupported operation.");
    }

    /**
     * @deprecated use {@link #getGridCoverageNames()}
     */
    public String[] listSubNames() {
        return getGridCoverageNames();
    }

    @Override
    public String[] getGridCoverageNames() {
        return new String[] { coverageName };
    }

    /**
     * @see org.opengis.coverage.grid.GridCoverageReader#getCurrentSubname()
     * @deprecated no replacement for that method
     */
    public String getCurrentSubname() {
        throw new UnsupportedOperationException("Unsupported operation.");
    }

    public String[] getMetadataNames(final String coverageName) {
        if (!checkName(coverageName)) {
            throw new IllegalArgumentException("The specified coverageName " + coverageName
                    + "is not supported");
        }
        return getMetadataNames();
    }

    /**
     * @see org.opengis.coverage.grid.GridCoverageReader#getMetadataNames()
     */
    public String[] getMetadataNames() {
        return null;
    }

    /**
     * @see org.opengis.coverage.grid.GridCoverageReader#getMetadataValue(java.lang.String)
     */
    public String getMetadataValue(final String name) {
        return getMetadataValue(coverageName, name);
    }

    public String getMetadataValue(final String coverageName, final String name) {
        if (!checkName(coverageName)) {
            throw new IllegalArgumentException("The specified coverageName " + coverageName
                    + "is not supported");
        }
        return null;
    }

    /**
     * @see org.opengis.coverage.grid.GridCoverageReader#getGridCoverageCount()
     */
    public int getGridCoverageCount() {
        return 1;
    }

    /**
     * Information about this source.
     * <p>
     * Subclasses should provide additional format specific information.
     * 
     * @return ServiceInfo describing getSource().
     */
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
     * Returns a list of files making up the source data for this reader (as a whole). The default
     * implementation returns the source, if it can be made into a File object, or null otherwise.
     * 
     * @return
     * @throws IOException
     */
    protected List<FileGroup> getFiles() throws IOException {
        File file = getSourceAsFile();

        if (file == null) {
            return null;
        } else {
            return Collections.singletonList(new FileGroup(file, null, null));
        }
    }

    /**
     * Returns the source as a File, if it can be converted to one, and it exists
     * 
     * @return
     */
    protected File getSourceAsFile() {
        File file = null;
        if (source instanceof File) {
            file = (File) source;
        } else if (source instanceof URL) {
            File sf = DataUtilities.urlToFile((URL) source);
            if (sf.exists()) {
                file = sf;
            }
        }
        return file;
    }

    /**
     * Default implementation returns a FileResourceInfo containing same fileGroup list contained in the ServiceInfo object.
     * 
     * @param coverageName
     * @return
     */
    @Override
    public ResourceInfo getInfo(String coverageName) {

        ResourceInfo info = null;
        try {
            List<FileGroup> files = getFiles();
            if (files != null && !files.isEmpty()) {
                info  = new DefaultFileResourceInfo(files);
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
    protected void finalize() throws Throwable {
        dispose();
        super.finalize();
    }

    /**
     * Return the set of dynamic parameterDescriptors (the ones related to domains) for this reader. Default implementation returns an empty set of
     * parameters
     * 
     * @return
     */
    @SuppressWarnings("rawtypes")
    public Set<ParameterDescriptor<List>> getDynamicParameters() throws IOException {
        return getDynamicParameters(coverageName);
    }
    
    /**
     * Return the set of dynamic parameterDescriptors (the ones related to domains) for this reader. Default implementation returns an empty set of
     * parameters
     * 
     * @return
     */
    @SuppressWarnings("rawtypes")
    public Set<ParameterDescriptor<List>> getDynamicParameters(String coverageName)  throws IOException {
        return Collections.emptySet();
    }

    @Override
    public int getNumOverviews(String coverageName) {
        if (!checkName(coverageName)) {
            throw new IllegalArgumentException("The specified coverageName " + coverageName
                    + "is not supported");
        }
        if (dtLayout == null) {
            // Back to the default
            return numOverviews;
        }
        return dtLayout.getNumInternalOverviews()
                + (dtLayout.getNumExternalOverviews() > 0 ? dtLayout.getNumExternalOverviews() : 0);
    }

    @Override
    public int getNumOverviews() {
        // Default implementation for backwards compatibility
        return getNumOverviews(coverageName);
    }

    public DatasetLayout getDatasetLayout() {
        // Default implementation for backwards compatibility
        return getDatasetLayout(coverageName);
    }

    public DatasetLayout getDatasetLayout(String coverageName) {
        if (!checkName(coverageName)) {
            throw new IllegalArgumentException("The specified coverageName " + coverageName
                    + "is not supported");
        }
        return dtLayout;
    }

    public GridEnvelope getOverviewGridEnvelope(int overviewIndex) throws IOException {
        return getOverviewGridEnvelope(coverageName, overviewIndex);
    }

    public GridEnvelope getOverviewGridEnvelope(String coverageName, int overviewIndex)
            throws IOException {
        if (!checkName(coverageName)) {
            throw new IllegalArgumentException("The specified coverageName " + coverageName
                    + "is not supported");
        }

        // Default implementation for backwards compatibility
        return null;
    }

    @Override
    public ImageLayout getImageLayout(String coverageName) throws IOException {
        if (!checkName(coverageName)) {
            throw new IllegalArgumentException("The specified coverageName " + coverageName
                    + "is not supported");
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
        layout.setTileGridXOffset(0).setTileGridYOffset(0).setTileWidth(reader.getTileWidth(0))
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
            throw new IllegalArgumentException("The specified coverageName " + coverageName
                    + "is not supported");
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
            throw new IllegalArgumentException("The specified coverageName " + coverageName
                    + "is not supported");
        }
    	
		return highestRes;
	}
    
    double[] getHighestRes() {
		return getHighestRes(coverageName);
	}
    
    /**
     * Return the ground control points for the default coverage, or null if there are none
     * 
     * @return
     */
    public GroundControlPoints getGroundControlPoints() {
        return null;
    }

    /**
     * Return the ground control points for the specified, or null if there are none
     * 
     * @return
     */
    public GroundControlPoints getGroundControlPoints(String coverageName) {
        if (!checkName(coverageName)) {
            throw new IllegalArgumentException("The specified coverageName " + coverageName
                    + "is not supported");
        }
        return null;
    }
    
    /**
     * Computes the raster to model of a rescaled output raster, based on the original transform and
     * output raster scaling factor
     * 
     * @param coverageRaster
     * @return
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

    /**
     * Retrieves the sibling of the specified file, if available, or null otherwise
     * 
     * @param file
     * @param extension
     * @return
     */
    protected static File getSibling(final File file, String extension) {
        String parentPath = file.getParent();
        String filename = file.getName();
        final int i = filename.lastIndexOf('.');
        filename = (i == -1) ? filename : filename.substring(0, i);

        // getting name and extension
        final String base = (parentPath != null) ? new StringBuilder(parentPath)
                .append(File.separator).append(filename).toString() : filename;

        // We can now construct the baseURL from this string.
        File file2Parse = new File(new StringBuilder(base).append(extension).toString());
        if (file2Parse.exists()) {
            return file2Parse;
        } else {
            return null;
        }
    }

    /**
     * Adds all the siblings that could be found to exist to the given file list
     * 
     * @param file
     * @param extension
     * @return
     */
    protected void addAllSiblings(final File file, List<File> files, String... extensions) {
        String parentPath = file.getParent();
        String filename = file.getName();
        final int i = filename.lastIndexOf('.');
        filename = (i == -1) ? filename : filename.substring(0, i);

        // getting name and extension
        final String base = (parentPath != null) ? new StringBuilder(parentPath)
                .append(File.separator).append(filename).toString() : filename;

        // We can now construct the baseURL from this string.
        for (String extension : extensions) {
            File file2Parse = new File(new StringBuilder(base).append(extension)
                    .toString());
            if (file2Parse.exists()) {
                files.add(file2Parse);
            }
        }
    }

    /**
     * Adds the specified siblings, if not null, and existing
     * 
     * @param files
     * @param siblings
     */
    protected void addSiblings(List<File> files, File... siblings) {
        for (File sibling : siblings) {
            if (sibling != null && sibling.exists()) {
                files.add(sibling);
            }
        }
    }

    protected MultiLevelROIProvider getMultiLevelROIProvider (String coverageName) {
        throw new UnsupportedOperationException("The abstract reader doesn't implement this method yet");
    }

}
