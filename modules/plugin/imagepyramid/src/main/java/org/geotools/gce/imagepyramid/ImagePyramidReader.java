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
package org.geotools.gce.imagepyramid;

import it.geosolutions.imageio.maskband.DatasetLayout;

import java.awt.Rectangle;
import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.channels.Channels;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageReadParam;
import javax.media.jai.ImageLayout;

import org.apache.commons.io.IOUtils;
import org.geotools.coverage.CoverageFactoryFinder;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.OverviewPolicy;
import org.geotools.data.DataSourceException;
import org.geotools.data.DataUtilities;
import org.geotools.data.PrjFileReader;
import org.geotools.factory.Hints;
import org.geotools.gce.imagemosaic.ImageMosaicReader;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.operation.builder.GridToEnvelopeMapper;
import org.opengis.coverage.grid.Format;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.coverage.grid.GridCoverageReader;
import org.opengis.coverage.grid.GridEnvelope;
import org.opengis.geometry.Envelope;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.parameter.ParameterValue;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.datum.PixelInCell;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

/**
 * This reader is responsible for providing access to a pyramid of mosaics of georeferenced
 * coverages that are read directly through imageio readers, like tiff, pngs, etc...
 * 
 * <p>
 * Specifically this plugin relies on the image mosaic plugin to handle each single level of
 * resolutions available, hence all the magic is done inside the mosaic plugin.
 * 
 * 
 * <p>
 * For information on how to build a mosaic, please refer to the {@link ImageMosaicReader}
 * documentation.
 * 
 * <p>
 * If you are looking for information on how to create a pyramid, here you go.
 * 
 * The pyramid itself does no magic. All the magic is performed by the single mosaic readers that
 * are polled depending on the requeste resolution levels. Therefore the <b>first step</b> is having
 * a mosaic of images like geotiff, tiff, jpeg, or png which is going to be the base for the
 * pyramid.
 * 
 * <p>
 * The <b>second step</b> is to build the next (lower resolution) levels for the pyramid. <br>
 * If you look inside the spike dire of the geotools project you will find a (growing) set of tools
 * that can be used for doing processing on coverages. <br>
 * Specifically there is one tool called PyramidBuilder that can be used to build the pyramid level
 * by level.
 * 
 * <p>
 * <b>Last step</b> is providing a prj file with the projection of the pyramid (btw all the levels
 * has to be in the same projection) as well as a properties file with this structure:
 * 
 * <pre>
 *           #
 *           #Mon Aug 21 22:23:27 CEST 2006
 *           #name of the coverage
 *           Name=ikonos
 *           #different resolution levels available
 *           Levels=1.2218682749859724E-5,9.220132503102996E-6 2.4428817977683634E-5,1.844026500620314E-5 4.8840552865873626E-5,3.686350299024973E-5 9.781791400307775E-5,7.372700598049946E-5 1.956358280061555E-4,1.4786360643866836E-4 3.901787184256844E-4,2.9572721287731037E-4
 *           #where all the levels reside
 *           LevelsDirs=0 2 4 8 16 32
 *           #number of levels availaible
 *           LevelsNum=6
 *           #envelope for this pyramid
 *           Envelope2D=13.398228477973406,43.591366397808976 13.537912459169803,43.67121274528585
 * </pre>
 * 
 * <p>
 * Starting with 16.x ImagePyramid can now support ImageMosaics with inner overviews.
 * See {@link ImageLevelsMapper} for additional details of the Levels entry of 
 * a pyramid of mosaics with inner overviews.
 * 
 * @author Simone Giannecchini
 * @author Stefan Alfons Krueger (alfonx), Wikisquare.de : Support for
 *         jar:file:foo.jar/bar.properties like URLs
 * @since 2.3
 * 
 *
 *
 * @source $URL$
 */
