/*
 * GeoTools - The Open Source Java GIS Toolkit
 * http://geotools.org
 *
 * (C) 2017, Open Source Geospatial Foundation (OSGeo)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation;
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 */
package org.geotools.gce.imagemosaic;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.data.DataAccess;
import org.geotools.data.DataStore;
import org.geotools.data.DefaultRepository;
import org.geotools.data.FeatureSource;
import org.geotools.data.ServiceInfo;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.factory.Hints;
import org.geotools.feature.NameImpl;
import org.geotools.test.TestData;
import org.geotools.util.URLs;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.Name;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValue;

public class ImageMosaicRepositoryTest {

    static final GeneralParameterValue[] NO_DEFERRED_LOAD;

    static final ImageMosaicFormat FORMAT = new ImageMosaicFormat();
    static {
        final ParameterValue<Boolean> useJai = AbstractGridFormat.USE_JAI_IMAGEREAD.createValue();
        useJai.setValue(false);
        NO_DEFERRED_LOAD = new GeneralParameterValue[] { useJai };
    }

    @Rule
    public TemporaryFolder crsMosaicFolder = new TemporaryFolder();

    @Test
    public void createFromExistingStore() throws Exception {
        // setup mosaic
        URL storeUrl = TestData.url(this, "rgba");
        File testDataFolder = new File(storeUrl.toURI());
        File testDirectory = crsMosaicFolder.newFolder("rgbaShapefile");
        FileUtils.copyDirectory(testDataFolder, testDirectory);
        // clean up, other tests might have left the rgba source dir dirty
        for (File file : testDirectory.listFiles(f -> f.getName().startsWith("rgba"))) {
            file.delete();
        }

        // make it create a shapefile
        ImageMosaicReader reader = FORMAT.getReader(testDirectory);
        assertNotNull(reader);
        reader.dispose();

        // clean up and rename all shapefiles to make sure the store is not overwriting it by accident
        removeSupportFiles(testDirectory);
        for (File f : testDirectory
                .listFiles(f -> f.getName().startsWith(testDirectory.getName()))) {
            String extension = FilenameUtils.getExtension(f.getName());
            f.renameTo(new File(testDirectory, "test." + extension));
        }

        // signal the intention to use a store name using both datastore.properties and indexer
        Properties properties = new Properties();
        properties.put(Utils.Prop.STORE_NAME, "test");
        try (FileOutputStream fos = new FileOutputStream(
                new File(testDirectory, "datastore.properties"))) {
            properties.store(fos, null);
        }
        properties = new Properties();
        properties.put(Utils.Prop.USE_EXISTING_SCHEMA, "true");
        properties.put(Utils.Prop.TYPENAME, "test");
        try (FileOutputStream fos = new FileOutputStream(
                new File(testDirectory, "indexer.properties"))) {
            properties.store(fos, null);
        }

        DefaultRepository repository = new DefaultRepository();
        ShapefileDataStore ds = new ShapefileDataStore(
                URLs.fileToUrl(new File(testDirectory, "test.shp")));
        repository.register("test", ds);

        // now re-init from the existing shapefile data store
        Hints hints = new Hints(Hints.REPOSITORY, repository);
        reader = FORMAT.getReader(testDirectory, hints);
        assertNotNull(reader);
        GridCoverage2D coverage = reader.read(NO_DEFERRED_LOAD);
        assertNotNull(coverage);
        coverage.dispose(true);
        reader.dispose();
    }

    @Test
    public void createFromExistingDataAccess() throws Exception {
        // setup mosaic
        URL storeUrl = TestData.url(this, "rgba");
        File testDataFolder = new File(storeUrl.toURI());
        File testDirectory = crsMosaicFolder.newFolder("rgbaShapefile");
        FileUtils.copyDirectory(testDataFolder, testDirectory);
        // clean up, other tests might have left the rgba source dir dirty
        for (File file : testDirectory.listFiles(f -> f.getName().startsWith("rgba"))) {
            file.delete();
        }

        // make it create a shapefile
        ImageMosaicReader reader = FORMAT.getReader(testDirectory);
        assertNotNull(reader);
        reader.dispose();

        // clean up and rename all shapefiles to make sure the store is not overwriting it by accident
        removeSupportFiles(testDirectory);
        for (File f : testDirectory
                .listFiles(f -> f.getName().startsWith(testDirectory.getName()))) {
            String extension = FilenameUtils.getExtension(f.getName());
            f.renameTo(new File(testDirectory, "test." + extension));
        }

        // signal the intention to use a store name using both datastore.properties and indexer
        Properties properties = new Properties();
        properties.put(Utils.Prop.STORE_NAME, "foo:test");
        try (FileOutputStream fos = new FileOutputStream(
                new File(testDirectory, "datastore.properties"))) {
            properties.store(fos, null);
        }
        properties = new Properties();
        properties.put(Utils.Prop.USE_EXISTING_SCHEMA, "true");
        properties.put(Utils.Prop.TYPENAME, "test");
        try (FileOutputStream fos = new FileOutputStream(
                new File(testDirectory, "indexer.properties"))) {
            properties.store(fos, null);
        }

        DefaultRepository repository = new DefaultRepository();
        ShapefileDataStore ds = new ShapefileDataStore(
                URLs.fileToUrl(new File(testDirectory, "test.shp")));
        Name name = new NameImpl("foo", "test");
        TestDataAccess dataAccess = new TestDataAccess(name, ds);
        repository.register(name, dataAccess);

        // now re-init from the existing shapefile data store
        Hints hints = new Hints(Hints.REPOSITORY, repository);
        reader = FORMAT.getReader(testDirectory, hints);
        assertNotNull(reader);
        GridCoverage2D coverage = reader.read(NO_DEFERRED_LOAD);
        assertNotNull(coverage);
        coverage.dispose(true);
        reader.dispose();
        ds.dispose();
    }

