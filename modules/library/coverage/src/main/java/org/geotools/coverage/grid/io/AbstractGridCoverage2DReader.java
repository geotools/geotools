/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.ColorModel;
import java.awt.image.SampleModel;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.spi.ImageInputStreamSpi;
import javax.imageio.stream.ImageInputStream;
import javax.media.jai.PlanarImage;

import org.geotools.coverage.CoverageFactoryFinder;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.TypeMap;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.data.DataSourceException;
import org.geotools.data.DefaultServiceInfo;
import org.geotools.data.ServiceInfo;
import org.geotools.factory.GeoTools;
import org.geotools.factory.Hints;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.operation.builder.GridToEnvelopeMapper;
import org.geotools.referencing.operation.transform.IdentityTransform;
import org.geotools.referencing.operation.transform.ProjectiveTransform;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Errors;
import org.geotools.util.logging.Logging;
import org.opengis.coverage.ColorInterpretation;
import org.opengis.coverage.grid.Format;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.coverage.grid.GridCoverageReader;
import org.opengis.coverage.grid.GridEnvelope;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.InvalidParameterNameException;
import org.opengis.parameter.InvalidParameterValueException;
import org.opengis.parameter.ParameterNotFoundException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.datum.PixelInCell;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

/**
 * This class is a first attempt for providing a way to get more informations
 * out of a single 2D raster datasets (x,y). It is worth to remark that for the
 * moment this is thought for 2D rasters not for 3D or 4D rasters (x,y,z,t).
 * 
 * <p>
 * The main drawback I see with the current GeoApi GridCoverageReader interface
 * is that there is no way to get real information about a raster source unless
 * you instantiate a GridCoverage. As an instance it is impossible to know the
 * envelope, the number of overviews, the tile size. This information is needed
 * in order to perform decimation on reading or to use built-in overviews<br>
 * This really impacts the ability to exploit raster datasets in a desktop
 * environment where caching is crucial.
 * 
 * @author Simone Giannecchini, GeoSolutions
 * @since 2.3
 */
public abstract class AbstractGridCoverage2DReader implements GridCoverageReader {

    /** The {@link Logger} for this {@link AbstractGridCoverage2DReader}. */
    private final static Logger LOGGER = Logging.getLogger("org.geotools.data.coverage.grid");

	public static final double EPS = 1E-6;

	/**
	 * This contains the  number of overviews.aaa
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
	 * In case we are trying to read from a GZipped file this will be set to
	 * true.
	 */
	protected boolean gzipped;

	/**
	 * The original {@link GridRange} for the {@link GridCoverage2D} of this
	 * reader.
	 */
	protected GridEnvelope originalGridRange = null;

	/**
	 * Input stream that can be used to initialize subclasses of
	 * {@link AbstractGridCoverage2DReader}.
	 */
	protected ImageInputStream inStream = null;

	/** Resolutions avialaible through an overviews based mechanism. */
	protected double[][] overViewResolutions = null;

	/**
	 * {@link GridCoverageFactory} instance.
	 */
	protected GridCoverageFactory coverageFactory;

	private ArrayList<Resolution> resolutionsLevels;

    protected ImageInputStreamSpi inStreamSPI;

	
 
    /**
     * Default protected constructor. Useful for wrappers.
     */
    protected AbstractGridCoverage2DReader() {
        
    }
    /**
     * Creates a new instance of a {@link AIGReader}. I assume nothing about file extension.
     * 
     * @param input
     *            Source object for which we want to build an {@link AIGReader}.
     * @throws DataSourceException
     */
    public AbstractGridCoverage2DReader(Object input) throws DataSourceException {
        this(input, null);
    }

    /**
     * Creates a new instance of a {@link AIGReader}. I assume nothing about file extension.
     * 
     * @param input
     *            Source object for which we want to build an {@link AIGReader}.
     * @param hints
     *            Hints to be used by this reader throughout his life.
     * @throws DataSourceException
     */
    public AbstractGridCoverage2DReader(Object input, Hints hints) throws DataSourceException {
        
        // 
        // basic management of hints
        //
        if (hints == null)
            this.hints = new Hints();
        if (hints != null) {
            this.hints.add(hints);

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
            final IOException ex = new IOException(Errors.format(ErrorKeys.NULL_ARGUMENT_$1,"input"));
            throw new DataSourceException(ex);
        }
        this.source = input;
       
    }
	    
