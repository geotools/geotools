/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.imageio.netcdf;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.geotools.coverage.grid.io.FileSetManager;
import org.geotools.coverage.grid.io.FileSystemFileSetManager;
import org.geotools.coverage.io.catalog.DataStoreConfiguration;
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.feature.NameImpl;
import org.geotools.gce.imagemosaic.Utils;
import org.geotools.gce.imagemosaic.catalog.index.Indexer;
import org.geotools.gce.imagemosaic.catalog.index.Indexer.Collectors;
import org.geotools.gce.imagemosaic.catalog.index.Indexer.Collectors.Collector;
import org.geotools.gce.imagemosaic.catalog.index.Indexer.Coverages;
import org.geotools.gce.imagemosaic.catalog.index.Indexer.Coverages.Coverage;
import org.geotools.gce.imagemosaic.catalog.index.IndexerUtils;
import org.geotools.gce.imagemosaic.catalog.index.ObjectFactory;
import org.geotools.gce.imagemosaic.catalog.index.ParametersType;
import org.geotools.gce.imagemosaic.catalog.index.SchemaType;
import org.geotools.gce.imagemosaic.catalog.index.SchemasType;
import org.geotools.gce.imagemosaic.properties.DefaultPropertiesCollectorSPI;
import org.geotools.gce.imagemosaic.properties.PropertiesCollector;
import org.geotools.gce.imagemosaic.properties.PropertiesCollectorFinder;
import org.geotools.gce.imagemosaic.properties.PropertiesCollectorSPI;
import org.geotools.imageio.netcdf.Slice2DIndex.Slice2DIndexManager;
import org.geotools.imageio.netcdf.utilities.NetCDFUtilities;
import org.geotools.referencing.factory.gridshift.DataUtilities;
import org.geotools.resources.coverage.CoverageUtilities;
import org.geotools.util.Utilities;
import org.geotools.util.logging.Logging;
import org.opengis.feature.type.Name;

/** 
 * A class used to store any auxiliary indexing information
 * such as the low level indexer definition as well as the
 * datastore properties configuration specifying where to 
 * build that index.
 * 
 * Since 14.x is it also possible to store the catalog into
 * a PostGis based DB
 * 
 * 
 * @author Daniele Romagnoli, GeoSolutions SAS
 */
public class AncillaryFileManager implements FileSetManager{

    /**
     * The Ancillary file manager will parse different type of 
     * auxiliary files: an XML based indexer specifying the 
     * definition of the low level index describing the 
     * multidim granules catalog, as well as datastore 
     * properties file containing the configuration of the 
     * PostGIS DB where the catalog should be stored.
     */
    enum AuxiliaryFileType {
        INDEXER_XML {
            File lookup(String baseName, File parentDirectory,
                    File destinationDirectory) {
                File file;
                // CASE 1: side file (for backward compatibility)
                // Compose the path to an optional XML auxiliary file in the same directory of the input file
                // (filename.xml)
                String optionalAuxiliaryPath = parentDirectory.getAbsolutePath() + File.separator
                        + baseName + INDEX_SUFFIX;
                file = new File(optionalAuxiliaryPath);
                if (!file.exists() || !file.canRead()) {
                    // CASE 2: side file in hidden folder (for retrocompatibility)
                    // Compose the path to an optional XML auxiliary file inside a directory of with the same
                    // name of the file but with a dot before (.filename/filename.xml)
                    optionalAuxiliaryPath = parentDirectory.getAbsolutePath() + File.separator
                            + "." + baseName + File.separator + baseName + INDEX_SUFFIX;
                    file = new File(optionalAuxiliaryPath);
                    if (!file.exists() || !file.canRead()) {
                        file = null;
                    }
                }

                if (file == null) {
                    // CASE 3: the recent approach using HASH of the file to prevent conflicts
                    // With files with same name
                    file = new File(destinationDirectory, baseName + INDEX_SUFFIX);
                }
                return file;
            }
        },

