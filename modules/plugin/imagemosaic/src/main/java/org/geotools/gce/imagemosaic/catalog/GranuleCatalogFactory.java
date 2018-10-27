/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007 - 2016, Open Source Geospatial Foundation (OSGeo)
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
import java.net.URL;
import java.util.Properties;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FilenameUtils;
import org.geotools.coverage.util.CoverageUtilities;
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.data.Repository;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.gce.imagemosaic.Utils;
import org.geotools.util.URLs;
import org.geotools.util.factory.Hints;
import org.geotools.util.logging.Logging;

/**
 * Simple Factory class for creating {@link GranuleCatalog} instance to handle the catalog of
 * granules for this mosaic.
 *
 * @author Simone Giannecchini, GeoSolutions SAS
 */
public abstract class GranuleCatalogFactory {

    private static final Logger LOGGER = Logging.getLogger(GranuleCatalogFactory.class);

    /** Default private constructor to enforce singleton */
    private GranuleCatalogFactory() {}

    public static GranuleCatalog createGranuleCatalog(
            final Properties params,
            final boolean caching,
            final boolean create,
            final DataStoreFactorySpi spi,
            final Hints hints) {
        // build the catalog
        Repository repository = (Repository) hints.get(Hints.REPOSITORY);
        String storeName = (String) params.get(Utils.Prop.STORE_NAME);
        AbstractGTDataStoreGranuleCatalog catalog;
        if (storeName != null && !storeName.trim().isEmpty()) {
            if (repository == null) {
                throw new IllegalArgumentException(
                        "Was given a store name "
                                + storeName
                                + " but there is no Repository to resolve it");
            } else {
                catalog =
                        new RepositoryDataStoreCatalog(
                                params, create, repository, storeName, spi, hints);
            }
        } else {
            catalog = new GTDataStoreGranuleCatalog(params, create, spi, hints);
        }

        // caching wrappers
        if (caching) {
            return new STRTreeGranuleCatalog(params, catalog, hints);
        } else {
            return new CachingDataStoreGranuleCatalog(catalog);
        }
    }

    public static GranuleCatalog createGranuleCatalog(
            final URL sourceURL,
            final CatalogConfigurationBean catalogConfigurationBean,
            final Properties overrideParams,
            final Hints hints) {
        final File sourceFile = URLs.urlToFile(sourceURL);
        final String extension = FilenameUtils.getExtension(sourceFile.getAbsolutePath());

        // STANDARD PARAMS
        final Properties params = new Properties();

        params.put(Utils.Prop.PATH_TYPE, catalogConfigurationBean.getPathType());

        if (catalogConfigurationBean.getLocationAttribute() != null)
            params.put(
                    Utils.Prop.LOCATION_ATTRIBUTE, catalogConfigurationBean.getLocationAttribute());

        if (catalogConfigurationBean.getSuggestedSPI() != null)
            params.put(Utils.Prop.SUGGESTED_SPI, catalogConfigurationBean.getSuggestedSPI());

        if (catalogConfigurationBean.getSuggestedFormat() != null)
            params.put(Utils.Prop.SUGGESTED_FORMAT, catalogConfigurationBean.getSuggestedFormat());

        if (catalogConfigurationBean.getSuggestedIsSPI() != null)
            params.put(Utils.Prop.SUGGESTED_IS_SPI, catalogConfigurationBean.getSuggestedIsSPI());

        params.put(Utils.Prop.HETEROGENEOUS, catalogConfigurationBean.isHeterogeneous());
        params.put(Utils.Prop.WRAP_STORE, catalogConfigurationBean.isWrapStore());
        if (sourceURL != null) {
            File parentDirectory = URLs.urlToFile(sourceURL);
            if (parentDirectory.isFile()) parentDirectory = parentDirectory.getParentFile();
            params.put(Utils.Prop.PARENT_LOCATION, URLs.fileToUrl(parentDirectory).toString());
        } else params.put(Utils.Prop.PARENT_LOCATION, null);
        // add typename
        String typeName = catalogConfigurationBean.getTypeName();
        if (typeName != null) {
            params.put(Utils.Prop.TYPENAME, catalogConfigurationBean.getTypeName());
        }
        // SPI
        DataStoreFactorySpi spi = null;

        // Now format specific code
        if (extension.equalsIgnoreCase("shp")) {
            //
            // SHAPEFILE
            //
            params.put(ShapefileDataStoreFactory.URLP.key, sourceURL);
            params.put(ShapefileDataStoreFactory.CREATE_SPATIAL_INDEX.key, Boolean.TRUE);
            params.put(ShapefileDataStoreFactory.ENABLE_SPATIAL_INDEX.key, Boolean.TRUE);
            params.put(ShapefileDataStoreFactory.MEMORY_MAPPED.key, Boolean.FALSE);
            params.put(ShapefileDataStoreFactory.CACHE_MEMORY_MAPS.key, Boolean.FALSE);
            params.put(ShapefileDataStoreFactory.DBFTIMEZONE.key, TimeZone.getTimeZone("UTC"));
            spi = Utils.SHAPE_SPI;
        } else {
            // read the properties file
            Properties properties = CoverageUtilities.loadPropertiesFromURL(sourceURL);
            if (properties == null) return null;

            // get the params
            for (Object p : properties.keySet()) {
                params.put(p.toString(), properties.get(p).toString());
            }

            // SPI for datastore
            final String SPIClass = properties.getProperty("SPI");
            try {
                // create a datastore as instructed
                spi = (DataStoreFactorySpi) Class.forName(SPIClass).newInstance();
            } catch (Exception e) {
                // if we are directed to use a pre-existing store then don't complain about lack of
                // SPI
                if (properties.get(Utils.Prop.STORE_NAME) == null) {
                    if (LOGGER.isLoggable(Level.WARNING)) {
                        LOGGER.log(Level.WARNING, e.getLocalizedMessage(), e);
                    }
                    return null;
                }
            }
        }
        // Instantiate
        if (overrideParams != null && !overrideParams.isEmpty()) {
            params.putAll(overrideParams);
        }
        return createGranuleCatalog(
                params, catalogConfigurationBean.isCaching(), false, spi, hints);
    }
}
