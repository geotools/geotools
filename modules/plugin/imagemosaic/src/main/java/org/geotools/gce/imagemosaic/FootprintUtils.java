/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2010, Open Source Geospatial Foundation (OSGeo)
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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.api.data.FeatureSource;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.gce.imagemosaic.Utils.Prop;
import org.geotools.util.URLs;
import org.geotools.util.Utilities;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.locationtech.jts.io.WKTWriter;

/**
 * Utility class to manage mosaic's footprint
 *
 * @author Daniele Romagnoli, GeoSolutions S.A.S.
 */
class FootprintUtils {

    /**
     * A set of properties to be ignored when parsing the properties file. It is used to get only the
     * FootprintManagement property, avoiding by this way to load and compute useless elements.
     */
    static final Set<String> IGNORE_PROPS = new HashSet<>();

    static {
        IGNORE_PROPS.add(Prop.ENVELOPE2D);
        IGNORE_PROPS.add(Prop.ABSOLUTE_PATH);
        IGNORE_PROPS.add(Prop.SUGGESTED_SPI);
        IGNORE_PROPS.add(Prop.EXP_RGB);
        IGNORE_PROPS.add(Prop.LEVELS);
        IGNORE_PROPS.add(Prop.LOCATION_ATTRIBUTE);
        IGNORE_PROPS.add(Prop.NAME);
    }

    static final String FOOTPRINT_EXT = ".fpt";

    static final String FOOTPRINT_PREFIX = "footprint";

    static final String FOOTPRINT = FOOTPRINT_PREFIX + ".shp";

    private FootprintUtils() {}

    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(FootprintUtils.class);

