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
package org.geotools.gce.imagemosaic;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.MultiPixelPackedSampleModel;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.media.jai.ImageLayout;
import javax.media.jai.JAI;
import javax.media.jai.ParameterBlockJAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.ROI;
import javax.media.jai.ROIShape;
import javax.media.jai.operator.ConstantDescriptor;
import javax.media.jai.operator.MosaicDescriptor;

import org.apache.commons.io.FilenameUtils;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.TypeMap;
import org.geotools.coverage.grid.GeneralGridEnvelope;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.grid.io.OverviewPolicy;
import org.geotools.data.DataSourceException;
import org.geotools.data.DataUtilities;
import org.geotools.factory.Hints;
import org.geotools.gce.imagemosaic.RasterManager.OverviewLevel;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.image.ImageWorker;
import org.geotools.referencing.CRS;
import org.geotools.referencing.operation.transform.AffineTransform2D;
import org.opengis.coverage.ColorInterpretation;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.geometry.BoundingBox;
import org.opengis.referencing.datum.PixelInCell;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.MathTransform2D;
import org.opengis.referencing.operation.TransformException;

import com.sun.media.jai.codecimpl.util.ImagingException;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.index.ItemVisitor;
/**
 * A RasterLayerResponse. An instance of this class is produced everytime a
 * requestCoverage is called to a reader.
 * 
 * @author Daniele Romagnoli, GeoSolutions
 * @author Stefan Alfons Krueger (alfonx), Wikisquare.de : Support for jar:file:foo.jar/bar.properties URLs
 */
@SuppressWarnings("deprecation")
class RasterLayerResponse{

	/**
	 * Simple plcaeholder class to store the result of 
	 * 
	 * @author Simone Giannecchini, S.A.S.
	 *
	 */
    static class GranuleLoadingResult {

        RenderedImage loadedImage;

        ROIShape inclusionArea;

        public ROIShape getFootprint() {
            return inclusionArea;
        }

        public RenderedImage getRaster() {
            return loadedImage;
        }

        GranuleLoadingResult(RenderedImage loadedImage, ROIShape inclusionArea) {
            this.loadedImage = loadedImage;
            this.inclusionArea = inclusionArea;
        }
    }
	
	
	/**
	 * 
	 * @author Simone Giannecchini, GeoSolutions SAS
	 *
	 */
	class GranuleLoader implements Callable<GranuleLoadingResult>{

		final ReferencedEnvelope cropBBox;
		
		final MathTransform2D mosaicWorldToGrid;
		
		final Granule granule;
		
		final ImageReadParam readParameters;
		
		final int imageIndex;

		final Dimension tilesDimension;
		
		GranuleLoader(
				final ImageReadParam readParameters, 
				final int imageIndex,
				final ReferencedEnvelope cropBBox, 
				final MathTransform2D mosaicWorldToGrid,
				final Granule granule,
				final Dimension tilesDimension) {
			this.readParameters = ImageMosaicUtils.cloneImageReadParam(readParameters);
			this.imageIndex = imageIndex;
			this.cropBBox = cropBBox;
			this.mosaicWorldToGrid = mosaicWorldToGrid;
			this.granule = granule;
			this.tilesDimension= tilesDimension!=null?(Dimension) tilesDimension.clone():null;
		}
		
		public BoundingBox getCropBBox() {
			return cropBBox;
		}

		public MathTransform2D getMosaicWorldToGrid() {
			return mosaicWorldToGrid;
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
		
		public GranuleLoadingResult call() throws Exception {
			return granule.loadRaster(readParameters, imageIndex, cropBBox, mosaicWorldToGrid, request,tilesDimension);
		}

	}
	
	class GranuleIndexVisitor implements ItemVisitor{

		final URL inputURL;
		/**
		 * Default {@link Constructor}
		 * @param inputURL 
		 */
		public GranuleIndexVisitor(URL inputURL) {
		    this.inputURL = inputURL;
		}

