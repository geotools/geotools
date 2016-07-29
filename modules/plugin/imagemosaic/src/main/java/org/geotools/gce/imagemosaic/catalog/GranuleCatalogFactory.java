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
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.data.DataUtilities;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.factory.Hints;
import org.geotools.gce.imagemosaic.PathType;
import org.geotools.gce.imagemosaic.Utils;
import org.geotools.resources.coverage.CoverageUtilities;
import org.geotools.util.logging.Logging;

/**
 * Simple Factory class for creating {@link GranuleCatalog} instance to handle the catalog of granules for this mosaic.
 * 
 * @author Simone Giannecchini, GeoSolutions SAS
 *
 * @source $URL$
 */
public abstract class GranuleCatalogFactory {

    private final static Logger LOGGER = Logging.getLogger("GranuleCatalogFactory");

    /**
     * Default private constructor to enforce singleton
     */
    private GranuleCatalogFactory() {
    }

    public static GranuleCatalog createGranuleCatalog(final Properties params,
            final boolean caching, final boolean create, final DataStoreFactorySpi spi,
            final Hints hints) {
        if (caching) {
            return new STRTreeGranuleCatalog(params, spi, hints);
        } else {
            return new CachingDataStoreGranuleCatalog(
                    new GTDataStoreGranuleCatalog(params, create, spi, hints));
        }
    }

    public static GranuleCatalog createGranuleCatalog(final URL sourceURL,
            final CatalogConfigurationBean catalogConfigurationBean,
            final Properties overrideParams, final Hints hints) {
        final File sourceFile = DataUtilities.urlToFile(sourceURL);
        final String extension = FilenameUtils.getExtension(sourceFile.getAbsolutePath());

        // STANDARD PARAMS
        final Properties params = new Properties();

        params.put(Utils.Prop.PATH_TYPE,
                catalogConfigurationBean.isAbsolutePath() ? PathType.ABSOLUTE : PathType.RELATIVE);

        if (catalogConfigurationBean.getLocationAttribute() != null)
            params.put(Utils.Prop.LOCATION_ATTRIBUTE,
                    catalogConfigurationBean.getLocationAttribute());

        if (catalogConfigurationBean.getSuggestedSPI() != null)
            params.put(Utils.Prop.SUGGESTED_SPI, catalogConfigurationBean.getSuggestedSPI());

        params.put(Utils.Prop.HETEROGENEOUS, catalogConfigurationBean.isHeterogeneous());
        params.put(Utils.Prop.WRAP_STORE, catalogConfigurationBean.isWrapStore());
        if (sourceURL != null) {
            File parentDirectory = DataUtilities.urlToFile(sourceURL);
            if (parentDirectory.isFile())
                parentDirectory = parentDirectory.getParentFile();
            params.put(Utils.Prop.PARENT_LOCATION,
                    DataUtilities.fileToURL(parentDirectory).toString());
        } else
            params.put(Utils.Prop.PARENT_LOCATION, null);
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
            if (properties == null)
                return null;

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
                if (LOGGER.isLoggable(Level.WARNING))
                    LOGGER.log(Level.WARNING, e.getLocalizedMessage(), e);
                return null;
            }
        }
        // Instantiate
        if (overrideParams != null && !overrideParams.isEmpty()) {
            params.putAll(overrideParams);
        }
        final GranuleCatalog catalog = catalogConfigurationBean.isCaching()
                ? new STRTreeGranuleCatalog(params, spi, hints)
                : new CachingDataStoreGranuleCatalog(
                        new GTDataStoreGranuleCatalog(params, false, spi, hints));

        return catalog;
    }

}