    /**
     * Read the current grid coverage from the stream.
     * <p>
     * Example:<pre><code>
     * </code></pre>
     * The method {@link #hasMoreGridCoverages} should be invoked first in order to verify that a
     * coverage is available.
     * 
     * @param parameters Optional parameters matching {@link Format#getReadParameters}.
     * @return a {@linkplain GridCoverage grid coverage} from the input source.
     * @throws InvalidParameterNameException
     *             if a parameter in {@code parameters} doesn't have a recognized name.
     * @throws InvalidParameterValueException
     *             if a parameter in {@code parameters} doesn't have a valid value.
     * @throws ParameterNotFoundException
     *             if a parameter was required for the operation but was not provided in the {@code
     *             parameters} list.
     * @throws CannotCreateGridCoverageException
     *             if the coverage can't be created for a logical reason (for example an unsupported
     *             format, or an inconsistency found in the data).
     * @throws IOException
     *             if a read operation failed for some other input/output reason, including
     *             {@link FileNotFoundException} if no file with the given {@code name} can be
     *             found, or {@link javax.imageio.IIOException} if an error was thrown by the
     *             underlying image library.
     */
    public abstract GridCoverage2D read(GeneralParameterValue[] parameters)
            throws IllegalArgumentException, IOException;

	// -------------------------------------------------------------------------
	//
	// old support methods
	//
	// -------------------------------------------------------------------------
	
	/**
	 * This method is responsible for preparing the read param for doing an
	 * {@link ImageReader#read(int, ImageReadParam)}.
	 * 
	 * 
	 * <p>
	 * This method is responsible for preparing the read param for doing an
	 * {@link ImageReader#read(int, ImageReadParam)}. It sets the passed
	 * {@link ImageReadParam} in terms of decimation on reading using the
	 * provided requestedEnvelope and requestedDim to evaluate the needed
	 * resolution. It also returns and {@link Integer} representing the index of
	 * the raster to be read when dealing with multipage raster.
	 * 
	 * @param overviewPolicy
	 *            it can be one of {@link Hints#VALUE_OVERVIEW_POLICY_IGNORE},
	 *            {@link Hints#VALUE_OVERVIEW_POLICY_NEAREST},
	 *            {@link Hints#VALUE_OVERVIEW_POLICY_QUALITY} or
	 *            {@link Hints#VALUE_OVERVIEW_POLICY_SPEED}. It specifies the
	 *            policy to compute the overviews level upon request.
	 * @param readP
	 *            an instance of {@link ImageReadParam} for setting the
	 *            subsampling factors.
	 * @param requestedEnvelope
	 *            the {@link GeneralEnvelope} we are requesting.
	 * @param requestedDim
	 *            the requested dimensions.
	 * @return the index of the raster to read in the underlying data source.
	 * @throws IOException
	 * @throws TransformException
	 */
	protected Integer setReadParams(OverviewPolicy overviewPolicy,ImageReadParam readP,
			GeneralEnvelope requestedEnvelope, Rectangle requestedDim)
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
		if(overviewPolicy == null)
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
		final boolean useOverviews = (numOverviews >0) ? true : false;

		// //
		//
		// Resolution requested. I am here computing the resolution required by
		// the user.
		//
		// //
		double[] requestedRes = getResolution(requestedEnvelope, requestedDim,crs);
		if (requestedRes == null)
			return imageChoice;

		// //
		//
		// overviews or decimation
		//
		// //
		if (useOverviews) 
			imageChoice= pickOverviewLevel(overviewPolicy, requestedRes);
		