		private final List<Future<GranuleLoadingResult>> tasks= new ArrayList<Future<GranuleLoadingResult>>();
		private int   granulesNumber;
		private boolean doInputTransparency;
		private List<ROI> rois = new ArrayList<ROI>();
		private Color inputTransparentColor;
		private PlanarImage[] alphaChannels;

		public void visitItem(final Object item) {

			// Get location and envelope of the image to load.
			final SimpleFeature feature = (SimpleFeature) item;
			final String granuleLocation = (String) feature.getAttribute(rasterManager.getLocationAttribute());
			Geometry inclusionGeometry = null;
			
			final ReferencedEnvelope granuleBBox = ReferencedEnvelope.reference(feature.getBounds());
			

			// Load a granule from disk as requested.
			if (LOGGER.isLoggable(Level.FINE))
				LOGGER.fine("About to read image number " + granulesNumber);

			// If the granule is not there, dump a message and continue
			final URL rasterFile = rasterManager.getPathType().resolvePath(parentLocation, granuleLocation);
			if (rasterFile == null) {
				if (LOGGER.isLoggable(Level.INFO))
					LOGGER.info("File not found: "+granuleLocation);				
				return;
			}
			if (LOGGER.isLoggable(Level.FINE))
				LOGGER.fine("File found "+granuleLocation);
			
			// granule cache
			Granule granule=null;
			synchronized (rasterManager.granulesCache) {
				
				// Comment by Stefan Krueger
				// TODO Before the File.toURI().toString was jused as the cache key. For URL that potentially throws an URISystaxException and i used just toString()  
				
				// granule's information are cached
				if (rasterManager.granulesCache.containsKey(rasterFile.toString())) {
					granule=rasterManager.granulesCache.get(rasterFile.toString());
					inclusionGeometry = granule.inclusionGeometry;
				} else {
					// granule's information are NOT cached
					//get footprint first, can be null in case we don't have any geometry
					String featureID=feature.getID();
					featureID=FilenameUtils.getExtension(featureID);
					inclusionGeometry=rasterManager.getGranuleFootprint(featureID);
    				granule=new Granule(granuleBBox,rasterFile,rasterManager.parent.suggestedSPI, inclusionGeometry);
    				rasterManager.granulesCache.put(rasterFile.toString(),granule);
				}
			}
			
			//
			// load raster data
			//
			//create a granule loader
			final Geometry bb = JTS.toGeometry((BoundingBox)mosaicBBox);
			if (!footprintManagement || inclusionGeometry == null || footprintManagement && inclusionGeometry.intersects(bb)){
        			final GranuleLoader loader = new GranuleLoader(baseReadParameters, imageChoice, mosaicBBox, finalWorldToGridCorner, granule, request.getTileDimensions());
                            if (multithreadingAllowed && rasterManager.parent.multiThreadedLoader != null)
                                    tasks.add(rasterManager.parent.multiThreadedLoader.submit(loader));
                                else
                                    tasks.add(new FutureTask<GranuleLoadingResult>(loader));
                                
        			granulesNumber++;
        		}
			
			if(granulesNumber>request.getMaximumNumberOfGranules())
				throw new IllegalStateException("The maximum number of allowed granules ("+request.getMaximumNumberOfGranules()+")has been exceeded.");
		}
		



