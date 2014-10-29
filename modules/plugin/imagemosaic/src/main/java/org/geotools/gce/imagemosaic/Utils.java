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
import it.geosolutions.imageio.pam.PAMDataset.PAMRasterBand;
import it.geosolutions.imageio.pam.PAMDataset.PAMRasterBand.Metadata;
import it.geosolutions.imageio.pam.PAMDataset.PAMRasterBand.Metadata.MDI;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Queue;
import java.util.Set;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.spi.ImageInputStreamSpi;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.stream.ImageInputStream;
import javax.media.jai.BorderExtender;
import javax.media.jai.Histogram;
import javax.media.jai.ImageLayout;
import javax.media.jai.JAI;
import javax.media.jai.RasterFactory;
import javax.media.jai.TileCache;
import javax.media.jai.TileScheduler;
import javax.media.jai.remote.SerializableRenderedImage;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.geotools.data.DataAccessFactory.Param;
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.data.DataUtilities;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.Hints;
import org.geotools.factory.Hints.Key;
import org.geotools.filter.visitor.DefaultFilterVisitor;
import org.geotools.gce.imagemosaic.catalog.CatalogConfigurationBean;
import org.geotools.gce.imagemosaic.catalog.index.Indexer;
import org.geotools.gce.imagemosaic.catalog.index.IndexerUtils;
import org.geotools.gce.imagemosaic.catalog.index.ObjectFactory;
import org.geotools.gce.imagemosaic.catalog.index.ParametersType.Parameter;
import org.geotools.gce.imagemosaic.catalogbuilder.CatalogBuilderConfiguration;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.image.io.ImageIOExt;
import org.geotools.referencing.operation.matrix.XAffineTransform;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Errors;
import org.geotools.util.Converters;
import org.geotools.util.Range;
import org.geotools.util.Utilities;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.spatial.BBOX;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;

/**
 * Sparse utilities for the various mosaic classes. I use them to extract
 * complex code from other places.
 * 
 * @author Simone Giannecchini, GeoSolutions S.A.S.
 * 
 *
 *
 * @source $URL$
 */
public class Utils {
    
    public final static FilterFactory2 FF = CommonFactoryFinder.getFilterFactory2();
    
    final private static String DATABASE_KEY = "database";

    final private static String MVCC_KEY = "MVCC";

    final private static double RESOLUTION_TOLERANCE_FACTOR = 1E-2;

    public final static Key EXCLUDE_MOSAIC = new Key(Boolean.class);

    public final static Key CHECK_AUXILIARY_METADATA = new Key(Boolean.class);

    public final static Key AUXILIARY_FILES_PATH = new Key(String.class);

    public final static Key PARENT_DIR = new Key(String.class);

    public final static Key MOSAIC_READER = new Key(ImageMosaicReader.class);

    public static final String RANGE_SPLITTER_CHAR = ";";

    public final static String INDEXER_PROPERTIES = "indexer.properties";

    public final static String INDEXER_XML = "indexer.xml";

    private static JAXBContext CONTEXT = null;
    
    public final static String PAM_DATASET = "PamDataset";

    static final String DEFAULT = "default";

    /** EHCache instance to cache histograms */ 
    private static Cache ehcache;

    /** RGB to GRAY coefficients (for Luminance computation) */
    public final static double RGB_TO_GRAY_MATRIX [][]= {{ 0.114, 0.587, 0.299, 0 }};

    /** 
     * Flag indicating whether to compute optimized crop ops (instead of standard
     * mosaicking op) when possible (As an instance when mosaicking a single granule) 
     */
    final static boolean OPTIMIZE_CROP; 

    /**
     * Logger.
     */
    private final static Logger LOGGER = org.geotools.util.logging.Logging
                    .getLogger(Utils.class.toString());

    static {
        final String prop = System.getProperty("org.geotools.imagemosaic.optimizecrop");
        if (prop != null && prop.equalsIgnoreCase("FALSE")){
            OPTIMIZE_CROP = false;
        } else {
            OPTIMIZE_CROP = true;
        }

        try {
            CONTEXT = JAXBContext.newInstance("org.geotools.gce.imagemosaic.catalog.index");
        } catch (JAXBException e) {
            LOGGER.log(Level.FINER, e.getMessage(), e);
        }
        CLEANUP_FILTER = initCleanUpFilter(); 
    }
    
    public static class Prop {
        public final static String LOCATION_ATTRIBUTE = "LocationAttribute";
        public final static String ENVELOPE2D = "Envelope2D";
        public final static String LEVELS_NUM = "LevelsNum";
        public final static String LEVELS = "Levels";
        public final static String SUGGESTED_SPI = "SuggestedSPI";
        public final static String EXP_RGB = "ExpandToRGB";
        public final static String ABSOLUTE_PATH = "AbsolutePath";
        public final static String AUXILIARY_FILE = "AuxiliaryFile";
        public final static String NAME = "Name";
        public final static String INDEX_NAME = "Name";
        public final static String FOOTPRINT_MANAGEMENT = "FootprintManagement";
        public final static String HETEROGENEOUS = "Heterogeneous";
        public static final String TIME_ATTRIBUTE = "TimeAttribute";
        public static final String ELEVATION_ATTRIBUTE = "ElevationAttribute";
        public static final String ADDITIONAL_DOMAIN_ATTRIBUTES = "AdditionalDomainAttributes";

        /**
         * Sets if the target schema should be used to locate granules (default is FALSE)<br/>
         * {@value TRUE|FALSE}
         */
        public final static String USE_EXISTING_SCHEMA = "UseExistingSchema"; 
        public final static String TYPENAME = "TypeName";
        public final static String PATH_TYPE = "PathType";
        public final static String PARENT_LOCATION = "ParentLocation";
        public final static String ROOT_MOSAIC_DIR = "RootMosaicDirectory";
        public final static String INDEXING_DIRECTORIES = "IndexingDirectories";
        public final static String HARVEST_DIRECTORY = "HarvestingDirectory";
        public final static String CAN_BE_EMPTY = "CanBeEmpty";

        /** Sets if the reader should look for auxiliary metadata PAM files */
        public static final String CHECK_AUXILIARY_METADATA = "CheckAuxiliaryMetadata";

        //Indexer Properties specific properties
        public  static final String RECURSIVE = "Recursive";
        public static final String WILDCARD = "Wildcard";
        public static final String SCHEMA = "Schema";
        public static final String RESOLUTION_LEVELS = "ResolutionLevels";
        public static final String PROPERTY_COLLECTORS = "PropertyCollectors";
        public final static String CACHING= "Caching";
    }
        /**
     * Extracts a bbox from a filter in case there is at least one.
     * 
     * I am simply looking for the BBOX filter but I am sure we could
     * use other filters as well. I will leave this as a todo for the moment.
     * 
     * @author Simone Giannecchini, GeoSolutions SAS.
     * @todo TODO use other spatial filters as well
     */
    public static class BBOXFilterExtractor extends DefaultFilterVisitor{
    
    	public ReferencedEnvelope getBBox() {
    		return bbox;
    	}
    	private ReferencedEnvelope bbox;
    	@Override
    	public Object visit(BBOX filter, Object data) {
    		final ReferencedEnvelope bbox= ReferencedEnvelope.reference(filter.getBounds());
    		if(this.bbox!=null){
    		    this.bbox=(ReferencedEnvelope) this.bbox.intersection(bbox);
    		}
    		else{
    		    this.bbox=bbox;
    		}
    		return super.visit(filter, data);
    	}
    	
    }

	/**
	 * Default wildcard for creating mosaics.
	 */
	public static final String DEFAULT_WILCARD = "*.*";

	/**
	 * Default path behavior with respect to absolute paths.
	 */
	public static final boolean DEFAULT_PATH_BEHAVIOR = false;

