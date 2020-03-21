/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.imagemosaic.catalog;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.geotools.coverage.grid.io.footprint.FootprintGeometryProvider;
import org.geotools.coverage.grid.io.footprint.FootprintLoader;
import org.geotools.coverage.grid.io.footprint.FootprintLoaderSpi;
import org.geotools.coverage.grid.io.footprint.MultiLevelROIProvider;
import org.geotools.coverage.grid.io.footprint.MultiLevelROIProviderFactory;
import org.geotools.coverage.grid.io.footprint.SidecarFootprintProvider;
import org.geotools.filter.text.ecql.ECQL;
import org.geotools.util.URLs;
import org.geotools.util.factory.Hints;
import org.opengis.filter.Filter;

/**
 * Factory class used for returning a {@link MultiLevelROIProvider} based on the input footprint
 * properties and files for mosaics
 *
 * @author Andrea Aime GeoSolutions
 * @author Nicola Lagomarsini GeoSolutions
 */
public class MultiLevelROIProviderMosaicFactory extends MultiLevelROIProviderFactory {

    public static final String FILTER_PROPERTY = "footprint_filter";

    private MultiLevelROIProviderMosaicFactory() {}

    public static MultiLevelROIProvider createFootprintProvider(File mosaicFolder) {
        return createFootprintProvider(mosaicFolder, (Hints) null);
    }

    /**
     * Builds a footprint provider from mosaic location
     *
     * @param mosaicFolder The folder that contains the mosaic config files
     */
    public static MultiLevelROIProvider createFootprintProvider(File mosaicFolder, Hints hints) {
        File configFile = new File(mosaicFolder, "footprints.properties");
        final Properties properties = initProperties(configFile);

        // load the type of config file
        String source = (String) properties.get(MultiLevelROIProviderFactory.SOURCE_PROPERTY);
        FootprintGeometryProvider provider = null;
        if (source == null) {
            // see if we have the default whole mosaic footprint
            File defaultShapefileFootprint = new File(mosaicFolder, "footprints.shp");
            if (defaultShapefileFootprint.exists()) {
                provider =
                        buildShapefileSource(
                                mosaicFolder, defaultShapefileFootprint.getName(), properties);
            } else {
                provider = new SidecarFootprintProvider(mosaicFolder);
            }
        } else if (MultiLevelROIProviderFactory.TYPE_SIDECAR.equals(source)) {
            provider = new SidecarFootprintProvider(mosaicFolder);
        } else if (source.toLowerCase().endsWith(".shp")) {
            provider = buildShapefileSource(mosaicFolder, source, properties);
        } else if (MultiLevelROIProviderFactory.TYPE_RASTER.equals(source)) {
            // Raster masking
            return new MultiLevelROIRasterProvider(mosaicFolder);
        } else if (MultiLevelROIProviderFactory.TYPE_MULTIPLE_SIDECAR.equals(source)) {
            return createMultiLevelROIOverviewsProvider(mosaicFolder, properties, hints);
        } else {
            throw new IllegalArgumentException(
                    "Invalid source type, it should be a reference "
                            + "to a shapefile or 'sidecar', but was '"
                            + source
                            + "' instead");
        }

        // Create the provider
        return createProvider(provider, properties, null);
    }

    /** Create a {@link MultiLevelROIGeometryOverviewsProvider} based on specified properties */
    private static MultiLevelROIProvider createMultiLevelROIOverviewsProvider(
            File mosaicFolder, Properties properties, Hints hints) {

        // Getting the footprint loader SPI
        String footprintLoaderSpi =
                (String)
                        properties.get(MultiLevelROIGeometryOverviewsProvider.FOOTPRINT_LOADER_SPI);

        // Getting the footprint loader SPI for overviews (it may be null).
        String overviewsFootprintLoaderSpi =
                (String)
                        properties.get(
                                MultiLevelROIGeometryOverviewsProvider
                                        .OVERVIEWS_FOOTPRINT_LOADER_SPI);

        // Get the overviews suffix String format
        String overviewsSuffixFormat =
                (String)
                        properties.getProperty(
                                MultiLevelROIGeometryOverviewsProvider.OVERVIEWS_SUFFIX_FORMAT_KEY,
                                MultiLevelROIGeometryOverviewsProvider
                                        .DEFAULT_OVERVIEWS_SUFFIX_FORMAT);

        // Whether overviewsROI are provided in raster space (or model space)
        String overviewsRoiInRasterSpaceString =
                (String)
                        properties.getProperty(
                                MultiLevelROIGeometryOverviewsProvider
                                        .OVERVIEWS_ROI_IN_RASTER_SPACE_KEY);
        boolean overviewsRoiInRasterSpace =
                overviewsRoiInRasterSpaceString != null
                        ? Boolean.parseBoolean(overviewsRoiInRasterSpaceString)
                        : MultiLevelROIGeometryOverviewsProvider
                                .DEFAULT_OVERVIEWS_ROI_IN_RASTER_SPACE;

        FootprintLoaderSpi spi = null;
        FootprintLoader footprintLoader = null;
        FootprintLoader overviewsFootprintLoader = null;
        try {
            if (footprintLoaderSpi != null) {
                spi =
                        (FootprintLoaderSpi)
                                Class.forName(footprintLoaderSpi)
                                        .getDeclaredConstructor()
                                        .newInstance();
                footprintLoader = spi.createLoader();
                overviewsFootprintLoader = footprintLoader;

                if (overviewsFootprintLoaderSpi != null) {
                    // Use dedicate LoaderSPI for overviews
                    spi =
                            (FootprintLoaderSpi)
                                    Class.forName(overviewsFootprintLoaderSpi)
                                            .getDeclaredConstructor()
                                            .newInstance();
                    overviewsFootprintLoader = spi.createLoader();
                }
            }

            return new MultiLevelROIGeometryOverviewsProvider(
                    mosaicFolder,
                    overviewsSuffixFormat,
                    MultiLevelROIGeometryOverviewsProvider.LOOK_FOR_OVERVIEWS,
                    footprintLoader,
                    overviewsFootprintLoader,
                    overviewsRoiInRasterSpace,
                    hints);
        } catch (InstantiationException
                | IllegalAccessException
                | ClassNotFoundException
                | NoSuchMethodException
                | InvocationTargetException e) {
            throw new IllegalArgumentException(
                    "Exception occurred while creating FootprintLoader", e);
        }
    }

    private static FootprintGeometryProvider buildShapefileSource(
            File mosaicFolder, String location, Properties properties) {
        File shapefile = new File(location);
        if (!shapefile.isAbsolute()) {
            shapefile = new File(mosaicFolder, location);
        }

        try {
            if (!shapefile.exists()) {
                throw new IllegalArgumentException(
                        "Tried to load the footprints from "
                                + shapefile.getCanonicalPath()
                                + " but the file was not found");
            } else {
                final Map<String, Serializable> params = new HashMap<String, Serializable>();
                params.put("url", URLs.fileToUrl(shapefile));
                String cql = (String) properties.get(FILTER_PROPERTY);
                Filter filter = null;
                if (cql != null) {
                    filter = ECQL.toFilter(cql);
                } else {
                    filter = ECQL.toFilter("location = granule.location");
                }
                String typeName = shapefile.getName();
                typeName = typeName.substring(0, typeName.lastIndexOf('.'));
                return new GTDataStoreFootprintProvider(params, typeName, filter);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    "Failed to create a shapefile based footprint provider", e);
        }
    }
}