    /**
     * Given a footprint summary file (.fpt), populate the provided footprints <ID-Geometry> pairs Map
     *
     * @param footprintSummaryFile the footprint summary file.
     * @param footprintsIDGeometryMap the Map to be populated
     */
    static void initFootprintsGranuleIDGeometryMap(
            final File footprintSummaryFile, final Map<String, Geometry> footprintsIDGeometryMap) {
        Utilities.ensureNonNull("footprintSummaryFile", footprintSummaryFile);
        if (!footprintSummaryFile.exists() || !footprintSummaryFile.canRead()) {
            throw new IllegalArgumentException(
                    "Unable to access to the provided footprint file " + footprintSummaryFile.getAbsolutePath());
        }
        Utilities.ensureNonNull("footprintsID_GeometryMap", footprintsIDGeometryMap);

        try (BufferedReader bReader =
                new BufferedReader(new FileReader(footprintSummaryFile, StandardCharsets.UTF_8))) {
            String footprint;

            final WKTReader geometryReader = new WKTReader();
            while ((footprint = bReader.readLine()) != null) {
                String[] fpt = footprint.split("=");
                if (fpt.length == 2) {
                    // parse the geometry
                    footprintsIDGeometryMap.put(fpt[0], geometryReader.read(fpt[1]));
                }
            }
            bReader.close();
        } catch (IOException | ParseException e) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, e.getLocalizedMessage(), e);
            }
        }
    }

    /**
     * Look for a footprint geometry for the provided featureID.
     *
     * @param featureID the ID of a feature for which the footprint should be searched
     * @param footprintsMap the map containing <.ID,Geometry> pairs
     * @return the related {@link Geometry} if found, or null otherwise.
     */
    static Geometry lookupFootprintGeometry(final String featureID, final Map<String, Geometry> footprintsMap) {
        Utilities.ensureNonNull("featureID", featureID);
        Utilities.ensureNonNull("footprintsMap", footprintsMap);

        if (footprintsMap != null && !footprintsMap.isEmpty()) {
            final String id = featureID.substring(featureID.lastIndexOf(".") + 1, featureID.length());
            return footprintsMap.containsKey(id) ? footprintsMap.get(id) : null;
        }
        return null;
    }

    /** Init the provided footprint map containing <String(location), Geometry(footprint)> pairs. */
    static void initFootprintsLocationGeometryMap(
            final ShapefileDataStore footprintStore, final Map<String, Geometry> footprintsMap) throws IOException {
        Utilities.ensureNonNull("footprintStore", footprintStore);
        Utilities.ensureNonNull("footprintsMap", footprintsMap);

        final String[] typeNames = footprintStore.getTypeNames();
        if (typeNames.length <= 0)
            throw new IllegalArgumentException(
                    "Problems when opening the footprint, no typenames for the schema are defined");

        final String typeName = typeNames[0];
        final FeatureSource<SimpleFeatureType, SimpleFeature> featureSource = footprintStore.getFeatureSource(typeName);
        final FeatureCollection<SimpleFeatureType, SimpleFeature> features = featureSource.getFeatures();
        if (features == null) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("No features found in the footprint");
            }
            return;
        }

        try (FeatureIterator<SimpleFeature> it = features.features()) {
            // load the feature from the footprint shapefile store
            if (!it.hasNext()) {
                throw new IllegalArgumentException(
                        "The provided FeatureCollection<SimpleFeatureType, SimpleFeature>  or "
                                + "empty, it's impossible to create an index!");
            }

            // now add the footprint to the Map
            while (it.hasNext()) {
                final SimpleFeature feature = it.next();
                final Geometry g = (Geometry) feature.getDefaultGeometry();
                final String location = (String) feature.getAttribute("location");
                footprintsMap.put(location, g);
            }
        }
    }

    /**
     * Build a "ID=Geometry" pair for the provided feature ID, by looking for a geometry in the provided footprintsMap
     * for the specified locationKey
     */
    private static String buildIDGeometryPair(
            final Map<String, Geometry> footprintGeometryMap,
            final String featureID,
            final String locationKey,
            final WKTWriter writer) {
        String idGeometryPair = "";
        Utilities.ensureNonNull("featureID", featureID);
        Utilities.ensureNonNull("writer", writer);
        Utilities.ensureNonNull("locationKey", locationKey);
        Utilities.ensureNonNull("footprintGeometryMap", footprintGeometryMap);

        if (!footprintGeometryMap.isEmpty() && footprintGeometryMap.containsKey(locationKey)) {
            final Geometry polygon = footprintGeometryMap.get(locationKey);
            if (polygon != null) {
                final String s = writer.write(polygon);
                String id = featureID;
                id = id.substring(id.lastIndexOf(".") + 1, id.length());
                idGeometryPair =
                        new StringBuilder(id).append("=").append(s).append("\n").toString();
            }
        }
        return idGeometryPair;
    }

    /**
     * Write a footprint summary file (".fpt") given an input <String,Geometry> map containing granule location and
     * related geometry, and the index shapefile store to associate footprints to granules.
     *
     * @param footprintSummaryFile the output footprint summary file
     * @param footprintsLocationGeometryMap the map containing <granuleLocation,footprintGeometry> pairs
     * @throws MalformedURLException In case the url we create internally for the mosaic index is wrong (should never
     *     happen)
     */
    static void writeFootprintSummary(
            final File footprintSummaryFile,
            final File indexFile,
            final Map<String, Geometry> footprintsLocationGeometryMap)
            throws MalformedURLException {
        Utilities.ensureNonNull("footprintSummaryFile", footprintSummaryFile);
        Utilities.ensureNonNull("indexFile", indexFile);
        Utilities.ensureNonNull("footprintsLocationGeometryMap", footprintsLocationGeometryMap);
        if (footprintsLocationGeometryMap.isEmpty()) return;
        final ShapefileDataStore store = new ShapefileDataStore(URLs.fileToUrl(indexFile));

        if (footprintsLocationGeometryMap.isEmpty()) return;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(footprintSummaryFile, StandardCharsets.UTF_8))) {
            final String[] typeNames = store.getTypeNames();
            if (typeNames.length <= 0) {
                throw new IllegalArgumentException(
                        "Problems when opening the shapefile, no typenames for the schema are defined");
            }
            final String typeName = typeNames[0];

            final FeatureSource<SimpleFeatureType, SimpleFeature> featureSource = store.getFeatureSource(typeName);
            final FeatureCollection<SimpleFeatureType, SimpleFeature> features = featureSource.getFeatures();

            if (features == null) {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.fine("No features found in the shapefile");
                }
                return;
            }

            // load the feature from the shapefile
            try (FeatureIterator<SimpleFeature> it = features.features()) {
                if (!it.hasNext())
                    throw new IllegalArgumentException(
                            "The provided FeatureCollection<SimpleFeatureType, SimpleFeature>  or empty, it's impossible to create an index!");

                final WKTWriter geometryWriter = new WKTWriter();

                // Scan the index shapefile to get granules location
                while (it.hasNext()) {
                    final SimpleFeature feature = it.next();
                    final String location = (String) feature.getAttribute("location");
                    if (location != null && location.trim().length() > 0) {
                        final String locationKey = location;

                        // Check if a footprint exist in the map for this granule
                        if (footprintsLocationGeometryMap.containsKey(locationKey)) {

                            // Build a featureID=Geometry pair and write it in
                            // the Footprint summary file
                            final String idGeometryPair = FootprintUtils.buildIDGeometryPair(
                                    footprintsLocationGeometryMap, feature.getID(), locationKey, geometryWriter);
                            writer.write(idGeometryPair);
                        }
                    }
                }
                writer.flush();
                writer.close();
            }
        } catch (Throwable e) {
            // ignore exception
            if (LOGGER.isLoggable(Level.FINEST)) LOGGER.log(Level.FINEST, e.getLocalizedMessage(), e);
        } finally {
            try {
                store.dispose();
            } catch (Throwable e) {
                if (LOGGER.isLoggable(Level.FINEST)) LOGGER.log(Level.FINEST, e.getLocalizedMessage(), e);
            }
        }
    }

    /** Search the footprint shape file in the specified directory. */
    static File searchFootprint(final String indexingDirectory) {
        File footprintFile = null;
        if (indexingDirectory != null && indexingDirectory.trim().length() > 0) {
            final File file = new File(indexingDirectory, FootprintUtils.FOOTPRINT);
            if (file != null && file.exists()) {
                footprintFile = file;
            }
        }
        return footprintFile;
    }
}
