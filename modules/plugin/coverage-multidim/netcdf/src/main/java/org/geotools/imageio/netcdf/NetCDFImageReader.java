/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2014, Open Source Geospatial Foundation (OSGeo)
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

import it.geosolutions.imageio.stream.input.URIImageInputStream;
import it.geosolutions.imageio.utilities.ImageIOUtilities;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.IIOException;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.spi.ImageReaderSpi;

import org.apache.commons.io.FilenameUtils;
import org.geotools.coverage.grid.io.FileSetManager;
import org.geotools.coverage.io.catalog.CoverageSlice;
import org.geotools.coverage.io.catalog.CoverageSlicesCatalog;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.NameImpl;
import org.geotools.feature.SchemaException;
import org.geotools.gce.imagemosaic.catalog.index.Indexer.Coverages.Coverage;
import org.geotools.gce.imagemosaic.catalog.index.SchemaType;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.imageio.GeoSpatialImageReader;
import org.geotools.imageio.netcdf.cv.CoordinateVariable;
import org.geotools.imageio.netcdf.utilities.NetCDFCRSUtilities;
import org.geotools.imageio.netcdf.utilities.NetCDFUtilities;
import org.geotools.imageio.netcdf.utilities.NetCDFUtilities.CheckType;
import org.geotools.util.SoftValueHashMap;
import org.geotools.util.Utilities;
import org.geotools.util.logging.Logging;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;

import ucar.ma2.Array;
import ucar.ma2.IndexIterator;
import ucar.ma2.InvalidRangeException;
import ucar.ma2.Range;
import ucar.ma2.Section;
import ucar.nc2.Variable;
import ucar.nc2.constants.AxisType;
import ucar.nc2.dataset.CoordinateAxis;
import ucar.nc2.dataset.CoordinateAxis1D;
import ucar.nc2.dataset.CoordinateSystem;
import ucar.nc2.dataset.NetcdfDataset;
import ucar.nc2.dataset.VariableDS;

/**
 * Base implementation for NetCDF-CF image flat reader. Pixels are assumed
 * organized according the COARDS convention (a precursor of <A
 * HREF="http://www.cfconventions.org/">CF Metadata conventions</A>), i.e. in (<var>t</var>,<var>z</var>,<var>y</var>,<var>x</var>)
 * order, where <var>x</var> varies faster. The image is created from the two
 * last dimensions (<var>x</var>,<var>y</var>).
 * 
 * Each ImageIndex corresponds to a 2D-slice of NetCDF.
 * 
 * {@link NetCDFImageReader} is a {@link ImageReader} able to create
 * {@link RenderedImage} from NetCDF-CF sources.
 * 
 * @author Alessio Fabiani, GeoSolutions
 * @author Simone Giannecchini, GeoSolutions
 */
public class NetCDFImageReader extends GeoSpatialImageReader implements FileSetManager{

    /** INTERNAL_INDEX_CREATION_PAGE_SIZE */
    private static final int INTERNAL_INDEX_CREATION_PAGE_SIZE = 1000;

    private final static Logger LOGGER = Logging.getLogger(NetCDFImageReader.class.toString());

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
    private final SoftValueHashMap<String, VariableAdapter> coverageSourceDescriptorsCache= new SoftValueHashMap<String, VariableAdapter>();

    final Map<String, String> dimensionsMapping = new HashMap<String, String> ();

    /** The source file */
    private File file;

    ReferencedEnvelope boundingBox;

    /** Boolean indicating if the input file needs flipping or not*/
        private boolean needsFlipping = false;

    public NetCDFImageReader(ImageReaderSpi originatingProvider ) {
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
            dataset = NetcdfDataset.acquireDataset(uriInStream.getUri().toString(), null);
        }
        if (input instanceof URL) {
            final URL tempURL = (URL) input;
            String protocol = tempURL.getProtocol();
            if (protocol.equalsIgnoreCase("http") || protocol.equalsIgnoreCase("dods")) {
                dataset = NetcdfDataset.acquireDataset(tempURL.toExternalForm(), null);
            }
        }