public final class ImagePyramidReader extends AbstractGridCoverage2DReader implements
        GridCoverageReader {

    /** Logger. */
    private final static Logger LOGGER = org.geotools.util.logging.Logging
            .getLogger(ImagePyramidReader.class.toString());

    /**
     * The input properties file to read the pyramid information from.
     */
    private URL sourceURL;

    private String[] coverageNames;

    private int count = 1;

    private ImageLevelsMapper imageLevelsMapper;

    /**
     * Constructor for an {@link ImagePyramidReader}.
     * 
     * @param source The source object.
     * @param uHints {@link Hints} to control the behaviour of this reader.
     * @throws IOException
     * @throws UnsupportedEncodingException
     * 
     */
    public ImagePyramidReader(Object source, Hints uHints) throws IOException {
        // //
        //
        // managing hints
        //
        // //
        if (this.hints == null)
            this.hints = new Hints();
        if (uHints != null) {
            this.hints.add(uHints);
        }
        this.coverageFactory = CoverageFactoryFinder.getGridCoverageFactory(this.hints);

        // Check source
        if (source == null) {
            throw new DataSourceException(
                    "ImagePyramidReader:null source set to read this coverage.");
        }
        this.source = source;
        this.sourceURL = Utils.checkSource(source, uHints);
        if (sourceURL == null) {
            throw new DataSourceException(
                    "This plugin accepts only a URL, a File or a String pointing to a directory with a structure similar to the one of gdal_retile!");
        }

        // get the crs if able to
        final URL prjURL = DataUtilities.changeUrlExt(sourceURL, "prj");
        PrjFileReader crsReader = null;
        try {
            crsReader = new PrjFileReader(Channels.newChannel(prjURL.openStream()));
        } catch (FactoryException e) {
            throw new DataSourceException(e);
        } finally {
            try {
                crsReader.close();
            } catch (Throwable e) {
                if (LOGGER.isLoggable(Level.FINE))
                    LOGGER.log(Level.FINE, e.getLocalizedMessage(), e);
            }
        }
        final Object tempCRS = hints.get(Hints.DEFAULT_COORDINATE_REFERENCE_SYSTEM);
        if (tempCRS != null) {
            this.crs = (CoordinateReferenceSystem) tempCRS;
            LOGGER.log(Level.WARNING, "Using forced coordinate reference system " + crs.toWKT());
        } else {
            final CoordinateReferenceSystem tempcrs = crsReader.getCoordinateReferenceSystem();
            if (tempcrs == null) {
                // use the default crs
                crs = AbstractGridFormat.getDefaultCRS();
                LOGGER.log(
                        Level.WARNING,
                        "Unable to find a CRS for this coverage, using a default one: "
                                + crs.toWKT());
            } else
                crs = tempcrs;
        }

        // Load properties file with information about levels and envelope
        parseMainFile(sourceURL);
    }

    /**
     * Parses the main properties file loading the information regarding geographic extent and
     * overviews.
     * 
     * @param sourceFile
     * @throws IOException
     * @throws FileNotFoundException
     */
    private void parseMainFile(final URL sourceURL) throws IOException {

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Parsing pyramid properties file at:" + sourceURL.toExternalForm());
        }
        BufferedInputStream propertyStream = null;
        InputStream openStream = null;
        try {
            openStream = sourceURL.openStream();
            propertyStream = new BufferedInputStream(openStream);
            final Properties properties = new Properties();
            properties.load(propertyStream);

            // load the envelope
            final String envelope = properties.getProperty("Envelope2D");
            String[] pairs = envelope.split(" ");
            final double cornersV[][] = new double[2][2];
            String pair[];
            for (int i = 0; i < 2; i++) {
                pair = pairs[i].split(",");
                cornersV[i][0] = Double.parseDouble(pair[0]);
                cornersV[i][1] = Double.parseDouble(pair[1]);
            }
            this.originalEnvelope = new GeneralEnvelope(cornersV[0], cornersV[1]);
            this.originalEnvelope.setCoordinateReferenceSystem(crs);

            imageLevelsMapper = new ImageLevelsMapper(properties);

            numOverviews = imageLevelsMapper.getNumOverviews();
            overViewResolutions = imageLevelsMapper.getOverViewResolutions();
            highestRes = imageLevelsMapper.getHighestResolution();

            // name
            coverageName = properties.getProperty("Name");
            if (coverageName != null) {
                if (coverageName.contains(",")) {
                    coverageNames = coverageName.split(",");
                    coverageName = coverageNames[0];
                } else {
                    coverageNames = new String[] { coverageName };
                }
                count = coverageNames.length;
            }

            // original gridrange (estimated)
            originalGridRange = new GridEnvelope2D(new Rectangle((int) Math.round(originalEnvelope
                    .getSpan(0) / highestRes[0]), (int) Math.round(originalEnvelope.getSpan(1)
                    / highestRes[1])));
            final GridToEnvelopeMapper geMapper = new GridToEnvelopeMapper(originalGridRange,
                    originalEnvelope);
            geMapper.setPixelAnchor(PixelInCell.CELL_CORNER);
            raster2Model = geMapper.createTransform();

            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("Parsed pyramid properties file at:" + sourceURL.toExternalForm());
            }
        } finally {
            // close input stream
            if (propertyStream != null)
                IOUtils.closeQuietly(propertyStream);

            if (openStream != null)
                IOUtils.closeQuietly(openStream);
        }

    }

    /**
     * Constructor for an {@link ImagePyramidReader}.
     * 
     * @param source The source object.
     * @throws IOException
     * @throws UnsupportedEncodingException
     * 
     */
    public ImagePyramidReader(Object source) throws IOException {
        this(source, null);
    }

    /**
     * @see org.opengis.coverage.grid.GridCoverageReader#getFormat()
     */
    public Format getFormat() {
        return new ImagePyramidFormat();
    }

    @Override
    public GridCoverage2D read(GeneralParameterValue[] params) throws IOException {
        return read(coverageName, params);
    }

    @Override
    public GridEnvelope getOriginalGridRange(String coverageName) {
        return getFirstLevelReader(coverageName, false).getOriginalGridRange(
                getReaderCoverageName(coverageName));
    }

    @Override
    public CoordinateReferenceSystem getCoordinateReferenceSystem(String coverageName) {
        return getFirstLevelReader(coverageName, false).getCoordinateReferenceSystem(
                getReaderCoverageName(coverageName));
    }

    @Override
    public GeneralEnvelope getOriginalEnvelope(String coverageName) {
        return getFirstLevelReader(coverageName, false).getOriginalEnvelope(
                getReaderCoverageName(coverageName));
    }

    @Override
    public MathTransform getOriginalGridToWorld(String coverageName, PixelInCell pixInCell) {
        return getFirstLevelReader(coverageName, false).getOriginalGridToWorld(
                getReaderCoverageName(coverageName), pixInCell);
    }

    @Override
    public Set<ParameterDescriptor<List>> getDynamicParameters(String coverageName) {
        return getFirstLevelReader(coverageName, false).getDynamicParameters(
                getReaderCoverageName(coverageName));
    }

    @Override
    public int getNumOverviews(String coverageName) {
        return getFirstLevelReader(coverageName, false).getNumOverviews(
                getReaderCoverageName(coverageName));
    }

    @Override
    public DatasetLayout getDatasetLayout(String coverageName) {
        return getFirstLevelReader(coverageName, false).getDatasetLayout(
                getReaderCoverageName(coverageName));
    }

    @Override
    public ImageLayout getImageLayout(String coverageName) throws IOException {
        return getFirstLevelReader(coverageName, false).getImageLayout(
                getReaderCoverageName(coverageName));
    }

    @Override
    public double[][] getResolutionLevels(String coverageName) throws IOException {
        return getFirstLevelReader(coverageName, false).getResolutionLevels(
                getReaderCoverageName(coverageName));
    }

    @Override
    public GridCoverage2D read(final String coverageName, GeneralParameterValue[] params)
            throws IOException {

        GeneralEnvelope requestedEnvelope = null;
        Rectangle dim = null;
        OverviewPolicy overviewPolicy = null;
        if (params != null) {
            // /////////////////////////////////////////////////////////////////////
            //
            // Checking params
            //
            // /////////////////////////////////////////////////////////////////////
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    @SuppressWarnings("rawtypes")
                    final ParameterValue param = (ParameterValue) params[i];
                    if (param == null) {
                        continue;
                    }
                    final String name = param.getDescriptor().getName().getCode();
                    if (name.equals(AbstractGridFormat.READ_GRIDGEOMETRY2D.getName().toString())) {
                        final GridGeometry2D gg = (GridGeometry2D) param.getValue();
                        requestedEnvelope = new GeneralEnvelope((Envelope) gg.getEnvelope2D());
                        dim = gg.getGridRange2D().getBounds();
                        continue;
                    }
                    if (name.equals(AbstractGridFormat.OVERVIEW_POLICY.getName().toString())) {
                        overviewPolicy = (OverviewPolicy) param.getValue();
                        continue;
                    }
                }
            }
        }

        //
        // Loading tiles
        //
        return loadRequestedTiles(coverageName, requestedEnvelope, dim, params, overviewPolicy);
    }

    /**
     * This method loads the tiles which overlap the requested envelope using the provided values
     * for alpha and input ROI.
     * 
     * @param coverageName
     * @param requestedEnvelope
     * @param dim
     * @param params
     * @param overviewPolicy
     * @return A {@link GridCoverage}, well actually a {@link GridCoverage2D}.
     * @throws TransformException
     * @throws IOException
     */
    private GridCoverage2D loadRequestedTiles(final String coverageName,
            GeneralEnvelope requestedEnvelope, Rectangle dim, GeneralParameterValue[] params,
            OverviewPolicy overviewPolicy) throws IOException {

        // if we get here we have something to load

        // compute the requested resolution
        final ImageReadParam readP = new ImageReadParam();
        Integer imageChoice = 0;
        if (dim != null) {
            try {
                imageChoice = setReadParams(overviewPolicy, readP, requestedEnvelope, dim);
            } catch (TransformException e) {
                throw new DataSourceException(e);
            }
        }
            
        // Check to have the needed reader in memory
        // light check to see if this reader had been disposed, not synch-ing for performance.
        if (!imageLevelsMapper.hasReaders()) {
            throw new IllegalStateException("This ImagePyramidReader has already been disposed");
        }

        ImageMosaicReader reader = getImageMosaicReaderForLevel(coverageName, imageChoice);

        //
        // Abusing of the created ImageMosaicreader for getting a
        // gridcoverage2d, then rename it
        //
        GridCoverage2D mosaicCoverage = reader.read(getReaderCoverageName(coverageName), params);
        if (mosaicCoverage != null) {
            return new GridCoverage2D(coverageName, mosaicCoverage);
        } else {
            // the mosaic can still return null in corner cases, handle that gracefully
            return null;
        }
    }

    /**
     * @see org.opengis.coverage.grid.GridCoverageReader#dispose()
     */
    @Override
    public synchronized void dispose() {
        super.dispose();
        imageLevelsMapper.dispose();
    }

    @Override
    public String[] getGridCoverageNames() {
        return coverageNames;
    }

    /**
     * @return the number of coverages for this reader.
     */
    @Override
    public int getGridCoverageCount() {
        return count;
    }

    /**
     * Retrieve meta data value from requested coverage and for requested metadata
     * 
     * @param coverageName
     * @param name
     * @return
     */
    @Override
    public String getMetadataValue(final String coverageName, final String name) {
        return getImageMosaicMetadataValue(coverageName, name);

    }

    /**
     * Retrieve data value for requested metadata
     */
    public String getMetadataValue(final String name) {
        return getImageMosaicMetadataValue(coverageName, name);
    }

    private ImageMosaicReader getFirstLevelReader(String coverageName) {
        return getFirstLevelReader(coverageName, false);
    }

    private ImageMosaicReader getFirstLevelReader(String coverageName, boolean canBeNull) {
        ImageMosaicReader reader = null; 
        try {
            reader = getImageMosaicReaderForLevel(coverageName, 0);
        } catch (IOException e) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.log(Level.WARNING, "Could not get reader for datasource.", e);
            }
            if (reader == null && !canBeNull){
                throw new IllegalArgumentException(
                    "Could not get reader for the specified coverageName: " + coverageName, e);
            }
        }
        return reader;
    }

    private String getImageMosaicMetadataValue(final String coverageName, final String name) {
        ImageMosaicReader firstLevelReader = getFirstLevelReader(coverageName);
        if (firstLevelReader == null) {
            return null;
        }

        if (name.equalsIgnoreCase(HAS_TIME_DOMAIN)) {
            return String.valueOf(this.hasTimeDomain(coverageName, firstLevelReader));
        }

        if (TIME_DOMAIN.equalsIgnoreCase(name) || TIME_DOMAIN_MAXIMUM.equalsIgnoreCase(name)
                || TIME_DOMAIN_MINIMUM.equalsIgnoreCase(name)) {
            if (this.hasTimeDomain(coverageName, firstLevelReader)) {
                return this.getTimeDomain(coverageName, firstLevelReader, name);
            }
        }
        return firstLevelReader.getMetadataValue(getReaderCoverageName(coverageName), name);

    }

    @Override
    public String[] getMetadataNames(final String coverageName) {
        // Looks like the metadaNames are fixed on pyramid
        return getMetadataNames();
    }

    public String[] getMetadataNames() {
        final String[] parentNames = super.getMetadataNames();
        final List<String> metadataNames = new ArrayList<String>();
        metadataNames.add(TIME_DOMAIN);
        metadataNames.add(HAS_TIME_DOMAIN);
        metadataNames.add(TIME_DOMAIN_MINIMUM);
        metadataNames.add(TIME_DOMAIN_MAXIMUM);
        metadataNames.add(TIME_DOMAIN_RESOLUTION);

        if (parentNames != null)
            metadataNames.addAll(Arrays.asList(parentNames));
        return metadataNames.toArray(new String[metadataNames.size()]);
    }

    /**
     * Retrieve time domains metadata values for the requested ImageMosaicReader
     * 
     * @param reader
     * @param name
     * @return
     */
    private String getTimeDomain(final String coverageName, ImageMosaicReader reader,
            final String name) {
        if (hasTimeDomain(coverageName, reader) && reader != null) {
            return reader.getMetadataValue(getReaderCoverageName(coverageName), name);
        }
        return null;
    }

    /**
     * Verify if the requested Mosaic has a time domain configuration
     * 
     * @param reader
     * @return True if has time domain configuration
     */
    private boolean hasTimeDomain(final String coverageName, ImageMosaicReader reader) {
        if (reader != null) {
            String strHasTimeDomain = reader.getMetadataValue(getReaderCoverageName(coverageName),
                    HAS_TIME_DOMAIN);
            return Boolean.parseBoolean(strHasTimeDomain);
        }
        return false;
    }

    /**
     * Retrieve the ImageMosaicReader for the requested Level and load if necessary
     * 
     * @return ImageMosaicReader for level
     * */
    public ImageMosaicReader getImageMosaicReaderForLevel(Integer imageChoice)
            throws IOException {
        return getImageMosaicReaderForLevel(coverageName, imageChoice);
    }
    
    /**
     * Retrieve the ImageMosaicReader for the requested Level and load if necessary
     * 
     * @return ImageMosaicReader for level
     * */
    public ImageMosaicReader getImageMosaicReaderForLevel(String coverageName, Integer imageChoice)
            throws IOException {
        return imageLevelsMapper.getReader(imageChoice, coverageName, sourceURL, hints);
    }

    private String getReaderCoverageName(String coverageName) {
        // When dealing with a single coverage, ImagePyramid exposes a coverageName
        // whilst the underlying ImageMosaic may have a different one
        return count > 1 ? coverageName : ImageMosaicReader.UNSPECIFIED;
    }

}
