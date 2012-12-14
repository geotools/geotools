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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.spi.ImageReaderSpi;

import org.apache.commons.io.filefilter.FileFilterUtils;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.data.DataSourceException;
import org.geotools.data.DataUtilities;
import org.geotools.data.Query;
import org.geotools.factory.Hints;
import org.geotools.feature.visitor.FeatureCalc;
import org.geotools.feature.visitor.MaxVisitor;
import org.geotools.feature.visitor.MinVisitor;
import org.geotools.feature.visitor.UniqueVisitor;
import org.geotools.gce.imagemosaic.catalog.GranuleCatalog;
import org.geotools.gce.imagemosaic.catalog.GranuleCatalogFactory;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.parameter.DefaultParameterDescriptor;
import org.geotools.referencing.operation.transform.AffineTransform2D;
import org.geotools.resources.coverage.FeatureUtilities;
import org.geotools.util.Utilities;
import org.opengis.coverage.grid.Format;
import org.opengis.coverage.grid.GridCoverageReader;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.geometry.BoundingBox;
import org.opengis.metadata.Identifier;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterDescriptor;
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
 *
 *
 * @source $URL$
 */
@SuppressWarnings("rawtypes")
public class ImageMosaicReader extends AbstractGridCoverage2DReader implements GridCoverageReader {


    /**
     * An {@link AdditionalDomainManager} class which allows to deal with additional domains
     * (if any) defined inside the mosaic. It provides DOMAIN_ALIAS <--to--> original attribute mapping
     * capabilities, metadata retrieval, filter creation, and domain support check
     * 
     * @author Daniele Romagnoli, GeoSolutions S.a.S.
     *
     */
    class AdditionalDomainManager {

        private static final String DOMAIN_SUFFIX = "_DOMAIN";

        private static final String HAS_PREFIX = "HAS_";

        // Consider using arrays for dimensions checks
        // Set of supported additional domains
        private Set<String> additionalDomains;

        // Set of additional domains availability (is it really needed?)
        private Set<String> hasAdditionalDomains;

        // Mapping between domain (usually, an UPPER CASE name) and related original attribute
        private Map<String, String> domainToOriginalAttribute;

        // comma separated String of additional domain attributes
        private String additionalDomainAttributes;

        // Set of supported dynamic parameters (depending on the available domains) 
        private Set<ParameterDescriptor<List>> dynamicParameters = null;

        // Quick access set to look for supported parameters by ID
        private Set<Identifier> supportedParameters = null;

        /**
         * build an AdditionalDomainManager on top of the provided additionalDomainAttributes 
         * (a comma separated list of attribute names).
         *
         * @param additionalDomainAttributes
         */
        AdditionalDomainManager (String additionalDomainAttributes) {
            this.additionalDomainAttributes = additionalDomainAttributes;
            final String[] additionalDomainsNames = additionalDomainAttributes.split(",");
            final int numDomains = additionalDomainsNames.length;
            if (numDomains <= 0)
                throw new IllegalArgumentException("Number of Domains should be > 0");
            dynamicParameters = new HashSet<ParameterDescriptor<List>>(numDomains);
            supportedParameters = new HashSet<Identifier>(numDomains);
            additionalDomains = new HashSet<String>(numDomains);
            hasAdditionalDomains = new HashSet<String>(numDomains);
            domainToOriginalAttribute = new HashMap<String,String>(numDomains);
            for (String domain : additionalDomainsNames) {
                addDomain(domain);
            }
        }

        /**
         * Clean up mappings
         */
        public void dispose() {
            if (domainToOriginalAttribute != null) {
                domainToOriginalAttribute.clear();
                domainToOriginalAttribute = null;
            }
            if (additionalDomains != null) {
                additionalDomains.clear();
                additionalDomains = null;
            }
            if (hasAdditionalDomains != null) {
                hasAdditionalDomains.clear();
                hasAdditionalDomains = null;
            }
            if (supportedParameters != null) {
                supportedParameters.clear();
                supportedParameters = null;
            }
            if (dynamicParameters != null) {
                dynamicParameters.clear();
                dynamicParameters = null;
            }
        }

        /**
         * Add a domain to the manager
         * @param domain the name of the domain
         */
        public void addDomain(final String domain) {
            final String domainMetadata = domain.toUpperCase() + DOMAIN_SUFFIX;
            additionalDomains.add(domainMetadata);
            hasAdditionalDomains.add(HAS_PREFIX + domainMetadata);
            domainToOriginalAttribute.put(domainMetadata, domain);

            // currently supporting only List of strings
            final DefaultParameterDescriptor<List> parameter = DefaultParameterDescriptor.create(domain, "Additional " + domain + " domain", List.class, null, false);
            dynamicParameters.add(parameter);
            supportedParameters.add(parameter.getName());
        }

