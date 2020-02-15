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
import java.util.function.BiFunction;
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
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.AttributeTypeBuilder;
import org.geotools.feature.NameImpl;
import org.geotools.feature.TypeBuilder;
import org.geotools.test.TestData;
import org.geotools.util.URLs;
import org.geotools.util.factory.Hints;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.feature.type.GeometryType;
import org.opengis.feature.type.Name;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValue;

public class ImageMosaicRepositoryTest {

    static final GeneralParameterValue[] NO_DEFERRED_LOAD;

    static final ImageMosaicFormat FORMAT = new ImageMosaicFormat();

    static {
        final ParameterValue<Boolean> useJai = AbstractGridFormat.USE_JAI_IMAGEREAD.createValue();
        useJai.setValue(false);
        NO_DEFERRED_LOAD = new GeneralParameterValue[] {useJai};
    }

    @Rule public TemporaryFolder crsMosaicFolder = new TemporaryFolder();

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

        // clean up and rename all shapefiles to make sure the store is not overwriting it by
        // accident
        removeSupportFiles(testDirectory);
        for (File f :
                testDirectory.listFiles(f -> f.getName().startsWith(testDirectory.getName()))) {
            String extension = FilenameUtils.getExtension(f.getName());
            f.renameTo(new File(testDirectory, "test." + extension));
        }

        // signal the intention to use a store name using both datastore.properties and indexer
        Properties properties = new Properties();
        properties.put(Utils.Prop.STORE_NAME, "test");
        try (FileOutputStream fos =
                new FileOutputStream(new File(testDirectory, "datastore.properties"))) {
            properties.store(fos, null);
        }
        properties = new Properties();
        properties.put(Utils.Prop.USE_EXISTING_SCHEMA, "true");
        properties.put(Utils.Prop.TYPENAME, "test");
        try (FileOutputStream fos =
                new FileOutputStream(new File(testDirectory, "indexer.properties"))) {
            properties.store(fos, null);
        }