    @Test
    public void createFromEmptyStore() throws Exception {
        // setup mosaic
        URL storeUrl = TestData.url(this, "rgba");
        File testDataFolder = new File(storeUrl.toURI());
        File testDirectory = crsMosaicFolder.newFolder("rgbaShapefile");
        FileUtils.copyDirectory(testDataFolder, testDirectory);
        // clean up, other tests might have left the rgba source dir dirty
        for (File file : testDirectory.listFiles(f -> f.getName().startsWith("rgba"))) {
            file.delete();
        }

        // signal the intention to use a store name using both datastore.properties and indexer
        Properties properties = new Properties();
        properties.put(Utils.Prop.STORE_NAME, "test");
        try (FileOutputStream fos = new FileOutputStream(
                new File(testDirectory, "datastore.properties"))) {
            properties.store(fos, null);
        }
        properties = new Properties();
        properties.put(Utils.Prop.TYPENAME, "abcd");
        try (FileOutputStream fos = new FileOutputStream(
                new File(testDirectory, "indexer.properties"))) {
            properties.store(fos, null);
        }

        // setup a directory data store of shapefiles
        DefaultRepository repository = new DefaultRepository();
        final Map<String, Serializable> params = Collections.singletonMap(
                ShapefileDataStoreFactory.URLP.key, URLs.fileToUrl(testDirectory));
        DataStore ds = new ShapefileDataStoreFactory().createDataStore(params);
        assertNotNull(ds);
        repository.register("test", ds);

        // now init, the code should create the expected shapefile
        Hints hints = new Hints(Hints.REPOSITORY, repository);
        ImageMosaicReader reader = FORMAT.getReader(testDirectory, hints);
        final File expectedIndexFile = new File(testDirectory, "abcd.shp");
        assertTrue(expectedIndexFile.getAbsolutePath() + " does not exist",
                expectedIndexFile.exists());
        assertNotNull(reader);
        GridCoverage2D coverage = reader.read(NO_DEFERRED_LOAD);
        assertNotNull(coverage);
        coverage.dispose(true);
        reader.dispose();
        ds.dispose();
    }

    /**
     * Removes the sample image and
     * 
     * @param directory
     */
    private void removeSupportFiles(File directory) {
        new File(directory, Utils.SAMPLE_IMAGE_NAME).delete();
        new File(directory, directory.getName() + ".properties").delete();
    }

    private static class TestDataAccess implements DataAccess {

        private DataStore delegate;

        private Name name;

        public TestDataAccess(Name name, DataStore delegate) {
            this.name = name;
            this.delegate = delegate;
        }

        @Override
        public ServiceInfo getInfo() {
            return null;
        }

        @Override
        public void createSchema(FeatureType featureType) throws IOException {
            throw new UnsupportedOperationException();

        }

        @Override
        public void updateSchema(Name typeName, FeatureType featureType) throws IOException {
            throw new UnsupportedOperationException();

        }

        @Override
        public void removeSchema(Name typeName) throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<Name> getNames() throws IOException {
            return Arrays.asList(name);
        }

        @Override
        public FeatureType getSchema(Name name) throws IOException {
            if (this.name.equals(name)) {
                return delegate.getSchema(name.getLocalPart());
            }
            return null;
        }

        @Override
        public FeatureSource getFeatureSource(Name typeName) throws IOException {
            if (this.name.equals(name)) {
                return delegate.getFeatureSource(name.getLocalPart());
            }
            return null;
        }

        @Override
        public void dispose() {
            delegate.dispose();
        }

    }
}
