/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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

import java.awt.Rectangle;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.spi.ImageReaderSpi;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.data.DataSourceException;
import org.geotools.data.DataUtilities;
import org.geotools.data.Query;
import org.geotools.data.QueryCapabilities;
import org.geotools.factory.Hints;
import org.geotools.feature.visitor.FeatureCalc;
import org.geotools.feature.visitor.MaxVisitor;
import org.geotools.feature.visitor.MinVisitor;
import org.geotools.feature.visitor.UniqueVisitor;
import org.geotools.filter.SortByImpl;
import org.geotools.gce.imagemosaic.catalog.GranuleCatalog;
import org.geotools.gce.imagemosaic.catalog.GranuleCatalogFactory;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.operation.transform.AffineTransform2D;
import org.geotools.resources.coverage.FeatureUtilities;
import org.geotools.util.Utilities;
import org.opengis.coverage.grid.Format;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.coverage.grid.GridCoverageReader;
import org.opengis.coverage.grid.GridCoverageWriter;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.filter.sort.SortBy;
import org.opengis.filter.sort.SortOrder;
import org.opengis.geometry.BoundingBox;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValue;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
/**
 * This reader is responsible for providing access to mosaic of georeferenced
 * images. Citing JAI documentation:
 * 
 * The "Mosaic" operation creates a mosaic of two or more source images. This
 * operation could be used for example to assemble a set of overlapping
 * geospatially rectified images into a contiguous image. It could also be used
 * to create a montage of photographs such as a panorama.
 * 
 * All source images are assumed to have been geometrically mapped into a common
 * coordinate space. The origin (minX, minY) of each image is therefore taken to
 * represent the location of the respective image in the common coordinate
 * system of the source images. This coordinate space will also be that of the
 * destination image.
 * 
 * All source images must have the same data type and sample size for all bands
 * and have the same number of bands as color components. The destination will
 * have the same data type, sample size, and number of bands and color
 * components as the sources.
 * 
 * 
 * @author Simone Giannecchini, GeoSolutions S.A.S
 * @author Stefan Alfons Krueger (alfonx), Wikisquare.de : Support for jar:file:foo.jar/bar.properties URLs
 * @since 2.3
 * 
 */
public final class ImageMosaicReader extends AbstractGridCoverage2DReader implements GridCoverageReader, GridCoverageWriter {

        /** Logger. */
	private final static Logger LOGGER = org.geotools.util.logging.Logging.getLogger(ImageMosaicReader.class);

	/**
	 * The source {@link URL} pointing to the index shapefile for this
	 * {@link ImageMosaicReader}.
	 */
	URL sourceURL;

	boolean expandMe;
	
	PathType pathType;
	
	ExecutorService multiThreadedLoader = null;

	String locationAttributeName="location";

	RasterManager rasterManager;

	int maxAllowedTiles=ImageMosaicFormat.MAX_ALLOWED_TILES.getDefaultValue();

	/** The suggested SPI to avoid SPI lookup*/
	ImageReaderSpi suggestedSPI;

	GranuleCatalog catalog;

	String timeAttribute;

	boolean cachingIndex;

	String elevationAttribute;
	
	String runtimeAttribute;

	boolean imposedBBox;
	
	boolean heterogeneousGranules;
	
	/**
	 * UTC timezone to serve as reference
	 */
	static final TimeZone UTC_TZ = TimeZone.getTimeZone("UTC");
	
