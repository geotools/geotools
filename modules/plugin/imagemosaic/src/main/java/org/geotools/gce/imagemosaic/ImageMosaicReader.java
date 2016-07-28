/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006 - 2016, Open Source Geospatial Foundation (OSGeo)5
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
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.spi.ImageReaderSpi;
import javax.media.jai.ImageLayout;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.DefaultHarvestedSource;
import org.geotools.coverage.grid.io.DimensionDescriptor;
import org.geotools.coverage.grid.io.GranuleSource;
import org.geotools.coverage.grid.io.HarvestedSource;
import org.geotools.coverage.grid.io.OverviewPolicy;
import org.geotools.coverage.grid.io.StructuredGridCoverage2DReader;
import org.geotools.coverage.grid.io.footprint.MultiLevelROIProvider;
import org.geotools.data.DataSourceException;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultFileServiceInfo;
import org.geotools.data.FileGroupProvider.FileGroup;
import org.geotools.data.ResourceInfo;
import org.geotools.data.ServiceInfo;
import org.geotools.factory.Hints;
import org.geotools.gce.imagemosaic.ImageMosaicEventHandlers.ExceptionEvent;
import org.geotools.gce.imagemosaic.ImageMosaicEventHandlers.FileProcessingEvent;
import org.geotools.gce.imagemosaic.ImageMosaicEventHandlers.ProcessingEvent;
import org.geotools.gce.imagemosaic.OverviewsController.OverviewLevel;
import org.geotools.gce.imagemosaic.Utils.Prop;
import org.geotools.gce.imagemosaic.catalog.CatalogConfigurationBean;
import org.geotools.gce.imagemosaic.catalog.GranuleCatalog;
import org.geotools.gce.imagemosaic.catalog.GranuleCatalogFactory;
import org.geotools.gce.imagemosaic.catalog.MultiLevelROIProviderMosaicFactory;
import org.geotools.gce.imagemosaic.catalogbuilder.CatalogBuilderConfiguration;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.operation.transform.AffineTransform2D;
import org.geotools.util.Utilities;
import org.opengis.coverage.grid.Format;
import org.opengis.coverage.grid.GridEnvelope;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.geometry.BoundingBox;
import org.opengis.metadata.Identifier;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.parameter.ParameterValue;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.datum.PixelInCell;
import org.opengis.referencing.operation.MathTransform;
/**
 * Reader responsible for providing access to mosaic of georeferenced
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
public class ImageMosaicReader extends AbstractGridCoverage2DReader implements StructuredGridCoverage2DReader {

    Set<String> names = new HashSet<String>();

    String defaultName = null;

    public static final String UNSPECIFIED = "_UN$PECIFIED_";

    Map<String, RasterManager> rasterManagers = new ConcurrentHashMap<String, RasterManager>();

    public RasterManager getRasterManager(String name) {
          if(name != null && rasterManagers.containsKey(name)){
              return rasterManagers.get(name);
          }
          return null;
  }
    
    @Override
    public String[] getGridCoverageNames() {
        return names.toArray(new String[]{});
    }

    /** Logger. */
	private final static Logger LOGGER = org.geotools.util.logging.Logging.getLogger(ImageMosaicReader.class);

	/**
	 * The source {@link URL} pointing to the index shapefile for this
	 * {@link ImageMosaicReader}.
	 */
	URL sourceURL;
	
        File parentDirectory;

	boolean expandMe;
	
	PathType pathType;
	
	ExecutorService multiThreadedLoader;

	String locationAttributeName=Utils.DEFAULT_LOCATION_ATTRIBUTE;

        int maxAllowedTiles=ImageMosaicFormat.MAX_ALLOWED_TILES.getDefaultValue();

	/** The suggested SPI to avoid SPI lookup*/
	ImageReaderSpi suggestedSPI;
	
	GranuleCatalog granuleCatalog;

	boolean cachingIndex;

	boolean imposedBBox;
	
	boolean heterogeneousGranules;

        boolean checkAuxiliaryMetadata = false;

	String typeName;

	/**
	 * Enumeration object used for defining 3 different behaviours for the Harvesting, each of them associated
	 * to one of these 3 objects:
	 * <ul>
	 * <li>File</li>
	 * <li>Directory</li>
	 * <li>List of Files</li>
	 * </ul>
	 * 
	 * @author Nicola Lagomarsini, GeoSolutions S.A.S.
	 *
	 */
	public enum HarvestedResource{
	    FILE {
            @Override
            public void harvest(String defaultCoverage, Object source, Hints hints, final List<HarvestedSource> result, ImageMosaicReader reader) {
                File file;
                if (source instanceof Collection<?>) {
                    file = (File) ((Collection<?>) source).iterator().next();
                } else {
                    file = (File) source;
                }
                // Directory associated to the input File
                File directory = file.getParentFile();
                // File Filter associated to the input File
                IOFileFilter filter = FileFilterUtils.nameFileFilter(file.getName());
                // Harvesting file
                harvestCalculation(defaultCoverage, result, reader, directory, filter);
                
            }
        }, DIRECTORY {
            @Override
            public void harvest(String defaultCoverage, Object source, Hints hints, final List<HarvestedSource> result, ImageMosaicReader reader) {
                File directory;
                if (source instanceof Collection<?>) {
                    directory = (File) ((Collection<?>) source).iterator().next();
                } else {
                    directory = (File) source;
                }
                // Harvesting directory
                harvestCalculation(defaultCoverage, result, reader, directory, null);
                
            }
        }, FILE_COLLECTION {
            @Override
            public void harvest(String defaultCoverage, Object source, Hints hints,
                    final List<HarvestedSource> result, final ImageMosaicReader reader) {
                // I have already checked that it is a Collection of File objects
                Collection<File> files = (Collection<File>) source;
                
                // Harvesting
             
                // prepare the walker configuration
                CatalogBuilderConfiguration configuration = new CatalogBuilderConfiguration();
                configuration.setParameter(Prop.ABSOLUTE_PATH, Boolean.toString(true));
                
                // Setting of the HARVEST_DIRECTORY property for passing the checks even if it is not used
                // Selection of the first file
                Iterator<File> it = files.iterator();
                String indexingPath = it.next().getAbsolutePath();
                configuration.setParameter(Prop.HARVEST_DIRECTORY, indexingPath);
                
                if(defaultCoverage == null) {
                    String[] coverageNames = reader.getGridCoverageNames();
                    defaultCoverage = (coverageNames != null && coverageNames.length > 0) ? coverageNames[0] : Utils.DEFAULT_INDEX_NAME;
                } 
                
                configuration.setParameter(Prop.INDEX_NAME, defaultCoverage);
                configuration.setHints(new Hints(Utils.MOSAIC_READER, reader));
                
                File mosaicSource = DataUtilities.urlToFile(reader.sourceURL);
                if(!mosaicSource.isDirectory()) {
                    mosaicSource = mosaicSource.getParentFile();
                }
                
                configuration.setParameter(Prop.ROOT_MOSAIC_DIR, mosaicSource.getAbsolutePath());
                
                // run the walker and collect information
                ImageMosaicEventHandlers eventHandler = new ImageMosaicEventHandlers();
                final ImageMosaicConfigHandler catalogHandler = new ImageMosaicConfigHandler(
                        configuration, eventHandler) {
                    protected GranuleCatalog buildCatalog() throws IOException {
                        return reader.granuleCatalog;
                    };
                };
                // Creation of the Walker for the File List
                ImageMosaicFileCollectionWalker walker = new ImageMosaicFileCollectionWalker(catalogHandler, eventHandler,files);
                eventHandler.addProcessingEventListener(new ImageMosaicEventHandlers.ProcessingEventListener() {
                    
                    @Override
                    public void getNotification(ProcessingEvent event) {
                        if(event instanceof FileProcessingEvent) {
                            FileProcessingEvent fileEvent = (FileProcessingEvent) event;
                            result.add(new DefaultHarvestedSource(fileEvent.getFile(), fileEvent.isIngested(), fileEvent.getMessage()));
                        }
                    }
                    
                    @Override
                    public void exceptionOccurred(ExceptionEvent event) {
                        // nothing to do
                    }
                });
                // Wait the Walker ends its operations
                walker.run();
            }
        };
	    
	    HarvestedResource(){}
	    
	    /**
	     * Harvesting of the input resource. The result will be strored inside the {@link List} object.
	     * 
	     * @param defaultCoverage
	     * @param source
	     * @param hints
	     * @param result
	     * @param reader
	     */
	    public abstract void harvest(String defaultCoverage, Object source, Hints hints, final List<HarvestedSource> result, ImageMosaicReader reader);
	    
	    /**
	     * Returns the HarvestedResource associated to the input Object
	     * 
	     * @param source
	     * @return
	     */
            public static HarvestedResource getResourceFromObject(Object source) {
                // Check if the resource is a File or a Directory
                if (source instanceof File) {
                    return getResourceFromFile((File) source);
                }
                // For a String instance, it is converted to String
                if (source instanceof String) {
                    File file = new File((String) source);
                    return getResourceFromFile(file);
                }
                // Check if the input Object is a File Collection
                if (source instanceof Collection<?>) {
                    Collection<File> files = null;
                    try {
                        files = (Collection<File>) source;
                    } catch (ClassCastException e) {
                        // Log the exception
                        if (LOGGER.isLoggable(Level.WARNING)) {
                            LOGGER.log(Level.WARNING, e.getMessage(), e);
                        }
                    }
                    // If the files are present
                    if (files != null) {
                        // No File is saved
                        int fileSize = files.size();
                        // Check on the File Size
                        if (fileSize < 1) {
                            return null;
                        } else if (fileSize == 1) {
                            // If the Collection size is 1 then the object can be only a file or a directory
                            return getResourceFromFile(files.iterator().next());
                        } else {
                            return FILE_COLLECTION;
                        }
    
                    }
                }
                return null;
            }

	    /**
	     * Check if the File Object is a DIRECTORY or not and return the associated {@link HarvestedResource}
	     * 
	     * @param file
	     * @return
	     */
            private static HarvestedResource getResourceFromFile(File file) {
                if(file != null){
                    if(file.isDirectory()){
                        return DIRECTORY;
                    }else{
                        return FILE;
                    }                    
                }
                return null;
            }
            
            /**
             * Method for harvesting on a directory
             * 
             * @param defaultCoverage
             * @param result
             * @param reader
             * @param directory
             * @param filter
             */
            private static void harvestCalculation(String defaultCoverage,
                final List<HarvestedSource> result, final ImageMosaicReader reader, File directory,
                IOFileFilter filter) {
                // prepare the walker configuration
                CatalogBuilderConfiguration configuration = new CatalogBuilderConfiguration();
                configuration.setParameter(Prop.ABSOLUTE_PATH, Boolean.toString(Utils.DEFAULT_PATH_BEHAVIOR));
                String indexingPath = directory.getAbsolutePath();
                configuration.setParameter(Prop.HARVEST_DIRECTORY, indexingPath);
                if(defaultCoverage == null) {
                    String[] coverageNames = reader.getGridCoverageNames();
                    defaultCoverage = (coverageNames != null && coverageNames.length > 0) ? coverageNames[0] : Utils.DEFAULT_INDEX_NAME;
                } 
                configuration.setParameter(Prop.INDEX_NAME, defaultCoverage);
                configuration.setHints(new Hints(Utils.MOSAIC_READER, reader));
                
                File mosaicSource = DataUtilities.urlToFile(reader.sourceURL);
                if(!mosaicSource.isDirectory()) {
                    mosaicSource = mosaicSource.getParentFile();
                }
                
                configuration.setParameter(Prop.ROOT_MOSAIC_DIR, mosaicSource.getAbsolutePath());
                
                // run the walker and collect information
                ImageMosaicEventHandlers eventHandler = new ImageMosaicEventHandlers();
                final ImageMosaicConfigHandler catalogHandler = new ImageMosaicConfigHandler(configuration,
                        eventHandler) {
                @Override
                protected GranuleCatalog buildCatalog() throws IOException {
                    return reader.granuleCatalog;
                }
            };
                // build the index
                ImageMosaicDirectoryWalker walker = new ImageMosaicDirectoryWalker(catalogHandler, eventHandler,filter);
                eventHandler.addProcessingEventListener(new ImageMosaicEventHandlers.ProcessingEventListener() {
                    
                    @Override
                    public void getNotification(ProcessingEvent event) {
                        if(event instanceof FileProcessingEvent) {
                            FileProcessingEvent fileEvent = (FileProcessingEvent) event;
                            result.add(new DefaultHarvestedSource(fileEvent.getFile(), fileEvent.isIngested(), fileEvent.getMessage()));
                        }
                    }
                    
                    @Override
                    public void exceptionOccurred(ExceptionEvent event) {
                        // nothing to do
                    }
                });

                walker.run();
            }
	}
	
    /**
     * Constructor.
     * 
     * @param source The source object.
     * @throws IOException
     * @throws UnsupportedEncodingException
     * 
     */
    public ImageMosaicReader(Object source, Hints uHints) throws IOException {
        super(source, uHints);

        //
        // try to extract a multithreaded loader if available
        //
        if (this.hints.containsKey(Hints.EXECUTOR_SERVICE)) {
            final Object executor = uHints.get(Hints.EXECUTOR_SERVICE);
            if (executor != null && executor instanceof ExecutorService) {
                multiThreadedLoader = (ExecutorService) executor;
                if (LOGGER.isLoggable(Level.FINE)) {
                    if (multiThreadedLoader instanceof ThreadPoolExecutor) {
                        final ThreadPoolExecutor tpe = (ThreadPoolExecutor) multiThreadedLoader;
                        LOGGER.fine("Using ThreadPoolExecutor with the following settings: "
                                + "core pool size = " + tpe.getCorePoolSize()
                                + "\nmax pool size = " + tpe.getMaximumPoolSize()
                                + "\nkeep alive time "
                                + tpe.getKeepAliveTime(TimeUnit.MILLISECONDS));
                    }
                }
            }
        }

        // max allowed tiles for a single request
        if (this.hints.containsKey(Hints.MAX_ALLOWED_TILES))
            this.maxAllowedTiles = ((Integer) this.hints.get(Hints.MAX_ALLOWED_TILES));

        //
        // Check source
        //
        if (source instanceof ImageMosaicDescriptor) {
            initReaderFromDescriptor((ImageMosaicDescriptor) source, uHints);
        } else {
            try {
                
             // Cloning the hints
                Hints localHints = new Hints(uHints);
                if (localHints != null) {
                    localHints.add(new Hints(Utils.MOSAIC_READER, this));
                } 
                initReaderFromURL(source, localHints);
            } catch (Exception e) {
                LOGGER.log(Level.FINE, "Exception generated while trying to initialize ImageMosaicReader: "
                        + e.getMessage(), e);
                throw new DataSourceException(e);
            }
        }
    }

    /**
     * Init this {@link ImageMosaicReader} using the provided {@link ImageMosaicDescriptor} as source.
     * 
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
        GranuleCatalog catalog = source.getCatalog();
        if (catalog == null) {
            throw new DataSourceException("Unable to create reader for this mosaic since the inner catalog is null.");
        }

        final SimpleFeatureType schema = catalog.getType(configuration.getCatalogConfigurationBean().getTypeName());
        if (schema == null) {
            throw new DataSourceException("Unable to create reader for this mosaic since the inner catalog schema is null.");
        }
        
        granuleCatalog = catalog;

        // grid geometry
        setGridGeometry(typeName);

        // raster manager
        addRasterManager(configuration, true);
    }

    /**
     * Init this {@link ImageMosaicReader} using the provided object as a source referring to an {@link URL}.
     * 
     * @param source
     * @param uHints
     * @throws DataSourceException
     */
    private void initReaderFromURL(final Object source, final Hints hints) throws Exception {
        this.sourceURL = Utils.checkSource(source, hints);
        
        // Preliminar check on source
        if (this.sourceURL == null) {
            throw new DataSourceException(
                    "This plugin accepts File, URL or String. The string may describe a File or an URL");
        }

        // Load properties file
        MosaicConfigurationBean configuration = null;
        try {
            if (sourceURL.getProtocol().equals("file")) {
                final File sourceFile = DataUtilities.urlToFile(sourceURL);
                if (!sourceFile.exists()) {
                    throw new DataSourceException("The specified sourceURL doesn't refer to an existing file");
                }
            }
            if (sourceURL != null) {
                parentDirectory = DataUtilities.urlToFile(sourceURL);
                if (!parentDirectory.isDirectory()) {
                        parentDirectory = parentDirectory.getParentFile();
                }
            }
            configuration = Utils.loadMosaicProperties(sourceURL, this.locationAttributeName);
            if (configuration == null) {
                //
                // do we have a datastore properties file? It will preempt on the shapefile
                //
                final File parent = DataUtilities.urlToFile(sourceURL).getParentFile();

                // this can be used to look for properties files that do NOT define a datastore
                final File[] properties = parent.listFiles((FilenameFilter) FileFilterUtils.and(
                        FileFilterUtils.notFileFilter(FileFilterUtils
                                .nameFileFilter("indexer.properties")), FileFilterUtils.and(
                                FileFilterUtils.notFileFilter(FileFilterUtils
                                        .nameFileFilter("datastore.properties")), FileFilterUtils
                                        .makeFileOnly(FileFilterUtils
                                                .suffixFileFilter(".properties")))));

                // do we have a valid datastore + mosaic properties pair?
                final File datastoreProperties = new File(parent, "datastore.properties");

                // Scan for MosaicConfigurationBeans from properties files
                List<MosaicConfigurationBean> beans = new ArrayList<MosaicConfigurationBean>();
                for (File propFile : properties) {
                    if (Utils.checkFileReadable(propFile) && Utils.loadMosaicProperties(DataUtilities.fileToURL(propFile), "") != null) {
                        configuration = Utils.loadMosaicProperties(DataUtilities.fileToURL(propFile), this.locationAttributeName);
                        if (configuration != null) {
                            beans.add(configuration);
                        }
                    }
                }
                
                // In case we didn't find any configuration bean and datastore properties, we can't do anything
                if (beans.isEmpty() && !datastoreProperties.exists()) {
                    throw new DataSourceException("No mosaic properties file or datastore properties file have been found");
                }
                
                // Catalog initialization from datastore
                GranuleCatalog catalog = null;
                final Properties params = ImageMosaicConfigHandler.createGranuleCatalogProperties(datastoreProperties);

                // Since we are dealing with a catalog from an existing store, make sure to scan for all the typeNames on initialization
                final Object typeNames=params.get(Utils.SCAN_FOR_TYPENAMES);
                if (typeNames!=null){
                    params.put(Utils.SCAN_FOR_TYPENAMES, Boolean.valueOf(typeNames.toString()));
                } else {
                    params.put(Utils.SCAN_FOR_TYPENAMES, Boolean.TRUE);
                }
                if (beans.size() > 0) {
                    catalog = GranuleCatalogFactory.createGranuleCatalog(sourceURL, beans.get(0).getCatalogConfigurationBean(), params, getHints());
                } else {
                    catalog = ImageMosaicConfigHandler
                            .createGranuleCatalogFromDatastore(parent, datastoreProperties, true, getHints());
                } 
                MultiLevelROIProvider rois = MultiLevelROIProviderMosaicFactory.createFootprintProvider(parent);
                catalog.setMultiScaleROIProvider(rois);
                if (granuleCatalog != null) {
                    granuleCatalog.dispose();
                }
                granuleCatalog = catalog;

                if (granuleCatalog == null) {
                    throw new DataSourceException("Unable to create index for this URL " + sourceURL);
                }

                // Creating a RasterManager for each mosaic configuration found on disk
                for (MosaicConfigurationBean bean : beans) {
                    // Add a RasterManager on top of this Mosaic configuration bean and initialize it
                    addRasterManager(bean, true);
                }
            } else {
                
                // Old style code: we have a single MosaicConfigurationBean. Use that to create the catalog 
                granuleCatalog = ImageMosaicConfigHandler
                        .createCatalog(sourceURL, configuration, this.hints);
                File parent = DataUtilities.urlToFile(sourceURL).getParentFile();
                MultiLevelROIProvider rois = MultiLevelROIProviderMosaicFactory.createFootprintProvider(parent);
                granuleCatalog.setMultiScaleROIProvider(rois);
                addRasterManager(configuration, true);
            }

        } catch (Throwable e) {
            
            // Dispose catalog
            try {
                if (granuleCatalog != null) {
                    granuleCatalog.dispose();
                }
            } catch (Throwable e1) {
                if (LOGGER.isLoggable(Level.FINEST)) {
                    LOGGER.log(Level.FINEST, e1.getLocalizedMessage(), e1);
                }
            } finally {
                granuleCatalog = null;
            }
            
            // dispose raster managers as well
            try {
                disposeManagers();
            } catch (Throwable e1) {
                if (LOGGER.isLoggable(Level.FINEST)) {
                    LOGGER.log(Level.FINEST, e1.getLocalizedMessage(), e1);
                }
            } finally {
                rasterManagers = null;
            }

            // rethrow
            throw new DataSourceException(e);
        }
    }

    private void setGridGeometry(final ReferencedEnvelope envelope, final GranuleCatalog catalog,
            String typeName) {
        Utilities.ensureNonNull("index", catalog);
        //
        // save the bbox and prepare other info
        //
        final BoundingBox bounds = catalog.getBounds(typeName);
        
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

	private void setGridGeometry (final String typeName) {
	    setGridGeometry(null, granuleCatalog, typeName); 
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
		
		checkAuxiliaryMetadata = configuration.isCheckAuxiliaryMetadata();
		
		CatalogConfigurationBean catalogConfigurationBean = configuration.getCatalogConfigurationBean();
		
		// do we have heterogenous granules
		heterogeneousGranules = catalogConfigurationBean.isHeterogeneous();

		// absolute or relative path
		pathType = catalogConfigurationBean.isAbsolutePath()?PathType.ABSOLUTE:PathType.RELATIVE;
		
		//
		// location attribute
		//
		locationAttributeName = catalogConfigurationBean.getLocationAttribute();
		
		// suggested SPI
		final String suggestedSPIClass = catalogConfigurationBean.getSuggestedSPI();
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
		cachingIndex = catalogConfigurationBean.isCaching();
		
		// imposed BBOX
                if(configuration.getEnvelope()!=null){
            		this.imposedBBox=true;
            		// we set the BBOX later to retain also the CRS
                } else {
                	this.imposedBBox=false;
                }
		
		// typeName to be used for reading the mosaic
		this.typeName = catalogConfigurationBean.getTypeName();

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

    public GridCoverage2D read(GeneralParameterValue[] params) throws IOException {
	     return read (UNSPECIFIED, params); 
	}

	/**
	 * 
	 * @see org.opengis.coverage.grid.GridCoverageReader#read(org.opengis.parameter.GeneralParameterValue[])
	 * @Override
	 */
	public GridCoverage2D read(String coverageName, GeneralParameterValue[] params) throws IOException {
	   
	    // check if we were disposed already
	    if(rasterManagers == null){
	        throw new IOException("Looks like this reader has been already disposed or it has not been properly initialized.");
	    }
		if (LOGGER.isLoggable(Level.FINE)) {
    		    if (sourceURL != null) {
    			LOGGER.fine("Reading mosaic from " + sourceURL.toString());
    		    } else {
    		        LOGGER.fine("Reading mosaic");
    		    }
    		    final double[][] levels = getResolutionLevels(coverageName);
    		    if (levels != null) {
    		        final double[] highRes = levels[0];
    		        LOGGER.fine("Highest res "+ highRes[0] + " " + highRes[1]);
    		    }
		    
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
		final Collection<GridCoverage2D> response = read(params, coverageName);
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
     * Look for the parameter containing the coverage name and check its validity. Then delegate the proper RasterManager to do the read operation.
     * 
     * @param params
     * @return
     * @throws IOException
     */
    private Collection<GridCoverage2D> read(GeneralParameterValue[] params, String coverageName)
            throws IOException {
        coverageName = checkUnspecifiedCoverage(coverageName);
        return getRasterManager(coverageName).read(params);
    }

    /**
     * Package private accessor for {@link Hints}.
     * 
     * @return this {@link Hints} used by this reader.
     */
    Hints getHints() {
        return super.hints;
    }

    /**
     * Package private accessor for the highest resolution values.
     * 
     * @return the highest resolution values.
     */
    double[] getHighestRes() {
        return super.highestRes;
    }
	
    /**
     * 
     * @return
     */
    double[][] getOverviewsResolution() {
        return super.overViewResolutions;
    }

    int getNumberOfOvervies() {
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

    /**
     * Number of coverages for this reader is 1
     * 
     * @return the number of coverages for this reader.
     */
    @Override
    public int getGridCoverageCount() {
        return names.size();
    }

    /**
     * Releases resources held by this reader.
     * 
     */
    @Override
    public synchronized void dispose() {
        super.dispose();
        synchronized (this) {
            try {
                if (granuleCatalog != null)
                    this.granuleCatalog.dispose();
                disposeManagers();
            } catch (Exception e) {
                if (LOGGER.isLoggable(Level.FINE))
                    LOGGER.log(Level.FINE, e.getLocalizedMessage(), e);
            }
        }
    }

    /**
     * Dispose raster managers
     */
    private void disposeManagers() {
        if (rasterManagers != null) {
            Set<String> keys = rasterManagers.keySet();
            for (String key: keys) {
                rasterManagers.get(key).dispose();
            }
            rasterManagers.clear();
            rasterManagers = null;
        }
    }

    @Override
    public String[] getMetadataNames() {
        return getMetadataNames(UNSPECIFIED);
    }

    /**
     * Populate the metadata names array for the specified coverageName
     * @param coverageName
     * @return
     */
    @Override
    public String[] getMetadataNames(String coverageName) {
        String name = checkUnspecifiedCoverage(coverageName);
        RasterManager manager = getRasterManager(name);
        return manager != null ? manager.getMetadataNames() : null;
    }

    @Override
    public String getMetadataValue(final String name) {
        return getMetadataValue(UNSPECIFIED, name);
    }

    @Override
    public String getMetadataValue(String coverageName, final String name) {
        coverageName = checkUnspecifiedCoverage(coverageName);
        RasterManager manager = getRasterManager(coverageName);
        return manager.getMetadataValue(name);
    }

    @Override
    public Set<ParameterDescriptor<List>> getDynamicParameters() {
        return getDynamicParameters(UNSPECIFIED);
    }
    
    @Override
    public Set<ParameterDescriptor<List>> getDynamicParameters(String coverageName) {
        coverageName = checkUnspecifiedCoverage(coverageName);
        RasterManager manager =  getRasterManager(coverageName);
        return (Set<ParameterDescriptor<List>>) (manager.domainsManager != null ? manager.domainsManager
                .getDynamicParameters() :  Collections.emptySet());
    }

    public boolean isParameterSupported(Identifier name) {
        return isParameterSupported(UNSPECIFIED, name);
    }
    
    @Override
    public int getNumOverviews(String coverageName) {
        coverageName = checkUnspecifiedCoverage(coverageName);
        RasterManager manager =  getRasterManager(coverageName);
        return manager.overviewsController.getNumberOfOverviews();
    }

    @Override
    public int getNumOverviews() {
        return getNumOverviews(UNSPECIFIED);
    }

    @Override
    public double[] getReadingResolutions(OverviewPolicy policy,
            double[] requestedResolution) {
        return getReadingResolutions(UNSPECIFIED, policy, requestedResolution);
    }

    @Override
    public double[] getReadingResolutions(String coverageName, OverviewPolicy policy,
            double[] requestedResolution) {
        coverageName = checkUnspecifiedCoverage(coverageName);
        RasterManager manager =  getRasterManager(coverageName);
        final int numOverviews = getNumOverviews(coverageName);
        OverviewsController overviewsController = manager.overviewsController; 
        OverviewLevel level = null;
        if (numOverviews > 0) {
            int imageIdx = overviewsController.pickOverviewLevel(policy, requestedResolution);
            level = overviewsController.getLevel(imageIdx);
        } else {
            level = overviewsController.getLevel(0);
        }
        return new double[]{level.resolutionX, level.resolutionY};
    }

        /**
         * Check whether the specified parameter is supported for the specified coverage.
         * @param coverageName
         * @param parameterName
         * @return
         */
        public boolean isParameterSupported(String coverageName, Identifier parameterName) {
            coverageName = checkUnspecifiedCoverage(coverageName);
            RasterManager manager = getRasterManager(coverageName);
            return manager.domainsManager != null ? manager.domainsManager.isParameterSupported(parameterName) : false;
    }

    /**
     * Checker whether the specified coverageName is supported. In case the name is Unspecified and the manager only has 1 coverage, then it returns
     * the only available coverage name (using default to speed up the response without need to access the set through an iterator). In case of
     * multiple coverages, throws an Exceptions if the coverage name is unspecified.
     * 
     * @param coverageName
     */
    private String checkUnspecifiedCoverage(String coverageName) {
        if (coverageName.equalsIgnoreCase(UNSPECIFIED)) {
            if (getGridCoverageCount() > 1) {
                throw new IllegalArgumentException(
                        "Need to specify the coverageName for a reader related to multiple coverages");
            } else {
                return defaultName;
            }
        } else {
            if (!names.contains(coverageName)) {
                throw new IllegalArgumentException("The specified coverageName is unavailable");
            } else {
                return coverageName;
            }
        }
    }

    /**
     * Create a RasterManager on top of this {@link MosaicConfigurationBean}
     * @param configuration the {@link MosaicConfigurationBean} to be used to create the {@link RasterManager}
     * @param init {@code true} if the Manager should be initialized. 
     * @return
     * @throws IOException
     */
    protected RasterManager addRasterManager(final MosaicConfigurationBean configuration, final boolean init) throws IOException {
        Utilities.ensureNonNull("MosaicConfigurationBean", configuration);
        String name = configuration.getName();
        RasterManager rasterManager = new RasterManager(this, configuration);
        rasterManagers.put(name, rasterManager);
        names.add(name);
        if (defaultName == null) {
            defaultName = name;
        }
        if (init) {
            rasterManager.initialize(false);
        }
        return rasterManager;
    }

    @Override
    public GranuleSource getGranules(String coverageName, final boolean readOnly)
            throws IOException,
            UnsupportedOperationException {
        if (coverageName == null) {
            coverageName = defaultName;
        }
        RasterManager manager = getRasterManager(coverageName);
        if (manager == null) {
            // Consider creating a new GranuleStore
        } else {
            return manager.getGranuleSource(readOnly, getHints());
        }
        return null;
    }

    @Override
    public boolean isReadOnly() {
        return false;
    }

    @Override
    public void createCoverage(String coverageName, SimpleFeatureType indexSchema) throws IOException, UnsupportedOperationException {
        RasterManager manager = getRasterManager(coverageName);
        if (manager != null) {
            manager.createStore(indexSchema);
        } else {
            throw new IOException("This implementation requires to create a RasterManager for a coverage before creating the store. " + coverageName);
        }
    }

    @Override
    public boolean removeCoverage(String coverageName, final boolean delete) throws IOException {
        return removeCoverage(coverageName, delete, true);
    }

    /**
     * 
     * @param coverageName
     * @param forceDelete
     * @param checkForReferences
     *          {@code true} true in case, when deleting, we need to check whether the file is being referred by some
     *          other coverage or not. In the latter case, we can safely delete it
     *          
     * @return
     * @throws IOException
     */
    private boolean removeCoverage(String coverageName, final boolean forceDelete, final boolean checkForReferences) throws IOException {
        RasterManager manager = getRasterManager(coverageName);
        if (manager != null) {
           manager.removeStore(coverageName, forceDelete, checkForReferences);
           
           // Should I preserve managers for future re-harvesting or it's ok
           // to remove them
           rasterManagers.remove(coverageName);
           names.remove(coverageName);
           if (defaultName == coverageName) {
               Iterator<String> iterator = names.iterator();
               if (iterator.hasNext()) {
                   defaultName = iterator.next();
               } else {
                   defaultName = null;
               }
           }

           return true;
        } else {
            throw new IOException("No Raster manager have been found for the specified coverageName. " + coverageName);
        }
    }
    
    

    @Override
    public GeneralEnvelope getOriginalEnvelope() {
        return getOriginalEnvelope(UNSPECIFIED);
    }

    @Override
    public GeneralEnvelope getOriginalEnvelope(String coverageName) {
        String name = checkUnspecifiedCoverage(coverageName);
        RasterManager manager = getRasterManager(name);
        return manager.spatialDomainManager.coverageEnvelope;
    }

    @Override
    public GridEnvelope getOriginalGridRange() {
        return getOriginalGridRange(UNSPECIFIED);
    }
    
    @Override
    public GridEnvelope getOriginalGridRange(String coverageName) {
        String name = checkUnspecifiedCoverage(coverageName);
        RasterManager manager = getRasterManager(name);
        return manager.spatialDomainManager.gridEnvelope;
    }

    @Override
    public MathTransform getOriginalGridToWorld(PixelInCell pixInCell) {
        return getOriginalGridToWorld(UNSPECIFIED, pixInCell);
    }
    
    @Override
    public MathTransform getOriginalGridToWorld(String coverageName, PixelInCell pixInCell) {
        String name = checkUnspecifiedCoverage(coverageName);
        RasterManager manager = getRasterManager(name);
        return manager.spatialDomainManager.getOriginalGridToWorld(pixInCell);
    }

    @Override
    public CoordinateReferenceSystem getCoordinateReferenceSystem() {
        return getCoordinateReferenceSystem(UNSPECIFIED);
    }
    @Override
    public CoordinateReferenceSystem getCoordinateReferenceSystem(String coverageName) {
        String name = checkUnspecifiedCoverage(coverageName);
        RasterManager manager = getRasterManager(name);
        return manager.spatialDomainManager.coverageCRS;
    }

    @Override
    public ImageLayout getImageLayout() throws IOException {
        return getImageLayout(UNSPECIFIED);
    }

    @Override
    public ImageLayout getImageLayout(String coverageName) throws IOException {
        String name = checkUnspecifiedCoverage(coverageName);
        RasterManager manager = getRasterManager(name);
        return manager.defaultImageLayout;
    }
    
    @Override
    public double[][] getResolutionLevels() throws IOException {
        return getResolutionLevels(UNSPECIFIED);
    }

    @Override
    public double[][] getResolutionLevels(String coverageName) throws IOException {
        String name = checkUnspecifiedCoverage(coverageName);
        RasterManager manager = getRasterManager(name);
        return manager.levels;
    }

    @Override
    public List<HarvestedSource> harvest(String defaultCoverage, Object source, Hints hints)
            throws IOException, UnsupportedOperationException {
        // Get the HarvestedResource object associated to the source. This object defines the harvseting behaviour.
        HarvestedResource resource = HarvestedResource.getResourceFromObject(source);

        // Check if the source object can be accepted
        final List<HarvestedSource> result = new ArrayList<HarvestedSource>();
        if (resource == null) {
            result.add(new DefaultHarvestedSource(source, false, "Unrecognized source type"));
            return result;
        } else if (source instanceof File && !((File) source).exists() || singleFileList(source)) {
            result.add(new DefaultHarvestedSource(source, false,
                    "Specified file path does not exist"));
            return result;
        }
        // Harvesting of the input source
        resource.harvest(defaultCoverage, source, hints, result, this);
        String coverage = defaultCoverage != null ? defaultCoverage : this.defaultName;
        // rebuild the raster manager for this coverage, as the spatial and dimensional domains
        // have probably changed
        RasterManager rasterManager = rasterManagers.get(coverage);
        rasterManager.initialize(true);

        return result;
    }

    /**
     * Simple method used for checking if the list contains a single object and it is a file
     * 
     * @param source
     * @return
     */
    private boolean singleFileList(Object source) {
        if (source instanceof Collection<?>) {
            Collection<?> collection = ((Collection<?>) source);
            if(collection.size() == 1){
                // Selection of the single file
                File file = (File) collection.iterator().next();
                // Check if it exists
                if (!file.exists()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public List<DimensionDescriptor> getDimensionDescriptors(String coverageName) throws IOException {
            RasterManager manager = getRasterManager(coverageName);
            return manager.getDimensionDescriptors();
    }

    @Override
    public synchronized void delete(boolean deleteData) throws IOException {

        String[] coverageNames = getGridCoverageNames();
        for (String coverageName: coverageNames) {
            removeCoverage(coverageName, deleteData, true);
        }

        // Dispose before deleting to make sure any lock on files or resources is released
        dispose();

        //drop the DB
        granuleCatalog.drop();

        // try to delete data
        if (deleteData) {
            // quick way: delete everything
            final File[] list = parentDirectory.listFiles();
            for (File file: list) {
                FileUtils.deleteQuietly(file);
            }
        } else {
            finalizeCleanup();
        }
    }

    /**
     * Finalize the clean up by removing any file returned by the cleanup filter.
     * Note that some H2 .db files change their name during life cycle. So they won't be stored inside the fileset manager
     */
    private void finalizeCleanup() {
        IOFileFilter filesFilter = Utils.getCleanupFilter();
        Collection<File> files = FileUtils.listFiles(parentDirectory, filesFilter, null);
        for (File file : files) {
            FileUtils.deleteQuietly(file);
        }

    }

    @Override
    public boolean removeCoverage(String coverageName) throws IOException, UnsupportedOperationException {
        return removeCoverage(coverageName, false);
    }
    
    /**
     * This subclass of the {@link ImageMosaicWalker} cycles around a List of files and for each one calls the superclass
     * handleFile() method. For each file is done a check if it really exists, it can be read and it is not a directory.
     * 
     * @author Nicola Lagomarsini, GeoSolutions S.A.S.
     *
     */
    private static class ImageMosaicFileCollectionWalker extends ImageMosaicWalker{

        /** Input File list to walk on*/
        private Collection<File> files;

        public ImageMosaicFileCollectionWalker(ImageMosaicConfigHandler configHandler,
                ImageMosaicEventHandlers eventHandler, Collection<File> files) {
            super(configHandler, eventHandler);

            this.files = files;
        }

        @Override
        public void run() {
            try {
                // Initialization steps
                configHandler.indexingPreamble();
                startTransaction();
                
                // Setting of the Collection size
                setNumFiles(files.size());
                
                // Creation of an Iterator on the input files
                Iterator<File> it = files.iterator();
                
                // Cycle on all the input files
                while (it.hasNext()) {
                    File file = it.next();
                    
                    // Stop the Harvesting if requested
                    if (getStop()){
                        break;
                    }
                    
                    // Check if the File has an absolute path
                    if(checkFile(file)){
                        handleFile(file);
                    }else{
                        // SKIP and log
                        skipFile(file.getAbsolutePath());
                    }
                }
                
                // close transaction
                if (getStop()) {
                    rollbackTransaction();
                } else {
                    commitTransaction();
                }
                
            } catch (IOException e) {
                //Exception Logged
                LOGGER.log(Level.WARNING, e.getMessage(), e);
                try {
                    // Rollback of the Transaction
                    rollbackTransaction();
                } catch (IOException e1) {
                    throw new IllegalStateException(e);
                }
            }finally{
                // close transaction
                try {
                    closeTransaction();
                } catch (Exception e) {
                    final String message = "Unable to close transaction" + e.getLocalizedMessage();
                    if (LOGGER.isLoggable(Level.WARNING)) {
                        LOGGER.log(Level.WARNING, message, e);
                    }
                    // notify listeners
                    eventHandler.fireException(e);
                }

                // close indexing
                try {
                    configHandler.indexingPostamble(!getStop());
                } catch (Exception e) {
                    final String message = "Unable to close indexing" + e.getLocalizedMessage();
                    if (LOGGER.isLoggable(Level.WARNING)) {
                        LOGGER.log(Level.WARNING, message, e);
                    }
                    // notify listeners
                    eventHandler.fireException(e);
                }
            }
        }   
    }

    @Override
    public ResourceInfo getInfo(String coverageName) {
        String name = checkUnspecifiedCoverage(coverageName);
        RasterManager manager = getRasterManager(name);
        String parentLocation = DataUtilities.fileToURL(parentDirectory).toString();
        return new ImageMosaicFileResourceInfo(manager, parentLocation, this.locationAttributeName);
    }

    @Override
    public ServiceInfo getInfo() {
        IOFileFilter filesFilter = Utils.MOSAIC_SUPPORT_FILES_FILTER;
        Collection<File> files = FileUtils.listFiles(parentDirectory, filesFilter, null);
        List<FileGroup> fileGroups = new ArrayList<FileGroup>();
        for (File file: files) {
            fileGroups.add(new FileGroup(file, null, null));
        }
        return new DefaultFileServiceInfo(fileGroups);

    }
}
