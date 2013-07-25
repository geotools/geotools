/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2013, Open Source Geospatial Foundation (OSGeo)
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

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;
import org.geotools.coverage.grid.io.GranuleSource;
import org.geotools.coverage.grid.io.GranuleStore;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.coverage.grid.io.StructuredGridCoverage2DReader;
import org.geotools.data.DataSourceException;
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.factory.Hints;
import org.geotools.feature.collection.AbstractFeatureVisitor;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.gce.imagemosaic.Utils.Prop;
import org.geotools.gce.imagemosaic.catalog.CatalogConfigurationBean;
import org.geotools.gce.imagemosaic.catalog.GranuleCatalog;
import org.geotools.gce.imagemosaic.catalog.GranuleCatalogFactory;
import org.geotools.gce.imagemosaic.catalog.index.Indexer;
import org.geotools.gce.imagemosaic.catalog.index.Indexer.Coverages.Coverage;
import org.geotools.gce.imagemosaic.catalog.index.IndexerUtils;
import org.geotools.gce.imagemosaic.catalog.index.SchemaType;
import org.geotools.gce.imagemosaic.catalog.index.SchemasType;
import org.geotools.gce.imagemosaic.catalogbuilder.CatalogBuilderConfiguration;
import org.geotools.gce.imagemosaic.properties.PropertiesCollector;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.util.DefaultProgressListener;
import org.geotools.util.Utilities;
import org.opengis.feature.Feature;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;
import org.opengis.geometry.Envelope;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.PrecisionModel;

/**
 * An utility class which allows to create schema, catalogs, and populate them.
 * 
 * @author Daniele Romagnoli, GeoSolutions SAS
 *
 */
public class CatalogManager {

    private final static PrecisionModel PRECISION_MODEL = new PrecisionModel(PrecisionModel.FLOATING);
    private final static GeometryFactory GEOM_FACTORY = new GeometryFactory(PRECISION_MODEL);;

    /** Default Logger * */
    private final static Logger LOGGER = org.geotools.util.logging.Logging.getLogger(CatalogManager.class);
    
    /**
     * Create a GranuleCatalog on top of the provided configuration
     * @param runConfiguration
     * @return
     * @throws IOException
     */
    public static GranuleCatalog createCatalog(CatalogBuilderConfiguration runConfiguration) throws IOException {
        //
        // create the index
        //
        // do we have a datastore.properties file?
        final File parent = new File(runConfiguration.getParameter(Prop.ROOT_MOSAIC_DIR));
        GranuleCatalog catalog;
        
        // Consider checking that from the indexer if any
        final File datastoreProperties = new File(parent, "datastore.properties");
        // GranuleCatalog catalog = null;
        if (Utils.checkFileReadable(datastoreProperties)) {
            // read the properties file
            catalog = createGranuleCatalogFromDatastore(parent, datastoreProperties, true,runConfiguration.getHints());
        } else {

            // we do not have a datastore properties file therefore we continue with a shapefile datastore
            final URL file = new File(parent, runConfiguration.getParameter(Utils.Prop.INDEX_NAME) + ".shp").toURI().toURL();
            final Map<String, Serializable> params = new HashMap<String, Serializable>();
            params.put(ShapefileDataStoreFactory.URLP.key, file);
            if (file.getProtocol().equalsIgnoreCase("file")) {
                params.put(ShapefileDataStoreFactory.CREATE_SPATIAL_INDEX.key, Boolean.TRUE);
            }
            params.put(ShapefileDataStoreFactory.MEMORY_MAPPED.key, Boolean.TRUE);
            params.put(ShapefileDataStoreFactory.DBFTIMEZONE.key, TimeZone.getTimeZone("UTC"));
            params.put(Utils.Prop.LOCATION_ATTRIBUTE, runConfiguration.getParameter(Utils.Prop.LOCATION_ATTRIBUTE));
            catalog = GranuleCatalogFactory.createGranuleCatalog(params, false, true, Utils.SHAPE_SPI,runConfiguration.getHints());
        }

        return catalog;
    }
    
    public static Properties createGranuleCatalogProperties(File datastoreProperties) throws IOException {
        Properties properties = Utils.loadPropertiesFromURL(DataUtilities.fileToURL(datastoreProperties));
        if (properties == null) {
            throw new IOException("Unable to load properties from:" + datastoreProperties.getAbsolutePath());
        }
        return properties;
    }
    
