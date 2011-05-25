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

import jaitools.imageutils.ROIGeometry;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.MultiPixelPackedSampleModel;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageReadParam;
import javax.measure.unit.Unit;
import javax.media.jai.BorderExtender;
import javax.media.jai.Histogram;
import javax.media.jai.ImageLayout;
import javax.media.jai.Interpolation;
import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.ROI;
import javax.media.jai.ROIShape;
import javax.media.jai.RenderedOp;
import javax.media.jai.TileCache;
import javax.media.jai.TileScheduler;
import javax.media.jai.operator.AffineDescriptor;
import javax.media.jai.operator.ConstantDescriptor;
import javax.media.jai.operator.CropDescriptor;
import javax.media.jai.operator.MosaicDescriptor;

import org.apache.commons.io.FilenameUtils;
import org.geotools.coverage.Category;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.TypeMap;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.data.DataSourceException;
import org.geotools.data.DataUtilities;
import org.geotools.data.Query;
import org.geotools.factory.Hints;
import org.geotools.feature.visitor.MaxVisitor;
import org.geotools.filter.IllegalFilterException;
import org.geotools.gce.imagemosaic.OverviewsController.OverviewLevel;
import org.geotools.gce.imagemosaic.catalog.GranuleCatalogVisitor;
import org.geotools.gce.imagemosaic.processing.ArtifactsFilterDescriptor;
import org.geotools.geometry.Envelope2D;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.image.ImageWorker;
import org.geotools.referencing.CRS;
import org.geotools.referencing.operation.matrix.XAffineTransform;
import org.geotools.referencing.operation.transform.AffineTransform2D;
import org.geotools.resources.coverage.CoverageUtilities;
import org.geotools.resources.coverage.FeatureUtilities;
import org.geotools.resources.geometry.XRectangle2D;
import org.geotools.resources.i18n.Vocabulary;
import org.geotools.resources.i18n.VocabularyKeys;
import org.geotools.resources.image.ImageUtilities;
import org.geotools.util.DateRange;
import org.geotools.util.NumberRange;
import org.geotools.util.SimpleInternationalString;
import org.opengis.coverage.ColorInterpretation;
import org.opengis.coverage.SampleDimension;
import org.opengis.coverage.SampleDimensionType;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.expression.Expression;
import org.opengis.geometry.BoundingBox;
import org.opengis.referencing.datum.PixelInCell;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.MathTransform1D;
import org.opengis.referencing.operation.MathTransform2D;
import org.opengis.referencing.operation.TransformException;
import org.opengis.util.InternationalString;

import com.sun.media.jai.codecimpl.util.ImagingException;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
/**
 * A RasterLayerResponse. An instance of this class is produced everytime a
 * requestCoverage is called to a reader.
 * 
 * @author Simone Giannecchini, GeoSolutions
 * @author Daniele Romagnoli, GeoSolutions
 * @author Stefan Alfons Krueger (alfonx), Wikisquare.de : Support for jar:file:foo.jar/bar.properties URLs
 */
class RasterLayerResponse{
    
    /**
     * Simple placeholder class to store the result of a Granule Loading
     * which comprises of a raster as well as a {@link ROIShape} for its footprint.
     * 
     * @author Daniele Romagnoli, GeoSolutions S.A.S.
     * 
     */
    static class GranuleLoadingResult {

        RenderedImage loadedImage;

        ROI footprint;
        
        URL granuleUrl;
        
        boolean doFiltering;

        public ROI getFootprint() {
            return footprint;
        }

        public RenderedImage getRaster() {
            return loadedImage;
        }

        public URL getGranuleUrl() {
            return granuleUrl;
        }
        public boolean isDoFiltering() {
            return doFiltering;
        }
        GranuleLoadingResult(RenderedImage loadedImage, ROI footprint) {
            this(loadedImage, footprint, null);
        }

        GranuleLoadingResult(RenderedImage loadedImage, ROI footprint, URL granuleUrl) {
            this(loadedImage, footprint, granuleUrl, false);
        }

        GranuleLoadingResult(RenderedImage loadedImage, ROI footprint, URL granuleUrl, final boolean doFiltering) {
            this.loadedImage = loadedImage;
            this.footprint = footprint;
            this.granuleUrl = granuleUrl;
            this.doFiltering = doFiltering;
        }
    }
    
    private static final class SimplifiedGridSampleDimension extends GridSampleDimension implements SampleDimension{

		/**
		 * 
		 */
		private static final long serialVersionUID = 2227219522016820587L;