	/**
	 * Default behavior with respect to index caching.
	 */
	private static final boolean DEFAULT_CACHING_BEHAVIOR = false;
	
	
	/**
	 * Creates a mosaic for the provided input parameters.
	 * 
	 * @param location
	 *            path to the directory where to gather the elements for the
	 *            mosaic.
	 * @param indexName
	 *            name to give to this mosaic
	 * @param wildcard
	 *            wildcard to use for walking through files. We are using
	 *            commonsIO for this task
	 * @param absolutePath
	 * 			  tells the catalogue builder to use absolute paths.
	 * @param hints hints to control reader instantiations
	 * @return <code>true</code> if everything is right, <code>false</code>if
	 *         something bad happens, in which case the reason should be logged
	 *         to the logger.
	 */
	static boolean createMosaic(
			final String location, 
			final String indexName,
			final String wildcard, 
			final boolean absolutePath, 
			final Hints hints) {

		// create a mosaic index builder and set the relevant elements
		final CatalogBuilderConfiguration configuration = new CatalogBuilderConfiguration();
		configuration.setHints(hints);// retain hints as this may contain an instance of an ImageMosaicReader
		List<Parameter> parameterList = configuration.getIndexer().getParameters().getParameter();
               
               IndexerUtils.setParam(parameterList, Prop.ABSOLUTE_PATH, Boolean.toString(absolutePath));
               IndexerUtils.setParam(parameterList, Prop.ROOT_MOSAIC_DIR, location);
               IndexerUtils.setParam(parameterList, Prop.INDEX_NAME, indexName);
               IndexerUtils.setParam(parameterList, Prop.WILDCARD, wildcard);
               IndexerUtils.setParam(parameterList, Prop.INDEXING_DIRECTORIES, location);

        // create the builder
        // final ImageMosaicWalker catalogBuilder = new ImageMosaicWalker(configuration);
        final ImageMosaicEventHandlers eventHandler = new ImageMosaicEventHandlers();
        final ImageMosaicConfigHandler catalogHandler = new ImageMosaicConfigHandler(configuration,
                eventHandler);
        final ImageMosaicWalker walker;
        if (catalogHandler.isUseExistingSchema()) {
            walker = new ImageMosaicDatastoreWalker(catalogHandler, eventHandler);
        } else {
            walker = new ImageMosaicDirectoryWalker(catalogHandler, eventHandler);
        }

		// this is going to help us with catching exceptions and logging them
		final Queue<Throwable> exceptions = new LinkedList<Throwable>();
		try {

			final ImageMosaicEventHandlers.ProcessingEventListener listener = new ImageMosaicEventHandlers.ProcessingEventListener() {

				@Override
				public void exceptionOccurred(ImageMosaicEventHandlers.ExceptionEvent event) {
					final Throwable t = event.getException();
					exceptions.add(t);
					if (LOGGER.isLoggable(Level.SEVERE))
						LOGGER.log(Level.SEVERE, t.getLocalizedMessage(), t);

				}

				@Override
				public void getNotification(ImageMosaicEventHandlers.ProcessingEvent event) {
					if (LOGGER.isLoggable(Level.FINE))
						LOGGER.fine(event.getMessage());

				}

			};
			eventHandler.addProcessingEventListener(listener);
			walker.run();
		} catch (Throwable e) {
			LOGGER.log(Level.SEVERE, "Unable to build mosaic", e);
			return false;
		} finally {
			catalogHandler.dispose();
		}

		// check that nothing bad happened
		if (exceptions.size() > 0)
			return false;
		return true;
	}

	// Make additional filters pluggable
    private static IOFileFilter initCleanUpFilter() {
        IOFileFilter filesFilter = FileFilterUtils.or(
                FileFilterUtils.suffixFileFilter("properties"),
                FileFilterUtils.suffixFileFilter("shp"), FileFilterUtils.suffixFileFilter("dbf"),
                FileFilterUtils.suffixFileFilter("sbn"), FileFilterUtils.suffixFileFilter("sbx"),
                FileFilterUtils.suffixFileFilter("shx"), FileFilterUtils.suffixFileFilter("qix"),
                FileFilterUtils.suffixFileFilter("lyr"), FileFilterUtils.suffixFileFilter("prj"),
                FileFilterUtils.suffixFileFilter("ncx"), FileFilterUtils.suffixFileFilter("gbx9"),
                FileFilterUtils.nameFileFilter("error.txt"),
                FileFilterUtils.nameFileFilter("_metadata"),
                FileFilterUtils.suffixFileFilter("sample_image"),
                FileFilterUtils.nameFileFilter("error.txt.lck"),
                FileFilterUtils.suffixFileFilter("xml"),
                FileFilterUtils.suffixFileFilter("db"));
        return filesFilter;
    }

    public static String getMessageFromException(Exception exception) {
		if (exception.getLocalizedMessage() != null)
			return exception.getLocalizedMessage();
		else
			return exception.getMessage();
	}

//	static URL checkSource(Object source) throws MalformedURLException,
//			DataSourceException {
//		return checkSource(source, null);
//	}

	

	static MosaicConfigurationBean loadMosaicProperties(final URL sourceURL,
                final String defaultLocationAttribute) {
	    return loadMosaicProperties(sourceURL, defaultLocationAttribute, null);
	}
	
	static MosaicConfigurationBean loadMosaicProperties(
	        final URL sourceURL,
		final String defaultLocationAttribute, 
		final Set<String> ignorePropertiesSet) {
		
		if(LOGGER.isLoggable(Level.FINE)){
			LOGGER.log(Level.FINE,"Trying to load properties file from URL:"+sourceURL);
		}
		
		// ret value
		final MosaicConfigurationBean retValue = new MosaicConfigurationBean();
		final CatalogConfigurationBean catalogConfigurationBean = new CatalogConfigurationBean();
		retValue.setCatalogConfigurationBean(catalogConfigurationBean);
		final boolean ignoreSome = ignorePropertiesSet != null && !ignorePropertiesSet.isEmpty();

        //
        // load the properties file
        //
        URL propsURL = sourceURL;
        if (!sourceURL.toExternalForm().endsWith(".properties")) {
            propsURL = DataUtilities.changeUrlExt(sourceURL, "properties");
            if (propsURL.getProtocol().equals("file")) {
                final File sourceFile = DataUtilities.urlToFile(propsURL);
                if (!sourceFile.exists()) {
                    if (LOGGER.isLoggable(Level.INFO)) {
                        LOGGER.info("properties file doesn't exist");
                    }
                    return null;
                }
            }
        }
		
		final Properties properties = loadPropertiesFromURL(propsURL);
		if (properties == null) {
			if (LOGGER.isLoggable(Level.INFO))
				LOGGER.info("Unable to load mosaic properties file");
			return null;
		}

		String[] pairs = null;
		String pair[] = null;
		
		//
		// imposed bbox is optional
		//              
		if (!ignoreSome || !ignorePropertiesSet.contains(Prop.ENVELOPE2D)) {
			String bboxString = properties.getProperty(Prop.ENVELOPE2D, null);
			if(bboxString!=null){
				bboxString=bboxString.trim();
				try{
					ReferencedEnvelope bbox = parseEnvelope(bboxString);
					if(bbox!=null)
						retValue.setEnvelope(bbox);
					else
						if (LOGGER.isLoggable(Level.INFO))
							LOGGER.info("Cannot parse imposed bbox.");
				}catch (Exception e) {
					if (LOGGER.isLoggable(Level.INFO))
						LOGGER.log(Level.INFO,"Cannot parse imposed bbox.",e);
				}
			}
				
		}
		
		if (!ignoreSome
                        || !ignorePropertiesSet.contains(Prop.AUXILIARY_FILE)) {
                    retValue.setAuxiliaryFilePath(properties.getProperty(Prop.AUXILIARY_FILE));
        }

                if (!ignoreSome || !ignorePropertiesSet.contains(Prop.CHECK_AUXILIARY_METADATA)) {
                    final boolean checkAuxiliaryMetadata = Boolean.valueOf(properties.getProperty(
                            Prop.CHECK_AUXILIARY_METADATA, "false").trim());
                    retValue.setCheckAuxiliaryMetadata(checkAuxiliaryMetadata);
                }
                
		
		//
		// resolutions levels
		//              
		if (!ignoreSome || !ignorePropertiesSet.contains(Prop.LEVELS)) {
			int levelsNumber = Integer.parseInt(properties.getProperty(Prop.LEVELS_NUM, "1").trim());
			retValue.setLevelsNum(levelsNumber);
			if (!properties.containsKey(Prop.LEVELS)) {
				if (LOGGER.isLoggable(Level.INFO))
					LOGGER.info("Required key Levels not found.");
				return null;
			}
			final String levels = properties.getProperty(Prop.LEVELS).trim();
			pairs = levels.split(" ");
			if (pairs == null || pairs.length != levelsNumber) {
				if (LOGGER.isLoggable(Level.INFO))
					LOGGER.info("Levels number is different from the provided number of levels resoltion.");
				return null;
			}
			final double[][] resolutions = new double[levelsNumber][2];
			for (int i = 0; i < levelsNumber; i++) {
				pair = pairs[i].split(",");
				if (pair == null || pair.length != 2) {
					if (LOGGER.isLoggable(Level.INFO))
						LOGGER.info("OverviewLevel number is different from the provided number of levels resoltion.");
					return null;
				}
				resolutions[i][0] = Double.parseDouble(pair[0]);
				resolutions[i][1] = Double.parseDouble(pair[1]);
			}
			retValue.setLevels(resolutions);
		}

		//
		// typename, is mandatory when we don't use shapeiles
		//              
		if (!ignoreSome || !ignorePropertiesSet.contains(Prop.TYPENAME)) {
			String typeName = properties.getProperty(Prop.TYPENAME, null);
			catalogConfigurationBean.setTypeName(typeName);
		}

		//
		// suggested spi is optional
		//
		if (!ignoreSome || !ignorePropertiesSet.contains(Prop.SUGGESTED_SPI)) {
			if (properties.containsKey(Prop.SUGGESTED_SPI)) {
				final String suggestedSPI = properties.getProperty(
						Prop.SUGGESTED_SPI).trim();
				catalogConfigurationBean.setSuggestedSPI(suggestedSPI);
			}
		}

		//
		// time attribute is optional
		//
		if (properties.containsKey(Prop.TIME_ATTRIBUTE)) {
		        final String timeAttribute = properties.getProperty("TimeAttribute").trim();
			retValue.setTimeAttribute(timeAttribute);
		}

		//
		// elevation attribute is optional
		//
		if (properties.containsKey(Prop.ELEVATION_ATTRIBUTE)) {
		        final String elevationAttribute = properties.getProperty(Prop.ELEVATION_ATTRIBUTE).trim();
			retValue.setElevationAttribute(elevationAttribute);
		}

		//
                // additional domain attribute is optional
                //
                if (properties.containsKey(Prop.ADDITIONAL_DOMAIN_ATTRIBUTES)) {
                        final String additionalDomainAttributes = properties.getProperty(Prop.ADDITIONAL_DOMAIN_ATTRIBUTES).trim();
                        retValue.setAdditionalDomainAttributes(additionalDomainAttributes);
                }

		//
		// caching
		//
		if (properties.containsKey(Prop.CACHING)) {
			String caching = properties.getProperty(Prop.CACHING).trim();
			try {
			    catalogConfigurationBean.setCaching(Boolean.valueOf(caching));
			} catch (Throwable e) {
			    catalogConfigurationBean.setCaching(Boolean.valueOf(Utils.DEFAULT_CACHING_BEHAVIOR));
			}
		}

		//
		// name is not optional
		//
		if (!ignoreSome || !ignorePropertiesSet.contains(Prop.NAME)){
                    if(!properties.containsKey(Prop.NAME)) {
                            if(LOGGER.isLoggable(Level.SEVERE))
                                    LOGGER.severe("Required key Name not found.");          
                            return null;
                    }                       
                    String coverageName = properties.getProperty(Prop.NAME).trim();
                    retValue.setName(coverageName);
                }

		// need a color expansion?
		// this is a newly added property we have to be ready to the case where
		// we do not find it.
		if (!ignoreSome || !ignorePropertiesSet.contains(Prop.EXP_RGB)) {
			final boolean expandMe = Boolean.valueOf(properties.getProperty(
					Prop.EXP_RGB, "false").trim());
			retValue.setExpandToRGB(expandMe);
		}
		
		// 
		// Is heterogeneous granules mosaic
		//
                if (!ignoreSome || !ignorePropertiesSet.contains(Prop.HETEROGENEOUS)) {
                    final boolean heterogeneous = Boolean.valueOf(properties.getProperty(
                            Prop.HETEROGENEOUS, "false").trim());
                    catalogConfigurationBean.setHeterogeneous(heterogeneous);
                }

		//
		// Absolute or relative path
		//
		if (!ignoreSome || !ignorePropertiesSet.contains(Prop.ABSOLUTE_PATH)) {
			final boolean absolutePath = Boolean.valueOf(properties
					.getProperty(Prop.ABSOLUTE_PATH,
							Boolean.toString(Utils.DEFAULT_PATH_BEHAVIOR))
					.trim());
			catalogConfigurationBean.setAbsolutePath(absolutePath);
		}

		//
		// Footprint management
		//
		if (!ignoreSome
				|| !ignorePropertiesSet.contains(Prop.FOOTPRINT_MANAGEMENT)) {
			final boolean footprintManagement = Boolean.valueOf(properties
					.getProperty(Prop.FOOTPRINT_MANAGEMENT, "false").trim());
			retValue.setFootprintManagement(footprintManagement);
		}

		//
		// location
		//  
		if (!ignoreSome
				|| !ignorePropertiesSet.contains(Prop.LOCATION_ATTRIBUTE)) {
		    catalogConfigurationBean.setLocationAttribute(properties.getProperty(
					Prop.LOCATION_ATTRIBUTE, Utils.DEFAULT_LOCATION_ATTRIBUTE).trim());
		}
		
		// return value
		return retValue;
	}