        INDEXER_DATASTORE {
            File lookup (String baseName, File parentDirectory, File destinationDirectory) {
                File file = null;

                // CASE 1: side file (for backward compatibility)
                // Compose the path to an optional datastore file in the same directory of the input file
                String optionalAuxiliaryDatastorePath = parentDirectory.getAbsolutePath() + File.separator
                        + DEFAULT_DATASTORE_PROPERTIES;
                file = new File(optionalAuxiliaryDatastorePath);
                if (!file.exists() || !file.canRead()) {
                    // CASE 2: side file in hidden folder (for backward compatibility)
                    // Compose the path to an optional datastore file inside a directory with the same
                    // name of the file but with a dot before (.filename/mddatastore.properties)
                    optionalAuxiliaryDatastorePath = parentDirectory.getAbsolutePath() + File.separator
                            + "." + baseName + File.separator + DEFAULT_DATASTORE_PROPERTIES;
                    file = new File(optionalAuxiliaryDatastorePath);
                    if (!file.exists() || !file.canRead()) {
                        file = null;
                    }
                }
                if (file == null) {
                    // CASE 3: the recent approach using HASH of the file to prevent conflicts
                    // With files with same name
                    file = new File(destinationDirectory, DEFAULT_DATASTORE_PROPERTIES);
                    if (!file.exists() || !file.canRead()) {
                        file = null;
                    }
                }
                return file;
            }
        };

        abstract File lookup (String baseName, File parentDirectory, File destinationDirectory);
    }

    private FileSetManager fileSetManager; 

    private final static Logger LOGGER = Logging.getLogger(AncillaryFileManager.class.toString());

    private static ObjectFactory OBJECT_FACTORY = new ObjectFactory();

    /** Default schema name */
    static final String DEFAULT_SCHEMA_NAME = "def";

    private final static Set<String> CUT_EXTENSIONS = new HashSet<String>();

    private static final Set<PropertiesCollectorSPI> pcSPIs = PropertiesCollectorFinder.getPropertiesCollectorSPI();

    private static JAXBContext CONTEXT = null;

    // contains information about dimensions that will produce multiple bands indexed by the dimension name
    private final Map<String, MultipleBandsDimensionInfo> multipleBandsDimensionsInfo = new HashMap<>();

    static {
        try {
            CONTEXT = JAXBContext.newInstance("org.geotools.gce.imagemosaic.catalog.index");
        } catch (Exception e) {
            LOGGER.log(Level.INFO, e.getMessage(), e);
        } 
        CUT_EXTENSIONS.add("nc");
        CUT_EXTENSIONS.add("ncml");
    }

    private Indexer indexer;

    private static final String INDEX_SUFFIX = ".xml";

    private static final String COVERAGE_NAME = "coverageName";

    private static final String DEFAULT_DATASTORE_PROPERTIES = "mddatastore.properties";
    
    /**
     * The list of Slice2D indexes
     */
    private final List<Slice2DIndex> slicesIndexList = new ArrayList<Slice2DIndex>();
    
    /** 
     * The Slice2D index manager
     */
    Slice2DIndexManager slicesIndexManager;

    /** The map of coverages elements */
    Map<String, Coverage> coveragesMapping = new HashMap<String, Coverage>();

    /** coverage Name to variable mapping */
    Map<Name, String> variablesMap = null;

    /** specify whether the auxiliary file contains explicit schema definition to be forced */
    boolean imposedSchema = false;

    /** A propertyCollectors map */
    private Map<String, PropertiesCollector> collectors = null;

    private File destinationDir;

    /** The main NetCDF file */
    private File ncFile;

    /** The parent folder of the main File */
    private File parentDirectory;

    /** File storing the slices index (index, Tsection, Zsection) */
    private File slicesIndexFile;

    /** File storing the datastore properties */
    private File datastoreIndexFile;

    /** File storing the coverages indexer */
    private File indexerFile;

    public AncillaryFileManager(final File netcdfFile, final String indexFilePath) throws IOException, JAXBException, NoSuchAlgorithmException {
        this(netcdfFile, indexFilePath, null);
    }

