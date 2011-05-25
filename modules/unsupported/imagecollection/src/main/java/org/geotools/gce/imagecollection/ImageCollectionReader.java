/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.imagecollection;

import java.awt.image.ColorModel;
import java.awt.image.SampleModel;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.media.jai.PlanarImage;

import org.apache.commons.io.FilenameUtils;
import org.geotools.coverage.Category;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.TypeMap;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.data.DataSourceException;
import org.geotools.data.DataUtilities;
import org.geotools.factory.Hints;
import org.geotools.geometry.GeneralEnvelope;
import org.opengis.coverage.ColorInterpretation;
import org.opengis.coverage.grid.Format;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.coverage.grid.GridCoverageReader;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.referencing.operation.MathTransform;

/**
 *
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/trunk/modules/unsupported/imagecollection/src/main/java/org/geotools/gce/imagecollection/ImageCollectionReader.java $
 */
public final class ImageCollectionReader extends AbstractGridCoverage2DReader implements GridCoverageReader {
    /** Logger for the {@link ImageCollectionReader} class. */
    private final static Logger LOGGER = org.geotools.util.logging.Logging.getLogger(ImageCollectionReader.class.toString());

    class DefaultsValue {
        String path;
        
        int maxWidth = 65536;
        
        int maxHeight = 65536;
        
        /** 
         * Time which needs to elapse between 2 consecutive checks for a file changes
         * (Milliseconds)
         */
        long timeBetweenChecks = 1000 * 600;
        
        int epsgCode = 404000; 
    }
    
    DefaultsValue defaultValues = new DefaultsValue();
    
    /**
     * Number of coverages for this reader is
     * 
     * @return the number of coverages for this reader.
     */
    @Override
    public int getGridCoverageCount() {
        return 1;
    }

    private RasterManager rasterManager;

    URL sourceURL;
    
    String rootPath;
    
//    RasterLayout[] overViewLayouts;

//    RasterLayout hrLayout;

    boolean expandMe = true;
    
    @Override
    public void dispose() {
        super.dispose();
        rasterManager.dispose();
    }

    /**
     * Let us retrieve the {@link GridCoverageFactory} that we want to use.
     * 
     * @return retrieves the {@link GridCoverageFactory} that we want to use.
     */
    GridCoverageFactory getGridCoverageFactory() {
        return coverageFactory;
    }

    /**
     * Creates a new instance of GeoTiffReader
     * 
     * @param input
     *            the GeoTiff file
     * @throws DataSourceException
     */
    public ImageCollectionReader(Object input) throws DataSourceException {
        this(input, null);

    }

    /**
     * Creates a new instance of GeoTiffReader
     * 
     * @param input
     *            the GeoTiff file
     * @param uHints
     *            user-supplied hints TODO currently are unused
     * @throws DataSourceException
     */
    public ImageCollectionReader(Object input, Hints uHints)
            throws DataSourceException {
        super(input, uHints);

        //
        // Set the source being careful in case it is an URL pointing to a file
        //
        File file = null;
        try {
            this.sourceURL = Utils.checkSource(source);
            source = DataUtilities.urlToFile(sourceURL);
            if (source instanceof File){
                file = (File) source;
                rootPath = FilenameUtils.getFullPath(FilenameUtils.normalizeNoEndSeparator(file.getAbsolutePath()) + Utils.SEPARATOR);
                loadConfig();
            }
            
        } catch (IOException e) {
            throw new DataSourceException(e);
        }

        rasterManager = new RasterManager(this);
        getHRInfo();
    }


    /**
     * Setup basic referencing info.
     */
    private void getHRInfo() {
        
      originalGridRange = rasterManager.getCoverageGridrange();
      originalEnvelope = rasterManager.getCoverageEnvelope();
      originalEnvelope.setCoordinateReferenceSystem(rasterManager.getCoverageCRS());
      crs = rasterManager.getCoverageCRS();
      raster2Model = rasterManager.getRaster2Model();
        
//        originalGridRange = new GridEnvelope2D(0, 0, Integer.MAX_VALUE, Integer.MAX_VALUE);
//        originalEnvelope = new GeneralEnvelope(new Rectangle2D.Double(0, 0, Integer.MAX_VALUE, Integer.MAX_VALUE));
//        originalEnvelope.setCoordinateReferenceSystem(Utils.DEFAULT_IMAGE_CRS);
//        crs = Utils.DEFAULT_IMAGE_CRS;
        
      highestRes = new double[]{1.0, 1.0};
        
    }

