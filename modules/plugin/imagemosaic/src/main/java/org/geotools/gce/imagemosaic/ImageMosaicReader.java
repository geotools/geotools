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
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
import org.geotools.factory.Hints;
import org.geotools.feature.visitor.CalcResult;
import org.geotools.feature.visitor.FeatureCalc;
import org.geotools.gce.imagemosaic.catalog.GranuleCatalog;
import org.geotools.gce.imagemosaic.catalog.GranuleCatalogFactory;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.parameter.DefaultParameterDescriptor;
import org.geotools.referencing.operation.transform.AffineTransform2D;
import org.geotools.resources.coverage.FeatureUtilities;
import org.geotools.util.Range;
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
import org.opengis.referencing.ReferenceIdentifier;
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
     * {@link DomainDescriptor} describe a single domain in terms of name and {@link ParameterDescriptor} 
     * that can be used to filter values during a read operation.
     * 
     * <p> Notice that there is no caching of values for the domain itself right now.
     * 
     * <p>
     * The domain must have unique identifiers.
     * 
     * @author Simone Giannecchini, GeoSolutions SAS
     *
     */
    class DomainDescriptor{

        static final String DOMAIN_SUFFIX = "_DOMAIN";

        static final String HAS_PREFIX = "HAS_";
        
        
        /** Unique identifier for this domain.*/
        private final String identifier;
                
        /** propertyName for this domain that tells me which Property from the underlying catalog provides values for it.*/
        private final String propertyName;
        
        /** The {@link ParameterDescriptor} that can be used to filter on this domain during a read operation.*/
        private final DefaultParameterDescriptor<List> domainParameterDescriptor;
        

        /**
         * @return the identifier
         */
        private String getIdentifier() {
            return identifier;
        }

        /**
         * @return the propertyName
         */
        private String getPropertyName() {
            return propertyName;
        }

        /**
         * @return the domainaParameterDescriptor
         */
        private DefaultParameterDescriptor<List> getDomainaParameterDescriptor() {
            return domainParameterDescriptor;
        }

        private DomainDescriptor(String identifier, String propertyName) {
            this.identifier = identifier;
            this.propertyName = propertyName;
            final String name = identifier.toUpperCase();
            this.domainParameterDescriptor=
                    DefaultParameterDescriptor.create(
                            name, 
                            "Additional " + identifier + " domain", 
                            List.class, 
                            null, 
                            false); 
        }
        /**
         * Extract the time domain extrema.
         * 
         * @param extrema a {@link String} either TIME_DOMAIN_MAXIMUM or TIME_DOMAIN_MINIMUM.
         * 
         * @return either TIME_DOMAIN_MAXIMUM or TIME_DOMAIN_MINIMUM as a {@link String}.
         * TODO use num for extrema
         */
        private String getExtrema(String extrema) {
            try {
                final FeatureCalc visitor = rasterManager.createExtremaQuery(extrema,propertyName);
                
                // check result
                CalcResult tempRes = visitor.getResult();
                if(tempRes==null){
                    throw new IllegalStateException("Unable to compute extrema value:"+extrema);
                }
                final Object result=tempRes.getValue();
                if(result==null){
                    throw new IllegalStateException("Unable to compute extrema value:"+extrema);
                }                
                return ConvertersHack.convert(result, String.class);
            } catch (IOException e) {
                if(LOGGER.isLoggable(Level.WARNING))
                        LOGGER.log(Level.WARNING,"Unable to compute extrema for TIME_DOMAIN",e);
                return null;
            }
        }
        
        /**
         * Retrieves the values for this domain
         * @return
         */
        private String getValues(){
            try {            
                final Set result = rasterManager.extractDomain(propertyName);          
                // check result
                if(result.size()<=0){
                    return "";
                }
                
                final StringBuilder buff= new StringBuilder();
                for(Iterator it= result.iterator(); it.hasNext();){
                    buff.append(ConvertersHack.convert(it.next(), String.class));
                    if(it.hasNext()){
                        buff.append(",");
                    }
                }
                return buff.toString();

            } catch (IOException e) {
                if(LOGGER.isLoggable(Level.WARNING))
                        LOGGER.log(Level.WARNING,"Unable to parse attribute: " + identifier ,e);
                return "";
            }
        }
    }
    /**
     * An {@link DomainManager} class which allows to deal with additional domains
     * (if any) defined inside the mosaic. It provides DOMAIN_ALIAS <--to--> original attribute mapping
     * capabilities, metadata retrieval, filter creation, and domain support check
     * 
     * @author Daniele Romagnoli, GeoSolutions S.a.S.
     * TODO move to {@link RasterManager}
     */
    class DomainManager {
        
        private final Map<String, DomainDescriptor> domainsMap= new HashMap<String, ImageMosaicReader.DomainDescriptor>();


        DomainManager (Map<String,String> additionalDomainAttributes, SimpleFeatureType simpleFeatureType){
            Utilities.ensureNonNull("additionalDomainAttributes", additionalDomainAttributes);
            Utilities.ensureNonNull("simpleFeatureType", simpleFeatureType);   
            init(additionalDomainAttributes, simpleFeatureType);
        }

        /**
         * @param additionalDomainAttributes
         * @param simpleFeatureType
         * @throws IllegalArgumentException
         */
        private void init(Map<String, String> additionalDomainAttributes,
                SimpleFeatureType simpleFeatureType) throws IllegalArgumentException {
            for(java.util.Map.Entry<String,String> entry:additionalDomainAttributes.entrySet()){
                
                final String domainName=entry.getKey();
                String propertyName=entry.getValue();
                // is the name equals to the propertyname?
                try{
                    if(simpleFeatureType.getDescriptor(propertyName) != null){
                        // add
                        addDomain(domainName,propertyName);    
                        
                        // continue
                        continue;
                    }
                    
                }catch (Exception e) {
                    if(LOGGER.isLoggable(Level.FINE)){
                        LOGGER.log(Level.FINE,e.getLocalizedMessage(),e);
                    }
                }
                
                // ok why we don't have it? Maybe shapefile name truncation?
                if(propertyName.length()>10){
                    // hakc for shapes
                    propertyName= propertyName.substring(0,10);
                    // alias in provided type
                    try{
                        if(simpleFeatureType.getDescriptor(propertyName) != null){
                            // add
                            addDomain(domainName,propertyName);    
                            
                            // continue
                            continue;
                        }
                        
                    }catch (Exception e) {
                        if(LOGGER.isLoggable(Level.FINE)){
                            LOGGER.log(Level.FINE,e.getLocalizedMessage(),e);
                        }
                    }                    
                }
                
                // if I got here, we are in trouble. No way to add this param
                throw new IllegalArgumentException("Unable to add this domain:"+domainName+"-"+propertyName);

            }
        }
        
        /**
         * build an AdditionalDomainManager on top of the provided additionalDomainAttributes 
         * (a comma separated list of attribute names).
         *
         * @param additionalDomainAttributes
         * @param simpleFeatureType 
         */
        DomainManager (String additionalDomainAttributes, SimpleFeatureType simpleFeatureType) {
            Utilities.ensureNonNull("additionalDomainAttributes", additionalDomainAttributes);
            Utilities.ensureNonNull("simpleFeatureType", simpleFeatureType);
            
            final Map<String,String> domainPairs= new HashMap<String, String>();
            
            // split, looking for multiple values
            final String[] additionalDomainsNames = additionalDomainAttributes.split(",");
            if (additionalDomainsNames.length <= 0){
                throw new IllegalArgumentException("Number of Domains should be > 0");
            }

            // add al the provided domain
            for (String domainName : additionalDomainsNames) {
                domainPairs.put(domainName, domainName);
            }
            init(domainPairs, simpleFeatureType);
            
        }

        /**
         * Add a domain to the manager
         * @param domain the name of the domain
         * @param propertyName 
         */
        private void addDomain(String name, String propertyName) {
            Utilities.ensureNonNull("name", name);
            Utilities.ensureNonNull("propertyName", propertyName);
            
            // === checks
            // existing!
            if(domainsMap.containsKey(name)){
                throw new IllegalArgumentException("Trying to add a domain with an existing name"+name);
            }
                   
            // ad with uppercase and with suffix, the parameter that describes it will match this
            final String upperCase = name.toUpperCase();
            domainsMap.put(
                    upperCase+DomainDescriptor.DOMAIN_SUFFIX,
                    new DomainDescriptor(name, propertyName)
            );

        }

        /**
         * Check whether a specific parameter (identified by the {@link Identifier} name) is supported by 
         * this manager (and therefore, by the reader).
         * @param name
         * @return
         */
        public boolean isParameterSupported(final Identifier name) {
            if (!domainsMap.isEmpty()) {
                for(DomainDescriptor domain:domainsMap.values()){
                    final ReferenceIdentifier nameLoc = domain.getDomainaParameterDescriptor().getName();
                    if(nameLoc.equals(name)){
                        return true;
                    }
                }
            }
            return false;
        }

        /**
         * Setup the List of metadataNames for this additional domains manager
         *
         * @return
         */
        public List<String> getMetadataNames() {
            final List<String> metadataNames = new ArrayList<String>();
            if (!domainsMap.isEmpty()) {
                for(DomainDescriptor domain:domainsMap.values()){
                    String domainName = domain.getIdentifier().toUpperCase();
                    metadataNames.add(domainName+DomainDescriptor.DOMAIN_SUFFIX);
                    metadataNames.add(DomainDescriptor.HAS_PREFIX+domainName+DomainDescriptor.DOMAIN_SUFFIX);                        
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
            Utilities.ensureNonNull("name", name);
            
            String value = null;
            if (domainsMap.size()>0) {
                // is a domain?
                if(domainsMap.containsKey(name)){
                    final DomainDescriptor domainDescriptor = domainsMap.get(name);
                    value = domainDescriptor.getValues();
                } else {
                    // is a simple Has domain query?
                    if(name.startsWith(DomainDescriptor.HAS_PREFIX)){
                        final String substring = name.substring(DomainDescriptor.HAS_PREFIX.length(),name.length());
                        if(domainsMap.containsKey(substring)){
                            return Boolean.toString(Boolean.TRUE);
                        } else {
                            return Boolean.toString(Boolean.FALSE);                        
                        }
                    } else {
                        // MINUM or MAXIMUM
                        if(name.endsWith("MINIMUM")||name.endsWith("MAXIMUM")){
                            return domainsMap.get(name.substring(0, name.lastIndexOf("_"))).getExtrema(name);
                        }
                    }
                }
            }
            return value;
        }
        
        /**
         * Setup a Filter on top of the specified domainRequest which is in the form "key=value"
         * 
         * @param domain
         * @param values
         * @return
         */
        public Filter createFilter(String domain, List values) {
            // === checks
            if (domain == null || domain.isEmpty()){
                throw new IllegalArgumentException("Null domain requested");
            }
            if (values == null || values.isEmpty()){
                throw new IllegalArgumentException("Null domain values provided");
            }
            if (domainsMap.isEmpty() || !domainsMap.containsKey(domain)) {
                throw new IllegalArgumentException("requested domain is not supported by this mosaic: " + domain);
            }
            
            
            // get the property name
            DomainDescriptor domainDescriptor = domainsMap.get(domain);
            final String propertyName=domainDescriptor.getPropertyName();
            
            // === create the filter
            // loop values and AND them
            final int size = values.size();
            final List<Filter> filters= new ArrayList<Filter>();
            for(int i=0;i<size;i++){
                // checks
                final Object value = values.get(i);
                if(value==null){
                    if(LOGGER.isLoggable(Level.INFO)){
                        LOGGER.info("Ignoring null date for the filter:"+domain);
                    }
                    continue;
                }                    
                if(value instanceof Range){
                    // RANGE                        
                    final Range range= (Range)value;
                    filters.add( 
                            FeatureUtilities.DEFAULT_FILTER_FACTORY.and(
                                    FeatureUtilities.DEFAULT_FILTER_FACTORY.lessOrEqual(
                                            FeatureUtilities.DEFAULT_FILTER_FACTORY.property(propertyName), 
                                            FeatureUtilities.DEFAULT_FILTER_FACTORY.literal(range.getMaxValue())),
                                    FeatureUtilities.DEFAULT_FILTER_FACTORY.greaterOrEqual(
                                            FeatureUtilities.DEFAULT_FILTER_FACTORY.property(propertyName), 
                                            FeatureUtilities.DEFAULT_FILTER_FACTORY.literal(range.getMinValue()))
                            ));                                
                    continue;
                }  
                // SINGLE value
                filters.add( 
                        FeatureUtilities.DEFAULT_FILTER_FACTORY.equal(
                                FeatureUtilities.DEFAULT_FILTER_FACTORY.property(propertyName),
                                FeatureUtilities.DEFAULT_FILTER_FACTORY.literal(value),true)
                            );                    
            }
            return FeatureUtilities.DEFAULT_FILTER_FACTORY.or(filters);
            
        }
        
       /** 
         * Return the set of dynamic parameterDescriptors (the ones related to domains) for this reader 
         * @return
         */
        public Set<ParameterDescriptor<List>> getDynamicParameters() {
            Set<ParameterDescriptor<List>> dynamicParameters= new HashSet<ParameterDescriptor<List>>();
            if (!domainsMap.isEmpty()) {
                for(DomainDescriptor domain:domainsMap.values()){
                    dynamicParameters.add(domain.getDomainaParameterDescriptor());
                }
            }
           // return
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

	String locationAttributeName=Utils.DEFAULT_LOCATION_ATTRIBUTE;

	RasterManager rasterManager;

        /** The inner {@link DomainManager} instance which allows to manage custom dimensions */
        DomainManager domainsManager;

	int maxAllowedTiles=ImageMosaicFormat.MAX_ALLOWED_TILES.getDefaultValue();

	/** The suggested SPI to avoid SPI lookup*/
	ImageReaderSpi suggestedSPI;

	GranuleCatalog catalog;

	boolean cachingIndex;

	boolean imposedBBox;
	
	boolean heterogeneousGranules;

	String typeName;

        DomainManager timeDomainManager;

        DomainManager elevationDomainManager;
	
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
		    try {
                        initReaderFromURL(source, uHints);
                    } catch (Exception e) {
                        throw new DataSourceException(e);
                    }
		}
	}
	
	/**
	 * Init this {@link ImageMosaicReader} using the provided {@link ImageMosaicDescriptor} as source. 
	 * @param source
	 * @param uHints
	 * @throws DataSourceException
	 */
    private void initReaderFromDescriptor(final ImageMosaicDescriptor source, final Hints uHints) throws IOException {
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

        final SimpleFeatureType schema = catalog.getType();
        if (schema == null) {
            throw new DataSourceException("Unable to create reader for this mosaic since the inner catalog schema is null.");
        }
        
        if (configuration.getAdditionalDomainAttributes() != null) {
            domainsManager = new DomainManager(configuration.getAdditionalDomainAttributes(),schema);
        }
        
        // time attribute
        if(configuration.getTimeAttribute()!=null){
            final HashMap<String, String> init=new HashMap<String, String>();
            init.put("TIME", configuration.getTimeAttribute());
            timeDomainManager= new DomainManager(init,schema);
        }
        // elevation attribute
        if(configuration.getElevationAttribute()!=null){
            final HashMap<String, String> init=new HashMap<String, String>();
            init.put("ELEVATION", configuration.getElevationAttribute());
            elevationDomainManager= new DomainManager(init,schema);
        }

        // grid geometry
        setGridGeometry();
        
        // raster manager
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
    private void initReaderFromURL(final Object source, final Hints hints) throws Exception {
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
		if(this.hints.containsKey(Hints.MOSAIC_LOCATION_ATTRIBUTE)){
		    this.locationAttributeName=((String)this.hints.get(Hints.MOSAIC_LOCATION_ATTRIBUTE));	
		}
		
		//
		// Load tiles informations, especially the bounds, which will be
		// reused
		//
		try {
			// create the index
			catalog= GranuleCatalogFactory.createGranuleCatalog(sourceURL, configuration);
			// error
			if(catalog==null){
			    throw new DataSourceException("Unable to create index for this URL "+sourceURL);
			}
                        final SimpleFeatureType type= catalog.getType();
                        if (type==null){
                            throw new IllegalArgumentException("Problems when opening the index, no typenames for the schema are defined");
                        }
                        
                        
			// additional domain properties
		        SimpleFeatureType schema = catalog.getType();
                        if (configuration.getAdditionalDomainAttributes() != null) {
		            domainsManager = new DomainManager(configuration.getAdditionalDomainAttributes(),schema);
		        }
		        // time attribute
		        if(configuration.getTimeAttribute()!=null){
                            final HashMap<String, String> init=new HashMap<String, String>();
                            init.put("TIME", configuration.getTimeAttribute());
		            timeDomainManager= new DomainManager(init,schema);
		        }
                        // elevation attribute
                        if(configuration.getElevationAttribute()!=null){
                            final HashMap<String, String> init=new HashMap<String, String>();
                            init.put("ELEVATION", configuration.getElevationAttribute());
                            elevationDomainManager= new DomainManager(init,schema);
                        }

			
			// everything is fine
			if (LOGGER.isLoggable(Level.FINE))
				LOGGER.fine("Connected mosaic reader to its index "
						+ sourceURL.toString());

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
			
			// creating the raster manager
			rasterManager = new RasterManager(this);
		}
		catch (Throwable e) {
			try {
				if(catalog!=null){
				    catalog.dispose();
				}
			} catch (Throwable e1) {
				if (LOGGER.isLoggable(Level.FINEST)){
				    LOGGER.log(Level.FINEST, e1.getLocalizedMessage(), e1);
				}
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
						
			// rethrow
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
	private void extractProperties(final MosaicConfigurationBean configuration) throws IOException {

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
			} catch (Exception e) {
				if(LOGGER.isLoggable(Level.FINE))
					LOGGER.log(Level.FINE,e.getLocalizedMessage(),e);
				suggestedSPI=null;
			} 
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
            if (domainsManager != null) {
                metadataNames.addAll(domainsManager.getMetadataNames());
            }
            if(parentNames!=null)
                metadataNames.addAll(Arrays.asList(parentNames));
            return metadataNames.toArray(new String[metadataNames.size()]);
	}

	@Override
	public String getMetadataValue(final String name) {
	    String value=super.getMetadataValue(name);
	    if(value!=null){
	        return value;
	    }
	    final boolean hasTimeDomain = timeDomainManager!=null;
	    final boolean hasElevationDomain = elevationDomainManager!=null;
	    
            if (name.equalsIgnoreCase(HAS_ELEVATION_DOMAIN))
                return String.valueOf(hasElevationDomain);
    
            if (name.equalsIgnoreCase(HAS_TIME_DOMAIN)){
                return String.valueOf(hasTimeDomain);
            }
    
            // NOT supported
            if (name.equalsIgnoreCase(TIME_DOMAIN_RESOLUTION)){
                return null;
            }            
            // NOT supported
            if (name.equalsIgnoreCase(ELEVATION_DOMAIN_RESOLUTION)){
                return null;
            }
    
            
            if (hasTimeDomain){
                if(name.equalsIgnoreCase("time_domain")) {
                    return timeDomainManager.getMetadataValue(name);    
                }
                if ((name.equalsIgnoreCase("time_domain_minimum") || name.equalsIgnoreCase("time_domain_maximum"))) {
                    return timeDomainManager.getMetadataValue(name);
                }
            }
            
            
            if (hasElevationDomain) {
                if(name.equalsIgnoreCase("elevation_domain")){
                    return elevationDomainManager.getMetadataValue(name);
                }
                
                if(name.equalsIgnoreCase("elevation_domain_minimum") || name.equalsIgnoreCase("elevation_domain_maximum")){
                    return elevationDomainManager.getMetadataValue(name);
                }
    
            }
 
        		
            // check additional domains
            if (domainsManager != null) {
                return domainsManager.getMetadataValue(name);
            } 

            // 
            return value;
	}



    @Override
    public Set<ParameterDescriptor<List>> getDynamicParameters() {
        return domainsManager != null ? domainsManager.getDynamicParameters() : super.getDynamicParameters();
    }

    public boolean isParameterSupported(Identifier name) {
        return domainsManager != null ? domainsManager.isParameterSupported(name) : false;
    }
}