        /**
         * Check whether a specific domain is supported by this manager.
         * It can be useful to check wheter a domain specified in the request is supported by this reader.
         * @param domain
         * @return 
         */
        public boolean isDomainSupported(final String domain) {
            final String domainValue = domain.toUpperCase() + DOMAIN_SUFFIX;
            return !additionalDomains.isEmpty() && additionalDomains.contains(domainValue);
        }

        /**
         * Check whether a specific parameter (identified by the {@link Identifier} name) is supported by 
         * this manager (and therefore, by the reader).
         * @param name
         * @return
         */
        public boolean isParameterSupported(final Identifier name) {
            return supportedParameters.contains(name);
        }

        /**
         * Setup the List of metadataNames for this additional domains manager
         *
         * @return
         */
        public List<String> getMetadataNames() {
            final List<String> metadataNames = new ArrayList<String>();
            if (additionalDomains != null && !additionalDomains.isEmpty()) {
                for (String domain: additionalDomains) {
                    metadataNames.add(domain);
                }
            }
            if (hasAdditionalDomains!= null && !hasAdditionalDomains.isEmpty()) {
                for (String hasDomain: hasAdditionalDomains) {
                    metadataNames.add(hasDomain);
                }
            }
            return metadataNames;
        }

        /**
         * Return the value of a specific metadata by parsing the requested name as a Domain Name
         * @param name
         * @return
         */
        public String getMetadataValue(String name) {
            String value = null;
            if (additionalDomainAttributes != null) {
                String domainName = name.toUpperCase();
                if (additionalDomains.contains(domainName)) {
                    value = extractAdditionalDomain(domainToOriginalAttribute.get(domainName));
                }

                if (hasAdditionalDomains.contains(domainName)) {
                    String extractDomain = domainName.length() > 4 ? domainName.substring(HAS_PREFIX.length()) : "";
                    value = String.valueOf(additionalDomains.contains(extractDomain));
                }
            }
            return value;
        }

        /**
         * Setup a Filter on top of the specified domainRequest which is in the form "key=value"
         * @param domainRequest
         * @return
         */
        public Filter createFilter(String domainRequest) {
            if (domainRequest == null || domainRequest.isEmpty())
                throw new IllegalArgumentException("Null domain requested");

            String[] keyValuePair = domainRequest.split("=");
            if (keyValuePair.length != 2) {
                throw new IllegalArgumentException("requested domains should be in the form \"name=value\"");
            }

            // Update this code if we move to support multiple values for the same domain (we can additionally
            // split the domainValue on ",")
            String domainName = keyValuePair[0];
            String domainValue = keyValuePair[1];

            if (!isDomainSupported(domainName)) {
                throw new IllegalArgumentException("requested domain is not supported by this mosaic: " + domainName);
            }
            return  FeatureUtilities.DEFAULT_FILTER_FACTORY.equal(
                    FeatureUtilities.DEFAULT_FILTER_FACTORY.property(domainName),
                    FeatureUtilities.DEFAULT_FILTER_FACTORY.literal(domainValue),true);
        }

        /** 
         * Return the set of dynamic parameterDescriptors (the ones related to domains) for this reader 
         * @return
         */
        public Set<ParameterDescriptor<List>> getDynamicParameters() {
            return dynamicParameters;
        }

    }

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

        /** The inner {@link AdditionalDomainManager} instance which allows to manage custom dimensions */
        AdditionalDomainManager additionalDomainManager;

	int maxAllowedTiles=ImageMosaicFormat.MAX_ALLOWED_TILES.getDefaultValue();

	/** The suggested SPI to avoid SPI lookup*/
	ImageReaderSpi suggestedSPI;

	GranuleCatalog catalog;

	String timeAttribute;

	boolean cachingIndex;

	String elevationAttribute;

	boolean imposedBBox;
	
	boolean heterogeneousGranules;

	String typeName;
	
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
	    
	    //
	    // try to extract a multithreaded loader if available
	    //
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