	/**
	 * Parses a bbox in the form of MIX,MINY MAXX,MAXY
	 * @param bboxString the string to parse the bbox from
	 * @return a {@link ReferencedEnvelope} with the parse bbox or null
	 */
	public static ReferencedEnvelope parseEnvelope(final String bboxString) {
		if(bboxString==null||bboxString.length()==0)
			return null;
		
		final String[] pairs = bboxString.split(" ");
		if (pairs != null &&pairs.length == 2) {
		
			String[] pair1 = pairs[0].split(",");
			String[] pair2 = pairs[1].split(",");
			if(pair1!=null&&pair1.length==2&&pair2!=null&&pair2.length==2)
				return new ReferencedEnvelope(
						 Double.parseDouble(pair1[0]),
						 Double.parseDouble(pair2[0]),
						 Double.parseDouble(pair1[1]),
						 Double.parseDouble(pair2[1]),
						 null);
	
		}
		// something bad happened
		return null;
	}

	public static Properties loadPropertiesFromURL(URL propsURL) {
		final Properties properties = new Properties();
		InputStream stream = null;
		InputStream openStream = null;
		try {
			openStream = propsURL.openStream();
			stream = new BufferedInputStream(openStream);
			properties.load(stream);
		} catch (FileNotFoundException e) {
			if (LOGGER.isLoggable(Level.SEVERE))
				LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
			return null;
		} catch (IOException e) {
			if (LOGGER.isLoggable(Level.SEVERE))
				LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
			return null;
		} finally {

			if (stream != null)
				IOUtils.closeQuietly(stream);

			if (openStream != null)
				IOUtils.closeQuietly(openStream);

		}
		return properties;
	}

	public static IOFileFilter excludeFilters(final IOFileFilter inputFilter,
			IOFileFilter... filters) {
		IOFileFilter retFilter = inputFilter;
		for (IOFileFilter filter : filters) {
			retFilter = FileFilterUtils.and(retFilter,
					FileFilterUtils.notFileFilter(filter));
		}
		return retFilter;
	}

	/**
	 * Look for an {@link ImageReader} instance that is able to read the
	 * provided {@link ImageInputStream}, which must be non null.
	 * 
	 * <p>
	 * In case no reader is found, <code>null</code> is returned.
	 * 
	 * @param inStream
	 *            an instance of {@link ImageInputStream} for which we need to
	 *            find a suitable {@link ImageReader}.
	 * @return a suitable instance of {@link ImageReader} or <code>null</code>
	 *         if one cannot be found.
	 */
	static ImageReader getReader(final ImageInputStream inStream) {
		Utilities.ensureNonNull("inStream", inStream);
		// get a reader
		inStream.mark();
		final Iterator<ImageReader> readersIt = ImageIO
				.getImageReaders(inStream);
		if (!readersIt.hasNext()) {
			return null;
		}
		return readersIt.next();
	}

	/**
	 * Retrieves the dimensions of the {@link RenderedImage} at index
	 * <code>imageIndex</code> for the provided {@link ImageReader} and
	 * {@link ImageInputStream}.
	 * 
	 * <p>
	 * Notice that none of the input parameters can be <code>null</code> or a
	 * {@link NullPointerException} will be thrown. Morevoer the
	 * <code>imageIndex</code> cannot be negative or an
	 * {@link IllegalArgumentException} will be thrown.
	 * 
	 * @param imageIndex
	 *            the index of the image to get the dimensions for.
	 * @param inStream
	 *            the {@link ImageInputStream} to use as an input
	 * @param reader
	 *            the {@link ImageReader} to decode the image dimensions.
	 * @return a {@link Rectangle} that contains the dimensions for the image at
	 *         index <code>imageIndex</code>
	 * @throws IOException
	 *             in case the {@link ImageReader} or the
	 *             {@link ImageInputStream} fail.
	 */
	static Rectangle getDimension(final int imageIndex,final ImageReader reader)
			throws IOException {
		Utilities.ensureNonNull("reader", reader);
		if (imageIndex < 0)
			throw new IllegalArgumentException(Errors.format(
					ErrorKeys.INDEX_OUT_OF_BOUNDS_$1, imageIndex));
		return new Rectangle(0, 0, reader.getWidth(imageIndex), reader
				.getHeight(imageIndex));
	}

