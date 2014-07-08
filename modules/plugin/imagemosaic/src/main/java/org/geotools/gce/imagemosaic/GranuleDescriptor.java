/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2013, Open Source Geospatial Foundation (OSGeo)
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

import it.geosolutions.imageio.pam.PAMDataset;
import it.geosolutions.imageio.pam.PAMParser;
import it.geosolutions.imageio.utilities.ImageIOUtilities;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Rectangle2D;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
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
import javax.media.jai.TileCache;
import javax.media.jai.TileScheduler;

import org.apache.commons.beanutils.MethodUtils;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.data.DataUtilities;
import org.geotools.factory.Hints;
import org.geotools.gce.imagemosaic.catalog.MultiLevelROI;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.image.ImageWorker;
import org.geotools.image.io.ImageIOExt;
import org.geotools.image.jai.Registry;
import org.geotools.referencing.CRS;
import org.geotools.referencing.operation.builder.GridToEnvelopeMapper;
import org.geotools.referencing.operation.matrix.XAffineTransform;
import org.geotools.referencing.operation.transform.AffineTransform2D;
import org.geotools.resources.coverage.CoverageUtilities;
import org.geotools.resources.geometry.XRectangle2D;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Errors;
import org.geotools.resources.image.ImageUtilities;
import org.jaitools.imageutils.ROIGeometry;
import org.jaitools.media.jai.vectorbinarize.VectorBinarizeDescriptor;
import org.jaitools.media.jai.vectorbinarize.VectorBinarizeRIF;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.geometry.BoundingBox;
import org.opengis.referencing.datum.PixelInCell;
import org.opengis.referencing.operation.MathTransform2D;
import org.opengis.referencing.operation.TransformException;

import com.vividsolutions.jts.geom.Geometry;

/**
 * A granuleDescriptor is a single piece of the mosaic, with its own overviews and
 * everything.
 * 
 * <p>
 * This class is responsible for caching the various size of the different
 * levels of each single granuleDescriptor since computing them each time is expensive
 * (opening a file, looking for a reader, parsing metadata,etc...).
 * 
 * <p>
 * Right now we are making the assumption that a single granuleDescriptor is made a by a
 * single file with embedded overviews, either explicit or intrinsic through wavelets like MrSID,
 * ECW or JPEG2000.
 *      
 * @author Simone Giannecchini, GeoSolutions S.A.S.
 * @author Stefan Alfons Krueger (alfonx), Wikisquare.de : Support for jar:file:foo.jar/bar.properties URLs
 * @since 2.5.5
 *
 *
 * @source $URL$
 */
public class GranuleDescriptor {
    
	/** Logger. */
	private final static Logger LOGGER = org.geotools.util.logging.Logging.getLogger(GranuleDescriptor.class);

    private static final String AUXFILE_EXT = ".aux.xml";