		private double nodata;
		private double minimum;
		private double maximum;
		private double scale;
		private double offset;
		private Unit<?> unit;
		private SampleDimensionType type;
		private ColorInterpretation color;
		private Category bkg;

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
			super(description,!Double.isNaN(nodata)?
					new Category[]{new Category(Vocabulary
		                    .formatInternational(VocabularyKeys.NODATA), new Color[]{new Color(0, 0, 0, 0)} , NumberRange
		                    .create(nodata, nodata), NumberRange
		                    .create(nodata, nodata))}:null,unit);
			this.nodata=nodata;
			this.minimum=minimum;
			this.maximum=maximum;
			this.scale=scale;
			this.offset=offset;
			this.unit=unit;
			this.type=type;
			this.color=color;
			this.bkg=new Category("Background", Utils.TRANSPARENT, 0);
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
			return new double[]{nodata};
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
		public MathTransform1D getSampleToGeophysics() {
			return super.getSampleToGeophysics();
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
		public Category getBackground() {
			return bkg;
		}

		@Override
		public InternationalString[] getCategoryNames()
				throws IllegalStateException {
			return new InternationalString[]{SimpleInternationalString.wrap("Background")};
		}
	}
	
	/**
	 * My specific {@link MaxVisitor} that keeps track of the feature used for the maximum.
	 * @author Simone Giannecchini, GeoSolutions SAS
	 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/plugin/imagemosaic/src/main/java/org/geotools/gce/imagemosaic/RasterLayerResponse.java $
	 */
	public static class MaxVisitor2 extends MaxVisitor{

		@SuppressWarnings("unchecked")
		private Comparable oldValue;
		private int oldNanCount;
		private int oldNullCount;
		
		private Feature targetFeature=null;
		
		public MaxVisitor2(Expression expr) throws IllegalFilterException {
			super(expr);
		}

		public MaxVisitor2(int attributeTypeIndex, SimpleFeatureType type)
				throws IllegalFilterException {
			super(attributeTypeIndex, type);
		}

		public Feature getTargetFeature() {
			return targetFeature;
		}

		public MaxVisitor2(String attrName, SimpleFeatureType type)
				throws IllegalFilterException {
			super(attrName, type);
		}

		public MaxVisitor2(String attributeTypeName) {
			super(attributeTypeName);
		}

		@Override
		public void reset() {
			super.reset();
			this.oldValue=null;
			this.targetFeature=null;
		}

		@Override
		public void setValue(Object result) {
			super.setValue(result);
			this.oldValue=null;
			this.targetFeature=null;
		}

		@SuppressWarnings("unchecked")
		@Override
		public void visit(Feature feature) {
			super.visit(feature);
			// if we got a NAN let's leave
			final int nanCount=getNaNCount();
			if(oldNanCount!=nanCount)
			{
				oldNanCount=nanCount;
				return;
			}
			
			// if we got a null let's leave			
			final int nullCount=getNullCount();
			if(oldNullCount!=nullCount)
			{
				oldNullCount=nullCount;
				return;
			}
			
			// check if we got a real value
			final Comparable max=getMax();
			if ( oldValue==null||max.compareTo(oldValue) != 0) {
	        	targetFeature=feature;
	        	oldValue=max;
	        }			
		}

		
	}

	/**
	 * This class is responsible for putting together the granules for the final mosaic.
	 * 
	 * @author Simone Giannecchini, GeoSolutions SAS
	 *
	 */
	class MosaicBuilder implements GranuleCatalogVisitor{

		/**
		 * Default {@link Constructor}
		 */
		public MosaicBuilder() {
		}
		

		private final List<Future<GranuleLoadingResult>> tasks= new ArrayList<Future<GranuleLoadingResult>>();
		private int   granulesNumber;
		private List<ROI> rois = new ArrayList<ROI>();
		private Color inputTransparentColor;
		private PlanarImage[] alphaChannels;
		private RasterLayerRequest request;
        
                private ROI[] sourceRoi;
        
                private double[][] sourceThreshold;
        
                private boolean doInputTransparency;
		
		private List<RenderedImage> sources = new ArrayList<RenderedImage>();

		public RenderedImage[] getSourcesAsArray() {
		    RenderedImage []imageSources = new RenderedImage[sources.size()];
    	            sources.toArray(imageSources);
    	            return imageSources;
                }