    public AncillaryFileManager(final File netcdfFile, final String indexFilePath, final String datastoreFilePath) throws IOException, JAXBException, NoSuchAlgorithmException {

        org.geotools.util.Utilities.ensureNonNull("file", netcdfFile);
        if (!netcdfFile.exists()) {
            throw new IllegalArgumentException("The specified file doesn't exist: " + netcdfFile);
        }

        // Set files  
        fileSetManager = new FileSystemFileSetManager();
        ncFile = netcdfFile;
        parentDirectory = new File(ncFile.getParent());

        // Look for external folder configuration
        final String baseFolder = NetCDFUtilities.EXTERNAL_DATA_DIR;
        File baseDir = null;
        if (baseFolder != null) {
            baseDir = new File(baseFolder);
            // Check it again in case it has been deleted in the meantime:
            baseDir = NetCDFUtilities.isValidDir(baseDir) ? baseDir : null;
        }

        String mainFilePath = ncFile.getCanonicalPath();

        // Selection of the hashcode for creating a unique directory of the auxiliary files
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(mainFilePath.getBytes());
        String hashCode = convertToHex(md.digest());

        String mainName = FilenameUtils.getName(mainFilePath);
        //TODO: Improve that check on extensions.
        String extension = FilenameUtils.getExtension(mainName);
        String baseName = cutExtension(extension) ? FilenameUtils.removeExtension(mainName) : mainName;
        String outputLocalFolder = "." + baseName + "_" + hashCode;
        destinationDir = new File(parentDirectory, outputLocalFolder);

        // append base file folder tree to the optional external data dir
        if (baseDir != null) {
            destinationDir = new File(baseDir, outputLocalFolder);
        }

        boolean createdDir = false;
        if (!destinationDir.exists()) {
            createdDir = destinationDir.mkdirs();
            // Creation of an origin.txt file with the absolute file path internally written
            File origin = new File(destinationDir, "origin.txt");
            FileUtils.write(origin, ncFile.getAbsolutePath());
        }

        // Init auxiliary file names
        slicesIndexFile = new File(destinationDir, baseName + ".idx");
        indexerFile = lookupFile(indexFilePath, baseName, AuxiliaryFileType.INDEXER_XML);

        if (!createdDir) {
            // Check for index to be reset only in case we didn't created a new directory.
            checkReset(ncFile, slicesIndexFile, destinationDir);
        }
        fileSetManager.addFile(destinationDir.getAbsolutePath());

        // init
        initIndexer();
        datastoreIndexFile = lookupFile(datastoreFilePath, baseName, AuxiliaryFileType.INDEXER_DATASTORE);
    }

    /**
     * Use different approaches to look for the specified file since it can be provided
     * externally (as from the imageMosaic sharing the same indexer between multiple
     * NetCDF files), it can be contained into a .DIR folder or it can be contained
     * into a HASHNAME folder.
     * 
     * @param auxFilePath
     * @param baseName
     */
    private File lookupFile(String filePath, String baseName, AuxiliaryFileType type ) {
        // CASE 1: file externally provided
        File file = null;
        if (filePath != null) {
            file = new File(filePath);
            if (!file.exists() || !file.canRead()) {
                file = null;
            }
        }
        if (file != null) {
            return file;
        } else {
            file = type.lookup(baseName, parentDirectory, destinationDir);
        }

        return file;
    }

    private static boolean cutExtension(String extension) {
        return CUT_EXTENSIONS.contains(extension);
    }

    /**
     * Check whether the file have been updated.
     * @param ncFile
     * @param slicesIndexFile
     * @param destinationDir
     * @throws IOException
     */
    private static void checkReset(final File mainFile, final File slicesIndexFile, final File destinationDir) throws IOException {
        // TODO: Consider acquiring a LOCK on the file
        if (slicesIndexFile.exists()) {
            final long mainFileTime = mainFile.lastModified();
            final long indexTime = slicesIndexFile.lastModified();

            // Check whether the NetCDF time is more recent with respect to the auxiliary indexes
            if (mainFileTime > indexTime) {
                // Need to delete all the auxiliary files and start from scratch
                final Collection<File> listedFiles = FileUtils.listFiles(destinationDir, null, true);
                for (File file: listedFiles) {

                    // Preserve summary file which contains mapping between coverages and underlying variables
                    if (!file.getAbsolutePath().endsWith(INDEX_SUFFIX)) {
                        FileUtils.deleteQuietly(file);
                    }
                }
            }
        }
    }

    /**
     * Write indexer to disk
     * @throws IOException
     * @throws JAXBException
     * 
     * TODO: Need to check for thread safety
     */
    public void writeToDisk() throws IOException, JAXBException {
        // Write collected information
        Slice2DIndexManager.writeIndexFile(slicesIndexFile, slicesIndexList);
        if (!indexerFile.exists()) {
            storeIndexer(indexerFile, coveragesMapping);
        }
    }