	/**
	 * Constructor.
	 * 
	 * @param source
	 *            The source object.
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 * 
	 */
	public ImageMosaicReader(Object source, Hints uHints) throws IOException {
	    super(source,uHints);
	    if (this.hints.containsKey(Hints.EXECUTOR_SERVICE)) {
	      final Object executor = uHints.get(Hints.EXECUTOR_SERVICE);
	      if (executor != null && executor instanceof ExecutorService){
	          multiThreadedLoader = (ExecutorService) executor;
	          if (LOGGER.isLoggable(Level.FINE)){
	              if (multiThreadedLoader instanceof ThreadPoolExecutor){
	                  final ThreadPoolExecutor tpe = (ThreadPoolExecutor) multiThreadedLoader;
	                  LOGGER.fine("Using ThreadPoolExecutor with the following settings: " +
	                              "core pool size = " + tpe.getCorePoolSize() + 
	                              "\nmax pool size = " + tpe.getMaximumPoolSize() + 
	                              "\nkeep alive time " + tpe.getKeepAliveTime(TimeUnit.MILLISECONDS));    
	              }
	          }
	      }
	    }
		if(this.hints.containsKey(Hints.MAX_ALLOWED_TILES))
			this.maxAllowedTiles= ((Integer)this.hints.get(Hints.MAX_ALLOWED_TILES));		


		// /////////////////////////////////////////////////////////////////////
		//
		// Check source
		//
		// /////////////////////////////////////////////////////////////////////
		if (source instanceof ImageMosaicDescriptor) {
		    initReaderFromDescriptor((ImageMosaicDescriptor) source, uHints);
		} else {
		    initReaderFromURL(source, uHints);
		}
	}
	
	/**
	 * Init this {@link ImageMosaicReader} using the provided {@link ImageMosaicDescriptor} as source. 
	 * @param source
	 * @param uHints
	 * @throws DataSourceException
	 */
    private void initReaderFromDescriptor(final ImageMosaicDescriptor source, final Hints uHints) throws DataSourceException {
        Utilities.ensureNonNull("source", source);
        final MosaicConfigurationBean configuration = source.getConfiguration();
        if (configuration == null) {
            throw new DataSourceException("Unable to create reader for this mosaic since we could not parse the configuration.");
        }
        extractProperties(configuration);
        catalog = source.getCatalog();
        if (catalog == null) {
            throw new DataSourceException("Unable to create reader for this mosaic since the inner catalog is null.");
        }
        setGridGeometry();
        
        rasterManager = new RasterManager(this);
        rasterManager.defaultSM = configuration.getSampleModel();
        
    }

