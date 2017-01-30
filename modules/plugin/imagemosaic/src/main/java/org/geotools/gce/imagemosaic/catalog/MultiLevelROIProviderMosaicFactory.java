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
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.geotools.coverage.grid.io.footprint.FootprintGeometryProvider;
import org.geotools.coverage.grid.io.footprint.MultiLevelROIProvider;
import org.geotools.coverage.grid.io.footprint.MultiLevelROIProviderFactory;
import org.geotools.coverage.grid.io.footprint.SidecarFootprintProvider;
import org.geotools.data.DataUtilities;
import org.geotools.filter.text.ecql.ECQL;
import org.opengis.filter.Filter;

/**
 * Factory class used for returning a {@link MultiLevelROIProvider} based on the input footprint properties and files for mosaics
 * 
 * @author Andrea Aime GeoSolutions
 * @author Nicola Lagomarsini GeoSolutions
 */
public class MultiLevelROIProviderMosaicFactory extends MultiLevelROIProviderFactory {

    public static final String FILTER_PROPERTY = "footprint_filter";

    private MultiLevelROIProviderMosaicFactory() {
    }

    /**
     * Builds a footprint provider from mosaic location
     * 
     * @param mosaicFolder The folder that contains the mosaic config files
     * @return
     * @throws Exception
     */
    public static MultiLevelROIProvider createFootprintProvider(File mosaicFolder) {
        File configFile = new File(mosaicFolder, "footprints.properties");
        final Properties properties = initProperties(configFile);

        // load the type of config file
        String source = (String) properties.get(MultiLevelROIProviderFactory.SOURCE_PROPERTY);
        FootprintGeometryProvider provider = null;
        if (source == null) {
            // see if we have the default whole mosaic footprint
            File defaultShapefileFootprint = new File(mosaicFolder, "footprints.shp");
            if (defaultShapefileFootprint.exists()) {
                provider = buildShapefileSource(mosaicFolder, defaultShapefileFootprint.getName(),
                        properties);
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
        } else {
            throw new IllegalArgumentException("Invalid source type, it should be a reference "
                    + "to a shapefile or 'sidecar', but was '" + source + "' instead");
        }

        // Create the provider
        return createProvider(provider, properties, null);
    }

    private static FootprintGeometryProvider buildShapefileSource(File mosaicFolder,
            String location, Properties properties) {
        File shapefile = new File(location);
        if (!shapefile.isAbsolute()) {
            shapefile = new File(mosaicFolder, location);
        }

        try {
            if (!shapefile.exists()) {
                throw new IllegalArgumentException("Tried to load the footprints from "
                        + shapefile.getCanonicalPath() + " but the file was not found");
            } else {
                final Map<String, Serializable> params = new HashMap<String, Serializable>();
                params.put("url", DataUtilities.fileToURL(shapefile));
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