    /**
     * Write to disk the variable summary, a simple text file containing variable names.
     * 
     * @param indexerFile
     * @param coveragesMapping
     * @throws JAXBException 
     */
    private void storeIndexer(final File indexerFile,
            final Map<String, Coverage> coveragesMapping) throws JAXBException {
        if (coveragesMapping == null || coveragesMapping.isEmpty()) {
            throw new IllegalArgumentException("No valid coverages name to be written");
        }

        // Create the main indexer
        final Indexer indexer = OBJECT_FACTORY.createIndexer();
        Coverages coverages = OBJECT_FACTORY.createIndexerCoverages();
        indexer.setCoverages(coverages);

        // create coverages
        final List<Coverage> coveragesList = coverages.getCoverage();
        final Collection <Coverage> inputCoverages = coveragesMapping.values();
        for (Coverage cov: inputCoverages) {

            // Create a coverage object
            final Coverage coverage = OBJECT_FACTORY.createIndexerCoveragesCoverage();
            coverage.setName(cov.getName());
            coverage.setOrigName(cov.getOrigName());
            coveragesList.add(coverage);

            // Create the schema object
            final SchemaType schema = OBJECT_FACTORY.createSchemaType();
            coverage.setSchema(schema);
            final SchemaType inputSchema = cov.getSchema();
            schema.setAttributes(inputSchema.getAttributes());
            schema.setName(inputSchema.getName());
        }
        // Marshalling the indexer to XML on disk
        Marshaller marshaller = CONTEXT.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.marshal(indexer, indexerFile);
    }

    /**
     * Return a {@link Name} representation of the coverage name
     * @param varName
     * @return
     */
    public Name getCoverageName(String varName) {
        final Collection<Coverage> coverages = coveragesMapping.values();
        for (Coverage cov: coverages) {
            if (varName.equalsIgnoreCase(cov.getOrigName())) {
                return new NameImpl(cov.getName());
            }
        }
        return null;
    }


