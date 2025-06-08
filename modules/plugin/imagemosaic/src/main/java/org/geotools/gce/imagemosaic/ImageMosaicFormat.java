/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006 - 2016, Open Source Geospatial Foundation (OSGeo)
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

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.jai.Interpolation;
import org.apache.commons.io.FilenameUtils;
import org.geotools.api.coverage.grid.Format;
import org.geotools.api.coverage.grid.GridCoverageWriter;
import org.geotools.api.data.DataAccessFactory.Param;
import org.geotools.api.data.DataStore;
import org.geotools.api.data.DataStoreFactorySpi;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.filter.Filter;
import org.geotools.api.parameter.GeneralParameterDescriptor;
import org.geotools.api.parameter.ParameterDescriptor;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.imageio.GeoToolsWriteParams;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.gce.imagemosaic.catalog.CatalogConfigurationBean;
import org.geotools.gce.imagemosaic.catalog.GranuleCatalog;
import org.geotools.parameter.DefaultParameterDescriptor;
import org.geotools.parameter.DefaultParameterDescriptorGroup;
import org.geotools.parameter.ParameterGroup;
import org.geotools.util.Converters;
import org.geotools.util.URLs;
import org.geotools.util.Utilities;
import org.geotools.util.factory.Hints;
import org.locationtech.jts.geom.Geometry;

/**
 * {@link AbstractGridFormat} subclass for controlling {@link ImageMosaicReader} creation. As the name says, it handles
 * mosaic of georeferenced images, which means
 *
 * <ol>
 *   <li>tiff+tfw+prj
 *   <li>jpeg+tfw+prj
 *   <li>png+tfw+prj
 *   <li>geotiff
 * </ol>
 *
 * This does not mean that you throw there a couple of images and it will do the trick no matter how these images are.
 * Requirements are:
 *
 * <ul>
 *   <li>(almost) equal spatial resolution
 *   <li>same number of bands
 *   <li>same data type
 *   <li>same projection
 * </ul>
 *
 * The first requirement can be relaxed a little but if they have the same spatial resolution the performances are much
 * better. There are parameters that you can use to control the behaviour of the mosaic in terms of thresholding and
 * transparency. They are as follows:
 *
 * <ul>
 *   <li>--DefaultParameterDescriptor FINAL_ALPHA = new DefaultParameterDescriptor( "FinalAlpha", Boolean.class, null,
 *       Boolean.FALSE)-- It asks the plugin to add transparency on the final created mosaic. IT simply performs a
 *       threshonding looking for areas where there is no data, i.e., intensity is really low and transform them into
 *       transparent areas. It is obvious that depending on the nature of the input images it might interfere with the
 *       original values.
 *   <li>---ALPHA_THRESHOLD = new DefaultParameterDescriptor( "AlphaThreshold", Double.class, null,
 *       Double.valueOf(1));--- Controls the transparency addition by specifying the treshold to use.
 *   <li>INPUT_IMAGE_THRESHOLD = new DefaultParameterDescriptor( "InputImageROI", Boolean.class, null, Boolean.FALSE)---
 *       INPUT_IMAGE_THRESHOLD_VALUE = new DefaultParameterDescriptor( "InputImageROIThreshold", Integer.class, null,
 *       Integer.valueOf(1));--- These two can be used to control the application of ROIs on the input images based on
 *       tresholding values. Basically using the threshold you can ask the mosaic plugin to load or not certain pixels
 *       of the original images.
 *
 * @author Simone Giannecchini (simboss), GeoSolutions
 * @author Stefan Alfons Krueger (alfonx), Wikisquare.de : Support for jar:file:foo.jar/bar.properties URLs
 * @since 2.3
 */
public final class ImageMosaicFormat extends AbstractGridFormat implements Format {

    static final double DEFAULT_ARTIFACTS_FILTER_PTILE_THRESHOLD = 0.1;

    /** Logger. */
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(ImageMosaicFormat.class);

    /** Filter tiles based on attributes from the input coverage */
    public static final ParameterDescriptor<Filter> FILTER =
            new DefaultParameterDescriptor<>("Filter", Filter.class, null, null);

