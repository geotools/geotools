/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2015, Open Source Geospatial Foundation (OSGeo)
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
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.MultiPixelPackedSampleModel;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.media.jai.operator.ConstantDescriptor;

import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.TypeMap;
import org.geotools.coverage.grid.GeneralGridEnvelope;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.grid.io.OverviewPolicy;
import org.geotools.coverageio.jp2k.RasterManager.OverviewLevel;
import org.geotools.data.DataSourceException;
import org.geotools.factory.Hints;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.image.ImageWorker;
import org.geotools.referencing.CRS;
import org.geotools.referencing.operation.transform.AffineTransform2D;
import org.geotools.resources.coverage.CoverageUtilities;
import org.geotools.resources.image.ImageUtilities;
import org.opengis.coverage.ColorInterpretation;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.geometry.BoundingBox;
import org.opengis.referencing.datum.PixelInCell;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.MathTransform2D;
import org.opengis.referencing.operation.TransformException;

import com.sun.media.jai.codecimpl.util.ImagingException;
/**
 * A RasterLayerResponse. An instance of this class is produced everytime a
 * requestCoverage is called to a reader.
 * 
 * @author Daniele Romagnoli, GeoSolutions S.A.S.
 * @author Simone Giannecchini, GeoSolutions S.A.S.
 */
class RasterLayerResponse{
	
	/**
	 * 
	 * @author Simone Giannecchini, GeoSolutions SAS
	 *
	 */
	class GranuleLoader {
		final ReferencedEnvelope cropBBox;
		
		final MathTransform2D worldToGrid;
		
		final Granule granule;
		
		final ImageReadParam readParameters;
		
		final int imageIndex;

		final Dimension tilesDimension;
		
		GranuleLoader(
				final ImageReadParam readParameters, 
				final int imageIndex,
				final ReferencedEnvelope cropBBox, 
				final MathTransform2D worldToGrid,
				final Granule granule,
				final Dimension tilesDimension) {
			this.readParameters = ImageUtilities.cloneImageReadParam(readParameters);
			this.imageIndex = imageIndex;
			this.cropBBox = cropBBox;
			this.worldToGrid = worldToGrid;
			this.granule = granule;
			this.tilesDimension= tilesDimension!=null?(Dimension) tilesDimension.clone():null;
		}
		
		public BoundingBox getCropBBox() {
			return cropBBox;
		}


		public MathTransform2D getWorldToGrid() {
			return worldToGrid;
		}


		public Granule getGranule() {
			return granule;
		}


		public ImageReadParam getReadParameters() {
			return readParameters;
		}


		public int getImageIndex() {
			return imageIndex;
		}
		
		public RenderedImage loadGranule() throws IOException {
			
			return granule.loadRaster(readParameters, imageIndex, cropBBox, worldToGrid, request,tilesDimension);
		}

	}
	
	class GranuleWorker {
		private int granulesNumber;
		
		public void produce(){
			Granule granule = rasterManager.getGranule();

			//
			// load raster data
			//
			//create a granule loader
			final GranuleLoader loader = new GranuleLoader(baseReadParameters, imageChoice, bbox, finalWorldToGridCorner,granule,request.getTileDimensions());

			Color inputTransparentColor = request.getInputTransparentColor();
			boolean doInputTransparency = inputTransparentColor != null;

			final RenderedImage loadedImage;
			try {
				loadedImage=loader.loadGranule();
				if(loadedImage==null)
				{
					if(LOGGER.isLoggable(Level.FINE))
						LOGGER.log(Level.FINE,"Unable to load the raster with request "+request.toString());
					return;
				}

				//
				// We check here if the images have an alpha channel or some
				// other sort of transparency. In case we have transparency
				// I also save the index of the transparent channel.
				//
				final ColorModel cm = loadedImage.getColorModel();
				alphaIn = cm.hasAlpha();
			} catch (IOException e) {
				LOGGER.log(Level.SEVERE,"Unable to load the raster" ,e);
				return;
			}
			catch (ImagingException|javax.media.jai.util.ImagingException  e) {
				LOGGER.log(Level.FINE,"Loading image  failed, original request was " + request, e);
				return;
			}

			final RenderedImage raster = processGranuleRaster(
					loadedImage,
					doInputTransparency,
					inputTransparentColor);

			theImage = raster;

			//increment index
			granulesNumber = 1;
		}
	}

	/** Logger. */
	private final static Logger LOGGER = org.geotools.util.logging.Logging.getLogger(RasterLayerResponse.class);
	
	/**
	 * The GridCoverage produced after a {@link #processRequest()} method call
	 */
	private GridCoverage2D gridCoverage;

	/** The {@link RasterLayerRequest} originating this response */
	private RasterLayerRequest request;

	/** The coverage factory producing a {@link GridCoverage} from an image */
	private GridCoverageFactory coverageFactory;

	/** The base envelope related to the input coverage */
	private GeneralEnvelope coverageEnvelope;