    /**
     * Init this {@link ImageMosaicReader} using the provided object as a source referring to an {@link URL}. 
     * 
     * @param source
     * @param uHints
     * @throws DataSourceException
     */
    private void initReaderFromURL(final Object source, final Hints hints) throws MalformedURLException, DataSourceException {
		this.sourceURL=Utils.checkSource(source,hints);
		if(this.sourceURL==null)
			throw new DataSourceException("This plugin accepts File, URL or String. The string may describe a File or an URL");
		
		//
		// Load properties file with information about levels and envelope
		//
		final MosaicConfigurationBean configuration = loadMosaicProperties();
		if(configuration==null)
			throw new DataSourceException("Unable to create reader for this mosaic since we could not parse the configuration.");
		//location attribute override
		if(this.hints.containsKey(Hints.MOSAIC_LOCATION_ATTRIBUTE))
			this.locationAttributeName=((String)this.hints.get(Hints.MOSAIC_LOCATION_ATTRIBUTE));	
		
		// 
		//
		// Load tiles informations, especially the bounds, which will be
		// reused
		//
		// 
		try {
			// create the index
			catalog= GranuleCatalogFactory.createGranuleCatalog(sourceURL, configuration);
			
			// error
			if(catalog==null)
				throw new DataSourceException("Unable to create index for this URL "+sourceURL);
			
			// everything is fine
			if (LOGGER.isLoggable(Level.FINE))
				LOGGER.fine("Connected mosaic reader to its index "
						+ sourceURL.toString());
			final SimpleFeatureType type= catalog.getType();
			if (type==null)
				throw new IllegalArgumentException("Problems when opening the index, no typenames for the schema are defined");
			
			setGridGeometry(configuration.getEnvelope());

            //
            // get the crs if able to
            //
            final Object tempCRS = this.hints.get(Hints.DEFAULT_COORDINATE_REFERENCE_SYSTEM);
            if (tempCRS != null) {
                this.crs = (CoordinateReferenceSystem) tempCRS;
                LOGGER.log(Level.WARNING, "Using forced coordinate reference system ");
            } else {
                final CoordinateReferenceSystem tempcrs = type.getGeometryDescriptor().getCoordinateReferenceSystem();
                if (tempcrs == null) {
                    // use the default crs
                    crs = AbstractGridFormat.getDefaultCRS();
                    LOGGER.log(Level.WARNING, "Unable to find a CRS for this coverage, using a default one" );
                } else
                    crs = tempcrs;
            }
			
			//
			// location attribute field checks
			//	
			if(this.locationAttributeName==null)
			{
				//get the first string
				for(AttributeDescriptor attribute: type.getAttributeDescriptors()){
					if(attribute.getType().getBinding().equals(String.class))
						this.locationAttributeName=attribute.getName().toString();
				}
			}
			if(type.getDescriptor(this.locationAttributeName)==null)
				throw new DataSourceException("The provided name for the location attribute is invalid.");
			
			//
			// time attribute field checks
			//
			//time attribute override
			if(this.timeAttribute==null)
			{
				//get the first attribute that can be use as date
				for(AttributeDescriptor attribute: type.getAttributeDescriptors()){
					// TODO improve this code
					if(attribute.getType().getBinding().equals(Date.class))
					{
						this.timeAttribute=attribute.getName().toString();
						break;
					}
					if(attribute.getType().getBinding().equals(Timestamp.class))
					{
						this.timeAttribute=attribute.getName().toString();
						break;
					}
					if(attribute.getType().getBinding().equals(java.sql.Date.class))
					{
						this.timeAttribute=attribute.getName().toString();
						break;
					}						
				}
			}
			if(this.timeAttribute!=null&&this.timeAttribute.length()>0&&type.getDescriptor(this.timeAttribute)==null)
				throw new DataSourceException("The provided name for the timeAttribute attribute is invalid.");			
			
			// creating the raster manager
			rasterManager = new RasterManager(this);
		}
		catch (Throwable e) {
			try {
				if(catalog!=null)
					catalog.dispose();
			} catch (Throwable e1) {
				if (LOGGER.isLoggable(Level.FINEST))
					LOGGER.log(Level.FINEST, e1.getLocalizedMessage(), e1);
			}
			finally{
				catalog=null;
			}
			
			// dispose raster manager as well
			try {
				if(rasterManager!=null)
					rasterManager.dispose();
			} catch (Throwable e1) {
				if (LOGGER.isLoggable(Level.FINEST))
					LOGGER.log(Level.FINEST, e1.getLocalizedMessage(), e1);
			}
			finally{
				rasterManager=null;
			}
			
			throw new  DataSourceException(e);
		}

		
	}

	private void setGridGeometry(ReferencedEnvelope envelope) {
		Utilities.ensureNonNull("index", catalog);
	    //
        // save the bbox and prepare other info
        //
        final BoundingBox bounds = catalog.getBounds();
        if(bounds.isEmpty()) {
                throw new IllegalArgumentException("Cannot create a mosaic out of an empty index");
        }
        
        // we might have an imposed bbox
        this.crs=bounds.getCoordinateReferenceSystem();
        if(envelope==null)
        	this.originalEnvelope=new GeneralEnvelope(bounds);
        else{
        	this.originalEnvelope=new GeneralEnvelope(envelope);
        	this.originalEnvelope.setCoordinateReferenceSystem(crs);
        }
        
        // original gridrange (estimated). I am using the floor here in order to make sure
        // we always stays inside the real area that we have for the granule
        originalGridRange = new GridEnvelope2D(
                        new Rectangle(
                                        (int) (originalEnvelope.getSpan(0)/ highestRes[0]), 
                                        (int) (originalEnvelope.getSpan(1)/ highestRes[1])
                                        )
                        );
        raster2Model= new AffineTransform2D(
                highestRes[0], 
                0, 
                0, 
                -highestRes[1], 
                originalEnvelope.getLowerCorner().getOrdinate(0)+0.5*highestRes[0], 
                originalEnvelope.getUpperCorner().getOrdinate(1)-0.5*highestRes[1]);   
		
	}

	private void setGridGeometry () {
	    setGridGeometry(null)                ; 
    }