    /**
     * Dispose the Manager
     */
    public void dispose() {
        try {
            slicesIndexList.clear();
            
            if (slicesIndexManager != null) {
                slicesIndexManager.dispose();
            }
        } catch (IOException e) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.warning("Errors Disposing the indexer." + e.getLocalizedMessage());
            }
        } finally {
            slicesIndexManager = null;
        }
    }

    /**
     * Return a {@link Slice2DIndex} related to the provided imageIndex
     * @param imageIndex
     * @return
     * @throws IOException
     */
    public Slice2DIndex getSlice2DIndex(final int imageIndex) throws IOException {
        Slice2DIndex variableIndex;
        if (slicesIndexManager != null) {
            variableIndex = slicesIndexManager.getSlice2DIndex(imageIndex);
        } else {
            variableIndex = slicesIndexList.get(imageIndex);
        }
        return variableIndex;
    }

    public File getSlicesIndexFile() {
        return slicesIndexFile;
    }

    public File getIndexerFile() {
        return indexerFile;
    }
    
    public File getDestinationDir() {
        return destinationDir;
    }

    public File getDatastoreIndexFile() {
        return datastoreIndexFile;
    }

    public void addSlice(final Slice2DIndex variableIndex) {
        slicesIndexList.add(variableIndex);
    }

    public Coverage addCoverage(String varName) {
        // Create a new coverage to be added.
        Coverage coverage = OBJECT_FACTORY.createIndexerCoveragesCoverage();
        coverage.setName(varName);
        coverage.setOrigName(varName);
        addCoverage(coverage);
        return coverage;
    }

    private Coverage addCoverage(Coverage coverage) {
        if (variablesMap == null) {
            variablesMap = new HashMap<Name, String>();
            coveragesMapping = new HashMap<String, Coverage>();
        }
        coveragesMapping.put(coverage.getName(), coverage);
        variablesMap.put(new NameImpl(coverage.getName()), coverage.getOrigName());
        return coverage;
    }

    public void initSliceManager() throws IOException {
        slicesIndexManager = new Slice2DIndexManager(slicesIndexFile);
        slicesIndexManager.open();
    }

    public void resetSliceManager() throws IOException {
        if (slicesIndexManager != null) {
            slicesIndexManager.dispose();
        }
        // clean existing index
        slicesIndexList.clear();
    }

    /** 
     * Get the list of Names for the underlying coverage list
     * @return
     */
    public List<Name> getCoveragesNames() {
        final List<Name> names = new ArrayList<Name>();
        Collection<Coverage> coverages =  coveragesMapping.values();
        for (Coverage cov: coverages) {
            names.add(new NameImpl(cov.getName()));
        }
        return names;
    }

    /**
     * Retrieve basic indexer properties by scanning the indexer XML instance.
     * @throws JAXBException
     */
    private void initIndexer() throws JAXBException {
        if (indexerFile.exists() && indexerFile.canRead()) {
            Unmarshaller unmarshaller = CONTEXT.createUnmarshaller();
            if (unmarshaller != null) {
                indexer = (Indexer) unmarshaller.unmarshal(indexerFile);
                // indexed information about dimensions that supports multiple bands
                initMultipleBandsDimensionsInfo(indexer);
                // Parsing schemas
                final SchemasType schemas = indexer.getSchemas();
                Map<String, String> schemaMapping = new HashMap<String, String>();
                if (schemas != null) {
                    // Map schema names to schema attributes string
                    List<SchemaType> schemaElements = schemas.getSchema();
                    for (SchemaType schemaElement: schemaElements) {
                        schemaMapping.put(schemaElement.getName(), schemaElement.getAttributes());
                    }
                }

                // Parsing properties collectors
                initPropertiesCollectors();

                // Parsing coverages 
                initCoverages(schemaMapping);
                
            }
        }
    }

    /**
     * Init the coverages naming and schema mappings
     */
    private void initCoverages(Map<String,String> schemaMapping) {
        final Coverages coverages = indexer.getCoverages();
        if (coverages != null) {
            final List<Coverage> coverageElements = coverages.getCoverage();

            // Loop over coverages
            for (Coverage coverageElement : coverageElements) {

                // get the coverageName
                String coverageName = coverageElement.getName();
                if (coverageName == null) {
                    // null coverageName... try to setup it through name collector
                    coverageName = getCoverageNameFromCollector(coverageElement.getNameCollector());
                }

                // Get the origName for that coverage
                String origName = coverageElement.getOrigName();
                if (origName != null && !origName.isEmpty()) {
                    origName = origName.trim();
                } else {
                    origName = coverageName;
                }

                // Get the coverage schema and attributes
                final SchemaType coverageSchema = coverageElement.getSchema();
                String coverageSchemaRef = null;
                String schemaAttributes = null;
                if (coverageSchema != null) {
                    imposedSchema = true;
                    schemaAttributes = coverageSchema.getAttributes();
                    coverageSchemaRef = coverageSchema.getRef();
                }

                // initialize schemaName with the coverageName unless there isn't a schema
                // reference
                String schemaName = coverageName;

                // in case of coverageSchemaRef not null, link to that reference schema
                if (coverageSchemaRef == null || coverageSchemaRef.trim().length() == 0)  {
                    schemaMapping.put(coverageName, schemaAttributes);
                } else {
                    schemaName = coverageSchemaRef;
                    schemaAttributes = schemaMapping.get(schemaName);
                }

                // Add the newly created indexer coverage
                final Coverage coverage = createCoverate(coverageName, origName, schemaAttributes, schemaName);
                addCoverage(coverage);
            }
        }
    }

    /**
     * Create a Coverage indexer object with the specified set of properties
     * @param coverageName name of the coverage
     * @param origName name of the underlying variable 
     * @param schemaAttributes schema definition attributes
     * @param schemaName schema name
     * @return
     */
    private Coverage createCoverate(String coverageName, String origName, String schemaAttributes,
            String schemaName) {
        SchemaType schema = OBJECT_FACTORY.createSchemaType();
        Coverage coverage = OBJECT_FACTORY.createIndexerCoveragesCoverage();
        coverage.setOrigName(origName);
        coverage.setName(coverageName);
        coverage.setSchema(schema);
        schema.setAttributes(schemaAttributes);
        schema.setName(schemaName);
        return coverage;
    }

    /**
     * Get the coverageName using the specified nameCollector
     * 
     * @param nameCollector The name of the propertiesCollector which will be used to setup 
     * the coverage name
     * @return
     */
    private String getCoverageNameFromCollector(final String nameCollector) {
        String coverageName = null;
        if (collectors != null && collectors.containsKey(nameCollector)) {
            Map<String, Object> keyValues = new HashMap<String, Object>();
            PropertiesCollector collector = collectors.get(nameCollector);
            collector.collect(ncFile);
            collector.setProperties(keyValues);
            collector.reset();
            coverageName = (String) keyValues.get(COVERAGE_NAME);
        }
        return coverageName;
    }

    /**
     * Initialize the propertiesCollectors machinery
     */
    private void initPropertiesCollectors() {
        final Collectors collectors = indexer.getCollectors();
        if (collectors != null) {
            List<Collector> collectorList = collectors.getCollector();
            if (collectorList != null) {
                this.collectors = new HashMap<String, PropertiesCollector>();
                
                // Scan the collectors list defined inside the indexer 
                for (Collector collector: collectorList) {
                    final String collectorName = collector.getName();
                    final String spiName = collector.getSpi();
                    final String value = collector.getValue();
                    final String mapped = collector.getMapped();
                    PropertiesCollectorSPI selectedSPI = null;
                    
                    // Look for a matching property collector in the set of registered ones
                    for (PropertiesCollectorSPI spi : pcSPIs) {
                        if (spi.isAvailable() && spi.getName().equalsIgnoreCase(spiName)) {
                            selectedSPI = spi;
                            break;
                        }
                    }
                    if (selectedSPI == null) {
                        if (LOGGER.isLoggable(Level.INFO)) {
                            LOGGER.info("Unable to find a PropertyCollector for this INTERNAL_STORE_SPI: " + spiName);
                        }
                        continue;
                    }

                    // property names
                    final String propertyNames[] = new String[]{mapped != null ? mapped : COVERAGE_NAME};

                    // create the PropertiesCollector
                    final PropertiesCollector pc = selectedSPI.create(DefaultPropertiesCollectorSPI.REGEX_PREFIX + value,  Arrays.asList(propertyNames));
                    if (pc != null) {
                        this.collectors.put(collectorName, pc);
                    }
                }
            }
        }
    }

    public String getTypeName(String coverageName) {
        return coveragesMapping.get(coverageName).getSchema().getName();
    }

    /**
     * Add the default schema to this coverage
     * @param coverage
     * @return
     */
    public String setSchema(Coverage coverage, final String schemaName, final String schemaDef) {
        Utilities.ensureNonNull("coverage", coverage);
        Utilities.ensureNonNull("schemaName", schemaName);
        if (coverage != null) {
            SchemaType schema = coverage.getSchema();
            if (schema == null) {
                schema = OBJECT_FACTORY.createSchemaType();
                coverage.setSchema(schema);
            }
            schema.setName(schemaName);
            if(schemaDef!=null){
                schema.setAttributes(schemaDef);
            }
            return schemaName;
        }
        return null;
    }
    
    /**
     * @param varName
     * @return
     */
    public boolean acceptsVariable(String varName) {
        Utilities.ensureNonNull("varName", varName);
        if (indexer == null || indexer.getCoverages() == null) {
            return true;
        }
        for (Coverage filteringCoverage: indexer.getCoverages().getCoverage()) {
            if (varName.equalsIgnoreCase(filteringCoverage.getName()) || 
                    varName.equalsIgnoreCase(filteringCoverage.getOrigName())) {
                return true;
            } 
        }
        return false;
    }

    public boolean isImposedSchema() {
        return imposedSchema;
    }

    @Override
    public void addFile(String filePath) {
        fileSetManager.addFile(filePath);
    }

    @Override
    public List<String> list() {
        return fileSetManager.list();
    }

    @Override
    public void removeFile(String filePath) {
        fileSetManager.removeFile(filePath);
    }

    @Override
    public void purge() {
        try {
            resetSliceManager();
        } catch (IOException e) {
            LOGGER.log(Level.FINER, e.getMessage(), e);
        }
        fileSetManager.purge();
    }
    
    public static String convertToHex(byte[] data) {
        StringBuilder buf = new StringBuilder();
        for (byte b : data) {
            int halfbyte = (b >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                buf.append((0 <= halfbyte) && (halfbyte <= 9) ? (char) ('0' + halfbyte) : (char) ('a' + (halfbyte - 10)));
                halfbyte = b & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }

    /**
     * Create the {@link DataStoreConfiguration} using the external
     * datastoreIndexFile if provided, or the H2 based default.
     * @return
     * @throws IOException
     */
    public DataStoreConfiguration getDatastoreConfiguration() throws IOException {
        DataStoreConfiguration datastoreConfiguration = null;
        if (datastoreIndexFile != null) {
            URL datastoreURL = DataUtilities.fileToURL(datastoreIndexFile);
            Properties properties = CoverageUtilities.loadPropertiesFromURL(datastoreURL);
            if (properties != null) {
                final String SPIClass = properties.getProperty("SPI");
                try {
                    // create a datastore as instructed
                    final DataStoreFactorySpi spi = (DataStoreFactorySpi) Class.forName(SPIClass)
                            .newInstance();
                    Map<String, Serializable> datastoreParams = Utils.filterDataStoreParams(
                            properties, spi);

                    // create a datastore configuration using the specified SPI and datastoreParams
                    datastoreConfiguration = new DataStoreConfiguration(spi, datastoreParams);
                    datastoreConfiguration.setDatastoreSpi(spi);
                    datastoreConfiguration.setParams(datastoreParams);
                    datastoreConfiguration.setShared(true);
                    // update params for the shared case
                    checkStoreWrapping(datastoreConfiguration);
                } catch (Exception e) {
                    final IOException ioe = new IOException();
                    throw (IOException) ioe.initCause(e);
                }
            }
        } else {
            File parentFile = slicesIndexFile.getParentFile();
            String database = FilenameUtils.removeExtension(
                    FilenameUtils.getName(slicesIndexFile.getCanonicalPath())).replace(".", "");
            datastoreConfiguration = new DataStoreConfiguration(
                    DataStoreConfiguration.getDefaultParams(database, parentFile));
        }
        return datastoreConfiguration;
    }

    /**
     * Check whether the dataStore needs to be wrapped 
     * (as an instance, to allow long typeNames and attributes).
     * 
     * @param datastoreConfiguration
     * @throws IOException
     */
    private void checkStoreWrapping(DataStoreConfiguration datastoreConfiguration) throws IOException {
        Map<String, Serializable> params = datastoreConfiguration.getParams();
        String param = getParameter(Utils.Prop.WRAP_STORE);
        if (param != null && param.trim().equalsIgnoreCase("true")) {
            params.put(Utils.Prop.WRAP_STORE, true);
            params.put(Utils.Prop.PARENT_LOCATION, DataUtilities.fileToURL(getDestinationDir())
                    .toString());
        }
    }

    public String getParameter (String parameterKey) {
        ParametersType indexerParams = indexer != null ? indexer.getParameters() : null;
        return IndexerUtils.getParam(indexerParams, parameterKey);
    }

    public boolean getParameterAsBoolean (String parameterKey) {
        ParametersType indexerParams = indexer != null ? indexer.getParameters() : null;
        String param = IndexerUtils.getParam(indexerParams, parameterKey);
        return Boolean.valueOf(param);
    }

    /**
     * Utility method that wil retrieve from the indexer file information about multiple bands
     * dimensions and will parse that information and index it by the dimensions names.
     */
    private void initMultipleBandsDimensionsInfo(Indexer indexer) {
        if (indexer.getMultipleBandsDimensions() == null ||
                indexer.getMultipleBandsDimensions().getMultipleBandsDimension() == null) {
            // no multiple bands dimensions in the data set
            return;
        }
        for (Indexer.MultipleBandsDimensions.MultipleBandsDimension multipleBandsDimension
                : indexer.getMultipleBandsDimensions().getMultipleBandsDimension()) {
            // multiple bands dimensions are ignored by default
            NetCDFUtilities.addIgnoredDimension(multipleBandsDimension.getName());
            // index by the dimensions name the multiple bands information
            multipleBandsDimensionsInfo.put(multipleBandsDimension.getName(),
                    new MultipleBandsDimensionInfo(multipleBandsDimension.getBandsNames()));
        }
    }

    /**
     * This method will return the multiple bands information associated with the provided
     * dimension name or NULL if the dimensions is single band.
     */
    MultipleBandsDimensionInfo getMultipleBandsDimensionInfo(String dimensionName) {
        // simple lookup in the hash table, if the dimensions is single band we simply return NULL
        return multipleBandsDimensionsInfo.get(dimensionName);
    }
}