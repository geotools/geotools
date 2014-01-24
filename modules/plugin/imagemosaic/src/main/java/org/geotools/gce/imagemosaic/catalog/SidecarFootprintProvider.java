/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2013, Open Source Geospatial Foundation (OSGeo)
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
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;
import org.geotools.data.DataUtilities;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.util.logging.Logging;
import org.opengis.feature.simple.SimpleFeature;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.InputStreamInStream;
import com.vividsolutions.jts.io.WKBReader;
import com.vividsolutions.jts.io.WKTReader;

/**
 * A footprint provider looking for sidecar files (SHP, WKB, WKT)
 * 
 * @author Andrea Aime - GeoSolutions
 */
public class SidecarFootprintProvider implements FootprintGeometryProvider {

    static final Logger LOGGER = Logging.getLogger(SidecarFootprintProvider.class);

    static final FootprintLoader[] LOADERS = new FootprintLoader[] { new ShapefileLoader(),
            new WKBLoader(), new WKTLoader() };

    private File mosaicFolder;

    private volatile FootprintLoader lastLoader;

    public SidecarFootprintProvider(File mosaicFolder) {
        this.mosaicFolder = mosaicFolder;
    }

    @Override
    public Geometry getFootprint(SimpleFeature feature) throws IOException {
        Object value = feature.getAttribute("location");
        if (value != null && value instanceof String) {
            String strValue = (String) value;
            String path = getFullPath(strValue);
            String noExtension = getNoExtensionPath(path);

            // Try to reuse over and over the last loader, to avoid checking the others
            // The last loader is kept in a volatile variable and all the work is done via
            // a local copy of it to avoid concurrency issues
            FootprintLoader loader = lastLoader;
            Geometry result = null;
            try {
                if (loader != null) {
                    result = loader.loadFootprint(noExtension);
                }
                if (result == null) {
                    for (FootprintLoader test : LOADERS) {
                        result = test.loadFootprint(noExtension);
                        if (result != null) {
                            lastLoader = test;
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                throw new IOException("Failed to load the footprint for granule " + strValue, e);
            }

            return result;
        } else {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("Could not use the location attribute value to search for "
                        + "a sidecar file, the value was: " + value);
            }
            return null;
        }
    }

    private String getNoExtensionPath(String path) {
        int idx = path.lastIndexOf(".");
        String noExtension;
        if (idx > 0) {
            noExtension = path.substring(0, idx);
        } else {
            noExtension = path;
        }
        return noExtension;
    }

    private String getFullPath(String strValue) throws IOException {
        File file = new File(strValue);
        if (!file.isAbsolute()) {
            file = new File(mosaicFolder, strValue);
        }
        String path = file.getCanonicalPath();
        return path;
    }

    @Override
    public void dispose() {
        // nothing to do, in this providers we don't keep files open
    }

    /**
     * Helper that loads a sidecar footprint file in a certain format
     */
    private interface FootprintLoader {

        /**
         * Tries to load the sidecar geometry given the granule path without extension.
         * 
         * @param pathNoExtension
         * @return The footprint, or null if the file was not found
         * @throws Exception In case the file was found, but there were issues loading the geometry
         */
        Geometry loadFootprint(String pathNoExtension) throws Exception;
    }

    /**
     * Loads WKB files
     */
    private static class WKBLoader implements FootprintLoader {

        WKBReader reader = new WKBReader();

        @Override
        public Geometry loadFootprint(String pathNoExtension) throws Exception {
            File file = new File(pathNoExtension + ".wkb");
            if (file.exists()) {
                FileInputStream is = null;
                try {
                    is = new FileInputStream(file);
                    return reader.read(new InputStreamInStream(is));
                } finally {
                    IOUtils.closeQuietly(is);
                }
            }

            return null;
        }

    }

    /**
     * Loads WKT files
     */
    private static class WKTLoader implements FootprintLoader {

        WKTReader reader = new WKTReader();

        @Override
        public Geometry loadFootprint(String pathNoExtension) throws Exception {
            File file = new File(pathNoExtension + ".wkt");
            if (file.exists()) {
                FileReader fr = null;
                try {
                    fr = new FileReader(file);
                    return reader.read(fr);
                } finally {
                    IOUtils.closeQuietly(fr);
                }
            }

            return null;
        }

    }

    /**
     * Loads footprints from a sidecar shepefile with a single record, will complain if more than
     * one is found
     */
    private static class ShapefileLoader implements FootprintLoader {

        @Override
        public Geometry loadFootprint(String pathNoExtension) throws Exception {
            File file = new File(pathNoExtension + ".shp");
            if (file.exists()) {
                ShapefileDataStore ds = new ShapefileDataStore(DataUtilities.fileToURL(file));
                SimpleFeatureIterator fi = null;
                try {
                    fi = ds.getFeatureSource().getFeatures().features();
                    if (!fi.hasNext()) {
                        return null;
                    } else {
                        SimpleFeature sf = fi.next();
                        Geometry result = (Geometry) sf.getDefaultGeometry();
                        if (fi.hasNext()) {
                            throw new IOException(
                                    "Found more than one footprint record in the shapefile "
                                            + file.getCanonicalPath());
                        }
                        return result;
                    }
                } finally {
                    if (fi != null) {
                        fi.close();
                    }
                    ds.dispose();
                }
            }

            return null;
        }

    }

}