    /**
     * Create a granule catalog from a datastore properties file
     * @param parent
     * @param datastoreProperties
     * @param create
     * @param hints 
     * @return
     * @throws IOException
     */
    public static GranuleCatalog createGranuleCatalogFromDatastore(File parent, File datastoreProperties, boolean create, Hints hints) throws IOException {
        GranuleCatalog catalog = null;
        Utilities.ensureNonNull("datastoreProperties", datastoreProperties);
        Properties properties = createGranuleCatalogProperties(datastoreProperties);
        // SPI
        final String SPIClass = properties.getProperty("SPI");
        try {
            // create a datastore as instructed
            final DataStoreFactorySpi spi = (DataStoreFactorySpi) Class.forName(SPIClass).newInstance();
            final Map<String, Serializable> params = Utils.createDataStoreParamsFromPropertiesFile(properties, spi);

            // set ParentLocation parameter since for embedded database like H2 we must change the database
            // to incorporate the path where to write the db
            params.put("ParentLocation", DataUtilities.fileToURL(parent).toExternalForm());

            catalog = GranuleCatalogFactory.createGranuleCatalog(params, false, create, spi,hints);
        } catch (Exception e) {
            final IOException ioe = new IOException();
            throw (IOException) ioe.initCause(e);
        } 
        return catalog;
    }

    /**
     * Create a {@link SimpleFeatureType} from the specified configuration.
     * @param configurationBean
     * @param actualCRS
     * @return
     */
    public static SimpleFeatureType createSchema(CatalogBuilderConfiguration runConfiguration, String name,
            CoordinateReferenceSystem actualCRS) {
        SimpleFeatureType indexSchema = null;
        SchemaType schema = null;
        String schemaAttributes = null;
        Indexer indexer = runConfiguration.getIndexer();
        if (indexer != null) {
            SchemasType schemas = indexer.getSchemas();
            Coverage coverage = IndexerUtils.getCoverage(indexer, name);
            if (coverage != null) {
                schema = IndexerUtils.getSchema(indexer, coverage);
            }
            if (schema != null) {
                schemaAttributes = schema.getAttributes();
            } else if (schemas != null) {
                List<SchemaType> schemaList = schemas.getSchema();
                // CHECK THAT
                if (!schemaList.isEmpty()) {
                    schemaAttributes = schemaList.get(0).getAttributes();
                }
            }
        }
        if (schemaAttributes == null) {
            schemaAttributes = runConfiguration.getSchema(name);
        }
        if (schemaAttributes != null) {
            schemaAttributes = schemaAttributes.trim();
            // get the schema
            try {
                indexSchema = DataUtilities.createType(name, schemaAttributes);
                // override the crs in case the provided one was wrong or absent
                indexSchema = DataUtilities.createSubType(indexSchema,
                        DataUtilities.attributeNames(indexSchema), actualCRS);
            } catch (Throwable e) {
                if (LOGGER.isLoggable(Level.FINE))
                    LOGGER.log(Level.FINE, e.getLocalizedMessage(), e);
                indexSchema = null;
            }
        }

        if (indexSchema == null) {
            // Proceed with default Schema
            final SimpleFeatureTypeBuilder featureBuilder = new SimpleFeatureTypeBuilder();
            featureBuilder.setName(runConfiguration.getParameter(Prop.INDEX_NAME));
            featureBuilder.setNamespaceURI("http://www.geo-solutions.it/");
            featureBuilder.add(runConfiguration.getParameter(Prop.LOCATION_ATTRIBUTE).trim(), String.class);
            featureBuilder.add("the_geom", Polygon.class, actualCRS);
            featureBuilder.setDefaultGeometry("the_geom");
            String timeAttribute = runConfiguration.getTimeAttribute();
            addAttributes(timeAttribute, featureBuilder, Date.class);
            indexSchema = featureBuilder.buildFeatureType();
        }
        return indexSchema;
    }
    
