/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.flatgeobuf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.Serializable;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.DataUtilities;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.util.URLs;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

public class FlatGeobufDirectoryDataStoreTest {

    @Rule public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void dataStore() throws Exception {

        // Create a directory
        File directory = temporaryFolder.newFolder();

        // Copy over some FGB files
        String[] fgbNames = {"lines", "points", "polygons"};
        for (String name : fgbNames) {
            File file =
                    URLs.urlToFile(
                            getClass()
                                    .getClassLoader()
                                    .getResource("org/geotools/data/flatgeobuf/" + name + ".fgb"));
            Files.copy(
                    file.toPath(),
                    new File(directory, file.getName()).toPath(),
                    StandardCopyOption.REPLACE_EXISTING);
        }

        // Get a DataStore
        Map<String, Serializable> params = new HashMap<>();
        URL url = directory.toURI().toURL();
        params.put("url", url);
        DataStore store = DataStoreFinder.getDataStore(params);

        // Get layers
        List<String> names = Arrays.asList(store.getTypeNames());
        for (String name : fgbNames) {
            assertTrue(names.contains(name));
        }

        // Make sure we can get layers
        for (String name : names) {
            SimpleFeatureSource fs = store.getFeatureSource(name);
            assertNotNull(fs.getBounds());
            assertNotNull(fs.getSchema());
            assertTrue(fs.getCount(Query.ALL) > 0);
        }

        // Write a new Layer
        SimpleFeatureType featureType =
                DataUtilities.createType("locations", "geom:Point,name:String,id:int");
        store.createSchema(featureType);
        SimpleFeatureStore featureStore = (SimpleFeatureStore) store.getFeatureSource("locations");
        GeometryFactory gf = JTSFactoryFinder.getGeometryFactory();
        SimpleFeature feature1 =
                SimpleFeatureBuilder.build(
                        featureType,
                        new Object[] {
                            gf.createPoint(new Coordinate(-8.349609375, 14.349547837185362)),
                            "ABC",
                            1
                        },
                        "location.1");
        SimpleFeature feature2 =
                SimpleFeatureBuilder.build(
                        featureType,
                        new Object[] {
                            gf.createPoint(new Coordinate(-18.349609375, 24.349547837185362)),
                            "DEF",
                            2
                        },
                        "location.2");
        SimpleFeatureCollection collection = DataUtilities.collection(feature1, feature2);
        featureStore.addFeatures(collection);
        assertEquals(2, featureStore.getCount(Query.ALL));
    }

    @Test
    public void removeSchema() throws Exception {
        File dir = temporaryFolder.newFolder("layers");
        File file1 = Files.createFile(Paths.get(dir.getAbsolutePath(), "points.fgb")).toFile();
        File file2 = Files.createFile(Paths.get(dir.getAbsolutePath(), "lines.fgb")).toFile();
        Map<String, Serializable> params = new HashMap<>();
        URL url = dir.toURI().toURL();
        params.put("url", url);
        DataStore store = DataStoreFinder.getDataStore(params);
        // File 1
        assertTrue(file1.exists());
        store.removeSchema("points");
        assertFalse(file1.exists());
        // File 2
        assertTrue(file2.exists());
        store.removeSchema("lines.fgb");
        assertFalse(file2.exists());
    }
}