		public void produce(){
			
			// reusable parameters
			alphaChannels = new PlanarImage[granulesNumber];
			int granuleIndex=0;
			inputTransparentColor = request.getInputTransparentColor();
			doInputTransparency = inputTransparentColor != null&&!footprintManagement;
			// execute them all
			boolean firstGranule=true;
			int[] alphaIndex=null;
			
			for (Future<GranuleLoadingResult> future :tasks) {
				
				final RenderedImage loadedImage;
				final GranuleLoadingResult result;
				try {
					if(!multithreadingAllowed) {
						//run the loading in this thread
						final FutureTask<GranuleLoadingResult> task=(FutureTask<GranuleLoadingResult>) future;
						task.run();
					}
					result = future.get();
					if (result == null) {
                        if(LOGGER.isLoggable(Level.FINE))
                                LOGGER.log(Level.FINE,"Unable to load the raster for granule " +granuleIndex+ " with request "+request.toString());
                        continue;
                    }
					loadedImage = result.getRaster();
					if (loadedImage == null) {
						if(LOGGER.isLoggable(Level.FINE))
							LOGGER.log(Level.FINE,"Unable to load the raster for granule " +granuleIndex+ " with request "+request.toString());
						continue;
					}
					if (firstGranule) {
						//
						// We check here if the images have an alpha channel or some
						// other sort of transparency. In case we have transparency
						// I also save the index of the transparent channel.
						//
						// Specifically, I have to check if the loaded image have
						// transparency, because if we do a ROI and/or we have a
						// transparent color to set we have to remove it.
						//
						final ColorModel cm = loadedImage.getColorModel();
						alphaIn = cm.hasAlpha();
						if (alphaIn||doInputTransparency)
							alphaIndex = new int[] { cm.getNumComponents() - 1 };


						//
						// we set the input threshold accordingly to the input
						// image data type. I find the default value (which is 0) very bad
						// for data type other than byte and ushort. With float and double
						// it can cut off a large par of the dynamic.
						//
//						if(!Double.isNaN(request.getThreshold()))
//							pbjMosaic.setParameter("sourceThreshold", new double[][]{{request.getThreshold()}});
//						else
						pbjMosaic.setParameter("sourceThreshold",
							new double[][] { { ImageMosaicUtils.getThreshold(loadedImage.getSampleModel().getDataType()) } });
						
						
						firstGranule=false;
										
					}					
					
				} catch (InterruptedException e) {
					if(LOGGER.isLoggable(Level.SEVERE))
						LOGGER.log(Level.SEVERE,"Unable to load the raster for granule " +granuleIndex,e);
					continue;
				} catch (ExecutionException e) {
					if(LOGGER.isLoggable(Level.SEVERE))
						LOGGER.log(Level.SEVERE,"Unable to load the raster for granule " +granuleIndex,e);
					continue;
				} catch (ImagingException e) {
					if (LOGGER.isLoggable(Level.FINE))
						LOGGER.fine("Adding to mosaic image number " + granuleIndex+ " failed, original request was "+request);
					continue;
				} catch (javax.media.jai.util.ImagingException e) {
					if (LOGGER.isLoggable(Level.FINE))
						LOGGER.fine("Adding to mosaic image number " + granuleIndex+ " failed, original request was "+request);
					continue;
				}


				if (LOGGER.isLoggable(Level.FINE))
					LOGGER.fine("Adding to mosaic image number " + granuleIndex);
				
				//
				// add to the mosaic collection, with preprocessing
				//
				final RenderedImage raster = processGranuleRaster(
						loadedImage,
						granuleIndex, 
						alphaIndex,
						alphaIn, 
						alphaChannels, 
						doInputTransparency,
						inputTransparentColor);
				
				// we need to add its roi in order to avoid problems whith the mosaic overl
				ROI imageBounds = new ROIShape(PlanarImage.wrapRenderedImage(raster).getBounds());
				if (footprintManagement){
				    final ROIShape footprint = result.getFootprint();
				    if (footprint != null){
				        if (imageBounds.contains(footprint.getBounds())) {
				            imageBounds = footprint;
				        } else {
				            imageBounds = imageBounds.intersect(footprint);
				        }
				    }
				    
				    
				}
				rois.add(imageBounds);
				
				// add to mosaic
				pbjMosaic.addSource(raster);
			
				//increment index 
				granuleIndex++;
			}

			granulesNumber = granuleIndex;
			if (granulesNumber == 0) {
				if (LOGGER.isLoggable(Level.FINE)) {
					LOGGER.log(Level.FINE,"Unable to load any granule ");
				}
				return;
			}

			
			//
			// management of the alpha information which
			// can be the result of a masking operation upon the request for a
			// transparent color or the result of input images with internal
			// transparency.

			if ((alphaIn || doInputTransparency) && !footprintManagement) {
				// //
				//
				// In case the input images have transparency information
				// this
				// way we can handle it.
				//
				// //
				pbjMosaic.setParameter("sourceAlpha", alphaChannels);

			}

			pbjMosaic.setParameter("sourceROI", rois.toArray(new ROI[rois.size()]));			
			
		}
		
	}

