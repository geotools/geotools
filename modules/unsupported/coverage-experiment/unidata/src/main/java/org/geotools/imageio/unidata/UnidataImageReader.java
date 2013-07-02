/*
 *    ImageI/O-Ext - OpenSource Java Image translation Library
 *    http://www.geo-solutions.it/
 *    http://java.net/projects/imageio-ext/
 *    (C) 2007 - 2009, GeoSolutions
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    either version 3 of the License, or (at your option) any later version.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.imageio.unidata;

import it.geosolutions.imageio.stream.input.URIImageInputStream;
import it.geosolutions.imageio.utilities.ImageIOUtilities;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.IIOException;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.spi.ImageReaderSpi;

import org.apache.commons.io.FilenameUtils;
import org.geotools.coverage.io.CoverageSourceDescriptor;
import org.geotools.coverage.io.catalog.CoverageSlice;
import org.geotools.coverage.io.catalog.CoverageSlicesCatalog;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.feature.NameImpl;
import org.geotools.feature.SchemaException;
import org.geotools.gce.imagemosaic.ImageMosaicFormat;
import org.geotools.gce.imagemosaic.catalog.index.Indexer.Coverages.Coverage;
import org.geotools.gce.imagemosaic.catalog.index.SchemaType;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.imageio.GeoSpatialImageReader;
import org.geotools.imageio.unidata.cv.CoordinateVariable;
import org.geotools.imageio.unidata.utilities.UnidataCRSUtilities;
import org.geotools.imageio.unidata.utilities.UnidataUtilities;
import org.geotools.imageio.unidata.utilities.UnidataUtilities.CheckType;
import org.geotools.util.SoftValueHashMap;
import org.geotools.util.logging.Logging;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.Name;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import ucar.ma2.Array;
import ucar.ma2.IndexIterator;
import ucar.ma2.InvalidRangeException;
import ucar.ma2.Range;
import ucar.ma2.Section;
import ucar.nc2.Dimension;
import ucar.nc2.Variable;
import ucar.nc2.constants.AxisType;
import ucar.nc2.dataset.CoordinateAxis;
import ucar.nc2.dataset.CoordinateAxis1D;
import ucar.nc2.dataset.CoordinateSystem;
import ucar.nc2.dataset.NetcdfDataset;
import ucar.nc2.dataset.VariableDS;

/**
 * An abstract class that handles most of the ucar netcdf libs backed datatypes.
 * 
 * @author Alessio Fabiani, GeoSolutions
 * @author Simone Giannecchini, GeoSolutions
 * @author Andrea Antonello
 * 
 * TODO caching for {@link CoverageSourceDescriptor}
 */
public abstract class UnidataImageReader extends GeoSpatialImageReader {

    /** INTERNAL_INDEX_CREATION_PAGE_SIZE */
    private static final int INTERNAL_INDEX_CREATION_PAGE_SIZE = 1000;

    private final static Logger LOGGER = Logging.getLogger(UnidataImageReader.class.toString());

    /** 
     * An instance of {@link AncillaryFileManager} which takes care of handling all the auxiliary index
     * files and initializations.
     */
    AncillaryFileManager ancillaryFileManager;

    /** Summary set of coverage names */
    // TODO this duplicates the info that we have in the AncillaryFileManager
    final List<Name> coverages = new ArrayList<Name>();

    @Override
    public List<Name> getCoveragesNames() {
        return Collections.unmodifiableList(coverages);
    }

    @Override
    public int getCoveragesNumber() {
        return coverages.size();
    }

    /**
     * The NetCDF dataset, or {@code null} if not yet open. The NetCDF file is
     * open by {@link #ensureOpen} when first needed.
     */
    private NetcdfDataset dataset;

    final Map<String, CoordinateVariable<?>> coordinatesVariables = new HashMap<String, CoordinateVariable<?>>();

    private CheckType checkType = CheckType.UNSET;

    /** Internal Cache for CoverageSourceDescriptor.**/
    private final SoftValueHashMap<String, UnidataVariableAdapter> coverageSourceDescriptorsCache= new SoftValueHashMap<String, UnidataVariableAdapter>();

    final Map<String, String> dimensionsMapping = new HashMap<String, String> ();

    /** The source file */
    private File file;

    ReferencedEnvelope boundingBox;

    public UnidataImageReader( ImageReaderSpi originatingProvider ) {
        super(originatingProvider);
    }