	private boolean frozen = false;

	private RasterManager rasterManager;

	private Color transparentColor;

	private RenderedImage theImage;

	private ReferencedEnvelope bbox;

	private Rectangle rasterBounds;

	private MathTransform2D finalGridToWorldCorner;

	private MathTransform2D finalWorldToGridCorner;

	private int imageChoice=0;

	private ImageReadParam baseReadParameters= new ImageReadParam();

	private boolean alphaIn=false;

	private MathTransform baseGridToWorld;

	/**
	 * Construct a {@code RasterLayerResponse} given a specific
	 * {@link RasterLayerRequest}, a {@code GridCoverageFactory} to produce
	 * {@code GridCoverage}s and an {@code ImageReaderSpi} to be used for
	 * instantiating an Image Reader for a read operation,
	 * 
	 * @param request
	 *            a {@link RasterLayerRequest} originating this response.
	 * @param coverageFactory
	 *            a {@code GridCoverageFactory} to produce a {@code
	 *            GridCoverage} when calling the {@link #compute()} method.
	 * @param readerSpi
	 *            the Image Reader Service provider interface.
	 */
	public RasterLayerResponse(final RasterLayerRequest request, final RasterManager rasterManager) {
		this.request = request;
		coverageEnvelope = rasterManager.getCoverageEnvelope();
		this.coverageFactory = rasterManager.getCoverageFactory();
		this.rasterManager = rasterManager;
		baseGridToWorld=rasterManager.getRaster2Model();
		transparentColor=request.getInputTransparentColor();
	}

	/**
	 * Compute the coverage request and produce a grid coverage which will be
	 * returned by {@link #createResponse()}. The produced grid coverage may be
	 * {@code null} in case of empty request.
	 * 
	 * @return the {@link GridCoverage} produced as computation of this response
	 *         using the {@link #processRequest()}  method.
	 * @throws IOException
	 * @uml.property name="gridCoverage"
	 */
	public GridCoverage2D createResponse() throws IOException {
		processRequest();
		return gridCoverage;
	}

	/**
	 * @return the {@link RasterLayerRequest} originating this response.
	 * 
	 * @uml.property name="request"
	 */
	public RasterLayerRequest getOriginatingCoverageRequest() {
		return request;
	}

	/**
	 * This method creates the GridCoverage2D from the underlying file given a
	 * specified envelope, and a requested dimension.
	 * 
	 * @param iUseJAI
	 *            specify if the underlying read process should leverage on a
	 *            JAI ImageRead operation or a simple direct call to the {@code
	 *            read} method of a proper {@code ImageReader}.
	 * @param overviewPolicy
	 *            the overview policy which need to be adopted
	 * @return a {@code GridCoverage}
	 * 
	 * @throws java.io.IOException
	 */
	private synchronized void processRequest() throws IOException {
		if (request.isEmpty())
			throw new IOException("Empty request " + request.toString());

		if (frozen)
			return;
		
		// assemble granules
		final RenderedImage image = assembleGranules();
		
		RenderedImage finalRaster = postProcessRaster(image);
		
		//create the coverage
		gridCoverage=prepareCoverage(finalRaster);
		
		//freeze
		frozen = true;
	}