		//
		// Check source
		//
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
		MosaicConfigurationBean configuration = Utils.loadMosaicProperties(sourceURL,this.locationAttributeName);
		if(configuration==null){
			//
			// do we have a datastore properties file? It will preempt on the shapefile
			//
        	final File parent=DataUtilities.urlToFile(sourceURL).getParentFile();
			
			// this can be used to look for properties files that do NOT define a datastore
			final File[] properties = parent.listFiles(
					(FilenameFilter)
					FileFilterUtils.and(
							FileFilterUtils.notFileFilter(FileFilterUtils.nameFileFilter("indexer.properties")),
						FileFilterUtils.and(
								FileFilterUtils.notFileFilter(FileFilterUtils.nameFileFilter("datastore.properties")),
								FileFilterUtils.makeFileOnly(FileFilterUtils.suffixFileFilter(".properties")
						)
					)
			));
			
			// do we have a valid datastore + mosaic properties pair?
			for(File propFile:properties)
				if(Utils.checkFileReadable(propFile)&&
						Utils.loadMosaicProperties(DataUtilities.fileToURL(propFile), "")!=null){
					configuration = Utils.loadMosaicProperties(DataUtilities.fileToURL(propFile),this.locationAttributeName);
				}               	
			
		}
		if(configuration==null)
			throw new DataSourceException("Unable to create reader for this mosaic since we could not parse the configuration.");
		
		// now load the configuration and extract properties from there
		extractProperties(configuration);
		
		//location attribute override
		if(this.hints.containsKey(Hints.MOSAIC_LOCATION_ATTRIBUTE))
			this.locationAttributeName=((String)this.hints.get(Hints.MOSAIC_LOCATION_ATTRIBUTE));	
		
		//
		// Load tiles informations, especially the bounds, which will be
		// reused
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
			// perform checks on location attribute name
			//
			if(this.locationAttributeName==null) {
			    throw new DataSourceException("The provided name for the location attribute is invalid.");
			} else {
			    if(type.getDescriptor(this.locationAttributeName)==null){
			        // ORACLE fix
			        this.locationAttributeName=this.locationAttributeName.toUpperCase();
			        
			        // try again with uppercase
			        if(type.getDescriptor(this.locationAttributeName)==null){
			            throw new DataSourceException("The provided name for the location attribute is invalid.");
			        }
			    }
			}
			
			//
			// time attribute field checks
			//
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
			
			try {
                            // dispose the additional domains manager
                            if(additionalDomainManager != null)
                                additionalDomainManager.dispose();
                    } catch (Throwable e1) {
                            if (LOGGER.isLoggable(Level.FINEST))
                                    LOGGER.log(Level.FINEST, e1.getLocalizedMessage(), e1);
                    }
                    finally{
                        additionalDomainManager = null;
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
	    setGridGeometry(null); 
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
		
		// do we have heterogenous granules
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
		
		final String additionalDomainAttribute = configuration.getAdditionalDomainAttributes();
		if (additionalDomainAttribute != null) {
		    additionalDomainManager = new AdditionalDomainManager(additionalDomainAttribute);
		}


		// caching for the index
		cachingIndex = configuration.isCaching();
		
		// imposed BBOX
        if(configuration.getEnvelope()!=null){
    		this.imposedBBox=true;
    		// we set the BBOX later to retain also the CRS
        } else {
        	this.imposedBBox=false;
        }
		
		// typeName to be used for reading the mosaic
		this.typeName=configuration.getTypeName();
		
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
		if(this.maxAllowedTiles!=ImageMosaicFormat.MAX_ALLOWED_TILES.getDefaultValue()){
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
		
		//
		// Loading tiles trying to optimize as much as possible
		//
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
            metadataNames.add(TIME_DOMAIN);
            metadataNames.add(HAS_TIME_DOMAIN);
            metadataNames.add(TIME_DOMAIN_MINIMUM);
            metadataNames.add(TIME_DOMAIN_MAXIMUM);
            metadataNames.add(TIME_DOMAIN_RESOLUTION);
            metadataNames.add(ELEVATION_DOMAIN);
            metadataNames.add(ELEVATION_DOMAIN_MINIMUM);
            metadataNames.add(ELEVATION_DOMAIN_MAXIMUM);
            metadataNames.add(HAS_ELEVATION_DOMAIN);
            metadataNames.add(ELEVATION_DOMAIN_RESOLUTION);
            if (additionalDomainManager != null) {
                metadataNames.addAll(additionalDomainManager.getMetadataNames());
            }

            if(parentNames!=null)
                metadataNames.addAll(Arrays.asList(parentNames));
            return metadataNames.toArray(new String[metadataNames.size()]);
	}

	@Override
	public String getMetadataValue(final String name) {
	    final String superValue=super.getMetadataValue(name);
	    if(superValue!=null)
	        return superValue;
	    
            if (name.equalsIgnoreCase(HAS_ELEVATION_DOMAIN))
                return String.valueOf(elevationAttribute != null);
    
            if (name.equalsIgnoreCase(HAS_TIME_DOMAIN))
                return String.valueOf(timeAttribute != null);
    
            if (name.equalsIgnoreCase(TIME_DOMAIN_RESOLUTION))
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

            if (additionalDomainManager != null) {
                String value = additionalDomainManager.getMetadataValue(name);
                if (value != null) {
                    return value;
                }
            }
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
            final SimpleDateFormat df= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            df.setTimeZone(Utils.UTC_TIME_ZONE);
            return df.format(result);//ZULU
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
            final Object result = visitor.getResult().getValue();
            if(result instanceof Number) {
                return result.toString();
            } else {
                return null;
            }
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
            final Set<Number> result = extractDomain(elevationAttribute);          
            // check result
            if(result.size()<=0)
                    return "";
            
            final StringBuilder buff= new StringBuilder();
            for(Iterator<Number> it= result.iterator(); it.hasNext();){
            	buff.append(((Number) it.next()).toString());
                if(it.hasNext())
                   buff.append(",");
            }
            return buff.toString();
        } catch (IOException e) {
            if(LOGGER.isLoggable(Level.WARNING))
                    LOGGER.log(Level.WARNING,"Unable to parse attribute: ELEVATION_DOMAIN",e);
            return "";
        }
    }