    /**
     * Get the {@link NetcdfDataset} out og an input object.
     * 
     * @param input the input object.
     * @return the dataset or <code>null</code>.
     * @throws IOException
     */
    private NetcdfDataset extractDataset( Object input ) throws IOException {
        NetcdfDataset dataset = null;
        if (input instanceof URIImageInputStream) {
            URIImageInputStream uriInStream = (URIImageInputStream) input;
            dataset = NetcdfDataset.openDataset(uriInStream.getUri().toString());
        }
        if (input instanceof URL) {
            final URL tempURL = (URL) input;
            String protocol = tempURL.getProtocol();
            if (protocol.equalsIgnoreCase("http") || protocol.equalsIgnoreCase("dods")) {
                dataset = NetcdfDataset.openDataset(tempURL.toExternalForm());
            }
        }

        if (dataset == null) {
            dataset = UnidataUtilities.getDataset(input);
        }

        return dataset;
    }

    protected void setNumImages( final int numImages ) {
        if (numImages <= 0) {
            throw new IllegalArgumentException("Number of Images is negative: " + numImages);
        }
        if (this.numImages == -1) {
            this.numImages = numImages;
        }
    }

    @Override
    public int getHeight( int imageIndex ) throws IOException {
        final UnidataVariableAdapter wrapper = getCoverageDescriptor(imageIndex);
        if (wrapper != null){
        	return wrapper.getHeight();
        }
        return -1;
    }

    @Override
    public Iterator<ImageTypeSpecifier> getImageTypes( int imageIndex ) throws IOException {
        final List<ImageTypeSpecifier> l = new java.util.ArrayList<ImageTypeSpecifier>();
        final UnidataVariableAdapter wrapper = getCoverageDescriptor(imageIndex);
        if (wrapper != null) {
            final SampleModel sampleModel = wrapper.getSampleModel();
            final ImageTypeSpecifier imageType = new ImageTypeSpecifier(ImageIOUtilities.createColorModel(sampleModel),
                    sampleModel);
            l.add(imageType);
        }
        return l.iterator();
    }

    public int getWidth( int imageIndex ) throws IOException {
        final UnidataVariableAdapter wrapper = getCoverageDescriptor(imageIndex);
        if (wrapper != null){
            return wrapper.getWidth();
        }
        return -1;
    }

    /**
     * Get a {@link Variable} by name.
     * 
     * @param varName the name of the {@link Variable} to pick.
     * @return the variable or <code>null</code>.
     */
    public Variable getVariableByName( final String varName ) {
        final List<Variable> varList = dataset.getVariables();
        for( Variable var : varList ) {
            if (var.getFullName().equals(varName))
                return var;
        }
        return null;
    }


    /**
     * Reset the status of this reader
     */
    public void reset() {
        super.setInput(null, false, false);
        dispose();
    }