	/** Logger. */
	private final static Logger LOGGER = org.geotools.util.logging.Logging.getLogger(RasterLayerResponse.class);
	
	/**
	 * The GridCoverage produced after a {@link #compute()} method call
	 */
	private GridCoverage2D gridCoverage;

	/** The {@link RasterLayerRequest} originating this response */
	private RasterLayerRequest request;

	/** The coverage factory producing a {@link GridCoverage} from an image */
	private GridCoverageFactory coverageFactory;

	/** The base envelope related to the input coverage */
	private GeneralEnvelope coverageEnvelope;

	private URL inputURL;

	private boolean frozen = false;

	private RasterManager rasterManager;

	private String parentLocation;

	private Color finalTransparentColor;

	private ParameterBlockJAI pbjMosaic;

	private ReferencedEnvelope mosaicBBox;

	private Rectangle rasterBounds;

	private MathTransform2D finalGridToWorldCorner;

	private MathTransform2D finalWorldToGridCorner;

	private int imageChoice=0;

	private ImageReadParam baseReadParameters= new ImageReadParam();

	private boolean multithreadingAllowed=false;
	
	private boolean footprintManagement = !ImageMosaicUtils.IGNORE_FOOTPRINT;
	
	private boolean alphaIn=false;

	private MathTransform baseGridToWorld;

	private double[] backgroundValues;

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
	public RasterLayerResponse(final RasterLayerRequest request,
			final RasterManager rasterManager) {
		this.request = request;
		inputURL = rasterManager.getInputURL();

		try {
			parentLocation = DataUtilities.getParentUrl(inputURL).toExternalForm();
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException("Unable to determine the parent location of "+inputURL, e);
		}
		coverageEnvelope = rasterManager.getCoverageEnvelope();
		this.coverageFactory = rasterManager.getCoverageFactory();
		this.rasterManager = rasterManager;
		baseGridToWorld=rasterManager.getRaster2Model();
		finalTransparentColor=request.getOutputTransparentColor();
		// are we doing multithreading?
		multithreadingAllowed= request.isMultithreadingAllowed();
		footprintManagement = request.isFootprintManagement();
		backgroundValues = request.getBackgroundValues();

	}

