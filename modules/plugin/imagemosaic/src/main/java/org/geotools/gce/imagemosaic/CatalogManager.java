/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2013 - 2016, Open Source Geospatial Foundation (OSGeo)
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

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;
import org.geotools.coverage.grid.io.GranuleSource;
import org.geotools.coverage.grid.io.GranuleStore;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.coverage.grid.io.StructuredGridCoverage2DReader;
import org.geotools.coverage.grid.io.footprint.MultiLevelROIProvider;
import org.geotools.data.DataSourceException;
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.factory.Hints;
import org.geotools.feature.collection.AbstractFeatureVisitor;
import org.geotools.gce.imagemosaic.Utils.Prop;
import org.geotools.gce.imagemosaic.catalog.CatalogConfigurationBean;
import org.geotools.gce.imagemosaic.catalog.GranuleCatalog;
import org.geotools.gce.imagemosaic.catalog.GranuleCatalogFactory;
import org.geotools.gce.imagemosaic.catalog.MultiLevelROIProviderMosaicFactory;
import org.geotools.gce.imagemosaic.catalogbuilder.CatalogBuilderConfiguration;
import org.geotools.gce.imagemosaic.properties.PropertiesCollector;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.util.DefaultProgressListener;
import org.opengis.feature.Feature;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;

import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;

/**
 * An utility class which allows to create schema, catalogs, and populate them.
 * 
 * @author Daniele Romagnoli, GeoSolutions SAS
 *
 */
public class CatalogManager {

    /** Default Logger * */
    private final static Logger LOGGER = org.geotools.util.logging.Logging.getLogger(CatalogManager.class);

    /**
     * Tries to drop a datastore referred by the datastore connections 
     * properties specified in the provided file.
     * 
     * Current implementation only drop a postGIS datastore.
     * 
     * @param datastoreProperties
     * @throws IOException
     */
    public static void dropDatastore(File datastoreProperties) throws IOException {
        final Properties properties = ImageMosaicConfigHandler.createGranuleCatalogProperties(datastoreProperties);
        final String SPIClass = properties.getProperty("SPI");
        try {
            // drop a datastore. Right now, only postGIS drop is supported
            final DataStoreFactorySpi spi = (DataStoreFactorySpi) Class.forName(SPIClass).newInstance();
            Utils.dropDB(spi, properties);
        } catch (Exception e) {
            final IOException ioe = new IOException();
            throw (IOException) ioe.initCause(e);
        } 
    }

}