    /** Control the type of the final mosaic. */
    public static final ParameterDescriptor<Boolean> FADING = new DefaultParameterDescriptor<>(
            "Fading", Boolean.class, new Boolean[] {Boolean.TRUE, Boolean.FALSE}, Boolean.FALSE);

    /** Control the transparency of the output coverage. */
    public static final ParameterDescriptor<Color> OUTPUT_TRANSPARENT_COLOR =
            new DefaultParameterDescriptor<>("OutputTransparentColor", Color.class, null, null);

    /** Control the thresholding on the input coverage */
    public static final ParameterDescriptor<Integer> MAX_ALLOWED_TILES =
            new DefaultParameterDescriptor<>("MaxAllowedTiles", Integer.class, null, Integer.valueOf(-1));

    /** Control the default artifact filter luminance thresholding on the input coverages */
    public static final ParameterDescriptor<Integer> DEFAULT_ARTIFACTS_FILTER_THRESHOLD =
            new DefaultParameterDescriptor<>("DefaultArtifactsFilterThreshold", Integer.class, null, Integer.MIN_VALUE);

    /** Control the artifact filter ptile thresholding */
    public static final ParameterDescriptor<Double> ARTIFACTS_FILTER_PTILE_THRESHOLD = new DefaultParameterDescriptor<>(
            "ArtifactsFilterPtileThreshold",
            Double.class,
            null,
            Double.valueOf(DEFAULT_ARTIFACTS_FILTER_PTILE_THRESHOLD));

    /**
     * Defines a virtual native resolution. It virtually represents the native resolution to be used after reading a
     * dataset before doing any other operation. It could be useful to limit somehow the data quality offered to the
     * final user. As an instance if we have data at resolutions 0.5m, 1m, 2m, 4m, 8m and the virtual native resolution
     * is 4m, and the request is looking for data at 2m resolution we should downsample the best nearest level to 4m and
     * then get back at 2m. This will result in potentially pixellated output. When specified, values should be always
     * positive.
     */
    public static final ParameterDescriptor<double[]> VIRTUAL_NATIVE_RESOLUTION =
            new DefaultParameterDescriptor<>("VirtualNativeResolution", double[].class, null, null);

    /** Control the threading behavior for this plugin. */
    public static final ParameterDescriptor<Boolean> ALLOW_MULTITHREADING = new DefaultParameterDescriptor<>(
            "AllowMultithreading", Boolean.class, new Boolean[] {Boolean.TRUE, Boolean.FALSE}, Boolean.FALSE);

    /** Control the background values for the output coverage */
    public static final ParameterDescriptor<double[]> BACKGROUND_VALUES =
            new DefaultParameterDescriptor<>("BackgroundValues", double[].class, null, null);

    /** Control the interpolation to be used in mosaicking */
    public static final ParameterDescriptor<Interpolation> INTERPOLATION = AbstractGridFormat.INTERPOLATION;

    /** Control the requested resolution calculation. */
    public static final ParameterDescriptor<Boolean> ACCURATE_RESOLUTION = new DefaultParameterDescriptor<>(
            "Accurate resolution computation",
            Boolean.class,
            new Boolean[] {Boolean.TRUE, Boolean.FALSE},
            Boolean.FALSE);

    /**
     * When this read parameter is set to true, the reader will produce output in the requested CRS, assuming it matches
     * one of the native CRSs, as reported by the #GridCoverage2DReader@MULTICRS_EPSGCODES metadata entry, and that the
     * requested grid geometry is expressed in said CRS. When set to false (default), then only the native CRS declared
     * by #GridCoverage2DReader@getCoordinateReferenceSystem will be produced in output
     */
    public static final ParameterDescriptor<Boolean> OUTPUT_TO_ALTERNATIVE_CRS = new DefaultParameterDescriptor<>(
            "Output To Alternative CRS", Boolean.class, new Boolean[] {Boolean.TRUE, Boolean.FALSE}, Boolean.FALSE);

    /**
     * Optional Sorting for the granules of the mosaic.
     *
     * <p>
     *
     * <p>It does work only with DBMS as indexes
     */
    public static final ParameterDescriptor<String> SORT_BY =
            new DefaultParameterDescriptor<>("SORTING", String.class, null, null);