    /**
     * Add splitted attributes to the featureBuilder
     *
     * @param attribute
     * @param featureBuilder
     * @param classType
     * TODO: Remove that once reworking on the dimension stuff
     */
    private static void addAttributes(String attribute, SimpleFeatureTypeBuilder featureBuilder,
            Class classType) {
        if (attribute != null) {
            if (!attribute.contains(Utils.RANGE_SPLITTER_CHAR)) {
                featureBuilder.add(attribute, classType);
            } else {
                String[] ranges = attribute.split(Utils.RANGE_SPLITTER_CHAR);
                if (ranges.length != 2) {
                    throw new IllegalArgumentException(
                            "All ranges attribute need to be composed of a maximum of 2 elements:\n"
                                    + "As an instance (min;max) or (low;high) or (begin;end) , ...");
                } else {
                    featureBuilder.add(ranges[0], classType);
                    featureBuilder.add(ranges[1], classType);
                }
            }
        }

    }

    /**
     * Get a {@link GranuleSource} related to a specific coverageName from an inputReader
     * and put the related granules into a {@link GranuleStore} related to the same coverageName
     * of the mosaicReader.
     * 
     * @param coverageName the name of the coverage to be managed
     * @param fileBeingProcessed the reference input file
     * @param inputReader the reader source of granules
     * @param mosaicReader the reader where to store source granules
     * @param configuration the configuration
     * @param envelope
     * @param transaction
     * @param propertiesCollectors
     * @throws IOException
     */
    static void updateCatalog(
            final String coverageName,
            final File fileBeingProcessed,
            final GridCoverage2DReader inputReader,
            final ImageMosaicReader mosaicReader,
            final CatalogBuilderConfiguration configuration, 
            final GeneralEnvelope envelope,
            final DefaultTransaction transaction, 
            final List<PropertiesCollector> propertiesCollectors) throws IOException {
        
        // Retrieving the store and the destination schema
        final GranuleStore store = (GranuleStore) mosaicReader.getGranules(coverageName, false);
        if (store == null) {
            throw new IllegalArgumentException("No valid granule store has been found for: " + coverageName);
        }
        final SimpleFeatureType indexSchema = store.getSchema();
        final SimpleFeature feature = new ShapefileCompatibleFeature(DataUtilities.template(indexSchema));
        store.setTransaction(transaction);
        
        final ListFeatureCollection collection = new ListFeatureCollection(indexSchema);
        final String fileLocation = prepareLocation(configuration, fileBeingProcessed);
        final String locationAttribute = configuration.getParameter(Prop.LOCATION_ATTRIBUTE);

        // getting input granules
        if (inputReader instanceof StructuredGridCoverage2DReader) {
            
            //
            // Case A: input reader is a StructuredGridCoverage2DReader. We can get granules from a source 
            // 
            // Getting granule source and its input granules
            final GranuleSource source = ((StructuredGridCoverage2DReader) inputReader).getGranules(coverageName, true);
            final SimpleFeatureCollection originCollection = source.getGranules(null);
            final DefaultProgressListener listener = new DefaultProgressListener();
            
            // Getting attributes structure to be filled
            final Collection<Property> destProps = feature.getProperties();
            final Set<Name> destAttributes = new HashSet<Name>();
            for (Property prop: destProps) {
                destAttributes.add(prop.getName());
            }
            
            // Collecting granules
            originCollection.accepts( new AbstractFeatureVisitor(){
                public void visit( Feature feature ) {
                    if(feature instanceof SimpleFeature)
                    {
                            // get the feature
                            final SimpleFeature sourceFeature = (SimpleFeature) feature;
                            final SimpleFeature destFeature = DataUtilities.template(indexSchema);
                            Collection<Property> props = sourceFeature.getProperties();
                            Name propName = null;
                            Object propValue = null;
                            
                            // Assigning value to dest feature for matching attributes 
                            for (Property prop: props) {
                                propName = prop.getName();
                                propValue = prop.getValue();
                                
                                // Matching attributes are set
                                if (destAttributes.contains(propName)) {
                                    destFeature.setAttribute(propName, propValue);
                                }
                            }
                            
                            // Set location
                            destFeature.setAttribute(locationAttribute, fileLocation);
                            
                            // delegate remaining attributes set to properties collector 
                            updateAttributesFromCollectors(destFeature, fileBeingProcessed, inputReader, propertiesCollectors);
                            collection.add(destFeature);
                            
                            // check if something bad occurred
                            if(listener.isCanceled()||listener.hasExceptions()){
                                if(listener.hasExceptions())
                                    throw new RuntimeException(listener.getExceptions().peek());
                                else
                                    throw new IllegalStateException("Feature visitor has been canceled");
                            }
                    }
                } 
            }, listener);
        } else {
            //
            // Case B: old style reader, proceed with classic way, using properties collectors 
            // 
            feature.setAttribute(indexSchema.getGeometryDescriptor().getLocalName(),
                    GEOM_FACTORY.toGeometry(new ReferencedEnvelope((Envelope) envelope)));
            feature.setAttribute(locationAttribute, fileLocation);
            
            updateAttributesFromCollectors(feature, fileBeingProcessed, inputReader, propertiesCollectors);
            collection.add(feature);
        }

        // drop all the granules associated to the same         
        Filter filter = Utils.FF.equal(Utils.FF.property(locationAttribute), Utils.FF.literal(fileLocation), 
                !isCaseSensitiveFileSystem(fileBeingProcessed));
        store.removeGranules(filter);
        
        // Add the granules collection to the store
        store.addGranules(collection);
    }