        /**
	 * Loads the properties file that contains useful information about this
	 * coverage.
	 * 
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	private MosaicConfigurationBean loadMosaicProperties(){
		// discern if we have a shapefile based index or a datastore based index
		final File sourceFile=DataUtilities.urlToFile(sourceURL);
		final String extension= FilenameUtils.getExtension(sourceFile.getAbsolutePath());
		MosaicConfigurationBean configuration=null;
		if(extension.equalsIgnoreCase("shp"))
		{
			// shapefile
			configuration=Utils.loadMosaicProperties(DataUtilities.changeUrlExt(sourceURL, "properties"),this.locationAttributeName);
		}
		else
		{
			// we need to look for properties files that do NOT define a datastore
			final File[] properties = sourceFile.getParentFile().listFiles(
					(FilenameFilter)
					FileFilterUtils.andFileFilter(
							FileFilterUtils.notFileFilter(FileFilterUtils.nameFileFilter("indexer.properties")),
						FileFilterUtils.andFileFilter(
								FileFilterUtils.notFileFilter(FileFilterUtils.nameFileFilter("datastore.properties")),
								FileFilterUtils.makeFileOnly(FileFilterUtils.suffixFileFilter(".properties")
						)
					)
			));
			
			
			// check the valid mosaic properties files
			for(File propFile:properties)
				if(Utils.checkFileReadable(propFile))
				{
					// try to load the config
					configuration=Utils.loadMosaicProperties(DataUtilities.fileToURL(propFile), this.locationAttributeName);
					if(configuration!=null)
						break;
					
					// proceed with next prop file
				}		
							
		}
		// we did not find any good candidate for mosaic.properties file, this will signal it		
		if(configuration!=null)
			return extractProperties(configuration);
		return configuration;
	}

	private MosaicConfigurationBean extractProperties(final MosaicConfigurationBean configuration) {

		// resolutions levels
		numOverviews = configuration.getLevelsNum() - 1;
		final double[][] resolutions = configuration.getLevels();
		overViewResolutions = numOverviews >= 1 ? new double[numOverviews][2]: null;
		highestRes = new double[2];
		highestRes[0] = resolutions[0][0];
		highestRes[1] = resolutions[0][1];

		if (LOGGER.isLoggable(Level.FINE))
			LOGGER.fine(new StringBuilder("Highest res ").append(highestRes[0])
					.append(" ").append(highestRes[1]).toString());

		if (numOverviews > 0){
	   		for (int i = 0; i < numOverviews; i++) {     			
				overViewResolutions[i][0] = resolutions[i+1][0];
				overViewResolutions[i][1] = resolutions[i+1][1];
	   		}	
		}
	
		// name
		coverageName = configuration.getName();

		// need a color expansion?
		// this is a newly added property we have to be ready to the case where
		// we do not find it.
		expandMe = configuration.isExpandToRGB();
		
		heterogeneousGranules = configuration.isHeterogeneous();


		// absolute or relative path
		pathType = configuration.isAbsolutePath()?PathType.ABSOLUTE:PathType.RELATIVE;
		
		//
		// location attribute
		//
		locationAttributeName = configuration.getLocationAttribute();
		
		// suggested SPI
		final String suggestedSPIClass = configuration.getSuggestedSPI();
		if (suggestedSPIClass != null){
			try {
				final Class<?> clazz=Class.forName(suggestedSPIClass);
				if(clazz.newInstance() instanceof ImageReaderSpi)
					suggestedSPI=(ImageReaderSpi)clazz.newInstance();
				else
					suggestedSPI=null;
			} catch (ClassNotFoundException e) {
				if(LOGGER.isLoggable(Level.FINE))
					LOGGER.log(Level.FINE,e.getLocalizedMessage(),e);
				suggestedSPI=null;
			} catch (InstantiationException e) {
				if(LOGGER.isLoggable(Level.FINE))
					LOGGER.log(Level.FINE,e.getLocalizedMessage(),e);
				suggestedSPI=null;
			} catch (IllegalAccessException e) {
				if(LOGGER.isLoggable(Level.FINE))
					LOGGER.log(Level.FINE,e.getLocalizedMessage(),e);
				suggestedSPI=null;
			}
		}
		
		// time param
		final String timeAttribute = configuration.getTimeAttribute();
		if(timeAttribute != null)
			this.timeAttribute = timeAttribute;
		
		
		// elevation param
		final String elevationAttribute = configuration.getElevationAttribute();
		if(elevationAttribute != null)
			this.elevationAttribute = elevationAttribute;		

		// runtime param
		final String runtimeAttribute = configuration.getRuntimeAttribute();
		if(runtimeAttribute != null)
			this.runtimeAttribute = runtimeAttribute;		


		// caching for the index
		cachingIndex = configuration.isCaching();
		
		// imposed BBOX?
		this.imposedBBox=true;
		
		return configuration;
	}

	/**
	 * Constructor.
	 * 
	 * @param source
	 *            The source object.
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 * 
	 */
	public ImageMosaicReader(Object source) throws IOException {
		this(source, null);

	}