    /** 
     * Setup collection configuration by checking for a properties file containing basic info such as
     * coverageName, relative path of the default image, expand RGB flag, timeBetweenChecks.
     * @throws FileNotFoundException
     */
    private void loadConfig() throws FileNotFoundException {
        final String propertiesPath = rootPath + Utils.CONFIG_FILE;
        final File propertiesFile = new File(propertiesPath);
        String coverage = null;
        final boolean hasPropertiesFile = Utils.checkFileReadable(propertiesFile);
        if (hasPropertiesFile){
            
            // //
            //
            // STEP 0:
            // Loading configuration from properties file
            //
            // //
            Properties props = new Properties();
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(propertiesFile);
                props.load(fis);
                coverage = initProperties(props);
                
            } catch (FileNotFoundException e) {
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.log(Level.WARNING, "Unable to parse the config file: " + propertiesPath, e);
                }

            } catch (IOException e) {
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.log(Level.WARNING, "Unable to parse the config file: " + propertiesPath, e);
                }
            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (Throwable t) {
                        // Does nothing
                    }
                }
            }
        }

        // //
        //
        // STEP 1:
        // Setting parameter which haven't been found in the property file
        //
        // //
        if (coverage == null){
            coverageName = FilenameUtils.getBaseName(FilenameUtils.getFullPathNoEndSeparator(rootPath));    
        } else {
            coverageName = coverage;
        }
        
        if (defaultValues.path == null){
            final File parent = new File(rootPath);
            final List<File> files;
            if (parent.exists() && parent.isDirectory() && parent.canRead()){
                files = Utils.getFileList(parent, Utils.FILE_FILTER, true);
                if (!files.isEmpty()){
                    String path = files.get(0).getAbsolutePath();
                    defaultValues.path = path;
                    if (path.startsWith(rootPath)){
                        defaultValues.path = path.substring(rootPath.length()); 
                    }
                }
            }
        }
        
        if (!hasPropertiesFile){
            updatePropertiesFile(propertiesFile);
        }
    }
    
    /**
     * Init coverage properties from the provided Properties file
     * @param props
     * @return
     */
    private String initProperties(Properties props) {
        String coverage = null;
        // Getting coverage name
        if (props.containsKey(Utils.ImageCollectionProperties.COVERAGE_NAME)) {
            final String coverageName = (String) props.get(Utils.ImageCollectionProperties.COVERAGE_NAME);
            if (coverageName != null && coverageName.trim().length() > 0) {
                coverage = coverageName;
            }
        }
        // Getting default path
        if (props.containsKey(Utils.ImageCollectionProperties.DEFAULT_PATH)) {
            final String defaultPath = (String) props.get(Utils.ImageCollectionProperties.DEFAULT_PATH);
            if (defaultPath != null && defaultPath.trim().length() > 0) {
                defaultValues.path = defaultPath;
            }
        }
        // Getting expand to rgb property (used to deal with paletted images)
        if (props.containsKey(Utils.ImageCollectionProperties.EXPAND_RGB)) {
            final String expand = (String) props.get(Utils.ImageCollectionProperties.EXPAND_RGB);
            if (expand != null && expand.trim().length() > 0) {
                this.expandMe = Boolean.parseBoolean(expand);
            }
        }
        
        // Getting timeIntervalCheck. 
        if (props.containsKey(Utils.ImageCollectionProperties.TIME_BETWEEN_CHECKS)) {
            final String timeCheck = (String) props.get(Utils.ImageCollectionProperties.TIME_BETWEEN_CHECKS);
            if (timeCheck != null && timeCheck.trim().length() > 0) {
                try {
                    defaultValues.timeBetweenChecks = Long.parseLong(timeCheck) * 1000;
                } catch (NumberFormatException nfe){
                    if (LOGGER.isLoggable(Level.WARNING)){
                        LOGGER.log(Level.WARNING, "Unable to parse the specified time interval check.", nfe);
                    }
                }
            }
        }
        
        // Getting MaxWidth parameter. 
        if (props.containsKey(Utils.ImageCollectionProperties.MAX_WIDTH)) {
            final String maxW = (String) props.get(Utils.ImageCollectionProperties.MAX_WIDTH);
            if (maxW != null && maxW.trim().length() > 0) {
                try {
                    defaultValues.maxWidth = Integer.parseInt(maxW);
                } catch (NumberFormatException nfe){
                    if (LOGGER.isLoggable(Level.WARNING)){
                        LOGGER.log(Level.WARNING, "Unable to parse the specified Max Width property.", nfe);
                    }
                }
            }
        }
        
        // Getting MaxHeight parameter. 
        if (props.containsKey(Utils.ImageCollectionProperties.MAX_HEIGHT)) {
            final String maxH = (String) props.get(Utils.ImageCollectionProperties.MAX_HEIGHT);
            if (maxH != null && maxH.trim().length() > 0) {
                try {
                    defaultValues.maxHeight = Integer.parseInt(maxH);
                } catch (NumberFormatException nfe){
                    if (LOGGER.isLoggable(Level.WARNING)){
                        LOGGER.log(Level.WARNING, "Unable to parse the specified Max Height property.", nfe);
                    }
                }
            }
        }
        
        
        //TODO: Re-enable this or modify this once we get support for CRS with y as DISPLAY_DOWN
//        if (props.containsKey(Utils.ImageCollectionProperties.EPSG_CODE)) {
//            final String epsgCode = (String) props.get(Utils.ImageCollectionProperties.EPSG_CODE);
//            if (epsgCode != null && epsgCode.trim().length() > 0) {
//                this.epsgCode = Integer.parseInt(epsgCode);
//            }
//        }
        
        return coverage;
    }

    private void updatePropertiesFile(File propertiesFile) {
        if (!propertiesFile.exists()){
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(propertiesFile);
                Properties prop = new Properties();
                prop.put(Utils.ImageCollectionProperties.DEFAULT_PATH, defaultValues.path);
                prop.store(fos, null);
            } catch (IOException e) {
                if (LOGGER.isLoggable(Level.WARNING)){
                    LOGGER.log(Level.WARNING, "Unable to store properties in a config.property file ", e );
                }
            } finally {
                if (fos != null){
                    try {
                        fos.close();
                        fos = null;
                    } catch (Throwable t){
                        //Doesn nothing
                    }
                }
            }
        }
    }

    /**
     * @see org.opengis.coverage.grid.GridCoverageReader#getFormat()
     */
    public Format getFormat() {
        return new ImageCollectionFormat();
    }

    /**
     * @see org.opengis.coverage.grid.GridCoverageReader#read(org.opengis.parameter.GeneralParameterValue[])
     */
    @Override
    public GridCoverage2D read(GeneralParameterValue[] params)
            throws IOException {

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Reading image from " + sourceURL.toString() + "\nHighest res " 
                    + highestRes[0] + " " + highestRes[1]);
        }

        final Collection<GridCoverage2D> response = rasterManager.read(params);
        if (response.isEmpty()){
            if (LOGGER.isLoggable(Level.FINE)){
                LOGGER.fine("The response is empty. ==> returning a null GridCoverage");
            }
            return null;
        } else
            return response.iterator().next();
    }

    /**
     * Creates a {@link GridCoverage} for the provided {@link PlanarImage} using
     * the {@link #raster2Model} that was provided for this coverage.
     * 
     * <p>
     * This method is vital when working with coverages that have a raster to
     * model transformation that is not a simple scale and translate.
     * 
     * @param image
     *            contains the data for the coverage to create.
     * @param raster2Model
     *            is the {@link MathTransform} that maps from the raster space
     *            to the model space.
     * @return a {@link GridCoverage}
     * @throws IOException
     */
    protected final GridCoverage createCoverage(PlanarImage image,
            MathTransform raster2Model) throws IOException {

        // creating bands
        final SampleModel sm = image.getSampleModel();
        final ColorModel cm = image.getColorModel();
        final int numBands = sm.getNumBands();
        final GridSampleDimension[] bands = new GridSampleDimension[numBands];
        // setting bands names.

        for (int i = 0; i < numBands; i++) {
            final ColorInterpretation colorInterpretation = TypeMap.getColorInterpretation(cm, i);
            if (colorInterpretation == null)
                throw new IOException("Unrecognized sample dimension type");
            Category[] categories = null;
            bands[i] = new GridSampleDimension(colorInterpretation.name(), categories, null).geophysics(true);
        }
        // creating coverage
        if (raster2Model != null) {
            return coverageFactory.create(coverageName, image, crs, raster2Model, bands, null, null);
        }
        return coverageFactory.create(coverageName, image, new GeneralEnvelope(
                originalEnvelope), bands, null, null);

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

    int getNumberOfOverviews() {
        return super.numOverviews;
    }

    String getName() {
        return super.coverageName;
    }

}