	/**
	 * Default priority for the underlying {@link Thread}.
	 */
	public static final int DEFAULT_PRIORITY = Thread.NORM_PRIORITY;
	/**
	 * Default location attribute name.
	 */
	public static final String DEFAULT_LOCATION_ATTRIBUTE = "location";

	public static final String DEFAULT_INDEX_NAME = "index";

	/**
	 * Checks that a {@link File} is a real file, exists and is readable.
	 * 
	 * @param file
	 *            the {@link File} instance to check. Must not be null.
	 * 
	 * @return <code>true</code> in case the file is a real file, exists and is
	 *         readable; <code>false </code> otherwise.
	 */
	public static boolean checkFileReadable(final File file) {
		if (LOGGER.isLoggable(Level.FINE)) {
			final String message = getFileInfo(file);
			LOGGER.fine(message);
		}
		if (!file.exists() || !file.canRead() || !file.isFile())
			return false;
		return true;
	}

    /**
     * Creates a human readable message that describe the provided {@link File} object in terms of its properties.
     * 
     * <p>
     * Useful for creating meaningful log messages.
     * 
     * @param file the {@link File} object to create a descriptive message for
     * @return a {@link String} containing a descriptive message about the provided {@link File}.
     * 
     */
    public static String getFileInfo(final File file) {
        final StringBuilder builder = new StringBuilder();
        builder.append("Checking file:").append(
        		FilenameUtils.getFullPath(file.getAbsolutePath())).append(
        		"\n");
        builder.append("isHidden:").append(file.isHidden()).append("\n");
        builder.append("exists:").append(file.exists()).append("\n");
        builder.append("isFile").append(file.isFile()).append("\n");
        builder.append("canRead:").append(file.canRead()).append("\n");        
        builder.append("canWrite").append(file.canWrite()).append("\n");
        builder.append("canExecute:").append(file.canExecute()).append("\n");
        builder.append("isAbsolute:").append(file.isAbsolute()).append("\n");
        builder.append("lastModified:").append(file.lastModified()).append("\n");
        builder.append("length:").append(file.length());
        final String message=builder.toString();
        return message;
    }