	/**
	 * Compute the coverage request and produce a grid coverage which will be
	 * returned by {@link #createResponse()}. The produced grid coverage may be
	 * {@code null} in case of empty request.
	 * 
	 * @return the {@link GridCoverage} produced as computation of this response
	 *         using the {@link #compute()} method.
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
	private  void processRequest() throws IOException {

		if (request.isEmpty())
		{
			if(LOGGER.isLoggable(Level.FINE))
				LOGGER.log(Level.FINE,"Request is empty: "+request.toString());
			this.gridCoverage=null;
			return;
		}

		if (frozen)
			return;
		
		// assemble granules
		final RenderedImage mosaic = prepareResponse();
		

		//postproc
		RenderedImage finalRaster = postProcessRaster(mosaic);
		
		//create the coverage
		gridCoverage=prepareCoverage(finalRaster);
		
		//freeze
		frozen = true;
		
	}

	private RenderedImage postProcessRaster(RenderedImage mosaic) {
		// alpha on the final mosaic
		if (finalTransparentColor != null) {
			if (LOGGER.isLoggable(Level.FINE))
				LOGGER.fine("Support for alpha on final mosaic");
			return ImageMosaicUtils.makeColorTransparent(finalTransparentColor,mosaic);

		}
		return mosaic;
	}

	/**
	 * This method loads the granules which overlap the requested
	 * {@link GeneralEnvelope} using the provided values for alpha and input
	 * ROI.
	 * @return
	 * @throws DataSourceException
	 */
	private RenderedImage prepareResponse() throws DataSourceException {

		try {
			
			//
			// prepare the params for executing a mosaic operation.
			//
			pbjMosaic = new ParameterBlockJAI("Mosaic");
			pbjMosaic.setParameter("backgroundValues", backgroundValues);
			// It might important to set the mosaic type to blend otherwise
			// sometimes strange results jump in.
			if (request.isBlend()) 
				pbjMosaic.setParameter("mosaicType",MosaicDescriptor.MOSAIC_TYPE_BLEND);
			else
				pbjMosaic.setParameter("mosaicType",MosaicDescriptor.MOSAIC_TYPE_OVERLAY);

			// select the relevant overview, notice that at this time we have
			// relaxed a bit the requirement to have the same exact resolution
			// for all the overviews, but still we do not allow for reading the
			// various grid to world transform directly from the input files,
			// therefore we are assuming that each granule has a scale and
			// translate only grid to world that can be deduced from its base
			// level dimension and envelope. The grid to world transforms for
			// the other levels can be computed accordingly knowning the scale
			// factors.
			if (request.getRequestedBBox() != null&& request.getRequestedRasterArea() != null)
				imageChoice = setReadParams(request.getOverviewPolicy(), baseReadParameters, request);
			else
				imageChoice = 0;
			assert imageChoice>=0;
			if (LOGGER.isLoggable(Level.FINE))
				LOGGER.fine(new StringBuffer("Loading level ").append(
						imageChoice).append(" with subsampling factors ")
						.append(baseReadParameters.getSourceXSubsampling()).append(" ")
						.append(baseReadParameters.getSourceYSubsampling()).toString());			
			
			
			// ok we got something to return, let's load records from the index
			final BoundingBox cropBBOX = request.getCropBBox();
			if (cropBBOX != null)
				mosaicBBox = ReferencedEnvelope.reference(cropBBOX);
			else
				mosaicBBox = new ReferencedEnvelope(coverageEnvelope);
						
			//compute final world to grid
			// base grid to world for the center of pixels
			final AffineTransform g2w = new AffineTransform((AffineTransform) baseGridToWorld);
			// move it to the corner
			g2w.concatenate(ImageMosaicUtils.CENTER_TO_CORNER);
			
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
			finalGridToWorldCorner=new AffineTransform2D(g2w);
			finalWorldToGridCorner = finalGridToWorldCorner.inverse();// compute raster bounds
			rasterBounds=new GeneralGridEnvelope(CRS.transform(finalWorldToGridCorner, mosaicBBox),PixelInCell.CELL_CORNER,false).toRectangle();
			if (rasterBounds.width == 0)
			    rasterBounds.width++;
			if (rasterBounds.height == 0)
			    rasterBounds.height++;
			
			
			// create the index visitor and visit the feature
			final GranuleIndexVisitor visitor = new GranuleIndexVisitor(inputURL);
			rasterManager.getFeaturesFromIndex(mosaicBBox, visitor);
			visitor.produce();
			
			//
			// Did we actually load anything?? Notice that it might happen that
			// either we have holes inside the definition area for the mosaic
			// or we had some problem with missing tiles, therefore it might
			// happen that for some bboxes we don't have anything to load.
			//
			if (visitor.granulesNumber>=1) {

				//
				// Create the mosaic image by doing a crop if necessary and also
				// managing the transparent color if applicable. Be aware that
				// management of the transparent color involves removing
				// transparency information from the input images.
				// 
				if (LOGGER.isLoggable(Level.FINE))
					LOGGER.fine(new StringBuilder("Loaded bbox ").append(
							mosaicBBox.toString()).append(" while crop bbox ")
							.append(request.getCropBBox().toString())
							.toString());
				
				
				return buildMosaic();				
			
			} else {
			    
				// if we get here that means that we do not have anything to load
				// but still we are inside the definition area for the mosaic,
				// therefore we create a fake coverage using the background values,
				// if provided (defaulting to 0), as well as the compute raster
				// bounds, envelope and grid to world.
				Number[] values = ImageMosaicUtils.getBackgroundValues(rasterManager.defaultSM, backgroundValues);
			        
				return ConstantDescriptor.create(
									Float.valueOf(rasterBounds.width), 
									Float.valueOf(rasterBounds.height),
									values, 
									rasterManager.defaultImageLayout!=null?new RenderingHints(JAI.KEY_IMAGE_LAYOUT,rasterManager.defaultImageLayout):null);
			}

		} catch (IOException e) {
			throw new DataSourceException("Unable to create this mosaic", e);
		} catch (TransformException e) {
			throw new DataSourceException("Unable to create this mosaic", e);
		} 
	}