    /**
     * Merging behavior for the various granules of the mosaic we are going to produce.
     *
     * <p>
     *
     * <p>This parameter controls whether we want to merge in a single mosaic or stack all the bands into the final
     * mosaic.
     */
    public static final ParameterDescriptor<String> MERGE_BEHAVIOR = new DefaultParameterDescriptor<>(
            "MergeBehavior",
            String.class,
            MergeBehavior.valuesAsStrings(),
            MergeBehavior.getDefault().toString());

    /**
     * Controls the removal of excess granules
     *
     * <p>
     *
     * <p>This parameter controls whether the mosaic will attempt to remove excess granules, that is, granules not
     * contributing pixels to the output, before performing the mosaicking. This is useful only if granules are
     * overlapping, do not enable otherwise.
     */
    public static final ParameterDescriptor<ExcessGranulePolicy> EXCESS_GRANULE_REMOVAL =
            new DefaultParameterDescriptor<>(
                    "ExcessGranuleRemoval",
                    ExcessGranulePolicy.class,
                    new ExcessGranulePolicy[] {ExcessGranulePolicy.NONE, ExcessGranulePolicy.ROI},
                    ExcessGranulePolicy.NONE);

    /**
     * mask (as a polygon in native coordinates) to be applied to the produced mosaic. At the moment, it will be ignored
     * in case of Heterogeneous CRS.
     */
    public static final ParameterDescriptor<Geometry> GEOMETRY_MASK =
            new DefaultParameterDescriptor<>("GeometryMask", Geometry.class, null, null);

    /** Control the Masking buffering (in raster size) */
    public static final ParameterDescriptor<Double> MASKING_BUFFER_PIXELS =
            new DefaultParameterDescriptor<>("MaskingBufferPixels", Double.class, null, Double.valueOf(-1));

    /**
     * Control whether to set the ROI property in the output mosaic (as an instance, even when background values are set
     * which usually results into setting a null ROI after the mosaic)
     */
    public static final ParameterDescriptor<Boolean> SET_ROI_PROPERTY = new DefaultParameterDescriptor<>(
            "SetRoiProperty", Boolean.class, new Boolean[] {Boolean.TRUE, Boolean.FALSE}, Boolean.FALSE);

    /** Creates an instance and sets the metadata. */
    public ImageMosaicFormat() {
        setInfo();
    }

    /** Sets the metadata information. */
    private void setInfo() {
        final HashMap<String, String> info = new HashMap<>();
        info.put("name", "ImageMosaic");
        info.put("description", "Image mosaicking plugin");
        info.put("vendor", "Geotools");
        info.put("docURL", "");
        info.put("version", "1.0");
        mInfo = info;

        // reading parameters
        readParameters =
                new ParameterGroup(new DefaultParameterDescriptorGroup(mInfo, new GeneralParameterDescriptor[] {
                    READ_GRIDGEOMETRY2D,
                    INPUT_TRANSPARENT_COLOR,
                    OUTPUT_TRANSPARENT_COLOR,
                    USE_JAI_IMAGEREAD,
                    BACKGROUND_VALUES,
                    SUGGESTED_TILE_SIZE,
                    ALLOW_MULTITHREADING,
                    MAX_ALLOWED_TILES,
                    TIME,
                    ELEVATION,
                    FILTER,
                    ACCURATE_RESOLUTION,
                    SORT_BY,
                    MERGE_BEHAVIOR,
                    FOOTPRINT_BEHAVIOR,
                    OVERVIEW_POLICY,
                    BANDS,
                    EXCESS_GRANULE_REMOVAL,
                    RESCALE_PIXELS
                }));

        // reading parameters
        writeParameters = null;
    }

    /** @see org.geotools.data.coverage.grid.AbstractGridFormat#getReader(Object) */
    @Override
    public ImageMosaicReader getReader(Object source) {
        return getReader(source, null);
    }

    /** */
    @Override
    public GridCoverageWriter getWriter(Object destination) {
        throw new UnsupportedOperationException("This plugin does not support writing.");
    }