		// /////////////////////////////////////////////////////////////////////
		// DECIMATION ON READING
		// /////////////////////////////////////////////////////////////////////
		decimationOnReadingControl(imageChoice, readP, requestedRes);
		return imageChoice;

	}

	/**
	 * This method is responsible for checking the overview policy as defined by
	 * the provided {@link Hints}.
	 * 
	 * @return the overview policy which can be one of
	 *         {@link Hints#VALUE_OVERVIEW_POLICY_IGNORE},
	 *         {@link Hints#VALUE_OVERVIEW_POLICY_NEAREST},
	 *         {@link Hints#VALUE_OVERVIEW_POLICY_SPEED}, {@link Hints#VALUE_OVERVIEW_POLICY_QUALITY}.
	 *         Default is {@link Hints#VALUE_OVERVIEW_POLICY_NEAREST}.
	 */
	private OverviewPolicy extractOverviewPolicy() {
		OverviewPolicy overviewPolicy=null;
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
	
	private Integer pickOverviewLevel(OverviewPolicy policy, double[] requestedRes) {
	    // setup policy
        if(policy == null)
        	policy=extractOverviewPolicy();
        
        // sort resolutions from smallest pixels (higher res) to biggest pixels (higher res)
        // keeping a reference to the original image choice
        synchronized (this) {
        	if(resolutionsLevels==null){
        	 resolutionsLevels = new ArrayList<Resolution>();
        	 //note that we assume what follows:
        	 // -highest resolution image is at level 0.
        	 // -all the overviews share the same envelope
        	 // -the aspect ratio for the overviews is constant
        	 // -the provided resolutions are taken directly from the grid
        	 resolutionsLevels.add(new Resolution(1,highestRes[0],highestRes[1], 0));
        	 if(numOverviews>0)
        	 {
	             for (int i = 0; i < overViewResolutions.length; i++) 
	            	 resolutionsLevels.add(new Resolution(overViewResolutions[i][0]/highestRes[0],overViewResolutions[i][0],overViewResolutions[i][1] , i+1));
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
        final Resolution max = (Resolution) resolutionsLevels.get(0);
		final double requestedScaleFactorX = reqx / max.resolutionX;
		final double requestedScaleFactorY = reqy / max.resolutionY;
		final int leastReduceAxis = requestedScaleFactorX <= requestedScaleFactorY ? 0
				: 1;
		final double requestedScaleFactor = leastReduceAxis == 0 ? requestedScaleFactorX
				: requestedScaleFactorY;
        
        
		// are we looking for a resolution even higher than the native one?
        if(requestedScaleFactor<=1)
            return max.imageChoice;
        // are we looking for a resolution even lower than the smallest overview?
        final Resolution min = (Resolution) resolutionsLevels.get(resolutionsLevels.size() - 1);
        if(requestedScaleFactor>=min.scaleFactor)
            return min.imageChoice;
        // Ok, so we know the overview is between min and max, skip the first
        // and search for an overview with a resolution lower than the one requested,
        // that one and the one from the previous step will bound the searched resolution
        Resolution prev = max;
        final int size=resolutionsLevels.size();
        for (int i = 1; i <size; i++) {
            final Resolution curr = resolutionsLevels.get(i);
            // perfect match check
            if(curr.scaleFactor==requestedScaleFactor) {
                return curr.imageChoice;
            }
            
            // middle check. The first part of the condition should be sufficient, but
            // there are cases where the x resolution is satisfied by the lowest resolution, 
            // the y by the one before the lowest (so the aspect ratio of the request is 
            // different than the one of the overviews), and we would end up going out of the loop
            // since not even the lowest can "top" the request for one axis 
            if(curr.scaleFactor>requestedScaleFactor|| i == size - 1) {
                if(policy == OverviewPolicy.QUALITY)
                    return prev.imageChoice;
                else if(policy == OverviewPolicy.SPEED)
                    return curr.imageChoice;
                else if(requestedScaleFactor - prev.scaleFactor < curr.scaleFactor - requestedScaleFactor)
                    return prev.imageChoice;
                else
                    return curr.imageChoice;
            }
            prev = curr;
        }
        //fallback
        return max.imageChoice;
    }
	
	/**
	 * Returns the actual resolution used to read the data given the specified target resolution
	 * and the specified overview policy
	 * @param policy
	 * @param resolutions
	 * @return
	 */
	public double[] getReadingResolutions(OverviewPolicy policy, double[] requestedResolution) {
	    // find the target resolution level
	    double[] result;
	    if(numOverviews > 0) {
	        int imageIdx = pickOverviewLevel(policy, requestedResolution);
	        result = overViewResolutions[imageIdx];
	    } else {
	        result = highestRes;
	    }
	    
	    // return via cloning to protect internal state
	    double[] clone = new double[result.length];
	    System.arraycopy(result, 0, clone, 0, result.length);
	    return clone;
	}
	
	/**
	 * Simple support class for sorting overview resolutions
	 * @author Andrea Aime
	 * @author Simone Giannecchini, GeoSolutions.
	 * @since 2.5
	 */
	private static class Resolution implements Comparable<Resolution> {
	    double scaleFactor;

	    double  resolutionX;
	    double resolutionY;
	    int imageChoice;
	    
        public Resolution(
        		final double scaleFactor,
        		final double resolutionX,
        		final double resolutionY,
        		int imageChoice) {
            this.scaleFactor = scaleFactor;
            this.resolutionX=resolutionX;
            this.resolutionY=resolutionY;
            this.imageChoice = imageChoice;
        }
	    
	    public int compareTo(Resolution other) {
	        if(scaleFactor > other.scaleFactor)
	            return 1;
	        else if(scaleFactor < other.scaleFactor)
	            return -1;
	        else 
	        	return 0;
	    }
	    
	    public String toString() {
	        return "Resolution[Choice=" + imageChoice + ",scaleFactor=" + scaleFactor + "]";
	    }
	}
	

	/**
	 * This method is responsible for evaluating possible subsampling factors
	 * once the best resolution level has been found, in case we have support
	 * for overviews, or starting from the original coverage in case there are
	 * no overviews available.
	 * 
	 * Anyhow this method should not be called directly but subclasses should
	 * make use of the setReadParams method instead in order to transparently
	 * look for overviews.
	 * 
	 * @param imageChoice
	 * @param readP
	 * @param requestedRes
	 */
	protected final void decimationOnReadingControl(Integer imageChoice,
			ImageReadParam readP, double[] requestedRes) {
		{

			int w, h;
			double selectedRes[] = new double[2];
			final int choice = imageChoice.intValue();
			if (choice == 0) {
				// highest resolution
				w = originalGridRange.getSpan(0);
				h = originalGridRange.getSpan(1);
				selectedRes[0] = highestRes[0];
				selectedRes[1] = highestRes[1];
			} else {
				// some overview
				selectedRes[0] = overViewResolutions[choice - 1][0];
				selectedRes[1] = overViewResolutions[choice - 1][1];
				w = (int) Math.round(originalEnvelope.getSpan(0)/ selectedRes[0]);
				h = (int) Math.round(originalEnvelope.getSpan(1)/ selectedRes[1]);

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
				int subSamplingFactorX = (int) Math.floor(requestedRes[0]/ selectedRes[0]);
				subSamplingFactorX = subSamplingFactorX == 0 ? 1: subSamplingFactorX;

				while (w / subSamplingFactorX <= 0 && subSamplingFactorX >= 0)subSamplingFactorX--;
				subSamplingFactorX = subSamplingFactorX == 0 ? 1: subSamplingFactorX;

				int subSamplingFactorY = (int) Math.floor(requestedRes[1]/ selectedRes[1]);
				subSamplingFactorY = subSamplingFactorY == 0 ? 1: subSamplingFactorY;

				while (h / subSamplingFactorY <= 0 && subSamplingFactorY >= 0)
					subSamplingFactorY--;
				subSamplingFactorY = subSamplingFactorY == 0 ? 1: subSamplingFactorY;

				readP.setSourceSubsampling(subSamplingFactorX,subSamplingFactorY, 0, 0);
			}

		}
	}

	/**
	 * Creates a {@link GridCoverage} for the provided {@link PlanarImage} using
	 * the {@link #originalEnvelope} that was provided for this coverage.
	 * 
	 * @param image
	 *            contains the data for the coverage to create.
	 * @return a {@link GridCoverage}
	 * @throws IOException
	 */
	protected final GridCoverage createImageCoverage(PlanarImage image)
			throws IOException {
		return createImageCoverage(image, null);

	}

	/**
	 * Creates a {@link GridCoverage} for the provided {@link PlanarImage} using
	 * the {@link #raster2Model} that was provided for this coverage.
	 * 
	 * <p>
	 * This method is vital when working with coverages that have a raster to
	 * model transformation that is not a simple scale and translate.
	 * 
	 * @param image
	 *            contains the data for the coverage to create.
	 * @param raster2Model
	 *            is the {@link MathTransform} that maps from the raster space
	 *            to the model space.
	 * @return a {@link GridCoverage}
	 * @throws IOException
	 */
	protected final GridCoverage2D createImageCoverage(PlanarImage image,
			MathTransform raster2Model) throws IOException {

		// creating bands
        final SampleModel sm=image.getSampleModel();
        final ColorModel cm=image.getColorModel();
		final int numBands = sm.getNumBands();
		final GridSampleDimension[] bands = new GridSampleDimension[numBands];
		// setting bands names.
		for (int i = 0; i < numBands; i++) {
		        final ColorInterpretation colorInterpretation=TypeMap.getColorInterpretation(cm, i);
		        if(colorInterpretation==null)
		               throw new IOException("Unrecognized sample dimension type");
			bands[i] = new GridSampleDimension(colorInterpretation.name()).geophysics(true);
		}
		// creating coverage
		if (raster2Model != null) {
			return coverageFactory.create(coverageName, image, crs,
					raster2Model, bands, null, null);
		}
		return coverageFactory.create(coverageName, image, new GeneralEnvelope(
				originalEnvelope), bands, null, null);

	}


	/**
	 * This method is responsible for computing the resolutions in for the
	 * provided grid geometry in the provided crs.
	 * 
	 * <P>
	 * It is worth to note that the returned resolution array is of length of 2
	 * and it always is lon, lat for the moment.<br>
	 * It might be worth to remove the axes reordering code when we are
	 * confident enough with the code to handle the north-up crs.
	 * <p>
	 * TODO use orthodromic distance?
	 * 
	 * @param envelope
	 *            the GeneralEnvelope
	 * @param dim
	 * @param crs
	 * @throws DataSourceException
	 */
	protected final static double[] getResolution(GeneralEnvelope envelope,
			Rectangle2D dim, CoordinateReferenceSystem crs)
			throws DataSourceException {
		double[] requestedRes = null;
		try {
			if (dim != null && envelope != null&&crs!=null) {
				// do we need to transform the originalEnvelope?
				final CoordinateReferenceSystem crs2D = CRS.getHorizontalCRS(
						envelope.getCoordinateReferenceSystem());
				if (crs2D != null && !CRS.equalsIgnoreMetadata(crs, crs2D)) {
					final MathTransform tr = CRS.findMathTransform(crs2D, crs,true);
					if (!tr.isIdentity())
					{
						envelope = CRS.transform(tr, envelope);
						envelope.setCoordinateReferenceSystem(crs);
					}
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
	 * Retrieves the {@link CoordinateReferenceSystem} for dataset pointed by
	 * this {@link AbstractGridCoverage2DReader}.
	 * 
	 * @return the {@link CoordinateReferenceSystem} for dataset pointed by this
	 *         {@link AbstractGridCoverage2DReader}.
	 */
	public final CoordinateReferenceSystem getCrs() {
		return crs;
	}

	/**
	 * Retrieves the {@link GeneralGridRange} that represents the raster grid
	 * dimensions of the highest resolution level in this dataset.
	 * 
	 * @return the {@link GeneralGridRange} that represents the raster grid
	 *         dimensions of the highest resolution level in this dataset.
	 */
	public final GridEnvelope getOriginalGridRange() {
		return originalGridRange;
	}

	/**
	 * Retrieves the {@link GeneralEnvelope} for this
	 * {@link AbstractGridCoverage2DReader}.
	 * 
	 * @return the {@link GeneralEnvelope} for this
	 *         {@link AbstractGridCoverage2DReader}.
	 */
	public final GeneralEnvelope getOriginalEnvelope() {
		return originalEnvelope;
	}
	
	/**
	 * Retrieves the original grid to world transformation for this
	 * {@link AbstractGridCoverage2DReader}.
	 * 
	 * @param pixInCell specifies the datum of the transformation we want.
	 * @return the original grid to world transformation for this
	 *         {@link AbstractGridCoverage2DReader}.
	 */
	public final MathTransform getOriginalGridToWorld(final PixelInCell pixInCell) {
	    synchronized (this) {
	        if(raster2Model==null){
	            final GridToEnvelopeMapper geMapper= new GridToEnvelopeMapper(this.originalGridRange,this.originalEnvelope);
	            geMapper.setPixelAnchor(PixelInCell.CELL_CENTER);
	            raster2Model=geMapper.createTransform();
	        }
	    }

	    //we do not have to change the pixel datum
	    if( pixInCell==PixelInCell.CELL_CENTER)
	        return raster2Model;

	    //we do have to change the pixel datum
	    if(raster2Model instanceof AffineTransform){
	        final AffineTransform tr= new AffineTransform((AffineTransform) raster2Model);
	        tr.concatenate(AffineTransform.getTranslateInstance(-0.5,-0.5));
	        return ProjectiveTransform.create(tr);
	    }
	    if(raster2Model instanceof IdentityTransform){
	        final AffineTransform tr= new AffineTransform(1,0,0,1,0,0);
	        tr.concatenate(AffineTransform.getTranslateInstance(-0.5,-0.5));
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
		if (inStream != null&&closeMe ) {
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
	 */
	public void skip() {
		throw new UnsupportedOperationException("Unsupported operation.");
	}

	/**
	 * @see org.opengis.coverage.grid.GridCoverageReader#hasMoreGridCoverages()
	 */
	public boolean hasMoreGridCoverages() {
		throw new UnsupportedOperationException("Unsupported operation.");
	}

	/**
	 * @see org.opengis.coverage.grid.GridCoverageReader#listSubNames()
	 */
	public String[] listSubNames() {
		throw new UnsupportedOperationException("Unsupported operation.");
	}

	/**
	 * @see org.opengis.coverage.grid.GridCoverageReader#getCurrentSubname()
	 */
	public String getCurrentSubname() {
		throw new UnsupportedOperationException("Unsupported operation.");
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
		return null;
	}

	/**
	 * @see org.opengis.coverage.grid.GridCoverageReader#getGridCoverageCount()
	 */
	public int getGridCoverageCount() {
		throw new UnsupportedOperationException("Unsupported operation.");
	}
	
	

	/**
    * Information about this source.
    * <p>
    * Subclasses should provide additional format specific information.
    * 
    * @return ServiceInfo describing getSource().
    */
   public ServiceInfo getInfo(){
       DefaultServiceInfo info = new DefaultServiceInfo();
       info.setDescription( source == null? null : String.valueOf(source) );
       if( source instanceof URL ){
           URL url = (URL) source;
           info.setTitle( url.getFile() );
           try {
               info.setSource( url.toURI() );
           } catch (URISyntaxException e) {               
           }
       }
       else if( source instanceof File ){
           File file = (File) source;
           String filename = file.getName();
           if( filename == null || filename.length() == 0 ){
               info.setTitle( file.getName() );
           }
           info.setSource( file.toURI() );
       }
       return info;
   }
   
//   /**
//    * Information about the named gridcoveage.
//    * 
//    * @param subname Name indicing grid coverage to describe
//    * @return ResourceInfo describing grid coverage indicated
//    */
//   public ResourceInfo getInfo( String subname ){
//       DefaultResourceInfo info = new DefaultResourceInfo();
//       info.setName( subname );
//       info.setBounds( new ReferencedEnvelope( this.getOriginalEnvelope()));
//       info.setCRS( this.getCrs() );
//       info.setTitle( subname );
//       return info;
//   }
   
   /**
	 * Forcing disposal of this {@link AbstractGridCoverage2DReader} which may
	 * keep an {@link ImageInputStream} open.
	 */
	@Override
	protected void finalize() throws Throwable {
		dispose();
		super.finalize();
	}
}