	/**
	 * 
	 * @see org.opengis.coverage.grid.GridCoverageReader#getFormat()
	 */
	public Format getFormat() {
		return new ImageMosaicFormat();
	}

	/**
	 * 
	 * @see org.opengis.coverage.grid.GridCoverageReader#read(org.opengis.parameter.GeneralParameterValue[])
	 */
	public GridCoverage2D read(GeneralParameterValue[] params) throws IOException {
	   
	    // check if we were disposed already
	    if(rasterManager==null){
	        throw new IOException("Looks like this reader has been already disposed or it has not been properly initialized.");
	    }
		if (LOGGER.isLoggable(Level.FINE)) {
    		    if (sourceURL != null) {
    			LOGGER.fine("Reading mosaic from " + sourceURL.toString());
    		    } else {
    		        LOGGER.fine("Reading mosaic");
    		    }
		    LOGGER.fine("Highest res "+highestRes[0]+" "+highestRes[1]);
		}
		//
		// add max allowed tiles if missing
		//
		if(this.maxAllowedTiles!=Integer.MAX_VALUE){
			if(params!=null){
				// first thing let's see if we have it already, in which case we do nothing since a read parameter override a Hint
				boolean found=false;
				for(GeneralParameterValue pv:params)
				{
					if(pv.getDescriptor().getName().equals(ImageMosaicFormat.MAX_ALLOWED_TILES.getName()))
					{
						found=true;
						break;
					}
				}
				
				//ok, we did not find it, let's add it back 
				if(!found)
				{
					final GeneralParameterValue[] temp = new GeneralParameterValue[params.length+1];
					System.arraycopy(params, 0, temp, 0, params.length);
					ParameterValue<Integer> tempVal = ImageMosaicFormat.MAX_ALLOWED_TILES.createValue();
					tempVal.setValue(this.maxAllowedTiles);
					temp[params.length]=tempVal;
				}
			}
			else
			{
				// we do not have nay read params, we have to create the array for them
				ParameterValue<Integer> tempVal = ImageMosaicFormat.MAX_ALLOWED_TILES.createValue();
				tempVal.setValue(this.maxAllowedTiles);
				params= new GeneralParameterValue[]{tempVal};
			}
				
		}
		
		// /////////////////////////////////////////////////////////////////////
		//
		// Loading tiles trying to optimize as much as possible
		//
		// /////////////////////////////////////////////////////////////////////
		
		final Collection<GridCoverage2D> response = rasterManager.read(params);
		if (response.isEmpty()) {
		    if (LOGGER.isLoggable(Level.FINE)){
		        LOGGER.fine("The response is empty. ==> returning a null GridCoverage");
		    }
		    return null;
		} else {
			return response.iterator().next();
		}
	}

	/**
	 * Package private accessor for {@link Hints}.
	 * 
	 * @return this {@link Hints} used by this reader.
	 */
	Hints getHints(){
		return super.hints;
	}
	
	/**
	 * Package private accessor for the highest resolution values.
	 * 
	 * @return the highest resolution values.
	 */
	double[] getHighestRes(){
		return super.highestRes;
	}
	
	/**
	 * 
	 * @return
	 */
	double[][] getOverviewsResolution(){
		return super.overViewResolutions;
	}
	