    @Override
    public void setInput( Object input, boolean seekForwardOnly, boolean ignoreMetadata) {
        super.setInput(input, seekForwardOnly, ignoreMetadata);
        if (dataset != null) {
            reset();
        }
        try {
            dataset = extractDataset(input);
            file = UnidataUtilities.getFile(input);
            if (file != null) {
                ancillaryFileManager = new AncillaryFileManager(file, getAuxiliaryFilesPath());
            }

            init();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Index Initialization. Scan the coverageDescriptorsCache and store indexing information.
     * @param coverageDescriptorsCache
     * @return
     * @throws InvalidRangeException
     * @throws IOException
     */
    protected int initIndex( ) throws InvalidRangeException, IOException {
        DefaultTransaction transaction = new DefaultTransaction("indexTransaction" + System.nanoTime());
        int numImages = 0;
        try {

            // init slice catalog
            final File sliceIndexFile = ancillaryFileManager.getSlicesIndexFile(); 
            initCatalog(
                    sliceIndexFile.getParentFile(),
                    FilenameUtils.removeExtension(FilenameUtils.getName(sliceIndexFile.getCanonicalPath())).replace(".", ""));
            final List<Variable> variables = dataset.getVariables();
            if (variables != null) {

                // cycle on all variables to get parse them
                for (final Variable var_ : variables) {
                    if (var_ != null && var_ instanceof VariableDS) {
                        final VariableDS variable= (VariableDS) var_;
                        
                        // get the name
                        // check if it is filtered or not
                        String varName = variable.getFullName();
                        if (!ancillaryFileManager.acceptsVariable(varName)) {
                            continue;
                        }

                        // is it acceptable?
                        if (!UnidataUtilities.isVariableAccepted(variable, checkType)) {
                            continue;
                        }

                        // COVERAGE NAME
                        // Add the accepted variable to the list of coverages name
                        final Name coverageName = getCoverageName(varName);
                        // TODO wrap this into an object that wraps a Unidata CoordinateSystem
                        CoordinateSystem cs = UnidataCRSUtilities.getCoordinateSystem(variable);
                        // SCHEMA
                        // TODO push into VariableWrapper
                        final SimpleFeatureType indexSchema = getIndexSchema(coverageName,cs);

                        if (indexSchema == null) {
                            throw new IllegalStateException("Unable to created index schema for coverage:"+coverageName);
                        }
                        // create 
                        checkSchema(indexSchema);
                        // get variable
                        final UnidataVariableAdapter vaAdapter= getCoverageDescriptor(coverageName);
                        final int variableImageStartIndex = numImages;
                        final int numberOfSlices = vaAdapter.getNumberOfSlices();
                        final int variableImageNum = variableImageStartIndex + numberOfSlices;
                        final int rank = variable.getRank();
                        numImages+=numberOfSlices;

                        // 2D SLICE INDEX PREPARATION
                        // TODO Embed into variable wrapper
                        final boolean hasVerticalAxis = cs.hasVerticalAxis();
                        final int bandDimension = rank - UnidataUtilities.Z_DIMENSION;
                        final ListFeatureCollection collection= new ListFeatureCollection(indexSchema);
                        int features = 0;
                        for (int imageIndex = variableImageStartIndex; imageIndex < variableImageNum; imageIndex++) {
                            int zIndex = -1;
                            int tIndex = -1;
                            for (int i = 0; i < rank; i++) {
                                switch (rank - i) {
                                case UnidataUtilities.X_DIMENSION:
                                case UnidataUtilities.Y_DIMENSION:
                                    break;
                                default: {
                                    if (i == bandDimension && hasVerticalAxis) {
                                        zIndex = vaAdapter.getZIndex(imageIndex-variableImageStartIndex);
                                    } else {
                                        tIndex =  vaAdapter.getTIndex(imageIndex-variableImageStartIndex);
                                    }
                                    break;
                                }
                                }
                            }

                            //Put a new sliceIndex in the list
                            final UnidataSlice2DIndex variableIndex = new UnidataSlice2DIndex(tIndex, zIndex, varName);
                            ancillaryFileManager.addSlice(variableIndex);

                            // Create a feature for that index to be put in the CoverageSlicesCatalog
                            final SimpleFeature feature = createFeature(
                                    variable, 
                                    coverageName.toString(), 
                                    tIndex, 
                                    zIndex, 
                                    cs, 
                                    imageIndex, 
                                    indexSchema);
                            collection.add(feature);
                            features++;
                            
                            // write down in pages
                            if (features % INTERNAL_INDEX_CREATION_PAGE_SIZE == 0) {
                                getCatalog().addGranules(indexSchema.getTypeName(), collection, transaction);
                                collection.clear();
                            }
                        }
                        // add residual features
                        if (collection.size() > 0) {
                            getCatalog().addGranules(indexSchema.getTypeName(), collection, transaction);
                            collection.clear();
                        }
                    }
                }
            }
            // write things to disk
            ancillaryFileManager.writeToDisk();
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("Committing changes to the DB");
            }
            transaction.commit();
        } catch (Throwable e) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("Rollback");
            }
            transaction.rollback();
            throw new IOException(e);
        } finally {
            try {
                transaction.close();
            } catch (Throwable t) {

            }
        }
        return numImages;
    }

    /** 
     * Create the schema in case not already defined
     * @param indexSchema
     * @throws IOException
     */
    private void checkSchema(SimpleFeatureType indexSchema) throws IOException {
        final String typeName = indexSchema.getTypeName();
        final CoverageSlicesCatalog catalog = getCatalog();
        if (typeName != null) {
            // Check if already existing
            String[] typeNames = catalog.getTypeNames();
            if (typeNames != null) {
                for (String tn : typeNames) {
                    if (tn.equalsIgnoreCase(typeName)) {
                        return;
                    }
                }
            }
            catalog.createType(indexSchema);
        }
    }

    private SimpleFeatureType getIndexSchema(Name name,CoordinateSystem cs) throws Exception {
        // Getting the schema for this coverage
        SimpleFeatureType indexSchema = null;
        final String coverageName = name.toString();
        Coverage coverage = ancillaryFileManager.coveragesMapping.get(coverageName);
        SchemaType schema = coverage.getSchema();
        String schemaName = null;
        if (schema != null) {
            schemaName = schema.getName();
        }
        
        // no schema was defined yet, let's create a default one
        if (schemaName == null) {
            ancillaryFileManager.setSchemaName(coverage,coverageName);
        } else {
            // we might have a schema suggested from the indexer
            indexSchema = ancillaryFileManager.suggestSchema(coverage);
        }
        
        if (indexSchema==null){
            // TODO incapsulate in coveragedescriptor
            indexSchema = createSchema(coverage, cs);
        }
        return indexSchema;
    }

    private void initMapping(CoordinateSystem cs) {
        // check other dimensions
        for (CoordinateAxis axis : cs.getCoordinateAxes()) {
            // get from coordinate vars
            final CoordinateVariable<?> cv = coordinatesVariables.get(axis.getFullName()); 
            final String name = cv.getName();
            switch(cv.getAxisType()){
            case GeoX: case GeoY: case Lat: case Lon:
                continue;
            case Height: case Pressure: case RadialElevation: case RadialDistance: case GeoZ:
                if (UnidataCRSUtilities.VERTICAL_AXIS_NAMES.contains(name)) {
                    dimensionsMapping.put(UnidataUtilities.ELEVATION_DIM, name);
                } else {
                    dimensionsMapping.put(name.toUpperCase(), name);
                }
                break;
            case Time: 
                dimensionsMapping.put(UnidataUtilities.TIME_DIM, name);
                break;
            }
        }
    }

    /**
     * @param coverage
     * @param cs
     * @return
     * @throws SchemaException 
     */
    SimpleFeatureType createSchema(Coverage coverage, CoordinateSystem cs) throws SchemaException {

        // init with base
        String schemaAttributes = CoverageSlice.Attributes.BASE_SCHEMA;

        // check other dimensions
        for (CoordinateAxis axis:cs.getCoordinateAxes()) {
            // get from coordinate vars
            final CoordinateVariable<?> cv = this.coordinatesVariables.get(axis.getFullName()); 
            final String name = cv.getName();
            switch(cv.getAxisType()){
            case GeoX: case GeoY: case Lat: case Lon:
                continue;
            }
            schemaAttributes+=("," + name + ":" + cv.getType().getName());
        }

        final SchemaType schema = coverage.getSchema();
        schema.setAttributes(schemaAttributes);
        final CoordinateReferenceSystem actualCRS = UnidataCRSUtilities.WGS84;
        
        SimpleFeatureType indexSchema = DataUtilities.createType(schema.getName(), schemaAttributes);
        return DataUtilities.createSubType(indexSchema,
                        DataUtilities.attributeNames(indexSchema), actualCRS);
        
        
    }

    private Name getCoverageName(String varName) {
        Name coverageName = ancillaryFileManager.getCoverageName(varName);
        if (coverageName == null) {
            ancillaryFileManager.addCoverage(varName);
            coverageName = new NameImpl(varName);
        }
        coverages.add(coverageName);
        return coverageName;
    }

    /**
     * Create a SimpleFeature on top of the provided variable and indexes.
     * 
     * @param variable the input variable 
     * @param tIndex the time index 
     * @param zIndex the zeta index
     * @param cs the {@link CoordinateSystem} associated with that variable
     * @param imageIndex the index to be associated to the feature in the index
     * @param indexSchema the schema to be used to create the feature
     * @param geometry the geometry to be attached to the feature
     * @return the created {@link SimpleFeature}
     * TODO move to variable wrapper
     */
    private SimpleFeature createFeature(
            final Variable variable,
            final String coverageName,
            final int tIndex,
            final int zIndex,
            final CoordinateSystem cs,
            final int imageIndex, 
            final SimpleFeatureType indexSchema) {
        
        final Date date = getTimeValueByIndex(variable, tIndex, cs);
        final Number verticalValue = getVerticalValueByIndex(variable, zIndex, cs);

        final SimpleFeature feature = DataUtilities.template(indexSchema);
        feature.setAttribute(CoverageSlice.Attributes.GEOMETRY, UnidataCRSUtilities.GEOM_FACTORY.toGeometry(boundingBox));
        feature.setAttribute(CoverageSlice.Attributes.INDEX, imageIndex);

        // Check if we have time and elevation domain and set the attribute if needed
        if (date != null) {
            feature.setAttribute(dimensionsMapping.get(UnidataUtilities.TIME_DIM), date);
        }

        final String elevationAttribute = dimensionsMapping.get(UnidataUtilities.ELEVATION_DIM);
        if (!Double.isNaN(verticalValue.doubleValue())) {
            List<AttributeDescriptor> descriptors = indexSchema.getAttributeDescriptors();
            String attribute = null;
            

            // Once we don't deal anymore with old coverage APIs, we can consider directly use the dimension name as attribute
            for (AttributeDescriptor descriptor: descriptors) {
                if (descriptor.getLocalName().equalsIgnoreCase(elevationAttribute)) {
                    attribute = elevationAttribute;
                    break;
                }
            }
            
            // custom dimension, mapped to an attribute using its name
            if (attribute == null) {
                // Assuming the custom dimension is always the last attribute
                attribute = variable.getDimension(0).getShortName();
            }
            feature.setAttribute(attribute, verticalValue);
        }
        return feature;
    }

    private void extractCoordinatesVariable( ) throws IOException {
        
        // get the coordinate variables
        for( CoordinateAxis axis : dataset.getCoordinateAxes() ) {
            if (axis instanceof CoordinateAxis1D) {
                coordinatesVariables.put(axis.getFullName(), CoordinateVariable.create((CoordinateAxis1D)axis));
            }
        }
        initMapping(dataset.getCoordinateSystems().get(0));
    }

    @Override
    public void dispose() {
        synchronized (coverageSourceDescriptorsCache) {
            coverageSourceDescriptorsCache.clear();
        }
        
        super.dispose();
        checkType = CheckType.UNSET;

        coordinatesVariables.clear();
        numImages = -1;
        try {
            if (dataset != null) {
                dataset.close();
            }
           
            if (ancillaryFileManager != null) {
                ancillaryFileManager.dispose();
            }
            
        } catch (IOException e) {
            if (LOGGER.isLoggable(Level.WARNING))
                LOGGER.warning("Errors closing NetCDF dataset." + e.getLocalizedMessage());
        } finally {
            dataset = null;
            ancillaryFileManager = null;
        }
    }

    /**
     * Initialize main properties for this reader.
     * 
     * @throws exception
     *                 {@link InvalidRangeException}
     */
    private void init() throws IOException {
        int numImages = 0;
        try {
            if (dataset != null) {
                checkType = UnidataUtilities.getCheckType(dataset);

                // get the coordinate variables
                extractCoordinatesVariable();
                extractBBOX();
                final File slicesIndexFile = ancillaryFileManager.getSlicesIndexFile();

                if (slicesIndexFile != null) {
                    // === use sidecar index
                    if (slicesIndexFile.exists()) {
                        ancillaryFileManager.initSliceManager();
                        numImages = ancillaryFileManager.slicesIndexManager.getNumberOfRecords();
                        if (!ignoreMetadata) {
                            initCatalog(slicesIndexFile.getParentFile(),
                            FilenameUtils.removeExtension(FilenameUtils.getName(slicesIndexFile.getCanonicalPath())).replace(".", ""));
                            coverages.addAll(ancillaryFileManager.getCoveragesNames());
                        }
                    }

                    if (numImages <= 0 || !slicesIndexFile.exists()) {
                        // === index doesn't exists already, build it first
                        // close existing
                        
                        // TODO: Optimize this. Why it's storing the index and reading it back??
                        ancillaryFileManager.resetSliceManager();
                        numImages = initIndex();

                        // reopen file to cut caching
                        ancillaryFileManager.initSliceManager();
                        numImages = ancillaryFileManager.slicesIndexManager.getNumberOfRecords();
                    }

                } else {
                    // === the dataset is no file dataset, need to build memory index
                    numImages = initIndex();
                }

            } else{
                throw new IllegalArgumentException("No valid NetCDF dataset has been found");
            }
        } catch (InvalidRangeException e) {
            throw new IllegalArgumentException("Exception occurred during NetCDF file parsing", e);
        } 
        setNumImages(numImages);
    }

    /**
     * @throws IOException 
     * 
     */
    private void extractBBOX() throws IOException {
        double [] lon= new double[2];
        double [] lat= new double[2];
        byte set=0;
        for(CoordinateVariable<?> cv:coordinatesVariables.values()){
            if(cv.isNumeric()){

                // is it lat or lon?
                AxisType type = cv.getAxisType();
                switch (type) {
                case GeoY: case Lat:
                    if(cv.isRegular()){
                        lat[0]=cv.getStart();
                        lat[1]=lat[0]+cv.getIncrement()*(cv.getSize()-1);
                    } else {
                        lat[0]=((Number)cv.getMinimum()).doubleValue();
                        lat[1]=((Number)cv.getMaximum()).doubleValue();
                    }
                    set++;
                    break;
                case GeoX:case Lon:
                    if(cv.isRegular()){
                        lon[0]=cv.getStart();
                        lon[1]=lon[0]+cv.getIncrement()*(cv.getSize()-1);
                    } else {
                        lon[0]=((Number)cv.getMinimum()).doubleValue();
                        lon[1]=((Number)cv.getMaximum()).doubleValue();
                    }
                    set++;
                    break;
                default:
                    break;
                }
            }
            if(set==2){
                break;
            }
        }
        // create the envelope
        if(set!=2){
            throw new IllegalStateException("Unable to create envelope for this dataset");
        }
        boundingBox = new ReferencedEnvelope(lon[0],lon[1], lat[0],lat[1], UnidataCRSUtilities.WGS84);        
        
    }

    /**
     * Wraps a generic exception into a {@link IIOException}.
     */
    protected IIOException netcdfFailure( final Exception e ) throws IOException {
        return new IIOException(new StringBuilder("Can't read file ").append(dataset.getLocation()).toString(), e);
    }

    /**
     * Return the {@link UnidataSlice2DIndex} associated to the specified imageIndex
     * @param imageIndex
     * @return
     * @throws IOException
     */
    public UnidataSlice2DIndex getSlice2DIndex( int imageIndex ) throws IOException {
        return ancillaryFileManager.getSlice2DIndex(imageIndex);
    }

    /**
     * Return the {@link VariableWrapper} related to that imageIndex
     * @param imageIndex
     * @return
     */
    protected UnidataVariableAdapter getCoverageDescriptor( int imageIndex ) {
        checkImageIndex(imageIndex);
        try {
            UnidataSlice2DIndex slice2DIndex = getSlice2DIndex(imageIndex);
            if (slice2DIndex != null) {
                return getCoverageDescriptor(new NameImpl(slice2DIndex.getVariableName()));
            }
        } catch (IOException e) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, e.getLocalizedMessage(), e);
            }
        }
        return null;
    }
    

    @Override
    public UnidataVariableAdapter getCoverageDescriptor(Name name) {
        final String name_ = name.toString();
        synchronized (coverageSourceDescriptorsCache) {
            if(coverageSourceDescriptorsCache.containsKey(name_)){
                return coverageSourceDescriptorsCache.get(name_);
            }

            // create, cache and return
            UnidataVariableAdapter cd;
            try {
                String origName = ancillaryFileManager.variablesMap.get(name);
                if (origName == null) {
                    origName = name.getLocalPart();
                }
                cd = new UnidataVariableAdapter(this, (VariableDS) getVariableByName(origName));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            coverageSourceDescriptorsCache.put(name_, cd);
            return cd;
        }
    }

    /**
     * @see javax.imageio.ImageReader#read(int, javax.imageio.ImageReadParam)
     */
    @Override
    public BufferedImage read( int imageIndex, ImageReadParam param ) throws IOException {
        clearAbortRequest();
    
        final UnidataSlice2DIndex slice2DIndex = getSlice2DIndex(imageIndex);
        final String variableName=slice2DIndex.getVariableName();
        final UnidataVariableAdapter wrapper=getCoverageDescriptor(new NameImpl(variableName));
        
    
        /*
         * Fetches the parameters that are not already processed by utility
         * methods like 'getDestination' or 'computeRegions' (invoked below).
         */
        final int strideX, strideY;
        // final int[] srcBands;
        final int[] dstBands;
        if (param != null) {
            strideX = param.getSourceXSubsampling();
            strideY = param.getSourceYSubsampling();
            // srcBands = param.getSourceBands();
            dstBands = param.getDestinationBands();
        } else {
            strideX = 1;
            strideY = 1;
            // srcBands = null;
            dstBands = null;
        }
    
        /*
         * Gets the destination image of appropriate size. We create it now
         * since it is a convenient way to get the number of destination bands.
         */
        final int width = wrapper.getWidth();
        final int height        = wrapper.getHeight();
        /*
         * Computes the source region (in the NetCDF file) and the destination
         * region (in the buffered image). Copies those informations into UCAR
         * Range structure.
         */
        final Rectangle srcRegion = new Rectangle();
        final Rectangle destRegion = new Rectangle();
        computeRegions(param, width, height, null, srcRegion, destRegion);
        flipVertically(param, height, srcRegion);
        int destWidth = destRegion.x + destRegion.width;
        int destHeight = destRegion.y + destRegion.height;
    
        /*
         * build the ranges that need to be read from each 
         * dimension based on the source region
         */
        final List<Range> ranges = new LinkedList<Range>();
        try {
            // add the ranges the COARDS way: T, Z, Y, X
            // T
            int first = slice2DIndex.getTIndex();
            int length = 1;
            int stride = 1;
            if (first != -1){
                ranges.add(new Range(first, first + length - 1, stride));
            }
            // Z
            first = slice2DIndex.getZIndex();
            if (first != -1){
                ranges.add(new Range(first, first + length - 1, stride));
            }
            // Y
            first = srcRegion.y;
            length = srcRegion.height;
            stride = strideY;
            ranges.add(new Range(first, first + length - 1, stride));
            // X
            first = srcRegion.x;
            length = srcRegion.width;
            stride = strideX;
            ranges.add(new Range(first, first + length - 1, stride));
        } catch (InvalidRangeException e) {
            throw netcdfFailure(e);
        }
    
        /*
         * create the section of multidimensional array indices
         * that defines the exact data that need to be read 
         * for this image index and parameters 
         */
        final Section section = new Section(ranges);
    
        /*
         * Setting SampleModel and ColorModel.
         */
        final SampleModel sampleModel = wrapper.getSampleModel().createCompatibleSampleModel(destWidth, destHeight);
        final ColorModel colorModel = ImageIOUtilities.createColorModel(sampleModel);
    
        final WritableRaster raster = Raster.createWritableRaster(sampleModel, new Point(0, 0));
        final BufferedImage image = new BufferedImage(colorModel, raster, colorModel.isAlphaPremultiplied(), null);
    
        /*
         * Reads the requested sub-region only.
         */
        processImageStarted(imageIndex);
        final int numDstBands = 1;
        final float toPercent = 100f / numDstBands;
        final int type = raster.getSampleModel().getDataType();
        final int xmin = destRegion.x;
        final int ymin = destRegion.y;
        final int xmax = destRegion.width + xmin;
        final int ymax = destRegion.height + ymin;
        for( int zi = 0; zi < numDstBands; zi++ ) {
            // final int srcBand = (srcBands == null) ? zi : srcBands[zi];
            final int dstBand = (dstBands == null) ? zi : dstBands[zi];
            final Array array;
            try {
                // TODO leak through
                array = 
                    wrapper.variableDS.read(section);
            } catch (InvalidRangeException e) {
                throw netcdfFailure(e);
            }
            final IndexIterator it = array.getIndexIterator();
             for (int y = ymax; --y >= ymin;) {
                for( int x = xmin; x < xmax; x++ ) {
                    switch( type ) {
                        case DataBuffer.TYPE_DOUBLE: {
                            raster.setSample(x, y, dstBand, it.getDoubleNext());
                            break;
                        }
                        case DataBuffer.TYPE_FLOAT: {
                            raster.setSample(x, y, dstBand, it.getFloatNext());
                            break;
                        }
                        case DataBuffer.TYPE_BYTE: {
                            byte b = it.getByteNext();
                            // int myByte = (0x000000FF & ((int) b));
                            // short anUnsignedByte = (short) myByte;
                            // raster.setSample(x, y, dstBand, anUnsignedByte);
                            raster.setSample(x, y, dstBand, b);
                            break;
                        }
                        default: {
                            raster.setSample(x, y, dstBand, it.getIntNext());
                            break;
                        }
                    }
                }
            }
            /*
             * Checks for abort requests after reading. It would be a waste of a
             * potentially good image (maybe the abort request occurred after we
             * just finished the reading) if we didn't implemented the
             * 'isCancel()' method. But because of the later, which is checked
             * by the NetCDF library, we can't assume that the image is
             * complete.
             */
            if (abortRequested()) {
                processReadAborted();
                return image;
            }
            /*
             * Reports progress here, not in the deeper loop, because the costly
             * part is the call to 'variable.read(...)' which can't report
             * progress. The loop that copy pixel values is fast, so reporting
             * progress there would be pointless.
             */
            processImageProgress(zi * toPercent);
        }
        processImageComplete();
        return image;
    }
    
    protected static void flipVertically(final ImageReadParam param, final int srcHeight,
            final Rectangle srcRegion) {
        final int spaceLeft = srcRegion.y;
        srcRegion.y = srcHeight - (srcRegion.y + srcRegion.height);
        /*
         * After the flip performed by the above line, we still have 'spaceLeft' pixels left for a downward translation. We usually don't need to care
         * about if, except if the source region is very close to the bottom of the source image, in which case the correction computed below may be
         * greater than the space left.
         * 
         * We are done if there is no vertical subsampling. But if there is subsampling, then we need an adjustment. The flipping performed above must
         * be computed as if the source region had exactly the size needed for reading nothing more than the last line, i.e. 'srcRegion.height' must
         * be a multiple of 'sourceYSubsampling' plus 1. The "offset" correction is computed below accordingly.
         */
        if (param != null) {
            int offset = (srcRegion.height - 1) % param.getSourceYSubsampling();
            srcRegion.y += offset;
            offset -= spaceLeft;
            if (offset > 0) {
                // Happen only if we are very close to image border and
                // the above translation bring us outside the image area.
                srcRegion.height -= offset;
            }
        }
    }
    
    public String getTypeName(final String coverageName) {
        return ancillaryFileManager.getTypeName(coverageName);
    }

    /**
     * Utility method to retrieve the z-index of a Variable coverageDescriptor stored on
     * {@link NetCDFImageReader} NetCDF Flat Reader {@link HashMap} indexMap.
     * 
     * @param var
     *                {@link Variable}
     * @param range
     *                {@link Range}
     * @param imageIndex
     *                {@link int}
     * 
     * @return z-index {@link int} -1 if variable rank &lt; 3
     * @deprecated
     */
    public static int getZIndex(Variable var, Range range, int imageIndex) {
        final int rank = var.getRank();
    
        if (rank > 2) {
            if (rank == 3) {
                return (imageIndex - range.first());
            } else {
                // return (int) Math.ceil((imageIndex - range.first()) /
                // var.getDimension(rank - (Z_DIMENSION + 1)).getLength());
                return (imageIndex - range.first()) % UnidataUtilities.getZDimensionLength(var);
            }
        }
    
        return -1;
    }

    /** Return the zIndex-th value of the vertical dimension of the specified variable, as a double, or {@link Double#NaN} 
     * in case that variable doesn't have a vertical axis.
     * 
     * @param unidataReader the reader to be used for that search
     * @param variable the variable to be accessed
     * @param timeIndex the requested index
     * @param cs the coordinateSystem to be scan
     * @return
     * TODO move to variable wrapper
     */
    public Number getVerticalValueByIndex(Variable variable, final int zIndex,
            final CoordinateSystem cs ) {
        double ve = Double.NaN;
        if (cs != null && cs.hasVerticalAxis()) {
            final int rank = variable.getRank();
    
            final Dimension verticalDimension = variable.getDimension(rank - UnidataUtilities.Z_DIMENSION);
            return (Number)coordinatesVariables.get(verticalDimension.getFullName()).read(zIndex);
        }
        return ve;
    }

    /**
     * Utility method to retrieve the t-index of a Variable coverageDescriptor stored on
     * {@link NetCDFImageReader} NetCDF Flat Reader {@link HashMap} indexMap.
     * 
     * @param var
     *                {@link Variable}
     * @param range
     *                {@link Range}
     * @param imageIndex
     *                {@link int}
     * 
     * @return t-index {@link int} -1 if variable rank > 4
     * @deprecated
     */
    public static int getTIndex(Variable var, Range range, int imageIndex) {
        final int rank = var.getRank();
    
        if (rank > 2) {
            if (rank == 3) {
                return (imageIndex - range.first());
            } else {
                // return (imageIndex - range.first()) % var.getDimension(rank -
                // (Z_DIMENSION + 1)).getLength();
                return (int) Math.ceil((imageIndex - range.first())
                        / UnidataUtilities.getZDimensionLength(var));
            }
        }
    
        return -1;
    }

    /** Return the timeIndex-th value of the time dimension of the specified variable, as a Date, or null in case that
     * variable doesn't have a time axis.
     * 
     * @param unidataReader the reader to be used for that search
     * @param variable the variable to be accessed
     * @param timeIndex the requested index
     * @param cs the coordinateSystem to be scan
     * @return
     * TODO move to variable wrapper
     */
    public Date getTimeValueByIndex( Variable variable, int timeIndex,
            final CoordinateSystem cs ) {
    
        if (cs != null && cs.hasTimeAxis()) {
            final int rank = variable.getRank();
            final Dimension temporalDimension = variable.getDimension(rank
                    - ((cs.hasVerticalAxis() ? UnidataUtilities.Z_DIMENSION : 2) + 1));
            return (Date) coordinatesVariables.get(temporalDimension.getFullName()).read(timeIndex);
        }
    
        return null;
    }
}