	private RenderedImage processGranuleRaster(
			RenderedImage granule, 
			final int granuleIndex, 
			final int[] alphaIndex,
			final boolean alphaIn,
			final PlanarImage[] alphaChannels,
			final boolean doTransparentColor, final Color transparentColor) {

		//
		// INDEX COLOR MODEL EXPANSION
		//
		// Take into account the need for an expansions of the original color
		// model.
		//
		// If the original color model is an index color model an expansion
		// might be requested in case the different palettes are not all the
		// same. In this case the mosaic operator from JAI would provide wrong
		// results since it would take the first palette and use that one for
		// all the other images.
		//
		// There is a special case to take into account here. In case the input
		// images use an IndexColorModel it might happen that the transparent
		// color is present in some of them while it is not present in some
		// others. This case is the case where for sure a color expansion is
		// needed. However we have to take into account that during the masking
		// phase the images where the requested transparent color was present
		// will have 4 bands, the other 3. If we want the mosaic to work we
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
			if (LOGGER.isLoggable(Level.FINE))
				LOGGER.fine("Support for alpha on input image number "+ granuleIndex);
			granule = ImageMosaicUtils.makeColorTransparent(transparentColor, granule);
			alphaIndex[0]= granule.getColorModel().getNumComponents() - 1 ;

		}
		//
		// ROI
		//
		if ((alphaIn || doTransparentColor) && !footprintManagement) {
			ImageWorker w = new ImageWorker(granule);
			if (granule.getSampleModel() instanceof MultiPixelPackedSampleModel)
				w.forceComponentColorModel();
			//
			// ALPHA in INPUT
			//
			// I have to select the alpha band and provide it to the final
			// mosaic operator. I have to force going to ComponentColorModel in
			// case the image is indexed.
			//
			if (granule.getColorModel() instanceof IndexColorModel) {
				alphaChannels[granuleIndex] = w.forceComponentColorModel().retainLastBand().getPlanarImage();
			} else
				alphaChannels[granuleIndex] = w.retainBands(alphaIndex).getPlanarImage();

		}

		return granule;

	}

	/**
	 * Once we reach this method it means that we have loaded all the images
	 * which were intersecting the requested envelope. Next step is to create
	 * the final mosaic image and cropping it to the exact requested envelope.
	 * 
	 * @return A {@link RenderedImage}}.
	 */
	private RenderedImage buildMosaic() throws IOException  {

		final ImageLayout layout = new ImageLayout(
				rasterBounds.x,
				rasterBounds.y,
				rasterBounds.width,
				rasterBounds.height);
		//tiling
		final Dimension tileDimensions=request.getTileDimensions();
		if(tileDimensions!=null)
			layout.setTileHeight(tileDimensions.width).setTileWidth(tileDimensions.height);
		final RenderingHints hints = new RenderingHints(JAI.KEY_IMAGE_LAYOUT,layout);
		RenderedImage mosaic = JAI.create("Mosaic", pbjMosaic, hints);

		if (LOGGER.isLoggable(Level.FINE))
			LOGGER.fine(new StringBuffer("Mosaic created ").toString());


		// create the coverage
		return mosaic;

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
		        bands[i] = new GridSampleDimension(colorInterpretation.name()).geophysics(true);
		}

        return coverageFactory.create(rasterManager.getCoverageIdentifier(), image,new GeneralEnvelope(mosaicBBox), bands, null, null);		

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
		imageChoice = rasterManager.overviewsController.pickOverviewLevel(overviewPolicy, request);

		// DECIMATION ON READING
		rasterManager.decimationController.performDecimation(imageChoice, readParams, request);
		return imageChoice;
	}


}