    /**
     * Checks if the file system is case sensitive or not using File.exists (the only method
     * that also works on OSX too according to 
     * http://stackoverflow.com/questions/1288102/how-do-i-detect-whether-the-file-system-is-case-sensitive )
     * @param fileBeingProcessed
     * @return
     */
    private static boolean isCaseSensitiveFileSystem(File fileBeingProcessed) {
        File loCase = new File(fileBeingProcessed.getParentFile(), fileBeingProcessed.getName().toLowerCase());
        File upCase = new File(fileBeingProcessed.getParentFile(), fileBeingProcessed.getName().toUpperCase());
        return loCase.exists() && upCase.exists();
    }

    /**
     * Update feature attributes through properties collector
     * @param feature
     * @param fileBeingProcessed
     * @param inputReader
     * @param propertiesCollectors
     */
    private static void updateAttributesFromCollectors(
            final SimpleFeature feature,
            final File fileBeingProcessed, 
            final GridCoverage2DReader inputReader,
            final List<PropertiesCollector> propertiesCollectors) {
        // collect and dump properties
        if (propertiesCollectors != null && propertiesCollectors.size() > 0)
            for (PropertiesCollector pc : propertiesCollectors) {
                pc.collect(fileBeingProcessed).collect(inputReader)
                        .setProperties(feature);
                pc.reset();
            }
        
    }

    /**
     * Prepare the location on top of the configuration and file to be processed.
     * @param runConfiguration
     * @param fileBeingProcessed
     * @return
     * @throws IOException
     */
    private static String prepareLocation(CatalogBuilderConfiguration runConfiguration, final File fileBeingProcessed) throws IOException {
        // absolute
        if (Boolean.valueOf(runConfiguration.getParameter(Utils.Prop.ABSOLUTE_PATH))) {
            return fileBeingProcessed.getAbsolutePath();
        }

        // relative
        String targetPath = fileBeingProcessed.getCanonicalPath();
        String basePath = runConfiguration.getParameter(Utils.Prop.ROOT_MOSAIC_DIR);
        String relative = getRelativePath(targetPath, basePath, File.separator); //TODO: Remove this replace after fixing the quote escaping
        return relative;
    }
    