    @Override
    public boolean accepts(Object source, Hints hints) {
        Utilities.ensureNonNull("source", source);
        if (source instanceof ImageMosaicDescriptor) {
            return checkDescriptor((ImageMosaicDescriptor) source);
        } else {
            return checkForUrl(source, hints);
        }
    }

    /** @see org.geotools.data.coverage.grid.AbstractGridFormat#accepts(Object input) */
    @Override
    public boolean accepts(Object source) {
        return accepts(source, null);
    }

    /** Checks that the provided {@link ImageMosaicDescriptor} is well formed. */
    private static boolean checkDescriptor(final ImageMosaicDescriptor source) {
        // TODO: improve checks
        final GranuleCatalog catalog = source.getCatalog();
        final MosaicConfigurationBean configuration = source.getConfiguration();
        if (configuration == null) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("Mosaic configuration is missing");
            }
            return false;
        }
        if (configuration.getLevels() == null) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("resolution leves is unavailable ");
            }

            return false;
        }
        if (catalog == null) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("Granule Catalog is unavailable ");
            }
            return false;
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    private boolean checkForUrl(Object source, Hints hints) {
        try {
            if (hints != null
                    && hints.containsKey(Utils.EXCLUDE_MOSAIC)
                    && (Boolean) hints.get(Utils.EXCLUDE_MOSAIC) == true) {
                return false;
            }

            // Minimal check. In case we found the indexer we say that we can deal with that mosaic
            // An additional getReader may confirm or deny that in case.
            boolean indexerFound = Utils.minimalIndexCheck(source);
            if (indexerFound) {
                return true;
            }

            //
            // Check source
            //
            // if it is a URL or a String let's try to see if we can get a file to
            // check if we have to build the index
            ImageMosaicReader reader = getReader(source, hints);
            if (reader != null) {
                // TODO: It's inefficient
                reader.dispose();
                return true;
            }

            URL sourceURL = Utils.checkSource(source, hints);
            if (sourceURL == null) {
                return false;
            }
            if (source instanceof File) {
                File file = (File) source;
                if (!file.exists()) {
                    return false; // file does not exist
                }
            }

            //
            // Load tiles informations, especially the bounds, which will be
            // reused
            //
            DataStore tileIndexStore = null;
            CoordinateReferenceSystem crs = null;
            boolean shapefile = true;
            try {
                final File sourceF = URLs.urlToFile(sourceURL);
                if (FilenameUtils.getName(sourceF.getAbsolutePath()).equalsIgnoreCase(Utils.DATASTORE_PROPERTIES)) {
                    shapefile = false;
                    // load spi anche check it
                    // read the properties file
                    final Properties properties = new Properties();
                    try (FileInputStream stream = new FileInputStream(sourceF)) {
                        properties.load(stream);
                    }

                    // SPI
                    final String SPIClass = properties.getProperty("SPI");
                    // create a datastore as instructed
                    final DataStoreFactorySpi spi = (DataStoreFactorySpi)
                            Class.forName(SPIClass).getDeclaredConstructor().newInstance();

                    // get the params
                    final Map<String, Serializable> params = new HashMap<>();
                    final Param[] paramsInfo = spi.getParametersInfo();
                    for (Param p : paramsInfo) {
                        // search for this param and set the value if found
                        if (properties.containsKey(p.key))
                            params.put(p.key, (Serializable) Converters.convert(properties.getProperty(p.key), p.type));
                        else if (p.required && p.sample == null) {
                            if (LOGGER.isLoggable(Level.FINE))
                                LOGGER.fine("Required parameter missing: " + p.toString());
                            return false;
                        }
                    }
                    // H2 workadound
                    if (Utils.isH2Store(spi)) {
                        Utils.fixH2DatabaseLocation(
                                params, URLs.fileToUrl(sourceF.getParentFile()).toExternalForm());
                    }

                    tileIndexStore = spi.createDataStore(params);
                    if (tileIndexStore == null) return false;

                } else {
                    URL testPropertiesUrl = URLs.changeUrlExt(sourceURL, "properties");
                    File testFile = URLs.urlToFile(testPropertiesUrl);
                    if (!testFile.exists()) {
                        return false;
                    }

                    ShapefileDataStore store = new ShapefileDataStore(sourceURL);
                    store.setTimeZone(Utils.UTC_TIME_ZONE);
                    tileIndexStore = store;
                }

                //
                // Now look for the properties file and try to parse relevant fields
                //
                URL propsUrl = null;
                if (shapefile) propsUrl = URLs.changeUrlExt(sourceURL, "properties");
                else {
                    //
                    // do we have a datastore properties file? It will preempt on the shapefile
                    //
                    final File parent = URLs.urlToFile(sourceURL).getParentFile();

                    // this can be used to look for properties files that do NOT define a datastore
                    final File[] properties = parent.listFiles(Utils.MOSAIC_PROPERTY_FILTER);

                    // do we have a valid datastore + mosaic properties pair?
                    if (properties != null) {
                        for (File propFile : properties) {
                            if (Utils.checkFileReadable(propFile)
                                    && Utils.loadMosaicProperties(URLs.fileToUrl(propFile)) != null) {
                                propsUrl = URLs.fileToUrl(propFile);
                                break;
                            }
                        }
                    }
                }

                // get the properties file
                if (propsUrl == null) return false;
                final MosaicConfigurationBean configuration = Utils.loadMosaicProperties(propsUrl);
                if (configuration == null) return false;

                CatalogConfigurationBean catalogBean = configuration.getCatalogConfigurationBean();
                // we need the type name with a DB to pick up the right table
                // for shapefiles this can be null so taht we select the first and ony one
                String typeName = catalogBean.getTypeName();
                if (typeName == null) {
                    final String[] typeNames = tileIndexStore.getTypeNames();
                    if (typeNames.length <= 0) return false;
                    typeName = typeNames[0];
                }
                if (typeName == null) return false;

                // now try to connect to the index
                SimpleFeatureSource featureSource = null;
                try {
                    featureSource = tileIndexStore.getFeatureSource(typeName);
                } catch (Exception e) {
                    featureSource = tileIndexStore.getFeatureSource(typeName.toUpperCase());
                }
                if (featureSource == null) {
                    return false;
                }

                final SimpleFeatureType schema = featureSource.getSchema();
                if (schema == null) {
                    return false;
                }

                crs = featureSource.getSchema().getGeometryDescriptor().getCoordinateReferenceSystem();
                if (crs == null) return false;
                // looking for the location attribute
                final String locationAttributeName = catalogBean.getLocationAttribute();
                if (locationAttributeName != null
                        && schema != null
                        && schema.getDescriptor(locationAttributeName) == null
                        && schema.getDescriptor(locationAttributeName.toUpperCase()) == null) {
                    return false;
                }

                return true;

            } finally {
                try {
                    if (tileIndexStore != null) tileIndexStore.dispose();
                } catch (Throwable e) {
                    if (LOGGER.isLoggable(Level.FINE)) LOGGER.log(Level.FINE, e.getLocalizedMessage(), e);
                }
            }

        } catch (Throwable e) {
            if (LOGGER.isLoggable(Level.FINE)) LOGGER.log(Level.FINE, e.getLocalizedMessage(), e);
            return false;
        }
    }

    /** @see AbstractGridFormat#getReader(Object, Hints) */
    @Override
    public ImageMosaicReader getReader(Object source, Hints hints) {
        try {
            if (hints == null) {
                hints = new Hints(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE);
            }
            final ImageMosaicReader reader = new ImageMosaicReader(source, hints);
            return reader;
        } catch (MalformedURLException e) {
            if (LOGGER.isLoggable(Level.WARNING)) LOGGER.log(Level.WARNING, e.getLocalizedMessage(), e);
            return null;
        } catch (IOException e) {
            if (LOGGER.isLoggable(Level.FINE)) LOGGER.log(Level.FINE, e.getLocalizedMessage(), e);
            return null;
        }
    }

    /**
     * Throw an exception since this plugin is readonly.
     *
     * @return nothing.
     */
    @Override
    public GeoToolsWriteParams getDefaultImageIOWriteParameters() {
        throw new UnsupportedOperationException("Unsupported method.");
    }

    @Override
    public GridCoverageWriter getWriter(Object destination, Hints hints) {
        throw new UnsupportedOperationException("This plugin does not support writing.");
    }
}