	/**
	 * This method loads the granules which overlap the requested
	 * {@link GeneralEnvelope} using the provided values for alpha and input
	 * ROI.
	 */
	private RenderedImage assembleGranules() throws DataSourceException {
		try {
			// select the relevant overview, notice that at this time we have
			// relaxed a bit the requirement to have the same exact resolution
			// for all the overviews, but still we do not allow for reading the
			// various grid to world transform directly from the input files,
			// therefore we are assuming that each granule has a scale and
			// translate only grid to world that can be deduced from its base
			// level dimension and envelope. The grid to world transforms for
			// the other levels can be computed accordingly knowning the scale
			// factors.
			
			if (request.getRequestedBBox() != null && request.getRequestedRasterArea() != null)
				imageChoice = setReadParams(request.getOverviewPolicy(), baseReadParameters,request);
			else
				imageChoice = 0;
			assert imageChoice>=0;
			if (LOGGER.isLoggable(Level.FINE)) {
				LOGGER.fine(new StringBuffer("Loading level ").append(
						imageChoice).append(" with subsampling factors ")
						.append(baseReadParameters.getSourceXSubsampling()).append(" ")
						.append(baseReadParameters.getSourceYSubsampling()).toString());
			}
			
			final BoundingBox cropBBOX = request.getCropBBox();
			if (cropBBOX != null)
				bbox = ReferencedEnvelope.reference(cropBBOX);
			else
				bbox = new ReferencedEnvelope(coverageEnvelope);
			
			//compute final world to grid
			// base grid to world for the center of pixels
			final AffineTransform g2w = new AffineTransform((AffineTransform) baseGridToWorld);
			// move it to the corner
			g2w.concatenate(CoverageUtilities.CENTER_TO_CORNER);
			
			//keep into account overviews and subsampling
			final OverviewLevel level = rasterManager.overviewsController.resolutionsLevels.get(imageChoice);
			final OverviewLevel baseLevel = rasterManager.overviewsController.resolutionsLevels.get(0);
			final AffineTransform2D adjustments = new AffineTransform2D(
					(level.resolutionX/baseLevel.resolutionX)*baseReadParameters.getSourceXSubsampling(),
					0,
					0,
					(level.resolutionY/baseLevel.resolutionY)*baseReadParameters.getSourceYSubsampling(),
					0,
					0);
			g2w.concatenate(adjustments);
			finalGridToWorldCorner = new AffineTransform2D(g2w);
			finalWorldToGridCorner = finalGridToWorldCorner.inverse();// compute raster bounds
			rasterBounds=new GeneralGridEnvelope(CRS.transform(finalWorldToGridCorner, bbox),PixelInCell.CELL_CORNER,false).toRectangle();
			
			final GranuleWorker worker = new GranuleWorker();
			worker.produce();
			
			//
			// Did we actually load anything?? 
			//
			if (worker.granulesNumber>=1) {

				if (LOGGER.isLoggable(Level.FINE))
					LOGGER.fine(new StringBuilder("Loaded bbox ").append(
							bbox.toString()).append(" while crop bbox ")
							.append(request.getCropBBox().toString())
							.toString());
				return theImage;				
			
			}
			else{
				// if we get here that means that we do not have anything to load
				// but still we are inside the definition area 
				//we don't have background values available
				return ConstantDescriptor.create(
								Float.valueOf(rasterBounds.width), 
								Float.valueOf(rasterBounds.height),
								new Byte[] { 0 },
								this.rasterManager.getHints());
			}

		} catch (IOException|TransformException e) {
			throw new DataSourceException("Unable to create this image", e);
		}
	}

	private RenderedImage processGranuleRaster(
		RenderedImage granule,
		final boolean doTransparentColor, final Color transparentColor) {

		//
		// INDEX COLOR MODEL EXPANSION
		//
		// Take into account the need for an expansions of the original color
		// model.
		//
		// If the original color model is an index color model an expansion
		// might be requested in case the different palettes are not all the
		// same. 
		//
		// There is a special case to take into account here. In case the input
		// images use an IndexColorModel it might happen that the transparent
		// color is present in some of them while it is not present in some
		// others. This case is the case where for sure a color expansion is
		// needed. However we have to take into account that during the masking
		// phase the images where the requested transparent color was present
		// will have 4 bands, the other 3. If we want the image to work we
		// have to add an extra band to the latter type of images for providing
		// alpha information to them.
		//
		//
		if (rasterManager.expandMe && granule.getColorModel() instanceof IndexColorModel) {
			granule = new ImageWorker(granule).forceComponentColorModel().getRenderedImage();
		}

		//
		// TRANSPARENT COLOR MANAGEMENT
		//
		if (doTransparentColor) {
			LOGGER.fine("Support for alpha on input");
			granule = ImageUtilities.maskColor(transparentColor, granule);
		}
		return granule;

	}

	private GridCoverage2D prepareCoverage(RenderedImage image) throws IOException {
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
			bands[i] = new GridSampleDimension(colorInterpretation.name());
		}

        return coverageFactory.create(rasterManager.getCoverageIdentifier(), image,new GeneralEnvelope(bbox), bands, null, null);
	}

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
	 * @param readParams
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
	private int setReadParams(final OverviewPolicy overviewPolicy,
			final ImageReadParam readParams, final RasterLayerRequest request)
			throws IOException, TransformException {

		// Default image index 0
		int imageChoice = 0;
		// default values for subsampling
		readParams.setSourceSubsampling(1, 1, 0, 0);

		//
		// Init overview policy
		//
		// //
		// when policy is explictly provided it overrides the policy provided
		// using hints.
		final OverviewPolicy policy;
		if (overviewPolicy == null)
			policy = rasterManager.overviewPolicy;
		else
			policy = overviewPolicy;

		// requested to ignore overviews
		if (policy.equals(OverviewPolicy.IGNORE))
			return imageChoice;

		// overviews and decimation
		imageChoice = rasterManager.overviewsController.pickOverviewLevel(overviewPolicy,request);

		// DECIMATION ON READING
		rasterManager.decimationController.performDecimation(imageChoice,readParams, request);
		return imageChoice;
	}

	private RenderedImage postProcessRaster(RenderedImage image) {
		// alpha on the final image
		if (transparentColor != null) {
			LOGGER.fine("Support for alpha on final image");
			final ImageWorker w = new ImageWorker(image);
			if (image.getSampleModel() instanceof MultiPixelPackedSampleModel)
				w.forceComponentColorModel();
			return w.makeColorTransparent(transparentColor).getRenderedImage();
	
		}
		return image;
	}
}