        if (dataset == null) {
            dataset = NetCDFUtilities.getDataset(input);
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
        final VariableAdapter wrapper = getCoverageDescriptor(imageIndex);
        if (wrapper != null){
                return wrapper.getHeight();
        }
        return -1;
    }

    @Override
    public Iterator<ImageTypeSpecifier> getImageTypes( int imageIndex ) throws IOException {
        final List<ImageTypeSpecifier> l = new java.util.ArrayList<ImageTypeSpecifier>();
        final VariableAdapter wrapper = getCoverageDescriptor(imageIndex);
        if (wrapper != null) {
            final SampleModel sampleModel = wrapper.getSampleModel();
            final ImageTypeSpecifier imageType = new ImageTypeSpecifier(ImageIOUtilities.createColorModel(sampleModel),
                    sampleModel);
            l.add(imageType);
        }
        return l.iterator();
    }

    public int getWidth( int imageIndex ) throws IOException {
        final VariableAdapter wrapper = getCoverageDescriptor(imageIndex);
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
            file = NetCDFUtilities.getFile(input);
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
    protected int initIndex() throws InvalidRangeException, IOException {
        DefaultTransaction transaction = new DefaultTransaction("indexTransaction" + System.nanoTime());
        int numImages = 0;
        try {

            // init slice catalog
            final File sliceIndexFile = ancillaryFileManager.getSlicesIndexFile(); 
            initCatalog(sliceIndexFile.getParentFile(),
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
                        if (!NetCDFUtilities.isVariableAccepted(variable, checkType)) {
                            continue;
                        }

                        // COVERAGE NAME
                        // Add the accepted variable to the list of coverages name
                        final Name coverageName = getCoverageName(varName);
                        final CoordinateSystem cs = NetCDFCRSUtilities.getCoordinateSystem(variable);

                        final SimpleFeatureType indexSchema = getIndexSchema(coverageName, cs);
                        // get variable adapter which maps to a coverage in the end
                        final VariableAdapter vaAdapter = getCoverageDescriptor(coverageName);

                        if (indexSchema == null) {
                            throw new IllegalStateException("Unable to created index schema for coverage:"+coverageName);
                        }

                        final int variableImageStartIndex = numImages;
                        final int numberOfSlices = vaAdapter.getNumberOfSlices();
                        numImages += numberOfSlices;

                        int startPagingIndex = 0;
                        final int limit = INTERNAL_INDEX_CREATION_PAGE_SIZE;
                        final ListFeatureCollection collection = new ListFeatureCollection(indexSchema);

                        int writtenFeatures = 0;
                        while (writtenFeatures < numberOfSlices) {
                            // Get a bunch of features 
                            vaAdapter.getFeatures(startPagingIndex, limit, collection);
                            if (variableImageStartIndex != 0) {
                                // Need to updated the imageIndex of the features since all indexes 
                                // are zero based inside each variable but we need to index them inside
                                // the whole NetCDF dataset. 
                                updateFeaturesIndex(collection, variableImageStartIndex);
                            }
                            final int features = collection.size();
                            if (features > 0) {
                                // adding granules to the catalog and updating the number of written features
                                getCatalog().addGranules(indexSchema.getTypeName(), collection, transaction);
                                collection.clear();
                                startPagingIndex += features;
                            }
                            writtenFeatures += features;
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
            if (transaction != null) {
                transaction.rollback();
            }
            throw new IOException(e);
        } finally {
            try {
                if (transaction != null) {
                    transaction.close();
                }
            } catch (Throwable t) {

            }
        }
        return numImages;
    }

    /**
     * Update features imageIndex by referring them to a specific offset
     * @param collection
     * @param offset
     */
    private void updateFeaturesIndex(final ListFeatureCollection collection, final int offset) {
        final SimpleFeatureIterator featuresIt = collection.features();
        SimpleFeature feature = null;
        while (featuresIt.hasNext()) {
            feature = featuresIt.next();
            Integer index = (Integer) feature.getAttribute(CoverageSlice.Attributes.INDEX);
            feature.setAttribute(CoverageSlice.Attributes.INDEX, index + offset);
        }
    }

    private void initMapping(List<CoordinateAxis> coordinateAxis) {
        // check other dimensions
        for (CoordinateAxis axis : coordinateAxis) {
            // get from coordinate vars
            final CoordinateVariable<?> cv = coordinatesVariables.get(axis.getFullName());
            if (cv != null) {
                final String name = cv.getName();
                switch(cv.getAxisType()){
                    case GeoX: case GeoY: case Lat: case Lon:
                        continue;
                    case Height: case Pressure: case RadialElevation: case RadialDistance: case GeoZ:
                        if (NetCDFCRSUtilities.VERTICAL_AXIS_NAMES.contains(name) && !dimensionsMapping.containsKey(NetCDFUtilities.ELEVATION_DIM)) {
                            // Main elevation dimension
                            dimensionsMapping.put(NetCDFUtilities.ELEVATION_DIM, name);
                        } else {
                            // additional elevation dimension
                            dimensionsMapping.put(name.toUpperCase(), name);
                        }
                        break;
                    case Time:
                        if (!dimensionsMapping.containsKey(NetCDFUtilities.TIME_DIM)) {
                            // Main time dimension
                            dimensionsMapping.put(NetCDFUtilities.TIME_DIM, name);
                        } else {
                            // additional time dimension
                            dimensionsMapping.put(name.toUpperCase(), name);
                        }
                        break;
                }
            }else {
                if (LOGGER.isLoggable(Level.SEVERE)) {
                    LOGGER.severe("Null coordinate variable: '" + axis.getFullName() + "' while processing input: " + this.getInput());
                }
            }
        }
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

    private void extractCoordinatesVariable( ) throws IOException {
        // get the coordinate variables
        for( CoordinateAxis axis : dataset.getCoordinateAxes() ) {
            if (axis instanceof CoordinateAxis1D && axis.getAxisType() != null) {
                coordinatesVariables.put(axis.getFullName(), CoordinateVariable.create((CoordinateAxis1D)axis));
            } else {
                // Workaround for Unsupported Axes
                Set<String> unsupported = NetCDFUtilities.getUnsupportedDimensions();
                if (axis instanceof CoordinateAxis1D && unsupported.contains(axis.getFullName())) {
                    axis.setAxisType(AxisType.GeoZ);
                    coordinatesVariables.put(axis.getFullName(),
                            CoordinateVariable.create((CoordinateAxis1D) axis));
                // Workaround for files that have a time dimension, but in a format that could not be parsed
                } else if ("time".equals(axis.getFullName())) {
                    LOGGER.warning("Detected unparseable unit string in time axis: '"
                            + axis.getUnitsString() + "'.");
                    axis.setAxisType(AxisType.Time);
                    coordinatesVariables.put(axis.getFullName(),
                            CoordinateVariable.create((CoordinateAxis1D) axis));
                } else {
                    LOGGER.warning("Unsupported axis: " + axis + " in input: " + this.getInput()
                            + " has been found");
                }
            }
        }
        initMapping(dataset.getCoordinateAxes());
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
                checkType = NetCDFUtilities.getCheckType(dataset);

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
        for (CoordinateVariable<?> cv : coordinatesVariables.values()) {
            if (cv.isNumeric()) {

                // is it lat or lon?
                AxisType type = cv.getAxisType();
                switch (type) {
                case GeoY: case Lat:
                    if (cv.isRegular()) {
                        lat[0] = cv.getStart() - (cv.getIncrement() / 2d);
                        lat[1] = lat[0] + cv.getIncrement() * (cv.getSize());
                    } else {
                        double min = ((Number) cv.getMinimum()).doubleValue();
                        double max = ((Number) cv.getMaximum()).doubleValue();
                        double incr = (max - min) / (cv.getSize() - 1);
                        lat[0] = min - (incr / 2d);
                        lat[1] = max + (incr / 2d);
                    }
                    if(lat[1] > lat[0]){
                        needsFlipping  = true;
                    }else{
                        needsFlipping = false;
                    }
                    set++;
                    break;
                case GeoX:
                case Lon:
                    if (cv.isRegular()) {
                        lon[0] = cv.getStart() - (cv.getIncrement() / 2d);
                        lon[1] = lon[0] + cv.getIncrement() * (cv.getSize());
                    } else {
                        double min = ((Number) cv.getMinimum()).doubleValue();
                        double max = ((Number) cv.getMaximum()).doubleValue();
                        double incr = (max - min) / (cv.getSize() - 1);
                        lon[0] = min - (incr / 2d);
                        lon[1] = max + (incr / 2d);
                    }
                    set++;
                    break;
                default:
                    break;
                }
            }
            if (set == 2) {
                break;
            }
        }
        // create the envelope
        if (set != 2) {
            throw new IllegalStateException("Unable to create envelope for this dataset");
        }
        boundingBox = new ReferencedEnvelope(lon[0], lon[1], lat[0], lat[1], NetCDFCRSUtilities.WGS84);
        
    }

    /**
     * Wraps a generic exception into a {@link IIOException}.
     */
    protected IIOException netcdfFailure( final Exception e ) throws IOException {
        return new IIOException(new StringBuilder("Can't read file ").append(dataset.getLocation()).toString(), e);
    }

    /**
     * Return the {@link Slice2DIndex} associated to the specified imageIndex
     * @param imageIndex
     * @return
     * @throws IOException
     */
    public Slice2DIndex getSlice2DIndex( int imageIndex ) throws IOException {
        return ancillaryFileManager.getSlice2DIndex(imageIndex);
    }

    /**
     * Return the {@link VariableWrapper} related to that imageIndex
     * @param imageIndex
     * @return
     */
    protected VariableAdapter getCoverageDescriptor( int imageIndex ) {
        checkImageIndex(imageIndex);
        try {
            Slice2DIndex slice2DIndex = getSlice2DIndex(imageIndex);
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
    public VariableAdapter getCoverageDescriptor(Name name) {
        Utilities.ensureNonNull("name", name);
        final String name_ = name.toString();
        synchronized (coverageSourceDescriptorsCache) {
            if(coverageSourceDescriptorsCache.containsKey(name_)){
                return coverageSourceDescriptorsCache.get(name_);
            }

            // create, cache and return
            VariableAdapter cd;
            try {
                String origName = ancillaryFileManager.variablesMap != null ? ancillaryFileManager.variablesMap.get(name) : null;
                if (origName == null) {
                    origName = name.getLocalPart();
                } 
//                else {
//                    throw new IllegalArgumentException("Unable to locate descriptor for Coverage: "+name);
//                }
                cd = new VariableAdapter(this, name, (VariableDS) getVariableByName(origName));
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
    
        final Slice2DIndex slice2DIndex = getSlice2DIndex(imageIndex);
        final String variableName=slice2DIndex.getVariableName();
        final VariableAdapter wrapper=getCoverageDescriptor(new NameImpl(variableName));
        
    
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

        // Flipping is needed only when the input latitude coordinate is ordered
        // from min to max
        if(needsFlipping){
                flipVertically(param, height, srcRegion);
        }
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

        CoordinateAxis axis = wrapper.variableDS.getCoordinateSystems().get(0).getLatAxis();
        boolean flipYAxis = false;
        try {
            Array yAxisStart = axis.read(new Section().appendRange(2));
            float y1 = yAxisStart.getFloat(0);
            float y2 = yAxisStart.getFloat(1);
            if (y2 > y1) {
                flipYAxis=true;
            }
        } catch (InvalidRangeException e) {
            throw new RuntimeException(e);
        }
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
            if (flipYAxis) {
                final IndexIterator it = array.getIndexIterator();
                for (int y = ymax; --y >= ymin; ) {
                    for (int x = xmin; x < xmax; x++) {
                        switch (type) {
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
            }else{
                switch( type ) {
                    case DataBuffer.TYPE_DOUBLE: {
                        DoubleBuffer doubleBuffer = array.getDataAsByteBuffer().asDoubleBuffer();
                        double[] samples = new double[destRegion.width * destRegion.height];
                        doubleBuffer.get(samples);
                        raster.setSamples(xmin, ymin, destRegion.width, destRegion.height, dstBand, samples);
                        break;
                    }
                    case DataBuffer.TYPE_FLOAT:
                        float[] samples = new float[destRegion.width * destRegion.height];
                        FloatBuffer floatBuffer = array.getDataAsByteBuffer().asFloatBuffer();
                        floatBuffer.get(samples);
                        raster.setSamples(xmin,ymin,destRegion.width,destRegion.height,dstBand,samples);
                        break;
                    case DataBuffer.TYPE_BYTE:
                        //THIS ONLY WORKS FOR ONE BAND!!
                        raster.setDataElements(xmin,ymin,destRegion.width,destRegion.height,array.getDataAsByteBuffer().array());
                        break;
                    case DataBuffer.TYPE_INT:
                        IntBuffer intBuffer = array.getDataAsByteBuffer().asIntBuffer();
                        int[] intSamples = new int[destRegion.width * destRegion.height];
                        intBuffer.get(intSamples);
                        raster.setSamples(xmin, ymin, destRegion.width, destRegion.height, dstBand, intSamples);
                        break;
                    default: {
                        final IndexIterator it = array.getIndexIterator();
                        for (int y = ymin; y < ymax; y++ ) {
                            for (int x = xmin; x < xmax; x++) {
                                raster.setSample(x, y, dstBand, it.getIntNext());
                            }
                        }
                        break;
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
    
    /** 
     * Create the schema in case not already defined
     * @param indexSchema
     * @throws IOException
     */
    private void forceSchemaCreation(SimpleFeatureType indexSchema) throws IOException {
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

    public SimpleFeatureType getIndexSchema(Name coverageName, CoordinateSystem coordinateSystem) throws Exception {
                // get the name for this variable to check his coveragename
        final String _coverageName = coverageName.toString();
        // get the coverage definition for this variable, at this stage this exists otherwise we would have skipped it!
        final Coverage coverage = ancillaryFileManager.coveragesMapping.get(_coverageName);
        
        // now check the schema creation
        SchemaType schema = coverage.getSchema();
        String schemaDef=schema!=null?schema.getAttributes():null;
        
        // no schema was defined yet, let's create a default one
        if (schema == null||schema.getAttributes()==null) {
            // TODO incapsulate in coveragedescriptor
            schemaDef = suggestSchemaFromCoordinateSystem(coverage, coordinateSystem);
            
            //set the schema name to be the coverageName
            ancillaryFileManager.setSchema(coverage,coverage.getName(),schemaDef);   
            schema = coverage.getSchema();
        } 
        
        // create featuretype, the name is the CoverageName
        final SimpleFeatureType indexSchema = NetCDFUtilities.createFeatureType(coverage.getName(),schemaDef,NetCDFCRSUtilities.WGS84);
        
        // create 
        forceSchemaCreation(indexSchema);
        
        // return
        return indexSchema;
    }

    /**
     * @param coverage
     * @param cs
     * @return
     * @throws SchemaException 
     */
    String suggestSchemaFromCoordinateSystem(Coverage coverage, CoordinateSystem cs) throws SchemaException {

        // init with base
        String schemaAttributes = CoverageSlice.Attributes.BASE_SCHEMA;

        // check other dimensions
        for (CoordinateAxis axis:cs.getCoordinateAxes()) {
            // get from coordinate vars
            final CoordinateVariable<?> cv = coordinatesVariables.get(axis.getFullName()); 
            final String name = cv.getName();
            switch(cv.getAxisType()){
            case GeoX: case GeoY: case Lat: case Lon:
                continue;
            }
            schemaAttributes+=("," + name + ":" + cv.getType().getName());
        }
        return schemaAttributes;
    }

    @Override
    public void addFile(String filePath) {
         ancillaryFileManager.addFile(filePath);
    }

    @Override
    public List<String> list() {
        return ancillaryFileManager.list();
    }

    @Override
    public void removeFile(String filePath) {
        ancillaryFileManager.removeFile(filePath);
    }

    @Override
    public void purge() {
        getCatalog().dispose();
        ancillaryFileManager.purge();
    }
  
}