		public void visit(GranuleDescriptor granuleDescriptor, Object o) {
			
			//
			// load raster data
			//
			//create a granuleDescriptor loader
            final Geometry bb = JTS.toGeometry((BoundingBox)mosaicBBox);
            final Geometry inclusionGeometry = granuleDescriptor.inclusionGeometry;
            if (!footprintManagement || inclusionGeometry == null || footprintManagement && inclusionGeometry.intersects(bb)){
                final GranuleLoader loader = new GranuleLoader(baseReadParameters, imageChoice, mosaicBBox, finalWorldToGridCorner, granuleDescriptor, request, hints);
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
				boolean doFiltering;
				try {
					if(!multithreadingAllowed || rasterManager.parent.multiThreadedLoader == null)
					{
						//run the loading in this thread
					    final FutureTask<GranuleLoadingResult> task=(FutureTask<GranuleLoadingResult>) future;
                                            task.run();
					}
					result = future.get();
                                        if (result == null) {
                                            if (LOGGER.isLoggable(Level.FINE))
                                                LOGGER.log(Level.FINE, "Unable to load the raster for granule " 
                                                        + granuleIndex + " with request " + request.toString());
                                            continue;
                                        }
					loadedImage = result.getRaster();
					doFiltering = result.isDoFiltering();
					if(loadedImage==null)
					{
						if(LOGGER.isLoggable(Level.FINE))
							LOGGER.log(Level.FINE,"Unable to load the raster for granuleDescriptor " +granuleIndex+ " with request "+request.toString());
						continue;
					}
					if(firstGranule){
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
						sourceThreshold = new double[][] { { CoverageUtilities.getMosaicThreshold(loadedImage.getSampleModel().getDataType()) } };
						
						
						firstGranule=false;
										
					}					
					
				} catch (InterruptedException e) {
					if(LOGGER.isLoggable(Level.SEVERE))
						LOGGER.log(Level.SEVERE,"Unable to load the raster for granuleDescriptor " +granuleIndex,e);
					continue;
				} catch (ExecutionException e) {
					if(LOGGER.isLoggable(Level.SEVERE))
						LOGGER.log(Level.SEVERE,"Unable to load the raster for granuleDescriptor " +granuleIndex,e);
					continue;
				}

				catch (ImagingException e) {
					if (LOGGER.isLoggable(Level.FINE))
						LOGGER.fine("Adding to mosaic image number " + granuleIndex+ " failed, original request was "+request);
					continue;
				}
				catch (javax.media.jai.util.ImagingException e) {
					if (LOGGER.isLoggable(Level.FINE))
						LOGGER.fine("Adding to mosaic image number " + granuleIndex+ " failed, original request was "+request);
					continue;
				}


				if (LOGGER.isLoggable(Level.FINER)) {
					LOGGER.finer("Adding to mosaic image number " + granuleIndex);
				}
				
				//
				// add to the mosaic collection, with preprocessing
				//
				RenderedImage raster = processGranuleRaster(
						loadedImage,
						granuleIndex, 
						alphaIndex,
						alphaIn, 
						alphaChannels, 
						doInputTransparency,
						inputTransparentColor);
				
				// we need to add its roi in order to avoid problems with the mosaic overlapping
				Rectangle bounds = PlanarImage.wrapRenderedImage(raster).getBounds();
				Geometry mask = JTS.toGeometry(new Envelope(bounds.getMinX(), bounds.getMaxX(), bounds.getMinY(), bounds.getMaxY()));
				ROI imageBounds = new ROIGeometry(mask);
                                if (footprintManagement){
                                    final ROI footprint = result.getFootprint();
                                    if (footprint != null) {
                                        if (imageBounds.contains(footprint.getBounds2D().getBounds())) {
                                            imageBounds = footprint;
                                        } else {
                                            imageBounds = imageBounds.intersect(footprint);
                                        }
                                    }
                                    
                                    //Artifacts filtering processing
                                if (defaultArtifactsFilterThreshold != Integer.MIN_VALUE && doFiltering){
                                    int artifactThreshold = defaultArtifactsFilterThreshold; 
                                    if (artifactsFilterPTileThreshold != -1){
                                        final URL url = result.getGranuleUrl();
                                        
                                        //Looking for a histogram for that granule in order to 
                                        //setup dynamic threshold 
                                        if (url != null){
                                            final File inputFile = DataUtilities.urlToFile(url);
                                            final String inputFileName = inputFile.getPath();
                                            final String path = FilenameUtils.getFullPath(inputFileName);
                                            final String baseName = FilenameUtils.getBaseName(inputFileName);
                                            final String histogramPath = path + baseName + "." + "histogram";
                                            final Histogram histogram = Utils.getHistogram(histogramPath);
                                            if (histogram != null) {
                                                final double[]p = histogram.getPTileThreshold(artifactsFilterPTileThreshold);
                                                artifactThreshold = (int)p[0];
                                            }
                                        }
                                    }
                                    if (LOGGER.isLoggable(Level.FINE)){
                                        LOGGER.log(Level.FINE, "Filtering granules artifacts");
                                    }
                                    raster = ArtifactsFilterDescriptor.create(raster, imageBounds, new double[]{0}, artifactThreshold, 3, hints);
                                    }
                                }
                                rois.add(imageBounds);

				// add to mosaic
                                sources.add(raster);
                                
			
				//increment index 
				granuleIndex++;
			}

			granulesNumber=granuleIndex;
			if(granulesNumber==0)
			{
				if(LOGGER.isLoggable(Level.FINE))
					LOGGER.log(Level.FINE,"Unable to load any granuleDescriptor ");
				return;
			}
			
	                sourceRoi = rois.toArray(new ROI[rois.size()]);
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

	private boolean frozen = false;

	private RasterManager rasterManager;

	private Color finalTransparentColor;

	private ReferencedEnvelope mosaicBBox;

	private Rectangle rasterBounds;

	private MathTransform2D finalGridToWorldCorner;

	private MathTransform2D finalWorldToGridCorner;

	private int imageChoice=0;

	private ImageReadParam baseReadParameters= new ImageReadParam();

	private boolean multithreadingAllowed=false;
	
	private boolean footprintManagement = !Utils.IGNORE_FOOTPRINT;
	
	private int defaultArtifactsFilterThreshold = Integer.MIN_VALUE;
	
	private double artifactsFilterPTileThreshold = ImageMosaicFormat.DEFAULT_ARTIFACTS_FILTER_PTILE_THRESHOLD;
	
	private boolean setRoiProperty;
	
	private boolean alphaIn=false;
	
	private boolean oversampledRequest = false;

	private MathTransform baseGridToWorld;
	
	private Interpolation interpolation;
	
	private boolean needsReprojection;

	private double[] backgroundValues;
	
	private Hints hints;
	
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
		coverageEnvelope = rasterManager.spatialDomainManager.coverageEnvelope;
		this.coverageFactory = rasterManager.getCoverageFactory();
		this.rasterManager = rasterManager;
		this.hints = rasterManager.getHints();
		baseGridToWorld=rasterManager.spatialDomainManager.coverageGridToWorld2D;
		finalTransparentColor=request.getOutputTransparentColor();
		// are we doing multithreading?
		multithreadingAllowed= request.isMultithreadingAllowed();
		footprintManagement = request.isFootprintManagement();
		setRoiProperty = request.isSetRoiProperty();
		backgroundValues = request.getBackgroundValues();
		interpolation = request.getInterpolation();
		needsReprojection = request.isNeedsReprojection();
		defaultArtifactsFilterThreshold = request.getDefaultArtifactsFilterThreshold();
		artifactsFilterPTileThreshold = request.getArtifactsFilterPTileThreshold();
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
		gridCoverage = prepareCoverage(finalRaster);
		
		//freeze
		frozen = true;
		
	}