	int getNumberOfOvervies(){
		return super.numOverviews;
	}
	

    /** Package scope grid to world transformation accessor */
    MathTransform getRaster2Model() {
        return raster2Model;
    }
    
    /**
     * Let us retrieve the {@link GridCoverageFactory} that we want to use.
     * 
     * @return
     * 			retrieves the {@link GridCoverageFactory} that we want to use.
     */
    GridCoverageFactory getGridCoverageFactory(){
    	return coverageFactory;
    }

	String getName() {
		return super.coverageName;
	}

	public Object getDestination() {
		return null;
	}

	public void setCurrentSubname(String arg0) throws IOException {
		throw new UnsupportedOperationException("Unsupported method");
		
	}

	public void setMetadataValue(String arg0, String arg1) throws IOException {
		throw new UnsupportedOperationException("Unsupported method");
		
	}

	public void write(GridCoverage arg0, GeneralParameterValue[] arg1)throws IllegalArgumentException, IOException {
		throw new UnsupportedOperationException("Unsupported method");
		
	}
	
	
	/**
	 * Number of coverages for this reader is 1
	 * 
	 * @return the number of coverages for this reader.
	 */
	@Override
	public int getGridCoverageCount() {
		return 1;
	}

	/**
	 * Releases resources held by this reader.
	 * 
	 */
	@Override
	public synchronized void dispose() {
		super.dispose();
		try{
		    if(rasterManager!=null)
		        rasterManager.dispose();
		} catch (Exception e) {
                    if(LOGGER.isLoggable(Level.FINE))
                        LOGGER.log(Level.FINE,e.getLocalizedMessage(),e);
                } finally {
                    rasterManager=null;
                }
	}

	@Override
	public String[] getMetadataNames() {
	    final String []parentNames = super.getMetadataNames();
            final List<String> metadataNames = new ArrayList<String>();
            metadataNames.add("TIME_DOMAIN");
            metadataNames.add("HAS_TIME_DOMAIN");
            metadataNames.add("TIME_DOMAIN_MINIMUM");
            metadataNames.add("TIME_DOMAIN_MAXIMUM");
            metadataNames.add("TIME_DOMAIN_RESOLUTION");
            metadataNames.add("ELEVATION_DOMAIN");
            metadataNames.add("ELEVATION_DOMAIN_MINIMUM");
            metadataNames.add("ELEVATION_DOMAIN_MAXIMUM");
            metadataNames.add("HAS_ELEVATION_DOMAIN");
            metadataNames.add("ELEVATION_DOMAIN_RESOLUTION");
            if(parentNames!=null)
                metadataNames.addAll(Arrays.asList(parentNames));
            return metadataNames.toArray(new String[metadataNames.size()]);
	}