    /**
     * Extract the elevation domain as a comma separated list of string values.
     * @return a {@link String} that contains a comma separated list of values.
     */
    private String extractAdditionalDomain(String domain) {
        try {
            final Set<String> result = extractDomain(domain);          
            // check result
            if(result.size()<=0)
                    return "";
            
            final StringBuilder buff= new StringBuilder();
            for(Iterator<String> it= result.iterator(); it.hasNext();){
                buff.append(((String) it.next()));
                if(it.hasNext())
                   buff.append(",");
            }
            return buff.toString();
        } catch (IOException e) {
            if(LOGGER.isLoggable(Level.WARNING))
                    LOGGER.log(Level.WARNING,"Unable to parse attribute: " + domain ,e);
            return "";
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

//        final QueryCapabilities queryCapabilities = rasterManager.granuleCatalog.getQueryCapabilities();
//        boolean manualSort=false;        
        Query query = new Query(rasterManager.granuleCatalog.getType().getTypeName());
        query.setPropertyNames(Arrays.asList(attribute));
//        final SortBy[] sortBy=new SortBy[]{
//                	new SortByImpl(
//                			FeatureUtilities.DEFAULT_FILTER_FACTORY.property(attribute),
//                			SortOrder.ASCENDING
//                	)};
//        if(queryCapabilities.supportsSorting(sortBy))
//                query.setSortBy(sortBy);
//        else
//                manualSort=true;	
        final UniqueVisitor visitor= new UniqueVisitor(attribute);
        rasterManager.granuleCatalog.computeAggregateFunction(query, visitor);
        
        // check result
//        final Set result =manualSort? new TreeSet(visitor.getUnique()):visitor.getUnique();
        // 17052012 SG MANUAL ORDERING as there is not guarantee that the ordering with an aggregation would work
        return new TreeSet(visitor.getUnique());
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
                    return "";	
                    
            final StringBuilder buff= new StringBuilder();
            final SimpleDateFormat df= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            df.setTimeZone(Utils.UTC_TIME_ZONE); // we DO work only with UTC times
            for(Date date:result){
                    buff.append(df.format(date));//ZULU
                    buff.append(",");
            }
            return buff.substring(0,buff.length()-1).toString();
        } catch (IOException e) {
            if(LOGGER.isLoggable(Level.WARNING))
                    LOGGER.log(Level.WARNING,"Unable to parse attribute:TIME_DOMAIN",e);
            return "";
        }
    }

    @Override
    public Set<ParameterDescriptor<List>> getDynamicParameters() {
        return additionalDomainManager != null ? additionalDomainManager.getDynamicParameters() : super.getDynamicParameters();
    }

    public boolean isParameterSupported(Identifier name) {
        return additionalDomainManager != null ? additionalDomainManager.isParameterSupported(name) : false;
    }
}