    /**
     * Get the relative path from one file to another, specifying the directory separator. 
     * If one of the provided resources does not exist, it is assumed to be a file unless it ends with '/' or
     * '\'.
     * 
     * @param targetPath targetPath is calculated to this file
     * @param basePath basePath is calculated from this file
     * @param pathSeparator directory separator. The platform default is not assumed so that 
     *        we can test Unix behaviour when running on Windows (for example)
     * @return
     */
    public static String getRelativePath(String targetPath, String basePath, String pathSeparator) {

        // Normalize the paths
        String normalizedTargetPath = FilenameUtils.normalizeNoEndSeparator(targetPath);
        String normalizedBasePath = FilenameUtils.normalizeNoEndSeparator(basePath);

        // Undo the changes to the separators made by normalization
        if (pathSeparator.equals("/")) {
            normalizedTargetPath = FilenameUtils.separatorsToUnix(normalizedTargetPath);
            normalizedBasePath = FilenameUtils.separatorsToUnix(normalizedBasePath);

        } else if (pathSeparator.equals("\\")) {
            normalizedTargetPath = FilenameUtils.separatorsToWindows(normalizedTargetPath);
            normalizedBasePath = FilenameUtils.separatorsToWindows(normalizedBasePath);

        } else {
            throw new IllegalArgumentException("Unrecognised dir separator '" + pathSeparator + "'");
        }

        String[] base = normalizedBasePath.split(Pattern.quote(pathSeparator));
        String[] target = normalizedTargetPath.split(Pattern.quote(pathSeparator));

        // First get all the common elements. Store them as a string,
        // and also count how many of them there are.
        StringBuilder common = new StringBuilder();

        int commonIndex = 0;
        while (commonIndex < target.length && commonIndex < base.length
                && target[commonIndex].equals(base[commonIndex])) {
            common.append(target[commonIndex] + pathSeparator);
            commonIndex++;
        }

        if (commonIndex == 0) {
            // No single common path element. This most
            // likely indicates differing drive letters, like C: and D:.
            // These paths cannot be relativized.
            throw new RuntimeException("No common path element found for '" + normalizedTargetPath 
                    + "' and '" + normalizedBasePath + "'");
        }   

        // The number of directories we have to backtrack depends on whether the base is a file or a dir
        // For example, the relative path from
        //
        // /foo/bar/baz/gg/ff to /foo/bar/baz
        // 
        // ".." if ff is a file
        // "../.." if ff is a directory
        //
        // The following is a heuristic to figure out if the base refers to a file or dir. It's not perfect, because
        // the resource referred to by this path may not actually exist, but it's the best I can do
        boolean baseIsFile = true;

        File baseResource = new File(normalizedBasePath);

        if (baseResource.exists()) {
            baseIsFile = baseResource.isFile();

        } else if (basePath.endsWith(pathSeparator)) {
            baseIsFile = false;
        }

        StringBuilder relative = new StringBuilder();

        if (base.length != commonIndex) {
            int numDirsUp = baseIsFile ? base.length - commonIndex - 1 : base.length - commonIndex;

            for (int i = 0; i < numDirsUp; i++) {
                relative.append(".." + pathSeparator);
            }
        }
        relative.append(normalizedTargetPath.substring(common.length()));
        return relative.toString();
    }

    /**
     * Make sure a proper type name is specified in the catalogBean, it will be used to 
     * create the {@link GranuleCatalog}
     * 
     * @param sourceURL
     * @param configuration
     * @throws IOException 
     */
    private static void checkTypeName(URL sourceURL, MosaicConfigurationBean configuration) throws IOException {
        CatalogConfigurationBean catalogBean = configuration.getCatalogConfigurationBean();
        if (catalogBean.getTypeName() == null) {
            if (sourceURL.getPath().endsWith("shp")) {
                // In case we didn't find a typeName and we are dealing with a shape index, 
                // we set the typeName as the shape name
                final File file = DataUtilities.urlToFile(sourceURL);
                catalogBean.setTypeName(FilenameUtils.getBaseName(file.getCanonicalPath()));
            } else {
                // use the default "mosaic" name
                catalogBean.setTypeName("mosaic");
            }
        }
    }

    /**
     * Create a {@link GranuleCatalog} on top of the provided Configuration
     * @param sourceURL
     * @param configuration
     * @param hints
     * @return
     * @throws IOException 
     */
    static GranuleCatalog createCatalog(final URL sourceURL, final MosaicConfigurationBean configuration, Hints hints) throws IOException {
        CatalogConfigurationBean catalogBean = configuration.getCatalogConfigurationBean();
        
        // Check the typeName
        checkTypeName(sourceURL, configuration);
        if (hints != null && hints.containsKey(Hints.MOSAIC_LOCATION_ATTRIBUTE)) {
            final String hintLocation = (String) hints
                    .get(Hints.MOSAIC_LOCATION_ATTRIBUTE);
            if (!catalogBean.getLocationAttribute().equalsIgnoreCase(hintLocation)) {
                throw new DataSourceException("wrong location attribute");
            }
        }
        // Create the catalog
        return GranuleCatalogFactory.createGranuleCatalog(sourceURL, catalogBean, null,hints);
    }

}