	@Override
	public String getMetadataValue(final String name) {
	    final String superValue=super.getMetadataValue(name);
	    if(superValue!=null)
	        return superValue;
	    
            if (name.equalsIgnoreCase("HAS_ELEVATION_DOMAIN"))
                return String.valueOf(elevationAttribute != null);
    
            if (name.equalsIgnoreCase("HAS_TIME_DOMAIN"))
                return String.valueOf(timeAttribute != null);
    
            if (name.equalsIgnoreCase("TIME_DOMAIN_RESOLUTION"))
                return null;
    
            final boolean getTimeDomain = (timeAttribute != null && name.equalsIgnoreCase("time_domain"));
            if (getTimeDomain) {
                return extractTimeDomain();
    
            }
    
            final boolean getTimeExtrema = timeAttribute != null
                    && (name.equalsIgnoreCase("time_domain_minimum") || name.equalsIgnoreCase("time_domain_maximum"));
            if (getTimeExtrema) {
                return extractTimeExtrema(name);
    
            }
    
            final boolean getElevationAttribute = (elevationAttribute != null && name.equalsIgnoreCase("elevation_domain"));
            if (getElevationAttribute) {
                return extractElevationDomain();
    
            }
    
            final boolean getElevationExtrema = elevationAttribute != null
                    && (name.equalsIgnoreCase("elevation_domain_minimum") || name.equalsIgnoreCase("elevation_domain_maximum"));
            if (getElevationExtrema) {
                return extractElevationExtrema(name);
    
            }
        		
//		final boolean getRuntimeAttribute=name.equalsIgnoreCase("runtime_domain");
//		if(getRuntimeAttribute){
//			Query query;
//			try {
//				query = new DefaultQuery(rasterManager.granuleCatalog.getType().getTypeName());
//				query.setPropertyNames(Arrays.asList("runtime"));
//				final SortBy[] sortBy=new SortBy[]{
//						new SortByImpl(
//								FeatureUtilities.DEFAULT_FILTER_FACTORY.property("runtime"),
//								SortOrder.DESCENDING
//						)};
//				if(queryCapabilities.supportsSorting(sortBy))
//					query.setSortBy(sortBy);
////				else
////					manualSort=true;				
//				final UniqueVisitor visitor= new UniqueVisitor("runtime");
//				rasterManager.granuleCatalog.computeAggregateFunction(query, visitor);
//				
//				// check result
//				final Set<Integer> result = new TreeSet<Integer>(new Comparator<Integer>() {
//
//					public int compare(Integer o1, Integer o2) {
//						// Revert Order
//						if (o1 > 02)
//							return -1;
//						else if (o1 < o2)
//							return 1;
//						return 0;
//					}
//				});
//				result.addAll(visitor.getUnique());
//				if(result.size()<=0)
//					return null;
//				final StringBuilder buff= new StringBuilder();
//				for(Iterator<Integer> it=result.iterator();it.hasNext();){
//					final int value= it.next();
//					buff.append(value);
//					if(it.hasNext())
//						buff.append(",");
//				}
//				return buff.toString();
//			} catch (IOException e) {
//				if(LOGGER.isLoggable(Level.WARNING))
//					LOGGER.log(Level.WARNING,"Unable to parse attribute:"+name,e);
//			}
//			
//		}
//		
		return super.getMetadataValue(name);
	}


    /**
     * Extract the time domain extrema.
     * 
     * @param metadataName a {@link String} either TIME_DOMAIN_MAXIMUM or TIME_DOMAIN_MINIMUM.
     * 
     * @return either TIME_DOMAIN_MAXIMUM or TIME_DOMAIN_MINIMUM as a {@link String}.
     */
    private String extractTimeExtrema(String metadataName) {
        if(timeAttribute==null){
            if(LOGGER.isLoggable(Level.INFO))
                LOGGER.info("Requesting extrema on attribute "+metadataName+" when no such an attribute is supported!");
            return null;
        }
        try {
            final FeatureCalc visitor = createExtremaQuery(metadataName,rasterManager.timeAttribute);
            
            // check result
            final Date result=(Date) visitor.getResult().getValue();
            final SimpleDateFormat df= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            df.setTimeZone(UTC_TZ);
            return df.format(result)+"Z";//ZULU
        } catch (IOException e) {
            if(LOGGER.isLoggable(Level.WARNING))
                    LOGGER.log(Level.WARNING,"Unable to compute extrema for TIME_DOMAIN",e);
            return null;
        }
    }

    /**
     * @param metadataName
     * @param attributeName 
     * @return
     * @throws IOException
     */
    private FeatureCalc createExtremaQuery(String metadataName, String attributeName) throws IOException {
        final Query query = new Query(rasterManager.granuleCatalog.getType().getTypeName());
        query.setPropertyNames(Arrays.asList(attributeName));
                              
        final FeatureCalc visitor= 
            metadataName.toLowerCase().endsWith("maximum")?
                new MaxVisitor(attributeName):new MinVisitor(attributeName);
        rasterManager.granuleCatalog.computeAggregateFunction(query, visitor);
        return visitor;
    }
    