    static {
        try {
            Registry.registerRIF(JAI.getDefaultInstance(), new VectorBinarizeDescriptor(),
                    new VectorBinarizeRIF(), Registry.JAI_TOOLS_PRODUCT);
        } catch (Exception e) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, e.getLocalizedMessage());
            }
        }
    }
	
  
    OverviewsController overviewsController;
    
	/**
	 * This class represent an overview level in a single granuleDescriptor.
	 * 
	 * <p> Notice that the internal transformations for the various levels are reffered to the corner, rather than to the centre.
	 * @author Simone Giannecchini, GeoSolutions S.A.S.
	 *
	 */
	class GranuleOverviewLevelDescriptor{

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

		public GranuleOverviewLevelDescriptor(final double scaleX,final double scaleY,final int width,final int height) {
			this.scaleX = scaleX;
			this.scaleY = scaleY;
			this.baseToLevelTransform=new AffineTransform2D( XAffineTransform.getScaleInstance(scaleX,scaleY,0,0));
			
			final AffineTransform gridToWorldTransform_ = new AffineTransform(baseToLevelTransform);
			gridToWorldTransform_.preConcatenate(CoverageUtilities.CENTER_TO_CORNER);
			gridToWorldTransform_.preConcatenate(baseGridToWorld);
			this.gridToWorldTransformCorner=new AffineTransform2D(gridToWorldTransform_);
			this.width = width;
			this.height = height;
			this.rasterDimensions= new Rectangle(0,0,width,height);
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
			buffer.append("Description of a granuleDescriptor level").append("\n")
			.append("width:\t\t").append(width).append("\n")
			.append("height:\t\t").append(height).append("\n")
			.append("scaleX:\t\t").append(scaleX).append("\n")
			.append("scaleY:\t\t").append(scaleY).append("\n")
			.append("baseToLevelTransform:\t\t").append(baseToLevelTransform.toString()).append("\n")
			.append("gridToWorldTransform:\t\t").append(gridToWorldTransformCorner.toString()).append("\n");
			return buffer.toString();
		}
		
	}
	
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

        PAMDataset pamDataset;

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

        GranuleLoadingResult(RenderedImage loadedImage, ROI footprint, URL granuleUrl, final boolean doFiltering, final PAMDataset pamDataset) {
            this.loadedImage = loadedImage;
            Object roi = loadedImage.getProperty("ROI");
            if(roi instanceof ROI) {
                this.footprint = (ROI) roi;
            }            
            this.granuleUrl = granuleUrl;
            this.doFiltering = doFiltering;
            this.pamDataset = pamDataset;
        }
    }

    private static PAMParser pamParser = PAMParser.getInstance();
    
    ReferencedEnvelope granuleBBOX;
	
	MultiLevelROI roiProvider;
        
	URL granuleUrl;
	
	int maxDecimationFactor = -1;
	
	final Map<Integer,GranuleOverviewLevelDescriptor> granuleLevels= Collections.synchronizedMap(new HashMap<Integer,GranuleOverviewLevelDescriptor>());
	
	AffineTransform baseGridToWorld;
	
	ImageReaderSpi cachedReaderSPI;

	SimpleFeature originator;
	
	PAMDataset pamDataset;
	
	boolean handleArtifactsFiltering = false;
	
	boolean filterMe = false;

        ImageInputStreamSpi cachedStreamSPI;

        private GridToEnvelopeMapper geMapper;
        
	private void init(final BoundingBox granuleBBOX, final URL granuleUrl,
			final ImageReaderSpi suggestedSPI, final MultiLevelROI roiProvider,
			final boolean heterogeneousGranules, final boolean handleArtifactsFiltering, final Hints hints) {
		this.granuleBBOX = ReferencedEnvelope.reference(granuleBBOX);
		this.granuleUrl = granuleUrl;
		this.roiProvider = roiProvider;
		this.handleArtifactsFiltering = handleArtifactsFiltering;
    		filterMe = handleArtifactsFiltering && roiProvider != null;
                
		
		// create the base grid to world transformation
		ImageInputStream inStream = null;
		ImageReader reader = null;
		try {
			//
			//get info about the raster we have to read
			//
			
			// get a stream
		        if(cachedStreamSPI==null){
		            cachedStreamSPI = Utils.getInputStreamSPIFromURL(granuleUrl);
		        }
		        assert cachedStreamSPI!=null:"no cachedStreamSPI available!";
			inStream = cachedStreamSPI.createInputStreamInstance(granuleUrl, ImageIO.getUseCache(), ImageIO.getCacheDirectory());
			if(inStream == null){
                            final File file = DataUtilities.urlToFile(granuleUrl);
                            if(file!=null){
                                if(LOGGER.isLoggable(Level.WARNING)){
                                    LOGGER.log(Level.WARNING,Utils.getFileInfo(file));
                                }
                            }
			    throw new IllegalArgumentException("Unable to get an input stream for the provided file "+granuleUrl.toString());
			}
			
			// get a reader and try to cache the suggested SPI first
			if(cachedReaderSPI == null){
			    cachedReaderSPI = Utils.getReaderSpiFromStream(suggestedSPI, inStream);
				
			}
			if (reader == null) {
			    if (cachedReaderSPI == null) {
			        throw new IllegalArgumentException("Unable to get a ReaderSPI for the provided input: " + granuleUrl.toString());
			    }
			    reader = cachedReaderSPI.createReaderInstance();
			}
			
			if(reader == null)
				throw new IllegalArgumentException("Unable to get an ImageReader for the provided file "+granuleUrl.toString());
			boolean ignoreMetadata = customizeReaderInitialization(reader, hints);
			reader.setInput(inStream, false, ignoreMetadata);
			//get selected level and base level dimensions
			final Rectangle originalDimension = Utils.getDimension(0, reader);
			
			// build the g2W for this tile, in principle we should get it
			// somehow from the tile itself or from the index, but at the moment
			// we do not have such info, hence we assume that it is a simple
			// scale and translate
			this.geMapper= new GridToEnvelopeMapper(new GridEnvelope2D(originalDimension), granuleBBOX);
			geMapper.setPixelAnchor(PixelInCell.CELL_CENTER);//this is the default behavior but it is nice to write it down anyway
			this.baseGridToWorld = geMapper.createAffineTransform();
			
			// add the base level
			this.granuleLevels.put(Integer.valueOf(0), new GranuleOverviewLevelDescriptor(1, 1, originalDimension.width, originalDimension.height));
			
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
			    final int numberOfOvervies = reader.getNumImages(true) - 1;
			    final AffineTransform2D baseG2W = baseOverviewLevelDescriptor.getGridToWorldTransform();
			    final int width = baseOverviewLevelDescriptor.getWidth();
			    final int height = baseOverviewLevelDescriptor.getHeight();
			    final double resX = AffineTransform2D.getScaleX0(baseG2W);
			    final double resY = AffineTransform2D.getScaleY0(baseG2W);
			    final double[] highestRes = new double[]{resX, resY};
			    final double[][] overviewsResolution = new double[numberOfOvervies][2];
			    
			    // Populating overviews and initializing overviewsController
			    for (int i = 0; i < numberOfOvervies; i++){
			        overviewsResolution[i][0]= (highestRes[0] * width) / reader.getWidth(i + 1);
			        overviewsResolution[i][1]= (highestRes[1] * height) / reader.getHeight(i + 1);
			    }
			    overviewsController = new OverviewsController(highestRes, numberOfOvervies, overviewsResolution);
			}
                        //////////////////////////////////////////////////////////////////////////
			
			if (hints != null && hints.containsKey(Utils.CHECK_AUXILIARY_METADATA)) {
			    boolean checkAuxiliaryMetadata = (Boolean) hints.get(Utils.CHECK_AUXILIARY_METADATA);
			    if (checkAuxiliaryMetadata) {
			        checkPamDataset();
			    }
			}
			

		} catch (IllegalStateException e) {
			throw new IllegalArgumentException(e);
			
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		} finally {
		    // close/dispose stream and readers
			try {
				if(inStream != null){
					inStream.close();
				}
			} catch (Throwable e) {
				throw new IllegalArgumentException(e);
			} finally{
				if (reader != null){
					reader.dispose();
				}
			}
		}
	}
	
	/**
	 * Look for GDAL Auxiliary File and unmarshall it to setup a PamDataset if available 
	 * @throws IOException
	 */
	private void checkPamDataset() throws IOException {
	    final File file = DataUtilities.urlToFile(granuleUrl);
            final String path = file.getCanonicalPath();
            final String auxFile = path + AUXFILE_EXT;
            pamDataset = pamParser.parsePAM(auxFile);
    }

    private boolean customizeReaderInitialization(ImageReader reader, Hints hints) {
            String classString = reader.getClass().getName();
            // Special Management for NetCDF readers to set external Auxiliary File
            if (hints != null && hints.containsKey(Utils.AUXILIARY_FILES_PATH)) {
                if (classString.equalsIgnoreCase("org.geotools.imageio.netcdf.NetCDFImageReader")) {
                    try {
                        String auxiliaryFilePath = (String) hints.get(Utils.AUXILIARY_FILES_PATH);
                        if (hints.containsKey(Utils.PARENT_DIR)) {
                            String parentDir = (String) hints.get(Utils.PARENT_DIR);
                            // if the path stars with the parentDir, it's already absolute (old configuration file)
                            if (!auxiliaryFilePath.startsWith(parentDir)) {
                                auxiliaryFilePath = parentDir + File.separatorChar + auxiliaryFilePath;
                            }
                        }
                        MethodUtils.invokeMethod(reader, "setAuxiliaryFilesPath", auxiliaryFilePath);
                        return true;
                    } catch (NoSuchMethodException e) {
                        throw new RuntimeException(e);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    } catch (InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            return false;
        
    }

    public GranuleDescriptor(
                final String granuleLocation,
                final BoundingBox granuleBBox, 
                final ImageReaderSpi suggestedSPI,
                final MultiLevelROI roiProvider) {
	    this (granuleLocation, granuleBBox, suggestedSPI, roiProvider, -1, false);
	}
	
	public GranuleDescriptor(
                final String granuleLocation,
                final BoundingBox granuleBBox, 
                final ImageReaderSpi suggestedSPI,
                final MultiLevelROI roiProvider,
                final boolean heterogeneousGranules) {
            this (granuleLocation, granuleBBox, suggestedSPI, roiProvider, -1, heterogeneousGranules);
        }
	
	public GranuleDescriptor(
                final String granuleLocation,
                final BoundingBox granuleBBox, 
                final ImageReaderSpi suggestedSPI,
                final MultiLevelROI roiProvider,
                final int maxDecimationFactor){
	    this(granuleLocation, granuleBBox, suggestedSPI, roiProvider, maxDecimationFactor, false);
	    
	}
	
	public GranuleDescriptor(
	        final String granuleLocation,
                final BoundingBox granuleBBox, 
                final ImageReaderSpi suggestedSPI,
                final MultiLevelROI roiProvider,
                final int maxDecimationFactor, 
                final boolean heterogeneousGranules) {
		this(granuleLocation, granuleBBox, suggestedSPI, roiProvider, maxDecimationFactor, heterogeneousGranules, false);				
    }

	public GranuleDescriptor(
	        final String granuleLocation,
                final BoundingBox granuleBBox, 
                final ImageReaderSpi suggestedSPI,
                final MultiLevelROI roiProvider,
                final int maxDecimationFactor, 
                final boolean heterogeneousGranules,
				final boolean handleArtifactsFiltering) {

	    this.maxDecimationFactor = maxDecimationFactor;
            final URL rasterFile = DataUtilities.fileToURL(new File(granuleLocation));
            
            if (rasterFile == null) {
                    return;
            }
            
            if (LOGGER.isLoggable(Level.FINER)) {
                LOGGER.finer("File found " + granuleLocation);
            }
    
            this.originator = null;
            init (granuleBBox, rasterFile, suggestedSPI, roiProvider, heterogeneousGranules, handleArtifactsFiltering, null);
        
	}
	
    /**
     * 
     * @param feature
     * @param suggestedSPI
     * @param pathType
     * @param locationAttribute
     * @param parentLocation
     */
    public GranuleDescriptor(
            final SimpleFeature feature, 
            final ImageReaderSpi suggestedSPI,
            final PathType pathType,
            final String locationAttribute,
            final String parentLocation) {
                this(feature,suggestedSPI,pathType,locationAttribute,parentLocation, false);
                }
    
    public GranuleDescriptor(SimpleFeature feature, ImageReaderSpi suggestedSPI,
            PathType pathType, String locationAttribute, String parentLocation,
            boolean heterogeneousGranules, Hints hints) {
        this(feature,suggestedSPI,pathType,locationAttribute,parentLocation, null, heterogeneousGranules, hints);
        
    }
    
    public GranuleDescriptor(SimpleFeature feature, ImageReaderSpi suggestedSPI,
            PathType pathType, String locationAttribute, String parentLocation,
            boolean heterogeneousGranules) {
        this(feature,suggestedSPI,pathType,locationAttribute,parentLocation, heterogeneousGranules, null);
    }
    
    /**
     * Constructor for the {@link GranuleDescriptor} assuming it doesn't belong to an 
     *  heterogeneous granules set.
     * @param feature a {@link SimpleFeature} referring to that granule
     * @param suggestedSPI the suggested {@link ImageReaderSpi} to be used to get a reader
     *          to handle this granule.
     * @param pathType A {@link PathType} identifying if the granule location should be resolved as
     *          a relative or an absolute path. 
     * @param locationAttribute the attribute containing the granule location.
     * @param parentLocation the location of the parent of that granule.
     * @param inclusionGeometry the footprint of that granule (if any). It may be null.
     */
    public GranuleDescriptor(
            SimpleFeature feature, 
            ImageReaderSpi suggestedSPI,
            PathType pathType,
            final String locationAttribute,
            final String parentLocation,
            final MultiLevelROI roiProvider) {
        this(feature,suggestedSPI,pathType,locationAttribute,parentLocation, roiProvider, false, null);
    }
    
    /**
     * Constructor for the {@link GranuleDescriptor}
     * @param feature a {@link SimpleFeature} referring to that granule
     * @param suggestedSPI the suggested {@link ImageReaderSpi} to be used to get a reader
     *          to handle this granule.
     * @param pathType A {@link PathType} identifying if the granule location should be resolved as
     *          a relative or an absolute path. 
     * @param locationAttribute the attribute containing the granule location.
     * @param parentLocation the location of the parent of that granule.
     * @param inclusionGeometry the footprint of that granule (if any). It may be null.
     * @param heterogeneousGranules if {@code true}, this granule belongs to a set of heterogeneous granules
     */
	public GranuleDescriptor(
			final SimpleFeature feature, 
			final ImageReaderSpi suggestedSPI,
			final PathType pathType,
			final String locationAttribute,
			final String parentLocation,
			final MultiLevelROI roiProvider,
			final boolean heterogeneousGranules,
			final Hints hints) {
		// Get location and envelope of the image to load.
		final String granuleLocation = (String) feature.getAttribute(locationAttribute);
		final ReferencedEnvelope granuleBBox = ReferencedEnvelope.reference(feature.getBounds());
		

		// If the granuleDescriptor is not there, dump a message and continue
		final URL rasterFile = pathType.resolvePath(parentLocation, granuleLocation);
		if (rasterFile == null) {
	                throw new IllegalArgumentException(Errors.format(ErrorKeys.ILLEGAL_ARGUMENT_$2,"granuleLocation",granuleLocation));		    
			
		}
		if (LOGGER.isLoggable(Level.FINE))
			LOGGER.fine("File found "+granuleLocation);

		this.originator=feature;
		init(granuleBBox,rasterFile,suggestedSPI, roiProvider, heterogeneousGranules, false, hints);
		
		
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
			final Hints hints) throws IOException {
		
		if (LOGGER.isLoggable(java.util.logging.Level.FINER)){
		    final String name = Thread.currentThread().getName();
			LOGGER.finer("Thread:" + name + " Loading raster data for granuleDescriptor "+this.toString());
		}
		ImageReadParam readParameters = null;
		int imageIndex;
		final boolean useFootprint = roiProvider != null&&request.getFootprintBehavior()!=FootprintBehavior.None;
		Geometry inclusionGeometry = useFootprint ? roiProvider.getFootprint(): null;
		final ReferencedEnvelope bbox = useFootprint? 
		        new ReferencedEnvelope(granuleBBOX.intersection(inclusionGeometry.getEnvelopeInternal()), granuleBBOX.getCoordinateReferenceSystem()):
		            granuleBBOX;
		boolean doFiltering = false;
                if (filterMe && useFootprint){
                    doFiltering = Utils.areaIsDifferent(inclusionGeometry, baseGridToWorld, granuleBBOX);
                }
		
		
                // intersection of this tile bound with the current crop bbox
                final ReferencedEnvelope intersection = new ReferencedEnvelope(bbox.intersection(cropBBox), cropBBox.getCoordinateReferenceSystem());
                if (intersection.isEmpty()) {
                    if (LOGGER.isLoggable(java.util.logging.Level.FINE)){
                            LOGGER.fine(new StringBuilder("Got empty intersection for granule ").append(this.toString())
                                    .append(" with request ").append(request.toString()).append(" Resulting in no granule loaded: Empty result").toString());
                    }
                    return null;
                }
                
        // check if the requested bbox intersects or overlaps the requested area 
        if(useFootprint && inclusionGeometry != null && !JTS.toGeometry(cropBBox).intersects(inclusionGeometry)) {
            if (LOGGER.isLoggable(java.util.logging.Level.FINE)) {
                LOGGER.fine(new StringBuilder("Got empty intersection for granule ").append(this.toString())
                        .append(" with request ").append(request.toString()).append(" Resulting in no granule loaded: Empty result").toString());
            }
            return null;
        }
                

		ImageInputStream inStream=null;
		ImageReader reader=null;
		try {
			//
			//get info about the raster we have to read
			//
			
			// get a stream
		        assert cachedStreamSPI!=null:"no cachedStreamSPI available!";
                        inStream = cachedStreamSPI.createInputStreamInstance(granuleUrl, ImageIO.getUseCache(), ImageIO.getCacheDirectory());
			if(inStream==null)
				return null;
			
	
			// get a reader and try to cache the relevant SPI
			if(cachedReaderSPI==null){
				reader = ImageIOExt.getImageioReader(inStream);
				if(reader!=null)
					cachedReaderSPI=reader.getOriginatingProvider();
			}
			else
				reader=cachedReaderSPI.createReaderInstance();
			if(reader==null) {
				if (LOGGER.isLoggable(java.util.logging.Level.WARNING)){
					LOGGER.warning(new StringBuilder("Unable to get s reader for granuleDescriptor ").append(this.toString())
					        .append(" with request ").append(request.toString()).append(" Resulting in no granule loaded: Empty result").toString());
				}
				return null;
			}
			// set input
			customizeReaderInitialization(reader, hints);
			reader.setInput(inStream);
			
			// Checking for heterogeneous granules
			if (request.isHeterogeneousGranules()){
			    // create read parameters
			    readParameters = new ImageReadParam();
			    
			    //override the overviews controller for the base layer
			    imageIndex = ReadParamsController.setReadParams(
			            request.spatialRequestHelper.getComputedResolution(),
			            request.getOverviewPolicy(),
			            request.getDecimationPolicy(), 
			            readParameters,
			            request.rasterManager,
			            overviewsController);
			} else {
			    imageIndex = index;
			    readParameters = imageReadParameters;
			}
			
			//get selected level and base level dimensions
			final GranuleOverviewLevelDescriptor selectedlevel= getLevel(imageIndex,reader);
	
			
			// now create the crop grid to world which can be used to decide
			// which source area we need to crop in the selected level taking
			// into account the scale factors imposed by the selection of this
			// level together with the base level grid to world transformation
            AffineTransform2D cropWorldToGrid= new AffineTransform2D(selectedlevel.gridToWorldTransformCorner);
            cropWorldToGrid=(AffineTransform2D) cropWorldToGrid.inverse();
			// computing the crop source area which lives into the
			// selected level raster space, NOTICE that at the end we need to
			// take into account the fact that we might also decimate therefore
			// we cannot just use the crop grid to world but we need to correct
			// it.
			final Rectangle sourceArea = CRS.transform(cropWorldToGrid, intersection).toRectangle2D().getBounds();
			//gutter
			if(selectedlevel.baseToLevelTransform.isIdentity()){
			    sourceArea.grow(2, 2);
			}
			XRectangle2D.intersect(sourceArea, selectedlevel.rasterDimensions, sourceArea);//make sure roundings don't bother us
			// is it empty??
			if (sourceArea.isEmpty()) {
				if (LOGGER.isLoggable(java.util.logging.Level.FINE)){
					LOGGER.fine("Got empty area for granuleDescriptor "+this.toString()+
					        " with request "+request.toString()+" Resulting in no granule loaded: Empty result");
					
				}
				return null;

			} else if (LOGGER.isLoggable(java.util.logging.Level.FINER)){
				LOGGER.finer( "Loading level " + imageIndex + " with source region: "
				        + sourceArea + " subsampling: " 
				        + readParameters.getSourceXSubsampling() + "," 
				        + readParameters.getSourceYSubsampling() + " for granule:" 
				        + granuleUrl) ;
			}

			// Setting subsampling 
			int newSubSamplingFactor = 0;
			final String pluginName = cachedReaderSPI.getPluginClassName();
			if (pluginName != null && pluginName.equals(ImageUtilities.DIRECT_KAKADU_PLUGIN)){
				final int ssx = readParameters.getSourceXSubsampling();
				final int ssy = readParameters.getSourceYSubsampling();
				newSubSamplingFactor = ImageIOUtilities.getSubSamplingFactor2(ssx, ssy);
				if (newSubSamplingFactor != 0) {
				    if (newSubSamplingFactor > maxDecimationFactor && maxDecimationFactor != -1){
				        newSubSamplingFactor = maxDecimationFactor;
				    }
				    readParameters.setSourceSubsampling(newSubSamplingFactor, newSubSamplingFactor,0,0);
				}
			}
			
			// set the source region
			readParameters.setSourceRegion(sourceArea);
			RenderedImage raster;
			try {
				// read
				raster= request.getReadType().read(readParameters,imageIndex, granuleUrl, selectedlevel.rasterDimensions, reader, hints,false);
				
			} catch (Throwable e) {
				if (LOGGER.isLoggable(java.util.logging.Level.FINE)){
					LOGGER.log(java.util.logging.Level.FINE,
					        "Unable to load raster for granuleDescriptor "
					        + this.toString() + " with request "
					        + request.toString() + " Resulting in no granule loaded: Empty result",e);
				}
				return null;
			}

			// use fixed source area
			sourceArea.setRect(readParameters.getSourceRegion());
			
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
			double decimationScaleX =  ((1.0 * sourceArea.width) / raster.getWidth());
			double decimationScaleY =  ((1.0 * sourceArea.height) / raster.getHeight());
			final AffineTransform decimationScaleTranform = XAffineTransform.getScaleInstance(decimationScaleX, decimationScaleY);

			// keep into account translation  to work into the selected level raster space
			final AffineTransform afterDecimationTranslateTranform =XAffineTransform.getTranslateInstance(sourceArea.x, sourceArea.y);
			
			// now we need to go back to the base level raster space
			final AffineTransform backToBaseLevelScaleTransform =selectedlevel.baseToLevelTransform;
			
			// now create the overall transform
			final AffineTransform finalRaster2Model = new AffineTransform(baseGridToWorld);
			finalRaster2Model.concatenate(CoverageUtilities.CENTER_TO_CORNER);
                        
			if(!XAffineTransform.isIdentity(backToBaseLevelScaleTransform, Utils.AFFINE_IDENTITY_EPS))
				finalRaster2Model.concatenate(backToBaseLevelScaleTransform);
			if(!XAffineTransform.isIdentity(afterDecimationTranslateTranform, Utils.AFFINE_IDENTITY_EPS))
				finalRaster2Model.concatenate(afterDecimationTranslateTranform);
			if(!XAffineTransform.isIdentity(decimationScaleTranform, Utils.AFFINE_IDENTITY_EPS))
				finalRaster2Model.concatenate(decimationScaleTranform);
			
            // adjust roi
            if (useFootprint) {

                ROIGeometry transformed;
                try {
                    transformed = roiProvider.getTransformedROI(finalRaster2Model.createInverse());
                    if (transformed.getAsGeometry().isEmpty()) {
                        // inset might have killed the geometry fully
                        return null;
                    } 

                    PlanarImage pi = PlanarImage.wrapRenderedImage(raster);
                    if(!transformed.intersects(pi.getBounds())) {
                        return null;
                    }
                    pi.setProperty("ROI", transformed);
                    raster = pi;

                } catch (NoninvertibleTransformException e) {
                    if (LOGGER.isLoggable(java.util.logging.Level.INFO))
                        LOGGER.info("Unable to create a granuleDescriptor " + this.toString()
                                + " due to a problem when managing the ROI");
                    return null;
                }

            }
			// keep into account translation factors to place this tile
			finalRaster2Model.preConcatenate((AffineTransform) mosaicWorldToGrid);
			final Interpolation interpolation = request.getInterpolation();
			
			
			//paranoiac check to avoid that JAI freaks out when computing its internal layouT on images that are too small
			Rectangle2D finalLayout= ImageUtilities.layoutHelper(
					raster, 
					(float)finalRaster2Model.getScaleX(), 
					(float)finalRaster2Model.getScaleY(), 
					(float)finalRaster2Model.getTranslateX(), 
					(float)finalRaster2Model.getTranslateY(), 
					interpolation);
			if(finalLayout.isEmpty()){
				if(LOGGER.isLoggable(java.util.logging.Level.INFO))
					LOGGER.info("Unable to create a granuleDescriptor " + this.toString() 
					        + " due to jai scale bug creating a null source area");
				return null;
			}
			
                        
			// apply the affine transform  conserving indexed color model
			final RenderingHints localHints = new RenderingHints(JAI.KEY_REPLACE_INDEX_COLOR_MODEL, interpolation instanceof InterpolationNearest? Boolean.FALSE:Boolean.TRUE);
			if(XAffineTransform.isIdentity(finalRaster2Model,Utils.AFFINE_IDENTITY_EPS)) {
			    return new GranuleLoadingResult(raster, null, granuleUrl, doFiltering, pamDataset);
			} else {
				//
				// In case we are asked to use certain tile dimensions we tile
				// also at this stage in case the read type is Direct since
				// buffered images comes up untiled and this can affect the
				// performances of the subsequent affine operation.
				//
				final Dimension tileDimensions=request.getTileDimensions();
				if(tileDimensions!=null&&request.getReadType().equals(ReadType.DIRECT_READ)) {
					final ImageLayout layout = new ImageLayout();
					layout.setTileHeight(tileDimensions.width).setTileWidth(tileDimensions.height);
					localHints.add(new RenderingHints(JAI.KEY_IMAGE_LAYOUT,layout));
				} else {
				    if (hints != null && hints.containsKey(JAI.KEY_IMAGE_LAYOUT)) {
			                    final Object layout = hints.get(JAI.KEY_IMAGE_LAYOUT);
			                    if (layout != null && layout instanceof ImageLayout) {
			                        localHints.add(new RenderingHints(JAI.KEY_IMAGE_LAYOUT, ((ImageLayout) layout).clone()));
			                    }
			                }
				}
				if (hints != null && hints.containsKey(JAI.KEY_TILE_CACHE)){
				    final Object cache = hints.get(JAI.KEY_TILE_CACHE);
				    if (cache != null && cache instanceof TileCache)
				        localHints.add(new RenderingHints(JAI.KEY_TILE_CACHE, (TileCache) cache));
				}
				if (hints != null && hints.containsKey(JAI.KEY_TILE_SCHEDULER)){
                                    final Object scheduler = hints.get(JAI.KEY_TILE_SCHEDULER);
                                    if (scheduler != null && scheduler instanceof TileScheduler)
                                        localHints.add(new RenderingHints(JAI.KEY_TILE_SCHEDULER, (TileScheduler) scheduler));
                                }
				boolean addBorderExtender = true;
                if (hints != null && hints.containsKey(JAI.KEY_BORDER_EXTENDER)) {
                    final Object extender = hints.get(JAI.KEY_BORDER_EXTENDER);
                    if (extender != null && extender instanceof BorderExtender) {
                        localHints.add(new RenderingHints(JAI.KEY_BORDER_EXTENDER, (BorderExtender) extender));
                        addBorderExtender = false;
                    }
                }
                // BORDER extender
                if (addBorderExtender) {
                    localHints.add(ImageUtilities.BORDER_EXTENDER_HINTS);
                }
                
                ImageWorker iw = new ImageWorker(raster);
                iw.setRenderingHints(localHints);
                iw.affine(finalRaster2Model, interpolation, request.getBackgroundValues());
				return new GranuleLoadingResult(iw.getRenderedImage(), null, granuleUrl, doFiltering, pamDataset);
			}
		
		} catch (IllegalStateException e) {
			if (LOGGER.isLoggable(java.util.logging.Level.WARNING)) {
				LOGGER.log(java.util.logging.Level.WARNING, new StringBuilder("Unable to load raster for granuleDescriptor ")
				.append(this.toString()).append(" with request ").append(request.toString()).append(" Resulting in no granule loaded: Empty result").toString(), e);
			}
			return null;
		} catch (org.opengis.referencing.operation.NoninvertibleTransformException e) {
                    if (LOGGER.isLoggable(java.util.logging.Level.WARNING)) {
                        LOGGER.log(java.util.logging.Level.WARNING, new StringBuilder("Unable to load raster for granuleDescriptor ")
                        .append(this.toString()).append(" with request ").append(request.toString()).append(" Resulting in no granule loaded: Empty result").toString(), e);
                    }
			return null;
		} catch (TransformException e) {
                    if (LOGGER.isLoggable(java.util.logging.Level.WARNING)) {
                        LOGGER.log(java.util.logging.Level.WARNING, new StringBuilder("Unable to load raster for granuleDescriptor ")
                        .append(this.toString()).append(" with request ").append(request.toString()).append(" Resulting in no granule loaded: Empty result").toString(), e);
                    }
			return null;

                } finally {
                    try {
                        if (request.getReadType() != ReadType.JAI_IMAGEREAD && inStream != null) {
                            inStream.close();
                        }
                    } finally {
                        if (request.getReadType() != ReadType.JAI_IMAGEREAD && reader != null) {
                            reader.dispose();
                        }
                    }
                }
            }

	private GranuleOverviewLevelDescriptor getLevel(final int index, final ImageReader reader) {

		if(reader==null)
			throw new NullPointerException("Null reader passed to the internal GranuleOverviewLevelDescriptor method");		
		synchronized (granuleLevels) {
			if(granuleLevels.containsKey(Integer.valueOf(index)))
				return granuleLevels.get(Integer.valueOf(index));
			else
			{
				//load level
				// create the base grid to world transformation
				try {
					//
					//get info about the raster we have to read
					//
					
					//get selected level and base level dimensions
					final Rectangle levelDimension = Utils.getDimension(index, reader);
					
					final GranuleOverviewLevelDescriptor baseLevel= granuleLevels.get(0);
					final double scaleX=baseLevel.width/(1.0*levelDimension.width);
					final double scaleY=baseLevel.height/(1.0*levelDimension.height);
					
					// add the base level
					final GranuleOverviewLevelDescriptor newLevel=new GranuleOverviewLevelDescriptor(scaleX,scaleY,levelDimension.width,levelDimension.height);
					this.granuleLevels.put(Integer.valueOf(index),newLevel);
					
					return newLevel;
					

				} catch (IllegalStateException e) {
					throw new IllegalArgumentException(e);
					
				} catch (IOException e) {
					throw new IllegalArgumentException(e);
				} 

			}			
		}
	}

	GranuleOverviewLevelDescriptor getLevel(final int index) {
		
			//load level
			// create the base grid to world transformation
			ImageInputStream inStream=null;
			ImageReader reader=null;
			try {
				
				// get a stream
			        assert cachedStreamSPI!=null:"no cachedStreamSPI available!";
			        inStream = cachedStreamSPI.createInputStreamInstance(granuleUrl, ImageIO.getUseCache(), ImageIO.getCacheDirectory());
				if(inStream==null)
					throw new IllegalArgumentException("Unable to create an inputstream for the granuleurl:"+(granuleUrl!=null?granuleUrl:"null"));
		
				// get a reader and try to cache the relevant SPI
				if(cachedReaderSPI==null){
					reader = ImageIOExt.getImageioReader( inStream);
					if(reader!=null)
						cachedReaderSPI=reader.getOriginatingProvider();
				}
				else
					reader=cachedReaderSPI.createReaderInstance();
				if(reader==null)
					throw new IllegalArgumentException("Unable to get an ImageReader for the provided file "+granuleUrl.toString());
				final boolean ignoreMetadata = customizeReaderInitialization(reader, null);
				reader.setInput(inStream, false, ignoreMetadata);
				
				// call internal method which will close everything
				return getLevel(index, reader);

			} catch (IllegalStateException e) {
				
				// clean up
				try{
					if(inStream!=null)
						inStream.close();
				}
				catch (Throwable ee) {
					
				}
				finally{
					if(reader!=null)
						reader.dispose();
				}
				
				throw new IllegalArgumentException(e);
				
			} catch (IOException e) {
				
				// clean up
				try{
					if(inStream!=null)
						inStream.close();
				}
				catch (Throwable ee) {
				}
				finally{
					if(reader!=null)
						reader.dispose();
				}
				
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
		int i=1;
		for(final GranuleOverviewLevelDescriptor granuleOverviewLevelDescriptor : granuleLevels.values())
		{
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
        if(roiProvider == null) {
            return null;
        } else {
            return roiProvider.getFootprint();
        }
    }

}