        DefaultRepository repository = new DefaultRepository();
        ShapefileDataStore ds =
                new ShapefileDataStore(URLs.fileToUrl(new File(testDirectory, "test.shp")));
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
        createFromExistingDataAccess((ds, name) -> new TestDataAccess(name, ds));
    }

    @Test
    public void createFromExistingDataAccessWithComplex() throws Exception {
        createFromExistingDataAccess(
                (ds, name) -> {
                    try {
                        SimpleFeatureType schema = ds.getSchema(name);
                        FeatureType featureType = buildComplexTypeFromSimple(schema);
                        return new TestDataAccessWithComplex(name, featureType, ds);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    public void createFromExistingDataAccess(
            BiFunction<DataStore, Name, DataAccess> dataAccessProvider) throws Exception {
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

        // clean up and rename all shapefiles to make sure the store is not overwriting it by
        // accident
        removeSupportFiles(testDirectory);
        for (File f :
                testDirectory.listFiles(f -> f.getName().startsWith(testDirectory.getName()))) {
            String extension = FilenameUtils.getExtension(f.getName());
            f.renameTo(new File(testDirectory, "test." + extension));
        }

        // signal the intention to use a store name using both datastore.properties and indexer
        Properties properties = new Properties();
        properties.put(Utils.Prop.STORE_NAME, "foo:test");
        try (FileOutputStream fos =
                new FileOutputStream(new File(testDirectory, "datastore.properties"))) {
            properties.store(fos, null);
        }
        properties = new Properties();
        properties.put(Utils.Prop.USE_EXISTING_SCHEMA, "true");
        properties.put(Utils.Prop.TYPENAME, "test");
        try (FileOutputStream fos =
                new FileOutputStream(new File(testDirectory, "indexer.properties"))) {
            properties.store(fos, null);
        }

        DefaultRepository repository = new DefaultRepository();
        ShapefileDataStore ds =
                new ShapefileDataStore(URLs.fileToUrl(new File(testDirectory, "test.shp")));
        Name name = new NameImpl("foo", "test");
        DataAccess dataAccess = dataAccessProvider.apply(ds, name);
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
        try (FileOutputStream fos =
                new FileOutputStream(new File(testDirectory, "datastore.properties"))) {
            properties.store(fos, null);
        }
        properties = new Properties();
        properties.put(Utils.Prop.TYPENAME, "abcd");
        try (FileOutputStream fos =
                new FileOutputStream(new File(testDirectory, "indexer.properties"))) {
            properties.store(fos, null);
        }

        // setup a directory data store of shapefiles
        DefaultRepository repository = new DefaultRepository();
        final Map<String, Serializable> params =
                Collections.singletonMap(
                        ShapefileDataStoreFactory.URLP.key, URLs.fileToUrl(testDirectory));
        DataStore ds = new ShapefileDataStoreFactory().createDataStore(params);
        assertNotNull(ds);
        repository.register("test", ds);

        // now init, the code should create the expected shapefile
        Hints hints = new Hints(Hints.REPOSITORY, repository);
        ImageMosaicReader reader = FORMAT.getReader(testDirectory, hints);
        final File expectedIndexFile = new File(testDirectory, "abcd.shp");
        assertTrue(
                expectedIndexFile.getAbsolutePath() + " does not exist",
                expectedIndexFile.exists());
        assertNotNull(reader);
        GridCoverage2D coverage = reader.read(NO_DEFERRED_LOAD);
        assertNotNull(coverage);
        coverage.dispose(true);
        reader.dispose();
        ds.dispose();
    }

    /** Test for GEOS-8311 load a mosaic when it doesn't have a projection set. */
    @Test
    public void createFromEmptyStoreWithNoProjection() throws Exception {
        // setup mosaic
        URL storeUrl = TestData.url(this, "rgba");
        File testDataFolder = new File(storeUrl.toURI());
        File testDirectory = crsMosaicFolder.newFolder("rgba_no_proj");
        FileUtils.copyDirectory(testDataFolder, testDirectory);
        // clean up, other tests might have left the rgba source dir dirty
        for (File file : testDirectory.listFiles(f -> f.getName().startsWith("rgba"))) {
            file.delete();
        }
        // remove the .prj files
        for (File file : testDirectory.listFiles(f -> f.getName().endsWith("prj"))) {
            file.delete();
        }

        ImageMosaicReader reader = FORMAT.getReader(testDirectory);

        assertNotNull(reader);
        GridCoverage2D coverage = reader.read(NO_DEFERRED_LOAD);
        assertNotNull(coverage);
        coverage.dispose(true);
        reader.dispose();
    }

    /** Test for GEOS-8311 load a mosaic when it doesn't have a projection set. */
    @Test
    public void createFromEmptyStoreWithNoProjectionGeoTif() throws Exception {
        // setup mosaic
        URL storeUrl = TestData.url(this, "nocrs");
        File testDataFolder = new File(storeUrl.toURI());
        File testDirectory = crsMosaicFolder.newFolder("no_crs");
        FileUtils.copyDirectory(testDataFolder, testDirectory);

        // remove the .prj files - there shouldn't be any but check
        for (File file : testDirectory.listFiles(f -> f.getName().endsWith("prj"))) {
            file.delete();
        }
        // signal the intention to use a store name using both datastore.properties and indexer
        ImageMosaicReader reader = FORMAT.getReader(testDirectory);

        assertNotNull(reader);
        GridCoverage2D coverage = reader.read(NO_DEFERRED_LOAD);
        assertNotNull(coverage);
        coverage.dispose(true);
        reader.dispose();
    }

    /** Removes the sample image and */
    private void removeSupportFiles(File directory) {
        new File(directory, Utils.SAMPLE_IMAGE_NAME).delete();
        new File(directory, directory.getName() + ".properties").delete();
    }

    private static class TestDataAccess implements DataAccess {

        private DataStore delegate;

        protected Name name;

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
            if (this.name.equals(typeName)) {
                return delegate.getFeatureSource(name.getLocalPart());
            }
            return null;
        }

        @Override
        public void dispose() {
            delegate.dispose();
        }
    }

    /**
     * A DataAccess that actually has a complex source. Won't return a FeatureSource, but it's
     * enough to test
     */
    private static class TestDataAccessWithComplex extends TestDataAccess {

        private final FeatureType complexType;

        public TestDataAccessWithComplex(
                Name baseName, FeatureType complexType, DataStore delegate) {
            super(baseName, delegate);
            this.complexType = complexType;
        }

        @Override
        public List<Name> getNames() throws IOException {
            return Arrays.asList(name, complexType.getName());
        }

        @Override
        public FeatureType getSchema(Name name) throws IOException {
            if (name.equals(complexType.getName())) {
                return complexType;
            } else {
                return super.getSchema(name);
            }
        }
    }

    /**
     * Builds a complex feature type by decorating a simple one with some extras, code borrowed by
     * GeoServer OpenSearch for EO
     */
    FeatureType buildComplexTypeFromSimple(SimpleFeatureType base) {
        TypeBuilder typeBuilder = new TypeBuilder(CommonFactoryFinder.getFeatureTypeFactory(null));
        String nsURI = "http://www.geotools.org/test";

        // map the source attributes
        AttributeTypeBuilder ab = new AttributeTypeBuilder();
        for (AttributeDescriptor ad : base.getAttributeDescriptors()) {
            String name = ad.getLocalName();
            String namespaceURI = nsURI;

            // map into output type
            ab.init(ad);
            ab.setMinOccurs(0);
            AttributeDescriptor mappedDescriptor;
            if (ad instanceof GeometryDescriptor) {
                GeometryType at = ab.buildGeometryType();
                ab.setCRS(((GeometryDescriptor) ad).getCoordinateReferenceSystem());
                mappedDescriptor = ab.buildDescriptor(new NameImpl(namespaceURI, name), at);
            } else {
                AttributeType at = ab.buildType();
                mappedDescriptor = ab.buildDescriptor(new NameImpl(namespaceURI, name), at);
            }

            typeBuilder.add(mappedDescriptor);
        }
        // adding the metadata property
        AttributeDescriptor metadataDescriptor =
                buildSimpleDescriptor(new NameImpl("metadata"), String.class);
        typeBuilder.add(metadataDescriptor);

        // adding the quicklook property
        AttributeDescriptor quicklookDescriptor =
                buildSimpleDescriptor(new NameImpl("quicklook"), byte[].class);
        typeBuilder.add(quicklookDescriptor);

        // map OGC links
        AttributeDescriptor linksDescriptor =
                buildFeatureListDescriptor(new NameImpl("ogcLinks"), base);
        typeBuilder.add(linksDescriptor);

        typeBuilder.setName("product");
        typeBuilder.setNamespaceURI(nsURI);
        return typeBuilder.feature();
    }

    private AttributeDescriptor buildSimpleDescriptor(Name name, Class binding) {
        AttributeTypeBuilder ab = new AttributeTypeBuilder();
        ab.name(name.getLocalPart()).namespaceURI(name.getNamespaceURI());
        ab.setBinding(binding);
        AttributeDescriptor descriptor = ab.buildDescriptor(name, ab.buildType());
        return descriptor;
    }

    private AttributeDescriptor buildFeatureListDescriptor(Name name, SimpleFeatureType schema) {
        return buildFeatureDescriptor(name, schema, 0, Integer.MAX_VALUE);
    }

    private AttributeDescriptor buildFeatureDescriptor(
            Name name, SimpleFeatureType schema, int minOccurs, int maxOccurs) {
        AttributeTypeBuilder ab = new AttributeTypeBuilder();
        ab.name(name.getLocalPart()).namespaceURI(name.getNamespaceURI());
        ab.setMinOccurs(minOccurs);
        ab.setMaxOccurs(maxOccurs);
        AttributeDescriptor descriptor = ab.buildDescriptor(name, schema);
        return descriptor;
    }
}
