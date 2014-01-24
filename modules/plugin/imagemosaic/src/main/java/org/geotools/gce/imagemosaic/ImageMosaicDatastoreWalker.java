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

/**
 * This class is responsible for walking through the target schema and check all the located granules.
 * 
 * <p>
 * Its role is basically to simplify the construction of the mosaic by implementing a visitor pattern for the files that we have to use for the index.
 * 
 * 
 * @author Carlo Cancellieri - GeoSolutions SAS
 * 
 * @TODO check the schema structure
 * 
 */
class ImageMosaicDatastoreWalker extends ImageMosaicWalker {

    /** Default Logger * */
    final static Logger LOGGER = org.geotools.util.logging.Logging
            .getLogger(ImageMosaicDatastoreWalker.class);

    /**
     * @param updateFeatures if true update catalog with loaded granules
     * @param imageMosaicConfigHandler TODO
     */
    public ImageMosaicDatastoreWalker(ImageMosaicConfigHandler configHandler,
            ImageMosaicEventHandlers eventHandler) {
        super(configHandler, eventHandler);
    }

    /**
     * run the walker on the store
     */
    public void run() {

        SimpleFeatureIterator it = null;
        try {

            configHandler.indexingPreamble();
            startTransaction();

            // start looking into catalog
            final GranuleCatalog catalog = configHandler.getCatalog();
            for (String typeName : catalog.getTypeNames()) {

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
                // create an iterator
                it = coll.features();
                // TODO setup index name

                while (it.hasNext()) {
                    // get next element
                    final SimpleFeature feature = it.next();

                    // String
                    // locationAttrName=config.getCatalogConfigurationBean().getLocationAttribute();
                    String locationAttrName = configHandler.getRunConfiguration().getParameter(
                            Prop.LOCATION_ATTRIBUTE);
                    Object locationAttrObj = feature.getAttribute(locationAttrName);
                    File file = null;
                    if (locationAttrObj instanceof String) {
                        final String path = (String) locationAttrObj;
                        if (Boolean.getBoolean(configHandler.getRunConfiguration().getParameter(
                                Prop.ABSOLUTE_PATH))) {
                            // absolute files
                            file = new File(path);
                            // check this is _really_ absolute
                            if (!checkFile(file)) {
                                file = null;
                            }
                        }
                        if (file == null) {
                            // relative files
                            file = new File(configHandler.getRunConfiguration().getParameter(
                                    Prop.ROOT_MOSAIC_DIR), path);
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
                        eventHandler.fireException(new IOException(
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

        }
    }
}