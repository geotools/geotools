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
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.gce.imagemosaic.Utils.Prop;
import org.geotools.gce.imagemosaic.catalog.GranuleCatalog;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * This class is responsible for walking through the target schema and check all the located
 * granules.
 *
 * <p>Its role is basically to simplify the construction of the mosaic by implementing a visitor
 * pattern for the files that we have to use for the index.
 *
 * @author Carlo Cancellieri - GeoSolutions SAS @TODO check the schema structure
 */
class ImageMosaicDatastoreWalker extends ImageMosaicWalker {

    /** Default Logger * */
    static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(ImageMosaicDatastoreWalker.class);

    /**
     * @param updateFeatures if true update catalog with loaded granules
     * @param imageMosaicConfigHandler TODO
     */
    public ImageMosaicDatastoreWalker(
            ImageMosaicConfigHandler configHandler, ImageMosaicEventHandlers eventHandler) {
        super(configHandler, eventHandler);
    }

    /** run the walker on the store */
    public void run() {

        SimpleFeatureIterator it = null;
        GranuleCatalog catalog = null;
        try {

            configHandler.indexingPreamble();
            startTransaction();

            // start looking into catalog
            catalog = configHandler.getCatalog();
            String locationAttrName =
                    configHandler.getRunConfiguration().getParameter(Prop.LOCATION_ATTRIBUTE);
            String requestedTypeName =
                    configHandler.getRunConfiguration().getParameter(Prop.TYPENAME);
            String location =
                    configHandler.getRunConfiguration().getParameter(Prop.LOCATION_ATTRIBUTE);
            for (String typeName : catalog.getTypeNames()) {
                if (requestedTypeName != null && !requestedTypeName.equals(typeName)) {
                    continue;
                }

                if (!Utils.isValidMosaicSchema(catalog.getType(typeName), location)) {
                    LOGGER.log(Level.FINE, "Skipping invalid mosaic index table " + typeName);
                    continue;
                }

                // how many rows for this feature type?
                final Query query = new Query(typeName);
                int numFiles = catalog.getGranulesCount(query);
                if (numFiles <= 0) {
                    // empty table?
                    LOGGER.log(Level.FINE, "No rows in the typeName: " + typeName);
                    continue;
                }
                setNumFiles(numFiles);

                // cool, now let's walk over the features
                final SimpleFeatureCollection coll = catalog.getGranules(query);

                SimpleFeatureType schema = coll.getSchema();
                if (schema.getDescriptor(locationAttrName) == null) {
                    LOGGER.fine(
                            "Skipping feature type "
                                    + typeName
                                    + " as the location attribute "
                                    + locationAttrName
                                    + " is not part of the schema");
                    continue;
                } else if (schema.getGeometryDescriptor() == null) {
                    LOGGER.fine(
                            "Skipping feature type "
                                    + typeName
                                    + " as it does not have a footprint column");
                    continue;
                }

                // create an iterator
                it = coll.features();
                // TODO setup index name

                while (it.hasNext()) {
                    // get next element
                    final SimpleFeature feature = it.next();

                    Object locationAttrObj = feature.getAttribute(locationAttrName);
                    File file = null;
                    if (locationAttrObj instanceof String) {
                        final String path = (String) locationAttrObj;
                        if (Boolean.getBoolean(
                                configHandler
                                        .getRunConfiguration()
                                        .getParameter(Prop.ABSOLUTE_PATH))) {
                            // absolute files
                            file = new File(path);
                            // check this is _really_ absolute
                            if (!checkFile(file)) {
                                file = null;
                            }
                        }
                        if (file == null) {
                            // relative files
                            file =
                                    new File(
                                            configHandler
                                                    .getRunConfiguration()
                                                    .getParameter(Prop.ROOT_MOSAIC_DIR),
                                            path);
                            // check this is _really_ relative
                            if (!(file.exists() && file.canRead() && file.isFile())) {
                                // let's try for absolute, despite what the config says
                                // absolute files
                                file = new File(path);
                                // check this is _really_ absolute
                                if (!(checkFile(file))) {
                                    file = null;
                                }
                            }
                        }

                        // final check
                        if (file == null) {
                            // SKIP and log
                            // empty table?
                            super.skipFile(path);
                            continue;
                        }

                    } else if (locationAttrObj instanceof File) {
                        file = (File) locationAttrObj;
                    } else {
                        eventHandler.fireException(
                                new IOException(
                                        "Location attribute type not recognized for column name: "
                                                + locationAttrName));
                        stop();
                        break;
                    }

                    // process this file
                    handleFile(file);
                }
            } // next table

            // close transaction
            // did we cancel?
            if (getStop()) {
                rollbackTransaction();
            } else {
                commitTransaction();
            }

        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Failure occurred while collecting the granules", e);
            try {
                rollbackTransaction();
            } catch (IOException e1) {
                throw new IllegalStateException(e1);
            }
        } finally {
            // close read iterator
            if (it != null) {
                try {
                    it.close();
                } catch (Exception e) {
                    LOGGER.log(Level.FINE, e.getLocalizedMessage(), e);
                }
            }
            // close transaction
            try {
                closeTransaction();
            } catch (Exception e) {
                final String message = "Unable to close indexing" + e.getLocalizedMessage();
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.log(Level.WARNING, message, e);
                }
                // notify listeners
                eventHandler.fireException(e);
            }

            // close indexing
            try {
                configHandler.indexingPostamble(!getStop());
            } catch (Exception e) {
                final String message = "Unable to close indexing" + e.getLocalizedMessage();
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.log(Level.WARNING, message, e);
                }
                // notify listeners
                eventHandler.fireException(e);
            }

            try {
                if (catalog != null) {
                    catalog.dispose();
                }
            } catch (RuntimeException e) {
                String message = "Failed to dispose harvesting catalog";
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.log(Level.WARNING, message, e);
                }
            }
        }
    }
}
