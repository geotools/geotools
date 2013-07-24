package org.geotools.imageio.unidata;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.geotools.feature.NameImpl;
import org.geotools.gce.imagemosaic.catalog.index.Indexer;
import org.geotools.gce.imagemosaic.catalog.index.Indexer.Collectors;
import org.geotools.gce.imagemosaic.catalog.index.Indexer.Collectors.Collector;
import org.geotools.gce.imagemosaic.catalog.index.Indexer.Coverages;
import org.geotools.gce.imagemosaic.catalog.index.Indexer.Coverages.Coverage;
import org.geotools.gce.imagemosaic.catalog.index.ObjectFactory;
import org.geotools.gce.imagemosaic.catalog.index.SchemaType;
import org.geotools.gce.imagemosaic.catalog.index.SchemasType;
import org.geotools.gce.imagemosaic.properties.DefaultPropertiesCollectorSPI;
import org.geotools.gce.imagemosaic.properties.PropertiesCollector;
import org.geotools.gce.imagemosaic.properties.PropertiesCollectorFinder;
import org.geotools.gce.imagemosaic.properties.PropertiesCollectorSPI;
import org.geotools.imageio.unidata.utilities.UnidataUtilities;
import org.geotools.util.Utilities;
import org.geotools.util.logging.Logging;
import org.opengis.feature.type.Name;

/** 
 * A class used to store any auxiliary indexing information
 * 
 * @author Daniele Romagnoli, GeoSolutions SAS
 */
class AncillaryFileManager {

    private final static Logger LOGGER = Logging.getLogger(AncillaryFileManager.class.toString());

    private static ObjectFactory OBJECT_FACTORY = new ObjectFactory();

    /** Default schema name */
    static final String DEFAULT_SCHEMA_NAME = "def";
    
    private static final Set<PropertiesCollectorSPI> pcSPIs = PropertiesCollectorFinder.getPropertiesCollectorSPI();

    private static JAXBContext CONTEXT = null;

    static {
        try {
            CONTEXT = JAXBContext.newInstance("org.geotools.gce.imagemosaic.catalog.index");
        } catch (Exception e) {
            LOGGER.log(Level.INFO, e.getMessage(), e);
        } 
    }

    private Indexer indexer;

    private static final String INDEX_SUFFIX = ".xml";

    private static final String COVERAGE_NAME = "coverageName";
    
    /**
     * The list of Slice2D indexes
     */
    private final List<UnidataSlice2DIndex> slicesIndexList = new ArrayList<UnidataSlice2DIndex>();
    
    /** 
     * The Slice2D index manager
     */
    protected UnidataSlice2DIndexManager slicesIndexManager;

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

    /** File storing the coverages indexer */
    private File indexerFile;
    
    public AncillaryFileManager(final File netcdfFile, final String indexFilePath) throws IOException, JAXBException {
        org.geotools.util.Utilities.ensureNonNull("file", netcdfFile);
        if (!netcdfFile.exists()) {
            throw new IllegalArgumentException("The specified file doesn't exist: " + netcdfFile);
        }

        // Set files  
        ncFile = netcdfFile;
        parentDirectory = new File(ncFile.getParent());

        // Look for external folder configuration
        final String baseFolder = UnidataUtilities.EXTERNAL_DATA_DIR;
        File baseDir = null;
        if (baseFolder != null) {
            baseDir = new File(baseFolder);
            // Check it again in case it has been deleted in the meantime:
            baseDir = UnidataUtilities.isValid(baseDir) ? baseDir : null;
        }

        String mainFilePath = ncFile.getCanonicalPath();
        String baseName = FilenameUtils.removeExtension(FilenameUtils.getName(mainFilePath));
        String outputLocalFolder = "." + baseName;
        destinationDir = new File(parentDirectory, outputLocalFolder);

        // append base file folder tree to the optional external data dir
        if (baseDir != null) {
            destinationDir = new File(baseDir, outputLocalFolder);
        }

        boolean createdDir = false;
        if (!destinationDir.exists()) {
            createdDir = destinationDir.mkdirs();
        }

        // Init auxiliary file names
        slicesIndexFile = new File(destinationDir, baseName + ".idx");
        if (indexFilePath != null) {
            indexerFile = new File(indexFilePath);
            if (!indexerFile.exists() || !indexerFile.canRead()) {
                indexerFile = null;
            }
        }
        if (indexerFile == null) {
            indexerFile = new File(destinationDir, baseName + INDEX_SUFFIX);
        }
        
        if (!createdDir) {
            // Check for index to be reset only in case we didn't created a new directory.
            checkReset(ncFile, slicesIndexFile, destinationDir);
        }

        // init
        initIndexer();
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
        UnidataSlice2DIndexManager.writeIndexFile(slicesIndexFile, slicesIndexList);
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
        marshaller.marshal(indexer, indexerFile);
    }

    /**
     * Return a {@link Name} representation of the coverage name
     * @param varName
     * @return
     */
    Name getCoverageName(String varName) {
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
     * Return a {@link UnidataSlice2DIndex} related to the provided imageIndex
     * @param imageIndex
     * @return
     * @throws IOException
     */
    public UnidataSlice2DIndex getSlice2DIndex(final int imageIndex) throws IOException {
        UnidataSlice2DIndex variableIndex;
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

    public void addSlice(final UnidataSlice2DIndex variableIndex) {
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
        slicesIndexManager = new UnidataSlice2DIndexManager(slicesIndexFile);
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
        if(indexer==null){
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
}