	/**
	 * @param testingDirectory
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IOException
	 */
	public static String checkDirectory(String testingDirectory, boolean writable)
			throws IllegalArgumentException {
	    
		File inDir = new File(testingDirectory);
		boolean failure= !inDir.exists()||!inDir.isDirectory()||inDir.isHidden()||!inDir.canRead();
		if(writable){
		    failure|=!inDir.canWrite();
		}
		if (failure) {
		        String message="Unable to create the mosaic\n"+
                        "location is:"+testingDirectory+"\n"+
                        "location exists:"+inDir.exists()+"\n"+
                        "location is a directory:"+inDir.isDirectory()+"\n"+
                        "location is writable:"+inDir.canWrite()+"\n"+
                        "location is readable:"+inDir.canRead()+"\n"+
                        "location is hidden:"+inDir.isHidden()+"\n";
			LOGGER.severe(message);
			throw new IllegalArgumentException(message);
		}
		try {
			testingDirectory = inDir.getCanonicalPath();
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
		testingDirectory = FilenameUtils.normalize(testingDirectory);
		if (!testingDirectory.endsWith(File.separator))
			testingDirectory = testingDirectory + File.separator;
		// test to see if things are still good
		inDir = new File(testingDirectory);
		failure= !inDir.exists()||!inDir.isDirectory()||inDir.isHidden()||!inDir.canRead();
                if(writable){
                    failure|=!inDir.canWrite();
                }
                if (failure) {
                        String message="Unable to create the mosaic\n"+
                        "location is:"+testingDirectory+"\n"+
                        "location exists:"+inDir.exists()+"\n"+
                        "location is a directory:"+inDir.isDirectory()+"\n"+
                        "location is writable:"+inDir.canWrite()+"\n"+
                        "location is readable:"+inDir.canRead()+"\n"+
                        "location is hidden:"+inDir.isHidden()+"\n";
                        LOGGER.severe(message);
                        throw new IllegalArgumentException(message);
                }
		return testingDirectory;
	}

	static boolean checkURLReadable(URL url) {
		try {
			url.openStream().close();
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public static final DataStoreFactorySpi SHAPE_SPI = new ShapefileDataStoreFactory();

	static final String DIRECT_KAKADU_PLUGIN = "it.geosolutions.imageio.plugins.jp2k.JP2KKakaduImageReader";

	public static final boolean DEFAULT_RECURSION_BEHAVIOR = true;

	/**
	 * 
	 * @param datastoreProperties
	 * @return
	 * @throws IOException
	 */
	public static Map<String, Serializable> createDataStoreParamsFromPropertiesFile(
			final URL datastoreProperties)
			throws IOException {
		// read the properties file
		Properties properties = loadPropertiesFromURL(datastoreProperties);
		if (properties == null)
			return null;

		// SPI
		final String SPIClass = properties.getProperty("SPI");
		try {
			// create a datastore as instructed
			final DataStoreFactorySpi spi = (DataStoreFactorySpi) Class.forName(SPIClass).newInstance();
			return createDataStoreParamsFromPropertiesFile(properties, spi);
		} catch (Exception e) {
			final IOException ioe = new IOException();
			throw (IOException) ioe.initCause(e);
		}
	}

	/**
	 * Store a sample image from which we can derive the default SM and CM
	 * 
	 * @param sampleImageFile
	 *            where we should store the image
	 * @param defaultSM
	 *            the {@link SampleModel} for the sample image.
	 * @param defaultCM
	 *            the {@link ColorModel} for the sample image.
	 * @throws IOException
	 *             in case something bad occurs during writing.
	 */
	public static void storeSampleImage(final File sampleImageFile,
			final SampleModel defaultSM, final ColorModel defaultCM)
			throws IOException {
		// create 1X1 image
		final SampleModel sm = defaultSM.createCompatibleSampleModel(1, 1);
		final WritableRaster raster = RasterFactory.createWritableRaster(sm,
				null);
		final BufferedImage sampleImage = new BufferedImage(defaultCM, raster,
				false, null);

		// serialize it
		OutputStream outStream = null;
		ObjectOutputStream ooStream = null;
		SerializableRenderedImage sri = null;
		try {
			outStream = new BufferedOutputStream(new FileOutputStream(
					sampleImageFile));
			ooStream = new ObjectOutputStream(outStream);
			sri = new SerializableRenderedImage(sampleImage, true);
			ooStream.writeObject(sri);
		} finally {
			try {
				if (ooStream != null)
					ooStream.close();
			} catch (Throwable e) {
				IOUtils.closeQuietly(ooStream);
			}
			try {
				if (outStream != null)
					outStream.close();
			} catch (Throwable e) {
				IOUtils.closeQuietly(outStream);
			}
			try {
                            if (sri != null)
                                    sri.dispose();
                        } catch (Throwable e) {
                        }
		}
	}

	/**
	 * Load a sample image from which we can take the sample model and color
	 * model to be used to fill holes in responses.
	 * 
	 * @param sampleImageFile
	 *            the path to sample image.
	 * @return a sample image from which we can take the sample model and color
	 *         model to be used to fill holes in responses.
	 */
	public static RenderedImage loadSampleImage(final File sampleImageFile) {
		// serialize it
		InputStream inStream = null;
		ObjectInputStream oiStream = null;
		try {

			// do we have the sample image??
			if (Utils.checkFileReadable(sampleImageFile)) {
				inStream = new BufferedInputStream(new FileInputStream(
						sampleImageFile));
				oiStream = new ObjectInputStream(inStream);

				// load the image
				return (RenderedImage) oiStream.readObject();

			} else {
				if (LOGGER.isLoggable(Level.WARNING))
					LOGGER.warning("Unable to find sample image for path "
							+ sampleImageFile);
				return null;
			}
		} catch (FileNotFoundException e) {
			if (LOGGER.isLoggable(Level.WARNING))
				LOGGER.log(Level.WARNING, e.getLocalizedMessage(), e);
			return null;
		} catch (IOException e) {
			if (LOGGER.isLoggable(Level.WARNING))
				LOGGER.log(Level.WARNING, e.getLocalizedMessage(), e);
			return null;
		} catch (ClassNotFoundException e) {
			if (LOGGER.isLoggable(Level.WARNING))
				LOGGER.log(Level.WARNING, e.getLocalizedMessage(), e);
			return null;
		} finally {
			try {
				if (inStream != null)
					inStream.close();
			} catch (Throwable e) {

				if (LOGGER.isLoggable(Level.FINE))
					LOGGER.log(Level.FINE, e.getLocalizedMessage(), e);
			}
			try {
				if (oiStream != null)
					oiStream.close();
			} catch (Throwable e) {

				if (LOGGER.isLoggable(Level.FINE))
					LOGGER.log(Level.FINE, e.getLocalizedMessage(), e);
			}
		}
	}

	/**
	 * A transparent color for missing data.
	 */
	static final Color TRANSPARENT = new Color(0,0,0,0);
        
	// final static Boolean IGNORE_FOOTPRINT = Boolean.getBoolean("org.geotools.footprint.ignore");
	
    public static final boolean DEFAULT_FOOTPRINT_MANAGEMENT = true;
	
	public static final boolean DEFAULT_CONFIGURATION_CACHING = false;

            public static Map<String, Serializable> createDataStoreParamsFromPropertiesFile(
			Properties properties, DataStoreFactorySpi spi) throws IOException {
		// get the params
		final Map<String, Serializable> params = new HashMap<String, Serializable>();
		final Param[] paramsInfo = spi.getParametersInfo();
		for (Param p : paramsInfo) {
			// search for this param and set the value if found
			if (properties.containsKey(p.key)){
			    params.put(p.key, (Serializable) Converters.convert(properties.getProperty(p.key), p.type));
			}
			else if (p.required && p.sample == null)
				throw new IOException("Required parameter missing: "+ p.toString());
		}
		
		return params;
	}
            
    public static Map<String, Serializable> filterDataStoreParams(
            Properties properties, DataStoreFactorySpi spi) throws IOException {
        // get the params
        final Map<String, Serializable> params = new HashMap<String, Serializable>();
        final Param[] paramsInfo = spi.getParametersInfo();
        for (Param p : paramsInfo) {
            // search for this param and set the value if found
            if (properties.containsKey(p.key)) {
                params.put(p.key,
                        (Serializable) Converters.convert(properties.get(p.key), p.type));
            } else if (p.required && p.sample == null)
                throw new IOException("Required parameter missing: " + p.toString());
        }

        return params;
    }

    static URL checkSource(Object source, Hints hints) {
        URL sourceURL = null;
        File sourceFile = null;

        //
        // Check source
        //
        // if it is a URL or a String let's try to see if we can get a file to
        // check if we have to build the index
        if (source instanceof File) {
                sourceFile = (File) source;
                sourceURL = DataUtilities.fileToURL(sourceFile);
        } else if (source instanceof URL) {
            sourceURL = (URL) source;
            if (sourceURL.getProtocol().equals("file")) {
                sourceFile = DataUtilities.urlToFile(sourceURL);
            }
        } else if (source instanceof String) {
                // is it a File?
                final String tempSource = (String) source;
                File tempFile = new File(tempSource);
                if (!tempFile.exists()) {
                        // is it a URL
                        try {
                                sourceURL = new URL(tempSource);
                                source = DataUtilities.urlToFile(sourceURL);
                        } catch (MalformedURLException e) {
                                sourceURL = null;
                                source = null;
                        }
                } else {
                        sourceURL = DataUtilities.fileToURL(tempFile);

                        // so that we can do our magic here below
                        sourceFile = tempFile;
                }
        }
        // //
        //
        // at this point we have tried to convert the thing to a File as hard as
        // we could, let's see what we can do
        //
        // //
        if (sourceFile != null) {
                if (!sourceFile.isDirectory())
                        // real file, can only be a shapefile at this stage or a
                        // datastore.properties file
                        sourceURL = DataUtilities.fileToURL((File) sourceFile);
                else {
                        // it's a DIRECTORY, let's look for a possible properties files
                        // that we want to load
                        final String locationPath = sourceFile.getAbsolutePath();
                        final String defaultIndexName = getDefaultIndexName(locationPath);
                        boolean datastoreFound = false;
                        boolean buildMosaic = false;

                        //
                        // do we have a datastore properties file? It will preempt on
                        // the shapefile
                        // TODO: Refactor these checks once we integrate datastore on indexer.xml
                        //
                        File dataStoreProperties = new File(locationPath,"datastore.properties");
//                        File emptyFile = new File(locationPath,"empty");

                        // this can be used to look for properties files that do NOT
                        // define a datastore
                        final File[] properties = sourceFile
                                        .listFiles((FilenameFilter) FileFilterUtils
                                                        .and(
                                                                        FileFilterUtils
                                                                                        .notFileFilter(FileFilterUtils
                                                                                                        .nameFileFilter("datastore.properties")),
                                                                        FileFilterUtils
                                                                                        .makeFileOnly(FileFilterUtils
                                                                                                        .suffixFileFilter(".properties"))));

                        // do we have a valid datastore + mosaic properties pair?
                        if (Utils.checkFileReadable(dataStoreProperties)) {
                                // we have a datastore.properties file
                                datastoreFound = true;

                                // check the first valid mosaic properties
                                boolean found = false;
                                for (File propFile : properties)
                                        if (Utils.checkFileReadable(propFile)) {
                                                // load it
                                                if (null != Utils.loadMosaicProperties(DataUtilities.fileToURL(propFile),Utils.DEFAULT_LOCATION_ATTRIBUTE)) {
                                                        found = true;
                                                        break;
                                                }
                                        }

                                // we did not find any good candidate for mosaic.properties
                                // file, this will signal it
                                if (!found)
                                        buildMosaic = true;

                        } else
                        {
                                // we did not find any good candidate for mosaic.properties
                                // file, this will signal it
                                buildMosaic = true;
                                datastoreFound = false;
                        }

                        //
                        // now let's try with shapefile and properties couple
                        //
                        File shapeFile = null;
                        if (!datastoreFound) {
                                for (File propFile : properties) {

                                        // load properties
                                        if (null == Utils.loadMosaicProperties(DataUtilities.fileToURL(propFile), Utils.DEFAULT_LOCATION_ATTRIBUTE))
                                                continue;

                                        // look for a couple shapefile, mosaic properties file
                                        shapeFile = new File(locationPath, FilenameUtils.getBaseName(propFile.getName())+ ".shp");
                                        if (!Utils.checkFileReadable(shapeFile)&& Utils.checkFileReadable(propFile))
                                                buildMosaic = true;
                                        else {
                                                buildMosaic = false;
                                                break;
                                        }
                                }

                        }

                        // did we find anything? If no, we try to build a new mosaic
                        if (buildMosaic) {
                                ////
                                //
                                // Creating a new mosaic
                                //
                                ////
                                // try to build a mosaic inside this directory and see what
                                // happens
                            
                                // preliminar checks
                                final File mosaicDirectory= new File(locationPath);
                                if(!mosaicDirectory.exists()||mosaicDirectory.isFile()||!mosaicDirectory.canWrite()){
                                    if(LOGGER.isLoggable(Level.SEVERE)){
                                        LOGGER.log(Level.SEVERE,"Unable to create the mosaic, check the location:\n"+
                                                "location is:"+locationPath+"\n"+
                                                "location exists:"+mosaicDirectory.exists()+"\n"+
                                                "location is a directory:"+mosaicDirectory.isDirectory()+"\n"+
                                                "location is writable:"+mosaicDirectory.canWrite()+"\n"+
                                                "location is readable:"+mosaicDirectory.canRead()+"\n"+
                                                "location is hidden:"+mosaicDirectory.isHidden()+"\n");
                                    }
                                    return null;
                                }
                            
                                // actual creation
                                createMosaic(locationPath, defaultIndexName, DEFAULT_WILCARD, DEFAULT_PATH_BEHAVIOR, hints);

                                // check that the mosaic properties file was created
                                final File propertiesFile = new File(locationPath,
                                                defaultIndexName + ".properties");
                                if (!Utils.checkFileReadable(propertiesFile)) {
                                        // retrieve a null so that we shows that a problem occurred
                                        if (!checkMosaicHasBeenInitialized(locationPath, defaultIndexName)) {
                                            sourceURL = null;
                                            return sourceURL;
                                        }
                                }

                                // check that the shapefile was correctly created in case it
                                // was needed
                                sourceURL = updateSourceURL(sourceURL, datastoreFound, locationPath, defaultIndexName/*, emptyFile*/);
                                
                        } else
                                // now set the new source and proceed
                                sourceURL = datastoreFound ? DataUtilities.fileToURL(dataStoreProperties) : DataUtilities.fileToURL(shapeFile); 

                }
        } else {
                // SK: We don't set SourceURL to null now, just because it doesn't
                // point to a file
                // sourceURL=null;
        }
        return sourceURL;
    }

    private static String getDefaultIndexName(final String locationPath) {
        if(locationPath == null) {
            return null;
        }
        File file = new File(locationPath);
        if(file.isDirectory()) {
            File indexer = new File(file, "indexer.properties");
            if(indexer.exists()) {
                URL indexerUrl = DataUtilities.fileToURL(indexer);
                Properties config = Utils.loadPropertiesFromURL(indexerUrl);
                if(config != null && config.get(Utils.Prop.NAME) != null) {
                    return (String) config.get(Utils.Prop.NAME);
                } 
            }
        }
        
        return FilenameUtils.getName(locationPath);
    }
    

    /**
     * Look for a proper sourceURL to be returned.
     * 
     * @param sourceURL
     * @param datastoreFound 
     * @param locationPath
     * @param defaultIndexName
     * @param emptyFile
     * @return
     */
    private static URL updateSourceURL(URL sourceURL, boolean datastoreFound, String locationPath, String defaultIndexName/*,
            File emptyFile*/) {
        if (!datastoreFound) {
            File shapeFile = new File(locationPath, defaultIndexName + ".shp");

            if (!Utils.checkFileReadable(shapeFile)) {
//                if (!Utils.checkFileReadable(emptyFile)) {
                    sourceURL = null;
//                } else {
//                    sourceURL = DataUtilities.fileToURL(emptyFile);
//                }
            } else {
                // now set the new source and proceed
                sourceURL = DataUtilities.fileToURL(shapeFile);
            }
        } else {
                File dataStoreProperties = new File(locationPath,"datastore.properties");

                // datastore.properties as the source
                if (!Utils.checkFileReadable(dataStoreProperties)){
                    sourceURL = null;
                }
                else {
                    sourceURL = DataUtilities.fileToURL(dataStoreProperties);
                }
        }

       
        return sourceURL;
    }

    private static boolean checkMosaicHasBeenInitialized(String locationPath, String defaultIndexName) {
        File mosaicFile = new File(locationPath, defaultIndexName + ".xml");
        if (Utils.checkFileReadable(mosaicFile)) {
            return true;
        }
        mosaicFile = new File(locationPath, defaultIndexName + ".properties");
        if (Utils.checkFileReadable(mosaicFile)) {
            return true;
        }
        
        return false;
    }

    static final double SAMEBBOX_THRESHOLD_FACTOR = 20;

    static final double AFFINE_IDENTITY_EPS = 1E-6;

    public static final boolean DEFAULT_COLOR_EXPANSION_BEHAVIOR = false;

    public static final TimeZone UTC_TIME_ZONE = TimeZone.getTimeZone("UTC");

    static final String DESCENDING_ORDER_IDENTIFIER = " D"; // SortOrder.DESCENDING.identifier();

    static final String ASCENDING_ORDER_IDENTIFIER = " A"; // SortOrder.ASCENDING.identifier();

    public static final String SCAN_FOR_TYPENAMES = "TypeNames";

    public static final String SAMPLE_IMAGE_NAME = "sample_image";

    public static final String TIME_DOMAIN = "TIME";

    public static final String ELEVATION_DOMAIN = "ELEVATION";

    public static final String ADDITIONAL_DOMAIN = "ADDITIONAL";

    public static ObjectFactory OBJECT_FACTORY = new ObjectFactory();

    private static IOFileFilter CLEANUP_FILTER;

    /**
     * Private constructor to initialize the ehCache instance. It can be configured through a Bean.
     * 
     * @param ehcache
     */
    private Utils(Cache ehcache) {
        Utils.ehcache = ehcache;
    }
    
    /**
     * Setup a {@link Histogram} object by deserializing  
     * a file representing a serialized Histogram.
     * 
     * @param file
     * @return the deserialized histogram.
     */
    static Histogram getHistogram(final String file){
        Utilities.ensureNonNull("file", file);
        Histogram histogram = null;
        
        // Firstly: check if the histogram have been already
        // deserialized and it is available in cache
        if (ehcache != null && ehcache.isKeyInCache(file)){
            if (ehcache.isElementInMemory(file)){
                final Element element = ehcache.get(file);
                if (element != null){
                    final Serializable value = element.getValue();
                    if (value != null && value instanceof Histogram){
                        histogram = (Histogram) value; 
                        return histogram;
                    }
                }
            }
        }
        
        // No histogram in cache. Deserializing...
        if (histogram == null){
            FileInputStream fileStream = null;
            ObjectInputStream objectStream = null;
            try {

                fileStream = new FileInputStream(file);
                objectStream = new ObjectInputStream(fileStream);
                histogram = (Histogram) objectStream.readObject();
                if (ehcache != null){
                    ehcache.put(new Element(file, histogram));
                }
            } catch (FileNotFoundException e) {
                if (LOGGER.isLoggable(Level.FINE)){
                    LOGGER.fine("Unable to parse Histogram:" + e.getLocalizedMessage());
                }
            } catch (IOException e) {
                if (LOGGER.isLoggable(Level.FINE)){
                    LOGGER.fine("Unable to parse Histogram:" + e.getLocalizedMessage());
                }
            } catch (ClassNotFoundException e) {
                if (LOGGER.isLoggable(Level.FINE)){
                    LOGGER.fine("Unable to parse Histogram:" + e.getLocalizedMessage());
                }
            } finally {
                if (objectStream != null){
                    IOUtils.closeQuietly(objectStream);
                }
                if (fileStream != null){
                    IOUtils.closeQuietly(fileStream);
                }
            }
        }
        return histogram;
    }

    /**
         * Check if the provided granule's footprint covers the same area of the granule's bbox.
         * @param granuleFootprint the granule Footprint
         * @param granuleBBOX the granule bbox
         * @return {@code true} in case the footprint isn't covering the FULL granule's bbox. 
         */
        static boolean areaIsDifferent(
                final Geometry granuleFootprint,
                final AffineTransform baseGridToWorld,
                final ReferencedEnvelope granuleBBOX) {
            
            // // 
            //
            // First preliminar check:
            // check if the footprint's bbox corners are the same of the granule's bbox
            // (Using a threshold)
            //
            // //
            final Envelope envelope = granuleFootprint.getEnvelope().getEnvelopeInternal();
            double deltaMinX = Math.abs(envelope.getMinX() - granuleBBOX.getMinX());
            double deltaMinY = Math.abs(envelope.getMinY() - granuleBBOX.getMinY());
            double deltaMaxX = Math.abs(envelope.getMaxX() - granuleBBOX.getMaxX());
            double deltaMaxY = Math.abs(envelope.getMaxY() - granuleBBOX.getMaxY());
            final double resX = XAffineTransform.getScaleX0(baseGridToWorld);
            final double resY = XAffineTransform.getScaleY0(baseGridToWorld);
            final double toleranceX = resX / Utils.SAMEBBOX_THRESHOLD_FACTOR;
            final double toleranceY = resY / Utils.SAMEBBOX_THRESHOLD_FACTOR;
            
            // Taking note of the area of a single cell
            final double cellArea = resX * resY;
    
            if (deltaMinX > toleranceX || deltaMaxX > toleranceX || deltaMinY > toleranceY || deltaMaxY > toleranceY){
                // delta exceed tolerance. Area is not the same	        
                return true;
            }
            
            // //
            //
            // Second check:
            // Here, the footprint's bbox and the granule's bbox are equal.
            // However this is not enough:
            // - suppose the footprint is a diamond
            // - Create a rectangle by circumscribing the diamond
            // - If this rectangle match with the granule's bbox, this doesn't imply
            // that the diamond covers the same area of the bbox.
            // Therefore, we need to compute the area and compare them.
            //
            // //
            final double footprintArea = granuleFootprint.getArea();
            //final double bboxArea = granuleBBOX.getArea();
            final double bboxArea = granuleBBOX.getHeight() * granuleBBOX.getWidth();
            	    
            // If 2 areas are different more than the cellArea, then they are not the same area
            if (Math.abs(footprintArea - bboxArea) > cellArea)
                return true;
            return false;
    }

    /**
     * Checks if the Shape equates to a Rectangle, if it does it performs a conversion, otherwise returns null
     * 
     * @param shape
     * @return
     */
    static Rectangle toRectangle(Shape shape) {
        if (shape instanceof Rectangle) {
            return (Rectangle) shape;
        }

        if (shape == null) {
            return null;
        }

        // check if it's equivalent to a rectangle
        PathIterator iter = shape.getPathIterator(new AffineTransform());
        double[] coords = new double[2];

        // not enough points?
        if (iter.isDone()) {
            return null;
        }

        // get the first and init the data structures
        iter.next();
        int action = iter.currentSegment(coords);
        if (action != PathIterator.SEG_MOVETO && action != PathIterator.SEG_LINETO) {
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

            if (action == PathIterator.SEG_CLOSE) {
                break;
            }
            if (action != PathIterator.SEG_LINETO) {
                return null;
            }

            // check orthogonal step (x does not change and y does, or vice versa)
            double x = coords[0];
            double y = coords[1];
            if (!(prevx == x && prevy != y) && !(prevx != x && prevy == y)) {
                return null;
            }

            // update mins and maxes
            if (x < minx) {
                minx = x;
            } else if (x > maxx) {
                maxx = x;
            }
            if (y < miny) {
                miny = y;
            } else if (y > maxy) {
                maxy = y;
            }

            // keep track of prev step
            prevx = x;
            prevy = y;
        }

        // if more than 4 other points it's not a standard rectangle
        iter.next();
        if (!iter.isDone() || i != 3) {
            return null;
        }

        // turn it into a rectangle
        return new Rectangle2D.Double(minx, miny, maxx - minx, maxy - miny).getBounds();
    }

    public static ImageLayout getImageLayoutHint(RenderingHints renderHints) {
        if (renderHints == null||!renderHints.containsKey(JAI.KEY_IMAGE_LAYOUT)) {
            return null;
        } else {
            return (ImageLayout) renderHints.get(JAI.KEY_IMAGE_LAYOUT);
        }
    }

    public static TileCache getTileCacheHint(RenderingHints renderHints) {
        if (renderHints == null||!renderHints.containsKey(JAI.KEY_TILE_CACHE)) {
            return null;
        } else {
            return (TileCache) renderHints.get(JAI.KEY_TILE_CACHE);
        }
    }

    public static BorderExtender getBorderExtenderHint(RenderingHints renderHints) {
        if (renderHints == null||!renderHints.containsKey(JAI.KEY_BORDER_EXTENDER)) {
            return null;
        } else {
            return (BorderExtender) renderHints.get(JAI.KEY_BORDER_EXTENDER);
        }
    }
    
    public static TileScheduler getTileSchedulerHint(RenderingHints renderHints) {
        if (renderHints == null||!renderHints.containsKey(JAI.KEY_TILE_SCHEDULER)) {
            return null;
        } else {
            return (TileScheduler) renderHints.get(JAI.KEY_TILE_SCHEDULER);
        }
    }
    
    /**
     * Create a Range of numbers from a couple of values.
     * @param firstValue
     * @param secondValue
     * @return
     */
    public static Range<? extends Number> createRange(Object firstValue, Object secondValue) {
        Class<? extends Object> targetClass = firstValue.getClass();
        Class<? extends Object> target2Class = secondValue.getClass();
        if (targetClass != target2Class) {
            throw new IllegalArgumentException("The 2 values need to belong to the same class:\n"
                    + "firstClass = " + targetClass.toString() + "; secondClass = " + targetClass.toString()); 
        }
        if (targetClass == Byte.class) {
            return new Range<Byte>(Byte.class, (Byte) firstValue, (Byte) secondValue);
        } else if (targetClass == Short.class) {
            return new Range<Short>(Short.class, (Short) firstValue, (Short) secondValue);
        } else if (targetClass == Integer.class) {
            return new Range<Integer>(Integer.class, (Integer) firstValue, (Integer) secondValue);
        } else if (targetClass == Long.class) {
            return new Range<Long>(Long.class, (Long) firstValue, (Long) secondValue);
        } else if (targetClass == Float.class) {
            return new Range<Float>(Float.class, (Float) firstValue, (Float) secondValue);
        } else if (targetClass == Double.class) {
            return new Range<Double>(Double.class, (Double) firstValue, (Double) secondValue);
        } else return null;
    }
    
    /**
     * Simple minimal check which checks whether and indexer file exists 
     * @param source
     * @return
     */
    public static boolean minimalIndexCheck(Object source) {
        File sourceFile = null;
        URL sourceURL = null; 
        if (source instanceof File) {
            sourceFile = (File) source;
        } else if (source instanceof URL) {
            sourceURL = (URL) source;
            if (sourceURL.getProtocol().equals("file")) {
                sourceFile = DataUtilities.urlToFile(sourceURL);
            }
        } else if (source instanceof String) {
            // is it a File?
            final String tempSource = (String) source;
            File tempFile = new File(tempSource);
            if (!tempFile.exists()) {
                // is it a URL
                try {
                    sourceURL = new URL(tempSource);
                    source = DataUtilities.urlToFile(sourceURL);
                } catch (MalformedURLException e) {
                    sourceURL = null;
                    source = null;
                }
            } else {
                sourceURL = DataUtilities.fileToURL(tempFile);

                // so that we can do our magic here below
                sourceFile = tempFile;
            }
        }
        final File indexerProperties = new File(sourceFile, Utils.INDEXER_PROPERTIES);
        if (Utils.checkFileReadable(indexerProperties)) {
            return true;
        }
        final File indexerXML = new File(sourceFile, Utils.INDEXER_XML);
        if (Utils.checkFileReadable(indexerXML)) {
            return true;
        }
        return false;
    }

    /**
     * Check whether 2 resolution levels sets are homogeneous (within a tolerance)
     * @param numberOfLevels
     * @param resolutionLevels
     * @param compareLevels
     * @return
     */
    public static boolean homogeneousCheck(final int numberOfLevels, double[][] resolutionLevels,
            double[][] compareLevels) {
        for (int k = 0; k < numberOfLevels; k++) {
            if (Math.abs(resolutionLevels[k][0] - compareLevels[k][0]) > RESOLUTION_TOLERANCE_FACTOR
                    * compareLevels[k][0]
                    || Math.abs(resolutionLevels[k][1] - compareLevels[k][1]) > RESOLUTION_TOLERANCE_FACTOR
                            * compareLevels[k][1]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Unmarshal the file and return and Indexer object.
     * 
     * @param indexerFile
     * @return
     * @throws JAXBException
     */
    public static Indexer unmarshal(File indexerFile) throws JAXBException {
        Unmarshaller unmarshaller = null;
        Indexer indexer = null;
        if (indexerFile != null) {
            unmarshaller = CONTEXT.createUnmarshaller();
            indexer = (Indexer) unmarshaller.unmarshal(indexerFile);
        }
        return indexer;
    }

    /**
     * This method checks the {@link ColorModel} of the current image with the one of the first image in order to check if they are compatible or
     * not in order to perform a mosaic operation.
     * 
     * <p>
     * It is worth to point out that we also check if, in case we have two index color model image, we also try to suggest whether or not we
     * should do a color expansion.
     * 
     * @param defaultCM
     * @param defaultPalette
     * @param actualCM
     * @return a boolean asking to skip this feature.
     */
    static boolean checkColorModels(ColorModel defaultCM, byte[][] defaultPalette, MosaicConfigurationBean configuration, ColorModel actualCM) {
        //
        //
        // ComponentColorModel
        //
        //
        
        if (defaultCM instanceof ComponentColorModel && actualCM instanceof ComponentColorModel) {
            final ComponentColorModel defCCM = (ComponentColorModel) defaultCM, actualCCM = (ComponentColorModel) actualCM;
            
            // number of color components
            final int numColorComponents = defCCM.getNumColorComponents();
            if(numColorComponents != actualCCM.getNumColorComponents()){
                return false;
            }
            
            // componets size
            for(int i=0;i<numColorComponents;i++){
                if(defaultCM.getComponentSize(i)!=defaultCM.getComponentSize(i)){
                    return false;
                }
            }
            return !(defCCM.hasAlpha() == actualCCM.hasAlpha() 
                    &&defCCM.isAlphaPremultiplied() == actualCCM.isAlphaPremultiplied()//&& colorSpaceIsOk
                    && defCCM.getTransparency() == actualCCM.getTransparency()
                    && defCCM.getTransferType() == actualCCM.getTransferType()
                    && defCCM.getPixelSize() == actualCCM.getPixelSize());
            
        }
    
        //
        //
        // IndexColorModel
        //
        //
    
        if (defaultCM instanceof IndexColorModel && actualCM instanceof IndexColorModel) {
            final IndexColorModel defICM = (IndexColorModel) defaultCM, actualICM = (IndexColorModel) actualCM;
            if (defICM.getNumColorComponents() != actualICM.getNumColorComponents()
                    || defICM.hasAlpha() != actualICM.hasAlpha()
                    || !defICM.getColorSpace().equals(actualICM.getColorSpace())
                    || defICM.getTransferType() != actualICM.getTransferType())
                return true;
    
            //
            // Suggesting expansion in the simplest case
            //
            if (defICM.getMapSize() != actualICM.getMapSize()
                    || defICM.getTransparency() != actualICM.getTransparency()
                    || defICM.getTransferType() != actualICM.getTransferType()
                    || defICM.getTransparentPixel() != actualICM.getTransparentPixel()) {
                configuration.setExpandToRGB(true);
                return false;
            }
    
            //
            // Now checking palettes to see if we need to do a color convert
            //
            // get the palette for this color model
            int numBands = actualICM.getNumColorComponents();
            byte[][] actualPalette = new byte[3][actualICM.getMapSize()];
            actualICM.getReds(actualPalette[0]);
            actualICM.getGreens(actualPalette[0]);
            actualICM.getBlues(actualPalette[0]);
            if (numBands == 4)
                actualICM.getAlphas(defaultPalette[0]);
            // compare them
            for (int i = 0; i < defICM.getMapSize(); i++)
                for (int j = 0; j < numBands; j++)
                    if (actualPalette[j][i] != defaultPalette[j][i]) {
                        configuration.setExpandToRGB(true);
                        break;
                    }
            return false;
    
        }
    
        //
        // if we get here this means that the two color models where completely
        // different, hence skip this feature.
        //
        return true;
    }
    
    
    /*
     * Checks if the provided factory spi builds a H2 store
     */
    public static boolean isH2Store(DataStoreFactorySpi spi) {
        String spiName = spi == null ? null : spi.getClass().getName();
        return "org.geotools.data.h2.H2DataStoreFactory".equals(spiName) || 
        "org.geotools.data.h2.H2JNDIDataStoreFactory".equals(spiName);
    }

    public static void fixH2DatabaseLocation(Map<String, Serializable> params, String parentLocation) throws MalformedURLException {
        if(params.containsKey(DATABASE_KEY)){
            String dbname = (String) params.get(DATABASE_KEY);
            // H2 database URLs must not be percent-encoded: see GEOT-4262.
            params.put(DATABASE_KEY,
                    "file:" + (new File(DataUtilities.urlToFile(new URL(parentLocation)),
                                    dbname)).getPath());
        }
    }

    /**
     * Checks if the provided factory spi builds a Oracle store
     */
    public static boolean isOracleStore(DataStoreFactorySpi spi) {
        String spiName = spi == null ? null : spi.getClass().getName();
        return "org.geotools.data.oracle.OracleNGOCIDataStoreFactory".equals(spiName) ||
                "org.geotools.data.oracle.OracleNGJNDIDataStoreFactory".equals(spiName) ||
                "org.geotools.data.oracle.OracleNGDataStoreFactory".equals(spiName);
    }

    /**
     * Checks if the provided factory spi builds a Postgis store
     */
    public static boolean isPostgisStore(DataStoreFactorySpi spi) {
        String spiName = spi == null ? null : spi.getClass().getName();
        return "org.geotools.data.postgis.PostgisNGJNDIDataStoreFactory".equals(spiName) ||
               "org.geotools.data.postgis.PostgisNGDataStoreFactory".equals(spiName);
    }

    /**
     * Merge statistics across datasets.
     * 
     * @param pamDatasets
     * @return
     */
    public static PAMDataset mergePamDatasets (PAMDataset[] pamDatasets) {
        PAMDataset merged = pamDatasets[0];
        if (pamDatasets.length > 1) {
            merged = initRasterBands(pamDatasets[0]);
            if (merged != null){
                for (PAMDataset pamDataset: pamDatasets) {
                    updatePamDatasets(pamDataset, merged);
                }
            }
        }
        return merged;
    }

    /**
     * Merge basic statistics on destination {@link PAMDataset} 
     * {@link PAMRasterBand}s need to have same size. No checks are performed here 
     * 
     * @param inputPamDataset
     * @param outputPamDataset
     */
    private static void updatePamDatasets(PAMDataset inputPamDataset, PAMDataset outputPamDataset) {
        List<PAMRasterBand> inputRasterBands = inputPamDataset.getPAMRasterBand();
        List<PAMRasterBand> outputRasterBands = outputPamDataset.getPAMRasterBand();
        for (int i = 0; i < inputRasterBands.size(); i++) {
            updateRasterBand(inputRasterBands.get(i), outputRasterBands.get(i));
        }
        
    }

    /**
     * Merge basic statistics on {@link PAMRasterBand} by updating min/max
     * Other statistics still need some work.
     * {@link MDI}s need to have same size. No checks are performed here 
     * 
     * @param inputPamRasterBand
     * @param outputPamRasterBand
     */
    private static void updateRasterBand(PAMRasterBand inputPamRasterBand, PAMRasterBand outputPamRasterBand) {
        List<MDI> mdiInputs = inputPamRasterBand.getMetadata().getMDI();
        List<MDI> mdiOutputs = outputPamRasterBand.getMetadata().getMDI();
        for (int i=0; i < mdiInputs.size(); i++) {
            MDI mdiInput = mdiInputs.get(i);
            MDI mdiOutput = mdiOutputs.get(i);
            updateMDI(mdiInput, mdiOutput);
        }
        
    }

    /**
     * Update min and max for mdiOutput.
     * Other statistics need better management. 
     * For the moment we simply returns the min between them
     * @param mdiInput
     * @param mdiOutput
     */
    private static void updateMDI(MDI mdiInput, MDI mdiOutput) {
        Double current = Double.parseDouble(mdiInput.getValue());
        Object value = mdiOutput.getValue();
        if (value != null) {
            Double output = Double.parseDouble((String) value);
            if (mdiInput.getKey().toUpperCase().endsWith("_MAXIMUM")) {
                if (current < output) {
                    current = output;
                }
            } else {
                if (output < current) {
                    current = output;
                }
            }
        }
        mdiOutput.setValue(Double.toString(current));
    }

    /** 
     * Initialize a list of {@link PAMRasterBand}s having same size of the 
     * sample {@link PAMDataset} and same metadata names.
     * @param merged
     * @param samplePam
     * @return 
     */
    private static PAMDataset initRasterBands(PAMDataset samplePam) {
        PAMDataset merged = null;
        if (samplePam != null) {
            merged = new PAMDataset();
            final List<PAMRasterBand> samplePamRasterBands = samplePam.getPAMRasterBand(); 
            final int numBands = samplePamRasterBands.size();
            List<PAMRasterBand> pamRasterBands = merged.getPAMRasterBand();
            PAMRasterBand sampleBand = samplePamRasterBands.get(0);
            List<MDI> sampleMetadata = sampleBand.getMetadata().getMDI();
            for (int i = 0; i<numBands; i++) {
                final PAMRasterBand band = new PAMRasterBand();
                final Metadata metadata = new Metadata();
                List<MDI> mdiList = metadata.getMDI();
                for (MDI mdi: sampleMetadata) {
                    MDI addedMdi = new MDI();
                    addedMdi.setKey(mdi.getKey());
                    mdiList.add(addedMdi);
                }
                band.setMetadata(metadata);
                pamRasterBands.add(band);
            }
        }
        return merged;
    }

    public static IOFileFilter getCleanupFilter() {
       return CLEANUP_FILTER;
    }

    public static void fixH2MVCCParam(Map<String, Serializable> params) {
        if (params != null) {
            // H2 database URLs must not be percent-encoded: see GEOT-4262.
            params.put(MVCC_KEY, true);
        }
    }

    public static void fixPostgisDBCreationParams(Map<String, Serializable> datastoreParams) {
        datastoreParams.put("create database", true);
    }

    public static void dropDB(DataStoreFactorySpi spi, Properties properties) throws IOException {
        if (Utils.isPostgisStore(spi)) {
            final Map params = filterDataStoreParams(properties, spi);
            // Use reflection to invoke dropDatabase on postGis factory DB 
            try {
                MethodUtils.invokeMethod(spi, "dropDatabase", params);
            } catch (NoSuchMethodException e) {
                throw new IOException("Unable to drop the database: ", e);
            } catch (IllegalAccessException e) {
                throw new IOException("Unable to drop the database: ", e);
            } catch (InvocationTargetException e) {
                throw new IOException("Unable to drop the database: ", e);
            }
        }
    }

    public static ImageReaderSpi getReaderSpiFromStream(ImageReaderSpi suggestedSPI,
            ImageInputStream inStream) throws IOException {
        ImageReaderSpi readerSPI = null;
        // get a reader and try to cache the suggested SPI first
        inStream.mark();
        if (suggestedSPI != null && suggestedSPI.canDecodeInput(inStream)) {
            readerSPI = suggestedSPI;
            inStream.reset();
        } else {
            inStream.mark();
            ImageReader reader = ImageIOExt.getImageioReader(inStream);
            if (reader != null)
                readerSPI = reader.getOriginatingProvider();
            inStream.reset();
        }
        return readerSPI;
    }

    public static ImageInputStreamSpi getInputStreamSPIFromURL(URL granuleUrl) throws IOException {

        ImageInputStreamSpi streamSPI = ImageIOExt.getImageInputStreamSPI(granuleUrl, true);
        if (streamSPI == null) {
            final File file = DataUtilities.urlToFile(granuleUrl);
            if (file != null) {
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.log(Level.WARNING, Utils.getFileInfo(file));
                }
            }
            throw new IllegalArgumentException(
                    "Unable to get an input stream for the provided granule "
                            + granuleUrl.toString());
        }
        return streamSPI;
    }
}