	private RenderedImage postProcessRaster(RenderedImage mosaic) {
		// alpha on the final mosaic
		if (finalTransparentColor != null) {
			if (LOGGER.isLoggable(Level.FINE))
				LOGGER.fine("Support for alpha on final mosaic");
			return ImageUtilities.maskColor(finalTransparentColor,mosaic);

		}
		if (!needsReprojection){
		    try {
		        
		        // creating source grid to world corrected to the pixel corner
                        final AffineTransform sourceGridToWorld = new AffineTransform((AffineTransform) finalGridToWorldCorner);
		        
		        // target world to grid at the corner
                        final AffineTransform targetGridToWorld = new AffineTransform(request.getRequestedGridToWorld());
                        targetGridToWorld.concatenate(CoverageUtilities.CENTER_TO_CORNER);
                        
                        // target world to grid at the corner
                        final AffineTransform targetWorldToGrid=targetGridToWorld.createInverse();
                        // final complete transformation
                        targetWorldToGrid.concatenate(sourceGridToWorld);
                        
                        //update final grid to world
                        finalGridToWorldCorner=new AffineTransform2D(targetGridToWorld);
                        //
                        // Check and see if the affine transform is doing a copy.
                        // If so call the copy operation.
                        //
                        // we are in raster space here, so 1E-3 is safe
                        if(XAffineTransform.isIdentity(targetWorldToGrid, Utils.AFFINE_IDENTITY_EPS))
                            return mosaic;
		        
		        // create final image
		        // TODO this one could be optimized further depending on how the affine is created
		        //
                        // In case we are asked to use certain tile dimensions we tile
                        // also at this stage in case the read type is Direct since
                        // buffered images comes up untiled and this can affect the
                        // performances of the subsequent affine operation.
                        //
		        final Hints localHints= new Hints(hints);
                        if (hints != null && !hints.containsKey(JAI.KEY_BORDER_EXTENDER)) {
                            final Object extender = hints.get(JAI.KEY_BORDER_EXTENDER);
                            if (!(extender != null && extender instanceof BorderExtender)) {
                                localHints.add(ImageUtilities.EXTEND_BORDER_BY_COPYING);
                            }
                        }
	        
                        mosaic = AffineDescriptor.create(mosaic, targetWorldToGrid , interpolation, backgroundValues, localHints);
                    } catch (NoninvertibleTransformException e) {
                        if (LOGGER.isLoggable(Level.SEVERE)){
                            LOGGER.log(Level.SEVERE, "Unable to create the requested mosaic ", e );
                        }
                    }
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
			if (request.getRequestedBBox() != null && request.getRequestedRasterArea() != null && !request.isHeterogeneousGranules())
				imageChoice = ReadParamsController.setReadParams(
				        request.getRequestedResolution(),
				        request.getOverviewPolicy(),
				        request.getDecimationPolicy(),
				        baseReadParameters,
				        request.rasterManager,
				        request.rasterManager.overviewsController); // use general overviews controller
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
			final AffineTransform g2w;
			final OverviewLevel baseLevel = rasterManager.overviewsController.resolutionsLevels
					.get(0);
			final OverviewLevel selectedLevel = rasterManager.overviewsController.resolutionsLevels.get(imageChoice);
			final double resX = baseLevel.resolutionX;
			final double resY = baseLevel.resolutionY;
			final double[] requestRes = request.getRequestedResolution();

                        g2w = new AffineTransform((AffineTransform) baseGridToWorld);
                        g2w.concatenate(CoverageUtilities.CENTER_TO_CORNER);
                        
			if ((requestRes[0] < resX || requestRes[1] < resY) ) {
			    // Using the best available resolution
			    oversampledRequest = true;
			} else {
				
			    // SG going back to working on a per level basis to do the composition
			    // g2w = new AffineTransform(request.getRequestedGridToWorld());
			    g2w.concatenate(AffineTransform.getScaleInstance(selectedLevel.scaleFactor,selectedLevel.scaleFactor));
			    g2w.concatenate(AffineTransform.getScaleInstance(baseReadParameters.getSourceXSubsampling(), baseReadParameters.getSourceYSubsampling()));
			}

			// move it to the corner
			finalGridToWorldCorner = new AffineTransform2D(g2w);
			finalWorldToGridCorner = finalGridToWorldCorner.inverse();// compute raster bounds
			final GeneralEnvelope tempRasterBounds = CRS.transform(finalWorldToGridCorner, mosaicBBox);
			rasterBounds=tempRasterBounds.toRectangle2D().getBounds();
			
			
//			 SG using the above may lead to problems since the reason is that  may be a little (1 px) bigger
//			 than what we need. The code below is a bit better since it uses a proper logic (see GridEnvelope
//			 Javadoc)
//			rasterBounds = new GridEnvelope2D(new Envelope2D(tempRasterBounds), PixelInCell.CELL_CORNER);
			if (rasterBounds.width == 0)
			    rasterBounds.width++;
			if (rasterBounds.height == 0)
			    rasterBounds.height++;
			if(oversampledRequest)
			    rasterBounds.grow(2, 2);
			
                        // make sure we do not go beyond the raster dimensions for this layer
                        final GeneralEnvelope levelRasterArea_ = CRS.transform(finalWorldToGridCorner, rasterManager.spatialDomainManager.coverageBBox);
                        final GridEnvelope2D levelRasterArea = new GridEnvelope2D(new Envelope2D(levelRasterArea_), PixelInCell.CELL_CORNER);
                        XRectangle2D.intersect(levelRasterArea, rasterBounds, rasterBounds);
			
			// create the index visitor and visit the feature
			final MosaicBuilder visitor = new MosaicBuilder();
			visitor.request = request;
			final List times = request.getRequestedTimes();
			final List elevations=request.getElevation();
			final Filter filter = request.getFilter();
			final boolean hasTime=(times!=null&&times.size()>0);
			final boolean hasElevation=(elevations!=null && elevations.size()>0);
			final boolean hasFilter = filter != null;

			final SimpleFeatureType type = rasterManager.granuleCatalog.getType();
			Query query = null;
			if (type != null){
			    query= new Query(rasterManager.granuleCatalog.getType().getTypeName());
			    final Filter bbox=FeatureUtilities.DEFAULT_FILTER_FACTORY.bbox(FeatureUtilities.DEFAULT_FILTER_FACTORY.property(rasterManager.granuleCatalog.getType().getGeometryDescriptor().getName()),mosaicBBox);
			    query.setFilter( bbox);
			}

                        if(hasTime||hasElevation||hasFilter )
                        {
                                //handle elevation indexing first since we then combine this with the max in case we are asking for current in time
                                if (hasElevation){
                                        
                                        final List<Filter> elevationF=new ArrayList<Filter>();
                                        for( Object elevation: elevations){
                                            if(elevation==null){
                                                if(LOGGER.isLoggable(Level.INFO))
                                                    LOGGER.info("Ignoring null elevation for the elevation filter");
                                                continue;
                                            }
                                            if(elevation instanceof Number){
                                                elevationF.add( FeatureUtilities.DEFAULT_FILTER_FACTORY.equal(
                                                    FeatureUtilities.DEFAULT_FILTER_FACTORY.property(rasterManager.elevationAttribute), 
                                                    FeatureUtilities.DEFAULT_FILTER_FACTORY.literal(elevation),
                                                    true)); 
                                            } else {
                                                // convert to range and create a correct range filter
                                                @SuppressWarnings("rawtypes")
                                                final NumberRange range= (NumberRange)elevation;
                                                elevationF.add( 
                                                        FeatureUtilities.DEFAULT_FILTER_FACTORY.and(
                                                                FeatureUtilities.DEFAULT_FILTER_FACTORY.lessOrEqual(
                                                                        FeatureUtilities.DEFAULT_FILTER_FACTORY.property(rasterManager.elevationAttribute), 
                                                                        FeatureUtilities.DEFAULT_FILTER_FACTORY.literal(range.getMaximum())),
                                                                FeatureUtilities.DEFAULT_FILTER_FACTORY.greaterOrEqual(
                                                                        FeatureUtilities.DEFAULT_FILTER_FACTORY.property(rasterManager.elevationAttribute), 
                                                                        FeatureUtilities.DEFAULT_FILTER_FACTORY.literal(range.getMinimum()))
                                                        )); 
                                            }
                                            
                                        }
                                        final int elevationSize=elevationF.size();
                                        if(elevationSize>1)//should not happen
                                            query.setFilter(
                                                    FeatureUtilities.DEFAULT_FILTER_FACTORY.and(query.getFilter(),
                                                            FeatureUtilities.DEFAULT_FILTER_FACTORY.or(elevationF))
                                                            );  
                                        else
                                            if(elevationSize==1)
                                                query.setFilter(FeatureUtilities.DEFAULT_FILTER_FACTORY.and(query.getFilter(), elevationF.get(0)));        
                                }

                                //handle generic filter since we then combine this with the max in case we are asking for current in time
                                if (hasFilter){
                                        query.setFilter(FeatureUtilities.DEFAULT_FILTER_FACTORY.and(query.getFilter(), filter));        
                                }
                                
                                // fuse time query with the bbox query
                                if(hasTime){
                                        final List<Filter> timeFilter=new ArrayList<Filter>();
                                        for( Object datetime: times){
                                            if(datetime==null){
                                                if(LOGGER.isLoggable(Level.INFO))
                                                    LOGGER.info("Ignoring null date for the time filter");
                                                continue;
                                            }
                                            if(datetime instanceof Date){
                                                timeFilter.add(
                                                        FeatureUtilities.DEFAULT_FILTER_FACTORY.equal(
                                                                FeatureUtilities.DEFAULT_FILTER_FACTORY.property(rasterManager.timeAttribute), 
                                                                FeatureUtilities.DEFAULT_FILTER_FACTORY.literal(datetime),true));
                                            }else {
                                                // convert to range and create a correct range filter
                                                final DateRange range= (DateRange)datetime;
                                                timeFilter.add( 
                                                        FeatureUtilities.DEFAULT_FILTER_FACTORY.and(
                                                                FeatureUtilities.DEFAULT_FILTER_FACTORY.lessOrEqual(
                                                                        FeatureUtilities.DEFAULT_FILTER_FACTORY.property(rasterManager.timeAttribute), 
                                                                        FeatureUtilities.DEFAULT_FILTER_FACTORY.literal(range.getMaxValue())),
                                                                FeatureUtilities.DEFAULT_FILTER_FACTORY.greaterOrEqual(
                                                                        FeatureUtilities.DEFAULT_FILTER_FACTORY.property(rasterManager.timeAttribute), 
                                                                        FeatureUtilities.DEFAULT_FILTER_FACTORY.literal(range.getMinValue()))
                                                        )); 
                                            }                                                

                                        }
                                        final int sizeTime=timeFilter.size();
                                        if(sizeTime>1)//should not happen
                                            query.setFilter(
                                                    FeatureUtilities.DEFAULT_FILTER_FACTORY.and(
                                                            query.getFilter(), FeatureUtilities.DEFAULT_FILTER_FACTORY.or(timeFilter)));
                                        else
                                            if(sizeTime==1)
                                                query.setFilter(FeatureUtilities.DEFAULT_FILTER_FACTORY.and(query.getFilter(), timeFilter.get(0)));
                                }
                                
                                rasterManager.getGranules(query, visitor);

                        } else {
                            rasterManager.getGranules(mosaicBBox, visitor);    
                        }
			// get those granules
			visitor.produce();
			
			//
			// Did we actually load anything?? Notice that it might happen that
			// either we have holes inside the definition area for the mosaic
			// or we had some problem with missing tiles, therefore it might
			// happen that for some bboxes we don't have anything to load.
			//
			RenderedImage returnValue=null;
			if (visitor.granulesNumber>=1) {

				//
				// Create the mosaic image by doing a crop if necessary and also
				// managing the transparent color if applicable. Be aware that
				// management of the transparent color involves removing
				// transparency information from the input images.
				// 			
				returnValue= buildMosaic(visitor);
				if(returnValue!=null){
				    if (LOGGER.isLoggable(Level.FINE))
				        LOGGER.fine("Loaded bbox "+mosaicBBox.toString()+" while crop bbox "+request.getCropBBox().toString());
				    return returnValue;
				}
			
			}
                        if (LOGGER.isLoggable(Level.FINE))
                            LOGGER.fine("Creating constant image for area with no data");
                        
                        // if we get here that means that we do not have anything to load
                        // but still we are inside the definition area for the mosaic,
                        // therefore we create a fake coverage using the background values,
                        // if provided (defaulting to 0), as well as the compute raster
                        // bounds, envelope and grid to world.

                        final Number[] values = Utils.getBackgroundValues(rasterManager.defaultSM, backgroundValues);
                        // create a constant image with a proper layout
                        return ConstantDescriptor.create(
                                Float.valueOf(rasterBounds.width),
                                Float.valueOf(rasterBounds.height),
                                values,
                                rasterManager.defaultImageLayout!=null?new RenderingHints(JAI.KEY_IMAGE_LAYOUT,rasterManager.defaultImageLayout):null);
			

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
			granule = ImageUtilities.maskColor(transparentColor, granule);
			alphaIndex[0]= granule.getColorModel().getNumComponents() - 1 ;
		}
		//
		// ROI
		//
		if (alphaIn || doTransparentColor) {
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
	 * @param visitor 
	 * 
	 * @return A {@link RenderedImage}}.
	 */
	private RenderedImage buildMosaic(final MosaicBuilder visitor) throws IOException  {

	    // build final layout and use it for cropping purposes
		final ImageLayout layout = new ImageLayout(
				rasterBounds.x,
				rasterBounds.y,
				rasterBounds.width,
				rasterBounds.height);
		
                //prepare hints
                final Dimension tileDimensions=request.getTileDimensions();
                if(tileDimensions!=null)
                        layout.setTileHeight(tileDimensions.width).setTileWidth(tileDimensions.height);
                final RenderingHints localHints = new RenderingHints(JAI.KEY_IMAGE_LAYOUT,layout);
                if (hints != null && !hints.isEmpty()){
                    if (hints.containsKey(JAI.KEY_TILE_CACHE)){
                        final Object tc = hints.get(JAI.KEY_TILE_CACHE);
                        if (tc != null && tc instanceof TileCache)
                            localHints.add(new RenderingHints(JAI.KEY_TILE_CACHE, (TileCache) tc));
                    }
                    boolean addBorderExtender = true;
                    if (hints != null && hints.containsKey(JAI.KEY_BORDER_EXTENDER)){
                        final Object extender = hints.get(JAI.KEY_BORDER_EXTENDER);
                        if (extender != null && extender instanceof BorderExtender) {
                            localHints.add(new RenderingHints(JAI.KEY_BORDER_EXTENDER, (BorderExtender) extender));
                            addBorderExtender = false;
                        }
                    }
                    if (addBorderExtender){
                        localHints.add(ImageUtilities.BORDER_EXTENDER_HINTS);
                    }
                    if (hints.containsKey(JAI.KEY_TILE_SCHEDULER)){
                        final Object ts = hints.get(JAI.KEY_TILE_SCHEDULER);
                        if (ts != null && ts instanceof TileScheduler)
                            localHints.add(new RenderingHints(JAI.KEY_TILE_SCHEDULER, (TileScheduler) ts));
                    }
                }	
                
		//
		// SPECIAL CASE
		// 1 single tile, we try not do a mosaic.
		if(visitor.granulesNumber==1 && Utils.OPTIMIZE_CROP){
		    // the roi is exactly equal to the 
		    final ROI roi = visitor.rois.get(0);
		    Rectangle bounds = toRectangle(roi.getAsShape());
	        if (bounds != null) {
	            final RenderedImage image= visitor.getSourcesAsArray()[0];
	            final Rectangle imageBounds= PlanarImage.wrapRenderedImage(image).getBounds();
	            if(imageBounds.equals(bounds)){
	                
	                // do we need to crop
	                if(!imageBounds.equals(rasterBounds)){
	                    // we have to crop
	                    
	                    XRectangle2D.intersect(imageBounds, rasterBounds, imageBounds);
	                    
	                    if(imageBounds.isEmpty()){
	                        // return back a constant image
	                        return null;
	                    }
	                    // crop
	                    return CropDescriptor.create(
	                            image, 
	                            new Float(imageBounds.x), 
	                            new Float(imageBounds.y), 
	                            new Float(imageBounds.width), 
	                            new Float(imageBounds.height), 
	                            localHints);
	                }
	                return image;
	            }
	        }
		}


		final ROI[] sourceRoi = visitor.sourceRoi;
		final RenderedImage mosaic = MosaicDescriptor.create(
		        visitor.getSourcesAsArray(), 
		        request.isBlend()? MosaicDescriptor.MOSAIC_TYPE_BLEND: MosaicDescriptor.MOSAIC_TYPE_OVERLAY, 
		        (alphaIn || visitor.doInputTransparency) ? visitor.alphaChannels : null, sourceRoi, 
		        visitor.sourceThreshold, 
		        backgroundValues, 
		        localHints);
		
		if (setRoiProperty) {
		    
    		    //Adding globalRoi to the output
    		    RenderedOp rop = (RenderedOp) mosaic;
                    ROI globalRoi = null;
                    ROI[] rois = sourceRoi;
                    for (int i=0; i<rois.length; i++){
                        if (globalRoi == null){
                              globalRoi = new ROIGeometry(((ROIGeometry)rois[i]).getAsGeometry());
                        } else {
                            globalRoi = globalRoi.add(rois[i]);
                        }
                    }
                    rop.setProperty("ROI", globalRoi);
		}

		if (LOGGER.isLoggable(Level.FINE))
			LOGGER.fine("Mosaic created ");


		// create the coverage
		return mosaic;

	}
	
	/**
	 * Checks if the Shape equates to a Rectangle, if it does it performs a conversion, otherwise
	 * returns null
	 * @param shape
	 * @return
	 */
	Rectangle toRectangle(Shape shape) {
	    if(shape instanceof Rectangle) {
	        return (Rectangle) shape;
	    }
	    
	    if(shape == null) {
	        return null;
	    }
	    
	    // check if it's equivalent to a rectangle
	    PathIterator iter = shape.getPathIterator(new AffineTransform());
        double[] coords = new double[2];
	    
        // not enough points?
	    if(iter.isDone()) {
	        return null;
	    }
	    
	    // get the first and init the data structures
	    iter.next();
	    int action = iter.currentSegment(coords);
	    if(action != PathIterator.SEG_MOVETO && action != PathIterator.SEG_LINETO) {
	        return null;
	    }
        double minx = coords[0];
        double miny = coords[1];
        double maxx = minx;
        double maxy = miny;
        double prevx = minx;
        double prevy = miny;
        int i = 0;
        
        // at most 4 steps, if more it's not a strict rectangle
	    for (; i < 4 && !iter.isDone(); i++) {
	        iter.next();
	        action = iter.currentSegment(coords);
	        
	        if(action == PathIterator.SEG_CLOSE) {
	            break;
	        }
	        if(action != PathIterator.SEG_LINETO) {
	            return null;
	        }
	        
	        // check orthogonal step (x does not change and y does, or vice versa)
            double x = coords[0];
	        double y = coords[1];
	        if(!(prevx == x && prevy != y) &&
	           !(prevx != x && prevy == y)) {
	            return null;
	        }
	        
	        // update mins and maxes
	        if(x < minx) {
	            minx = x;
	        } else if(x > maxx) {
	            maxx = x;
	        }
	        if(y < miny) {
	            miny = y;
	        } else if(y > maxy) {
	            maxy = y;
	        }
	        
	        // keep track of prev step
	        prevx = x;
	        prevy = y;
        }
	    
	    // if more than 4 other points it's not a standard rectangle
	    iter.next();
	    if(!iter.isDone() || i != 3) {
	        return null;
	    }
	    
	    // turn it into a rectangle
	    return new Rectangle2D.Double(minx, miny, maxx - minx, maxy - miny).getBounds();
	}
	
	/**
	 * This method is responsible for creating a coverage from the supplied {@link RenderedImage}.
	 * 
	 * @param image
	 * @return
	 * @throws IOException
	 */
	private GridCoverage2D prepareCoverage(RenderedImage image) throws IOException {
		
		// creating bands
        final SampleModel sm=image.getSampleModel();
        final ColorModel cm=image.getColorModel();
		final int numBands = sm.getNumBands();
		final GridSampleDimension[] bands = new GridSampleDimension[numBands];
		// setting bands names.
		for (int i = 0; i < numBands; i++) {
			// color interpretation
	        final ColorInterpretation colorInterpretation=TypeMap.getColorInterpretation(cm, i);
	        if(colorInterpretation==null)
	               throw new IOException("Unrecognized sample dimension type");
	        
	        // sample dimension type
	        final SampleDimensionType st=TypeMap.getSampleDimensionType(sm, i);
		    
	        // set some no data values, as well as Min and Max values
	        final double noData;
	        double min=-Double.MAX_VALUE,max=Double.MAX_VALUE;
	        if(backgroundValues!=null)
	        {
	        	// sometimes background values are not specified as 1 per each band, therefore we need to be careful
	        	noData= backgroundValues[backgroundValues.length > i ? i:0];
	        }
	        else
	        {
	        	if(st.compareTo(SampleDimensionType.REAL_32BITS)==0)
	        		noData= Float.NaN;
	        	else
	        		if(st.compareTo(SampleDimensionType.REAL_64BITS)==0)
		        		noData= Double.NaN;
	        		else
		        		if(st.compareTo(SampleDimensionType.SIGNED_16BITS)==0)
		        		{
		        			noData=Short.MIN_VALUE;
		        			min=Short.MIN_VALUE;
		        			max=Short.MAX_VALUE;
		        		}
		        		else
		        			if(st.compareTo(SampleDimensionType.SIGNED_32BITS)==0)
		        			{
		        				noData= Integer.MIN_VALUE;

			        			min=Integer.MIN_VALUE;
			        			max=Integer.MAX_VALUE;		        				
		        			}
		        			else
			        			if(st.compareTo(SampleDimensionType.SIGNED_8BITS)==0)
			        			{
			        				noData= -128;
			        				min=-128;
			        				max=127;
			        			}
			        			else
			        			{
			        				//unsigned
				        			noData= 0;
				        			min=0;
				        			
				        			
				        			// compute max
				        			if(st.compareTo(SampleDimensionType.UNSIGNED_1BIT)==0)
				        				max=1;
				        			else
				        				if(st.compareTo(SampleDimensionType.UNSIGNED_2BITS)==0)
				        					max=3;
					        			else
					        				if(st.compareTo(SampleDimensionType.UNSIGNED_4BITS)==0)
					        					max=7;
					        				else
						        				if(st.compareTo(SampleDimensionType.UNSIGNED_8BITS)==0)
						        					max=255;
						        				else
							        				if(st.compareTo(SampleDimensionType.UNSIGNED_16BITS)==0)
							        					max=65535;
							        				else
								        				if(st.compareTo(SampleDimensionType.UNSIGNED_32BITS)==0)
								        					max=Math.pow(2, 32)-1;
				        							        			
			        			}
	        	
		        		     
	        }
	        bands[i] = new SimplifiedGridSampleDimension(
	        		colorInterpretation.name(),
	        		st,
	        		colorInterpretation,
	        		noData,
	        		min,
	        		max,
	        		1,							//no scale 
	        		0,							//no offset
	        		null
	        		).geophysics(true);
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
                null);		

	}

}