    /**
     * Extract the elevation domain extrema.
     * 
     * @param metadataName a {@link String} either ELEVATION_DOMAIN_MAXIMUM or ELEVATION_DOMAIN_MINIMUM.
     * 
     * @return either ELEVATION_DOMAIN_MAXIMUM or ELEVATION_DOMAIN_MINIMUM as a {@link String}.
     */
    private String extractElevationExtrema(String metadataName) {
        if(elevationAttribute==null){
            if(LOGGER.isLoggable(Level.INFO))
                LOGGER.info("Requesting extrema on attribute "+metadataName+" when no such an attribute is supported!");
            return null;
        }
        try {
            final FeatureCalc visitor = createExtremaQuery(metadataName,rasterManager.elevationAttribute);
            
            // check result
            final Double result=(Double) visitor.getResult().getValue();
            return Double.toString(result);
        } catch (IOException e) {
            if(LOGGER.isLoggable(Level.WARNING))
                    LOGGER.log(Level.WARNING,"Unable to compute extrema for ELEVATION_DOMAIN",e);
            return null;
        }
    }

    /**
     * Extract the elevation domain as a comma separated list of string values.
     * @return a {@link String} that contains a comma separated list of values.
     */
    private String extractElevationDomain() {
        if(elevationAttribute==null){
            if(LOGGER.isLoggable(Level.INFO))
                LOGGER.info("Requesting domain on attribute elevation when no such an attribute is supported!");
            return null;
        }
        try {
            final Set<Double> result = extractDomain(elevationAttribute);          
            // check result
            if(result.size()<=0)
                    return null;
            
            final StringBuilder buff= new StringBuilder();
            for(Iterator<Double> it=result.iterator();it.hasNext();){
                    final double value= (Double) it.next();
                    buff.append(value);
                    if(it.hasNext())
                    	buff.append(",");
            }
            return buff.toString();
        } catch (IOException e) {
            if(LOGGER.isLoggable(Level.WARNING))
                    LOGGER.log(Level.WARNING,"Unable to parse attribute: ELEVATION_DOMAIN",e);
            return null;
        }
    }

    /**
     * Extract the domain of a dimension as a set of unique values.
     * 
     * <p>
     * It retrieves a comma separated list of values as a {@link String}.
     * 
     * @return a comma separated list of values as a {@link String}.
     * @throws IOException
     */
    private Set extractDomain(final String attribute)
            throws IOException {

        final QueryCapabilities queryCapabilities = rasterManager.granuleCatalog.getQueryCapabilities();
        boolean manualSort=false;        
        Query query = new Query(rasterManager.granuleCatalog.getType().getTypeName());
        query.setPropertyNames(Arrays.asList(attribute));
        final SortBy[] sortBy=new SortBy[]{
                	new SortByImpl(
                			FeatureUtilities.DEFAULT_FILTER_FACTORY.property(attribute),
                			SortOrder.ASCENDING
                	)};
        if(queryCapabilities.supportsSorting(sortBy))
                query.setSortBy(sortBy);
        else
                manualSort=true;				
        final UniqueVisitor visitor= new UniqueVisitor(attribute);
        rasterManager.granuleCatalog.computeAggregateFunction(query, visitor);
        
        // check result
        final Set result = manualSort?
                new TreeSet(visitor.getUnique()):
                visitor.getUnique();
        if(result.size()<=0)
                return null;
        return result;
    }

    /**
     * Extract the elevation domain as a comma separated list of string values.
     * @return a {@link String} that contains a comma separated list of values.
     */
    private String extractTimeDomain() {
        if(timeAttribute==null){
            if(LOGGER.isLoggable(Level.INFO))
                LOGGER.info("Requesting domain on attribute time when no such an attribute is supported!");
            return null;
        }
        try {
            final Collection<Date>result =extractDomain(timeAttribute);
            
            // check result
            if(result.size()<=0)
                    return null;	
                    
            final StringBuilder buff= new StringBuilder();
            final SimpleDateFormat df= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            df.setTimeZone(UTC_TZ);
            for(Date date:result){
                    buff.append(df.format(date)).append("Z");//ZULU
                    buff.append(",");
            }
            return buff.substring(0,buff.length()-1).toString();
        } catch (IOException e) {
            if(LOGGER.isLoggable(Level.WARNING))
                    LOGGER.log(Level.WARNING,"Unable to parse attribute:TIME_DOMAIN",e);
            return null;
        }
    }
}
